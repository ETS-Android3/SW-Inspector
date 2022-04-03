package com.evans.swinspector;

public class Substat {

    public enum StatType {
        HP,
        HP_PERC,
        DEF,
        DEF_PERC,
        ATK,
        ATK_PERC,
        SPD,
        ACCURACY,
        CRI_RATE,
        CRI_DMG,
        RESISTANCE
    }

    private StatType type;
    private Integer value;

    public Substat(StatType type, Integer value) {
        this.type = type;
        this.value = value;
    }

    public Substat(int type, Integer value) {
        switch(type){
            default:
                this.type = null;
                break;
            case 1:
                this.type = StatType.HP;
                break;
            case 2:
                this.type = StatType.HP_PERC;
                break;
            case 3:
                this.type = StatType.ATK;
                break;
            case 4:
                this.type = StatType.ATK_PERC;
                break;
            case 5:
                this.type = StatType.DEF;
                break;
            case 6:
                this.type = StatType.DEF_PERC;
                break;
            case 7:
                this.type = StatType.SPD;
                break;
            case 8:
                this.type = StatType.CRI_RATE;
                break;
            case 9:
                this.type = StatType.CRI_DMG;
                break;
            case 10:
                this.type = StatType.RESISTANCE;
                break;
            case 11:
                this.type = StatType.ACCURACY;
                break;
            case 12:
                this.type = StatType.HP_PERC;
                break;
        }
        this.value = value;
    }

    public StatType getType() {
        return this.type;
    }

    public void setType(StatType type) {
        this.type = type;
    }

    public Integer getValue() {
        return this.value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.type + " " + this.value;
    }
}
