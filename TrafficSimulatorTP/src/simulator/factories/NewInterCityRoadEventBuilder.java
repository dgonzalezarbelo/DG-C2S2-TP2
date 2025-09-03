package simulator.factories;


import simulator.model.Event;
import simulator.model.NewInterCityRoadEvent;
import simulator.model.Weather;

public class NewInterCityRoadEventBuilder extends NewRoadEventBuilder{
	
	private final static String TYPE = "new_inter_city_road";
	

	public NewInterCityRoadEventBuilder() {
		super(NewInterCityRoadEventBuilder.TYPE);
	}

	public Event createRoad(int time, String id, String src, String des, int length, int co2limit, int maxSpeed, Weather weather) {
		return new NewInterCityRoadEvent(time, id, src, des, length, co2limit, maxSpeed, weather);
	}

}
