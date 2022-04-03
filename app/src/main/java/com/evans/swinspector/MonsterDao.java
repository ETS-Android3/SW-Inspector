package com.evans.swinspector;

import androidx.room.*;
import java.util.List;

@Dao
public interface MonsterDao {
    @Query("SELECT * FROM monster ORDER BY `unit_master_id`")
    List<Monster> getAll();

    @Query("DELETE FROM monster")
    void deleteAll();

    @Query("UPDATE monster SET atk = :atk, def = :def, hp = :hp, spd = :spd, crit_rate = :crit_rate, crit_damage = :crit_damage, resistance = :resistance, accuracy = :accuracy, slot_two_pref = :slot_two_pref, slot_four_pref = :slot_four_pref, slot_six_pref = :slot_six_pref, set_pref = :set_pref WHERE unit_master_id = :unit_master_id")
    void update(int unit_master_id, int atk, int def, int hp, int spd, int crit_rate, int crit_damage, int resistance, int accuracy, List<Substat.StatType> slot_two_pref, List<Substat.StatType> slot_four_pref, List<Substat.StatType> slot_six_pref, List<Rune.Set> set_pref);

    @Insert
    void insertAll(Monster... monsters);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAsync(List<Monster> monsters);

    @Delete
    void delete(Monster monster);
}
