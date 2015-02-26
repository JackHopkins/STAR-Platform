package persistence;

public interface ObjectCache {
	public void add(String key) throws Exception;
	public void refresh(String key);
	public void delete(String key);
}
