package com.czopi.administrador.lifeagenda;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.LocalDate;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class NewTaskActivity extends AppCompatActivity {

    TextInputEditText etTitle, etDD, etMM, etYYYY, etDescription;
    TextInputLayout etlTitle, etlDD, etlMM, etlYYYY, etlDescription;
    TextView progressText;
    SeekBar seekBar;
    Button btSave, btDiscard;
    List<Task> taskArrayList;
    int idCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JodaTimeAndroid.init(this);
        setContentView(R.layout.activity_new_task);

        Toolbar myToolbar = (Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(R.string.new_task);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressText = (TextView)findViewById(R.id.progressText);

        //Code for TextView Title
        etlTitle = (TextInputLayout)findViewById(R.id.etlTitle);
        etTitle = (TextInputEditText)findViewById(R.id.etTitle);

        TextWatcher twTitle = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isTitleValid();
                if (!(isEtEmpty(etMM) && isEtEmpty(etDD) && isEtEmpty(etYYYY))) {
                    enableSaveButton();
                }

            }
        };
        etTitle.addTextChangedListener(twTitle);

        //Code for EditText DD
        etlDD = (TextInputLayout)findViewById(R.id.etlDD);
        etDD = (TextInputEditText)findViewById(R.id.etDD);

        TextWatcher twDD = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isEtEmpty(etDD)) {
                    etlDD.setErrorEnabled(true);
                    etlDD.setError("Required field");
                } else {
                    etlDD.setErrorEnabled(false);
                    etlDD.setError(null);
                    if (!(isEtEmpty(etMM) && isEtEmpty(etDD))) {
                        if (isDateValid() && !isEtEmpty(etTitle)) {
                            enableSaveButton();
                        }
                    }
                }
            }
        };
        etDD.addTextChangedListener(twDD);

        //Code for EditText MM
        etlMM = (TextInputLayout)findViewById(R.id.etlMM);
        etMM = (TextInputEditText)findViewById(R.id.etMM);

        TextWatcher twMM = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isEtEmpty(etMM)) {
                    etlMM.setErrorEnabled(true);
                    etlMM.setError("Required field");
                } else {
                    etlMM.setErrorEnabled(false);
                    etlMM.setError(null);
                    if (!(isEtEmpty(etYYYY) && isEtEmpty(etDD))) {
                        if (isDateValid() && !isEtEmpty(etTitle)) {
                            enableSaveButton();
                        }
                    }
                }
            }
        };
        etMM.addTextChangedListener(twMM);

        //Code for EditText YYYY
        etlYYYY = (TextInputLayout)findViewById(R.id.etlYYYY);
        etYYYY = (TextInputEditText)findViewById(R.id.etYYYY);

        TextWatcher twYYYY = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isEtEmpty(etYYYY)) {
                    etlYYYY.setErrorEnabled(true);
                    etlYYYY.setError("Required field");
                } else {
                    etlYYYY.setErrorEnabled(false);
                    etlYYYY.setError(null);
                    if (!(isEtEmpty(etDD) && isEtEmpty(etMM))) {
                        if (isDateValid() && !isEtEmpty(etTitle)) {
                            enableSaveButton();
                        }
                    }
                }
            }
        };
        etYYYY.addTextChangedListener(twYYYY);

        etlDescription = (TextInputLayout)findViewById(R.id.etlDescription);
        etDescription = (TextInputEditText)findViewById(R.id.etDescription);
        seekBar = (SeekBar)findViewById(R.id.seekBar);
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

            }
        });

        //Code for Save Button
        btSave = (Button)findViewById(R.id.btSave);
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enableSaveButton()) {
                    String pTitle = etTitle.getText().toString();
                    String etDDText = etDD.getText().toString();
                    String etMMText = etMM.getText().toString();
                    String etYYYYText = etYYYY.getText().toString();
                    int dd = Integer.parseInt(etDDText);
                    int mm = Integer.parseInt(etMMText);
                    int yyyy = Integer.parseInt(etYYYYText);
                    LocalDate pDueDate = new LocalDate(yyyy, mm, dd);
                    LocalDate pCreationDate = new LocalDate();
                    int pPriority = seekBar.getProgress();
                    String pDescription = etDescription.getText().toString();
                    int pId = idCounter;
                    idCounter++;

                    taskArrayList.add(new Task(pId, pTitle, pDueDate, pCreationDate, pPriority, pDescription));

                    GsonBuilder builder = new GsonBuilder()
                            .registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
                    Gson gson = builder.create();

                    SharedPreferences prefs = getSharedPreferences(
                            "com.czopi.administrador.lifeagenda", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    String json = gson.toJson(taskArrayList);
                    editor.putString("taskList", json);
                    editor.putInt("idCounter", idCounter);
                    editor.apply();

                    finish();

                }
            }
        });

        //Code for Discard Button
        btDiscard = (Button)findViewById(R.id.btDiscard);
        btDiscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        GsonBuilder builder = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        Gson gson = builder.create();

        SharedPreferences prefs = getSharedPreferences(
                "com.czopi.administrador.lifeagenda", Context.MODE_PRIVATE);
        String json = prefs.getString("taskList", null);
        if (json == null) {
            taskArrayList = new ArrayList<>();
        } else {
            Type type = new TypeToken<ArrayList<Task>>(){}.getType();
            taskArrayList = gson.fromJson(json, type);
        }
        idCounter = prefs.getInt("idCounter", 0);

    }

    public boolean isEtEmpty(EditText et) {
        String etText = et.getText().toString();
        return etText.equals("");
    }

    //Checks for Date validity
    public boolean isDateValid() {
        if (isEtEmpty(etDD) || isEtEmpty(etMM) || isEtEmpty(etYYYY)) {
            return false;
        }
        String etDDText = etDD.getText().toString();
        String etMMText = etMM.getText().toString();
        String etYYYYText = etYYYY.getText().toString();
        int dd = Integer.parseInt(etDDText);
        int mm = Integer.parseInt(etMMText);
        int yyyy = Integer.parseInt(etYYYYText);
        try {
            LocalDate currentDate = new LocalDate();
            LocalDate enteredDate = new LocalDate(yyyy, mm, dd);
            if (enteredDate.isBefore(currentDate) ) {
                etlDD.setErrorEnabled(true);
                etlDD.setError("Invalid Date");
                return false;
            }
        } catch (IllegalArgumentException iae) {
            etlDD.setErrorEnabled(true);
            etlDD.setError("Invalid Date");
            return false;
        }
        etlDD.setErrorEnabled(false);
        etlDD.setError(null);
        return true;
    }

    public boolean isTitleValid() {
        int etlTitleMaxChar = etlTitle.getCounterMaxLength();
        String etTitleText = etTitle.getText().toString();
        int etTitleLength = etTitleText.length();
        if(etTitleLength > etlTitleMaxChar) {
            etlTitle.setErrorEnabled(true);
            etlTitle.setError("Title is too long");
            return false;
        } else if (etTitleText.equals("")) {
            etlTitle.setErrorEnabled(true);
            etlTitle.setError("Title can not be empty");
            return false;
        } else {
            etlTitle.setErrorEnabled(false);
            etlTitle.setError(null);
            return true;
        }
    }

    //Checks if everything is alright to continue
    public boolean enableSaveButton() {
        if (isTitleValid()) {
            if (isDateValid()) {
                btSave.setEnabled(true);
                btSave.setTextColor(getResources().getColor(R.color.transparent87));
                return true;
            }
        }
        btSave.setEnabled(false);
        btSave.setTextColor(getResources().getColor(R.color.transparent26));
        return false;
    }

}