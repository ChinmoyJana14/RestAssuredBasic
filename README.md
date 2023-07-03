# RestAssuredBasic
Basic API Testing using Rest Assured

HTTP methods which are commonly used to communicate with Rest API’s are
	GET, POST, PUT, and DELETE
	GET- The GET method is used to extract information from the given server using a given URI. While using GET request, 
		it should only extract data and should have no other effect on the data. No Payload/Body required
		How to send input data in GET?
		Ans: Using Query Parameters
	POST- A POST request is used to send data to the server, for example, customer information, file upload, etc. using HTML forms.
		How to send input data in POST?
		Ans: Using Form Parameters /Body Payload
	PUT- Replaces all current representations of the target resource with the uploaded content.
	DELETE- Removes all current representations of the target resource given by a URI.
	
End point: Address where API is hosted on the Server.
	End Point Request URL can be constructed as below
	Base URL/resource/(Query/Path)Parameters
Resources:
	Resources represent API/Collection which can be accessed from the Server
	Google.com/maps
	google.com/search
	google.com/images

Path Parameters:
	Path parameters are variable parts of a URL path. They are typically used to point to a specific resource within a collection, such as a user identified by ID
	https://www.google.com/Images/1123343
	https://www.google.com/docs/1123343
	https://amazon.com/orders/112

https://www.google.com/search?q=newyork&oq=newyork&aqs=chrome..69i57j0l7.2501j0j7&sourceid=chrome&ie=UTF-8

Query Parameters:
	Query Parameter is used to sort/filter the resources.
	Query Parameters are identified with ?
	https://amazon.com/orders?sort_by=2/20/2020

Headers/Cookies:
	Headers represent the meta-data associated with the API request and response. In layman terms, we were sending Additional details to API to process our request.
	Example : Authorization details

Installation:
	Java, Eclipse
	In Maven
		io.rest-assured
		org.testng
		org.hamcrest

Rest Assured Structure
	Manually 
		import static io.restassured.RestAssured.*;
		import static org.hamcrest.Matchers.*;
	Given -> All input details
	When -> Submit the API with request method, resource
	Then -> validate the response
	
Basic Code Example
			RestAssured.baseURI = "https://rahulshettyacademy.com";
			given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json").body(PayLoad.AddPlace())//log().all() for log everyting
			.when().post("maps/api/place/add/json")
			.then().log().all().assertThat().statusCode(200).body("scope",equalTo("APP"))
						.header("server", "Apache/2.4.41 (Ubuntu)").extract().response().asString();
			
		public class PayLoad {	
			public static String AddPlace()
			{
				return  "{\r\n"
						+ "  \"location\": {\r\n"
						+ "    \"lat\": -38.383494,\r\n"
						+ "    \"lng\": 33.427362\r\n"
						+ "  },\r\n"
						+ "  \"accuracy\": 50,\r\n"
						+ "  \"name\": \"Frontline house\",\r\n"
						+ "  \"phone_number\": \"(+91) 983 893 3937\",\r\n"
						+ "  \"address\": \"29, side layout, cohen 09\",\r\n"
						+ "  \"types\": [\r\n"
						+ "    \"shoe park\",\r\n"
						+ "    \"shop\"\r\n"
						+ "  ],\r\n"
						+ "  \"website\": \"https://rahulshettyacademy.com\",\r\n"
						+ "  \"language\": \"French-IN\"\r\n"
						+ "}\r\n"
						+ "";				
			}
		}

To parse json -> JsonPath Class   import io.restassured.path.json.JsonPath
	JsonPath js = new JsonPath("Json as String");
	int noOfCourse = js.getInt("courses.size()");
	int purchaseAmount = js.getInt("dashboard.purchaseAmount");
	String firstCourse = js.get("courses[0].title");
		for(int i = 0; i<noOfCourse ; i++)
		{
			String courseTitles = js.get("courses["+i+"].title");
			System.out.print(courseTitles);
			System.out.println(" " +js.get("courses["+i+"].price").toString());					
		}
	
Cookie Based Authentication: Jira API -> Use object of SessionFilter class to store the session
	import io.restassured.filter.session.SessionFilter;
	SessionFilter session = new SessionFilter();
	given().header("Content-Type","application/json").body("As String containing username and password")
			.filter(session).post("/rest/auth/1/session");

