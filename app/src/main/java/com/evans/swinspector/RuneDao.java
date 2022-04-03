package com.evans.swinspector;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;

@Dao
public interface RuneDao {
    @Query("SELECT * FROM rune")
    List<Rune> getAll();

    @Query("SELECT * FROM rune WHERE isAccount")
    List<Rune> getAllAccountRune();

    @Query("SELECT * FROM rune WHERE NOT isAccount")
    List<Rune> getAllScanned();

    @Query("SELECT `set`, COUNT(*) as count FROM rune GROUP BY `set` ORDER BY count desc")
    List<SetCount> countAllSets();

    @Query("DELETE FROM rune WHERE isAccount")
    void deleteAccountRunes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Rune... runes);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAsync(List<Rune> runes);

    @Delete
    void delete(Rune rune);
}
