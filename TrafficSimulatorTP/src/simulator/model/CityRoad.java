package simulator.model;

public class CityRoad extends Road {
	
	CityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length,
			Weather weather) {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}
	
	void reduceTotalContamination() {
		if(Weather.WINDY.equals(this.weather) || Weather.STORM.equals(this.weather))
			this.totalCont -= 10;
		else
			this.totalCont -= 2;
		if(this.totalCont < 0) this.totalCont = 0;
	}
	
	void updateSpeedLimit() {}
	
	int calculateVehicleSpeed(Vehicle v) {
		return (int)(((11.0 - v.getContClass()) / 11.0) * this.speedLimit);
	}
}
