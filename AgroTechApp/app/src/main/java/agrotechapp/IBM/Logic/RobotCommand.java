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

public class RobotCommand {

    private static String urlComm = "https://nodered-ibmdigitalnationcompetition.eu-gb.mybluemix.net/robotCommands";


    public static Boolean sendCommand(String mode, String dir, String speed) throws JSONException {
        // Set up JSON Object to send as parameter
        JSONObject data = new JSONObject();
        data.put("mode", mode);
        data.put("dir", dir);
        data.put("speed", speed);

        //Set up Post Request
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpPost request = new HttpPost(urlComm);
            StringEntity params = new StringEntity(data.toString());
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            HttpResponse result = httpClient.execute(request);
            String resultString = EntityUtils.toString(result.getEntity(), "UTF-8");


            if(result.getStatusLine().getStatusCode() == 200){
                System.out.println(resultString + " SENT CORRECTLY");
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return true;
    }

}

