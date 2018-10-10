package com.example.medcare;

import com.example.medcare.user.User;
import com.example.medcare.utilities.Progress;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements
  View.OnClickListener {

  private static final String TAG = "GoogleActivity";
  private static final String FBTAG = "FacebookLogin";
  private static final String EMAIL = "email";
  private static final String PUBLIC_PROFILE = "public_profile";
  private static final int RC_SIGN_IN = 9001;
  private EditText mEmailField;
  private EditText mPasswordField;

  // [START declare_auth]
  private FirebaseAuth mAuth;
  // [END declare_auth]
  private GoogleSignInClient mGoogleSignInClient;
  private CallbackManager callbackManager;
  private Intent intent;
  private LoginButton loginButton;
  private SignInButton signInButton;
  // Get database reference
  private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
  private DatabaseReference mDatabaseRef = mDatabase.getReference();
  private User user;

  // Used to load the 'native-lib' library on application startup.
  /*static {
    System.loadLibrary("native-lib");
  }*/

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    user = new User();
    intent = new Intent(getApplicationContext(), PatentsListActivity.class);

    // Views
    mEmailField = findViewById(R.id.loginEmail);
    mPasswordField = findViewById(R.id.loginPass);

    // Button
    findViewById(R.id.loginBtn).setOnClickListener(this);
    findViewById(R.id.needAndAccount).setOnClickListener(this);
    // Google sign-in button.
    signInButton = findViewById(R.id.sign_in_button);
    signInButton.setSize(SignInButton.SIZE_WIDE);
    signInButton.setOnClickListener(this);

    // Get database reference
    mDatabase = FirebaseDatabase.getInstance();
    mDatabaseRef = mDatabase.getReference();

    // [START initialize_auth]
    mAuth = FirebaseAuth.getInstance();
    // [END initialize_auth]

    // [START config_signin]
    // Configure Google Sign In
    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
      .requestIdToken(getString(R.string.default_web_client_id))
      .requestEmail()
      .build();
    // [END config_signin]

    mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    // Initialize Facebook Login button
    callbackManager = CallbackManager.Factory.create();
    loginButton = findViewById(R.id.login_button);
    loginButton.setReadPermissions(EMAIL, PUBLIC_PROFILE);
    // If you are using in a fragment, call loginButton.setFragment(this);

    LoginManager.getInstance().registerCallback(callbackManager,
      new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
          Log.d(FBTAG, "facebook:onSuccess:" + loginResult);
          handleFacebookAccessToken(loginResult.getAccessToken());
        }

        @Override
        public void onCancel() {
          Log.d(FBTAG, "facebook:onCancel");
          // [START_EXCLUDE]
          //updateUI(null);
          // [END_EXCLUDE]
        }

        @Override
        public void onError(FacebookException error) {
          Log.d(FBTAG, "facebook:onError", error);
          // [START_EXCLUDE]
          //updateUI(null);
          // [END_EXCLUDE]
        }
      });
  }

  // [START on_start_check_user]
  @Override
  public void onStart() {
    super.onStart();
    // Check if user is signed in (non-null) and update UI accordingly.
    FirebaseUser currentUser = mAuth.getCurrentUser();
    // Check if user is logged in
    AccessToken accessToken = AccessToken.getCurrentAccessToken();
    boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

    if(isLoggedIn && (currentUser != null)){

      intent = new Intent(getApplicationContext(), PatentsListActivity.class);
      intent.putExtra("signInProvider", "facebook");
      startActivity(intent);

    }else if((GoogleSignIn.getLastSignedInAccount(this) != null) && (currentUser != null)){

      intent.putExtra("signInProvider", "google");
      startActivity(intent);

    }else if(currentUser != null){

      intent.putExtra("signInProvider", "emailpassword");
      startActivity(intent);
    }


  }
  // [END on_start_check_user]

  @Override
  public void onClick(View v) {
    int i = v.getId();
    if (i == R.id.loginBtn){
      firebaseSignIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
    }else if(i == R.id.sign_in_button){
      googleSignIn();
    }
    else if(i == R.id.needAndAccount){
      intent = new Intent(this, RegisterActivity.class);
      startActivity(intent);
    }
  }



  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    super.onActivityResult(requestCode, resultCode, data);

    // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
    if (requestCode == RC_SIGN_IN) {
      Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
      try {
        // Google Sign In was successful, authenticate with Firebase
        GoogleSignInAccount account = task.getResult(ApiException.class);
        firebaseAuthWithGoogle(account);
      } catch (ApiException e) {
        // Google Sign In failed, update UI appropriately
        Log.w(TAG, "Google sign in failed", e);
        // [START_EXCLUDE]
        //updateUI(null);
        // [END_EXCLUDE]
      }
    }

    // Pass the activity result back to the Facebook SDK
    callbackManager.onActivityResult(requestCode, resultCode, data);
  }

  // [START auth_with_google]
  private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
    Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
    // [START_EXCLUDE silent]
    //showProgressDialog();
    // [END_EXCLUDE]

    AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
    if (mAuth.getCurrentUser() != null) {
      mAuth.signInWithCredential(credential)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
          @Override
          public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
              // Sign in success, update UI with the signed-in user's information
              Log.d(TAG, "signInWithCredential:success");
              FirebaseUser user = mAuth.getCurrentUser();

              intent.putExtra("signInProvider", "google");
              startActivity(intent);
              //updateUI(user);
            } else {
              // If sign in fails, display a message to the user.
              Log.w(TAG, "signInWithCredential:failure", task.getException());
              Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).setDuration(8000).show();
              //updateUI(null);
            }

            // [START_EXCLUDE]
            //hideProgressDialog();
            // [END_EXCLUDE]
          }
        });
    }else{
      mAuth.signInWithCredential(credential);
      intent.putExtra("signInProvider", "google");
      startActivity(intent);
    }
  }
  // [END auth_with_google]

  private void readFromDatabase(){

    // Read from the database
    mDatabaseRef.child("users").addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        // This method is called once with the initial value and again
        // whenever data at this location is updated.

        /*String value = dataSnapshot.getValue(String.class);
        Log.d("Database read", "Value is: " + value);*/
        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){

          user = singleSnapshot.getValue(User.class);
          Log.d("user email", user.getEmail());
          if(user.getEmail().equals(mEmailField.getText().toString().trim())){
            Log.d("user email", user.getEmail());

            //
            Log.d("firebaseSignIn","username: " + user.getUsername());
            intent.putExtra("signInProvider", "emailpassword");
            intent.putExtra("username", user.getUsername());
            startActivity(intent);
          }
        }
      }

      @Override
      public void onCancelled(DatabaseError error) {
        // Failed to read value
        Log.w(TAG, "Failed to read value.", error.toException());
      }
    });
  }

  // [START signin]
  private void googleSignIn() {
    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
    startActivityForResult(signInIntent, RC_SIGN_IN);
  }

  private void firebaseSignIn(String email, String password) {
    Log.d(TAG, "signIn:" + email);
    if (!validateForm()) {
      return;
    }

    Progress.showSimpleProgressDialog(this);

    // [START sign_in_with_email]
    mAuth.signInWithEmailAndPassword(email, password)
      .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
          if (task.isSuccessful()) {
            // Sign in success, update UI with the signed-in user's information
            Log.d(TAG, "signInWithEmail:success");

            readFromDatabase();

            Progress.removeSimpleProgressDialog();
          } else {
            // If sign in fails, display a message to the user.
            Log.w(TAG, "signInWithEmail:failure", task.getException());
            Toast.makeText(LoginActivity.this, "Authentication failed.",
              Toast.LENGTH_SHORT).show();
            Progress.removeSimpleProgressDialog();
          }

          // [START_EXCLUDE]
          if (!task.isSuccessful()) {
            Toast.makeText(getApplicationContext(),R.string.auth_failed,Toast.LENGTH_LONG).show();
          }
          Progress.removeSimpleProgressDialog();
          // [END_EXCLUDE]
        }
      });
    // [END sign_in_with_email]
  }

  // [START auth_with_facebook]
  private void handleFacebookAccessToken(AccessToken token) {
    Log.d(FBTAG, "handleFacebookAccessToken:" + token);
    // [START_EXCLUDE silent]
    //showProgressDialog();
    // [END_EXCLUDE]

    Progress.showSimpleProgressDialog(this);

    AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
    mAuth.signInWithCredential(credential)
      .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
          if (task.isSuccessful()) {
            // Sign in success, update UI with the signed-in user's information
            Log.d(FBTAG, "signInWithCredential:success");
            FirebaseUser user = mAuth.getCurrentUser();
            intent = new Intent(getApplicationContext(), PatentsListActivity.class);
            intent.putExtra("signInProvider", "facebook");
            startActivity(intent);
            //updateUI(user);
            Progress.showSimpleProgressDialog(getApplicationContext());
          } else {
            // If sign in fails, display a message to the user.
            Log.w(FBTAG, "signInWithCredential:failure", task.getException());
            Toast.makeText(LoginActivity.this, "Authentication failed.",
              Toast.LENGTH_SHORT).show();
          }

        }
      });
    Progress.removeSimpleProgressDialog();
  }
  // [END auth_with_facebook]

  private boolean validateForm() {
    boolean valid = true;

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

  /*private void updateUI(FirebaseUser user) {
    hideProgressDialog();
    if (user != null) {
      mStatusTextView.setText(getString(R.string.google_status_fmt, user.getEmail()));
      mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));

      findViewById(R.id.sign_in_button).setVisibility(View.GONE);
      findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
    } else {
      mStatusTextView.setText(R.string.signed_out);
      mDetailTextView.setText(null);

      findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
      findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
    }
  }*/

  /**
   * A native method that is implemented by the 'native-lib' native library,
   * which is packaged with this application.
   */
  //public native String stringFromJNI();
}
