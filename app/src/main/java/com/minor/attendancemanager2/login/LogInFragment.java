package com.minor.attendancemanager2.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.minor.attendancemanager2.HomeActivity;
import com.minor.attendancemanager2.R;

public class LogInFragment extends Fragment {

    EditText et1,et2;
    FirebaseAuth auth;
    Button b;
    TextView tv;
    ProgressDialog pd;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_log_in, container, false);
        et1 = myView.findViewById(R.id.etLoginEmail);
        et2 = myView.findViewById(R.id.etLoginPass);
        b = myView.findViewById(R.id.buttonLogin);
        tv = myView.findViewById(R.id.tvSignUp);
        auth = FirebaseAuth.getInstance();

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd = new ProgressDialog(getActivity());
                pd.setMessage("Please Wait");
                pd.show();
                String email = et1.getText().toString();
                String password = et2.getText().toString();

                if(email.isEmpty() || password.isEmpty()){
                    pd.dismiss();
                    et1.setError("Fill id or password");
                }
                else{
                    auth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            pd.dismiss();
                            startActivity(new Intent(getActivity(), HomeActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(getActivity(), "Id or password is incorrect", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.loginPage,new CreateAccFragment());
                ft.commit();
                ft.addToBackStack("");
            }
        });

        return myView;
    }

}