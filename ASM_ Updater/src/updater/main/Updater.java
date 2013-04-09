package updater.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.jar.JarFile;

import org.objectweb.asm.tree.ClassNode;

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
			//Load Analyzers
			this.loadAnalyzers();
			//Run Analyzers
			this.runAnalyzers();

		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	private void loadAnalyzers() {
		this.analyzers.add(new MLStringAnalyzer());
		this.analyzers.add(new NodeAnalyzer());
	}
	private void runAnalyzers() {
		//Iterate over
		for(ClassNode node : CLASSES.values()) {
			//Iterate over all of the loaded Analyzers
			for(AbstractAnalyzer analyzer : this.analyzers) {
				//Run the analyzer
				analyzer.run(node);
			}			
		}
	}
	
}








