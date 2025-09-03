package simulator.model;

import java.util.LinkedList;
import java.util.List;

public class MoveAllStrategy implements DequeuingStrategy {
	
	public MoveAllStrategy() {}
	
	public List<Vehicle> dequeue(List<Vehicle> q) {
		List<Vehicle> movingVehicles = new LinkedList<Vehicle>();
		for(Vehicle v : q) {
			movingVehicles.add(v);
		}
		return movingVehicles;
	}
}
