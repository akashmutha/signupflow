package loginscreen.solution.example.com.loginscreen;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;

import loginscreen.solution.example.com.loginscreen.Data.local.DatabaseHelper;
import loginscreen.solution.example.com.loginscreen.Model.User;
import loginscreen.solution.example.com.loginscreen.Util.Constants;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout ltButtonLayout;
    private Button loginButton;
    private Button signupButton;
    private LinearLayout nameLayout;
    private EditText name;
    private LinearLayout commonLayout; // for email and password
    private EditText email;
    private EditText password;
    private ViewFlipper viewFlipper;
    private Button signInButton;
    private Button createAccountButton;
    private EditText phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        loginButton = (Button) findViewById(R.id.bt_login);
        signupButton = (Button) findViewById(R.id.bt_signup);
        nameLayout = (LinearLayout) findViewById(R.id.lt_name);
        name = (EditText) findViewById(R.id.et_name);
        email = (EditText) findViewById(R.id.et_email);
        password = (EditText) findViewById(R.id.et_password);
        viewFlipper = (ViewFlipper) findViewById(R.id.view_flipper);
        signInButton = (Button) findViewById(R.id.bt_sign_in);
        phoneNumber = (EditText) findViewById(R.id.et_phone);
        createAccountButton = (Button) findViewById(R.id.bt_create);
        loginButton.setOnClickListener(this);
        signupButton.setOnClickListener(this);
        signInButton.setOnClickListener(this);
        createAccountButton.setOnClickListener(this);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.bt_login:
                if (viewFlipper.getDisplayedChild() != 0) {
                    viewFlipper.showPrevious();
                    nameLayout.setVisibility(View.INVISIBLE);
                }
                break;

            case R.id.bt_sign_in:
                if (email != null && email.getText() != null && password != null
                        && password.getText() != null) {
                    loginUser();
                } else {
                    Toast.makeText(getApplicationContext(), getResources()
                            .getString(R.string.loginfail), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.bt_signup:
                if (viewFlipper.getDisplayedChild() != 1) {
                    viewFlipper.showNext();
                    nameLayout.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.bt_create:
                // process fields on the background thread
                String error_msg = null;
                if (!validateName()) {
                    error_msg = "Name is not valid!";
                } else if (!validateEmail()) {
                    error_msg = "Email is not valid !";
                } else if (!vaildatePhoneNumber()) {
                    error_msg = "Phone number is not valid";
                } else if (!validatePassword()) {
                    error_msg = "Password is not valid";
                }
                if (error_msg == null) {
                    insertUser();
                } else {
                    Toast.makeText(getApplicationContext(), error_msg, Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                break;
        }
    }

    private boolean validateFields() {
        return validateName() && validateEmail() && validatePassword() && vaildatePhoneNumber();
    }

    private boolean validateEmail() {
        return !(email == null || !email.getText().toString()
                .matches(Constants.VALID_EMAIL_ADDRESS_REGEX));
    }

    private boolean vaildatePhoneNumber() {
        return !(phoneNumber == null || !phoneNumber.getText().toString()
                .matches(Constants.VALID_PHONE_NUMBER_REGEX));
    }

    private boolean validatePassword() {
        return !(password == null || !password.getText().toString()
                .matches(Constants.VALID_PASSWORD_REGEX));
    }

    private boolean validateName() {
        return !(name == null || !name.getText().toString()
                .matches(Constants.VALID_NAME_REGEX));
    }

    private void insertUser(){
        new AsyncTask<String, Integer, String>() {
            @Override
            protected String doInBackground(String... params) {
                User user = new User();
                user.setName(params[0]);
                user.setEmailId(params[1]);
                user.setPhoneNumber(params[2]);
                user.setPassword(params[3]);
                return DatabaseHelper.getHelper(getApplicationContext())
                        .insertUser(user);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            }
        }.execute(name.getText().toString(), email.getText().toString()
                , phoneNumber.getText().toString(), password.getText().toString());
    }

    private void loginUser(){
        new AsyncTask<String, Integer, User>() {
            @Override
            protected User doInBackground(String... params) {
                return DatabaseHelper.getHelper(getApplicationContext())
                        .getAuthorisedUser(params[0], params[1]);
            }

            @Override
            protected void onPostExecute(User user) {
                super.onPostExecute(user);
                if (user == null) {
                    Toast.makeText(getApplicationContext(), getApplicationContext()
                                    .getResources().getString(R.string.loginfail),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Intent in = new Intent(MainActivity.this, LoginWelcomeActivity.class);
                    in.putExtra("email_id", user.getEmailId());
                    startActivity(in);
                }
            }
        }.execute(email.getText().toString(), password.getText().toString());
    }
}
