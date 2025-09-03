package simulator.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;

public class MapByRoadComponent extends JPanel implements TrafficSimObserver {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final int _JRADIUS = 10;

	private static final Color _BG_COLOR = Color.WHITE;
	private static final Color _JUNCTION_COLOR = Color.BLUE;
	private static final Color _JUNCTION_LABEL_COLOR = new Color(200, 100, 0);
	private static final Color _GREEN_LIGHT_COLOR = Color.GREEN;
	private static final Color _RED_LIGHT_COLOR = Color.RED;
	
	private Controller contr;
	private RoadMap map;
	
	private Image car;
	
	MapByRoadComponent(Controller contr) {
		this.contr = contr;
		initGUI();
		this.contr.addObserver(this);
	}
	
	private void initGUI() {
		car = loadImage("car.png");
	}
	
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		g.setColor(_BG_COLOR);
		g.clearRect(0, 0, getWidth(), getHeight());

		if (map == null || map.getJunctions().size() == 0) {
			g.setColor(Color.red);
			g.drawString("No map yet!", getWidth() / 2 - 50, getHeight() / 2);
		} else {
			drawMap(g);
		}
		
	}
	
	private void drawMap(Graphics g) {
		drawRoads(g);
	}
	
	private void drawRoads(Graphics g) {
		List<Road> roads = map.getRoads();
		for(int i = 0; i < roads.size(); i++) {
			int x1 = 50;
			int x2 = getWidth() - 100;
			int y = (i + 1) * 50;
			
			g.setColor(Color.BLACK);
			g.drawLine(x1, y, x2, y);
			
			g.setColor(_JUNCTION_COLOR);
			g.fillOval(x1 - _JRADIUS / 2, y - _JRADIUS / 2, _JRADIUS, _JRADIUS);
			
			Color destJuncColor = _RED_LIGHT_COLOR;
			int idx = roads.get(i).getDest().getGreenLightIndex();
			if (idx != -1 && roads.get(i).equals(roads.get(i).getDest().getInRoads().get(idx))) {
				destJuncColor = _GREEN_LIGHT_COLOR;
			}
			
			g.setColor(destJuncColor);
			g.fillOval(x2 - _JRADIUS / 2, y - _JRADIUS / 2, _JRADIUS, _JRADIUS);
			for(Vehicle v : roads.get(i).getVehicles()) {
				int x = x1 + (int) ((x2 - x1) * ((double) v.getLocation() / (double) roads.get(i).getLength()));
				g.drawImage(car, x, y - 6, 12, 12, this);
				int vLabelColor = (int) (25.0 * (10.0 - (double) v.getContClass()));
				g.setColor(new Color(0, vLabelColor, 0));
				g.drawString(v.getId(), x, y - 6);
			}
			g.setColor(Color.BLACK);
			g.drawString(roads.get(i).getId(), x1 - 35, y + 6);
			g.setColor(_JUNCTION_LABEL_COLOR);
			g.drawString(roads.get(i).getSrc().getId(), x1, y - 10);
			g.drawString(roads.get(i).getDest().getId(), x2, y - 10);
			g.drawImage(roadWeatherImage(roads.get(i)), x2 + 6, y - 16, 32, 32, this);
			g.drawImage(roadContImage(roads.get(i)), x2 + 44, y - 16, 32, 32, this);
		}
	}
	
	private Image loadImage(String img) {
		Image i = null;
		try {
			return ImageIO.read(new File("resources/icons/" + img));
		} 
		catch (IOException e) {
		}
		return i;
	}

	private Image roadWeatherImage(Road r) {
		Image image = loadImage("sun.png");	//Para que eclipse no se queje
		switch(r.getWeather()) {
		case SUNNY:
			image = loadImage("sun.png");
			break;
		case CLOUDY:
			image = loadImage("cloud.png");
			break;
		case RAINY:
			image = loadImage("rain.png");
			break;
		case WINDY:
			image = loadImage("wind.png");
			break;
		case STORM:
			image = loadImage("storm.png");
			break;
		}
		return image;
	}
	
	private Image roadContImage(Road r) {
		int c = (int) Math.floor(Math.min((double) r.getTotalCO2() / (1.0 + (double) r.getContLimit()), 1.0) / 0.19);
		return loadImage(String.format("cont_%d.png", c));
	}
	
	private void update(RoadMap map) {
		this.map = map;
		repaint();
	}
	
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		update(map);
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onError(String err) {}

}
