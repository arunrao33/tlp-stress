package com.thelastpickle.tlpstress.profiles

import com.datastax.driver.core.Session
import com.datastax.driver.core.BoundStatement

interface IStressRunner {
    fun getNextOperation(partitionKey: String) : Operation
}

/**
 * Stress profile interface.  A stress profile defines the schema, prepared
 * statements, and queries that will be executed.  It should be fairly trivial
 * to imp
 */
interface IStressProfile {

    /**
     * jcommander arguments that will be auto added to the commander subcommand.
     * This lets us drop in arbitrary classes, annotate them with Parameters,
     * and they just magically show up in the CLI tool.
     */
    fun getArguments() : Any

    /**
     * Handles any prepared statements that are needed
     * the class should track all prepared statements internally
     * and pass them on to the Runner
     */
    fun prepare(session: Session)
    /**
     * returns a bunch of DDL statements
     * this can be create table, index, materialized view, etc
     * for most tests this is probably a single table
     * it's OK to put a clustering order in, but otherwise the schema
     * should not specify any other options here because they can all
     * but supplied on the command line.
     *
     * I may introduce a means of supplying default values, because
     * there are plenty of use cases where you would want a specific
     * compaction strategy most of the time (like a time series, or a cache)
     */
    fun schema(): List<String>

    /**
     * returns an instance of the stress runner for this particular class
     * This was done to allow a single instance of an IStress profile to be
     * generated, and passed to the ProfileRunner.
     * The issue is that the profile needs to generate a single schema
     * but then needs to create multiple stress runners
     * this allows the code to be a little cleaner
     */
    fun getRunner(): IStressRunner
}


sealed class Operation {
    // we're going to track metrics on the mutations differently
    // inserts will also carry data that might be saved for later validation
    data class Mutation(val bound: BoundStatement,
                        val fields: Map<String, Any>) : Operation()
    data class SelectStatement(var bound: BoundStatement): Operation()
    // JMX commands


}