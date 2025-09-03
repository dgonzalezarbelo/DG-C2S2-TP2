package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetContClassEvent;

public class SetContClassEventBuilder extends Builder<Event> {

	public SetContClassEventBuilder() {
		super("set_cont_class");
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		int time = data.getInt("time");
		JSONArray ja = data.getJSONArray("info");
		List<Pair<String, Integer>> cs = new ArrayList<Pair<String, Integer>>();
		for(int i = 0; i < ja.length(); i++) {
			JSONObject jo = ja.getJSONObject(i);
			Pair<String, Integer> p = new Pair<String, Integer> (jo.getString("vehicle"), jo.getInt("class"));
			cs.add(p);
		}
		return new SetContClassEvent(time, cs);
	}

}
