package updater.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.jar.JarFile;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import updater.main.generic.AbstractAnalyzer;
import updater.main.impl.*;
import updater.main.utils.JarUtils;

public class Updater {

	public static HashMap<String, ClassNode> CLASSES = new HashMap<String, ClassNode>();
	private ArrayList<AbstractAnalyzer> analyzers = new ArrayList<AbstractAnalyzer>();
	JarUtils ju = new JarUtils();

	public Updater() {
		try {
			JarFile jar = new JarFile("runescape.jar");
			CLASSES = ju.parseJar(jar);
			// Load Analyzers
			// this.loadAnalyzers();
			// Run Analyzers
			// this.runAnalyzers();
			System.out.println("All Classes: ");
			File classes = new File("Classes.txt");
			classes.createNewFile();
			PrintWriter out = new PrintWriter(new FileWriter(classes));
			for (ClassNode node : CLASSES.values()) {
				if (!node.name.contains("jag") && !node.name.contains("jac")) {
					ListIterator<FieldNode> fnIt = node.fields.listIterator();
					out.println("");
					out.println("");
					out.println("Name: " + node.name);
					if (fnIt.hasNext()) {
						out.println(">>Fields (name, type, access):");
						while (fnIt.hasNext()) {
							FieldNode fn = fnIt.next();
							out.println(">>>>" + fn.name + ", " + fn.desc
									+ ", " + fn.access);
						}
					}
					ListIterator<MethodNode> mnIt = node.methods.listIterator();
					methodIterator: while (mnIt.hasNext()) {
						out.println(">>Methods (name, type, access):");
						while (mnIt.hasNext()) {
							MethodNode mn = mnIt.next();
							out.println(">>>>" + mn.name + ", " + mn.desc
									+ ", " + mn.access);
						}
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadAnalyzers() {
		this.analyzers.add(new MLStringAnalyzer());
		this.analyzers.add(new NodeAnalyzer());
	}

	private void runAnalyzers() {
		// Iterate over
		for (ClassNode node : CLASSES.values()) {
			// Iterate over all of the loaded Analyzers
			for (AbstractAnalyzer analyzer : this.analyzers) {
				// Run the analyzer
				analyzer.run(node);
			}
		}
	}

}
