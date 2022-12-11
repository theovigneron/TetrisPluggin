package appli.plateform;

import java.util.List;

public class DescripteurPlugin {
	
	String name;
	String classname;
	String description;
	String type;
	List<String> dependencies;
	
	public DescripteurPlugin(String name, String classname, String description, List<String> dependencies,String type) {
		this.name = name;
		this.classname = classname;
		this.description = description;
		this.dependencies = dependencies;
		this.type = type;
	}

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getDependencies() {
		return dependencies;
	}

	public void setDependencies(List<String> dependencies) {
		this.dependencies = dependencies;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	

}
