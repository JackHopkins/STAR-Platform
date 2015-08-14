package integration;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.libs.ws.WS;

import play.test.*;
import static play.test.Helpers.*;

public class AgentTest extends WithApplication{

	private int port = 3333;
	private int timeout = 10000;
	@Before
	public void setUp() throws Exception {
		
	
	}
	@Test
	public void test() {
	
		 running(Helpers.testServer(3333), new Runnable() {
		        public void run() {
		           
		                WS.url("http://localhost:"+port).get().get(timeout).getStatus();
		            
		        }
		    });
	  
	}

	@After
	public void tearDown() throws Exception {
		Helpers.stop(app);
	}
}

