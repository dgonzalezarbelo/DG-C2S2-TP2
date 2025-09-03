package simulator.factories;

import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.Weather;

public abstract class NewRoadEventBuilder extends Builder<Event> {

	public NewRoadEventBuilder(String type) {
		super(type);
	}
	
	@Override
	protected Event createTheInstance(JSONObject data) {
		return createRoad(data.getInt("time"), data.getString("id"), data.getString("src"), data.getString("dest"),
				data.getInt("length"), data.getInt("co2limit"), data.getInt("maxspeed"), Weather.valueOf(data.getString("weather")));
	}
	
	protected abstract Event createRoad(int time, String id, String src, String des, int length, int co2limit, int maxSpeed, Weather weather);

}
