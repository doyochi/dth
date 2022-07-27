package id.hikmah.binar.digitaltalenthub.database

import androidx.room.*
import kotlinx.coroutines.Job

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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addJob(job: JobEntity): Long

    @Query("SELECT * FROM JobEntity WHERE id_user = :userId")
    fun getAllJob(userId: Int): List<JobEntity>

    @Update
    fun updateJob(job: JobEntity): Int

    @Delete
    fun deleteNote(job: JobEntity): Int
}