package com.example.have_it;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 *This is the activity for edit or delete event
 * @author songkunguo
 */
public class ViewEditEventActivity extends AppCompatActivity implements DatabaseUserReference, FirestoreGetDocument, FirestoreAddData, FirestoreDeleteData{
    /**
     *Reference to event input, of class {@link EditText}
     */
    EditText eventText;
    /**
     *Reference to date input, of class {@link EditText}
     */
    TextView dateText;
    /**
     *Reference to address, of class {@link TextView}
     */
    TextView addressText;
    /**
     *Reference to the pick location button, of class {@link Button}
     */
    Button changeLocation;
    /**
     *Reference to the confirm button, of class {@link Button}
     */
    Button confirm;
    /**
     *Reference to the delete button, of class {@link Button}
     */
    Button delete;
    /**
     *Reference to the dialog for picking date, of class {@link DatePickerDialog}
     */
    DatePickerDialog picker;
    /**
     * The event date selected, of class {@link String}
     */
    String selectedEventDate;
    /**
     * The habit title selected, of class {@link String}
     */
    String selectedHabit;

    /**
     * Lagitude and Longitude to store the location as String Variable
     */
    String latitude = null;
    String longitude = null;

    Context context;
    public static final int CAMERA_PREM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE =102;
    public static final int GALLERY_REQUEST_CODE=105;
    String currentPhotoPath;
    ImageView selectedImage;
    ImageButton cameraBtn, galleryBtn;
    Uri contentUri;

