package com.example.fruitsense5;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyHolder> {

    Context context;
    ArrayList<ModelClass> arrayList;
    LayoutInflater layoutInflater;

    public ProductAdapter(Context context, ArrayList<ModelClass> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_file,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.productName.setText(arrayList.get(position).getProductName());
        //holder.productPrice.setText(arrayList.get(position).getProductPrice());
        holder.img.setImageResource(arrayList.get(position).getImg());

        // Set click listener for the image
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the intended product page
                launchProductPage(position);
            }
        });

        // Set click listener for the name
        holder.productName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the intended product page
                launchProductPage(position);
            }
        });
    }

    // Method to launch the intended product page
    private void launchProductPage(int position) {
        ModelClass model = arrayList.get(position);
        String productName = model.getProductName();

        // Launch the product page based on the clicked product name
        Intent intent;
        switch (productName) {
            case "Apple":
                intent = new Intent(context, fruit1_page.class);
                context.startActivity(intent);
                break;
            case "Banana":
                intent = new Intent(context, fruit2_page.class);
                context.startActivity(intent);
                break;
            case "Tomato":
                intent = new Intent(context, fruit3_page.class);
                context.startActivity(intent);
                break;
            case "The Downside of Overripe Fruits":
                intent = new Intent(context, article1_page.class);
                context.startActivity(intent);
                break;
            case "Fruit Ripening Stages: From Green to Sweet":
                intent = new Intent(context, article2_page.class);
                context.startActivity(intent);
                break;
            case "Fruit Storage Hacks":
                intent = new Intent(context, article3_page.class);
                context.startActivity(intent);
                break;
            case "Factors Affecting Fruit Quality)":
                intent = new Intent(context, article4_page.class);
                context.startActivity(intent);
                break;
            case "Nutritional Benefits of Fresh Fruits":
                intent = new Intent(context, article5_page.class);
                context.startActivity(intent);
                break;
            case "Global Trends in Fruit Consumption":
                intent = new Intent(context, article6_page.class);
                context.startActivity(intent);
                break;
            // Add more cases for other product pages
            default:
                // Handle default case if needed
                break;
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice;
        ImageView img;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            productName = itemView.findViewById(R.id.txt1);
            //productPrice = itemView.findViewById(R.id.txt2);
        }
    }
}
