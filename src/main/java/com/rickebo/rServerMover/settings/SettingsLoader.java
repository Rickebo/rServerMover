package com.rickebo.rServerMover.settings;
import com.google.common.base.Charsets;
import com.rickebo.rServerMover.G;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import org.yaml.snakeyaml.representer.Representer;

import java.io.*;

public class SettingsLoader
{
	private String[] basePath;
	
	private File settingsFile;
	
	public SettingsLoader(String... basePaths) throws IOException
	{
		this.basePath = basePaths;
		
		for (String path : basePaths)
			this.settingsFile = this.settingsFile == null ? new File(path) : new File(this.settingsFile, path);
		
		this.settingsFile.mkdirs();
		this.settingsFile = new File(this.settingsFile, "config.yml");
		
		if (!this.settingsFile.exists())
		{
			this.settingsFile.createNewFile();
			copyResource("config.yml", this.settingsFile, true);
		}
	}
	
	private static String combine(String a, String b)
	{
		File af = new File(a);
		File bf = new File(af, b);
		
		return bf.getPath();
	}
	
	public Settings loadSettings() throws IOException
	{
		return readSettings(this.settingsFile, Settings.class);
	}
	
	private static void copyResource(String resourceName, File output, boolean overwrite) throws IOException
	{
		ClassLoader loader = G.class.getClassLoader();
		InputStream stream = loader.getResourceAsStream(resourceName);
		
		if (stream == null)
			throw new FileNotFoundException("Specified resource \"" + resourceName + "\" was not found.");
		
		try (FileOutputStream writer = new FileOutputStream(output))
		{
			byte[] buffer = new byte[0x1000];
			int length;
			
			while ((length = stream.read(buffer)) > 0)
				writer.write(buffer, 0, length);
		}
	}
	
	private static <T> T readSettings(File settingsFile, Class<T> base, Class<?>... subClasses)
			throws FileNotFoundException
	{
		InputStreamReader reader = new InputStreamReader(
				new FileInputStream(settingsFile), Charsets.UTF_8);
		
		PropertyUtils utils = new PropertyUtils();
		utils.setAllowReadOnlyProperties(true);
		
		Representer representer = new Representer();
		representer.setPropertyUtils(utils);
		
		SettingsConstructor constructor = new SettingsConstructor(base, subClasses);
		
		Yaml yaml = new Yaml(constructor, representer);
		return yaml.load(reader);
	}
}
