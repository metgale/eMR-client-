package hr.foi.air.activities;

import hr.foi.air.database.DataAdapter;
import hr.foi.air.menu.menuHelper;
import hr.foi.air.services.CommentModel;
import hr.foi.air.services.CommentService;

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
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * User can browse all the reviews comments
 * 
 * @author FRM
 * 
 */
public class ListCommentsActivity extends Activity {

	CommentModel[] data = null;
	private ArrayAdapter<String> listAdapter;

	protected void onCreate(Bundle icicle) {

		super.onCreate(icicle);
		setContentView(R.layout.listcomments_activity);
		Bundle b = this.getIntent().getExtras();
		int id = b.getInt("id");
		getComments(id);
	}

	/**
	 * Gets comments by movie id and shows them listed
	 * 
	 * @param id
	 *            - review id from intents bundle
	 */
	private void getComments(int id) {
		ListView mainListView;
		mainListView = (ListView) findViewById(R.id.mainListView);
		ColorDrawable divcolor = new ColorDrawable(Color.GRAY);
		mainListView.setDivider(divcolor);
		mainListView.setDividerHeight(1);

		CommentService c = new CommentService();
		try {
			data = c.getComments(id);
		} catch (ClientProtocolException e) {

		} catch (IOException e) {

		} catch (JSONException e) {

		}

		listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow);
		for (int i = 0; i < data.length; i++) {
			listAdapter.add(data[i].getComment());
		}
		mainListView.setAdapter(listAdapter);
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
			i = new Intent(ListCommentsActivity.this, MainMenuActivity.class);
			startActivity(i);
			break;
		case R.id.Movies:
			i = new Intent(ListCommentsActivity.this, ListMoviesActivity.class);
			startActivity(i);
			break;
		case R.id.Login:
			i = new Intent(ListCommentsActivity.this, LoginActivity.class);
			startActivity(i);
			break;
		}
		return true;
	}

}
