package jayms.json.serial;

import java.io.File;

import jayms.json.JSONFile;

public class JSONDeserializer {

	private JSONFile file;
	
	public JSONDeserializer(File file) {
		this(new JSONFile(file));
	}
	
	public JSONDeserializer(JSONFile file) {
		this.file = file;
	}
	
	public <T> T deserialize(DeserializerTransformer<T> transformer) {
		return transformer.transform(file.read());
	}
}
