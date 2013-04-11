package updater.main.utils;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

public class JarUtils {

	public HashMap<String, ClassNode> parseJar(JarFile jar) {
		HashMap<String, ClassNode> classes = new HashMap<String, ClassNode>();
		try {
			Enumeration<?> enumeration = jar.entries();
			// Iterate over every file in the jar
			while (enumeration.hasMoreElements()) {
				JarEntry entry = (JarEntry) enumeration.nextElement();
				// If this file isn't a class, it will be skipped.
				if (entry.getName().endsWith(".class")
						&& !entry.getClass().getPackage().getName().contains(
								"jag")
						&& !entry.getClass().getPackage().getName().contains(
								"jac")) {
					// Will Parse the class file
					ClassReader classReader = new ClassReader(jar
							.getInputStream(entry));
					ClassNode classNode = new ClassNode();
					classReader.accept(classNode, ClassReader.SKIP_DEBUG
							| ClassReader.SKIP_FRAMES);
					// Add the file to the HashSet
					classes.put(classNode.name, classNode);
				}
			}
			jar.close();
			return classes;
		} catch (Exception e) {
			return null;
		}
	}
}
