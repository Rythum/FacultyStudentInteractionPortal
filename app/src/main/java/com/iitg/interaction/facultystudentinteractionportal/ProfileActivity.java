package com.iitg.interaction.facultystudentinteractionportal;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.StaticLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import java.net.URI;

public class ProfileActivity extends AppCompatActivity {

    TextView fullname;
    TextView username;
    TextView email;
    TextView occupation;
    TextView rollnumber;
    TextView year;
    TextView department;

    ListView ll;

    ImageView profilepic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Profile");

        fullname = findViewById(R.id.tv_name);
        username = findViewById(R.id.tv_username);
        email = findViewById(R.id.tv_email);
        occupation=findViewById(R.id.tv_occupation);
        rollnumber=findViewById(R.id.tv_rollnumber);
        year = findViewById(R.id.tv_year);
        department = findViewById(R.id.tv_department);
        profilepic = findViewById(R.id.image_profilepic);
        profilepic.setImageResource(R.mipmap.sampleppic1);

        if(UserInfo.logined)
        {
            Log.d("DEBUG","profile uri "+ UserInfo.profilepicurl);
//            profilepic.setImageURI(null);
           profilepic.setImageURI(UserInfo.profilepicurl);
           if(profilepic==null)
           {
               profilepic.setImageResource(R.mipmap.sampleppic1);
           }

            //profilepic.setImageURI();


            fullname.setText(UserInfo.fullname);

            username.setText("Username: " + UserInfo.username);
            email.setText(UserInfo.email);
            occupation.setText(UserInfo.occupation);
            department.setText(UserInfo.department);

            if(UserInfo.usertype.equals("Stud"))
            {
                rollnumber.setText(UserInfo.rollnumber);
                year.setText(UserInfo.year);
            }
            else
            {
                rollnumber.setVisibility(View.INVISIBLE);
                year.setVisibility(View.INVISIBLE);
            }
        }
//          ll = (ListView) findViewById(R.id.lv_courses);
//        TextView emp = findViewById(R.id.tv_emptylist);
//        if(!(UserInfo.courses==null)) {
//
//            final ArrayAdapter<String> adpater = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,UserInfo.courses);
//            ll.setAdapter(adpater);
//        }
//
//        else
//            {
//                emp.setVisibility(View.VISIBLE);
//            }
//
//      ll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//          @Override
//          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//              /* open Cources Form */
//          }
//      });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}


