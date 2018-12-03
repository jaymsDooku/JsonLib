package jayms.json.node;

import java.util.Collections;
import java.util.Map;

import jayms.json.Utils;

public class JSONNode {
	
	private JSONNode parent;
	private String name;
	private Object value;
	
	public JSONNode(JSONNode parent, String name, Object value) {
		this.parent = parent;
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}
	
	public Object getValue() {
		return value;
	}
	
	
	public void addChild(JSONNode JSONNode) {
		if (hasChild(JSONNode.getName())) {
			throw new IllegalArgumentException("A JSONNode with this name is aleady a child of this JSONNode!");
		}
		getChildNodes().put(JSONNode.getName(), JSONNode);
	}
	
	
	public void removeChild(String JSONNodeName) {
		if (hasChild(JSONNodeName)) {
			getChildNodes().remove(JSONNodeName);
		}
	}
	
	
	public boolean hasChild(String JSONNodeName) {
		if (!isParentNode()) {
			return false;
		}
		return getChildNodes().containsKey(JSONNodeName); 
	}
	
	
	public JSONNode childNode(String JSONNodeName) {
		if (!isParentNode()) {
			throw new IllegalArgumentException("Can't get child JSONNode because this JSONNode isn't a parent!");
		}
		Map<String, JSONNode> children = getChildNodes();
		if (!children.containsKey(JSONNodeName)) {
			throw new IllegalArgumentException("Can't get child JSONNode because this JSONNode isn't a child of this JSONNode!");
		}
		return children.get(JSONNodeName);
	}
	
	public double getDoubleValue() {
		Object result = getValue();
		checkNull(result);
		if (!(result instanceof String)) {
			throw new IllegalArgumentException("Failed to cast value to double.");
		}
		return (double) result;
	}
	
	public float getFloatValue() {
		Object result = getValue();
		checkNull(result);
		if (!(result instanceof Float)) {
			throw new IllegalArgumentException("Failed to cast value to float.");
		}
		return (float) result;
	}
	
	public boolean getBooleanValue() {
		Object result = getValue();
		checkNull(result);
		if (!(result instanceof Boolean)) {
			throw new IllegalArgumentException("Failed to cast value to boolean.");
		}
		return (boolean) result;
	}
	
	public char getCharValue() {
		Object result = getValue();
		checkNull(result);
		if (!(result instanceof Character)) {
			throw new IllegalArgumentException("Failed to cast value to char.");
		}
		return (char) result;
	}
	
	public byte getByteValue() {
		Object result = getValue();
		checkNull(result);
		if (!(result instanceof Byte)) {
			throw new IllegalArgumentException("Failed to cast value to byte.");
		}
		return (byte) result;
	}
	
	public long getLongValue() {
		Object result = getValue();
		checkNull(result);
		if (!(result instanceof Long)) {
			throw new IllegalArgumentException("Failed to cast value to long.");
		}
		return (long) result;
	}
	
	public short getShortValue() {
		Object result = getValue();
		checkNull(result);
		if (!(result instanceof Short)) {
			throw new IllegalArgumentException("Failed to cast value to short.");
		}
		return (short) result;
	}

	public int getIntegerValue() {
		Object result = getValue();
		checkNull(result);
		if (!(result instanceof Integer)) {
			throw new IllegalArgumentException("Failed to cast value to int.");
		}
		return (int) result;
	}
	
	public String getStringValue() {
		Object result = getValue();
		checkNull(result);
		if (!(result instanceof String)) {
			throw new IllegalArgumentException("Failed to cast value to string.");
		}
		return (String) result;
	}
	
	private void checkNull(Object value) {
		Utils.checkNull(value);
	}

	
	public JSONNode getParentJSONNode() {
		return parent;
	}

	
	public Map<String, JSONNode> getChildren() {
		return Collections.unmodifiableMap(getChildNodes());
	}
	
	private Map<String, JSONNode> getChildNodes() {
		if (!isParentNode()) {
			return null;
		}
		return (Map<String, JSONNode>) value;
	}
	
	public boolean isParentNode() {
		if (!(value instanceof Map)) {
			return false;
		}
		try {
			Map<String, JSONNode> map = (Map<String, JSONNode>) value;
			return true;
		}catch (Exception e) {
			return false;
		}
	}

	
	public boolean isChildNode() {
		return parent != null;
	}
}
