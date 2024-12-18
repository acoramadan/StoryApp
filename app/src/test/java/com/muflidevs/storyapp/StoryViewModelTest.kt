@file:Suppress("SameReturnValue")

package com.muflidevs.storyapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.muflidevs.storyapp.data.remote.repository.StoryRepository
import com.muflidevs.storyapp.data.remote.response.Story
import com.muflidevs.storyapp.ui.adapter.StoryListAdapter
import com.muflidevs.storyapp.viewModel.StoryViewModel
import com.muflidevs.storyapp.viewModel.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = com.muflidevs.storyapp.viewModel.utils.MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: StoryRepository

    @Test
    fun `when Get Story Should Not Null and Return Data`() = runTest {
        val dummyStory =
            com.muflidevs.storyapp.viewModel.utils.DataDummy.generateDummyStoryResponse()

        val data: PagingData<Story> = StoryPaging.snapshot(dummyStory)

        val expectedQuote = MutableLiveData<PagingData<Story>>()

        expectedQuote.value = data

        Mockito.`when`(storyRepository.getStory()).thenReturn(expectedQuote)

        val storyViewModel = StoryViewModel(storyRepository)
        val actualStory: PagingData<Story> = storyViewModel.storys.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryListAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)

        assertNotNull(differ.snapshot())
        assertEquals(dummyStory.size, differ.snapshot().size)
        assertEquals(dummyStory[0], differ.snapshot()[0])
    }

    @Test
    fun `when Get Story Empty Should Return No Data`() = runTest {
        val data: PagingData<Story> = PagingData.from(emptyList())
        val excpectedStory = MutableLiveData<PagingData<Story>>()
        excpectedStory.value = data
        Mockito.`when`(storyRepository.getStory()).thenReturn(excpectedStory)

        val storyViewModel = StoryViewModel(storyRepository)
        val actualQuote: PagingData<Story> = storyViewModel.storys.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryListAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )

        differ.submitData(actualQuote)

        assertEquals(0, differ.snapshot().size)
    }

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {

        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {

        }

        override fun onRemoved(position: Int, count: Int) {
        }

        override fun onChanged(position: Int, count: Int, payload: Any?) {

        }
    }

    class StoryPaging : PagingSource<Int, LiveData<List<Story>>>() {

        companion object {
            fun snapshot(story: List<Story>): PagingData<Story> {
                return PagingData.from(story)
            }
        }

        @Suppress("SameReturnValue")
        override fun getRefreshKey(state: PagingState<Int, LiveData<List<Story>>>): Int {
            return 0
        }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<Story>>> {
            return LoadResult.Page(emptyList(), 0, 1)
        }
    }
}