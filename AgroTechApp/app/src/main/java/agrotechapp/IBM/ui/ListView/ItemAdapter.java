package agrotechapp.IBM.ui.ListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.zip.Inflater;

import agrotechapp.IBM.R;

public class ItemAdapter extends BaseAdapter {
    String[] IDs;
    String[] temps;
    String[] pHs;
    String[] soils;
    String[] dates;

    LayoutInflater mInflator;

    public ItemAdapter(Context c, String[] ids, String[] temps,String[] pH,String[] soil, String[] date){
        this.IDs = ids;
        this.temps = temps;
        this.pHs = pH;
        this.soils = soil;
        this.dates = date;
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
        View v = mInflator.inflate(R.layout.my_listview_details,null);
        TextView idTextView = (TextView) v.findViewById(R.id.IDsList);
        TextView pHTextView = (TextView) v.findViewById(R.id.pHList);
        TextView tempTextView = (TextView) v.findViewById(R.id.tempList);
        TextView soilTextView = (TextView) v.findViewById(R.id.soilList);
        TextView dateTextView = (TextView) v.findViewById(R.id.dateList);

        String id = IDs[i];
        String temp = temps[i];
        String pH = pHs[i];
        String soil = soils[i];
        String date = dates[i];

        idTextView.setText(id);
        pHTextView.setText(pH);
        tempTextView.setText(temp);
        soilTextView.setText(soil);
        dateTextView.setText(date);

        return v;
    }
}
