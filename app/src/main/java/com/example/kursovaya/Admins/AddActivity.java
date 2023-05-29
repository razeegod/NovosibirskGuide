package com.example.kursovaya.Admins;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kursovaya.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AddActivity extends AppCompatActivity {

    private String categoryName;
    private ImageView PlacePhoto;
    private EditText PlaceName;
    private String Name;
    private EditText PlaceDescription;
    private String Description;
    private EditText PlaceUrl;
    private String Url;
    private EditText PlaceNumber;
    private String Number;
    private EditText PlaceSchedule;
    private String Schedule;
    private Button BackBtn;
    private Button CreatePlaceBtn, DeletePlaceBtn, ChangePlaceBtn;
    private LinearLayout LinerLayoutAdd;
    private TextView TextCategory;
    private static final int GalleryPick = 1;
    private Uri ImageUri;

    private String placeRandomKey;
    private StorageReference PlaceImageRef;
    private String downloadImageUrl;
    private DatabaseReference PlacesRef;
    private ProgressDialog loadingBar;
    private EditText PlaceCoordinates;
    private String Coordinates;
    private String placeID, mode;

    public AddActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        init();

        TextCategory.setText(getIntent().getExtras().getString("category"));

        placeID = getIntent().getStringExtra("PlaceID");
        mode = getIntent().getStringExtra("mode");

        if (mode.equals("add"))
        {
            BackBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent backIntent = new Intent(AddActivity.this, AddNewPlace.class);
                    startActivity(backIntent);
                }
            });
            CreatePlaceBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ValidatePlaceData();
                }
            });
        }
        else if(mode.equals("change"))
        {
            CreatePlaceBtn.setVisibility(View.GONE);
            LinerLayoutAdd.setVisibility(View.VISIBLE);

            loadPlaceData();
            BackBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent backIntent = new Intent(AddActivity.this, AdminPlaceActivity.class);
                    backIntent.putExtra("category", categoryName);
                    startActivity(backIntent);
                }
            });

            ChangePlaceBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updatePlaceData();
                }
            });
            DeletePlaceBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DeletePlaceData();
                }
            });
        }

        PlacePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

    }

    private void DeletePlaceData()
    {
        PlacesRef.child(categoryName).child(placeID)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Intent Intent = new Intent(AddActivity.this, AdminPlaceActivity.class);
                            Intent.putExtra("category", categoryName);
                            startActivity(Intent);
                        } else {
                            String message = task.getException().toString();
                            Toast.makeText(AddActivity.this, "Произошла ошибка: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void loadPlaceData() {
        DatabaseReference placeRef = FirebaseDatabase.getInstance().getReference().child("Places").child(categoryName).child(placeID);

        placeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    CreatePlaceBtn.setVisibility(View.GONE);
                    LinerLayoutAdd.setVisibility(View.VISIBLE);

                    String phone = snapshot.child("Number").getValue() != null ? snapshot.child("Number").getValue().toString() : "";
                    String image = snapshot.child("Image").getValue().toString();
                    String description = snapshot.child("Description").getValue().toString();
                    String name = snapshot.child("Name").getValue().toString();
                    String schedule = snapshot.child("Schedule").getValue().toString();
                    String coordinates = snapshot.child("Coordinates").getValue().toString();
                    String url = snapshot.child("URL").getValue().toString();

                    PlaceName.setText(name);
                    PlaceCoordinates.setText(coordinates);
                    PlaceUrl.setText(url);
                    PlaceDescription.setText(description);
                    Picasso.get().load(image).into(PlacePhoto);
                    PlaceSchedule.setText(schedule);
                    PlaceNumber.setText(phone);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Обработка ошибок
            }
        });
    }


    private void ValidatePlaceData()
    {
        Description = PlaceDescription.getText().toString();
        Name = PlaceName.getText().toString();
        Number = PlaceNumber.getText().toString();
        Url = PlaceUrl.getText().toString();
        Schedule = PlaceSchedule.getText().toString();
        Coordinates = PlaceCoordinates.getText().toString();

        if(ImageUri == null)
        {
            Toast.makeText(this, "Добавьте изображение", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Name))
        {
            Toast.makeText(this, "Введите название", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Description))
        {
            Toast.makeText(this, "Добавьте описание", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Url))
        {
            Toast.makeText(this, "Добавьте ссылку", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Schedule))
        {
            Toast.makeText(this, "Укажите расписание", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Coordinates))
        {
            Toast.makeText(this, "Укажите координаты", Toast.LENGTH_SHORT).show();
        }
        else
        {
            StorePlaceInformation();
        }
    }



    private void StorePlaceInformation()
    {

        loadingBar.setTitle("Создание нового места");
        loadingBar.setMessage("Пожалуйста, подождите...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        long currentTimeMillis = System.currentTimeMillis();
        placeRandomKey = String.valueOf(currentTimeMillis);
        StorageReference filePath = PlaceImageRef.child(ImageUri.getLastPathSegment() + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AddActivity.this, "Произошла ошибка: " + message , Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AddActivity.this, "Изображение получено", Toast.LENGTH_SHORT).show();

                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadImageUrl = task.getResult().toString();
                            Toast.makeText(AddActivity.this, "Изображение сохранено", Toast.LENGTH_SHORT).show();
                            SavePlaceInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void SavePlaceInfoToDatabase()
    {
        HashMap<String, Object> placeData = new HashMap<>();

        placeData.put("PlaceID", placeRandomKey);
        placeData.put("Description", Description);
        placeData.put("Image", downloadImageUrl);
        placeData.put("Schedule", Schedule);
        placeData.put("Number", Number);
        placeData.put("Name", Name);
        placeData.put("URL", Url);
        placeData.put("Coordinates", Coordinates);

        PlacesRef.child(categoryName).child(placeRandomKey).updateChildren(placeData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    loadingBar.dismiss();

                    Toast.makeText(AddActivity.this, "Место добавлено!", Toast.LENGTH_SHORT).show();

                    Intent Intent = new Intent(AddActivity.this, AddNewPlace.class);
                    startActivity(Intent);
                }
                else
                {
                    loadingBar.dismiss();

                    String message = task.getException().toString();
                    Toast.makeText(AddActivity.this, "Произошла ошибка: " + message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void OpenGallery()
    {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GalleryPick && resultCode == RESULT_OK && data != null)
        {
            ImageUri = data.getData();
            PlacePhoto.setImageURI(ImageUri);
        }
    }

    private void updatePlaceData()
    {
        Description = PlaceDescription.getText().toString();
        Name = PlaceName.getText().toString();
        Number = PlaceNumber.getText().toString();
        Url = PlaceUrl.getText().toString();
        Schedule = PlaceSchedule.getText().toString();
        Coordinates = PlaceCoordinates.getText().toString();

        if (TextUtils.isEmpty(Name)) {
            Toast.makeText(this, "Введите название", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Description)) {
            Toast.makeText(this, "Добавьте описание", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Url)) {
            Toast.makeText(this, "Добавьте ссылку", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Schedule)) {
            Toast.makeText(this, "Укажите расписание", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Coordinates)) {
            Toast.makeText(this, "Укажите координаты", Toast.LENGTH_SHORT).show();
        } else {
            if (ImageUri != null) {
                updatePlaceImage();
            } else {
                updatePlaceInfoToDatabase();
            }
        }
    }

    private void updatePlaceImage() {
        loadingBar.setTitle("Обновление места");
        loadingBar.setMessage("Пожалуйста, подождите...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        StorageReference filePath = PlaceImageRef.child(ImageUri.getLastPathSegment() + ".jpg");
        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AddActivity.this, "Произошла ошибка: " + message , Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AddActivity.this, "Изображение получено", Toast.LENGTH_SHORT).show();

                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadImageUrl = task.getResult().toString();
                            Toast.makeText(AddActivity.this, "Изображение сохранено", Toast.LENGTH_SHORT).show();
                            updatePlaceInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void updatePlaceInfoToDatabase()
    {
        HashMap<String, Object> placeData = new HashMap<>();

        placeData.put("Description", Description);
        if (ImageUri != null) {
            placeData.put("Image", downloadImageUrl);
        }
        placeData.put("Schedule", Schedule);
        placeData.put("Number", Number);
        placeData.put("Name", Name);
        placeData.put("URL", Url);
        placeData.put("Coordinates", Coordinates);
        PlacesRef.child(categoryName).child(placeID).updateChildren(placeData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    loadingBar.dismiss();
                    Toast.makeText(AddActivity.this, "Место обновлено!", Toast.LENGTH_SHORT).show();
                    Intent Intent = new Intent(AddActivity.this, AdminPlaceActivity.class);
                    startActivity(Intent);
                }
                else
                {
                    loadingBar.dismiss();
                    String message = task.getException().toString();
                    Toast.makeText(AddActivity.this, "Произошла ошибка: " + message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void init()
    {
        LinerLayoutAdd = findViewById(R.id.liner_layout_addactivity);
        categoryName = getIntent().getStringExtra("category");
        PlacePhoto = findViewById(R.id.select_place_image);
        PlaceName = findViewById(R.id.place_name);
        PlaceDescription = findViewById(R.id.description_place);
        PlaceUrl = findViewById(R.id.place_url);
        PlaceNumber = findViewById(R.id.number_place);
        PlaceSchedule = findViewById(R.id.place_schedule);
        BackBtn = findViewById(R.id.back_btn);
        CreatePlaceBtn = findViewById(R.id.btn_addNewProduct);
        DeletePlaceBtn = findViewById(R.id.btn_deletePlace);
        ChangePlaceBtn = findViewById(R.id.btn_changePlace);
        PlaceImageRef = FirebaseStorage.getInstance().getReference().child("Place Images");
        TextCategory = (TextView) findViewById(R.id.category_name);
        PlacesRef = FirebaseDatabase.getInstance().getReference().child("Places");
        loadingBar = new ProgressDialog(this);
        PlaceCoordinates = (EditText) findViewById(R.id.place_longitude_latitude);
    }

}
