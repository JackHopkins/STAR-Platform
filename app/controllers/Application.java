package controllers;

import models.GolemPlatform;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import play.*;
import play.mvc.*;
import uk.ac.rhul.cs.dice.star.agent.AgentBody;
import uk.ac.rhul.cs.dice.star.container.AbstractContainer;
import uk.ac.rhul.cs.dice.star.entity.Entity;
import views.html.*;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }
    public static Result test() {
    	return ok(test.render());
    }
    public static Result containers() {
    	return ok(containers.render());
    }
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
}
