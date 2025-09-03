package simulator.model;

public class NewJunctionEvent extends Event {
	
	private Junction newJunction;
	String id;
	LightSwitchingStrategy lsStrategy;
	DequeuingStrategy dqStrategy;
	int xCoor;
	int yCoor;
	
	public NewJunctionEvent(int time, String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dqStrategy, int xCoor, int yCoor) {
		super(time);
		this.id = id;
		this.lsStrategy = lsStrategy;
		this.dqStrategy = dqStrategy;
		this.xCoor = xCoor;
		this.yCoor = yCoor;
	}

	@Override
	void execute(RoadMap map) {
		this.newJunction = new Junction(id, lsStrategy, dqStrategy, xCoor, yCoor);
		map.addJunction(newJunction);
	}
	
	@Override
	public String toString() {
		return "New Junction '"+id+"'";
	}

}
