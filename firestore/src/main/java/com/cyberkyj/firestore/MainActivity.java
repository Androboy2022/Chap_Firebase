package com.cyberkyj.firestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button setdatabtn = findViewById(R.id.firestoresetdatabtn);
        setdatabtn.setOnClickListener(this);

        Button deletedocbtn = findViewById(R.id.firestoredeletedocbtn);
        deletedocbtn.setOnClickListener(this);

        Button deletefieldbtn = findViewById(R.id.firestoredeletefieldbtn);
        deletefieldbtn.setOnClickListener(this);

        Button selectdocbtn = findViewById(R.id.firestoreseldatabtn);
        selectdocbtn.setOnClickListener(this);

        Button selectwheredocbtn = findViewById(R.id.firestoreselwheredatabtn);
        selectwheredocbtn.setOnClickListener(this);

        Button listenerdocbtn = findViewById(R.id.firestorelistenerdatabtn);
        listenerdocbtn.setOnClickListener(this);

        Button listenerQuerydocbtn = findViewById(R.id.firestorelistenerquerydatabtn);
        listenerQuerydocbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {

            case R.id.firestoresetdatabtn:
                setData();
                break;
            case R.id.firestoredeletedocbtn:
                deleteDoc();
                break;
            case R.id.firestoredeletefieldbtn:
                deleteField();
                break;
            case R.id.firestoreseldatabtn:
                selectDoc();
                break;
            case R.id.firestoreselwheredatabtn:
                selectWhereDoc();
                break;
            case R.id.firestorelistenerdatabtn:
                listenerDoc();
                break;
            case R.id.firestorelistenerquerydatabtn:
                listeneQueryDoc();
                break;
        }
    }

    private void setData()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> member = new HashMap<>();
        member.put("name", "아이유");
        member.put("address", "서울");
        member.put("age", 30);


        db.collection("member").document("iulove").set(member)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void deleteDoc()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("member").document("cyberkyj").delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void deleteField()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String,Object> updates = new HashMap<>();
        updates.put("job", FieldValue.delete());

        db.collection("member").document("iulove").update(updates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(),"성공",Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"실패",Toast.LENGTH_LONG).show();

            }
        });
    }

    private void selectDoc()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("member").document("iulove");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                if (task.isSuccessful())
                {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists())
                    {
                        //Log.d("Data", "DocumentSnapshot data: " + document.getData());
                        UserInfo userInfo = document.toObject(UserInfo.class);
                        Log.d("Data","name = "+userInfo.getName());
                        Log.d("Data","address = "+userInfo.getAddress());
                        Log.d("Data", "id = "+userInfo.getId());
                        Log.d("Data","pwd = "+userInfo.getPwd());
                        Log.d("Data","age = "+userInfo.getAge());

                    }
                    else
                    {
                        Log.d("Data", "No such document");
                    }
                }
                else
                {
                    Log.d("Data", "get failed with ", task.getException());
                }
            }
        });
    }

    private void selectWhereDoc(){
        FirebaseFirestore db= FirebaseFirestore.getInstance();
        db.collection("member").whereEqualTo("age",30)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    for(QueryDocumentSnapshot document : task.getResult()){

                        Log.d("Data",document.getId()+" => "+document.getData());
                        UserInfo userInfo = document.toObject(UserInfo.class);
                        Log.d("Data","name = "+userInfo.getName());
                        Log.d("Data","address = "+userInfo.getAddress());
                        Log.d("Data", "id = "+userInfo.getId());
                        Log.d("Data","pwd = "+userInfo.getPwd());
                        Log.d("Data","age = "+userInfo.getAge());
                    }
                }
            }
        });
    }


    private void listenerDoc()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("member").document("iulove");
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>()
        {
            @Override
            public void onEvent(DocumentSnapshot snapshot, FirebaseFirestoreException e)
            {
                if (snapshot != null && snapshot.exists())
                {
                    Log.d("Data", "Current data: " + snapshot.getData());
                }
                else
                {
                    Log.d("Data", "Current data: null");
                }
            }
        });
    }

    private void listeneQueryDoc(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("member").whereEqualTo("id","btslove")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        for(DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()){
                            Log.d("Data", "listeneQueryDoc dc.getType() = "+dc.getType());
                            switch (dc.getType()){
                                case ADDED:
                                    Log.d("Data", "New city: "+dc.getDocument().getData());
                                    break;
                                case MODIFIED:
                                    Log.d("Data", "Modified city: "+dc.getDocument().getData());
                                    break;
                                case REMOVED:
                                    Log.d("Data", "Remove city: "+dc.getDocument().getData());
                                    break;
                            }
                        }
                    }
                });
    }



}
