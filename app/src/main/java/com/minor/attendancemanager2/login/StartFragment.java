package com.minor.attendancemanager2.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.minor.attendancemanager2.HomeActivity;
import com.minor.attendancemanager2.R;

public class StartFragment extends Fragment {

    Button b;
    FirebaseAuth auth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_start, container, false);
        b = myView.findViewById(R.id.button1);
        auth =  FirebaseAuth.getInstance();
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.loginPage,new LogInFragment());
                ft.commit();
            }
        });
        return myView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(auth.getCurrentUser() != null){
            startActivity(new Intent(getActivity(),HomeActivity.class));
        }
    }
}