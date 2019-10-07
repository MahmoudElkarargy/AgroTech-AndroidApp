package agrotechapp.IBM.ui.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

import agrotechapp.IBM.MainActivity;
import agrotechapp.IBM.R;
import agrotechapp.IBM.ui.ListView.listView;

public class HomeFragment extends Fragment implements View.OnTouchListener{

    private HomeViewModel homeViewModel;
    View root;
    private static int fieldNumber=1;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);

        updateTime();

        ImageView fieldOne = (ImageView) root.findViewById (R.id.fieldOne);
        if (fieldOne != null) {
            fieldOne.setOnTouchListener ((View.OnTouchListener) this);
        }

//        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
//                textView.setText(s);
            }
        });

        Button viewDetails = (Button) root.findViewById(R.id.viewDetailsBtn);
        viewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bla = new Intent(getContext(),listView.class);
                startActivity(bla);
            }
        });
        return root;
    }

    public boolean onTouch (View v, MotionEvent ev) {
        boolean handledHere = false;
        final int action = ev.getAction();
        // (1)
        final int evX = (int) ev.getX();
        final int evY = (int) ev.getY();
        int nextImage = -1;			// resource id of the next image to display

        ImageView fieldone = (ImageView) v.findViewById (R.id.fieldOne);
        TextView fieldNumTextView = (TextView)root.findViewById(R.id.fieldNumTextView);
        if (fieldone == null) return false;

        Integer tagNum = (Integer) fieldone.getTag ();
        int currentResource = (tagNum == null) ? R.drawable.fieldone : tagNum.intValue ();

        switch (action) {

            case MotionEvent.ACTION_DOWN :
//                Log.d("myTag","ActionUp");
                // On the UP, we do the click action.
                // The hidden image (image_areas) has three different hotspots on it.
                // The colors are red, blue, and yellow.
                // Use image_areas to determine which region the user touched.
                int touchColor = getHotspotColor (R.id.invisibleMap, evX, evY);

                // Compare the touchColor to the expected values. Switch to a different image, depending on what color was touched.
                // Note that we use a Color Tool object to test whether the observed color is close enough to the real color to
                // count as a match. We do this because colors on the screen do not match the map exactly because of scaling and
                // varying pixel density.
                ColorTool ct = new ColorTool ();
                int tolerance = 25;
//                nextImage = R.drawable.fieldone;
                if (ct.closeMatch (Color.BLUE, touchColor, tolerance)) {
                    nextImage = R.drawable.fieldtwo;
                    fieldNumTextView.setText("FIELD 2");
                    fieldNumber =2;
//                    Log.d("myTag","FieldTwooo Clicked");
                }
                else if(ct.closeMatch (Color.GREEN, touchColor, tolerance)){
//
                    nextImage = R.drawable.fieldone;
                    fieldNumTextView.setText("FIELD 1");
                    fieldNumber =1;
//                    Log.d("myTag","FieldOne Clicked");
                }

                // If the next image is the same as the last image, go back to the default.
                // toast ("Current image: " + currentResource + " next: " + nextImage);
//                if (currentResource == nextImage) {
//                    nextImage = R.drawable.fieldone;
//                }
                handledHere = true;
                break;

            default:
                handledHere = false;
        } // end switch

        if (handledHere) {

            if (nextImage > 0) {
                fieldone.setImageResource (nextImage);
                fieldone.setTag (nextImage);
            }
        }
        return handledHere;
    }


    public int getHotspotColor (int hotspotId, int x, int y) {
        ImageView img = (ImageView) root.findViewById (hotspotId);
        if (img == null) {
            return 0;
        } else {
            img.setDrawingCacheEnabled(true);
            Bitmap hotspots = Bitmap.createBitmap(img.getDrawingCache());
            if (hotspots == null) {
                return 0;
            } else {
                img.setDrawingCacheEnabled(false);
                return hotspots.getPixel(x, y);
            }
        }
    }

    public int getFieldNumber() {
        return fieldNumber;
    }

    public void updateTime(){
        TextView timeTextView = (TextView) root.findViewById(R.id.timeTextView);
        ImageView timeImageView = (ImageView) root.findViewById(R.id.timeImageView);
        Calendar c1 = GregorianCalendar.getInstance();
        int hour = c1.get(Calendar.HOUR_OF_DAY);
        int minutes = c1.get(Calendar.MINUTE);
        if(hour > 6 && hour < 18){
            timeImageView.setImageResource(R.drawable.time);
            timeTextView.setTextColor(getResources().getColor(R.color.colorDarkGrey));
        }else{
            timeImageView.setImageResource(R.drawable.time_night);
            timeTextView.setTextColor(getResources().getColor(R.color.colorWhite));
        }
        timeTextView.setText(String.valueOf(hour) + ":" + String.valueOf(minutes));
    }

}