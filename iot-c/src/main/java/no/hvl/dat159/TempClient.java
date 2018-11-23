package no.hvl.dat159;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TempClient {


	private String thingName = "temperature";
	private static JsonParser jsonParser = new JsonParser();
	
	public TempClient() {
	}
	
	public boolean publishTemp(double d) throws IOException {
		
		JsonObject content = new JsonObject();
		
		content.addProperty("temperature", d);
				
		thingName = URLEncoder.encode(thingName, "UTF-8");
		
		URL url = new URL("http://localhost:8080/temperature/current");
				
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		
		connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
		connection.setRequestMethod("PUT");
		connection.setDoInput(true);
		connection.setDoOutput(true);
		
		PrintWriter out = new PrintWriter(connection.getOutputStream());
		
		out.println(content.toString());
		
		out.flush();
		out.close();
		
		JsonObject response = readResponse(connection.getInputStream());

		connection.disconnect();

		return (response.has("this") && response.get("this").getAsString().equals("succeeded"));
	}

	public String getTemp() throws IOException {
					
		thingName = URLEncoder.encode(thingName, "UTF-8");
		
		URL url = new URL("http://localhost:8080/temperature/current");
				
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		
		connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
		connection.setRequestProperty("Accept", "application/json");
		connection.setRequestMethod("GET");
		connection.setDoInput(true);
		connection.setDoOutput(true);
		
		JsonObject response = readResponse(connection.getInputStream());
	
		connection.disconnect();

		return response.get("temperature").toString();
	}
	
	private JsonObject readResponse(InputStream in) {
		
		Scanner scan = new Scanner(in);
		StringBuilder sn = new StringBuilder();
		
		while (scan.hasNext())
			sn.append(scan.nextLine()).append('\n');
		
		scan.close();
		
		return jsonParser.parse(sn.toString()).getAsJsonObject();
	}
}