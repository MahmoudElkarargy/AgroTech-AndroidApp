package agrotechapp.IBM.ui.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
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
import com.anychart.charts.Pie;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import agrotechapp.IBM.Dashboard;
import agrotechapp.IBM.Logic.SensorData;
import agrotechapp.IBM.Logic.User;
import agrotechapp.IBM.R;

public class listView extends AppCompatActivity {

    ListView myListView;
    ImageView backImageView;
    TextView backTextView;

    String[] IDs ;
    String[] temps;
    String[] pHs;
    String[] soil;
    String[] dates;

    private Date dateTimes;
    private String[] time;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        User user = User.getInstance();
        ArrayList< ArrayList<SensorData>> sensorsData = new ArrayList<ArrayList<SensorData>>();
        sensorsData = user.getSensorsData();


        Resources res = getResources();
        myListView = (ListView) findViewById(R.id.myList);
        IDs = res.getStringArray(R.array.IDs);
        temps = res.getStringArray(R.array.temps);
        pHs = res.getStringArray(R.array.pH);
        soil = res.getStringArray(R.array.soil);
        dates = res.getStringArray(R.array.date);

        //parsing sensors data;
        parseSensorData(sensorsData);
        drawGraph();



        ItemAdapter itemAdapter = new ItemAdapter(this,IDs,temps,pHs,soil,dates);
        myListView.setAdapter(itemAdapter);


        backImageView = findViewById(R.id.backImageView);
        backTextView = findViewById(R.id.backTextView);

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goBackIntent = new Intent(getApplicationContext(), Dashboard.class);
                startActivity(goBackIntent);
            }
        });

        backTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goBackIntent = new Intent(getApplicationContext(), Dashboard.class);
                startActivity(goBackIntent);
            }
        });


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

    private void parseSensorData(ArrayList<ArrayList<SensorData>> sensorsData){
        int num = 0;
        for(ArrayList<SensorData> s : sensorsData) {
            for(SensorData entry : s) {
                System.out.println(entry.getDeviceID() + " " + entry.getTime() + " " + entry.getTemperature());
                if(entry.getDeviceID()<10)
                    IDs[num] = "0" + Integer.toString(entry.getDeviceID());
                else IDs[num] = Integer.toString(entry.getDeviceID());
                if(entry.getTemperature()<10)
                    temps[num] = "0" + Double.toString(entry.getTemperature());
                else temps[num] = Double.toString(entry.getTemperature());
                if(entry.getpH()<10)
                    pHs[num] = "0" + Double.toString(entry.getpH());
                else pHs[num] = Double.toString(entry.getpH());
                if(entry.getSoilMoisture()<10)
                    soil[num] = "0" + Double.toString(entry.getSoilMoisture());
                else soil[num] = Double.toString(entry.getSoilMoisture());
                dates[num] = entry.getTime();
                num++;
            }
        }

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
}

