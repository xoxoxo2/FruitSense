package com.example.fruitsense5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.net.Uri;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fruitsense5.ml.Model;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.tensorflow.lite.DataType;
//import org.tensorflow.lite.schema.Model;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class result_page extends AppCompatActivity {

    ImageView back;
    ImageView imageView;
    TextView resultTxt, descriptionTxt, confidenceTxt;
    int imageSize = 224;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_page);

        imageView = findViewById(R.id.image);
        resultTxt = findViewById(R.id.text1);
        descriptionTxt = findViewById(R.id.description);
        confidenceTxt = findViewById(R.id.confidence);

        // back arrow
        back = findViewById(R.id.backImageView);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(result_page.this, camera_page.class);
                startActivity(intent);
            }
        });

        // Handle image from camera
        Bitmap imageBitmap = getIntent().getParcelableExtra("imageBitmap");
        if (imageBitmap != null) {
            imageView.setImageBitmap(imageBitmap);
            classifyImage(imageBitmap);
        }

        // Handle image from gallery
        String imageUri = getIntent().getStringExtra("imageUri");
        if (imageUri != null) {
            imageView.setImageURI(Uri.parse(imageUri));
            classifyImageUri(imageUri);
        }
    }

    private void classifyImage(Bitmap imageBitmap) {
        // Resize bitmap to the size expected by the model
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, imageSize, imageSize, true);

        try {
            Model model = Model.newInstance(getApplicationContext());

            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intValues = new int[imageSize * imageSize];
            resizedBitmap.getPixels(intValues, 0, resizedBitmap.getWidth(), 0, 0, resizedBitmap.getWidth(), resizedBitmap.getHeight());
            int pixel = 0;
            for (int i = 0; i < imageSize; i++) {
                for (int j = 0; j < imageSize; j++) {
                    int val = intValues[pixel++]; // RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
                }
            }
            inputFeature0.loadBuffer(byteBuffer);

            Model.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();
            int maxPos = 0;
            float maxConfidence = 0;
            for (int i = 0; i < confidences.length; i++) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }
            String[] classes = {"Unripe Banana", "Unripe Banana", "Unripe Banana", "Ripe Banana", "Ripe Banana", "Ripe Banana", "Overripe Banana", "Overripe Banana",
                    "Unripe Apple", "Unripe Apple", "Unripe Apple", "Ripe Apple", "Ripe Apple", "Overripe Apple", "Overripe Apple",
                    "Unripe Tomato", "Unripe Tomato", "Unripe Tomato", "Unripe Tomato", "Ripe Tomato", "Ripe Tomato", "Overripe Tomato", "Overripe Tomato" };
            resultTxt.setText(classes[maxPos]);

            String ripenessText = "";
            if (maxPos == 0) { // Unripe banana
                ripenessText = "This banana is currently unripe. It will be perfect for eating within 10 days or more.";
            } else if (maxPos == 1) { // Unripe banana
                ripenessText = "This banana is currently unripe. It will be perfect for eating within 7 days.";
            } else if (maxPos == 2) { // Unripe banana
                ripenessText = "This banana is currently unripe. It will be perfect for eating within 3 days.";
            } else if (maxPos == 3) { // Ripe banana
                ripenessText = "This banana is ripe and best consumed within the next 3 days for optimal taste.";
            } else if (maxPos == 4) { // Ripe banana
                ripenessText = "This banana is ripe and best consumed within the next 2 days for optimal taste.";
            } else if (maxPos == 5) { // Ripe banana
                ripenessText = "This banana is ripe and best consumed within the next 1 day for optimal taste.";
            } else if (maxPos == 6) { // Overripe banana
                ripenessText = "This banana is overripe and best consumed within the next 1 day before fully overripe.";
            } else if (maxPos == 7) { // Overripe banana
                ripenessText = "This overripe banana and should no longer be consumed.";
            } else if (maxPos == 8) { // Unripe Apple
                ripenessText = "This apple is currently unripe. It will be perfect to consume after 170 days.";
            } else if (maxPos == 9) { // Unripe Apple
                ripenessText = "This apple is currently unripe. It will be perfect to consume after 30 days.";
            } else if (maxPos == 10) { // Unripe Apple
                ripenessText = "This apple is currently unripe. It will be perfect to consume after 26 - 30 days.";
            } else if (maxPos == 11) { // Ripe Apple
                ripenessText = "This apple is ripe and best consumed after 10 days for optimal taste and to be fully ripe.";
            } else if (maxPos == 12) { // Ripe Apple
                ripenessText = "This apple is ripe and best consumed within the next 5 days for optimal taste.";
            } else if (maxPos == 13) { // Overripe Apple
                ripenessText = "This apple is overripe and best consumed within the next 5 days before fully overripe.";
            } else if (maxPos == 14) { // Overripe Apple
                ripenessText = "This apple banana and should no longer be consumed.";
            } else if (maxPos == 15) { // Unripe Tomato
                ripenessText = "This tomato is currently unripe. It will be perfect to consume after 14 days.";
            } else if (maxPos == 16) { // Unripe Tomato
                ripenessText = "This tomato is currently unripe. It will be perfect to consume after 8 - 13 days.";
            } else if (maxPos == 17) { // Unripe Tomato
                ripenessText = "This tomato is currently unripe. It will be perfect to consume after 4 - 7 days.";
            } else if (maxPos == 18) { // Unripe Tomato
                ripenessText = "This tomato is currently unripe. It will be perfect to consume after 2 - 4 days.";
            } else if (maxPos == 19) { // Ripe Tomato
                ripenessText = "This tomato is ripe and best consumed within the next 2 - 4 days for optimal taste to be fully ripe.";
            } else if (maxPos == 20) { // Ripe Tomato
                ripenessText = "This tomato is ripe and best consumed within the next 3 - 4 days for optimal taste.";
            } else if (maxPos == 21) { // Overripe Tomato
                ripenessText = "This tomato is overripe and best consumed within the next 1 - 3 day before fully overripe.";
            } else if (maxPos == 22) { // Overripe Tomato
                ripenessText = "This tomato is overripe and should no longer be consumed.";
            } else { // Default
                ripenessText = "This is not a fruit image, please capture or upload banana image.";
            }
            descriptionTxt.setText(ripenessText);

            String s = "";
            for (int i = 0; i < classes.length; i++) {
                s += String.format("%s: %.1f%%\n", classes[i], confidences[i] * 100);
            }
            confidenceTxt.setText(s);

            model.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void classifyImageUri(String imageUri) {
        try {
            // Create a StorageReference to the image
            StorageReference imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(imageUri);

            // Download the image as a byte array
            imageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap imageBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    imageView.setImageBitmap(imageBitmap); // Display the image
                    classifyImage(imageBitmap); // Classify the image
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                    Toast.makeText(result_page.this, "Failed to download image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
