package automationTests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import junit.framework.Assert;

public class GithubSearchApiTest {
	
	public static void main(String[] args) {
		// Setup the URL for gitHub
		
		RestAssured.baseURI = "https://api.github.com/";
		RequestSpecification httpRequest = RestAssured.given();
 
		// Query the server with name=RewardsNetwork and sort forks with order desc
		Response response = httpRequest.request(Method.GET, "/search/repositories?q=RewardsNetwork in:Name&sort=forks&order=desc");
 
		
		String responseBody = response.getBody().asString();
		//System.out.println("Response Body is =>  " + responseBody);	
		
		// Test Scenario 1: Verify the Response code is 200
		
		int statusCode = response.getStatusCode();
		Assert.assertEquals(statusCode /*actual value*/, 200 /*expected value*/);
		
		// Test Scenario 2: Verify the Response Status code is 200
		
		String statusLine = response.getStatusLine();
		Assert.assertEquals(statusLine /*actual value*/, "HTTP/1.1 200 OK" /*expected value*/);
		
		// Test Scenario 3: Verify the Content Type
		
		String contentType = response.header("Content-Type");
		System.out.println("Content-Type value: " + contentType);		
		Assert.assertEquals(contentType /* actual value */, "application/json; charset=utf-8" /* expected value */);
	
	 
	    // Test Scenario 4: Verify the remote server type
		String serverType =  response.header("Server");
		System.out.println("Server value: " + serverType);
		Assert.assertEquals(serverType /* actual value */, "GitHub.com" /* expected value */);
		
	 
		// Test Scenario 5: Verify the remote server content encoding
		String acceptLanguage = response.header("Content-Encoding");
		System.out.println("Content-Encoding: " + acceptLanguage);
		
		Assert.assertEquals(acceptLanguage /* actual value */, "gzip" /* expected value */);
		
		
		// Test Scenario 6: Verify response body contains RewardsNetwork
		Assert.assertEquals(responseBody.contains("RewardsNetwork"), true );
		
		
		// Test Scenario 7: Verify the default pagination is 30 item and less
		JsonPath jsonPathEvaluator = response.jsonPath();
		System.out.println("Response " + jsonPathEvaluator.get("total_count"));
		//System.out.println("Response " + jsonPathEvaluator.get("items"));
		
		List<String> jsonResponse = response.jsonPath().getList("items");
		
		System.out.println(jsonResponse.size());
		
		int size = jsonResponse.size();
		Assert.assertTrue(size <= 30);
		
		// Test Scenario 8: Verify the full name contains  RewardsNetwork
		List<String> allNames = jsonPathEvaluator.getList("items.full_name");
		 
		String testStr = "RewardsNetwork";
		
		for(String name : allNames)
		{
			System.out.println("Name: " + name);
			String str = name.toUpperCase();
			Assert.assertTrue(str.contains(testStr.toUpperCase()));
		}
		
        // Test Scenario 9: Verify the response is sorted by number of forks by desc order
		List<Integer> allForks = jsonPathEvaluator.getList("items.forks");
		 
		for(Integer fork : allForks)
		{
			System.out.println("Fork Number: " + fork);
		}
        
		List<Integer> tmp = new ArrayList<Integer>(allForks);
		Collections.sort(tmp,Collections.reverseOrder());
		boolean sorted = tmp.equals(allForks);
		Assert.assertTrue(sorted);
		
	}

}
