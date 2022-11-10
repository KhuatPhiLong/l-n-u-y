package poly.edu.vn.send_otp_login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView tv_id_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_id_user = findViewById(R.id.tv_id_user);
        Intent intent = getIntent();
        String phone = intent.getStringExtra("phone_number");
        tv_id_user.setText(phone);
    }
}