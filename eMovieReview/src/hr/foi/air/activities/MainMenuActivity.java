package hr.foi.air.activities;

import hr.foi.air.database.DataAdapter;
import hr.foi.air.menu.menuHelper;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 * User can login, signup or list movies
 * 
 * @author FRM
 * 
 */
public class MainMenuActivity extends Activity {

	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.mainmenu_activity);
		Button logout = (Button) findViewById(R.id.btn_Logout);
		logout.setVisibility(View.INVISIBLE);

		DataAdapter db = new DataAdapter(getBaseContext());
		db.openToRead();
		int isLogged = db.isUserLogged();
		db.close();

		Button login = (Button) findViewById(R.id.btn_Login);
		Button signup = (Button) findViewById(R.id.btn_Signup);
		if (isLogged != 0) {
			login.setVisibility(View.INVISIBLE);
			signup.setVisibility(View.INVISIBLE);
			logout.setVisibility(View.VISIBLE);

			Intent i = new Intent(this, ListMoviesActivity.class);
			startActivity(i);
		}
	}

	/**
	 * Opens Signup activity
	 * 
	 * @param target
	 */
	public void openSignup(View target) {
		Intent i = new Intent(this, SignupActivity.class);
		startActivity(i);
	}

	/**
	 * Opens Login activity
	 * 
	 * @param target
	 */
	public void openLogin(View target) {
		Intent i = new Intent(this, LoginActivity.class);
		startActivity(i);

	}

	/**
	 * Opens ListMovies activity
	 * 
	 * @param target
	 */
	public void openPreview(View target) {
		Intent i = new Intent(this, ListMoviesActivity.class);
		startActivity(i);
	}

	/**
	 * Opens Logout activity
	 * 
	 * @param target
	 */
	public void logout(View target) {
		DataAdapter db = new DataAdapter(getBaseContext());
		db.openToWrite();
		db.Logout();
		db.close();
		refresh();
	}

	/**
	 * Reopens Mainmenu Activity
	 * 
	 */
	public void refresh() {
		finish();
		startActivity(getIntent());
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
			i = new Intent(MainMenuActivity.this, MainMenuActivity.class);
			startActivity(i);
			break;
		case R.id.Movies:
			i = new Intent(MainMenuActivity.this, ListMoviesActivity.class);
			startActivity(i);
			break;

		case R.id.Login:
			i = new Intent(MainMenuActivity.this, LoginActivity.class);
			startActivity(i);
			break;
		}

		return true;
	}

}
