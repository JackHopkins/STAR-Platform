package uk.ac.rhul.cs.dice.star.persistence.models;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.avaje.ebean.Ebean;

import exceptions.NoArgConstructorRequiredException;
import exceptions.PluginInstantiationException;
import uk.ac.rhul.cs.dice.star.action.AbstractEffector;
import uk.ac.rhul.cs.dice.star.action.AbstractSensor;
import uk.ac.rhul.cs.dice.star.agent.AbstractAgentBody;
import uk.ac.rhul.cs.dice.star.agent.AbstractAgentBrain;
import uk.ac.rhul.cs.dice.star.agent.AbstractAgentMind;
import uk.ac.rhul.cs.dice.star.container.Container;
import uk.ac.rhul.cs.dice.star.entity.View;
import uk.ac.rhul.cs.dice.star.persistence.JarFileLoader;
import uk.ac.rhul.cs.dice.star.persistence.Resource;

@Entity
public class AgentWrapper extends Package{

	@OneToMany(cascade = CascadeType.ALL)
	Map<String, ResourceDB> resourceMap; 
	/*public AgentJARWrapper(String name, String uploader, PlatformType type) {
		super(name, uploader, type);
	}*/
	/*@Constraints.Required
	private String name;
	@Id
	private Long id = System.currentTimeMillis();
	
	private boolean verified = false;
	
	@Formats.DateTime(pattern="dd/MM/yyyy")
	public Date date = new Date();
	*/
	public AgentWrapper(File file, String name) throws PluginInstantiationException {
		super(file, name);
		JarFileLoader loader;
		try {	
			loader = new JarFileLoader(this.getFile());
			resourceMap = loader.getAllResources();
			
			Class<?> clazz = loader.getByExtending(AbstractAgentMind.class).iterator().next();
		

			this.setName(clazz.getName()+":"+(new Date()).toGMTString());
			setFile(file);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

	}
	public AgentWrapper() {
		super();
	}
	public AgentWrapper(String name) {
		super(name);
	}
	/*public File getFile() {
		return se.digiplant.res.Res.get(id+"","default");
	}
	public void setFile(File file) {
		System.out.println(file.getName());
		se.digiplant.res.Res.put(file, "default", id+"");
	}*/
	@SuppressWarnings("unchecked")
	/**
	 * @param container
	 * @return
	 * @throws Exception
	 */
	public boolean run(Container containerObj) throws Exception {

			try {
				JarFileLoader loader = new JarFileLoader(this.getFile());
				loader.getByExtending(AbstractAgentBrain.class).isEmpty();
				
				AbstractAgentBody body = initialiseAgent(loader);
				body.setEnvironment(containerObj);
				containerObj.makePresent(body);
				
			} catch (ClassNotFoundException | IOException | IllegalArgumentException | SecurityException e) {
				e.printStackTrace();
				return false;
			}

		return false;
	}

	@SuppressWarnings("unchecked")
	private AbstractAgentBody initialiseAgent(JarFileLoader loader)
			throws MalformedURLException, ClassNotFoundException, Exception {
		Class<? extends AbstractAgentBrain> agentClass;
		Class<? extends AbstractAgentBody> bodyClass;
		Class<? extends AbstractAgentMind> mindClass;
		Set<Class<?>> effectorClasses = loader.getByExtending(AbstractEffector.class);
		
		Set<Class<?>> sensorClasses = loader.getByExtending(AbstractSensor.class);
		agentClass = (Class<? extends AbstractAgentBrain>) loader.getByExtending(AbstractAgentBrain.class).iterator().next();
		bodyClass = (Class<? extends AbstractAgentBody>) loader.getByExtending(AbstractAgentBody.class).iterator().next();
		mindClass = (Class<? extends AbstractAgentMind>) loader.getByExtending(AbstractAgentMind.class).iterator().next();
		
		
		final Map<String, View> views = 		loader.getWithFileType(".rythm.html");
		//final Map<String, String> resources = loader
		
		try {
		
			Constructor<? extends AbstractAgentBrain> agentCon = agentClass.getDeclaredConstructor();
			Constructor<? extends AbstractAgentBody> bodyCon = bodyClass.getDeclaredConstructor();
			Constructor<? extends AbstractAgentMind> mindCon = mindClass.getDeclaredConstructor();
			
			//List<Constructor> effectorCons = new ArrayList<Constructor>
			//List<Constructor> sensorCon = sensorClass.getDeclaredConstructor();
			
			AbstractAgentBrain brain = (AbstractAgentBrain) agentCon.newInstance();
			
			AbstractAgentBody body = (AbstractAgentBody) bodyCon.newInstance();
			Map<String, Resource> resources = new HashMap<String, Resource>();
			for (String key : resourceMap.keySet()) {
				ResourceDB resourceDB = resourceMap.get(key);
			
				resources.put(key, JarFileLoader.getResource(resourceDB));
			}
			body.setRenderer(views, resources);
			AbstractAgentMind mind = (AbstractAgentMind) mindCon.newInstance();
			mind.setBrain(brain);
			
			brain.setMind(mind);
		    brain.setBody(body);
		    
			for (Class<?> clazz : effectorClasses) {
				Constructor<?> eff = clazz.getDeclaredConstructor();
				AbstractEffector effector = (AbstractEffector) eff.newInstance();
				effector.setBody(body);
				body.registerEffector(effector);
			}
			for (Class<?> clazz : sensorClasses) {
				Constructor<?> sen;
					sen = clazz.getDeclaredConstructor();
				
				AbstractSensor sensor = (AbstractSensor) sen.newInstance();
				 
				sensor.init(getName(), body);
				body.registerSensor(sensor, true);
				
			}
			return body;
			
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			throw new NoArgConstructorRequiredException();
		} catch (SecurityException e) {
			e.printStackTrace();
		} 
		return null;
	}

	public static List<AgentWrapper> getAgents() {
		return new ArrayList<AgentWrapper>();//MongoDBManager.getInstance().dataStore.createQuery(AgentJARWrapper.class).field("uploader").equal(user).asList();
	}
	public void save() {
		
		Ebean.save(this);
	}
	public static AgentWrapper getByName(String name) {
		return find.where().eq("name", name).findUnique();
	}
	public static AgentWrapper getById(Long id) {
		return find.byId(id);
	}
	public static Finder<Long,AgentWrapper> find = new Finder<Long,AgentWrapper>(
		    Long.class, AgentWrapper.class
	);
	public void delete() {
		Ebean.delete(this);
	} 
}
