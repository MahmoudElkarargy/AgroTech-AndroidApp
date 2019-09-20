package agrotechapp.IBM;


import androidx.appcompat.app.AppCompatActivity;
import agrotechapp.IBM.Logic.*;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.json.JSONException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Resources res;
    //Layout components
    Button signInBtn;
    TextView signUpTextView;
    TextView forgotPassTextView;
    EditText emailEditText;
    EditText passwordEditText;
    //variables
    String email;
    String password;
    private boolean authenticated = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        res = getResources();
        signInBtn = (Button) findViewById(R.id.signInBtn);
        signUpTextView = (TextView) findViewById(R.id.signUpTextView);
        forgotPassTextView = (TextView) findViewById(R.id.forgotPassTextView);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailEditText.getText().toString();
                password = passwordEditText.getText().toString();
                //code to authenticate login
//                try {
//                    authenticated = User.Server.authenticateUser(email, password);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                if(authenticated){
                    User user = User.getInstance();
                    Intent goToDashboard = new Intent(getApplicationContext(), Dashboard.class);
                    startActivity(goToDashboard);
                }
            }
        });

        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToSignUp = new Intent(getApplicationContext(), SignUp.class);
                startActivity(goToSignUp);
            }
        });

        forgotPassTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //code to reset password
            }
        });
    }
}
