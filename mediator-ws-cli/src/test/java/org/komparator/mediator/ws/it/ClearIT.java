package org.komparator.mediator.ws.it;



import org.junit.Test;

public class ClearIT  extends BaseIT{

	   // tests
    // assertEquals(expected, actual);

    // public String ping(String x)
	
   @Test
    public void pingEmptyTest() {
        mediatorClient.clear();
    }

}
