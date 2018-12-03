package jayms.json.serial;

import java.io.File;

import jayms.json.JSONComponent;
import jayms.json.JSONFile;

public class JSONSerializer {

	private JSONFile file;
	
	public JSONSerializer(File file) {
		this(new JSONFile(file));
	}
	
	public JSONSerializer(JSONFile file) {
		this.file = file;
	}
	
	public <T> void serialize(T obj, SerializerTransformer<T> transformer) {
		JSONComponent component = transformer.transform(obj);
		file.write(component);
	}
}
