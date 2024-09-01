package com.example.fruitsense5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class articles_page extends AppCompatActivity {

    ImageView back;

    //Main Content
    ImageView articleImg1, articleImg2, articleImg3, articleImg4, articleImg5, articleImg6;
    TextView articleTxt1, articleTxt2, articleTxt3, articleTxt4, articleTxt5, articleTxt6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles_page);

        back = findViewById(R.id.backImageView);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(articles_page.this, home_page.class);
                startActivity(intent);
            }
        });


        //Main Content
        articleImg1 = findViewById(R.id.imageView1);
        articleImg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(articles_page.this, article1_page.class);
                startActivity(intent);
            }
        });

        articleTxt1 = findViewById(R.id.textView1);
        articleTxt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(articles_page.this, article1_page.class);
                startActivity(intent);
            }
        });

        articleImg2 = findViewById(R.id.imageView2);
        articleImg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(articles_page.this, article2_page.class);
                startActivity(intent);
            }
        });

        articleTxt2 = findViewById(R.id.textView2);
        articleTxt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(articles_page.this, article2_page.class);
                startActivity(intent);
            }
        });

        articleImg3 = findViewById(R.id.imageView3);
        articleImg3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(articles_page.this, article3_page.class);
                startActivity(intent);
            }
        });

        articleTxt3 = findViewById(R.id.textView3);
        articleTxt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(articles_page.this, article3_page.class);
                startActivity(intent);
            }
        });

        articleImg4 = findViewById(R.id.imageView4);
        articleImg4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(articles_page.this, article4_page.class);
                startActivity(intent);
            }
        });

        articleTxt4 = findViewById(R.id.textView4);
        articleTxt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(articles_page.this, article4_page.class);
                startActivity(intent);
            }
        });

        articleImg5 = findViewById(R.id.imageView5);
        articleImg5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(articles_page.this, article5_page.class);
                startActivity(intent);
            }
        });

        articleTxt5 = findViewById(R.id.textView5);
        articleTxt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(articles_page.this, article5_page.class);
                startActivity(intent);
            }
        });

        articleImg6 = findViewById(R.id.imageView6);
        articleImg6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(articles_page.this, article6_page.class);
                startActivity(intent);
            }
        });

        articleTxt6 = findViewById(R.id.textView6);
        articleTxt6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(articles_page.this, article6_page.class);
                startActivity(intent);
            }
        });
    }
}