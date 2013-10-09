package hr.foi.air.database;

import hr.foi.air.services.UserModel;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Database operations
 * 
 * @author FRM
 * 
 */
public class DataAdapter {
	public static final String DATABASE_NAME = "database.db";
	public static final int DATABASE_VERSION = 1;
	public static final String TABLE = "data";

	private DBHelper sqLiteHelper;
	private SQLiteDatabase sqLiteDatabase;
	private Context context;

	public DataAdapter(Context c) {
		context = c;
	}

	/**
	 * Opens readable database
	 * 
	 * @return
	 * @throws android.database.SQLException
	 */
	public DataAdapter openToRead() throws android.database.SQLException {
		sqLiteHelper = new DBHelper(context, DATABASE_NAME, null,
				DATABASE_VERSION);
		sqLiteDatabase = sqLiteHelper.getReadableDatabase();
		return this;
	}

	/**
	 * Opens writable database
	 * 
	 * @return
	 * @throws android.database.SQLException
	 */
	public DataAdapter openToWrite() throws android.database.SQLException {
		sqLiteHelper = new DBHelper(context, DATABASE_NAME, null,
				DATABASE_VERSION);
		sqLiteDatabase = sqLiteHelper.getWritableDatabase();
		return this;
	}

	/**
	 * Close database
	 * 
	 */
	public void close() {
		sqLiteHelper.close();
	}

	/**
	 * Inserts new user to database (for login)
	 * 
	 * @param username
	 *            - users username
	 * @param password
	 *            - users password
	 * @param id
	 *            - users id
	 * @return
	 */
	public long insertUser(String username, String password, int id) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("username", username);
		contentValues.put("password", password);
		contentValues.put("id", id);
		return sqLiteDatabase.insert(TABLE, null, contentValues);
	}

	/**
	 * Checks wheter user is logged in (for auth)
	 * 
	 * @return
	 */
	public int isUserLogged() {
		Cursor cur = sqLiteDatabase.rawQuery("SELECT COUNT(*) FROM data", null);
		if (cur != null) {
			cur.moveToFirst();
			if (cur.getInt(0) == 0) {
				cur.close();
				return 0;
			}
		}
		cur.close();
		return 1;

	}

	/**
	 * Get user details (logged in user)
	 * 
	 * @return
	 */
	public UserModel selectUser() {
		String[] columns = new String[] { "id", "username", "password" };
		Cursor cursor = sqLiteDatabase.query(TABLE, columns, null, null, null,
				null, null);
		UserModel user = new UserModel();
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			int id = cursor.getInt(cursor.getColumnIndex("id"));
			String username = cursor.getString(cursor
					.getColumnIndex("username"));
			String password = cursor.getString(cursor
					.getColumnIndex("password"));
			user.setUsername(username);
			user.setPassword(password);
			user.setId(id);
			cursor.close();

		}
		cursor.close();
		return user;

	}

	/**
	 * Logs out user
	 * 
	 */
	public void Logout() {
		sqLiteDatabase.delete(TABLE, null, null);
	}
}
