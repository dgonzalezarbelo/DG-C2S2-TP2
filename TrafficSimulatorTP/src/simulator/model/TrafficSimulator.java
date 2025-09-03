package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.*;

import simulator.misc.SortedArrayList;

public class TrafficSimulator implements Observable<TrafficSimObserver> {
	
	private RoadMap map;
	private List<Event> eventList;
	private List<Event> readOnlyEventList;
	private int time;
	private ArrayList<TrafficSimObserver> viewList;
	
	public TrafficSimulator() {
		this.map = new RoadMap();
		this.eventList = new SortedArrayList<Event>();
		this.readOnlyEventList = Collections.unmodifiableList(this.eventList);
		this.time = 0;
		this.viewList = new ArrayList<>();
	}
	
	public void addEvent(Event e) {
		this.eventList.add(e); //Se insertan ordenados porque es una SortedArrayList
		this.onEventAdded(e);
	}
	
	public void advance() {
		time++;
		
		this.onAdvanceStart();
		
		while(eventList.size() != 0 && eventList.get(0).getTime() == time) {
			eventList.get(0).execute(map);
			eventList.remove(0);
		}
		
		List<Junction> jList = map.getJunctions();
		for(Junction j : jList) j.advance(time);
		
		List<Road> rList = map.getRoads();
		for(Road r : rList) r.advance(time);
		
		this.onAdvanceEnd();
	}
	
	public void reset() {
		map.reset();
		eventList.clear();
		time = 0;
		this.onReset();
	}
	
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		jo.put("time", time);
		jo.put("state", map.report());
		return jo;
	}

	@Override
	public void addObsever(TrafficSimObserver o) {
		this.viewList.add(o);		
		this.onRegister();
	}

	@Override
	public void removeObserver(TrafficSimObserver o) {
		this.viewList.remove(o);
		
	}
	
	private void onAdvanceStart() {
		for(int i = 0; i  < this.viewList.size(); i++)
			viewList.get(i).onAdvanceStart(this.map, this.readOnlyEventList, this.time);
			
	}
	
	private void onAdvanceEnd() {
		for(int i = 0; i  < this.viewList.size(); i++)
			viewList.get(i).onAdvanceEnd(this.map, this.readOnlyEventList, this.time);
	}
	
	private void onEventAdded(Event event) {
		for(int i = 0; i  < this.viewList.size(); i++)
			viewList.get(i).onEventAdded(this.map, this.readOnlyEventList, event, this.time);
	}
	
	private void onReset() {
		for(int i = 0; i  < this.viewList.size(); i++)
			viewList.get(i).onReset(this.map, this.readOnlyEventList, this.time);
	}
	
	private void onRegister() {
		for(int i = 0; i  < this.viewList.size(); i++)
			viewList.get(i).onRegister(this.map, this.readOnlyEventList, this.time);
	}
	
	public void onError(String message) {
		for(int i = 0; i < this.viewList.size(); i++)
			viewList.get(i).onError(message);
	}	
	
}
