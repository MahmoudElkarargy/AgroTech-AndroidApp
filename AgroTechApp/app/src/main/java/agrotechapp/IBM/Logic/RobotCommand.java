package agrotechapp.IBM.Logic;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RobotCommand {

    private static String urlComm = "https://nodered-ibmdigitalnationcompetition.eu-gb.mybluemix.net/robotCommands";


    public static String sendCommand(String mode, String dir, String speed) throws JSONException {
        // Set up JSON Object to send as parameter
        JSONObject data = new JSONObject();
        data.put("mode", mode);
        data.put("dir", dir);
        data.put("speed", speed);


        //Set up Post Request
        OkHttpClient client = new OkHttpClient();
        MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(JSON,data.toString());
        Request request = new Request.Builder()
                .url(urlComm)
                .post(body)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if(!response.isSuccessful()){
                return "False";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "True";
    }

}

