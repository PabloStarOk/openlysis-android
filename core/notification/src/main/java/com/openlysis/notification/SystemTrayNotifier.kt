package com.openlysis.notification

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import com.openlysis.core.designsystem.icon.AppIconsIds
import com.openlysis.data.analysis.model.analysis.AnalysisStatus
import com.openlysis.data.analysis.model.common.Verdict
import com.openlysis.data.analysis.model.message.Message
import com.openlysis.notification.constant.Notifications
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

/**
 * Notifier implementation that displays notifications in the system tray.
 *
 * @param context The application context, injected by Hilt.
 */
@Singleton
internal class SystemTrayNotifier
    @Inject
    constructor(
        @ApplicationContext private val context: Context
    ) : Notifier {
        private val completedAnalysisStringResources =
            mapOf(
                Verdict.Unknown to
                    Pair(
                        R.string.worker_notification_title_sms_analysis_completed_generic,
                        R.string.worker_notification_content_sms_analysis_completed_unknown
                    ),
                Verdict.Undetected to
                    Pair(
                        R.string.worker_notification_title_sms_analysis_completed_generic,
                        R.string.worker_notification_content_sms_analysis_completed_undetected
                    ),
                Verdict.Suspicious to
                    Pair(
                        R.string.worker_notification_title_sms_analysis_completed_suspicious,
                        R.string.worker_notification_content_sms_analysis_completed_suspicious
                    ),
                Verdict.Malicious to
                    Pair(
                        R.string.worker_notification_title_sms_analysis_completed_malicious,
                        R.string.worker_notification_content_sms_analysis_completed_malicious
                    )
            )

        override fun notifyAnalyzableSms(
            message: Message,
            notificationId: Int,
            analyzeIntent: Intent,
            cancelIntent: Intent,
            tapIntent: Intent
        ) = with(context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                return
            }

            createSmsNotificationChannel()

            val analyzePendingIntent =
                analyzeIntent.asUniquePending(
                    notificationId,
                    this,
                    ANALYZE_REQUEST_CODE
                )
            val cancelPendingIntent =
                cancelIntent.asUniquePending(
                    notificationId,
                    this,
                    CANCEL_REQUEST_CODE
                )
            val tapPendingIntent =
                PendingIntent.getActivity(
                    context,
                    CONTENT_TAP_REQUEST_CODE,
                    tapIntent,
                    PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
                )

            val notification =
                createSmsNotification(
                    message,
                    analyzePendingIntent,
                    cancelPendingIntent,
                    tapPendingIntent
                )
            NotificationManagerCompat.from(this).notify(notificationId, notification)
        }

        override fun notifyMessageAnalysisFinalization(
            messageSender: String,
            analysisStatus: AnalysisStatus,
            analysisVerdict: Verdict
        ) = with(context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                return
            }

            val notificationId = Random.nextInt()
            val notification =
                createMessageAnalysisNotification(
                    messageSender,
                    analysisStatus,
                    analysisVerdict
                )
            NotificationManagerCompat.from(this).notify(notificationId, notification)
        }

        override fun createMessageAnalysisNotification(
            messageSender: String,
            analysisStatus: AnalysisStatus,
            analysisVerdict: Verdict
        ): Notification {
            val isFinalStatus =
                analysisStatus != AnalysisStatus.Queued &&
                    analysisStatus != AnalysisStatus.InProgress

            val (contentTitle, contentText) =
                when (analysisStatus) {
                    AnalysisStatus.Completed -> {
                        val (titleResId, textResId) =
                            completedAnalysisStringResources.getValue(analysisVerdict)
                        val contentTitle = context.getString(titleResId)
                        val contentText = context.getString(textResId, messageSender)
                        Pair(contentTitle, contentText)
                    }
                    AnalysisStatus.Failed, AnalysisStatus.Timeout -> {
                        val contentTitle =
                            context.getString(
                                R.string.worker_notification_title_sms_analysis_failed
                            )

                        val contentText =
                            context.getString(
                                R.string.worker_notification_content_sms_analysis_failed
                            )
                        Pair(contentTitle, contentText)
                    }
                    AnalysisStatus.Queued, AnalysisStatus.InProgress -> {
                        val contentTitle =
                            context.getString(
                                R.string.worker_notification_title_sms_analysis_in_progress
                            )
                        val contentText =
                            context.getString(
                                R.string.worker_notification_content_sms_analysis_in_progress
                            )
                        Pair(contentTitle, contentText)
                    }
                }

            // TODO: Add action to retry in case of failure.
            return NotificationCompat
                .Builder(context, Notifications.SMS_ANALYSIS_NOTIFICATION_CHANNEL_ID)
                .apply {
                    setSmallIcon(AppIconsIds.Openlysis)
                    setPriority(NotificationCompat.PRIORITY_HIGH)

                    if (isFinalStatus) {
                        setAutoCancel(true)
                    } else {
                        setOngoing(true)
                        setProgress(0, 0, true)
                    }

                    setContentTitle(contentTitle)
                    setContentText(contentText)

                    if (analysisStatus == AnalysisStatus.Completed) {
                        setStyle(NotificationCompat.BigTextStyle().bigText(contentText))
                    }
                }.build()
        }

        private fun Context.createSmsNotificationChannel() {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                return
            }

            val channelName = getString(R.string.notifications_sms_analysis_channel_name)
            val descriptionText =
                getString(R.string.notifications_sms_analysis_channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel =
                NotificationChannel(
                    Notifications.SMS_ANALYSIS_NOTIFICATION_CHANNEL_ID,
                    channelName,
                    importance
                ).apply {
                    description = descriptionText
                }
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        private fun Context.createSmsNotification(
            message: Message,
            analyzePendingIntent: PendingIntent,
            cancelPendingIntent: PendingIntent,
            tapIntent: PendingIntent
        ): Notification {
            val content =
                getString(
                    R.string.notifications_sms_analyze_content,
                    message.sender
                )
            return NotificationCompat
                .Builder(context, Notifications.SMS_ANALYSIS_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(AppIconsIds.Openlysis)
                .setContentTitle(getString(R.string.notifications_sms_analyze_title))
                .setContentText(content)
                .setStyle(NotificationCompat.BigTextStyle().bigText(content))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .addAction(
                    AppIconsIds.Search,
                    getString(R.string.notifications_sms_analyze_primary_action_label),
                    analyzePendingIntent
                ).addAction(
                    AppIconsIds.Cross,
                    getString(R.string.notifications_sms_analyze_cancel_action_label),
                    cancelPendingIntent
                ).setContentIntent(tapIntent)
                .setAutoCancel(true)
                .build()
        }

        private fun Intent.asUniquePending(
            notificationId: Int,
            context: Context,
            requestCode: Int
        ): PendingIntent {
            data =
                Notifications.NOTIFICATION_DATA_URI_PLACEHOLDER
                    .format(notificationId)
                    .toUri()
                    .normalizeScheme()

            return PendingIntent.getBroadcast(
                context,
                requestCode,
                this,
                PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
            )
        }

        private companion object {
            const val ANALYZE_REQUEST_CODE = 0
            const val CANCEL_REQUEST_CODE = 1
            const val CONTENT_TAP_REQUEST_CODE = 2
        }
    }