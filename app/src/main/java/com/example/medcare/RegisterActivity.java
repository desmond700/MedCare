package com.example.medcare;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medcare.user.User;
import com.example.medcare.utilities.Progress;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements
  View.OnClickListener {


  private static final String TAG = "EmailPassword";

  private TextView mStatusTextView;
  private EditText mNameField;
  private EditText mEmailField;
  private EditText mPasswordField;

  // [START declare_auth]
  private FirebaseAuth mAuth;
  // [END declare_auth]
  private FirebaseDatabase mDatabase;
  private DatabaseReference mDatabaseRef;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register);


    // Views
    mStatusTextView = findViewById(R.id.textView6);
    mNameField = findViewById(R.id.editText3);
    mEmailField = findViewById(R.id.editText4);
    mPasswordField = findViewById(R.id.editText5);

    // Button
    findViewById(R.id.registerBtn).setOnClickListener(this);

    // [START initialize_auth]
    mAuth = FirebaseAuth.getInstance();
    // [END initialize_auth]

    // Get database reference
    mDatabase = FirebaseDatabase.getInstance();
    mDatabaseRef = mDatabase.getReference();
  }


  @Override
  public void onClick(View v) {
    int i = v.getId();
    if (i == R.id.registerBtn) {
      createAccount(mNameField.getText().toString(), mEmailField.getText().toString(), mPasswordField.getText().toString());
    }
  }


  private void createAccount(final String name, final String email, final String password) {
    Log.d(TAG, "createAccount:" + email);
    if (!validateForm()) {
      return;
    }

    Progress.showSimpleProgressDialog(this);

    // [START create_user_with_email]
    mAuth.createUserWithEmailAndPassword(email, password)
      .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
          if (task.isSuccessful()) {
            // Sign in success, update UI with the signed-in user's information
            Log.d(TAG, "createUserWithEmail:success");
            FirebaseUser user = mAuth.getCurrentUser();
            Log.d("userid",user.getUid());


            writeNewUser(user.getUid(), name, email, password);

            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);

            Progress.removeSimpleProgressDialog();
          } else {
            // If sign in fails, display a message to the user.
            Log.w(TAG, "createUserWithEmail:failure", task.getException());
            Toast.makeText(RegisterActivity.this, "Authentication failed.",
              Toast.LENGTH_SHORT).show();
            //updateUI(null);
          }

          // [START_EXCLUDE]
          //hideProgressDialog();
          // [END_EXCLUDE]
        }
      });
    // [END create_user_with_email]
  }

  private void writeNewUser(String userId, String name, String email, String password) {

    Map<String, User> users = new HashMap<>();
    users.put(userId, new User(name, email, password));

    mDatabaseRef.child("users").setValue(users);
  }

  private boolean validateForm() {
    boolean valid = true;

    String name = mNameField.getText().toString();
    if (TextUtils.isEmpty(name)) {
      mNameField.setError("Required.");
      valid = false;
    } else {
      mNameField.setError(null);
    }

    String email = mEmailField.getText().toString();
    if (TextUtils.isEmpty(email)) {
      mEmailField.setError("Required.");
      valid = false;
    } else {
      mEmailField.setError(null);
    }

    String password = mPasswordField.getText().toString();
    if (TextUtils.isEmpty(password)) {
      mPasswordField.setError("Required.");
      valid = false;
    } else {
      mPasswordField.setError(null);
    }

    return valid;
  }

  /*private void userProfile(){

    FirebaseUser user = mAuth.getCurrentUser();
    if(user != null){
      UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
        .setDisplayName(mNameField.getText().toString().trim())
        .build();

      user.updateProfile(profileUpdates)
        .addOnCompleteListener(new OnCompleteListener<Void>() {
          @Override
          public void onComplete(@NonNull Task<Void> task) {
            if(task.isSuccessful()){
              Log.d();
            }
          }
        })
    }
  }*/
}
