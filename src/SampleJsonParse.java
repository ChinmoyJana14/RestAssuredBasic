import org.testng.Assert;

import file.PayLoad;
import io.restassured.path.json.JsonPath;

public class SampleJsonParse {
	
	/*Test Cases
	1. Print No of courses returned by API

	2.Print Purchase Amount

	3. Print Title of the first course

	4. Print All course titles and their respective Prices

	5. Print no of copies sold by RPA Course

	6. Verify if Sum of all Course prices matches with Purchase Amount
	*/
	public static void main(String[] args) {
		
		JsonPath js = new JsonPath(PayLoad.CoursePrice());
		
		int noOfCourse = js.getInt("courses.size()");
		System.out.println(noOfCourse);
		
		int purchaseAmount = js.getInt("dashboard.purchaseAmount");
		System.out.println(purchaseAmount);
		
		String firstCourse = js.get("courses[0].title");
		System.out.println(firstCourse);
		
		String thirdCourse = js.get("courses[2].title");
		System.out.println(thirdCourse);
		
		for(int i = 0; i<noOfCourse ; i++)
		{
			String courseTitles = js.get("courses["+i+"].title");
			System.out.print(courseTitles);
			System.out.println(" " +js.get("courses["+i+"].price").toString());					
		}
		
		System.out.println("Print no of copies sold by RPA Course");
		for(int i = 0; i<noOfCourse ; i++)
		{
			String courseTitles = js.get("courses["+i+"].title");
			if(courseTitles.equalsIgnoreCase("RPA")) 
			{
				System.out.println(" " +js.get("courses["+i+"].copies").toString());	
				break;
			}
		}	
		System.out.println("Verify if Sum of all Course prices matches with Purchase Amount");
		Assert.assertEquals(purchaseAmount, SumValidation.SumOfCourses());
	}

	
}
