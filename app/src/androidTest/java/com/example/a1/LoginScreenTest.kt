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
    fun Forgotpass_redirectsToForgotPassScreenAndBack() {
        onView(withId(R.id.forgotPassword)).perform(click())
        onView(withId(R.id.LoginRedirect)).perform(click())
        intended(hasComponent(LoginScreen::class.java.name))
    }

    @Test
    fun Register_redirectsToRegisterScreen() {
        onView(withId(R.id.RegisterRedirect)).perform(click())
        intended(hasComponent(RegisterScreen::class.java.name))
    }
    @Test
    fun Register_redirectsToRegisterScreenAndBack() {
        onView(withId(R.id.RegisterRedirect)).perform(click())
        onView(withId(R.id.loginredirect)).perform(click())
        intended(hasComponent(LoginScreen::class.java.name))
    }



}
