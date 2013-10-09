package hr.foi.air.activities;

import hr.foi.air.database.DataAdapter;
import hr.foi.air.menu.menuHelper;
import hr.foi.air.services.UserService;

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
 * User can register a new account
 * 
 * @author FRM
 * 
 */
public class SignupActivity extends Activity {
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup_activity);
	}

	/**
	 * Registers new account
	 * 
	 * @param target
	 */
	public void userReg(View target) {
		String username = ((EditText) findViewById(R.id.username)).getText()
				.toString();
		String password = ((EditText) findViewById(R.id.password)).getText()
				.toString();
		JSONObject data = new JSONObject();
		try {
			data.put("username", username);
			data.put("password", password);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		UserService s = new UserService();
		String user = data.toString();
		s.userReg(user);
		Toast.makeText(this, "Thank you for registering!", Toast.LENGTH_SHORT)
				.show();
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
			i = new Intent(SignupActivity.this, MainMenuActivity.class);
			startActivity(i);
			break;
		case R.id.Movies:
			i = new Intent(SignupActivity.this, ListMoviesActivity.class);
			startActivity(i);
			break;
		case R.id.Login:
			i = new Intent(SignupActivity.this, LoginActivity.class);
			startActivity(i);
			break;
		}
		return true;
	}
}