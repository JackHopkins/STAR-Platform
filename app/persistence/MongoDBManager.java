package persistence;

import java.io.File;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.List;









import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import com.mongodb.Mongo;
import com.mongodb.MongoURI;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;

import models.users.UserType;


public class MongoDBManager {

	private static MongoDBManager instance;
	public Mongo mongo;
	public Morphia morphia;
	public Datastore dataStore;
	private String user = "Jasper";
	private char[] pass = new String("ignite").toCharArray();
	
	private boolean exists = false;

	protected MongoDBManager() {	   
		try {
			tryRemote();

		} catch (Exception e) {
			tryLocal();
		}


	}

	private void tryLocal() {
		System.out.println("Cannot connect to remote database. Trying locally.");


		try {
			mongo = new Mongo();
			morphia = new Morphia();
			//morphia.map(Site.class).
		

			dataStore = morphia.createDatastore(mongo, "lash-new");
			dataStore.ensureIndexes();

		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.err.println("Please start the Mongo Daemon.");
		}
	}

	private void tryRemote() throws UnknownHostException {
		String textUri;
		try {
		textUri = ConfigFactory.load().getString("mongodb");
		}catch(ConfigException e) {
			Config config = ConfigFactory.parseFile(new File("conf/application.conf")).resolve();
			textUri = config.getString("mongodb");
		}
		MongoURI uri = new MongoURI(textUri);



		mongo = new Mongo(uri);
		
		morphia = new Morphia();
		//morphia.map(Site.class).

		dataStore = morphia.createDatastore(mongo, "lash-new", user, pass);
		dataStore.ensureIndexes();
	}

	public static MongoDBManager getInstance() {		   
		if(instance == null) {
			instance = new MongoDBManager();
		}
		return instance;
	}

	public boolean exists()
	{
		return exists;
	}
	

}
