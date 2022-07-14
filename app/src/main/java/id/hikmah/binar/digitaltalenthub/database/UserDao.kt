package id.hikmah.binar.digitaltalenthub.database

import androidx.room.*

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User): Long

    @Query("SELECT * FROM User WHERE username = :username")
    fun checkUsername(username: String): List<User>

    @Query("SELECT * FROM User WHERE username = :username AND password = :password")
    fun checkLogin(username: String, password: String): List<User>

    @Query("SELECT * FROM User WHERE username = :username")
    fun getId(username: String): User


}