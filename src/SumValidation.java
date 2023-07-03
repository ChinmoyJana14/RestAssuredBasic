import org.testng.Assert;
import org.testng.annotations.Test;

import file.PayLoad;
import io.restassured.path.json.JsonPath;

public class SumValidation {
	
	public static long SumOfCourses()
	{
		JsonPath js = new JsonPath(PayLoad.CoursePrice());
		int noOfCourse = js.getInt("courses.size()");
		long sum =0;
		for(int i = 0; i<noOfCourse-1 ; i++)
		{
			sum = sum + (js.getInt("courses["+i+"].price") * js.getInt("courses["+i+"].copies"));
		}
		return sum;
	}

}
