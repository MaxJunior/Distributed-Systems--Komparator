package org.komparator.mediator.ws;

import java.util.Timer;

import org.komparator.mediator.ws.cli.MediatorClient;

public class MediatorApp {

	public static void main(String[] args) throws Exception {
		// Check arguments
		if (args.length == 0 || args.length == 2) {
			System.err.println("Argument(s) missing!");
			System.err.println("Usage: java " + MediatorApp.class.getName() + " wsURL OR uddiURL wsName wsURL");
			return;
		}
		String uddiURL = null;
		String wsName = null;
		String wsURL = null;
	    int numOfSeconds = 5 ;

		// Create server implementation object, according to options
		MediatorEndpointManager endpoint = null;
		if (args.length == 1) {
			wsURL = args[0];
			endpoint = new MediatorEndpointManager(wsURL);
		} else if (args.length >= 3) {
			uddiURL = args[0];
			wsName = args[1];
			wsURL = args[2];
			endpoint = new MediatorEndpointManager(uddiURL, wsName, wsURL);
			endpoint.setVerbose(true);
		}

		try {
			endpoint.start();
			if(wsURL.contains("8071")){
				LifeProof proofLife = new LifeProof(wsURL);			
				Timer timer = new Timer(/*isDaemon*/ true);
				timer.schedule(proofLife, /*delay*/ 0 * 1000, /*period*/ numOfSeconds * 1000);
			}
			endpoint.awaitConnections();
		} finally {
			endpoint.stop();
		}
        
	}
	

}
