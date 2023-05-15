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

	// Une carte (Map) pour stocker les plugins chargés
	private static Map<String, DescripteurPlugin> plugins = new HashMap<String, DescripteurPlugin>();
	public String path;
	
	public Loader() throws Exception {
		this.path = new File("").getAbsoluteFile().getAbsolutePath().concat(File.separator + "ressource");
		try {
			// Charge les plugins à partir du chemin spécifié
			this.loaderPlugins(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void main(String[] args) throws Exception {
		// Crée une instance du Loader
		Loader loader = new Loader();
		// Obtient une instance de jeu de Tetris à partir du plugin configuré
		ITetrisGame app;
		app = (ITetrisGame) loader.getPluginFromJson("appli.interfaces.ITetrisGame",  jsonFileReader("/autorun.json", loader.path)
				.get("appli.interfaces.ITetrisGame").getAsJsonObject());
		app.startGame();
	}


	// Charge les plugins à partir du chemin spécifié et les stocke dans la carte (Map) 'plugins'
	private Map<String, DescripteurPlugin> loaderPlugins(String path) throws IOException, ParseException {
		JsonObject jsonContent = jsonFileReader("/plugins.json", path);

		// Obtient l'ensemble des entrées du JsonObject
		Set<Entry<String, JsonElement>> entrySet = jsonContent.entrySet();
		for (Map.Entry<String, JsonElement> entry : entrySet) {
			JsonObject entryObject = entry.getValue().getAsJsonObject();
			//Recuperation des specs des plugins
			String classname = entryObject.get("classname").getAsString();
			String description = entryObject.get("description").getAsString();
			String type = entryObject.get("type").getAsString();
			String nom = entryObject.get("nom").getAsString();

			// Obtient les dépendances du plugin à partir de l'objet JsonObject
			List<String> dependances = new ArrayList<String>();
			entryObject.get("dependances").getAsJsonArray().forEach(item -> {
				dependances.add(item.getAsJsonObject().get("interface").getAsString());
			});
			//Creation d'un descripteur de plugin permettant d'instancier un objet gardant toute les données.
			DescripteurPlugin descripteur = new DescripteurPlugin(nom, classname, description, dependances, type);
			// Ajoute le descripteur du plugin à la carte (Map) 'plugins'
            System.out.println("module "+nom+" chargé");
			plugins.put(nom, descripteur);
		}
		return plugins;
	}

    // Obtient un objet JsonObject à partir du fichier spécifié dans le chemin spécifié
    public static JsonObject jsonFileReader(String nomFichier, String path) throws IOException, ParseException {
        FileReader pluginFileReader = new FileReader(path + nomFichier);
        Gson gson = new Gson();
        JsonReader jsonReader = gson.newJsonReader(pluginFileReader);
        return gson.fromJson(jsonReader, JsonObject.class);
    }
    

    // Obtient un plugin à partir de l'objet JsonObject spécifique à un nom d'interface
    private Object getPluginFromJson(String interfaceName, JsonObject interfaceJson) throws Exception {
        DescripteurPlugin descripteurPlugin = Loader.plugins.get(interfaceJson.get("nom").getAsString());
        
        if (descripteurPlugin.getDependencies().size() == 0) {
            // S'il n'y a pas de dépendances, crée une instance de la classe du plugin
            return Class.forName(descripteurPlugin.getClassname()).getDeclaredConstructor().newInstance();
        }
    
        // S'il y a des dépendances, obtient les plugins correspondants récursivement
        JsonArray dependancesJson = interfaceJson.get("dependances").getAsJsonArray();
        ArrayList<Object> dependances = new ArrayList<Object>();
    
        for (JsonElement dependencyJson : dependancesJson) {
        	dependances.add(this.getPluginFromJson(interfaceName, dependencyJson.getAsJsonObject()));
        }
    
        // S'il n'y a aucune dépendance récupérée
        if (dependances.size() == 0) {
            return Class.forName(descripteurPlugin.getClassname()).getDeclaredConstructor().newInstance();
        }
    
        // Crée un tableau de classes correspondant aux dépendances du plugin
        Class<?>[] dependenciesNames = new Class<?>[descripteurPlugin.getDependencies().size()];
        try {
            for (int i = 0; i < descripteurPlugin.getDependencies().size(); i++) {
                dependenciesNames[i] = Class.forName(descripteurPlugin.getDependencies().get(i));
            }
            return Class.forName(descripteurPlugin.getClassname()).getDeclaredConstructor(dependenciesNames).newInstance(dependances.toArray());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
}