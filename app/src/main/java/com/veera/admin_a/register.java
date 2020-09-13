package com.veera.admin_a;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.PasswordAuthentication;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class register extends AppCompatActivity {

    private RelativeLayout rlayout;
    private Animation animation;
    String key_batch="";

    EditText batch,name,password,repassword,reg_email;
    Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = findViewById(R.id.bgHeader);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        batch=findViewById(R.id.reg_batch);
        name=findViewById(R.id.reg_name);
        password=findViewById(R.id.reg_password);
        repassword=findViewById(R.id.reg_repassword);
        signup=findViewById(R.id.reg_signup);
        reg_email=findViewById(R.id.reg_email);

        rlayout = findViewById(R.id.rlayout);
        animation = AnimationUtils.loadAnimation(this,R.anim.uptodowndiagonal);
        rlayout.setAnimation(animation);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(),"Processsing",Toast.LENGTH_LONG).show();
                int i=0;
                if(batch.getText().toString().isEmpty())
                {
                    batch.requestFocus();
                    batch.setError("Batch Number Required 😉");
                }
                else if(name.getText().toString().isEmpty())
                {
                    name.requestFocus();
                    name.setError("No name for U 😏😏");
                }
                else if(password.getText().toString().isEmpty())
                {
                    password.requestFocus();
                    password.setError("Don't need Security 🙄");
                }
                else if (repassword.getText().toString().isEmpty())
                {
                    repassword.requestFocus();
                    repassword.setError("Retype Your Password");
                }
                else if(reg_email.getText().toString().isEmpty())
                {
                        reg_email.requestFocus();
                        reg_email.setError("Email can't be empty");
                }
                else
                {
                    if(password.getText().toString().equals(repassword.getText().toString()))
                    {
                        String pw=password.getText().toString();
                        Pattern specailCharPatten = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
                        Pattern UpperCasePatten = Pattern.compile("[A-Z ]");
                        Pattern lowerCasePatten = Pattern.compile("[a-z ]");
                        Pattern digitCasePatten = Pattern.compile("[0-9 ]");
                        if(pw.length()<8)
                        {
                            password.requestFocus();
                            password.setError("Password should contain atleast 8 character");
                        }
                        else if(!specailCharPatten.matcher(pw).find())
                        {
                            password.requestFocus();
                            password.setError("Password must have atleast one specail character");
                        }else if(!UpperCasePatten.matcher(pw).find())
                        {
                            password.requestFocus();
                            password.setError("Password must have atleast one uppercase character");

                        }else if(!lowerCasePatten.matcher(pw).find())
                        {
                            password.requestFocus();
                            password.setError("Password must have atleast one lowercase character");
                        }
                        else if(!digitCasePatten.matcher(pw).find())
                        {
                            password.requestFocus();
                            password.setError("Password must have atleast one digit character");
                        }
                        else
                            i=1;
                    }
                    else
                        {
                            repassword.requestFocus();
                            repassword.setError("Should Match with Your Password");
                        }
                }

                if(i!=0) {
                    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("batch").child(batch.getText().toString());
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            try{
                                key_batch = dataSnapshot.getValue().toString();
                            }catch(Exception e){
                                Toast.makeText(getApplicationContext(),"Unauthorized User",Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(register.this, login.class);
                                startActivity(i);
                                finish();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(getApplicationContext(),"Unauthorized User",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                  try {
                      DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("login");
                      Map<String, Object> tmap = new HashMap<>();
                      tmap.put("batch", batch.getText().toString());
                      tmap.put("name", name.getText().toString());
                      tmap.put("password", password.getText().toString());
                      tmap.put("email",reg_email.getText().toString());
                      reference.child(batch.getText().toString()).updateChildren(tmap);
                  }catch(Exception e)
                  {
                      Toast.makeText(getApplicationContext(),"Can't access Database",Toast.LENGTH_SHORT).show();
                  }
                    Intent intent = new Intent(register.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
