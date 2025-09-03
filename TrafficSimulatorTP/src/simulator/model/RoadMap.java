package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.*;

public class RoadMap {
	
	private List<Junction> junctions;
	private List<Road> roads;
	private List<Vehicle> vehicles;
	private Map<String, Junction> junctionsMap;
	private Map<String, Road> roadsMap;
	private Map<String, Vehicle> vehiclesMap;
	
	RoadMap() {
		this.junctions = new ArrayList<Junction>();
		this.roads = new ArrayList<Road>();
		this.vehicles = new ArrayList<Vehicle>();
		this.junctionsMap = new HashMap<String, Junction>();
		this.roadsMap = new HashMap<String, Road>();
		this.vehiclesMap = new HashMap<String, Vehicle>();
	}
	
	void addJunction(Junction j) {
		if(junctionsMap.containsKey(j.getId()))
			throw new IllegalArgumentException("There is already a junction with the same id");
		junctions.add(j);
		junctionsMap.put(j.getId(), j);
	}
	
	void addRoad(Road r) {
		if(roadsMap.containsKey(r.getId()))
			throw new IllegalArgumentException("There is already a road with the same id");
		if(!junctionsMap.containsValue(r.getSrc()) || !junctionsMap.containsValue(r.getDest()))
			throw new IllegalArgumentException("Either the source or destination of the road is not contained in the roadMap");
		roads.add(r);
		roadsMap.put(r.getId(), r);
	}
	
	void addVehicle(Vehicle v) {
		if(vehiclesMap.containsKey(v.getId()))
			throw new IllegalArgumentException("There is already a vehicle with the same id");
		int i = 1;
		List<Junction> itinerary = v.getItinerary();
		//Recorremos el itinerario del vehículo, comprobando que todas las carreteras que conectan cada par de junctions están contenidas en el roadMap
		while(i < v.getItinerary().size()) {
			Road road = itinerary.get(i - 1).roadTo(itinerary.get(i));
			if(road == null || !roadsMap.containsValue(road)) 
				throw new IllegalArgumentException("A road that connects two of the junctions from the itinerary isn't contained in the roadMap");
			i++;
		}
		vehicles.add(v);
		vehiclesMap.put(v.getId(), v);
	}
	
	public Junction getJunction(String id) {
		return junctionsMap.get(id);
	}
	
	public Road getRoad(String id) {
		return roadsMap.get(id);
	}
	
	public Vehicle getVehicle(String id) {
		return vehiclesMap.get(id);
	}
	
	public List<Junction> getJunctions() {
		return Collections.unmodifiableList(junctions);
	}
	
	public List<Road> getRoads() {
		return Collections.unmodifiableList(roads);
	}
	
	public List<Vehicle> getVehicles() {
		return Collections.unmodifiableList(vehicles);
	}

	void reset() {
		junctions.clear();
		roads.clear();
		vehicles.clear();
		junctionsMap.clear();
		roadsMap.clear();
		vehiclesMap.clear();
	}
	
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		JSONArray junctionsArray = new JSONArray();
		JSONArray roadsArray = new JSONArray();
		JSONArray vehiclesArray = new JSONArray();
		for(Junction j : junctions) {
			junctionsArray.put(j.report());
		}
		for(Road r : roads) {
			roadsArray.put(r.report());
		}
		for(Vehicle v : vehicles) {
			vehiclesArray.put(v.report());
		}
		jo.put("junctions", junctionsArray);
		jo.put("roads", roadsArray);
		jo.put("vehicles", vehiclesArray);
		return jo;		
	}
}
