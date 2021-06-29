package lunnardo.jwork_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText etEmail = findViewById(R.id.et_Email);
        EditText etPassword = findViewById(R.id.et_password);
        Button btnLogin = findViewById(R.id.btn_Login);
        TextView tvRegister = findViewById(R.id.tv_Register);
        Button btnShowPassword = findViewById(R.id.btn_show_password);
        SharedPreferences sharedPreferences = getSharedPreferences("login_session", MODE_PRIVATE);

        if (sharedPreferences.getBoolean("loggedIn", false)) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("jobseekerId", sharedPreferences.getInt("jobseekerId", 0));
            intent.putExtra("jobseekerName", sharedPreferences.getString("jobseekerName", "0"));
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else {
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email = etEmail.getText().toString();
                    String password = etPassword.getText().toString();

                    if (email.isEmpty()) {
                        etEmail.setError("Please enter your email address");
                        etEmail.requestFocus();
                        return;
                    }

                    if (password.isEmpty()) {
                        etPassword.setError("Please enter your password");
                        etPassword.requestFocus();
                        return;
                    }

                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject != null) {
                                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);

                                    SharedPreferences sharedPreferences = getSharedPreferences("login_session", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putInt("jobseekerId", jsonObject.getInt("id"));
                                    editor.putString("jobseekerName", jsonObject.getString("name"));
                                    editor.putBoolean("loggedIn", true);
                                    editor.apply();

                                    loginIntent.putExtra("jobseekerId", jsonObject.getInt("id"));
                                    loginIntent.putExtra("jobseekerName", jsonObject.getString("name"));
                                    loginIntent.addFlags(loginIntent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(loginIntent);
                                    finish();
                                }
                            } catch (JSONException e) {
                                Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
//                            e.printStackTrace();
                            }
                        }
                    };
                    LoginRequest loginRequest = new LoginRequest(email, password, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                    queue.add(loginRequest);
                }
            });
        }
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }

        });

        btnShowPassword.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                switch ( event.getAction() ) {

                    case MotionEvent.ACTION_UP:
                        etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        break;

                    case MotionEvent.ACTION_DOWN:
                        etPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                        break;

                }
                return true;
            }
        });
    }
}