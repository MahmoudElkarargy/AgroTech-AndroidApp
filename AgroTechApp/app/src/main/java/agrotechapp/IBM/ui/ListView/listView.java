package agrotechapp.IBM.ui.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.view.Window;


import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Cartesian3d;
import com.anychart.charts.Pie;
import com.anychart.core.cartesian.series.Area3d;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.MarkerType;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
import com.anychart.graphics.vector.hatchfill.HatchFillType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import agrotechapp.IBM.Dashboard;
import agrotechapp.IBM.Logic.SendMailTask;
import agrotechapp.IBM.Logic.SensorData;
import agrotechapp.IBM.Logic.User;
import agrotechapp.IBM.R;
import agrotechapp.IBM.ui.home.HomeFragment;

public class listView extends AppCompatActivity {

    ListView myListView;
    ImageView backImageView;
    TextView backTextView;
    TextView fieldNumberTextView;
    TextView cropTypeTextView;
    TextView tempReadingTextView;
    TextView pHReadingTextView;
    TextView soilReadingTextView;
    String[] IDs ;
    String[] temps;
    String[] pHs;
    String[] soil;
    String[] dates;
    String tempString;
    int numberOfSelectedField=0;
    private Date dateTimes;
    private String[] time;
    int numberOfSensorData=0;
    HomeFragment homeFragment = new HomeFragment();

    String fromEmail = "agrotech.customers@gmail.com";
    String fromPassword = "AgroTech2019";
    User user = User.getInstance();
    String toEmail = user.getEmail();
    List<String> toEmailList = Arrays.asList(toEmail
            .split("\\s*,\\s*"));
    String emailSubject = "AgroTech";
    String emailBody = "WARNING! check your field readings!";
    boolean sendEmail = false;
    static ArrayList< ArrayList<SensorData>> sensorsData;
    public listView(){
        sensorsData = new ArrayList<ArrayList<SensorData>>();
        sensorsData = user.getSensorsData();
        //parsing sensors data;
        parseSensorData(sensorsData);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);




        Resources res = getResources();
        myListView = (ListView) findViewById(R.id.myList);


        drawGraph();



//        Log.d("myTag","nbof Selected: "+numberOfSelectedField);
        ItemAdapter itemAdapter = new ItemAdapter(this,IDs,temps,pHs,soil,dates,numberOfSelectedField);
        myListView.setAdapter(itemAdapter);


