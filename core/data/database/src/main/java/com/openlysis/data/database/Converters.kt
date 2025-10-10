package com.openlysis.data.database

import androidx.room.TypeConverter
import kotlinx.datetime.Instant
import java.net.URI

/**
 * Type converters for Room database, enabling support for [Instant] and [URI] types.
 *
 * - [Instant] is stored as a [Long] (epoch millis).
 * - [URI] is stored as a [String].
 *
 * These converters are registered in [AppDatabase] via type converters.
 */
internal class Converters {
    /** Converts a [Long] timestamp to [Instant]. */
    @TypeConverter
    fun fromTimestamp(value: Long): Instant = Instant.fromEpochMilliseconds(value)

    /** Converts an [Instant] to a [Long] timestamp. */
    @TypeConverter
    fun instantToTimestamp(instant: Instant): Long = instant.toEpochMilliseconds()

    /** Converts a [String] to [URI]. */
    @TypeConverter
    fun fromStringToUri(value: String): URI = URI(value)

    /** Converts a [URI] to [String]. */
    @TypeConverter
    fun uriToString(uri: URI): String = uri.toString()
}