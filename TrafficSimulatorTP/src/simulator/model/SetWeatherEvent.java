package simulator.model;

import java.util.List;

import simulator.misc.Pair;

public class SetWeatherEvent extends Event {
	
	List<Pair<String, Weather>> ws;
	
	public SetWeatherEvent(int time, List<Pair<String, Weather>> ws) {
		super(time);
		if(ws == null)
			throw new IllegalArgumentException("ws must be different from null");
		this.ws = ws;
	}

	@Override
	void execute(RoadMap map) {
		for(Pair<String, Weather> p : ws) {
			Road r = map.getRoad(p.getFirst());
			if(r == null)
				throw new IllegalArgumentException("A road from ws isn't contained in the roadMap");
			r.setWeather(p.getSecond());
		}
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("Change Weather: ");
		str.append("[");
		boolean first = true;
		for(Pair<String, Weather> p : ws) {
			if(first) first = false;
			else str.append(", ");
			str.append(String.format("(%s,%s)", p.getFirst(), p.getSecond()));
		}
		str.append("]");
		return str.toString();
	}
}
