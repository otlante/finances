package com.otlante.finances

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.otlante.finances.ui.screens.settings.SettingsScreen
import com.otlante.finances.ui.theme.FinancesTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SettingsTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testSettingsItemsExist() {
        // Ждем загрузки приложения
        composeTestRule.waitUntil(timeoutMillis = 3000) {
            composeTestRule.onAllNodesWithText("Расходы").fetchSemanticsNodes().size == 1
        }
        composeTestRule.onNodeWithText("Настройки").performClick()
        composeTestRule.waitUntil(timeoutMillis = 3000) {
            composeTestRule.onAllNodesWithText("Код пароль").fetchSemanticsNodes().size == 1
        }

        // Проверяем наличие всех элементов настроек
        composeTestRule.onNodeWithText("Тёмная тема").assertExists()
        composeTestRule.onNodeWithText("Основной цвет").assertExists()
        composeTestRule.onNodeWithText("Хаптики").assertExists()
        composeTestRule.onNodeWithText("Код пароль").assertExists()
        composeTestRule.onNodeWithText("Синхронизация").assertExists()
        composeTestRule.onNodeWithText("Язык").assertExists()
        composeTestRule.onNodeWithText("О программе").assertExists()
    }
} 