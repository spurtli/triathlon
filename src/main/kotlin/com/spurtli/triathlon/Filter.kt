package com.spurtli.triathlon

import org.apache.beam.sdk.transforms.Filter
import org.apache.beam.sdk.transforms.SerializableFunction

class Filter {
  companion object {
    /**
     * A generic filter operation. Takes a comparator predicate and returns a
     * [PCollection] filtered by the predicate.
     *
     * @param comparator The comparator predicate.
     */
    fun <T> by(comparator: (element: T) -> Boolean): Filter<T> = Filter
      .by<T, SerializableFunction<T, Boolean>>(SerializableFunction { comparator(it) })

    /**
     * A utility filter transform to only return PCollection elements that match
     * the value of the input argument.
     *
     * @param value
     */
    fun <T> equal(value: Any?) = by<T> { it == value }

    /**
     * A utility filter transform to only return PCollection elements that do
     * not match the value of the input argument.
     *
     * @param value
     */
    fun <T> notEqual(value: Any?) = by<T> { it != value }

    /**
     * A utility filter transform to extract all null values from a PCollection.
     * Probably completely useless.
     */
    fun <T> isNull() = equal<T>(null)

    /**
     * A utility filter transform to return a PCollection of elements that are
     * not null.
     */
    fun <T> isNotNull() = notEqual<T>(null)
  }
}
