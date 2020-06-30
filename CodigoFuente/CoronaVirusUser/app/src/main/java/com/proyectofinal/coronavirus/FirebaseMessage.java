package com.proyectofinal.coronavirus;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessage extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        if(remoteMessage.getData().size() > 0){
            mostrarNotificacion(remoteMessage.getData().get("title"),remoteMessage.getData().get("message"));
        }
        if(remoteMessage.getNotification()!=null){
            mostrarNotificacion(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
        }
    }

    private RemoteViews getNotificacionPersonalizada(String titulo, String mensaje){
        RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(),R.layout.notification);
        //remoteViews.setTextViewText(R.id.textViewTitulo, titulo);
        remoteViews.setTextViewText(R.id.textViewMensaje, mensaje);
        remoteViews.setImageViewResource(R.id.imageView1, R.drawable.flag_peru);
        remoteViews.setImageViewResource(R.id.imageView2, R.drawable.flag_peru);
        return remoteViews;
    }

    public void mostrarNotificacion(String titulo, String mensaje) {
        Intent intent=new Intent(this, MainActivity.class);
        String channel_id="coronavirus_channel";
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,intent,PendingIntent.FLAG_ONE_SHOT);
        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(getApplicationContext(),channel_id)
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setSound(uri)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000,1000,1000,1000,1000})
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN){
            builder=builder.setContent(getNotificacionPersonalizada(titulo,mensaje));
        }else{
            builder = builder.setContentTitle(titulo)
                    .setContentText(mensaje)
                    .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark);
        }
        NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel notificationChannel=new NotificationChannel(channel_id,"coronavirus",NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setSound(uri,null);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        notificationManager.notify(1,builder.build());
    }
}
