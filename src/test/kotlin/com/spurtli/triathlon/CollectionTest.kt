package com.spurtli.triathlon

import com.spurtli.triathlon.test.schemas.Id
import org.apache.beam.sdk.testing.PAssert
import org.apache.beam.sdk.testing.TestPipeline
import org.apache.beam.sdk.transforms.Create
import org.apache.beam.sdk.values.Row
import org.junit.Rule
import org.junit.Test

internal class CollectionTest {
  @Rule
  @JvmField
  val testPipeline: TestPipeline = TestPipeline.create()

  @Test
  fun testForEach() {
    val sideEffect = mutableListOf<Int>()

    val input = testPipeline.apply(Create.of(listOf(1, 2, 3)))
    input
      .apply(Collection.forEach { sideEffect.add(it * 2) })

    testPipeline.run()
  }

  @Test
  fun testMapWithScalar() {
    val input = testPipeline.apply(Create.of(listOf(1, 2, 3)))
    val output = input
      .apply(Collection.map<Int, Int> { it * 2 })

    PAssert
      .that(output)
      .containsInAnyOrder(2, 4, 6)

    testPipeline.run()
  }

  @Test
  fun testMapWithRow() {
    val input = testPipeline.apply(Create.of(listOf(1, 2, 3)))

    val mapFn: (Int) -> Row = { it ->
      Row.withSchema(Id.schema)
        .withFieldValue("id", it)
        .build()
    }

    val output = input
      .apply(Collection.map<Int, Row> { mapFn(it) })
      .setRowSchema(Id.schema)

    PAssert
      .that(output)
      .containsInAnyOrder(
        createIdRow(1),
        createIdRow(2),
        createIdRow(3),
      )

    testPipeline.run()
  }

  private fun createIdRow(value: Int): Row? {
    return Row.withSchema(Id.schema)
      .withFieldValue("id", value)
      .build()
  }
}
