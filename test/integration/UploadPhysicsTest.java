package integration;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.Assert;

import play.libs.ws.WS;
import play.test.*;
import uk.ac.rhul.cs.dice.star.persistence.JarFileLoader;
import uk.ac.rhul.cs.dice.star.physics.Physics;
import static play.test.Helpers.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarFile;

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
public class UploadPhysicsTest extends WithApplication{

	private int port = 3333;
	private int timeout = 10000;
	@Before
	public void setUp() throws Exception {
		
	
	}
	@Test
	public void testUploadPhysics() {
	
		
		 running(Helpers.testServer(port), new Runnable() {
		        public void run() {
		        	
		        	File physicsFile = new File("test/resources/Physics-Test.jar");
		        	
		        	//MultipartEntity req = new MultipartEntity();
		        	//req.addPart(body);
		        	//File physicsFile = new File("./test/resources/Physics-Test.jar");
		        	//String path = physicsFile.getAbsolutePath();
		    	/*	try {
		    			JarFile jfile = new JarFile(physicsFile);
		    			JarFileLoader loader = new JarFileLoader(physicsFile);
		    			Class clazz = loader.getByExtending(Physics.class).iterator().next();
		    			System.out.println(clazz.getName());
		    		} catch (IOException | ClassNotFoundException e1) {
		    			e1.printStackTrace();
		    			Assert.isTrue(false);
		    		}*/
		    			String name = "physicstest";
		        	  WS.url("http://localhost:"+port+"/v1/upload/physics?name="+name).post(physicsFile).get(10000);
		        	  int status = WS.url("http://localhost:"+port+"/v1/physicscontainer?physics="+name).post("").get(timeout).getStatus();
				      
		        	  
			        
		        }
	    });
		
	  
	}

	@After
	public void tearDown() throws Exception {
		
	}
}

