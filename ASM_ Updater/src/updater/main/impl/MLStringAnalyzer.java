package updater.main.impl;

import java.util.ListIterator;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import updater.main.generic.AbstractAnalyzer;

public class MLStringAnalyzer extends AbstractAnalyzer {

	@Override
	protected boolean canRun(ClassNode node) {
		//Iterate through all of the methods in the class.
		ListIterator<MethodNode> mnIt = node.methods.listIterator();
		while(mnIt.hasNext()) {
			MethodNode mn = mnIt.next();
			//Check to see if the method is in the classes constructor
			if(mn.name.equals("<init>")) {
				//Check to see if the constructor takes 4 String as it's paramaters
				if(mn.desc.equals("(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V")) {
					return true;
				}
			}
		}	
		return false;
	}

	@Override
	protected void analyze(ClassNode node) {
		System.out.println(String.format("%s is MLString"));
	}

}
