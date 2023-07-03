import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import pojo.Api;
import pojo.GetCourse;
import pojo.WebAutomation;

public class oAuthTest {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
/*
		System.setProperty("webdriver.chrome.driver", "E:\\Software\\Grid\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.get("https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&auth_url=https://accounts.google.com/o/oauth2/v2/auth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php");
			driver.findElement(By.cssSelector("input[type='email']")).sendKeys("sanujana8");
			driver.findElement(By.cssSelector("input[type='email']")).sendKeys(Keys.ENTER);
			Thread.sleep(3000);
			driver.findElement(By.cssSelector("input[type='password']")).sendKeys("");
			driver.findElement(By.cssSelector("input[type='password']")).sendKeys(Keys.ENTER);
			Thread.sleep(3000);
			String code = driver.getCurrentUrl().split("code=")[1].split("&scope")[0].toString();
			System.out.println(code);
*/	
			String url	= 
"https://rahulshettyacademy.com/getCourse.php?code=4%2F0AbUR2VO9Hp01ffPcSaFxdwWeGRqBONbyEmaZzniEOWRDVNwmtfCU-riB6jryqXV0nfeJSg&scope=email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+openid&authuser=0&prompt=none";
					String code = url.split("code=")[1].split("&scope")[0].toString();
			String accessTokenResponse = given().urlEncodingEnabled(false)//special characters will not be encoded 
			.queryParam("code", code)
			.queryParam("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
			.queryParam("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
			.queryParam("redirect_uri", "https://rahulshettyacademy.com/getCourse.php")
			.queryParam("grant_type", "authorization_code")
			.when().log().all()
			.post("https://www.googleapis.com/oauth2/v4/token").asString();

			JsonPath jsonPath  = new JsonPath(accessTokenResponse);
			String accessToken = jsonPath.getString("access_token");
			
			GetCourse getCourse = given().queryParam("access_token", accessToken).expect().defaultParser(Parser.JSON)
			.when()
			.get("https://rahulshettyacademy.com/getCourse.php").as(GetCourse.class);
			System.out.println(getCourse.getInstructor());
			System.out.println(
					getCourse.getCourses().getApi().get(1).getCourseTitle());
			//Price of one course
			List<Api> apiCourse = getCourse.getCourses().getApi();
			for (Api api : apiCourse) {
				if(api.getCourseTitle().contains("Rest Assured")) {
					System.out.println(api.getPrice());
				}	
			}
			//List of webAutomation
			List<WebAutomation> webAutomationCourses = getCourse.getCourses().getWebAutomation();
			for(int i = 0; i<webAutomationCourses.size();i++) {
				System.out.println(webAutomationCourses.get(i).getCourseTitle());
			}
			
			//Validate the Array list
			String[] expectedCourseTitles = {"Selenium Webdriver Java","Cypress","Protractor1"};
			List<String> expectedCourseTitlesList = Arrays.asList(expectedCourseTitles);
			
			ArrayList<String> actualCourseTitlesList= new  ArrayList<String>();
			for(int i = 0; i<webAutomationCourses.size();i++) {
				actualCourseTitlesList.add(webAutomationCourses.get(i).getCourseTitle());
			}	
			
			Assert.assertTrue(actualCourseTitlesList.equals(expectedCourseTitlesList));
			


			

	}

}
