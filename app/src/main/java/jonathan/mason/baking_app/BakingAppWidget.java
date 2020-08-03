package jonathan.mason.baking_app;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import jonathan.mason.baking_app.Data.Recipe;

/**
 * Implementation of App Widget functionality fro Baking App.
 */
public class BakingAppWidget extends AppWidgetProvider {

    /**
     * Update all instances of widget to show last selected ingredients.
     * <p>Based on answer to "Update Android Widget From Activity" by Atul O Holic:
     * https://stackoverflow.com/questions/4073907/update-android-widget-from-activity/4074665.</p>
     * @param context Context.
     * @appWidgetManager App widget manager.
     * @param selectedRecipe Selected recipe.
     */
    static void updateAllAppWidgets(Context context, AppWidgetManager appWidgetManager, Recipe selectedRecipe) {

        // Construct the RemoteViews object
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
        remoteViews.setTextViewText(R.id.widget_ingredients_caption, context.getString(R.string.widget_ingredients_caption_with_recipe, selectedRecipe.getName()));
        remoteViews.setTextViewText(R.id.widget_ingredients, selectedRecipe.getIngredients(context));

        // Launch MainActivity screen if widget is clicked.
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);

        // Instruct the widget manager to update the widgets
        appWidgetManager.updateAppWidget(new ComponentName(context, BakingAppWidget.class), remoteViews);
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);

        // Launch MainActivity screen if widget is clicked.
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

