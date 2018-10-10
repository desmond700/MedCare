package com.example.medcare;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.medcare.patient.MedicalDiagnostic;
import com.example.medcare.utilities.CustomExpandableListAdapter;
import com.example.medcare.utilities.ExpandableListDataPump;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;


public class PatientViewActivity extends AppCompatActivity {

  private DrawerLayout mDrawerLayout;
  private Intent intent;
  private GoogleSignInClient mGoogleSignInClient;
  // [START declare_auth]
  private FirebaseAuth mAuth;
  // [END declare_auth]
  private String signinProvider;

  ExpandableListView expandableListView;
  ExpandableListAdapter expandableListAdapter;
  List<String> expandableListTitle;
  HashMap<String, List<MedicalDiagnostic>> expandableListDetail;
  Bundle bundle;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_patient_view);

    mDrawerLayout = findViewById(R.id.drawer_layout);
    // [START initialize_auth]
    mAuth = FirebaseAuth.getInstance();
    // [END initialize_auth]
    //mUser = mAuth.getCurrentUser();
    intent = getIntent();
    signinProvider = intent.getStringExtra("signInProvider");
    bundle = new Bundle();

    //
    Toolbar toolbar = findViewById(R.id.toolbar);
    //toolbar.setTitle("");
    //toolbar.setSubtitle("");
    setSupportActionBar(toolbar);
    ActionBar actionbar = getSupportActionBar();
    actionbar.setDisplayHomeAsUpEnabled(true);
    actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);


    expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
    expandableListDetail = ExpandableListDataPump.getData();
    expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
    expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);
    expandableListView.setAdapter(expandableListAdapter);
    expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

      @Override
      public void onGroupExpand(int groupPosition) {
        Toast.makeText(getApplicationContext(),
          expandableListTitle.get(groupPosition) + " List Expanded.",
          Toast.LENGTH_SHORT).show();
      }
    });

    expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

      @Override
      public void onGroupCollapse(int groupPosition) {
        Toast.makeText(getApplicationContext(),
          expandableListTitle.get(groupPosition) + " List Collapsed.",
          Toast.LENGTH_SHORT).show();

      }
    });

    expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
      @Override
      public boolean onChildClick(ExpandableListView parent, View v,
                                  int groupPosition, int childPosition, long id) {
        Toast.makeText(
          getApplicationContext(),
          expandableListTitle.get(groupPosition)
            + " -> "
            + expandableListDetail.get(
            expandableListTitle.get(groupPosition)).get(
            childPosition), Toast.LENGTH_SHORT
        ).show();
        return false;
      }
    });

  //
    NavigationView navigationView = findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(
      new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem menuItem) {
          // set item as selected to persist highlight
          menuItem.setChecked(false);

          switch(menuItem.getItemId()){
            case R.id.signout:
              menuItem.setChecked(true);
              signoutConfirmDialog(signinProvider);
              break;
          }
          // close drawer when item is tapped
          mDrawerLayout.closeDrawers();

          // Add code here to update the UI based on the item selected
          // For example, swap UI fragments here

          return true;
        }
      });

    TextView email = findViewById(R.id.patientsEmail);
    TextView strAddr = findViewById(R.id.strAddress);
    TextView city = findViewById(R.id.city);
    TextView country = findViewById(R.id.country);
    TextView gender = findViewById(R.id.gender);
    TextView age = findViewById(R.id.age);
    TextView dateOfBirth = findViewById(R.id.dateOfBirth);

    bundle = intent.getExtras();
    String[] address = bundle.getStringArray("address");
    String emailStr = bundle.getString("email");
    char genderStr = bundle.getChar("gender");
    String dobStr = bundle.getString("dateOfBirth");
    int ageStr = bundle.getInt("age");

    email.setText("Email: "+emailStr);
    gender.setText("Sex: "+genderStr);
    age.setText("Age: "+dobStr);
    dateOfBirth.setText("Date of birth: "+ageStr);
    actionbar.setTitle(bundle.getString("firstname") + " " + bundle.getString("lastname"));
    strAddr.setText(address[0]);
    city.setText(address[1]);
    country.setText(address[2]);
	}


  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      // Respond to the action bar's Up/Home button
      case android.R.id.home:
        //NavUtils.navigateUpFromSameTask(this);
        mDrawerLayout.openDrawer(GravityCompat.START);
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    //getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }


  public void handleSignout(String signinProvider){

    intent = new Intent(getApplicationContext(), LoginActivity.class);

    switch(signinProvider){
      case "emailpassword":
        // Firebase sign out
        mAuth.signOut();
        // Start LoginActivity
        startActivity(intent);
        break;
      case "google":
        // Firebase sign out
        mAuth.signOut();
        // Google sign out
        mGoogleSignInClient.signOut();
        // Start LoginActivity
        startActivity(intent);
        break;
      case "facebook":
        // Firebase sign out
        mAuth.signOut();
        // Facebook sign out
        LoginManager.getInstance().logOut();
        // Start LoginActivity
        startActivity(intent);
        break;
    }
  }

  private TextView getTitleTextView(Toolbar toolbar) {
    try {
      Class<?> toolbarClass = Toolbar.class;
      Field titleTextViewField = toolbarClass.getDeclaredField("mTitleTextView");
      titleTextViewField.setAccessible(true);
      TextView titleTextView = (TextView) titleTextViewField.get(toolbar);

      return titleTextView;
    }
    catch (NoSuchFieldException e) {
      e.printStackTrace();
    }catch(IllegalAccessException e){
      e.printStackTrace();
    }

    return null;
  }

  private void signoutConfirmDialog(final String signinProvider) {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);

    builder
      .setMessage("Are you sure?")
      .setPositiveButton("Yes",  new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int id) {
          // Yes-code
          handleSignout(signinProvider);
        }
      })
      .setNegativeButton("No", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog,int id) {
          dialog.cancel();
        }
      })
      .show();
  }
}
