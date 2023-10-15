package com.example.pbsm3.roomModel

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pbsm3.roomModel.entities.AccountRoom
import com.example.pbsm3.roomModel.entities.CategoryRoom
import com.example.pbsm3.roomModel.entities.TransactionRoom

@Database(entities = [CategoryRoom::class, AccountRoom::class, TransactionRoom::class], version =
1, exportSchema = false)
abstract class PBSM3Database: RoomDatabase(){
    abstract fun categoryDao(): CategoryRoomDao
    abstract fun accountDao(): AccountDao
    abstract fun transactionDao(): TransactionDao

    companion object{
        @Volatile
        private var Instance: PBSM3Database? = null
        fun getDatabase(context:Context): PBSM3Database {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, PBSM3Database::class.java, "pbsm3database")
                    .fallbackToDestructiveMigration() //see footnote [1] at bottom.
                    .build()
                    .also{ Instance = it }
            }
        }
    }
}



/**[1]: Normally, you would provide a migration object with a
 *  migration strategy for when the schema changes. A
 *  migration object is an object that defines how you take
 *  all rows with the old schema and convert them to rows
 *  in the new schema, so that no data is lost. Migration
 *  is beyond the scope of this codelab, but the term refers to
 *  when the schema is changed and you need to move your date
 *  without losing the data. Since this is a sample app, a
 *  simple alternative is to destroy and rebuild the database,
 *  which means that the inventory data is lost. For example,
 *  if you change something in the entity class, like adding a
 *  new parameter, you can allow the app to delete and
 *  re-initialize the database.*/