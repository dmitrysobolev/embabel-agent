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

import com.embabel.common.util.loggerFor
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Example demonstrating custom tool creation and usage.
 * This example shows how to:
 * 1. Create custom tools for agents
 * 2. Register tools with the agent system
 * 3. Execute tools and handle results
 */
@SpringBootApplication
@ComponentScan(basePackages = ["com.embabel"])
class CustomToolsExample(
    private val toolRegistry: CustomToolRegistry
) : CommandLineRunner {

    private val logger = loggerFor<CustomToolsExample>()

    override fun run(vararg args: String?) {
        logger.info("Starting Custom Tools Example")

        // Demonstrate tool discovery
        demonstrateToolDiscovery()

        // Demonstrate tool execution
        demonstrateToolExecution()

        logger.info("Custom Tools Example completed")
    }

    private fun demonstrateToolDiscovery() {
        logger.info("🔧 Discovering available tools...")

        val availableTools = toolRegistry.getAvailableTools()
        logger.info("Found ${availableTools.size} tools:")

        availableTools.forEach { tool ->
            logger.info("  - ${tool.name}: ${tool.description}")
            logger.info("    Parameters: ${tool.parameters.joinToString(", ")}")
        }
        logger.info("")
    }

    private fun demonstrateToolExecution() {
        logger.info("⚡ Executing sample tool operations...")

        // Execute calculator tool
        executeCalculatorTool()

        // Execute weather tool
        executeWeatherTool()

        // Execute time tool
        executeTimeTool()

        logger.info("")
    }

    private fun executeCalculatorTool() {
        logger.info("🧮 Calculator Tool Example:")
        try {
            val result1 = toolRegistry.executeTool("calculator", mapOf("operation" to "add", "a" to "15", "b" to "25"))
            logger.info("  15 + 25 = $result1")

            val result2 = toolRegistry.executeTool("calculator", mapOf("operation" to "multiply", "a" to "7", "b" to "8"))
            logger.info("  7 × 8 = $result2")

            val result3 = toolRegistry.executeTool("calculator", mapOf("operation" to "divide", "a" to "100", "b" to "4"))
            logger.info("  100 ÷ 4 = $result3")
        } catch (e: Exception) {
            logger.error("Error executing calculator tool", e)
        }
    }

    private fun executeWeatherTool() {
        logger.info("🌤️ Weather Tool Example:")
        try {
            val result1 = toolRegistry.executeTool("weather", mapOf("location" to "New York"))
            logger.info("  Weather in New York: $result1")

            val result2 = toolRegistry.executeTool("weather", mapOf("location" to "London"))
            logger.info("  Weather in London: $result2")
        } catch (e: Exception) {
            logger.error("Error executing weather tool", e)
        }
    }

    private fun executeTimeTool() {
        logger.info("🕐 Time Tool Example:")
        try {
            val result1 = toolRegistry.executeTool("time", mapOf("timezone" to "UTC"))
            logger.info("  Current time (UTC): $result1")

            val result2 = toolRegistry.executeTool("time", mapOf("timezone" to "PST"))
            logger.info("  Current time (PST): $result2")
        } catch (e: Exception) {
            logger.error("Error executing time tool", e)
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(CustomToolsExample::class.java, *args)
        }
    }
}

/**
 * Registry for managing custom tools
 */
@Component
class CustomToolRegistry {

    private val tools = mutableMapOf<String, CustomTool>()
    private val logger = loggerFor<CustomToolRegistry>()

    init {
        // Register built-in example tools
        registerTool(CalculatorTool())
        registerTool(WeatherTool())
        registerTool(TimeTool())
        logger.info("Initialized tool registry with ${tools.size} tools")
    }

    fun registerTool(tool: CustomTool) {
        tools[tool.name] = tool
        logger.debug("Registered tool: ${tool.name}")
    }

    fun getAvailableTools(): List<CustomTool> = tools.values.toList()

    fun executeTool(name: String, parameters: Map<String, String>): String {
        val tool = tools[name] ?: throw IllegalArgumentException("Tool '$name' not found")
        return tool.execute(parameters)
    }
}

/**
 * Base interface for custom tools
 */
interface CustomTool {
    val name: String
    val description: String
    val parameters: List<String>

    fun execute(parameters: Map<String, String>): String
}

/**
 * Example calculator tool
 */
class CalculatorTool : CustomTool {
    override val name = "calculator"
    override val description = "Performs basic mathematical operations"
    override val parameters = listOf("operation", "a", "b")

    override fun execute(parameters: Map<String, String>): String {
        val operation = parameters["operation"] ?: throw IllegalArgumentException("Missing operation parameter")
        val a = parameters["a"]?.toDoubleOrNull() ?: throw IllegalArgumentException("Invalid parameter 'a'")
        val b = parameters["b"]?.toDoubleOrNull() ?: throw IllegalArgumentException("Invalid parameter 'b'")

        return when (operation.lowercase()) {
            "add", "+" -> (a + b).toString()
            "subtract", "-" -> (a - b).toString()
            "multiply", "*" -> (a * b).toString()
            "divide", "/" -> {
                if (b == 0.0) "Error: Division by zero"
                else (a / b).toString()
            }
            else -> "Error: Unknown operation '$operation'"
        }
    }
}

/**
 * Example weather tool (mock implementation)
 */
class WeatherTool : CustomTool {
    override val name = "weather"
    override val description = "Gets weather information for a location"
    override val parameters = listOf("location")

    private val mockWeatherData = mapOf(
        "new york" to "Sunny, 22°C (72°F), Light breeze",
        "london" to "Cloudy, 15°C (59°F), Light rain expected",
        "tokyo" to "Partly cloudy, 18°C (64°F), Moderate humidity",
        "sydney" to "Clear skies, 25°C (77°F), Gentle breeze"
    )

    override fun execute(parameters: Map<String, String>): String {
        val location = parameters["location"]?.lowercase() ?: throw IllegalArgumentException("Missing location parameter")

        return mockWeatherData[location] ?: "Weather data not available for '$location'. " +
                "Available locations: ${mockWeatherData.keys.joinToString(", ")}"
    }
}

/**
 * Example time tool
 */
class TimeTool : CustomTool {
    override val name = "time"
    override val description = "Gets current time for a timezone"
    override val parameters = listOf("timezone")

    override fun execute(parameters: Map<String, String>): String {
        val timezone = parameters["timezone"]?.uppercase() ?: "UTC"
        val currentTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        return when (timezone) {
            "UTC" -> "${currentTime.format(formatter)} UTC"
            "PST" -> "${currentTime.minusHours(8).format(formatter)} PST"
            "EST" -> "${currentTime.minusHours(5).format(formatter)} EST"
            "JST" -> "${currentTime.plusHours(9).format(formatter)} JST"
            else -> "${currentTime.format(formatter)} (${timezone} - offset not implemented)"
        }
    }
}
