package com.evans.swinspector;

import android.util.Log;

import androidx.room.*;

import java.util.List;

@Entity
public class Monster {
    @PrimaryKey(autoGenerate = true)
    public int mid;

    @ColumnInfo(name = "unit_master_id")
    public int unit_master_id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "atk")
    public int atk;

    @ColumnInfo(name = "def")
    public int def;

    @ColumnInfo(name = "hp")
    public int hp;

    @ColumnInfo(name = "spd")
    public int spd;

    @ColumnInfo(name = "crit_rate")
    public int crit_rate;

    @ColumnInfo(name = "crit_damage")
    public int crit_damage;

    @ColumnInfo(name = "resistance")
    public int resistance;

    @ColumnInfo(name = "accuracy")
    public int accuracy;

    @ColumnInfo(name = "slot_two_pref")
    public List<Substat.StatType> slot_two_pref;

    @ColumnInfo(name = "slot_four_pref")
    public List<Substat.StatType> slot_four_pref;

    @ColumnInfo(name = "slot_six_pref")
    public List<Substat.StatType> slot_six_pref;

    @ColumnInfo(name = "set_pref")
    public List<Rune.Set> set_pref;

    @ColumnInfo(name = "second_awaken")
    public Integer second_awaken;

    public Monster(String name, int unit_master_id, int atk, int def, int hp, int spd, int crit_rate, int crit_damage, int resistance, int accuracy, List<Substat.StatType> slot_two_pref, List<Substat.StatType> slot_four_pref, List<Substat.StatType> slot_six_pref, List<Rune.Set> set_pref, Integer second_awaken) {
        this.name = name;
        this.unit_master_id = unit_master_id;
        this.atk = atk;
        this.def = def;
        this.hp = hp;
        this.spd = spd;
        this.crit_rate = crit_rate;
        this.crit_damage = crit_damage;
        this.resistance = resistance;
        this.accuracy = accuracy;
        this.slot_two_pref = slot_two_pref;
        this.slot_four_pref = slot_four_pref;
        this.slot_six_pref = slot_six_pref;
        if(set_pref != null && set_pref.size() > 0){
            this.set_pref = set_pref;
        }else {
            this.set_pref = null;
        }

        this.second_awaken = second_awaken;
    }

    public int getScore(Substat.StatType stat) {
        if(stat == Substat.StatType.HP || stat == Substat.StatType.HP_PERC) {
            return this.hp;
        } else if(stat == Substat.StatType.ATK || stat == Substat.StatType.ATK_PERC) {
            return this.atk;
        } else if(stat == Substat.StatType.DEF || stat == Substat.StatType.DEF_PERC) {
            return this.def;
        } else if(stat == Substat.StatType.SPD) {
            return this.spd;
        }else if(stat == Substat.StatType.CRI_RATE) {
            return this.crit_rate;
        }else if(stat == Substat.StatType.CRI_DMG) {
            return this.crit_damage;
        }else if(stat == Substat.StatType.RESISTANCE) {
            return this.resistance;
        }else if(stat == Substat.StatType.ACCURACY) {
            return this.accuracy;
        }else {
            return -1;
        }
    }

    @Override
    public String toString() {
        return this.name + "\n" +
                "ATK " + this.atk + ", " +
                "DEF " + this.def + ", " +
                "HP " + this.hp + ", " +
                "SPD " + this.spd + ", " +
                "CRI_RATE " + this.crit_rate + ", " +
                "CRI_DMG " + this.crit_damage + ", " +
                "RESISTANCE " + this.resistance + ", " +
                "ACCURACY " + this.accuracy + "\n" +
                hasSlotTwoPref() + "\n" +
                hasSlotFourPref() + "\n" +
                hasSlotSixPref() + "\n" +
                hasSet() + "\n";
    }

    public String hasSlotTwoPref() {
        if(this.slot_two_pref != null) {
            return this.slot_two_pref.toString();
        }

        return "No slot two pref";
    }

    public String hasSlotFourPref() {
        if(this.slot_four_pref != null) {
            return this.slot_four_pref.toString();
        }

        return "No slot four pref";
    }

    public String hasSlotSixPref() {
        if(this.slot_six_pref != null) {
            return this.slot_six_pref.toString();
        }

        return "No slot six pref";
    }

    public String hasSet() {
        if(this.set_pref != null) {
            return this.set_pref.toString();
        }
        return "No sets";
    }

    public boolean checkDefault() {
        if(atk == 0 && def == 0 && hp == 0 && spd == 0 && crit_rate == 0 && crit_damage == 0 && resistance == 0 && accuracy == 0) {
            if(set_pref == null || set_pref.size() == 0 && slot_two_pref == null || slot_two_pref.size() == 0 && slot_four_pref == null || slot_four_pref.size() == 0 && slot_six_pref == null || slot_six_pref.size() == 0) {
                return true;
            }
        }

        return false;
    }
}