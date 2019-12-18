package com.rickebo.rServerMover.settings;

import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.nodes.Node;

import java.util.HashMap;

public class SettingsConstructor extends Constructor
{
	private HashMap<String, Class<?>> classMap = new HashMap<>();
	
	public SettingsConstructor(Class<? extends Object> theRoot, Class<? extends Object>... allClasses)
	{
		super(theRoot);
		
		classMap.put(theRoot.getName(), theRoot);
		for (Class<? extends Object> c : allClasses)
			classMap.put(c.getName(), c);
	}
	
	protected Class<?> getClassForNode(Node node)
	{
		String name = node.getTag().getClassName();
		Class<?> cl = classMap.get(name);
		if (cl == null)
			throw new YAMLException("Class not found: " + name);
		else
			return cl;
	}
}