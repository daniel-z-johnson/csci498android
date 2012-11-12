package csci498.danjohns.LunchList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class LunchListActivity extends FragmentActivity implements LunchFragment.OnRestaurantListener {
	public final static String ID_EXTRA = "csci498.danjohns._ID";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		LunchFragment lunch = (LunchFragment)getSupportFragmentManager().findFragmentById(R.id.lunch);
		
		lunch.setOnRestaurantListener(this);
	}

	@Override
	public void onRestaurantSelected(long id) {
		Intent i = new Intent(this, DetailForm.class);
		
		i.putExtra(ID_EXTRA, String.valueOf(id));
		startActivity(i);
	}
}
