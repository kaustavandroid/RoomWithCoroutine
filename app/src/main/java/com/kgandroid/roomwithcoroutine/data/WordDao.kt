/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kgandroid.roomwithcoroutine.data

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * The Data Access Object for the Plant class.
 */
@Dao
interface WordDao {
    @Query("SELECT * FROM word_table ORDER BY word ASC")
    fun getAlphabetizedWords(): LiveData<List<Word>>//Always run on background thread that have return type live data

    @Query("SELECT * FROM word_table WHERE wordId = :wordId")
    fun getWordByIds(wordId: String): LiveData<Word>//Always run on background thread that have return type live data

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(plants: List<Word>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(word: Word) :  Long

    @Delete
    suspend fun deleteWord(word: Word) : Int

    @Update
    suspend fun updateWord(word: Word) : Int

    @Query("DELETE FROM word_table")
    suspend fun deleteAll()
}

