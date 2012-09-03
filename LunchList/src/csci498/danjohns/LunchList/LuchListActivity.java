package csci498.danjohns.LunchList;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LuchListActivity extends Activity {
	Restaurant r = new Restaurant();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Button save = (Button)findViewById(R.id.save);
        
        save.setOnClickListener(onSave);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    private View.OnClickListener onSave = new View.OnClickListener(){

		@Override
		public void onClick(View v) {
			EditText name = (EditText)findViewById(R.id.name);
			EditText address = (EditText)findViewById(R.id.addr);
			
			r.setName(address.getText().toString());
			r.setAddress(address.getText().toString());
		}
    };
}
