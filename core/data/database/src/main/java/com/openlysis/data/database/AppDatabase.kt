package com.openlysis.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.openlysis.data.database.dao.EmailMultiReputationDao
import com.openlysis.data.database.dao.EmailReputationDao
import com.openlysis.data.database.dao.FileAnalysisDao
import com.openlysis.data.database.dao.FileMultiAnalysisDao
import com.openlysis.data.database.dao.MessageAnalysisDao
import com.openlysis.data.database.dao.PhoneMultiReputationDao
import com.openlysis.data.database.dao.PhoneReputationDao
import com.openlysis.data.database.dao.UrlAnalysisDao
import com.openlysis.data.database.dao.UrlMultiAnalysisDao
import com.openlysis.data.database.entity.analysis.FileAnalysisEntity
import com.openlysis.data.database.entity.analysis.FileMultiAnalysisEntity
import com.openlysis.data.database.entity.analysis.UrlAnalysisEntity
import com.openlysis.data.database.entity.analysis.UrlMultiAnalysisEntity
import com.openlysis.data.database.entity.message.MessageAnalysisEntity
import com.openlysis.data.database.entity.reputation.EmailReputationEntity
import com.openlysis.data.database.entity.reputation.MultiReputationEntity
import com.openlysis.data.database.entity.reputation.PhoneReputationEntity
import com.openlysis.data.local.BuildConfig

/**
 * The Room database for the application, containing all local entities and DAOs.
 *
 * Entities:
 * - [UrlAnalysisEntity], [FileAnalysisEntity], [UrlMultiAnalysisEntity], [FileMultiAnalysisEntity]
 * - [EmailReputationEntity], [PhoneReputationEntity], [MultiReputationEntity], [MessageAnalysisEntity]
 *
 * DAOs:
 * - [UrlAnalysisDao], [FileAnalysisDao], [UrlMultiAnalysisDao], [FileMultiAnalysisDao]
 * - [EmailReputationDao], [PhoneReputationDao], [EmailMultiReputationDao], [PhoneMultiReputationDao], [MessageAnalysisDao]
 *
 * Uses [Converters] for custom type conversions.
 *
 * @see RoomDatabase
 * @see TypeConverters
 */
@Database(
    entities = [
        UrlAnalysisEntity::class,
        FileAnalysisEntity::class,
        UrlMultiAnalysisEntity::class,
        FileMultiAnalysisEntity::class,
        EmailReputationEntity::class,
        PhoneReputationEntity::class,
        MultiReputationEntity::class,
        MessageAnalysisEntity::class
    ],
    version = BuildConfig.DB_VERSION,
    exportSchema = true
)
@TypeConverters(Converters::class)
internal abstract class AppDatabase : RoomDatabase() {
    /** Returns the [UrlAnalysisDao] for URL analysis operations. */
    abstract fun urlAnalysisDao(): UrlAnalysisDao

    /** Returns the [FileAnalysisDao] for file analysis operations. */
    abstract fun fileAnalysisDao(): FileAnalysisDao

    /** Returns the [UrlMultiAnalysisDao] for URL multi-analysis operations. */
    abstract fun urlMultiAnalysisDao(): UrlMultiAnalysisDao

    /** Returns the [FileMultiAnalysisDao] for file multi-analysis operations. */
    abstract fun fileMultiAnalysisDao(): FileMultiAnalysisDao

    /** Returns the [EmailReputationDao] for email reputation operations. */
    abstract fun emailReputationDao(): EmailReputationDao

    /** Returns the [PhoneReputationDao] for phone reputation operations. */
    abstract fun phoneReputationDao(): PhoneReputationDao

    /** Returns the [EmailMultiReputationDao] for email multi-reputation operations. */
    abstract fun emailMultiReputationDao(): EmailMultiReputationDao

    /** Returns the [PhoneMultiReputationDao] for phone multi-reputation operations. */
    abstract fun phoneMultiReputationDao(): PhoneMultiReputationDao

    /** Returns the [MessageAnalysisDao] for message analysis operations. */
    abstract fun messageAnalysisDao(): MessageAnalysisDao
}