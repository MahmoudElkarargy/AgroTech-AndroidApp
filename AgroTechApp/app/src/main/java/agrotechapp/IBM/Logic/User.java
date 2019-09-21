package agrotechapp.IBM.Logic;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
		public static String authenticateUser(String email, String password) throws JSONException {
			// Set up JSON Object to send as parameter
			JSONObject data = new JSONObject();
			data.put("email", email);
			data.put("password", password);


			//Set up Post Request
			OkHttpClient client = new OkHttpClient();
			MediaType JSON
					= MediaType.parse("application/json; charset=utf-8");

			RequestBody body = RequestBody.create(JSON,data.toString());
					Request request = new Request.Builder()
					.url(urlAuth)
					.post(body)
					.build();
			try {
				Response response = client.newCall(request).execute();
				String resultString = response.body().string();
				System.out.println(resultString);
				if(resultString.equals("[[],[]]")){
	            	System.out.print("Wrong Email or Password");
	            	return "False";
	            }else {
	            	parseUser(resultString);
	            }

			} catch (IOException e) {
				e.printStackTrace();
			}

			return "True";
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
}
