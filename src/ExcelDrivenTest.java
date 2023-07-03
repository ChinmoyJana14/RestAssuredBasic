import org.testng.annotations.Test;

import file.ReUsableMethods;

import static io.restassured.RestAssured.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class ExcelDrivenTest {

	//public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	//}
	
	@Test
	public void addbook() throws IOException
	{
		DataDriven dataDriven = new DataDriven();
		ArrayList<String> data = dataDriven.getData("Add Book", "Sheet1");
		System.out.println(data.get(2));
		HashMap<String, Object> jsonMap = new HashMap<>();
		jsonMap.put("name", data.get(1));
		jsonMap.put("isbn", data.get(2));
		jsonMap.put("aisle", data.get(3));
		jsonMap.put("author", data.get(4));
		
		RestAssured.baseURI ="http://216.10.245.166";
		String res = given()
						.header("Content-Type","application/json")
						.body(jsonMap)
						.when().log().all().post("Library/Addbook.php")
						.then().log().all().assertThat().statusCode(200).extract().response().asString();
		JsonPath js = ReUsableMethods.rawToJson(res);
		String id = js.get("ID");
		System.out.println(id);	
	}

}
