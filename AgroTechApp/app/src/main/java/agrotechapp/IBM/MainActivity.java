package agrotechapp.IBM;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import agrotechapp.IBM.Logic.*;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import org.json.JSONException;

public class MainActivity extends AppCompatActivity {

    Resources res;
    //Layout components
    Button signInBtn;
    TextView signUpTextView;
    TextView forgotPassTextView;
    TextView userValidationTextView;
    EditText emailEditText;
    EditText passwordEditText;
    //variables
    String email;
    String password;
    private String authenticated = "False";
    User user;

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
        userValidationTextView = (TextView) findViewById(R.id.userValidationTextView);

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                email = emailEditText.getText().toString();
                password = passwordEditText.getText().toString();
                //code to authenticate login
                new AuthenticateLogIn().execute(email,password);
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

    private class AuthenticateLogIn extends AsyncTask<String, Void, String> {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... strings) {
            signInBtn.setBackgroundColor(res.getColor(R.color.colorDarkGrey));
            try {
                authenticated = User.Server.authenticateUser(strings[0], strings[1]);
                if(authenticated == "True"){
                    user = User.getInstance();
                    return authenticated;
                }
//                return  User.Server.authenticateUser(strings[0],strings[1]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "False";
        }

        @Override
        protected void onPostExecute(String result) {
            signInBtn.setBackgroundColor(res.getColor(R.color.colorPrimary));
            if(result.equals("True")) {
                Intent goToDashboard = new Intent(getApplicationContext(), Dashboard.class);
                startActivity(goToDashboard);
                userValidationTextView.setText("");
            }else{
                userValidationTextView.setText("Invalid Username or Password");
            }
        }
    }
}
