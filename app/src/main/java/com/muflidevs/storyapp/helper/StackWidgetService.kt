package com.muflidevs.storyapp.helper

import android.content.Intent
import android.widget.RemoteViewsService
import com.muflidevs.storyapp.viewModel.StackRemoteViewsFactory

class StackWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(p0: Intent?): RemoteViewsFactory =
        StackRemoteViewsFactory(this.applicationContext)

}