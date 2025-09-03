package simulator.view;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import simulator.control.Controller;

public class MainWindow extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Controller ctrl;
	private Border defaultBorder = BorderFactory.createLineBorder(Color.BLACK, 1);
	
	private JPanel mainPanel;
	private JPanel viewsPanel;
	private JPanel tablesPanel;
	private JPanel mapsPanel;
	
	private JPanel eventsView;
	private JPanel vehiclesView;
	private JPanel roadsView;
	private JPanel junctionsView;
	private JPanel mapView;
	private JPanel mapByRoadView;
	
	public MainWindow(Controller ctrl) {
		super("Traffic Simulator");
		this.ctrl = ctrl;
		initGUI();
	}
	
	private void initGUI() {
		
		this.mainPanel = new JPanel(new BorderLayout());
		this.setContentPane(mainPanel);
		mainPanel.add(new ControlPanel(ctrl), BorderLayout.PAGE_START);
		mainPanel.add(new StatusBar(ctrl),BorderLayout.PAGE_END);
		
		this.viewsPanel = new JPanel(new GridLayout(1, 2));
		mainPanel.add(viewsPanel, BorderLayout.CENTER);
		
		this.tablesPanel = new JPanel();
		tablesPanel.setLayout(new BoxLayout(tablesPanel, BoxLayout.Y_AXIS));
		viewsPanel.add(tablesPanel);
		
		this.mapsPanel = new JPanel();
		mapsPanel.setLayout(new BoxLayout(mapsPanel, BoxLayout.Y_AXIS));
		viewsPanel.add(mapsPanel);
		
		// tables
		
		//EventsTable
		this.eventsView =	createViewPanel(new JTable(new EventsTableModel(ctrl)), "Events");
		tablesPanel.add(eventsView);
		
		//VehiclesTable
		this.vehiclesView = createViewPanel(new JTable(new VehiclesTableModel(ctrl)), "Vehicles");
		tablesPanel.add(vehiclesView);
		
		//RoadsTable
		this.roadsView = createViewPanel(new JTable(new RoadsTableModel(ctrl)), "Roads");
		tablesPanel.add(roadsView);
		
		//JunctionsTable
		this.junctionsView = createViewPanel(new JTable(new JunctionsTableModel(ctrl)), "Junctions");
		tablesPanel.add(junctionsView);
		
		// maps
		this.mapView = createViewPanel(new MapComponent(ctrl), "Map");
		mapView.setPreferredSize(new Dimension(500, 400));
		mapsPanel.add(mapView);
		
		this.mapByRoadView = createViewPanel(new MapByRoadComponent(ctrl), "Map by Road");
		mapByRoadView.setPreferredSize(new Dimension(500, 400));
		mapsPanel.add(mapByRoadView);
		
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null);	//Se pone en el centro de la pantalla
		this.setVisible(true);
	}
	
	private JPanel createViewPanel(JComponent c, String title) {
		JPanel p = new JPanel( new BorderLayout() );
		p.setBorder(BorderFactory.createTitledBorder(defaultBorder, title,
				TitledBorder.LEFT, TitledBorder.TOP));
		p.setPreferredSize(new Dimension(500, 200));
		p.add(new JScrollPane(c));
		return p;
	}
}
