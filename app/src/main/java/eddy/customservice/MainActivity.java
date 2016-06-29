package eddy.customservice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.tvTitle)
    TextView tvTitle;
    @Bind(R.id.tvEmail)
    TextView tvEmail;
    @Bind(R.id.etEmail)
    EditText etEmail;
    @Bind(R.id.btRegister)
    Button btRegister;
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //hide views if registered user exists
        if (checkRegistration()){
            tvEmail.setVisibility(View.INVISIBLE);
            etEmail.setVisibility(View.INVISIBLE);
            btRegister.setVisibility(View.INVISIBLE);
            Toast.makeText(this, user, Toast.LENGTH_LONG).show();
        }

        //temp logout method for testing only!
        tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences users = getSharedPreferences("RegisteredUser", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = users.edit();
                editor.remove("emailAddress");
                editor.apply();
                Toast.makeText(getApplicationContext(), "deleted user", Toast.LENGTH_SHORT).show();
                tvEmail.setVisibility(View.VISIBLE);
                etEmail.setVisibility(View.VISIBLE);
                btRegister.setVisibility(View.VISIBLE);
                etEmail.setEnabled(true);
                btRegister.setEnabled(true);
            }
        });
    }

    private boolean checkRegistration() { //get email address from shared preferences
        SharedPreferences users = getSharedPreferences("RegisteredUser", Context.MODE_PRIVATE);
        user = users.getString("emailAddress", "");
        return !user.equals("");
    }

    @OnClick(R.id.btRegister)
    public void onClick() {
        //locally save valid email address
        SharedPreferences users = getSharedPreferences("RegisteredUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = users.edit();
        if (validate(etEmail.getText())){
            editor.putString("emailAddress", etEmail.getText().toString());
            editor.apply();
            if(Build.VERSION.SDK_INT >= 23) {
                if (!Settings.canDrawOverlays(MainActivity.this)) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, 1234);
                }
                else
                {
                    Intent intent = new Intent(this, MenuService.class);
                    startService(intent);
                }
            }
            etEmail.setEnabled(false);
            btRegister.setEnabled(false);

        }
    }

    private boolean validate(CharSequence input) {
        //validate email address
        return !TextUtils.isEmpty(input) && android.util.Patterns.EMAIL_ADDRESS.matcher(input).matches();
    }
}
