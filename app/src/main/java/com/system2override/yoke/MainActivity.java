package com.system2override.yoke;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.app.AppOpsManager.OPSTR_GET_USAGE_STATS;
import static android.support.v4.app.AppOpsManagerCompat.MODE_ALLOWED;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;


import com.google.api.services.tasks.TasksScopes;
import com.google.api.services.tasks.model.*;
import com.squareup.otto.Subscribe;
import com.system2override.yoke.BannedAppManagement.BannedAppScreen;
import com.system2override.yoke.Models.BannedApps;
import com.system2override.yoke.Models.RoomModels.Habit;
import com.system2override.yoke.Models.Streaks;
import com.system2override.yoke.Models.TimeBank;
import com.system2override.yoke.OttoMessages.TimeBankEarnedTime;
import com.system2override.yoke.TodoManagement.TodoManagementScreen;
import com.system2override.yoke.databinding.ActivityMainBinding;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    private static final String TAG = "MainActivity";
    GoogleAccountCredential mCredential;
    public static final String[] SCOPES = { TasksScopes.TASKS_READONLY };
    private TextView mOutputText;
    private Button mCallApiButton;
    private TextView timeAvailableView;

    ProgressDialog mProgress;


    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private static final String BUTTON_TEXT = "Call Google Tasks API";
    private static final String PREF_ACCOUNT_NAME = "accountName";

    private String readFile(String file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String         line = null;
        StringBuilder  stringBuilder = new StringBuilder();
        String         ls = System.getProperty("line.separator");

        try {
            while((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }

            return stringBuilder.toString();
        } finally {
            reader.close();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);


        /*

        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Calling Google Tasks API ...");

        // Initialize credentials and service object.
        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());

*/
        activityMainBinding.button2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, TodoManagementScreen.class);
                startActivity(i);
            }
        });

        ((Button) findViewById(R.id.mainBannedAppsButton)).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent i = new Intent(MainActivity.this, BannedAppScreen.class);
               startActivity(i);
           }
        });

        ((Button) findViewById(R.id.resetStreakButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeBank timeBank = MyApplication.getTimeBank();
                Streaks streak = MyApplication.getStreaks();

                HarnessDatabase db = MyApplication.getDb();
                timeBank.resetTime();
                streak.endStreakDay();
            }
        });
        setupTimeBank();

