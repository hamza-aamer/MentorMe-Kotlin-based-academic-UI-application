package com.example.a1

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreenTest {

    @get:Rule
    var activityRule = ActivityScenarioRule(LoginScreen::class.java)

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }


    @Test
    fun Forgotpass_redirectsToForgotPassScreen() {
        onView(withId(R.id.forgotPassword)).perform(click())
        intended(hasComponent(ForgotPassScreen::class.java.name))
    }
    @Test
    fun loginButton_redirectsToBottomNavigationBar() {
        onView(withId(R.id.loginbtn)).perform(click())
        intended(hasComponent(BottomNavigationBar::class.java.name))
    }


}
