package jayms.json.serial;

import jayms.json.JSONObject;

public interface SerializerTransformer<T> {

	JSONObject transform(T obj);
	
}
