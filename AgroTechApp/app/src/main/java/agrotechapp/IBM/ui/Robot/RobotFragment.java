package agrotechapp.IBM.ui.Robot;

import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.gson.JsonObject;
import com.ibm.wiotp.sdk.codecs.JsonCodec;
import com.ibm.wiotp.sdk.device.DeviceClient;
import com.ibm.wiotp.sdk.device.config.DeviceConfig;
import com.ibm.wiotp.sdk.device.config.DeviceConfigAuth;
import com.ibm.wiotp.sdk.device.config.DeviceConfigIdentity;
import com.ibm.wiotp.sdk.device.config.DeviceConfigOptions;

import agrotechapp.IBM.Dashboard;
import agrotechapp.IBM.Logic.*;

import agrotechapp.IBM.MainActivity;
import agrotechapp.IBM.R;
import agrotechapp.IBM.SignUp;

import org.json.JSONException;


public class RobotFragment extends Fragment {

    boolean isManualMode = false;
    TextView commandTextView;
    String command;
    private RobotViewModel galleryViewModel;
    RobotCommand robot = new RobotCommand();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(RobotViewModel.class);
        View root = inflater.inflate(R.layout.fragment_robot, container, false);
        ImageView forwardImageView = (ImageView) root.findViewById(R.id.forwardImageView);
        ImageView rightImageView = (ImageView) root.findViewById(R.id.rightImageView);
        ImageView leftImageView = (ImageView) root.findViewById(R.id.leftImageView);
        ImageView backImageView = (ImageView) root.findViewById(R.id.backImageView);
        EditText speedEditText = (EditText) root.findViewById(R.id.speedEditText);
        Switch manualSwitch = (Switch) root.findViewById(R.id.manualSwitch);
        Button stopButton = (Button) root.findViewById(R.id.stopButton);
        commandTextView = (TextView) root.findViewById(R.id.commandTextView);

        manualSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isManualMode = !isManualMode;
            }
        });

        new initServer().execute();

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new sendCommand().execute("manual", "S", speedEditText.getText().toString());
            }
        });


        forwardImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(isManualMode){
                    if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                        //released
                        new sendCommand().execute("manual", "S", speedEditText.getText().toString());
                        forwardImageView.setImageResource(R.drawable.arrow);
                    } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        //pressed
                        new sendCommand().execute("manual", "F", speedEditText.getText().toString());
                        forwardImageView.setImageResource(R.drawable.arrow_green);
                    }
                }
                return true;
            }
        });


        rightImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(isManualMode){
                    if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                        //released
                        new sendCommand().execute("manual", "S", speedEditText.getText().toString());
                        rightImageView.setImageResource(R.drawable.arrow);
                    } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        //pressed
                        new sendCommand().execute("manual", "R", speedEditText.getText().toString());
                        rightImageView.setImageResource(R.drawable.arrow_green);
                    }
                }
                return true;
            }
        });


        leftImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(isManualMode){
                    if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                        //released
                        new sendCommand().execute("manual", "S", speedEditText.getText().toString());
                        leftImageView.setImageResource(R.drawable.arrow);
                    } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        //pressed
                        new sendCommand().execute("manual", "L", speedEditText.getText().toString());
                        leftImageView.setImageResource(R.drawable.arrow_green);
                    }
                }
                return true;
            }
        });

        backImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(isManualMode){
                    if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                        //released
                        new sendCommand().execute("manual", "S", speedEditText.getText().toString());
                        backImageView.setImageResource(R.drawable.arrow);
                    } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        //pressed
                        new sendCommand().execute("manual", "B", speedEditText.getText().toString());
                        backImageView.setImageResource(R.drawable.arrow_green);
                    }
                }
                return true;
            }
        });
        return root;
    }

    private class sendCommand extends AsyncTask<String, Void, String> {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... strings) {
            try {
                robot.send(strings[0], strings[1], strings[2]);
                command = strings[1] + strings[2];
                return "True";
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "False";
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("True")) {
                if (isManualMode) {
                    commandTextView.setText(command);
                } else {
                    commandTextView.setText("Auto");
                }
            } else {
                commandTextView.setText("Unable to send command");
            }
        }
    }

    private class initServer extends AsyncTask<String, Void, String> {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... strings) {
            try {
                commandTextView.setText("Connecting...");
                robot.initServer();
                return "True";
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "False";
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("True")) {
                commandTextView.setText("Connected");
            } else {
                commandTextView.setText("Unable to initialize server command");
            }
        }
    }

}