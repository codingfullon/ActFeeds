package com.example.kavitapc.fitnessreminder;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ContactUS extends AppCompatActivity {
    String textData;
    EditText mailText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
         mailText = findViewById(R.id.editTextEmailBody);
    }

    public void sendEmail(View view){
        textData = mailText.getText().toString();

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto","actfeeds@outlook.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Mail from ActFeed");
        emailIntent.putExtra(Intent.EXTRA_TEXT, textData);
        startActivity(Intent.createChooser(emailIntent, "Send email..."));

    }
}
