package com.roksidark.musicapplication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.roksidark.core.data.model.entity.AlbumItem
import com.roksidark.core.data.model.entity.SearchItem
import com.roksidark.core.data.model.entity.album.Album
import com.roksidark.core.data.model.entity.artist.Artist
import com.roksidark.core.domain.repository.RemoteRepository
import com.roksidark.core.domain.usecase.ArtistUseCases
import com.roksidark.core.domain.usecase.GetAlbumsRemotely
import com.roksidark.core.domain.usecase.GetArtistsRemotely
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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.eq
import org.mockito.kotlin.given
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify


@OptIn(ExperimentalCoroutinesApi::class)
class TestMainViewModel {

    @get:Rule
    val executor = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockObserverArtists: Observer<DataState<List<Artist>>>
    @Mock
    private lateinit var mockObserverAlbums: Observer<DataState<List<Album?>>>

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

    @Test
    fun `performSearch should fetch artists and update items LiveData`() = runTest {

        val viewModel = MainViewModel(useCases)

        given(useCases.getArtistsRemotely.invoke(eq(FORMAT), eq(searchItem)))
            .willReturn(searchItemAlbums)

        viewModel.items.observeForever(mockObserverArtists)
        viewModel.performSearch(searchItem)
        advanceUntilIdle()

        verify(repository).getArtists(eq(FORMAT), eq(searchItem))

        val argumentCaptor = argumentCaptor<DataState<List<Artist>>>()
        verify(mockObserverArtists).onChanged(argumentCaptor.capture())
        val capturedState = argumentCaptor.firstValue
        assertTrue(capturedState is DataState.Success)

        val capturedArtists = (capturedState as DataState.Success<List<Artist>>).data
        assertEquals(searchItemAlbums.artists, capturedArtists)
    }

    @Test
    fun `performSearch should handle error and emit DataState Error`() = runTest {
        val viewModel = MainViewModel(useCases)

        given(useCases.getArtistsRemotely.invoke(eq(FORMAT), eq(searchItem)))
            .willThrow()

        viewModel.items.observeForever(mockObserverArtists)
        viewModel.performSearch(searchItem)
        advanceUntilIdle()

        verify(repository).getArtists(eq(FORMAT), eq(searchItem))

        val argumentCaptor = argumentCaptor<DataState<List<Artist>>>()
        verify(mockObserverArtists).onChanged(argumentCaptor.capture())
        val capturedState = argumentCaptor.firstValue
        assertTrue(capturedState is DataState.Error)
    }

    @Test
    fun `getDetails should fetch albums and update itemAlbums LiveData`() = runTest {
        val viewModel = MainViewModel(useCases)

        given(useCases.getAlbumRemotely.invoke(eq(FORMAT), eq(ID)))
            .willReturn(albumItem)

        viewModel.itemAlbums.observeForever(mockObserverAlbums)
        viewModel.getDetails(ID)
        advanceUntilIdle()

        verify(repository).getAlbums(eq(FORMAT), eq(ID))

        val argumentCaptor = argumentCaptor<DataState<List<Album?>>>()
        verify(mockObserverAlbums).onChanged(argumentCaptor.capture())
        val capturedState = argumentCaptor.firstValue
        assertTrue(capturedState is DataState.Success)

        val capturedAlbums = (capturedState as DataState.Success<List<Album?>>).data
        assertEquals(albumItem.releases, capturedAlbums)
    }

    @Test
    fun `getDetails should handle error and emit DataState Error`() = runTest {
        val viewModel = MainViewModel(useCases)

        given(useCases.getAlbumRemotely.invoke(eq(FORMAT), eq(ID)))
            .willThrow()

        viewModel.itemAlbums.observeForever(mockObserverAlbums)
        viewModel.getDetails(ID)
        advanceUntilIdle()

        verify(repository).getAlbums(eq(FORMAT), eq(ID))

        val argumentCaptor = argumentCaptor<DataState<List<Album?>>>()
        verify(mockObserverAlbums).onChanged(argumentCaptor.capture())
        val capturedState = argumentCaptor.firstValue
        assertTrue(capturedState is DataState.Error)
    }

    companion object {
        const val searchItem = "lacuna"
        private val testArtistList = listOf(
            Artist(
                "1", null, null, "Artist1",
                null, null, null, null,
                null, null
            ),
            Artist(
                "2", null, null, "Artist2",
                null, null, null, null,
                null, null
            )
        )

        val searchItemAlbums = SearchItem("2023-05-06T09:56:10.505Z", testArtistList)
        const val ID = "123"
        private val albumList = listOf(
            Album(
                "1", null, "Album1", null,
                null, null, null, null, null, null
            ),
            Album(
                "2", null, "Album2", null,
                null, null, null, null, null, null
            )
        )

        val albumItem = AlbumItem(albumList)
    }
}