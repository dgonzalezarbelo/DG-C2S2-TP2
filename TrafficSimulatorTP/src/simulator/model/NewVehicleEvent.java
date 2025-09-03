package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class NewVehicleEvent extends Event {
	
	Vehicle newVehicle;
	String id;
	int maxSpeed;
	int contClass;
	List<String> itineraryIds;
	List<Junction> itinerary;
	
	public NewVehicleEvent(int time, String id, int maxSpeed, int contClass, List<String> itinerary) {
		super(time);
		this.id = id;
		this.maxSpeed = maxSpeed;
		this.contClass = contClass;
		this.itineraryIds = itinerary;
	}

	@Override
	void execute(RoadMap map) {
		this.itinerary = new ArrayList<Junction>();
		for(String s : itineraryIds) {
			itinerary.add(map.getJunction(s));
		}
		this.newVehicle = new Vehicle(id, maxSpeed, contClass, itinerary);
		map.addVehicle(newVehicle);
		newVehicle.moveToNextRoad();
	}
	
	@Override
	public String toString() {
		return "New Vehicle '"+id+"'";
	}

}
