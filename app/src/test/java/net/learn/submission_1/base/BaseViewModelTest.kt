package net.learn.submission_1.base

import net.learn.submission_1.utils.Dummy
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class BaseViewModelTest {

    private lateinit var viewModel: BaseViewModel
    private val dummyMovie = Dummy.generateDummnyMovies()[0]

    @Before
    fun setUp() {
        viewModel = BaseViewModel()
    }

    @Test
    fun getDetails() {
        viewModel.setId(dummyMovie.id)
        val entity = viewModel.getDetails()
        assertNotNull(entity)
        assertEquals(dummyMovie.id, entity?.id)
        assertEquals(dummyMovie.backdropPath, entity?.backdropPath)
        assertEquals(dummyMovie.originalLanguage, entity?.originalLanguage)
        assertEquals(dummyMovie.overview, entity?.overview)
        assertEquals(dummyMovie.posterPath, entity?.posterPath)
        assertEquals(dummyMovie.title, entity?.title)
        assertEquals(dummyMovie.releaseDate, entity?.releaseDate)
        assertEquals(dummyMovie.voteAverage, entity?.voteAverage)
    }


    @Test
    fun getMovies() {
        val MovieItems = viewModel.getMovies()
        assertNotNull(MovieItems)
        assertEquals(10, MovieItems.size)
    }

    @Test
    fun getTvShows() {
        val TvItems = viewModel.getTvShows()
        assertNotNull(TvItems)
        assertEquals(10, TvItems.size)
    }
}