package hr.foi.air.activities;

import hr.foi.air.database.DataAdapter;
import hr.foi.air.menu.menuHelper;
import hr.foi.air.services.ReviewService;
import hr.foi.air.services.UserModel;

import java.io.UnsupportedEncodingException;

import org.apache.http.auth.AuthenticationException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * User can post new review
 * 
 * @author FRM
 * 
 */
public class AddReviewActivity extends Activity {
	int movie_id;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addreview_activity);
		Bundle b = this.getIntent().getExtras();
		movie_id = b.getInt("movie_id");
	}

	/**
	 * Save review (on button click)
	 * 
	 * @param target
	 * @throws AuthenticationException
	 * @throws JSONException
	 * @throws UnsupportedEncodingException
	 */
	public void postReview(View target) throws AuthenticationException,
			JSONException, UnsupportedEncodingException {
		String title = ((EditText) findViewById(R.id.title)).getText()
				.toString();
		String rew = ((EditText) findViewById(R.id.review)).getText()
				.toString();

		DataAdapter db = new DataAdapter(getBaseContext());
		db.openToRead();
		UserModel user = db.selectUser();
		int user_id = user.getId();
		db.close();

		JSONObject review = new JSONObject();

		try {
			review.put("movie_id", movie_id);
			review.put("title", title);
			review.put("review", rew);
			review.put("user_id", user_id);

		} catch (JSONException e) {
		}
		ReviewService r = new ReviewService();
		String rev = review.toString();
		r.add(rev, getBaseContext());
		Toast.makeText(this, "Review has been saved, redirecting...",
				Toast.LENGTH_SHORT).show();

		Intent i = new Intent(this, ListMoviesActivity.class);
		startActivity(i);
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
			i = new Intent(AddReviewActivity.this, MainMenuActivity.class);
			startActivity(i);
			break;
		case R.id.Movies:
			i = new Intent(AddReviewActivity.this, ListMoviesActivity.class);
			startActivity(i);
			break;
		case R.id.Login:
			i = new Intent(AddReviewActivity.this, LoginActivity.class);
			startActivity(i);
			break;
		}

		return true;
	}

}