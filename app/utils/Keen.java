package utils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import io.keen.client.java.JavaKeenClientBuilder;
import io.keen.client.java.KeenClient;
import io.keen.client.java.KeenProject;

public class Keen {

	private static String keenWrite;
	public static String keenRead;
	private static String keenMaster;
	public static String keenProjectId;
	private static KeenProject keenProject;
	private static Keen instance;
	protected Keen() {
		Config config = ConfigFactory.parseFile(new File("conf/application.conf")).resolve();
		keenWrite = config.getString("keen.write");
		keenRead = config.getString("keen.read");
		keenMaster = config.getString("keen.master");
		keenProjectId = config.getString("keen.projectId");
		
		KeenClient client = new JavaKeenClientBuilder().build();
		KeenClient.initialize(client);
		keenProject = new KeenProject(keenProjectId, keenWrite, keenRead);
		KeenClient.client().setDebugMode(true);
		client.setDefaultProject(keenProject);
	}
	public Keen getInstance() {
		if (instance == null) instance = new Keen();
		return instance;
	}
	
	public void fireEvent(IEventOntology type, Map<String, Object> data) {
		try {
		KeenClient.client().addEvent(type.name(), data);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void fireEvent(IEventOntology type, String name, Object value) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(name, value);
		fireEvent(type, map);
	}
	public void fireEvent(String type, Map<String, Object> data) {
		try {
		KeenClient.client().addEvent(type, data);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void fireEvent(String type, String name, Object value) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(name, value);
		fireEvent(type, map);
	}
}
