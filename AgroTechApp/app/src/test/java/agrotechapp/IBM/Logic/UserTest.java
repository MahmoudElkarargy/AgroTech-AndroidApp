package agrotechapp.IBM.Logic;

import org.json.JSONException;
import org.junit.Test;
import java.util.ArrayList;


public class UserTest {

    @Test
    public void main() throws JSONException {
        //Test Log-In Authentication and data loading
        User.Server.authenticateUser("rowan.hisham133@gmail.com", "1234");
		User user = User.getInstance();
		System.out.println(user.getFirstName() + " " + user.getLastName());
		System.out.println(user.getEmail());
		for(ArrayList<SensorData> s : user.getSensorsData()) {
			for(SensorData entry : s) {
				System.out.println(entry.getDeviceID() + " " + entry.getTime() + " " + entry.getTemperature());
			}
		}
//		//Test Robot command
        RobotCommand.sendCommand("manual","F","200");
    }
}