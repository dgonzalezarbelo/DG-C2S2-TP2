package simulator.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class RoadsTableModel  extends AbstractTableModel implements TrafficSimObserver{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Controller contr;
	private String[] colNames = { "Id", "Length", "Weather", "Max. Speed", "Speed limit" , "Total CO2", "Contamination limit"};
	private List<Road> roads;
	private RoadMap map;
	
	RoadsTableModel(Controller contr) {
		this.contr = contr;
		this.roads = null;
		this.contr.addObserver(this);
	}
	
	private void update() {
		this.roads = map.getRoads();
		fireTableDataChanged();	
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
		return this.roads == null ? 0 : this.roads.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) { //Primera columna es el tiempo y segunda una descripción. La cero es el número del evento.
		Object s = null;
		switch (columnIndex) {
		case 0:
			s = roads.get(rowIndex).getId();
			break;
		case 1:
			s = roads.get(rowIndex).getLength();
			break;
		case 2:
			s = roads.get(rowIndex).getWeather();
			break;
		case 3:
			s = roads.get(rowIndex).getMaxSpeed();
			break;
		case 4:
			s = roads.get(rowIndex).getSpeedLimit();
			break;
		case 5:
			s = roads.get(rowIndex).getTotalCO2();
			break;
		case 6:
			s = roads.get(rowIndex).getContLimit();
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
