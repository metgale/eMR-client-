package hr.foi.air.activities;

import hr.foi.air.database.DataAdapter;
import hr.foi.air.menu.menuHelper;
import hr.foi.air.services.CommentService;
import hr.foi.air.services.ReviewModel;
import hr.foi.air.services.ReviewService;
import hr.foi.air.services.UserModel;
import hr.foi.air.services.Vote;

import java.io.IOException;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * User can like, disslike review or open ListComments activity
 * 
 * @author FRM
 * 
 */
public class ReviewActivity extends Activity {
	Vote v = new Vote();
	ReviewService r = new ReviewService();
	int id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.review_activity);
		Bundle b = this.getIntent().getExtras();
		id = b.getInt("id");
		ReviewModel review = null;
		Button up = (Button) findViewById(R.id.btnLike);
		Button down = (Button) findViewById(R.id.btnDisslike);
		DataAdapter db = new DataAdapter(getBaseContext());
		db.openToRead();

		int isLogged = db.isUserLogged();
		db.close();

		LinearLayout lin = (LinearLayout) findViewById(R.id.linearLayout);
		if (isLogged == 0) {
			lin.setVisibility(View.INVISIBLE);
			down.setVisibility(ImageView.INVISIBLE);
			up.setVisibility(ImageView.INVISIBLE);
		}

		try {
			try {
				review = r.getView(id);
				String title = review.getTitle();
				String rev = review.getReview();
				String user = review.getUsername();
				int vote = review.getVote();
				TextView username = (TextView) findViewById(R.id.user);
				TextView showTitle = (TextView) findViewById(R.id.txtViewTitle);
				TextView showReview = (TextView) findViewById(R.id.txtViewReview);
				TextView showVote = (TextView) findViewById(R.id.txtViewVote);
				int green = getResources().getColor(R.color.green);
				int red = getResources().getColor(R.color.red);
				showTitle.setText(title);
				showReview.setText(rev);
				username.setText(user);

				// Provjera stanja glasova - promjena pozadinske boje ovisno o
				// odgovoru. Dorada u 3. fazi.
				if (vote > 0) {
					showVote.setText(Integer.toString(vote));
					showVote.setTextColor(green);
				} else if (vote < 0) {
					showVote.setText(Integer.toString(vote));
					showVote.setTextColor(red);
				} else if (vote == 0) {
					showVote.setText(Integer.toString(vote));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch blockS
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			i = new Intent(ReviewActivity.this, MainMenuActivity.class);
			startActivity(i);
			break;
		case R.id.Movies:
			i = new Intent(ReviewActivity.this, ListMoviesActivity.class);
			startActivity(i);
			break;
		case R.id.Login:
			i = new Intent(ReviewActivity.this, LoginActivity.class);
			startActivity(i);
			break;

		}
		return true;
	}

	/**
	 * Adds like to review
	 * 
	 * @param target
	 * @throws IOException
	 * @throws JSONException
	 */
	public void like(View target) throws IOException, JSONException {
		int vote = in();
		int newVote = vote + 1;
		out(newVote);
	}

	/**
	 * Adds disslike to review
	 * 
	 * @param target
	 * @throws IOException
	 * @throws JSONException
	 */
	public void disslike(View target) throws IOException, JSONException {
		int vote = in();
		int newVote = vote - 1;
		out(newVote);
	}

	/**
	 * Gets current review rates
	 * 
	 * @return vote
	 * @throws IOException
	 * @throws JSONException
	 */
	public int in() throws IOException, JSONException {
		ReviewModel review = r.getView(id);
		int vote = review.getVote();
		return vote;
	}

	/**
	 * Sends new review rates
	 * 
	 * @param newVote
	 */
	public void out(int newVote) {
		v.vote(newVote, id, getBaseContext());
		refresh();
		Toast.makeText(this, "Thanks for voting!", Toast.LENGTH_SHORT).show();
	}

	/**
	 * Reopens Review activity
	 * 
	 */
	public void refresh() {
		finish();
		startActivity(getIntent());
	}

	/**
	 * Opens listComments activity
	 * 
	 * @param Target
	 *            - target
	 */
	public void showComments(View Target) {
		Bundle b = new Bundle();
		b.putInt("id", id);
		Intent i = new Intent(ReviewActivity.this, ListCommentsActivity.class);
		i.putExtras(b);
		ReviewActivity.this.startActivity(i);
	}

	/**
	 * User post comment
	 * 
	 * @param target
	 *            - target
	 * @throws AuthenticationException
	 * @throws JSONException
	 * @throws UnsupportedEncodingException
	 */
	public void postComment(View target) throws AuthenticationException,
			JSONException, UnsupportedEncodingException {
		String comm = ((EditText) findViewById(R.id.comment)).getText()
				.toString();

		DataAdapter db = new DataAdapter(getBaseContext());
		db.openToRead();
		UserModel user = db.selectUser();
		int user_id = user.getId();
		db.close();

		JSONObject commentPost = new JSONObject();

		try {
			commentPost.put("user_id", user_id);
			commentPost.put("review_id", id);
			commentPost.put("comment", comm);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CommentService c = new CommentService();
		String comment = commentPost.toString();
		c.add(comment, getBaseContext());
		Toast.makeText(this, "Comment saved", Toast.LENGTH_SHORT).show();

		Bundle b = new Bundle();
		b.putInt("id", id);
		Intent i = new Intent(ReviewActivity.this, ReviewActivity.class);
		i.putExtras(b);
		ReviewActivity.this.startActivity(i);
	}

}
