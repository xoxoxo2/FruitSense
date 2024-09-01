package com.example.fruitsense5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;

public class home_page extends AppCompatActivity {

    ///SEARCH VIEW
    SearchView searchView;
    RecyclerView recyclerView;
    ArrayList<ModelClass> arrayList = new ArrayList<>();
    ArrayList<ModelClass> searchList;
    String[] productList;

    int[] imgList = new int[]{R.drawable.fruit1_apple, R.drawable.fruit2_banana, R.drawable.fruit3_tomato, R.drawable.article1_single, R.drawable.article1_single, R.drawable.article3_single,
            R.drawable.article4_single, R.drawable.article5_single, R.drawable.article6_single};


    //Main Content
    Button articleBtn, fruitBtn;
    ImageView articleImg1, articleImg2, fruitImg1, fruitImg2;
    TextView articleTxt1, articleTxt2, fruitTxt1, fruitTxt2;

    //Menu Navigation
    ImageView home, camera, profile;
    TextView homeTxt, cameraTxt, profileTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);


        // Retrieve localized product names
        productList = new String[] {
                getString(R.string.apple),
                getString(R.string.banana),
                getString(R.string.tomato),
                getString(R.string.downside_of_overripe_fruits),
                getString(R.string.fruit_ripening_stages),
                getString(R.string.fruit_storage_hacks),
                getString(R.string.factors_affecting_fruit_quality),
                getString(R.string.nutritional_benefits_of_fresh_fruits),
                getString(R.string.global_trends_in_fruit_consumption)
        };

        //SEARCH VIEW
        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.searchView);

        //SEARCH VIEW
        for (int i = 0; i < productList.length ; i++) {
            ModelClass modelClass = new ModelClass();
            modelClass.setProductName(productList[i]);
            modelClass.setImg(imgList[i]);
            arrayList.add(modelClass);
        }

        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(home_page.this);
        recyclerView.setLayoutManager(layoutManager);

        ProductAdapter productAdapter = new ProductAdapter(home_page.this,arrayList);
        recyclerView.setAdapter(productAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchList = new ArrayList<>();

                if (query.length() > 0) {
                    for (int i = 0; i < arrayList.size() ; i++){
                        if (arrayList.get(i).getProductName().toUpperCase().contains(query.toUpperCase())) {
                            ModelClass modelClass = new ModelClass();
                            modelClass.setProductName(arrayList.get(i).getProductName());
                            modelClass.setImg(arrayList.get(i).getImg());
                            searchList.add(modelClass);
                        }
                    }

                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(home_page.this);
                    recyclerView.setLayoutManager(layoutManager);

                    ProductAdapter productAdapter = new ProductAdapter(home_page.this, searchList);
                    recyclerView.setAdapter(productAdapter);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList = new ArrayList<>();

                if (newText.length() > 0) {
                    for (int i = 0; i < arrayList.size() ; i++){
                        if (arrayList.get(i).getProductName().toUpperCase().contains(newText.toUpperCase())) {
                            ModelClass modelClass = new ModelClass();
                            modelClass.setProductName(arrayList.get(i).getProductName());
                            modelClass.setImg(arrayList.get(i).getImg());
                            searchList.add(modelClass);
                        }
                    }

                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(home_page.this);
                    recyclerView.setLayoutManager(layoutManager);

                    ProductAdapter productAdapter = new ProductAdapter(home_page.this, searchList);
                    recyclerView.setAdapter(productAdapter);
                }
                return false;
            }
        });

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    recyclerView.setVisibility(View.VISIBLE); // Show the RecyclerView
                } else {
                    recyclerView.setVisibility(View.GONE); // Hide the RecyclerView
                }
            }
        });

        //Main Content
        articleImg1 = findViewById(R.id.imageView1);
        articleImg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home_page.this, article1_page.class);
                startActivity(intent);
            }
        });

        articleTxt1 = findViewById(R.id.textView1);
        articleTxt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home_page.this, article1_page.class);
                startActivity(intent);
            }
        });

        articleImg2 = findViewById(R.id.imageView2);
        articleImg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home_page.this, article2_page.class);
                startActivity(intent);
            }
        });

        articleTxt2 = findViewById(R.id.textView2);
        articleTxt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home_page.this, article2_page.class);
                startActivity(intent);
            }
        });

        fruitImg1 = findViewById(R.id.imageView3);
        fruitImg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home_page.this, fruit1_page.class);
                startActivity(intent);
            }
        });

        fruitTxt1 = findViewById(R.id.textView5);
        fruitTxt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home_page.this, fruit1_page.class);
                startActivity(intent);
            }
        });

        fruitImg2 = findViewById(R.id.imageView4);
        fruitImg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home_page.this, fruit2_page.class);
                startActivity(intent);
            }
        });

        fruitTxt2 = findViewById(R.id.textView6);
        fruitTxt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home_page.this, fruit2_page.class);
                startActivity(intent);
            }
        });



        articleBtn = findViewById(R.id.button1);
        articleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home_page.this, articles_page.class);
                startActivity(intent);
            }
        });

        fruitBtn = findViewById(R.id.button2);
        fruitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home_page.this, fruits_page.class);
                startActivity(intent);
            }
        });


        //menu navigation
        home = findViewById(R.id.homeImage);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home_page.this, home_page.class);
                startActivity(intent);
            }
        });

        homeTxt = findViewById(R.id.homeText);
        homeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home_page.this, home_page.class);
                startActivity(intent);
            }
        });

        camera = findViewById(R.id.cameraImage);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home_page.this, camera_page.class);
                startActivity(intent);
            }
        });

        cameraTxt = findViewById(R.id.cameraText);
        cameraTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home_page.this, camera_page.class);
                startActivity(intent);
            }
        });

        profile = findViewById(R.id.profileImage);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home_page.this, profile_page.class);
                startActivity(intent);
            }
        });

        profileTxt = findViewById(R.id.profileText);
        profileTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home_page.this, profile_page.class);
                startActivity(intent);
            }
        });
    }
}