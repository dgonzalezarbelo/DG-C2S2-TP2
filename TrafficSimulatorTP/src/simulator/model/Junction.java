package simulator.model;

import java.util.ArrayList;
import java.util.Collections;

import org.json.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Junction extends SimulatedObject {

	private List<Road> inRoads;	//Las carreteras entrantes
	private Map<Junction, Road> roadsMap;	//Para buscar la carretera con la que llegar a cierto cruce
	private List<List<Vehicle>> qs;	//La posicion i representa la cola de vehiculos esperando a entrar por la carretera i
	private Map<Road, List<Vehicle>> qsMap;	//Asocia a cada carretera su cola de entrada
	private int currGreen;
	private int lastSwitchingTime;
	private LightSwitchingStrategy lsStrategy;
	private DequeuingStrategy dqStrategy;
	private int xCoor;
	private int yCoor;
	
	Junction(String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dqStrategy, int xCoor, int yCoor) {
		super(id);
		if(lsStrategy == null)
			throw new IllegalArgumentException("lsStrategy must be different from null");
		this.lsStrategy = lsStrategy;
		if(dqStrategy == null)
			throw new IllegalArgumentException("dqStrategy must be different from null");
		this.dqStrategy = dqStrategy;
		if(xCoor < 0)
			throw new IllegalArgumentException("xCoor must be non negative");
		this.xCoor = xCoor;
		if(yCoor < 0)
			throw new IllegalArgumentException("yCoor must be non negative");
		this.yCoor = yCoor;
		this.inRoads = new ArrayList<Road>();
		this.roadsMap = new HashMap<Junction, Road>();
		this.qs = new ArrayList<List<Vehicle>>();
		this.qsMap = new HashMap<Road, List<Vehicle>>();
		this.currGreen = -1;
		this.lastSwitchingTime = 0;
	}

	void addIncommingRoad(Road r) {
		if(!r.getDest().equals(this))
			throw new IllegalArgumentException("The road's destination must be this junction");
		if(this.inRoads.contains(r))
			throw new IllegalArgumentException("The road is already contained in this junctions list");
		this.inRoads.add(r);
		LinkedList<Vehicle> queue = new LinkedList<Vehicle>();
		for(Vehicle v : r.getVehicles()) {
			if(VehicleStatus.WAITING.equals(v.getStatus())) queue.add(v);
		}
		qs.add(queue);
		qsMap.put(r, queue);
	}
	
	void addOutGoingRoad(Road r) {
		if(!r.getSrc().equals(this)) {
			throw new IllegalArgumentException("The road's source must be this junction");
		}
		if(roadsMap.containsKey(r.getDest()))
			throw new IllegalArgumentException("There's already a road with the same destination");
		roadsMap.put(r.getDest(), r);
	}
	
	void enter(Vehicle v) {
		int index = inRoads.indexOf(v.getRoad());
		if(index == -1)
			throw new IllegalArgumentException("The vehicle must be in a road connected to this junction");
		if(!VehicleStatus.WAITING.equals(v.getStatus()))
			throw new IllegalArgumentException("The vehicle must be WAITING");
		qs.get(index).add(v);
	}
	
	Road roadTo(Junction j) {
		return roadsMap.get(j);
	}

	void advance(int time) {
		//Si hay un semáforo en verde movemos a los vehículos correspondientes a la carretera de dicho semáforo
		if(currGreen != -1) {
			List<Vehicle> movingVehicles = this.dqStrategy.dequeue(qsMap.get(inRoads.get(currGreen)));
			for(Vehicle v : movingVehicles) {
				v.moveToNextRoad();
				qsMap.get(inRoads.get(currGreen)).remove(v);
			}
		}
		int newGreenLight = this.lsStrategy.chooseNextGreen(inRoads, qs, currGreen, lastSwitchingTime, time);
		if(newGreenLight != currGreen) {
			currGreen = newGreenLight;
			lastSwitchingTime = time;
		}
	}
	
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		JSONArray ja = new JSONArray();
		jo.put("id", this._id);
		String roadId;
		if(currGreen == -1) roadId = "none";
		else roadId = this.inRoads.get(currGreen).getId();
		jo.put("green", roadId);
		for(int i = 0; i < inRoads.size(); i++) {
			JSONObject rJO = new JSONObject();
			rJO.put("road", inRoads.get(i).getId());
			JSONArray rJA = new JSONArray();
			List<Vehicle> list = qs.get(i);
			for(int j = 0; j < list.size(); j++) {
				rJA.put(list.get(j).getId());
			}
			rJO.put("vehicles", rJA);
			ja.put(rJO);
		}
		jo.put("queues", ja);
		return jo;
	}
	
	public String getId() {
		return this._id;
	}
	
	public String toString() {
		return this._id;
	}

	public int getX() {
		return this.xCoor;
	}
	
	public int getY() {
		return this.yCoor;
	}

	public int getGreenLightIndex() {
		return this.currGreen;
	}

	public List<Road> getInRoads() { 
		return Collections.unmodifiableList(inRoads);
	}
	
	public List<Vehicle> getRoadQueue(Road r) {
		int i = 0;
		boolean found = false;
		while(i < this.inRoads.size() && !found) {
			if(this.inRoads.get(i) == r) {
				found = true;
			}
			else i++;
		}
		return Collections.unmodifiableList(this.qs.get(i));
	}
	
}
