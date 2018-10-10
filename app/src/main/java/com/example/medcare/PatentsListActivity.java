package com.example.medcare;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medcare.patient.Address;
import com.example.medcare.patient.MedicalDiagnostic;
import com.example.medcare.patient.Patient;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.example.medcare.utilities.LetterSectionListAdapter;
import com.example.medcare.utilities.LetterSectionListItem;
import com.example.medcare.utilities.LetterSectionListItemComparator;

public class PatentsListActivity extends AppCompatActivity implements
  View.OnClickListener {

  private DrawerLayout mDrawerLayout;
  private GoogleSignInClient mGoogleSignInClient;
  // [START declare_auth]
  private FirebaseAuth mAuth;
  // [END declare_auth]
  private Intent intent;
  private View hView;
  private TextView navHeadEmail;
  private Button navHeadSignOut;
  private ImageView navProfileImage;
  private TextView navHeadUserName;
  private Map<String, Integer> mapIndex;
  //private ListView fruitList;
  private String signinProvider;
  private FirebaseUser mUser;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_patents_list);

    // [START initialize_auth]
    mAuth = FirebaseAuth.getInstance();
    // [END initialize_auth]
    mUser = mAuth.getCurrentUser();
    intent = getIntent();
    signinProvider = intent.getStringExtra("signInProvider");
    mDrawerLayout = findViewById(R.id.drawer_layout);

    //
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    ActionBar actionbar = getSupportActionBar();
    actionbar.setDisplayHomeAsUpEnabled(true);
    actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);


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

    //String[] fruits = getResources().getStringArray(R.array.fruits_array);

    //Arrays.asList(fruits);

    /*fruitList = findViewById(R.id.list_fruits);
    fruitList.setAdapter(new ArrayAdapter<String>(this,
      android.R.layout.simple_list_item_1, fruits));*/

    //getIndexList(fruits);

    //displayIndex();


    //
    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
      .requestIdToken(getString(R.string.default_web_client_id))
      .requestEmail()
      .build();
    // [END config_signin]

    mGoogleSignInClient = GoogleSignIn.getClient(this, gso);



    hView =  navigationView.inflateHeaderView(R.layout.nav_header);

    navHeadUserName = hView.findViewById(R.id.profileName);
    if(intent.getStringExtra("username") != null){
      navHeadUserName.setText(intent.getStringExtra("username"));
    }else{
      try{
        navHeadUserName.setText(mUser.getDisplayName());
      }catch(NullPointerException ex){
        Log.d("Display name", ex.toString());
      }

    }


    try {
      navHeadEmail = hView.findViewById(R.id.profileEmail);
      navHeadEmail.setText(mUser.getEmail());
    }catch (NullPointerException ex){
      Toast.makeText(PatentsListActivity.this, "Email couldn't be retrieved.",
        Toast.LENGTH_SHORT).show();
    }

    //provider = mAuth.getCurrentUser().getProviders().get(0);
    navProfileImage = hView.findViewById(R.id.profilePic);
    try{
      Picasso.get()
        .load(mUser.getPhotoUrl())
        .placeholder(R.drawable.user)
        .error(R.drawable.user)
        .into(navProfileImage);
    }catch(NullPointerException ex){}

    //
    createBookList();

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

  @Override
  public void onClick(View v) {
    TextView selectedIndex =(TextView)v;
    int i = v.getId();
    //if(i == R.id.side_list_item){
      //fruitList.setSelection(mapIndex.get(selectedIndex.getText()));
    //}

  }

  /*private void getIndexList(String[] fruits) {
    mapIndex = new LinkedHashMap<String, Integer>();
    for (int i = 0; i < fruits.length; i++) {
      String fruit = fruits[i];
      String index = fruit.substring(0, 1);

      if (mapIndex.get(index) == null)
        mapIndex.put(index, i);
    }
  }

  private void displayIndex() {
    LinearLayout indexLayout = findViewById(R.id.side_index);
    TextView textView;
    List<String> indexList = new ArrayList<String>(mapIndex.keySet());
    for (String index : indexList) {
      textView = (TextView) getLayoutInflater().inflate(
        R.layout.side_index_item, null);
      textView.setText(index);
      textView.setOnClickListener(this);
      indexLayout.addView(textView);
    }
  }*/



  private void createBookList(){
    List<LetterSectionListItem> books = createPatients();

    //Find the list view in my activity
    ListView bookListView = findViewById(R.id.booklistview);

    //Using custom booklist_row view that the header will be wrapped around
    LetterSectionListAdapter bookListAdapter = new LetterSectionListAdapter(this, R.layout.booklist_row, books) {
      @Override
      public View getView(int position, View v, ViewGroup parent) {
        //Must call this before to wrap the header around the view
        v = super.getView(position, v, parent);

        TextView bookTitle = v.findViewById(R.id.book_title);
        TextView authorName = v.findViewById(R.id.author_name);

        final Patient patient = (Patient) this.getItem(position);
        bookTitle.setText(patient.getFirstName() + ", " + patient.getLastName());
        authorName.setText(patient.getAddress().getCity());


        final String[] address = {
          patient.getAddress().getStreet(),
          patient.getAddress().getCity() + ", " + patient.getAddress().getProvince() + ", " + patient.getAddress().getPostalCode(),
          patient.getAddress().getCountry()
        };

        v.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {

            // Start Activity
            intent = new Intent(getApplicationContext(), PatientViewActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("firstname",patient.getFirstName());
            bundle.putString("lastname",patient.getLastName());
            bundle.putString("email",patient.getEmail());
            bundle.putChar("gender",patient.getGender());
            bundle.putString("dateOfBirth",patient.getDateOfBirth());
            bundle.putInt("age",patient.getAge());
            bundle.putStringArray("address", address);
            intent.putExtras(bundle);
            startActivity(intent);

          }
        });

        return v;
      }
    };


    bookListView.setAdapter(bookListAdapter);
  }

  private List<LetterSectionListItem> createPatients(){
    List<LetterSectionListItem> patients = new ArrayList<>();

    patients.add(new Patient(1,"cherise123@gmail.com", "Cherise", "Williams", "02/09/1995", 23, 'f', new Address(){{
      setStreet("22 Derry Rd.");
      setCity("Mississauga");
      setProvince("ON");
      setPostalCode("L4T 0A1");
      setCountry("Canadian");

    }}, new MedicalDiagnostic()));
    patients.add(new Patient(2,"bradleywilson432@gmail.com", "Bradley", "Wilson", "11/02/1986", 32, 'm', new Address(){{
      setStreet("44 Ashferd Rd.");
      setCity("Brampton");
      setProvince("ON");
      setPostalCode("L4T 3B4");
      setCountry("Canada");

    }}, new MedicalDiagnostic()));
    patients.add(new Patient(3,"deborah765@hotmail.com", "Deborah", "Ellington", "12/07/1989", 29, 'f', new Address(){{
      setStreet("2334 Keele St.");
      setCity("Toronto");
      setProvince("ON");
      setPostalCode("M4B 1B3");
      setCountry("Canada");

    }}, new MedicalDiagnostic()));
    patients.add(new Patient(4,"ashleypalmer765@gmail.com", "Ashley", "Palmer", "30/03/1993", 25, 'f', new Address(){{
      setStreet("133 Lakeshore Rd E");
      setCity("Molton");
      setProvince("ON");
      setPostalCode("M7F 3L4");
      setCountry("Canada");

    }}, new MedicalDiagnostic()));
    patients.add(new Patient(5,"cameronhilton543@yahoo.com", "Cameron", "Hilton", "24/04/1982", 36, 'm', new Address(){{
      setStreet("77 Dixie Rd.");
      setCity("Mississauga");
      setProvince("ON");
      setPostalCode("L4T 1P3");
      setCountry("Canada");

    }}, new MedicalDiagnostic()));

    for (LetterSectionListItem patient : patients) {
      patient.calculateSortString();
    }

    return patients;
  }


  private List<LetterSectionListItem> createBooks(){
    List<LetterSectionListItem> books = new ArrayList<>();

    books.add(new Book(){{
      setBookId(1);
      setBookName("Duty");
      setAuthorFirstName("Robert");
      setAuthorLastName("Gates");
    }});
    books.add(new Book(){{
      setBookId(2);
      setBookName("A Short Guide to a Long Life");
      setAuthorFirstName("David");
      setAuthorLastName("Agus");
    }});
    books.add(new Book(){{
      setBookId(3);
      setBookName("The Fault in Our Stars");
      setAuthorFirstName("John");
      setAuthorLastName("Green");
    }});
    books.add(new Book(){{
      setBookId(4);
      setBookName("Lone Survivor");
      setAuthorFirstName("Marcus");
      setAuthorLastName("Luttrell");
    }});
    books.add(new Book(){{
      setBookId(5);
      setBookName("Divergent Series Complete Box Set");
      setAuthorFirstName("Veronica");
      setAuthorLastName("Roth");
    }});
    books.add(new Book(){{
      setBookId(6);
      setBookName("S.");
      setAuthorFirstName("J.J.");
      setAuthorLastName("Adams");
    }});
    books.add(new Book(){{
      setBookId(7);
      setBookName("The Invention of Wings");
      setAuthorFirstName("Sue Monk");
      setAuthorLastName("Kidd");
    }});
    books.add(new Book(){{
      setBookId(8);
      setBookName("Hollow City");
      setAuthorFirstName("Ransom");
      setAuthorLastName("Riggs");
    }});
    books.add(new Book(){{
      setBookId(9);
      setBookName("The Goldfinch");
      setAuthorFirstName("Donna");
      setAuthorLastName("Tartt");
    }});
    books.add(new Book(){{
      setBookId(10);
      setBookName("The Book Thief");
      setAuthorFirstName("Markus");
      setAuthorLastName("Zusak");
    }});
    books.add(new Book(){{
      setBookId(11);
      setBookName("The Body Book");
      setAuthorFirstName("Cameron");
      setAuthorLastName("Diaz");
    }});
    books.add(new Book(){{
      setBookId(12);
      setBookName("Gone Girl");
      setAuthorFirstName("Gillian");
      setAuthorLastName("Flynn");
    }});

    for (LetterSectionListItem book : books) {
      book.calculateSortString();
    }

    return books;
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

