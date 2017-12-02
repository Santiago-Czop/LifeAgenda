package com.czopi.administrador.lifeagenda;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FAQActivity extends AppCompatActivity {

    Toolbar myToolbar;
    List<String> FAQListParent, FAQListChild;
    ExpandableListView eListView;
    MyFAQAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(R.string.FAQ);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FAQListParent = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.FAQQuestions)));
        FAQListChild = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.FAQAnswers)));

        eListView = (ExpandableListView) findViewById(R.id.eListView);
        myAdapter = new MyFAQAdapter(this, FAQListParent, FAQListChild);
        eListView.setAdapter(myAdapter);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            eListView.setIndicatorBounds(width - GetPixelFromDips(50), width - GetPixelFromDips(10));
        } else {
            eListView.setIndicatorBoundsRelative(width - GetPixelFromDips(50), width - GetPixelFromDips(10));
        }

    }

    public int GetPixelFromDips(float pixels) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (pixels * scale + 0.5f);
    }
}
