package bobrchess.of.by.belaruschess.view.activity.impl;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class PackageModel {
    private Context context;

    public PackageModel(Context context) {
        this.context = context;
    }

    public void putTokenMap(Map<String, String> tokenMap) {
        final SharedPreferences.Editor editor = context.getSharedPreferences("userPreferences", MODE_PRIVATE).edit();
        tokenMap.entrySet().forEach(entry -> editor.putString(entry.getKey(), entry.getValue()));
        editor.apply();
    }

    public void putValue(String propertyName, String propertyValue) {
        SharedPreferences.Editor editor = context.getSharedPreferences("userPreferences", MODE_PRIVATE).edit();
        editor.putString(propertyName, propertyValue);
        editor.apply();
    }

    public String getValue(String propertyName) {
        SharedPreferences prefs = context.getSharedPreferences("userPreferences", MODE_PRIVATE);
        return prefs.getString(propertyName, null);
    }
}