    /**
     *This is the method invoked when the activity starts
     * @param savedInstanceState {@link Bundle} used for its super class
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_edit_event);
        Intent i = getIntent();
        selectedEventDate = i.getStringExtra("event_date");
        selectedHabit = i.getStringExtra("habit");
        context = this;
        contentUri = Uri.EMPTY;

        eventText = findViewById(R.id.event_editText_viewedit);
        dateText = findViewById(R.id.event_date_viewedit);
        addressText = findViewById(R.id.address);
        changeLocation = findViewById(R.id.change_location_button);
        confirm = findViewById(R.id.confirm_button_viewedit);
        delete = findViewById(R.id.delete_button);
        selectedImage = findViewById(R.id.displayImageView);
        galleryBtn = findViewById(R.id.galleryBtn);
        cameraBtn = findViewById(R.id.cameraBtn);

        getDocument();
        cameraBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                askCameraPermissions();
            }
        });
        galleryBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, GALLERY_REQUEST_CODE);
            }
        });



        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(ViewEditEventActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String selectDayString = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                Date selectDay = new Date();
                                try {
                                    selectDay = new SimpleDateFormat("yyyy-MM-dd")
                                            .parse(selectDayString);
                                } catch (ParseException e){
                                    Toast.makeText(getApplicationContext(),"Not valid date", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                dateText.setText(new SimpleDateFormat("yyyy-MM-dd").format(selectDay));
                            }
                        }, year, month, day);
                picker.show();
            }
        });



        Intent intent = new Intent(ViewEditEventActivity.this.getApplicationContext(), ChangeLocationMapsActivity.class);
        changeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("LAT", latitude);
                intent.putExtra("LONG",longitude);
                startActivityForResult(intent, 2404);
            }
        });





        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDataInFirestore();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDataToFirestore();
            }
        });
    }

    /**
     *This is the method invoked when the back in menu is pressed
     * @param item used for its super class
     * @return the result of its super selecting the same option
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This is the method for getting a reference to document
     */
    @Override
    public void getDocument() {
        final CollectionReference eventListReference = db.collection("Users")
                .document(logged.getUID())
                .collection("HabitList")
                .document(selectedHabit).collection("EventList");


        eventListReference.document(selectedEventDate).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                eventText.setText((String)documentSnapshot.getData().get("event"));
                //  SimpleDateFormat spf= new SimpleDateFormat("yyyy-MM-dd");
                //DateText.setText(spf.format(((Timestamp)documentSnapshot.getData().get("date")).toDate()));
                dateText.setText( documentSnapshot.getData().get("date").toString());
                latitude = (String) documentSnapshot.getData().get("latitude");
                longitude = (String) documentSnapshot.getData().get("longitude");
                StorageReference eventImageRef = storageReference.child("eventPhotos/"+logged.getUID()+"/"+selectedHabit+"/"+dateText.getText().toString()+".jpg");
                eventImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(selectedImage);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String word = eventText.getText().toString()+".jpg";
                        Toast.makeText(ViewEditEventActivity.this, word, Toast.LENGTH_SHORT).show();
                    }
                });

                if(latitude != null){
                    Geocoder geocoder;
                    List<Address> addresses;
                    geocoder = new Geocoder(context, Locale.getDefault());

                    try {
                        addresses = geocoder.getFromLocation(Double.valueOf(latitude), Double.valueOf(longitude), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        String address = addresses.get(0).getAddressLine(0);
                        addressText.setText(address);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    addressText.setText("No location selected");

                }


            }
        });
    }

    /**
     * This is the method for adding data to the firestore
     */
    @Override
    public void addDataToFirestore() {
        final CollectionReference eventListReference = db.collection("Users")
                .document(logged.getUID())
                .collection("HabitList")
                .document(selectedHabit).collection("EventList");
        // Retrieving the city name and the province name from the EditText fields
        final String event = eventText.getText().toString();
        HashMap<String, Object> data = new HashMap<>();
        if (event.length()>0){
            data.put("event", event);
            data.put("date", dateText.getText().toString());
            data.put("latitude", latitude);
            data.put("longitude", longitude);

            if (dateText.getText().toString().equals(selectedEventDate)){
                eventListReference
                        .document(selectedEventDate)
                        .update(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                uploadImageToFirebase(selectedHabit,eventText.getText().toString(), dateText.getText().toString(), contentUri);
                                Log.d("Edit Habit", "Habit data has been edited successfully!");
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("Edit Habit", "Error editing document", e);
                            }
                        });
            }
            else {
                eventListReference
                        .document(dateText.getText().toString())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Toast.makeText(getApplicationContext(), "cannot edit event: another event at the same day", Toast.LENGTH_LONG).show();
                                    } else {
                                        uploadImageToFirebase(selectedHabit,eventText.getText().toString(), dateText.getText().toString(), contentUri);
                                        StorageReference eventImageRef = storageReference.child("eventPhotos/"+logged.getUID()+"/"+selectedHabit+"/"+selectedEventDate+".jpg");
                                        // Delete the file
                                        eventImageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("Delete photo", "Photo deleted successfully");
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("Delete photo", "Error deleting photo", e);
                                            }
                                        });

                                        eventListReference
                                                .document(selectedEventDate)
                                                .delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d("Delete event", "event data has been deleted successfully!");

                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w("Delete event", "Error deleting document", e);
                                                    }
                                                });
                                        eventListReference
                                                .document(dateText.getText().toString())
                                                .set(data)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        // These are a method which gets executed when the task is succeeded
                                                        Log.d("Adding event", "event data has been edited successfully!");
                                                        finish();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        // These are a method which gets executed if there’s any problem
                                                        Log.d("Adding event", "Habit data could not be edited!" + e.toString());
                                                        Toast.makeText(getApplicationContext(), "Not being able to edit data, please check duplication event", Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                    }
                                }
                            }
                        });
            }
        }
    }
    //camera related
    private void askCameraPermissions() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.CAMERA}, CAMERA_PREM_CODE);
        }else {
            dispatchTakePictureIntent();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PREM_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Camera Permission is Required to Use camera.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void uploadImageToFirebase( String habitTitle, String event, String date, Uri contentUri) {
        final StorageReference image = storageReference.child("eventPhotos/"+logged.getUID()+"/"+habitTitle+"/"+date+".jpg");
        if (!contentUri.equals(Uri.EMPTY)){
            image.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get().load(uri).into(selectedImage);
                        }
                    });

                    Toast.makeText(ViewEditEventActivity.this, "Image Is Uploaded.", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ViewEditEventActivity.this, "Upload Failed.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    /**
     * save photo on the gallery
     *
     */

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }
    /**
     * This is the method for deleting data to the firestore
     */
    @Override
    public void deleteDataInFirestore() {
        final CollectionReference eventListReference = db.collection("Users")
                .document(logged.getUID())
                .collection("HabitList")
                .document(selectedHabit).collection("EventList");
        // Toast.makeText( getApplicationContext(),  selected_event_date, Toast.LENGTH_SHORT).show();
        eventListReference.document(selectedEventDate)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Delete Event", "Habit data has been deleted successfully!");
                        StorageReference eventImageRef = storageReference.child("eventPhotos/"+logged.getUID()+"/"+selectedHabit+"/"+dateText.getText().toString()+".jpg");
                        // Delete the file
                        eventImageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("Delete photo", "Photo deleted successfully");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("Delete photo", "Error deleting photo", e);
                            }
                        });

                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Delete event", "Error deleting document", e);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == 2404) {
            if(data != null) {
                latitude = data.getStringExtra("LAT");
                longitude = data.getStringExtra("LONG");

                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(this, Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(Double.valueOf(latitude), Double.valueOf(longitude), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    String address = addresses.get(0).getAddressLine(0);
                    addressText.setText(address);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            File f = new File(currentPhotoPath);


            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            contentUri = Uri.fromFile(f);
            selectedImage.setImageURI(contentUri);
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);
        }else if(requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            contentUri = data.getData();
            selectedImage.setImageURI(contentUri);
        }
    }
}