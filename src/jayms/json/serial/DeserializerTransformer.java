package jayms.json.serial;

import jayms.json.JSONObject;

public interface DeserializerTransformer<T> {

	T transform(JSONObject jsonObj);
	
}
