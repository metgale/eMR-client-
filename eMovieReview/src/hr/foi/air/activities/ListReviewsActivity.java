package hr.foi.air.activities;

import hr.foi.air.database.DataAdapter;
import hr.foi.air.menu.menuHelper;
import hr.foi.air.services.ReviewModel;
import hr.foi.air.services.ReviewService;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * 
 * User can browse all the movie reviews and choose details from one to be
 * opened in new activity
 * 
 * @author FRM
 * 
 */
public class ListReviewsActivity extends Activity {
	ReviewModel[] data = null;
	private ArrayAdapter<String> listAdapter;

	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.listreviews_activity);
		Bundle b = this.getIntent().getExtras();
		int id = b.getInt("id");
		listReviews(id);
	}

	/**
	 * Gets reviews by movie id and shows them listed
	 * 
	 * @param id
	 *            - movieId from intents bundle
	 */
	private void listReviews(int id) {
		ListView mainListView;
		mainListView = (ListView) findViewById(R.id.mainListView);

		ReviewService r = new ReviewService();
		try {
			data = r.movieReviews(id);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow);

		for (int i = 0; i < data.length; i++) {
			listAdapter.add(data[i].getTitle());
		}
		mainListView.setAdapter(listAdapter);
		ColorDrawable divcolor = new ColorDrawable(Color.GRAY);
		mainListView.setDivider(divcolor);
		mainListView.setDividerHeight(1);
		mainListView.setOnItemClickListener(new OnItemClickListener() {

			// spremanje id vrijednosti kliknutog filma u b.bundle,
			// spremanje u intent i slanje
			public void onItemClick(AdapterView<?> arg0, View arg1, int id,
					long arg3) {
				int dbId = (int) data[id].id;
				Bundle b = new Bundle();
				b.putInt("id", dbId);
				Intent i = new Intent(ListReviewsActivity.this,
						ReviewActivity.class);
				i.putExtras(b);
				ListReviewsActivity.this.startActivity(i);
			}
		});
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
			i = new Intent(ListReviewsActivity.this, MainMenuActivity.class);
			startActivity(i);
			break;
		case R.id.Movies:
			i = new Intent(ListReviewsActivity.this, ListMoviesActivity.class);
			startActivity(i);
			break;
		case R.id.Login:
			i = new Intent(ListReviewsActivity.this, LoginActivity.class);
			startActivity(i);
			break;

		}
		return true;
	}

}
