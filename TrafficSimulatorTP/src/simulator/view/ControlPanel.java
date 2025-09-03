package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class ControlPanel extends JPanel implements TrafficSimObserver {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Controller contr;
	
	private JFileChooser fc;
	private JButton fcButton;
	private JButton changeCO2Button;
	private JButton weatherButton;
	private JSpinner ticksSpinner;
	private JButton runButton;
	private JButton stopButton;
	private JButton exitButton;
	
	private JFrame parent;
	private RoadMap map;
	private Boolean _stopped;
	private int time;
	
	ControlPanel (Controller contr) {
		this.contr = contr;
		this._stopped = false;
		this.parent = (JFrame) SwingUtilities.getWindowAncestor(this);
		initGUI();
		contr.addObserver(this);
	}
	
	private void initGUI() {
		this.setLayout(new BorderLayout());
		JToolBar toolBar = new JToolBar();
		this.add(toolBar);
		
		//JFileChooser
		this.fc = new JFileChooser();
		this.fcButton = new JButton();
		fcButton.setToolTipText("Open");
		fcButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int ret = fc.showOpenDialog(null);
				if (ret == JFileChooser.APPROVE_OPTION) {
					File fichero = fc.getSelectedFile();
					contr.reset();
					InputStream is;
					try {
						is = new FileInputStream(fichero);
						contr.loadEvents(is);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, e.getMessage());
					}
				} else {
					JOptionPane.showMessageDialog(null,
							"Se ha pulsado cancelar o ha ocurrido un error.");
				}
			}
			
		});
		fcButton.setIcon(new ImageIcon("resources/icons/open.png"));
		
		//ChangeCO2Button
		this.changeCO2Button = new JButton();
		changeCO2Button.setToolTipText("Change CO2 Class of a vehicle");
		changeCO2Button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ChangeCO2ClassDialog dialog = new ChangeCO2ClassDialog(parent, map, time);
				int ret = dialog.open();
				if(ret == 0)
					JOptionPane.showMessageDialog(null,
							"Se ha pulsado cancelar o ha ocurrido un error.");
				else
					if(dialog.getNewEvent() != null)
						contr.addEvent(dialog.getNewEvent());
			}
			
		});
		changeCO2Button.setIcon(new ImageIcon("resources/icons/co2class.png"));
		
		//ChangeWeatherDialog
		weatherButton = new JButton();
		weatherButton.setToolTipText("Change Weather of a Road");
		weatherButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ChangeWeatherDialog dialog = new ChangeWeatherDialog(parent, map, time);
				int ret = dialog.open();
				if(ret == 0)
					JOptionPane.showMessageDialog(null,
							"Se ha pulsado cancelar o ha ocurrido un error.");
				else
					if(dialog.getNewEvent() != null)
						contr.addEvent(dialog.getNewEvent());
			}
			
		});
		weatherButton.setIcon(new ImageIcon("resources/icons/weather.png"));
		
		//TicksSpinner
		this.ticksSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 10000, 1));
		ticksSpinner.setToolTipText("Simulation tick to run: 1-10000");
		ticksSpinner.setMaximumSize(new Dimension(10, 100));
		
		//RunButton y TicksSpinner	
		this.runButton = new JButton();
		runButton.setToolTipText("Run the simulator");
		runButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int ticks = (int) ticksSpinner.getValue();
				enableToolBar(false);
				_stopped = false;
				run_sim(ticks);
			}
			
		});
		runButton.setIcon(new ImageIcon("resources/icons/run.png"));
		
		//StopButton
		this.stopButton = new JButton();
		stopButton.setToolTipText("Stop the simulator");
		stopButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				stop();
			}
			
		});
		stopButton.setIcon(new ImageIcon("resources/icons/stop.png"));
		
		//ExitButton
		this.exitButton = new JButton();
		exitButton.setToolTipText("Exit the simulator");
		exitButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String buttons[] = {"Yes", "No"};
				int n = JOptionPane.showOptionDialog(null, "Are you sure you want to quit?", "Quit",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, buttons, buttons[0]);
				if(n == 0)
					System.exit(0);
			}
			
		});
		exitButton.setIcon(new ImageIcon("resources/icons/exit.png"));
		
		toolBar.add(fcButton);
		toolBar.addSeparator();
		toolBar.add(changeCO2Button);
		toolBar.add(weatherButton);
		toolBar.addSeparator();
		toolBar.add(runButton);
		toolBar.add(stopButton);
		toolBar.add(new JLabel("Ticks:"));
		toolBar.add(ticksSpinner);
		toolBar.add(Box.createGlue());
		toolBar.add(exitButton);
		
		setVisible(true);
	}
	
	private void run_sim(int n) {
		if (n > 0 && !_stopped) {
			try {
				contr.run(1);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, e.getMessage());
				_stopped = true;
				return;
			}
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					run_sim(n - 1);
				}
			});
		} else {
			enableToolBar(true);
			_stopped = true;	
		}
	}
	
	private void enableToolBar(boolean enable) {
		this.fcButton.setEnabled(enable);
		this.changeCO2Button.setEnabled(enable);
		this.weatherButton.setEnabled(enable);
		this.runButton.setEnabled(enable);
	}
	
	private void stop() {
		_stopped = true;
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		this.time = time;
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		this.map = map;
	}

	@Override
	public void onError(String err) {}

}
