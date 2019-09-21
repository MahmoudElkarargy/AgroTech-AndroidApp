package agrotechapp.IBM.ui.ListView;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import agrotechapp.IBM.Dashboard;
import agrotechapp.IBM.R;

public class listView extends AppCompatActivity {

    ListView myListView;
    String[] IDs;
    String[] temps;
    String[] pHs;
    String[] soil;
    String[] dates;
    ImageView backImageView;
    TextView backTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        Resources res = getResources();
        myListView = (ListView) findViewById(R.id.myList);
        IDs = res.getStringArray(R.array.IDs);
        temps = res.getStringArray(R.array.temps);
        pHs = res.getStringArray(R.array.pH);
        soil = res.getStringArray(R.array.soil);
        dates = res.getStringArray(R.array.date);

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
}
