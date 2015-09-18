package uk.ac.rhul.cs.dice.star.persistence.models;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import play.Logger;
import akka.event.Logging.Debug;

import com.avaje.ebean.Ebean;
import com.ning.http.client.Body;

import exceptions.NoArgConstructorRequiredException;
import exceptions.PluginInstantiationException;
import uk.ac.rhul.cs.dice.star.action.AbstractEffector;
import uk.ac.rhul.cs.dice.star.action.AbstractSensor;
import uk.ac.rhul.cs.dice.star.agent.AbstractAgentBody;
import uk.ac.rhul.cs.dice.star.agent.AbstractAgentBrain;
import uk.ac.rhul.cs.dice.star.agent.AbstractAgentMind;
import uk.ac.rhul.cs.dice.star.agent.DefaultAgentBody;
import uk.ac.rhul.cs.dice.star.agent.DefaultAgentBrain;
import uk.ac.rhul.cs.dice.star.container.AbstractContainer;
import uk.ac.rhul.cs.dice.star.container.Container;
import uk.ac.rhul.cs.dice.star.entity.View;
import uk.ac.rhul.cs.dice.star.persistence.JarFileLoader;
import uk.ac.rhul.cs.dice.star.persistence.Resource;
import uk.ac.rhul.cs.dice.star.physics.Physics;

@Entity
public class AgentWrapper extends Package{

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy="agent")
	public List<ResourceDB> resourceList; 
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
			resourceList = loader.getAllResources(this);
			
			Logger.debug("Creating resource map: "+resourceList.size());
			/*Class<?> clazz = loader.getByExtending(AbstractAgentMind.class).iterator().next();
		

			this.setName(clazz.getName()+":"+(new Date()).toGMTString());*/
			//setFile(file);
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
	public String run(Container containerObj) throws Exception {

			return run(containerObj, this.getName()+"-"+containerObj.getEntityNames().size());
		
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * @param container
	 * @return
	 * @throws Exception
	 */
	public String run(Container containerObj, String agentId) throws Exception {

			try {
				JarFileLoader loader = new JarFileLoader(this.getFile());
				loader.getByExtending(AbstractAgentBrain.class).isEmpty();
				
				AbstractAgentBody body = initialiseAgent(loader, containerObj);
				body.setAgentId(agentId);
				System.out.println("Creating agent with id: "+body.getId());
			//	body.setEnvironment(containerObj);
				containerObj.makePresent(body);
				return body.getId();
			} catch (ClassNotFoundException | IOException | IllegalArgumentException | SecurityException e) {
				e.printStackTrace();
				throw new Exception(e);
			}

		
	}

	@SuppressWarnings("unchecked")
	private AbstractAgentBody initialiseAgent(JarFileLoader loader, Container containerObj)
			throws MalformedURLException, ClassNotFoundException, Exception {
		
		Class<? extends AbstractAgentBrain> agentClass;
		Class<? extends AbstractAgentBody> bodyClass;
		Class<? extends AbstractAgentMind> mindClass;
		
		Set<Class<?>> effectorClasses = loader.getByExtending(AbstractEffector.class);
		Set<Class<?>> sensorClasses = loader.getByExtending(AbstractSensor.class);
		
		Iterator<Class<?>> brainIterator = loader.getByExtending(AbstractAgentBrain.class).iterator();
		Iterator<Class<?>> bodyIterator = loader.getByExtending(AbstractAgentBody.class).iterator();
		Iterator<Class<?>> mindIterator = loader.getByExtending(AbstractAgentMind.class).iterator();
		
		Logger.debug("Has brain: "+brainIterator.hasNext());
		Logger.debug("Has body: "+bodyIterator.hasNext());
		Logger.debug("Has mind: "+mindIterator.hasNext());
		
		if (brainIterator.hasNext()) {
		agentClass = (Class<? extends AbstractAgentBrain>) brainIterator.next();
		}else{
			agentClass = DefaultAgentBrain.class;
		}
		
		if (bodyIterator.hasNext()) {
		bodyClass = (Class<? extends AbstractAgentBody>) bodyIterator.next();
		}else{
			bodyClass = DefaultAgentBody.class;
		}
		
		if (mindIterator.hasNext()) {
		mindClass = (Class<? extends AbstractAgentMind>) mindIterator.next();
		}else{
			throw new ClassNotFoundException("Could not find a mind for this agent.");
		}
		
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
			
			System.out.println("Container Physics: "+((AbstractContainer)containerObj).getGovernor());
			body.setEnvironment(containerObj);
			body.setRenderer(views, JarFileLoader.getResourceMap(resourceList));
			body.setAgentId(getName());
			AbstractAgentMind mind = (AbstractAgentMind) mindCon.newInstance();
			mind.setBrain(brain);
			
			brain.setMind(mind);
		    brain.setBody(body);
		    
		    
		   int index = 0;
			for (Class<?> clazz : sensorClasses) {
				Constructor<?> sen = clazz.getDeclaredConstructor();
				
				AbstractSensor sensor = (AbstractSensor) sen.newInstance();
				
				sensor.init(clazz.getName()+" "+index++, body);
				
				body.registerSensor(sensor, true);
				
			}
			
		    index = 0;
			for (Class<?> clazz : effectorClasses) {
				Constructor<?> eff = clazz.getDeclaredConstructor();
				AbstractEffector effector = (AbstractEffector) eff.newInstance();
				effector.setBody(body);
				effector.setId(clazz.getName()+" "+index++);
				body.registerEffector(effector);
			}
			
			brain.startCycle();
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
		return find.findList();
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
	public static void deleteById(Long id) {
		Ebean.delete(find.byId(id));
	}
	public static void deleteByName(String name) {
		Ebean.delete(getByName(name));
	} 
}
