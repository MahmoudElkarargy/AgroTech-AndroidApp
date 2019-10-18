package agrotechapp.IBM.Logic;

import com.google.gson.JsonObject;
import com.ibm.wiotp.sdk.codecs.JsonCodec;
import com.ibm.wiotp.sdk.device.DeviceClient;
import com.ibm.wiotp.sdk.device.config.DeviceConfig;
import com.ibm.wiotp.sdk.device.config.DeviceConfigAuth;
import com.ibm.wiotp.sdk.device.config.DeviceConfigIdentity;
import com.ibm.wiotp.sdk.device.config.DeviceConfigOptions;
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
    private DeviceClient deviceClient2;

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

    public void initServer() throws Exception{
        DeviceConfigIdentity identity = new DeviceConfigIdentity("bo5aph","typedevice","pidevice");
        DeviceConfigAuth auth = new DeviceConfigAuth("123456789");
        DeviceConfigOptions options = new DeviceConfigOptions();
        options.mqtt.sessionExpiry = 0;
        options.mqtt.cleanStart = true;
        DeviceConfig config = new DeviceConfig(identity, auth,options);
        deviceClient2 = new DeviceClient(config);
        deviceClient2.registerCodec(new JsonCodec());
        deviceClient2.connect();
    }

    public void send(String mode, String command, String speed) throws Exception{
        JsonObject data = new JsonObject();
        data.addProperty("mode", mode);
        data.addProperty("dir", command);
        data.addProperty("speed", speed);
        deviceClient2.publishEvent("data", data);
    }
}

