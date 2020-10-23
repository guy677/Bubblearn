package com.example.androidproject01.Controller;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.androidproject01.models.User;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class AuthRepository {

    //     Related to the Google Single sign on button
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference usersRef = database.getReference().child("Members");

    public MutableLiveData<User> firebaseSignIn(String email, String password) {
        MutableLiveData<User> authenticatedUserMutableLiveData = new MutableLiveData<>();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        assert firebaseUser != null;
                        User user = User.toUser(firebaseUser);
                        authenticatedUserMutableLiveData.setValue(user);
                        Log.d("AuthRepository", "signInWithEmail:success");
                    } else {
                        // If sign in fails, display a message to the user.
                        authenticatedUserMutableLiveData.setValue(null);
                        Log.w("AuthRepository", "signInWithEmail:failure");
                    }

                });
        return authenticatedUserMutableLiveData;
    }

    public MutableLiveData<User> firebaseSignInWithGoogle(AuthCredential googleAuthCredential) {
        MutableLiveData<User> authenticatedUserMutableLiveData = new MutableLiveData<>();
        AtomicBoolean isNewUser = new AtomicBoolean(false);
        firebaseAuth.signInWithCredential(googleAuthCredential).addOnCompleteListener(authTask -> {
            if (authTask.isSuccessful()) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    isNewUser.set(Objects.requireNonNull(authTask.getResult()).getAdditionalUserInfo().isNewUser());
                    User user = User.toUser(firebaseUser);
                    //user.setNew(isNewUser);
                    authenticatedUserMutableLiveData.setValue(user);
                    if(isNewUser.get()) {
                        usersRef.child(user.getUid()).child("Settings").setValue(user);
                    }
                    Log.d("AuthRepository", "GoogleLogIn:success");
                }
            } else {
                authenticatedUserMutableLiveData.setValue(null);
                Log.d("AuthRepository", "GoogleLogIn:failure");
            }
        });
        return authenticatedUserMutableLiveData;
    }

    public MutableLiveData<User> Register(String email,String password){
        MutableLiveData<User> authenticatedUserMutableLiveData = new MutableLiveData<>();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("AuthViewModel", "createUserWithEmail:success");
                        FirebaseUser fireBaseuser = firebaseAuth.getCurrentUser();
                        assert fireBaseuser != null;
                        User user = User.toUser(fireBaseuser);
                        user.setName("User");
                        authenticatedUserMutableLiveData.setValue(user);
                        usersRef.child(user.getUid()).child("Settings").setValue(user);
                        Log.d("AuthRepository", "signInWithEmail:success");
                    } else {
                        authenticatedUserMutableLiveData.setValue(null);
                        // If sign in fails, display a message to the user.
                        Log.w("AuthRepository", "createUserWithEmail:failure");
                    }
                });
        return  authenticatedUserMutableLiveData;
    }

    public FirebaseUser getUser() {
        return firebaseAuth.getCurrentUser();
    }

    public void signOut() {
        FirebaseAuth.getInstance().signOut();
    }
}
