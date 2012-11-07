package csci498.danjohns.LunchList;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

public class LunchListWidget extends AppWidgetProvider {
	
	@Override
	public void onUpdate(Context ctxt, AppWidgetManager mgr, int[] appWidgetIds) {
		ctxt.startService(new Intent(ctxt, WidgetService.class));
	}
	
}
