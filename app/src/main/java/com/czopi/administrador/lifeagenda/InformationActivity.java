package com.czopi.administrador.lifeagenda;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import org.joda.time.LocalDate;

import java.util.Locale;

public class InformationActivity extends AppCompatActivity {

    TextView tvVersion, tvCopyright;
    Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(R.string.information_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvVersion = (TextView)findViewById(R.id.tvVersion);
        String version;
        if (Locale.getDefault().getLanguage().equals("es")){
            version = "Versión ";
        } else {
            version = "Version ";
        }
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            version += pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        tvVersion.setText(version);

        tvCopyright = (TextView) findViewById(R.id.tvCopyright);
        LocalDate date = new LocalDate();
        String tvCopyrightText = "© 2017";
        if (date.getYear() != 2017) {
            tvCopyrightText = tvCopyrightText + "-" + String.valueOf(date.getYear());
        }
        tvCopyrightText += " Santiago Czop";
        if (Locale.getDefault().getLanguage().equals("es")){
            tvCopyrightText += "\nTodos los derechos reservados";
        } else {
            tvCopyrightText += "\nAll rights reserved";
        }
        tvCopyright.setText(tvCopyrightText);


    }

}
