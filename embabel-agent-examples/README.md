# Embabel Agent Examples

This module contains examples and demonstrations of Embabel Agent functionality.

## Examples Included

### 1. Basic RAG Example (`basic-rag`)
- Demonstrates how to set up and use the RAG (Retrieval-Augmented Generation) system
- Shows document ingestion and similarity search
- Example queries and responses

### 2. Agent Conversation Example (`agent-conversation`)
- Shows how to create conversational agents
- Demonstrates context management and multi-turn conversations
- Integration with different AI models

### 3. Planning System Example (`planning`)
- Demonstrates the planning system with actions, goals, and cost-benefit analysis
- Shows how to create plans and execute them step by step
- Example of cost and value calculations for decision making

### 4. Custom Tools Example (`custom-tools`)
- Demonstrates how to create and register custom tools for agents
- Shows tool discovery and execution
- Example of extending agent capabilities

### 5. Vector Store Examples (`vector-store`)
- Examples using different vector store implementations
- Neo4j vector store configuration and usage
- Document embedding and retrieval patterns

## Running the Examples

Each example can be run as a standalone Spring Boot application:

```bash
# Build the module
mvn clean compile

# Run a specific example (using the convenient script)
./run-examples.sh

# Or run directly with Maven
mvn spring-boot:run -Dspring-boot.run.main-class=com.embabel.examples.rag.BasicRagExample

# Run with specific profiles
mvn spring-boot:run -Dspring-boot.run.profiles=neo
```

## Using the Run Script

The module includes a convenient script `run-examples.sh` that makes it easy to run different examples:

```bash
# Show the menu
./run-examples.sh

# Run specific examples directly
./run-examples.sh 1          # Basic RAG Example
./run-examples.sh conversation   # Conversation Example
./run-examples.sh planning      # Planning Example
./run-examples.sh tools         # Custom Tools Example

# Run tests
./run-examples.sh test

# Build the module
./run-examples.sh build

# Run with Neo4j profile
./run-examples.sh neo
```

## Configuration

Examples use different Spring profiles to demonstrate various configurations:
- `test`: Uses fake/mock implementations for testing
- `neo`: Uses Neo4j vector store
- `default`: Uses default implementations

## Sample Data

The `src/main/resources/sample-data/` directory contains sample documents and data files used by the examples.

## Dependencies

This module depends on:
- `embabel-agent-api`: Core agent functionality
- `embabel-agent-rag`: RAG implementation
- `embabel-agent-starter`: Spring Boot starter
- Spring AI: AI and vector store capabilities
