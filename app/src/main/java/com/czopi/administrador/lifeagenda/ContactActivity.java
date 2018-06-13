package com.czopi.administrador.lifeagenda;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;


public class ContactActivity extends AppCompatActivity {

    TextInputLayout etlEMail, etlCEMail, etlSubject, etlBody;
    TextInputEditText etEMail, etCEMail, etSubject, etBody;
    TextView readme;
    Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(R.string.contact_us);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etlEMail = (TextInputLayout) findViewById(R.id.etlEMail);
        etlCEMail = (TextInputLayout) findViewById(R.id.etlCEMail);
        etlSubject = (TextInputLayout) findViewById(R.id.etlSubject);
        etlBody = (TextInputLayout) findViewById(R.id.etlBody);
        etEMail = (TextInputEditText) findViewById(R.id.etEMail);
        etCEMail = (TextInputEditText) findViewById(R.id.etCEMail);
        etSubject = (TextInputEditText) findViewById(R.id.etSubject);
        etBody = (TextInputEditText) findViewById(R.id.etBody);

        readme = (TextView) findViewById(R.id.readme);
        String txt = readme.getText().toString();
        SpannableString ss = new SpannableString(txt);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(ContactActivity.this, FAQActivity.class));
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        if (Locale.getDefault().getLanguage().equals("es")) {
            ss.setSpan(clickableSpan, 57, 61, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }  else {
            ss.setSpan(clickableSpan, 60, 65, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        readme.setText(ss);
        readme.setMovementMethod(LinkMovementMethod.getInstance());
        readme.setHighlightColor(Color.TRANSPARENT);
        btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isETEmpty(etlEMail, etEMail) || isETEmpty(etlCEMail, etCEMail) || isETEmpty(etlSubject, etSubject) || isETEmpty(etlBody, etBody)) {
                    return;
                }
                if (!isValidEmail(etEMail.getText().toString())) {
                    etlEMail.setErrorEnabled(true);
                    etlEMail.setError("E-Mail address is not valid");
                    return;
                } else {
                    etlEMail.setErrorEnabled(false);
                }
                if (!isValidEmail(etCEMail.getText().toString())) {
                    etlCEMail.setErrorEnabled(true);
                    etlCEMail.setError("E-Mail address is not valid");
                    return;
                } else {
                    etlCEMail.setErrorEnabled(false);
                }
                if (!etEMail.getText().toString().equals(etCEMail.getText().toString())) {
                    etlCEMail.setErrorEnabled(true);
                    etlCEMail.setError("E-Mail addresses are not identical.");
                    return;
                } else {
                    etlCEMail.setErrorEnabled(false);
                }
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"czop.developer@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, etSubject.getText().toString());
                i.putExtra(Intent.EXTRA_TEXT   , etBody.getText().toString());
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(ContactActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public static boolean isValidEmail(CharSequence target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public boolean isETEmpty (TextInputLayout etl, TextInputEditText et) {
        if (et.getText().toString().equals("")) {
            etl.setErrorEnabled(true);
            etl.setError("This field can't be empty");
            return true;
        } else {
            etl.setErrorEnabled(false);
            return false;
        }
    }
}
