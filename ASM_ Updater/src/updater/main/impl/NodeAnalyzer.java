package updater.main.impl;

import java.util.ListIterator;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

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
		String previousName = "";
		ListIterator<MethodNode> mnIt = node.methods.listIterator();
		methodIterator: while(mnIt.hasNext()) {
			MethodNode mn = mnIt.next();
			if((mn.access & Opcodes.ACC_STATIC) == 0) {
				ListIterator<AbstractInsnNode> ainIt = mn.instructions.iterator();
				while(ainIt.hasNext()) {
					AbstractInsnNode ain = ainIt.next();
					if(ain instanceof FieldInsnNode) {
						previousName = ((FieldInsnNode) ain).name;
						break methodIterator;
					}
				}
			}
		}
		System.out.println("Previous Field: " + previousName);
		if(previousName.length() > 0) {
			fnIt = node.fields.listIterator();
			while(fnIt.hasNext()) {
				FieldNode fn = fnIt.next();
				if((fn.access & Opcodes.ACC_STATIC) == 0) {
					if(fn.desc.equals(String.format("L%s;", node.name))
							&& !fn.name.equals(previousName)) {
						System.out.println("Next Field: " + fn.name);
					}
				}
			}
		}
	}
}
