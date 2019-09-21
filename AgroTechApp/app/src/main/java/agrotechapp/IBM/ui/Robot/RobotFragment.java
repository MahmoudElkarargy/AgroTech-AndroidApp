package agrotechapp.IBM.ui.Robot;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import agrotechapp.IBM.Logic.*;

import agrotechapp.IBM.MainActivity;
import agrotechapp.IBM.R;
import agrotechapp.IBM.SignUp;

import org.json.JSONException;


public class RobotFragment extends Fragment {

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

        forwardImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getContext(), SignUp.class);
                startActivity(startIntent);
                //send forward command
//                try {
//                    RobotCommand.sendCommand("manual","F","200");
//                }
//                catch(JSONException e){
//
//                }
            }
        });

        rightImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send right command
            }
        });

        leftImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send left command
            }
        });

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send back command
            }
        });
        return root;
    }
}