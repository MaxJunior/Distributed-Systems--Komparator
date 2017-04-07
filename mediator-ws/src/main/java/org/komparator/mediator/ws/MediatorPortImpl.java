package org.komparator.mediator.ws;



import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.jws.WebService;

import org.komparator.supplier.ws.BadProductId_Exception;
import org.komparator.supplier.ws.BadText_Exception;
import org.komparator.supplier.ws.ProductView;
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
	private List<CartView> listCartView = new ArrayList<CartView>();

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
		
		listCartView.clear();
		
	}

    public ItemView  transformProdViewToItemView(ProductView prod, String supplierId){
    	ItemView item = new ItemView();
    	ItemIdView itemIdView = new ItemIdView();
    	
    	item.setDesc(prod.getDesc());
    	
    	itemIdView.setProductId(prod.getId());
    	itemIdView.setSupplierId(supplierId);
    	item.setItemId(itemIdView);
    	
    	item.setPrice(prod.getPrice());
    	
    	return item;
    	
    }
    
     
    public void orderItemViewListByPrice(List<ItemView> itemViewList){
    	
    	Collections.sort(itemViewList, new Comparator(){
    		
    		public int compare(Object item1,Object item2){
    			Integer price1 = ((ItemView)item1 ).getPrice();
    			Integer price2 = ((ItemView)item2 ).getPrice();
    			
    			return price1.compareTo(price2);
    		}
    	});
    	
    	
    }
    
   public void orderItemViewListByIdandPrice(List<ItemView> itemViewList){
    	
    	Collections.sort(itemViewList, new Comparator(){
    		
    		public int compare(Object item1,Object item2){
    			
    			String x1 = ((ItemView) item1).getItemId().getProductId();
    			String x2 = ((ItemView) item2).getItemId().getProductId();
    			int sComp = x1.compareTo(x2);
    			
    			if(sComp != 0){ 
    				return sComp;
    			}else {
    				Integer price1 = ((ItemView)item1 ).getPrice();
        			Integer price2 = ((ItemView)item2 ).getPrice();
        			
        			return price1.compareTo(price2);
    			}
    			
    		}
    	});
    	
    	
    }
    
	@Override
	public List<ItemView> getItems(String productId) throws InvalidItemId_Exception {
		
		List<ItemView> items = new ArrayList<ItemView>();
		SupplierClient client;
		int numbOfSuppliers = getNumberOfCurrentSuppliers();
        System.out.println("Number of suppliers : " + numbOfSuppliers);
        
		for(int urlId = 1 ; urlId <= numbOfSuppliers; urlId++ ) {
			try {
				client = new SupplierClient(endpointManager.getUddiURL(),SUPPLIER_SERVER_NAME + urlId);
				ProductView prodView = client.getProduct(productId);
				if(prodView != null)
					items.add(transformProdViewToItemView(prodView,SUPPLIER_SERVER_NAME + urlId)); 
				
			} catch (SupplierClientException e) {
				client=null;
				System.out.println("ERROR : FAIL TO CLEAR SUPPLIER CLIENT");
				
			} catch (BadProductId_Exception e) {
				InvalidItemId invalidItem = new InvalidItemId();
				invalidItem.setMessage(productId);
				throw new InvalidItemId_Exception("Bad productId", invalidItem);
			}
		}
		
		if(items.isEmpty()){
			InvalidItemId invalidItem = new InvalidItemId();
			invalidItem.setMessage(productId);
			throw new InvalidItemId_Exception("Bad productId", invalidItem);
		}
		
		System.out.println("===========BEFORE========== ");
		printItemViewList(items);
		orderItemViewListByPrice(items);
		System.out.println("===========AFTER========== ");
		printItemViewList(items);
		return items;
	}
	
	public void printItemViewList(List<ItemView> itemViewList) {
		System.out.println("===========List of ItemViews ========== ");
		for(ItemView item : itemViewList){
			System.out.println("ItemIdView : =" + item.getItemId().getProductId() + ",\nDescription : " + item.getDesc() 
				+ "\n, Price =" + item.getPrice() + "\n\n");
			
		}
	}


	 public List<ItemView> transformListProdViewToListItemView(List<ProductView> listProd, String supplierId){
	    	
		 List<ItemView> itemViewRet = new ArrayList<ItemView>() ;
		  for(ProductView prodView : listProd){
			 itemViewRet.add(transformProdViewToItemView(prodView,supplierId)) ;
			  
		  }
		  
		  return itemViewRet;
	    }
	
	
	@Override
	public List<ItemView> searchItems(String descText) throws InvalidText_Exception {
		
		List<ItemView> items = new ArrayList<ItemView>();
		SupplierClient client;
		int numbOfSuppliers = getNumberOfCurrentSuppliers();
        System.out.println("Number of suppliers : " + numbOfSuppliers);
        
		for(int urlId = 1 ; urlId <= numbOfSuppliers; urlId++ ) {
			try {
				client = new SupplierClient(endpointManager.getUddiURL(),SUPPLIER_SERVER_NAME + urlId);
				List<ProductView> listProdView = client.searchProducts(descText);
				if(listProdView != null)
					items.addAll(transformListProdViewToListItemView(listProdView,SUPPLIER_SERVER_NAME + urlId)); 
				
			} catch (SupplierClientException e) {
				client=null;
				System.out.println("ERROR : FAIL SUPPLIER CLIENT");
				
			} catch (BadText_Exception e) {
				InvalidText invalidItem = new InvalidText();
				throw new InvalidText_Exception("Invalid Test", invalidItem);
			}
		}
		if(items.isEmpty()){
			InvalidText invalidItem = new InvalidText();
			throw new InvalidText_Exception("Invalid Test", invalidItem);		
		}
		
		orderItemViewListByIdandPrice(items);
		printItemViewList(items);
		
		return items;
	}

	@Override
	public List<CartView> listCarts() {
		return listCartView;
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
		
		if(cartId == null || cartId.trim().equals("")){
			InvalidCartId faultInfo = new InvalidCartId();
			throw new InvalidCartId_Exception("invalid addToCart", faultInfo);
		}
		if(itemQty <= 0){
			InvalidQuantity faultInfo = new InvalidQuantity();
			throw new InvalidQuantity_Exception("invalid addToCart", faultInfo);
		}
		if(itemId == null){
			InvalidItemId faultInfo = new InvalidItemId();
			throw new InvalidItemId_Exception("invalid addToCart", faultInfo);
		}
		
		
		boolean ItemExistsInCart = false;
		CartItemView cartItemView;
		
		boolean cartExists=false;
		
		for(CartView cartView : listCartView){      /// Verificar se carrinho nao existe
			if(cartView.getCartId().equals(cartId)){
				cartExists=true;
			}
		}
		if(!cartExists){     ////criar novo carrinho:
			CartView newCart = new CartView();
			newCart.setCartId(cartId);
			listCartView.add(newCart);
		}
		
		
		for(CartView cartView : listCartView){       //procura carrinho existente
			if(cartView.getCartId().equals(cartId)){
				
				/*CartItemView cartItemToFindInCart = new CartItemView();
				ItemView itemToFindInCart = new ItemView();
				itemToFindInCart.setDesc(value);
				cartItemToFindInCart.setItem(itemToFindInCart);*/
				
				for(CartItemView cartItemViewInCart : cartView.getItems()) { //procura se item ja existe no carrinho
					if(cartItemViewInCart.getItem().getItemId().getProductId().equals(itemId.getProductId())            /////encontra se item existe 
							&&  cartItemViewInCart.getItem().getItemId().getSupplierId().equals(itemId.getSupplierId())){
						
						ItemExistsInCart = true;
						/*if(supplierItemQuantity < (cartItemViewInCart.getQuantity() + itemQty)){
							NotEnoughItems faultInfo = new NotEnoughItems();
							throw new NotEnoughItems_Exception("not enough items", null);
						}*/
						cartItemViewInCart.setQuantity(cartItemViewInCart.getQuantity() + itemQty);    ////aumenta a quantidade
						
						
					}
					
				}
				
				if(!ItemExistsInCart){                       ////caso o item nao exista no carrinho criar novo cartItemView:
					
					cartItemView = new CartItemView();
					
					ItemView newItem = new ItemView();
					
					List<ItemView> listItemView = new ArrayList<ItemView>();
					listItemView = getItems(itemId.getProductId());
					
					for(ItemView item : listItemView){       /////  procurar na lista de items de todos os fornecedores o item do fornecedor pedido (para ver o preco por exemplo)
						if(item.getItemId().getSupplierId().equals(itemId.getSupplierId())){
							newItem.setDesc(item.getDesc());
							newItem.setItemId(item.getItemId());
							newItem.setPrice(item.getPrice());
						}
					}
					
					cartItemView.setItem(newItem);
					cartItemView.setQuantity(itemQty);
					cartView.getItems().add(cartItemView);
				
				}
			}
		} 
		
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
