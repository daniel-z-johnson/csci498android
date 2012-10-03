package csci498.danjohns.LunchList;

import org.mcsoxford.rss.RSSFeed;
import org.mcsoxford.rss.RSSReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.util.Log;

public class FeedActivity extends Activity {
	
	private static class FeedTask extends AsyncTask<String, Void, RSSFeed> {
		private RSSReader reader = new RSSReader();
		private Exception e = null;
		private FeedActivity activity = null;
		
		FeedTask(FeedActivity activity) {
			attach(activity);
		}
		
		void attach(FeedActivity activity) {
			this.activity = activity;
		}
		
		void detach() {
			this.activity = null;
		}
		
		@Override
		public RSSFeed doInBackground(String... urls) {
			RSSFeed result = null;
			
			try {
				result = reader.load(urls[0]);
			} catch(Exception e) {
				this.e = e;
			}
			
			return result;
		}
		
		@Override
		public void onPostExecute(RSSFeed feed) {
			if(e == null)
				activity.setFeed(feed);
			else{
				Log.e("LunchList", "Exception parsing feed", e);
				//activity.goBlooey(e);
			}
		}
	}
}
