package simulator.model;

import java.util.LinkedList;
import java.util.List;

public class MoveFirstStrategy implements DequeuingStrategy {

	public MoveFirstStrategy () {}
	
	public List<Vehicle> dequeue(List<Vehicle> q) {
		List<Vehicle> movingVehicles = new LinkedList<Vehicle>();
		if(!q.isEmpty()) movingVehicles.add(q.get(0));
		return movingVehicles;
	}
}
