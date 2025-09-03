package simulator.launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import simulator.control.Controller;
import simulator.factories.*;
import simulator.model.*;
import simulator.view.MainWindow;

public class Main {

	private final static Integer _timeLimitDefaultValue = 10;
	private static String _inFile = null;
	private static String _outFile = null;
	private static Factory<Event> _eventsFactory = null;
	private static int _ticks;
	private static enum _modes {GUI, BATCH};
	private static _modes _mode;

	private static void parseArgs(String[] args) {

		// define the valid command line options
		//
		Options cmdLineOptions = buildOptions();

		// parse the command line as provided in args
		//
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);
			parseModeOption(line);
			parseHelpOption(line, cmdLineOptions);
			parseInFileOption(line);
			parseOutFileOption(line);
			parseTicksOption(line);
			// if there are some remaining arguments, then something wrong is
			// provided in the command line!
			//
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}

	}

	private static Options buildOptions() {
		Options cmdLineOptions = new Options();

		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Events input file").build());
		cmdLineOptions.addOption(
				Option.builder("o").longOpt("output").hasArg().desc("Output file, where reports are written.").build());
		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message").build());
		cmdLineOptions.addOption(
				Option.builder("t").longOpt("ticks").hasArg()
				.desc("Ticks to the simulator's main loop (default value is 10).").build());
		cmdLineOptions.addOption(Option.builder("m").longOpt("mode").hasArg().desc("Mode").build());
		return cmdLineOptions;
	}
	
	private static void parseModeOption(CommandLine line) {
		if (line.hasOption("m")) {
			if (line.getOptionValue("m").equals("gui"))
				Main._mode = _modes.GUI;
			else
				Main._mode = _modes.BATCH;
				
		}
		else
			Main._mode = _modes.GUI;
	}

	private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}

	private static void parseInFileOption(CommandLine line) throws ParseException {
		if (Main._mode.equals(_modes.BATCH)) {
			_inFile = line.getOptionValue("i");
			if (_inFile == null) {
				throw new ParseException("An events file is missing");
			}
		}
		else {
			if (line.hasOption("i")) {
				_inFile = line.getOptionValue("i");
			}
		}
	}

	private static void parseOutFileOption(CommandLine line) throws ParseException {
		_outFile = line.getOptionValue("o");
	}

	private static void parseTicksOption(CommandLine line) throws ParseException {
		Integer ticks;
		try {
			ticks = Integer.parseInt(line.getOptionValue("t", _timeLimitDefaultValue.toString()));	
			_ticks = ticks;
		}
		catch(NumberFormatException nfe) {
			throw new ParseException("Ticks must be a number");
		}
	}
	
	private static void initFactories() {
		ArrayList<Builder<LightSwitchingStrategy>> lsbs = new ArrayList<>();
		lsbs.add( new RoundRobinStrategyBuilder() );
		lsbs.add( new MostCrowdedStrategyBuilder() );
		Factory<LightSwitchingStrategy> lssFactory = new BuilderBasedFactory<>(lsbs);
		
		ArrayList<Builder<DequeuingStrategy>> dqbs = new ArrayList<>();
		dqbs.add( new MoveFirstStrategyBuilder() );
		dqbs.add( new MoveAllStrategyBuilder() );
		Factory<DequeuingStrategy> dqsFactory = new BuilderBasedFactory<>(dqbs);
		
		ArrayList<Builder<Event>> ebs = new ArrayList<>();
		ebs.add(new NewJunctionEventBuilder(lssFactory,dqsFactory));
		ebs.add(new NewCityRoadEventBuilder());
		ebs.add(new NewInterCityRoadEventBuilder());
		ebs.add(new NewVehicleEventBuilder());
		ebs.add(new SetContClassEventBuilder());
		ebs.add(new SetWeatherEventBuilder());
		
		_eventsFactory = new BuilderBasedFactory<Event>(ebs);
	}

	private static void startBatchMode() throws IOException {
		InputStream is = new FileInputStream(new File(_inFile));
		OutputStream os;
		
		if(_outFile == null) os = System.out;
		else os = new FileOutputStream(new File(_outFile));
		
		Controller controller = new Controller(new TrafficSimulator(), _eventsFactory);
		controller.loadEvents(is);
		controller.run(_ticks, os);
	}
	
	private static void startGUIMode() throws IOException {
		Controller controller = new Controller(new TrafficSimulator(), _eventsFactory);
		if (_inFile != null)
		{
			InputStream is = new FileInputStream(new File(_inFile));
			controller.loadEvents(is);
		}
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new MainWindow(controller);
			}
			
		});
		
	}

	private static void start(String[] args) throws IOException {
		Main.initFactories();
		Main.parseArgs(args);
		if (Main._mode == Main._modes.BATCH) Main.startBatchMode();
		else Main.startGUIMode();
	}

	// example command lines:
	//
	// -i resources/examples/ex1.json
	// -i resources/examples/ex1.json -t 300
	// -i resources/examples/ex1.json -o resources/tmp/ex1.out.json
	// --help

	public static void main(String[] args) {
		try {
			Main.start(args);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
