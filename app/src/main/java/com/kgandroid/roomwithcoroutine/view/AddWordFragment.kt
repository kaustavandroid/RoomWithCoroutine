package com.kgandroid.roomwithcoroutine.view

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
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
import com.kgandroid.roomwithcoroutine.data.WordRepository
import com.kgandroid.roomwithcoroutine.databinding.FragmentAddWordBinding
import com.kgandroid.roomwithcoroutine.viewmodel.WordViewModel
import com.kgandroid.roomwithcoroutine.viewmodel.WordViewModelFactory
import kotlinx.android.synthetic.main.fragment_add_word.*


class AddWordFragment  : Fragment() {
    private lateinit var dataBinding : FragmentAddWordBinding
    //private val viewModel: WordViewModel by viewModels()
    private lateinit var viewModel: WordViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(inflater , R.layout.fragment_add_word , container ,false)
        //initialiseWord()
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //viewModel by viewModels()
        //dataBinding.listener = this
        val dao = AppDatabase.getInstance(requireContext()).wordDao()
        val repository = WordRepository(dao)
        val factory = WordViewModelFactory(repository)
        viewModel = ViewModelProvider(this,factory).get(WordViewModel::class.java)

        dataBinding.viewmodel = viewModel
        dataBinding.handler = this
        viewModel.insertRowIdLiveData.observe(viewLifecycleOwner, Observer { insertionId ->
            insertionId?.let {
                if (it !== -1L) {
                    Toast.makeText(context, "New Word Inserted Successfully", Toast.LENGTH_LONG).show()
                    val action  = AddWordFragmentDirections.actionAddWordFragmentToWordListFragment()
                    Navigation.findNavController(view).navigate(action)
                }
            }
        })

        viewModel.textNumberWordMeaningMutableLiveData.observe(viewLifecycleOwner, Observer { charNumberWordMeaning ->
            charNumberWordMeaning?.let {
                Log.d("Total Characters" , charNumberWordMeaning.toString())
                var num = 50-charNumberWordMeaning
                tvTracker.text = num.toString() + " of 50 left"

            }
        })
    }
    fun passwordValidator (editable: Editable)  {
        if (TextUtils.isEmpty(editable.toString())){
            dataBinding.wordEdittext.setError("Please enter a word")
        }
    }

}