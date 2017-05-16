package org.komparator.supplier.ws.cli;

import javax.jws.HandlerChain;

//@HandlerChain(file = "hello_handler-chain-binding.xml")

/** Main class that starts the Supplier Web Service client. */
public class SupplierClientApp {

	public static void main(String[] args) throws Exception {
		// Check arguments
		if (args.length < 2) {
			System.err.println("Argument(s) missing!");
			System.err.println("Usage: java " + SupplierClientApp.class.getName() + " wsURL");
			return;
		}
		
		String uddiURL = args[0];
		String name = args[1] + "1"; // TO BE CHANGED : THE INCREMENT
		

		// Create client
		System.out.printf("Creating client for server at %s and %s%n", uddiURL,name);
		SupplierClient client = new SupplierClient(uddiURL,name);

		// the following remote invocations are just basic examples
		// the actual tests are made using JUnit

		System.out.println("Invoke ping()...");
		String result = client.ping("client");
		System.out.print("Result: ");
		System.out.println(result);
	}

}
