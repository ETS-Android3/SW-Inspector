package com.evans.swinspector;

import android.util.Log;
import android.widget.Toast;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@Entity
public class Rune {

    public enum Grade {
        COMMON,
        MAGIC,
        RARE,
        HERO,
        LEGEND
    }

    public enum Set {
        ENERGY,
        FATAL,
        BLADE,
        SWIFT,
        FOCUS,
        GUARD,
        ENDURE,
        SHIELD,
        REVENGE,
        WILL,
        NEMESIS,
        VAMPIRE,
        DESTROY,
        DESPAIR,
        VIOLENT,
        RAGE,
        FIGHT,
        DETERMINATION,
        ENHANCE,
        ACCURACY,
        TOLERANCE
    }

    public enum Type {
        REGULAR,
        ANCIENT
    }

    @PrimaryKey(autoGenerate = true)
    public long mid;

    @ColumnInfo(name = "grade")
    private Grade grade;
    @ColumnInfo(name = "set")
    private Set set;
    @ColumnInfo(name = "slot")
    private Integer slot;
    @ColumnInfo(name = "upgrade")
    private Integer upgrade;
    @ColumnInfo(name = "type")
    private Type type;
    @ColumnInfo(name = "natural_star")
    private Integer naturalStar;
    private Substat mainStat;
    private Substat innateStat;
    @ColumnInfo(name = "sub_stats")
    private List<Substat> subStats;
    @ColumnInfo(name = "isAccount")
    private boolean isAccount;

    @Ignore
    public Rune(Grade grade, Set set, Integer slot, Integer upgrade, Type type, Integer naturalStar, List<Substat> subStats, boolean isAccount) {
        if(grade.equals(Rune.Grade.LEGEND)) {
            if(subStats.size() == 6) {
                List<Substat> subs = subStats.subList(2, 6);
                this.grade = grade;
                this.set = set;
                this.slot = slot;
                this.upgrade = upgrade;
                this.type = type;
                this.naturalStar = naturalStar;
                this.mainStat = subStats.get(0);
                this.innateStat = subStats.get(1);
                this.subStats = subs;
                this.isAccount = isAccount;
            } else {
                List<Substat> subs = subStats.subList(1, 5);
                this.grade = grade;
                this.set = set;
                this.slot = slot;
                this.upgrade = upgrade;
                this.type = type;
                this.naturalStar = naturalStar;
                this.mainStat = subStats.get(0);
                this.subStats = subs;
                this.isAccount = isAccount;
            }
        } else if(grade.equals(Rune.Grade.HERO)) {
            if(subStats.size() == 5) {
                List<Substat> subs = subStats.subList(2, 5);
                this.grade = grade;
                this.set = set;
                this.slot = slot;
                this.upgrade = upgrade;
                this.type = type;
                this.naturalStar = naturalStar;
                this.mainStat = subStats.get(0);
                this.innateStat = subStats.get(1);
                this.subStats = subs;
                this.isAccount = isAccount;
            } else {
                List<Substat> subs = subStats.subList(1, 4);
                this.grade = grade;
                this.set = set;
                this.slot = slot;
                this.upgrade = upgrade;
                this.type = type;
                this.naturalStar = naturalStar;
                this.mainStat = subStats.get(0);
                this.subStats = subs;
                this.isAccount = isAccount;
            }
        } else if(grade.equals(Rune.Grade.RARE)) {
            if(subStats.size() == 4) {
                List<Substat> subs = subStats.subList(2, 4);
                this.grade = grade;
                this.set = set;
                this.slot = slot;
                this.upgrade = upgrade;
                this.type = type;
                this.naturalStar = naturalStar;
                this.mainStat = subStats.get(0);
                this.innateStat = subStats.get(1);
                this.subStats = subs;
                this.isAccount = isAccount;
            } else {
                List<Substat> subs = subStats.subList(1, 3);
                this.grade = grade;
                this.set = set;
                this.slot = slot;
                this.upgrade = upgrade;
                this.type = type;
                this.naturalStar = naturalStar;
                this.mainStat = subStats.get(0);
                this.subStats = subs;
                this.isAccount = isAccount;
            }
        }
        else if(grade.equals(Rune.Grade.MAGIC)) {
            if(innateStat != null) {
                List<Substat> subs = subStats.subList(0, 1);
                this.grade = grade;
                this.set = set;
                this.slot = slot;
                this.upgrade = upgrade;
                this.type = type;
                this.naturalStar = naturalStar;
                this.mainStat = mainStat;
                this.innateStat = innateStat;
                this.subStats = subs;
                this.isAccount = isAccount;
            } else {
                List<Substat> subs = subStats.subList(0, 1);
                this.grade = grade;
                this.set = set;
                this.slot = slot;
                this.upgrade = upgrade;
                this.type = type;
                this.naturalStar = naturalStar;
                this.mainStat = mainStat;
                this.subStats = subs;
                this.isAccount = isAccount;
            }
        }
        else if(grade.equals(Rune.Grade.COMMON)) {
            if(innateStat != null) {
                this.grade = grade;
                this.set = set;
                this.slot = slot;
                this.upgrade = upgrade;
                this.type = type;
                this.naturalStar = naturalStar;
                this.mainStat = mainStat;
                this.innateStat = innateStat;
                this.subStats = null;
                this.isAccount = isAccount;
            } else {
                this.grade = grade;
                this.set = set;
                this.slot = slot;
                this.upgrade = upgrade;
                this.type = type;
                this.naturalStar = naturalStar;
                this.mainStat = mainStat;
                this.subStats = null;
                this.isAccount = isAccount;
            }
        }
    }

