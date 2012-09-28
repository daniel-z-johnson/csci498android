package csci498.danjohns.LunchList;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.app.Activity; 

public class EditPreferences extends PreferenceActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		addPreferencesFromResource(R.xml.preferences);
	}

}
