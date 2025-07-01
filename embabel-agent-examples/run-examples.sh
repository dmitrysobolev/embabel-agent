#!/bin/bash

# Embabel Examples Runner Script
# This script helps you run different examples easily

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"

echo "🚀 Embabel Agent Examples Runner"
echo "Project root: $PROJECT_ROOT"
echo ""

# Function to run an example
run_example() {
    local example_class=$1
    local profile=${2:-"test"}
    local description=$3
    
    echo "📋 Running: $description"
    echo "   Class: $example_class"
    echo "   Profile: $profile"
    echo ""
    
    cd "$PROJECT_ROOT"
    mvn -pl embabel-agent-examples spring-boot:run \
        -Dspring-boot.run.main-class="$example_class" \
        -Dspring-boot.run.profiles="$profile" \
        -q
}

# Display menu
show_menu() {
    echo "Available Examples:"
    echo ""
    echo "1. Basic RAG Example"
    echo "   Demonstrates document ingestion and similarity search"
    echo ""
    echo "2. Conversation Example"
    echo "   Interactive conversational agent demonstration"
    echo ""
    echo "3. Planning Example"
    echo "   Demonstrates the planning system with actions and goals"
    echo ""
    echo "4. Custom Tools Example"
    echo "   Shows how to create and use custom tools"
    echo ""
    echo "5. Run Tests"
    echo "   Execute all example tests"
    echo ""
    echo "6. Build Examples Module"
    echo "   Compile and package the examples"
    echo ""
}

# Main execution
case "${1:-menu}" in
    "1"|"rag")
        run_example "com.embabel.examples.rag.BasicRagExample" "test" "Basic RAG Example"
        ;;
    "2"|"conversation")
        run_example "com.embabel.examples.conversation.ConversationExample" "test" "Conversation Example"
        ;;
    "3"|"planning")
        run_example "com.embabel.examples.planning.PlanningExample" "test" "Planning Example"
        ;;
    "4"|"tools")
        run_example "com.embabel.examples.tools.CustomToolsExample" "test" "Custom Tools Example"
        ;;
    "5"|"test")
        echo "🧪 Running all tests..."
        cd "$PROJECT_ROOT"
        mvn -pl embabel-agent-examples test
        ;;
    "6"|"build")
        echo "🔨 Building examples module..."
        cd "$PROJECT_ROOT"
        mvn -pl embabel-agent-examples clean compile
        ;;
    "neo")
        echo "🗄️ Running with Neo4j profile (make sure Neo4j is running)..."
        run_example "com.embabel.examples.rag.BasicRagExample" "neo" "Basic RAG Example with Neo4j"
        ;;
    "menu"|*)
        show_menu
        echo "Usage: $0 [option]"
        echo ""
        echo "Options:"
        echo "  1, rag          - Run Basic RAG Example"
        echo "  2, conversation - Run Conversation Example"
        echo "  3, planning     - Run Planning Example"
        echo "  4, tools        - Run Custom Tools Example"
        echo "  5, test         - Run all tests"
        echo "  6, build        - Build the module"
        echo "  neo             - Run RAG example with Neo4j profile"
        echo "  menu            - Show this menu (default)"
        ;;
esac
