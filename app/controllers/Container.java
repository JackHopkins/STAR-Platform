package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import exceptions.ContainerNotFoundException;
import models.GolemPlatform;
import play.*;
import play.api.libs.Files;
import play.libs.F.Function0;
import play.libs.F.Promise;
import play.libs.Json;
import play.mvc.*;
import uk.ac.rhul.cs.dice.star.action.Event;
import uk.ac.rhul.cs.dice.star.agent.AgentBody;
import uk.ac.rhul.cs.dice.star.container.AbstractContainer;
import uk.ac.rhul.cs.dice.star.entity.Entity;
import uk.ac.rhul.cs.dice.star.persistence.models.PhysicsWrapper;
import uk.ac.rhul.cs.dice.star.physics.Physics;
import views.html.*;
public class Container extends Controller {
	
	private static final String PHYSICS = "physics";
	private static final String APPLICATIONS = "applications";
	
	public static Result get() {
		JSONArray containers = new JSONArray();
		for (AbstractContainer container : GolemPlatform.getInstance().getContainers()) {
			JSONObject obj = new JSONObject();
			try {
				obj.put("name", container.getName());
				obj.put("physics", container.getGovernorClassName());
				JSONArray arr = new JSONArray();
				
				for (String name : container.getEntityNames()) {
					
					JSONObject entity = new JSONObject();
					Entity ent = container.getEntity(name);
					entity.put("id", ent.getId());
					
					if (ent instanceof AgentBody) {
					entity.put("type", "Agent");
					}else{
						entity.put("type", "Entity");
					}
					
					arr.put(entity);
				}
				obj.put("entities", arr);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			containers.put(obj);
		}
		return ok(containers.toString());
	}
    public static Result post(String container) {
    	Map<String, String[]> parameters = request().queryString();
    	Logger.info("Creating: "+container);
    	if (parameters.containsKey(PHYSICS)) {
    		String physicsName = parameters.get(PHYSICS)[0];
    		Physics physics = PhysicsWrapper.getPhysicsByName(physicsName);
    		if (physics == null) {
    			Logger.error("Physics not found with name: \""+physicsName+"\"");
    			return badRequest("Physics not found");
    		}
    		Logger.info("Creating container: \""+container+"\" with physics: \""+physics.toString()+"\"");
    		GolemPlatform.getInstance().createContainer(container, physics);
    	} else {
    		AbstractContainer containerObj = (AbstractContainer) GolemPlatform.getInstance().createContainer(container);
    		Logger.info("Creating container: \""+container+"\" with default physics");
    		GolemPlatform.getInstance().registerContainer(container, containerObj);
    	}
        return ok();
    }
    public static Result delete(String container) {
    	try {
    	GolemPlatform.getInstance().kill(container);
    	Logger.info("Deleting container: "+container);
    	return ok();
    	}catch(ContainerNotFoundException e) {
    		Logger.error("Container not found: "+container);
    		return Results.notFound(e.getMessage());
    	}
    }
    @SuppressWarnings("resource")
	public static Result put(String container) {
    	AbstractContainer containerObj = (AbstractContainer) GolemPlatform.getInstance().getContainer(container);
    	Logger.debug("Found container: \""+container+"\"");
    	File file = request().body().asRaw().asFile();
    	FileInputStream fin;
		try {
			fin = new FileInputStream(file);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			return badRequest(e1.getMessage());
		}
    	ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(fin);
		} catch (IOException e) {
			e.printStackTrace();
			return badRequest(e.getMessage());
		}
    	try {
			Event event = (Event) ois.readObject();
			containerObj.attempt(event);
			Logger.debug("Event has been successfully asserted with action: "+event.getActionType());
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
			return badRequest(e.getMessage());
		}
    	   
    	return ok();
    }
}
