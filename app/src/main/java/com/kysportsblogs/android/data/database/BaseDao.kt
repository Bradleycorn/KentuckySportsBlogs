package com.kysportsblogs.android.data.database

import androidx.room.*

@Dao
abstract class BaseDao<E> {
    /**
     * Insert an object in the database.
     *
     * @param obj the object to be inserted.
     * @return The SQLite row id
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insert(obj: E): Long

    /**
     * Insert an array of objects in the database.
     *
     * @param obj the objects to be inserted.
     * @return The SQLite row ids
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insert(obj: List<E>): List<Long>

    /**
     * Update an object from the database.
     *
     * @param obj the object to be updated
     */
    @Update
    abstract suspend fun update(obj: E)

    /**
     * Update an array of objects from the database.
     *
     * @param obj the object to be updated
     */
    @Update
    abstract suspend fun update(obj: List<E>)


    /**
     * Delete an object from the database
     *
     * @param obj the object to be deleted
     */
    @Delete
    abstract suspend fun delete(obj: E)

    /**
     * Perform an "upsert" for an object on the database.
     *
     * Attempt to insert first, and if that fails, do an update.
     *
     * @param obj the object to be inserted/updated
     */
    @Transaction
    open suspend fun upsert(obj: E) {
        val id = insert(obj)
        if (id == -1L) {
            update(obj)
        }
    }


    /**
     * Perform an "upsert" on a list of objects with the database.
     *
     * Attempt to insert first, and if that fails, do an update.
     *
     * @param objList the list of objects to be inserted/updated
     */
    @Transaction
    open suspend fun upsert(objList: List<E>) {
        val insertResults = insert(objList)
        val updateList: MutableList<E> = mutableListOf()

        insertResults.forEachIndexed { index, result ->
            if (result == -1L) {
                updateList.add(objList[index])
            }
        }

        if (!updateList.isEmpty()) {
            update(updateList)
        }
    }

}