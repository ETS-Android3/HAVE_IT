package com.example.have_it;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class FollowingController implements DatabaseUserReference{

    public static void getNowFollowing(FollowingUserList nowFollowingAdapter, ArrayList<GeneralUser> userDataList){
        final CollectionReference followingListReference = db.collection("Users")
                .document(logged.getUID())
                .collection("FollowingList");
        followingListReference.addSnapshotListener(new EventListener<QuerySnapshot>(){
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots
                    , @Nullable FirebaseFirestoreException error) {
                userDataList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                    String UID = (String) doc.getData().get("UID");
                    String name = (String) doc.getData().get("Name");
                    userDataList.add(new GeneralUser(UID, name));
                }
                nowFollowingAdapter.notifyDataSetChanged();
            }
        });
    }

    public static void setUserHabit(String UID, final HabitList habitAdapter, final ArrayList<Habit> habitDataList){
        final CollectionReference habitListReference = db.collection("Users")
                .document(UID).collection("HabitList");

        habitListReference.addSnapshotListener(new EventListener<QuerySnapshot>(){
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots
                    , @Nullable FirebaseFirestoreException error) {
                habitDataList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                    String title = (String) doc.getData().get("title");
                    String reason = (String) doc.getData().get("reason");
                    Timestamp startTimestamp = (Timestamp) doc.getData().get("dateStart");
                    Date dateStart = startTimestamp.toDate();
                    List<Boolean> weekdayRegArray = (List<Boolean>) doc.getData().get("weekdayReg");
                    Boolean publicity = (Boolean) doc.getData().get("publicity");
                    if (publicity) {
                        habitDataList.add(new Habit(title, reason, dateStart, (ArrayList<Boolean>) weekdayRegArray, publicity));
                    }
                }
                habitAdapter.notifyDataSetChanged();
            }
        });
    }

    public static void getUserList(final NewFollowUserList usersAdapter, final ArrayList<NewFollowUser> usersDataList
            , RequestedUserList requestedAdapter, FollowingUserList followingAdapter){
        final CollectionReference usersReference = db.collection("Users");
        usersReference.addSnapshotListener(new EventListener<QuerySnapshot>(){
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots
                    , @Nullable FirebaseFirestoreException error) {
                usersDataList.clear();
                ArrayList<String> requestedUID = requestedAdapter.getUID();
                ArrayList<String> followingUID = followingAdapter.getUID();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                    String UID = doc.getId();
                    String name = (String) doc.getData().get("Name");
                    boolean following = followingUID.contains(UID);
                    if (!UID.equals(logged.getUID())) {
                        usersDataList.add(new NewFollowUser(UID, name
                                , requestedUID.contains(UID), followingUID.contains(UID)));
                    }
                }
                usersAdapter.notifyDataSetChanged();
            }
        });
    }

    public static void sendRequest(NewFollowUser userSelection){
        final CollectionReference requestedListReference = db.collection("Users")
                .document(logged.getUID())
                .collection("RequestedList");
        HashMap<String, Object> requestedData = new HashMap<>();
        requestedData.put("Name",userSelection.getName());
        requestedData.put("UID",userSelection.getUID());
        requestedData.put("allowed",false);
        requestedData.put("replied",false);

        requestedListReference
                .document(userSelection.getUID())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if (!document.exists()){
                                requestedListReference
                                        .document(userSelection.getUID())
                                        .set(requestedData)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // These are a method which gets executed when the task is succeeded
                                                Log.d("Sending request", "Request sent successfully");
                                                //finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // These are a method which gets executed if there’s any problem
                                                Log.d("Sending request", "Failed due to " + e.toString());
                                                //Toast.makeText(getApplicationContext(),"Not being able to add data, please check duplication title", Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        }
                    }
                });

        final CollectionReference receivedRequestListReference = db.collection("Users")
                .document(userSelection.getUID())
                .collection("ReceivedRequestList");
        final DocumentReference usersReference = db.collection("Users").document(logged.getUID());
        usersReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                HashMap<String, Object> receivedRequestData = new HashMap<>();
                receivedRequestData.put("Name",(String) documentSnapshot.getData().get("Name"));
                receivedRequestData.put("UID",logged.getUID());
                receivedRequestListReference
                        .document(logged.getUID())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    DocumentSnapshot document = task.getResult();
                                    if (!document.exists()){
                                        receivedRequestListReference
                                                .document(logged.getUID())
                                                .set(receivedRequestData)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        // These are a method which gets executed when the task is succeeded
                                                        Log.d("Sending request", "Request sent successfully");
                                                        //finish();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        // These are a method which gets executed if there’s any problem
                                                        Log.d("Sending request", "Failed due to " + e.toString());
                                                        //Toast.makeText(getApplicationContext(),"Not being able to add data, please check duplication title", Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                    }
                                }
                            }
                        });
            }
        });
    }

    public static void getRequestedList(final RequestedUserList usersAdapter, final ArrayList<RequestedUser> usersDataList){
        final CollectionReference requestedListReference = db.collection("Users")
                .document(logged.getUID())
                .collection("RequestedList");
        requestedListReference.addSnapshotListener(new EventListener<QuerySnapshot>(){
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots
                    , @Nullable FirebaseFirestoreException error) {
                usersDataList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                    String UID = (String) doc.getData().get("UID");
                    String name = (String) doc.getData().get("Name");
                    boolean allowed = (boolean) doc.getData().get("allowed");
                    boolean replied = (boolean) doc.getData().get("replied");
                    usersDataList.add(new RequestedUser(UID, name, allowed, replied));
                }
                usersAdapter.notifyDataSetChanged();
            }
        });
    }

    public static void getFollowingRequest(FollowingRequestUserList requestedUserAdapter, ArrayList<GeneralUser> userDataList){
        final CollectionReference requestedListReference = db.collection("Users")
                .document(logged.getUID())
                .collection("ReceivedRequestList");
        requestedListReference.addSnapshotListener(new EventListener<QuerySnapshot>(){
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots
                    , @Nullable FirebaseFirestoreException error) {
                userDataList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                    String UID = (String) doc.getData().get("UID");
                    String name = (String) doc.getData().get("Name");
                    userDataList.add(new GeneralUser(UID, name));
                }
                requestedUserAdapter.notifyDataSetChanged();
            }
        });
    }

    public static void confirmFollowingRequest(GeneralUser userSelection){
        final DocumentReference usersReference = db.collection("Users").document(logged.getUID());
        usersReference
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                      HashMap<String, Object> requestedData = new HashMap<>();
                      requestedData.put("Name",(String) documentSnapshot.getData().get("Name"));
                      requestedData.put("UID",logged.getUID());
                      requestedData.put("allowed",true);
                      requestedData.put("replied",true);
                      replyFollowingRequest(userSelection,requestedData, true);
                    }
                });
    }

    public static void rejectFollowingRequest(GeneralUser userSelection){
        final DocumentReference usersReference = db.collection("Users").document(logged.getUID());
        usersReference
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        HashMap<String, Object> requestedData = new HashMap<>();
                        requestedData.put("Name",(String) documentSnapshot.getData().get("Name"));
                        requestedData.put("UID",logged.getUID());
                        requestedData.put("allowed",false);
                        requestedData.put("replied",true);
                        replyFollowingRequest(userSelection,requestedData, false);
                    }
                });
    }

    private static void replyFollowingRequest(GeneralUser userSelection
            , HashMap<String, Object> requestedData, boolean allowed){
        final CollectionReference requestedListReference = db.collection("Users")
                .document(userSelection.getUID())
                .collection("RequestedList");

        requestedListReference
                .document(logged.getUID())
                .update(requestedData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Requested update", "Updated");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Requested update", "Error updating", e);
                    }
                });

        final CollectionReference receivedRequestListReference = db.collection("Users")
                .document(logged.getUID())
                .collection("ReceivedRequestList");

        receivedRequestListReference
                .document(userSelection.getUID())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Delete replied request", "Done deleting");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Delete replied request", "Error deleting", e);
                    }
                });

        if (allowed){
            final CollectionReference followingListReference = db.collection("Users")
                    .document(userSelection.getUID())
                    .collection("FollowingList");
            HashMap<String, Object> followingData = new HashMap<>();
            followingData.put("Name", requestedData.get("Name"));
            followingData.put("UID", requestedData.get("UID"));
            followingListReference
                .document(logged.getUID())
                .set(followingData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // These are a method which gets executed when the task is succeeded
                        Log.d("Set following", "Set following success");
                        //finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // These are a method which gets executed if there’s any problem
                        Log.d("Set following", "Set following failed " + e.toString());
                        //Toast.makeText(getApplicationContext(),"Not being able to add data, please check duplication title", Toast.LENGTH_LONG).show();
                    }
                });
        }
    }

    public static void setBadge(TabLayout tabLayout){
        final CollectionReference requestedListReference = db.collection("Users")
                .document(logged.getUID())
                .collection("ReceivedRequestList");
        requestedListReference.addSnapshotListener(new EventListener<QuerySnapshot>(){
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots
                    , @Nullable FirebaseFirestoreException error) {
                int count = 0;
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                    count++;
                }
                tabLayout.getTabAt(2).getOrCreateBadge().setNumber(count);
            }
        });
    }
}
