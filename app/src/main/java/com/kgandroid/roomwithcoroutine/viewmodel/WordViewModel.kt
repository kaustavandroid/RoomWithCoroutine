package com.kgandroid.roomwithcoroutine.viewmodel

import android.app.Application
import android.util.Log
import androidx.databinding.Bindable
import androidx.databinding.BindingAdapter
import androidx.lifecycle.*
import com.kgandroid.roomwithcoroutine.adapters.WordListAdapter
import com.kgandroid.roomwithcoroutine.data.AppDatabase
import com.kgandroid.roomwithcoroutine.data.Word
import com.kgandroid.roomwithcoroutine.data.WordRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class WordViewModel(private val repository: WordRepository) : ViewModel() {

    //private var repository: WordRepository
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allWords: LiveData<List<Word>>
    var insertRowIdMutableLiveData = MutableLiveData<Long?>()
    val insertRowIdLiveData :LiveData<Long?>
        get() = insertRowIdMutableLiveData

    var deleteRowIdMutableLiveData = MutableLiveData<Int?>()
    val deleteRowIdLiveData :LiveData<Int?>
        get() = deleteRowIdMutableLiveData


    var updateRowIdMutableLiveData = MutableLiveData<Int?>()
    val updateRowIdLiveData :LiveData<Int?>
        get() = updateRowIdMutableLiveData


    var textNumberWordMeaningMutableLiveData = MutableLiveData<Int?>()
    val textNumberWordMeaningLiveData :LiveData<Int?>
        get() = textNumberWordMeaningMutableLiveData



    val inputWord = MutableLiveData<String>()

    val inputMeaning = MutableLiveData<String>()

    init {
       //repository = WordRepository.getInstance(AppDatabase.getInstance(application).wordDao())
        allWords = repository.allWords
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insertWord() = viewModelScope.launch(Dispatchers.IO) {
        val insertDateString: String =dateFormat.format(Calendar.getInstance().time)
        var newWord = Word(word =inputWord.value,
            meaning= inputMeaning.value , insertDate = insertDateString)
        val insertRowId = repository.insert(newWord)
        insertRowIdMutableLiveData.postValue(insertRowId)
        Log.d("InsertId" , insertRowId.toString())
    }

    fun deleteWord(word: Word) = viewModelScope.launch(Dispatchers.IO) {
        val deleteRowId = repository.delete(word)
        deleteRowIdMutableLiveData.postValue(deleteRowId)
        Log.d("DeleteId" , deleteRowId.toString())
    }


    fun updateWord(word: Word , meaning: String) = viewModelScope.launch(Dispatchers.IO) {
        val insertDateString: String =dateFormat.format(Calendar.getInstance().time)
        var updatedWord = word
        updatedWord.meaning = meaning
        updatedWord.insertDate = insertDateString
        val updateRowId = repository.update(updatedWord)
        updateRowIdMutableLiveData.postValue(updateRowId)
        Log.d("DeleteId" , updateRowId.toString())
    }

    fun onMeaningTextChanged(s: CharSequence,start: Int,before : Int,
                              count :Int){
        textNumberWordMeaningMutableLiveData.postValue(s.length)
    }

    companion object {
        val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.US)
    }
}
