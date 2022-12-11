package appli.plateform;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import appli.interfaces.ITetrisGame;

public class Loader {

	private static Map<String,DescripteurPlugin> plugins = new HashMap<String,DescripteurPlugin>();
	private String path;
	
	public static void main(String[] args) throws Exception {
		Loader loader = new Loader();
		ITetrisGame app = (ITetrisGame) loader.getConfiguredPlugin("appli.interfaces.ITetrisGame");
	}
	
	public Loader() throws Exception {	
		this.path = new File("").getAbsoluteFile().getAbsolutePath().concat(File.separator + "ressource");
		try {
			this.getPluginsToMap(path);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	private Map<String,DescripteurPlugin> getPluginsToMap(String path) throws IOException, ParseException {
		JsonObject jsonObject = getJsonObject(path, "/plugins.json");

		Set<Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
		for (Map.Entry<String, JsonElement> entry : entrySet) {
			JsonObject entryObject = entry.getValue().getAsJsonObject();
			String name = entryObject.get("name").getAsString();		
			String classname = entryObject.get("class").getAsString();
			String description = entryObject.get("description").getAsString();
			String type = entryObject.get("type").getAsString();
			
			List<String> dependencies = new ArrayList<String>();
			entryObject.get("dependencies").getAsJsonArray().forEach(item -> {
				dependencies.add(item.getAsJsonObject().get("interface").getAsString());

			});
			DescripteurPlugin descripteur = new DescripteurPlugin(name, classname, description, dependencies, type);
			System.out.println(descripteur);
			plugins.put(name, descripteur);
		}
		return plugins;
	}

	public JsonObject getJsonObject(String path, String namefile) throws IOException, ParseException {
		FileReader pluginFileReader = new FileReader(path + namefile);		
		Gson gson = new Gson();
		JsonReader jsonReader = gson.newJsonReader(pluginFileReader);
		JsonObject jsonObject = gson.fromJson(jsonReader, JsonObject.class);
		return jsonObject;
	}
	
	public JsonObject getJsonObjectByName(String interfaceName, String namefile) throws IOException, ParseException {
		FileReader pluginFileReader = new FileReader(path + namefile);
		Gson gson = new Gson();
		JsonReader jsonReader = gson.newJsonReader(pluginFileReader);
		JsonObject jsonObject = gson.fromJson(jsonReader, JsonObject.class);
		JsonObject interfaceJson = jsonObject.get(interfaceName).getAsJsonObject();
		return interfaceJson;
	}

	public Object getConfiguredPlugin(String interfaceName) throws Exception {
		return this.getPluginFromJson(interfaceName, getJsonObjectByName(interfaceName, "/autorun.json"));
	}
	
	private Object getPluginFromJson(String interfaceName, JsonObject interfaceJson) throws Exception {
		String pluginName = interfaceJson.get("name").getAsString();
		DescripteurPlugin descriptorPlugin = Loader.plugins.get(pluginName);
		if (descriptorPlugin.getDependencies().size() == 0) {
			return Loader.getPluginWithoutDependencies(descriptorPlugin);
		}

		JsonArray dependenciesJson = interfaceJson.get("dependencies").getAsJsonArray();
		ArrayList<Object> dependencies = new ArrayList<Object>();

		for (JsonElement dependencyJson : dependenciesJson) {
			JsonObject dependencyJsonObject = dependencyJson.getAsJsonObject();
			dependencies.add(this.getPluginFromJson(interfaceName, dependencyJsonObject));
		}

		return Loader.getPluginWithDependencies(descriptorPlugin, dependencies);
	}
	
	public static Object getPluginWithDependencies(DescripteurPlugin descriptorPlugin, List<Object> dependencies) {
		if (dependencies.size() == 0) {
			return Loader.getPluginWithoutDependencies(descriptorPlugin);
		}
		Class<?>[] dependenciesNames = new Class<?>[descriptorPlugin.getDependencies().size()];
		try {
			for (int i = 0; i < descriptorPlugin.getDependencies().size(); i++) {
				dependenciesNames[i] = Class.forName(descriptorPlugin.getDependencies().get(i));
			}
		} catch (ClassNotFoundException | SecurityException e) {
			e.printStackTrace();
			return null;
		}
		try {
			Object plugin = Class.forName(descriptorPlugin.getClassname()).getDeclaredConstructor(dependenciesNames).newInstance(dependencies.toArray());
			return plugin;
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Object getPluginWithoutDependencies(DescripteurPlugin pluginDescriptor) {

		try {
			System.out.println(pluginDescriptor.getClassname());
			Object plugin = Class.forName(pluginDescriptor.getClassname()).getDeclaredConstructor().newInstance();
			return plugin;
		} catch (Exception  e) {
			e.printStackTrace();
			return null;
		}
	}
	

}
