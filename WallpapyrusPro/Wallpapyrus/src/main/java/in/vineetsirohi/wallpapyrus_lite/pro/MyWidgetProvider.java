package in.vineetsirohi.wallpapyrus_lite.pro;

import in.vineetsirohi.utility.AppConstants;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class MyWidgetProvider extends AppWidgetProvider {

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		Log.d(AppConstants.LOG_TAG, "MyWidgetProvider.onUpdate()" + " : "
				);
		
		 final int numberOfWidgets = appWidgetIds.length;
	        for (int i=0; i<numberOfWidgets; i++) {
	            int appWidgetId = appWidgetIds[i];

	            Intent intent = new Intent(context, SettingsActivity.class);
	            intent.putExtra(SettingsActivity.IS_PROMPT_FOR_SET_LIVEWALLPAPER, true);
	            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

	            // Get the layout for the App Widget and attach an on-click listener
	            // to the button
	            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.mywidget);
	            views.setOnClickPendingIntent(R.id.imageButton1, pendingIntent);

	            // Tell the AppWidgetManager to perform an update on the current app widget
	            appWidgetManager.updateAppWidget(appWidgetId, views);
	        }

	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		Log.d(AppConstants.LOG_TAG, "MyWidgetProvider.onReceive()" + " : "
				);
		
	}

}
