package com.minor.attendancemanager2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.minor.attendancemanager2.home.AttendanceModal;
import com.minor.attendancemanager2.home.SubjectModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditActivity extends AppCompatActivity {

    EditText etSubject,etLecture;
    Button buttonEdit,buttonDelete;
    String subjectID;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    SubjectModel subjectModel;
    ArrayList<SubjectModel> subjectModelArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        etSubject = findViewById(R.id.editSubjectName);
        etLecture = findViewById(R.id.editLectureNumber);
        buttonEdit = findViewById(R.id.buttonEdit);
        buttonDelete = findViewById(R.id.buttonDelete);
        firebaseDatabase = FirebaseDatabase.getInstance();
        subjectModelArrayList = new ArrayList<>();
        subjectModel = getIntent().getParcelableExtra("subject");

        if(subjectModel != null){
            etSubject.setText(subjectModel.getSubject());
            etLecture.setText(subjectModel.getLecture());
            subjectID = subjectModel.getSubjectId();
        }

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String subject = etSubject.getText().toString();
                String lecture = etLecture.getText().toString();

                if(subject.isEmpty()){
                    etSubject.setError("Fill this!");
                }else if(lecture.isEmpty()){
                    etLecture.setError("Fill this!");
                } else {
                    Map<String, Object> map = new HashMap<>();
                    map.put("subject", subject);
                    map.put("lecture", lecture);
                    map.put("subjectId", subjectID);

                    databaseReference = firebaseDatabase.getReference("Subjects");
                    databaseReference.child(subjectID).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(EditActivity.this, "Subject Updated", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(EditActivity.this, HomeActivity.class));
                        }
                    });

                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference = firebaseDatabase.getReference("Attendance");
                databaseReference.child(subjectID).removeValue();
                databaseReference = firebaseDatabase.getReference("Subjects");
                databaseReference.child(subjectID).removeValue();
                if(subjectModelArrayList.size() == 0) {
                    databaseReference = firebaseDatabase.getReference("Percentage");
                    databaseReference.removeValue();
                }
                Toast.makeText(EditActivity.this, "Subject Deleted", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(EditActivity.this,HomeActivity.class));
            }
        });
    }
}