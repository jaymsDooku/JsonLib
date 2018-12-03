package jayms.json;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Set;

public final class Utils {
	
	public class Tuple<A, B> {
		
		private A a;
		private B b;
		
		public Tuple(A a, B b) {
			this.a = a;
			this.b = b;
		}
		
		public void setA(A a) {
			this.a = a;
		}
		
		public void setB(B b) {
			this.b = b;
		}
		
		public A getA() {
			return a;
		}
		
		public B getB() {
			return b;
		}
	}

	public static String toJSONString(Object value) {
		String result = "";
		if (value == null) {
			result = "null";
		}else if (value instanceof Boolean) {
			result = ((boolean) value) ? "true" : "false";
		}else if (value instanceof String) {
			result += "\"" + (String) value + "\"";
		}else if (value instanceof Character) {
			result += "\"" + ((Character) value).toString() + "\"";
		}else if (value instanceof Number) {
			result += value;
		}else if (value.getClass().isArray()) {
			result += new JSONArray(convertObjectToArray(value)).toJSONString();
		}else if (value instanceof JSONArray) {
			result += ((JSONArray) value).toJSONString();
		}else {
			result += new JSONObject(value).toJSONString();
		}
		return result;
	}
	
	public static String[] bools = { "false", "true" };
	public static String doubleRegExp = "[\\x00-\\x20]*[+-]?(((((\\p{Digit}+)(\\.)?((\\p{Digit}+)?)([eE][+-]?(\\p{Digit}+))?)|(\\.((\\p{Digit}+))([eE][+-]?(\\p{Digit}+))?)|(((0[xX](\\p{XDigit}+)(\\.)?)|(0[xX](\\p{XDigit}+)?(\\.)(\\p{XDigit}+)))[pP][+-]?(\\p{Digit}+)))[fFdD]?))[\\x00-\\x20]*";
	public static JSONParser parser = new JSONParser();
	
	public static Object fromJSONString(String json) {
		if (startsAndEnds(json, '"', '"')) {
			return json.substring(1, json.length()-1);
		}else if (isNumeric(json)) {
			if (json.contains(Character.toString(getDecimalSeperator()))) {
				if (json.matches(doubleRegExp)) {
					return Double.parseDouble(json);
				}else {
					return Float.parseFloat(json);
				}
			}
			BigInteger bigInt = new BigInteger(json);
			long val = bigInt.longValue();
			if (val < Integer.MAX_VALUE) {
				return bigInt.intValue();
			}else {
				return val;
			}
		}else if (json.equals("false") || json.equals("true")) {
			return json.equals("true") ? true : false;
		}else if (json.equals("null")) {
			return null;
		}else if (startsAndEnds(json, '{', '}')) {
			return parser.parseJSONObject(json);
		}else if (startsAndEnds(json, '[', ']')) {
			return parser.parseJSONArray(json);
		}else {
			System.out.println("THROWING: " + json);
			throw new IllegalArgumentException("In-valid JSON parsed!");
		}
	}
	
	public static void invalidJSON() {
		throw new IllegalArgumentException("In-valid JSON parsed!");
	}
	
	public static char getMinusSign() {
		DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
		return symbols.getMinusSign();
	}
	
	public static char getDecimalSeperator() {
		DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
		return symbols.getDecimalSeparator();
	}
	
	public static boolean isNumeric(String str) {
		
		char minusSymbol = getMinusSign();
		
		if (str.charAt(0) == minusSymbol) {
			str = str.substring(1, str.length());
		}
		
		boolean dpFound = false;
		char dpChar = getDecimalSeperator();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c == dpChar) {
				if (dpFound) {
					return false;
				}
				continue;
			}
			if (!Character.isDigit(c)) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean startsAndEnds(String str, char start, char end) {
		if (str == null || str == "") {
			return false;
		}
		return str.charAt(0) == start && str.charAt(str.length()-1) == end;
	}
	
	public static Object[] convertObjectToArray(Object o) {
		if (o.getClass().isArray()) {
			Class ofArray = o.getClass().getComponentType();
			if (ofArray.isPrimitive()) {
				ArrayList ar = new ArrayList();
				for (int i = 0; i < Array.getLength(o); i++) {
					ar.add(Array.get(o, i));
				}
				return ar.toArray();
			}else {
				return (Object[]) o;
			}
		}else {
			throw new IllegalArgumentException("Tried to pass non-array object to 'convertObjectToArray(Object o)' method!");
		}
	}
	
	public static Field[] getAllFields(Object o, boolean includeStatic) {
		Class<?> objClass = o.getClass();
		Field[] objFields = getNonStaticFields(objClass);
		Class<?> current = objClass;
		ArrayList<Field> superFields = new ArrayList<>();
		while (current.getSuperclass() != null) {
			Class<?> superClass = current.getSuperclass();
			Field[] tempFields = getNonStaticFields(superClass);
			for (int i = 0; i < tempFields.length; i++) {
				superFields.add(tempFields[i]);
			}
			current = superClass;
		}
		Field[] result = new Field[objFields.length + superFields.size()];
		for (int i = 0; i < objFields.length; i++) {
			result[i] = objFields[i];
		}
		int baseInd = objFields.length-1;
		for (int i = 0; i < superFields.size(); i++) {
			result[baseInd+i] = superFields.get(i);
		}
		return result;
	}
	
	public static Field[] getNonStaticFields(Class<?> clazz) {
		ArrayList<Field> result = new ArrayList<>();
		Field[] fields = clazz.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			if (Modifier.isStatic(field.getModifiers())) {
				continue;
			}
			result.add(field);
		}
		return result.toArray(new Field[result.size()]);
	}
	
	public static String getExtension(File f) {
		String relPath = f.getPath();
		int ind = relPath.lastIndexOf(".");
		return relPath.substring(ind, relPath.length());
	}
	
	public static boolean keysMatchToFields(Class<?> clazz, Set<String> keys) {
		Field[] fields = getNonStaticFields(clazz);
		for (Field f : fields) {
			if (!keys.contains(f.getName().toLowerCase())) {
				return false;
			}
		}
		return true;
	}
	
	public static void checkNull(Object value) {
		if (value == null) {
			throw new IllegalArgumentException("This key doesn't map to a value!");
		}
	}
}
