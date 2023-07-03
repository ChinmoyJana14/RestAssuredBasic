import static io.restassured.RestAssured.given;

import org.testng.Assert;

import io.restassured.path.json.JsonPath;

public class GraphQLScript {

	public static void main(String[] args) {
		// TODO Auto-generated method 
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
		
	}

}
