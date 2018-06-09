package automationTests;

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
	
	public class Item {		 
	    Integer id;
	    String name;
	    String full_name;
	    String description;	   
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		RestAssured.baseURI = "https://api.github.com/";
		 
		// Get the RequestSpecification of the request that you want to sent
		// to the server. The server is specified by the BaseURI that we have
		// specified in the above step.
		RequestSpecification httpRequest = RestAssured.given();
 
		// Make a request to the server by specifying the method Type and the method URL.
		// This will return the Response from the server. Store the response in a variable.
		Response response = httpRequest.request(Method.GET, "/search/repositories?q=RewardsNetwork in:Name&sort=forks&order=desc");
 
		// Now let us print the body of the message to see what response
		// we have recieved from the server
		String responseBody = response.getBody().asString();
		//System.out.println("Response Body is =>  " + responseBody);	
		
		// Verify the Response code is 200
		int statusCode = response.getStatusCode();
		 
		// Assert that correct status code is returned.
		Assert.assertEquals(statusCode /*actual value*/, 200 /*expected value*/);
		
		String statusLine = response.getStatusLine();
		Assert.assertEquals(statusLine /*actual value*/, "HTTP/1.1 200 OK" /*expected value*/);
		
		// Reader header of a give name. In this line we will get
		// Header named Content-Type
		String contentType = response.header("Content-Type");
		System.out.println("Content-Type value: " + contentType);
		
		Assert.assertEquals(contentType /* actual value */, "application/json; charset=utf-8" /* expected value */);
	
	 
		// Reader header of a give name. In this line we will get
		// Header named Server
		String serverType =  response.header("Server");
		System.out.println("Server value: " + serverType);
		Assert.assertEquals(serverType /* actual value */, "GitHub.com" /* expected value */);
		
	 
		// Reader header of a give name. In this line we will get
		// Header named Content-Encoding
		String acceptLanguage = response.header("Content-Encoding");
		System.out.println("Content-Encoding: " + acceptLanguage);
		
		Assert.assertEquals(acceptLanguage /* actual value */, "gzip" /* expected value */);
		
		
		Headers allHeaders = response.headers();
		
		// Iterate over all the Headers
		for(Header header : allHeaders)
		{
			System.out.println("Key: " + header.getName() + " Value: " + header.getValue());
		}
		
		// To check for sub string presence get the Response body as a String.
		// Do a String.contains
		
		Assert.assertEquals(responseBody.contains("RewardsNetwork"), true );
		
		
		
		JsonPath jsonPathEvaluator = response.jsonPath();
		System.out.println("Response " + jsonPathEvaluator.get("total_count"));
		//System.out.println("Response " + jsonPathEvaluator.get("items"));
		
		List<String> jsonResponse = response.jsonPath().getList("items");
		
		System.out.println(jsonResponse.size());
		
		
		
		List<String> allNames = jsonPathEvaluator.getList("items.full_name");
		 
		// Iterate over the list and print individual book item
		for(String name : allNames)
		{
			System.out.println("Name: " + name);
		}
		
        Integer firstId = jsonPathEvaluator.get("items[0].id");
        System.out.println("First ID " + firstId);
        
        
		List<Integer> allForks = jsonPathEvaluator.getList("items.forks");
		 
		// Iterate over the list and print individual book item
		for(Integer fork : allForks)
		{
			System.out.println("Fork Number: " + fork);
		}
        
		
		List<Integer> allStars = jsonPathEvaluator.getList("items.stargazers_count");
		 
		// Iterate over the list and print individual book item
		for(Integer stars : allStars)
		{
			System.out.println("Star Number: " + stars);
		}

/*
    	List<Item> allBooks = jsonPathEvaluator.getList("items", Item.class);
    	 
    	// Iterate over the list and print individual book item
    	// Note that every book entry in the list will be complete Json object of book
    	
    	for(Item book : allBooks)
    	{
    		System.out.println("Book: " + book.name);
    	}

*/
	}

}
