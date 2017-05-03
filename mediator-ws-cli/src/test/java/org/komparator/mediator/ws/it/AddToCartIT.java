package org.komparator.mediator.ws.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.komparator.mediator.ws.CartItemView;
import org.komparator.mediator.ws.CartView;
import org.komparator.mediator.ws.InvalidCartId_Exception;
import org.komparator.mediator.ws.InvalidItemId_Exception;
import org.komparator.mediator.ws.InvalidQuantity_Exception;
import org.komparator.mediator.ws.InvalidText_Exception;
import org.komparator.mediator.ws.ItemIdView;
import org.komparator.mediator.ws.ItemView;
import org.komparator.mediator.ws.NotEnoughItems_Exception;
import org.komparator.supplier.ws.BadProductId_Exception;
import org.komparator.supplier.ws.BadProduct_Exception;
import org.komparator.supplier.ws.ProductView;

public class AddToCartIT extends BaseIT{
	
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

	
	
	
	// members
	@Test(expected = InvalidCartId_Exception.class)
	public void getCartNullTest() throws InvalidCartId_Exception, InvalidItemId_Exception, InvalidQuantity_Exception, NotEnoughItems_Exception {
		mediatorClient.addToCart(null, idViewTest, 2);
	}

	@Test(expected = InvalidCartId_Exception.class)
	public void getCartEmptyTest() throws InvalidCartId_Exception, InvalidItemId_Exception, InvalidQuantity_Exception, NotEnoughItems_Exception {
		mediatorClient.addToCart("", idViewTest, 2);
	}
	
	@Test(expected = InvalidCartId_Exception.class)
	public void getCartWhitespaceTest() throws InvalidCartId_Exception, InvalidItemId_Exception, InvalidQuantity_Exception, NotEnoughItems_Exception {
		mediatorClient.addToCart("  ", idViewTest, 2);
	}
	
	@Test(expected = InvalidItemId_Exception.class)
	public void getItemNullTest() throws InvalidCartId_Exception, InvalidItemId_Exception, InvalidQuantity_Exception, NotEnoughItems_Exception {
		mediatorClient.addToCart(cart.getCartId(), null, 2);
	}
	
	@Test(expected = InvalidQuantity_Exception.class)
	public void negativeQuantity() throws InvalidCartId_Exception, InvalidItemId_Exception, InvalidQuantity_Exception, NotEnoughItems_Exception {
		mediatorClient.addToCart(cart.getCartId(), idViewTest, -1);
	}
	@Test(expected = InvalidQuantity_Exception.class)
	public void zeroQuantity() throws InvalidCartId_Exception, InvalidItemId_Exception, InvalidQuantity_Exception, NotEnoughItems_Exception {
		mediatorClient.addToCart(cart.getCartId(), idViewTest, 0);
	}
	
	@Test(expected = NotEnoughItems_Exception.class)
	public void testExceedindQuantity1() throws InvalidCartId_Exception, InvalidItemId_Exception, InvalidQuantity_Exception, NotEnoughItems_Exception {
		mediatorClient.addToCart(cart.getCartId(), idViewTest, 11);
	}
	
	@Test(expected = NotEnoughItems_Exception.class)
	public void testExceedindQuantity2() throws InvalidCartId_Exception, InvalidItemId_Exception, InvalidQuantity_Exception, NotEnoughItems_Exception {
		mediatorClient.addToCart(cart.getCartId(), idViewTest, 10);
		mediatorClient.addToCart(cart.getCartId(), idViewTest, 1);
	}
	
    @Test
    public void sucessNewCartFirstNewItem(){
    	
    	
    	ItemIdView idView = new ItemIdView();
    	idView.setProductId("X1");
    	idView.setSupplierId("A74_Supplier1");
    	
    	List<CartView> cartList;
    	
    	
    	
    	try {
    		mediatorClient.addToCart(cart.getCartId(), idView, 2);
		} catch (Exception e) {
			fail();
		}
    	
    	
    	cartList=mediatorClient.listCarts();
    	
    	assertEquals(2, cartList.get(0).getItems().get(0).getQuantity());
    	assertEquals("X1", cartList.get(0).getItems().get(0).getItem().getItemId().getProductId());
    	assertEquals("A74_Supplier1", cartList.get(0).getItems().get(0).getItem().getItemId().getSupplierId());
    	
    	
    }
    
    @Test
    public void sucessExistentCartTwoNewItem(){
    	
    	
    	ItemIdView idView1 = new ItemIdView();
    	idView1.setProductId("X1");
    	idView1.setSupplierId("A74_Supplier1");
    	
    	ItemIdView idView2 = new ItemIdView();
    	idView2.setProductId("Y3");
    	idView2.setSupplierId("A74_Supplier2");
    	
    	List<CartView> cartList;
    	
    	
    	
    	
    	try {
    		mediatorClient.addToCart(cart.getCartId(), idView1, 2);
    		mediatorClient.addToCart(cart.getCartId(), idView2, 3);
    		
		} catch (Exception e) {
			fail();
		}
    	
    	
    	cartList=mediatorClient.listCarts();
    	
    	assertEquals(2, cartList.get(0).getItems().get(0).getQuantity());
    	assertEquals("X1", cartList.get(0).getItems().get(0).getItem().getItemId().getProductId());
    	assertEquals("A74_Supplier1", cartList.get(0).getItems().get(0).getItem().getItemId().getSupplierId());
    	
    	assertEquals(3, cartList.get(0).getItems().get(1).getQuantity());
    	assertEquals("Y3", cartList.get(0).getItems().get(1).getItem().getItemId().getProductId());
    	assertEquals("A74_Supplier2", cartList.get(0).getItems().get(1).getItem().getItemId().getSupplierId());
    	
    	
    }
    
    
    
    @Test
    public void sucessExistentCartExistentItem(){
    	
    	
    	ItemIdView idView1 = new ItemIdView();
    	idView1.setProductId("X1");
    	idView1.setSupplierId("A74_Supplier1");
    	
    	ItemIdView idView2 = new ItemIdView();
    	idView2.setProductId("X1");
    	idView2.setSupplierId("A74_Supplier1");
    	
    	List<CartView> cartList;
    	
    	
    	
    	try {
    		mediatorClient.addToCart(cart.getCartId(), idView1, 2);
    		mediatorClient.addToCart(cart.getCartId(), idView2, 3);
    		
		} catch (Exception e) {
			fail();
		}
    	
    	
    	cartList=mediatorClient.listCarts();
    	
    	assertEquals(5, cartList.get(0).getItems().get(0).getQuantity());
    	assertEquals("X1", cartList.get(0).getItems().get(0).getItem().getItemId().getProductId());
    	assertEquals("A74_Supplier1", cartList.get(0).getItems().get(0).getItem().getItemId().getSupplierId());
    	
    	
    }
    
    
    
    
	// initialization and clean-up for each test
	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}


}
