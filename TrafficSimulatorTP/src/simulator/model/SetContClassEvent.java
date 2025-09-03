package simulator.model;

import java.util.List;

import simulator.misc.Pair;

public class SetContClassEvent extends Event {
	
	private List<Pair<String, Integer>> cs;
	
	public SetContClassEvent(int time, List<Pair<String, Integer>> cs) {
		super(time);
		if(cs == null)
			throw new IllegalArgumentException("cs must be different from null");
		this.cs = cs;
	}

	@Override
	void execute(RoadMap map) {
		for(Pair<String, Integer> p : cs) {
			Vehicle v = map.getVehicle(p.getFirst());
			if(v == null)
				throw new IllegalArgumentException("A vehicle from cs isn't contained in the roadMap");
			v.setContClass(p.getSecond());
		}
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("Change CO2 Class: ");
		str.append("[");
		boolean first = true;
		for(Pair<String, Integer> p : cs) {
			if(first) first = false;
			else str.append(", ");
			str.append(String.format("(%s,%s)", p.getFirst(), p.getSecond()));
		}
		str.append("]");
		return str.toString();
	}

	
	
}
