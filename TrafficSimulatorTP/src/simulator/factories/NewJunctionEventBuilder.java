package simulator.factories;

import org.json.*;

import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.NewJunctionEvent;

public class NewJunctionEventBuilder extends Builder<Event>{

	private Factory<LightSwitchingStrategy> lssFactory;
	private Factory<DequeuingStrategy> dqsFactory;
	
	public NewJunctionEventBuilder(Factory<LightSwitchingStrategy> lssFactory, Factory<DequeuingStrategy> dqsFactory) {
		super("new_junction");
		this.lssFactory = lssFactory;
		this.dqsFactory = dqsFactory;
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		int time = data.getInt("time");
		String id = data.getString("id");
		JSONArray coor = data.getJSONArray("coor");
		int xCoor = coor.getInt(0);
		int yCoor = coor.getInt(1);
		
		JSONObject lsJObj = data.getJSONObject("ls_strategy");
		LightSwitchingStrategy lsStrategy = lssFactory.createInstance(lsJObj);
		
		JSONObject dqJObj = data.getJSONObject("dq_strategy");
		DequeuingStrategy dqStrategy = dqsFactory.createInstance(dqJObj);
		return new NewJunctionEvent(time, id, lsStrategy, dqStrategy, xCoor, yCoor);
	}

}
