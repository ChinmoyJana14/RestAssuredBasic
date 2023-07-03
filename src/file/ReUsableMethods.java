package file;

import io.restassured.path.json.JsonPath;

public class ReUsableMethods {
	
		public static JsonPath rawToJson(String arg)
		{
			JsonPath js = new JsonPath(arg);
			return js;
		}

}
