package loginscreen.solution.example.com.loginscreen;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import loginscreen.solution.example.com.loginscreen.Data.local.DatabaseHelper;
import loginscreen.solution.example.com.loginscreen.Model.User;

public class LoginWelcomeActivity extends AppCompatActivity {

    private TextView name;
    private TextView phoneNumeber;
    private TextView emailId;

    private String email;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent in=new Intent(LoginWelcomeActivity.this,MainActivity.class);
        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(in);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_welcome);
        Bundle bundle = getIntent().getExtras();
        email = bundle.getString("email_id");
        name = (TextView)findViewById(R.id.tv_name);
        phoneNumeber = (TextView) findViewById(R.id.tv_phone);
        emailId = (TextView) findViewById(R.id.tv_email);
        showUserData();
    }

    private void showUserData(){
        new AsyncTask<String , Integer, User>(){
            @Override
            protected User doInBackground(String... params) {
                return  DatabaseHelper.getHelper(getApplicationContext())
                        .getUser(params[0]);
            }

            @Override
            protected void onPostExecute(User user) {
                super.onPostExecute(user);

                name.setText(user.getName());
                phoneNumeber.setText(user.getPhoneNumber());
                emailId.setText(user.getEmailId());
            }
        }.execute(email);
    }

}
