package org.komparator.mediator.ws.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.komparator.mediator.ws.InvalidItemId_Exception;
import org.komparator.mediator.ws.ItemView;
import org.komparator.supplier.ws.BadProductId_Exception;
import org.komparator.supplier.ws.BadProduct_Exception;
import org.komparator.supplier.ws.ProductView;

public class GetItemsIT extends BaseIT {
	
	// static members

	// one-time initialization and clean-up
		@BeforeClass
	public static void oneTimeSetUp() throws BadProductId_Exception, BadProduct_Exception {
		
		
		// clear remote service state before all tests
		supplierClient.clear();
		supplierClient2.clear();

				// fill-in test products
				// (since getProduct is read-only the initialization below
				// can be done once for all tests in this suite)
				{
					ProductView product = new ProductView();
					product.setId("X1");
					product.setDesc("Basketball");
					product.setPrice(10000);
					product.setQuantity(10);
					supplierClient.createProduct(product);
				}
				{
					ProductView product = new ProductView();
					product.setId("Y2");
					product.setDesc("Baseball");
					product.setPrice(20);
					product.setQuantity(20);
					supplierClient.createProduct(product);
				}
				{
					ProductView product = new ProductView();
					product.setId("Z3");
					product.setDesc("Soccer ball");
					product.setPrice(30);
					product.setQuantity(30);
					supplierClient.createProduct(product);
				}
				
				
				{
					ProductView product = new ProductView();
					product.setId("X1");
					product.setDesc("Basketball");
					product.setPrice(10);
					product.setQuantity(10);
					supplierClient2.createProduct(product);
				}
				{
					ProductView product = new ProductView();
					product.setId("Y3");
					product.setDesc("Baseball");
					product.setPrice(20);
					product.setQuantity(20);
					supplierClient2.createProduct(product);
				}
				{
					ProductView product = new ProductView();
					product.setId("Z3");
					product.setDesc("Soccer ball");
					product.setPrice(30);
					product.setQuantity(30);
					supplierClient2.createProduct(product);
				}
		   
				 
	}
	
	
	

	@AfterClass
	public static void oneTimeTearDown() {
		// clear remote service state after all tests
		supplierClient.clear();
		supplierClient2.clear();
	}

	// members
	
	@Test(expected = InvalidItemId_Exception.class)
	public void getItemsNullTest() throws InvalidItemId_Exception {
		mediatorClient.getItems(null);
	}

	@Test(expected = InvalidItemId_Exception.class)
	public void getItemsEmptyTest() throws InvalidItemId_Exception {
		mediatorClient.getItems("");
	}
	
	@Test(expected = InvalidItemId_Exception.class)
	public void getItemsWhitespaceTest() throws InvalidItemId_Exception {
		mediatorClient.getItems("   ");
	}

	@Test(expected = InvalidItemId_Exception.class)
	public void getItemsTabTestTest() throws InvalidItemId_Exception {
		mediatorClient.getItems("\t");
	}
    
	@Test(expected = InvalidItemId_Exception.class)
	public void getItemsNewlineTest() throws InvalidItemId_Exception {
		mediatorClient.getItems("\n");
	}
	@Test(expected = InvalidItemId_Exception.class)
	public void getItemsIdNoTFoundTest() throws InvalidItemId_Exception {
		mediatorClient.getItems("D2");
	}
	
	
	
    @Test
    public void sucessOneItem(){
		
    	ProductView product = new ProductView();
    	List<ItemView> listItemViews;
    	
    	try {
    		listItemViews = mediatorClient.getItems("Y3");
		} catch (InvalidItemId_Exception e) {
			
		
			listItemViews=null;
			fail();
		}
    	
    	assertEquals(1, listItemViews.size());
    	assertEquals("Y3", listItemViews.get(0).getItemId().getProductId());
    	assertEquals("A74_Supplier2", listItemViews.get(0).getItemId().getSupplierId());
    	
    	
    }
    
    @Test
    public void sucessMultipleItems(){
    	List<ItemView> listItemViews;
    	try {
    		listItemViews = mediatorClient.getItems("X1");
		} catch (InvalidItemId_Exception e) {
			listItemViews=null;
			fail();
		}
    	assertEquals(2, listItemViews.size());
    	
    	assertEquals("X1", listItemViews.get(0).getItemId().getProductId());
    	assertEquals("A74_Supplier2", listItemViews.get(0).getItemId().getSupplierId());
    	
    	assertEquals("X1", listItemViews.get(1).getItemId().getProductId());
    	assertEquals("A74_Supplier1", listItemViews.get(1).getItemId().getSupplierId());
    	
    }
    
	// initialization and clean-up for each test
	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}
	
	

}
