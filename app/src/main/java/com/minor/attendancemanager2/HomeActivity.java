package com.minor.attendancemanager2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.minor.attendancemanager2.home.AttendanceModal;
import com.minor.attendancemanager2.home.CriteriaModal;
import com.minor.attendancemanager2.home.PercentageModal;
import com.minor.attendancemanager2.home.SubjectAdapter;
import com.minor.attendancemanager2.home.SubjectModel;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FloatingActionButton addButton;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    RecyclerView subjectRV;
    ArrayList<SubjectModel> subjectModelArrayList;
    SubjectAdapter subjectAdapter;
    RelativeLayout homeRL;
    NumberPicker numberPicker;
    Button setCriteria;
    TextView overall;
    int criteria;
    int pre=0, abs=0;
    int over_pre,over_abs;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        auth = FirebaseAuth.getInstance();
        subjectRV = findViewById(R.id.rvSubject);
        addButton = findViewById(R.id.idFABAddSubject);
        homeRL = findViewById(R.id.rlSheet);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Subjects");
        subjectModelArrayList = new ArrayList<>();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,AddActivity.class));
            }
        });

        subjectAdapter = new SubjectAdapter(subjectModelArrayList,this,this::onSubjectClick);
        subjectRV.setLayoutManager(new LinearLayoutManager(this));
        subjectRV.setAdapter(subjectAdapter);
        getSubject();
    }

    public void getSubject(){
        subjectModelArrayList.clear();
        databaseReference = firebaseDatabase.getReference("Subjects");
        overall = findViewById(R.id.TVOverall);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                subjectModelArrayList.add(snapshot.getValue(SubjectModel.class));
                subjectAdapter.notifyDataSetChanged();
                if(subjectModelArrayList.size() != 0){
                    overall.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                subjectAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                subjectAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                subjectAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        getOverall();
    }

    @SuppressLint("SuspiciousIndentation")
    public void getOverall(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Attendance");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    over_pre = over_abs = 0;
                    for(DataSnapshot data : snapshot.getChildren()){
                        AttendanceModal attendanceModal = data.getValue(AttendanceModal.class);
                        over_pre += attendanceModal.getPresent();
                        over_abs += attendanceModal.getAbsent();
                    }
                    if(over_abs != 0) {
                        setPercentage();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }

    public void setPercentage(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Percentage");
        float over = (float) over_pre/over_abs * 100;
        PercentageModal percentageModal = new PercentageModal(over);
        databaseReference.setValue(percentageModal);
    }

    public void getPercentage(){
        overall = findViewById(R.id.TVOverall);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Percentage");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                float per = snapshot.getValue(Float.class);
                String p = String.format("%.2f",per);
                overall.setText(p + "%");
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                float per = snapshot.getValue(Float.class);
                String p = String.format("%.2f",per);
                overall.setText(p + "%");
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                float per = snapshot.getValue(Float.class);
                String p = String.format("%.2f",per);
                overall.setText(p + "%");
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                float per = snapshot.getValue(Float.class);
                String p = String.format("%.2f",per);
                overall.setText(p + "%");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void onSubjectClick(int position){
        displayBottomSheet(subjectModelArrayList.get(position));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.signout:
                auth.signOut();
                startActivity(new Intent(HomeActivity.this,LoginActivity.class));

            case R.id.criteria:
                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference("Criteria");
                Dialog dialog = new Dialog(HomeActivity.this);
                dialog.setContentView(R.layout.criteria_dialog);
                dialog.setCanceledOnTouchOutside(false);
                numberPicker = dialog.findViewById(R.id.criteriaPicker);
                setCriteria = dialog.findViewById(R.id.setCriteria);
                numberPicker.setMaxValue(100);
                numberPicker.setValue(criteria);
                numberPicker.setMinValue(5);
                dialog.show();
                numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                        criteria = i1;
                    }
                });
                setCriteria.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        CriteriaModal criteriaModal = new CriteriaModal(criteria);
                        databaseReference.setValue(criteriaModal);
                        Toast.makeText(HomeActivity.this, "Updated Criteria : " + criteria, Toast.LENGTH_SHORT).show();
                    }
                });
        }
        return super.onOptionsItemSelected(item);
    }

    public void displayBottomSheet(SubjectModel model){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, com.google.android.material.R.style.Theme_Design_BottomSheetDialog);
        View layout = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_layout, homeRL);
        bottomSheetDialog.setContentView(layout);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        bottomSheetDialog.show();

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView presentNumberTV = layout.findViewById(R.id.TVPresentNumber);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView absentNumberTV = layout.findViewById(R.id.TVAbsentNumber);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button presentBTN = layout.findViewById(R.id.BtnPresent);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button absentBTN = layout.findViewById(R.id.BtnAbsent);
        TextView subjectTV = layout.findViewById(R.id.idTVSubjectName);
        TextView lectureTV = layout.findViewById(R.id.idTVLecture);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) FloatingActionButton subBtn = layout.findViewById(R.id.TVSubtract);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView percentageTV = layout.findViewById(R.id.idTVPercentage);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView classesTV = layout.findViewById(R.id.idTVNoOfClasses);

        if(criteria == 0){
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference("Criteria");
            Dialog dialog = new Dialog(HomeActivity.this);
            dialog.setContentView(R.layout.criteria_dialog);
            dialog.setCanceledOnTouchOutside(false);
            numberPicker = dialog.findViewById(R.id.criteriaPicker);
            setCriteria = dialog.findViewById(R.id.setCriteria);
            numberPicker.setMaxValue(100);
            numberPicker.setValue(50);
            numberPicker.setMinValue(5);
            numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                    criteria = i1;
                }
            });
            setCriteria.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    databaseReference = firebaseDatabase.getReference("Criteria");
                    dialog.dismiss();
                    CriteriaModal criteriaModal = new CriteriaModal(criteria);
                    databaseReference.setValue(criteriaModal);
                    Toast.makeText(HomeActivity.this, "Criteria : " + criteria, Toast.LENGTH_SHORT).show();
                }
            });
            dialog.show();
            bottomSheetDialog.hide();
        }

        int lec = Integer.parseInt(model.getLecture());
        float c = (float)lec * criteria/100;

        //
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Attendance");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pre = snapshot.child(model.getSubjectId()).child("present").getValue(Integer.class);
                abs = snapshot.child(model.getSubjectId()).child("absent").getValue(Integer.class);
                float percentage =(float) pre/abs * 100;
                String pr = String.valueOf(pre);
                String ab = String.valueOf(abs);
                String pc = String.format("%.2f",percentage);
                presentNumberTV.setText(pr);
                absentNumberTV.setText(ab);
                percentageTV.setText("Percentage: " + pc + "%");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //

        presentBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pre < lec && abs < lec) {
                    pre++;
                    abs++;
                    //
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    databaseReference = firebaseDatabase.getReference("Attendance");
                    AttendanceModal attendanceModal = new AttendanceModal(pre,abs);
                    databaseReference.child(model.getSubjectId()).setValue(attendanceModal);
                    //
                    String pr = String.valueOf(pre);
                    String ab = String.valueOf(abs);
                    presentNumberTV.setText(pr);
                    absentNumberTV.setText(ab);
                }
            }
        });

        absentBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(abs < lec) {
                    abs++;
                    //
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    databaseReference = firebaseDatabase.getReference("Attendance");
                    AttendanceModal attendanceModal = new AttendanceModal(pre,abs);
                    databaseReference.child(model.getSubjectId()).setValue(attendanceModal);
                    //
                    String ab = String.valueOf(abs);
                    absentNumberTV.setText(ab);
                }
            }
        });

        subBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pre > 0) {
                    pre--;
                }
                if(abs > 0) {
                    abs--;
                }
                //
                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference("Attendance");
                AttendanceModal attendanceModal = new AttendanceModal(pre,abs);
                databaseReference.child(model.getSubjectId()).setValue(attendanceModal);
                //
                String pr = String.valueOf(pre);
                String ab = String.valueOf(abs);
                presentNumberTV.setText(pr);
                absentNumberTV.setText(ab);
            }
        });

        subjectTV.setText("Subject: " + model.getSubject());
        lectureTV.setText("Lecture: " + model.getLecture());
        classesTV.setText(Math.round(c) + " lectures must be ATTEND to attain " + criteria + "%");

        Button editBtn = layout.findViewById(R.id.idBtnEditCourse);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(HomeActivity.this, EditActivity.class);
                i.putExtra("subject", model);
                startActivity(i);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Criteria");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                criteria = snapshot.getValue(Integer.class);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                criteria = snapshot.getValue(Integer.class);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                criteria = snapshot.getValue(Integer.class);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                criteria = snapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        getPercentage();
    }

}