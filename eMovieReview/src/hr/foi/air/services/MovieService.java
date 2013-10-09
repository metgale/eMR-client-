package hr.foi.air.services;

import hr.foi.air.http.HttpHelper;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Web service communication&JSON parse
 * 
 * @author FRM
 * 
 */
public class MovieService {

	/**
	 * Gets all movies (JSON from web URL)
	 * 
	 * @return - returns movies
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public MovieModel[] getList() throws ClientProtocolException, IOException {
		String url = "http://gale.origami.hr/movies.json";
		String result = "im empty";
		HttpHelper h = new HttpHelper();
		result = h.get(url);
		try {
			JSONObject data = new JSONObject(result);
			JSONArray movielist = data.getJSONArray("movies");
			MovieModel[] movies = new MovieModel[movielist.length()];
			for (int i = 0; i < movielist.length(); i++) {
				JSONObject oneObject = movielist.getJSONObject(i);
				JSONObject movie = oneObject.getJSONObject("Movie");
				MovieModel movieModel = new MovieModel();
				movieModel.setId(movie.getInt("id"));
				movieModel.setTitle(movie.getString("title"));
				movieModel.setPoster(movie.getString("poster"));
				movies[i] = movieModel;
			}
			return movies;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Gets single movie data (JSON from web URL)
	 * 
	 * @param id
	 *            - movie id
	 * @return - returns movie data
	 * @throws IOException
	 * @throws JSONException
	 */
	public MovieModel getView(int id) throws IOException, JSONException {
		String url = "http://gale.origami.hr/movies/view/" + id + ".json";
		String result = "im empty";

		HttpHelper h = new HttpHelper();
		result = h.get(url);
		JSONObject data = new JSONObject(result);
		JSONObject movies = data.getJSONObject("movie");
		JSONObject movie = movies.getJSONObject("Movie");
		MovieModel movieModel = new MovieModel();
		movieModel.setTitle(movie.getString("title"));
		movieModel.setPoster(movie.getString("poster"));
		return movieModel;
	}
}
