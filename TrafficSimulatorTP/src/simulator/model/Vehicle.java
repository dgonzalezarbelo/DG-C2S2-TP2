package simulator.model;

import org.json.JSONObject;
import java.util.Collections;
import java.util.List;

public class Vehicle extends SimulatedObject {
	
	private List<Junction> itinerary;
	private int junctionIndex; //Guardamos el índice del último cruce
	private int maxSpeed;
	private int speed;
	private VehicleStatus status;
	private Road road;
	private int location;
	private int contClass;
	private int totalCont;
	private int totalTravelledDist;
	
	Vehicle(String id, int maxSpeed, int contClass, List<Junction> itinerary) {
		super(id);
		if(maxSpeed <= 0)
			throw new IllegalArgumentException("The maxSpeed must be a positive number");
		this.maxSpeed = maxSpeed;
		if(contClass < 0 || contClass > 10)
			throw new IllegalArgumentException("The contClass must be a number between 0 and 10 (both included)");
		this.contClass = contClass;
		if(itinerary.size() <= 1)
			throw new IllegalArgumentException("The itinerary must contain at least two elements");
		this.itinerary = Collections.unmodifiableList(itinerary);
		this.junctionIndex = 0;
		this.speed = 0;
		this.totalCont = 0;
		this.totalTravelledDist = 0;
		this.road = null;
		this.status = VehicleStatus.PENDING;
	}
	
	void setSpeed(int s) {
		if(s < 0)
			throw new IllegalArgumentException("The speed must ve a positive number");
		if(VehicleStatus.TRAVELING.equals(this.status)) {
			if(s < this.maxSpeed) this.speed = s;
			else this.speed = this.maxSpeed;
		}
	}
	
	void setContClass(int c) {
		if(c < 0 || c > 10)
			throw new IllegalArgumentException("The contClass must be a numbre between 0 and 10 (both included)");
		this.contClass = c;
	}
	
	void advance(int time) {
		if(VehicleStatus.TRAVELING.equals(this.status)) {
			int oldLocation = this.location;
			if(this.location + this.speed < this.road.getLength()) {
				this.location += this.speed;
				this.totalTravelledDist += this.speed;
			} 
			else {
				this.totalTravelledDist += road.getLength() - this.location;
				this.location = road.getLength();
				this.status = VehicleStatus.WAITING;
				this.itinerary.get(junctionIndex + 1).enter(this);
				this.speed = 0;	//El vehículo esta parado
			} 
			int newCont = (location - oldLocation) * this.contClass;
			this.totalCont += newCont;
			this.road.addContamination(newCont);
		}
	}
	
	void moveToNextRoad() {	
		if(VehicleStatus.PENDING.equals(this.status)) {
			this.road = this.itinerary.get(this.junctionIndex).roadTo(this.itinerary.get(this.junctionIndex + 1));
			this.road.enter(this);
			this.location = 0;
			this.status = VehicleStatus.TRAVELING;
		}
		else if (VehicleStatus.WAITING.equals(this.status)){
			this.road.exit(this);
			this.junctionIndex++;	//Pasamos al siguiente cruce
			if(this.junctionIndex == this.itinerary.size() - 1) {
				this.status = VehicleStatus.ARRIVED;
				this.location = 0;
			}
			else {
				Junction junction = this.itinerary.get(this.junctionIndex);
				this.road = junction.roadTo(this.itinerary.get(this.junctionIndex + 1));
				this.location = 0;
				this.road.enter(this);
				this.status = VehicleStatus.TRAVELING;
			}
		}
		else {
			throw new IllegalArgumentException("The vehicle status must be PENDING or WAITING");
		}
	}
	
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		jo.put("id", this._id);
		jo.put("speed", this.speed);
		jo.put("distance", this.totalTravelledDist);
		jo.put("co2", this.totalCont);
		jo.put("class", this.contClass);
		jo.put("status", this.status.name());
		if(!VehicleStatus.PENDING.equals(this.status) && !VehicleStatus.ARRIVED.equals(this.status)) jo.put("road", this.road.getId());
		if(!VehicleStatus.PENDING.equals(this.status) && !VehicleStatus.ARRIVED.equals(this.status)) jo.put("location", this.location);
		return jo;
	}
	
	public int getLocation() {
		return this.location;
	}
	
	public int getSpeed() {
		return this.speed;
	}
	
	public int getMaxSpeed() {
		return this.maxSpeed;
	}
	
	public int getContClass() {
		return this.contClass;
	}
	
	public VehicleStatus getStatus() {
		return this.status;
	}
	
	public int getTotalCO2() {
		return this.totalCont;
	}
	
	public List<Junction> getItinerary(){
		return Collections.unmodifiableList(this.itinerary);
	}
	
	public Road getRoad() {
		return this.road;
	}

	public int getTotalDist() {
		return this.totalTravelledDist;
	}
	
}
