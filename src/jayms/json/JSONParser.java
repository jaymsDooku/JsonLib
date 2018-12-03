package jayms.json;

import java.util.ArrayList;

public class JSONParser {

	public JSONParser() {
	}
	
	public JSONObject parseJSONObject(String json) {
		JSONObject result = new JSONObject();
		if (!Utils.startsAndEnds(json, '{', '}')) {
			Utils.invalidJSON();
		}
		json = json.substring(1, json.length()-1);
		String[] entries = breakToJSONString(json);
		for (int i = 0; i < entries.length; i++) {
			String entry = entries[i];
			int ind = 0;
			String key = null;
			String value = null;
			boolean gettingKey = false;
			boolean gotKey = false;
			ArrayList<Character> keyChars = new ArrayList<>();
			while (ind < entry.length()) {
				char c = entry.charAt(ind);
				ind++;
				if (c == '"' && !gotKey) {
					if (gettingKey) {
						StringBuilder sb = new StringBuilder();
						for (int k = 0; k < keyChars.size(); k++) {
							sb.append(keyChars.get(k));
						}
						key = sb.toString();
						gettingKey = false;
						gotKey = true;
						c = entry.charAt(ind);
						if (c != ':') {
							Utils.invalidJSON();
						}
					}else {
						gettingKey = true;
					}
					continue;
				}
				if (gettingKey) {
					keyChars.add(c);
					continue;
				}
				if (gotKey) {
					if (c == ':') {
						continue;
					}
					value = entry.substring(ind-1, entry.length());
					break;
				}
			}
			result.put(key, Utils.fromJSONString(value));
			
		}
		return result;
	}
	
	private String[] breakToJSONString(String json) {
		int ind = 0;
		ArrayList<String> resultList = new ArrayList<>();
		ArrayList<Character> built = new ArrayList<>();
		boolean dontBreakCurl = false;
		boolean dontBreakSquare = false;
		while (ind < json.length()) {
			char c = json.charAt(ind);
			ind++;
			switch (c) {
			case '{':
				dontBreakCurl = true;
				break;
			case '}':
				dontBreakCurl = false;
				break;
			case '[':
				dontBreakSquare = true;
				break;
			case ']':
				dontBreakSquare = false;
				break;
			}
			if (c == ',' && !dontBreakCurl && !dontBreakSquare) {
				appendBuiltToResult(built, resultList);
			}else {
				built.add(c);
				if (ind == json.length()) {
					appendBuiltToResult(built, resultList);
				}
			}
		}
		return resultList.toArray(new String[resultList.size()]);
	}
	
	private void appendBuiltToResult(ArrayList<Character> built, ArrayList<String> resultList) {
		StringBuilder sb = new StringBuilder();
		for (Character chara : built) {
			sb.append(chara);
		}
		built.clear();
		resultList.add(sb.toString());
	}
	
	public JSONArray parseJSONArray(String json) {
		JSONArray result = new JSONArray();
		if (!Utils.startsAndEnds(json, '[', ']')) {
			Utils.invalidJSON();
		}
		json = json.substring(1, json.length()-1);
		String[] values = breakToJSONString(json);
		for (int i = 0; i < values.length; i++) {
			result.add(Utils.fromJSONString(values[i]));
		}
		return result;
	}
}
