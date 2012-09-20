package csci498.danjohns.LunchList;

import android.os.Bundle;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;

import android.app.TabActivity;
import android.content.Context;
import android.database.Cursor;
import android.widget.TabHost;
import android.widget.AdapterView;

@SuppressWarnings("deprecation")
public class LuchListActivity extends TabActivity {
	Cursor model = null;
	RestaurantAdapter adapter = null;
	EditText name = null;
	EditText address = null;
	EditText notes = null;
	RadioGroup types = null;
	RestaurantHelper helper = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		helper = new RestaurantHelper(this);

		name = (EditText) findViewById(R.id.name);
		address = (EditText) findViewById(R.id.addr);
		notes = (EditText) findViewById(R.id.notes);
		types = (RadioGroup) findViewById(R.id.types);

		Button save = (Button) findViewById(R.id.save);

		save.setOnClickListener(onSave);

		ListView list = (ListView) findViewById(R.id.restaurants);

		model = helper.getAll();
		startManagingCursor(model);
		adapter = new RestaurantAdapter(model);
		list.setAdapter(adapter);

		TabHost.TabSpec spec = getTabHost().newTabSpec("tag1");

		spec.setContent(R.id.restaurants);
		spec.setIndicator("List", getResources().getDrawable(R.drawable.list));
		getTabHost().addTab(spec);

		spec = getTabHost().newTabSpec("tag2");
		spec.setContent(R.id.details);
		spec.setIndicator("Details",
				getResources().getDrawable(R.drawable.restaurant_icon));
		getTabHost().addTab(spec);

		getTabHost().setCurrentTab(0);

		list.setOnItemClickListener(onListClick);
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

			helper.insert(name.getText().toString(), address.getText().toString(), type, notes.getText().toString());
			model.requery();
		}
	};

	private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parant, View view, int position,
				long id) {
			model.moveToPosition(position);
			name.setText(helper.getName(model));
			address.setText(helper.getAddress(model));
			notes.setText(helper.getNotes(model));
			
			String type = helper.getType(model);
			
			if (type.equals("sit_down"))
				types.check(R.id.sit_down);
			else if (type.equals("take_out"))
				types.check(R.id.take_out);
			else
				types.check(R.id.delivery);
			
			getTabHost().setCurrentTab(1);
		}
	};

	class RestaurantAdapter extends CursorAdapter {

		public RestaurantAdapter(Cursor c) {
			super(LuchListActivity.this, c);
		}

		@Override
		public void bindView(View row, Context ctext, Cursor c) {
			RestaurantHolder holder = (RestaurantHolder)row.getTag();
			
			holder.populateFrom(c, helper);
		}

		@Override
		public View newView(Context ctxt, Cursor c, ViewGroup parent) {
			LayoutInflater inflater = getLayoutInflater();
			View row = inflater.inflate(R.layout.row, parent, false);
			RestaurantHolder holder = new RestaurantHolder(row);
			
			row.setTag(holder);
			
			return row;
		}
	}

	static class RestaurantHolder {
		private TextView name = null;
		private TextView address = null;
		private ImageView icon = null;

		RestaurantHolder(View row) {
			name = (TextView) row.findViewById(R.id.title);
			address = (TextView) row.findViewById(R.id.address);
			icon = (ImageView) row.findViewById(R.id.icon);
		}

		void populateFrom(Cursor c, RestaurantHelper helper) {
			name.setText(helper.getName(c));
			address.setText(helper.getAddress(c));

			if (helper.getType(c).equals("sit_down")) {
				icon.setImageResource(R.drawable.ball_red);
				name.setTextColor(Color.RED);
			} else if (helper.getType(c).equals("take_out")) {
				icon.setImageResource(R.drawable.ball_yellow);
				name.setTextColor(Color.YELLOW);
			} else {
				icon.setImageResource(R.drawable.ball_green);
				name.setTextColor(Color.GREEN);
			}
		}
	}
}
