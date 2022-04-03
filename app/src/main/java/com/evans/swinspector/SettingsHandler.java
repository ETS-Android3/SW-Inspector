package com.evans.swinspector;

import android.content.SharedPreferences;

public class SettingsHandler {

    SharedPreferences settings;

    public SettingsHandler(SharedPreferences settings) {
        this.settings = settings;
    }

    public boolean reduceFlats() {
        return settings.getBoolean("reduceFlats", false);
    }

    public void writeReduceFlats(boolean b) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("reduceFlats", b);
        editor.apply();
    }

    public double getFlatWeight() {
        return Double.parseDouble(settings.getString("flatWeight", "0.0"));
    }

    public void modifyFlatWeight(double weight) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("flatWeight", String.valueOf(weight));
        editor.apply();
    }

    public boolean includeSet() {
        return settings.getBoolean("includeSet", false);
    }

    public void writeSet(boolean b) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("includeSet", b);
        editor.apply();
    }

    public double getSetWeight() {
        return Double.parseDouble(settings.getString("setWeight", "0.0"));
    }

    public void modifySetWeight(double weight) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("setWeight", String.valueOf(weight));
        editor.apply();
    }
}
