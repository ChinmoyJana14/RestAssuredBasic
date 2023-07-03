import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import file.PayLoad;
import file.ReUsableMethods;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class JsonFromFile {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		//POST
		String response = given().queryParam("key", "qaclick123").header("Content-Type","application/json")
							.body(new String
									(Files.readAllBytes(Paths.get(System.getProperty("user.dir")+File.separator+"TestData"+File.separator+"InputJsons"+File.separator+"AddPlace_1.JSON")))
								 )
						  .when().post("maps/api/place/add/json")
						  .then().assertThat().statusCode(200).body("scope",equalTo("APP"))
						  			.header("server", "Apache/2.4.41 (Ubuntu)").extract().response().asString();
		
		//System.out.println(response);
		JsonPath js = ReUsableMethods.rawToJson(response);
		System.out.println(js.get("place_id").toString());                   
		System.out.println(js.getString("place_id"));
	}
	

}
