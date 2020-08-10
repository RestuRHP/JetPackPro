package net.learn.jetpack.repository

import android.util.Log
import net.learn.jetpack.datastore.movies.MovieDataStore
import net.learn.jetpack.model.movies.Movie

private const val MOVIE_STARTING_PAGE_INDEX = 1

class MovieRepository private constructor() : BaseRepository<MovieDataStore>() {
    private var nextRequestPage = MOVIE_STARTING_PAGE_INDEX
    private var isRequestInProgress = false

    suspend fun getSets(): MutableList<Movie>? {
        val cache = loadPage()
        if (cache != null) return cache
        saveToDB(nextRequestPage)
        return loadPage()
    }

    suspend fun paginationSets() {
        if (isRequestInProgress) return
        val successful = saveToDB(nextRequestPage)
        if (successful) {
            nextRequestPage++
        }
    }

    private suspend fun saveToDB(page: Int?): Boolean {
        isRequestInProgress = true
        var successful = false
        try {
            Log.d("lasRequestedPage", "$page")
            val response = remoteStore?.getSets(page = page)
            localStore?.addAll(response)
            loadPage()
            successful = true
        } catch (ex: Exception) {
            Log.d("Exception", "$ex")
        }
        isRequestInProgress = false
        return successful
    }

    suspend fun loadPage(): MutableList<Movie>? {
        val cache = localStore?.getSets(nextRequestPage)
        return cache
    }

    companion object {
        val instance by lazy { MovieRepository() }
    }
}