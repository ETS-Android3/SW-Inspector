package com.evans.swinspector;

import androidx.annotation.NonNull;
import androidx.room.*;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Monster.class, Rune.class, Account.class}, version = 4, exportSchema = false)
@TypeConverters({Converter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract MonsterDao monsterDao();
    public abstract RuneDao runeDao();
    public abstract AccountDao accountDao();

    static final Migration MIGRATION_1_2 = new Migration(1, 2){

        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `Rune` (`mid` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `grade` TEXT, `set` TEXT, `slot` INTEGER, `upgrade` INTEGER, `type` TEXT, `natural_star` INTEGER, `mainStat` TEXT, `innateStat` TEXT, `sub_stats` TEXT)");
        }
    };

    static final Migration MIGRATION_2_3 = new Migration(2, 3){

        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("DROP TABLE IF EXISTS monster");
            database.execSQL("DROP TABLE IF EXISTS rune");
            database.execSQL("DROP TABLE  IF EXISTS account");
            database.execSQL("CREATE TABLE IF NOT EXISTS `Monster` (`mid` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `unit_master_id` INTEGER NOT NULL, `name` TEXT, `atk` INTEGER NOT NULL, `def` INTEGER NOT NULL, `hp` INTEGER NOT NULL, `spd` INTEGER NOT NULL, `crit_rate` INTEGER NOT NULL, `crit_damage` INTEGER NOT NULL, `resistance` INTEGER NOT NULL, `accuracy` INTEGER NOT NULL)");
            database.execSQL("CREATE TABLE IF NOT EXISTS `Account` (`mid` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT, `monsters` TEXT)");
            database.execSQL("CREATE TABLE IF NOT EXISTS `Rune` (`mid` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `grade` TEXT, `set` TEXT, `slot` INTEGER, `upgrade` INTEGER, `type` TEXT, `natural_star` INTEGER, `mainStat` TEXT, `innateStat` TEXT, `sub_stats` TEXT, `isAccount` INTEGER NOT NULL)");
        }
    };

    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("DELETE FROM monster");
            database.execSQL("DROP TABLE IF EXISTS monster");
            database.execSQL("CREATE TABLE IF NOT EXISTS `Monster` (`mid` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `unit_master_id` INTEGER NOT NULL, `name` TEXT, `atk` INTEGER NOT NULL, `def` INTEGER NOT NULL, `hp` INTEGER NOT NULL, `spd` INTEGER NOT NULL, `crit_rate` INTEGER NOT NULL, `crit_damage` INTEGER NOT NULL, `resistance` INTEGER NOT NULL, `accuracy` INTEGER NOT NULL, `slot_two_pref` TEXT, `slot_four_pref` TEXT, `slot_six_pref` TEXT, `set_pref` TEXT, `second_awaken` INTEGER)");
        }
    };
}
