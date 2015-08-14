package functional;

import static org.junit.Assert.*;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import javassist.CannotCompileException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.Assert;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebean.config.dbplatform.H2Platform;
import com.avaje.ebean.config.dbplatform.MySqlPlatform;
import com.avaje.ebean.enhance.agent.InputStreamTransform;
import com.avaje.ebean.enhance.agent.Transformer;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.server.ddl.DdlGenerator;

import exceptions.PluginInstantiationException;
import play.test.*;
import static play.test.Helpers.*;
import uk.ac.rhul.cs.dice.star.persistence.models.AgentWrapper;
import uk.ac.rhul.cs.dice.star.persistence.models.PhysicsWrapper;

public class PhysicsWrapperTest {

    public static FakeApplication app;

	PhysicsWrapper pack;
	@Before
	public void setUp() throws Exception {
		
	
		 app = Helpers.fakeApplication(Helpers.inMemoryDatabase());
	     Helpers.start(app);

	}

	@Test
	public void test() {
	
	        	pack = new PhysicsWrapper("physics");
	    		pack.save();

				PhysicsWrapper wrap = pack.get(pack.getId());
				Assert.notNull(wrap, "Not saved");
				pack.delete();
	  
	}

	@After
	public void tearDown() throws Exception {
		Helpers.stop(app);
	}
}
