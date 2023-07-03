import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.List;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import pojo.AddPlace;
import pojo.Location;

public class SerializeTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		//POST
		AddPlace ap = new AddPlace();
		ap.setLanguage("English");
		ap.setAddress("7 katherine pl");
		ap.setName("C Jana");
		ap.setPhone_number("123443444");
		ap.setWebsite("abc.com");
		ap.setAccuracy(50);
		
		List<String> type = new ArrayList<String>();
		type.add("shop");
		type.add("wine_shop");
		ap.setTypes(type);
		
		Location l = new Location();
		l.setLat(1223.233);
		l.setLng(-1211.32432);
		ap.setLocation(l);

		Response response = given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
									.body(ap)
						  .when().post("maps/api/place/add/json")
						  .then().assertThat().statusCode(200).extract().response();
		String responseString = response.asString();
		System.out.println(responseString);
	}
}