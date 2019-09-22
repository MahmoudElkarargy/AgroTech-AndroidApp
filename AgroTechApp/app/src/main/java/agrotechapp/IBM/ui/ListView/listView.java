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


import java.util.ArrayList;

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
                IDs[num] = Integer.toString(entry.getDeviceID());
                temps[num] = Double.toString(entry.getTemperature());
                pHs[num] = Double.toString(entry.getpH());
                soil[num] = Double.toString(entry.getSoilMoisture());
                dates[num] = entry.getTime();
                num++;
            }
        }
    }
}

