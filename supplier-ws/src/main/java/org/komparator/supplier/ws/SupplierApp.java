package org.komparator.supplier.ws;

import javax.xml.ws.Endpoint;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;

/** Main class that starts the Supplier Web Service. */
public class SupplierApp {

	public static void main(String[] args) throws Exception {
		// Check arguments
		if (args.length < 3) {
			System.err.println("Argument(s) missing!");
			System.err.println("Usage: java " + SupplierApp.class.getName() + " uddiURL wsName");
			return;
		}
		
		String uddiURL = args[0];
		String name = args[1]; 
		
		//TODO
		String wsURL = args[2];  // change SupplierEndPointManager to receive just uddiURL and wsURL
		

		// Create server implementation object
		SupplierEndpointManager endpoint = new SupplierEndpointManager(uddiURL, name, wsURL);
		try {
			endpoint.start();
			endpoint.awaitConnections();
		} finally {
			endpoint.stop();
		}

	}

}
