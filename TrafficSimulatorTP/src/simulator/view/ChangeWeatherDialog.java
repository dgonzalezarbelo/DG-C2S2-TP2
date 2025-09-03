package simulator.view;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.SetWeatherEvent;
import simulator.model.Weather;

public class ChangeWeatherDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int status;
	private RoadMap map;
	
	private JPanel mainPanel;
	private JLabel helpMsg;
	private JPanel centerPanel;
	private JPanel buttonsPanel;
	private JButton okButton;
	private JButton cancelButton;
	
	private Road[] roads;
	private JComboBox<Road> roadsCombo;
	
	private static final Weather[] WEATHER_VALUES = Weather.values();
	private JComboBox<Weather> weatherCombo;
	
	private JSpinner ticksSpinner;
	private int time;
	
	ChangeWeatherDialog(Frame parent, RoadMap map, int time) {
		super(parent, true);
		this.map = map;
		this.time = time;
		initGUI();
	}
	
	private void initGUI() {
		status = 0;
		
		setTitle("Change Road Weather");

		this.roads = new Road[map.getRoads().size()];
		for(int i = 0; i < map.getRoads().size(); i++) {
			this.roads[i] = map.getRoads().get(i);
		}
		
		this.mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		setContentPane(mainPanel);
		
		this.helpMsg = new JLabel("<html>Schedule an event to change the weather of a road after a given number of<br>simulation ticks from now.</html>"); //Lo de html es para el salto de línea
		helpMsg.setAlignmentX(CENTER_ALIGNMENT);
		helpMsg.setPreferredSize(new Dimension(500, 50));
		mainPanel.add(helpMsg);
		
		this.centerPanel = new JPanel();
		centerPanel.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(centerPanel);
		
		this.roadsCombo = new JComboBox<Road>(roads);
		this.weatherCombo = new JComboBox<Weather>(WEATHER_VALUES);
		this.ticksSpinner = new JSpinner(new SpinnerNumberModel(1, 0, 10000, 1));
		
		centerPanel.add(new JLabel("Road:"));
		centerPanel.add(roadsCombo);
		centerPanel.add(new JLabel("Weather:"));
		centerPanel.add(weatherCombo);
		centerPanel.add(new JLabel("Ticks:"));
		centerPanel.add(ticksSpinner);
		
		this.buttonsPanel = new JPanel();
		buttonsPanel.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(buttonsPanel);
		
		//CancelButton
		this.cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				status = 0;
				ChangeWeatherDialog.this.setVisible(false);
			}
			
		});
		buttonsPanel.add(cancelButton);
		
		//OKButton
		this.okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Road r = (Road) roadsCombo.getSelectedItem();
				if(r != null) {
					status = 1;
					ChangeWeatherDialog.this.setVisible(false);
				}
				else {
					JOptionPane.showMessageDialog(null,
							"Debe seleccionarse una carretera.");
				}
			}
			
		});
		buttonsPanel.add(okButton);
		
		pack();
		this.setMinimumSize(new Dimension(500, 200));
		this.setPreferredSize(new Dimension(500, 200));
		this.setMaximumSize(new Dimension(500, 200));
		setResizable(false);
		setVisible(false);
	}
	
	public int open() {
		this.setLocationRelativeTo(null);
		setVisible(true);
		return status;
	}
	
	public Event getNewEvent() {
		Road r = (Road) roadsCombo.getSelectedItem();
		if(r != null) {
			Weather w = (Weather) weatherCombo.getSelectedItem();
			int tick = (int) ticksSpinner.getValue() + time;
			Pair<String, Weather> p = new Pair<>(r.getId(), w);
			List<Pair<String, Weather>> list = new ArrayList<>();
			list.add(p);
			return new SetWeatherEvent(tick, list);
		}
		else return null;
	}
	
}
