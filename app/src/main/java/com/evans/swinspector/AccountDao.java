package com.evans.swinspector;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AccountDao {
    @Query("SELECT * FROM account")
    List<Account> getAll();

    @Insert
    void insertAll(Account... accounts);

    @Delete
    void delete(Account account);
}
