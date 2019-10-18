package agrotechapp.IBM.Logic;

public class SensorData {

	private int deviceID;
	private double temperature;
	private double pH;
	private double soilMoisture;
	private String time;
	
	SensorData(int deviceID, double temperature, double pH, double soilMoisture, String time){
		this.deviceID = deviceID;
		this.temperature = temperature;
		this.pH = pH;
		this.soilMoisture = soilMoisture;
		this.time = time;
	}

	public int getDeviceID() {
		return deviceID;
	}
	public double getTemperature() {
		return temperature;
	}
	public double getpH() {
		return pH;
	}
	public double getSoilMoisture() {
		return soilMoisture;
	}
	public String getTime() {
		return time;
	}
	
}
