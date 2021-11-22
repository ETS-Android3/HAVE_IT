package com.example.have_it;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class HabitController {
        public static void addhabit(HashMap<String, Object> data , CollectionReference habitListReference,String title) {
            habitListReference
                    .document(title)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                   // Toast.makeText(getApplicationContext(),"cannot add because the habit with same title exists", Toast.LENGTH_LONG).show();
                                } else {
                                    habitListReference
                                            .document(title)
                                            .set(data)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // These are a method which gets executed when the task is succeeded
                                                    Log.d("Adding Habit", "Habit data has been added successfully!");

                                                    //finish();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // These are a method which gets executed if there’s any problem
                                                    Log.d("Adding Habit", "Habit data could not be added!" + e.toString());
                                                    //Toast.makeText(getApplicationContext(),"Not being able to add data, please check duplication title", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                }
                            }
                        }
                    });




        }
    public static void updateHabitWithsametitle( CollectionReference habitListReference,String selectedTitle,HashMap<String, Object> data ) {
                        habitListReference.document(selectedTitle)
                        .update(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("Edit Habit", "Habit data has been deleted successfully!");
                                //finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("Edit Habit", "Error deleting document", e);
                            }
                        });

    }
    public static void deleteHabit(CollectionReference habitListReference, CollectionReference eventListReference,String selectedTitle){

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

    }
    public static void updateHabitWithdifferenttitle(CollectionReference habitListReference, CollectionReference eventListReference,String selectedTitle ,String title,HashMap<String, Object> data){
        habitListReference
                .document(title)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                               // Toast.makeText(getApplicationContext(), "cannot edit because the habit with same title exists", Toast.LENGTH_LONG).show();
                            } else {
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
                                               // Toast.makeText(getApplicationContext(), "Not being able to edit data, please check duplication title", Toast.LENGTH_LONG).show();
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
                                                            //Toast.makeText(getApplicationContext(), "Not being able to edit data, please check duplication title", Toast.LENGTH_LONG).show();
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
                                               // finish();
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
    public static void getCollectionHabit(CollectionReference habitListReference, ArrayList<Habit> habitDataList,HabitList habitAdapter, ArrayList<Habit> todayHabitDataList,HabitList todayHabitAdapter) {
        habitListReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                habitDataList.clear();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    String title = (String) doc.getData().get("title");
                    String reason = (String) doc.getData().get("reason");
                    Timestamp startTimestamp = (Timestamp) doc.getData().get("dateStart");
                    Date dateStart = startTimestamp.toDate();
                    List<Boolean> weekdayRegArray = (List<Boolean>) doc.getData().get("weekdayReg");
                    Boolean publicity = (Boolean) doc.getData().get("publicity");
                    habitDataList.add(new Habit(title, reason, dateStart, (ArrayList<Boolean>) weekdayRegArray, publicity));
                }
                habitAdapter.notifyDataSetChanged();
                todayHabitDataList.clear();
                ArrayList<Habit> todayTemp = habitAdapter.getTodayHabits();
                for (Habit each : todayTemp) {
                    todayHabitDataList.add(each);
                }
                todayHabitAdapter.notifyDataSetChanged();
            }
        });
    }


}
