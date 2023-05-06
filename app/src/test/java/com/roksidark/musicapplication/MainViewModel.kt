package com.roksidark.musicapplication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.roksidark.core.data.model.entity.SearchItem
import com.roksidark.core.data.model.entity.artist.Artist
import com.roksidark.core.domain.repository.RemoteRepository
import com.roksidark.core.domain.usecase.ArtistUseCases
import com.roksidark.core.domain.usecase.GetAlbumsRemotely
import com.roksidark.core.domain.usecase.GetArtistsRemotely
import com.roksidark.core.util.Constant
import com.roksidark.core.util.Constant.FORMAT
import com.roksidark.feature.MainViewModel
import com.roksidark.feature.util.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.given
import org.mockito.kotlin.verify
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.junit.Assert.assertTrue


@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModel {

    @get:Rule val executor = InstantTaskExecutorRule()

    @Mock private lateinit var mockObserver: Observer<DataState<List<Artist>>>

    private val repository: RemoteRepository = mock()


    private val getArtists = GetArtistsRemotely(
        repository
    )

    private val getAlbums = GetAlbumsRemotely(
        repository
    )

    private val useCases = ArtistUseCases(
        getArtistsRemotely = getArtists,
        getAlbumRemotely = getAlbums
    )

    private val dispatcher = StandardTestDispatcher()

    private var mockHandle: AutoCloseable? = null

    @Before
    fun setUp() {
        mockHandle = MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mockHandle?.close()
    }

    companion object {
        const val SEARCH_TERM = "lacuna"
        private val TEST_ARTIST_LIST = listOf(
            Artist("1", null, null, "Artist1",
                null, null, null, null,
                null, null),
            Artist("2", null, null, "Artist2",
                null, null, null, null,
                null, null)
        )

        val SEARCH_ITEM = SearchItem("2023-05-06T09:56:10.505Z", TEST_ARTIST_LIST)
    }

    @Test
    fun `performSearch should fetch artists and update items LiveData`() = runTest {

        val viewModel = MainViewModel(useCases)

        given(useCases.getArtistsRemotely.invoke(eq(FORMAT), eq(SEARCH_TERM)))
            .willReturn(SEARCH_ITEM)

        viewModel.items.observeForever(mockObserver)
        viewModel.performSearch(SEARCH_TERM)
        advanceUntilIdle()

        verify(repository).getArtists(eq(FORMAT), eq(SEARCH_TERM))

        val argumentCaptor = argumentCaptor<DataState<List<Artist>>>()
        verify(mockObserver).onChanged(argumentCaptor.capture())
        val capturedState = argumentCaptor.firstValue
        assertTrue(capturedState is DataState.Success)

        val capturedArtists = (capturedState as DataState.Success<List<Artist>>).data
        assertEquals(SEARCH_ITEM.artists, capturedArtists)
    }

    @Test
    fun `performSearch should handle error and emit DataState Error`() = runTest {
        val viewModel = MainViewModel(useCases)

        given(useCases.getArtistsRemotely.invoke(eq(FORMAT), eq(SEARCH_TERM)))
            .willThrow()

        viewModel.items.observeForever(mockObserver)
        viewModel.performSearch(SEARCH_TERM)
        advanceUntilIdle()

        verify(repository).getArtists(eq(FORMAT), eq(SEARCH_TERM))

        val argumentCaptor = argumentCaptor<DataState<List<Artist>>>()
        verify(mockObserver).onChanged(argumentCaptor.capture())
        val capturedState = argumentCaptor.firstValue
        assertTrue(capturedState is DataState.Error)
    }
}