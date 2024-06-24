package com.example.ominformatics_shweta_bhosale;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ominformatics_shweta_bhosale.model.Orders;

import java.text.BreakIterator;

public class DeliveryActivity extends AppCompatActivity {

    private Orders orders;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private RadioGroup radioGroupStatus;
    private EditText edtDamageType; // Declare edtDamageType
    private EditText edtCollectedAmount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_delivery);


        orders = (Orders) getIntent().getSerializableExtra("order");

        ImageView imgPhoto = findViewById(R.id.imgPhoto);
        edtCollectedAmount = findViewById(R.id.edtCollectedAmount);
        edtDamageType = findViewById(R.id.edtDamageType);
        radioGroupStatus = findViewById(R.id.radioGroupStatus);
        Button btnTakePhoto = findViewById(R.id.btnTakePhoto);
        Button btnRetakePhoto = findViewById(R.id.btnRetakePhoto);
        Button btnSubmit = findViewById(R.id.btnSubmit);

        btnTakePhoto.setOnClickListener(v -> dispatchTakePictureIntent());
        btnRetakePhoto.setOnClickListener(v -> dispatchTakePictureIntent());
        radioGroupStatus.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioDamaged) {
                edtDamageType.setVisibility(View.VISIBLE);
            } else {
                edtDamageType.setVisibility(View.GONE);
            }
        });

        btnSubmit.setOnClickListener(v -> submitDeliveryDetails());
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView imgPhoto = null;
            imgPhoto.setImageBitmap(imageBitmap);
            imgPhoto.setVisibility(View.VISIBLE);
            findViewById(R.id.btnRetakePhoto).setVisibility(View.VISIBLE);
        }
    }

    private void submitDeliveryDetails() {
        try {
            double collectedAmount = Double.parseDouble(edtCollectedAmount.getText().toString());
            boolean isDamaged = radioGroupStatus.getCheckedRadioButtonId() == R.id.radioDamaged;
            String damageType = edtDamageType.getText().toString();

            // Perform validation
            if (collectedAmount > orders.getDeliveryCost()) {
                Toast.makeText(this, "Collected amount cannot exceed delivery charge", Toast.LENGTH_SHORT).show();
                return;
            }

            orders.setCollectedAmount(collectedAmount);
            orders.setDamaged(isDamaged);
            orders.setDamageType(damageType);
            orders.setDelivered(true);

            // Save the order to the database
            new Thread(() -> {
                OrderDatabase.getDatabase(getApplicationContext()).orderDao().updateOrder(orders);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Delivery details submitted", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }).start();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid collected amount", Toast.LENGTH_SHORT).show();
        }
    }
}