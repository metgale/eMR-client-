package hr.foi.air.services;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

/**
 * Web service communication&JSON parse
 * 
 * @author FRM
 * 
 */
public class CommentService {

	/**
	 * Gets comments (JSON from web URL)
	 * 
	 * @param id review id
	 * @return  returns comments
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws JSONException
	 */
	public CommentModel[] getComments(int id) throws ClientProtocolException,
			IOException, JSONException {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet request = new HttpGet("http://gale.origami.hr/reviews/view/"
				+ id + ".json");
		ResponseHandler<String> handler = new BasicResponseHandler();
		String result = httpclient.execute(request, handler);

		httpclient.getConnectionManager().shutdown();
		JSONObject data = new JSONObject(result);
		JSONObject data1 = data.getJSONObject("review");
		JSONArray data2 = data1.getJSONArray("Comment");

		CommentModel[] comments = new CommentModel[data2.length()];
		if (data2.length() == 0) {
			return comments;
		}
		for (int i = 0; i < data2.length(); i++) {
			JSONObject oneObject = data2.getJSONObject(i);
			CommentModel commentModel = new CommentModel();
			commentModel.setComment(oneObject.getString("comment"));
			comments[i] = commentModel;
		}
		return comments;
	}

	// Add comment
	/**
	 * Adds comment (JSON to web URL)
	 * 
	 * 
	 * @param comment
	 * @param c
	 * @throws AuthenticationException
	 */
	public void add(String comment, Context c) throws AuthenticationException {
		String url = "http://gale.origami.hr/comments/add.json";
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		StringEntity se = null;
		try {
			se = new StringEntity(comment);
		} catch (UnsupportedEncodingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		UserService u = new UserService();
		UsernamePasswordCredentials creds = u.getCredentials(c);
		post.addHeader(new BasicScheme().authenticate(creds, post));
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
