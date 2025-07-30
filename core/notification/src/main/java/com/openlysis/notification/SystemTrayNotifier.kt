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
import android.telephony.SmsMessage
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import com.openlysis.core.designsystem.icon.AppIconsIds
import com.openlysis.notification.constant.Notifications
import com.openlysis.notification.constant.SmsAnalysis
import com.openlysis.notification.receiver.AnalyzableSmsReceiver
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
        override fun notifyAnalyzableSms(
            sms: SmsMessage,
            smsFormat: String
        ) = with(context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                return
            }

            createSmsNotificationChannel()

            val notificationId = Random.nextInt(from = 1, until = Int.MAX_VALUE)
            val notification = createSmsNotification(notificationId, sms, smsFormat)
            NotificationManagerCompat.from(this).notify(notificationId, notification)
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
            notificationId: Int,
            sms: SmsMessage,
            smsFormat: String
        ): Notification {
            val analyzeIntent =
                smsAnalysisIntent(
                    notificationId,
                    SmsAnalysis.SubAction.Analyze,
                    sms,
                    smsFormat
                )
            val cancelIntent =
                smsAnalysisIntent(
                    notificationId,
                    SmsAnalysis.SubAction.Cancel,
                    sms,
                    smsFormat
                )
            val content =
                getString(
                    R.string.notifications_sms_analyze_content,
                    sms.displayOriginatingAddress
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
                    analyzeIntent
                ).addAction(
                    AppIconsIds.Cross,
                    getString(R.string.notifications_sms_analyze_cancel_action_label),
                    cancelIntent
                ).setAutoCancel(true)
                .build()
        }

        private fun Context.smsAnalysisIntent(
            notificationId: Int,
            subAction: SmsAnalysis.SubAction,
            sms: SmsMessage,
            smsFormat: String
        ): PendingIntent {
            val intent =
                Intent(context, AnalyzableSmsReceiver::class.java).apply {
                    action = SmsAnalysis.SMS_ANALYSIS_AVAILABLE_INTENT
                    data =
                        Notifications.NOTIFICATION_DATA_URI_PLACEHOLDER
                            .format(
                                notificationId
                            ).toUri()
                            .normalizeScheme()
                    putExtra(SmsAnalysis.EXTRA_NOTIFICATION_ID, notificationId)
                    putExtra(SmsAnalysis.EXTRA_SUB_ACTION, subAction.toString())
                    putExtra(SmsAnalysis.EXTRA_SMS_PDU, sms.pdu)
                    putExtra(SmsAnalysis.EXTRA_SMS_FORMAT, smsFormat)
                }
            return PendingIntent.getBroadcast(
                this,
                subAction.ordinal,
                intent,
                PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
            )
        }
    }