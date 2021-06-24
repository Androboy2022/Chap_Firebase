package com.cyberkyj.firebase_memo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MemoActivity extends AppCompatActivity implements View.OnClickListener{

    ArrayList<MemoItem> memoItems;
    MemoAdater memoAdapter;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        init();
        initView();
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        addChildEvent();

    }

    private void init()
    {
        memoItems = new ArrayList<>();
        //username = "user_" + new Random().nextInt(1000);
    }

    private void initView()
    {
        Button regbtn = findViewById(R.id.memobtn);
        regbtn.setOnClickListener(this);

//        Button userbtn = findViewById(R.id.reguser);
//        userbtn.setOnClickListener(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerView = findViewById(R.id.memolist);
        memoAdapter = new MemoAdater(memoItems, this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(memoAdapter);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.memobtn:
                regMemo();
                break;

        }
    }

    private void regMemo() {

        EditText titleedit = findViewById(R.id.memotitle);
        EditText contentsedit = findViewById(R.id.memocontents);

        if (titleedit.getText().toString().length() == 0 ||
                contentsedit.getText().toString().length() == 0) {
            Toast.makeText(this,
                    "메모 제목 또는 메모 내용이 입력되지 않았습니다. 입력 후 다시 시작해주세요.",
                    Toast.LENGTH_LONG).show();
            return;
    }

        MemoItem item = new MemoItem();
        //item.setUser(this.username);
        item.setMemoTitle(titleedit.getText().toString());
        item.setMemoContents(contentsedit.getText().toString());

        //memoItems.add(item);
//        memoAdapter.items.add(item);
//        memoAdapter.notifyDataSetChanged();
        databaseReference.child("memo").push().setValue(item);

    }

    private void addChildEvent()
    {
        //databaseReference.child("memo").child(uid).addChildEventListener(new ChildEventListener()
        databaseReference.child("memo").addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                //Log.d("namjinha", "addChildEvent in");
                MemoItem item = dataSnapshot.getValue(MemoItem.class);

                memoAdapter.items.add(item);
                memoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }
}