Create Issue:
	//Created Issue by postman
	
Add Comment	
	given().pathParam("id", "10101").log().all().header("Content-Type","application/json").header("","").body("As String containing the comment")
		.filter(session).when().post("/rest/api/2/issue/{id}/comment")
	
Sending Attachment:
		given().header("X-Atlassian-Token","no-check").filter(session).pathParam("id", "10200").multiPart(
					"file", new File(System.getProperty("user.dir")+File.separator+"TestData"+File.separator+"InputFiles"+File.separator+"Jira.txt"))
					.header("Content-Type","multipart/form-data").log().all()
		.when().post("/rest/api/2/issue/{id}/attachments")
		.then().log().all().assertThat().statusCode(200);
	
Path Parameter : use pathParam with key value and send the key via {} inside the resource uri
	given().pathParam("id", "10101").log().all().header("Content-Type","application/json").header("","").body("As String")
	.when().post("/rest/api/2/issue/{id}/comment")
	
relaxedHTTPSValidation: Handling SSL Certificate if no proper certification
	given().relaxedHTTPSValidation().
	
Limiting response: using queryParam
		String  issueDetails = 
		given().filter(session).pathParam("id", "10200").queryParam("fields", "comment")
		.when().get("/rest/api/2/issue/{id}")
		.then().log().all().extract().response().asString();

Auth 2.0 Authorization: GrantType -> Authorization Code
	Client: BookMyShow
	ClientID: ID that identifies the client (public)
	ClientSecret: client registered with google generated a password
	Resource Owner: Human
	Resource/Authorization Server: Google
	Step:
		1. User sign in into Google by hitting google authorization server and google pass a code to the application
		2. Application will use this code(with Client ID/Secret) to hit Google resource server in backend to get (Access Token, First Name, Last Name, Display Picture, Email)
		3. Application stored this Access Token on temp database and Every activities performed by the user uses the same access token stored in browser
		4. Application grants access to the user by validating the Access Token, when Access Token expires, user needs to sign in again
		
	To Generate code of step 1, one GET request will be there with below query parameters --getCode
		scope: a url contails the list of user info that the authorization server will provide, based on aggreement it will change
		auth_url: specific to authorization server
		client_id: Authorization server will provide to the Client in times of first registration
		response_type: type of response expected from the authorization server (like code)
		redirect_uri: url of client website
		state: optional parameter, for security purpose, ramdom text should present application
		Hit the url on the browser with Username and Password and copy the Code from the final URL
	To get the Access Token
		Need to submit the Authorization resource URI and query parameters like below ---exchangeCode
		code: generated from step 1
		client_id:Authorization server will provide to the Client in times of first registration
		client_secret:Authorization server will provide to the Client in times of first registration
		redirect_uri: url of client website
		grant_type: authorization_code
	Use the Access Token as query parameter to hit the actual API
	
		
Auth 2.0 Authorization: GrantType -> Client Credentials (When no human interaction)	application requires it's own data
	To get the Access Token
		Need to submit the Authorization resource URI and query parameters like below 
		client_id:Authorization server will provide to the Client in times of first registration
		client_secret:Authorization server will provide to the Client in times of first registration
		redirect_uri: url of client website
		grant_type: authorization_code	
		scope: Client Credentials

POJO: 		
Serialization : Java Object to Request Body (PayLoad)
	Create a class with properties required for the payload and use setter method to set the properties
	Create object of the class and use inside the body method of given()
	
Deserialization : Payload to Java Object
	Use the same class and it's getter methods
	
	For each nested Json, we need to create new child Classes and set return type the Class name
	FOr array items need, need to use List of the Class in the parent class
	Respose return type will be as(ClassName.class) instead of asString
	Use defaultParser(Parser.JSON) like
		GetCourse getCourse = given().queryParam("access_token", accessToken).expect().defaultParser(Parser.JSON)
	When content type is not application/json
	
