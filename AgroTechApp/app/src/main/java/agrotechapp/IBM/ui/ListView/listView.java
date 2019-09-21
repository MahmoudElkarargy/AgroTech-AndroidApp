package agrotechapp.IBM.ui.ListView;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import agrotechapp.IBM.R;

public class listView extends AppCompatActivity {

    ListView myListView;
    String[] IDs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        Resources res = getResources();
        myListView = (ListView) findViewById(R.id.myList);
        IDs = res.getStringArray(R.array.IDs);
        myListView.setAdapter(new ArrayAdapter<String>(this,R.layout.my_list_detail,IDs));

    }
}
