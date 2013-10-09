package hr.foi.air.services;

import hr.foi.air.database.DataAdapter;

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
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

/**
 * Web service communication and JSON parse
 * 
 * @author FRM
 * 
 */
public class UserService {

	/**
	 * Posts new user data (JSON to web URL)
	 * 
	 * @param user
	 *            - user data
	 */
	public void userReg(String user) {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://gale.origami.hr/users/add.json");
		StringEntity se = null;
		try {
			se = new StringEntity(user);
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		}
		se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		post.setEntity(se);
		try {
			client.execute(post);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets username and password (from database)
	 * 
	 * @param c
	 *            - context
	 * @return - returns usernamepasswordcredentials object
	 */
	public UsernamePasswordCredentials getCredentials(Context c) {
		DataAdapter db = new DataAdapter(c);
		db.openToRead();
		UserModel user = new UserModel();
		user = db.selectUser();
		String username = user.getUsername();
		String password = user.getPassword();
		UsernamePasswordCredentials creds = new UsernamePasswordCredentials(
				username, password);
		db.close();
		return creds;
	}

	/**
	 * Logs and authenticates user
	 * 
	 * @param username
	 *            - username
	 * @param password
	 *            - password
	 * @param c
	 *            - context
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws JSONException
	 * @throws AuthenticationException
	 */
	public boolean login(String username, String password, Context c)
			throws ClientProtocolException, IOException, JSONException,
			AuthenticationException {
		String url = "http://gale.origami.hr/users/login.json";
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);
		UsernamePasswordCredentials creds = new UsernamePasswordCredentials(
				username, password);
		String response = null;
		ResponseHandler<String> handler = new BasicResponseHandler();
		try {
			request.addHeader(new BasicScheme().authenticate(creds, request));
			response = httpclient.execute(request, handler);
			httpclient.getConnectionManager().shutdown();
		} catch (Exception e) {
			return false;
		}
		String usrname;
		int id;
		try {
			JSONObject response1 = new JSONObject(response);
			JSONObject one = response1.getJSONObject("user");
			JSONObject two = one.getJSONObject("User");

			UserModel user = new UserModel();
			user.setId(two.getInt("id"));
			user.setUsername(two.getString("username"));
			id = user.getId();
			usrname = user.getUsername();
		} catch (Exception e) {
			return false;
		}
		try {
			DataAdapter db = new DataAdapter(c);
			db.openToWrite();
			db.insertUser(usrname, password, id);
			db.close();
		} catch (Exception e) {
			return false;
		}
		return true;

	}

}
