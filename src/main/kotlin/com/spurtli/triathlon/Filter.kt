package com.spurtli.triathlon

import org.apache.beam.sdk.transforms.Filter
import org.apache.beam.sdk.transforms.SerializableFunction

class Filter {
  companion object {
    fun <T> by(comparator: (element: T) -> Boolean): Filter<T> = Filter
      .by<T, SerializableFunction<T, Boolean>>(SerializableFunction { comparator(it) })

    fun <T> equal(value: Any?) = by<T> { it == value }
    fun <T> notEqual(value: Any?) = by<T> { it != value }

    fun <T> isNull() = equal<T>(null)
    fun <T> isNotNull() = notEqual<T>(null)
  }
}
