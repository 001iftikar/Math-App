package com.example.mathapp.di

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.net.toUri
import com.example.mathapp.MainActivity
import com.example.mathapp.R
import com.example.mathapp.utils.ServiceConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

@Module
@InstallIn(ServiceComponent::class)
object NotificationModule {

    @ServiceScoped
    @Provides
    fun provideNotificationBuilder(@ApplicationContext context: Context): NotificationCompat.Builder {
        val flag = PendingIntent.FLAG_IMMUTABLE
        val clickIntent = Intent(
            Intent.ACTION_VIEW,
            ServiceConstants.STUDY_SESSION_DEEP_LINK.toUri(),
            context,
            MainActivity::class.java
        )

        val parentIntent = Intent(
            context,
            MainActivity::class.java
        ).apply {
            action = Intent.ACTION_VIEW
            data = "yourapp://studysmart".toUri()
        }

        val clickPendingIntent: PendingIntent = TaskStackBuilder
            .create(context).run {
                addNextIntentWithParentStack(parentIntent)
                addNextIntent(clickIntent)
                getPendingIntent(1, flag) as PendingIntent
            }
        return NotificationCompat.Builder(
            context,
            ServiceConstants.NOTIFICATION_CHANNEL_ID
        )
            .setContentTitle("Study Session")
            .setContentText("00:00:00")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(true)
            .setContentIntent(clickPendingIntent)
    }
    @ServiceScoped
    @Provides
    fun provideNotificationManager(@ApplicationContext context: Context): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

}