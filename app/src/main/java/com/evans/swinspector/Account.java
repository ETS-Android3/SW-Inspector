package com.evans.swinspector;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity
public class Account {
    @PrimaryKey(autoGenerate = true)
    public int mid;
    @ColumnInfo(name = "name")
    public String name;
    @ColumnInfo(name = "monsters")
    public List<AccountMonster> monsters;

    public Account(String name, List<AccountMonster> monsters) {
        this.name = name;
        this.monsters = monsters;
    }
}
