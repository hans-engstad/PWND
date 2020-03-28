package com.mygdx.game;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mygdx.game.interfaces.ICreateCallback;
import com.mygdx.game.interfaces.IFirebase;
import com.mygdx.game.interfaces.IRetrieveCallback;
import com.mygdx.game.interfaces.IUpdateCallback;
import com.mygdx.game.models.Match;

import java.util.HashMap;
import java.util.Map;


public class Firebase implements IFirebase {


    private final String MATCHES_KEY = "matches";

    private FirebaseFirestore db;

    ListenerRegistration matchUpdateListener;

    public Firebase(){
        db = FirebaseFirestore.getInstance();
    }

    public void addMatch(Match match, final ICreateCallback callback){
        db.collection(MATCHES_KEY)
                .add(match.serialize())
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        callback.onSuccess(documentReference.getId());

                        matchUpdateListener = documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>(){
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                                @Nullable FirebaseFirestoreException e) {
                                if (e != null) {
                                    // Event listen failed
                                    return;
                                }

                                if (snapshot != null && snapshot.exists()) {
                                    // Current data updated
                                    callback.onChange(snapshot.getData());
                                } else {
                                    // Current data is null
                                    callback.onDestroy();
                                }
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onFail(e);
                    }
                });
    }

    public void updateMatch(Match match, final IUpdateCallback callback){
        db.collection(MATCHES_KEY).document(match.getId())
                .update(match.serialize())
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        callback.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onFail(e);
                    }
                });
    }

    public void retrieveOpenMatches(final IRetrieveCallback callback){
        db.collection(MATCHES_KEY).whereEqualTo("status", Match.Status.OPEN.toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            Map<String, Object> data = new HashMap<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                data.put(document.getId(), document.getData());
                            }

                            callback.onSuccess(data);
                        }
                        else{
                            callback.onFail(task.getException());
                        }

                    }
                });
    }

    public void clearListeners (){
        if (matchUpdateListener != null){
            matchUpdateListener.remove();
        }
    }


}
