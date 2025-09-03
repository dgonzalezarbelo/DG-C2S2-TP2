package simulator.model;

import java.util.List;

public class MostCrowdedStrategy implements LightSwitchingStrategy {
	
	private int timeSlot;
	
	public MostCrowdedStrategy(int timeSlot) {
		this.timeSlot = timeSlot;
	}
	
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime, int currTime) {
		if(roads.size() == 0) return -1;
		else if(currGreen == -1) {
			int maxSize = 0, index = -1;
			for(int i = 0; i < qs.size(); i++) {
				if(qs.get(i).size() > maxSize) {
					maxSize = qs.get(i).size();
					index = i;
				}
			}
			return index;
		}
		else if(currTime - lastSwitchingTime < timeSlot) return currGreen;
		else {
			int maxSize = 0, index = -1;
			//Hacemos un recorrido circular
			for(int i = (currGreen + 1) % roads.size(); i != currGreen; i = (i + 1) % roads.size()) {
				if(qs.get(i).size() > maxSize) {
					maxSize = qs.get(i).size();
					index = i;
				}
			}
			return index;
		}
	}
	
}
