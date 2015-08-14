package uk.ac.rhul.cs.dice.star.persistence.models;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.bson.types.ObjectId;

import exceptions.PluginInstantiationException;
import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;
import uk.ac.rhul.cs.dice.star.persistence.JarFileLoader;
import uk.ac.rhul.cs.dice.star.physics.Physics;

@Entity
public abstract class Package extends Model{
	
	@Constraints.Required
	private String name;
	@Id
	private Long id = System.currentTimeMillis();
	
	private boolean verified = false;
	
	@Formats.DateTime(pattern="dd/MM/yyyy")
	public Date date = new Date();
	
	public Package() {
		
	}
	public Package(String name) {
		this.name = name;
	
	}
	public Package(File file, String name) {
		
		/*JarFileLoader loader;
		try {	
		//	System.out.println("NAME "+file.getName());
			loader = new JarFileLoader(file);
			Class clazz = loader.getByExtending(Physics.class).iterator().next();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			throw new PluginInstantiationException();
		}*/
		setFile(file);
		
		this.name = name;
	}
	public Package(File file) {
		setFile(file);
	}


	//545e66da5d4d12bd1e239aff
	public File getFile() {
		return se.digiplant.res.Res.get(id+".","default");
	}
	public void setFile(File file) {
		se.digiplant.res.Res.put(file, "default", id+"");
	}
	

	public void setName(String name) {
		this.name = name;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}

	public boolean isVerified() {
		return verified;
	}
	public void setVerified(boolean verified) {
		this.verified = verified;
	}


}