SpecBuilder:
	Used to reuse the code and make test code compact
		import io.restassured.builder.RequestSpecBuilder;
		import io.restassured.builder.ResponseSpecBuilder;
		import io.restassured.specification.RequestSpecification;
		import io.restassured.specification.ResponseSpecification;
		RequestSpecification req = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addQueryParam("key", "qaclick123")
			.setContentType(ContentType.JSON).build();
		
		ResponseSpecification responseSpec = new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();
		
		RequestSpecification res = given().spec(req)
									.body(ap);
		Response response = res.when().post("maps/api/place/add/json")
						  .then().spec(responseSpec).extract().response();
		String responseString = response.asString();
		
For the Form Parameter:
			Copy the sample data and beautify in postman
			in given() add every pair as param("","")
			
How to give PathParam
	RequestSpecification deleteOrderSpecReq = given().spec(deleteOrderBaseSpec).log().all().pathParam("productId", productId);	
	String delResponse = deleteOrderSpecReq.when().delete("/api/ecom/product/delete-product/{productId}")
		
Framework
	Dependencies:
		cucumber-java
		cucumber-junit
		rest-assured
		jackson-databind
		
Framework Components/Packages
	cucumber.Options
	TestRunner Class : Use to run the scenarios by connecting feature file with stepDefinitions
		import org.junit.runner.RunWith;
		import io.cucumber.junit.Cucumber;
		import io.cucumber.junit.CucumberOptions;
		@RunWith(Cucumber.class)
		@CucumberOptions(features = "src/test/java/features", glue= {"stepDefinitions"})
		public class TestRunner {
	
		}
	
	features
	abc.features
		Feature: Validating Place API's
		Scenario: Verify if place is being add sucessfully using AddPlaceAPLI
			Given Add Place Payload
			When users calls "AddPlaceAPI" with Post http request
			Then the API call is success with status code 200
			And "status" in response body is "OK"
			And "scope" in response body is "APP"

	resources
	TestDataBuild Class: For building the payload
	Utils Class: For building the request response specification
	
	stepDefinitions
	StepDefinition Class extends Utils : For test Step

Logging:
	import io.restassured.filter.log.RequestLoggingFilter;
	import io.restassured.filter.log.ResponseLoggingFilter;
	public static RequestSpecification req;//one copy of the variable in memory, regardless of how many instances of the class are created
	public RequestSpecification requestSpecification() throws FileNotFoundException
	{
		if (req==null)//It should not write again if already, to stop overriding for multiple runs
		{
			PrintStream log =  new PrintStream(new FileOutputStream("logging.txt"));//to write into a file, the file will be stored in the project level 
			RestAssured.baseURI = "https://rahulshettyacademy.com";
			//POST		
			req = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addQueryParam("key", "qaclick123")
					.addFilter(RequestLoggingFilter.logRequestTo(log))//It will apply for the whole request
					.addFilter(ResponseLoggingFilter.logResponseTo(log))//It will apply for the whole response
			.setContentType(ContentType.JSON).build();
			return req;
		}
		return req;
	}
	
Golabl Variable - property file
	Create a file global.properties inside resources
	public static String getGlobalValues(String key) throws IOException
	{
		Properties prop =  new Properties();
		FileInputStream stream = new FileInputStream(System.getProperty("user.dir")+ File.separator +"src\\test\\java\\resources\\global.properties"); // read the stream
		prop.load(stream);//putting stream inside property object
		return prop.getProperty(key);
	}
	Call the above method from outside like
		setBaseUri(getGlobalValues("baseURL"))

Data Driven:
	Feature: Validating Place API's
	Scenario Outline: Verify if place is being add sucessfully using AddPlaceAPLI

	Given Add Place Payload with "<name>" "<language>" "<address>"
	When users calls "AddPlaceAPI" with Post http request
	Then the API call is success with status code 200
	And "status" in response body is "OK"
	And "scope" in response body is "APP"
	
	Examples:
		|name |language | address		|
		|abc  | English | 12 ABC Street |
		|Xyz  | Dutch   | 234 xyz St    |
	
Handle API Resources:
	enum: special class in java which has collection of constants and methods
	
		public enum APIResources {
			
			addPlaceAPI("maps/api/place/add/json"),
			getPlaceAPLI("maps/api/place/get/json"),
			deletePlaceAPI("maps/api/place/delete/json");
			private String resource;
			
			APIResources(String resource) {
				this.resource=resource;
			}
			
			public String getResource()
			{
				return resource;		
			}
		}
	From the stepDefinitions class:
		APIResources apiResources = APIResources.valueOf(resource);//invoking the constructor of the enum class	with the value of the enum
		Response response = res.when().post(apiResources.getResource());
	
