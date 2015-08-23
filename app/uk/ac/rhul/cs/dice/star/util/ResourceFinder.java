package uk.ac.rhul.cs.dice.star.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;

import play.Logger;
import play.api.Play;
import uk.ac.rhul.cs.dice.star.persistence.Resource;
import uk.ac.rhul.cs.dice.star.persistence.models.ResourceDB;
import uk.ac.rhul.cs.dice.star.entity.View;

import com.google.common.io.ByteStreams;

public class ResourceFinder {

	//private final String container;

	private static final char DOT = '.';

	private static final char SLASH = '/';

	private static final String RESOURCE_REGEX = ".*\\.(css|js)";
	private static final String SPLIT_REGEX = "(css|js)";

	private static final String BAD_PACKAGE_ERROR = "Unable to get resources from path '%s'. Are you sure the package '%s' exists?";

	public static Resource findPublicResource(String path, String name) {
		System.out.println(controllers.routes.Assets.at(path).toString());
		return new Resource(controllers.routes.Assets.at(path).toString(), name);
	}
	@Deprecated
	public static Map<String, ResourceDB> findResources(String scannedPackage) {
	
		File scannedDir = Play.getFile(scannedPackage, Play.current());
		if (scannedDir == null) return new HashMap<String, ResourceDB>();
		if (scannedDir.listFiles() == null) return new HashMap<String, ResourceDB>();
		//File scannedDir = new File(scannedUrl.getFile());
		Map<String, ResourceDB> resources = new HashMap<String, ResourceDB>();
		for (File file : scannedDir.listFiles()) {
			resources.putAll(findResources(file, scannedPackage));
		}
		return resources;
	}
	@Deprecated
	private static Map<String, ResourceDB> findResources(File file, String scannedPackage) {
		Map<String, ResourceDB> views = new HashMap<String, ResourceDB>();
		String resource = scannedPackage + DOT + file.getName();
		if (file.isDirectory()) {
			for (File child : file.listFiles()) {
				views.putAll(findResources(child, resource));
			}
		} else if (resource.matches(RESOURCE_REGEX)) {
			Logger.debug(resource);
			int endIndex = resource.split(SPLIT_REGEX)[0].length()-1;
			String className = resource.substring(0, endIndex);
			try {
				views.put(className, getResourceFromFile(file));
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		return views;
	}

	@Deprecated
	public static ResourceDB getResourceFromFile(File file) throws IOException {
		final InputStream input = new FileInputStream(file);//getClass().getClassLoader().getResourceAsStream(classAsPath);

		String[] bits = file.getName().split("/");
		int parts = bits.length;
		int fileTypeLength = bits[parts-1].length();
		String className = file.getName().substring(file.getName().length()-fileTypeLength);
		className = className.replace('/', '.');
		byte[] bytes = ByteStreams.toByteArray(input);
		//ResourceDB resource = new ResourceDB(className, "");
		//resource.load(container);
		return null;
	}

	
}
