package csci498.danjohns.LunchList;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.SystemClock;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;

import android.app.TabActivity;
import android.widget.TabHost;
import android.view.MenuItem;
import android.widget.Toast;

import android.widget.AdapterView;

import java.util.concurrent.atomic.AtomicBoolean;

@SuppressWarnings("deprecation")
public class LuchListActivity extends TabActivity {
	List<Restaurant> model = new ArrayList<Restaurant>();
	RestaurantAdapter adapter = null;
	EditText name = null;
	EditText address = null;
	EditText notes = null;
	RadioGroup types = null;
	Restaurant current = null;
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

		adapter = new RestaurantAdapter();
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
	public void onPause() {
	}

	@Override
	public void onResume() {
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		helper.close();
	}


	//@Override
	//public boolean onCreateOptionsMenu(Menu menu) {
	//}

	private View.OnClickListener onSave = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			String type = null;
			
			switch (types.getCheckedRadioButtonId()) {
			case R.id.sit_down:
				current.setType("sit_down");
				break;

			case R.id.take_out:
				current.setType("take_out");
				break;

			case R.id.delivery:
				current.setType("delivery");
				break;
			}

			helper.insert(name.getText().toString(), address.getText().toString(), type, notes.getText().toString());
		}
	};

	private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parant, View view, int position,
				long id) {
			current = model.get(position);

			name.setText(current.getName());
			address.setText(current.getAddress());
			notes.setText(current.getNotes());

			if (current.getType().equals("sit_down"))
				types.check(R.id.sit_down);
			else if (current.getType().equals("take_out"))
				types.check(R.id.take_out);
			else
				types.check(R.id.delivery);

			getTabHost().setCurrentTab(1);
		}
	};

	//@Override
	//public boolean onOptionsItemSelected(MenuItem item) {
	//}

	class RestaurantAdapter extends ArrayAdapter<Restaurant> {
		RestaurantAdapter() {
			super(LuchListActivity.this, R.layout.row, model);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parant) {
			View row = convertView;
			RestaurantHolder holder = null;

			if (row == null) {
				LayoutInflater inflater = getLayoutInflater();

				row = inflater.inflate(R.layout.row, parant, false);
				holder = new RestaurantHolder(row);
				row.setTag(holder);
			} else
				holder = (RestaurantHolder) row.getTag();

			holder.populateFrom(model.get(position));

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

		void populateFrom(Restaurant r) {
			name.setText(r.getName());
			address.setText(r.getAddress());

			if (r.getType().equals("sit_down")) {
				icon.setImageResource(R.drawable.ball_red);
				name.setTextColor(Color.RED);
			} else if (r.getType().equals("take_out")) {
				icon.setImageResource(R.drawable.ball_yellow);
				name.setTextColor(Color.YELLOW);
			} else {
				icon.setImageResource(R.drawable.ball_green);
				name.setTextColor(Color.GREEN);
			}
		}
	}
}
