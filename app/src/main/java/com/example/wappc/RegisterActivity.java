package com.example.wappc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText username,mail,password;
    Button register;
    Toolbar toolbar;
    FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username=findViewById(R.id.iusername);
        mail=findViewById(R.id.imail);
        password=findViewById(R.id.ipassword);
        register=findViewById(R.id.iregister);

        auth=FirebaseAuth.getInstance();
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String txtusername=username.getText().toString();
               String txtmail=mail.getText().toString();
               String txtpw=password.getText().toString();
               if(TextUtils.isEmpty(txtusername)||TextUtils.isEmpty(txtmail) || TextUtils.isEmpty(txtpw))
               {
                   Toast.makeText(RegisterActivity.this,"Fill All required fields!",Toast.LENGTH_SHORT).show();
               }
               else
                   if(txtpw.length()<6)
                   {
                       Toast.makeText(RegisterActivity.this,"Password must be of atleast 6 characters",Toast.LENGTH_SHORT).show();
                   }else
                   {
                       register(txtusername,txtmail,txtpw);
                   }
            }
        });

    }

    public void register(final String username, String mail, String password){

        auth.createUserWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    FirebaseUser firebaseUser=auth.getCurrentUser();

                    String userid=firebaseUser.getUid();

                    reference= FirebaseDatabase.getInstance().getReference("Users").child(userid);

                     HashMap<String,String> map=new HashMap<>();
                    map.put("id",userid);
                    map.put("username",username);
                    map.put("imageURL","default");

                    reference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();

                            }
                        }
                    });
                }else
                {
                    Toast.makeText(RegisterActivity.this,"Error in registering with provided username and password",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}