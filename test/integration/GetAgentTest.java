package integration;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.Assert;

import play.libs.ws.WS;

import play.test.*;
import static play.test.Helpers.*;

/**
 */
public class GetAgentTest extends WithApplication{

	private int port = 3333;
	private int timeout = 10000;

	@Test
	public void testGetAgent() {
	
		 running(Helpers.testServer(port), new Runnable() {
		        public void run() {
		           int status = WS.url("http://localhost:"+port+"/v1/welcome").delete().get(timeout).getStatus();    
		           Assert.isTrue(status==200);
		        }
	    });
	  
	}

}

