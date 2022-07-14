package com.kgandroid.roomwithcoroutine.view

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.kgandroid.roomwithcoroutine.R
import com.kgandroid.roomwithcoroutine.adapters.WordListAdapter
import com.kgandroid.roomwithcoroutine.data.AppDatabase
import com.kgandroid.roomwithcoroutine.data.Word
import com.kgandroid.roomwithcoroutine.data.WordRepository
import com.kgandroid.roomwithcoroutine.databinding.FragmentWordListBinding
import com.kgandroid.roomwithcoroutine.viewmodel.WordViewModel
import com.kgandroid.roomwithcoroutine.viewmodel.WordViewModelFactory


class WordListFragment  : Fragment()  , WordClickListener , NavigateFromWordList {
    private lateinit var dataBinding : FragmentWordListBinding
    private val wordListAdapter =
        WordListAdapter(arrayListOf())
    //private val viewModel: WordViewModel by viewModels()
    private lateinit var viewModel: WordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true);
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(inflater , R.layout.fragment_word_list , container ,false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dao = AppDatabase.getInstance(requireContext()).wordDao()
        val repository = WordRepository(dao)
        val factory = WordViewModelFactory(repository)
        viewModel = ViewModelProvider(this,factory).get(WordViewModel::class.java)

        dataBinding.listener = this
        viewModel.allWords
        dataBinding.recyclerviewWord.apply {
            dataBinding.recyclerviewWord.layoutManager = LinearLayoutManager(context)
            dataBinding.recyclerviewWord.adapter = wordListAdapter
        }

        viewModel.allWords.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                val size = it.size
                for (i in 0..size-1) {
                        Log.i("ListItems", "$i --> " + it.get(i).word+" -->" + it.get(i).meaning)
                }
                wordListAdapter.updateWordList(it , viewModel , this)
            }
        })

        viewModel.deleteRowIdLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (it >= 1) {
                    Toast.makeText(context, "Word Deleted Successfully", Toast.LENGTH_LONG).show()
                }
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        val searchItem = menu.findItem(R.id.app_bar_search)
        val searchView: SearchView = searchItem.actionView as SearchView
        // listening to search query text change
        searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // filter recycler view when query submitted
                wordListAdapter.getFilter().filter(query)
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                // filter recycler view when text is changed
                wordListAdapter.getFilter().filter(query)
                return false
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.app_bar_search).setVisible(true)
        super.onPrepareOptionsMenu(menu)
    }

    override fun onclickWord(view :View, word: Word) {
        Toast.makeText(context, "Word Selected " + word.word, Toast.LENGTH_LONG).show()
        val action  = WordListFragmentDirections.actionWordListFragmentToWordDetailFragment(word)
        Navigation.findNavController(view).navigate(action)
    }

    override fun navigateToAddWord(view: View){
        val action = WordListFragmentDirections.actionWordListFragmentToAddWordFragment()
        Navigation.findNavController(view).navigate(action)
    }

}

interface NavigateFromWordList{
    fun navigateToAddWord(view:View)
}