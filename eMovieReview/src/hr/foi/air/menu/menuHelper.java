package hr.foi.air.menu;

import hr.foi.air.database.DataAdapter;
import android.content.Context;
import android.view.Menu;

/**
 * Menu helper
 * 
 * @author FRM
 * 
 */
public class menuHelper {

	/**
	 * Sets visibility to menu items depending on user status (if logged in true
	 * or false)
	 * 
	 * @param menu menu
	 *            
	 * @param c context
	 *           
	 * @return - true or false visibilities
	 */
	public boolean onPrepareOptionsMenu(Menu menu, Context c) {
		DataAdapter db = new DataAdapter(c);
		db.openToRead();
		int isLogged = db.isUserLogged();
		db.close();
		if (isLogged != 0) {
			menu.getItem(2).setVisible(false);
			return false;
		} else {
			menu.getItem(1).setVisible(false);
			menu.getItem(2).setVisible(true);
			return false;
		}
	}
}
