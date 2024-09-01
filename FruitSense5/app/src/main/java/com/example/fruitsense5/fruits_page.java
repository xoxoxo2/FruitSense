package com.example.fruitsense5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class fruits_page extends AppCompatActivity {

    ImageView back;

    //Main Content
    ImageView fruitImg1, fruitImg2, fruitImg3;
    TextView fruitTxt1, fruitTxt2, fruitTxt3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruits_page);

        back = findViewById(R.id.backImageView);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(fruits_page.this, home_page.class);
                startActivity(intent);
            }
        });

        //Main Content
        fruitImg1 = findViewById(R.id.imageView1);
        fruitImg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(fruits_page.this, fruit1_page.class);
                startActivity(intent);
            }
        });

        fruitTxt1 = findViewById(R.id.textView1);
        fruitTxt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(fruits_page.this, fruit1_page.class);
                startActivity(intent);
            }
        });

        fruitImg2 = findViewById(R.id.imageView2);
        fruitImg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(fruits_page.this, fruit2_page.class);
                startActivity(intent);
            }
        });

        fruitTxt2 = findViewById(R.id.textView2);
        fruitTxt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(fruits_page.this, fruit2_page.class);
                startActivity(intent);
            }
        });

        fruitImg3 = findViewById(R.id.imageView3);
        fruitImg3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(fruits_page.this, fruit3_page.class);
                startActivity(intent);
            }
        });

        fruitTxt3 = findViewById(R.id.textView3);
        fruitTxt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(fruits_page.this, fruit3_page.class);
                startActivity(intent);
            }
        });
    }
}