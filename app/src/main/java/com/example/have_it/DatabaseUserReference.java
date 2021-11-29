package com.example.have_it;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * This interface only provide reference tot he firestore database and the logged user status
 */
public interface DatabaseUserReference {
    /**
     * Reference to the database, of class {@link FirebaseFirestore}
     */
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    /**
     * Reference to the logged user, of class {@link User}
     */
    User logged = User.getInstance();
    /**
     * Reference to the firebase storage
     */
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
}
