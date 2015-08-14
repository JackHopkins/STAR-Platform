package integration;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.Assert;

import play.libs.ws.WS;

import play.test.*;
import static play.test.Helpers.*;

/**
 *  #Spawns a container. Physics can be defined with the "physics" parameter.
 *	#Relevant applications can be defined with the "applications" parameter.
 *	POST 		/v1/:container		  		controllers.Container.post(container)
 *	#Kills a container.
 *	DELETE		/v1/:container				controllers.Container.delete(container)
 *	#Puts an event in this container 
 *	PUT			/v1/:container				controllers.Container.put(container)
 * @author jack
 *
 */
public class UploadAgentTest extends WithApplication{

	private int port = 3333;
	private int timeout = 10000;
	@Before
	public void setUp() throws Exception {
		
	
	}
	@Test
	public void testUploadAgent() {
	
		 running(Helpers.testServer(port), new Runnable() {
		        public void run() {
		          WS.url("http://localhost:"+port+"/v1/upload/agent?name=testagent").post("/STAR-v3/test/resources/Agent-Test.jar");    
		          int status = WS.url("http://localhost:"+port+"/v1/welcome/testagent").post("").get(timeout).getStatus();
		          Assert.isTrue(status==200);
		        }
	    });
	  
	}

	@After
	public void tearDown() throws Exception {
		
	}
}

