package simulator.control;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.json.*;

import simulator.factories.Factory;
import simulator.model.Event;
import simulator.model.TrafficSimObserver;
import simulator.model.TrafficSimulator;

public class Controller {
	
	private TrafficSimulator sim;
	private Factory<Event> eventsFactory;
	
	public Controller(TrafficSimulator sim, Factory<Event> eventsFactory) {
		if(sim == null)
			throw new IllegalArgumentException("sim must be different from null");
		if(eventsFactory == null)
			throw new IllegalArgumentException("eventsFactory must be different from null");
		this.sim = sim;
		this.eventsFactory = eventsFactory;
	}
	
	public void loadEvents(InputStream in) {
		JSONObject jo = new JSONObject(new JSONTokener(in));
		try {
			if(!jo.has("events"))
				throw new IllegalArgumentException("The JSONObject must contain the key \"events\"");
			//Tomamos el array asociado a la clave events y creamos la instancia de cada evento del array
			JSONArray ja = jo.getJSONArray("events");
			for(int i = 0; i < ja.length(); i++) {
				Event e = eventsFactory.createInstance(ja.getJSONObject(i));
				sim.addEvent(e);
			}
		}
		catch(Exception e) {
			sim.onError(e.getMessage());
			throw e;
		}
		
	}
	
	public void run(int n, OutputStream out) {
		PrintStream p = new PrintStream(out);
		p.print("{ \"states\": [");
		for(int i = 0; i < n; i++) {
			sim.advance();
			p.print(sim.report().toString());
			p.println(",");
		}
		p.println("] }");
	}
	
	public void run(int n) {
		for(int i = 0; i < n; i++) {
			sim.advance();
		}
	}
	
	public void reset() {
		sim.reset();
	}
	
	public void addObserver(TrafficSimObserver o) {
		this.sim.addObsever(o);
	}
	
	public void removerObserver(TrafficSimObserver o) {
		this.sim.removeObserver(o);
	}
	
	public void addEvent(Event e) {
		this.sim.addEvent(e);
	}
	
}
