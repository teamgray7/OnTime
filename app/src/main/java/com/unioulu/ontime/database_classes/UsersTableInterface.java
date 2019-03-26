package com.unioulu.ontime.database_classes;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface UsersTableInterface {

    // Returns 3 if abort !
    @Insert(onConflict = OnConflictStrategy.ABORT)
    void createUser(UsersTable usersTable);

    @Query("SELECT * FROM UsersTable WHERE username = :username and password = :password")
    UsersTable fetchUserByUsernameAndPassword(String username, String password);

    // Used with forgotten password and username
    @Query("SELECT * FROM UsersTable WHERE email = :email")
    UsersTable fetchUserByEmail(String email);

    @Query("SELECT * FROM UsersTable WHERE username = :username AND email = :email")
    UsersTable fetchUserByEmailAndEmail(String username, String email);

    @Query("SELECT active FROM UsersTable WHERE username = :username")
    Boolean isUserActiveFromDB(String username);

    @Query("SELECT COUNT (*) FROM UsersTable")
    int usersCount();

    @Update(onConflict = OnConflictStrategy.ABORT)
    int updateUser(UsersTable usersTable);

    @Delete
    void deleteUser(UsersTable usersTable);

    // Only used for debugging
    @Query("SELECT * FROM UsersTable")
    List<UsersTable> fetchAllUsers();

    @Query("DELETE FROM UsersTable")
    void deleteUserTable();


}
