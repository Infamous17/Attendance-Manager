package com.minor.attendancemanager2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.minor.attendancemanager2.home.AttendanceModal;
import com.minor.attendancemanager2.home.SubjectModel;

public class AddActivity extends AppCompatActivity {

    Button btnAdd;
    EditText etsub,etlec;
    String subjectId;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        btnAdd = findViewById(R.id.etAdd);
        etsub = findViewById(R.id.etSubjectName);
        etlec = findViewById(R.id.etLecture);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Subjects");

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String subject = etsub.getText().toString();
                String lecture = etlec.getText().toString();
                subjectId = subject;
                //
                int pre=0,abs=0;
                AttendanceModal attendanceModal = new AttendanceModal(pre,abs);
                //
                SubjectModel subjectModel = new SubjectModel(subject,lecture,subjectId);

                if(subject.isEmpty()){
                    etsub.setError("Fill this!");
                }else if(lecture.isEmpty()){
                    etlec.setError("Fill this!");
                } else {
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            databaseReference = firebaseDatabase.getReference("Subjects");
                            databaseReference.child(subjectId).setValue(subjectModel);
                            //
                            databaseReference = firebaseDatabase.getReference("Attendance");
                            databaseReference.child(subjectId).setValue(attendanceModal);
                            //
                            startActivity(new Intent(AddActivity.this, HomeActivity.class));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(AddActivity.this, "Failed to add", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}