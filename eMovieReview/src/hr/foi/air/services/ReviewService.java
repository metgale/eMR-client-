package hr.foi.air.services;

import hr.foi.air.http.HttpHelper;

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
public class ReviewService {


	/**
	 * Get single review data (JSON from web URL)
	 * 
	 * @param id
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 */
	public ReviewModel getView(int id) throws IOException, JSONException {
		String url = "http://gale.origami.hr/reviews/view/" + id + ".json";
		HttpHelper h = new HttpHelper();
		String result = h.get(url);
		JSONObject data = new JSONObject(result);
		JSONObject reviews = data.getJSONObject("review");
		JSONObject review = reviews.getJSONObject("Review");
		ReviewModel reviewModel = new ReviewModel();
		reviewModel.setId(review.getInt("id"));
		reviewModel.setTitle(review.getString("title"));
		reviewModel.setReview(review.getString("review"));
		reviewModel.setVote(review.getInt("vote"));
		JSONObject review2 = reviews.getJSONObject("User");
		reviewModel.setUsername(review2.getString("username"));
		return reviewModel;
	}
	

	/**
	 * Return movie reviews by movieId (JSON from web URL);
	 * 
	 * @param id
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws JSONException
	 */
	public ReviewModel[] movieReviews(int id) throws ClientProtocolException,
			IOException, JSONException {
		String url = "http://gale.origami.hr/movies/view/" + id + ".json";
		String result = "im empty";
		HttpHelper h = new HttpHelper();
		result = h.get(url);
		JSONObject data = new JSONObject(result);
		JSONObject data1 = data.getJSONObject("movie");
		JSONArray data2 = data1.getJSONArray("Review");

		ReviewModel[] movieReviews = new ReviewModel[data2.length()];
		for (int i = 0; i < data2.length(); i++) {
			JSONObject oneObject = data2.getJSONObject(i);
			ReviewModel reviewModel = new ReviewModel();
			reviewModel.setReview(oneObject.getString("review"));
			reviewModel.setTitle(oneObject.getString("title"));
			reviewModel.setVote(oneObject.getInt("vote"));
			reviewModel.setId(oneObject.getInt("id"));
			movieReviews[i] = reviewModel;
		}
		return movieReviews;
	}
	

	/**
	 * Adds new review data (JSON to web URL)
	 * 
	 * @param rev - review
	 * @param c - context 
	 * @throws AuthenticationException
	 */
	public void add(String rev, Context c) throws AuthenticationException {
		String url = "http://gale.origami.hr/reviews/add.json";
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		StringEntity se = null;
		try {
			se = new StringEntity(rev);
		} catch (UnsupportedEncodingException e2) {
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
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
