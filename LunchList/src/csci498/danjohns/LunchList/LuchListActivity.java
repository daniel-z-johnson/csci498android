package csci498.danjohns.LunchList;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

public class LuchListActivity extends Activity {
	List<Restaurant> model = new ArrayList<Restaurant>();
	RestaurantAdapter adapter=null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Button save = (Button)findViewById(R.id.save);
        
        save.setOnClickListener(onSave);
        
        ListView list = (ListView)findViewById(R.id.restraurants);
        
        adapter = new RestaurantAdapter();
        list.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    private View.OnClickListener onSave = new View.OnClickListener(){

		@Override
		public void onClick(View v) {
			Restaurant r = new Restaurant();
			EditText name = (EditText)findViewById(R.id.name);
			EditText address = (EditText)findViewById(R.id.addr);
			
			r.setName(name.getText().toString());
			r.setAddress(address.getText().toString());
			
			RadioGroup types = (RadioGroup)findViewById(R.id.types);
			
			switch(types.getCheckedRadioButtonId()){
				case R.id.sit_down:
					r.setType("sit_down");
					break;
				
				case R.id.take_out:
					r.setType("take_out");
					break;
					
				case R.id.delivery:
					r.setType("delivery");
					break;
			}
			
			adapter.add(r);
		}
    };
    
    class RestaurantAdapter extends ArrayAdapter<Restaurant> {
    	public RestaurantAdapter() {
			super(LuchListActivity.this,
					android.R.layout.simple_list_item_1,
					model);
		}
    }
    
    public View getView(int position, View convertView, ViewGroup parant){
    	View row = convertView;
    	
    	if( row == null ){
    		LayoutInflater inflater = getLayoutInflater();
    		row = inflater.inflate(R.layout.row, null);
    	}
    	
    	Restaurant r = model.get(position);
    	((TextView)row.findViewById(R.id.title)).setText(r.getName());
    	((TextView)row.findViewById(R.id.address)).setText(r.getName());
    	
    	ImageView icon = (ImageView)row.findViewById(R.id.icon);
    	
    	if(r.getType().equals("sit_down"))
    		icon.setImageResource(R.drawable.ball_red);
    	else if(r.getType().equals("take_out"))
    		icon.setImageResource(R.drawable.ball_yellow);
    	else
    		icon.setImageResource(R.drawable.ball_green);
    	return row;
    }
}
