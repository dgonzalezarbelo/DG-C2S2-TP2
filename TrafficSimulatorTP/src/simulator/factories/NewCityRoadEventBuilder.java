package simulator.factories;

import simulator.model.Event;
import simulator.model.NewCityRoadEvent;
import simulator.model.Weather;

public class NewCityRoadEventBuilder extends NewRoadEventBuilder{
	
	private final static String TYPE = "new_city_road";

	public NewCityRoadEventBuilder() {
		super(NewCityRoadEventBuilder.TYPE);
	}

	public Event createRoad(int time, String id, String src, String des, int length, int co2limit, int maxSpeed, Weather weather) {
		return new NewCityRoadEvent(time, id, src, des, length, co2limit, maxSpeed, weather);
	}

}
