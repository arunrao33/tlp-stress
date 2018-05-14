package com.thelastpickle.tlpstress

import java.util.concurrent.ThreadLocalRandom
import kotlin.coroutines.experimental.buildSequence

/**
 * Accepts a function that generates numbers.
 * returns a partition key using a prefix
 * for any normal case there should be a compaction object function
 * that should create a generator that has a function with all the logic inside it
 */

class PartitionKeyGenerator(
        val genFunc: (max: Int) -> Int,
        val prefix: String) {
    /**
     *
     */
    companion object {
        fun random(prefix: String = "test") : PartitionKeyGenerator {
            return PartitionKeyGenerator({max -> ThreadLocalRandom.current().nextInt(1, max) }, prefix)
        }
    }


    fun generateKeys(max: Int) = buildSequence {
        while(true) {
            val tmp = genFunc(max)
            val result = prefix + tmp.toString()
            yield(result)
        }
    }

}