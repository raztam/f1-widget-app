package com.example.f1widgetapp.widgets

import com.example.f1widgetapp.data.modals.ConstructorWidgetSettings
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ConstructorWidgetStateResolverTest {

    @Test
    fun `uses stored settings when glance constructor id is empty`() {
        val stored = ConstructorWidgetSettings(
            constructorId = "ferrari",
            transparency = 0.6f,
            backgroundColor = 0xFF112233.toInt()
        )

        val resolved = resolveConstructorWidgetState(
            glanceConstructorId = "",
            glanceTransparency = null,
            glanceBackgroundColor = null,
            storedSettings = stored
        )

        assertEquals("ferrari", resolved.constructorId)
        assertEquals(0.6f, resolved.transparency)
        assertEquals(0xFF112233.toInt(), resolved.backgroundColor)
        assertTrue(resolved.needsBackfill)
    }

    @Test
    fun `keeps glance state when constructor id exists`() {
        val stored = ConstructorWidgetSettings(
            constructorId = "mclaren",
            transparency = 0.5f,
            backgroundColor = 0xFF445566.toInt()
        )

        val resolved = resolveConstructorWidgetState(
            glanceConstructorId = "mercedes",
            glanceTransparency = 0.8f,
            glanceBackgroundColor = 0xFF778899.toInt(),
            storedSettings = stored
        )

        assertEquals("mercedes", resolved.constructorId)
        assertEquals(0.8f, resolved.transparency)
        assertEquals(0xFF778899.toInt(), resolved.backgroundColor)
        assertFalse(resolved.needsBackfill)
    }

    @Test
    fun `uses defaults when both glance and stored constructor id are empty`() {
        val stored = ConstructorWidgetSettings()

        val resolved = resolveConstructorWidgetState(
            glanceConstructorId = "",
            glanceTransparency = null,
            glanceBackgroundColor = null,
            storedSettings = stored
        )

        assertEquals("", resolved.constructorId)
        assertEquals(0.9f, resolved.transparency)
        assertEquals(0xFF708090.toInt(), resolved.backgroundColor)
        assertFalse(resolved.needsBackfill)
    }
}