package com.kayali_developer.bakingapp.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class RecipeNetworkUtils {
    // Constructor
    public RecipeNetworkUtils() {
        throw new AssertionError();
    }

    public static boolean isConnected(Context context) {
        // Checking state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
