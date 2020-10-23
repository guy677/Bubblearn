package com.example.androidproject01.models;


import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.androidproject01.Controller.AuthRepository;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseUser;

public class AuthViewModel extends AndroidViewModel {
    private AuthRepository authRepository;
    public LiveData<User> authenticatedUserLiveData;

    public FirebaseUser getUser()
    {
        return authRepository.getUser();
    }
    public AuthViewModel(Application application) {
        super(application);
        authRepository = new AuthRepository();
    }
//    Related to the Google Single sign on button
    public void signInWithGoogle(AuthCredential googleAuthCredential) {
        authenticatedUserLiveData = authRepository.firebaseSignInWithGoogle(googleAuthCredential);
    }

    public void singIn(String email, String password){
        authenticatedUserLiveData = authRepository.firebaseSignIn(email,password);
    }

    public void Register(String email, String password){
        authenticatedUserLiveData = authRepository.Register(email,password);
    }

    public void signOut() {
        authRepository.signOut();
    }
}