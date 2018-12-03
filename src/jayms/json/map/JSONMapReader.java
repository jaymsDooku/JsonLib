package jayms.json.map;

import jayms.json.JSONObject;

public final class JSONMapReader {
	
	private JSONMapReader() {
	}
	
	public static JSONMap readFromObject(JSONObject obj) {
		return new JSONMap("root", obj);
	}
}
