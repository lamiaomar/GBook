package com.example.gbook.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.paging.PagingData
import com.example.gbook.BookViewModelFactory
import com.example.gbook.BookViewmodel
import com.example.gbook.ServiceLocator
import com.example.gbook.data.BooksData
import com.example.gbook.data.BooksRemoteDataSource
import com.example.gbook.data.BooksRepository
import com.example.gbook.data.firebase.BooksRealTimeDataSource
import com.example.gbook.data.network.BooksApi
import com.example.gbook.ui.adapter.SearchBooksGridAdapter
import com.example.gbook.databinding.FragmentSearchBinding
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow


class SearchFragment : Fragment() {


private val viewModel: BookViewmodel by activityViewModels {
    BookViewModelFactory(ServiceLocator.provideBooksRepository())
}
    private var binding : FragmentSearchBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
         binding = FragmentSearchBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding!!.lifecycleOwner = this

        // Giving the binding access to the ViewModel
        binding!!.viewModel = viewModel

        binding!!.photosGridSearch.adapter = SearchBooksGridAdapter()


        binding!!.searchView.setOnQueryTextListener( object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                binding!!.searchView.clearFocus()
                viewModel.getSearchBook(query)

                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                viewModel.getSearchBook(query)

                return true
            }

        })


        return binding!!.root

    }

//    private fun FragmentSearchBinding.bindState(
//        uiState : StateFlow<UiState>,
//        pagingData: Flow<PagingData<BooksData>>,
//        uiAction: (UiAction) -> Unit
//    ){
//        val searchAdapter = SearchBooksGridAdapter()
//        photos_grid_search.adapter = searchAdapter.withLoadStateHeaderAndFooter(
//            header =
//        )
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


}

//
//sealed class UiAction {
//    data class Search(val query: String) : UiAction()
//    data class Scroll(val currentQuery: String) : UiAction()
//}
//
//data class UiState(
//    val query: String = DEFAULT_QUERY,
//    val lastQueryScrolled: String = DEFAULT_QUERY,
//    val hasNotScrolledForCurrentSearch: Boolean = false
//)
//
//private const val LAST_QUERY_SCROLLED: String = "last_query_scrolled"
//private const val LAST_SEARCH_QUERY: String = "last_search_query"
//private const val DEFAULT_QUERY = "Android"