        backImageView = findViewById(R.id.backImageView);
        backTextView = findViewById(R.id.backTextView);
        fieldNumberTextView = findViewById(R.id.fieldNumTextView);
        cropTypeTextView = findViewById(R.id.cropTypeTextView);
        if(homeFragment.getFieldNumber()==1){
            fieldNumberTextView.setText("FIELD 1");
            cropTypeTextView.setText("WHEAT CROPS");
        }
        else{
            fieldNumberTextView.setText("FIELD 2");
            cropTypeTextView.setText("RICE CROPS");
        }
        backTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeFragment.setFieldNumber(1);
                parseSensorData(sensorsData);
                Intent goBackIntent = new Intent(getApplicationContext(), Dashboard.class);
                startActivity(goBackIntent);
            }
        });

        for(String st: temps){
            if(Double.valueOf(st) > user.getTempMax() && !sendEmail){
                sendEmail = true;
//                new SendMailTask(listView.this).execute(fromEmail, fromPassword, toEmailList, emailSubject, emailBody);
            }
        }

        for(String st: soil){
            if(Double.valueOf(st) > user.getSoilMax() && !sendEmail){
                sendEmail = true;
//                new SendMailTask(listView.this).execute(fromEmail, fromPassword, toEmailList, emailSubject, emailBody);
            }
        }

        for(String st: pHs){
            if(Double.valueOf(st) > user.getpHMax() && !sendEmail){
                sendEmail = true;
//                new SendMailTask(listView.this).execute(fromEmail, fromPassword, toEmailList, emailSubject, emailBody);
            }
        }

    }

    private class CustomDataEntry extends ValueDataEntry {

        CustomDataEntry(String x, Number value, Number value2, Number value3) {
            super(x, value);
            setValue("value2", value2);
            setValue("value3", value3);
        }
    }

    private int getNumOfSensors(ArrayList< ArrayList<SensorData>> sensorsData){
        int num = 0;
        for(ArrayList<SensorData> s : sensorsData) {
            for(SensorData entry : s) {
                num++;
            }
        }
        return num;
    }
    public ArrayList<ArrayList<SensorData>> getSensorData(){
        return sensorsData;
    }

    public void parseSensorData(ArrayList<ArrayList<SensorData>> sensorsData){
        int num = 0;
        numberOfSelectedField=0;
//        Log.d("myTag","I selected: "+homeFragment.getFieldNumber());
        for(ArrayList<SensorData> s : sensorsData) {
            for (SensorData entry : s) {
                if (entry.getDeviceID() == homeFragment.getFieldNumber()) {
                    numberOfSelectedField++;
                }
            }
        }
//        Log.d("myTag","nb: "+numberOfSelectedField);
        IDs = new String[numberOfSelectedField];
        temps = new String[numberOfSelectedField];
        pHs = new String[numberOfSelectedField];
        soil = new String[numberOfSelectedField];
        dates = new String[numberOfSelectedField];

        for(ArrayList<SensorData> s : sensorsData) {
            for(SensorData entry : s) {
                if (entry.getDeviceID() == homeFragment.getFieldNumber()) {
                    if (entry.getDeviceID() < 10)
                        IDs[num] = "0" + Integer.toString(entry.getDeviceID());
                    else IDs[num] = Integer.toString(entry.getDeviceID());
                    if (entry.getTemperature() < 10) {
                        temps[num] = "0" + Double.toString(entry.getTemperature());
                    }
                    else{
                        temps[num] = Double.toString(entry.getTemperature());
                    }
                    if (entry.getpH() < 10) {
                        pHs[num] = "0" + Double.toString(entry.getpH());
                    }
                    else{
                        pHs[num] = Double.toString(entry.getpH());
                    }
                    if (entry.getSoilMoisture() < 10) {
                        soil[num] = "0" + Double.toString(entry.getSoilMoisture());
                    }
                    else{
                        soil[num] = Double.toString(entry.getSoilMoisture());
                    }
                    dates[num] = entry.getTime();
                    num++;
                }
                else continue;
            }
        }
        for(int i=0; i<numberOfSelectedField-1; i++) {
            for (int j = 0; j < numberOfSelectedField - i - 1; j++) {
//                Log.d("myTag", "R: " + compare(dates[i], dates[i + 1]));
                if (compare(dates[j], dates[j + 1]) < 0) {
                    tempString = temps[j];
                    temps[j] = temps[j+1];
                    temps[j+1] = tempString;

                    tempString = dates[j];
                    dates[j] = dates[j+1];
                    dates[j+1] = tempString;

                    tempString = pHs[j];
                    pHs[j] = pHs[j+1];
                    pHs[j+1] = tempString;

                    tempString = soil[j];
                    soil[j] = soil[j+1];
                    soil[j+1] = tempString;

                }
            }
        }

//        for(int i=0; i<numberOfSelectedField; i++) {
//            Log.d("myTag", "R after: " + dates[i]);
//            Log.d("myTag", "S after: " + soil[i]);
//        }
    }

    public int compare(String arg0, String arg1) {
        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        int compareResult = 0;
        try {
            Date arg0Date = format.parse(arg0);
            Date arg1Date = format.parse(arg1);
            compareResult = arg0Date.compareTo(arg1Date);
        } catch (ParseException e) {
            e.printStackTrace();
            compareResult = arg0.compareTo(arg1);
        }
        return compareResult;
    }

    private void drawGraph(){
        AnyChartView anyChartView = findViewById(R.id.any_chart_view);
        Cartesian cartesian = AnyChart.line();

        cartesian.animation(true);

        cartesian.padding(5d, 10d, 5d, 10d);

        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                // TODO ystroke
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        cartesian.title("");

        cartesian.yAxis(0).title("");
        cartesian.xAxis(0).title("Time");
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);

        List<DataEntry> seriesData = new ArrayList<>();

        try{
            time = getTimes(dates);
        }
        catch (ParseException e){
            System.out.println("Error");
        }

        for(int i = 0; i<pHs.length;i++){
//            System.out.println(time[i]);
            seriesData.add(new CustomDataEntry(time[i], Float.parseFloat(temps[i]), Float.parseFloat(pHs[i]), Float.parseFloat(soil[i])));
        }

        Set set = Set.instantiate();
        set.data(seriesData);
        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");
        Mapping series2Mapping = set.mapAs("{ x: 'x', value: 'value2' }");
        Mapping series3Mapping = set.mapAs("{ x: 'x', value: 'value3' }");

        Line series1 = cartesian.line(series1Mapping);
        series1.name("Temperature");
        series1.tooltip().adjustFontSize();
        series1.hovered().markers().enabled(true);
        series1.hovered()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series1.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        Line series2 = cartesian.line(series2Mapping);
        series2.name("pH");
        series2.hovered().markers().enabled(true);
        series2.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series2.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        Line series3 = cartesian.line(series3Mapping);
        series3.name("Soil Moisture");
        series3.hovered().markers().enabled(true);
        series3.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series3.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        cartesian.legend().enabled(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 10d, 0d);

        anyChartView.setChart(cartesian);
    }



    private String[] getTimes(String[] dates) throws ParseException {
        //return times from dates and sort them
        String[] time = new String[dates.length];
        double[] timeDouble = new double[dates.length];
        for(int i =0; i<dates.length; i++){
            dateTimes = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dates[i]);
            time[i] = new SimpleDateFormat("H.mm").format(dateTimes);
            timeDouble[i] = Double.parseDouble(time[i]);
        }
        Arrays.sort(timeDouble);
        time = Arrays.stream(timeDouble).mapToObj(String::valueOf).toArray(String[]::new);
        return time;
    }

    public String getLastTemp(){
        return temps[0];
    }
    public String getLastpH(){
        return pHs[0];
    }
    public String getLastSoil(){
        return soil[0];
    }
}