package com.evans.swinspector;

import androidx.room.ColumnInfo;

public class SetCount {
    public Rune.Set set;
    @ColumnInfo(name = "count")
    public Integer count;
    SetCount(Rune.Set set, Integer count){
        this.set = set;
        this.count = count;
    }

    @Override
    public String toString(){
        return this.set.toString() + " " + this.count;
    }
}