package com.example.map2

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
class NotifService : Service() {
    override fun onBind(intent: Intent?): IBinder?
    {
        TODO("not implemented")
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int):Int {
        try {
            /**********************************notifier la personne dans ou il est dans une zone à risque*********************************************************/
            Log.d ("Sonthing", "Je suis dans le service de notification ")
            val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setContentTitle("منطقة خطرة") // title for notification
                .setContentText( "حذاري انت في بلدية فيها اشخاص مصابين,\n الوقاية خير من العلاج  ")// message for notification
                .setSmallIcon(R.drawable.logo_app) //small icon for notification
                .setAutoCancel(true)
                .setContentIntent(buttumnav.instancee!!.getPending())
                .build()
            startForeground(1, notification)  // Services d’Arrière-Plan
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
            {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    "منطقة خطرة ",
                    NotificationManager.IMPORTANCE_DEFAULT)
                channel.description = "حذاري انت في بلدية فيها اشخاص مصابين,\n الوقاية خير من العلاج  "
                channel.enableLights(true)
                channel.lightColor=Color.YELLOW
                channel.enableVibration(true)
                manager.createNotificationChannel(channel)
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        return START_STICKY
    }
    override fun onDestroy()
    {
        super.onDestroy()
    }
    companion object {
        const val CHANNEL_ID = "MyNotificationSmsServiceChannel"
    }
}