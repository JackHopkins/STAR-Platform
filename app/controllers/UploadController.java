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
	
	public static Result deleteAgent() {
		Map<String, String[]> parameters = request().queryString();
		AgentWrapper.deleteByName(parameters.get(NAME)[0]);
		Logger.debug("Deleting agent with name: "+parameters.get(NAME)[0]);
		return getAgents();
	}
	public static Result getAgents() {
		List<AgentWrapper> agents = AgentWrapper.getAgents();
		JSONArray arr = new JSONArray();
		for (AgentWrapper agent : agents) {
			JSONObject pack = new JSONObject();
			try {
				pack.put("name",agent.getName());
				pack.put("uploaded", agent.date.toLocaleString());
				arr.put(pack);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return ok(arr.toString());
	}
	public static Result getPhysics() {
		List<PhysicsWrapper> physics = PhysicsWrapper.getPhysicsList();
		Logger.debug(physics.size()+"");
		
		JSONArray arr = new JSONArray();
		for (PhysicsWrapper physic : physics) {
			JSONObject pack = new JSONObject();
			try {
				pack.put("name",physic.getName());
				pack.put("uploaded", physic.date.toLocaleString());
				arr.put(pack);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return ok(arr.toString());
	}

	public static Result uploadAgent() {
		File file;
		Map<String, String[]> parameters;
		try {
		file = request().body().asRaw().asFile();
		parameters = request().queryString();
		}catch(NullPointerException e) {
		file = request().body().asMultipartFormData().getFiles().get(0).getFile();
		parameters = request().body().asMultipartFormData().asFormUrlEncoded();
		}
		
	

		if (file != null) {
			AgentWrapper wrapper;
			if (parameters.containsKey(NAME)) {
				try {
					wrapper = new AgentWrapper(file, parameters.get(NAME)[0]);
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
		File file;
		Map<String, String[]> parameters;
		try {
		file = request().body().asRaw().asFile();
		parameters = request().queryString();
		}catch(NullPointerException e) {
		file = request().body().asMultipartFormData().getFiles().get(0).getFile();
		parameters = request().body().asMultipartFormData().asFormUrlEncoded();
		}
		
		Logger.debug("Physics uploading");
		
		if (file != null) {
			PhysicsWrapper wrapper;
			if (parameters.containsKey(NAME)) {
				try {
					wrapper = new PhysicsWrapper(file, parameters.get(NAME)[0]);
					
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
		Logger.debug("Currently there are: "+PhysicsWrapper.getPhysicsList().size()+" physics stored.");
		return ok();

	}

}
