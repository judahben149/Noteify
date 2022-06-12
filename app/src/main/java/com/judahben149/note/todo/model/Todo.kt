package com.judahben149.note.todo.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "todo_table")
data class Todo(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 1,

    @ColumnInfo(name = "todo_title")
    var todoBody: String,

    @ColumnInfo(name = "todo_date")
    var todoDate: Long,

    @ColumnInfo(name = "todo_time")
    var todoTime: Long,

    @ColumnInfo(name = "todo_priority")
    var isTodoPriority: Boolean = false,

    @ColumnInfo(name = "todo_completed")
    var isTodoCompleted: Boolean = false

)
