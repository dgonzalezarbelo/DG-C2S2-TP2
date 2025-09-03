package simulator.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class StatusBar extends JPanel implements TrafficSimObserver {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int time;
	private Event addedEvent;
	private JLabel timeLabel;
	private JLabel msgLabel;
	private Controller contr;
	private static final String WELCOME_MSG = "Welcome!";
	
	StatusBar(Controller contr) {
		this.time = 0;
		this.contr = contr;
		this.initGUI();
		this.contr.addObserver(this);
	}
	
	private void initGUI() {
		
		JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		this.timeLabel = new JLabel("Time: " + time);
		timePanel.add(this.timeLabel);
		timePanel.setPreferredSize(new Dimension(150,20));
		
		JPanel msgPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		this.msgLabel = new JLabel();
		msgPanel.add(this.msgLabel);
		
		JSeparator sep = new JSeparator(JSeparator.VERTICAL);
		sep.setPreferredSize(new Dimension(10,20));
		
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(timePanel);
		this.add(sep);
		this.add(msgPanel);
		
		this.setVisible(true);
		
	}
	
	private void update() {
		this.timeLabel.setText(String.format("Time: %s", this.time));
		if(this.addedEvent != null)
			this.msgLabel.setText(String.format("Event added(%s)", this.addedEvent.toString()));
		else
			this.msgLabel.setText("");
		this.repaint();
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		this.time = time;
		this.addedEvent = null;
		this.update();
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		this.time = time;
		this.update();
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		this.time = time;
		this.addedEvent = e;
		this.update();
		
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		this.time = time;	
		this.msgLabel.setText(WELCOME_MSG);
		this.update();
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		this.time = time;
		this.msgLabel.setText(WELCOME_MSG);
	}

	@Override
	public void onError(String err) {
		this.msgLabel.setText(err);
	}
}
