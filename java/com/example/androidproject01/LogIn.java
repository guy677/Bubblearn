package com.example.androidproject01;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.androidproject01.models.AuthViewModel;
import com.example.androidproject01.models.User;
import com.example.androidproject01.utils.baseView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogIn extends baseView {

    private static final int RC_SIGN_IN = 1 ;
    private static final String TAG = "LoginActivity";
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private AuthViewModel authViewModel;

    final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    final Pattern VALID_PASSWORD_REGEX =
            Pattern.compile("^(.+){8,}$");

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        ConstraintLayout constraintLayout = findViewById(R.id.layoutLogin);
        CardView cardView = findViewById(R.id.cardView2);
        constraintLayout.getBackground().setAlpha(150);
        cardView.getBackground().setAlpha(150);
        //Related to the Google Single sign on button
        initAuthViewModel();
        initSignInButton();
        initGoogleSignInClient();
        this.HideUI();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null) {
            User user = User.toUser(currentUser);
            updateUI(user);
        }
    }

    public void signIn(View view) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount googleSignInAccount = task.getResult(ApiException.class);
                if (googleSignInAccount != null) {
                    getGoogleAuthCredential(googleSignInAccount);
                }
            } catch (ApiException e) {
                Log.d(TAG,"Problem with google authentication");
            }
        }
    }


    public void singInNoGmail(View view) {
        EditText txtEmail = findViewById(R.id.txtEmail);
        EditText txtPassword = findViewById(R.id.txtPassword);
        String email = String.valueOf(txtEmail.getText());
        String password = String.valueOf(txtPassword.getText());
        boolean validate = validateUserAndPassword(email,password);
        if(validate) {
            authViewModel.singIn(email, password);
            authViewModel.authenticatedUserLiveData.observe(this, this::updateUI);
        }
    }

    private boolean validateUserAndPassword(String email, String password) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        boolean validEmail = matcher.matches();
        matcher = VALID_PASSWORD_REGEX.matcher(password);
        boolean validPassword = matcher.matches();
        if(!validEmail){
            Toast.makeText(this,R.string.wrongEmail,Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!validPassword){
            Toast.makeText(this,R.string.wrongPassword,Toast.LENGTH_SHORT).show();
            return false;
        }
        return  true;
    }

    public void Register(View view) {
        EditText txtEmail = findViewById(R.id.txtEmail);
        EditText txtPassword = findViewById(R.id.txtPassword);
        String email = String.valueOf(txtEmail.getText());
        String password = String.valueOf(txtPassword.getText());
        boolean validate = validateUserAndPassword(email,password);
        if(validate) {
            authViewModel.Register(email, password);
            authViewModel.authenticatedUserLiveData.observe(this, this::updateUI);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        moveTaskToBack(true);
    }

    @SuppressLint("ShowToast")
    private void updateUI(User user) {
        if(user != null) {
            Intent LoggedIn = new Intent(this, SelectCategoryActivity.class);
            startActivity(LoggedIn);
            mGoogleSignInClient.signOut();
            finish();
        }
        else{
            Toast.makeText(this,R.string.UnableLogIn,Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Initialize the google sign in onclick func
     */
    private void initSignInButton() {
        SignInButton googleSignInButton = findViewById(R.id.signInButton);
        googleSignInButton.setOnClickListener(v -> signIn(googleSignInButton));
    }


    /**
     * Initialize the googleClient for the third party log in via gmail
     */
    private void initGoogleSignInClient() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
    }

    private void getGoogleAuthCredential(GoogleSignInAccount googleSignInAccount) {
        String googleTokenId = googleSignInAccount.getIdToken();
        AuthCredential googleAuthCredential = GoogleAuthProvider.getCredential(googleTokenId, null);
        signInWithGoogleAuthCredential(googleAuthCredential);
    }

    @SuppressLint("ShowToast")
    private void signInWithGoogleAuthCredential(AuthCredential googleAuthCredential) {
        authViewModel.signInWithGoogle(googleAuthCredential);
        authViewModel.authenticatedUserLiveData.observe(this, this::updateUI);
    }

    private void initAuthViewModel() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }
}