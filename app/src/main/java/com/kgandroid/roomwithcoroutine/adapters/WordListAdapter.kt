package com.kgandroid.roomwithcoroutine.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.kgandroid.roomwithcoroutine.R
import com.kgandroid.roomwithcoroutine.data.Word
import com.kgandroid.roomwithcoroutine.databinding.WordlistItemBinding
import com.kgandroid.roomwithcoroutine.view.WordClickListener
import com.kgandroid.roomwithcoroutine.viewmodel.WordViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class WordListAdapter(val wordList: ArrayList<Word>) : RecyclerView.Adapter<WordListAdapter.ViewHolder>() ,Filterable  {
    private var viewModel: WordViewModel? = null
    private var itemListener: WordClickListener? = null
    var wordListFilter: ArrayList<Word> = wordList


    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        //val v = LayoutInflater.from(parent.context).inflate(R.layout.item_animal, parent, false)
        val inflater = LayoutInflater.from(parent.context)
        val v = DataBindingUtil.inflate<WordlistItemBinding>(
            inflater,
            R.layout.wordlist_item,
            parent,
            false
        )
        return ViewHolder(v)
    }

    fun updateWordList(newStudentList: List<Word>, viewModel: WordViewModel, itemListener: WordClickListener) {
        wordList.clear()
        wordList.addAll(newStudentList)
        wordListFilter.clear()
        wordListFilter.addAll(newStudentList)
        this.viewModel = viewModel
        this.itemListener = itemListener
        notifyDataSetChanged()
    }


    //this method i binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(wordListFilter[position])
    }

    /*companion object {
        private val DIFF_CALLBACK = object :
            DiffUtil.ItemCallback<Word>() {
            // Concert details may have changed if reloaded from the database,
            // but ID is fixed.
            override fun areItemsTheSame(oldStudent: Word,
                                         newStudent: Word) = oldStudent.wordId == newStudent.wordId

            override fun areContentsTheSame(oldStudent: Word,
                                            newStudent: Word) = oldStudent == newStudent
        }
    }*/


    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return wordListFilter.size
    }


    //the class is hodling the list view
    inner class ViewHolder( val binding: WordlistItemBinding) : RecyclerView.ViewHolder(binding.root),
        WordClickListener {
        fun bindItems(word: Word) {
            binding.listener = this
            binding.word = word
            binding.viewmodel = viewModel
        }
        override fun onclickWord(v: View, word: Word) {
            Log.d("Word Clicked" , word.word)
            itemListener?.onclickWord(v, word)
        }

    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    wordListFilter = wordList
                } else {
                    val filteredList: MutableList<Word> = ArrayList()
                    for (row in wordList) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        val b =row.word?.toLowerCase()?.contains(charString.toLowerCase())
                        if (b!!) {
                            filteredList.add(row)
                        }
                    }
                    wordListFilter = filteredList as ArrayList<Word>
                }
                val filterResults = FilterResults()
                filterResults.values = wordListFilter
                return filterResults
            }

            override fun publishResults(
                charSequence: CharSequence,
                filterResults: FilterResults
            ) {
                wordListFilter = filterResults.values as ArrayList<Word>
                notifyDataSetChanged()
            }
        }
    }


}