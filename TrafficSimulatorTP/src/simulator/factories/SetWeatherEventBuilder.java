package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.*;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetWeatherEvent;
import simulator.model.Weather;

public class SetWeatherEventBuilder extends Builder<Event> {

	public SetWeatherEventBuilder() {
		super("set_weather");
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		int time = data.getInt("time");
		JSONArray ja = data.getJSONArray("info");
		List<Pair<String, Weather>> ws = new ArrayList<Pair<String, Weather>>();
		for(int i = 0; i < ja.length(); i++) {
			JSONObject jo = ja.getJSONObject(i);
			Pair<String, Weather> p = new Pair<String, Weather> (jo.getString("road"), Weather.valueOf(jo.getString("weather")));
			ws.add(p);
		}
		return new SetWeatherEvent(time, ws);
	}

}
