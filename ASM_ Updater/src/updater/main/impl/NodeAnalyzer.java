package updater.main.impl;

import java.util.ListIterator;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import updater.main.generic.AbstractAnalyzer;

public class NodeAnalyzer extends AbstractAnalyzer {

	@Override
	protected boolean canRun(ClassNode node) {
		if(!node.superName.contains("java/lang/Object"))
			return false;
		int ownType = 0, longType = 0; 
		ListIterator<FieldNode> fnIt = node.fields.listIterator();
		while(fnIt.hasNext()) {
			FieldNode fn = fnIt.next();
			if((fn.access & Opcodes.ACC_STATIC) == 0) {
				if(fn.desc.equals(String.format("L%s;", node.name))) 
					ownType++;
				if(fn.desc.equals("J"))
					longType++;
			}
		}
		
		return ownType == 2 && longType ==1;
	}

	@Override
	protected void analyze(ClassNode node) {
		System.out.println("Found Node Class: " + node.name);
		ListIterator<FieldNode> fnIt = node.fields.listIterator();
		while(fnIt.hasNext()) {
			FieldNode fn = fnIt.next();
			if((fn.access & Opcodes.ACC_STATIC) == 0) {
				if(fn.desc.equals("J"))
					System.out.println("UID Field: " + fn.name);
			}
		}
	}

}
