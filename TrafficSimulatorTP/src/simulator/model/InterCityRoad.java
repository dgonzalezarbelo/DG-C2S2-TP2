package simulator.model;

public class InterCityRoad extends Road {

	InterCityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length,
			Weather weather) {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}
	
	void reduceTotalContamination() {
		int x = 0;
		switch(weather) {
		case SUNNY:
			x = 2;
			break;
		case CLOUDY:
			x = 3;
			break;
		case RAINY:
			x = 10;
			break;
		case WINDY:
			x = 15;
			break;
		case STORM:
			x = 20;
			break;
		}
		this.totalCont = (int)((((100.0 - x) / 100.0))*this.totalCont);
	}
	
	void updateSpeedLimit() {
		if(this.totalCont > this.contLimit)
			this.speedLimit = (int)(this.maxSpeed * 0.5);
		else
			this.speedLimit = this.maxSpeed;
	}
	
	int calculateVehicleSpeed(Vehicle v) {
		if(Weather.STORM.equals(this.weather))
			return (int)(this.speedLimit * 0.8);
		else 
			return this.speedLimit;
	}
}
