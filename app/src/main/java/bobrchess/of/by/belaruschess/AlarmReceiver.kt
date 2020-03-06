package bobrchess.of.by.belaruschess

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import bobrchess.of.by.belaruschess.handler.BitmapHandler
import bobrchess.of.by.belaruschess.handler.IOHandler
import bobrchess.of.by.belaruschess.handler.NotificationHandler
import bobrchess.of.by.belaruschess.model.EventDate
import bobrchess.of.by.belaruschess.view.activity.impl.MainActivity
import bobrchess.of.by.belaruschess.model.EventTournament
import bobrchess.of.by.belaruschess.model.OneTimeEvent

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
       /* if (context != null) {
            //register IOHandler, really important, really
            IOHandler.registerIO(context)

            val event =
                IOHandler.convertStringToEventDate(
                    context,
                    intent!!.getStringExtra(MainActivity.FRAGMENT_EXTRA_TITLE_EVENTSTRING)
                )
            val notificationID =
                intent.getIntExtra(MainActivity.FRAGMENT_EXTRA_TITLE_NOTIFICATIONID, 0)
            val eventID = intent.getIntExtra(MainActivity.FRAGMENT_EXTRA_TITLE_EVENTID, 0)
            event?.eventID = eventID

            when (event) {
                is EventTournament -> {
                    buildNotification(context, event, notificationID, eventID)
                }
                is AnnualEvent -> {
                    buildNotification(context, event, notificationID, eventID)
                }
                is OneTimeEvent -> {
                    buildNotification(context, event, notificationID, eventID)
                }
                else -> {

                }
            }

            //create new notification events for this event, except when its an onetime-event
            //currently all notifications for a specific event get cancelled before new notifications are created, this may be ineffective but its simple (sorry)
            if (event != null && event !is OneTimeEvent) {
                NotificationHandler.cancelNotification(context, event)
                NotificationHandler.scheduleNotification(context, event)
            }
        }*/
    }

    private fun buildNotification(
            context: Context,
            event: EventDate,
            notificationID: Int,
            eventID: Int
    ) {
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra(MainActivity.FRAGMENT_EXTRA_TITLE_EVENTID, eventID)
        intent.putExtra(MainActivity.FRAGMENT_EXTRA_TITLE_TYPE, MainActivity.FRAGMENT_TYPE_SHOW)
        intent.putExtra(MainActivity.FRAGMENT_EXTRA_TITLE_LOADALL, true)
        intent.action = notificationID.toString()
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(
                context,
                notificationID,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        //new channel ID system for android oreo and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //val name = context!!.getString(R.string.notification_channel_name)
            val channelName = context.getString(R.string.notification_channel_name)
            val descriptionText = context.getString(R.string.notification_channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel =
                NotificationChannel(NotificationHandler.CHANNEL_ID, channelName, importance).apply {
                    description = descriptionText
                }

            //setting the notification light
            when (event) {
                is EventTournament -> {
                    val lightColor = getLightColor(event, context)
                    if (lightColor != null) {
                        channel.enableLights(true)
                        channel.lightColor = lightColor
                    } else {
                        channel.enableLights(false)
                    }
                }
               /* is AnnualEvent -> {
                    val lightColor = getLightColor(event, context)
                    if (lightColor != null) {
                        channel.enableLights(true)
                        channel.lightColor = lightColor
                    } else {
                        channel.enableLights(false)
                    }
                }*/
                is OneTimeEvent -> {
                    val lightColor = getLightColor(event, context)
                    if (lightColor != null) {
                        channel.enableLights(true)
                        channel.lightColor = lightColor
                    } else {
                        channel.enableLights(false)
                    }
                }
            }
            // Register the channel with the system
            notificationManager.createNotificationChannel(channel)
        }

        //switch event type
        // EVENT BIRTHDAY
        when (event) {
            is EventTournament -> {
                var bitmap: Bitmap? = null

                if (event.imageUri != null) {
                    bitmap = BitmapHandler.getBitmapFromFile(context, eventID)
                    if (bitmap != null) {
                        bitmap = BitmapHandler.getCircularBitmap(bitmap, context.resources)
                    }
                }

                var defaults = Notification.DEFAULT_ALL

                val builder = NotificationCompat.Builder(context, NotificationHandler.CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_birthdaybuddy_icon_status_bar)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    // Set the intent that will fire when the user taps the notification
                    .setContentIntent(pendingIntent)
                    .setSubText(context.getText(R.string.event_type_birthday))
                    .setAutoCancel(true)
                    .setStyle(NotificationCompat.BigTextStyle())
                    .setContentText(buildEventBirthdayNotificationBodyText(context, event))
                //settings names according to their existence
                if (event.fullDescription != null) {
                    builder.setContentTitle(
                        context.getString(
                            R.string.notification_title_birthday,
                            event.fullDescription
                        )
                    )
                } else if (event.shortDescription != null) {
                    builder.setContentTitle(
                        context.getString(
                            R.string.notification_title_birthday,
                            "${event.name} ${event.shortDescription}"
                        )
                    )
                } else {
                    builder.setContentTitle(
                        context.getString(
                            R.string.notification_title_birthday,
                            event.name
                        )
                    )
                }

                if (bitmap != null) {
                    builder.setLargeIcon(bitmap)
                }

                if (!IOHandler.getBooleanFromKey(IOHandler.SharedPrefKeys.key_isNotificationVibrationOnBirthday)!!) {
                    defaults -= Notification.DEFAULT_VIBRATE
                }

                if (!IOHandler.getBooleanFromKey(IOHandler.SharedPrefKeys.key_isNotificationSoundOnBirthday)!!) {
                    defaults -= Notification.DEFAULT_SOUND
                }

                val lightColor = getLightColor(event, context)
                if (lightColor != null) {
                    defaults -= Notification.DEFAULT_LIGHTS
                    builder.setLights(lightColor, 500, 500)
                } else {
                    defaults -= Notification.DEFAULT_LIGHTS
                }

                builder.setDefaults(defaults)

                with(notificationManager) {
                    notify(notificationID, builder.build())
                }
            }
            // ANNUAL EVENT
           /* is AnnualEvent -> {

                var defaults = Notification.DEFAULT_ALL

                val builder = NotificationCompat.Builder(context, NotificationHandler.CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_birthdaybuddy_icon_status_bar)
                    .setContentTitle(
                        context.getString(
                            R.string.notification_title_annual_event,
                            event.name
                        )
                    )
                    .setStyle(NotificationCompat.BigTextStyle())
                    .setContentText(buildAnnualEventNotificationBodyText(context, event))
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    // Set the intent that will fire when the user taps the notification
                    .setContentIntent(pendingIntent)
                    .setSubText(context.getText(R.string.event_type_annual_event))
                    .setAutoCancel(true)

                if (!IOHandler.getBooleanFromKey(IOHandler.SharedPrefKeys.key_isNotificationVibrationOnAnnual)!!) {
                    defaults -= Notification.DEFAULT_VIBRATE
                }

                if (!IOHandler.getBooleanFromKey(IOHandler.SharedPrefKeys.key_isNotificationSoundOnAnnual)!!) {
                    defaults -= Notification.DEFAULT_SOUND
                }

                val lightColor = getLightColor(event, context)
                if (lightColor != null) {
                    defaults -= Notification.DEFAULT_LIGHTS
                    builder.setLights(lightColor, 500, 500)
                } else {
                    defaults -= Notification.DEFAULT_LIGHTS
                }

                builder.setDefaults(defaults)

                with(notificationManager) {
                    notify(notificationID, builder.build())
                }

                // ONE TIME EVENT
            }*/
            is OneTimeEvent -> {

                var defaults = Notification.DEFAULT_ALL

                val builder = NotificationCompat.Builder(context, NotificationHandler.CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_birthdaybuddy_icon_status_bar)
                    .setContentTitle(
                        context.getString(
                            R.string.notification_title_one_time_event,
                            event.name
                        )
                    )
                    .setStyle(NotificationCompat.BigTextStyle())
                    .setContentText(buildOneTimeEventNotificationBodyText(context, event))
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    // Set the intent that will fire when the user taps the notification
                    .setContentIntent(pendingIntent)
                    .setSubText(context.getText(R.string.event_type_one_time_event))
                    .setAutoCancel(true)

                if (!IOHandler.getBooleanFromKey(IOHandler.SharedPrefKeys.key_isNotificationVibrationOnOneTime)!!) {
                    defaults -= Notification.DEFAULT_VIBRATE
                }

                if (!IOHandler.getBooleanFromKey(IOHandler.SharedPrefKeys.key_isNotificationSoundOnOneTime)!!) {
                    defaults -= Notification.DEFAULT_SOUND
                }

                val lightColor = getLightColor(event, context)
                if (lightColor != null) {
                    defaults -= Notification.DEFAULT_LIGHTS
                    builder.setLights(lightColor, 500, 500)
                } else {
                    defaults -= Notification.DEFAULT_LIGHTS
                }

                builder.setDefaults(defaults)

                with(notificationManager) {
                    notify(notificationID, builder.build())
                }
            }
        }
    }

    private fun buildEventBirthdayNotificationBodyText(
            context: Context,
            tournament: EventTournament
    ): String {
        var returnString = ""
       /* when (tournament.getDaysUntil()) {
            //today
            0 -> {
                returnString = context.resources.getString(
                    R.string.notification_content_birthday_today,
                    tournament.getName()
                )
            }
            //tomorrow
            1 -> {
                returnString = context.resources.getString(
                    R.string.notification_content_birthday_tomorrow,
                    tournament.getName()
                )
            }
            else -> {
                returnString = context.resources.getString(
                    R.string.notification_content_birthday_future,
                    tournament.getName(),
                    tournament.getDaysUntil()
                )
            }
        }
        returnString += "\n${context.resources.getString(
                R.string.notification_content_birthday_years_old,
                tournament.getName(),
                tournament.getTurningAgeValue()
            )}"
        */
        return returnString
    }

    /*private fun buildAnnualEventNotificationBodyText(
        context: Context,
        annualEvent: AnnualEvent
    ): String {
        var returnString = ""
        when (annualEvent.getDaysUntil()) {
            //today
            0 -> {
                returnString += context.resources.getString(
                    R.string.notification_content_annual_event_today,
                    annualEvent.name
                )
            }
            //tomorrow
            1 -> {
                returnString += context.resources.getString(
                    R.string.notification_content_annual_event_tomorrow,
                    annualEvent.name
                )
            }
            else -> {
                returnString += context.resources.getString(
                    R.string.notification_content_annual_event_future,
                    annualEvent.name,
                    annualEvent.getDaysUntil()
                )
            }
        }
        if (annualEvent.hasStartYear) {
            returnString += "\n${context.resources.getQuantityString(
                R.plurals.annual_event_years,
                annualEvent.getXTimesSinceStarting(),
                annualEvent.getXTimesSinceStarting()
            )}"
        }
        return returnString
    }*/

    private fun buildOneTimeEventNotificationBodyText(
        context: Context,
        oneTimeEvent: OneTimeEvent
    ): String {
        var returnString = ""
        when (oneTimeEvent.getDaysUntil()) {
            //today
            0 -> {
                returnString += context.resources.getString(
                    R.string.notification_content_one_time_event_today,
                    oneTimeEvent.name
                )
            }
            //tomorrow
            1 -> {
                returnString += context.resources.getString(
                    R.string.notification_content_one_time_event_tomorrow,
                    oneTimeEvent.name
                )
            }
            else -> {
                returnString += context.resources.getString(
                    R.string.notification_content_one_time_event_future,
                    oneTimeEvent.name,
                    oneTimeEvent.getDaysUntil()
                )
            }
        }
        return returnString
    }

    private fun getLightColor(event: EventDate, context: Context): Int? {
        when (event) {
            is EventTournament -> {
                val lightValue =
                    IOHandler.getIntFromKey(IOHandler.SharedPrefKeys.key_notificationLightBirthday)!!
                return getLightARGBFromColorValue(lightValue, context)
            }
            /*is AnnualEvent -> {
                val lightValue =
                    IOHandler.getIntFromKey(IOHandler.SharedPrefKeys.key_notificationLightAnnual)!!
                return getLightARGBFromColorValue(lightValue, context)
            }*/
            is OneTimeEvent -> {
                val lightValue =
                    IOHandler.getIntFromKey(IOHandler.SharedPrefKeys.key_notificationLightOneTime)!!
                return getLightARGBFromColorValue(lightValue, context)
            }
            else -> {
                // no light
                return getLightARGBFromColorValue(0, context)
            }
        }
    }

    private fun getLightARGBFromColorValue(colorValue: Int, context: Context): Int? {
        when (colorValue) {
            0 -> {
                return null
            }
            1 -> {
                return ContextCompat.getColor(context, R.color.notification_light_white)
            }
            2 -> {
                return ContextCompat.getColor(context, R.color.notification_light_red)
            }
            3 -> {
                return ContextCompat.getColor(context, R.color.notification_light_green)
            }
            4 -> {
                return ContextCompat.getColor(context, R.color.notification_light_blue)
            }
            else -> {
                return null
            }
        }
    }
}