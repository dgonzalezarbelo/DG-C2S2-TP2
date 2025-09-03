package simulator.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class EventsTableModel extends AbstractTableModel implements TrafficSimObserver {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Controller contr;
	private String[] colNames = { "Time", "Desc." };
	private List<Event> events;
	
	EventsTableModel(Controller contr) {
		this.contr = contr;
		this.events = null;
		this.contr.addObserver(this);
	}
	
	private void update() {
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
		return this.events == null ? 0 : this.events.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) { //Primera columna es el tiempo y segunda una descripción. La cero es el número del evento.
		Object s = null;
		switch (columnIndex) {
		case 0:
			s = events.get(rowIndex).getTime();
			break;
		case 1:
			s = events.get(rowIndex).toString();
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
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		update();	
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		this.events = events;
		update();
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {}

	@Override
	public void onError(String err) {}

}
