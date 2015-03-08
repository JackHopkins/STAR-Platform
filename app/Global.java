import akka.actor.ActorRef;
import akka.actor.Props;

import com.fasterxml.jackson.databind.JsonNode;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;

import persistence.MongoDBManager;
import play.Application;
import play.GlobalSettings;
import play.cache.Cache;
import play.libs.Akka;
import play.libs.F.Function;
import play.libs.F.Promise;
import play.mvc.Result;
import scala.collection.mutable.Set;
import scala.concurrent.duration.Duration;
import securesocial.core.RuntimeEnvironment;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import service.UserService;
import service.MyEnvironment;
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
		Akka.system().scheduler().schedule(  
				Duration.create(0, TimeUnit.MINUTES),
				Duration.create(5, TimeUnit.MINUTES),
				new Runnable() {
					@Override
					public void run() {
						
					}
				}, Akka.system().dispatcher());
	}

}