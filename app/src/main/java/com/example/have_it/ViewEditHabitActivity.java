package com.example.have_it;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dpro.widgets.WeekdaysPicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 *This is the activity for editing or delete a  habit
 * @author ruiqingtian
 */
public class ViewEditHabitActivity extends AppCompatActivity implements DatabaseUserReference, FirestoreGetDocument, FirestoreAddData, FirestoreDeleteData{
    /**
     *Reference to title input, of class {@link EditText}
     */
    EditText titleText;
    /**
     *Reference to reason input, of class {@link EditText}
     */
    EditText reasonText;
    /**
     *Reference to date input, of class {@link TextView}
     */
    TextView startDateText;
    /**
     *Reference to weekdays input, of class {@link WeekdaysPicker}
     */
    WeekdaysPicker weekdaysPicker;
    /**
     * Reference to the switch for publicity of habit, of class {@link Switch}
     */
    Switch publicitySwitch;
    /**
     *Reference to the confirm button, of class {@link Button}
     */
    Button confirm;
    /**
     *Reference to the delete button, of class {@link Button}
     */
    Button delete;
    /**
     *event list, of class {@link ArrayList}
     */
    Button eventList;
    /**
     *Reference to the dialog for picking date, of class {@link DatePickerDialog}
     */
    DatePickerDialog picker;
    /**
     * The habit title for the selected habit, of class {@link String}
     */
    String selectedTitle;

    /**
     * count used to get the specific position of this habit located in the list (for recordering), of class {@link Integer}
     */
    Integer count;

    /**
     *This is the current context, of class {@link Context}
     */
    Context context;


    final long ONE_MEGABYTE = 1024 * 1024;



