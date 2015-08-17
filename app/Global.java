import akka.actor.ActorRef;
import akka.actor.Props;

import com.avaje.ebean.enhance.agent.InputStreamTransform;
import com.avaje.ebean.enhance.agent.Transformer;
import com.fasterxml.jackson.databind.JsonNode;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;

import exceptions.PluginInstantiationException;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.api.Play;
import play.cache.Cache;
import play.libs.Akka;
import play.libs.F.Function;
import play.libs.F.Promise;
import play.mvc.Result;
import scala.concurrent.duration.Duration;
import securesocial.core.RuntimeEnvironment;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.util.Assert;

import service.UserService;
import service.MyEnvironment;
import uk.ac.rhul.cs.dice.star.action.AbstractEffector;
import uk.ac.rhul.cs.dice.star.action.AbstractSensor;
import uk.ac.rhul.cs.dice.star.agent.AbstractAgentBody;
import uk.ac.rhul.cs.dice.star.agent.AbstractAgentBrain;
import uk.ac.rhul.cs.dice.star.agent.AbstractAgentMind;
import uk.ac.rhul.cs.dice.star.agent.AgentBrain;
import uk.ac.rhul.cs.dice.star.agent.AgentFactory;
import uk.ac.rhul.cs.dice.star.agent.DefaultAgentBody;
import uk.ac.rhul.cs.dice.star.agent.DefaultAgentBrain;
import uk.ac.rhul.cs.dice.star.container.AbstractContainer;
import uk.ac.rhul.cs.dice.star.containers.welcome.TutorialAgentBody;
import uk.ac.rhul.cs.dice.star.containers.welcome.TutorialAgentMind;
import uk.ac.rhul.cs.dice.star.containers.welcome.TutorialEffector;
import uk.ac.rhul.cs.dice.star.containers.welcome.TutorialSensor;
import uk.ac.rhul.cs.dice.star.persistence.Resource;
import uk.ac.rhul.cs.dice.star.persistence.models.ResourceDB;
import uk.ac.rhul.cs.dice.star.entity.View;
import uk.ac.rhul.cs.dice.star.persistence.models.PhysicsWrapper;
import uk.ac.rhul.cs.dice.star.physics.Physics;
import uk.ac.rhul.cs.dice.star.util.ResourceFinder;
import uk.ac.rhul.cs.dice.star.util.ViewFinder;
import models.GolemPlatform;
import models.users.UserType;


public class Global extends GlobalSettings
{
	@SuppressWarnings("rawtypes")
	private RuntimeEnvironment env = new MyEnvironment();
	
	@Override
	public <A> A getControllerInstance(Class<A> controllerClass) throws Exception {
		A result;
	        
		try {
			result = controllerClass.getDeclaredConstructor(RuntimeEnvironment.class).newInstance(env);
		} catch (NoSuchMethodException e) {
			// the controller does not receive a RuntimeEnvironment, delegate creation to base class.
			result = super.getControllerInstance(controllerClass);
		}
		return result;
	}
	
	@Override
	public void onStop(Application application) {
		
	}
	@Override
	public void onStart(Application application)
	{ 

		
		Akka.system().scheduler().scheduleOnce(
				Duration.create(0, TimeUnit.MILLISECONDS),
				new Runnable() {
					public void run() {
						
							String container = "welcome";
							String agent = "tutorial-agent";
							String path = "/assets/containers/"+container;
							
							Logger.info(path);
							
							Map<String, Resource> resources = new HashMap<String, Resource>();
							Map<String, View> views 		= new HashMap<String, View>();
							
							views.put("view", 		ViewFinder.findPublicView("containers/"+container+"/view.rythm.html", "view"));
							resources.put("star", 	ResourceFinder.findPublicResource("containers/"+container+"/star.css", "star"));
							
							Logger.info("Resources #: "+resources.size()+", Views #: "+views.size());
							Logger.info("Resources: "+resources.keySet().toString());
							Logger.info("Views: "+views.keySet().toString());
							
							GolemPlatform platform = GolemPlatform.getInstance();
							AbstractContainer containerObj = (AbstractContainer) platform.createContainer(container);
							platform.registerContainer(container, containerObj);
							Logger.info("Spinning up \""+container+"\" container");
							
							try {
							Set<Class<?>> effectors = new HashSet<Class<?>>();
							effectors.add(TutorialEffector.class);
							Set<Class<?>> sensors = new HashSet<Class<?>>();
							sensors.add(TutorialSensor.class);
							
							AbstractAgentBody body = AgentFactory.initAgent(
									agent,
									DefaultAgentBrain.class,
									TutorialAgentMind.class,
									TutorialAgentBody.class,
									effectors,
									sensors,
									containerObj);
							body.setRenderer(views, resources);
							
							containerObj.makePresent(body);
							Logger.info("Added agent to \""+container+"\"");
							}catch(Exception e) {
								e.printStackTrace();								
							}
							
						
					}
				},
				Akka.system().dispatcher()
				);	 
		
	}

}