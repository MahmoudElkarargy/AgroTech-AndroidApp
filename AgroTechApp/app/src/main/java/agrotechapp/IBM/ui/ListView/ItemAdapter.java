package agrotechapp.IBM.ui.ListView;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

import agrotechapp.IBM.Logic.SensorData;
import agrotechapp.IBM.Logic.User;
import agrotechapp.IBM.R;

public class ItemAdapter extends BaseAdapter {
    String[] IDs;
    String[] temps;
    String[] pHs;
    String[] soils;
    String[] dates;
//    int num;
    LayoutInflater mInflator;
    User user;

    public ItemAdapter(Context c, String[] ids, String[] temps,String[] pH,String[] soil, String[] date){
        this.IDs = ids;
        this.temps = temps;
        this.pHs = pH;
        this.soils = soil;
        this.dates = date;
//        this.num = num;
        mInflator = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return IDs.length;
    }

    @Override
    public Object getItem(int i) {
        return IDs[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        user = User.getInstance();
        View v = mInflator.inflate(R.layout.my_listview_details,null);
        TextView idTextView = (TextView) v.findViewById(R.id.IDsList);
        TextView pHTextView = (TextView) v.findViewById(R.id.pHList);
        TextView tempTextView = (TextView) v.findViewById(R.id.tempList);
        TextView soilTextView = (TextView) v.findViewById(R.id.soilList);
        TextView dateTextView = (TextView) v.findViewById(R.id.dateList);

        idTextView.setTextSize(16);
        pHTextView.setTextSize(16);
        tempTextView.setTextSize(16);
        soilTextView.setTextSize(16);
        dateTextView.setTextSize(16);
        dateTextView.setTextSize(16);

        String id = IDs[i];
        String temp = temps[i];
        String pH = pHs[i];
        String soil = soils[i];
        String date = dates[i];

        idTextView.setTextColor(v.getResources().getColor(R.color.colorRed));

        if(Double.valueOf(pH)>user.getpHMax() || Double.valueOf(pH)<user.getpHMin())
            pHTextView.setTextColor(v.getResources().getColor(R.color.colorRed));
        else
            pHTextView.setTextColor(v.getResources().getColor(R.color.colorPrimaryDark));


        if(Double.valueOf(temp)>user.getTempMax() || Double.valueOf(temp)<user.getTempMin())
            tempTextView.setTextColor(v.getResources().getColor(R.color.colorRed));
        else
            tempTextView.setTextColor(v.getResources().getColor(R.color.colorPrimaryDark));

        if(Double.valueOf(soil)>user.getSoilMax() || Double.valueOf(soil)<user.getSoilMin())
            soilTextView.setTextColor(v.getResources().getColor(R.color.colorRed));
        else
            soilTextView.setTextColor(v.getResources().getColor(R.color.colorPrimaryDark));

        dateTextView.setTextColor(v.getResources().getColor(R.color.colorPrimaryDark));



        idTextView.setText(id);
        pHTextView.setText(pH);
        tempTextView.setText(temp);
        soilTextView.setText(soil);
        dateTextView.setText(date);

        return v;
    }
}
