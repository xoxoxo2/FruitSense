package com.example.fruitsense5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Locale;

public class changelanguage_page extends AppCompatActivity {

    ImageView back;

    private RadioGroup radioGroupLanguages;
    private Button buttonSaveLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changelanguage_page);

        back = findViewById(R.id.backImageView);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(changelanguage_page.this, articles_page.class);
                startActivity(intent);
            }
        });

        radioGroupLanguages = findViewById(R.id.radioGroupLanguages);
        buttonSaveLanguage = findViewById(R.id.buttonSaveLanguage);

        buttonSaveLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveLanguageSelection();
            }
        });
    }

    private void saveLanguageSelection() {
        int selectedRadioButtonId = radioGroupLanguages.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);

        if (selectedRadioButton == null) {
            Toast.makeText(this, "Please select a language", Toast.LENGTH_SHORT).show();
            return;
        }

        String languageCode = "";

        // Compare the IDs directly
        if (selectedRadioButton.getId() == R.id.radioButtonEnglish) {
            languageCode = "en";
        } else if (selectedRadioButton.getId() == R.id.radioButtonMalay) {
            languageCode = "my";
        }

        // Save selected language in SharedPreferences
        SharedPreferences preferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("language", languageCode);
        editor.apply();

        // Apply language change
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.setLocale(locale);
        getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());

        // Redirect to profile_page with updated language
        Intent intent = new Intent(changelanguage_page.this, profile_page.class);
        startActivity(intent);
        finish(); // Finish current activity to prevent back button from returning here
    }
}