package com.example.androidproject01.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.androidproject01.R;
import com.example.androidproject01.models.AuthViewModel;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public abstract class baseView extends AppCompatActivity {
    protected BoomMenuButton bmb;
    protected AuthViewModel authViewModel;

    /*Canceling landscape view for all activities
    **/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        loadLanguage();
        setLanguage(languageState);

    }
    public String getLanguageState() {
        return languageState;
    }

    public void setLanguageState(String languageState) {
        this.languageState = languageState;
    }

    //private MediaPlayer song = MediaPlayer.create(this,R.raw.sound);
    private String languageState;

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void HideUI(){
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }



    //Implement Your specific Parameters According to view!
    protected void SetMenu(String[] MenuHeadString, HashMap<Integer, String[]> menuStrings, String[] MenuSubString, int[] imageResources, List<Intent> menuIntent){
        initAuthViewModel();
        bmb = findViewById(R.id.bmb);
        bmb.bringToFront();
        assert bmb != null;
        bmb.setButtonEnum(ButtonEnum.Ham);
        int numberOfButtons = MenuHeadString.length + 34;
        bmb.setPiecePlaceEnum(PiecePlaceEnum.getEnum(numberOfButtons));
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.getEnum(numberOfButtons));
        for(int i=0;i<MenuHeadString.length;i++){
            menuStrings.put(i,new String[]{MenuHeadString[i],MenuSubString[i]});
        }
        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            HamButton.Builder builder = new HamButton.Builder()
                    .normalText(Objects.requireNonNull(menuStrings.get(i))[0])
                    .subNormalText(Objects.requireNonNull(menuStrings.get(i))[1])
                    .normalColor(Color.parseColor("#ffa500"))
                    .normalTextColor(Color.BLACK)
                    .subNormalTextColor(Color.BLACK)
                    .normalImageRes(imageResources[i])
                    .listener(index -> {
                        // When the boom-button corresponding this builder is clicked.
                        if(Objects.requireNonNull(menuStrings.get(index))[0].equals(getResources().getString(R.string.LogOut))){
                            authViewModel.signOut();
                            stopService(new Intent(getApplicationContext(),MusicService.class));
                            startActivity(menuIntent.get(index));
                            finish();
                        }
                        else {
                            startActivity(menuIntent.get(index));
                        }
                    });
            bmb.addBuilder(builder);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onOptionsItemSelected(View view) {
        switch (((Button) view).getText().toString()) {
            case "en":
                languageState="en";
                setLanguage(languageState);
                Toast.makeText(this, "Locale in English !", Toast.LENGTH_LONG).show();
                saveLanguage();
                break;

            case "עב":
                languageState="עב";
                setLanguage(languageState);
                Toast.makeText(this, "שונה לעיברית", Toast.LENGTH_LONG).show();
                saveLanguage();
                break;

        }
    }

    private void initAuthViewModel() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }

    public  void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager)  this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(this.getCurrentFocus()).getWindowToken(), 0);
    }

    public boolean saveLanguage() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        SharedPreferences.Editor mEdit1 = sp.edit();
        mEdit1.putString("Language", languageState);
        return mEdit1.commit();
    }

    public  void loadLanguage() {
        SharedPreferences mSharedPreference1 =   PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        languageState = (mSharedPreference1.getString("Language", null));
    }

    public void setLanguage(String prefLanguage){
        //default value on first launch of the app
        if(prefLanguage==null){prefLanguage="en";}
        switch (prefLanguage) {
            case "en":
                languageState="en";
                Locale locale = new Locale("en");
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                //Toast.makeText(this, "Locale in English !", Toast.LENGTH_LONG).show();
                saveLanguage();
                break;

            case "עב":
                languageState="עב";
                Locale locale2 = new Locale("he");
                Locale.setDefault(locale2);
                Configuration config2 = new Configuration();
                config2.locale = locale2;
                getBaseContext().getResources().updateConfiguration(config2, getBaseContext().getResources().getDisplayMetrics());
                //Toast.makeText(this, "שונה לעיברית", Toast.LENGTH_LONG).show();
                saveLanguage();
                break;

        }
    }
}
