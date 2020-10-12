package com.spurtli.triathlon

import org.apache.beam.sdk.transforms.DoFn
import org.apache.beam.sdk.transforms.MapElements
import org.apache.beam.sdk.transforms.ParDo
import org.apache.beam.sdk.transforms.SerializableFunction
import org.apache.beam.sdk.values.TypeDescriptor

class Collection {
  companion object {
    /**
     * Loops over all elements of [input collection][org.apache.beam.sdk.values.PCollection]
     * and applies the side-effect(s) provided by [fn]. Returns the input
     * element without any mutations.
     *
     * This is useful for calling external services or to log/debug elements.
     *
     * @param fn The side-effect function applied to all elements.
     */
    fun <In> forEach(fn: (In) -> Unit) = ParDo.of(EachFn(fn))

    private class EachFn<T>(private val fn: (T) -> Unit) : DoFn<T, T>() {
      @ProcessElement
      fun process(context: ProcessContext) {
        val element = context.element()
        fn(element)
        context.output(element)
      }
    }

    /**
     * Map all elements from [input collection][org.apache.beam.sdk.values.PCollection]
     * to [output collection][org.apache.beam.sdk.values.PCollection].
     *
     * Be aware of the limitations of the map transform. You cannot reference
     * anything outside the mapping function which isn't a serializable object
     * or function.
     *
     * In case the [Output] type is a [Row][org.apache.beam.sdk.values.Row],
     * make sure to provide the correct [schema][org.apache.beam.sdk.values.PCollection.setRowSchema].
     *
     * @param fn The mapping function
     * @sample com.spurtli.triathlon.CollectionTest.testMapWithRow
     */
    inline fun <Input, reified Output> map(noinline fn: (Input) -> Output): MapElements<Input, Output> {
      val typeDescriptor = TypeDescriptor.of(Output::class.java)
      return MapElements
        .into(typeDescriptor)
        .via(SerializableFunction { fn(it) })
    }
  }
}