Run Selected Test Case:
	Tagging : run all the test cases with this tag from the features package
		@CucumberOptions(features = "src/test/java/features", glue= {"stepDefinitions"}, tags = "@AddPlace")
	one test case can have multiple tags
	
Run Test From CMD:
	Go to the path of the project from cmd
	mvn test 
	To run specific test cases pass parameter using D keyword
	mvn test -Dcucumber.options="--tags @AddPlace"
	To run specific feature file- we need to provide full path
	@CucumberOptions(features = "src/test/java/features/abc.features", glue= {"stepDefinitions"})

Reporting:
	https://github.com/damianszczepanik/maven-cucumber-reporting
	Add plugin = {"pretty", "json:target/JsonReports/report.json"} in @CucumberOptions
	copy the the mvn verify xml fro the above repo and paste it inside pom xml inside plugins
	update the version no with latest and remove classification part
	need toremove the <PluginManagement> tags in pom.xml file
	Run with : mvn test verify
	
GraphQL:
	To overcome overfetching of data from the backend using muiltiple API to build UI
	Facebook built GraphQL: Query to single graphql end point asking exact details UI need
	https://rahulshettyacademy.com/gq/graphql
		Input:
			query{
			  character(characterId:8)
			  {
				name
				id
				gender
				status
				}
			  location(locationId:8)
			  {
					name
				dimension
			  }
			  episode(episodeId:1)
			  {
				name
				episode
				air_date
			  }
			  characters(filters : {name : "Peter"})// for complex data types
			  {
				info
				{
				  count
				}
			  }
			}
		Output:
			{
			  "data": {
				"character": {
				  "name": "Tanishq",
				  "id": 8,
				  "gender": "Male",
				  "status": "Alive"
				},
				"location": {
				  "name": "Newzealand",
				  "dimension": "360"
				},
				"episode": {
				  "name": "Ranjha",
				  "episode": "123456",
				  "air_date": "14062023"
				},
				"characters": {
				  "info": {
					"count": 0
				  }
				}
			  }
			}
			
	Query Variable:
		Use Query Variables for passing parameters to the query like
		Query Variables:
		{
		  "characterId" :8,
		  "episodeId" :1
		}
		Input:
		query($characterId :Int!, $episodeId : Int!)
			{
			  character(characterId:$characterId)
			  {
				name
				id
				gender
				status
				}
			  location(locationId:8)
			  {
					name
				dimension
			  }
			  episode(episodeId:$episodeId)
			  {
				name
				episode
				air_date
			  }
			}
	
	Use Postman:
		All are POST
		Select GraphQL in body
		put variables in GRAPHQL VARIABLES

	Create Data in GraphQL
	Input:
		mutation
			{
			  createLocation(location : {name : "Aus" ,type:"South" ,dimension: "123"})
			  {
				id
				}
			}
	Output:
		{
		  "data": {
			"createLocation": {
			  "id": 70
			}
		  }
		}
	
	Deletion
		Input:
			mutation
			{
			  createLocation(location : {name : "Aus" ,type:"South" ,dimension: "123"})
			  {
				id
				}
			  
			  createCharacter(character : {name: "Max", type: "mack", status :"Alive", species:"Human", gender:"Male", image:"defevds", originId:12, locationId: 123})
			  {
				id
			  }
			  
			  createEpisode(episode: {name: "GOT", air_date:"16Dec2000",episode:"first"})
			  {
					id
			  }
			  
			  deleteLocations(locationIds : [74,73])
			  {
					locationsDeleted
			  }
			}
	
		Output:
			{
			  "data": {
				"createLocation": {
				  "id": 76
				},
				"createCharacter": {
				  "id": 63
				},
				"createEpisode": {
				  "id": 42
				},
				"deleteLocations": {
				  "locationsDeleted": 2
				}
			  }
			}
				
	
