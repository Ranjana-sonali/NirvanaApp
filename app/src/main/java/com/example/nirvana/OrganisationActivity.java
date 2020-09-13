package com.example.nirvana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class OrganisationActivity extends AppCompatActivity {
    private Spinner spinner;
    private ArrayList<String> arrayList;
    FirebaseDatabase firebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organisation);
        HomeOrganisation homeOrganisation=new HomeOrganisation();
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_organisation,homeOrganisation);
        fragmentTransaction.commit();
    }

    private boolean isValidEmailId(String email){

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }
    public void office_register(View view) {
        OfficeSignupFragment officeSignupFragment=new OfficeSignupFragment();
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_organisation,officeSignupFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void school_register(View view) {
        SchoolSignupFragment  schoolSignupFragment=new SchoolSignupFragment();
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_organisation,schoolSignupFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void clinic_register(View view) {
        ClinicSignupFragment clinicSignupFragment=new ClinicSignupFragment();
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_organisation,clinicSignupFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void hospital_register(View view) {
        HospitalSignupFragment hospitalSignupFragment=new HospitalSignupFragment();
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_organisation,hospitalSignupFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void login_school(View view) {
        Intent intent=new Intent(this,SchoolLoginActivity.class);
        startActivity(intent);
    }

    public void register_school(View view) {
        EditText name,address,email,contact,specific_need,purpose;
        ProgressBar progressBar;
        name=findViewById(R.id.school_name);
        address=findViewById(R.id.school_address);
        email=findViewById(R.id.school_email);
        contact=findViewById(R.id.school_contact);
        specific_need=findViewById(R.id.school_need);
        purpose=findViewById(R.id.school_purpose);
        progressBar=findViewById(R.id.progressBar2_doctor);
        progressBar.setVisibility(View.VISIBLE);
        spinner=findViewById(R.id.spinnerCountries);
        String code = CountryCode.countryAreaCodes[spinner.getSelectedItemPosition()];
        if(TextUtils.isEmpty(name.getText()))
        {
            name.setError("Please enter the hospital name");
            progressBar.setVisibility(View.GONE);
        }
        else if(TextUtils.isEmpty(address.getText()))
        {
            address.setError("Please enter the hospital address");
            progressBar.setVisibility(View.GONE);
        }
        else if(TextUtils.isEmpty(email.getText()))
        {
            email.setError("Please enter the email");
            progressBar.setVisibility(View.GONE);
        }
        else if(!(isValidEmailId(email.getText().toString())))
        {
            email.setError("Please enter the valid email");
            progressBar.setVisibility(View.GONE);
        }
        else if(TextUtils.isEmpty(address.getText()))
        {
            address.setError("Please enter the hospital address");
            progressBar.setVisibility(View.GONE);
        }
        else if(TextUtils.isEmpty(contact.getText()))
        {
            contact.setError("Please enter the contact number");
            progressBar.setVisibility(View.GONE);
        }
        else if(contact.getText().toString().length()<10)
        {
            contact.setError("Valid contact number required");
            progressBar.setVisibility(View.GONE);
        }
        else if(TextUtils.isEmpty(purpose.getText()))
        {
            purpose.setError("Please enter the purpose");
            progressBar.setVisibility(View.GONE);
        }
        else if(TextUtils.isEmpty(specific_need.getText()))
        {
            contact.setError("Please enter the specific needs");
            progressBar.setVisibility(View.GONE);
        }
        else
        {
            String Name,Address,Email,Contact,Specific_need,contact_number,Purpose;
            Name=name.getText().toString();
            Address=address.getText().toString();
            Email=email.getText().toString();
            Contact=contact.getText().toString();
            Specific_need=specific_need.getText().toString();
            Purpose=purpose.getText().toString();
            contact_number="+"+code+Contact;
            arrayList=new ArrayList<String>();
            arrayList.add(0,Name);
            arrayList.add(1,Address);
            arrayList.add(2,Email);
            arrayList.add(3,Specific_need);
            arrayList.add(4,contact_number);
            arrayList.add(5,Purpose);
            try {

            DatabaseReference databaseReference=firebaseDatabase.getReference().child("Schools").child(contact_number);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists())
                    {
                        Toast.makeText(OrganisationActivity.this,"This number is already register with an account",Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            }
            catch(Exception e)
            {

            }
            Intent intent=new Intent(OrganisationActivity.this,SchoolPhoneVerification.class);
            intent.putStringArrayListExtra("arr",arrayList);
            startActivity(intent);
        }
    }

    public void register_office(View view) {
        EditText name,address,email,contact,specific_need,purpose;
        ProgressBar progressBar;
        name=findViewById(R.id.office_name);
        address=findViewById(R.id.office_address);
        email=findViewById(R.id.office_email);
        contact=findViewById(R.id.office_contact);
        specific_need=findViewById(R.id.office_need);
        purpose=findViewById(R.id.office_purpose);
        progressBar=findViewById(R.id.progressBar2_doctor);
        progressBar.setVisibility(View.VISIBLE);
        spinner=findViewById(R.id.spinnerCountries);
        String code = CountryCode.countryAreaCodes[spinner.getSelectedItemPosition()];
        if(TextUtils.isEmpty(name.getText()))
        {
            name.setError("Please enter the hospital name");
            progressBar.setVisibility(View.GONE);
        }
        else if(TextUtils.isEmpty(address.getText()))
        {
            address.setError("Please enter the hospital address");
            progressBar.setVisibility(View.GONE);
        }
        else if(TextUtils.isEmpty(email.getText()))
        {
            email.setError("Please enter the email");
            progressBar.setVisibility(View.GONE);
        }
        else if(!(isValidEmailId(email.getText().toString())))
        {
            email.setError("Please enter the valid email");
            progressBar.setVisibility(View.GONE);
        }
        else if(TextUtils.isEmpty(address.getText()))
        {
            address.setError("Please enter the hospital address");
            progressBar.setVisibility(View.GONE);
        }
        else if(TextUtils.isEmpty(contact.getText()))
        {
            contact.setError("Please enter the contact number");
            progressBar.setVisibility(View.GONE);
        }
        else if(contact.getText().toString().length()<10)
        {
            contact.setError("Valid contact number required");
            progressBar.setVisibility(View.GONE);
        }
        else if(TextUtils.isEmpty(purpose.getText()))
        {
            purpose.setError("Please enter the purpose");
            progressBar.setVisibility(View.GONE);
        }
        else if(TextUtils.isEmpty(specific_need.getText()))
        {
            contact.setError("Please enter the specific needs");
            progressBar.setVisibility(View.GONE);
        }
        else
        {
            String Name,Address,Email,Contact,Specific_need,contact_number,Purpose;
            Name=name.getText().toString();
            Address=address.getText().toString();
            Email=email.getText().toString();
            Contact=contact.getText().toString();
            Specific_need=specific_need.getText().toString();
            Purpose=purpose.getText().toString();
            contact_number="+"+code+Contact;
            arrayList=new ArrayList<String>();
            arrayList.add(0,Name);
            arrayList.add(1,Address);
            arrayList.add(2,Email);
            arrayList.add(3,Specific_need);
            arrayList.add(4,contact_number);
            arrayList.add(5,Purpose);
            try {
                DatabaseReference databaseReference=firebaseDatabase.getReference().child("Schools").child(contact_number);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            Toast.makeText(OrganisationActivity.this,"This number is already register with an account",Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            catch(Exception e)
            {

            }
            Intent intent=new Intent(OrganisationActivity.this,OfficePhoneVerification.class);
            intent.putStringArrayListExtra("arr",arrayList);
            startActivity(intent);
        }
    }

    public void login_office(View view) {
        Intent intent=new Intent(this,OfficeLoginActivity.class);
        startActivity(intent);
    }

    public void register_hospital(View view) {
       EditText name,address,email,contact,specific_need;
       ProgressBar progressBar;
       name=findViewById(R.id.hospital_name);
       address=findViewById(R.id.hospital_address);
       email=findViewById(R.id.hospital_email);
       contact=findViewById(R.id.hospital_contact);
       specific_need=findViewById(R.id.hospital_need);
       progressBar=findViewById(R.id.progressBar2_doctor);
       progressBar.setVisibility(View.VISIBLE);
       spinner=findViewById(R.id.spinnerCountries);
       String code = CountryCode.countryAreaCodes[spinner.getSelectedItemPosition()];
        if(TextUtils.isEmpty(name.getText()))
        {
            name.setError("Please enter the hospital name");
            progressBar.setVisibility(View.GONE);
        }
        else if(TextUtils.isEmpty(address.getText()))
        {
            address.setError("Please enter the hospital address");
            progressBar.setVisibility(View.GONE);
        }
        else if(TextUtils.isEmpty(email.getText()))
        {
            email.setError("Please enter the email");
            progressBar.setVisibility(View.GONE);
        }
        else if(!(isValidEmailId(email.getText().toString())))
        {
            email.setError("Please enter the valid email");
            progressBar.setVisibility(View.GONE);
        }
        else if(TextUtils.isEmpty(address.getText()))
        {
            address.setError("Please enter the hospital address");
            progressBar.setVisibility(View.GONE);
        }
        else if(TextUtils.isEmpty(contact.getText()))
        {
            contact.setError("Please enter the contact number");
            progressBar.setVisibility(View.GONE);
        }
        else if(contact.getText().toString().length()<10)
        {
            contact.setError("Valid contact number required");
            progressBar.setVisibility(View.GONE);
        }
        else if(TextUtils.isEmpty(specific_need.getText()))
        {
            contact.setError("Please enter the specific needs");
            progressBar.setVisibility(View.GONE);
        }
        else
        {
            String Name,Address,Email,Contact,Specific_need,contact_number;
            Name=name.getText().toString();
            Address=address.getText().toString();
            Email=email.getText().toString();
            Contact=contact.getText().toString();
            Specific_need=specific_need.getText().toString();
            contact_number="+"+code+Contact;
            arrayList=new ArrayList<String>();
            arrayList.add(0,Name);
            arrayList.add(1,Address);
            arrayList.add(2,Email);
            arrayList.add(3,Specific_need);
            arrayList.add(4,contact_number);
            try {
                DatabaseReference databaseReference=firebaseDatabase.getReference().child("Schools").child(contact_number);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            Toast.makeText(OrganisationActivity.this,"This number is already register with an account",Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            catch(Exception e)
            {

            }
            Intent intent=new Intent(OrganisationActivity.this,HospitalPhoneVerification.class);
            intent.putStringArrayListExtra("arr",arrayList);
            startActivity(intent);
        }
    }

    public void login_hospital(View view) {
        Intent intent=new Intent(this,HospitalLoginActivity.class);
        startActivity(intent);
    }

    public void register_clinic(View view) {
        EditText name,address,email,contact,specific_need;
        ProgressBar progressBar;
        name=findViewById(R.id.clinic_name);
        address=findViewById(R.id.clinic_address);
        email=findViewById(R.id.clinic_email);
        contact=findViewById(R.id.clinic_contact);
        specific_need=findViewById(R.id.clinic_need);
        progressBar=findViewById(R.id.progressBar2_doctor);
        progressBar.setVisibility(View.VISIBLE);
        spinner=findViewById(R.id.spinnerCountries);
        String code = CountryCode.countryAreaCodes[spinner.getSelectedItemPosition()];
        if(TextUtils.isEmpty(name.getText()))
        {
            name.setError("Please enter the clinic name");
            progressBar.setVisibility(View.GONE);
        }
        else if(TextUtils.isEmpty(address.getText()))
        {
            address.setError("Please enter the clinic address");
            progressBar.setVisibility(View.GONE);
        }
        else if(TextUtils.isEmpty(email.getText()))
        {
            email.setError("Please enter the email");
            progressBar.setVisibility(View.GONE);
        }
        else if(!(isValidEmailId(email.getText().toString())))
        {
            email.setError("Please enter the valid email");
            progressBar.setVisibility(View.GONE);
        }
        else if(TextUtils.isEmpty(address.getText()))
        {
            address.setError("Please enter the clinic address");
            progressBar.setVisibility(View.GONE);
        }
        else if(TextUtils.isEmpty(contact.getText()))
        {
            contact.setError("Please enter the contact number");
            progressBar.setVisibility(View.GONE);
        }
        else if(contact.getText().toString().length()<10)
        {
            contact.setError("Valid contact number required");
            progressBar.setVisibility(View.GONE);
        }
        else if(TextUtils.isEmpty(specific_need.getText()))
        {
            contact.setError("Please enter the specific needs");
            progressBar.setVisibility(View.GONE);
        }
        else
        {
            String Name,Address,Email,Contact,Specific_need,contact_number;
            Name=name.getText().toString();
            Address=address.getText().toString();
            Email=email.getText().toString();
            Contact=contact.getText().toString();
            Specific_need=specific_need.getText().toString();
            contact_number="+"+code+Contact;
            arrayList=new ArrayList<String>();
            arrayList.add(0,Name);
            arrayList.add(1,Address);
            arrayList.add(2,Email);
            arrayList.add(3,Specific_need);
            arrayList.add(4,contact_number);
            try {
                DatabaseReference databaseReference=firebaseDatabase.getReference().child("Schools").child(contact_number);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            Toast.makeText(OrganisationActivity.this,"This number is already register with an account",Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            catch(Exception e)
            {

            }
            Intent intent=new Intent(OrganisationActivity.this,ClinicPhoneVerification.class);
            intent.putStringArrayListExtra("arr",arrayList);
            startActivity(intent);
        }
    }

    public void login_clinic(View view) {
        Intent intent=new Intent(this,ClinicLoginActivity.class);
        startActivity(intent);
    }


}
