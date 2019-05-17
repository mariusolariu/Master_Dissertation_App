package com.example.myapplication.util;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.myapplication.R;
import com.example.myapplication.ui.MainActivity;


/**
 * Whenever the device doesn't have an Internet connection the user is forced to connect using Wifi/Mob Data.
 * This class creates the needed dialog
 */
public class InternetConnectivityHelper {
    private static InternetConnectivityHelper instance;
    private Context appContext;
    private AlertDialog internetAlertDialog;

    private final int WIFI = 0;
    private final int MOBILE_DATA = 1;


    private InternetConnectivityHelper(){}

    public static InternetConnectivityHelper getInstance(Context context){
        if (instance == null) {
            instance = new InternetConnectivityHelper();
            instance.appContext = context;
            instance.createInternetDialog();
        }

        return instance;
    }

    private void createInternetDialog() {
        final Builder builderIAD = new Builder(appContext);
        CharSequence[] options = {"Wi-Fi", "Mobile data"};

        builderIAD.setTitle(R.string.network_off_title);
        /**
         * DialogInterface - Interface that defines a dialog-type class that can be shown, dismissed, or canceled, <br>
         * and may have buttons that can be clicked.
         */

        final DialogInterfListener hmsDialogInterfListener = new DialogInterfListener();

        builderIAD.setSingleChoiceItems(options, 0, hmsDialogInterfListener);

        builderIAD.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int selectedOption = hmsDialogInterfListener.getSelectedOption();

                switch (selectedOption) {
                    case WIFI:
                        try {
                            Intent wifiIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                            appContext.startActivity(wifiIntent);
                        } catch (ActivityNotFoundException e) {
                            e.printStackTrace();
                            Log.v(MainActivity.YMCA_TAG, "Wifi Settings activity is not present");
                        }

                        break;

                    case MOBILE_DATA:
                        try {
                            Intent dataUsageI = new Intent();
                            dataUsageI.setComponent(new ComponentName("com.android.settings","com.android.settings.Settings$DataUsageSummaryActivity"));
                            appContext.startActivity(dataUsageI);
                        } catch (ActivityNotFoundException e) {
                            e.printStackTrace();
                            Log.v(MainActivity.YMCA_TAG, "Data settings usage activity is not present");
                        }
                        break;
                }

            }
        });

        builderIAD.setNegativeButton(R.string.close_app, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((AppCompatActivity) appContext).finishAffinity();

                //FIXME might not be the best solution
//                System.exit(0);
            }
        });


        internetAlertDialog = builderIAD.create();
    }

    private class DialogInterfListener implements DialogInterface.OnClickListener {

        private int selectedOption;

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            switch (i) {
                case WIFI:
                    selectedOption = 0;
                    break;

                case MOBILE_DATA:
                    selectedOption = 1;
                    break;
            }
        }

        public int getSelectedOption() {
            return selectedOption;
        }
    }

    public void showDialog(){
        internetAlertDialog.show();
    }

    public boolean isDialogShowing(){ return internetAlertDialog.isShowing();}

    public void dismissDialog(){internetAlertDialog.dismiss();}

}
