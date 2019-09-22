package agrotechapp.IBM.ui.Robot;

import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        commandTextView = (TextView) root.findViewById(R.id.commandTextView);

        manualSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isManualMode = !isManualMode;
            }
        });

            forwardImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //send forward command
                    if(isManualMode){
                        new sendCommand().execute("manual","F", speedEditText.getText().toString());
                    }
                }
            });

            rightImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //send right command
                    if(isManualMode){
                        new sendCommand().execute("manual","R", speedEditText.getText().toString());
                    }
                }
            });

            leftImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //send left command
                    if(isManualMode){
                        new sendCommand().execute("manual","L", speedEditText.getText().toString());
                    }
                }
            });

            backImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //send back command
                    if(isManualMode){
                        new sendCommand().execute("manual","B", speedEditText.getText().toString());
                    }
                }
            });
        return root;
    }

    private class sendCommand extends AsyncTask<String, Void, String> {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... strings) {
            try {
                RobotCommand.sendCommand(strings[0],strings[1],strings[2]);
                command = strings[1] + strings[2];
                return "True";
            }catch (JSONException e) {
                e.printStackTrace();
            }
            return "False";
        }

        @Override
        protected void onPostExecute(String result) {
            if(result.equals("True")){
                if(isManualMode){
                    commandTextView.setText(command);
                }else{
                    commandTextView.setText("Auto");
                }
            }else{
                commandTextView.setText("Unable to send command");
            }
        }
    }
}