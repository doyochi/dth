package id.hikmah.binar.digitaltalenthub.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class JobEntity(
    @PrimaryKey(autoGenerate = true) var id: Int?,
    @ColumnInfo(name = "id_user") val idUser: Int,
    @ColumnInfo(name = "job_title") val jobTitle: String,
    @ColumnInfo(name = "desc") val desc: String,
    @ColumnInfo(name = "salary") val salary: Int
)
