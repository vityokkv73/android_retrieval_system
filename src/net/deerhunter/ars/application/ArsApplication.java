/*
 * This file is part of Android retrieval system project.
 * 
 * Android retrieval system is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version. 
 * 
 * Android retrieval system is distributed in the hope that
 * it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Android retrieval system. If not, see <http://www.gnu.org/licenses/>.
 */

package net.deerhunter.ars.application;

import android.app.Application;
import android.content.SharedPreferences;

/**
 * This is a main application class.
 * 
 * @author DeerHunter (vityokkv73@gmail.com)
 */
public class ArsApplication extends Application {
	private static volatile ArsApplication instance;

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
	}

	/**
	 * Returns an application instance.
	 * 
	 * @return An application instance
	 */
	public static ArsApplication getInstance() {
		return instance;
	}

	/**
	 * Returns the application preferences.
	 * 
	 * @return Application preferences
	 */
	public SharedPreferences getAppPrefs() {
		return getSharedPreferences(getPackageName(), MODE_PRIVATE);
	}

}
