package org.komparator.supplier.ws.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.komparator.supplier.ws.*;

/**
 * Test suite
 */
public class BuyProductIT extends BaseIT {

	// static members

	// one-time initialization and clean-up
	@BeforeClass
	public static void oneTimeSetUp() throws BadProductId_Exception, BadProduct_Exception {
		
		// clear remote service state before all tests
				client.clear();

				// fill-in test products
				// (since getProduct is read-only the initialization below
				// can be done once for all tests in this suite)
				{
					ProductView product = new ProductView();
					product.setId("X1");
					product.setDesc("Basketball");
					product.setPrice(10);
					product.setQuantity(10);
					client.createProduct(product);
				}
				{
					ProductView product = new ProductView();
					product.setId("Y2");
					product.setDesc("Baseball");
					product.setPrice(20);
					product.setQuantity(20);
					client.createProduct(product);
				}
				{
					ProductView product = new ProductView();
					product.setId("Z3");
					product.setDesc("Soccer ball");
					product.setPrice(30);
					product.setQuantity(30);
					client.createProduct(product);
				}
		
	}

	@AfterClass
	public static void oneTimeTearDown() {
		// clear remote service state after all tests
		client.clear();
	}

	// members

	// initialization and clean-up for each test
	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}
	
	
	

	// tests
	// assertEquals(expected, actual);

	// public String buyProduct(String productId, int quantity)
	// throws BadProductId_Exception, BadQuantity_Exception,
	// InsufficientQuantity_Exception {

	// bad input tests

	@Test(expected = BadProductId_Exception.class)
	public void buyProductIdNullTest() throws BadProductId_Exception, BadQuantity_Exception, InsufficientQuantity_Exception {
		client.buyProduct(null, 3);
	}
	@Test(expected = BadProductId_Exception.class)
	public void buyProductIdBlankTest() throws BadProductId_Exception, BadQuantity_Exception, InsufficientQuantity_Exception {
		client.buyProduct(" ", 3);
	}
	@Test(expected = BadProductId_Exception.class)
	public void buyProductWrongIdTest() throws BadProductId_Exception, BadQuantity_Exception, InsufficientQuantity_Exception {
		client.buyProduct("Z3333", 3);
	}
	
	@Test(expected = BadQuantity_Exception.class)
	public void buyProductNegativeQuantityTest() throws BadProductId_Exception, BadQuantity_Exception, InsufficientQuantity_Exception {
		client.buyProduct("Z3", -1);
	}
	@Test(expected = BadQuantity_Exception.class)
	public void buyProductZeroQuantityTest() throws BadProductId_Exception, BadQuantity_Exception, InsufficientQuantity_Exception {
		client.buyProduct("Z3", 0);
	}
	@Test(expected = InsufficientQuantity_Exception.class)
	public void buyProductTooMuchQuantityTest() throws BadProductId_Exception, BadQuantity_Exception, InsufficientQuantity_Exception {
		client.buyProduct("Z3", 100);
	}
	@Test(expected = InsufficientQuantity_Exception.class)
	public void buyProductOneTooMany() throws BadProductId_Exception, BadQuantity_Exception, InsufficientQuantity_Exception {
		client.buyProduct("Z3", 31);
	}
	
	

	// main tests
	
	
	@Test
	public void buyProductTest() throws BadText_Exception, BadProductId_Exception, BadQuantity_Exception, InsufficientQuantity_Exception {
		
		int initialQuantity = client.getProduct("X1").getQuantity();
		
		String purchase = client.buyProduct("X1", 5);
		assertEquals(client.listPurchases().isEmpty(), false);
		assertEquals(client.listPurchases().size(), 1);
		
		PurchaseView purchaseView = client.listPurchases().get(0);
		
		assertEquals(purchaseView.getId(), purchase);
		assertEquals(initialQuantity-5, purchaseView.getQuantity());
		
		
		
	}
	
	
	
}
