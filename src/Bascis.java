import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.testng.Assert;

import file.PayLoad;
import file.ReUsableMethods;

public class Bascis {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		//POST
		String response = given().queryParam("key", "qaclick123").header("Content-Type","application/json")
									.body(PayLoad.AddPlace())
						  .when().post("maps/api/place/add/json")
						  .then().assertThat().statusCode(200).body("scope",equalTo("APP"))
						  			.header("server", "Apache/2.4.41 (Ubuntu)").extract().response().asString();
		
		//System.out.println(response);
		JsonPath js = ReUsableMethods.rawToJson(response);
		System.out.println(js.get("place_id").toString());                   
		System.out.println(js.getString("place_id"));
		//PUT
		String newAddress = "7 katherine Pl, Mel";
		given().queryParam("key", "qaclick123").header("Content-Type","application/json")
							.body("{\r\n"
									+ "\"place_id\":\""+ js.getString("place_id") +"\",\r\n"
									+ "\"address\":\""+newAddress+"\",\r\n"
									+ "\"key\":\"qaclick123\"\r\n"
									+ "}")
							.when().put("maps/api/place/update/json")
							.then().assertThat().statusCode(200).body("msg",equalTo("Address successfully updated"));
		
		//GET
		String getPlaceResponse = given().queryParam("key", "qaclick123").queryParam("place_id", js.getString("place_id"))
							.when().get("maps/api/place/get/json")
							.then().assertThat().statusCode(200).body("address",equalTo(newAddress)).extract().asString();
		JsonPath js1 = ReUsableMethods.rawToJson(getPlaceResponse);
		String actualAddress = js1.getString("address");
		Assert.assertEquals(actualAddress, newAddress);
	}

}
