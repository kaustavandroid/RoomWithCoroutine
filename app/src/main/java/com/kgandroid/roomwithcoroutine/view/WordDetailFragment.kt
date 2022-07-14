package com.kgandroid.roomwithcoroutine.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.kgandroid.roomwithcoroutine.R
import com.kgandroid.roomwithcoroutine.data.AppDatabase
import com.kgandroid.roomwithcoroutine.data.Word
import com.kgandroid.roomwithcoroutine.data.WordRepository
import com.kgandroid.roomwithcoroutine.databinding.FragmentWordDetailBinding
import com.kgandroid.roomwithcoroutine.databinding.FragmentWordListBinding
import com.kgandroid.roomwithcoroutine.viewmodel.WordViewModel
import com.kgandroid.roomwithcoroutine.viewmodel.WordViewModelFactory
import kotlinx.android.synthetic.main.fragment_add_word.*
import kotlinx.android.synthetic.main.fragment_word_detail.*

class WordDetailFragment  : Fragment() {
    private lateinit var dataBinding : FragmentWordDetailBinding
    //private val viewModel: WordViewModel by viewModels()
    var word : Word? = null
    private lateinit var viewModel: WordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(inflater , R.layout.fragment_word_detail , container ,false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dao = AppDatabase.getInstance(requireContext()).wordDao()
        val repository = WordRepository(dao)
        val factory = WordViewModelFactory(repository)
        viewModel = ViewModelProvider(this,factory).get(WordViewModel::class.java)

        arguments?.let {
            word = WordDetailFragmentArgs.fromBundle(it).selectedWord
            Log.d("Update id-->" , word?.wordId.toString())
            Log.d("Update word-->" , word?.word)
            Log.d("Update meaning-->" , word?.meaning)
            Log.d("Update date-->" , word?.insertDate)

        }
        //android:text="@{word.meaning ,  default= `Kaustav Ghosh`}"

        dataBinding.word = word
        dataBinding.viewmodel = viewModel
        dataBinding.clickListener = this
        dataBinding.viewmodel = viewModel


        viewModel.textNumberWordMeaningMutableLiveData.observe(viewLifecycleOwner, Observer { charNumberWordMeaning ->
            charNumberWordMeaning?.let {
                Log.d("Total Characters" , charNumberWordMeaning.toString())
                var num = 50-charNumberWordMeaning
                tvTrackerEdit.text = num.toString() + " of 50 left"

            }
        })

        viewModel.updateRowIdLiveData.observe(viewLifecycleOwner, Observer { insertionId ->
            insertionId?.let {
                if (it >= 1) {
                    Toast.makeText(context, "Word Updated Successfully", Toast.LENGTH_LONG).show()
                    val action  = WordDetailFragmentDirections.actionWordDetailFragmentToWordListFragment()
                    Navigation.findNavController(view).navigate(action)
                }
            }
        })

    }

    fun navigateToWordList(view: View){
        val action  = WordDetailFragmentDirections.actionWordDetailFragmentToWordListFragment()
        Navigation.findNavController(view).navigate(action)
    }
}