package com.example.myname;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Objects;

import static android.view.View.OnClickListener;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    private static final int REQUEST_IMAGE_CAPTURE = 101;
    Button btn_takePicture;
    //aici am declarat var pentru searcj in google
    Button mButton;
    EditText mEdit;
    //var pentru notificare
    Button btNotification;
    //pentru setari
    Button bnSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //CAMERAAAAAAAAAAA
        imageView = findViewById(R.id.imageView);
        btn_takePicture = findViewById(R.id.btnTakePicture);


        //search in google
        mButton = (Button) findViewById(R.id.button);
        mEdit = (EditText) findViewById(R.id.searchInGoogle);

        mButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                String searchIn = mEdit.getText().toString();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=" + searchIn));
                startActivity(browserIntent);
            }
        });

        createNotificationChannel();
        btNotification = findViewById(R.id.bt_notification);


        btNotification.setOnClickListener(view -> {
            //display on the top of the main activity and visible for a short time period
            // Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, ReminderBroadcast.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            long timeButtonClick = System.currentTimeMillis();
            long tenSecondsInMillis = 1000 * 10;

            alarmManager.set(AlarmManager.RTC_WAKEUP, timeButtonClick + tenSecondsInMillis,
                    pendingIntent);
        });

        //setari
        bnSettings = findViewById(R.id.open_settings);
        bnSettings.setOnClickListener(view -> {
            Intent dialogIntent = new Intent(android.provider.Settings.ACTION_SETTINGS);
            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(dialogIntent);

        });



        btn_takePicture.setOnClickListener(view -> {
            Intent imageTakeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if (imageTakeIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(imageTakeIntent, REQUEST_IMAGE_CAPTURE);
            }
        });


    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "ReminderChannel";
            String description = "Channel for notify me";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifyMe", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }


    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }
}

