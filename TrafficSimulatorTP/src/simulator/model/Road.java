package simulator.model;

import org.json.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class Road extends SimulatedObject {
	
	//Los atributos tienen visibilidad protected para las clases que heredan de esta
	protected Junction srcJunc;
	protected Junction destJunc;
	protected int length;
	protected int maxSpeed;
	protected int speedLimit;
	protected int contLimit;
	protected Weather weather;
	protected int totalCont;
	protected List<Vehicle> vehicles;
	Comparator<Vehicle> cmp;
	
	Road(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) {
		super(id);//Creamos el comparador de vehículos para mantener la lista de vehículos ordenada
		cmp = new Comparator<Vehicle>() {
			public int compare(Vehicle v1, Vehicle v2) {
				if(v1.getLocation() > v2.getLocation()) return -1;
				else if(v1.getLocation() < v2.getLocation()) return 1;
				return 0;
			}
		};
		this.weather = weather;
		this.vehicles = new ArrayList<Vehicle>();
		if(srcJunc == null)
			throw new IllegalArgumentException("The srcJunc must be a Junction different from null");
		this.srcJunc = srcJunc;
		if(destJunc == null)
			throw new IllegalArgumentException("The destJunc must be a Junction different from null");
		this.destJunc = destJunc;
		this.srcJunc.addOutGoingRoad(this);
		this.destJunc.addIncommingRoad(this);
		if(length <= 0)
			throw new IllegalArgumentException("The length must be a positive value");
		this.length = length;
		if(maxSpeed <= 0)
			throw new IllegalArgumentException("The maxSpeed must be a positive value");
		this.maxSpeed = maxSpeed;
		this.speedLimit = maxSpeed;
		if(contLimit < 0)
			throw new IllegalArgumentException("The contLimit must be a non negative value");
		this.contLimit = contLimit;
		if(weather == null)
			throw new IllegalArgumentException("The weather must be a value different from null");
	}
	
	void enter(Vehicle v) {
		if(v.getLocation() != 0)
			throw new IllegalArgumentException("The vehicle's location must be 0");
		if(v.getSpeed() != 0)
			throw new IllegalArgumentException("The vehicle's speed must be 0");
		this.vehicles.add(v);
	}
	
	void exit(Vehicle v) {
		this.vehicles.remove(v);	//Al hacer esto la lista se mantiene ordenada
	}
	
	void setWeather(Weather w) {
		if(w == null)
			throw new IllegalArgumentException("The weather must be different from null");
		this.weather = w;
	}
	
	void addContamination(int c) {
		if(c < 0)
			throw new IllegalArgumentException("The contamination must be a non negative value");
		this.totalCont += c;
	}
	
	abstract void reduceTotalContamination();
	
	abstract void updateSpeedLimit();
	
	abstract int calculateVehicleSpeed(Vehicle v);
	
	void advance(int time) {
		this.reduceTotalContamination();
		this.updateSpeedLimit();
		//Recorremos la lista de vehículos, calculando la nueva velocidad de cada vehículo
		for(Vehicle v : vehicles) {
			v.setSpeed(this.calculateVehicleSpeed(v));
			v.advance(time);
		}
		vehicles.sort(cmp);
	}
	
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		jo.put("id", this._id);
		jo.put("speedlimit", this.speedLimit);
		jo.put("weather", this.weather.name());
		jo.put("co2", this.totalCont);
		JSONArray ja = new JSONArray();
		for(Vehicle v : vehicles) {
			ja.put(v.getId());
		}
		jo.put("vehicles", ja);
		return jo;
	}
	
	public int getLength() {
		return this.length;
	}

	public Junction getSrc() {
		return this.srcJunc;
	}
	
	public Junction getDest() {
		return this.destJunc;
	}
	
	public Weather getWeather() {
		return this.weather;
	}
	
	public int getContLimit() {
		return this.contLimit;
	}
	
	public int getMaxSpeed() {
		return this.maxSpeed;
	}
	
	public int getTotalCO2() {
		return this.totalCont;
	}
	
	public int getSpeedLimit() {
		return this.speedLimit;
	}
	
	public List<Vehicle> getVehicles() {
		return Collections.unmodifiableList(this.vehicles);
	}
	
	
}
