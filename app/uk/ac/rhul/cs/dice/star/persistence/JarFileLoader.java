package uk.ac.rhul.cs.dice.star.persistence;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.io.IOUtils;

import play.api.Play;
import uk.ac.rhul.cs.dice.star.persistence.Resource;
import uk.ac.rhul.cs.dice.star.persistence.models.ResourceDB;
import uk.ac.rhul.cs.dice.star.entity.View;

import com.google.common.io.ByteStreams;



public final class JarFileLoader extends ClassLoader {
	private final JarFile file;
	private final String path;

	public JarFileLoader(File file) throws IOException {

		path = file.getAbsolutePath();
		this.file = new JarFile(file);
	}
	public static Resource getResource(ResourceDB rDB) {
		Resource resource = new Resource();
		resource.location = rDB.location;
		resource.name = rDB.name;
		
		return resource;
	}
	public Set<Class<?>> getByExtending(Class<?> type) throws MalformedURLException, ClassNotFoundException {
		Enumeration<?> e = file.entries();

		Set<Class<?>> classes = new HashSet<Class<?>>();
		
		URL[] urls = { new URL("jar:file:" + path+"!/") };
		URLClassLoader.newInstance(urls);

		while (e.hasMoreElements()) {
			JarEntry je = (JarEntry) e.nextElement();
			
			if(je.isDirectory() || !je.getName().endsWith(".class")){
				continue;
			}
			// -6 because of .class
			String className = je.getName().substring(0,je.getName().length()-6);
			className = className.replace('/', '.');
			Class<?> c =  Play.current().classloader().loadClass(className);
			if (type.isAssignableFrom(c)) {
				//This is the class we're looking for
				classes.add(c);
			}

		}
		return classes;
	}

	public Map<String, ResourceDB> getAllResources() throws ClassNotFoundException, IOException {

		Enumeration<?> e = file.entries();
		Map<String, ResourceDB> resources = new HashMap<String, ResourceDB>();
		URL[] urls = { new URL("jar:file:" + path+"!/") };
		URLClassLoader.newInstance(urls);

		while (e.hasMoreElements()) {
			JarEntry je = (JarEntry) e.nextElement();
			if(je.isDirectory() || je.getName().endsWith(".html") || je.getName().endsWith(".class")){
				continue;
			}
			//cl.getURLs();
			//	final String classAsPath = je.getName().replaceAll(suffix, "").replace('.', '\\')+suffix;
			final InputStream input = file.getInputStream(je);//getClass().getClassLoader().getResourceAsStream(classAsPath);
			
			
			
			/*BufferedReader br = new BufferedReader(new InputStreamReader(input, "UTF-8"));
			try {
				StringBuilder sb = new StringBuilder();
				String line = br.readLine();

				while (line != null) {
					sb.append(line);
					sb.append(System.lineSeparator());
					line = br.readLine();
				}*/
			String[] bits = je.getName().split("/");
			int parts = bits.length;
			int fileTypeLength = bits[parts-1].length();
			String className = je.getName().substring(je.getName().length()-fileTypeLength);
			className = className.replace('/', '.');
			
				 final File tempFile = File.createTempFile(System.currentTimeMillis()+"", "");
			        tempFile.deleteOnExit();
			        try (FileOutputStream out = new FileOutputStream(tempFile)) {
			            IOUtils.copy(input, out);
			        }
			      String location = se.digiplant.res.Res.put(tempFile);
			
			
		//	byte[] bytes = ByteStreams.toByteArray(input);
		//	Resource resource = new Resource(className, location);
			//resource.load(name);
			resources.put(className, new ResourceDB(se.digiplant.res.routes.ResAssets.at(location).toString(), className));
		}
		return resources;
	}
	
	public Map<String, View> getWithFileType(String suffix) throws ClassNotFoundException, IOException {
		Enumeration<?> e = file.entries();

		Map<String, View> views = new HashMap<String, View>();
		URL[] urls = { new URL("jar:file:" + path+"!/") };
		URLClassLoader.newInstance(urls);

		while (e.hasMoreElements()) {
			JarEntry je = (JarEntry) e.nextElement();
			if(je.isDirectory() || !je.getName().endsWith(suffix)){
				continue;
			}
			//cl.getURLs();
			//	final String classAsPath = je.getName().replaceAll(suffix, "").replace('.', '\\')+suffix;
			final InputStream input = file.getInputStream(je);//getClass().getClassLoader().getResourceAsStream(classAsPath);

			BufferedReader br = new BufferedReader(new InputStreamReader(input, "UTF-8"));
			try {
				StringBuilder sb = new StringBuilder();
				String line = br.readLine();

				while (line != null) {
					sb.append(line);
					sb.append(System.lineSeparator());
					line = br.readLine();
				}
				String className = je.getName().substring(0,je.getName().length()-suffix.length());
				className = className.replace('/', '.');

				views.put(className, new View(className, sb));

			} finally {
				br.close();
			}

			// -11 because of .scala.html

		}
		return views;
	}
	/*
	public Class getByExtending(Class<?> type, String id, JarType jarType) throws ClassNotFoundException, IOException, InstantiationException, IllegalAccessException {

		Package jar;
		if (jarType.equals(JarType.AGENT)) {
			jar = AgentJARWrapper.get(id);
		}else{
			 	jar = ApplicationJARWrapper.get(id);
		}

		Class foundEntryPoint = null;
		for (String e : jar.getEntries()) {
			JarElement element = JarElement.get(e);
			Class je = (Class)deserialize(element.getData());

		    // -6 because of .class
		    String className = je.getName().substring(0,je.getName().length()-6);
		    className = className.replace('/', '.');
		    Class<?> c = loadClass(className);
		    if (type.isAssignableFrom(c)) {
		    	//This is the class we're looking for
		    	foundEntryPoint = c;
		    }
		    //Keep loading classes onto the path
		}
		return foundEntryPoint;
	}
	public Class getByExtending(Class<?> type, Enumeration<JarEntry> entries) throws ClassNotFoundException, IOException, InstantiationException, IllegalAccessException {

		Class foundEntryPoint = null;
		while (entries.hasMoreElements()) {
			JarEntry je = entries.nextElement();
			 if(je.isDirectory() || !je.getName().endsWith(".class")){
		            continue;
		        }
		    // -6 because of .class
			// ClassLoader.getSystemClassLoader().;


		    String className = je.getName().substring(0,je.getName().length()-6);
		    className = className.replace('/', '.');
		    try 
            {
                Class<?> c = Class.forName(className);
                if (type.isAssignableFrom(c)) {
    		    	//This is the class we're looking for
    		    	return c;
    		    }
            }
            catch (Throwable e) 
            {
                System.out.println("WARNING: failed to instantiate " + className + " from " + file);
            }

		    //Keep loading classes onto the path
		}
		return null;
	}
	 */

	private static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
		ByteArrayInputStream b = new ByteArrayInputStream(bytes);
		ObjectInputStream o = new ObjectInputStream(b);

		return o.readObject();
	}
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		JarEntry entry = this.file.getJarEntry(name.replace('.', '/') + ".class");
		if (entry == null) {
			throw new ClassNotFoundException(name);
		}
		try {
			byte[] array = new byte[1024];
			InputStream in = this.file.getInputStream(entry);
			ByteArrayOutputStream out = new ByteArrayOutputStream(array.length);
			int length = in.read(array);
			while (length > 0) {
				out.write(array, 0, length);
				length = in.read(array);
			}
			return defineClass(name, out.toByteArray(), 0, out.size());
		}
		catch (IOException exception) {
			throw new ClassNotFoundException(name, exception);
		}
	}
}