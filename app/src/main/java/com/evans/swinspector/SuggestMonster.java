package com.evans.swinspector;

import androidx.annotation.NonNull;

import java.util.Locale;

public class SuggestMonster {

    public String name = "";
    public Double score = 0.0;

    public SuggestMonster(String name, Double score) {
        this.name = name;
        this.score = score;
    }

    @NonNull
    @Override
    public String toString() {
        return this.name + " " + String.format(Locale.ENGLISH, "%.2f%%", this.score);
    }
}
