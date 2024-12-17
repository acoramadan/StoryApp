package com.muflidevs.storyapp.viewModel.utils

import com.muflidevs.storyapp.data.remote.response.Story

object DataDummy {
    fun generateDummyStoryResponse(): List<Story> {
        val items: MutableList<Story> = arrayListOf()

        for (i in 0..100) {
            val story = Story (
                id =  i.toString(),
                name = "user + $i",
                description = "description + $i",
                lat = i.toDouble(),
                lon = i.toDouble(),
                photoUrl = "http://exampleOfPicture$i",
                createdAt = "29/12/2024"
            )
            items.add(story)
        }
        return items
    }
}