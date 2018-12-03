package jayms.json;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

public class JSONArray implements JSONComponent, Iterable<Object> {

	private ArrayList<Object> arr = new ArrayList<>();
	
	public JSONArray() {	
	}
	
	public JSONArray(Object[] obj) {
		for (int i = 0; i < obj.length; i++) {
			arr.add(obj[i]);
		}
	}
	
	public JSONArray(ArrayList<Object> arr) {
		this.arr = arr;
	}
	
	public void add(Object o) {
		arr.add(o);
	}
	
	public void remove(Object o) {
		arr.remove(o);
	}
	
	public boolean isEmpty() {
		return arr.isEmpty();
	}
	
	@Override
	public String toJSONString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		int iteration = 1;
		for (Object o : arr) {
			String toAppend = "";
			toAppend += Utils.toJSONString(o);
			if (!(iteration >= arr.size())) {
				toAppend += ",";
			}
			sb.append(toAppend);
			iteration++;
		}
		sb.append("]");
		return sb.toString();
	}

	@Override
	public Iterator<Object> iterator() {
		return arr.iterator();
	}
}
