package com.openlysis.data.local

import androidx.room.TypeConverter
import java.net.URI
import java.net.URL
import java.time.Instant

/**
 * Type converters for Room database, enabling support for [Instant] and [URL] types.
 *
 * - [Instant] is stored as a [Long] (epoch millis).
 * - [URL] is stored as a [String].
 *
 * These converters are registered in [AppDatabase] via type converters.
 */
internal class Converters {
    /** Converts a [Long] timestamp to [Instant]. */
    @TypeConverter
    fun fromTimestamp(value: Long): Instant = Instant.ofEpochMilli(value)

    /** Converts an [Instant] to a [Long] timestamp. */
    @TypeConverter
    fun instantToTimestamp(instant: Instant): Long = instant.toEpochMilli()

    /** Converts a [String] to [URL]. */
    @TypeConverter
    fun fromStringToUrl(value: String): URL = URI(value).toURL()

    /** Converts a [URL] to [String]. */
    @TypeConverter
    fun urlToString(url: URL): String = url.toString()
}