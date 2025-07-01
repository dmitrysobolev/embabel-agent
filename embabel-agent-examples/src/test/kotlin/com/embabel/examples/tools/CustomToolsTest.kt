/*
 * Copyright 2024-2025 Embabel Software, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.embabel.examples.tools

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CustomToolsTest {

    @Test
    fun `calculator tool should perform basic operations`() {
        val calculator = CalculatorTool()

        // Test addition
        val addResult = calculator.execute(mapOf("operation" to "add", "a" to "10", "b" to "5"))
        assertEquals("15.0", addResult)

        // Test subtraction
        val subtractResult = calculator.execute(mapOf("operation" to "subtract", "a" to "10", "b" to "3"))
        assertEquals("7.0", subtractResult)

        // Test multiplication
        val multiplyResult = calculator.execute(mapOf("operation" to "multiply", "a" to "4", "b" to "6"))
        assertEquals("24.0", multiplyResult)

        // Test division
        val divideResult = calculator.execute(mapOf("operation" to "divide", "a" to "20", "b" to "4"))
        assertEquals("5.0", divideResult)
    }

    @Test
    fun `calculator tool should handle division by zero`() {
        val calculator = CalculatorTool()

        val result = calculator.execute(mapOf("operation" to "divide", "a" to "10", "b" to "0"))
        assertEquals("Error: Division by zero", result)
    }

    @Test
    fun `calculator tool should handle invalid operation`() {
        val calculator = CalculatorTool()

        val result = calculator.execute(mapOf("operation" to "power", "a" to "2", "b" to "3"))
        assertTrue(result.startsWith("Error: Unknown operation"))
    }

    @Test
    fun `calculator tool should throw exception for missing parameters`() {
        val calculator = CalculatorTool()

        assertThrows<IllegalArgumentException> {
            calculator.execute(mapOf("operation" to "add", "a" to "10"))
        }
    }

    @Test
    fun `weather tool should return mock weather data`() {
        val weather = WeatherTool()

        val nyWeather = weather.execute(mapOf("location" to "New York"))
        assertTrue(nyWeather.contains("Sunny"))

        val londonWeather = weather.execute(mapOf("location" to "London"))
        assertTrue(londonWeather.contains("Cloudy"))
    }

    @Test
    fun `weather tool should handle unknown locations`() {
        val weather = WeatherTool()

        val result = weather.execute(mapOf("location" to "Mars"))
        assertTrue(result.contains("Weather data not available"))
    }

    @Test
    fun `time tool should return formatted time`() {
        val time = TimeTool()

        val utcTime = time.execute(mapOf("timezone" to "UTC"))
        assertTrue(utcTime.contains("UTC"))
        assertTrue(utcTime.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2} UTC".toRegex()))

        val pstTime = time.execute(mapOf("timezone" to "PST"))
        assertTrue(pstTime.contains("PST"))
    }

    @Test
    fun `tool registry should manage tools correctly`() {
        val registry = CustomToolRegistry()

        // Check that default tools are registered
        val tools = registry.getAvailableTools()
        assertEquals(3, tools.size)

        val toolNames = tools.map { it.name }
        assertTrue(toolNames.contains("calculator"))
        assertTrue(toolNames.contains("weather"))
        assertTrue(toolNames.contains("time"))

        // Test tool execution through registry
        val result = registry.executeTool("calculator", mapOf("operation" to "add", "a" to "2", "b" to "3"))
        assertEquals("5.0", result)
    }

    @Test
    fun `tool registry should throw exception for unknown tool`() {
        val registry = CustomToolRegistry()

        assertThrows<IllegalArgumentException> {
            registry.executeTool("unknown-tool", emptyMap())
        }
    }
}
