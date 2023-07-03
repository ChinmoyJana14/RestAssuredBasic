import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.List;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojo.AddPlace;
import pojo.Location;

public class SpecBuilderTest {

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
		
		RequestSpecification req = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addQueryParam("key", "qaclick123")
		.setContentType(ContentType.JSON).build();
		
		ResponseSpecification responseSpec = new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();
		
		RequestSpecification res = given().spec(req)
									.body(ap);
		Response response = res.when().post("maps/api/place/add/json")
						  .then().spec(responseSpec).extract().response();
		String responseString = response.asString();
		System.out.println(responseString);
	}
}