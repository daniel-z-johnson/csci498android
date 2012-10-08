package csci498.danjohns.LunchList;

import org.mcsoxford.rss.RSSFeed;
import org.mcsoxford.rss.RSSItem;
import org.mcsoxford.rss.RSSReader;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.app.ListActivity;
import android.content.Intent;

public class FeedActivity extends ListActivity {
	public static final String FEED_URL = "csci498.danjohns.LunchList.FEED_URL";
	private InstanceState state = null;
	//Inner classes at end of methods
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		state = (InstanceState)getLastNonConfigurationInstance();
		
		if (state == null) {
			state = new InstanceState();
			state.handler = new FeedHandler(this);
			
			Intent i =  new Intent(this, FeedService.class);
			
			i.putExtra(FeedService.EXTRA_URL, getIntent().getStringExtra(FEED_URL));
			i.putExtra(FeedService.EXTRA_MESSENGER, new Messenger(state.handler));
			
			startService(i);
		} else {
			if (state.handler != null)
				state.handler.attach(this);
			
			if (state.feed != null)
				setFeed(state.feed);
		}
	}
	
	@Override
	public Object onRetainNonConfigurationInstance() {
		
		if (state.handler != null)
			state.handler.detach();
		
		return state;
		
	}
	
	private void setFeed(RSSFeed feed) {
		state.feed = feed;
		setListAdapter(new FeedAdapter(feed));
	}
	
	private void goBlooey(Throwable t) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		builder
			.setTitle("Exception!")
			.setMessage(t.toString())
			.setPositiveButton("ok", null)
			.show();
	}
	
	private static class FeedHandler extends Handler {
		private FeedActivity activity = null;
		
		FeedHandler(FeedActivity activity) {
			attach(activity);
		}
		
		void attach(FeedActivity activity) {
			this.activity = activity;
		}
		
		void detach() {
			this.activity = null;
		}
		
		@Override
		public void handleMessage (Message msg) {
			if (msg.arg1 == RESULT_OK)
				activity.setFeed((RSSFeed)msg.obj);
			else
				activity.goBlooey((Exception)msg.obj);
		}
	}
	
	private class FeedAdapter extends BaseAdapter {
		RSSFeed feed = null;
		
		FeedAdapter(RSSFeed feed) {
			super();
			
			this.feed = feed;
		}

		@Override
		public int getCount() {
			return feed.getItems().size();
		}

		@Override
		public Object getItem(int position) {
			return feed.getItems().get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			
			if (row == null) {
				LayoutInflater inflator = getLayoutInflater();
				
				row = inflator.inflate(android.R.layout.simple_list_item_1, parent, false);
			}
			
			RSSItem item =(RSSItem)getItem(position);
			
			((TextView)row).setText(item.getTitle()); //Lisp anyone?
			
			return row;
		}
				
	}
	
	private static class InstanceState {
		RSSFeed feed = null;
		FeedHandler handler = null;
	}
}
