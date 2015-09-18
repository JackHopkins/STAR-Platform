package models;

import java.io.File;
import java.util.Map;

import exceptions.ContainerNotFoundException;
import uk.ac.rhul.cs.dice.star.container.AbstractContainer;
import uk.ac.rhul.cs.dice.star.container.Container;

public interface Platform {

	public void registerContainer(String name, Object container);
	public Object createContainer(String name);
	public void loadAgent(String name, File file);
	public String createAgent(String container, String name) throws Exception;
	public void kill() throws ContainerNotFoundException;
}
