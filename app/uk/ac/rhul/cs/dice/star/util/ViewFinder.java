package uk.ac.rhul.cs.dice.star.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import play.Logger;
import play.api.Play;
import uk.ac.rhul.cs.dice.star.entity.View;

public class ViewFinder {

	private final String appName;

	private static final char DOT = '.';

	private static final char SLASH = '/';

	private static final String CLASS_SUFFIX = ".rythm.html";
	
	private static final String PREFIX = "/app/assets/containers/";

	private static final String BAD_PACKAGE_ERROR = "Unable to get views from path '%s'. Are you sure the package '%s' exists?";

	public ViewFinder(String appName) {
		this.appName = appName;
	}

	public View findPublicView(String path, String name) {
		StringBuilder builder = new StringBuilder();
		try {
			builder.append(IOUtils.toString(Play.classloader(Play.current()).getResourceAsStream(path), "UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return new View(name, builder);
	}
	public Map<String, View> findViews(String scannedPackage, String suffix) {
	//	String scannedPath = scannedPackage.replace(DOT, SLASH);
		//URL scannedUrl = Thread.currentThread().getContextClassLoader().getResource(scannedPath);
		/*if (scannedUrl == null) {
			throw new IllegalArgumentException(String.format(BAD_PACKAGE_ERROR, scannedPath, scannedPackage));
		}*/
		File scannedDir = Play.getFile(scannedPackage, Play.current());
		
		if (scannedDir == null)
		{
			Logger.debug("Cannot find a directory at "+scannedPackage);
			return new HashMap<String, View>();
		}
		if (scannedDir.listFiles() == null)
		{
			Logger.debug("Cannot find a directory at "+scannedPackage);
			return new HashMap<String, View>();
		}
		
		Map<String, View> views = new HashMap<String, View>();
		for (File file : scannedDir.listFiles()) {
			
			views.putAll(findViews(file, scannedPackage, suffix));
		}
		return views;
	}

	private Map<String, View> findViews(File file, String scannedPackage, String suffix) {
		Map<String, View> views = new HashMap<String, View>();
		String resource = scannedPackage + DOT + file.getName();
		System.out.println(file.getName());
		if (file.isDirectory()) {
			for (File child : file.listFiles()) {
				views.putAll(findViews(child, resource, suffix));
			}
		} else if (resource.endsWith(CLASS_SUFFIX)) {
			
			int endIndex = resource.length() - CLASS_SUFFIX.length();
			String className = resource.substring(0, endIndex);
			
			try {
				views.put(className.substring(PREFIX.length()+appName.length()+1, className.length()), getViewFromFile(file, suffix));
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		return views;
	}

	public View getViewFromFile(File f, String suffix) throws IOException {
		final InputStream input = new FileInputStream(f);
		BufferedReader br = new BufferedReader(new InputStreamReader(input, "UTF-8"));
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			String className = f.getName();//appName.substring(0,f.getName().length());
			className = className.replace('/', '.');

			return new View(className, sb);

		} finally {
			br.close();
		}

	}
	
}
