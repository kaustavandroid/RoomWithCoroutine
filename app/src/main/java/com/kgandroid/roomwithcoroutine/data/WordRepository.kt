package com.kgandroid.roomwithcoroutine.data

import androidx.lifecycle.LiveData

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class WordRepository(private val wordDao: WordDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allWords: LiveData<List<Word>> = wordDao.getAlphabetizedWords()

    suspend fun insert(word: Word) : Long {
        val insertId =  wordDao.insert(word)
        return insertId
    }

    suspend fun delete(word: Word) : Int {
        val deleteId =  wordDao.deleteWord(word)
        return deleteId
    }

    suspend fun update(word: Word) : Int {
        val updateId =  wordDao.updateWord(word)
        return updateId
    }

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: WordRepository? = null

        fun getInstance(plantDao: WordDao) =
            instance ?: synchronized(this) {
                instance ?: WordRepository(plantDao).also { instance = it }
            }
    }
}