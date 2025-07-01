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
package com.embabel.examples.conversation

import com.embabel.common.util.loggerFor
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan
import java.util.*

/**
 * Example demonstrating conversational agent capabilities.
 * This example shows how to:
 * 1. Create interactive conversations
 * 2. Maintain conversation context
 * 3. Handle multi-turn dialogues
 */
@SpringBootApplication
@ComponentScan(basePackages = ["com.embabel"])
class ConversationExample : CommandLineRunner {

    private val logger = loggerFor<ConversationExample>()
    private val scanner = Scanner(System.`in`)

    override fun run(vararg args: String?) {
        logger.info("Starting Conversation Example")
        logger.info("This example demonstrates interactive conversation capabilities")
        logger.info("Type 'exit' to quit the conversation")
        logger.info("")

        // Start interactive conversation
        startInteractiveConversation()

        logger.info("Conversation Example completed")
    }

    private fun startInteractiveConversation() {
        val conversationContext = ConversationContext()

        println("🤖 Hello! I'm your Embabel AI Assistant. How can I help you today?")

        while (true) {
            print("👤 You: ")
            val userInput = scanner.nextLine().trim()

            if (userInput.lowercase() == "exit") {
                println("🤖 Goodbye! Thanks for the conversation.")
                break
            }

            if (userInput.isBlank()) {
                continue
            }

            // Process the user input and generate a response
            val response = processUserInput(userInput, conversationContext)
            println("🤖 Assistant: $response")
            println()
        }
    }

    private fun processUserInput(input: String, context: ConversationContext): String {
        // Add user input to conversation history
        context.addMessage("user", input)

        // In a real implementation, this would use the agent's AI capabilities
        // For this example, we'll simulate different types of responses
        val response = when {
            input.contains("hello", ignoreCase = true) || input.contains("hi", ignoreCase = true) -> {
                "Hello! Nice to meet you. What would you like to know about the Embabel Agent system?"
            }
            input.contains("RAG", ignoreCase = true) || input.contains("retrieval", ignoreCase = true) -> {
                "RAG (Retrieval-Augmented Generation) is a powerful technique that combines information retrieval with text generation. " +
                "The Embabel Agent uses RAG to provide more accurate and contextual responses by retrieving relevant information from knowledge bases."
            }
            input.contains("vector", ignoreCase = true) || input.contains("embedding", ignoreCase = true) -> {
                "Vector embeddings are numerical representations of text that capture semantic meaning. " +
                "We use vector stores like Neo4j to efficiently search and retrieve similar content based on these embeddings."
            }
            input.contains("example", ignoreCase = true) || input.contains("demo", ignoreCase = true) -> {
                "I can show you various examples! We have demonstrations for RAG functionality, custom tools, " +
                "vector store operations, and more. What specific area interests you?"
            }
            context.hasContext() -> {
                "Based on our conversation about ${context.getMainTopics()}, let me elaborate further. " +
                "Is there a specific aspect you'd like me to focus on?"
            }
            else -> {
                "That's an interesting question! While I'm a demonstration example, " +
                "a full Embabel Agent would use its RAG capabilities and custom tools to provide detailed, " +
                "contextually relevant answers. What else would you like to explore?"
            }
        }

        // Add assistant response to conversation history
        context.addMessage("assistant", response)

        return response
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(ConversationExample::class.java, *args)
        }
    }
}

/**
 * Simple conversation context to track dialogue history and topics
 */
class ConversationContext {
    private val messages = mutableListOf<ConversationMessage>()

    fun addMessage(role: String, content: String) {
        messages.add(ConversationMessage(role, content, System.currentTimeMillis()))
    }

    fun hasContext(): Boolean = messages.size > 2

    fun getMainTopics(): String {
        val topics = messages
            .filter { it.role == "user" }
            .flatMap { extractKeywords(it.content) }
            .groupingBy { it }
            .eachCount()
            .entries
            .sortedByDescending { it.value }
            .take(3)
            .map { it.key }

        return topics.joinToString(", ")
    }

    private fun extractKeywords(text: String): List<String> {
        // Simple keyword extraction - in a real system this would be more sophisticated
        return text.lowercase()
            .split(" ")
            .filter { it.length > 3 }
            .filter { !it.matches("\\b(the|and|or|but|in|on|at|to|for|of|with|by)\\b".toRegex()) }
    }
}

data class ConversationMessage(
    val role: String,
    val content: String,
    val timestamp: Long
)
