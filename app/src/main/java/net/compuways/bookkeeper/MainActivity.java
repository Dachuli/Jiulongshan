package net.compuways.bookkeeper;


import android.*;
import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewParent;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import net.compuways.bookkeeper.util.IabHelper;
import net.compuways.bookkeeper.util.IabResult;
import net.compuways.bookkeeper.util.Inventory;
import net.compuways.bookkeeper.util.Purchase;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,RewardedVideoAdListener,LocationListener {

    String endcode = "颽醢䞔";
    String[] comacode = new String[]{"龘罾", "襬甕", "嵾皦", "蠹䒉", "堊搋", "豟鑴", "嬔篺", "鼖䪀"};
    private SharedPreferences SP;
    String dir = "DESC";
    GridLayout gridLayout, entries;
    ArrayList<Item> datarepareitems;
    long entryid = -918;
    ArrayList<Item> names = new ArrayList<>(),ulnames = new ArrayList<>();
    Item ucode;
    IabHelper mHelper;
    Purchase purchase;
    private InterstitialAd mInterstitialAd;
    RewardedVideoAd mRewardedVideoAd;

    String cs = "", unit = "";
    double drate = 0, inshare = -1, gasshare = -1, datashare = -1, entrytotal = 0;

    private static ArrayList<Integer> order = new ArrayList<>(),ulorder = new ArrayList<>();
    private int ID = -1;
    public long previousentry = H.ENTRIES;
    EntryFragment fragment = null;
    int page = 0, limit = 500;
    String baseday = "";
    private NavigationView navigationView;
    private Menu menu;
    public String cvsdata="";
    long adsfreetime=0,adsstart=0;
    static boolean rewardadsrun=false;
    Thread td=null;
    double lat=0,lon=0;
    Drawable drawable=null,editdrawable=null;
    //********************************************************************//

    BKViewModel vm;

    private void newoncreate(Bundle savedInstanceState){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            actionBar.setTitle("");
        }

        if (savedInstanceState != null) {
            entryid = savedInstanceState.getLong("entryid");
            page = savedInstanceState.getInt("page");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        vm=new BKViewModel(new Respositary());//?????????????????????dependency?
        vm.getMainInitLiveData().observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                localdatainit();
            }
        });
        vm.getMainInternetLiveData().observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                internetdatainit();
            }
        });
        taskdispatcher(vm);

