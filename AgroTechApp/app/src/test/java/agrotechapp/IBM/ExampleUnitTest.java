package agrotechapp.IBM;

import com.ibm.wiotp.sdk.app.ApplicationClient;
import com.ibm.wiotp.sdk.codecs.JsonCodec;
import com.ibm.wiotp.sdk.device.DeviceClient;
import com.ibm.wiotp.sdk.device.config.DeviceConfig;

import org.junit.Test;

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

//        DeviceConfig config = DeviceConfig.generateFromEnv();
//        DeviceClient  client = new DeviceClient(config);
//        client.registerCodec(new JsonCodec());
//        client.connect();

        ApplicationClient appClient = new ApplicationClient();
        appClient.registerCodec(new JsonCodec());
        appClient.connect();

    }
}