package orpg.editor.util;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import com.artemis.Component;
import java.io.File;

import orpg.shared.data.annotations.Attachable;
import orpg.shared.data.component.AttachableComponentDescriptor;

public class Reflection {

	public static Set<AttachableComponentDescriptor> getAttachableComponentDescriptors() {
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();

		Set<AttachableComponentDescriptor> classes = new TreeSet<AttachableComponentDescriptor>();

		String[] componentPackages = new String[] { "orpg.shared.data.component" };

		// Iterate through each package
		String path;
		for (String componentPackage : componentPackages) {
			path = componentPackage.replace('.', '/');
			try {
				Enumeration<URL> resources = classLoader
						.getResources(path);
				File directory;
				// Load all attachable classes in the package's directory
				while (resources.hasMoreElements()) {
					directory = new File(resources.nextElement().getFile());
					if (directory.isDirectory()) {
						addAttachableClasses(directory,
								"orpg.shared.data.component", classes);
					}
				}
			} catch (IOException e) {
			}
		}

		return classes;
	}

	private static void addAttachableClasses(File directory,
			String packageName, Set<AttachableComponentDescriptor> classes) {
		File[] files = directory.listFiles();
		Class clazz;
		Annotation annotation;

		for (File file : files) {
			if (file.isDirectory()) {
				// Recurse down
				addAttachableClasses(file,
						packageName + "." + file.getName(), classes);
			} else {
				// If it is a class, load it
				if (file.getName().endsWith(".class")) {
					try {
						clazz = Class.forName(packageName
								+ "."
								+ file.getName().substring(0,
										file.getName().length() - 6));
						if (Component.class.isAssignableFrom(clazz)
								&& (annotation = clazz
										.getAnnotation(Attachable.class)) != null) {
							classes.add(new AttachableComponentDescriptor(
									(Attachable) annotation, clazz));
						}
					} catch (ClassNotFoundException e) {
					}
				}
			}
		}

		return;
	}

}
