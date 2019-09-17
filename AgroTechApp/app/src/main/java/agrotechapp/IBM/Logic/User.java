package agrotechapp.IBM.Logic;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class User {
	
	private static User instance;
	public static User getInstance() {
		return instance;
	}
	
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private ArrayList< ArrayList<SensorData>> sensorsData;
	
	private User(String firstName, String lastName, String email, ArrayList< ArrayList<SensorData>> sensorsData ) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.sensorsData = sensorsData;
	}



	public String getFirstName() {
		return firstName;
	}



	public String getLastName() {
		return lastName;
	}



	public String getEmail() {
		return email;
	}



	public String getPassword() {
		return password;
	}



	public ArrayList<ArrayList<SensorData>> getSensorsData() {
		return sensorsData;
	}
	
	
	public static class Server {	
		
		private static String urlAuth = "https://nodered-ibmdigitalnationcompetition.eu-gb.mybluemix.net/auth";
		
		@RequiresApi(api = Build.VERSION_CODES.KITKAT)
		public static Boolean authenticateUser(String email, String password) throws JSONException {
			// Set up JSON Object to send as parameter
			JSONObject data = new JSONObject();
			data.put("email", email);
			data.put("password", password);
		      
			//Set up Post Request
		      try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
	            HttpPost request = new HttpPost(urlAuth);
	            StringEntity params = new StringEntity(data.toString());
	            request.addHeader("content-type", "application/json");
	            request.setEntity(params);
	            HttpResponse result = httpClient.execute(request);
	            String resultString = EntityUtils.toString(result.getEntity(), "UTF-8");
	            
	            //Check if the response is an empty array then user was not found in the database
	            if(resultString.equals("[[],[]]")){
	            	System.out.print("Wrong Email or Password");
	            	return false;
	            }else {
	            	parseUser(resultString);
	            }
	            
		      } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return true;
		}
		
		private static void parseUser(String data) throws JSONException {
			//split data received into a string array to parse it into a JSON Object
			String[] input = StringUtils.substringsBetween(data , "[{", "}]"); 
			
			input[0] = '{' + input[0] + '}';
			JSONObject userJS = new JSONObject(input[0]);
			
			//Parse Sensors History Log into a list consisting of lists of each device's separately 
			ArrayList< ArrayList<SensorData>> sensorsData = new ArrayList<ArrayList<SensorData>>() ;
			
			for(int i=1; i<input.length; i++) {
				input[i] = "{" + input[i] + "}";
				String[] deviceData = StringUtils.substringsBetween(input[i] , "{", "}");
				
				ArrayList<SensorData> deviceHistoryList = new ArrayList<SensorData>();
				
				for( String s : deviceData) {
					s = "{" + s + "}";
					 JSONObject jsonObject = new JSONObject(s);
					 SensorData entry = new SensorData(jsonObject.getInt("deviceID"),
							 jsonObject.getDouble("temperature"),
							 jsonObject.getDouble("pH"),
							 jsonObject.getDouble("soilMoisture"),
							 jsonObject.getString("timestamp"));
					 deviceHistoryList.add(entry);
				 }
				sensorsData.add(deviceHistoryList);
			}
			
			// Create User Singleton 
			instance = new User(userJS.getString("firstname"),
					userJS.getString("lastname"),
					userJS.getString("email"),
					sensorsData);
		}
	}
	
	
	// Test
	@RequiresApi(api = Build.VERSION_CODES.KITKAT)
	public static void main(String[] args) throws JSONException {
		Server.authenticateUser("rowan.hisham133@gmail.com", "1234");
		User user = User.getInstance();
		System.out.println(user.getFirstName() + " " + user.getLastName());
		for(ArrayList<SensorData> s : user.sensorsData) {
			for(SensorData entry : s) {
				System.out.println(entry.getDeviceID() + " " + entry.getTime() + " " + entry.getTemperature());
			}
		}
	}
}
