package agrotechapp.IBM;

import com.google.gson.JsonObject;
import com.ibm.wiotp.sdk.app.ApplicationClient;
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


        DeviceConfigIdentity identity = new DeviceConfigIdentity("bo5aph","PiGateway","myPiGateway");
        DeviceConfigAuth auth = new DeviceConfigAuth("12345678");
        DeviceConfigOptions options = new DeviceConfigOptions();
        DeviceConfig config = new DeviceConfig(identity, auth,options);
        DeviceClient deviceClient2 = new DeviceClient(config);
        deviceClient2.registerCodec(new JsonCodec());
        deviceClient2.connect();



    }
}