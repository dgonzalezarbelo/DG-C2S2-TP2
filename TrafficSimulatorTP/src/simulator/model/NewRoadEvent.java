package simulator.model;

public abstract class NewRoadEvent extends Event {
	
	protected Road newRoad;
	protected String id;
	protected String srcJunId;
	protected String destJuncId;
	protected Junction srcJun;
	protected Junction destJunc;
	protected int length;
	protected int co2Limit;
	protected int maxSpeed;
	protected Weather weather;
	
	public NewRoadEvent(int time, String id, String srcJunId, String destJuncId, int length, int co2Limit, int maxSpeed, Weather weather) {
		super(time);
		this.id = id;
		this.srcJunId = srcJunId;
		this.destJuncId = destJuncId;
		this.length = length;
		this.co2Limit = co2Limit;
		this.maxSpeed = maxSpeed;
		this.weather = weather;
	}
	
	void execute(RoadMap map) {
		this.srcJun = map.getJunction(srcJunId);
		this.destJunc = map.getJunction(destJuncId);
		this.newRoad = createRoadObject();
		map.addRoad(newRoad);
	}
	
	protected abstract Road createRoadObject();

	@Override
	public String toString() {
		return "New Road '"+id+"'";
	}

}
