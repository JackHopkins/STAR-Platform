package utils;

import play.mvc.Http.Request;
import uk.ac.rhul.cs.dice.star.http.HttpRequest;

public class RequestConverter {

	public static HttpRequest convert(Request request) {
		return new HttpRequest(request.headers(), request.queryString(), request.host(), request.path(), request.body().asText());
	}
}