    /**
     *This is the method invoked when the activity starts
     * @param savedInstanceState {@link Bundle} used for its super class
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_edit_habit);
        context = this.getApplicationContext();

        titleText = findViewById(R.id.habit_title_editText_viewedit);
        reasonText = findViewById(R.id.habit_reason_editText_viewedit);
        weekdaysPicker = (WeekdaysPicker) findViewById(R.id.habit_weekday_selection_viewedit);
        startDateText = findViewById(R.id.habit_start_date_viewedit);
        confirm = findViewById(R.id.confirm_button_viewedit);
        delete = findViewById(R.id.delete_button);
        eventList = findViewById(R.id.event_list_button);
        publicitySwitch = findViewById(R.id.publicity_switch_viewedit);

        Intent intent = getIntent();
        selectedTitle = intent.getStringExtra("habit");

        getDocument();

        startDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(ViewEditHabitActivity.this,
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
                                startDateText.setText(new SimpleDateFormat("yyyy-MM-dd").format(selectDay));
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               addDataToFirestore();
           }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDataInFirestore();
            }
        });


        final Intent eventListIntent = new Intent(this, EventPageActivity.class);
        eventList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventListIntent.putExtra("habit", selectedTitle);
                startActivity(eventListIntent);
            }
        });

        final FloatingActionButton indicatorButton = findViewById(R.id.indicator_button);
        final Intent indicatorIntent = new Intent(this, IndicatorActivity.class);
        indicatorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indicatorIntent.putExtra("habit", selectedTitle);
                startActivity(indicatorIntent);
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
     * This is the method for adding data to the firestore
     */
    @Override
    public void addDataToFirestore() {
        final CollectionReference habitListReference = db.collection("Users")
                .document(logged.getUID()).collection("HabitList");
        final CollectionReference eventListReference = db.collection("Users")
                .document(logged.getUID()).collection("HabitList")
                .document(selectedTitle).collection("EventList");
        // Retrieving the city name and the province name from the EditText fields
        final String title = titleText.getText().toString();
        final String reason = reasonText.getText().toString();
        Date startDate = new Date();
        try {
            startDate = new SimpleDateFormat("yyyy-MM-dd")
                    .parse(startDateText.getText().toString());
        } catch (ParseException e) {
            Toast.makeText(getApplicationContext(), "Not valid date", Toast.LENGTH_LONG).show();
            return;
        }
        final Timestamp startDateTimestamp = new Timestamp(startDate);

        List<Integer> selectedDays = weekdaysPicker.getSelectedDays();
        Boolean[] defaultReg = {false, false, false, false, false, false, false};
        List<Boolean> weekdayReg = new ArrayList<>(Arrays.asList(defaultReg));

        for (int each : selectedDays) {
            weekdayReg.set(each - 1, true);
        }

        final Boolean publicity = publicitySwitch.isChecked();

        HashMap<String, Object> data = new HashMap<>();

        if (title.length() > 0) {
            data.put("title", title);
            data.put("reason", reason);
            data.put("dateStart", startDateTimestamp);
            data.put("weekdayReg", weekdayReg);
            data.put("publicity", publicity);
            data.put("order", count);


            if (title.equals(selectedTitle)) {
                habitListReference.document(selectedTitle)
                        .update(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("Edit Habit", "Habit data has been deleted successfully!");
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("Edit Habit", "Error deleting document", e);
                            }
                        });
            } else {
                habitListReference
                        .document(title)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Toast.makeText(getApplicationContext(), "cannot edit because the habit with same title exists", Toast.LENGTH_LONG).show();
                                    } else {
                                        StorageReference imageFolderReference = storageReference.child("eventPhotos/"+logged.getUID()+"/"+selectedTitle);
                                        imageFolderReference.listAll()
                                                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                                                    @Override
                                                    public void onSuccess(ListResult listResult) {
                                                        for (StorageReference item : listResult.getItems()) {
                                                            item.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                                                @Override
                                                                public void onSuccess(byte[] bytes) {
                                                                    String imageName = item.getName();
                                                                    uploadImageToFirebase(title,imageName,bytes);
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception exception) {
                                                                }
                                                            });
                                                            item.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
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
                                                        }
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                    }
                                                });

                                        habitListReference
                                                .document(title)
                                                .set(data)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        // These are a method which gets executed when the task is succeeded
                                                        Log.d("Adding Habit", "Habit data has been edited successfully!");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        // These are a method which gets executed if there’s any problem
                                                        Log.d("Adding Habit", "Habit data could not be edited!" + e.toString());
                                                        Toast.makeText(getApplicationContext(), "Not being able to edit data, please check duplication title", Toast.LENGTH_LONG).show();
                                                    }
                                                });

                                        eventListReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                                                    FirebaseFirestoreException error) {
                                                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                                    HashMap<String, Object> event = new HashMap<>();
                                                    event.put("date", doc.getData().get("date"));
                                                    event.put("event", doc.getData().get("event"));
                                                    event.put("latitude", doc.getData().get("latitude"));
                                                    event.put("longitude", doc.getData().get("longitude"));


                                                    habitListReference.document(selectedTitle).collection("EventList")
                                                            .document((String) doc.getData().get("date"))
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


                                                    habitListReference.document(title).collection("EventList")
                                                            .document((String) doc.getData().get("date"))
                                                            .set(event)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    // These are a method which gets executed when the task is succeeded
                                                                    Log.d("Adding event", "Habit data has been edited successfully!");

                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    // These are a method which gets executed if there’s any problem
                                                                    Log.d("Adding event", "Habit data could not be edited!" + e.toString());
                                                                    Toast.makeText(getApplicationContext(), "Not being able to edit data, please check duplication title", Toast.LENGTH_LONG).show();
                                                                }
                                                            });


                                                }
                                            }
                                        });
                                        habitListReference.document(selectedTitle)
                                                .delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d("Delete Habit", "Habit data has been deleted successfully!");
                                                        finish();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w("Delete Habit", "Error deleting document", e);
                                                    }
                                                });
                                    }
                                }
                            }
                        });
            }
        }
    }

    /**
     * This is the method for getting a reference to document
     */
    @Override
    public void getDocument() {
        final CollectionReference habitListReference = db.collection("Users")
                .document(logged.getUID()).collection("HabitList");

        habitListReference.document(selectedTitle).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                titleText.setText(selectedTitle);
                reasonText.setText((String) documentSnapshot.getData().get("reason"));
                SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd");
                startDateText.setText(spf.format(((Timestamp) documentSnapshot.getData().get("dateStart")).toDate()));

                List<Integer> weekdayReg = new ArrayList<>(7);
                count = Integer.valueOf(String.valueOf(documentSnapshot.getData().get("order")));

                Integer c = 1;
                for (boolean each : (ArrayList<Boolean>) documentSnapshot.getData().get("weekdayReg")) {
                    if (each) {
                        weekdayReg.add(c);
                    }
                    c++;
                }
                weekdaysPicker.setSelectedDays(weekdayReg);

                publicitySwitch.setChecked((Boolean) documentSnapshot.getData().get("publicity"));
            }
        });
    }

    /**
     * This is the method for deleting data to the firestore
     */
    @Override
    public void deleteDataInFirestore() {
        final CollectionReference habitListReference = db.collection("Users")
                .document(logged.getUID()).collection("HabitList");
        final CollectionReference eventListReference = db.collection("Users")
                .document(logged.getUID()).collection("HabitList")
                .document(selectedTitle).collection("EventList");
        final StorageReference imageFolderReference = storageReference.child("eventPhotos/"+logged.getUID()+"/"+selectedTitle);


        habitListReference.document(selectedTitle)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Delete Habit", "Habit data has been deleted successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Delete Habit", "Error deleting document", e);
                    }
                });


        eventListReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    String date = (String) doc.getData().get("date");
                    eventListReference.document(date)
                            .delete();
                }
            }
        });

        imageFolderReference.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference item : listResult.getItems()) {
                            item.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
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
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });


        finish();
    }

    /**
     * This method is to update the image folder path on the firebase with new habit title
     * @param habitTitle habit title
     * @param imageName name of the image
     * @param imageData data if the image
     */
    private void uploadImageToFirebase(String habitTitle, String imageName, byte[] imageData) {
        final StorageReference imageReference = storageReference.child("eventPhotos/"+logged.getUID()+"/"+habitTitle+"/"+imageName);
        UploadTask uploadTask = imageReference.putBytes(imageData);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });


    }

}