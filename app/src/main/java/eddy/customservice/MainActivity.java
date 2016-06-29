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
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private String user;
    private SharedPreferences users;
    @Bind(R.id.tvEmail)
    TextView tvEmail;
    @Bind(R.id.etEmail)
    EditText etEmail;
    @Bind(R.id.btRegister)
    Button btRegister;
    @Bind(R.id.btCancel)
    Button btCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        users = getSharedPreferences("RegisteredUser", Context.MODE_PRIVATE);
        promptUser();
        //hide views if registered user exists
        if (checkRegistration()) {
            tvEmail.setVisibility(View.INVISIBLE);
            etEmail.setVisibility(View.INVISIBLE);
            btRegister.setVisibility(View.INVISIBLE);
            Toast.makeText(this, user, Toast.LENGTH_LONG).show();
        }
        else { //hide cancel button from unregistered user
            btCancel.setEnabled(false);
        }
    }

    private void promptUser() { //SDK 23 and above require user approval for drawing overlays
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(MainActivity.this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 1234);
            }
        }
    }

    private boolean checkRegistration() { //get email address from shared preferences
        user = users.getString("emailAddress", "");
        return !user.equals("");
    }

    //handle button actions
    @OnClick({R.id.btRegister, R.id.btCancel})
    public void onClick(View view) {
        SharedPreferences.Editor editor = users.edit();
        switch (view.getId()) {
            case R.id.btRegister:
                //locally save valid email address
                if (validate(etEmail.getText())) {
                    editor.putString("emailAddress", etEmail.getText().toString());
                    editor.apply();
                    Intent intent = new Intent(this, MenuService.class);
                    startService(intent);
                    etEmail.setEnabled(false);
                    btRegister.setEnabled(false);
                    btCancel.setEnabled(true);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Enter valid address", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btCancel:
                //logout and stop running service
                editor.remove("emailAddress");
                editor.apply();
                Intent intent = new Intent(getApplicationContext(), MenuService.class);
                stopService(intent);
                Toast.makeText(getApplicationContext(), "Service Stopped", Toast.LENGTH_LONG).show();
                tvEmail.setVisibility(View.VISIBLE);
                etEmail.setVisibility(View.VISIBLE);
                btRegister.setVisibility(View.VISIBLE);
                etEmail.setEnabled(true);
                etEmail.setText("");
                btRegister.setEnabled(true);
                btCancel.setEnabled(false);
                break;
        }
    }

    private boolean validate(CharSequence input) {
        //validate email address
        return !TextUtils.isEmpty(input) && Patterns.EMAIL_ADDRESS.matcher(input).matches();
    }
}