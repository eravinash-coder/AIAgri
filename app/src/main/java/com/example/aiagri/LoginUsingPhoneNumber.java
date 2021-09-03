package com.example.aiagri;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class LoginUsingPhoneNumber extends AppCompatActivity {
    // variable for FirebaseAuth class
    FirebaseAuth mAuth;

    // variable for our text input
    // field for phone and OTP.
    EditText edtPhone;
    PinView PinView;
    CountryCodePicker ccp;
    // String edtOTP;

    // buttons for generating OTP and verifying OTP
    Button idBtnVerify, idBtnGetOtp;

    // string for storing our verification ID
    String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_using_phone_number);

        // below line is for getting instance
        // of our FirebaseAuth.
        mAuth = FirebaseAuth.getInstance();

        // initializing variables for button and Edittext.
        ccp=findViewById(R.id .ccp);
        edtPhone = (EditText) findViewById(R.id.idEdtPhoneNumber);
        PinView = (PinView) findViewById(R.id.PinView);
        idBtnVerify = (Button) findViewById(R.id.idBtnVerify);
        idBtnGetOtp = (Button) findViewById(R.id.idBtnGetOtp);

        // setting onclick listener for generate OTP button.
        idBtnGetOtp.setOnClickListener(v -> {
            if (TextUtils.isEmpty(edtPhone.getText().toString())) {
                Toast.makeText(LoginUsingPhoneNumber.this, "Please enter a valid phone number.", Toast.LENGTH_SHORT).show();
            } else {

                String phone = "+"+ccp.getFullNumber() + edtPhone.getText().toString();
                sendVerificationCode(phone);
            }
        });

        // initializing on click listener
        // for verify otp button
        idBtnVerify.setOnClickListener(v -> {
            // validating if the OTP text field is empty or not.
            if (!TextUtils.isEmpty(Objects.requireNonNull(PinView.getText()).toString())) {
                // if OTP field is not empty calling
                // method to verify the OTP.
                verifyCode(PinView.getText().toString());
            } else {
                // if the OTP text field is empty display
                // a message to user to enter OTP
                Toast.makeText(LoginUsingPhoneNumber.this, "Please enter OTP", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        // inside this method we are checking if
        // the code entered is correct or not.
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this::onComplete);
    }


    private void sendVerificationCode(String number) {
        // this method is used for getting
        // OTP on user phone number.
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)            // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallBack)           // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    // callback method is called on Phone auth provider.
    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks

            // initializing our callbacks for on
            // verification callback method.
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        // below method is used when
        // OTP is sent from Firebase
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            // when we receive the OTP it
            // contains a unique id which
            // we are storing in our string
            // which we have already created.
            verificationId = s;
        }

        // this method is called when user
        // receive OTP from Firebase.
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            // below line is used for getting OTP code
            // which is sent in phone auth credentials.
            final String code = phoneAuthCredential.getSmsCode();

            // checking if the code
            // is null or not.
            if (code != null) {
                // if the code is not null then
                // we are setting that code to
                // our OTP edittext field.
                PinView.setText(code);

                // after setting this code
                // to OTP edittext field we
                // are calling our  method.
                verifyCode(code);
            }
        }

        // this method is called when firebase doesn't
        // sends our OTP code due to any error or issue.
        @Override
        public void onVerificationFailed(FirebaseException e) {
            // displaying error message with firebase exception.
            Toast.makeText(LoginUsingPhoneNumber.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    // below method is use to verify code from Firebase.
    private void verifyCode(String code) {
        // below line is used for getting getting
        // credentials from our verification id and code.
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        // after getting credential we are
        // calling sign in method.
        signInWithCredential(credential);
    }

    private void onComplete(Task<AuthResult> task) {
        if (task.isSuccessful()) {
// if the code is correct and the task is successful
// we are sending our user to new activity.
            Intent i = new Intent(LoginUsingPhoneNumber.this, MainActivity.class);
            startActivity(i);
            finish();
        } else {
// if the code is not correct then we are
// displaying an error message to the user.
            Toast.makeText(LoginUsingPhoneNumber.this,
                    Objects.requireNonNull(task.getException()).getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }
}
