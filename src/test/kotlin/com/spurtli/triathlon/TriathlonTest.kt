package com.spurtli.triathlon

import org.junit.Assert.assertEquals
import org.junit.Test

class TriathlonTest {
  @Test fun testVersion() {
    assertEquals("1.0.0", Triathlon.version)
  }
}
