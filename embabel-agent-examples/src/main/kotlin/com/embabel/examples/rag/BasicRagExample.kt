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
package com.embabel.examples.rag

import com.embabel.agent.rag.Ingester
import com.embabel.agent.rag.RagRequest
import com.embabel.agent.rag.RagService
import com.embabel.common.util.loggerFor
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan

/**
 * Basic example demonstrating RAG functionality.
 * This example shows how to:
 * 1. Ingest documents into the RAG system
 * 2. Perform similarity searches
 * 3. Retrieve relevant content
 */
@SpringBootApplication
@ComponentScan(basePackages = ["com.embabel"])
class BasicRagExample(
    private val ragService: RagService,
    private val ingester: Ingester,
) : CommandLineRunner {

    private val logger = loggerFor<BasicRagExample>()

    override fun run(vararg args: String?) {
        logger.info("Starting Basic RAG Example")
        logger.info("RAG Service: ${ragService.infoString()}")
        logger.info("Ingester: ${ingester.infoString()}")

        // Example 1: Check if ingester is active
        if (ingester.active()) {
            logger.info("Ingester is active, attempting to ingest sample documents...")
            ingestSampleDocuments()
        } else {
            logger.warn("Ingester is not active - no RAG services available")
        }

        // Example 2: Perform sample queries
        performSampleQueries()

        logger.info("Basic RAG Example completed")
    }

    private fun ingestSampleDocuments() {
        try {
            // In a real scenario, you would point to actual document files
            val sampleResourcePath = "classpath:sample-data/sample-document.txt"

            logger.info("Ingesting documents from: $sampleResourcePath")
            val result = ingester.ingest(sampleResourcePath)

            logger.info("Ingestion result: ${result.documentsWritten} documents written to ${result.storesWrittenTo.size} stores")
            logger.info("Stores written to: ${result.storesWrittenTo}")

            if (result.success()) {
                logger.info("✅ Document ingestion successful!")
            } else {
                logger.warn("⚠️ Document ingestion failed or no stores available")
            }
        } catch (e: Exception) {
            logger.error("Error during document ingestion", e)
        }
    }

    private fun performSampleQueries() {
        val sampleQueries = listOf(
            "What is artificial intelligence?",
            "How do neural networks work?",
            "Explain machine learning concepts",
            "What are the benefits of RAG systems?"
        )

        sampleQueries.forEach { query ->
            logger.info("🔍 Performing query: '$query'")

            try {
                val ragRequest = RagRequest(
                    query = query,
                    topK = 5,
                    similarityThreshold = 0.5
                )

                val response = ragService.search(ragRequest)

                logger.info("📄 Response from ${response.service}:")
                logger.info("   Found ${response.results.size} relevant chunks")

                response.results.forEachIndexed { index, result ->
                    logger.info("   ${index + 1}. Score: ${result.score}, Content: ${result.match.infoString(verbose = false)}")
                }

            } catch (e: Exception) {
                logger.error("Error performing query: '$query'", e)
            }

            logger.info("") // Empty line for readability
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(BasicRagExample::class.java, *args)
        }
    }
}
