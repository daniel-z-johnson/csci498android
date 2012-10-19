package csci498.danjohns.LunchList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class DetailForm extends Activity {
	EditText name = null;
	EditText address = null;
	EditText notes = null;
	EditText feed = null;
	RadioGroup types = null;
	RestaurantHelper helper = null;
	String restaurantID = null;
	TextView location = null;
	LocationManager locMgr = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_form);
		
		helper = new RestaurantHelper(this);

		name = (EditText) findViewById(R.id.name);
		address = (EditText) findViewById(R.id.addr);
		notes = (EditText) findViewById(R.id.notes);
		types = (RadioGroup) findViewById(R.id.types);
		feed = (EditText) findViewById(R.id.feed);
		location = (TextView)findViewById(R.id.location);
		locMgr = (LocationManager)getSystemService(LOCATION_SERVICE);

		//Button save = (Button) findViewById(R.id.save);

		//save.setOnClickListener(onSave);
		
		restaurantID = getIntent().getStringExtra(LunchListActivity.ID_EXTRA);
		
		if (restaurantID != null)
			load();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		helper.close();
	}
	
	private void save() {
		
		if (name.getText().toString().length() > 0) {
			String type = null;
			
			switch (types.getCheckedRadioButtonId()) {
			case R.id.sit_down:
				type = "sit_down";
				break;
			
			case R.id.take_out:
				type = "take_out";
				break;
			
			default:
				type = "delivery";
				break;
			}
			
			if (restaurantID == null)
				helper.insert(name.getText().toString(),
						      address.getText().toString(), 
						      type, 
						      notes.getText().toString(), 
						      feed.getText().toString());
			else
				helper.update(restaurantID, 
							   name.getText().toString(), 
							   address.getText().toString(), 
							   type, 
							   notes.getText().toString(), 
							   feed.getText().toString());
		}
	}
	
	@Override
	public void onPause() {
		save();
		locMgr.removeUpdates(onLocationChange);
		
		super.onPause();
	}
	
	private void load() {
		Cursor c = helper.getById(restaurantID);
		
		c.moveToFirst();
		name.setText(helper.getName(c));
		address.setText(helper.getAddress(c));
		notes.setText(helper.getNotes(c));
		feed.setText(helper.getFeed(c));
		
		String type = helper.getType(c);
		
		if(type.equals("sit_down"))
			types.check(R.id.sit_down);
		else if (type.equals("take_out"))
			types.check(R.id.take_out);
		else
			types.check(R.id.delivery);
		
		location.setText(String.valueOf(helper.getLatitude(c))
						+ ","
						+ String.valueOf(helper.getLongitude(c)));
		
		c.close();
	}
	
	@Override
	public void onSaveInstanceState(Bundle state) {
		super.onSaveInstanceState(state);
		
		state.putString("name", name.getText().toString());
		state.putString("address", address.getText().toString());
		state.putString("notes", notes.getText().toString());
		state.putInt("type", types.getCheckedRadioButtonId());
	}
	
	@Override
	public void onRestoreInstanceState(Bundle state) {
		super.onRestoreInstanceState(state);
		
		name.setText(state.getString("name"));
		address.setText(state.getString("address"));
		notes.setText(state.getString("notes"));
		types.check(state.getInt("type"));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.details_option, menu);
		
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.feed) {
			if( idNetworkAvailable()) {
				Intent i = new Intent(this, FeedActivity.class);
				
				i.putExtra(FeedActivity.FEED_URL, feed.getText().toString());
				startActivity(i);
			}else {
				Toast.makeText(this, "Sorry the internet is not available", Toast.LENGTH_LONG).show();
			}
			
			return true;
		} else if (item.getItemId() == R.id.location) {
			locMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, onLocationChange);
			
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (restaurantID == null) {
			menu.findItem(R.id.location).setEnabled(false);
		}
		
		return super.onPrepareOptionsMenu(menu);
	}
	
	private boolean idNetworkAvailable(){
		ConnectivityManager cm = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		return info != null;
	}
	
	LocationListener onLocationChange = new LocationListener() {

		@Override
		public void onLocationChanged(Location fix) {
			helper.updateLocation(restaurantID, fix.getLatitude(), fix.getLongitude());
			location.setText(String.valueOf(fix.getLatitude()) + " " + String.valueOf(fix.getLongitude()));
			Toast.makeText(DetailForm.this, "Location saved", Toast.LENGTH_LONG).show();
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
		}
		
	};
}
