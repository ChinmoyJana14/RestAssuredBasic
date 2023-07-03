import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import file.PayLoad;
import file.ReUsableMethods;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class DynamicJson {
	
	ArrayList<String> id = new ArrayList<String>();
	
	@BeforeMethod
	public void TearUp()
	{
		RestAssured.baseURI = "http://216.10.245.166";
	}
	
	//@Test()
	public void addBook()
	{		
	String response =	given().header("Content-Type","application/json").body(PayLoad.AddBook("chinmoy","12345"))
		.when().post("/Library/Addbook.php")
		.then().log().all().assertThat().statusCode(200).extract().response().asString();
	JsonPath js = ReUsableMethods.rawToJson(response);
	id.add(js.getString("ID"));
	}
	
	//@Test(dataProvider = "BooksData")
	public void addMultiBook(String isbn, String aisle)
	{
		String response =	given().header("Content-Type","application/json").body(PayLoad.AddBook(isbn,aisle))
				.when().post("/Library/Addbook.php")
				.then().assertThat().statusCode(200).extract().response().asString();
			JsonPath js = ReUsableMethods.rawToJson(response);
			System.out.println(js.getString("ID"));
	}
	
	//@Test
	public void deleteAddedBooks()
	{
		Iterator<String> it = id.iterator();
		while(it.hasNext())
		{
			given().header("Content-Type","application/json").body("{\r\n"
					+ " \r\n"
					+ "\"ID\" : \""+it.next()+"\"\r\n"
					+ " \r\n"
					+ "} ")
			.when().post("/Library/DeleteBook.php")
			.then().log().all().assertThat().statusCode(200).body("msg",equalTo("book is successfully deleted")).extract().response().asString();
		}
	}
	
	@DataProvider (name = "BooksData")
	public Object[][] GetData()
	{
		String  localTime = String.valueOf(System.currentTimeMillis());
		return new Object[][] {{"chinmoy",localTime},
								{"sanu",localTime},
								{"priya",localTime}};
	}
	
}