    public Rune(long mid, Grade grade, Set set, Integer slot, Integer upgrade, Type type, Integer naturalStar, Substat mainStat, Substat innateStat, List<Substat> subStats, boolean isAccount) {
        this.mid = mid;
        if(grade.equals(Rune.Grade.LEGEND)) {
            if(innateStat != null) {
                List<Substat> subs = subStats.subList(0, 4);
                this.grade = grade;
                this.set = set;
                this.slot = slot;
                this.upgrade = upgrade;
                this.type = type;
                this.naturalStar = naturalStar;
                this.mainStat = mainStat;
                this.innateStat = innateStat;
                this.subStats = subs;
                this.isAccount = isAccount;
            } else {
                List<Substat> subs = subStats.subList(0, 4);
                this.grade = grade;
                this.set = set;
                this.slot = slot;
                this.upgrade = upgrade;
                this.type = type;
                this.naturalStar = naturalStar;
                this.mainStat = mainStat;
                this.subStats = subs;
                this.isAccount = isAccount;
            }
        } else if(grade.equals(Rune.Grade.HERO)) {
            if(innateStat != null) {
                List<Substat> subs = subStats.subList(0, 3);
                this.grade = grade;
                this.set = set;
                this.slot = slot;
                this.upgrade = upgrade;
                this.type = type;
                this.naturalStar = naturalStar;
                this.mainStat = mainStat;
                this.innateStat = innateStat;
                this.subStats = subs;
                this.isAccount = isAccount;
            } else {
                List<Substat> subs = subStats.subList(0, 3);
                this.grade = grade;
                this.set = set;
                this.slot = slot;
                this.upgrade = upgrade;
                this.type = type;
                this.naturalStar = naturalStar;
                this.mainStat = mainStat;
                this.subStats = subs;
                this.isAccount = isAccount;
            }
        } else if(grade.equals(Rune.Grade.RARE)) {
            if(innateStat != null) {
                List<Substat> subs = subStats.subList(0, 2);
                this.grade = grade;
                this.set = set;
                this.slot = slot;
                this.upgrade = upgrade;
                this.type = type;
                this.naturalStar = naturalStar;
                this.mainStat = mainStat;
                this.innateStat = innateStat;
                this.subStats = subs;
                this.isAccount = isAccount;
            } else {
                List<Substat> subs = subStats.subList(0, 2);
                this.grade = grade;
                this.set = set;
                this.slot = slot;
                this.upgrade = upgrade;
                this.type = type;
                this.naturalStar = naturalStar;
                this.mainStat = mainStat;
                this.subStats = subs;
                this.isAccount = isAccount;
            }
        }
        else if(grade.equals(Rune.Grade.MAGIC)) {
            if(innateStat != null) {
                List<Substat> subs = subStats.subList(0, 1);
                this.grade = grade;
                this.set = set;
                this.slot = slot;
                this.upgrade = upgrade;
                this.type = type;
                this.naturalStar = naturalStar;
                this.mainStat = mainStat;
                this.innateStat = innateStat;
                this.subStats = subs;
                this.isAccount = isAccount;
            } else {
                List<Substat> subs = subStats.subList(0, 1);
                this.grade = grade;
                this.set = set;
                this.slot = slot;
                this.upgrade = upgrade;
                this.type = type;
                this.naturalStar = naturalStar;
                this.mainStat = mainStat;
                this.subStats = subs;
                this.isAccount = isAccount;
            }
        }
        else if(grade.equals(Rune.Grade.COMMON)) {
            if(innateStat != null) {
                this.grade = grade;
                this.set = set;
                this.slot = slot;
                this.upgrade = upgrade;
                this.type = type;
                this.naturalStar = naturalStar;
                this.mainStat = mainStat;
                this.innateStat = innateStat;
                this.subStats = null;
                this.isAccount = isAccount;
            } else {
                this.grade = grade;
                this.set = set;
                this.slot = slot;
                this.upgrade = upgrade;
                this.type = type;
                this.naturalStar = naturalStar;
                this.mainStat = mainStat;
                this.subStats = null;
                this.isAccount = isAccount;
            }
        }
    }

