package poly.edu.vn.send_otp_login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyPhoneNumberActivity extends AppCompatActivity {
    EditText edPhone;
    Button btnVerifyPhone;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone_number);
        edPhone = findViewById(R.id.edPhone);
        btnVerifyPhone = findViewById(R.id.btnVerifyPhone);
        mAuth = FirebaseAuth.getInstance();

        btnVerifyPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = edPhone.getText().toString().trim();
                onClickVeryNumber(phone);
                Toast.makeText(VerifyPhoneNumberActivity.this, "long", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onClickVeryNumber(String phone) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phone)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(VerifyPhoneNumberActivity.this, "Số điện thoại nhập không đúng", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String MaOTP, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(MaOTP, forceResendingToken);
                                goToEnterOTP(phone,MaOTP);
                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void goToEnterOTP(String phone, String maOTP) {
        Intent intent = new Intent(getBaseContext(),ConFimOTPActivity.class);
        intent.putExtra("phone_number",phone);
        intent.putExtra("ma_otp",maOTP);
        startActivity(intent);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = task.getResult().getUser();
                            goToMainActivity(user.getPhoneNumber());
                            // Update UI
                        } else {
                            // Sign in failed, display a message and update the UI
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(VerifyPhoneNumberActivity.this, "The verification code entered was invalid\nMã xác minh không hợp lệ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void goToMainActivity(String phoneNumber) {
        Intent intent = new Intent(getBaseContext(),MainActivity.class);
        intent.putExtra("phone_number",phoneNumber);
        startActivity(intent);
    }
}