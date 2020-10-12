package com.spurtli.triathlon

import org.apache.beam.sdk.transforms.DoFn
import org.apache.beam.sdk.transforms.ParDo
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import kotlin.reflect.full.companionObject

class Log {
  companion object {
    private val logger by LoggerImpl()

    fun <T> debug(msg: String = "") = ParDo.of<T, T>(WriterFn(msg, logger::debug))
    fun <T> info(msg: String = "") = ParDo.of<T, T>(WriterFn(msg, logger::info))
    fun <T> error(msg: String = "") = ParDo.of<T, T>(WriterFn(msg, logger::error))
  }

  private class WriterFn<T>(
    private val msg: String,
    private val fn: (msg: String) -> Unit) : DoFn<T, T>() {
    @ProcessElement
    fun process(context: ProcessContext) {
      val element = context.element()
      fn("$msg$element")
      context.output(element)
    }
  }

  private class LoggerImpl<in R : Any> : ReadOnlyProperty<R, Logger> {
    override fun getValue(thisRef: R, property: KProperty<*>) =
      getLogger(getClassForLogging(thisRef.javaClass))

    // getClassForLogging returns the javaClass or the enclosingClass if javaClass refers to a companion object.
    private fun <T : Any> getClassForLogging(javaClass: Class<T>): Class<*> {
      return javaClass.enclosingClass?.takeIf {
        it.kotlin.companionObject?.java == javaClass
      } ?: javaClass
    }

    private fun getLogger(forClass: Class<*>): Logger =
      LoggerFactory.getLogger(forClass)
  }
}
