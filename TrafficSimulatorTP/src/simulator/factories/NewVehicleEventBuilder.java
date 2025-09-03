package simulator.factories;

import java.util.ArrayList;

import org.json.*;

import simulator.model.Event;
import simulator.model.NewVehicleEvent;

public class NewVehicleEventBuilder extends Builder<Event>{

	public NewVehicleEventBuilder() {
		super("new_vehicle");
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		int time = data.getInt("time");
		String id = data.getString("id");
		int maxSpeed = data.getInt("maxspeed");
		int contClass = data.getInt("class");
		JSONArray ja = data.getJSONArray("itinerary");
		ArrayList<String> itinerary = new ArrayList<String>();
		for(int i = 0; i < ja.length(); i++) {
			itinerary.add(ja.getString(i));
		}
		return new NewVehicleEvent(time, id, maxSpeed, contClass, itinerary);
	}

}
