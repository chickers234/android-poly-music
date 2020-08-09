package com.example.polymusic;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import api.API;
import api.RetrofitClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    TextView tvSkip, tvSignup;
    EditText edt_email, edt_password;
    CheckBox check_remember;
    RetrofitClient retrofit = new RetrofitClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        getWindow().setFlags(
//                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//        );
        setContentView(R.layout.activity_login);

        Window window = getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(LoginActivity.this, R.color.orange));

        //ánh xạ
        btnLogin = findViewById(R.id.btnLogin);
        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);
        tvSkip = findViewById(R.id.tvSkip);
        tvSignup = findViewById(R.id.tvSignup);
        check_remember = findViewById(R.id.check_remember);

        //event
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edt_email.getText().toString();
                String password = edt_password.getText().toString();
                login(email, password);

            }
        });

        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, FetchDataActivity.class));
            }
        });

        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptions options = ActivityOptions.makeCustomAnimation(LoginActivity.this, android.R.anim.fade_in, android.R.anim.fade_out);
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class), options.toBundle());
                //startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

    }

    public void login(final String email, String password) {
        API api = retrofit.getClient().create(API.class);
        api.login(email, password).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d("status", response.code() + "");
                    if (response.code() == 200) {
                        Intent intent = new Intent(LoginActivity.this, FetchDataActivity.class);
                        if (email.equals("admin@gmail.com")) {
                            intent.putExtra("admin", true);
                        } else {
                            intent.putExtra("admin", false);
                        }
                        startActivity(intent);
                        //startActivity(new Intent(LoginActivity.this, FetchDataActivity.class));
                    }
                } else {
                    Log.d("status", response.code() + "");
                    Toast.makeText(LoginActivity.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        // Edit and commit
        editor.putString("Email", String.valueOf(edt_email.getText()));
        editor.putString("Password", String.valueOf(edt_password.getText()));
        editor.putBoolean("Save", check_remember.isChecked());
        editor.commit();

    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        // Get value
        String EmailValue = preferences.getString("Email", "");
        String PasswordValue = preferences.getString("Password", "");
        boolean save = preferences.getBoolean("Save", false);
        if (save) {
            edt_email.setText(EmailValue);
            edt_password.setText(PasswordValue);
        }
        check_remember.setChecked(save);
    }
}