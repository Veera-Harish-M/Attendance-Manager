package com.veera.admin_a;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login extends AppCompatActivity {

    ImageButton btRegister;
    TextView tvLogin,tvForget;
    EditText batch_no,pass;
    Button login_btn;
    String email_id;
    String app_password="";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_btn=findViewById(R.id.login_btn);
        batch_no=findViewById(R.id.login_batch);
        pass=findViewById(R.id.login_password);
        tvForget=findViewById(R.id.tvForgot);
        btRegister  = findViewById(R.id.btRegister);
        tvLogin     = findViewById(R.id.tvLogin);



        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!batch_no.getText().toString().isEmpty() && !pass.getText().toString().isEmpty()){
                try {
                    Toast.makeText(getApplicationContext(),"Processing",Toast.LENGTH_SHORT).show();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("login").child(batch_no.getText().toString());
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String ps = dataSnapshot.child("password").getValue().toString();

                            if (pass.getText().toString().equals(ps)) {

                                sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                myEdit.putString("batch",batch_no.getText().toString());
                                myEdit.putString("password",ps);
                                myEdit.commit();

                                Intent intent = new Intent(login.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                pass.requestFocus();
                                pass.setError("Invalid Password");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            batch_no.requestFocus();
                            batch_no.setError("Invalid BatchNo");

                        }
                    });
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"Can't Access Database",Toast.LENGTH_SHORT).show();
                }
            }else{
                    batch_no.requestFocus();
                    batch_no.setError("Invalid BatchNo");
                    pass.requestFocus();
                    pass.setError("Invalid Password");
                }
            }
        });


        tvForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(login.this);
                LayoutInflater inflater;
                inflater = login.this.getLayoutInflater();
                final View view = inflater.inflate(R.layout.forget_dialog, null);
                builder.setView(view);
                builder.setTitle("Reset Password");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        EditText d_batch=(EditText)view.findViewById(R.id.dialog_batch);
                        final EditText d_email=(EditText)view.findViewById(R.id.dialog_email);
                        if (!d_batch.getText().toString().isEmpty() && !d_email.getText().toString().isEmpty()) {
                            try {
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("login").child(d_batch.getText().toString());
                                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        email_id=dataSnapshot.child("email").getValue().toString();
                                        if(email_id.equals(d_email.getText().toString()))
                                        {
                                            app_password = dataSnapshot.child("password").getValue().toString();
                                            sendMail();
                                        }else{
                                            Toast.makeText(getApplicationContext(),"Email id mismatch",Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }

                                });

                            }catch (Exception e){
                                Toast.makeText(getApplicationContext(),"Can't access database",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(),"Enter valid details",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });



        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v==btRegister){
                    Intent intent   = new Intent(login.this,register.class);
                    Pair[] pairs    = new Pair[1];
                    pairs[0] = new Pair<View,String>(tvLogin,"tvLogin");
                    ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(login.this,pairs);
                    startActivity(intent,activityOptions.toBundle());
                }
            }
        });
    }

    private void sendMail() {

        String s="Password Reset";
        String m="Thanks for Using Our Application.\nThis is your password: "+app_password;
        JavaMailAPI javaMailAPI=new JavaMailAPI(this,email_id,s,m);
        javaMailAPI.execute();

    }
}
