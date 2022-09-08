package com.robo.news.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import com.robo.news.R
import com.robo.news.databinding.CustomToolbarBinding
import com.robo.news.databinding.FragmentHomeBinding
import com.robo.news.source.news.ArticleModel

import com.robo.news.ui.detail.DetailActivity
import com.robo.news.ui.news.NewsAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.dsl.module
import timber.log.Timber

val homeModule = module {
    factory { HomeFragment() }
}
sealed class Lodaing {
    data class  Hide(val hide: Boolean = false)
    var SHOW:Boolean =false
}

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var bindingToolbar: CustomToolbarBinding
    private val viewModel: HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        bindingToolbar = binding.toolbar
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding =  binding.let {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
            it
        }


        bindingToolbar.apply{
            title =  viewModel.title
            container.inflateMenu(R.menu.menu_search)
        }

        val menu = binding.toolbar.container.menu
        val search = menu.findItem(R.id.action_search)
        val searchView = search.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                firstLoad()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { viewModel.query = it }
                return true
            }

        })

        firstLoad()
        binding.listNews.adapter = newsAdapter
        viewModel.news.observe(viewLifecycleOwner, {
            Timber.e(it.articles.toString())
            if (viewModel.page == 1) newsAdapter.clear()
            newsAdapter.add(it.articles)
        })

        viewModel.message.observe(viewLifecycleOwner, {
            it?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        })
        binding.scroll.setOnScrollChangeListener { v: NestedScrollView, _, scrollY, _, _ ->
            if (scrollY == v?.getChildAt(0)!!.measuredHeight - v?.measuredHeight) {
                if (viewModel.page <= viewModel.maxpage && viewModel.loadMore.value == false) viewModel.fetch()
            }
        }

    }

    private fun firstLoad() {
        binding.scroll.scrollTo(0, 0)

        viewModel.apply {
            page=1
            maxpage =20
        }.fetch()

    }

    private val newsAdapter by lazy {
        NewsAdapter(arrayListOf(), object : NewsAdapter.OnAdapterListener {
            override fun onClick(articleModel: ArticleModel) {
                startActivity(
                    Intent(requireActivity(), DetailActivity::class.java)
                        .putExtra("intent_detail", articleModel)
                )
            }
        })
    }


}