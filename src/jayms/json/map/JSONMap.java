package jayms.json.map;

import jayms.json.JSONObject;
import jayms.json.Utils;

public class JSONMap {

	private String key;
	private Object value;
	
	public JSONMap(String key, Object value) {
		this.key = key;
		this.value = value;
	}
	
	public boolean isTree() {
		return value instanceof JSONObject;
	}
	
	public JSONMap childMap(String key) {
		JSONObject tree = checkTree();
		Object obj = tree.get(key);
		checkNull(obj);
		if (obj instanceof JSONObject) {
			return new JSONMap(key, (JSONObject) obj);
		}else if (!obj.getClass().isPrimitive()) {
			return new JSONMap(key, new JSONObject(obj));
		}else {
			throw new IllegalArgumentException("Child node isn't a tree!");
		}
	}
	
	public double getDouble(String key) {
		Object result = getObject(key);
		checkNull(result);
		if (!(result instanceof String)) {
			throw new IllegalArgumentException("Failed to cast value to double.");
		}
		return (double) result;
	}
	
	public float getFloat(String key) {
		Object result = getObject(key);
		checkNull(result);
		if (!(result instanceof Float)) {
			throw new IllegalArgumentException("Failed to cast value to float.");
		}
		return (float) result;
	}
	
	public boolean getBoolean(String key) {
		Object result = getObject(key);
		checkNull(result);
		if (!(result instanceof Boolean)) {
			throw new IllegalArgumentException("Failed to cast value to boolean.");
		}
		return (boolean) result;
	}
	
	public char getChar(String key) {
		Object result = getObject(key);
		checkNull(result);
		if (!(result instanceof Character)) {
			throw new IllegalArgumentException("Failed to cast value to char.");
		}
		return (char) result;
	}
	
	public byte getByte(String key) {
		Object result = getObject(key);
		checkNull(result);
		if (!(result instanceof Byte)) {
			throw new IllegalArgumentException("Failed to cast value to byte.");
		}
		return (byte) result;
	}
	
	public long getLong(String key) {
		Object result = getObject(key);
		checkNull(result);
		if (!(result instanceof Long)) {
			throw new IllegalArgumentException("Failed to cast value to long.");
		}
		return (long) result;
	}
	
	public short getShort(String key) {
		Object result = getObject(key);
		checkNull(result);
		if (!(result instanceof Short)) {
			throw new IllegalArgumentException("Failed to cast value to short.");
		}
		return (short) result;
	}

	public int getInteger(String key) {
		Object result = getObject(key);
		checkNull(result);
		if (!(result instanceof Integer)) {
			throw new IllegalArgumentException("Failed to cast value to int.");
		}
		return (int) result;
	}
	
	public String getString(String key) {
		Object result = getObject(key);
		checkNull(result);
		if (!(result instanceof String)) {
			throw new IllegalArgumentException("Failed to cast value to string.");
		}
		return (String) result;
	}
	
	public Object getObject(String key) {
		return checkTree().get(key);
	}
	
	private JSONObject checkTree() {
		if (!isTree()) {
			throw new IllegalArgumentException("This JSONNode isn't a tree!");
		}
		return (JSONObject) value;
	}
	
	private void checkNull(Object value) {
		Utils.checkNull(value);
	}
	
	public String getKey() {
		return key;
	}
	
	public Object getValue() {
		return value;
	}
}
