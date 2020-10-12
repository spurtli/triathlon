package com.spurtli.triathlon

import org.apache.beam.sdk.testing.PAssert
import org.apache.beam.sdk.testing.TestPipeline
import org.apache.beam.sdk.transforms.Create
import org.apache.beam.sdk.transforms.Values
import org.apache.beam.sdk.values.KV
import org.junit.Rule
import org.junit.Test

internal class ShardTest {
  @Rule
  @JvmField
  val testPipeline: TestPipeline = TestPipeline.create()

  @Test
  fun testAssignToSingleShard() {
    val input = testPipeline.apply(Create.of(listOf(1, 2, 3)))
    val output = input.apply(Shard.assign(1))

    PAssert
      .that(output)
      .containsInAnyOrder(
        KV.of(0, 1),
        KV.of(0, 2),
        KV.of(0, 3),
      )

    testPipeline.run()
  }

  @Test
  fun testAssignToMultipleShards() {
    val input = testPipeline.apply(Create.of(listOf(1, 2, 3)))

    val output = input
      .apply(Shard.assign(3))
      .apply(Values.create())

    PAssert
      .that(output)
      .containsInAnyOrder(1, 2, 3)

    testPipeline.run()
  }
}
