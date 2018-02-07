package com.ml.edu;

import android.content.Context;

import com.ml.edu.old.TheOldHomeActivity;
import com.ml.edu.old.music.TheOldMusicActivity;

/**
 * Created by afirez on 18-2-1.
 */

public class OldRouter {

    public static void routeToOldHomeActivity(Context context) {
        if (context != null) {
          context.startActivity(TheOldHomeActivity.intent(context));
        }
    }

    public static void routeToOldMusicActivity(Context context) {
        if (context != null) {
            context.startActivity(TheOldMusicActivity.intent(context));
        }
    }
}
