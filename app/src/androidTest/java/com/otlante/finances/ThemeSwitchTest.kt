package com.otlante.finances

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ThemeSwitchTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testThemeSwitchInMainActivity() {
        // Ждем, пока приложение загрузится
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithText("Расходы").fetchSemanticsNodes().size == 1
        }

        // Переходим в настройки через нижнюю навигацию
        composeTestRule.onNodeWithText("Настройки").performClick()

        // Ждем загрузки экрана настроек
        composeTestRule.waitUntil(timeoutMillis = 3000) {
            composeTestRule.onAllNodesWithText("Тёмная тема").fetchSemanticsNodes().size == 1
        }

        // Находим переключатель темной темы
        val themeSwitch = composeTestRule.onNodeWithTag("theme_switch", useUnmergedTree = true)
        themeSwitch.assertExists()

        // Проверяем начальное состояние (светлая тема по умолчанию)
        themeSwitch.assertIsOff()

        // Переключаем на темную тему
        themeSwitch.performClick()

        // Проверяем, что переключатель включен
        themeSwitch.assertIsOn()

        // Переключаем обратно на светлую тему
        themeSwitch.performClick()

        // Проверяем, что переключатель выключен
        themeSwitch.assertIsOff()
    }
} 