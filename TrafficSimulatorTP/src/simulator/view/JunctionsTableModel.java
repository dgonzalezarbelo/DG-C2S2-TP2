package simulator.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class JunctionsTableModel extends AbstractTableModel implements TrafficSimObserver {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Controller contr;
	private List<Junction> junctions;
	private String[] colNames = { "Id", "Green", "Queues" };
	private RoadMap map;
	
	JunctionsTableModel(Controller c) {
		this.contr = c;
		this.junctions =  null;
		this.contr.addObserver(this);
	}
	
	private void update() {
		this.junctions = map.getJunctions();
		fireTableDataChanged();
	}
	
	public void setJunctionsList(List<Junction> junctions) {
		this.junctions = junctions;
		update();
	}
	
	@Override
	public String getColumnName(int col) {
		return colNames[col];
	}

	@Override
	public int getColumnCount() {
		return colNames.length;
	}

	@Override
	public int getRowCount() {
		return this.junctions == null ? 0 : this.junctions.size();
	}

	@Override
	public Object getValueAt(int row, int column) {
		Object s = null;
		switch (column) {
		case 0:
			s = junctions.get(row).getId();
			break;
		case 1:
			s = junctions.get(row).getGreenLightIndex() != -1 ? junctions.get(row).getInRoads().get(junctions.get(row).getGreenLightIndex()) : "NONE";
			break;
		case 2:
			StringBuilder str = new StringBuilder();
			List<Road> inRoads = junctions.get(row).getInRoads();
			for(int i = 0; i < inRoads.size(); i++) {
				if(i > 0) str.append(", ");
				Road r = inRoads.get(i);
				str.append(String.format("%s:%s", r.getId(), r.getDest().getRoadQueue(r)));
			}
			s = str.toString();
			break;
		}
		return s;
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		update();
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		this.map = map;
		update();
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		this.map = map;
	}

	@Override
	public void onError(String err) {}

}
