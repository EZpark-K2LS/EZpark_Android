package com.ezpark.parking

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class SmokeTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun mapScreenRendersInitialContent() {
        composeTestRule.onNodeWithText("신성동 제2 공영 주차장").assertIsDisplayed()
        composeTestRule.onNodeWithText("주차장 확인하기").assertIsDisplayed()
    }

    @Test
    fun mapToDetailNavigation() {
        composeTestRule.onNodeWithText("주차장 확인하기").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("1대 주차가능").assertIsDisplayed()
    }
}
