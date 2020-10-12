package com.spurtli.triathlon.testUtils

import org.apache.beam.sdk.schemas.Schema

class Id {
  companion object {
    val schema: Schema = Schema.builder()
      .addInt32Field("id")
      .build()
  }
}
