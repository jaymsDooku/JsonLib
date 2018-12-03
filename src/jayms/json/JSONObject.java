package jayms.json;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

public class JSONObject extends LinkedHashMap<String, Object> implements JSONComponent {

	public JSONObject() {
	}
	
	public JSONObject(Object o) {
		if (o instanceof JSONObject) {
			HashMap<String, Object> map = (HashMap<String, Object>) o;
			for (Entry<String, Object> entry : map.entrySet()) {
				put(entry.getKey(), entry.getValue());
			}
			return;
		}
		if (o.getClass().isArray()) {
			throw new IllegalArgumentException("Tried to pass array to JSONObject! Use JSONArray for arrays!");
		}
		if (o.getClass().isPrimitive() || o instanceof String) {
			throw new IllegalArgumentException("Tried to pass primitive type or string to JSONObject!");
		}
		Field[] fields = Utils.getAllFields(o, false);
		for (Field f : fields) {
			String key = f.getName();
			Object value = null;
			try {
				f.setAccessible(true);
				value = f.get(o);
			} catch(Exception e) {
				e.printStackTrace();
			}
			put(key, value);
		}
	}
	
	public JSONObject(JSONObject o) {
		this((HashMap<String, Object>)o);
	}
	
	public JSONObject(HashMap<String, Object> map) {
		for (Entry<String, Object> entry : map.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}
	
	public UUID getUUID(Object key) {
		Object resultObj = this.get(key);
		if (resultObj == null) {
			return null;
		}
		if (!(resultObj instanceof String) && !(resultObj instanceof UUID)) {
			return null;
		}
		return resultObj instanceof UUID ? ((UUID) resultObj) : UUID.fromString((String) resultObj);
	}
	
	public String getString(Object key) {
		Object resultObj = this.get(key);
		if (resultObj == null) {
			return null;
		}
		if (!(resultObj instanceof String)) {
			return null;
		}
		return (String) resultObj;
	}
	
	public double getDouble(Object key) {
		Object resultObj = this.get(key);
		if (resultObj == null) {
			return Double.NaN;
		}
		if (!(resultObj instanceof Double)) {
			return Double.NaN;
		}
		return (double) resultObj;
	}
	
	public boolean getBoolean(Object key) {
		Object resultObj = this.get(key);
		if (resultObj == null) {
			return false;
		}
		if (!(resultObj instanceof String) && !(resultObj instanceof Boolean)) {
			return false;
		}
		
		return resultObj instanceof String ? Boolean.parseBoolean((String) resultObj) : (boolean) resultObj;
	}
	
	public <T> void replaceAsObject(String key, Class<T> clazz) {
		Object obj = get(key);
		if (!(obj instanceof JSONObject)) {
			throw new IllegalArgumentException("Tried to replace as object at key: " + key + " but that is not a JSONObject!");
		}
		put(key, ((JSONObject) obj).toObject(clazz));
	}
	
	public <T> T toObject(Class<T> clazz) {
		try {
			T result = clazz.newInstance();
			Field[] fields = Utils.getNonStaticFields(clazz);
			for (int i = 0; i < fields.length; i++) {
				Field f = fields[i];
				String fn = f.getName();
				if (!this.containsKey(fn)) {
					System.out.println("Doesn't contain everything!");
					return null;
				}
				f.setAccessible(true);
				Object toSet = get(fn);
				//TODO HANDLE JSONOBJECT
				f.set(result, toSet);
			}
			return result;
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public String toJSONString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		Set<Entry<String, Object>> entries = this.entrySet();
		int iteration = 1;
		for (Entry<String, Object> entry : entries) {
			String toAppend = "\"" + entry.getKey() + "\":";
			Object value = entry.getValue();
			toAppend += Utils.toJSONString(value);
			if (!(iteration >= entries.size())) {
				toAppend += ",";
			}
			sb.append(toAppend);
			iteration++;
		}
		sb.append("}");
		return sb.toString();
	}
	
}
