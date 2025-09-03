package simulator.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;

public class VehiclesTableModel extends AbstractTableModel implements TrafficSimObserver {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Controller contr;
	private List<Vehicle> vehicles;
	private String[] colNames = { "Id", "Location", "Itinerary", "CO2 Class", "Max. Speed", "Speed", "Total CO2", "Distance" };
	private RoadMap map;
	
	VehiclesTableModel(Controller c) {
		this.contr = c;
		this.contr.addObserver(this);
	}
	
	private void update() {
		this.vehicles = map.getVehicles();
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
		return this.vehicles == null ? 0 : this.vehicles.size();
	}

	@Override
	public Object getValueAt(int row, int column) {
		Object s = null;
		switch (column) {
		case 0:
			s = vehicles.get(row).getId();
			break;
		case 1:
			s = String.format("%s:%d", vehicles.get(row).getRoad(), vehicles.get(row).getLocation());
			break;
		case 2:
			s = vehicles.get(row).getItinerary();
			break;
		case 3:
			s = vehicles.get(row).getContClass();
			break;
		case 4:
			s = vehicles.get(row).getMaxSpeed();
			break;
		case 5:
			s = vehicles.get(row).getSpeed();
			break;
		case 6:
			s = vehicles.get(row).getTotalCO2();
			break;
		case 7:
			s = vehicles.get(row).getTotalDist();
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
