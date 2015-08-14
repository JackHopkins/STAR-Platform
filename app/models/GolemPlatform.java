package models;


import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import exceptions.AgentNotFoundException;
import exceptions.ContainerNotFoundException;
import play.libs.F.Function0;
import play.libs.F.Promise;
import play.mvc.Http.Request;
import uk.ac.rhul.cs.dice.star.base.Base;
import uk.ac.rhul.cs.dice.star.container.AbstractContainer;
import uk.ac.rhul.cs.dice.star.container.Container;
import uk.ac.rhul.cs.dice.star.container.DefaultContainer;
import uk.ac.rhul.cs.dice.star.http.ErrorCode;
import uk.ac.rhul.cs.dice.star.http.HttpRequest;
import uk.ac.rhul.cs.dice.star.http.HttpResponse;
import uk.ac.rhul.cs.dice.star.persistence.models.AgentWrapper;
import uk.ac.rhul.cs.dice.star.physics.DefaultPhysics;
import uk.ac.rhul.cs.dice.star.physics.Physics;


public class GolemPlatform implements Platform {

	/**
	 * Instance of the platform
	 */
	private static GolemPlatform instance;
	/**
	 * Golem runtime
	 */
	private Base golem;
	
	private ConcurrentHashMap<String, AbstractContainer> containers;
	
	public GolemPlatform() throws Exception {
		containers = new ConcurrentHashMap<String, AbstractContainer>();
		golem = new Base("");
	}
	/**
	 * Static getter for the platform singleton
	 * @return GolemPlatform instance
	 */
	public static GolemPlatform getInstance() {
		if (instance == null) {
			try {
				instance = new GolemPlatform();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return instance;
	}

	@Override
	public void registerContainer(String name, Object container) {
		try {
		containers.put(name, (AbstractContainer) container);
		}catch(ClassCastException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Object createContainer(String name) {
		return createContainer(name, new DefaultPhysics());
	}
	
	public AbstractContainer createContainer(String name, Physics physics) {
		if (containers.containsKey(name)) return (AbstractContainer) getContainer(name);
		return new DefaultContainer(name, physics);
	}

	public AbstractContainer getContainer(String name) {
		return containers.get(name);
	}
	
	public Collection<AbstractContainer> getContainers() {
		return containers.values();
	}
	
	@Override
	public void loadAgent(String name, File file) {
		AgentWrapper agent;
		try {
			agent = new AgentWrapper(file, name);
			agent.save();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void createAgent(String container, String name) throws ContainerNotFoundException, AgentNotFoundException {
		
		Container containerObj = (Container) containers.get(container);
			if (containerObj == null) {
				
			containerObj = (Container) createContainer(container);
			
			}
			try {
				AgentWrapper wrapper = AgentWrapper.getByName(name);
				if (wrapper != null) {
					wrapper.run(containerObj);
				}else{
					throw new AgentNotFoundException();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	@Override
	public void kill() {
		for (AbstractContainer container : containers.values()) {
			container.kill();
		}
		containers.clear();
	}
	
	public void kill(String id) throws ContainerNotFoundException {
		AbstractContainer container = containers.get(id);
		if (container != null) {
			containers.remove(id);
			container.kill();
		}
	}
	
	/**
	 * Routes a HTTP request to a GOLEM agent
	 * @param request HTTP request
	 * @param container Microservice container
	 * @param agent Microservice agent
	 * @return
	 */
	public Promise<HttpResponse> routeRequest(HttpRequest request, String container, String agent) {
		AbstractContainer containerObj = getContainer(container);
		if (containerObj == null) return error(ErrorCode.CONTAINER_BADREQUEST);

				
		return containerObj.route(request, agent);
	}

	private Promise<HttpResponse> error(final ErrorCode code) {
		return Promise.promise(
				new Function0<HttpResponse>() {
					public HttpResponse apply() {
						return new HttpResponse(code);
					}
				}
			);
	}
}
