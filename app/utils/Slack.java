package utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;





import org.json.JSONArray;
import org.json.JSONObject;

import play.libs.ws.WS;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class Slack {


	private static Slack instance;
	private String token;
	private String url;
	protected Slack() {
		Config config = ConfigFactory.parseFile(new File("conf/application.conf")).resolve();
		
		token = config.getString("slack.token");
		url = config.getString("slack.webhook");
	}
	public Slack getInstance() {
		if (instance == null) instance = new Slack();
		return instance;
	}
	
	@SuppressWarnings("unchecked")
	public void postToSlack(ISlackOntology type) throws IOException {

				
		URL object = new URL(url);

		JSONObject innerfields=new JSONObject();
		innerfields.put("title", type.getTitle());
		innerfields.put("value", type.getContent());
		innerfields.put("short", false);

		JSONArray fields = new JSONArray();
		fields.put(innerfields);
		JSONObject attachment = new JSONObject();
		attachment.put("fallback", type.getContent());
		attachment.put("color", type.getColorHex());
		attachment.put("fields",fields);

		JSONArray array = new JSONArray();
		array.put(attachment);

		JSONObject obj=new JSONObject();
		obj.put("username", type.getOntology());
		obj.put("icon_url", type.getIcon());
		obj.put("attachments", array);

		WS.url(url).post("token="+token+"&payload="+obj.toString());

	}
	public void postToSlack(String content) throws IOException {
		URL object = new URL(url);

		JSONObject obj=new JSONObject();
		obj.put("text", content);
		System.out.print(obj);

		WS.url(url).setContentType("application/x-www-form-urlencoded")
		.post("token="+token+"&payload="+obj.toString());

	}
}
