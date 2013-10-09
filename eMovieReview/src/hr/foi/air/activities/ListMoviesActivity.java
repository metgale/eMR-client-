package hr.foi.air.activities;

import hr.foi.air.database.DataAdapter;
import hr.foi.air.menu.menuHelper;
import hr.foi.air.services.MovieModel;
import hr.foi.air.services.MovieService;

import java.io.IOException;

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
 * User can browse all the movies and choose details from one to be opened in
 * new activity
 * 
 * @author FRM
 * 
 */
public class ListMoviesActivity extends Activity {

	public void onCreate(Bundle icicle) {

		super.onCreate(icicle);
		setContentView(R.layout.listmovies_activity);
		try {
			ispis();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets all movie titles and shows them listed
	 * 
	 * @throws IOException
	 */
	public void ispis() throws IOException {
		ArrayAdapter<String> listAdapter;
		ListView mainListView;
		mainListView = (ListView) findViewById(R.id.mainListView);

		MovieService m = new MovieService();
		final MovieModel[] data = m.getList();

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
				Intent i = new Intent(ListMoviesActivity.this,
						MovieActivity.class);
				i.putExtras(b);
				ListMoviesActivity.this.startActivity(i);
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
			i = new Intent(ListMoviesActivity.this, MainMenuActivity.class);
			startActivity(i);
			break;
		case R.id.Movies:
			i = new Intent(ListMoviesActivity.this, ListMoviesActivity.class);
			startActivity(i);
			break;
		case R.id.Login:
			i = new Intent(ListMoviesActivity.this, LoginActivity.class);
			startActivity(i);
			break;
		}
		return true;
	}
}
