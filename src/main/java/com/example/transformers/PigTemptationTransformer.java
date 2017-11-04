package com.example.transformers;

import org.cyberpwn.classtweaker.ClassTweakerHost;
import org.cyberpwn.classtweaker.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

// Implement org.cyberpwn.classtweaker.IClassTransformer
public class PigTemptationTransformer implements IClassTransformer
{
	public byte[] transform(String className, byte[] bytes)
	{
		// Let's get the package version
		// Since we are supporting 1.9.X - 1.12.X
		String version = ClassTweakerHost.getVersion();

		// Ensure that we are working with the EntityPig from v1_12_R1
		if(className.equals("net.minecraft.server." + version + ".EntityPig"))
		{
			// Define our search parameters
			String itemsClass = "net/minecraft/server/" + version + "/Items";
			String itemsDesc = "Lnet/minecraft/server/" + version + "/Item;";
			String itemsName = "CARROT_ON_A_STICK";

			// Define our replacement (for carrot on a stick) temptation item
			String itemsReplacementName = "DIAMOND";

			// Begin working with the classnode
			ClassNode node = new ClassNode();

			// Read in the class from the given bytes (be sure to expand frames)
			ClassReader classReader = new ClassReader(bytes);
			classReader.accept(node, ClassReader.EXPAND_FRAMES);

			// Loop through all of the classes methods
			for(Object i : node.methods)
			{
				MethodNode method = (MethodNode) i;

				// If the method is named "r", then we are at the goal selector method
				if(method.name.equals("r"))
				{
					// Loop through all method instructions to find
					// "goalSelector.a(4, new PathfinderGoalTempt(... Items.CARROT_ON_A_STICK,...
					for(AbstractInsnNode j : method.instructions.toArray())
					{
						if(j.getOpcode() == Opcodes.GETSTATIC)
						{
							FieldInsnNode finsnNode = (FieldInsnNode) j;
							// If the opcode matches what we are looking for...
							// And it's owner (Items.class) matches
							// And it's name matches (CARROT_ON_A_STICK)
							// And it's method description matches (Item.class)
							if(finsnNode.owner.equals(itemsClass) && finsnNode.name.equals(itemsName) && finsnNode.desc.equals(itemsDesc))
							{
								// Replace the name with our replacement name (DIAMOND)
								finsnNode.name = itemsReplacementName;

								// Break out, stop searching. We're done here
								break;
							}
						}
					}
				}
			}

			// Begin writing the class back to the classnode
			// Be sure to compute maxs since we diddnt define them here
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			node.accept(cw);

			// Return our modified class bytes
			return cw.toByteArray();
		}

		// The class given does not need to be transformed,
		// Return the bytes given
		return bytes;
	}
}
