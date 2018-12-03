package jayms.json;

import jayms.json.map.JSONMap;
import jayms.json.map.JSONMapReader;
import jayms.json.node.JSONNode;
import jayms.json.node.JSONNodeReader;
import jayms.json.serial.DeserializerTransformer;
import jayms.json.serial.JSONDeserializer;
import jayms.json.serial.JSONSerializer;
import jayms.json.serial.SerializerTransformer;

public class Main {

	public static void main(String[] args) {
		JSONArray arr = new JSONArray();
		arr.add("hey there");
		arr.add("hello");
		arr.add(0);
		arr.add(1000);
		arr.add(new Car());
		System.out.println(arr.toJSONString());
		JSONObject obj = new JSONObject();
		obj.put("name", "jayms");
		obj.put("age", 10);
		obj.put("career", 'l');
		obj.put("car", new Car());
		obj.put("array", arr);
		obj.put("admin", true);
		obj.put("dec", 0.583);
		obj.put("long", Long.MAX_VALUE);
		String objJSON = obj.toJSONString();
		System.out.println(objJSON);
		JSONParser parser = new JSONParser();
		JSONObject obj2 = parser.parseJSONObject(objJSON);
		String parsedJSON = obj2.toJSONString();
		System.out.println("Parsed JSON: " + parsedJSON);
		System.out.println(objJSON.equals(parsedJSON));
		JSONObject car = new JSONObject();
		Car carInst = new Car();
		Manufacturer manu = new Manufacturer();
		manu.networth = 10;
		carInst.manufacturer = manu;
		car.put("speed", carInst.speed);
		car.put("name", carInst.name);
		car.put("manufacturer", manu);
		JSONFile jsonFile = new JSONFile("test.json");
		jsonFile.write(carInst);
		jsonFile.reload();
		/*Car loaded = jsonFile.read(null, Car.class);
		System.out.println(loaded);*/
		JSONMap map = JSONMapReader.readFromObject(obj);
		System.out.println("Name: " + map.getString("name"));
		JSONMap carMap = map.childMap("car");
		System.out.println("Car Name: " + carMap.getString("name"));
		JSONNodeReader nodeReader = new JSONNodeReader();
		JSONNode node = nodeReader.readJSONNode(obj);
		System.out.println("Node Name: " + node.childNode("name").getStringValue());
		System.out.println("Node Car Name: " + node.childNode("car").childNode("name").getStringValue());
		/*JSONSerializer serializer = new JSONSerializer(jsonFile);
		serializer.serialize(loaded, new CarSerializerTransformer());
		JSONDeserializer deserializer = new JSONDeserializer(jsonFile);
		loaded = deserializer.deserialize(new CarDeserializerTransformer());
		System.out.println(loaded);*/
		JSONObject test = parser.parseJSONObject("{\"brushes\":[{\"x\":0.0,\"y\":0.0,\"z\":0.0,\"w\":10.0,\"h\":1.0,\"d\":10.0,\"t\":\"sprite.bg\"}]}");
		System.out.println(test);
	}
	
	public static class CarSerializerTransformer implements SerializerTransformer<Car> {

		@Override
		public JSONObject transform(Car obj) {
			JSONObject result = new JSONObject();
			result.put("speed", obj.speed);
			result.put("name", obj.name);
			result.put("manufacturer", obj.manufacturer);
			return result;
		}
		
	}
	
	public static class CarDeserializerTransformer implements DeserializerTransformer<Car> {

		@Override
		public Car transform(JSONObject component) {
			Car car = new Car();
			car.speed = (int) component.get("speed");
			car.name = (String) component.get("name");
			Object manu = component.get("manufacturer");
			if (manu instanceof JSONObject) {
				component.replaceAsObject("manufacturer", Manufacturer.class);
				manu = component.get("manufacturer");
			}
			car.manufacturer = (Manufacturer) manu;
			return car;
		}
		
	}
	
	public static class Car {
		
		public static int jewCount = 20;
		
		private int speed;
		private String name = "Good Car";
		private Manufacturer manufacturer = null;
		
		public Car() {
		}
		
		@Override
		public String toString() {
			return "Speed: " + speed + " Name: " + name + " Manufacturer: " + manufacturer.toString();
		}
	}
	
	public static class Manufacturer {
		
		private int networth;
		private String name = "nigga";
		private Car car = new Car();
		
		public Manufacturer() {
		}
		
		@Override
		public String toString() {
			return "Networth: " + networth + " Name: " + name;
		}
	}
}
