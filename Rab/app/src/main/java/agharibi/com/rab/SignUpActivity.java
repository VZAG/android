package agharibi.com.rab;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.prefs.BackingStoreException;

public class SignUpActivity extends Activity {

    protected EditText mUserName;
    protected EditText mPassword;
    protected EditText mEmail;
    protected Button mSignUpButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_sign_up);

        mUserName = (EditText) findViewById(R.id.signUpUserNameField);
        mPassword = (EditText) findViewById(R.id.signUpPasswordField);
        mEmail = (EditText) findViewById(R.id.signUpEmailField);
        mSignUpButton = (Button) findViewById(R.id.signUpButton);

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = mUserName.getText().toString();
                String password = mPassword.getText().toString();
                String email = mEmail.getText().toString();

                userName = userName.trim();
                password = password.trim();
                email = email.trim();

                if(userName.isEmpty() || password.isEmpty() || email.isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                        builder.setMessage(R.string.sign_up_error_message)
                                .setTitle(R.string.sign_up_error_title)
                                .setPositiveButton(android.R.string.ok, null);

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else {
                    setProgressBarIndeterminateVisibility(true);

                    BackendlessUser newUser = new BackendlessUser();
                    newUser.setEmail(email);
                    newUser.setPassword(password);
                    //newUser.getUser(userName);
                    newUser.setProperty("name", userName);




                    Backendless.UserService.register(newUser, new AsyncCallback<BackendlessUser>() {
                        @Override
                        public void handleResponse(BackendlessUser backendlessUser) {
                            setProgressBarIndeterminateVisibility(false);
                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                        }

                        @Override
                        public void handleFault(BackendlessFault backendlessFault) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                            builder.setMessage(backendlessFault.getMessage())
                                    .setTitle(R.string.sign_up_error_title)
                                    .setPositiveButton(android.R.string.ok, null);

                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    });
                }
            }
        });
    }

}
