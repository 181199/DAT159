package no.hvl.dat159;

import static spark.Spark.*;

import java.io.IOException;

import com.google.gson.*;

public class Main {

	static Temperature temp;
	static Heat state;

	public static void main(String[] args) throws IOException {

		temp = new Temperature();
		state = new Heat();

		port(8080);

		after((req, res) -> {
			res.type("application/json");
		});

		get("/temperature/current", (request, response) -> tempToJson());

		put("/temperature/current", (request, response) -> {
			Gson gson = new Gson();
			temp = gson.fromJson(request.body(), Temperature.class);
			return tempToJson();
		});

		get("/state/current", (request, response) -> stateToJson());
		
		put("/state/current", (request, response) -> {
			Gson gson = new Gson();
			state = gson.fromJson(request.body(), Heat.class);
			return stateToJson();
		});

	}

	static String tempToJson() {

		Gson gson = new Gson();

		String jsonInString = gson.toJson(temp);

		return jsonInString;
	}

	static String stateToJson() {

		Gson gson = new Gson();

		String jsonInString = gson.toJson(state);

		return jsonInString;
	}

}