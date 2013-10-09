package hr.foi.air.activities;

import hr.foi.air.database.DataAdapter;
import hr.foi.air.menu.menuHelper;
import hr.foi.air.services.MovieModel;
import hr.foi.air.services.MovieService;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * User can see poster or open AddReview/ListReviews activity
 * 
 * @author FRM
 * 
 */
public class MovieActivity extends Activity {
	int id;
	String titleString;

	public void onCreate(Bundle icicle) {

		super.onCreate(icicle);
		setContentView(R.layout.movie_activity);

		Bundle b = this.getIntent().getExtras();
		id = b.getInt("id");
		TextView title = (TextView) findViewById(R.id.title);
		ImageView poster = (ImageView) findViewById(R.id.poster);

		DataAdapter db = new DataAdapter(getBaseContext());
		db.openToRead();
		int isLogged = db.isUserLogged();
		db.close();
		if (isLogged == 0) {
			Button button = (Button) findViewById(R.id.addReview);
			button.setVisibility(View.INVISIBLE);
		}

		MovieService m = new MovieService();
		MovieModel movie = null;
		try {
			movie = m.getView(id);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		titleString = movie.getTitle();
		String posterString = movie.getPoster();
		title.append(titleString);

		BitmapFactory.Options bmOptions;
		bmOptions = new BitmapFactory.Options();
		bmOptions.inSampleSize = 1;
		Bitmap bm = LoadImage(posterString, bmOptions);
		poster.setImageBitmap(bm);
	}

	/**
	 * Opens list of reviews
	 * 
	 * @param target
	 */
	public void openUsersReviews(View target) {
		Bundle b = new Bundle();
		b.putInt("id", id);
		Intent i = new Intent(MovieActivity.this, ListReviewsActivity.class);
		i.putExtras(b);
		MovieActivity.this.startActivity(i);
	}

	/**
	 * Loads image from URL
	 * 
	 * @param URL
	 *            - poster URL f
	 * @param options
	 * @return bitmap
	 */
	private Bitmap LoadImage(String URL, BitmapFactory.Options options) {
		Bitmap bitmap = null;
		InputStream in = null;
		try {
			in = OpenHttpConnection(URL);
			bitmap = BitmapFactory.decodeStream(in, null, options);
			in.close();
		} catch (IOException e1) {
		}
		return bitmap;
	}

	/**
	 * Opens HttpConnection for loading image
	 * 
	 * @param strURL
	 * @return inputStream
	 * @throws IOException
	 */
	private InputStream OpenHttpConnection(String strURL) throws IOException {
		InputStream inputStream = null;
		URL url = new URL(strURL);
		URLConnection conn = url.openConnection();

		try {
			HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.setRequestMethod("GET");
			httpConn.connect();

			if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				inputStream = httpConn.getInputStream();
			}
		} catch (Exception ex) {
		}
		return inputStream;
	}

	/**
	 * Opens Addreview activity
	 * 
	 * @param target
	 */
	public void addReview(View target) {

		Bundle b = new Bundle();
		b.putInt("movie_id", id);
		Intent i = new Intent(MovieActivity.this, AddReviewActivity.class);
		i.putExtras(b);
		MovieActivity.this.startActivity(i);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		menuHelper m = new menuHelper();
		Context c = getBaseContext();
		m.onPrepareOptionsMenu(menu, c);
		return true;
	}

	Intent i;

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.Logout:
			DataAdapter db = new DataAdapter(getBaseContext());
			db.openToWrite();
			db.Logout();
			db.close();
			i = new Intent(MovieActivity.this, MainMenuActivity.class);
			startActivity(i);
			break;
		case R.id.Movies:
			i = new Intent(MovieActivity.this, ListMoviesActivity.class);
			startActivity(i);
			break;
		case R.id.Login:
			i = new Intent(MovieActivity.this, LoginActivity.class);
			startActivity(i);
			break;
		}

		return true;
	}

}
