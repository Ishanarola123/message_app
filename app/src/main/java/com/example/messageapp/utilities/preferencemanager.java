package com.example.messageapp.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import dagger.multibindings.StringKey;

public class preferencemanager {

    private final SharedPreferences sharedPreferences;

    public preferencemanager(Context context){
        sharedPreferences=context.getSharedPreferences(constants.KEY_PREFERENCE_NAME,Context.MODE_PRIVATE);

    }
    public void putBoolean(String Key,Boolean value){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean(Key,value);
        editor.apply();

    }
    public Boolean getBoolean(String Key)
    {
        return sharedPreferences.getBoolean(Key,false);

    }
    public void putString (String Key ,String value)
    {
       SharedPreferences.Editor editor=sharedPreferences.edit();
       editor.putString(Key,value);
       editor.apply();
    }
    public String getString (String key)
    {
        return sharedPreferences.getString(key,null);

    }
    public void clear()
    {
        SharedPreferences.Editor editor =sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

}
