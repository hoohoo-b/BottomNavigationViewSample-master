package bottomnav.thesevchefs.com.cooktasty;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import bottomnav.thesevchefs.com.cooktasty.cooktastyapi.APICallback;
import bottomnav.thesevchefs.com.cooktasty.cooktastyapi.UserAPI;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    @BindView(R.id.signup_name) EditText mSignupNameEditText;
    @BindView(R.id.signup_email) EditText mSignupEmailEditText;
    @BindView(R.id.signup_password) EditText mPasswordEditText;
    @BindView(R.id.signup_btn) Button mSignupBtn;
    @BindView(R.id.signup_link_login) TextView mLoginLinkTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.signup_link_login)
    public void onClickGoLoginActivity(View view) {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.signup_btn)
    public void signup() {

        if (!validate()) {
            onSignupFailed();
            return;
        }

        mSignupBtn.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        final String signupName = mSignupNameEditText.getText().toString();
        final String signupEmail = mSignupEmailEditText.getText().toString();
        final String signupPassword = mPasswordEditText.getText().toString();

        UserAPI.userSignUpAPI(this, signupEmail, signupPassword, signupName, new APICallback() {
            @Override
            public void onSuccess(Object result) {
                onSignupSuccess(signupEmail, signupPassword);
                progressDialog.dismiss();
            }
            @Override
            public void onError(Object result) {
                onSignupFailed();
                progressDialog.dismiss();
            }
        });
    }

    public void onSignupSuccess(String email, String password) {
        mSignupBtn.setEnabled(true);
        Intent resultIntent = new Intent();
        resultIntent.putExtra("email", email);
        resultIntent.putExtra("password", password);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        mSignupBtn.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = mSignupNameEditText.getText().toString();
        String email = mSignupEmailEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            mSignupNameEditText.setError("at least 3 characters");
            valid = false;
        } else {
            mSignupNameEditText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mSignupEmailEditText.setError("enter a valid email address");
            valid = false;
        } else {
            mSignupEmailEditText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 15) {
            mPasswordEditText.setError("between 4 and 15 alphanumeric characters");
            valid = false;
        } else {
            mPasswordEditText.setError(null);
        }

        return valid;
    }

}
