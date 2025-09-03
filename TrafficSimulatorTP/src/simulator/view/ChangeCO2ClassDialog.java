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
import simulator.model.RoadMap;
import simulator.model.SetContClassEvent;
import simulator.model.Vehicle;

public class ChangeCO2ClassDialog extends JDialog {
	
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
	
	private Vehicle[] vehicles;
	private JComboBox<Vehicle> vehicleCombo;
	
	private static final Integer[] CO2_VALUES = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
	private JComboBox<Integer> co2Combo;
	
	private JSpinner ticksSpinner;
	private int time;
	
	ChangeCO2ClassDialog(Frame parent, RoadMap map, int time) {
		super(parent, true);
		this.map = map;
		this.time = time;
		this.initGUI();
	}
	
	private void initGUI() {
		status = 0;
		
		setTitle("Change CO2 Class");
		
		this.vehicles = new Vehicle[map.getVehicles().size()];
		for(int i = 0; i < map.getVehicles().size(); i++) {
			this.vehicles[i] = map.getVehicles().get(i);
		}
		
		this.mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		setContentPane(mainPanel);
		
		this.helpMsg = new JLabel("<html>Schedule an event to change the CO2 class of a vehicle after a given number<br>of simulation ticks from now.</html>");
		helpMsg.setAlignmentX(CENTER_ALIGNMENT);
		helpMsg.setPreferredSize(new Dimension(500, 50));
		mainPanel.add(helpMsg);
		
		this.centerPanel = new JPanel();
		centerPanel.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(centerPanel);
		
		this.vehicleCombo = new JComboBox<Vehicle>(this.vehicles);
		this.co2Combo = new JComboBox<Integer>(CO2_VALUES);
		this.ticksSpinner = new JSpinner(new SpinnerNumberModel(1, 0, 1000, 1));
		
		centerPanel.add(new JLabel("Vehicle:"));
		centerPanel.add(vehicleCombo);
		centerPanel.add(new JLabel("CO2 Class:"));
		centerPanel.add(co2Combo);
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
				ChangeCO2ClassDialog.this.setVisible(false);
			}
			
		});
		buttonsPanel.add(cancelButton);
		
		//OKButton		
		this.okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Vehicle v = (Vehicle) vehicleCombo.getSelectedItem();
				if(v != null) {
					status = 1;
					ChangeCO2ClassDialog.this.setVisible(false);
				}
				else {
					JOptionPane.showMessageDialog(null,
							"Debe seleccionarse un vehículo.");
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
//		setLocation(getParent().getLocation().x + 250, getParent().getLocation().y + 380);
		this.setLocationRelativeTo(null);
		setVisible(true);
		return this.status;
	}
	
	public Event getNewEvent() {
		Vehicle v = (Vehicle) vehicleCombo.getSelectedItem();
		if(v != null) {
			Integer co2Class = (Integer) co2Combo.getSelectedItem();
			int tick = (int) ticksSpinner.getValue() + time;
			Pair<String, Integer> cs = new Pair<String, Integer>(v.getId(), co2Class);
			List <Pair<String, Integer>> list = new ArrayList<>();
			list.add(cs);
			return new SetContClassEvent(tick, list);
		}
		else return null;
	}
}
