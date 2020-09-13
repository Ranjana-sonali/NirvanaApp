package com.example.nirvana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.time.Year;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class DoctorPhoneVerification extends AppCompatActivity {
    private PhoneAuthCredential credential;
    private FirebaseAuth mAuth;
    private Task<Void> databaseReference;
    private StorageReference mStorageRef;
    private EditText code;
    private ProgressBar progressBar;
    private String Uname,Email,Phone,Address,Gender,Fname,Lname,Password,Affiliation,LinkedIn,Year_Of_Practice,Place_Of_Practice,Id,totalpatient,satisfiedpatient,rating,ratingby,link;
    private String mVerificationId,code1;
    private ArrayList<String> arr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_phone_verification);
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        progressBar=findViewById(R.id.progress_verify);
        Intent intent=getIntent();
        arr=intent.getStringArrayListExtra("arr");
        Uname=arr.get(0);
        Email=arr.get(1);
        Phone=arr.get(2);
        Address=arr.get(3);
        Gender=arr.get(4);
        Fname=arr.get(5);
        Lname=arr.get(6);
        Password=arr.get(7);
        Affiliation=arr.get(8);
        LinkedIn=arr.get(9);
        Year_Of_Practice=arr.get(10);
        Place_Of_Practice=arr.get(11);
        link=arr.get(12);
        totalpatient="0";
        satisfiedpatient="0";
        rating="0";
        ratingby="0";
        sendVerificationCode(Phone);
    }
    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobile,                 //phoneNo that is given by user
                60,                             //Timeout Duration
                TimeUnit.SECONDS,                   //Unit of Timeout
                TaskExecutors.MAIN_THREAD,          //Work done on main Thread
                mCallbacks);                       // OnVerificationStateChangedCallbacks
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                    //Getting the code sent by SMS
                    code1 = phoneAuthCredential.getSmsCode();

                    //sometime the code is not detected automatically
                    //in this case the code will be null
                    //so user has to manually enter the code
                    if (code1 != null) {
                        code=findViewById(R.id.code_doctor);
                        code.setText(code1);
                        //verifying the code
                        verifyVerificationCode(code1);
                    }
                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                    Toast.makeText(DoctorPhoneVerification.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }

                //when the code is generated then this method will receive the code.
                @Override
                public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//                super.onCodeSent(s, forceResendingToken);

                    //storing the verification id that is sent to the user
                    mVerificationId = s;
                }
            };

    private void verifyVerificationCode(String code) {
        //creating the credential
        try {
            credential = PhoneAuthProvider.getCredential(mVerificationId, code);
            progressBar.setVisibility(View.VISIBLE);
            signInWithPhoneAuthCredential(credential);
        }
        catch(Exception e)
        {
            Toast.makeText(DoctorPhoneVerification.this,"Please make sure that sim of entered number is available in your phone",Toast.LENGTH_SHORT).show();
        }
    }
    public void verify_doctor(View view) {
        credential = PhoneAuthProvider.getCredential(mVerificationId, code1);
        progressBar.setVisibility(View.VISIBLE);
        signInWithPhoneAuthCredential(credential);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(DoctorPhoneVerification.this,
                        new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Id=mAuth.getCurrentUser().getUid();
                                    //verification successful we will start the profile activity
                                    doctor_details doctor_details=new doctor_details(
                                            Uname,
                                            Email,
                                            Phone,
                                            Address,
                                            Gender,
                                            Fname,
                                            Lname,
                                            Password,
                                            Affiliation,
                                            Year_Of_Practice,
                                            Place_Of_Practice,
                                            LinkedIn,
                                            Id,
                                            totalpatient,
                                            satisfiedpatient,
                                            rating,
                                            ratingby,
                                            link
                                    );

                                    databaseReference= FirebaseDatabase.getInstance().getReference("Doctors").child(Phone)
                                            .setValue(doctor_details).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    progressBar.setVisibility(View.GONE);
                                                    Toast.makeText(DoctorPhoneVerification.this,"Successfully signed up",Toast.LENGTH_SHORT).show();
                                                    Intent intent=new Intent(DoctorPhoneVerification.this,Doctor_Welcome_Activity.class);
                                                    intent.putExtra("phone",Phone);
                                                    startActivity(intent);


                                                }
                                            });

                                } else {

                                    //verification unsuccessful.. display an error message

                                    String message = "Somthing is wrong, we will fix it soon...";

                                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                        message = "Invalid code entered...";
                                    }
                                    Toast.makeText(DoctorPhoneVerification.this,message,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
    }


}
