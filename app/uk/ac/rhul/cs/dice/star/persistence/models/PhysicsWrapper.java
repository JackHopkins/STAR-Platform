package uk.ac.rhul.cs.dice.star.persistence.models;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.avaje.ebean.Ebean;
import com.sun.media.jfxmedia.logging.Logger;

import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;
import exceptions.PluginInstantiationException;
import uk.ac.rhul.cs.dice.star.persistence.JarFileLoader;
import uk.ac.rhul.cs.dice.star.physics.Physics;

@Entity
public class PhysicsWrapper extends Package {

	/*public AgentJARWrapper(String name, String uploader, PlatformType type) {
		super(name, uploader, type);
	}*/

	public PhysicsWrapper(File file, String name) throws PluginInstantiationException {
		super(file, name);
		
	}
	
	public PhysicsWrapper(String name) {
		super(name);
	}
	
	public PhysicsWrapper() {
		super();
	}

	@SuppressWarnings("unchecked")
	public Physics getPhysics() throws IOException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		JarFileLoader loader = new JarFileLoader(getFile());
		Class<? extends Physics> agentClass;
		try {
			agentClass = (Class<? extends Physics>) loader.getByExtending(Physics.class).iterator().next();
			System.out.println("PHYSICS: "+agentClass.getName());
			Constructor physicsConstructor = agentClass.getDeclaredConstructor();
			Physics physics = (Physics) physicsConstructor.newInstance();
	
			return physics;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			//As this is checked at instantiation, this should never happen.
			return null;
		}
	}

	public static List<PhysicsWrapper> getPhysicsList() {
		return new ArrayList<PhysicsWrapper>();//MongoDBManager.getInstance().dataStore.createQuery(AgentJARWrapper.class).field("uploader").equal(user).asList();
	}


	public void save() {
		System.out.println("Saving physicswrapper with id: "+this.getId());
		Ebean.save(this);
	}
	public static PhysicsWrapper get(Long id2) {
		return PhysicsWrapper.find.byId(id2);//MongoDBManager.getInstance().dataStore.get(AgentJARWrapper.class, id2);
	}
	public void delete() {
		Ebean.delete(this);
	}
	public static Physics getPhysicsById(Long id2) {
		try {
			return PhysicsWrapper.get(id2).getPhysics();
		} catch (NoSuchMethodException | SecurityException
				| InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	public static Finder<Long,PhysicsWrapper> find = new Finder<Long,PhysicsWrapper>(
		    Long.class, PhysicsWrapper.class
	);
	
	public static Physics getPhysicsByName(String name) {
		try {
			List<PhysicsWrapper> physics = PhysicsWrapper.find.where().eq("name", name).findList();
			
			if (physics.isEmpty()) return null;
			return physics.get(0).getPhysics();
		} catch (NoSuchMethodException | SecurityException
				| InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| IOException
				| NullPointerException e) {
			e.printStackTrace();
			return null;
		}
	} 
}
