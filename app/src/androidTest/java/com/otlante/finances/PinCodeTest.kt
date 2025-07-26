package com.otlante.finances

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.otlante.finances.ui.screens.pinCode.PinCodeSetupScreen
import com.otlante.finances.ui.screens.pinCode.PinCodeEntryScreen
import com.otlante.finances.ui.theme.FinancesTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PinCodeTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testPinCodeInputValidation() {
        // Ждем загрузки приложения
        composeTestRule.waitUntil(timeoutMillis = 3000) {
            composeTestRule.onAllNodesWithText("Расходы").fetchSemanticsNodes().size == 1
        }

        composeTestRule.onNodeWithText("Настройки").performClick()
        composeTestRule.waitUntil(timeoutMillis = 3000) {
            composeTestRule.onAllNodesWithText("Код пароль").fetchSemanticsNodes().size == 1
        }
        composeTestRule.onNodeWithText("Код пароль").performClick()

        composeTestRule.waitUntil(timeoutMillis = 3000) {
            composeTestRule.onAllNodesWithText("Установите PIN-код").fetchSemanticsNodes().size == 1
        }

        val pinInput = composeTestRule.onNodeWithTag("pin_code_input", useUnmergedTree = true)
        val nextButton = composeTestRule.onNodeWithTag("pin_code_next", useUnmergedTree = true)

        // Проверяем, что кнопка неактивна при пустом поле
        nextButton.assertIsNotEnabled()

        // Вводим короткий пин-код (3 цифры)
        pinInput.performTextInput("123")
        nextButton.assertIsNotEnabled()

        pinInput.performTextClearance()
        nextButton.assertIsNotEnabled()

        // Вводим правильный пин-код (4 цифры)
        pinInput.performTextInput("1234")
        nextButton.assertIsEnabled()

        pinInput.performTextClearance()
        nextButton.assertIsNotEnabled()
    }
} 