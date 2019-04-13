package com.iitg.interaction.facultystudentinteractionportal;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaRouter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SignupActivity extends AppCompatActivity {
    public static boolean outlookuser=false;
    EditText name;
    EditText email;
    EditText rollnumber;
    Spinner department;
    EditText occupation;
    EditText year;
    EditText password,pass2;
    String usertype;
    Button signupbtn;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference dataref = firebaseDatabase.getReference("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);
        name = findViewById(R.id.et_name);
        email = findViewById(R.id.et_email);
        rollnumber = findViewById(R.id.et_rollnumber);
        year = findViewById(R.id.et_year);
        password = findViewById(R.id.et_password);
        pass2 = findViewById(R.id.et_password2);
        occupation = findViewById(R.id.et_occupation);
        department = findViewById(R.id.spn_department);
        signupbtn = findViewById(R.id.btn_signup);
        Integer roll=null;

        if(outlookuser) // SIGNUP VIA OUTLOOK
        {
            if(UserInfo.occupation!=null && !UserInfo.occupation.isEmpty())
            {
                occupation.setText(UserInfo.occupation);
                occupation.setEnabled(false);

                if(UserInfo.occupation.toLowerCase().contains("professor")) {
                    //UserInfo.usertype="Prof";
                    usertype="Prof";
                }
                else
                {
                    //UserInfo.usertype="Stud";
                    usertype="Stud";
                }
            }
            if(UserInfo.rollnumber!=null && !UserInfo.rollnumber.isEmpty()) // IF LAST NAME OF OUTLOOK PROFILE CONTAINS ROLL NUMBER.
            {
                rollnumber.setText(UserInfo.rollnumber);
                rollnumber.setEnabled(false);
                if(occupation.getText().toString().toLowerCase().equals("btech"))
                {
                    String tmp =  UserInfo.rollnumber.substring(0,2);
                    String yr= "20"+(Integer.valueOf(tmp)+4);
                    year.setText(yr);
                    year.setEnabled(false);
                }
                else if(occupation.getText().toString().toLowerCase().equals("mtech")||occupation.getText().toString().toLowerCase().equals("msc"))
                {
                    String tmp =  UserInfo.rollnumber.substring(0,2);
                    String yr= "20"+(Integer.valueOf(tmp)+2);
                    year.setText(yr);
                }
                else if(occupation.getText().toString().toLowerCase().equals("phd"))
                {
                    String tmp =  UserInfo.rollnumber.substring(0,2);
                    String yr= "20"+(Integer.valueOf(tmp)+4);
                    year.setText(yr);
                }
                else {
                    year.setText("0000");
                    year.setVisibility(View.INVISIBLE);
                }

                roll = Integer.parseInt(rollnumber.getText().toString().substring(4,6));


            }
            else   // LAST NAME (IN OUTLOOK PROFILE) DOES NOT CONTAIN ROLL NUMBER
            {
                rollnumber.setText("0");
                rollnumber.setVisibility(View.VISIBLE);
            }
            name.setText(UserInfo.fullname);
            email.setText(UserInfo.email);
            name.setEnabled(false);
            email.setEnabled(false);
        }

    /*    List<String> list = new ArrayList<String>();
        list.add("BTech");
        list.add("MTech");
        list.add("PHD");
        list.add("Professor");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        occupation.setAdapter(dataAdapter);
        */



        List<String> branchlist = new ArrayList<String>();
        branchlist.add("CSE");
        branchlist.add("ECE");
        branchlist.add("ME");
        branchlist.add("CE");
        branchlist.add("DD");
        branchlist.add("BSBE");
        branchlist.add("CL");
        branchlist.add("EEE");
        branchlist.add("CST");
        branchlist.add("MC");
        branchlist.add("EPH");
        branchlist.add("HSS");
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, branchlist);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        department.setAdapter(dataAdapter2);

        if(roll!=null)
        {
            Integer tt=null;
            for (int i=0;i<8;i++)
            {
                if (roll==i+1)
                {
                    department.setSelection(i);
                    tt=i;
                    break;
                }
            }

            if(roll==21)
            {
                department.setSelection(10);
                tt=10;
            }
            else if(roll==22)
            {
                department.setSelection(8);
                tt=8;

            }
            else if(roll==23)
            {
                department.setSelection(9);
                tt=9;

            }
            else if(roll==41)
            {
                department.setSelection(11);
                tt=11;

            }
            if(tt!=null)
            {
                department.setEnabled(false);
            }



        }

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    signupfunction();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    void signupfunction() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        final NewUser newUser;
        final String username;

       // String occup = occupation.getSelectedItem().toString();


        if(name.getText().toString().isEmpty()||email.getText().toString().isEmpty()||rollnumber.getText().toString().isEmpty()||year.getText().toString().isEmpty()||department.getSelectedItem().toString().isEmpty()||password.getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Please Enter all details!",Toast.LENGTH_LONG).show();
            return;
        }

        if(!email.getText().toString().endsWith("@iitg.ac.in"))
        {
            Toast.makeText(getApplicationContext(),"Only IITG members, use IITG email",Toast.LENGTH_LONG).show();
            return;
        }

    /*    if(occup.equals("Professor"))
        {
            usertype="Prof";
        }
        else
        {
            usertype="Stud";
        }
*/

        username= email.getText().toString().replace("@iitg.ac.in","");

        if(!password.getText().toString().equals(pass2.getText().toString()) )
        {
            Toast.makeText(getApplicationContext(),"Passwords do not match!",Toast.LENGTH_LONG).show();
            return;
        }

        newUser = new NewUser(username,name.getText().toString(),usertype,rollnumber.getText().toString(),email.getText().toString(),occupation.getText().toString(),department.getSelectedItem().toString(),year.getText().toString(),Sha1Custom.SHA1(password.getText().toString()));
        final ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) {
                    //create new user

                    dataref.child(username).setValue(newUser);
                    Toast.makeText(getApplicationContext(),"Registered Successfully! Your Username is : '"+username+"'",Toast.LENGTH_LONG).show();
                    Intent intent;
                    if(outlookuser)
                    {
                        UserInfo.fillUserInfo(newUser.username,newUser.fullname,newUser.usertype,newUser.rollnumber,newUser.email,newUser.occupation,newUser.department,newUser.year,newUser.courses);

                        intent= new Intent(SignupActivity.this,MainActivity.class);
                        outlookuser=false;
                    }
                    else
                    {
                        intent= new Intent(SignupActivity.this,LoginActivity.class);
                    }
                    startActivity(intent);

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"User Already Exists!",Toast.LENGTH_LONG).show();
                    return;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("database error", databaseError.getMessage()); //Don't ignore errors!
            }
        };
        dataref.child(username).addListenerForSingleValueEvent(eventListener);


    }

}
