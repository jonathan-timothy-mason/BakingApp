package jonathan.mason.baking_app;

import android.content.Context;
import android.content.res.Configuration;

/**
 * General utility functions.
 */
public class Utils {
    /**
     * Roughly determine if device is tablet or phone based about smallest screen width.
     * @param context Context.
     * @return True if tablet, false if phone.
     */
    public static boolean isTablet(Context context) {
        if(context.getResources().getConfiguration().smallestScreenWidthDp >= 720)
            return true;
        else
            return false;
    }

    /**
     * Determine if device is orientated to landscape or portrait.
     * @param context Context.
     * @return True if landsacpe, false if portrait.
     */
    public static boolean isLandscape(Context context) {
        if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            return true;
        else
            return false;
    }
}
