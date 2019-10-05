package agrotechapp.IBM;

import com.google.gson.JsonObject;
import com.ibm.wiotp.sdk.codecs.JsonCodec;
import com.ibm.wiotp.sdk.device.DeviceClient;
import com.ibm.wiotp.sdk.device.config.DeviceConfig;
import com.ibm.wiotp.sdk.device.config.DeviceConfigAuth;
import com.ibm.wiotp.sdk.device.config.DeviceConfigIdentity;
import com.ibm.wiotp.sdk.device.config.DeviceConfigOptions;

import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
//        assertEquals(4, 2 + 2);

//        System.setProperty("WIOTP_IDENTITY_ORGID", "value");
//        System.out.println(System.getenv("WIOTP_IDENTITY_ORGID"));
//        System.out.println(System.getenv("WIOTP_IDENTITY_TYPEID"));
//        System.out.println(System.getenv("WIOTP_IDENTITY_DEVICEID"));
//        System.out.println(System.getenv("WIOTP_AUTH_TOKEN"));

//        DeviceClient deviceClient = new DeviceClient();
//        deviceClient.registerCodec(new JsonCodec());
//        deviceClient.connect();
//        JsonObject data = new JsonObject();
//        data.addProperty("distance", 10);
//        deviceClient.publishEvent("myEvent", data);
//        deviceClient.disconnect();

//        ApplicationClient appClient = new ApplicationClient();
//        appClient.registerCodec(new JsonCodec());
//        appClient.connect();


        DeviceConfigIdentity identity = new DeviceConfigIdentity("bo5aph","typedevice","pidevice");
        DeviceConfigAuth auth = new DeviceConfigAuth("123456789");
        DeviceConfigOptions options = new DeviceConfigOptions();
        options.mqtt.port = 8883;
        options.mqtt.keepAlive = 1;
        DeviceConfig config = new DeviceConfig(identity, auth,options);
        DeviceClient deviceClient2 = new DeviceClient(config);
        deviceClient2.registerCodec(new JsonCodec());
        deviceClient2.connect();
        JsonObject data = new JsonObject();
        data.addProperty("mode", "manual");
        data.addProperty("dir", "F");
        data.addProperty("speed", "100");
        deviceClient2.publishEvent("data", data);

        deviceClient2.disconnect();
    }
}