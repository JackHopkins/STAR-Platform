package integration;

import org.javatuples.Tuple;
import org.javatuples.Unit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.Assert;

import play.Logger;
import play.libs.ws.WS;

import play.test.*;
import uk.ac.rhul.cs.dice.star.action.Action;
import uk.ac.rhul.cs.dice.star.action.Event;
import uk.ac.rhul.cs.dice.star.action.SpeakAction;

import static play.test.Helpers.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;

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
public class PutContainerTest extends WithApplication{

	private int port = 3333;
	private int timeout = 10000;

	@Test
	public void testPutContainer() {
	
		 running(Helpers.testServer(port), new Runnable() {
		        public void run() {
		        	
		        	ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ObjectOutputStream oos;
					Unit<String> unit = new Unit<String>("hi");
					SpeakAction action = new SpeakAction("tutorial", unit);
					Event event = new Event("tutorial", action, System.currentTimeMillis());
					try {
						oos = new ObjectOutputStream( baos );
						oos.writeObject( event );
						oos.close();
						InputStream is = new ByteArrayInputStream(baos.toByteArray());
						int status = WS.url("http://localhost:"+port+"/v1/welcome").setContentType("application/octet-stream")
						.put(is).get(timeout).getStatus();
						 Assert.isTrue(status==200);
					} catch (IOException e) {
						e.printStackTrace();
					}

		        }
	    });
	  
	}

}