    public Grade getGrade() {
        return this.grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public Set getSet() { return this.set; }

    public void setSet(Set set) { this.set = set; }

    public Integer getSlot() { return this.slot; }

    public void setSlot(Integer slot) { this.slot = slot; }

    public Integer getUpgrade() { return this.upgrade; }

    public void setUpgrade(Integer upgrade) { this.upgrade = upgrade; }

    public Type getType() { return this.type; }

    public void setType(Type type) { this.type = type; }

    public Integer getNaturalStar() { return this.naturalStar; }

    public void setNaturalStar(Integer naturalStar) { this.naturalStar = naturalStar; }

    public Substat getMainStat() {
        return this.mainStat;
    }

    public void setMainStat(Substat mainStat) {
        this.mainStat = mainStat;
    }

    public Substat getInnateStat() {
        return this.innateStat;
    }

    public void setInnateStat(Substat innateStat) {
        this.innateStat = innateStat;
    }

    public List<Substat> getSubStats() {
        return this.subStats;
    }

    public void setSubStats(List<Substat> subStats) {
        this.subStats = subStats;
    }

    public boolean isAccount() { return this.isAccount; }

    public void setIsAccount(boolean isAccount) { this.isAccount = isAccount; }

    public List<Double> getEfficiency(boolean flats, double weight) {
        HashMap<Substat.StatType, List<Integer>> mainMaxMap = new HashMap<Substat.StatType, List<Integer>>();
        HashMap<Substat.StatType, List<Integer>> subMaxMap = new HashMap<Substat.StatType, List<Integer>>();
        mainMaxMap.put(Substat.StatType.HP, Arrays.asList(804, 1092, 1380, 1704, 2088, 2448));
        mainMaxMap.put(Substat.StatType.HP_PERC, Arrays.asList(18, 20, 38, 43, 51, 63));
        mainMaxMap.put(Substat.StatType.ATK, Arrays.asList(54, 74, 93, 113, 135, 160));
        mainMaxMap.put(Substat.StatType.ATK_PERC, Arrays.asList(18, 20, 38, 43, 51, 63));
        mainMaxMap.put(Substat.StatType.DEF, Arrays.asList(54, 74, 93, 113, 135, 160));
        mainMaxMap.put(Substat.StatType.DEF_PERC, Arrays.asList(18, 20, 38, 43, 51, 63));
        mainMaxMap.put(Substat.StatType.SPD, Arrays.asList(18, 19, 25, 30, 39, 42));
        mainMaxMap.put(Substat.StatType.CRI_RATE, Arrays.asList(18, 20, 37, 41, 47, 58));
        mainMaxMap.put(Substat.StatType.CRI_DMG, Arrays.asList(20, 37, 43, 58, 65, 80));
        mainMaxMap.put(Substat.StatType.RESISTANCE, Arrays.asList(18, 20, 38, 44, 51, 64));
        mainMaxMap.put(Substat.StatType.ACCURACY, Arrays.asList(18, 20, 38, 44, 51, 64));
        subMaxMap.put(Substat.StatType.HP, Arrays.asList(300, 525, 825, 1125, 1500, 1875));
        subMaxMap.put(Substat.StatType.HP_PERC, Arrays.asList(10, 15, 25, 30, 35, 40));
        subMaxMap.put(Substat.StatType.ATK, Arrays.asList(20, 25, 40, 50, 75, 100));
        subMaxMap.put(Substat.StatType.ATK_PERC, Arrays.asList(10, 15, 25, 30, 35, 40));
        subMaxMap.put(Substat.StatType.DEF, Arrays.asList(20, 25, 40, 50, 75, 100));
        subMaxMap.put(Substat.StatType.DEF_PERC, Arrays.asList(10, 15, 25, 30, 35, 40));
        subMaxMap.put(Substat.StatType.SPD, Arrays.asList(5, 10, 15, 20, 25, 30));
        subMaxMap.put(Substat.StatType.CRI_RATE, Arrays.asList(5, 10, 15, 20, 25, 30));
        subMaxMap.put(Substat.StatType.CRI_DMG, Arrays.asList(10, 15, 20, 25, 25, 35));
        subMaxMap.put(Substat.StatType.RESISTANCE, Arrays.asList(10, 15, 20, 25, 35, 40));
        subMaxMap.put(Substat.StatType.ACCURACY, Arrays.asList(10, 15, 20, 25, 35, 40));

        double ratio = 0.0;

        if(flats) {
            if(slot == 2 || slot == 4 || slot == 6){
                if(mainStat.getType() == Substat.StatType.HP || mainStat.getType() == Substat.StatType.DEF || mainStat.getType() == Substat.StatType.ATK){
                    ratio += (double) (mainMaxMap.get(this.mainStat.getType()).get(this.naturalStar - 1) / mainMaxMap.get(this.mainStat.getType()).get(5)) * weight;
                } else {
                    ratio += (double) mainMaxMap.get(this.mainStat.getType()).get(this.naturalStar - 1) / mainMaxMap.get(this.mainStat.getType()).get(5);
                }
            }else {
                ratio += (double) mainMaxMap.get(this.mainStat.getType()).get(this.naturalStar - 1) / mainMaxMap.get(this.mainStat.getType()).get(5);
            }
        } else {
            ratio += (double) mainMaxMap.get(this.mainStat.getType()).get(this.naturalStar - 1) / mainMaxMap.get(this.mainStat.getType()).get(5);
        }



        if(this.innateStat != null) {
            ratio += (double) this.innateStat.getValue() / subMaxMap.get(this.innateStat.getType()).get(5);
        }

        for (Substat sub : this.subStats) {
            ratio += (double) sub.getValue() / subMaxMap.get(sub.getType()).get(5);
        }

        double efficiency = (ratio / 2.8) * 100;

        efficiency = efficiency + (Math.max(Math.ceil(12 - this.upgrade) / 3, 0) * 0.2 / 2.8) * 100;

        List<Double> values = Arrays.asList((ratio / 2.8) * 100, efficiency);

        return values;
    }

    @Override
    public String toString() {
        if(this.innateStat != null) {
            return "+" + this.upgrade + " " + this.set.toString().substring(0, 1).toUpperCase() + this.set.toString().substring(1).toLowerCase() + " Rune (" + this.slot + ")\n" +
                    this.mainStat.toString() + "\n" +
                    this.innateStat.toString() + "\n" +
                    this.subStats.toString();
        }else {
            return "+" + this.upgrade + " " + this.set.toString().substring(0, 1).toUpperCase() + this.set.toString().substring(1).toLowerCase() + " Rune\n" +
                    this.mainStat.toString() + "\n" +
                    this.subStats.toString();
        }
    }

    public List<SuggestMonster> suggestMonster(List<Monster> monsters, boolean flats, double flatWeight, boolean sets, double setWeight) {
        List<SuggestMonster> suggest = new ArrayList<>();

        for(Monster monster : monsters) {
            int score = 0;
            int found = 0;

            if(monster.getScore(this.mainStat.getType()) > -1) {
                if(flats) {
                    if(this.slot == 2 || this.slot == 4 || this.slot == 6) {
                        if(this.mainStat.getType() == Substat.StatType.HP || this.mainStat.getType() == Substat.StatType.ATK || this.mainStat.getType() == Substat.StatType.DEF) {
                            score += monster.getScore(this.mainStat.getType()) / flatWeight;
                        }
                    }else {
                        score += monster.getScore(this.mainStat.getType());
                    }
                } else {
                    score += monster.getScore(this.mainStat.getType());
                }

                found++;
            }

            if(this.innateStat != null && monster.getScore(this.innateStat.getType()) > -1) {
                score += monster.getScore(this.innateStat.getType());
                found++;
            }

            for(Substat sub : this.subStats) {
                if(monster.getScore(sub.getType()) > -1) {
                    if(flats) {
                        if(sub.getType() == Substat.StatType.HP || sub.getType() == Substat.StatType.ATK || sub.getType() == Substat.StatType.DEF) {
                            score += (int) Math.floor(monster.getScore(sub.getType()) / flatWeight);
                        } else {
                            score += monster.getScore(sub.getType());
                        }
                    }else {
                        score += monster.getScore(sub.getType());
                    }

                    found++;
                }
            }

            if(sets) {
                score += score * setWeight;
            }

            double total = (double) score / ((double) found * 5) * 100;

            suggest.add(new SuggestMonster(monster.name, total));
        }

        Collections.sort(suggest, new Comparator<SuggestMonster>() {
            @Override
            public int compare(SuggestMonster suggestMonster, SuggestMonster t1) {
                return Double.compare(t1.score, suggestMonster.score);
            }
        });

        return suggest;
    }

    public static Grade IntToGrade(int grade) {
        if(grade == 1 || grade == 11) {
            return Grade.COMMON;
        }else if (grade == 2 || grade == 12){
            return Grade.MAGIC;
        }else if (grade == 3 || grade == 13){
            return Grade.RARE;
        }else if (grade == 4 || grade == 14){
            return Grade.HERO;
        }else if (grade == 5 || grade == 15){
            return Grade.LEGEND;
        }else{
            return null;
        }
    }

    public static Set IntToSet(int set) {
        switch(set) {
            case 1:
                return Set.ENERGY;
            case 2:
                return Set.GUARD;
            case 3:
                return Set.SWIFT;
            case 4:
                return Set.BLADE;
            case 5:
                return Set.RAGE;
            case 6:
                return Set.FOCUS;
            case 7:
                return Set.ENDURE;
            case 8:
                return Set.FATAL;
            case 10:
                return Set.DESPAIR;
            case 11:
                return Set.VAMPIRE;
            case 13:
                return Set.VIOLENT;
            case 14:
                return Set.NEMESIS;
            case 15:
                return Set.WILL;
            case 16:
                return Set.SHIELD;
            case 17:
                return Set.REVENGE;
            case 18:
                return Set.DESTROY;
            case 19:
                return Set.FIGHT;
            case 20:
                return Set.DETERMINATION;
            case 21:
                return Set.ENHANCE;
            case 22:
                return Set.ACCURACY;
            case 23:
                return Set.TOLERANCE;
            default:
                return null;
        }
    }
}
