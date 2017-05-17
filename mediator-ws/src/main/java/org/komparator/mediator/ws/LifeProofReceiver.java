package org.komparator.mediator.ws;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.komparator.mediator.ws.cli.MediatorClient;
import org.komparator.mediator.ws.cli.MediatorClientException;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINamingException;

public class LifeProofReceiver extends TimerTask {
	
	MediatorEndpointManager _endpoint;
	Timer timer;
	
	public LifeProofReceiver(MediatorEndpointManager endpoint, Timer tim){
		_endpoint=endpoint;
		timer=tim;
	}

	@Override
	public void run() {

		if(_endpoint.getItsAlive()){
			System.out.println("Primary server is alive, do not substitute");
			
		}else{
			System.out.println("Primary server is not responding... Replacing...");
			
			try {
				//UDDINaming uddiNaming = new UDDINaming("http://localhost:9090");
				//uddiNaming.rebind("A74_Mediator", "http://localhost:8072/mediator-ws/endpoint");
				_endpoint.publishSecondaryServerToUDDI("A74_Mediator" ,"http://localhost:9090");
				_endpoint.setPrimary(true);
			} catch (Exception e) {
				System.out.println("error rebinding secundary server");;
				e.printStackTrace();
			}
			
			timer.cancel();
	        timer.purge();
			
			
		}
		
		
		
		
		_endpoint.setItsAlive(false);
	}
	
	
	
}