//        fragment = EntryFragment.newInstance(entryid);
//        fragment.setmain(this);
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.container, fragment)
//                .commit();

   //     SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
      //  basicsInit();
      //  backgroundInit();

    }

    /*after Activity get data from ViewModel, taskdispatch decide which operation to proceed based on the data
    if disclaimed not set, display disclaimer message
    if paid user, then....
    if unpaid user, then....

    */
    private void taskdispatcher(BKViewModel vm){

    }
    private void localdatainit(){

    }
    private void internetdatainit(){

    }



    //***********************************************************************//

    private void basicsInit(){
        SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        if (SP.getBoolean("PAID", false)) {
            H.PAID = true;
        } else {
            H.PAID = false;
        }

    }

    private void backgroundInit(){
        limit = Integer.parseInt(SP.getString("pagelength", "1000"));

        //If Mileage Reading is not set, initate all SP settings
        if (SP.getInt("E12", -1) == -1) {
            initiateSP();
        }

        if(!SP.getBoolean("PAID", false)) {// if not paid check every time
            puridcheck();
        }

        Menu menu_nav = navigationView.getMenu();
        MenuItem sp = menu_nav.findItem(R.id.nav_email);
        //To be implemented in other places
        //*
        if (H.PAID) {
            sp.setTitle("Emails Support");
        } else {
            sp.setTitle("Suggestions");
        }
        //*/

//        DatabaseHandler db = DatabaseHandler.getInstance(this);
//        ArrayList<Item> settings = db.getItemsByName(H.SETTINGKEY, H.ASC, -1, 100000);
//
//
//        // System.out.println("Settings++++++++++++++++++++++++++++\n=++++++++++++++++++++++++++++++++++++++++++++++++++++ Size="+settings.size());
//        if (SP.getBoolean("disclaimerv33", true)) {
//            SP.edit().putString("order", "").commit();
//            SP.edit().putString("ulorder", "").commit();
//        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(true){
            newoncreate(savedInstanceState);
            return;
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            actionBar.setTitle("");
        }

        if (savedInstanceState != null) {
            entryid = savedInstanceState.getLong("entryid");
            page = savedInstanceState.getInt("page");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);




        SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        if (SP.getBoolean("PAID", false)) {
            H.PAID = true;
        } else {
            H.PAID = false;
        }


        if(!SP.getBoolean("PAID", false)) {// if not paid check every time
           puridcheck();
        }
        //--------------


        //If Mileage Reading is not set, initate all SP settings
        if (SP.getInt("E12", -1) == -1) {
            initiateSP();
        }


        Menu menu_nav = navigationView.getMenu();
        MenuItem sp = menu_nav.findItem(R.id.nav_email);
        if (H.PAID) {
            sp.setTitle("Emails Support");
        } else {
            sp.setTitle("Suggestions");
        }

        limit = Integer.parseInt(SP.getString("pagelength", "1000"));

        DatabaseHandler db = DatabaseHandler.getInstance(this);
        ArrayList<Item> settings = db.getItemsByName(H.SETTINGKEY, H.ASC, -1, 100000);


       // System.out.println("Settings++++++++++++++++++++++++++++\n=++++++++++++++++++++++++++++++++++++++++++++++++++++ Size="+settings.size());
        if (SP.getBoolean("disclaimerv33", true)) {
            SP.edit().putString("order", "").commit();
            SP.edit().putString("ulorder", "").commit();
        }

        //When restoration operation, Orderupdate is true
        if (SP.getBoolean("Orderupdate", false)) {
            int importversion = SP.getInt("importversion", -1);
            if (importversion == 1) {
                olditems(db);
                ver3items(db);
            } else if (importversion == 2) {
                ver3items(db);
            }
            /*/
            else{
                Item settingitem = new Item();
                settingitem.set_name(H.TRIPITEM);
                settingitem.set_note("EARNINGS_BOOST_NON_COMMISSIONABLE");
                settingitem.set_rcdate(H.UBOOST);
                db.addItem(settingitem);

                settingitem = new Item();
                settingitem.set_name(H.SETTINGKEY);
                settingitem.set_date(H.RECEIVABLE);
                settingitem.set_note("[UBER BOOST]");//unit
                settingitem.set_rcdate(H.UBOOST);
                settingitem.set_source((short) 8);//"1000"
                settingitem.set_amount(H.UBER);
                db.addItem(settingitem);
            }
            //*/
            settings = db.getItemsByName(H.SETTINGKEY, H.ASC, -1, 100000);
            reset(settings);
            H.updateSPOrder(order, SP);// update new order
            H.updateSPULOrder(ulorder, SP);// update new order

            SP.edit().putBoolean("Orderupdate", false).commit();
            SP.edit().putInt("importversion", -1).commit();
        } else if (settings.size() < 6) {

            olditems(db);
            ver3items(db);
            settings = db.getItemsByName(H.SETTINGKEY, H.ASC, -1, 100000);
            SP.edit().putString("order", "").commit();
            SP.edit().putString("ulorder", "").commit();

        }else if(!checkver3(settings)){
            ver3items(db);
            settings = db.getItemsByName(H.SETTINGKEY, H.ASC, -1, 100000);
            SP.edit().putString("order", "").commit();// to make sure size of order =size of settingkeys
            SP.edit().putString("ulorder", "").commit();

        }else{
            reset(settings);
        }


        //if an order exsits
        String result = SP.getString("order", "");
        if (result.length() > 5) {
            String[] rss = result.split(",");
            order.clear();
            for (int i = 0; i < rss.length; i++) {
                order.add(Integer.valueOf(rss[i]));
            }

        }

        String resultul = SP.getString("ulorder", "");
        if (resultul.length() > 5) {
            String[] rss = resultul.split(",");
            ulorder.clear();
            for (int i = 0; i < rss.length; i++) {
                ulorder.add(Integer.valueOf(rss[i]));
            }

        }


        if (settings.size() > 6) {

            for (Item s : settings) {


                switch ((int) Math.round(s.get_date())) {
                    case 0:
                        gasshare = s.get_amount();
                        // startingm.setText(""+gasshare);
                        break;
                    case 1:
                        drate = s.get_amount();
                        // dratem.setText(""+drate);
                        break;
                    case 2:
                        inshare = s.get_amount();
                        //   insharem.setText(""+inshare);
                        break;
                    case 3:
                        cs = s.get_note().trim();
                        break;
                    case 4:
                        unit = s.get_note().trim();
                        break;
                    case 5:
                        datashare = s.get_amount();
                        //  datasharem.setText(""+datashare);
                        break;
                }

            }
        }


        String u = SP.getString("unit", "xxx");
        if (unit.equalsIgnoreCase("")) {
            unit = SP.getString("unit", "xxx");
            if (unit.equalsIgnoreCase("xxx")) {
                SP.edit().putString("unit", "imperial").commit();
                unit = SP.getString("unit", "xxx");
            }
        } else {

            if ((!u.equalsIgnoreCase("xxx")) && (!unit.equalsIgnoreCase(u))) {
                unit = u;
            }
        }


        String y = SP.getString("csymbol", "yyy");

        if (cs.equalsIgnoreCase("")) {
            cs = SP.getString("csymbol", "yyy");
            if (cs.equalsIgnoreCase("yyy")) {
                SP.edit().putString("csymbol", "$").commit();
                cs = SP.getString("csymbol", "yyy");
            }
        } else {
            if ((!y.equalsIgnoreCase("yyy")) && (!cs.equalsIgnoreCase(y))) {
                cs = y;
            }


        }

        if (cs.equalsIgnoreCase("yyy")) {
            SP.edit().putString("csymbol", "$").commit();
            cs = "$";
        }


        //System.out.println(" Remind out paid========================?"+H.PAID);
        if (!H.PAID) {
            SP.edit().putString("usageshare", "ON").commit();
            H.remind(this);
            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId("ca-app-pub-1632769004072873/7315910176");//real one
            //mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");//test
            mInterstitialAd.loadAd(new AdRequest.Builder().build());

            mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
            mRewardedVideoAd.setRewardedVideoAdListener(this);
            loadRewardedVideoAd();

        } else {
            H.MSGLEN = 500;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                sendMsg("sn="+DatabaseHandler.getInstance(MainActivity.this).getSN(),H.WK_BONUS);//to retrieve account info
            }
        }).start();

        if (SP.getLong("Instaldate", 0) == 0) {
            SP.edit().putLong("Instaldate", System.currentTimeMillis()).commit();
        } else {
            H.LIFE = System.currentTimeMillis() - SP.getLong("Instaldate", 0);
        }

        //System.out.println("Life==========xxxxxxxxxxxxxxxxxxxxxxxxxxxxx="+(H.LIFE/1000/60/60/24));
        adsfreetime=SP.getLong("adsfreetime",0);

        if (entryid == -918) {
            entryid = H.ENTRIES;
        }

        locationop();

        drawable=getResources().getDrawable(R.drawable.go);
        editdrawable=getResources().getDrawable(R.drawable.ic_mode_edit_black_24dp);

        fragment = EntryFragment.newInstance(entryid);
        fragment.setmain(this);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
        dataop();
    }

    private void locationop(){
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        2);
            }
        }else{
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
    }

    private void loadRewardedVideoAd() {


       // mRewardedVideoAd.loadAd("ca-app-pub-1632769004072873/4179789259",
        //        new AdRequest.Builder().build()); //Real one
        mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
                new AdRequest.Builder().build());// test one
    }

    private boolean checkver3(ArrayList<Item> settings){
        //check if settings contains newer item
        boolean newitem = false;
        for (Item it : settings) {
            if (it.get_rcdate() == H.TRIPDETAIL) {
                newitem = true;
                break;
            }
        }

        return newitem;
    }
    private void puridcheck(){
        String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgGBGL2uuFLBS+h80VRwlkhfYIubB3byEzGQqmQmir0ZuWDjyM8cY0Mj/r2JVMsdUlQOR7V5umwe2Jfih1SqYFyll1gLLzIAIh3zOCBhASNKWRsIK+Get9sAvhqFZNNMyFjo330qGSH7BpjfBtvmlKTHxEB9LzEzQ99kBcSeRE2VNIaOIArnrKzlF+hsHy4V1Y0a2ggRltRjSyAqyVQZdAc5pIWmnQ4dkoCuVOEu5bwU8bYHtOJdRXKHt576AUouluRKGiiPPUY5Iu4TaIzLUEAIg7aYBOjzlsaZ4Apo3FZgKX66gpGXfvdjxHqrYQV7+I+wouPe+xeMLLPjEQUC/pwIDAQAB";
        // compute your public key and store it in base64EncodedPublicKey
        mHelper = new IabHelper(this, base64EncodedPublicKey);
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    H.pupmsg(MainActivity.this, "In-app Billing set up problem:" + result);
                } else {
                    try {
                        mHelper.queryInventoryAsync(mGotInventoryListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        e.printStackTrace();
                    }


                }

            }
        });

    }
    private void reset(ArrayList<Item> settings) {
        names.clear();
        order.clear();
        ulnames.clear();
        ulorder.clear();
        int oi = 0,uloi=0;
        for (int i = 0; i < settings.size(); i++) {

            if (settings.get(i).get_date() >= H.NONCOUNTABLE && settings.get(i).get_date() <= H.PAYABLE) {
                if (settings.get(i).get_rcdate() != H.SUMMARY && settings.get(i).get_rcdate() != H.DSETTING) {

                        if(settings.get(i).get_rcdate()>=30000){
                            ulnames.add(settings.get(i));
                            ulorder.add(0, uloi);
                            uloi++;
                        }else{
                            names.add(settings.get(i));
                            order.add(0, oi);
                            oi++;
                        }

                }

            }

        }

    }


    private void ver3items(DatabaseHandler db) {


        //the following items are for known items from online account and matching entry
        Item settingitem = new Item();
        settingitem.set_name(H.TRIPITEM);
        settingitem.set_note("FARE");
        settingitem.set_rcdate(H.FARE);
        db.addItem(settingitem);


        settingitem = new Item();
        settingitem.set_name(H.TRIPITEM);
        settingitem.set_note("SURGE");
        settingitem.set_rcdate(H.FARE);
        db.addItem(settingitem);


        settingitem = new Item();
        settingitem.set_name(H.TRIPITEM);
        settingitem.set_note("WAIT TIME");//for wait time income
        settingitem.set_rcdate(H.WTINCOME);
        db.addItem(settingitem);

        settingitem = new Item();
        settingitem.set_name(H.TRIPITEM);
        settingitem.set_note("PREPARE TIME");// for duration of time
        settingitem.set_rcdate(H.PREPARETIME);
        db.addItem(settingitem);

        settingitem = new Item();
        settingitem.set_name(H.TRIPITEM);
        settingitem.set_note("UBER FEE");
        settingitem.set_rcdate(H.FEES);
        db.addItem(settingitem);

        settingitem = new Item();
        settingitem.set_name(H.TRIPITEM);
        settingitem.set_note("PRIME TIME");
        settingitem.set_rcdate(H.PBONUS);
        db.addItem(settingitem);

        settingitem = new Item();
        settingitem.set_name(H.TRIPITEM);
        settingitem.set_note("TRIP DETAIL");
        settingitem.set_rcdate(H.TRIPDETAIL);
        db.addItem(settingitem);

        settingitem = new Item();
        settingitem.set_name(H.TRIPITEM);
        settingitem.set_note("BOOKING FEE (PAYMENT)");
        settingitem.set_rcdate(H.UBKP);
        db.addItem(settingitem);

        settingitem = new Item();
        settingitem.set_name(H.TRIPITEM);
        settingitem.set_note("BOOKING FEE (DEDUCTION)");
        settingitem.set_rcdate(H.UBKD);
        db.addItem(settingitem);

        settingitem = new Item();
        settingitem.set_name(H.TRIPITEM);
        settingitem.set_note("TIPS");//?????
        settingitem.set_rcdate(H.TIPS);
        db.addItem(settingitem);

        settingitem = new Item();
        settingitem.set_name(H.TRIPITEM);
        settingitem.set_note("DISTANCE");
        settingitem.set_rcdate(H.DISTANCE);
        db.addItem(settingitem);

        settingitem = new Item();
        settingitem.set_name(H.TRIPITEM);
        settingitem.set_note("CANCELLATION");
        settingitem.set_rcdate(H.CANCELLATION);
        db.addItem(settingitem);

        settingitem = new Item();
        settingitem.set_name(H.TRIPITEM);
        settingitem.set_note("FARE_ADJUSTMENT_DELTA");
        settingitem.set_rcdate(H.FAREADJ);
        db.addItem(settingitem);

        settingitem = new Item();
        settingitem.set_name(H.TRIPITEM);
        settingitem.set_note("LYFT EARNINGS");
        settingitem.set_rcdate(H.WK_EARNING);
        db.addItem(settingitem);

        settingitem = new Item();
        settingitem.set_name(H.TRIPITEM);
        settingitem.set_note("DRIVER REFERRAL");
        settingitem.set_rcdate(H.WK_BONUS);
        db.addItem(settingitem);

        settingitem = new Item();
        settingitem.set_name(H.TRIPITEM);
        settingitem.set_note("LYFT BONUS");
        //settingitem.set_rcdate(H.FARE);
        settingitem.set_rcdate(H.WK_BONUS);
        db.addItem(settingitem);

        settingitem = new Item();
        settingitem.set_name(H.TRIPITEM);
        settingitem.set_note("BONUSES");
        //settingitem.set_rcdate(H.FARE);
        settingitem.set_rcdate(H.BONUS);
        db.addItem(settingitem);

        settingitem = new Item();
        settingitem.set_name(H.TRIPITEM);
        settingitem.set_note("BONUS");
        //settingitem.set_rcdate(H.FARE);
        settingitem.set_rcdate(H.BONUS);  //LYFT BONUS
        db.addItem(settingitem);

        settingitem = new Item();
        settingitem.set_name(H.TRIPITEM);
        settingitem.set_note("POWER DRIVER BONUS");
        settingitem.set_rcdate(H.WK_PD_BONUS);
        db.addItem(settingitem);

        settingitem = new Item();
        settingitem.set_name(H.TRIPITEM);
        settingitem.set_note("PAYOUT");
        settingitem.set_rcdate(H.UB_PAYOUT);
        db.addItem(settingitem);


        settingitem = new Item();
        settingitem.set_name(H.TRIPITEM);
        settingitem.set_note("LYFT WK_TAXS");
        settingitem.set_rcdate(H.TAXS);//
        db.addItem(settingitem);

        settingitem = new Item();
        settingitem.set_name(H.TRIPITEM);
        settingitem.set_note("LYFT TIPS");
        settingitem.set_rcdate(H.TIPS);
        db.addItem(settingitem);

        settingitem = new Item();
        settingitem.set_name(H.TRIPITEM);
        settingitem.set_note("LYFT CANCEL_EARNING");
        settingitem.set_rcdate(H.CANCELLATION);
        db.addItem(settingitem);

        settingitem = new Item();
        settingitem.set_name(H.TRIPITEM);
        settingitem.set_note("CANCEL EARNINGS");
        settingitem.set_rcdate(H.TRIPCANCEL);
        db.addItem(settingitem);

        settingitem = new Item();
        settingitem.set_name(H.TRIPITEM);
        settingitem.set_note("LYFT WK_TOLLS");
        settingitem.set_rcdate(H.TOLL);
        db.addItem(settingitem);

        settingitem = new Item();
        settingitem.set_name(H.TRIPITEM);
        settingitem.set_note("TOLLS");
        settingitem.set_rcdate(H.TRIPTOLL);
        db.addItem(settingitem);

        settingitem = new Item();
        settingitem.set_name(H.TRIPITEM);
        settingitem.set_note("WEEK HOURS");
        settingitem.set_rcdate(H.WK_ONLINEHRS);
        db.addItem(settingitem);

        settingitem = new Item();
        settingitem.set_name(H.TRIPITEM);
        settingitem.set_note("LYFTTIP");
        settingitem.set_rcdate(H.TRIPTIP);
        db.addItem(settingitem);

        settingitem = new Item();
        settingitem.set_name(H.TRIPITEM);
        settingitem.set_note("LYFTPAYOUT");
        settingitem.set_rcdate(H.UB_PAYOUT);
        db.addItem(settingitem);

        settingitem = new Item();
        settingitem.set_name(H.TRIPITEM);
        settingitem.set_note("LYFT WK_BONUS");
        settingitem.set_rcdate(H.WK_BONUS);
        db.addItem(settingitem);

        settingitem = new Item();
        settingitem.set_name(H.TRIPITEM);
        settingitem.set_note("EARNINGS_BOOST_NON_COMMISSIONABLE");
        settingitem.set_rcdate(H.UBOOST);
        db.addItem(settingitem);

      //  System.out.println("Tripitem added=========================");

//-------------Item setup----------------

            /*/date =1000 non calable item
               date=1001 Receivable Item
               date=1002 Payable Item
             */

        //*/


        settingitem = new Item();
        settingitem.set_name(H.SETTINGKEY);
        settingitem.set_date(H.NONCOUNTABLE);
        settingitem.set_note("[TRIP DETAIL]");//unit
        settingitem.set_rcdate(H.TRIPDETAIL);
        settingitem.set_source((short) 12);//"1100"
        settingitem.set_amount(H.ALLS);
        db.addItem(settingitem);
        //  names.add(settingitem);
        // order.add(3);// to be removed?


        settingitem = new Item();
        settingitem.set_name(H.SETTINGKEY);
        settingitem.set_date(H.RECEIVABLE);
        settingitem.set_note("[UBER FARE]");//unit
        settingitem.set_rcdate(H.FARE);
        settingitem.set_source((short) 8);//"1110"
        settingitem.set_amount(H.UBER);
        db.addItem(settingitem);
        //names.add(settingitem);
        ////order.add(15);

        settingitem = new Item();
        settingitem.set_name(H.SETTINGKEY);
        settingitem.set_date(H.RECEIVABLE);
        settingitem.set_note("[UBER WT INCOME]");//wait time income
        settingitem.set_rcdate(H.WTINCOME);
        settingitem.set_source((short) 8);//"1110"
        settingitem.set_amount(H.UBER);
        db.addItem(settingitem);
        //names.add(settingitem);
        ////order.add(16);

        settingitem = new Item();
        settingitem.set_name(H.SETTINGKEY);
        settingitem.set_date(H.RECEIVABLE);
        settingitem.set_note("[CANCELLATION]");//unit
        settingitem.set_rcdate(H.CANCELLATION);
        settingitem.set_source((short) 12);//"1100"
        settingitem.set_amount(H.UBER);
        db.addItem(settingitem);
        //names.add(settingitem);
        //order.add(17);


        settingitem = new Item();
        settingitem.set_name(H.SETTINGKEY);
        settingitem.set_date(H.PAYABLE);
        settingitem.set_note("[UBER FEES]");//unit
        settingitem.set_rcdate(H.FEES);
        settingitem.set_source((short) 8);//"1000"
        settingitem.set_amount(H.UBER);
        db.addItem(settingitem);


        settingitem = new Item();
        settingitem.set_name(H.SETTINGKEY);
        settingitem.set_date(H.RECEIVABLE);
        settingitem.set_note("[UBER BOOST]");//unit
        settingitem.set_rcdate(H.UBOOST);
        settingitem.set_source((short) 8);//"1000"
        settingitem.set_amount(H.UBER);
        db.addItem(settingitem);
        //names.add(settingitem);
        //order.add(18);


        settingitem = new Item();
        settingitem.set_name(H.SETTINGKEY);
        settingitem.set_date(H.NONCOUNTABLE);
        settingitem.set_note("[TAXS]");//unit
        settingitem.set_rcdate(H.TAXS);
        settingitem.set_source((short) 14);//"1000"
        settingitem.set_amount(H.ALLS);
        db.addItem(settingitem);
        //names.add(settingitem);
        //order.add(20);

        settingitem = new Item();
        settingitem.set_name(H.SETTINGKEY);
        settingitem.set_date(H.RECEIVABLE);
        settingitem.set_note("[TIPS]");//unit
        settingitem.set_rcdate(H.TIPS);
        settingitem.set_source((short) 12);//"1100"
        settingitem.set_amount(H.UBER);
        db.addItem(settingitem);
        //names.add(settingitem);
        //order.add(21);

        settingitem = new Item();
        settingitem.set_name(H.SETTINGKEY);
        settingitem.set_date(H.NONCOUNTABLE);
        settingitem.set_note("[TOLL FEES]");//unit
        settingitem.set_rcdate(H.TOLL);
        settingitem.set_source((short) 14);//"1000"
        settingitem.set_amount(H.ALLS);
        db.addItem(settingitem);
        //names.add(settingitem);
        //order.add(22);

        settingitem = new Item();
        settingitem.set_name(H.SETTINGKEY);
        settingitem.set_date(H.PAYABLE);
        settingitem.set_note("[OTHER COST/DEDUCTION]");//unit
        settingitem.set_rcdate(H.OCOST);
        settingitem.set_source((short) 12);//"1100"
        settingitem.set_amount(H.ALLS);
        db.addItem(settingitem);
        //names.add(settingitem);
        //order.add(23);

        settingitem = new Item();
        settingitem.set_name(H.SETTINGKEY);
        settingitem.set_date(H.RECEIVABLE);
        settingitem.set_note("[OTHER INCOME]");//unit
        settingitem.set_rcdate(H.OINCOME);
        settingitem.set_source((short) 12);//"1100"
        settingitem.set_amount(H.ALLS);
        db.addItem(settingitem);
        //names.add(settingitem);
        //order.add(24);

        settingitem = new Item();
        settingitem.set_name(H.SETTINGKEY);
        settingitem.set_date(H.NONCOUNTABLE);
        settingitem.set_note("[DISTANCE]");//unit
        settingitem.set_rcdate(H.DISTANCE);
        settingitem.set_source((short) 12);//"1100"
        settingitem.set_amount(H.ALLS);
        db.addItem(settingitem);
        //names.add(settingitem);
        //order.add(25);

        settingitem = new Item();
        settingitem.set_name(H.SETTINGKEY);
        settingitem.set_date(H.NONCOUNTABLE);
        settingitem.set_note("[PREPARE TIME]");//duration of time
        settingitem.set_rcdate(H.PREPARETIME);
        settingitem.set_source((short) 12);//"1100"
        settingitem.set_amount(H.ALLS);
        db.addItem(settingitem);
        //names.add(settingitem);
        //order.add(26);

        settingitem = new Item();
        settingitem.set_name(H.SETTINGKEY);
        settingitem.set_date(H.RECEIVABLE);
        settingitem.set_note("[UBER FARE ADJUSTMENT]");//unit
        settingitem.set_rcdate(H.FAREADJ);
        settingitem.set_source((short) 8);//"1000"
        settingitem.set_amount(H.UBER);
        db.addItem(settingitem);
        //names.add(settingitem);
        //order.add(27);


        settingitem = new Item();
        settingitem.set_name(H.SETTINGKEY);
        settingitem.set_date(H.RECEIVABLE);
        settingitem.set_note("[UBER BOOK FEE (Payment)]");//unit
        settingitem.set_rcdate(H.UBKP);
        settingitem.set_source((short) 8);//"1000"
        settingitem.set_amount(H.UBER);
        db.addItem(settingitem);
        //names.add(settingitem);
        //order.add(28);

        settingitem = new Item();
        settingitem.set_name(H.SETTINGKEY);
        settingitem.set_date(H.PAYABLE);
        settingitem.set_note("[UBER BOOK FEE (Deduction)]");//unit
        settingitem.set_rcdate(H.UBKD);
        settingitem.set_source((short) 8);//"1000"
        settingitem.set_amount(H.UBER);
        db.addItem(settingitem);
        //names.add(settingitem);
        //order.add(29);

        settingitem = new Item();
        settingitem.set_name(H.SETTINGKEY);
        settingitem.set_date(H.NONCOUNTABLE);
        settingitem.set_note("[LYFT TRIP BONUS]");//unit
        settingitem.set_rcdate(H.BONUS);
        settingitem.set_source((short) 4);//"1110"
        settingitem.set_amount(H.TRIP);
        db.addItem(settingitem);
        //names.add(settingitem);
        //order.add(30);

        settingitem = new Item();
        settingitem.set_name(H.SETTINGKEY);
        settingitem.set_date(H.NONCOUNTABLE);
        settingitem.set_note("[OTHER NEUTRAL]");//unit
        settingitem.set_rcdate(H.ONEUTRAL);
        settingitem.set_source((short) 14);//"1110"
        settingitem.set_amount(H.ALLS);
        db.addItem(settingitem);
        //names.add(settingitem);
        //order.add(31);

        settingitem = new Item();
        settingitem.set_name(H.SETTINGKEY);
        settingitem.set_date(H.NONCOUNTABLE);
        settingitem.set_note("[LYFT TRIP PRIME TIME]");//unit
        settingitem.set_rcdate(H.PBONUS);
        settingitem.set_source((short) 4);//"1110"
        settingitem.set_amount(H.TRIP);
        db.addItem(settingitem);
        //names.add(settingitem);
        //order.add(32);

        settingitem = new Item();
        settingitem.set_name(H.SETTINGKEY);
        settingitem.set_date(H.RECEIVABLE);
        settingitem.set_note("[LYFT WK EARNINGS]");//unit
        settingitem.set_rcdate(H.WK_EARNING);
        settingitem.set_source((short) 4);//"1000"
        settingitem.set_amount(H.WK);
        db.addItem(settingitem);
        //names.add(settingitem);
        //order.add(33);

        settingitem = new Item();
        settingitem.set_name(H.SETTINGKEY);
        settingitem.set_date(H.RECEIVABLE);
        settingitem.set_note("[LYFT WK_BONUS]");//unit
        settingitem.set_rcdate(H.WK_BONUS);
        settingitem.set_source((short) 4);//"1000"
        settingitem.set_amount(H.WK);
        db.addItem(settingitem);
        //names.add(settingitem);
        //order.add(34);


        settingitem = new Item();
        settingitem.set_name(H.SETTINGKEY);
        settingitem.set_date(H.RECEIVABLE);
        settingitem.set_note("[LYFT POWER DRIVER BONUS]");//unit
        settingitem.set_rcdate(H.WK_PD_BONUS);
        settingitem.set_source((short) 4);//"1000"
        settingitem.set_amount(H.WK);
        db.addItem(settingitem);
        //names.add(settingitem);
        //order.add(35);

        settingitem = new Item();
        settingitem.set_name(H.SETTINGKEY);
        settingitem.set_date(H.NONCOUNTABLE);
        settingitem.set_note("[PAYOUT]");//unit
        settingitem.set_rcdate(H.UB_PAYOUT);
        settingitem.set_source((short) 14);//"1110"
        settingitem.set_amount(H.UBER);
        db.addItem(settingitem);
        //names.add(settingitem);
        //order.add(36);

        settingitem = new Item();
        settingitem.set_name(H.SETTINGKEY);
        settingitem.set_date(H.NONCOUNTABLE);
        settingitem.set_note("[ONLINE HOURS]");//unit
        settingitem.set_rcdate(H.WK_ONLINEHRS);
        settingitem.set_source((short) 14);//"1110"
        settingitem.set_amount(H.ALLS);
        db.addItem(settingitem);
        //names.add(settingitem);
        //order.add(37);


        settingitem = new Item();
        settingitem.set_name(H.SETTINGKEY);
        settingitem.set_date(H.NONCOUNTABLE);
        settingitem.set_note("[LYFT TRIP FARE]");//unit
        settingitem.set_rcdate(H.LYFTFARE);
        settingitem.set_source((short) 4);//"0100"
        settingitem.set_amount(H.TRIP);
        db.addItem(settingitem);
        //names.add(settingitem);
        //order.add(38);

        settingitem = new Item();
        settingitem.set_name(H.SETTINGKEY);
        settingitem.set_date(H.NONCOUNTABLE);
        settingitem.set_note("[LYFT TRIP FEE]");//unit
        settingitem.set_rcdate(H.LYFTFEE);
        settingitem.set_source((short) 4);//"0100"
        settingitem.set_amount(H.TRIP);
        db.addItem(settingitem);
        //names.add(settingitem);
        //order.add(39);

        settingitem = new Item();
        settingitem.set_name(H.SETTINGKEY);
        settingitem.set_date(H.NONCOUNTABLE);
        settingitem.set_note("[LYFT TRIP TIP]");//unit
        settingitem.set_rcdate(H.TRIPTIP);
        settingitem.set_source((short) 4);//"0100"
        settingitem.set_amount(H.TRIP);
        db.addItem(settingitem);
        //names.add(settingitem);
        //order.add(40);

        settingitem = new Item();
        settingitem.set_name(H.SETTINGKEY);
        settingitem.set_date(H.NONCOUNTABLE);
        settingitem.set_note("[LYFT TRIP TAX]");//unit
        settingitem.set_rcdate(H.TRIPTAX);
        settingitem.set_source((short) 4);//"0100"
        settingitem.set_amount(H.TRIP);
        db.addItem(settingitem);
        //names.add(settingitem);
        //order.add(41);

        settingitem = new Item();
        settingitem.set_name(H.SETTINGKEY);
        settingitem.set_date(H.NONCOUNTABLE);
        settingitem.set_note("[LYFT TRIP CANCEL]");//unit
        settingitem.set_rcdate(H.TRIPCANCEL);
        settingitem.set_source((short) 4);//"0100"
        settingitem.set_amount(H.TRIP);
        db.addItem(settingitem);
        //names.add(settingitem);
        //order.add(42);

        settingitem = new Item();
        settingitem.set_name(H.SETTINGKEY);
        settingitem.set_date(H.NONCOUNTABLE);
        settingitem.set_note("[LYFT TRIP COST/DEDUCTION]");//unit
        settingitem.set_rcdate(H.TRIPCOST);
        settingitem.set_source((short) 4);//"0100"
        settingitem.set_amount(H.TRIP);
        db.addItem(settingitem);
        //names.add(settingitem);
        //order.add(43);

        settingitem = new Item();
        settingitem.set_name(H.SETTINGKEY);
        settingitem.set_date(H.NONCOUNTABLE);
        settingitem.set_note("[LYFT TRIP INCOME]");//unit
        settingitem.set_rcdate(H.TRIPINCOME);
        settingitem.set_source((short) 4);//"0100"
        settingitem.set_amount(H.TRIP);
        db.addItem(settingitem);
        //names.add(settingitem);
        //order.add(44);

        settingitem = new Item();
        settingitem.set_name(H.SETTINGKEY);
        settingitem.set_date(H.NONCOUNTABLE);
        settingitem.set_note("[LYFT TRIP TOLL]");//unit
        settingitem.set_rcdate(H.TRIPTOLL);
        settingitem.set_source((short) 4);//"0100"
        settingitem.set_amount(H.TRIP);
        db.addItem(settingitem);
        //names.add(settingitem);
        //order.add(45);


        settingitem = new Item();
        settingitem.set_name(H.SETTINGKEY);
        settingitem.set_date(H.NONCOUNTABLE);
        settingitem.set_note("[LYFT TRIP OTHERS]");//unit
        settingitem.set_rcdate(H.TRIPOTHERS);
        settingitem.set_source((short) 4);//"0100"
        settingitem.set_amount(H.TRIP);
        db.addItem(settingitem);




        localtripitem();

    }

    private void olditems(DatabaseHandler db) {
        Item settingitem = new Item();
        settingitem.set_name(H.SETTINGKEY);
        settingitem.set_date(0);
        settingitem.set_amount(90);//starting
        settingitem.set_rcdate(System.currentTimeMillis());
        db.addItem(settingitem);

        settingitem = new Item();
        settingitem.set_name(H.SETTINGKEY);
        settingitem.set_date(1);
        settingitem.set_amount(0.13);//drate
        settingitem.set_rcdate(System.currentTimeMillis());
        db.addItem(settingitem);


        settingitem = new Item();
        settingitem.set_name(H.SETTINGKEY);
        settingitem.set_date(3);
        settingitem.set_note("");//cs
        settingitem.set_rcdate(System.currentTimeMillis());
        db.addItem(settingitem);

        settingitem = new Item();
        settingitem.set_name(H.SETTINGKEY);
        settingitem.set_date(4);
        settingitem.set_note("");//unit
        settingitem.set_rcdate(System.currentTimeMillis());
        db.addItem(settingitem);

        settingitem = new Item();
        settingitem.set_name(H.SETTINGKEY);
        settingitem.set_date(2);
        settingitem.set_amount(80);//inshar
        settingitem.set_rcdate(System.currentTimeMillis());
        db.addItem(settingitem);

        settingitem = new Item();
        settingitem.set_name(H.SETTINGKEY);
        settingitem.set_date(5);
        settingitem.set_amount(90);//share of data
        settingitem.set_rcdate(System.currentTimeMillis());
        db.addItem(settingitem);

        //------------------
        settingitem = new Item();
        settingitem.set_name(H.SN);
        settingitem.set_note(System.currentTimeMillis() + "-" + ((int) (Math.random() * 10000)) + (H.PAID ? "P" : ""));//SN number
        db.addItem(settingitem);

        settingitem = new Item();
        settingitem.set_name(H.SETTINGKEY);
        settingitem.set_date(H.NONCOUNTABLE);
        settingitem.set_note("Statistics");//unit
        settingitem.set_rcdate(1);//ID for SETTINGKEY (H.Payable,H.Receivable,H.NONCountable)
        db.addItem(settingitem);

        //names.clear();
        //order.clear();
        // //names.add(settingitem);

        settingitem = new Item();
        settingitem.set_name(H.SETTINGKEY);
        settingitem.set_date(H.NONCOUNTABLE);
        settingitem.set_note("Driver Settings");//unit
        settingitem.set_rcdate(2);
        db.addItem(settingitem);
        // //names.add(settingitem);


        settingitem = new Item();
        settingitem.set_name(H.SETTINGKEY);
        settingitem.set_date(H.NONCOUNTABLE);
        settingitem.set_note("Mileage Reading");//unit
        settingitem.set_rcdate(12);
        settingitem.set_source((short) 14);//"1110"
        db.addItem(settingitem);
        //names.add(settingitem);
        //order.add(0);

        settingitem = new Item();
        settingitem.set_name(H.SETTINGKEY);
        settingitem.set_date(H.NONCOUNTABLE);
        settingitem.set_note("Hours Online");//unit
        settingitem.set_rcdate(13);
        settingitem.set_source((short) 1);//"0001"
        db.addItem(settingitem);
        //names.add(settingitem);
        //order.add(1);

        settingitem = new Item();
        settingitem.set_name(H.SETTINGKEY);
        settingitem.set_date(H.NONCOUNTABLE);
        settingitem.set_note("Hours On Trip");//unit
        settingitem.set_rcdate(14);
        settingitem.set_source((short) 14);//"1110"
        db.addItem(settingitem);
        //names.add(settingitem);
        //order.add(2);

        settingitem = new Item();
        settingitem.set_name(H.SETTINGKEY);
        settingitem.set_date(H.RECEIVABLE);
        settingitem.set_note("Uber Income");//unit
        settingitem.set_rcdate(3);
        settingitem.set_source((short) 8);//"1000"
        db.addItem(settingitem);
        //names.add(settingitem);
        //order.add(4);

        settingitem = new Item();
        settingitem.set_name(H.SETTINGKEY);
        settingitem.set_date(H.RECEIVABLE);
        settingitem.set_note("Lyft Income");//unit
        settingitem.set_rcdate(16);
        settingitem.set_source((short) 4);//"0100"
        db.addItem(settingitem);
        //names.add(settingitem);
        //order.add(5);

        settingitem = new Item();
        settingitem.set_name(H.SETTINGKEY);
        settingitem.set_date(H.RECEIVABLE);
        settingitem.set_note("Cash Tips");//unit
        settingitem.set_rcdate(4);
        settingitem.set_source((short) 14);//"1110"
        db.addItem(settingitem);
        //names.add(settingitem);
        //order.add(6);

        settingitem = new Item();
        settingitem.set_name(H.SETTINGKEY);
        settingitem.set_date(H.PAYABLE);
        settingitem.set_note("Gas");//unit
        settingitem.set_rcdate(5);
        settingitem.set_source((short) 1);//"0001"
        db.addItem(settingitem);
        //names.add(settingitem);
        //order.add(7);

        settingitem = new Item();
        settingitem.set_name(H.SETTINGKEY);
        settingitem.set_date(H.PAYABLE);
        settingitem.set_note("Tax");//unit
        settingitem.set_rcdate(17);
        settingitem.set_source((short) 14);//"1110"
        db.addItem(settingitem);
        //names.add(settingitem);
        //order.add(8);

        settingitem = new Item();
        settingitem.set_name(H.SETTINGKEY);
        settingitem.set_date(H.PAYABLE);
        settingitem.set_note("Phone Data");//unit
        settingitem.set_rcdate(6);
        settingitem.set_source((short) 1);//"0001"
        db.addItem(settingitem);
        //names.add(settingitem);
        //order.add(9);

        settingitem = new Item();
        settingitem.set_name(H.SETTINGKEY);
        settingitem.set_date(H.PAYABLE);
        settingitem.set_note("Insurance");//unit
        settingitem.set_rcdate(7);
        settingitem.set_source((short) 1);//"0001"
        db.addItem(settingitem);
        //names.add(settingitem);
        //order.add(10);

        settingitem = new Item();
        settingitem.set_name(H.SETTINGKEY);
        settingitem.set_date(H.PAYABLE);
        settingitem.set_note("Meal");//unit
        settingitem.set_rcdate(8);
        settingitem.set_source((short) 1);//"0001"
        db.addItem(settingitem);
        //names.add(settingitem);
        //order.add(11);

        settingitem = new Item();
        settingitem.set_name(H.SETTINGKEY);
        settingitem.set_date(H.PAYABLE);
        settingitem.set_note("Maintenance");//unit
        settingitem.set_rcdate(9);
        settingitem.set_source((short) 1);//"0001" each digit represnt one of uber, lyft,other,overhead
        db.addItem(settingitem);
        //names.add(settingitem);
        //order.add(12);

        settingitem = new Item();
        settingitem.set_name(H.SETTINGKEY);
        settingitem.set_date(H.PAYABLE);
        settingitem.set_note("Depreciation Saving");//unit
        settingitem.set_rcdate(10);
        settingitem.set_source((short) 1);//"0001"
        db.addItem(settingitem);
        //names.add(settingitem);
        //order.add(13);


        settingitem = new Item();
        settingitem.set_name(H.SETTINGKEY);
        settingitem.set_date(H.RECEIVABLE);
        settingitem.set_note("Other Income");//unit
        settingitem.set_rcdate(15);
        settingitem.set_source((short) 14);//"1110"
        db.addItem(settingitem);
        //names.add(settingitem);
        //order.add(14);


        settingitem = new Item();
        settingitem.set_name(H.SETTINGKEY);
        settingitem.set_date(H.PAYABLE);
        settingitem.set_note("Other Expense");//unit
        settingitem.set_rcdate(11);
        settingitem.set_source((short) 15);//"1111"
        db.addItem(settingitem);
        //names.add(settingitem);
        //order.add(19);


    }
    private void localtripitem(){
        DatabaseHandler db=DatabaseHandler.getInstance(this);

        Item settingitem = new Item();
        settingitem.set_name(H.TRIPITEM);
        settingitem.set_note("HST COLLECTED");
        settingitem.set_rcdate(H.TRIPTAX);
        db.addItem(settingitem);

        settingitem = new Item();
        settingitem.set_name(H.TRIPITEM);
        settingitem.set_note("HST COLLECTED_WK");
        settingitem.set_rcdate(H.TAXS);
        db.addItem(settingitem);

        settingitem = new Item();
        settingitem.set_name(H.TRIPITEM);
        settingitem.set_note("HST ON BONUSES");
        settingitem.set_rcdate(H.TRIPTAX);
        db.addItem(settingitem);

        settingitem = new Item();
        settingitem.set_name(H.TRIPITEM);
        settingitem.set_note("HST ON BONUSES_WK");
        settingitem.set_rcdate(H.TAXS);
        db.addItem(settingitem);

        settingitem = new Item();
        settingitem.set_name(H.TRIPITEM);
        settingitem.set_note("HST ON LYFT AND 3RD PARTY FEES");
        settingitem.set_rcdate(H.TRIPCOST);
        db.addItem(settingitem);

        settingitem = new Item();
        settingitem.set_name(H.TRIPITEM);
        settingitem.set_note("HST ON LYFT AND 3RD PARTY FEES_WK");
        settingitem.set_rcdate(H.OCOST);
        db.addItem(settingitem);

        settingitem = new Item();
        settingitem.set_name(H.TRIPITEM);
        settingitem.set_note("DRIVER REFERRAL_WK");
        settingitem.set_rcdate(H.WK_BONUS);
        db.addItem(settingitem);

        settingitem = new Item();
        settingitem.set_name(H.TRIPITEM);
        settingitem.set_note("HST");
        settingitem.set_rcdate(H.TAXS);
        db.addItem(settingitem);

    }

    private void updateentries(final GridLayout gridLayout, ArrayList<Item> items) {
        for (int i = 0; i < items.size(); i++) {

            Item item =null;
            ArrayList<Integer> _order=null;

            if(entryid==H.ENTRIES) {
                if(items.size()==order.size()){
                    _order=order;
                }else{
                    _order=ulorder;
                }
                item=items.get(_order.get(i));
            }else{

                if(items.size()==ulorder.size()){
                    _order=ulorder;
                }else{
                    _order=order;
                }
                item=items.get(_order.get(i));
            }
            final int count = DatabaseHandler.getInstance(this).getItemCountByName("" + item.get_rcdate());
            double sum = 0;
            String cs = "", unit = "";
            if (item.get_rcdate() == H.MILEAGE) {
                sum = DatabaseHandler.getInstance(this).getMileageSum();
                unit = this.unit.equalsIgnoreCase("metric") ? "km " : "mile ";
            } else {
                sum = DatabaseHandler.getInstance(this).getSum((int) item.get_rcdate());
                if (item.get_date() == H.RECEIVABLE || item.get_date() == H.PAYABLE) {
                    cs = this.cs;
                }
            }
            final double amount = H.round(sum);
            final String cs0 = cs, unit0 = unit;
            View view = H.getValue(gridLayout, i, 1);
            if (!(view instanceof TextView)) {
               continue;
            }

            final TextView tv = (TextView) view;
            tv.post(new Runnable() {
                @Override
                public void run() {
                    tv.setText(tv.getText() + "(" + count + "," + cs0 + amount + unit0 + ")");
                }
            });

        }

    }
    String getunitid(int i){
        switch (i%6){
            case 0:
                return getResources().getString(R.string.banner_ad_unit_id);
            case 1:
                return getResources().getString(R.string.banner_ad_secondunit_id);
            case 2:
                return getResources().getString(R.string.banner_ad3);
            case 3:
                return getResources().getString(R.string.banner_ad4);
            case 4:
                return getResources().getString(R.string.banner_ad5);
            case 5:
                return getResources().getString(R.string.banner_ad6);

        }

        return getResources().getString(R.string.banner_ad_unit_id);
    }

    void addAds(final GridLayout gridLayout,final int adssize,final int repeat) {
         addAds(gridLayout,-1,adssize,repeat);
    }
    void addAds(final GridLayout gridLayout,final int itemsize,final int adssize,final int repeat) {
        int r=0;
        final int row=gridLayout.getRowCount();
        for (int i = 0; i < row; i++) {


            if (i % repeat == repeat-1) {
                //System.out.println(" ads adding  size="+itemsize);
                GridLayout.Spec rowSpan = GridLayout.spec(i, 3);
                GridLayout.Spec colspan = GridLayout.spec(0, adssize);
                final GridLayout.LayoutParams gridParam = new GridLayout.LayoutParams(
                        rowSpan, colspan);
                gridParam.setGravity(Gravity.CENTER_HORIZONTAL);
                final AdView ad = new AdView(this);
                ad.setAdSize(AdSize.BANNER);
                ad.setAdUnitId(getunitid(i));
                ad.post(new Runnable() {
                    @Override
                    public void run() {
                        AdRequest adRequest = new AdRequest.Builder().build();
                        ad.loadAd(adRequest);
                    }
                });

                final int index=i;
                View view=ad;
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    LinearLayout ll=new LinearLayout(this);
                    ll.setOrientation(LinearLayout.HORIZONTAL);
                    ll.setGravity(Gravity.RIGHT);
                    ll.addView(ad);

                    final AdView ad2 = new AdView(this);
                    ad2.setAdSize(AdSize.BANNER);
                    ad2.setAdUnitId(getunitid(i+3));
                    ad2.post(new Runnable() {
                        @Override
                        public void run() {
                            AdRequest adRequest = new AdRequest.Builder().build();
                            ad2.loadAd(adRequest);
                        }
                    });
                    ll.addView(ad2);
                    final HorizontalScrollView hs=new HorizontalScrollView(MainActivity.this);
                    hs.addView(ll);
                    view=hs;
                    gridParam.setMargins(80, 0, 10, 0);
                    gridParam.setGravity(Gravity.CENTER_HORIZONTAL);
                }

                final View v=view;
                gridLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        if(index<(gridLayout.getRowCount()-3)){
                           //System.out.println(index+"  ,   "+row+"   "+gridLayout.getRowCount());
                            gridLayout.addView(v,gridParam);
                        }

                    }
                });


                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                r++;
            }


            if(itemsize==-1) {

                if(r>7){
                    break;
                }

            }

        }

    }

    void fillgridlayout(final GridLayout gridLayout, final ArrayList<Item> items,int entryid) {
        gridLayout.removeAllViews();
        int rows = items.size() + 2 + 3 * items.size() + 1;

        gridLayout.setMinimumWidth((int) (H.getScreenWidth(this) * 0.9));
        gridLayout.setColumnCount(2);
        gridLayout.setRowCount(rows);
        int margin = 20;

        int r = 0;
        GridLayout.Spec rowSpan = GridLayout.spec(r);
        GridLayout.Spec colspan = GridLayout.spec(0);

        GridLayout.LayoutParams gridParam = null;
        ArrayList<Integer> _order=null;
        if(entryid==H.ENTRIES){
            _order=order;
        }else if(entryid==H.D_ENTRIES){
            _order=ulorder;
        }


        for (int i = 0; i < items.size(); i++) {

            final Item item = items.get(_order.get(i));

            rowSpan = GridLayout.spec(r);
            colspan = GridLayout.spec(0);
            gridParam = new GridLayout.LayoutParams(
                    rowSpan, colspan);
            gridParam.setMargins(40, 0, margin, 0);
            ImageButton btn = new ImageButton(this);
            //btn.setBackgroundColor(gridLayout.getba);
            btn.setBackground(gridLayout.getBackground());
            btn.setLayoutParams(new LinearLayout.LayoutParams(50, 50));
            btn.setImageDrawable(drawable);
            btn.setId((int) item.get_rcdate());
            btn.setOnClickListener(entrylistener);
            gridLayout.addView(btn, gridParam);// button


            rowSpan = GridLayout.spec(r);
            colspan = GridLayout.spec(1);
            gridParam = new GridLayout.LayoutParams(
                    rowSpan, colspan);
            gridParam.setMargins(0, 30 + Integer.parseInt(SP.getString("lineadjust", "0")), margin, 0);
            TextView txv = new TextView(this);
            txv.setText(item.get_note());
            txv.setLongClickable(true);
            txv.setOnLongClickListener(entryLCListener);
            txv.setTag(item);
            txv.setId(i);
            txv.setTextSize(18);
            if (item.get_date() == H.RECEIVABLE) {//problem
                txv.setTextColor(Color.parseColor("#347235"));
            } else if (item.get_date() == H.PAYABLE) {
                //txv.setTextColor(Color.parseColor("#990012"));
                txv.setTextColor(Color.rgb(225, 0, 0));
            } else if (((int) item.get_amount()) == H.TRIP) {
                txv.setTextColor(Color.rgb(0, 0, 150));
            } else if (item.get_rcdate() == H.TAXS || item.get_rcdate() == H.TOLL) {
                txv.setTextColor(Color.rgb(255, 0, 255));

            } else {
                txv.setTextColor(Color.BLACK);
            }

            gridLayout.addView(txv, gridParam);// button

            r++;

            if(!H.PAID && adsfreetime<=0) {
                if ((r % 8) == 7) {
                    r = r + 3;
                }
            }


        }

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    updateentries(gridLayout, items);
                }
            }).start();
        }

        if(!H.PAID && adsfreetime<=0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    addAds(gridLayout, 2, 8);
                }
            }).start();
        }


    }

    private View.OnLongClickListener entryLCListener = new View.OnLongClickListener() {

        @Override
        public boolean onLongClick(final View v) {

            ID = v.getId();
            //final int IDs = (int) DummyContent.getItemID("" + ID);
            String title = "Options";
            LayoutInflater factory = LayoutInflater.from(MainActivity.this);
            final View textEntryView = factory.inflate(R.layout.modifyitems, null);
            final TextView name = (TextView) textEntryView.findViewById(R.id.itemname);
            final Item item = (Item) v.getTag();
            final int IDs = (int) item.get_rcdate();
            name.setText(title = item.get_note());


            final Button moveup = (Button) textEntryView.findViewById(R.id.moveup);
            moveup.setText("Move Up(Position: " + ID + ")");
            final Button movedown = (Button) textEntryView.findViewById(R.id.movedown);
            movedown.setText("Move Down(Position: " + ID + ")");
            final Button delete = (Button) textEntryView.findViewById(R.id.delete);
            final Button modify = (Button) textEntryView.findViewById(R.id.modify);


            TextView titlev = new TextView(MainActivity.this);
            titlev.setText(title);
            titlev.setGravity(Gravity.CENTER_HORIZONTAL);
            titlev.setTextSize(20);
            titlev.setBackgroundColor(Color.parseColor("#4F4FCB"));
            titlev.setTextColor(Color.WHITE);

            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            alert.setCustomTitle(titlev).setView(textEntryView).setPositiveButton("Close",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {

                            dialog.dismiss();
                            fragment = EntryFragment.newInstance(entryid);
                            fragment.setmain(MainActivity.this);
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.container, fragment)
                                    .commit();


                        }
                    });
            alert.setCancelable(true).show();


            if (IDs == H.SUMMARY || IDs == H.MILEAGE || IDs == H.DSETTING || H.dtcheck(IDs)) {

                modify.setEnabled(false);
                delete.setEnabled(false);
            }


            name.addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable s) {

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {

                }

            });

            modify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if (name.isShown()) {
                        String note = name.getText().toString();
                        if (note.length() == 0) {
                            return;
                        }
                        item.set_note(note);
                        DatabaseHandler db = DatabaseHandler.getInstance(MainActivity.this);
                        db.updateItem(item);
                        view.setEnabled(false);
                        ((TextView) v).setText(item.get_note());
                        name.setVisibility(View.GONE);

                        // This solution is not a good solution, but have no better choice
                        Intent i = getBaseContext().getPackageManager()
                                .getLaunchIntentForPackage(getBaseContext().getPackageName());
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    } else {
                        //name.setEnabled(true);
                        name.setVisibility(View.VISIBLE);

                        movedown.setEnabled(false);
                        moveup.setEnabled(false);
                        delete.setEnabled(false);
                        modify.setText("Update Change");
                    }

                    ucode = new Item();
                    ucode.set_note("MD");
                    ucode.set_name(H.USERCODE);
                    DatabaseHandler.getInstance(MainActivity.this).addItem(ucode);
                }
            });


            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    if (!H.PAID) {

                        (new AlertDialog.Builder(MainActivity.this)).setMessage("This feature ia only avaible in Ads Free Edition").setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int whichButton) {

                                        dialog.dismiss();

                                    }
                                }).show();
                        return;
                    }

                    String title = "Warning";

                    TextView titlev = new TextView(MainActivity.this);
                    titlev.setText(title);
                    titlev.setGravity(Gravity.CENTER_HORIZONTAL);
                    titlev.setTextSize(20);
                    titlev.setBackgroundColor(Color.RED);
                    titlev.setTextColor(Color.WHITE);

                    final AlertDialog.Builder warning = new AlertDialog.Builder(MainActivity.this);
                    warning.setCustomTitle(titlev).setMessage("Are your sure you want to delete this Entry:" + item.get_note() + " ?\n\nAll data related to this item will be erased!").setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {

                                    dialog.dismiss();
                                    DatabaseHandler db = DatabaseHandler.getInstance(MainActivity.this);
                                    db.deleteById("" + item.get_id());
                                    //  view.setEnabled(false);
                                    //  movedown.setEnabled(false);
                                    //   moveup.setEnabled(false);
                                    //    additem.setEnabled(false);
                                    //   modify.setEnabled(false);

                                    int index = names.indexOf(item);
                                    //ArrayList<Integer> order = DummyContent.getOrder();
                                    Item ti = names.remove(index);
                                    int ovalue = order.remove(index);
                                    // Whten an item is deleted, order no behind changed
                                    for (int i = 0; i < order.size(); i++) {
                                        if (order.get(i) > ovalue) {
                                            order.set(i, order.get(i) - 1);
                                        }
                                    }

                                    H.updateSPOrder(order, SP);

                                    Intent i = getBaseContext().getPackageManager()
                                            .getLaunchIntentForPackage(getBaseContext().getPackageName());
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(i);
                                    ucode = new Item();
                                    ucode.set_note("ET");
                                    ucode.set_name(H.USERCODE);
                                    DatabaseHandler.getInstance(MainActivity.this).addItem(ucode);

                                }
                            }).setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    dialog.dismiss();

                                }
                            });
                    warning.show();
                    ucode = new Item();
                    ucode.set_note("EL");
                    ucode.set_name(H.USERCODE);
                    DatabaseHandler.getInstance(MainActivity.this).addItem(ucode);

                }
            });

            moveup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    modify.setEnabled(false);
                    delete.setEnabled(false);


                    ArrayList<Integer> _order =null;
                    if(entryid==H.ENTRIES){
                        _order=order;
                    }else{
                        _order=ulorder;
                    }


                    if ((ID) <= 0) {
                        moveup.setEnabled(false);
                        return;
                    }

                    TextView tv = (TextView) H.getValue(gridLayout, (ID - 1), 1);
                    int color = tv.getCurrentTextColor();

                    TextView tv2 = (TextView) H.getValue(gridLayout, ID, 1);
                    String temb = tv2.getText().toString();
                    int color2 = tv2.getCurrentTextColor();


                    tv2.setText(tv.getText());
                    tv2.setTextColor(color);

                    tv.setText(temb);
                    tv.setTextColor(color2);


                    int tem = _order.get(ID);
                    _order.set(ID, _order.get(ID - 1));
                    _order.set(ID - 1, tem);

                    if(entryid==H.ENTRIES){
                        H.updateSPOrder(_order, SP);
                    }else{
                        H.updateSPULOrder(_order, SP);
                    }



                    ID--;

                    if (!movedown.isEnabled()) {
                        movedown.setEnabled(true);
                    }
                    movedown.setText("Move Down(Position: " + ID + ")");
                    moveup.setText("Move Up(Position: " + ID + ")");


                    ucode = new Item();
                    ucode.set_note("EU"+entryid);
                    ucode.set_name(H.USERCODE);
                    DatabaseHandler.getInstance(MainActivity.this).addItem(ucode);

                }
            });

            movedown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    modify.setEnabled(false);
                    delete.setEnabled(false);

                    ArrayList<Integer> _order =null;
                    if(entryid==H.ENTRIES){
                        _order=order;
                        if (ID >= names.size() - 1) {
                            movedown.setEnabled(false);
                            return;
                        }
                    }else{
                        _order=ulorder;
                        if (ID >= ulnames.size() - 1) {
                            movedown.setEnabled(false);
                            return;
                        }
                    }



                    TextView tv = (TextView) H.getValue(gridLayout, (ID + 1), 1);
                    int color = tv.getCurrentTextColor();

                    TextView tv2 = (TextView) H.getValue(gridLayout, ID, 1);
                    String temb = tv2.getText().toString();
                    int color2 = tv2.getCurrentTextColor();


                    tv2.setText(tv.getText());
                    tv2.setTextColor(color);

                    tv.setText(temb);
                    tv.setTextColor(color2);


                    int tem = _order.get(ID + 1);
                    _order.set(ID + 1, _order.get(ID));
                    _order.set(ID, tem);
                    //  DummyContent.setOrder(order);

                    if(entryid==H.ENTRIES){
                        H.updateSPOrder(_order, SP);
                    }else{
                        H.updateSPULOrder(_order, SP);
                    }


                    ID++;
                    if (!moveup.isEnabled()) {
                        moveup.setEnabled(true);
                    }

                    movedown.setText("Move Down(Position: " + ID + ")");
                    moveup.setText("Move Up(Position: " + ID + ")");
                    ucode = new Item();
                    ucode.set_note("ED"+entryid);
                    ucode.set_name(H.USERCODE);
                    DatabaseHandler.getInstance(MainActivity.this).addItem(ucode);

                }
            });


            return true;
        }
    };


    private View.OnClickListener entrylistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(!H.PAID && adsfreetime<=0){
                long lapse = (System.currentTimeMillis() - H.FAILEDTIME);
                if ((lapse) > H.ADSLOCK && lapse < (System.currentTimeMillis() - 60 * 1000)) {
                    H.pupmsg(MainActivity.this, "This function has been locked due to long period of absence of ads. Connect to internet to unlock it!");
                    return;
                }
            }

            entryid = v.getId();
            fragment = EntryFragment.newInstance(entryid);
            fragment.setmain(MainActivity.this);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment).addToBackStack(null)
                    .commit();


            previousentry = entryid;


            DatabaseHandler db = DatabaseHandler.getInstance(MainActivity.this);
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

                ucode = new Item();
                ucode.set_note("L" + entryid);
                ucode.set_name(H.USERCODE);
                db.addItem(ucode);

            } else {
                ucode = new Item();
                ucode.set_note("P" + entryid);
                ucode.set_name(H.USERCODE);
                db.addItem(ucode);
            }

        }
    };

    private void noverifymsg(){
        LayoutInflater factory = LayoutInflater.from(this);
        final View verifyview = factory.inflate(R.layout.everify, null);
        TextView msg = (TextView) verifyview.findViewById(R.id.msg);
        msg.setText("Your email address has not been verified and this feature has been disabled. If you have not registered your location and email address, click the button below to start.If you just finished registration,you may need to restart the app");
        final Button register =(Button)verifyview.findViewById(R.id.register);

        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setView(verifyview).setPositiveButton("Close",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {

                        dialog.dismiss();

                    }
                });
        final AlertDialog dia=alert.create();
        dia.show();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dia.dismiss();
                registrationop();
            }
        });


    }

    private void initiateSP() {
        SP.edit().putInt("E13", 3).commit(); //online
        SP.edit().putInt("E14", 0).commit(); //trip
        SP.edit().putInt("E12", 0).commit(); //mileage
        SP.edit().putInt("E16", 1).commit(); //lyft payment
        SP.edit().putInt("E4", 0).commit(); //tips
        SP.edit().putInt("E3", 0).commit(); //Uber Payment
        SP.edit().putInt("E11", 3).commit(); //expense
        SP.edit().putInt("E10", 3).commit(); //depreciation saving
        SP.edit().putInt("E9", 3).commit(); //Maintenance
        SP.edit().putInt("E8", 3).commit(); //meal
        SP.edit().putInt("E7", 3).commit(); //Insurance
        SP.edit().putInt("E6", 3).commit(); //data
        SP.edit().putInt("E5", 3).commit();//gas
    }


    //-------end of new adding

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        System.out.println("PresseddDraw=======");
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        if(true)return true;

        int id = item.getItemId();

        if (id == R.id.nav_entries) {
            fragment = EntryFragment.newInstance(H.ENTRIES);
            fragment.setmain(this);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        } else if (id == R.id.nav_download) {
            fragment = EntryFragment.newInstance(H.D_ENTRIES);
            fragment.setmain(this);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }else if (id == R.id.nav_uberlogin) {
            if(SP.getInt("verify",0)==0){

                noverifymsg();
            }else {

                fragment = EntryFragment.newInstance(H.UBERLOGIN);
                fragment.setmain(this);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment, "A").addToBackStack("A")
                        .commit();
            }
            ucode = new Item();
            ucode.set_note("UBERLOGIN");
            ucode.set_name(H.USERCODE);
            DatabaseHandler.getInstance(this).addItem(ucode);
        } else if (id == R.id.nav_lyftlogin) {

            if(SP.getInt("verify",0)==0){
                noverifymsg();

            }else {
                fragment = EntryFragment.newInstance(H.LYFTLOGIN);
                fragment.setmain(this);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment, "A").addToBackStack("A")
                        .commit();
            }

            ucode = new Item();
            ucode.set_note("LYFTLOGIN");
            ucode.set_name(H.USERCODE);
            DatabaseHandler.getInstance(this).addItem(ucode);

        } else if (id == R.id.nav_help) {
            Uri uri = Uri.parse("http://bookkeeper.vcounters.com/help.htm");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (intent == null) {
                Toast.makeText(this, "Connection Problem", Toast.LENGTH_LONG).show();
            } else {
                startActivity(intent);
            }
            ucode = new Item();
            ucode.set_note("-5");
            ucode.set_name(H.USERCODE);
            DatabaseHandler.getInstance(this).addItem(ucode);
        } else if (id == R.id.nav_about) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("About this app").setMessage("Version:" + H.version + "\nSN:" + DatabaseHandler.getInstance(this).getSN()).setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {

                            dialog.dismiss();

                        }
                    });
            alert.show();

          ///  if (mRewardedVideoAd.isLoaded()) {
          //      mRewardedVideoAd.show();
          //  }

            ucode = new Item();
            ucode.set_note("-7");
            ucode.set_name(H.USERCODE);
            DatabaseHandler.getInstance(this).addItem(ucode);
        } else if (id == R.id.nav_rating) {
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=net.compuways.bookkeeper");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (intent == null) {
                Toast.makeText(this, "Connection Problem", Toast.LENGTH_LONG).show();

            } else {
                startActivity(intent);
            }
            ucode = new Item();
            ucode.set_note("-6");
            ucode.set_name(H.USERCODE);
            DatabaseHandler.getInstance(this).addItem(ucode);

        } else if (id == R.id.nav_email) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SENDTO);

            intent.setData(Uri.parse("mailto:ulbookkeeper@gmail.com")); // only email apps should handle this

            if (H.PAID) {
                intent.putExtra(Intent.EXTRA_TEXT, "\n\n\n-----------------------------------------------\nPlease keep the following hardware info when you send support request" +
                        "\nOS:" + Build.VERSION.RELEASE + "\nModel:" + Build.MODEL + "\nMaker:" + Build.MANUFACTURER + "\nSN:" + DatabaseHandler.getInstance(this).getSN());
                intent.putExtra(Intent.EXTRA_SUBJECT, "BK Support from " + DatabaseHandler.getInstance(this).getSN());
            } else {
                intent.putExtra(Intent.EXTRA_TEXT, "\n\n\n-----------------------------------------------\nPlease leave your comment above.Thank you for your interest in this app!");
                intent.putExtra(Intent.EXTRA_SUBJECT, "BK Suggestion");
            }
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }else{
                H.pupmsg(this, "You don't have Email App Installed");
            }

            ucode = new Item();
            ucode.set_note("-10");
            ucode.set_name(H.USERCODE);
            DatabaseHandler.getInstance(this).addItem(ucode);
        } else if (id == R.id.nav_uber) {

            PackageManager p = getPackageManager();
            List<ApplicationInfo> packs = p.getInstalledApplications(0);
            boolean install = false;
            for (ApplicationInfo ap : packs) {
                String pkname = ap.packageName.toLowerCase();
                if (pkname.contains("com.ubercab.driver")) {
                    Intent intent = p.getLaunchIntentForPackage(pkname);
                    if(intent!=null) {
                        install = true;
                        startActivity(intent);
                    }

                }
            }

            if (!install) {
                H.pupmsg(this, "You don't have Uber Driver App Installed");
            }

            ucode = new Item();
            ucode.set_note("UBERAPP");
            ucode.set_name(H.USERCODE);
            DatabaseHandler.getInstance(this).addItem(ucode);

        } else if (id == R.id.nav_lyft) {

            PackageManager p = getPackageManager();
            List<ApplicationInfo> packs = p.getInstalledApplications(0);
            boolean install = false;
            for (ApplicationInfo ap : packs) {
                String pkname = ap.packageName.toLowerCase();
                if (pkname.contains("com.lyft.android.driver")) {
                    Intent intent = p.getLaunchIntentForPackage(pkname);
                    if(intent!=null) {
                        install = true;
                        startActivity(intent);
                    }


                }
            }

            if (!install) {
                H.pupmsg(this, "You don't have Lyft Driver App Installed");
            }

            ucode = new Item();
            ucode.set_note("LYFTAPP");
            ucode.set_name(H.USERCODE);
            DatabaseHandler.getInstance(this).addItem(ucode);

        } else if (id == R.id.nav_uberp) {

            PackageManager p = getPackageManager();
            List<ApplicationInfo> packs = p.getInstalledApplications(0);
            boolean install = false;
            for (ApplicationInfo ap : packs) {
                String pkname = ap.packageName.toLowerCase();
                if (pkname.contains("com.ubercab")) {
                    Intent intent = p.getLaunchIntentForPackage(pkname);
                    if(intent!=null) {
                        install = true;
                        startActivity(intent);
                    }


                }
            }

            if (!install) {
                H.pupmsg(this, "You don't have Uber Passenger App Installed");
            }
            ucode = new Item();
            ucode.set_note("UBERAPPP");
            ucode.set_name(H.USERCODE);
            DatabaseHandler.getInstance(this).addItem(ucode);

        } else if (id == R.id.nav_lyftp) {

            PackageManager p = getPackageManager();
            List<ApplicationInfo> packs = p.getInstalledApplications(0);
            boolean install = false;
            for (ApplicationInfo ap : packs) {
                String pkname = ap.packageName.toLowerCase();
                if (pkname.contains("me.lyft.android")) {
                    Intent intent = p.getLaunchIntentForPackage(pkname);
                    if(intent!=null) {
                        install = true;
                        startActivity(intent);
                    }


                }
            }

            if (!install) {
                H.pupmsg(this, "You don't have Lyft Passenger App Installed");
            }
            ucode = new Item();
            ucode.set_note("LYFTAPPP");
            ucode.set_name(H.USERCODE);
            DatabaseHandler.getInstance(this).addItem(ucode);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.



        if(true)return true;

        getMenuInflater().inflate(R.menu.files, menu);
        this.menu = menu;

        if (menu != null) {

            MenuItem ug = menu.findItem(R.id.action_upgrade);
            if (H.PAID) {
                ug.setVisible(false);
            }

            if(!SP.getBoolean("ONLINEACCESS",true)){

                    MenuItem sp = menu.findItem(R.id.action_forward);
                    sp.setEnabled(false);
                    sp.setVisible(false);




                    sp = menu.findItem(R.id.action_wv_back);
                    sp.setEnabled(false);
                    sp.setVisible(false);


            }
        }
        return true;
    }

    @Override
    public void onResume(){
        super.onResume();



        if(true)return;
        long day=(System.currentTimeMillis() - SP.getLong("DAYCHK", 0));
        //System.out.println(day+"  =====================?>"+H.DAYCHK);
        if(day>H.DAYCHK){

           // System.out.println(day+"  y>"+H.DAYCHK);

            new Thread() {
                public void run() {
                    checkid();
                }
            }.start();

            //-----In-app billing-------
            //if it is unpaid or, paid but time is older thant H.PURCK, contact the billing service. otherwise, not
            if(SP.getBoolean("PAID", false)){ // every day peroid calculate the hit count for paid user
                puridcheck();
            }

            SP.edit().putLong("DAYCHK", System.currentTimeMillis()).commit();

        }

        if(!H.PAID) {
            mRewardedVideoAd.resume(this);
            long lapse=System.currentTimeMillis()-SP.getLong("adsfreestarttime",0);
            //System.out.println(lapse+"   ========adsreset="+H.ADSRESET);
            if(lapse>H.ADSRESET){
                adsfreetime =0;
                SP.edit().putLong("adsfreetime",0).commit();
                if(fragment!=null && fragment.fabmsg!=null){
                    fragment.fabmsg.setText("0");
                }
               // System.out.println("   ========adsreseted");
            }
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 333: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    exportOP(333,"");

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            case 334: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    exportOP(334,cvsdata);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            case 666: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    importOP();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            case 2: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED & ContextCompat.checkSelfPermission(this,
                            Manifest.permission.INTERNET)
                            == PackageManager.PERMISSION_GRANTED) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

                    }

                    System.out.println("Location ===========");

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }

                System.out.println("Location ====xxxxxxxxx");
                return;
            }
        }
    }

    private void exportOP(final int requestcode,final String data) {
        if (!H.PAID && mInterstitialAd.isLoaded() && adsfreetime<=0) {
            mInterstitialAd.show();
        }

        final FileChooserDialog dialog = new FileChooserDialog(this);
        dialog.setCanCreateFiles(true);
        if(requestcode==333) {
            dialog.setFilter(".*ubk");
        }else if(requestcode==334){// for csv file
            dialog.setFilter(".*csv");
        }

        dialog.loadFolder(Environment.getExternalStorageDirectory() + "/");
        //dialog.loadFolder(Environment.DIRECTORY_DOWNLOADS);


        dialog.addListener(new FileChooserDialog.OnFileSelectedListener() {
            public void onFileSelected(Dialog source, File file) {
                source.hide();
            }

            public void onFileSelected(final Dialog source, final File folder, final String name) {
                source.hide();
                LayoutInflater factory = LayoutInflater.from(MainActivity.this);
                final View barview = factory.inflate(R.layout.progress, null);
                final ProgressBar pb = (ProgressBar) barview.findViewById(R.id.pb);
                final TextView percentage = (TextView) barview.findViewById(R.id.percentage);
                final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setView(barview);
                final AlertDialog alertDialog = alert.create();
                alertDialog.show();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(requestcode==333) {
                            fileexport(source, folder, name, pb, percentage, alertDialog);
                        }else if(requestcode==334){
                            exportcvs(source,folder,name,data,alertDialog);
                        }
                    }
                }).start();

            }
        });
        dialog.show();
    }

    private void importOP() {
        if (!H.PAID && mInterstitialAd.isLoaded() && adsfreetime<=0) {
            mInterstitialAd.show();
        }
        final FileChooserDialog dialog = new FileChooserDialog(MainActivity.this);
        dialog.setFilter(".*ubk");
        dialog.setShowConfirmation(true, false);
        dialog.getFileChoosercore().addSMsg(" to be restored?");
        dialog.loadFolder(Environment.getExternalStorageDirectory() + "/");
        dialog.addListener(new FileChooserDialog.OnFileSelectedListener() {
            public void onFileSelected(Dialog source, final File file) {
                source.dismiss();
                LayoutInflater factory = LayoutInflater.from(MainActivity.this);
                final View barview = factory.inflate(R.layout.progress, null);
                final ProgressBar pb = (ProgressBar) barview.findViewById(R.id.pb);
                final TextView percentage = (TextView) barview.findViewById(R.id.percentage);
                final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setView(barview);
                final AlertDialog alertDialog = alert.create();
                alertDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        fileimportOP(file, pb, percentage, alertDialog);
                    }
                }).start();


            }

            public void onFileSelected(Dialog source, File folder, String name) {
                source.hide();

            }
        });

        dialog.show();
    }

    private void toastmsg(final Context context, final String msg) {
        new Thread() {
            public void run() {
                MainActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast toast2 = Toast.makeText(context, msg, Toast.LENGTH_LONG);
                        toast2.show();
                    }
                });
            }
        }.start();
    }

    private void fileexport(final Dialog source, File folder, String name, final ProgressBar pb, final TextView percentage, final AlertDialog alertDialog) {

        DatabaseHandler db = DatabaseHandler.getInstance(MainActivity.this);
        final ArrayList<Item> bms = db.getAllItemsAndKey();
        if (bms == null || bms.size() == 0) {
            toastmsg(source.getContext(), "You don't have any data to back up!");
            return;
        }


        final File file = new File(folder.getAbsolutePath());


        if (!file.exists()) {
            if (!file.mkdirs()) {
                toastmsg(source.getContext(), "Folder creation failed ");
                return;
            }
        }

        final String fname = name;


        try {
            File filename = new File(file, fname + ".ubk");
            FileOutputStream outputStream = new FileOutputStream(filename);
            String data = "<version>3.0.0</version>";

            if (pb != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pb.setProgress(1);
                    }
                });
            }


            final double size = bms.size();
            for (int i = 0; i < bms.size(); i++) {
                Item bm = bms.get(i);
                data = data + bm.get_id() + comacode[0] + bm.get_date() + comacode[1] + bm.get_name() + comacode[2] + bm.get_amount() + comacode[3] + bm.get_rcdate() + comacode[4] + bm.get_source() + comacode[5] + bm.get_note() + comacode[6] + bm.get_xxx() + endcode;
                outputStream.write(data.getBytes());
                data = "";

                final int j = i;
                if (pb != null && (i % 60) == 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pb.setProgress(1 + (int) ((j / size) * 99));
                            percentage.setText((1 + (int) ((j / size) * 99)) + "%");
                        }
                    });
                }


            }
            outputStream.close();
            source.dismiss();

            if (pb != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pb.setProgress(100);
                        percentage.setText("100%");
                    }
                });
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                alertDialog.dismiss();
            }
        });


    }

    private void exportcvs(final Dialog source, File folder, String name, final String data, final AlertDialog alertDialog) {


        final File file = new File(folder.getAbsolutePath());


        if (!file.exists()) {
            if (!file.mkdirs()) {
                toastmsg(source.getContext(), "Folder creation failed ");
                return;
            }
        }

        final String fname = name;


        try {
            File filename = new File(file, fname + ".csv");
            FileOutputStream outputStream = new FileOutputStream(filename);

            outputStream.write(data.getBytes());
            outputStream.close();
            source.dismiss();



        } catch (Exception e) {
            e.printStackTrace();
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                alertDialog.dismiss();
            }
        });


    }

    private void fileimportOP(File file, final ProgressBar pb, final TextView percentage, final AlertDialog alertDialog) {
        final File file1 = file;

        String sb = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(file1));
            try {

                String line = br.readLine();
                long filesize = file1.length();
                long flength = 0;
                int n0 = line.indexOf("<version>");
                int n1 = line.indexOf("</version>");
                String version = getDigit(line.substring(n0, n1));

                line = line.substring(n1 + 10);

                while (line != null) {
                    sb = sb + line;
                    flength = flength + line.length();
                    line = br.readLine();
                }

                n0 = sb.lastIndexOf(endcode);
                String inuse = sb.substring(0, n0);
                String[] bmstr = inuse.split(endcode);


                ArrayList<Item> kmitem = new ArrayList<>();
                DatabaseHandler db = DatabaseHandler.getInstance(this);
                db.deleteAllItem();

                final double size = bmstr.length;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pb.setProgress(1);
                    }
                });

                if (version.equalsIgnoreCase("100")) {
                    SP.edit().putInt("importversion", 1).commit();
                    for (int i = 0; i < bmstr.length; i++) {
                        String bm = bmstr[i];
                        processRestore(bm, kmitem);

                        final int j = i;
                        if (pb != null && (j % 20) == 0) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pb.setProgress(1 + (int) ((j / size) * 99));
                                    percentage.setText((1 + (int) ((j / size) * 99)) + "%");
                                }
                            });
                        }

                    }
                    processkmitem(kmitem);
                    ucode = new Item();
                    ucode.set_note("F1");
                    ucode.set_name(H.USERCODE);
                    db.addItem(ucode);
                } else if (version.equalsIgnoreCase("200")) {
                    //toastmsg(this, version);
                    SP.edit().putInt("importversion", 2).commit();
                    for (int i = 0; i < bmstr.length; i++) {
                        String bm = bmstr[i];
                        processRestorev2(bm);
                        final int j = i;
                        if (pb != null && (j % 30) == 0) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pb.setProgress(1 + (int) ((j / size) * 99));
                                    percentage.setText((1 + (int) ((j / size) * 99)) + "%");
                                }
                            });
                        }


                    }
                    ucode = new Item();
                    ucode.set_note("F2");
                    ucode.set_name(H.USERCODE);
                    db.addItem(ucode);
                } else if (version.equalsIgnoreCase("300")) {
                    SP.edit().putInt("importversion", 3).commit();
                    //toastmsg(this, version);
                    for (int i = 0; i < bmstr.length; i++) {
                        String bm = bmstr[i];
                        processRestorev3(bm);
                        final int j = i;
                        if (pb != null && (j % 50) == 0) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pb.setProgress(1 + (int) ((j / size) * 99));
                                    percentage.setText((1 + (int) ((j / size) * 99)) + "%");
                                }
                            });
                        }

                    }
                    ucode = new Item();
                    ucode.set_note("F3");
                    ucode.set_name(H.USERCODE);
                    db.addItem(ucode);
                } else {
                    H.pupmsg(this, "Can't open this file");
                    return;
                }

                SP.edit().putBoolean("Orderupdate", true).commit();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (pb != null) {
                            pb.setProgress(100);
                            percentage.setText("100% Completed");
                        }
                    }
                });


                try {
                    for (int i = 0; i < 10; i++) {
                        Thread.sleep(1000);
                        final int j = i;
                        percentage.post(new Runnable() {
                            @Override
                            public void run() {
                                percentage.setText("100% Completed. In " + (9 - j) + " seconds, app will restart.");
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (alertDialog != null) {
                            alertDialog.dismiss();
                        }

                    }
                });

                Intent i = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage(getBaseContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                //----------------


            } finally {
                br.close();

            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (alertDialog != null) {
                    alertDialog.dismiss();
                }
            }
        });

    }

    private void processkmitem(ArrayList<Item> kmitem) {
        Collections.sort(kmitem);
        int km = (int) kmitem.get(kmitem.size() - 1).get_amount();
        DatabaseHandler db = DatabaseHandler.getInstance(this);
        int starting = db.getSettingV(-1000);
        if (starting < km) {
            km = starting;
        }

        for (int i = kmitem.size() - 1; i >= 0; i--) {
            kmitem.get(i).set_rcdate(km);
            String tem = kmitem.get(i).get_note();
            if (tem != null && tem.length() > 0) {
                tem = tem.substring(1);
                kmitem.get(i).set_note(tem);
            }
            db.addItem(kmitem.get(i));
            km = (int) kmitem.get(i).get_amount();
        }

        Item settingitem = new Item();
        settingitem.set_name(H.SETTINGKEY);
        settingitem.set_date(0);
        settingitem.set_amount(90);//gas
        settingitem.set_rcdate(System.currentTimeMillis());
        db.addItem(settingitem);


        settingitem = new Item();
        settingitem.set_name(H.SETTINGKEY);
        settingitem.set_date(5);
        settingitem.set_amount(93);//share of data
        settingitem.set_rcdate(System.currentTimeMillis());
        db.addItem(settingitem);

        settingitem = new Item();
        settingitem.set_name(H.SN);
        settingitem.set_note(System.currentTimeMillis() + "-" + ((int) (Math.random() * 10000)));//SN number
        db.addItem(settingitem);


    }

    //*/
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener =
            new IabHelper.OnConsumeFinishedListener() {
                public void onConsumeFinished(Purchase purchase, IabResult result) {
                    if (result.isSuccess()) {
                        H.pupmsg(MainActivity.this, "Succeful comsumed");
                    } else {
                        H.pupmsg(MainActivity.this, "Consumed error");
                    }
                }
            };
    //*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(true)return true;

        int id = item.getItemId();

        DatabaseHandler db = DatabaseHandler.getInstance(this);

        switch (id) {

            case R.id.action_back:
                //System.out.println("Click Back");
                if(entryid==H.REWARDV){
                    fragment = EntryFragment.newInstance(H.ENTRIES);
                    fragment.setmain(this);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, fragment,"A").addToBackStack("A")
                            .commit();
                }else {

                    onBackPressed();
                }
                return true;


                /*/
                if(true){
                    return true;
                }

                if (previousentry == entryid) {
                    previousentry = currententry;
                }

                fragment = EntryFragment.newInstance(previousentry);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();
                previousentry = currententry;

                //*/
            case R.id.action_statics:

                fragment = EntryFragment.newInstance(H.SUMMARY);
                fragment.setmain(this);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment,"A").addToBackStack("A")
                        .commit();

                //*/
                if (!H.PAID && mInterstitialAd.isLoaded() && adsfreetime<=0) {
                    mInterstitialAd.show();
                }
                //*/

                return true;
            case R.id.action_wv_back:
                if(!H.PAID) {
                    final int rm = SP.getInt("tripbonus",0)+H.TRIPLIMIT - DatabaseHandler.getInstance(this).getItemCountByName("" + H.TRIPDETAIL);
                    if(rm<=0){
                        H.pupmsg(this,"You have reached "+(SP.getInt("tripbonus",0)+H.TRIPLIMIT)+" trips limit for free edition.");
                        return true;
                    }
                }

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                baseday = sdf.format(H.getWMonday());

                fragment.JSONLOCK = true;
                fragment.wv.loadUrl("https://www.lyft.com/api/driver_routes/" + baseday);
                return true;
            case R.id.action_forward:
                if(!H.PAID) {
                    final int rm = SP.getInt("tripbonus",0)+H.TRIPLIMIT - DatabaseHandler.getInstance(this).getItemCountByName("" + H.TRIPDETAIL);
                    if(rm<=0){
                        H.pupmsg(this,"You have reached "+(SP.getInt("tripbonus",0)+H.TRIPLIMIT)+" trips limit for free edition.");
                        return true;
                    }
                }
                fragment.JSONLOCK = true;
                fragment.wv.loadUrl("https://partners.uber.com/p3/money/statements/view/current");

                return true;
            case R.id.action_records:

                fragment = EntryFragment.newInstance(H.ALLR);
                fragment.setmain(this);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment).addToBackStack("A")
                        .commit();
                // showRecords(textEntryView, db.getAllItems());
                ucode = new Item();
                ucode.set_note("-1");
                ucode.set_name(H.USERCODE);
                db.addItem(ucode);
                return true;
            case R.id.action_gas:

                fragment = EntryFragment.newInstance(H.GAS);
                fragment.setmain(this);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment).addToBackStack("A")
                        .commit();
                // showRecords(textEntryView, db.getAllItems());
                ucode = new Item();
                ucode.set_note("GAS");
                ucode.set_name(H.USERCODE);
                db.addItem(ucode);
                return true;

            case R.id.action_mileage:

                fragment = EntryFragment.newInstance(H.MILEAGE);
                fragment.setmain(this);
                //getSupportFragmentManager().;
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment,"A").addToBackStack("A")
                        .commit();
                // showRecords(textEntryView, db.getAllItems());
                ucode = new Item();
                ucode.set_note("MILE");
                ucode.set_name(H.USERCODE);
                db.addItem(ucode);
                return true;



            case R.id.action_export:

                 startexport(333,"");

                return true;

            case R.id.action_import:

                if (ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE)) {

                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.

                    } else {

                        // No explanation needed, we can request the permission.

                        ActivityCompat.requestPermissions(this,
                                new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                                666);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                } else {
                    importOP();
                }
                ucode = new Item();
                ucode.set_note("-3");
                ucode.set_name(H.USERCODE);
                db.addItem(ucode);
                return true;
            case R.id.action_dsetting:
                fragment = EntryFragment.newInstance(H.DSETTING);
                fragment.setmain(this);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment).addToBackStack("A")
                        .commit();

                //*/

                if (!H.PAID && mInterstitialAd.isLoaded() && adsfreetime<=0) {
                    mInterstitialAd.show();
                }

                //*/


                return true;

            case R.id.action_setting:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                ucode = new Item();
                ucode.set_note("-4");
                ucode.set_name(H.USERCODE);
                db.addItem(ucode);
                return true;

            case R.id.action_about:

                /*/
                try {

                    if (purchase != null) {
                        mHelper.consumeAsync(purchase,
                                mConsumeFinishedListener);
                        System.out.println("Purchsased");
                    } else {
                        System.out.println("Not Purchsased");
                    }
                } catch (IabHelper.IabAsyncInProgressException e) {
                    e.printStackTrace();
                }
                //*/
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Delete All").setMessage("Delete All Records").setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {

                                dialog.dismiss();
                                DatabaseHandler.getInstance(MainActivity.this).deleteAllItem();

                            }
                        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();


                 //*/
                return true;
            case R.id.action_add:
                if (!H.PAID && mInterstitialAd.isLoaded() && adsfreetime<=0) {
                    mInterstitialAd.show();
                }

                addentry();
                ucode = new Item();
                ucode.set_note("-8");
                ucode.set_name(H.USERCODE);
                db.addItem(ucode);
                return true;

            case R.id.action_upgrade:
                LayoutInflater factory = LayoutInflater.from(MainActivity.this);
                View views = factory.inflate(R.layout.upgrade, null);
                EditText msg = (EditText) views.findViewById(R.id.msg);
                msg.setText(getResources().getString(R.string.upgrademsg));
                Button btnug = (Button) views.findViewById(R.id.btnug);
                btnug.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            mHelper.launchPurchaseFlow(MainActivity.this, H.SKU, 10003,
                                    mPurchaseFinishedListener, "Thank you!");
                        } catch (IabHelper.IabAsyncInProgressException e) {
                            e.printStackTrace();
                        }
                    }
                });

               // AlertDialog.Builder
                        alert = new AlertDialog.Builder(this);
                alert.setCustomTitle(views).setPositiveButton("Close",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {

                                dialog.dismiss();

                            }
                        });
                alert.show();


                ucode = new Item();
                ucode.set_note("-11");
                ucode.set_name(H.USERCODE);
                db.addItem(ucode);
                return true;
            case R.id.action_datarepair:
                fragment = EntryFragment.newInstance(H.DATAE);
                fragment.setmain(this);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment).addToBackStack("A")
                        .commit();

                ucode = new Item();
                ucode.set_note("-9");
                ucode.set_name(H.USERCODE);
                db.addItem(ucode);
                return true;

        }

        return super.onOptionsItemSelected(item);


    }

    public void startexport(int requestcode,String data){
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {

                cvsdata=data;
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        requestcode);

            }
        } else {
            exportOP(requestcode,data);
        }

        ucode = new Item();
        ucode.set_note("-2");
        ucode.set_name(H.USERCODE);
        DatabaseHandler.getInstance(this).addItem(ucode);
    }

    View.OnClickListener cclistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            short id = (short) ((Integer.parseInt(((RadioButton) v).getHint().toString())) % 4);
            int row = (Integer.parseInt(((RadioButton) v).getHint().toString())) / 4;

            if (datarepareitems != null) {
                datarepareitems.get(row).set_source(id);

                DatabaseHandler.getInstance(MainActivity.this).updateItem(datarepareitems.get(row));
            }
            ucode = new Item();
            ucode.set_note("SS");
            ucode.set_name(H.USERCODE);
            DatabaseHandler.getInstance(MainActivity.this).addItem(ucode);

        }
    };

    private void check(EditText itemname, Button additem, CheckBox uber, CheckBox lyft, CheckBox other, CheckBox overhead, int[] choice) {
        if (itemname.getText().toString().length() == 0 || !(uber.isChecked() || lyft.isChecked() || other.isChecked() || overhead.isChecked()) ||
                choice[0] == -1) {
            additem.setEnabled(false);
            additem.setAlpha(.5f);
            additem.setClickable(false);
        } else {
            additem.setEnabled(true);
            additem.setAlpha(1.0f);
            additem.setClickable(true);
        }
    }

    private void addentry() {

        LayoutInflater factory = LayoutInflater.from(MainActivity.this);
        final View textEntryView = factory.inflate(R.layout.entryadd, null);
        final Button additem = (Button) textEntryView.findViewById(R.id.additem);
        final RadioGroup rg = (RadioGroup) textEntryView.findViewById(R.id.rg);
        final CheckBox uber = (CheckBox) textEntryView.findViewById(R.id.uberc);
        final CheckBox lyft = (CheckBox) textEntryView.findViewById(R.id.lyftc);
        final CheckBox other = (CheckBox) textEntryView.findViewById(R.id.otherc);
        final CheckBox overhead = (CheckBox) textEntryView.findViewById(R.id.overheadc);
        final EditText itemname = (EditText) textEntryView.findViewById(R.id.itemname);
        final int[] choice = {-1};
        uber.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    uber.setTextColor(Color.RED);
                    lyft.setTextColor(Color.BLACK);
                    other.setTextColor(Color.BLACK);
                    overhead.setTextColor(Color.BLACK);
                    choice[0] = 0;
                } else {
                    uber.setTextColor(Color.BLACK);
                    if (choice[0] == 0) {
                        choice[0] = -1;
                    }
                }
                check(itemname, additem, uber, lyft, other, overhead, choice);
            }
        });
        lyft.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    uber.setTextColor(Color.BLACK);
                    lyft.setTextColor(Color.RED);
                    other.setTextColor(Color.BLACK);
                    overhead.setTextColor(Color.BLACK);
                    choice[0] = 1;
                } else {
                    lyft.setTextColor(Color.BLACK);
                    if (choice[0] == 1) {
                        choice[0] = -1;
                    }
                }
                check(itemname, additem, uber, lyft, other, overhead, choice);
            }
        });
        other.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    uber.setTextColor(Color.BLACK);
                    lyft.setTextColor(Color.BLACK);
                    other.setTextColor(Color.RED);
                    overhead.setTextColor(Color.BLACK);
                    choice[0] = 2;
                } else {
                    other.setTextColor(Color.BLACK);
                    if (choice[0] == 2) {
                        choice[0] = -1;
                    }
                }
                check(itemname, additem, uber, lyft, other, overhead, choice);
            }
        });
        overhead.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    uber.setTextColor(Color.BLACK);
                    lyft.setTextColor(Color.BLACK);
                    other.setTextColor(Color.BLACK);
                    overhead.setTextColor(Color.RED);
                    choice[0] = 3;
                } else {
                    overhead.setTextColor(Color.BLACK);
                    if (choice[0] == 3) {
                        choice[0] = -1;
                    }
                }
                check(itemname, additem, uber, lyft, other, overhead, choice);
            }
        });

        itemname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                check(itemname, additem, uber, lyft, other, overhead, choice);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        TextView titlev = new TextView(MainActivity.this);
        titlev.setText("Add Entry");
        titlev.setGravity(Gravity.CENTER_HORIZONTAL);
        titlev.setTextSize(20);
        titlev.setBackgroundColor(Color.parseColor("#4F4FCB"));
        titlev.setTextColor(Color.WHITE);

        final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setCustomTitle(titlev).setView(textEntryView).setPositiveButton("Close",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {

                        dialog.dismiss();


                    }
                });
        alert.setCancelable(true).show();

        additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!H.PAID) {

                    (new AlertDialog.Builder(MainActivity.this)).setMessage("This feature ia only avaible in Ads Free Edition").setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {

                                    dialog.dismiss();

                                }
                            }).show();
                    return;
                }


                DatabaseHandler db = DatabaseHandler.getInstance(MainActivity.this);
                Item settingitem = new Item();
                settingitem.set_name(H.SETTINGKEY);

                int index = rg.indexOfChild(rg.findViewById(rg.getCheckedRadioButtonId()));
                settingitem.set_date(index == 0 ? H.RECEIVABLE : (index == 1 ? H.PAYABLE : H.NONCOUNTABLE));
                int nextid = db.getNextItemID();
               // System.out.println("NextID========="+nextid);

                settingitem.set_rcdate(nextid);
                settingitem.set_note(itemname.getText().toString());
                int src = (uber.isChecked() ? 64 : 0);
                src += (lyft.isChecked() ? 32 : 0);
                src += (other.isChecked() ? 16 : 0);
                src += (overhead.isChecked() ? 8 : 0);
                src += choice[0];
                settingitem.set_source((short) src);

                db.addItem(settingitem);

                order.add(names.size());
                H.updateSPOrder(order, SP);

                // This solution is not a good solution, but have no better choice
                Intent i = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage(getBaseContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);


            }
        });

    }


    private void processRestorev3(String bmstr) {
        String id = "", date = "", amount = "", name = "", note = "", rcdate = "", source = "", xxx = "";
        int n = 0;
        int n0 = bmstr.indexOf(comacode[0]);
        if (n0 >= 0) {
            id = bmstr.substring(0, n0);
            n = n0;
        }

        n0 = bmstr.indexOf(comacode[1]);

        if (n0 >= 0) {
            date = bmstr.substring(n + 2, n0);
            n = n0;
        }

        n0 = bmstr.indexOf(comacode[2]);
        if (n0 >= 0) {
            name = bmstr.substring(n + 2, n0);
            n = n0;
        }

        n0 = bmstr.indexOf(comacode[3]);
        if (n0 >= 0) {
            amount = bmstr.substring(n + 2, n0);
            n = n0;
        }

        n0 = bmstr.indexOf(comacode[4]);
        if (n0 >= 0) {
            rcdate = bmstr.substring(n + 2, n0);
            n = n0;
        }

        n0 = bmstr.indexOf(comacode[5]);
        if (n0 >= 0) {
            source = bmstr.substring(n + 2, n0);
            n = n0;
        }

        n0 = bmstr.indexOf(comacode[6]);
        if (n0 >= 0) {
            note = bmstr.substring(n + 2, n0);
            n = n0;
        }

        if (n < bmstr.length() - 2) {
            xxx = bmstr.substring(n + 2);
        }


        DatabaseHandler db = DatabaseHandler.getInstance(MainActivity.this);
        Item bm = new Item();

        bm.set_id(id != "" ? Integer.parseInt(id) : -1);
        bm.set_name(name);
        bm.set_date(date != "" ? Long.parseLong(date) : -1);
        bm.set_amount(amount != "" ? Double.parseDouble(amount) : -1);
        bm.set_rcdate(rcdate != "" ? Long.parseLong(rcdate) : -1);
        bm.set_source(source != "" ? (short) Integer.parseInt(source) : (short) -1);
        bm.set_note(note);
        bm.set_xxx(xxx);
        db.addItem(bm);

    }

    private void processRestorev2(String bmstr) {
        String id = "", date = "", amount = "", name = "", note = "", rcdate = "", source = "";
        int n = 0;
        int n0 = bmstr.indexOf(comacode[0]);
        if (n0 >= 0) {
            id = bmstr.substring(0, n0);
            n = n0;
        }

        n0 = bmstr.indexOf(comacode[1]);

        if (n0 >= 0) {
            date = bmstr.substring(n + 2, n0);
            n = n0;
        }

        n0 = bmstr.indexOf(comacode[2]);
        if (n0 >= 0) {
            name = bmstr.substring(n + 2, n0);
            n = n0;
        }

        n0 = bmstr.indexOf(comacode[3]);
        if (n0 >= 0) {
            amount = bmstr.substring(n + 2, n0);
            n = n0;
        }

        n0 = bmstr.indexOf(comacode[4]);
        if (n0 >= 0) {
            rcdate = bmstr.substring(n + 2, n0);
            n = n0;
        }

        n0 = bmstr.indexOf(comacode[5]);
        if (n0 >= 0) {
            source = bmstr.substring(n + 2, n0);
            n = n0;
        }

        if (n < bmstr.length() - 2) {
            note = bmstr.substring(n + 2);
        }


        DatabaseHandler db = DatabaseHandler.getInstance(MainActivity.this);
        Item bm = new Item();

        bm.set_id(id != "" ? Integer.parseInt(id) : -1);
        bm.set_name(name);
        bm.set_date(date != "" ? Long.parseLong(date) : -1);
        bm.set_amount(amount != "" ? Double.parseDouble(amount) : -1);
        bm.set_rcdate(rcdate != "" ? Long.parseLong(rcdate) : -1);
        bm.set_source(source != "" ? (short) Integer.parseInt(source) : (short) -1);
        bm.set_note(note);
        db.addItem(bm);


    }

    private void processRestore(String bmstr, ArrayList<Item> kmitem) {
        ///data=data+bm.get_groupid()+comacode[0]+bm.get_link()+comacode[1]+bm.get_linkDate()+comacode[2]+bm.get_type()+comacode[3]+bm.get_categoryID()+comacode[4]+bm.get_linkMDDate()+endcode;
        String id = "", date = "", amount = "", name = "", note = "", rcdate = "";

        int n = 0;
        int n0 = bmstr.indexOf(comacode[0]);
        if (n0 >= 0) {
            id = bmstr.substring(0, n0);
            n = n0;
        }

        n0 = bmstr.indexOf(comacode[1]);

        if (n0 >= 0) {
            date = bmstr.substring(n + 2, n0);
            n = n0;
        }


        n0 = bmstr.indexOf(comacode[2]);
        if (n0 >= 0) {
            name = bmstr.substring(n + 2, n0);
            n = n0;
        }

        n0 = bmstr.indexOf(comacode[3]);
        if (n0 >= 0) {
            amount = bmstr.substring(n + 2, n0);
            n = n0;
        }

        n0 = bmstr.indexOf(comacode[4]);
        if (n0 >= 0) {
            rcdate = bmstr.substring(n + 2, n0);
            n = n0;
        }
        if (n < bmstr.length() - 2) {
            note = bmstr.substring(n + 2);
        }

        DatabaseHandler db = DatabaseHandler.getInstance(MainActivity.this);
        Item bm = new Item();

        bm.set_id(id != "" ? Integer.parseInt(id) : -1);
        bm.set_name(name);
        bm.set_date(date != "" ? Long.parseLong(date) : -1);
        bm.set_amount(amount != "" ? Double.parseDouble(amount) : -1);
        bm.set_rcdate(rcdate != "" ? Long.parseLong(rcdate) : -1);
        bm.set_note(note);
        bm.set_source((short) 0);


        if (Long.parseLong(name) == H.MILEAGE) {
            kmitem.add(bm);
        } else {


            if (bm.get_name().equalsIgnoreCase("" + H.SETTINGKEY) && bm.get_date() == 0) {
                bm.set_date(-1000);
            } else if (bm.get_name().equalsIgnoreCase("" + H.SETTINGKEY) && bm.get_date() == H.UBERPAY) {
                bm.set_source((short) (8));
            } else if (bm.get_name().equalsIgnoreCase("" + H.SETTINGKEY) && bm.get_date() == H.TIP ||
                    bm.get_name().equalsIgnoreCase("" + H.SETTINGKEY) && bm.get_date() == H.OTHERINCOME ||
                    bm.get_name().equalsIgnoreCase("" + H.SETTINGKEY) && bm.get_date() == H.MILEAGE ||
                    bm.get_name().equalsIgnoreCase("" + H.SETTINGKEY) && bm.get_date() == H.TRIPHRS ||
                    bm.get_name().equalsIgnoreCase("" + H.SETTINGKEY) && bm.get_date() == H.TAX) {
                bm.set_source((short) (14));
            } else if (bm.get_name().equalsIgnoreCase("" + H.SETTINGKEY) && bm.get_date() == H.LYFTPAY) {
                bm.set_source((short) (4));
            } else if (bm.get_name().equalsIgnoreCase("" + H.SETTINGKEY) && bm.get_date() == H.GAS ||
                    bm.get_name().equalsIgnoreCase("" + H.SETTINGKEY) && bm.get_date() == H.MAINTENANCE ||
                    bm.get_name().equalsIgnoreCase("" + H.SETTINGKEY) && bm.get_date() == H.INSURANCE ||
                    bm.get_name().equalsIgnoreCase("" + H.SETTINGKEY) && bm.get_date() == H.MEAL ||
                    bm.get_name().equalsIgnoreCase("" + H.SETTINGKEY) && bm.get_date() == H.DSAVING ||
                    bm.get_name().equalsIgnoreCase("" + H.SETTINGKEY) && bm.get_date() == H.ONLINEHRS) {
                bm.set_source((short) (1));
            } else if (bm.get_name().equalsIgnoreCase("" + H.SETTINGKEY)) {
                bm.set_source((short) (15));
            }
            db.addItem(bm);
        }


    }

    private String getDigit(String str) {
        char[] ch = str.trim().toCharArray();
        String tem = "";
        for (int i = 0; i < ch.length; i++) {
            if (Character.isDigit(ch[i]))
                tem += ch[i];

        }
        return tem;
    }


    View.OnClickListener orderListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = Integer.valueOf(((TextView) v).getTag().toString().trim());
            ArrayList<Item> items = null;
            DatabaseHandler db = DatabaseHandler.getInstance(MainActivity.this);
            switch (id) {
                case 0://date
                    items = db.getAllItemsOrderBy(H.KEY_DATE, dir, (page - 1), limit);
                    break;
                case 1://source
                    items = db.getAllItemsOrderBy(H.KEY_SOURCE, dir, (page - 1), limit);
                    break;
                case 2://entry id
                    items = db.getAllItemsOrderBy(H.KEY_ITEM, dir, (page - 1), limit);
                    break;
            }

            H.fillgridlayout(MainActivity.this, gridLayout, items, -1, H.getScreenWidth(MainActivity.this),
                    6, false, true, true, null, orderListener, null, null);


            if (dir.equalsIgnoreCase("DESC")) {
                dir = "";
            } else {
                dir = "DESC";
            }
            ucode = new Item();
            ucode.set_note("AO" + id);
            ucode.set_name(H.USERCODE);
            db.addItem(ucode);
        }
    };

    private void dataop() {

        if (SP.getBoolean("Hardware", true)) {
            new Thread() {
                public void run() {
                    sendMsg("d=" + H.D + "&sn=" + DatabaseHandler.getInstance(MainActivity.this).getSN() + "&w=" + H.getScreenWidth(MainActivity.this) + "&h=" + H.getScreenHeight(MainActivity.this) +
                            "&m=" + Build.MODEL + "&b=" + Build.MANUFACTURER + "&o=" + Build.VERSION.RELEASE,H.DATA);

                }
            }.start();
            SP.edit().putBoolean("Hardware", false).commit();
        }

        //System.out.println("Agree=>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>="+SP.getBoolean("Kng", true)+"  "+SP.getBoolean("Kngx", true));

        if (SP.getBoolean("disclaimerv33", true)) {
            String msg ="<html><body><p style=\"text-align:center\">Disclaimer</p>"+

            "<p style='text-align:justify'>1.This app provides a bookkeeping tool for Uber or Lyft drivers to track their driving activities. Users should back up their "+
            "data whenever performing upgradation, reinstallation or any other system change operation. We, as app developers, are not responsible for "+
            "any data loss if a user fails to follow these data security procedures.</p><p style='text-align:justify'>"+

            "2.This version of app provides a means to download user trip information from Uber or Lyft online accounts. The user should authorize this app to "+
            "have access to user's personal information in Uber or Lyft online account. This app will strictly protect the user's privacy."+
            "All sensitive personal information will strictly be processed within user's mobile device and go no beyond. If a user does not authorize this app to "+
            "have access to his/her Uber or Lyft online account,he or she will not be able to use any downloading feature, but still be able to use "+
            "other functions.We strongly recommend that users should read the online instruction before proceeding to download any trip.</p><p style='text-align:justify'>"+

            "3.Downloading from Uber or Lyft online account is based on current website settings of Uber and Lyft websites. Uber or Lyft can change their website "+
            "anytime without notice. We are not responsible for any interruption of downloading functions due to any problem or restriction from Uber or Lyft. The amount of money that "+
            "a paid user pays for this app should be considered for the convenience of ads free environment, not to pay for the downloading subscription service.</p><p style='text-align:justify'>4.";


            msg=msg+getResources().getString(R.string.collect)+".</p></body></html>";
            WebView wb=new WebView(this);
                    wb.loadDataWithBaseURL("", msg, "text/html", "UTF-8", "");
            (new AlertDialog.Builder(this)).setView(wb).setPositiveButton("I Agree",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            SP.edit().putBoolean("disclaimerv33", false).commit();
                            if(!SP.getBoolean("Kng", true)) {
                                SP.edit().remove("Kng");
                            }
                            dialog.dismiss();

                            registrationop();

                        }
                    }).setNegativeButton("I Do Not Agree", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    SP.edit().putBoolean("ONLINEACCESS", false).commit();
                    SP.edit().putBoolean("disclaimerv33", false).commit();
                    if(!SP.getBoolean("Kng", true)) {
                        SP.edit().remove("Kng");
                    }

                    registrationop();
                    hidemenu();

                    ucode = new Item();
                    ucode.set_note("NAgree");
                    ucode.set_name(H.USERCODE);
                    DatabaseHandler.getInstance(MainActivity.this).addItem(ucode);

                }
            }).show();


        }


        if (SP.getBoolean("register", true) && !SP.getBoolean("disclaimerv33", true)) {
           registrationop();
        }

        if(!SP.getBoolean("ONLINEACCESS",true)){
            hidemenu();
        }

        final String datas = DatabaseHandler.getInstance(this).getUserCode();
        if (datas == null || datas.length() < H.MSGLEN) {
            return;
        }

        if (!H.PAID || SP.getString("usageshare", "ON").equalsIgnoreCase("ON")) {
            new Thread() {
                public void run() {
                    sendMsg("d=" + H.D + "&co=" + datas + "&sn=" + DatabaseHandler.getInstance(MainActivity.this).getSN(),H.DATA);
                }
            }.start();
        }
    }
    private void registrationop(){
        LayoutInflater factory = LayoutInflater.from(this);
        final View rgview = factory.inflate(R.layout.register, null);
        final EditText state2 = (EditText)rgview.findViewById(R.id.state2);
        final EditText email = (EditText)rgview.findViewById(R.id.email);
        final Button rgsave =(Button) rgview.findViewById(R.id.regsave);
        final Spinner country=(Spinner)rgview.findViewById(R.id.country);
        // Create an ArrayAdapter using the string array and a default spinner layout
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.country, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        country.setAdapter(adapter);

        final Spinner state=(Spinner)rgview.findViewById(R.id.state);
        final ArrayAdapter<CharSequence> stadapter = ArrayAdapter.createFromResource(this,
                R.array.state, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        stadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        state.setAdapter(stadapter);
        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 0 || position==200) {
                    state.setAdapter(stadapter);
                    if(state2.isShown()){
                        state2.setVisibility(View.GONE);
                    }
                    if(!state.isShown()){
                        state.setVisibility(View.VISIBLE);
                    }

                }else if(position==1 || position==37){
                    ArrayAdapter<CharSequence> pradapter = ArrayAdapter.createFromResource(MainActivity.this,
                            R.array.province, android.R.layout.simple_spinner_item);
                    state.setAdapter(pradapter);
                    if(state2.isShown()){
                        state2.setVisibility(View.GONE);
                    }

                    if(!state.isShown()){
                        state.setVisibility(View.VISIBLE);
                    }
                }else{
                    state.setVisibility(View.GONE);
                    state2.setVisibility(View.VISIBLE);
                   // System.out.println("Positon="+position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });



        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final AlertDialog adia=alert.setView(rgview).create();
        adia.show();

        rgsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String em=email.getText().toString().trim();
               // if(em==null || em.length()<=6 || em.indexOf("@")<=0 ||em.indexOf(".")<=0 || em.indexOf("@")>=(em.length()-5) ){
                if(!H.isEmailValid(em)){
                        runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            H.pupmsg(MainActivity.this,"Invalid Email");
                        }
                    });
                    return;
                }

                SP.edit().putString("country",country.getSelectedItem().toString()).commit();
                String msg="d=" + H.D + "&c=" + country.getSelectedItem().toString() + "&sn=" + DatabaseHandler.getInstance(MainActivity.this).getSN()+
                        "&e="+email.getText()+"&lat="+(SP.getLong("lat",0)/100000000.0)+"&lon="+(SP.getLong("lon",0)/100000000.0);
                if(state.isShown()) {
                    SP.edit().putString("state",state.getSelectedItem().toString().toUpperCase()).commit();
                    msg=msg+"&s="+state.getSelectedItem().toString();
                    //System.out.println(country.getSelectedItem().toString() + "  "+state.getSelectedItem().toString()+"   "+email.getText()+"  Lat="+(SP.getLong("lat",0)/100000000.0)+"    lon="+(SP.getLong("lon",0)/100000000.0));
                }else{
                    SP.edit().putString("state",state2.getText().toString().toUpperCase()).commit();
                    msg=msg+"&s="+state2.getText();
                    // System.out.println(country.getSelectedItem().toString() + "  "+state2.getText()+"   "+email.getText()+"  Lat="+(SP.getLong("lat",0)/100000000.0)+"    lon="+(SP.getLong("lon",0)/100000000.0));
                }
                //System.out.println(msg);
                final String msgf=msg;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        sendMsg(msgf,H.AUTOSAVE); //autosave for register
                    }
                }).start();
                adia.dismiss();
            }
        });

        SP.edit().putBoolean("register", false).commit();
        //SP.edit().putBoolean("register",false).commit();
    }


    private void hidemenu(){
        Menu menu_nav = navigationView.getMenu();
        MenuItem sp = menu_nav.findItem(R.id.nav_uberlogin);
        sp.setEnabled(false);
       // sp.setVisible(false);

        sp = menu_nav.findItem(R.id.nav_lyftlogin);
        sp.setEnabled(false);
       // sp.setVisible(false);


        if(menu!=null) {
            sp = menu.findItem(R.id.action_forward);
            if(sp!=null) {
                sp.setEnabled(false);
                sp.setVisible(false);
            }
        }


        if(menu!=null) {
            sp = menu.findItem(R.id.action_wv_back);
            if(sp!=null) {
                sp.setEnabled(false);
                sp.setVisible(false);
            }
        }

    }
    void sendMsg(String msg,int type) {

       // String url = "http://www.compuways.net/bookkeeper/data.php";
        String url ="";
        if(type==H.DATA) {
            url="http://bookkeeper.vcounters.com/data.php";
        }else if(type==H.BONUS){
            url="http://bookkeeper.vcounters.com/bonus.php";
        }else if(type==H.WK_BONUS){
            url="http://bookkeeper.vcounters.com/getbonusv33.php";
        }else if(type==H.AUTOSAVE){
             url="http://bookkeeper.vcounters.com/register.php"; //Register
        }else if(type==H.TAXS){
            url="http://bookkeeper.vcounters.com/addtaxname.php";
        }
        URL obj = null;
        HttpURLConnection con = null;
        try {
            obj = new URL(url);
            con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            final String USER_AGENT = "Mozilla/5.0";
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            // For POST only - START
            con.setDoOutput(true);
            OutputStream os = con.getOutputStream();
            os.write(msg.getBytes());
            os.flush();
            os.close();
            // For POST only - END
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;
                final StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                if (response.toString().equalsIgnoreCase("BKOK")) {
                    if(type==H.DATA) {
                        DatabaseHandler.getInstance(this).deleteUserCode();
                    }else if(type==H.AUTOSAVE){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                H.pupmsg(MainActivity.this,"Registration has been submitted, Please go to your email to confirm and finish the registration. You may need to go to junk mail to find it");
                            }
                        });

                    }
                } else if (response.toString().equalsIgnoreCase("BKOKT")) {
                    ;
                } else {
                    if(type==H.WK_BONUS){// get account information
                        String rs=response.toString();
                        if(rs.contains("PDOException")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    H.pupmsg(MainActivity.this, "Server Error");
                                }
                            });
                        }else if(rs.length()>0 && rs.length()<10){

                            String[] rss=rs.trim().split(":");

                           // System.out.println(rss.length+"tripbonus Respons============="+rs.trim());//+","+rss[0]+","+rss[1]+","+rss[2]+","+rss[3]);

                            SP.edit().putInt("tripbonus",Integer.parseInt(rss[0])).commit();
                            SP.edit().putInt("verify",Integer.parseInt(rss[1])).commit();

                        }

                    }
                }

                System.out.println("Respons========"+response.toString());
            }else{
                System.out.println("Not working:"+type);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void checkid() {

        //String url = "http://www.compuways.net/bookkeeper/checkid.php";//doesn't update 0n resume
        String url = "http://bookkeeper.vcounters.com/checkidnew.php";//good one
        //String url = "http://bookkeeper.vcounters.com/checkidtest.php";//in use
        String msg = DatabaseHandler.getInstance(this).getSN();
        msg = "sn=" + msg;
        URL obj = null;
        HttpURLConnection con = null;
        try {
            obj = new URL(url);
            con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            final String USER_AGENT = "Mozilla/5.0";
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            // For POST only - START
            con.setDoOutput(true);
            OutputStream os = con.getOutputStream();
            os.write(msg.getBytes());
            os.flush();
            os.close();
            // For POST only - END
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;
                final StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                if (response.toString().equalsIgnoreCase("BKOK")) {
                        // registered, count++;

                } else {
                    new Thread() {
                        public void run() {
                            sendMsg("d=" + H.D + "&sn=" + DatabaseHandler.getInstance(MainActivity.this).getSN() + "&w=" + H.getScreenWidth(MainActivity.this) + "&h=" + H.getScreenHeight(MainActivity.this) +
                                    "&m=" + Build.MODEL + "&b=" + Build.MANUFACTURER + "&o=" + Build.VERSION.RELEASE,H.DATA);

                        }
                    }.start();

                }

                //System.out.println("checkid.php Respons============"+response.toString());

            } else {
                System.out.println("POST request not working");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
            = new IabHelper.OnIabPurchaseFinishedListener() {

        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {

            if (result.isFailure()) {
                (new AlertDialog.Builder(MainActivity.this)).setMessage("Sorry!Upgrading failed. Please try again later.Reason:\n" + result).setPositiveButton("Close",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {

                                dialog.dismiss();

                            }
                        }).show();
                return;
            } else if (purchase.getSku().equals(H.SKU)) {
                successmsg();
            } else {
                (new AlertDialog.Builder(MainActivity.this)).setMessage("Sorry!Upgrading failed. Please try again later.Unknown Reason:\n" + result + "\npurchase=" + purchase.getSku()).setPositiveButton("Close",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {

                                dialog.dismiss();

                            }
                        }).show();
            }
        }
    };

    private void successmsg() {
        SP.edit().putBoolean("PAID", true).commit();

        //not implement this one
       // ArrayList<Item> itemm = DatabaseHandler.getInstance(MainActivity.this).getItemsByName("" + H.SN, H.ASC, -1, 1000);
        //Item it = itemm.get(0);
        //it.set_note(it.get_note());
       // DatabaseHandler.getInstance(MainActivity.this).updateItem(it);

        if(!SP.getBoolean("disclaimerv33", true)) {
            (new AlertDialog.Builder(MainActivity.this)).setMessage("You have successfully upgraded to ads free edition. Thank you for your support!\n\nRestart the app or Rotate device to enjoy ads free environment immediately?").setPositiveButton("Yes,Restart now",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            // This solution is not a good solution, but have no better choice
                            Intent i = getBaseContext().getPackageManager()
                                    .getLaunchIntentForPackage(getBaseContext().getPackageName());
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);

                            dialog.dismiss();

                        }
                    }).setNegativeButton("No. Restart Later", null).show();
        }

    }

    IabHelper.QueryInventoryFinishedListener mGotInventoryListener
            = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result,
                                             Inventory inventory) {

            if (result.isFailure()) {
                H.pupmsg(MainActivity.this, "failed inventory check");

            } else {
                // does the user have the premium upgrade?
                boolean mIsPremium = inventory.hasPurchase(H.SKU);
                purchase = inventory.getPurchase(H.SKU);// when to implement purchase consume, it is needed
                if (mIsPremium) {
                    SP.edit().putBoolean("PAID", true).commit();

                    if (!H.PAID) {
                        successmsg();
                    }
                } else {

                    if (H.PAID) {
                        H.pupmsg(MainActivity.this, "Google Play Store Record shows that this copy of app is not paid. Ads will turn on, Please contact Google PLay Store for more information");
                    }

                    SP.edit().putBoolean("PAID", false).commit();
                }


            }
        }
    };


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putLong("entryid", entryid);
        savedInstanceState.putInt("page", page);

    }

    @Override
    public void onRewarded(RewardItem reward) {

        final long adsplaytime=(System.currentTimeMillis()-adsstart)/1000;
        long lapse=System.currentTimeMillis()-SP.getLong("adsfreestarttime",0);
        //System.out.println("Ads Free Time========================"+"  >>>"+adsfreetime);
        final int am=reward.getAmount();
        if(lapse>H.ADSRESET){
            adsfreetime = am;
        }else{
            adsfreetime += am;
        }

        SP.edit().putLong("adsfreestarttime",System.currentTimeMillis()).commit();
        SP.edit().putLong("adsfreetime",adsfreetime).commit();

        SP.edit().putInt("tripbonus",SP.getInt("tripbonus",0)+1).commit();

        if(fragment!=null && fragment.adsfreev!=null){
            fragment.adsfreev.setText(adsfreetime+" Seconds Left\nBonus Trip:"+SP.getInt("tripbonus",0));
        }
        new Thread() {
            public void run() {
                sendMsg("d=" + H.D + "&sn=" + DatabaseHandler.getInstance(MainActivity.this).getSN() + "&pt=" + adsplaytime + "&b=" + SP.getInt("tripbonus",0)+"&am="+am,H.BONUS);
            }
        }.start();
    }


    @Override
    public void onRewardedVideoAdLeftApplication() {
        //Toast.makeText(this, "onRewardedVideoAdLeftApplication",
        //        Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdClosed() {
        loadRewardedVideoAd();

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
        //Toast.makeText(this, "onRewardedVideoAdFailedToLoad", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        //Toast.makeText(this, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdOpened() {
        //Toast.makeText(this, "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoStarted() {
        adsstart=System.currentTimeMillis();
        //Toast.makeText(this, "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
    }

   // @Override
    public void onRewardedVideoCompleted() {
       // Toast.makeText(this, "onRewardedVideoCompleted", Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onPause() {
        super.onPause();
        if(!H.PAID) {
            mRewardedVideoAd.pause(this);
        }
    }

    @Override
    public void onDestroy() {

        super.onDestroy();

        if(!H.PAID) {
            mRewardedVideoAd.destroy(this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        lat=location.getLatitude();
        lon=location.getLongitude();
        SP.edit().putLong("lat",(long)(lat*100000000)).commit();
        SP.edit().putLong("lon",(long)(lon*100000000)).commit();
        //System.out.println(lat+"======LocationLaLo======="+lon);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
