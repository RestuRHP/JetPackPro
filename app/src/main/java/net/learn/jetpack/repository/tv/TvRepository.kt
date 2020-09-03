package net.learn.jetpack.repository.tv

import android.util.Log
import net.learn.jetpack.datastore.tv.TvDataStore
import net.learn.jetpack.model.tv.TvShow
import net.learn.jetpack.repository.BaseRepository

private const val TV_STARTING_PAGE_INDEX = 1

class TvRepository private constructor() : BaseRepository<TvDataStore>() {
    private var nextRequestPage = TV_STARTING_PAGE_INDEX
    private var isRequestInProgress = false

    suspend fun getSets(): MutableList<TvShow>? {
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

    private suspend fun saveToDB(page: Int): Boolean {
        isRequestInProgress = true
        var successful = false
        try {
            val response = remoteStore?.getSets(page = page)
            localStore?.addAll(response)
            successful = true
        } catch (ex: Exception) {
            Log.d("Exception", "$ex")
        }
        isRequestInProgress = false
        return successful
    }

    suspend fun loadPage(): MutableList<TvShow>? {
        val cache = localStore?.getSets(nextRequestPage)
        return cache
    }

    companion object {
        val instance by lazy { TvRepository() }
    }

}