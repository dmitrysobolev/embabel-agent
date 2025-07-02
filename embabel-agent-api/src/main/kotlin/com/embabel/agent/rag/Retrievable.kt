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
package com.embabel.agent.rag

import com.embabel.common.core.StableIdentified
import com.embabel.common.core.types.Described
import com.embabel.common.core.types.HasInfoString
import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

/**
 * A Retrievable object instance is a chunk or an entity
 * It has a stable id.
 */
sealed interface Retrievable : StableIdentified, HasInfoString {

    override fun persistent(): Boolean = true

    val metadata: Map<String, Any?>
}

/**
 * Traditional RAG. Text chunk
 */
interface Chunk : Retrievable {

    /**
     * Text content
     */
    val text: String

    companion object {

        operator fun invoke(
            id: String,
            text: String,
            metadata: Map<String, Any?> = emptyMap(),
        ): Chunk {
            return ChunkImpl(
                id = id,
                text = text,
                metadata = metadata,
            )
        }

    }

    override fun infoString(verbose: Boolean?): String {
        return "chunk: $text"
    }
}

private data class ChunkImpl(
    override val id: String,
    override val text: String,
    override val metadata: Map<String, Any?>,
) : Chunk

/**
 * A fact.
 * @param assertion the text of the fact
 * @param authority the authority of the fact, such as a person
 */
data class Fact(
    val assertion: String,
    val authority: String,
    override val metadata: Map<String, Any?> = emptyMap(),
    override val id: String = UUID.randomUUID().toString(),
) : Retrievable {

    override fun infoString(verbose: Boolean?): String =
        "Fact $id from $authority: $assertion"
}

/**
 * Retrieved Entity
 */
interface EntityData : Retrievable, Described {

    @get:Schema(
        description = "description of this entity",
        example = "A customer of Acme Industries named Melissa Bell",
        required = true,
    )
    override val description: String

    /**
     * Labels of the entity. In Neo, this might include multiple labels.
     * In a relational database, this might be a single table name.
     */
    @get:Schema(
        description = "Labels of the entity. In Neo, this might include multiple labels. In a relational database, this might be a single table name.",
        example = "[\"Person\", \"Customer\"]",
        required = true,
    )
    val labels: Set<String>

    @get:Schema(
        description = "Properties of this object. Arbitrary key-value pairs, although likely specified in schema. Must filter out embedding",
        example = "{\"birthYear\": 1854, \"deathYear\": 1930}",
        required = true,
    )
    val properties: Map<String, Any>

    override fun infoString(verbose: Boolean?): String {
        val labelsString = labels.joinToString(":")
        return "(${labelsString} id='$id')"
    }

}
