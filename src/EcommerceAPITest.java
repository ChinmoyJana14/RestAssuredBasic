import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import pojo.LoginRequest;
import pojo.LoginResponse;
import pojo.OderDetails;
import pojo.Oders;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;

public class EcommerceAPITest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RequestSpecification req= 	new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.setContentType(ContentType.JSON).build();
		
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setUserEmail("sanujana8@gmail.com");
		loginRequest.setUserPassword("Pr@cticeChinmoy123");
		
		RequestSpecification reqLogin = given().log().all().spec(req).body(loginRequest);
		LoginResponse loginResponse = reqLogin.when().post("/api/ecom/auth/login").then().extract().response().as(LoginResponse.class);
		 System.out.println(loginResponse.getToken());
		
		//add product
		RequestSpecification addProductBasereq= new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.addHeader("Authorization", loginResponse.getToken()).build();
		
		RequestSpecification addProductSpec= given().log().all().spec(addProductBasereq).param("productName", "Pagli").param("productAddedBy", loginResponse.getuserId())
			.param("productCategory","abc").param("productSubCategory","xyz").param("productPrice", "200").param("productDescription", "bla bla")
			.param("productFor", "All").multiPart("productImage", new File(System.getProperty("user.dir")+File.separator+"TestData"+File.separator+"jacket.jpg"));
		
		String addProductResponse = addProductSpec.when().post("/api/ecom/product/add-product")
		.then().log().all().extract().asString();
		JsonPath js = new JsonPath(addProductResponse);
		String productId = js.get("productId");
		
		//Create Order
		RequestSpecification createOrderBaseSpec = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.addHeader("Authorization", loginResponse.getToken()).setContentType(ContentType.JSON).build();
		
		OderDetails oderDetails = new OderDetails();
		oderDetails.setCountry("India");
		oderDetails.setProductOrderedId(productId);
		
		List<OderDetails> listOrderDetails = new ArrayList<OderDetails>();  
		listOrderDetails.add(oderDetails);
		
		Oders order = new Oders();
		order.setOrders(listOrderDetails);
		
		RequestSpecification createOrderReqSpec = given().log().all().spec(createOrderBaseSpec).body(order);
		String createOrderResponsec= createOrderReqSpec.when().post("/api/ecom/order/create-order").then().log().all().extract().response().asString();
		System.out.println(createOrderResponsec);
		
		// Delete Product
		
		RequestSpecification deleteOrderBaseSpec = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.addHeader("Authorization", loginResponse.getToken()).setContentType(ContentType.JSON).build();
		
		RequestSpecification deleteOrderSpecReq = given().spec(deleteOrderBaseSpec).log().all().pathParam("productId", productId);
		
		String delResponse = deleteOrderSpecReq.when().delete("/api/ecom/product/delete-product/{productId}")
				.then().log().all().extract().response().asString();
		JsonPath jss = new JsonPath(delResponse);
		Assert.assertEquals("Product Deleted Successfully", jss.get("message"));
		
	}

}
