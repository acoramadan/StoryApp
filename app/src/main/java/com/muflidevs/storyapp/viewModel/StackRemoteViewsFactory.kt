package com.muflidevs.storyapp.viewModel

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.muflidevs.storyapp.R
import com.muflidevs.storyapp.data.remote.repository.AuthRepository
import com.muflidevs.storyapp.data.remote.repository.StoryRepository
import com.muflidevs.storyapp.data.remote.retrofit.ApiConfig
import com.muflidevs.storyapp.ui.widget.ImageBannerWidget
import kotlinx.coroutines.runBlocking
import java.net.URL

class StackRemoteViewsFactory(private val mContext: Context) :
    RemoteViewsService.RemoteViewsFactory {

    private val mWidgetItems = ArrayList<Bitmap>()

    override fun onCreate() {

    }

    override fun onDataSetChanged() {
        try {
            runBlocking {
                val authRepository =
                    AuthRepository(ApiConfig.getApiService(), mContext.applicationContext)
                val storyRepo = StoryRepository(ApiConfig.getApiService(authRepository.getToken()))

                mWidgetItems.clear()

                val storyList = storyRepo.getStories()

                storyList!!.forEach { story ->
                    try {
                        val bitmap = BitmapFactory.decodeStream(URL(story.photoUrl).openStream())
                        mWidgetItems.add(bitmap)
                    } catch (e: Exception) {
                        Log.e("onDataSetChanged", "Error downloading image: ${story.photoUrl}", e)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("onDataSetChanged", "Error in onDataSetChanged: ${e.message}", e)
        }
    }

    override fun onDestroy() {

    }

    override fun getCount(): Int = mWidgetItems.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.item_widget)
        rv.setImageViewBitmap(R.id.imageView, mWidgetItems[position])

        val extras = bundleOf(
            ImageBannerWidget.EXTRA_ITEM to position
        )
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = 0

    override fun hasStableIds(): Boolean = false

}
