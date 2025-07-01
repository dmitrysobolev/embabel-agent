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
package com.embabel.examples.planning

import com.embabel.plan.Action
import com.embabel.plan.Goal
import com.embabel.plan.Plan
import com.embabel.common.core.types.ZeroToOne
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component

/**
 * Example demonstrating the planning system with actions, goals, and cost-benefit analysis.
 */
@SpringBootApplication
class PlanningExampleApp

fun main(args: Array<String>) {
    runApplication<PlanningExampleApp>(*args)
}

@Component
class PlanningExample : CommandLineRunner {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun run(vararg args: String?) {
        logger.info("🎯 Planning System Example")
        logger.info("=" * 50)

        // Define some actions
        val gatherInfo = SimpleAction(
            name = "Gather Information",
            value = 0.3,
            cost = 0.2
        )

        val analyzeData = SimpleAction(
            name = "Analyze Data",
            value = 0.4,
            cost = 0.3
        )

        val makeDecision = SimpleAction(
            name = "Make Decision",
            value = 0.5,
            cost = 0.1
        )

        val implement = SimpleAction(
            name = "Implement Solution",
            value = 0.8,
            cost = 0.6
        )

        // Define a goal
        val solveProblems = SimpleGoal(
            name = "Solve Business Problem",
            value = 1.0
        )

        // Create different plans
        val quickPlan = Plan(
            actions = listOf(makeDecision, implement),
            goal = solveProblems
        )

        val thoroughPlan = Plan(
            actions = listOf(gatherInfo, analyzeData, makeDecision, implement),
            goal = solveProblems
        )

        // Compare plans
        logger.info("\n📊 Plan Comparison:")
        logger.info("Quick Plan:")
        logger.info("  ${quickPlan.infoString(verbose = false)}")
        logger.info("  Detailed: ${quickPlan.infoString(verbose = true)}")

        logger.info("\nThorough Plan:")
        logger.info("  ${thoroughPlan.infoString(verbose = false)}")
        logger.info("  Detailed: ${thoroughPlan.infoString(verbose = true)}")

        // Demonstrate plan execution
        logger.info("\n🔄 Executing Thorough Plan:")
        executePlan(thoroughPlan)
    }

    private fun executePlan(plan: Plan) {
        var currentPlan = plan

        while (!currentPlan.isComplete()) {
            val nextAction = currentPlan.actions.first()
            logger.info("Executing: ${nextAction.name} (value=${nextAction.value}, cost=${nextAction.cost})")

            // Simulate action execution
            Thread.sleep(500)

            // Create new plan with remaining actions
            currentPlan = Plan(
                actions = currentPlan.actions.drop(1),
                goal = currentPlan.goal
            )

            logger.info("Remaining plan: ${currentPlan.infoString(verbose = false)}")
        }

        logger.info("✅ Goal achieved: ${plan.goal.name}")
    }
}

// Simple implementations for the example
data class SimpleAction(
    override val name: String,
    override val value: ZeroToOne,
    override val cost: ZeroToOne
) : Action {
    override fun infoString(verbose: Boolean?): String =
        if (verbose == true) "$name (value=$value, cost=$cost)" else name
}

data class SimpleGoal(
    override val name: String,
    override val value: ZeroToOne
) : Goal {
    override fun infoString(verbose: Boolean?): String =
        if (verbose == true) "$name (value=$value)" else name
}

private operator fun String.times(count: Int): String = repeat(count)
