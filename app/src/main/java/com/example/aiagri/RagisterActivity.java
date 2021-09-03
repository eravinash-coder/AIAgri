package com.example.aiagri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RagisterActivity extends AppCompatActivity {
        TextView alreadyhaveaccount;
        EditText inputEmailR,inputPasswordR,inputConformPassword;
        Button btnregister;
        String emailpattern="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        ProgressDialog progressDialog;

        FirebaseAuth mAuth;
        FirebaseUser muser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ragister);
        alreadyhaveaccount=findViewById(R.id.alreadyHaveAccount);
        alreadyhaveaccount.setOnClickListener(v -> startActivity(new Intent(RagisterActivity.this, LoginActivity.class)));
        inputEmailR=findViewById(R.id.inputEmailR);
        inputPasswordR=findViewById(R.id.inputPasswordR);
        inputConformPassword=findViewById(R.id.inputConformPassword);
        progressDialog=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();
        muser=mAuth.getCurrentUser();
        btnregister=findViewById(R.id.btnRegister);
        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerforAuth();
            }

            private void PerforAuth() {
                String email=inputEmailR.getText().toString();
                        String Password=inputPasswordR.getText().toString();
                        String ConformPasswprd=inputConformPassword.getText().toString();

                if(!email.matches(emailpattern))
                        {
                            inputEmailR.setError("Enter Valid Email");
                        }else if (Password.isEmpty()||Password.length()<6)
                        {
                            inputPasswordR.setError("Passwords must more then 6 characters");
                        }else if (!Password.equals(ConformPasswprd))
                        {
                            inputConformPassword.setError("Password not match");
                        }else
                        {
                            progressDialog.setMessage("Please wait while Registration....");
                            progressDialog.setTitle("Registration");
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.show();
                            mAuth.createUserWithEmailAndPassword(email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful())
                                    {
                                        progressDialog.dismiss();
                                        sendUserToNextActivity();
                                        Toast.makeText(RagisterActivity.this, "Registaration Successful", Toast.LENGTH_SHORT).show();
                                    }else
                                    {
                                        progressDialog.dismiss();
                                        Toast.makeText(RagisterActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                                    }


                                }

                        private void sendUserToNextActivity() {
                            Intent intent=new Intent(RagisterActivity.this,MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    });
                }

            }
        });
    }
}