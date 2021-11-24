package com.example.have_it;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class EventController {
    static void addEventToFirestore(CollectionReference eventListReference, String event, HashMap<String, Object> data,String newDate) {
        eventListReference.document(newDate).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Toast.makeText(getApplicationContext(),"cannot add event: another event at the same day", Toast.LENGTH_LONG).show();
                    } else {
                        eventListReference
                                .document(newDate)
                                .set(data)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // These are a method which gets executed when the task is succeeded
                                        Log.d("Adding event", "event data has been added successfully!");
                                        // finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // These are a method which gets executed if there’s any problem
                                        Log.d("Adding event", "Habit event could not be added!" + e.toString());
                                        //Toast.makeText(getApplicationContext(),"Not being able to add data", Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                }
            }
        });
    }

    static void editEventWithDiffDate(CollectionReference eventListReference, String selectedEventDate, HashMap<String, Object> data,String newDate) {
        eventListReference
                .document(newDate)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                               // Toast.makeText(getApplicationContext(), "cannot edit event: another event at the same day", Toast.LENGTH_LONG).show();
                            } else {
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
                                        .document(newDate)
                                        .set(data)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // These are a method which gets executed when the task is succeeded
                                                Log.d("Adding event", "event data has been edited successfully!");
                                                //finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // These are a method which gets executed if there’s any problem
                                                Log.d("Adding event", "Habit data could not be edited!" + e.toString());
                                               // Toast.makeText(getApplicationContext(), "Not being able to edit data, please check duplication event", Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        }
                    }
                });
    }
    static void deleteEvent( CollectionReference eventListReference,String selectedEventDate) {
                        eventListReference.document(selectedEventDate)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("Delete Event", "Habit data has been deleted successfully!");
                                //finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("Delete event", "Error deleting document", e);
                            }
                        });
    }
    static void editEventWithSameDate(CollectionReference eventListReference,String selectedEventDate,HashMap<String, Object>data) {
                        eventListReference
                        .document(selectedEventDate)
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
    static void getCollectionEvent(CollectionReference eventListReference, EventList eventAdapter, ArrayList<Event> eventDataList) {
        eventListReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                eventDataList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    String event = (String) doc.getData().get("event");
                    String sDate = (String) doc.getData().get("date");
                    eventDataList.add(new Event(event,sDate));
                }
                eventAdapter.notifyDataSetChanged();
            }
        });

    }


}
