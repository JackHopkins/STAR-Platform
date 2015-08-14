package uk.ac.rhul.cs.dice.star.persistence.models;

import javax.persistence.Entity;

import uk.ac.rhul.cs.dice.star.persistence.Resource;

@Entity
public class ResourceDB {

	public String location;
	public String name;
	
	public ResourceDB() {
		
	}
	public ResourceDB(String location, String name) {
		this.location = location;
		this.name = name;
	}
	public String getLocation() {
		return location;
	}
	public String getName() {
		return name;
	}

}