//        activityMainBinding.mainViewTimeAvailable.setText(Long.toString(timeBank.getAvailableTime()));
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkForUsageStatsPermission(this);
//        checkForAccessFineLocation(this);
//        checkForGoogleTasksPermission(this);


    }

    private void setupTimeBank() {
        this.timeAvailableView = findViewById(R.id.mainViewTimeAvailable);
        TimeBank timeBank = MyApplication.getTimeBank();
        this.timeAvailableView.setText("Available time is " + Long.toString(timeBank.getTotalTimeForToday()/1000L));

        Button initialButton = findViewById(R.id.initialTimeButtonSave);
        initialButton.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 Intent i = new Intent(MainActivity.this, SetUsageLimitsScreen.class);
                                                 startActivity(i);
                                             }
                                         }
        );


        Button resetTimeBank = findViewById(R.id.resetTimeBank);
        resetTimeBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.getTimeBank().resetTime();
            }
        });

    }
    private void setupHabits() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: i was called");
    }

    private void checkForAccessFineLocation(Context context) {
        int hasAccessFineLocationPermission = ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION);

        if (hasAccessFineLocationPermission != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "onCreate: about to ask permission");
            ActivityCompat.requestPermissions(this, new String[] {ACCESS_FINE_LOCATION}, 1);
        }
    }

    // this only works with api 22+
    private void checkForUsageStatsPermission(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo appInfo = packageManager.getApplicationInfo(MyApplication.packageName, 0);
            AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOps.checkOpNoThrow(OPSTR_GET_USAGE_STATS, appInfo.uid, context.getPackageName());
            if (mode != MODE_ALLOWED) {
                startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
            }
        }  catch (PackageManager.NameNotFoundException e ) {
            Log.d(TAG, "checkForUsageStatsPermission: oops wrong package name");
        }
    }

    private void checkForGoogleTasksPermission(Context context) {
        if (! isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        }
    }

    /**
     * Attempt to call the API, after verifying that all the preconditions are
     * satisfied. The preconditions are: Google Play Services installed, an
     * account was selected and the device currently has online access. If any
     * of the preconditions are not satisfied, the app will prompt the user as
     * appropriate.
     */
    private void getResultsFromApi() {
        if (! isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (! isDeviceOnline()) {
       //     mOutputText.setText("No network connection available.");
            Log.d(TAG, "getResultsFromApi: No network connection available");
        } else {
            new MakeRequestTask(mCredential).execute();
        }
    }


    /**
     * Attempts to set the account used with the API credentials. If an account
     * name was previously saved it will use that one; otherwise an account
     * picker dialog will be shown to the user. Note that the setting the
     * account to use with the credentials object requires the app to have the
     * GET_ACCOUNTS permission, which is requested here if it is not already
     * present. The AfterPermissionGranted annotation indicates that this
     * function will be rerun automatically whenever the GET_ACCOUNTS permission
     * is granted.
     *
     * choose google account dialog pops up
     * then after choosing an account a dialog asking for permission to view tasks pops up
     */
    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(
                this, Manifest.permission.GET_ACCOUNTS)) {
            /*
            SharedPreferences settings =
                    this.getSharedPreferences(TodoAppConstants.ACCOUNTS_FILE, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(TodoAppConstants.GTASKS_ACCCOUNT_NAME, accountName);
            */
            SharedPreferences sharedPrefs = this.getSharedPreferences(TodoAppConstants.ACCOUNTS_FILE, Context.MODE_PRIVATE);
            String accountName = sharedPrefs.getString(TodoAppConstants.GTASKS_ACCCOUNT_NAME, null);
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
                getResultsFromApi();
            } else {
                // Start a dialog from which the user can choose an account
                startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
        }
    }

    /**
     * Called when an activity launched here (specifically, AccountPicker
     * and authorization) exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     * @param requestCode code indicating which activity result is incoming.
     * @param resultCode code indicating the result of the incoming
     *     activity result.
     * @param data Intent (containing result data) returned by incoming
     *     activity result.
     */
    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    mOutputText.setText(
                            "This app requires Google Play Services. Please install " +
                                    "Google Play Services on your device and relaunch this app.");
                } else {
                    getResultsFromApi();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings =
                                this.getSharedPreferences(TodoAppConstants.ACCOUNTS_FILE, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(TodoAppConstants.GTASKS_ACCCOUNT_NAME, accountName);
                        editor.apply();
                        mCredential.setSelectedAccountName(accountName);
                        getResultsFromApi();
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    getResultsFromApi();
                }
                break;
        }
    }

    /**
     * Respond to requests for permissions at runtime for API 23 and above.
     * @param requestCode The request code passed in
     *     requestPermissions(android.app.Activity, String, int, String[])
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }

    /**
     * Callback for when a permission is granted using the EasyPermissions
     * library.
     * @param requestCode The request code associated with the requested
     *         permission
     * @param list The requested permission list. Never null.
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Do nothing.
    }

    /**
     * Callback for when a permission is denied using the EasyPermissions
     * library.
     * @param requestCode The request code associated with the requested
     *         permission
     * @param list The requested permission list. Never null.
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Do nothing.
    }

    /**
     * Checks whether the device currently has a network connection.
     * @return true if the device has a network connection, false otherwise.
     */
    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Check that Google Play services APK is installed and up to date.
     * @return true if Google Play Services is available and up to
     *     date on this device; false otherwise.
     */
    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    /**
     * Attempt to resolve a missing, out-of-date, invalid or disabled Google
     * Play Services installation via a user dialog, if possible.
     */
    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }


    /**
     * Display an error dialog showing that Google Play Services is missing
     * or out of date.
     * @param connectionStatusCode code describing the presence (or lack of)
     *     Google Play Services on this device.
     */
    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                MainActivity.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    /**
     * An asynchronous task that handles the Google Tasks API call.
     * Placing the API calls in their own task ensures the UI stays responsive.
     */
    private class MakeRequestTask extends AsyncTask<Void, Void, List<String>> {
        private com.google.api.services.tasks.Tasks mService = null;
        private Exception mLastError = null;

        MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.tasks.Tasks.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Google Tasks API Android Quickstart")
                    .build();
        }

        /**
         * Background task to call Google Tasks API.
         * @param params no parameters needed for this task.
         */
        @Override
        protected List<String> doInBackground(Void... params) {
            try {
                return getDataFromApi();
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }

        /**
         * Fetch a list of the first 10 task lists.
         * @return List of Strings describing task lists, or an empty list if
         *         there are no task lists found.
         * @throws IOException
         */
        private List<String> getDataFromApi() throws IOException {
            // List up to 10 task lists.
            List<String> taskListInfo = new ArrayList<String>();
            TaskLists result = mService.tasklists().list()
                    .setMaxResults(Long.valueOf(10))
                    .execute();
            List<TaskList> tasklists = result.getItems();
            List<String> allTasks = new ArrayList<String>();
            if (tasklists != null) {
                for (TaskList taskList : tasklists) {
                    List<Task> resultingTasks = mService.tasks()
                            .list(taskList.getId())
                            .setShowCompleted(false)
                            .execute()
                            .getItems();
                    for (int i = 0; i < resultingTasks.size(); i++) {
                        Task resultingTask = resultingTasks.get(i);
                        allTasks.add(String.format("%s (%s)\n",
                                resultingTask.getId(), resultingTask.getTitle()));
                    }
                    /*
                    taskListInfo.add(String.format("%s (%s)\n",
                            tasklist.getTitle(),
                            tasklist.getId()));
                            */
                }
            }
            return allTasks;
        }


        @Override
        protected void onPreExecute() {
            mProgress.show();
        }

        @Override
        protected void onPostExecute(List<String> output) {
            mProgress.hide();
            if (output == null || output.size() == 0) {
                Log.d(TAG, "onPostExecute: no results returned");
            } else {
                output.add(0, "Data retrieved using the Google Tasks API:");
                Log.d(TAG, "onPostExecute: " + TextUtils.join("\n", output));
            }
        }

        @Override
        protected void onCancelled() {
            mProgress.hide();
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            MainActivity.REQUEST_AUTHORIZATION);
                } else {
                    Log.d(TAG, "onCancelled: " + "The following error occurred:\n"
                            + mLastError.getMessage());
                }
            } else {
                Log.d(TAG, "onCancelled: "+"Request cancelled.");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        TimeBank timeBank = MyApplication.getTimeBank();
        long remainingTime = timeBank.getTimeRemaining();
        Log.d(TAG, "onResume: remaining time " + Long.toString(remainingTime));
        Log.d(TAG, "onResume: spent time " + Long.toString(timeBank.getSpentTime()));
        TextView remainingTimeView = findViewById(R.id.mainTimeRemainingView);
        remainingTimeView.setText("Remaining time view is " + Long.toString(remainingTime/1000) + " seconds");
    }

    @Subscribe
    protected void updateTimeAvailableView(TimeBankEarnedTime event) {
        Log.d(TAG, "updateTimeAvailableView: ");
        TimeBank timeBank = MyApplication.getTimeBank();
        this.timeAvailableView.setText("Available time is " + Long.toString(timeBank.getTotalTimeForToday()));
    }
}
