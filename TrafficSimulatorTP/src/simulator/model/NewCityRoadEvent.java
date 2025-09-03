package simulator.model;

public class NewCityRoadEvent extends NewRoadEvent {

	public NewCityRoadEvent(int time, String id, String srcJun, String destJunc, int length, int co2Limit, int maxSpeed,
			Weather weather) {
		super(time, id, srcJun, destJunc, length, co2Limit, maxSpeed, weather);
	}
	
	protected Road createRoadObject() {
		return new CityRoad(id, srcJun, destJunc, maxSpeed, co2Limit, length, weather);
	}
	
}
