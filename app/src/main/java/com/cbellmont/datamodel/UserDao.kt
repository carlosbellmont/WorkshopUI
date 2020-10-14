package com.cbellmont.neoland.datamodel.user

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cbellmont.neoland.datamodel.UserWithBootcamp

@Dao
interface UserDao {
    @Query("SELECT * FROM User")
    fun getAll(): List<User>

    @Query("SELECT * FROM User")
    fun getAllLive(): LiveData<List<User>>

    @Query("SELECT * FROM User WHERE userId IN (:UsersId)")
    fun loadAllByIds(UsersId: IntArray): List<User>

    @Query("SELECT * FROM User WHERE fkBootcampId  = :bootcampId")
    fun getByBootcampId(bootcampId : Int): LiveData<List<User>>

    @Query("SELECT * FROM User WHERE name LIKE (:nombreUser)")
    fun loadAllByTitle(nombreUser: String): List<User>

    @Insert
    fun insert(User: User)

    @Update
    fun update(User: User)

    @Insert
    fun insertAll(Users: List<User>)

    @Delete
    fun delete(User: User)

    @Query("DELETE FROM User")
    fun deleteAll()

    @Query("SELECT * FROM User INNER JOIN Bootcamp ON User.fkBootcampId = Bootcamp.bootcampId WHERE Bootcamp.fkCampusId = :campusId")
    fun getByCampusId(campusId : Int): LiveData<List<UserWithBootcamp>>

}
