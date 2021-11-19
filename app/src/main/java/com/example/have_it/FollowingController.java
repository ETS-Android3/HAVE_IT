package com.example.have_it;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class FollowingController implements DatabaseUserReference{

    public static void getNowFollowing(GeneralUserList nowFollowingAdapter, ArrayList<GeneralUser> userDataList){
//        final DocumentReference
//        db.collection("User")
//                .document(logged.getUID())
//                .get()
//                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        userDataList.clear();
//                        ArrayList<String> UIDs =  new ArrayList<>((ArrayList<String>) documentSnapshot.getData().get("FollowingList"));
//                        for (String UID : UIDs){
//                            final String[] name = new String[1];
//                            db.collection("User")
//                                    .document(UID)
//                                    .get()
//                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>(){
//                                        @Override
//                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
//                                            name[0] = (String) documentSnapshot.get("Name");
//                                        }
//                                    });
//                            userDataList.add(new GeneralUser(UID, name[0]));
//                        }
//                        nowFollowingAdapter.notifyDataSetChanged();
//                    }
//                });
    }
}
