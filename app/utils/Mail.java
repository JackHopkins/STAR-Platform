package utils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.HttpPost;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import play.libs.F.Function;
import play.libs.F.Promise;
import play.libs.ws.WSResponse;
import play.libs.ws.*;

import play.mvc.Results.*;
import play.mvc.Result;
import com.fasterxml.jackson.databind.JsonNode;
import com.microtripit.mandrillapp.lutung.MandrillApi;
import com.microtripit.mandrillapp.lutung.model.MandrillApiError;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage.Recipient;
import com.microtripit.mandrillapp.lutung.view.MandrillMessageStatus;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;


public class Mail {

	private static Mail instance;
	private static int mailPort;
	private static String mailHost;
	private static boolean mailTls;
	private static boolean mailSsl;
	private static String mailUser;
	private static String mailPassword ;

	protected Mail() {
				Config config = ConfigFactory.parseFile(new File("conf/application.conf")).resolve();
				mailHost = config.getString("mail.host");
				mailUser = config.getString("mail.user");
				mailPassword = config.getString("mail.password");
				mailSsl = config.getBoolean("mail.ssl");
				mailTls = config.getBoolean("mail.tls");
				mailPort = config.getInt("mail.port");
			
	}
	public static synchronized Mail getInstance() {
		if (instance == null) {
			instance = new Mail();
		}
		return instance;
	}
	public void sendEmail(String fromEmail,
			String fromName,
			String subject,
			String messageHtml,
			String recipientString) throws EmailException, MandrillApiError, IOException {

	/*	Email email = new SimpleEmail();
		email.setHostName(host);
		email.setSmtpPort(port);
		email.setAuthenticator(new DefaultAuthenticator(user, password));
		email.setTLS(tls);
		//email.setSSLOnConnect(ssl);
		/*	EmailAttachment attachment = new EmailAttachment();
		attachment.setPath(attachmentFile.getAbsolutePath());
		attachment.setDisposition(EmailAttachment.ATTACHMENT);
		//attachment.setDescription(attachmentFile.ge);
		attachment.setName(attachmentFile.getName());

		email.setFrom(from);
		email.setSubject(subject);
		email.setMsg(message);
		email.addTo(recipient);
		//email.attach(attachment);
		// send the email
		email.send();*/
		MandrillApi mandrillApi = new MandrillApi(mailPassword);

		// create your message
		MandrillMessage message = new MandrillMessage();
		message.setSubject(subject);
		message.setHtml(messageHtml);
		message.setAutoText(true);
		message.setFromName(fromName);
		message.setFromEmail(fromEmail);
		// add recipients
		ArrayList<Recipient> recipients = new ArrayList<Recipient>();
		Recipient recipient = new Recipient();
		recipient.setEmail(recipientString);
		//recipient.setName("Claire Annette");
		recipients.add(recipient);
		
		message.setTo(recipients);
		message.setPreserveRecipients(true);
		
		// ... add more message details if you want to!
		// then ... send
		MandrillMessageStatus[] messageStatusReports = mandrillApi
		        .messages().send(message, false);
	}
}
