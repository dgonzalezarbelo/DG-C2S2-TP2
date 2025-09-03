package simulator.factories;

import org.json.JSONObject;

import simulator.model.LightSwitchingStrategy;
import simulator.model.MostCrowdedStrategy;

public class MostCrowdedStrategyBuilder extends Builder<LightSwitchingStrategy> {
	
	private static final int TIME_SLOT = 1;
	
	public MostCrowdedStrategyBuilder() {
		super("most_crowded_lss");
	}

	@Override
	protected LightSwitchingStrategy createTheInstance (JSONObject data) {
		if (data.has("timeslot"))
			return new MostCrowdedStrategy(data.getInt("timeslot"));
		
		return new MostCrowdedStrategy(MostCrowdedStrategyBuilder.TIME_SLOT);
	}

}
