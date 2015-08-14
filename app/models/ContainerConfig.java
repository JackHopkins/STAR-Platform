package models;

import java.util.List;

public class ContainerConfig {
	String name;
	String maintainer;
	String physics;
	List<AgentConfig> agents;
}
class AgentConfig {
	String name;
	String[] effectors;
	String[] sensors;
	String mind;
	String body;
	String brain;
}