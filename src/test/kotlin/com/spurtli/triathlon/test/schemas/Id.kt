package com.spurtli.triathlon.test.schemas

import org.apache.beam.sdk.schemas.Schema

class Id {
  companion object {
    val schema: Schema = Schema.builder()
      .addInt32Field("id")
      .build()
  }
}
