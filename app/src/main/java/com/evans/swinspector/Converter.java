package com.evans.swinspector;

import android.util.Log;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class Converter {
    @TypeConverter
    public static String fromGrade(Rune.Grade grade) {
        return grade.toString();
    }

    @TypeConverter
    public static Rune.Grade toGrade(String grade) {
        return Rune.Grade.valueOf(grade);
    }

    @TypeConverter
    public static String fromSet(Rune.Set set) {
        return set.toString();
    }

    @TypeConverter
    public static Rune.Set toSet(String set) {
        return Rune.Set.valueOf(set);
    }

    @TypeConverter
    public static String fromSubstat(Substat substat) {
        Gson gson = new Gson();

        Type type = new TypeToken<Substat>() {}.getType();
        String json = gson.toJson(substat, type);
        return json;
    }

    @TypeConverter
    public static Substat toSubstat(String inSubstat) {
        Gson gson = new Gson();
        Type type = new TypeToken<Substat>() {}.getType();
        Substat substat = gson.fromJson(inSubstat, type);

        return substat;
    }

    @TypeConverter
    public static String fromStatType(List<Substat.StatType> types) {
        Gson gson = new Gson();

        Type type = new TypeToken<List<Substat.StatType>>() {}.getType();
        String json = gson.toJson(types, type);
        return json;
    }

    @TypeConverter
    public static List<Substat.StatType> toStatList(String inType) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Substat.StatType>>() {}.getType();
        List<Substat.StatType> types = gson.fromJson(inType, type);

        return types;
    }

    @TypeConverter
    public static String fromRuneSetList(List<Rune.Set> types) {
        Gson gson = new Gson();

        Type type = new TypeToken<List<Rune.Set>>() {}.getType();
        String json = gson.toJson(types, type);
        return json;
    }

    @TypeConverter
    public static List<Rune.Set> toRuneSetList(String inType) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Rune.Set>>() {}.getType();
        List<Rune.Set> types = gson.fromJson(inType, type);

        return types;
    }

    @TypeConverter
    public static String fromSubstatList(List<Substat> substats) {
        Gson gson = new Gson();

        Type type = new TypeToken<List<Substat>>() {}.getType();
        String json = gson.toJson(substats, type);
        return json;
    }

    @TypeConverter
    public static List<Substat> toSubstatList(String string) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Substat>>() {}.getType();
        List<Substat> substats = gson.fromJson(string, type);

        return substats;
    }

    @TypeConverter
    public static String fromAccountMonsterList(List<AccountMonster> monsters) {
        Gson gson = new Gson();

        Type type = new TypeToken<List<AccountMonster>>() {}.getType();
        String json = gson.toJson(monsters, type);
        return json;
    }

    @TypeConverter
    public static List<AccountMonster> toAccountMonsterList(String string) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<AccountMonster>>() {}.getType();
        List<AccountMonster> monsters = gson.fromJson(string, type);

        return monsters;
    }

    @TypeConverter
    public static String fromRuneList(List<Rune> runes) {
        Gson gson = new Gson();

        Type type = new TypeToken<List<Rune>>() {}.getType();
        String json = gson.toJson(runes, type);
        return json;
    }

    @TypeConverter
    public static List<Rune> toRuneList(String string) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Rune>>() {}.getType();
        List<Rune> runes = gson.fromJson(string, type);

        return runes;
    }
}
