package com.inkeep.actfeeds;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.inkeep.actfeeds.R;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ContactUS extends AppCompatActivity {
    String textData;
    EditText mailText;
    String textSystemInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
         mailText = findViewById(R.id.editTextEmailBody);


    }

    public void sendEmail(View view){
        textData = mailText.getText().toString();
      



        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto","actfeeds@gmail.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Mail from ActFeed");
        emailIntent.putExtra(Intent.EXTRA_TEXT, textData+"\n\n\n\n\n"+textSystemInfo);
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }
    
    public void getSystemInfo(View view){
        int packageVersion=1;
        String packageVersionName="";
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            packageVersionName = packageInfo.versionName;
            packageVersion = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            //Handle exception
        }

        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM, dd yyyy HH:mm:ss z", Locale.ENGLISH);
        textSystemInfo =   "Time: "+DATE_FORMAT.format(Build.TIME)+"\n"+
                        "Package version name "+packageVersionName+"\n"+
                        "Package version "+packageVersion+"\n"+
                        "type: " + Build.TYPE+"\n"+
                        "user: " + Build.USER+"\n"+
                        "BASE: " + Build.VERSION_CODES.BASE+"\n"+
                        "INCREMENTAL " + Build.VERSION.INCREMENTAL+"\n"+
                        "SDK  " + Build.VERSION.SDK+"\n"+
                        "Version Code: " + Build.VERSION.RELEASE+"\n"+
                        "BRAND " + Build.BRAND+"\n"+
                        "SERIAL: " + Build.SERIAL+"\n"+
                        "MODEL: " + Build.MODEL+"\n"+
                        "ID: " + Build.ID+"\n"+
                        "Manufacture: " + Build.MANUFACTURER+"\n"+
                        "FINGERPRINT: "+Build.FINGERPRINT+"\n"+

                        "BOARD: " + Build.BOARD+"\n"+
                        "HOST " + Build.HOST+"\n"+

                        "class: "+Build.class+"\n"+
                        "package name: "+ getPackageName();
    }
}
