package com.example.have_it;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.List;


public class FollowingController implements DatabaseUserReference{

    public static void getNowFollowing(GeneralUserList nowFollowingAdapter, ArrayList<GeneralUser> userDataList){
        final DocumentReference usersReference = db.collection("Users").document(logged.getUID());
        usersReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                userDataList.clear();
                for (String each : (ArrayList<String>) documentSnapshot.getData().get("FollowingList")) {
                    setGeneralUser(each, nowFollowingAdapter, userDataList);
                }
            }
        });
    }

    public static void setGeneralUser(String UID,GeneralUserList nowFollowingAdapter, ArrayList<GeneralUser> userDataList){
        final DocumentReference usersReference = db.collection("Users").document(UID);
        usersReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String name = (String) documentSnapshot.getData().get("Name");
                userDataList.add(new GeneralUser(UID, name));
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

    public static void getUserList(final GeneralUserList usersAdapter, final ArrayList<GeneralUser> usersDataList){
        final CollectionReference usersReference = db.collection("Users");
        usersReference.addSnapshotListener(new EventListener<QuerySnapshot>(){
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots
                    , @Nullable FirebaseFirestoreException error) {
                usersDataList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                    String UID = doc.getId();
                    String name = (String) doc.getData().get("Name");
                    if (!UID.equals(logged.getUID())) {
                        usersDataList.add(new GeneralUser(UID, name));
                    }
                }
                usersAdapter.notifyDataSetChanged();
            }
        });
    }
}
