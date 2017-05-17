package org.komparator.mediator.ws.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.komparator.mediator.ws.CartView;
import org.komparator.mediator.ws.EmptyCart_Exception;
import org.komparator.mediator.ws.InvalidCartId_Exception;
import org.komparator.mediator.ws.InvalidCreditCard_Exception;
import org.komparator.mediator.ws.InvalidItemId_Exception;
import org.komparator.mediator.ws.InvalidQuantity_Exception;
import org.komparator.mediator.ws.ItemIdView;
import org.komparator.mediator.ws.NotEnoughItems_Exception;
import org.komparator.mediator.ws.Result;
import org.komparator.mediator.ws.ShoppingResultView;
import org.komparator.supplier.ws.BadProductId_Exception;
import org.komparator.supplier.ws.BadProduct_Exception;
import org.komparator.supplier.ws.ProductView;

public class BuyCartIT extends BaseIT{
	

	static CartView cart;
	ItemIdView idViewTest;
	
		
	@BeforeClass
	public static void oneTimeSetUp() throws BadProductId_Exception, BadProduct_Exception {
		   
				 
	}
	
	
	
	
	@AfterClass
	public static void oneTimeTearDown() {
		// clear remote service state after all tests
		supplierClient.clear();
		supplierClient2.clear();
	}
	
	@Before
	public void setup() throws BadProductId_Exception, BadProduct_Exception{
		
		// clear remote service state before all tests
				supplierClient.clear();
				supplierClient2.clear();
				mediatorClient.clear();
				
				
				
				idViewTest = new ItemIdView();
				idViewTest.setProductId("X1");
				idViewTest.setSupplierId("A74_Supplier1");
				
				cart = new CartView();
				cart.setCartId("carrinho1");

						// fill-in test products
						// (since getProduct is read-only the initialization below
						// can be done once for all tests in this suite)
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
		
		
	}

	
	
	@Test(expected = InvalidCartId_Exception.class)
	public void getCartNullTest() throws EmptyCart_Exception, InvalidCartId_Exception, InvalidCreditCard_Exception {
		mediatorClient.buyCart(cart.getCartId(), "4556648855991861");
	}
	
	@Test(expected = InvalidCreditCard_Exception.class)
	public void getWrongCard() throws EmptyCart_Exception, InvalidCartId_Exception, InvalidCreditCard_Exception {
		mediatorClient.buyCart(cart.getCartId(), "errado");
	}
	
	
	
	@Test(expected = InvalidCartId_Exception.class)
	public void testNullCart() throws EmptyCart_Exception, InvalidCartId_Exception, InvalidCreditCard_Exception {
		mediatorClient.buyCart(null, "4556648855991861");
	}
	@Test(expected = InvalidCartId_Exception.class)
	public void testEmptyCartId() throws EmptyCart_Exception, InvalidCartId_Exception, InvalidCreditCard_Exception {
		mediatorClient.buyCart("", "4556648855991861");
	}
	@Test(expected = InvalidCartId_Exception.class)
	public void testInexistingCart() throws EmptyCart_Exception, InvalidCartId_Exception, InvalidCreditCard_Exception {
		mediatorClient.buyCart("xyz", "4556648855991861");
	}
	
	
    
    @Test
    public void sucessBuyExistingCart(){
    	
    	
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
    		mediatorClient.addToCart(cart.getCartId(), idView2, 3);
    		
		} catch (Exception e) {
			fail();
		}
    	
    	
    	try {
    		shoppingResult = mediatorClient.buyCart(cart.getCartId(), "4556648855991861");
		} catch (EmptyCart_Exception | InvalidCartId_Exception | InvalidCreditCard_Exception e) {
			fail();
			e.printStackTrace();
		}
    	assertEquals(Result.COMPLETE,shoppingResult.getResult());
    	assertEquals(2,shoppingResult.getPurchasedItems().size());
    	assertEquals(0,shoppingResult.getDroppedItems().size());
    	assertEquals(20060,shoppingResult.getTotalPrice());
    	
    }
    
    
    
    @Test
    public void sucessBuyPartial() throws InvalidCartId_Exception, InvalidItemId_Exception, InvalidQuantity_Exception, NotEnoughItems_Exception, EmptyCart_Exception, InvalidCreditCard_Exception{
    	
    	
    	{
			ItemIdView id = new ItemIdView();
			id.setProductId("X1");
			id.setSupplierId("A74_Supplier1");
			mediatorClient.addToCart("xyz", id, 2);
			mediatorClient.addToCart("otherXyz", id, 2); // Purchased
		}

		{
			ItemIdView id = new ItemIdView();
			id.setProductId("Y2");
			id.setSupplierId("A74_Supplier1");
			mediatorClient.addToCart("xyz", id, 15);
			mediatorClient.addToCart("otherXyz", id, 7); // Dropped
		}

		// -- buy carts
		ShoppingResultView[] shpResViews = new ShoppingResultView[2];
		shpResViews[0] = mediatorClient.buyCart("xyz", "4556648855991861");
		shpResViews[1] = mediatorClient.buyCart("otherXyz", "4556648855991861");

		// verify id and result of first buy
		assertNotNull(shpResViews[0].getId());
		assertEquals(Result.COMPLETE, shpResViews[0].getResult());

		// verify the entire result of the second buy (i.e., the partial one)
		assertNotNull(shpResViews[1].getId());
		assertEquals(Result.PARTIAL, shpResViews[1].getResult());
		assertEquals(1, shpResViews[1].getDroppedItems().size());
		assertEquals(1, shpResViews[1].getPurchasedItems().size());
		final int expectedTotalPrice = 20000; // sum(qty*price) of purchased
												// items
		assertEquals(expectedTotalPrice, shpResViews[1].getTotalPrice());
    	
    }
    
    
    
    
    
    
    
	// initialization and clean-up for each test
	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}


	

}
