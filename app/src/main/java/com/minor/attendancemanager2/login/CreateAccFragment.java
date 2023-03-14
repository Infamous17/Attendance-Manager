package com.minor.attendancemanager2.login;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.minor.attendancemanager2.R;

public class CreateAccFragment extends Fragment {

    EditText et1,et2,et3,et4;
    Button b1;
    FirebaseAuth auth;
    ProgressDialog pd;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_create_acc, container, false);
        et1 = myView.findViewById(R.id.etCustomerEmail);
        et2 = myView.findViewById(R.id.etCustomerPass);
        et3 = myView.findViewById(R.id.etCustomerConfirmPass);
        et4 = myView.findViewById(R.id.etCustomerName);
        b1 = myView.findViewById(R.id.buttonSubmit);
        auth = FirebaseAuth.getInstance();

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = et1.getText().toString();
                String name = et4.getText().toString();
                String password = et2.getText().toString();
                String confirmPassword = et3.getText().toString();
                pd = new ProgressDialog(getActivity());
                pd.setMessage("Please Wait");
                pd.show();

                if(name.isEmpty() || email.isEmpty()){
                    pd.dismiss();
                    Toast.makeText(getActivity(), "Fill the given information", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(password.equals(confirmPassword)){
                        auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                pd.dismiss();
                                FragmentManager fm = getFragmentManager();
                                FragmentTransaction ft = fm.beginTransaction();
                                ft.replace(R.id.loginPage, new LogInFragment());
                                ft.commit();
                                ft.addToBackStack("");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                pd.dismiss();
                                Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else{
                        pd.dismiss();
                        Toast.makeText(getActivity(), "Passwords didn't match", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return myView;
    }
}