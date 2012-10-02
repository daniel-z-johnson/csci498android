package csci498.danjohns.LunchList;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

public class DetailForm extends Activity {
	EditText name = null;
	EditText address = null;
	EditText notes = null;
	EditText feed = null;
	RadioGroup types = null;
	RestaurantHelper helper = null;
	String restaurantID = null;
	
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

		Button save = (Button) findViewById(R.id.save);

		save.setOnClickListener(onSave);
		
		restaurantID = getIntent().getStringExtra(LunchListActivity.ID_EXTRA);
		
		if (restaurantID != null)
			load();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		helper.close();
	}
	
	private View.OnClickListener onSave = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			String type = null;
			
			switch (types.getCheckedRadioButtonId()) {
				case R.id.sit_down:
					type = "sit_down";
					break;
					
				case R.id.take_out:
					type = "take_out";
					break;
					
				case R.id.delivery:
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
				helper.update(restaurantID, name.getText().toString(), 
											address.getText().toString(), 
											type, 
											notes.getText().toString(), 
											feed.getText().toString());
			
			finish();
		}
	};
	
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
}
