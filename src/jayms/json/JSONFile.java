package jayms.json;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;


public class JSONFile {

	public static class JSONFileConfiguration {
		
		private boolean replaceFile = true;
		
		public JSONFileConfiguration() {
		}
		
		public void setReplaceFile(boolean set) {
			replaceFile  = set;
		}
		
		public boolean replaceFile() {
			return replaceFile;
		}
	}
	
	private File file;
	private String jsonString;
	private JSONFileConfiguration config;
	
	public JSONFile(String path) {
		this(path, new JSONFileConfiguration());
	}
	
	public JSONFile(File file) {
		this(file, new JSONFileConfiguration());
	}
	
	public JSONFile(String path, JSONFileConfiguration config) {
		this(new File(path));
	}
	
	public JSONFile(File file, JSONFileConfiguration config) {
		if (!Utils.getExtension(file).equals(".json")) {
			throw new IllegalArgumentException("Tried to pass non .json file to JSONFile.");
		}
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (!file.isFile()) {
			throw new IllegalArgumentException("Tried to pass non file to JSONFile.");
		}
		this.file = file;
		this.config = config;
		reload();
	}
	
	public void clear() {
		try {
			PrintWriter clear = new PrintWriter(file);
			clear.write("");
			clear.close();
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void reload() {
		jsonString = "";
		BufferedReader br = null;
		try {

			String sCurrentLine;

			br = new BufferedReader(new FileReader(file));

			while ((sCurrentLine = br.readLine()) != null) {
				jsonString += sCurrentLine;
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public <T> void write(T obj) {
		if (obj instanceof JSONComponent) {
			write((JSONComponent) obj);
			return;
		}
		Class<?> objClass = obj.getClass();
		Field[] fields = Utils.getNonStaticFields(objClass);
		JSONObject jsonObj = new JSONObject();
		for (Field f : fields) {
			try {
				f.setAccessible(true);
				jsonObj.put(f.getName(), f.get(obj));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		write(jsonObj);
	}
	
	public void write(JSONComponent jsonComponent) {
		BufferedWriter writer = null;
		try {
			if (config.replaceFile()) {
				new PrintWriter(file).close();
			}
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(jsonComponent.toJSONString());
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public JSONArray readArray() {
		JSONParser parser = new JSONParser();
		return parser.parseJSONArray(jsonString);
	}
	
	public JSONObject read() {
		JSONParser parser = new JSONParser();
		return parser.parseJSONObject(jsonString);
	}
	
	public <T> T read(String path, Class<T> type) {
		T result = null;
		try {
			result = type.newInstance();
			JSONParser parser = new JSONParser();
			JSONObject obj = parser.parseJSONObject(jsonString);
			if (!Utils.keysMatchToFields(type, obj.keySet())) {
				throw new IllegalArgumentException("JSONObject isn't an instance of this class.");
			}
			Field[] fields = Utils.getNonStaticFields(type);
			String[] fieldNames = new String[fields.length];
			for (int i = 0; i < fields.length; i++) {
				fieldNames[i] = fields[i].getName();
			}
			for (String name : fieldNames) {
				try {
					Field f = result.getClass().getDeclaredField(name);
					f.setAccessible(true);
					Object toSet = obj.get(name);
					Object alt = null;
					if (toSet instanceof JSONObject) {
						alt = ((JSONObject) toSet).toObject(f.getType());
					}
					f.set(result, alt == null ? toSet : alt);
				} catch (IllegalArgumentException | NoSuchFieldException | SecurityException e) {
					e.printStackTrace();
				}
			}
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return result;
	}
}
