package org.komparator.mediator.ws.cli;

import java.util.List;

import org.komparator.mediator.ws.CartView;
import org.komparator.mediator.ws.EmptyCart_Exception;
import org.komparator.mediator.ws.InvalidCartId_Exception;
import org.komparator.mediator.ws.InvalidCreditCard_Exception;
import org.komparator.mediator.ws.ItemIdView;
import org.komparator.mediator.ws.ShoppingResultView;
import org.komparator.supplier.ws.ProductView;
import org.komparator.supplier.ws.cli.SupplierClient;

public class MediatorClientApp {

    public static void main(String[] args) throws Exception {
        // Check arguments
        if (args.length == 0) {
            System.err.println("Argument(s) missing!");
            System.err.println("Usage: java " + MediatorClientApp.class.getName()
                    + " wsURL OR uddiURL wsName");
            return;
        }
        String uddiURL = null;
        String wsName = null;
        String wsURL = null;
        if (args.length == 1) {
            wsURL = args[0];
        } else if (args.length >= 2) {
            uddiURL = args[0];
            wsName = args[1];
        }

        // Create client
        MediatorClient client = null;

        if (wsURL != null) {
            System.out.printf("Creating client for server at %s%n", wsURL);
            client = new MediatorClient(wsURL);
        } else if (uddiURL != null) {
            System.out.printf("Creating client using UDDI at %s for server with name %s%n",
                uddiURL, wsName);
            client = new MediatorClient(uddiURL, wsName);
        }
        
        
/*
        System.out.println("Invoke ping()...");
  
        String result = client.ping("client");*/
  
        
        
        
   //------------------ addToCart and buyCart  -------------// 


    	 MediatorClient mediatorClient;
         SupplierClient supplierClient;
    	 SupplierClient supplierClient2;

		String uddiURL1 = "http://localhost:9090";
		String wsName1 = "A74_Mediator";

		mediatorClient = new MediatorClient(uddiURL1, wsName1);

		supplierClient = new SupplierClient(uddiURL1, "A74_Supplier" + 1);
		supplierClient2 = new SupplierClient(uddiURL1, "A74_Supplier" + 2);
        
        
		supplierClient.clear();
		supplierClient2.clear();
		mediatorClient.clear();
		
		
		
		ItemIdView idViewTest = new ItemIdView();
		idViewTest.setProductId("X1");
		idViewTest.setSupplierId("A74_Supplier1");
		
		CartView cart = new CartView();
		cart.setCartId("carrinho1");
		
		CartView cart2 = new CartView();
		cart2.setCartId("carrinho2");

				{
					ProductView product = new ProductView();
					product.setId("X1");
					product.setDesc("Basket");
					product.setPrice(10000);
					product.setQuantity(10);
					supplierClient.createProduct(product);
				}
				{
					ProductView product = new ProductView();
					product.setId("Y2");
					product.setDesc("Base");
					product.setPrice(20);
					product.setQuantity(20);
					supplierClient.createProduct(product);
				}
				{
					ProductView product = new ProductView();
					product.setId("Z3");
					product.setDesc("Soccer ball");
					product.setPrice(60);
					product.setQuantity(30);
					supplierClient.createProduct(product);
				}
				{
					ProductView product = new ProductView();
					product.setId("K6");
					product.setDesc("soft");
					product.setPrice(36);
					product.setQuantity(30);
					supplierClient.createProduct(product);
				}
				
				
				////////////////
				
				{
					ProductView product = new ProductView();
					product.setId("X1");
					product.setDesc("Basket ball");
					product.setPrice(30);
					product.setQuantity(10);
					supplierClient2.createProduct(product);
				}
				{
					ProductView product = new ProductView();
					product.setId("Y3");
					product.setDesc("Base");
					product.setPrice(20);
					product.setQuantity(20);
					supplierClient2.createProduct(product);
				}
				{
					ProductView product = new ProductView();
					product.setId("Z3");
					product.setDesc("Soccer ball");
					product.setPrice(10);
					product.setQuantity(30);
					supplierClient2.createProduct(product);
				}
		
		
		
        
        
        
				ItemIdView idView1 = new ItemIdView();
		    	idView1.setProductId("X1");
		    	idView1.setSupplierId("A74_Supplier1");
		    	
		    	ItemIdView idView2 = new ItemIdView();
		    	idView2.setProductId("Y3");
		    	idView2.setSupplierId("A74_Supplier2");
		    	
		    	List<CartView> cartList;
		    	ShoppingResultView shoppingResult = null;
		    	
		    	
		    	
		    	try {
		    		mediatorClient.addToCart(cart.getCartId(), idView1, 2);
		    		mediatorClient.addToCart(cart2.getCartId(), idView2, 2);
		    		
				} catch (Exception e) {
					//fail();
				}
		    	
		    	
		    	try {
		    		shoppingResult = mediatorClient.buyCart(cart.getCartId(), "4556648855991861");
		    		
		    		shoppingResult = mediatorClient.buyCart(cart2.getCartId(), "4556648855991861");
				} catch (EmptyCart_Exception | InvalidCartId_Exception | InvalidCreditCard_Exception e) {
					e.printStackTrace();
				}

        
        
        
        

    }
}
