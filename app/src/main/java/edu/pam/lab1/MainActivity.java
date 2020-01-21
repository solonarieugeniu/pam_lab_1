package edu.pam.lab1;

import android.app.Notification;
import android.app.SearchManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static edu.pam.lab1.MainApp.CHANNEL_1_ID;

public class MainActivity extends AppCompatActivity {

    public static final String DEFAULT_INSERT_TEXT_MESSAGE = "Insert your text here";
    public static final String DEFAULT_NOTIFICATION_TITLE_MESSAGE = "Ion Antohi!";
    public static final String DEFAULT_NOTIFICATION_TEXT_MESSAGE = "Solonari, PAM exam is passed!";
    private static final int pic_id = 123;
    private NotificationManagerCompat notificationManagerCompat;
    private EditText editText;
    private EditText editTitle;
    private FloatingActionButton searchButtton;
    private ImageView imageFrame;
    private FloatingActionButton btnCamera;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                btnCamera.setEnabled(true);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageFrame = (ImageView) findViewById(R.id.imageView);
        searchButtton = findViewById(R.id.search_button);
        searchButtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick_searchButton();
            }
        });

        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        btnCamera = (FloatingActionButton) findViewById(R.id.camera_button);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                if (selectedId != -1) {
                    openCameraIntent(selectedId);
                }
            }
        });

        notificationManagerCompat = NotificationManagerCompat.from(this);

        editTitle = findViewById(R.id.notificationTitle);
        editText = findViewById(R.id.notificationText);
    }


    public void onClick_searchButton() {
        String searchFor = ((EditText) findViewById(R.id.searchBar)).getText().toString();
        Intent viewSearch = new Intent(Intent.ACTION_WEB_SEARCH);
        viewSearch.putExtra(SearchManager.QUERY, searchFor);

        if (searchFor.isEmpty() || searchFor.equals(DEFAULT_INSERT_TEXT_MESSAGE)) {
            ((EditText) findViewById(R.id.searchBar)).setHint(DEFAULT_INSERT_TEXT_MESSAGE);
        } else {
            startActivity(viewSearch);
        }
    }

    public void openCameraIntent(int cameraSelection) {
        Intent camera_intent
                = new Intent(MediaStore
                .ACTION_IMAGE_CAPTURE);
        if (cameraSelection == 0) {
            camera_intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);
        }

        startActivityForResult(camera_intent, pic_id);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == pic_id) {
            Bitmap photo = (Bitmap) data.getExtras()
                    .get("data");

            imageFrame.setImageBitmap(photo);
        }
    }

    public void sendNotification(View view) {
        String title = !editTitle.getText().toString().equals("") ? editTitle.getText().toString() : DEFAULT_NOTIFICATION_TITLE_MESSAGE;
        String text = !editText.getText().toString().equals("") ? editText.getText().toString() : DEFAULT_NOTIFICATION_TEXT_MESSAGE;

        final Notification notification = new Notification.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(text)
                .setCategory(NotificationCompat.CATEGORY_EVENT)
                .build();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                notificationManagerCompat.notify(1, notification);

            }
        }, 10000);
    }
}
