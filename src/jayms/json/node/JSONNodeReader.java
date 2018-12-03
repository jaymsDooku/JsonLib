package jayms.json.node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;

import jayms.json.JSONObject;

public class JSONNodeReader {

	public JSONNodeReader() {
	}
	
	public JSONNode readJSONNode(JSONObject jsonObj) {
		JSONNode result = new JSONNode(null, "root", new LinkedHashMap<String, JSONNode>());
		jsonObj = ensureJSON(jsonObj);
		ArrayList<JSONNode> nodes = readNodes(result, jsonObj);
		for (JSONNode n : nodes) {
			result.addChild(n);
		}
		return result;
	}
	
	private JSONObject ensureJSON(JSONObject jsonObj) {
		for (Entry<String, Object> entry : jsonObj.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			if (!(value.getClass().isPrimitive()) && !(value instanceof String) && !(value instanceof JSONObject)) {
				jsonObj.put(key, new JSONObject(value));
			}
		}
		return jsonObj;
	}
	
	private ArrayList<JSONNode> readNodes(JSONNode parent, JSONObject jsonObj) {
		ArrayList<JSONNode> result = new ArrayList<>();
		Set<Entry<String, Object>> entries = jsonObj.entrySet();
		Iterator<Entry<String, Object>> it = entries.iterator();
		while (it.hasNext()) {
			Entry<String, Object> entry = it.next();
			String key = entry.getKey();
			Object value = entry.getValue();
			JSONNode node = null;
			if (value instanceof JSONObject) {
				node = new JSONNode(parent, key, new LinkedHashMap<String, JSONNode>());
				ArrayList<JSONNode> children = readNodes(node, (JSONObject) value);
				for (JSONNode n : children) {
					node.addChild(n);
				}
			}else {
				node = new JSONNode(parent, key, value);
			}
			result.add(node);
		}
		return result;
	}
}
