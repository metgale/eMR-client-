package hr.foi.air.services;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

/**
 * Vote
 * 
 * @author FRM
 * 
 */
public class Vote {

	/**
	 * Sends new review rating (JSON to web URL)
	 * 
	 * @param newVote  new reviews rating
	 * @param id review id
	 * @param c context
	 */
	public void vote(int newVote, int id, Context c) {

		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://gale.origami.hr/reviews/edit/"
				+ id + ".json");
		JSONObject rev = new JSONObject();
		try {
			rev.put("vote", newVote);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String revi = rev.toString();
		StringEntity se = null;
		try {
			se = new StringEntity(revi);
		} catch (UnsupportedEncodingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		UserService u = new UserService();
		UsernamePasswordCredentials creds = u.getCredentials(c);
		try {
			post.addHeader(new BasicScheme().authenticate(creds, post));
		} catch (AuthenticationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		post.setEntity(se);
		try {
			client.execute(post);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
