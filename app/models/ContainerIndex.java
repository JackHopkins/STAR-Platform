package models;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import uk.ac.rhul.cs.dice.star.container.AbstractContainer;

@Deprecated
public enum ContainerIndex {
	INSTANCE {
		private Map<String, AbstractContainer> containers = new ConcurrentHashMap<String, AbstractContainer>();
		@Override
		public Collection<AbstractContainer> getContainers() {
			return containers.values();
		}

		@Override
		public AbstractContainer getContainer(String id) {
			return containers.get(id);
		}

		@Override
		public void registerContainer(AbstractContainer container) {
			containers.put(container.getId(), container);
		}
		
	};
	
	public abstract Collection<AbstractContainer> getContainers();
	public abstract AbstractContainer getContainer(String id);
	public abstract void registerContainer(AbstractContainer container);
}
