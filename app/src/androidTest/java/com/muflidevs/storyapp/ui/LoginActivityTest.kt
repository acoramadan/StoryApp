package com.muflidevs.storyapp.ui


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.muflidevs.storyapp.R
import com.muflidevs.storyapp.helper.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginActivityTest {

    @get:Rule
    val activity = ActivityScenarioRule(LoginActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @org.junit.Test
    fun loginIsSuccessAndLogoutIsSuccess() {

        onView(withId(R.id.ed_login_email)).perform(
            typeText("zaldyGG@gmail.com"), closeSoftKeyboard()
        )
        onView(withId(R.id.ed_login_password)).perform(
            typeText("123456678"), closeSoftKeyboard()
        )

        onView(withId(R.id.login_btn)).perform(click())

        onView(withId(R.id.main_activity_real))
            .check(matches(isDisplayed()))

        onView(withId(R.id.logout_btn)).perform(click())

        onView(withId(R.id.ed_login_email))
            .check(matches(isDisplayed()))
    }

}