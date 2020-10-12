package com.spurtli.triathlon

import org.apache.beam.sdk.transforms.WithKeys
import java.util.concurrent.ThreadLocalRandom

class Shard {
  companion object {
    /**
     * Splits up elements into [shards]. This can be useful if you need to
     * randomly distribute elements across multiple workers and when you cannot
     * rely on the size of bundles. The transform does not group elements by
     * shards, but instead packs them into a [pair][org.apache.beam.sdk.values.KV]
     * where the key represents the shard ID and the value is the input element.
     *
     * You can use the returned collection as input for the
     * [transform][org.apache.beam.sdk.transforms.GroupIntoBatches].
     *
     * @param shards The number of shards the input should be split up.
     * @return The sharded element.
     */
    fun <T> assign(shards: Int) = WithKeys.of<Int, T> { ThreadLocalRandom.current().nextInt(shards) }
  }
}
