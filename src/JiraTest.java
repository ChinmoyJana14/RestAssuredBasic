import static io.restassured.RestAssured.*;

import java.io.File;

import org.testng.Assert;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

public class JiraTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		RestAssured.baseURI = "http://localhost:8081";
		
		//Log In
		//Alternative way of getting the session
		
		SessionFilter session = new SessionFilter();
		String logInResponse = given().relaxedHTTPSValidation().header("Content-Type","application/json").body("{ \r\n"
				+ "    \"username\": \"ChinmoyJana\", \r\n"
				+ "    \"password\": \"ChinmoyPractice\"\r\n"
				+ "}").log().all().filter(session).post("/rest/auth/1/session")
			.then().log().all().statusCode(200).extract().response().asString();
		
		//JsonPath js = new JsonPath(logInResponse);
		//String SessionID = js.getString("session.value");

		//Add Comment
		String inputComment = "My Comment 2.0";
		String commentResponse =
		given().pathParam("id", "10200").log().all().header("Content-Type","application/json").header("","").body("{\r\n"
				+ "    \"body\": \""+inputComment+"\",\r\n"
				+ "    \"visibility\": {\r\n"
				+ "        \"type\": \"role\",\r\n"
				+ "        \"value\": \"Administrators\"\r\n"
				+ "    }\r\n"
				+ "}").filter(session).when().post("/rest/api/2/issue/{id}/comment")
		.then().log().all().statusCode(201).extract().response().asString();
		JsonPath js = new JsonPath(commentResponse);
		String inputCommentId = js.get("id");
		
		//Add Attachment	
		given().header("X-Atlassian-Token","no-check").filter(session).pathParam("id", "10200").multiPart(
					"file", new File(System.getProperty("user.dir")+File.separator+"TestData"+File.separator+"InputFiles"+File.separator+"Jira.txt"))
					.header("Content-Type","multipart/form-data").log().all()
		.when().post("/rest/api/2/issue/{id}/attachments")
		.then().log().all().assertThat().statusCode(200);
		
		//Get Issue
		given().filter(session).pathParam("id", "10200")
			.when().get("/rest/api/2/issue/{id}")
			.then().log().all().extract().response().asString();
		
		//Limit the response
		System.out.println("*****Limited Response************");
		String  issueDetails = 
		given().filter(session).pathParam("id", "10200").queryParam("fields", "comment")
		.when().get("/rest/api/2/issue/{id}")
		.then().log().all().extract().response().asString();
		
		//Validate the comments
		JsonPath js1 = new JsonPath(issueDetails);
		int commentsCount = js1.getInt("fields.comment.comments.size()");
		for(int i =0; i<commentsCount;i++)
		{
			String commentIDActual = js1.get("fields.comment.comments["+i+"].id").toString();
			if(commentIDActual.equalsIgnoreCase(inputCommentId))
			{
				String actualComment = js1.get("fields.comment.comments["+i+"].body").toString();
				System.out.println(actualComment);
				Assert.assertEquals(actualComment, inputComment);
			}
		}
	}

}
