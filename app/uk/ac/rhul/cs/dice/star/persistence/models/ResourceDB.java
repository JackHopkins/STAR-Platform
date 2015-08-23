package uk.ac.rhul.cs.dice.star.persistence.models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import uk.ac.rhul.cs.dice.star.persistence.Resource;

@Entity
public class ResourceDB {

	@Id
	Long id;
	
	public String location;
	public String name;
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name="agent")
	AgentWrapper agent;
	
	public ResourceDB() {
		
	}
	public ResourceDB(String location, String name, AgentWrapper agent) {
		this.location = location;
		this.name = name;
		this.agent = agent;
	}
	public String getLocation() {
		return location;
	}
	public String getName() {
		return name;
	}

}
