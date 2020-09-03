package net.learn.jetpack.ui.tv

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.display_fragment.*
import net.learn.jetpack.R
import net.learn.jetpack.model.tv.TvShow
import net.learn.jetpack.repository.tv.TvRepository
import net.learn.jetpack.ui.BaseViewState

class TvShowFragment : Fragment() {
    private lateinit var vm: TvViewModel
    private lateinit var adapter: TvAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.display_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TvAdapter()
        rv_movies.adapter = adapter
        rv_movies.setHasFixedSize(true)
        rv_movies.layoutManager = LinearLayoutManager(this.context)

        val factory =
            TvListFactory(TvRepository.instance)
        vm = ViewModelProvider(this, factory).get(TvViewModel::class.java).apply {
            viewState.observe(viewLifecycleOwner, Observer(this@TvShowFragment::handleState))
//            swapRefresh.setOnRefreshListener { getSets() }
        }

        setupScrollListener()
    }

    private fun handleState(viewState: BaseViewState<TvShow>?) {
        viewState?.let {
            toggleLoading(it.loading)
//            it.data?.let { data -> showData(data) }
            it.error?.let { error -> showError(error) }
        }
    }

    private fun showData(data: MutableList<TvShow>) {
        adapter.updateData(data)

    }

    private fun showError(error: Exception) {
        Toast.makeText(context, "$error", Toast.LENGTH_LONG).show()
    }

    private fun toggleLoading(loading: Boolean) {
//        swapRefresh.isRefreshing = loading
    }

    private fun setupScrollListener() {
        rv_movies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager
                layoutManager?.let {
                    val visibleItemCount = it.childCount
                    val totalItemCount = it.itemCount
                    val firstVisibleItemPosition = when (layoutManager) {
                        is LinearLayoutManager -> layoutManager.findLastVisibleItemPosition()
                        is GridLayoutManager -> layoutManager.findLastVisibleItemPosition()
                        else -> return
                    }
                    vm.listScrolled(visibleItemCount, firstVisibleItemPosition, totalItemCount)
                }
            }
        })
    }

}