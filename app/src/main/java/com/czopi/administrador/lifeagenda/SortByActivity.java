package com.czopi.administrador.lifeagenda;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckedTextView;

public class SortByActivity extends AppCompatActivity {

    CheckedTextView CTVTitle, CTVDueDate, CTVCreationDate, CTVPriority;
    String checked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort_by);

        Toolbar myToolbar = (Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(R.string.sort_by);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CTVTitle = (CheckedTextView)findViewById(R.id.CTVTitle);
        CTVDueDate = (CheckedTextView)findViewById(R.id.CTVDueDate);
        CTVCreationDate = (CheckedTextView)findViewById(R.id.CTVCreationDate);
        CTVPriority = (CheckedTextView)findViewById(R.id.CTVPriority);

        CTVTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CTVTitle.isChecked()) {
                    checked = "CTVTitle";
                    CTVTitle.toggle();
                    CTVDueDate.setChecked(false);
                    CTVCreationDate.setChecked(false);
                    CTVPriority.setChecked(false);
                }
            }
        });
        CTVDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CTVDueDate.isChecked()) {
                    checked = "CTVDueDate";
                    CTVDueDate.toggle();
                    CTVTitle.setChecked(false);
                    CTVCreationDate.setChecked(false);
                    CTVPriority.setChecked(false);
                }
            }
        });
        CTVCreationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CTVCreationDate.isChecked()) {
                    checked = "CTVCreationDate";
                    CTVCreationDate.toggle();
                    CTVDueDate.setChecked(false);
                    CTVTitle.setChecked(false);
                    CTVPriority.setChecked(false);
                }
            }
        });
        CTVPriority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CTVPriority.isChecked()) {
                    checked = "CTVPriority";
                    CTVPriority.toggle();
                    CTVDueDate.setChecked(false);
                    CTVCreationDate.setChecked(false);
                    CTVTitle.setChecked(false);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = getSharedPreferences(
                "com.czopi.administrador.lifeagenda", Context.MODE_PRIVATE);
        checked = prefs.getString("sortingMethod", "CTVDueDate");
        switch (checked) {
            case "CTVDueDate":
                CTVDueDate.toggle();
                break;
            case "CTVTitle":
                CTVTitle.toggle();
                break;
            case "CTVCreationDate":
                CTVCreationDate.toggle();
                break;
            case "CTVPriority":
                CTVPriority.toggle();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences prefs = getSharedPreferences(
                "com.czopi.administrador.lifeagenda", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("sortingMethod", checked);
        editor.apply();

    }
}
