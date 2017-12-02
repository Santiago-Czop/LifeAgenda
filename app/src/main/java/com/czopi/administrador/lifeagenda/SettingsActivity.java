package com.czopi.administrador.lifeagenda;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    boolean notifications;
    boolean vibration;
    TextView btn_faq, btn_contact, btn_info, progressText;
    SwitchCompat switch_ntf, switch_vbr;
    SeekBar seekBar;
    Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(R.string.settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressText = (TextView) findViewById(R.id.progressText);

        //Code for FAQActivity Button
        btn_faq = (TextView) findViewById(R.id.btn_faq);
        btn_faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent(view.getContext(), FAQActivity.class);
                startActivity(i1);
            }
        });

        //Code for Contact us Button
        btn_contact = (TextView) findViewById(R.id.btn_contact);
        btn_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i2 = new Intent(view.getContext(), ContactActivity.class);
                startActivity(i2);
            }
        });

        //Code for App's Information Button
        btn_info = (TextView) findViewById(R.id.btn_info);
        btn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i3 = new Intent(view.getContext(), InformationActivity.class);
                startActivity(i3);
            }
        });

        //Code for Notification Switch
        switch_ntf = (SwitchCompat) findViewById(R.id.switch_ntf);
        switch_ntf.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences prefs = getSharedPreferences(
                        "com.czopi.administrador.lifeagenda", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("ntf_boolean", isChecked);
                editor.apply();
            }
        });

        //Code for Vibration Switch
        switch_vbr = (SwitchCompat) findViewById(R.id.switch_vbr);
        switch_vbr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences prefs = getSharedPreferences(
                        "com.czopi.administrador.lifeagenda", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("vbr_boolean", isChecked);
                editor.apply();
            }
        });

        //Code Priority SeekBar
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String progressString = String.valueOf(progress + 1);
                progressText.setText(progressString);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress() + 1;
                SharedPreferences prefs = getSharedPreferences(
                        "com.czopi.administrador.lifeagenda", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("minPriority", progress);
                editor.apply();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = getSharedPreferences(
                "com.czopi.administrador.lifeagenda", Context.MODE_PRIVATE);
        notifications = prefs.getBoolean("ntf_boolean", true);
        vibration = prefs.getBoolean("vbr_boolean", false);
        switch_ntf.setChecked(notifications);
        switch_vbr.setChecked(vibration);

    }
}