In RestAssured:
	To get the payload in json format from the query use network tab -> Payload -> View Source -> Copy
		-> paste inside .body("") of given()
	
	//Query
		int characterId = 14;
		String response = given().log().all().header("Content-Type", "application/json")
			.body("{\"query\":\"\\t\\tquery($characterId :Int!, $episodeId : Int!, $locationId : Int!)\\n\\t\\t\\t{\\n\\t\\t\\t  character(characterId:$characterId)\\n\\t\\t\\t  {\\n\\t\\t\\t\\tname\\n\\t\\t\\t\\tid\\n\\t\\t\\t\\tgender\\n\\t\\t\\t\\tstatus\\n\\t\\t\\t\\t}\\n\\t\\t\\t  location(locationId:$locationId)\\n\\t\\t\\t  {\\n\\t\\t\\t\\t\\tname\\n\\t\\t\\t\\tdimension\\n\\t\\t\\t  }\\n\\t\\t\\t  episode(episodeId:$episodeId)\\n\\t\\t\\t  {\\n\\t\\t\\t\\tname\\n\\t\\t\\t\\tepisode\\n\\t\\t\\t\\tair_date\\n\\t\\t\\t  }\\n\\t\\t\\t}\",\"variables\":{\"characterId\":"+characterId+",\"episodeId\":40,\"locationId\":77}}")
			.when().post("https://rahulshettyacademy.com/gq/graphql")
			.then().log().all().extract().response().asString();
		
		JsonPath js = new JsonPath(response);
		Assert.assertEquals(js.get("data.location.name"), "Perth");
	//Mutation
		String characterName = "Max";
		String mutationResponse = given().log().all().header("Content-Type", "application/json")
				.body("{\"query\":\"mutation($locationName: String!, $characterName: String!, $episodeName: String!)\\n{\\n  createLocation(location : {name : $locationName ,type:\\\"South\\\" ,dimension: \\\"123\\\"})\\n  {\\n    id\\n\\t}\\n  \\n  createCharacter(character : {name: $characterName, type: \\\"mack\\\", status :\\\"Alive\\\", species:\\\"Human\\\", gender:\\\"Male\\\", image:\\\"defevds\\\", originId:12, locationId: 75})\\n  {\\n    id\\n  }\\n  \\n  createEpisode(episode: {name: $episodeName, air_date:\\\"16Dec2000\\\",episode:\\\"first\\\"})\\n  {\\n\\t\\tid\\n  }\\n  \\n  deleteLocations(locationIds : [74,73])\\n  {\\n\\t\\tlocationsDeleted\\n  }\\n}\\n\",\"variables\":{\"locationName\":\"Perth\",\"characterName\":\""+characterName+"\",\"episodeName\":\"BreakingBad\"}}")
				.when().post("https://rahulshettyacademy.com/gq/graphql")
				.then().log().all().extract().response().asString();	

Jankins:
	Install and run Jenkins from local
		New Item -> freestyleproject -> Use custom workspace -> Paste repository path
		Build -> Add Build Step ->Invoke top level maven targets-> test verify -Dcucumber.options="--tags @AddPlace" -> Save -> BuildNow
	To parameter
		Add parameter -> Choice Parameter -> name and choices by next line
		Provide the parameterarized run command like
			test verify -Dcucumber.options="--tags @"$tag""
			Build with parameters with dropdown and build
			
Git: Same as Java Selenium
	

DataDriven Testing using Excel:
	Create object of XSSFWorkbook class
	Get access to the particular sheet
	Get access to the first row
	Identify Testcases column by scanning the entire first row
	once column identified, scan entire Testcases column to identify particular test case name by iterating the for this column
	After identify the specific test case, pull all the data of that row to feed the test case by adding to an arraylist

Create JSON from a HashMap:
https://github.com/rest-assured/rest-assured/wiki/Usage#create-json-from-a-hashmap
	Map<String, Object>  jsonAsMap = new HashMap<>();
		jsonAsMap.put("firstName", "John");
		jsonAsMap.put("lastName", "Doe");
	Map<String, Object>  jsonAsMap2 = new HashMap<>();
		jsonAsMap2.put("street name", "John");
		jsonAsMap2.put("st no", "Doe");		
	jsonAsMap.put("address", jsonAsMap2);// for nested json
	given().
			contentType(JSON).
			body(jsonAsMap).
	when().
			post("/somewhere").
	then().
			statusCode(200);
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
