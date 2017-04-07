package org.komparator.mediator.ws;



import java.util.Collection;
import java.util.List;

import javax.jws.WebService;

import org.komparator.supplier.ws.cli.SupplierClient;
import org.komparator.supplier.ws.cli.SupplierClientException;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINamingException;

@WebService(
		endpointInterface = "org.komparator.mediator.ws.MediatorPortType", 
		wsdlLocation = "mediator.1_0.wsdl", 
		name = "MediatorWebService", 
		portName = "MediatorPort", 
		targetNamespace = "http://ws.mediator.komparator.org/", 
		serviceName = "MediatorService"
)

public class MediatorPortImpl implements MediatorPortType{

	// end point manager
	private MediatorEndpointManager endpointManager;
	private final String SUPPLIER_SERVER_NAME = "A74_Supplier";

	public MediatorPortImpl(MediatorEndpointManager endpointManager) {
		this.endpointManager = endpointManager;
	}

	
	public int  getNumberOfCurrentSuppliers(){
		Collection<String> listOfURL = null ;
		try {
		     listOfURL =endpointManager.getUddiNaming().list(SUPPLIER_SERVER_NAME+"%");
		} catch (UDDINamingException e1) {
			System.out.println("Error : UDDI_NAMING IS INVALID OR INEXISTENT");
		}

    //   System.out.println("List of URL :  " + listOfURL.toString());
       
       int numbOfSuppliers = listOfURL.size();
		return numbOfSuppliers ;
	}
    
	// Main operations -------------------------------------------------------

    // TODO
	
    
	// Auxiliary operations --------------------------------------------------	
	
	
	
	@Override
	public String ping(String arg0) {
	//	SupplierClient cli =  new SupplierClient(arg0);
		
		System.out.println("BEFORE PING " );
		SupplierClient client;
		
		
	   
	    int numbOfSuppliers = getNumberOfCurrentSuppliers();
        String result= "[ ";
		for(int urlId = 1 ; urlId <= numbOfSuppliers; urlId++  ) {
			try {
				client = new SupplierClient(endpointManager.getUddiURL(),SUPPLIER_SERVER_NAME + urlId);
				String currentResult = client.ping(arg0);      
				System.out.println("AFTER PING " );
				if(currentResult == null){
					return "Error in ping method";
				}
				
				result +=" " + SUPPLIER_SERVER_NAME + urlId +" ping result: " + currentResult +" ; ";
			} catch (SupplierClientException e) {
				client=null;
				System.out.println("ERROR : FAIL CREATING SUPPLIER CLIENT");
			}
			
		
		}
		result += " ]";
		System.out.println("RETURNED VALUE :  " + result);
		
		return   result;
		
	}


	@Override
	public void clear() {
		SupplierClient client;
		int numbOfSuppliers = getNumberOfCurrentSuppliers();
   
		for(int urlId = 1 ; urlId <= numbOfSuppliers; urlId++  ) {
			try {
				client = new SupplierClient(endpointManager.getUddiURL(),SUPPLIER_SERVER_NAME + urlId);
				 client.clear(); 
			} catch (SupplierClientException e) {
				client=null;
				System.out.println("ERROR : FAIL TO CLEAR SUPPLIER CLIENT");
			}     
		}
		
	}


	@Override
	public List<ItemView> getItems(String productId) throws InvalidItemId_Exception {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<CartView> listCarts() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<ItemView> searchItems(String descText) throws InvalidText_Exception {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ShoppingResultView buyCart(String cartId, String creditCardNr)
			throws EmptyCart_Exception, InvalidCartId_Exception, InvalidCreditCard_Exception {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void addToCart(String cartId, ItemIdView itemId, int itemQty) throws InvalidCartId_Exception,
			InvalidItemId_Exception, InvalidQuantity_Exception, NotEnoughItems_Exception {
		// TODO Auto-generated method stub
		
	}


	@Override
	public List<ShoppingResultView> shopHistory() {
		// TODO Auto-generated method stub
		return null;
	}

	


	
	// View helpers -----------------------------------------------------
	
    // TODO

    
	// Exception helpers -----------------------------------------------------

    // TODO

}
