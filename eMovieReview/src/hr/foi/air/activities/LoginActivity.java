package hr.foi.air.activities;

import hr.foi.air.database.DataAdapter;
import hr.foi.air.menu.menuHelper;
import hr.foi.air.services.UserService;

import java.io.IOException;

import org.apache.http.auth.AuthenticationException;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

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
 * User can login to application
 * 
 * @author FRM
 * 
 */
public class LoginActivity extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
	}

	/**
	 * User login
	 * 
	 * @param target
	 * @throws AuthenticationException
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws JSONException
	 */
	public void btnLogin(View target) throws AuthenticationException,
			ClientProtocolException, IOException, JSONException {

		EditText username = (EditText) findViewById(R.id.usernameTxt);
		EditText password = (EditText) findViewById(R.id.passwordTxt);

		String user = username.getText().toString();
		String pass = password.getText().toString();

		UserService u = new UserService();
		if (u.login(user, pass, getBaseContext())) {
			Toast.makeText(this, "Welcome " + user, Toast.LENGTH_SHORT).show();
			Intent i = new Intent(this, MainMenuActivity.class);
			startActivity(i);
		} else {
			Toast.makeText(this, "Wrong username or password/no connection",
					Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * Opens Signup activity
	 * 
	 * @param target
	 */
	public void btnSignup(View target) {
		Intent i = new Intent(LoginActivity.this, SignupActivity.class);
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
			i = new Intent(LoginActivity.this, MainMenuActivity.class);
			startActivity(i);
			break;
		case R.id.Movies:
			i = new Intent(LoginActivity.this, ListMoviesActivity.class);
			startActivity(i);
			break;
		case R.id.Login:
			i = new Intent(LoginActivity.this, LoginActivity.class);
			startActivity(i);
			break;
		}

		return true;
	}

}
