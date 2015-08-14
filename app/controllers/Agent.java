package controllers;

import exceptions.AgentNotFoundException;
import exceptions.ContainerNotFoundException;
import models.GolemPlatform;
import play.*;
import play.libs.F.Function;
import play.libs.F.Function0;
import play.libs.F.Promise;
import play.mvc.*;
import play.twirl.api.Html;
import uk.ac.rhul.cs.dice.star.http.ErrorCode;
import uk.ac.rhul.cs.dice.star.http.HttpRequest;
import uk.ac.rhul.cs.dice.star.http.HttpResponse;
import views.html.*;

public class Agent extends Controller {

    public static Promise<Result> get(String container, String agent) {
		
		final HttpRequest httpRequest = new HttpRequest(request().headers(),
				request().queryString(),
				request().host(),
				request().path(),
				request().body().asText());
		
		
		return GolemPlatform.getInstance().routeRequest(httpRequest, container, agent).map(
				new Function<HttpResponse, Result>() {
					@Override
					public Result apply(HttpResponse response) throws Throwable {
						if (response.getError().equals(ErrorCode.NONE)) {
							return ok(new Html(response.getContent()));
						}else{
							return badRequest(response.getError().getMessage());
						}
					}
				}
			);
      
    }
    public static Result put(String container, String agent) {
    	 return ok();
    }
    public static Result delete(String container, String agent) {
        GolemPlatform.getInstance().getContainer(container).removeEntity(agent);
        return ok();
    }
    public static Result post(String container, String agent) {
    	try {
        GolemPlatform.getInstance().createAgent(container, agent);
    	}catch(ContainerNotFoundException e) {
    	e.printStackTrace();
    	return badRequest();
    	} catch (AgentNotFoundException e) {
			e.printStackTrace();
			return badRequest();
		}
    	return ok();
    }
    
}
