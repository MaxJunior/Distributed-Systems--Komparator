package org.komparator.mediator.ws;

import java.util.TimerTask;

import org.komparator.mediator.ws.cli.MediatorClient;
import org.komparator.mediator.ws.cli.MediatorClientException;

public class LifeProof extends TimerTask {
	
	String  wsURL  = null ;
	public LifeProof(String wsURL){
		this.wsURL = wsURL ;
	}

	@Override
	public void run() {

		
		//	System.out.println("pRIMARIO");
			MediatorClient mediatorClient  = null;
			try {
				mediatorClient = new MediatorClient("http://localhost:8072/mediator-ws/endpoint");
			} catch (MediatorClientException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	//		System.out.println("pRIMAR2");
			
			try {
				mediatorClient.imAlive();
	//			System.out.println("pRIMAR3");
			}catch(Exception e){System.out.println("Error : " + e);  }
			
		
	}
	
	
	
	
	
}
