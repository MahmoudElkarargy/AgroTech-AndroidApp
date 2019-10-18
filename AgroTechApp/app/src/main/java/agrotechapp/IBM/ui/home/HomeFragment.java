package agrotechapp.IBM.ui.home;

import android.app.Activity;
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

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import agrotechapp.IBM.Logic.SendMailTask;
import agrotechapp.IBM.Logic.User;
import agrotechapp.IBM.R;
import agrotechapp.IBM.ui.ListView.listView;

public class HomeFragment extends Fragment implements View.OnTouchListener{

    static private HomeViewModel homeViewModel;
    static private View root;
    static private TextView tempTextView, pHTextView, soilMoistureTextView;
    static private listView listview;
    static private User user = User.getInstance();
    static private TextView fieldNumTextView;
    private boolean isThereWarning = false;
    private static int fieldNumber=1;
    static private int olddatanumbers, newNumbers;
    static private Activity activity;

    private String fromEmail = "agrotech.customers@gmail.com";
    private String fromPassword = "AgroTech2019";

    private String toEmail = user.getEmail();
    private List<String> toEmailList = Arrays.asList(toEmail
            .split("\\s*,\\s*"));
    private String emailSubject = "AgroTech";
    private String emailBody = "WARNING! check your field readings!";
    private boolean sendEmail = false;
    private static boolean emailIsSent = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);
        activity = this.getActivity();
        updateTime();
        tempTextView = (TextView) root.findViewById(R.id.tempTextView);
        soilMoistureTextView = (TextView) root.findViewById(R.id.soilMoistureTextView);
        pHTextView = (TextView) root.findViewById(R.id.pHTextView);
        fieldNumTextView = (TextView)root.findViewById(R.id.fieldNumTextView);
        fieldNumTextView.setText("FIELD 1");
        listview = new listView();

        olddatanumbers = listview.getNumOfSensors(user.getSensorsData());
//        Log.d("myTag","number of data: "+olddatanumbers);
        updateDashboard();

        ImageView fieldOne = (ImageView) root.findViewById (R.id.fieldOne);
        if (fieldOne != null) {
            fieldOne.setOnTouchListener ((View.OnTouchListener) this);
        }

        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
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
        if (fieldone == null) return false;

        Integer tagNum = (Integer) fieldone.getTag ();
        int currentResource = (tagNum == null) ? R.drawable.fieldone : tagNum.intValue ();

        switch (action) {

            case MotionEvent.ACTION_DOWN :
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
                    isThereWarning = false;
                    fieldNumber =2;
                    listview.parseSensorData(listview.getSensorData());
                    updateDashboard();
                }
                else if(ct.closeMatch (Color.GREEN, touchColor, tolerance)){
//
                    nextImage = R.drawable.fieldone;
                    fieldNumTextView.setText("FIELD 1");
                    isThereWarning = false;
                    fieldNumber =1;
                    listview.parseSensorData(listview.getSensorData());
                    updateDashboard();
                }

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
    public void setFieldNumber(int fieldNumber){
        this.fieldNumber = fieldNumber;
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
        if(minutes < 9){
            String mins = "0" + String.valueOf(minutes);
            timeTextView.setText(String.valueOf(hour) + ":" + mins);
        }else{
            timeTextView.setText(String.valueOf(hour) + ":" + String.valueOf(minutes));
        }
    }

    public void updateDashboard() {
        user = User.getInstance();
        listview.parseSensorData(user.getSensorsData());


        //Email checking..
        newNumbers = listview.getNumOfSensors(user.getSensorsData());
//        Log.d("myTag", "Now nb is: "+newNumbers);
        if(activity==null) {
//            Log.d("myTag", "WHYYYYYY????");
        }
        if(!emailIsSent) {
            if (Double.valueOf(listview.getLastTemp()) > user.getTempMax() || Double.valueOf(listview.getLastTemp()) < user.getTempMin()) {
                sendEmail = true;
            }

            if (Double.valueOf(listview.getLastSoil()) > user.getSoilMax() || Double.valueOf(listview.getLastSoil()) < user.getSoilMin()) {
                sendEmail = true;
            }

            if (Double.valueOf(listview.getLastpH()) > user.getpHMax() || Double.valueOf(listview.getLastpH()) < user.getpHMin()) {
                sendEmail = true;
            }
            if (sendEmail) {
                emailIsSent = true;
//                Log.d("myTag", "EMAIL SENT!!");
                sendEmail = false;
//                if(activity==null) {
////                    Activity activity = getActivity();
//                    Log.d("myTag", "NULLLLLLLLLLLL");
////                    new SendMailTask(activity).execute(fromEmail, fromPassword, toEmailList, emailSubject, emailBody);
////                    Log.d("myTag", "it was null");
//                }else {
//                    Log.d("myTag", "EMAIL SENT!!2222");
                    new SendMailTask(activity).execute(fromEmail, fromPassword, toEmailList, emailSubject, emailBody);
//                    Log.d("myTag", "Excuted");
//                }
            }
        }
        else if(olddatanumbers != newNumbers){
//            Log.d("myTag", "added new entiry");
            olddatanumbers = newNumbers;
            emailIsSent = false;
        }
//        else {
//            Log.d("myTag", "NOOOOOO Email Sent!!!");
//        }

        if (Double.valueOf(listview.getLastTemp()) > user.getTempMax() || Double.valueOf(listview.getLastTemp()) < user.getTempMin()) {
            tempTextView.setTextColor(root.getResources().getColor(R.color.colorRed));
            isThereWarning = true;
        } else {
            tempTextView.setTextColor(root.getResources().getColor(R.color.colorPrimaryDark));
        }

            if (Double.valueOf(listview.getLastSoil()) > user.getSoilMax() || Double.valueOf(listview.getLastSoil()) < user.getSoilMin()) {
                soilMoistureTextView.setTextColor(root.getResources().getColor(R.color.colorRed));
                isThereWarning = true;
            } else {
                soilMoistureTextView.setTextColor(root.getResources().getColor(R.color.colorPrimaryDark));
            }

            if (Double.valueOf(listview.getLastpH()) > user.getpHMax() || Double.valueOf(listview.getLastpH()) < user.getpHMin()) {
                pHTextView.setTextColor(root.getResources().getColor(R.color.colorRed));
                isThereWarning = true;
            } else {
                pHTextView.setTextColor(root.getResources().getColor(R.color.colorPrimaryDark));
            }

            if (isThereWarning) {
                fieldNumTextView.setTextColor(root.getResources().getColor(R.color.colorRed));
                if (fieldNumTextView.getText().length() < 8)
                    fieldNumTextView.setText(fieldNumTextView.getText() + " [WARNING]");
            } else {
                fieldNumTextView.setTextColor(root.getResources().getColor(R.color.colorPrimary));
                fieldNumTextView.setText(fieldNumTextView.getText().subSequence(0,7));
            }

        tempTextView.setText(listview.getLastTemp());
        soilMoistureTextView.setText(listview.getLastSoil());
        pHTextView.setText(listview.getLastpH());
    }
}