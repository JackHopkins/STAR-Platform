package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import exceptions.PluginInstantiationException;
import play.Logger;
import play.Play;
import play.api.libs.Files.TemporaryFile;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import uk.ac.rhul.cs.dice.star.persistence.JarFileLoader;
import uk.ac.rhul.cs.dice.star.persistence.models.AgentWrapper;
import uk.ac.rhul.cs.dice.star.persistence.models.PhysicsWrapper;
import uk.ac.rhul.cs.dice.star.physics.Physics;

public class UploadController extends Controller {

	private final static String NAME = "name";
	
	public static Result getAgents() {
		List<AgentWrapper> agents = AgentWrapper.getAgents();
		JSONArray arr = new JSONArray();
		for (AgentWrapper agent : agents) {
			JSONObject pack = new JSONObject();
			try {
				pack.put("name",agent.getName());
				pack.put("uploaded", agent.date.toLocaleString());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return ok(arr.toString());
	}
	public static Result getPhysics() {
		List<PhysicsWrapper> physics = PhysicsWrapper.getPhysicsList();
		JSONArray arr = new JSONArray();
		for (PhysicsWrapper physic : physics) {
			JSONObject pack = new JSONObject();
			try {
				pack.put("name",physic.getName());
				pack.put("uploaded", physic.date.toLocaleString());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return ok(arr.toString());
	}

	public static Result uploadAgent() {
		File file = request().body().asRaw().asFile();
		Map<String, String[]> queryParameters = request().queryString();

		if (file != null) {
			AgentWrapper wrapper;
			if (queryParameters.containsKey(NAME)) {
				try {
					wrapper = new AgentWrapper(file, queryParameters.get(NAME)[0]);
				} catch (PluginInstantiationException e) {
					e.printStackTrace();
					return badRequest();
				}
			}else{
				wrapper = new AgentWrapper();
				wrapper.setFile(file);				
			}
			wrapper.save();

		}else{
			return badRequest();
		}
		return ok();
	}

	public static Result uploadPhysics() {
		File file = request().body().asRaw().asFile();	

		
		Map<String, String[]> queryParameters = request().queryString();
		if (file != null) {
			PhysicsWrapper wrapper;
			if (queryParameters.containsKey(NAME)) {
				try {
					wrapper = new PhysicsWrapper(file, queryParameters.get(NAME)[0]);
					
				} catch (PluginInstantiationException e) {
					e.printStackTrace();
					Logger.error("Could not upload physics with message: "+e.getMessage());
					return badRequest(e.getMessage());
				}
			}else{
				wrapper = new PhysicsWrapper();
				wrapper.setFile(file);				
			}
			Logger.info("Saving physics with name: \""+wrapper.getName()+"\"");
			wrapper.save();

		}else{
			Logger.error("Physics JAR not found.");	
			return badRequest("Physics JAR not found.");
		}

		return ok();

	}

}
