package net.compuways.bookkeeper;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
//import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardedVideoAd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.annotation.Target;
import java.net.MalformedURLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EntryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EntryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EntryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    GridLayout gridLayout;
    private EditText prereading, amount;
    private TextView note, startingm, dratem, insharem, milesum, datasharem, bdateD, edateD, txv, percentage, msg, rs;
    private RadioButton foruber, forlyft, forother, overhead;
    private SharedPreferences SP;
    private Button bdatem, edatem, autou = null, auto = null, save, settingsave, tend, tstart, pickdt;

    //private InterstitialAd mInterstitialAd;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
    SimpleDateFormat sdffull = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

    private double drate = 0, inshare = -1, gasshare = -1, datashare = -1, entrytotal = 0, tempp = 0, difn, difl, difu;
    private int width, mileoriention = 0, tript = 0, tripcp = 0;
    private String cs = "", unit = "", lyftyear = "", lyftweek = "";
    private long entryid = -1, tstartv = 0, tendv = 0, bdate = 0, edate = 0, orderentryid = -1;
    private boolean backrun = true, service = true;
    private Item ucode;
    Calendar myCalendar = Calendar.getInstance();
    boolean landscreen = false, JSONLOCK = false, JSONCAL = false, temb = true;
    private OnFragmentInteractionListener mListener;
    WebView webView, detailweb, wv;
    private Menu menu;
    private ImageButton tripbtn;
    public Item webitem = null;
    private int webyear = -1, orderid = -1, tripcount = 0, tripcounter = 0, updated = 0, added = 0, missed = 0;
    private RadioButton currentrb = null;
    ProgressBar progressbar;
    private Button viewidbtn = null;
    private Drawable previousid, redicon, greenicon, blackicon;
    private RadioGroup lyftrg, crg = null;
    private AlertDialog alertDialogn = null;
    HorizontalScrollView hscrew = null;
    ScrollView hscroll = null;
    private MainActivity mainActivity;
    private MenuItem menunext, menuprevious;
    private boolean autodownload = false, rechecking = false;

    TextView adsfreev=null,fabmsg=null;
    FloatingActionButton fab=null;
    int tabstate=0;
    //*********************************************//
    BKViewModel viewmodel;

    //********************************************//

    public EntryFragment() {

    }

    public static EntryFragment newInstance(long entryid) {
        EntryFragment fragment = new EntryFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM1, entryid);
        fragment.setArguments(args);
        return fragment;
    }
    public void setmain(MainActivity main){
        this.mainActivity=main;
    }
    public void setRecsLayout(final GridLayout gridLayout, final long entryid) {
        this.gridLayout = gridLayout;
        if (hscrew != null) {

            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hscrew.removeAllViews();

                    /*/
                    if (entryid != H.SUMMARY && entryid != H.WEEKR && entryid != H.DAILYR && entryid != H.TRIPDETAIL) {
                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                        params.setMargins(0, 0, 0, 0);
                        params.gravity = Gravity.NO_GRAVITY;
                        hscrew.setLayoutParams(params);
                    }
                    //*/
                    hscrew.addView(gridLayout);
                }
            });

        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            entryid = getArguments().getLong(ARG_PARAM1);
        }

        setRetainInstance(true);

    }

    private View newoncreateview(final LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState){
        View view = null;
        viewmodel=BKViewModel.getInstance(new Respositary());

        //TODO
        long entryid=0;
        if(entryid==H.ENTRIES){

        }
        //>>>>>>>>............

        return view;
    }
    /**
     * linsterner to catch frament event to request viewModel to take respective action
     *
     *
     *
    */
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(true) return newoncreateview(inflater,container,savedInstanceState);

        View view = null;

        SP = PreferenceManager.getDefaultSharedPreferences(mainActivity.getBaseContext());
        DatabaseHandler db = DatabaseHandler.getInstance(mainActivity);
        width = H.getScreenWidth(mainActivity);

        wvop();

        unit = mainActivity.unit;
        cs = mainActivity.cs;
        inshare = mainActivity.inshare;
        datashare = mainActivity.datashare;
        gasshare = mainActivity.gasshare;
        drate = mainActivity.drate;

        if (drate == 0) {
            if (unit.equalsIgnoreCase("imperial")) {
                drate = 0.2;
            } else {
                drate = 0.13;
            }
        }

        if (entryid == H.ENTRIES || entryid == H.D_ENTRIES) {
            view = inflater.inflate(R.layout.content_main, container, false);
            gridLayout = view.findViewById(R.id.entries);
            mainActivity.gridLayout = gridLayout;

            if(entryid==H.ENTRIES) {
                mainActivity.fillgridlayout(gridLayout, mainActivity.names,H.ENTRIES);
            }else{
                mainActivity.fillgridlayout(gridLayout, mainActivity.ulnames,H.D_ENTRIES);//downloaded entries
            }

        } else if (entryid == H.MILEAGE) {
            view = inflater.inflate(R.layout.mileage, container, false);

            prereading = (EditText) view.findViewById(R.id.prereading);
            milesum = (TextView) view.findViewById(R.id.milesum);

            foruber = (RadioButton) view.findViewById(R.id.foruber);
            amount = (EditText) view.findViewById(R.id.amount);


            // db = DatabaseHandler.getInstance(getActivity());
            int kilometer = (int) db.getMax(H.MILEAGE);//_rcdate store the previous ending reading


            if (kilometer < 10) {
                kilometer = SP.getInt("start", 0);
            } else if (kilometer < SP.getInt("BREADING", 0)) {
                kilometer = SP.getInt("BREADING", 0);
            }


            prereading.setText("" + kilometer);
            int ekilometer = kilometer;
            if (ekilometer < SP.getInt("EREADING", 0)) {
                ekilometer = SP.getInt("EREADING", 0);
            }
            amount.setText("" + ekilometer);

            prereading.addTextChangedListener(prereadinglistner);
            amount.addTextChangedListener(prereadinglistner);


            entrytotal = db.getMileageSum();

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

                mileoriention = H.MILELAND;
            } else {
                mileoriention = H.MILEPORT;
            }


        } else if (entryid == H.ONLINEHRS || entryid == H.TRIPHRS) {
            view = inflater.inflate(R.layout.timerecording, container, false);
            tend = (Button) view.findViewById(R.id.tend);
            tstart = (Button) view.findViewById(R.id.tstart);
            tstart.setTextColor(Color.WHITE);
            tstart.setEnabled(true);
            tstart.setOnClickListener(tstartListener);
            tend.setOnClickListener(tendListener);
            tend.setTextColor(Color.WHITE);
            tend.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    doubleclickend();
                    return true;
                }
            });


            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                tstart.setWidth(width / 4);
                tend.setWidth(width * 3 / 4);
            } else {
                tstart.setWidth(width / 2);
                tend.setWidth(width / 2);
            }

            if (entryid == H.ONLINEHRS) {
                tstartv = SP.getLong("online", 0);
                SP.edit().putInt("tripscreen", 0).commit();//This is to control whether the End button show the online tine
                if (tstartv == 0) {
                    tstart.setText("Start Online");
                    tstart.setEnabled(true);

                } else {
                    timecount();
                    tstart.setEnabled(false);

                }
                tend.setText("End Online");
                entrytotal = db.getSum(H.ONLINEHRS);
            } else if (entryid == H.TRIPHRS) {
                if (SP.getLong("online", 0) == 0) {
                    SP.edit().putLong("online", System.currentTimeMillis()).commit();
                }

                SP.edit().putInt("tripscreen", 1).commit();//This is to control whether the End button show the online tine

                tstartv = SP.getLong("trip", 0);
                if (tstartv == 0) {
                    tstart.setText("Start Trip");
                    tstart.setEnabled(true);

                } else {

                    timecount();
                    tstart.setEnabled(false);

                }
                tend.setText("End Trip");

                onlineclock();

                entrytotal = db.getSum(H.TRIPHRS);

            }

        } else if (entryid == H.SUMMARY || entryid == H.WEEKR || entryid == H.DAILYR) {
            view = inflater.inflate(R.layout.sum, container, false);
            txv = (TextView) view.findViewById(R.id.period);

            hscrew = (HorizontalScrollView) view.findViewById(R.id.hscrew);
            progressbar = (ProgressBar) view.findViewById(R.id.pb);
            percentage = (TextView) view.findViewById(R.id.percentage);

            edate = SP.getLong("edate", 0);
            bdate = SP.getLong("bdate", 0);

            if (edate == 0 || bdate == 0) {
                edate = System.currentTimeMillis();
                SP.edit().putLong("edate", edate).commit();
                bdate = System.currentTimeMillis() - 24l * 60l * 60l * 1000l * 365l;
                SP.edit().putLong("bdate", bdate).commit();
            }
            txv.setText("Report Period:" + (sdf.format(new Date(bdate))) + "--" + (sdf.format(new Date(edate))) + "(Tap to Change)");
            txv.setTag("" + entryid);
            txv.setOnClickListener(changeperiod);

            gridLayout = (GridLayout) view.findViewById(R.id.summary);

            ucode = new Item();
            ucode.set_note("SU");
            ucode.set_name(H.USERCODE);
            db.addItem(ucode);

        } else if (entryid == H.DSETTING) {
            view = inflater.inflate(R.layout.driversettings, container, false);

            startingm = (EditText) view.findViewById(R.id.sharegasm);
            startingm.setText("" + gasshare);

            dratem = (EditText) view.findViewById(R.id.dratem);
            dratem.setText("" + (Math.round(100 * drate) / 100.0));
            settingsave = (Button) view.findViewById(R.id.settingsave);
            startingm.addTextChangedListener(textwatcher);
            dratem.addTextChangedListener(textwatcher);

            insharem = (EditText) view.findViewById(R.id.inshare);
            insharem.setText("" + (Math.round(100 * inshare) / 100.0));
            insharem.addTextChangedListener(textwatcher);

            datasharem = (EditText) view.findViewById(R.id.datasharem);
            datasharem.setText("" + (Math.round(100 * datashare) / 100.0));
            datasharem.addTextChangedListener(textwatcher);

            settingsave.setOnClickListener(settingSaveListener);

            TextView miledes = (TextView) view.findViewById(R.id.miledes);
            TextView ratedes = (TextView) view.findViewById(R.id.ratedes);
            if (unit.equalsIgnoreCase("metric")) {
                ratedes.setText("Depreciation Rate(" + cs + "/KM)");
            } else {
                ratedes.setText("Depreciation Rate(" + cs + "/Mile)");
            }

            ucode = new Item();
            ucode.set_note("DS");
            ucode.set_name(H.USERCODE);
            db.addItem(ucode);


        } else if (entryid == H.DATAE || entryid == H.ALLR) {

            view = inflater.inflate(R.layout.list, container, false);
            hscrew = (HorizontalScrollView) view.findViewById(R.id.hscrew);
            progressbar = (ProgressBar) view.findViewById(R.id.pb);

            ucode = new Item();
            ucode.set_note("DA");
            ucode.set_name(H.USERCODE);
            db.addItem(ucode);
        } else if (entryid == H.UBERLOGIN || entryid == H.LYFTLOGIN) {

            view = inflater.inflate(R.layout.driverlogin, container, false);
            msg = view.findViewById(R.id.msg);
            rs = view.findViewById(R.id.rs);

            webView = view.findViewById(R.id.webView);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setDomStorageEnabled(true);
            webView.addJavascriptInterface(new MyJavaScriptInterface(mainActivity), "HtmlViewer");
            JSONLOCK = true;

            webView.setWebViewClient(new WebViewClient() {

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);


                    if (url.contains("statements/all")) {
                        view.loadUrl("javascript:" + steall);
                    } else if (url.contains("statements/index")) {
                        view.loadUrl("javascript:" + currentjsn);
                    }

                }


            });

            String link = "https://partners.uber.com/p3/money/statements/index";

            if (entryid == H.LYFTLOGIN) {
                link = "https://lyft.com/login";
            }

            webView.loadUrl(link);

        } else if (entryid == H.ENTRYMATCH) {

            view = inflater.inflate(R.layout.matchsetting, container, false);
            final RadioGroup rgf = (RadioGroup) view.findViewById(R.id.frombl);
            final RadioGroup rgm = (RadioGroup) view.findViewById(R.id.mentry);
            final RadioGroup rg0 = (RadioGroup) view.findViewById(R.id.entrynames);
            H.fillRadioGroup(mainActivity, rg0, H.ULACC);
            RadioButton rb = new RadioButton(mainActivity);
            rb.setText("IGNORE");
            rg0.addView(rb);
            TextView txv = new TextView(mainActivity);
            txv.setText("Tap to select corresponding entry below:");
            rg0.addView(txv, 0);
            final Button savechange = view.findViewById(R.id.change);
            ArrayList<Item> items = null;
            if (entryid == H.ENTRYMATCH) {
                items = db.getItemsByName("" + H.TRIPITEM, H.DESC, -1, 1000);
            }
           // System.out.println("?????=========" + items.size());
            this.fillMatchRadioGroup(rgf, rgm, items, H.ULACC);

            rgf.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    int index = radioGroup.indexOfChild(radioGroup.findViewById(radioGroup.getCheckedRadioButtonId()));
                    //System.out.println("?????=" + index);
                    rgm.check((rgm.getChildAt(index)).getId());
                }
            });

            rg0.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    int index = rgf.indexOfChild(rgf.findViewById(rgf.getCheckedRadioButtonId()));
                    RadioButton rb = (RadioButton) rgm.getChildAt(index);

                    RadioButton me = radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
                    rb.setTag(me.getTag());
                    rb.setText(me.getText());
                    rb.setTextColor(me.getCurrentTextColor());

                    if (!savechange.isEnabled()) {
                        savechange.setEnabled(true);
                    }
                }
            });
            savechange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    LayoutInflater factory = LayoutInflater.from(mainActivity);
                    final View pswview = factory.inflate(R.layout.driversettingpsw, null);
                    final EditText psw = pswview.findViewById(R.id.password);

                    final AlertDialog.Builder alert = new AlertDialog.Builder(mainActivity);
                    alert.setView(pswview).setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    dialog.dismiss();
                                   // if (psw.getText().toString().trim().equals("MYULBOOKKEEPER")) {
                                        if (psw.getText().toString().trim().equals("111")) {
                                        updateSetting(rgf, rgm);
                                        H.pupmsg(mainActivity, "Password OK");
                                    } else {
                                        H.pupmsg(mainActivity, "Password doesn't match");
                                    }

                                }
                            }).setNegativeButton("Cancel", null);
                    alert.show();
                }
            });

        }else if (entryid == H.TRIPDETAIL) {
            view = inflater.inflate(R.layout.tripdetail, container, false);
            detailweb = view.findViewById(R.id.detailweb);
            detailweb.getSettings().setJavaScriptEnabled(true);
            detailweb.addJavascriptInterface(new MyJavaScriptInterface(mainActivity), "HtmlViewer");
            detailweb.setWebViewClient(new WebViewClient() {


                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);


                    if (url.contains("statements/all")) {
                        view.loadUrl("javascript:" + steall);
                    } else if (url.contains("/payments/trips/")) {
                        int n = url.indexOf("payments/trips/");
                        String tripid = url.substring(n + 15);
                        view.loadUrl("javascript:var tripid='" + tripid + "';" + tripupdate);

                    }

                }


            });


            String html = "";
            if (webitem == null) {
                html = H.getHtml(mainActivity, "");
            } else {
                html = H.getHtml(mainActivity, webitem.get_xxx());
                // System.out.println("xxx=\n" + webitem.get_xxx());
            }
            detailweb.loadDataWithBaseURL("", html, "text/html", "UTF-8", "");
            FloatingActionButton tab=(FloatingActionButton)view.findViewById(R.id.tab);
            final ScrollView sc=(ScrollView) view.findViewById(R.id.half);
            final ScrollView scu=(ScrollView) view.findViewById(R.id.hscroll);

            tab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(tabstate==0) {
                        sc.setVisibility(View.GONE);
                       // scu.se
                        tabstate=1;
                    }else if(tabstate==1){
                        sc.setVisibility(View.VISIBLE);
                        tabstate=2;
                    }else if(tabstate==2){
                        sc.setVisibility(View.VISIBLE);
                        scu.setVisibility(View.GONE);
                        tabstate=3;
                    }else{
                        scu.setVisibility(View.VISIBLE);
                        tabstate=0;
                    }
                }
            });

        } else if(entryid==H.REWARDV){
            view = inflater.inflate(R.layout.rewardv, container, false);
            Button btn=(Button)view.findViewById(R.id.viewvideo);
            adsfreev=(TextView)view.findViewById(R.id.adsfreetime);
            WebView info=(WebView) view.findViewById(R.id.description);
            String html="<html><body><p style='color:red;text-align:center'>AdsFree Time  Rules</p><p></p><p><ul><li>Users can obtain ads free time and increase trip download limit by viewing the ads video</li>\n" +
                    "    <li>After completing each view of ads video, you can acquire 180 seconds(3 minutes) of ads free time and 1 trip download increment</li>\n" +
                    "    <li>You can view the ads video as many times as you want and accumulate the ads free time and bonus trip downloads</li>\n" +
                    "    <li>Ads free time and bonus trip download has no money value and can not be transferred to other persoon</li>\n" +
                    "    <li>Ads free time must be  used within 24 hours.After 24 hours,the balance will be turned to Zero. However the bonus trip download will\n" +
                    "    remain forever with your current SN number</li><li>Those rules can be modified in the future without notice</li></ul></p></body></html>";
            info.loadDataWithBaseURL("", html, "text/html", "UTF-8", "");
            //mainActivity.adsfreelefttime=mainActivity.adsfreetime*1000-System.currentTimeMillis()+SP.getLong("adsfreestarttime",System.currentTimeMillis());
            adsfreev.setText((mainActivity.adsfreetime)+" seconds left\nBonus Trip:"+SP.getInt("tripbonus",0));
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mainActivity.mRewardedVideoAd.isLoaded()){
                        mainActivity.mRewardedVideoAd.show();
                        ((Button)view).setText("View Next Video");
                    }else{

                    }
                }
            });
        }else{
            view = inflater.inflate(R.layout.entry, container, false);
            entrytotal = db.getSum((int) entryid);
        }

        if (entryid > H.DSETTING) {
            detailInit(view, entryid);
        }


        ActionBar bar = mainActivity.getActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
        }
        setHasOptionsMenu(true);

        final AdView mAdView = (AdView) view.findViewById(R.id.adView);
        final AdView mAdView2 = (AdView) view.findViewById(R.id.adView2);
        final AdView mAdView3 = (AdView) view.findViewById(R.id.adView3);
        final AdView mAdView4 = (AdView) view.findViewById(R.id.adView4);
        final AdView mAdView5 = (AdView) view.findViewById(R.id.adView5);
        final AdView mAdView6 = (AdView) view.findViewById(R.id.adView6);
        final AdView mAdView7 = (AdView) view.findViewById(R.id.adView7);
        fab=(FloatingActionButton) view.findViewById(R.id.fab);

        if (!H.PAID) {
            if(entryid!=H.REWARDV){
                fabmsg=(TextView)view.findViewById(R.id.fabmsg);

                if(fabmsg!=null && H.adsfreerun) {
                    //rewardVadsMonitor(fabmsg);
                }

                if(fab!=null){

                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            EntryFragment fragment = EntryFragment.newInstance(H.REWARDV);
                            fragment.setmain(mainActivity);
                            mainActivity.fragment=fragment;
                            mainActivity.getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.container, fragment)
                                    .commit();

                            ucode = new Item();
                            ucode.set_note("REWARDV");
                            ucode.set_name(H.USERCODE);
                            DatabaseHandler.getInstance(mainActivity).addItem(ucode);
                        }
                    });
                }

               // System.out.println("Rewards NotV========================");
            }else{
               // System.out.println("Rewards YesV========================");
            }

            MobileAds.initialize(mainActivity, "ca-app-pub-1632769004072873~1228909947");
            final AdRequest adRequest = new AdRequest.Builder().build();
            final AdRequest adRequest2 = new AdRequest.Builder().build();
            final AdRequest adRequest3 = new AdRequest.Builder().build();
            final AdRequest adRequest4 = new AdRequest.Builder().build();
            final AdRequest adRequest5 = new AdRequest.Builder().build();
            final AdRequest adRequest6 = new AdRequest.Builder().build();
            final AdRequest adRequest7 = new AdRequest.Builder().build();
            //mainActivity.adsfreelefttime=mainActivity.adsfreetime*1000-System.currentTimeMillis()+SP.getLong("adsfreestarttime",System.currentTimeMillis());

            if(mainActivity.adsfreetime<=0) {

                //System.out.println("Ads is back===================");

                if (mAdView != null) {
                    mAdView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mAdView.loadAd(adRequest);
                        }
                    }, 3000);
                    mAdView.setAdListener(H.adlistener);
                }


                if (mAdView2 != null)
                    mAdView2.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mAdView2.loadAd(adRequest2);
                        }
                    }, 6000);

                if (mAdView3 != null)
                    mAdView3.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mAdView3.loadAd(adRequest3);
                        }
                    }, 8000);

                if (mAdView4 != null)
                    mAdView4.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mAdView4.loadAd(adRequest4);
                        }
                    }, 10000);

                if (mAdView5 != null)
                    mAdView5.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mAdView5.loadAd(adRequest5);
                        }
                    }, 12000);

                if (mAdView6 != null)
                    mAdView6.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mAdView6.loadAd(adRequest6);
                        }
                    }, 14000);
                if (mAdView7 != null)
                    mAdView7.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mAdView7.loadAd(adRequest7);
                        }
                    }, 16000);


                H.remind(mainActivity);

            }else{
                //System.out.println("Ads is gone===================");
                if(mAdView!=null) {
                    mAdView.setVisibility(View.GONE);
                }
                if (mAdView2 != null)
                    mAdView2.setVisibility(View.GONE);

                if (mAdView2 != null)
                    mAdView2.setVisibility(View.GONE);

                if (mAdView3 != null)
                    mAdView3.setVisibility(View.GONE);

                if (mAdView4 != null)
                    mAdView4.setVisibility(View.GONE);

                if (mAdView5 != null)
                    mAdView5.setVisibility(View.GONE);

                if (mAdView6 != null)
                    mAdView6.setVisibility(View.GONE);

                if (mAdView7 != null)
                    mAdView7.setVisibility(View.GONE);
            }

        } else {

            if(fab!=null){
                fab.setVisibility(View.GONE);
            }

            mAdView.setVisibility(View.GONE);
            if (mAdView2 != null)
                mAdView2.setVisibility(View.GONE);

            if (mAdView2 != null)
                mAdView2.setVisibility(View.GONE);

            if (mAdView3 != null)
                mAdView3.setVisibility(View.GONE);

            if (mAdView4 != null)
                mAdView4.setVisibility(View.GONE);

            if (mAdView5 != null)
                mAdView5.setVisibility(View.GONE);

            if (mAdView6 != null)
                mAdView6.setVisibility(View.GONE);

            if (mAdView7 != null)
                mAdView7.setVisibility(View.GONE);
        }

        final View viewf = view;
        new Thread(new Runnable() {
            @Override
            public void run() {
                updateRecords(entryid, viewf);
            }
        }).start();

        android.support.v7.app.ActionBar toolbar = mainActivity.getSupportActionBar();
        if (entryid == H.ENTRIES && toolbar != null) {

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                toolbar.setTitle("");
            } else {
                toolbar.setTitle(mainActivity.getResources().getString(R.string.app_name));
            }

            if (Build.VERSION.SDK_INT < 21) {
                toolbar.setLogo(mainActivity.getResources().getDrawable(R.drawable.ic_appicon));
            } else {
                toolbar.setLogo(mainActivity.getDrawable(R.drawable.ic_appicon));
            }
        } else if (entryid == H.ALLR && toolbar != null) {
            toolbar.setTitle("All User Records");
        } else if (entryid == H.DATAE && toolbar != null) {
            toolbar.setTitle("Records Editing");
        } else if (entryid == H.UBERLOGIN && toolbar != null) {
            toolbar.setTitle("Uber Login");
        } else if (entryid == H.LYFTLOGIN && toolbar != null) {
            toolbar.setTitle("Lyft Login");
        } else if (entryid == H.ENTRYMATCH && toolbar != null) {
            toolbar.setTitle("Entry Matching");
        } else if (toolbar != null) {
            toolbar.setTitle(DatabaseHandler.getInstance(mainActivity).getEntryNameByID(entryid));
        }

        if (entryid != H.ENTRIES && getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            toolbar.setLogo(null);
        }

        mainActivity.page = -1;
        orderid = -1;
        orderentryid = -1;
        mainActivity.entryid = entryid;

        return view;
    }

    private void adsinThread(final GridLayout gridLayout, final int size, final int adssize) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    mainActivity.addAds(gridLayout, (int) (size * 1.0 / H.Repeat) - 1, 5, H.Repeat);
                }
            }).start();

    }

    @Override
    public void onPause() {
        super.onPause();
        H.adsfreerun=false;
        //adsfreerun=false;
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mainActivity.td!=null){
            mainActivity.td.interrupt();
            mainActivity.td=null;
        }
        //adsfreerun=false;
    }

    @Override
    public void onResume() {
        super.onResume();
        // mainActivity.rewardadsrun=true;
        H.adsfreerun=true;
        if(!H.PAID && fabmsg!=null) {
            rewardVadsMonitor(fabmsg);
        }
    }


     void rewardVadsMonitor(final TextView msg){
        //System.out.println(SP.getLong("adsfreetime",-1)+"[+--===================---+]"+"    run="+mainActivity.rewardadsrun);
        //mainActivity.adsfreelefttime=mainActivity.adsfreetime*1000-System.currentTimeMillis()+SP.getLong("adsfreestarttime",System.currentTimeMillis());
        if(mainActivity.adsfreetime>0 && H.adsfreerun){

                runtd(msg);
        }
    }

    private void runtd(final TextView msg){
        mainActivity.td=new Thread(new Runnable() {
            @Override
            public void run() {

                while (mainActivity.adsfreetime > 0 && H.adsfreerun) {
                    try {

                        Thread.sleep(1000);
                        mainActivity.adsfreetime -= 1;
                        mainActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (msg != null) {
                                    msg.setText("" + (mainActivity.adsfreetime));
                                    SP.edit().putLong("adsfreetime", mainActivity.adsfreetime).commit();
                                }
                            }
                        });
                        //mainActivity.adsfreelefttime=mainActivity.adsfreetime*1000-System.currentTimeMillis()+SP.getLong("adsfreestarttime",System.currentTimeMillis());
                       // System.out.println(SP.getLong("adsfreetime", -1) + "[--+---]" + (mainActivity.adsfreetime));

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        mainActivity.td.start();
    }
    private void updateSetting(RadioGroup rgf, RadioGroup rgm) {
        RadioButton rb = null;
        Item it = null, itm = null;
        for (int i = 1; i < rgf.getChildCount(); i++) {
            rb = (RadioButton) rgm.getChildAt(i);
            if (rb.getText().toString() != "IGNORE" && rb.getText().toString() != "To be set") {
                it = (Item) rgf.getChildAt(i).getTag();
                itm = (Item) rgm.getChildAt(i).getTag();
                if (it.get_rcdate() != itm.get_rcdate()) {

                    it.set_rcdate(itm.get_rcdate());
                    DatabaseHandler.getInstance(mainActivity).updateItem(it);

                }

            }

        }
    }

    private void wvop() {
        wv = new WebView(mainActivity);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.addJavascriptInterface(new EntryFragment.MyJavaScriptInterface(mainActivity), "HtmlViewer");
        wv.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);


                if (url.contains("statements/view_by_id")) {

                    int n = url.indexOf("view_by_id");

                    if (n > 0) {
                        String id = url.substring(n + 11);
                        view.loadUrl("javascript:" + wkstecoding);
                    }


                } else if (url.contains("/payments/trips/")) {
                    int n = url.indexOf("payments/trips/");
                    String tripid = url.substring(n + 15);

                    view.loadUrl("javascript:var tripid='" + tripid + "';" + tripupdate);

                } else if (url.contains("/statements/view/")) {
                    int n = url.indexOf("statements/view/");
                    String viewid = url.substring(n + 16);
                    view.loadUrl("javascript:var viewid='" + viewid + "';" + jsonparser);

                } else if (url.contains("statements/index")) {
                    webyear = -1;
                    view.loadUrl("javascript:" + currentjsn);

                } else if (url.contains("https://www.lyft.com/api/route_detail_earnings/route/")) {//detail trip
                    view.loadUrl("javascript:window.HtmlViewer.lyft(document.getElementsByTagName('html')[0].textContent,'TRIP','')");

                } else if (url.contains("https://www.lyft.com/api/driver_yearly_details/")) {  //year data
                    view.loadUrl("javascript:window.HtmlViewer.lyft(document.getElementsByTagName('html')[0].textContent,'YEARS','')");
                } else if (url.contains("https://www.lyft.com/api/driver_routes/")) { //weekly reference data
                    int n = url.indexOf("api/driver_routes/");
                    String wk = url.substring(n + 18);
                    view.loadUrl("javascript:window.HtmlViewer.lyft(document.getElementsByTagName('html')[0].textContent,'WEEKS','" + wk + "')");
                } else if (url.contains("https://www.lyft.com/api/driver_week_earnings/")) { //weekly data
                    int n = url.indexOf("lyft.com/api/driver_week_earnings/");
                    String wk = url.substring(n + 34);
                    view.loadUrl("javascript:window.HtmlViewer.lyftwk(document.getElementsByTagName('html')[0].textContent,'" + wk + "')");
                } else if (url.contains("https://www.lyft.com/drive/routes/")) { //weekly website
                    int n = url.indexOf("https://www.lyft.com/drive/routes/");
                    String wkdate = url.substring(n + 34);
                    view.loadUrl("javascript:var wkdate='" + wkdate + "';" + lyftwk);
                } else if (url.contains("https://auth.uber.com/login/")) {
                    H.pupmsg(mainActivity, "You need to log into Uber account");
                }

            }

        });

    }

    void fillMatchRadioGroup(RadioGroup rgf, RadioGroup rgm, ArrayList<Item> items, int type) {
        rgf.removeAllViews();
        TextView txv1 = new TextView(mainActivity);
        if (type == H.ULACC) {
            txv1.setText("Entries from Uber or Lyft");
        } else if (type == H.ULACCS) {
            txv1.setText("Entries from Uber");
        }
        rgf.addView(txv1);

        rgm.removeAllViews();
        TextView txv2 = new TextView(mainActivity);

        if (type == H.ULACC) {
            txv2.setText("   Corresponding Entries in this App");
        } else if (type == H.ULACCS) {
            txv2.setText("   Corresponding Entries in this App");
        }

        rgm.addView(txv2);


        for (int i = 0; i < items.size(); i++) {

            final Item item = items.get(i);
            if (item.get_note().equalsIgnoreCase("Trip Detail")) {
                continue;
            }

            RadioButton btn = new RadioButton(mainActivity);
            btn.setText(item.get_note());
            btn.setTag(item);
            rgf.addView(btn);


            RadioButton btn2 = new RadioButton(mainActivity);

            ArrayList<Item> items1 = DatabaseHandler.getInstance(mainActivity).getEntryItemById(item.get_rcdate());

            if (items1.size() > 0) {
                btn2.setText(items1.get(0).get_note());

                if (items1.get(0).get_date() == H.RECEIVABLE) {
                    btn2.setTextColor(Color.parseColor("#347235"));
                } else if (items1.get(0).get_date() == H.PAYABLE) {
                    btn2.setTextColor(Color.parseColor("#990012"));
                } else if (items1.get(0).get_rcdate() == H.TAXS || items1.get(0).get_rcdate() == H.TOLL) {
                    btn2.setTextColor(Color.rgb(255, 50, 200));
                } else {
                    btn2.setTextColor(Color.BLACK);
                }

                btn2.setTag(items1.get(0));
            } else {
                btn2.setText("To be set");
            }
            btn2.setEnabled(false);
            rgm.addView(btn2);

            if (i == 0) {
                rgf.check(btn.getId());
                rgm.check(btn2.getId());
            }

        }


        rgm.setEnabled(false);

    }

    void updateRecords(final long entryid, View view) {

        gridLayout = new GridLayout(mainActivity);
        ArrayList<Item> tem = null;
        if (entryid != H.DATAE && entryid != H.ALLR && entryid != H.SUMMARY && entryid != H.WEEKR && entryid != H.DAILYR) {
            tem = DatabaseHandler.getInstance(mainActivity).getItemsByName("" + entryid, H.DESC, -1, mainActivity.limit);//initial size
        }

        final ArrayList<Item> its = tem;
        // System.out.println("updatedR==============="+its.size());

        if (progressbar != null && !progressbar.isShown()) {
            progressbar.setVisibility(View.VISIBLE);
        }

        //mainActivity.adsfreelefttime=mainActivity.adsfreetime*1000-System.currentTimeMillis()+SP.getLong("adsfreestarttime",System.currentTimeMillis());
        boolean localb=!H.PAID && mainActivity.adsfreetime<=0;
        //System.out.println("Ads free time"+mainActivity.adsfreetime+" ======================== locab="+localb);
        if (entryid == H.MILEAGE) {
            H.fillgridlayout(mainActivity, gridLayout, its, entryid, H.getScreenWidth(mainActivity), 6,
                    true, false, true, editListener, orderListener, null, null, progressbar);
            if (localb) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mainActivity.addAds(gridLayout, (int) (its.size() * 1.0 / H.Repeat) - 1, 6, H.Repeat);
                    }
                }).start();
            }

        } else if (entryid == H.TRIPDETAIL) {

            if (webitem != null) {
                int i = -3;

                for (int j = 0; j < its.size(); j++) {
                    if (webitem.get_date() == ((Item) its.get(j)).get_date()) {
                        i = j;
                        break;
                    }
                }

                if (i >= 0) {
                    Item it = its.remove(i);
                    its.add(0, it);
                }
            }
            //tripdetailitems=its;
            H.fillgridlayout(mainActivity, gridLayout, its, entryid, H.getScreenWidth(mainActivity), 5,
                    true, false, true, editListener, orderListener, null, tripdetailLC, progressbar);
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    detailwebOP(gridLayout, its.size() == 0 ? null : its.get(0), mainActivity);
                }
            });

            if (localb) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mainActivity.addAds(gridLayout, (int) (its.size() * 1.0 / H.Repeat) - 1, 5, H.Repeat);
                    }
                }).start();
            }


        } else if (entryid == H.SUMMARY) {

            if (landscreen || getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                summary(gridLayout, progressbar, percentage);

            } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                summary_land(gridLayout, progressbar, percentage);

            }

        } else if (entryid == H.DATAE) {

            mainActivity.datarepareitems = DatabaseHandler.getInstance(mainActivity).getAllItemsOrderBy("" + H.KEY_ITEM, mainActivity.dir, -1, mainActivity.limit);
            H.fillgridlayout(mainActivity, gridLayout, mainActivity.datarepareitems, H.DATAE, H.getScreenWidth(mainActivity), 6, false, false
                    , false, null, null, mainActivity.cclistener, null, progressbar);
            if (localb) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mainActivity.addAds(gridLayout, (int) (mainActivity.datarepareitems.size() * 1.0 / H.Repeat) - 1, 6, H.Repeat);
                    }
                }).start();
            }

        } else if (entryid == H.ALLR) {

            final ArrayList<Item> items = DatabaseHandler.getInstance(mainActivity).getAllItemsOrderBy("" + H.KEY_ITEM, mainActivity.dir, -1, mainActivity.limit);
            H.fillgridlayout(mainActivity, gridLayout, items, -1, H.getScreenWidth(mainActivity), 6, false, true
                    , true, null, orderListener, null, null, progressbar);
            if (localb) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mainActivity.addAds(gridLayout, (int) (items.size() * 1.0 / H.Repeat) - 1, 6, H.Repeat);
                    }
                }).start();
            }

        } else if (entryid == H.DAILYR) {
            dailyReport(gridLayout, progressbar, percentage);
        } else if (entryid == H.WEEKR) {
            weeklyReport(gridLayout, progressbar, percentage);
        } else {
            H.fillgridlayout(mainActivity, gridLayout, its, entryid, H.getScreenWidth(mainActivity), 5,
                    true, false, true, editListener, orderListener, null, null, progressbar);
            if (localb) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mainActivity.addAds(gridLayout, (int) (its.size() * 1.0 / H.Repeat) - 1, 5, H.Repeat);
                    }
                }).start();
            }

        }

        if (progressbar != null && progressbar.isShown()) {
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressbar.setVisibility(View.GONE);
                }
            });

        }

        setRecsLayout(gridLayout, entryid);


        if (localb) {
            //LinearLayout scr=view.findViewById(R.id.manual);
            ScrollView scr = view.findViewById(R.id.half);

            if (scr != null && mainActivity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                //H.pupmsg(mainActivity,scr.getLayoutParams().height+"<>"+((int)(H.getScreenHeight(mainActivity)*2.0/3.0)));
                // H.pupmsg(mainActivity, gridLayout.getHeight() + "<ilI>" + H.getScreenHeight(mainActivity));
                if (H.getScreenHeight(mainActivity) < 2500) {
                    scr.getLayoutParams().height = (int) (H.getScreenHeight(mainActivity) * 2.0 / 3.0);
                }
            } else if (scr != null && mainActivity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                // scr.getLayoutParams().width=321;
            }

        }

    }

    private void detailInit(View rootView, long entryid) {
        save = (Button) rootView.findViewById(R.id.save);
        if (H.PAID && save != null) {
            save.setText("Save\n(" + entrytotal + ")");
        }

        if (amount == null) {
            amount = (EditText) rootView.findViewById(R.id.amount);

            if (amount != null) {
                amount.addTextChangedListener(savebuttonlistener);
            }
        }

        myCalendar = Calendar.getInstance();

        if (entryid != H.TRIPDETAIL) {
            note = (EditText) rootView.findViewById(R.id.note);
            pickdt = (Button) rootView.findViewById(R.id.pickdt);
            pickdt.setOnClickListener(dtlistner);
            pickdt.setText(myCalendar.get(Calendar.DATE) + "/" + (myCalendar.get(Calendar.MONTH) + 1) + "/" + myCalendar.get(Calendar.YEAR) +
                    "   " + myCalendar.get(Calendar.HOUR_OF_DAY) + ":" + myCalendar.get(Calendar.MINUTE) + ":" + myCalendar.get(Calendar.SECOND) +
                    "(Tap to Change)");
            foruber = (RadioButton) rootView.findViewById(R.id.foruber);
            forlyft = (RadioButton) rootView.findViewById(R.id.forlyft);
            forother = (RadioButton) rootView.findViewById(R.id.forother);
            overhead = (RadioButton) rootView.findViewById(R.id.overhead);
        }


        switch ((int) entryid) {
            case H.MAINTENANCE:
            case H.INSURANCE:
            case H.DATA:
            case H.GAS:
            case H.MEAL:
            case H.DSAVING:
            case H.ONLINEHRS:
                forother.setEnabled(false);
                forlyft.setEnabled(false);
                foruber.setEnabled(false);
            case H.EXPENSES:
                if (overhead != null)
                    overhead.setChecked(true);
                break;
            case H.LYFTPAY:
                forlyft.setChecked(true);
                forother.setEnabled(false);
                overhead.setEnabled(false);
                foruber.setEnabled(false);
                break;

            case H.UBERPAY:
                forother.setEnabled(false);
                overhead.setEnabled(false);
                forlyft.setEnabled(false);
                break;
            case H.TRIPHRS:
            case H.MILEAGE:
            case H.TIP:
            case H.OTHERINCOME:
            case H.TAX:
                foruber.setChecked(true);

                if (overhead != null)
                    overhead.setEnabled(false);
                break;
            case H.TRIPDETAIL:
                break;
            default:
                radiobuttonOP((int) entryid);

        }

        if (H.checks(entryid)) {
            if (save != null) {
                save.setVisibility(View.GONE);

            }
            if (note != null) {
                note.setVisibility(View.GONE);

            }
            if (pickdt != null) {
                pickdt.setVisibility(View.GONE);

            }
            if (amount != null) {
                amount.setVisibility(View.GONE);

            }
            RadioGroup rg = rootView.findViewById(R.id.sources);
            if (rg != null) {
                rg.setVisibility(View.GONE);
            }

            ScrollView half = rootView.findViewById(R.id.half);
            if (half != null) {
                half.setVisibility(View.GONE);
            }
            LinearLayout layout = rootView.findViewById(R.id.linearlayout);
            if (layout != null) {
                ViewGroup.LayoutParams params = layout.getLayoutParams();

                params.height = ViewGroup.LayoutParams.MATCH_PARENT;
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                layout.setLayoutParams(params);
            }
        }


        DatabaseHandler db = DatabaseHandler.getInstance(mainActivity);

        if (entryid == H.MILEAGE || entryid == H.ONLINEHRS || entryid == H.TRIPHRS) {
            cs = "";
        }

        //----------Record tables under entry screen-----------

        hscrew = (HorizontalScrollView) rootView.findViewById(R.id.hscrew);
        hscroll = (ScrollView) rootView.findViewById(R.id.hscroll);
        progressbar = (ProgressBar) rootView.findViewById(R.id.pb);

        if (hscrew == null) {
           // System.out.println("vinisde00==null");
        } else {
           // System.out.println("vinisde00==not null");
            if (gridLayout != null) {

                ViewParent vp = gridLayout.getParent();
                if (vp != null && vp instanceof HorizontalScrollView) {
                    HorizontalScrollView hs = (HorizontalScrollView) vp;
                    hs.removeAllViews();
                }

               // System.out.println("gridlayout not null");
                hscrew.removeAllViews();
                hscrew.addView(gridLayout);
            }
        }


        if (save != null) {
            save.setOnClickListener(btnSaveListener);
        }

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

    private View.OnClickListener dtlistner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Toast.makeText(getContext(),"dt liset",Toast.LENGTH_LONG);
            new DatePickerDialog(getContext(), dtL, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            ucode = new Item();
            ucode.set_note(entryid + "DL");
            ucode.set_name(H.USERCODE);
            DatabaseHandler.getInstance(mainActivity).addItem(ucode);
        }
    };

    DatePickerDialog.OnDateSetListener dtL = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            Calendar time = Calendar.getInstance();
            myCalendar.set(myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DATE),
                    time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.SECOND));
            pickdt.setText(myCalendar.get(Calendar.DATE) + "/" + (myCalendar.get(Calendar.MONTH) + 1) + "/" + myCalendar.get(Calendar.YEAR) +
                    "   " + myCalendar.get(Calendar.HOUR_OF_DAY) + ":" + myCalendar.get(Calendar.MINUTE) + ":" + myCalendar.get(Calendar.SECOND) +
                    "(Tap to Change)");

        }

    };

    private View.OnClickListener btnSaveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String stram = amount.getText().toString();
            if (stram.length() < 1 || (stram.length() == 1 && stram.startsWith("."))) {
                return;
            }

            saveToDB(Double.valueOf(stram), H.MANUALSAVE);
            v.setEnabled(false);
            ucode = new Item();
            ucode.set_note(entryid + "SB");
            ucode.set_name(H.USERCODE);
            DatabaseHandler.getInstance(mainActivity).addItem(ucode);

        }
    };


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
        redicon = mainActivity.getResources().getDrawable(R.drawable.ic_check_box_red_24dp);
        greenicon = mainActivity.getResources().getDrawable(R.drawable.ic_check_box_green_24dp);
        blackicon = mainActivity.getResources().getDrawable(R.drawable.ic_check_box_black_24dp);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            //      throw new RuntimeException(context.toString()
            //              + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    TextWatcher prereadinglistner = new TextWatcher() {

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
            H.milesumop(prereading, amount, milesum, save, H.MAIN, SP);
        }
    };

    private View.OnClickListener tstartListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            v.setEnabled(false);
            // amount.setEnabled(false);
            // datePicker.setEnabled(false);
            tstartv = System.currentTimeMillis();

            if (entryid == H.ONLINEHRS) {
                SP.edit().putLong("online", tstartv).commit();
            } else if (entryid == H.TRIPHRS) {
                SP.edit().putLong("trip", tstartv).commit();
            }

            backrun = true;
            new Thread(new Runnable() {
                public void run() {

                    while (backrun) {
                        try {

                            v.post(new Runnable() {
                                public void run() {
                                    long diff = System.currentTimeMillis() - tstartv;
                                    long seconds = (diff / 1000) % 60;
                                    long minutes = (diff / 1000 / 60) % 60;
                                    ;
                                    long hours = (diff / 1000 / 60 / 60);

                                    ((Button) v).setText(hours + ":" + ((minutes < 10) ? "0" : "") + minutes + ":" + ((seconds < 10) ? "0" : "") + seconds);

                                }
                            });

                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }


                }
            }).start();

            if (SP.getLong("online", 0) == 0) {
                SP.edit().putLong("online", System.currentTimeMillis()).commit();
            }
            onlineclock();
            ucode = new Item();
            ucode.set_note(entryid + "S");
            ucode.set_name(H.USERCODE);
            DatabaseHandler.getInstance(mainActivity).addItem(ucode);

        }
    };

    private View.OnClickListener tendListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // v.setEnabled(false);

            if (tstart.isEnabled()) {
                return;
            }

            tendv = System.currentTimeMillis();

            if (entryid == H.ONLINEHRS) {
                tstart.setText("Start Online");
                tstartv = SP.getLong("online", 0);
                SP.edit().putLong("online", 0).commit();
            } else if (entryid == H.TRIPHRS) {
                tstart.setText("Start Trip");
                tstartv = SP.getLong("trip", 0);
                SP.edit().putLong("trip", 0).commit();
            } else {

                if (SP == null) {
                    SP = PreferenceManager.getDefaultSharedPreferences(mainActivity.getBaseContext());
                }

                if (SP.getLong("Entryid", -1) == H.TRIPHRS) {
                    tstart.setText("Start Trip");
                    tstartv = SP.getLong("trip", 0);
                    SP.edit().putLong("trip", 0).commit();
                } else if (SP.getLong("Entryid", -1) == H.ONLINEHRS) {
                    tstart.setText("Start Online");
                    tstartv = SP.getLong("online", 0);
                    SP.edit().putLong("online", 0).commit();
                }
                //  H.pupmsg(getActivity(),"entryid="+SP.getLong("Entryid", -1));
            }

            saveToDB((Math.round(((tendv - tstartv) / 1000.0 / 60.0 / 60.0) * 1000.0)) / 1000.0, H.AUTOSAVE);
            backrun = false;

            tstart.setEnabled(true);
            ucode = new Item();
            ucode.set_note(entryid + "E");
            ucode.set_name(H.USERCODE);
            DatabaseHandler.getInstance(mainActivity).addItem(ucode);
        }
    };
    private View.OnClickListener changeperiod = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            changeperiod((String) v.getTag());
        }
    };

    private void timecount() {

        new Thread(new Runnable() {
            public void run() {
                // a potentially  time consuming task
                //final int r = (int) (Math.random() * 100);
                while (backrun) {
                    try {

                        tstart.post(new Runnable() {
                            public void run() {

                                long diff = System.currentTimeMillis() - tstartv;
                                long seconds = (diff / 1000) % 60;
                                long minutes = (diff / 1000 / 60) % 60;
                                ;
                                long hours = (diff / 1000 / 60 / 60);
                                //tstart.setText(hours+":"+minutes+":"+seconds + "   t" + r);

                                tstart.setText(hours + ":" + ((minutes < 10) ? "0" : "") + minutes + ":" + ((seconds < 10) ? "0" : "") + seconds);

                            }
                        });

                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


            }
        }).start();

    }

    private void onlineclock() {

        service = true;

        new Thread(new Runnable() {
            public void run() {
                while (service) {
                    try {
                        if (tend == null || SP.getInt("tripscreen", 0) == 0) {
                            service = false;
                            return;
                        }

                        tend.post(new Runnable() {
                            public void run() {
                                long diff = System.currentTimeMillis() - SP.getLong("online", 0);
                                long seconds = (diff / 1000) % 60;
                                long minutes = (diff / 1000 / 60) % 60;
                                ;
                                long hours = (diff / 1000 / 60 / 60);

                                if (SP.getLong("online", 0) == 0) {
                                    seconds = minutes = hours = 0;
                                }

                                Activity activity = mainActivity;
                                if (activity != null) {

                                    //  if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                    //      tend.setText("End Trip\n" + hours + ":" + ((minutes < 10) ? "0" : "") + minutes + ":" + ((seconds < 10) ? "0" : "") + seconds);
                                    //  } else {
                                    tend.setText("End Trip(" + hours + ":" + ((minutes < 10) ? "0" : "") + minutes + ":" + ((seconds < 10) ? "0" : "") + seconds + ")");
                                    //  }


                                }


                            }
                        });

                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                }


            }
        }).start();

    }

    private void saveToDB(double am, int savemode) {
        saveToDB(am, savemode, -1);
    }

    private void saveToDB(double am, int savemode, int specialid) {

        Item item = new Item();
        // Calendar calendar = new GregorianCalendar(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());

        if (savemode == H.MANUALSAVE) {
            item.set_date(myCalendar.getTimeInMillis());
        } else if (savemode == H.AUTOSAVE) {
            item.set_date(System.currentTimeMillis());
        }
        item.set_rcdate(System.currentTimeMillis());
        item.set_source(getSelectedSource());

        item.set_amount(am);

        if (entryid == H.MILEAGE) {
            item.set_amount(Long.parseLong(amount.getText().toString()));// Ending reading of mileage
            item.set_rcdate(Long.parseLong(prereading.getText().toString()));// gasshare reading of mileage

        }

        item.set_note(note.getText().toString());

        if (specialid == -1) {
            item.set_name("" + entryid);
        } else {
            item.set_name("" + specialid);
        }


        DatabaseHandler db = DatabaseHandler.getInstance(mainActivity);
        db.checkaddItem(item);

        if (entryid == H.MILEAGE) {
            H.fillgridlayout(mainActivity, gridLayout,
                    db.getItemsByName("" + entryid, H.DESC, -1, mainActivity.limit), entryid, H.getScreenWidth(mainActivity), 6, true, false, true, editListener, orderListener, null, null);
            entrytotal = db.getMileageSum();
        } else {
            H.fillgridlayout(mainActivity, gridLayout,
                    db.getItemsByName("" + entryid, H.DESC, -1, mainActivity.limit), entryid, H.getScreenWidth(mainActivity), 5, true, false, true, editListener, orderListener, null, null);
            entrytotal = db.getSum((int) entryid);
        }

        if (H.PAID) {
            save.setText("Save\n(" + entrytotal + ")");
        }

        if (prereading != null) {
            prereading.setText(amount.getText().toString());
        }
        if (amount != null) {
            amount.setText("");
        }
        if (note != null) {
            note.setText("");
        }

    }

    private void doubleclickend() {
        tstartv = SP.getLong("online", 0);
        if (!tstart.isEnabled() || tstartv == 0) {
            return;
        }

        (new AlertDialog.Builder(mainActivity)).setMessage("End online?").setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                        tendv = System.currentTimeMillis();
                        SP.edit().putLong("online", 0).commit();
                        saveToDB((Math.round(((tendv - tstartv) / 1000.0 / 60.0 / 60.0) * 1000.0)) / 1000.0, H.AUTOSAVE, H.ONLINEHRS);
                        backrun = false;
                        service = false;
                        dialog.dismiss();

                    }
                }).setNegativeButton("NO", null).show();
    }

    private short getSelectedSource() {
        if (forlyft.isChecked()) return 1;
        if (forother.isChecked()) return 2;
        if (overhead != null && overhead.isChecked()) return 3;

        return 0;
    }

    private void changeperiod(final String entryid) {

        LayoutInflater factory = LayoutInflater.from(mainActivity);
        final View periodview = factory.inflate(R.layout.period, null);
        bdatem = (Button) periodview.findViewById(R.id.bdatem);
        edatem = (Button) periodview.findViewById(R.id.edatem);
        bdateD = (TextView) periodview.findViewById(R.id.bdateD);
        edateD = (TextView) periodview.findViewById(R.id.edateD);

        edateD.setText(sdf.format(SP.getLong("edate", 0)));
        bdateD.setText(sdf.format(SP.getLong("bdate", 0)));
        bdatem.setOnClickListener(bdateListener);
        edatem.setOnClickListener(edateListener);

        TextView titlev = new TextView(mainActivity);
        titlev.setText("Date Selection");
        titlev.setGravity(Gravity.CENTER_HORIZONTAL);
        titlev.setTextSize(20);
        titlev.setBackgroundColor(Color.parseColor("#4F4FCB"));
        titlev.setTextColor(Color.WHITE);

        final AlertDialog.Builder alert = new AlertDialog.Builder(mainActivity);
        alert.setCustomTitle(titlev).setView(periodview).setPositiveButton("Reload",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {

                        dialog.dismiss();

                        if (txv != null)
                            txv.setText("Report Period:" + (sdf.format(new Date(bdate)) + "-" + (sdf.format(new Date(edate)))) + "(Tap to Change)");

                        reload(entryid);


                    }
                }).setNegativeButton("Cancel", null);
        alert.show();

        ucode = new Item();
        ucode.set_note("CP");
        ucode.set_name(H.USERCODE);
        DatabaseHandler.getInstance(mainActivity).addItem(ucode);


    }

    private void bupdate() {
        Calendar cal = Calendar.getInstance();
        cal.set(myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);


        bdateD.setText(sdf.format(myCalendar.getTime()));
        SP.edit().putLong("bdate", cal.getTimeInMillis()).commit();
        bdate = cal.getTimeInMillis();
    }

    private void eupdate() {
        Calendar cal = Calendar.getInstance();
        cal.set(myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);

        edateD.setText(sdf.format(myCalendar.getTime()));
        SP.edit().putLong("edate", cal.getTimeInMillis()).commit();
        edate = cal.getTimeInMillis();
    }

    DatePickerDialog.OnDateSetListener bdateL = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            bupdate();
        }

    };

    private View.OnClickListener edateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new DatePickerDialog(mainActivity, edateL, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();

        }
    };

    private View.OnClickListener bdateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            new DatePickerDialog(mainActivity, bdateL, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        }
    };

    public void reload(String entryid) {
        gridLayout.removeAllViews();
        if (entryid == null || entryid.equalsIgnoreCase("" + H.SUMMARY)) {
            EntryFragment fragment = EntryFragment.newInstance(H.SUMMARY);
            mainActivity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment).addToBackStack(null)
                    .commit();
        } else if (entryid.equalsIgnoreCase("" + H.DAILYR)) {
            EntryFragment fragment = EntryFragment.newInstance(H.DAILYR);
            mainActivity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment).addToBackStack(null)
                    .commit();
        } else if (entryid.equalsIgnoreCase("" + H.WEEKR)) {
            EntryFragment fragment = EntryFragment.newInstance(H.WEEKR);
            mainActivity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment).addToBackStack(null)
                    .commit();
        }


    }

    DatePickerDialog.OnDateSetListener edateL = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            eupdate();
        }

    };

    private void summary(final GridLayout gridLayout, final ProgressBar pb, final TextView percentage) {
        DatabaseHandler db = DatabaseHandler.getInstance(mainActivity);
        // ArrayList<Item> temI0 = DummyContent.getDETAILS();
        ArrayList<Item> temI0 = db.getItemsByName(H.SETTINGKEY, H.DESC, -1, 100000);
        ArrayList<Item> temI = new ArrayList<>();
        double usum = 0, lsum = 0;
        final int m = temI0.size();
        int jj = 0;
        for (Item item : temI0) {
            if (item.get_rcdate() == H.MILEAGE) {
                item.set_amount(db.getMileageSum(bdate, edate + 24 * 60 * 60 * 1000));
            } else {
                int entyid = (int) item.get_rcdate();
                item.set_amount(db.getSum(entyid, bdate, edate + 24 * 60 * 60 * 1000));
                if (entyid == H.TAXS || entyid == H.TOLL) {
                    usum += db.getSum(entyid, (short) 0, bdate, edate + 24 * 60 * 60 * 1000);
                    lsum += db.getSum(entyid, (short) 1, bdate, edate + 24 * 60 * 60 * 1000);
                } else if (entyid == H.UB_PAYOUT) {
                    usum -= db.getSum(entyid, (short) 0, bdate, edate + 24 * 60 * 60 * 1000);
                    lsum -= db.getSum(entyid, (short) 1, bdate, edate + 24 * 60 * 60 * 1000);
                }
            }

            temI.add(null);
            final int jjj = jj;
            if (pb != null) {

                pb.post(new Runnable() {
                    @Override
                    public void run() {
                        pb.setProgress((int) ((jjj / m) * 20.0));
                    }
                });
                percentage.post(new Runnable() {
                    @Override
                    public void run() {
                        percentage.setText((int) ((jjj / m) * 20.0) + "%");
                    }
                });

            }
            jj++;
        }

        int tcount = db.getItemCountByName(H.TRIPDETAIL, (short) -1, bdate, (edate + 24 * 60 * 60 * 1000));
        int ucount = db.getItemCountByName(H.TRIPDETAIL, (short) 0, bdate, (edate + 24 * 60 * 60 * 1000));
        int lcount = db.getItemCountByName(H.TRIPDETAIL, (short) 1, bdate, (edate + 24 * 60 * 60 * 1000));

        Collections.copy(temI, temI0);
        Collections.sort(temI);


        int r = 0, c = 0;
        GridLayout.Spec rowSpan = GridLayout.spec(r);
        GridLayout.Spec colspan = GridLayout.spec(c);
        GridLayout.LayoutParams gridParam = null;

        TextView label = null;
        TextView tv = null;

        int margin = 10;
        int lr = H.getScreenWidth(mainActivity) / 15;

        ArrayList<Item> tem = new ArrayList<>();
        ArrayList<Item> noc = new ArrayList<>();


        gridLayout.setColumnCount(2);
        gridLayout.setRowCount(20 + temI.size() + 2 + 7);
        int i = 0;
        for (r = 0; r < 7; r++) {
            c = 0;
            rowSpan = GridLayout.spec(r);
            colspan = GridLayout.spec(c);
            gridParam = new GridLayout.LayoutParams(
                    rowSpan, colspan);
            gridParam.setMargins(10, 0, lr, margin);

            gridLayout.addView(getLabel(r), gridParam);
            //tincome.setText("" + Math.round(100 * payment) / 100.0);
            tv = getTextView(r);
            c = 1;
            rowSpan = GridLayout.spec(r);
            colspan = GridLayout.spec(c);
            gridParam = new GridLayout.LayoutParams(
                    rowSpan, colspan);
            gridParam.setMargins(lr, 0, 0, margin);
            gridLayout.addView(tv, gridParam);
            final int j = i;
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (pb != null) {
                        pb.setProgress(25 + (int) ((j / 7.0) * 5.0));
                        percentage.setText(25 + (int) ((j / 7.0) * 5.0) + "%");
                    }
                }
            });

            i++;
        }

        //-------Add row count---------
        r++;
        c = 0;
        rowSpan = GridLayout.spec(r);
        colspan = GridLayout.spec(c);
        gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        gridParam.setMargins(10, 0, lr, margin);
        tv = new TextView(mainActivity);
        tv.setText("Total Uber&Lyft Trip:");
        gridLayout.addView(tv, gridParam);
        //tincome.setText("" + Math.round(100 * payment) / 100.0);

        c = 1;
        rowSpan = GridLayout.spec(r);
        colspan = GridLayout.spec(c);
        gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        tv = new TextView(mainActivity);
        tv.setText("" + tcount);
        gridParam.setMargins(lr, 0, 0, margin);
        gridLayout.addView(tv, gridParam);

        r++;
        c = 0;
        rowSpan = GridLayout.spec(r);
        colspan = GridLayout.spec(c);
        gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        gridParam.setMargins(50, 0, lr, margin);
        tv = new TextView(mainActivity);
        tv.setText("Uber:");
        gridLayout.addView(tv, gridParam);
        //tincome.setText("" + Math.round(100 * payment) / 100.0);

        c = 1;
        rowSpan = GridLayout.spec(r);
        colspan = GridLayout.spec(c);
        gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        tv = new TextView(mainActivity);
        tv.setText("" + ucount);
        gridParam.setMargins(lr + 40, 0, 0, margin);
        gridLayout.addView(tv, gridParam);

        r++;
        c = 0;
        rowSpan = GridLayout.spec(r);
        colspan = GridLayout.spec(c);
        gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        gridParam.setMargins(50, 0, lr, margin);
        tv = new TextView(mainActivity);
        tv.setText("Lyft:");
        gridLayout.addView(tv, gridParam);
        //tincome.setText("" + Math.round(100 * payment) / 100.0);

        c = 1;
        rowSpan = GridLayout.spec(r);
        colspan = GridLayout.spec(c);
        gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        tv = new TextView(mainActivity);
        tv.setText("" + lcount);
        gridParam.setMargins(lr + 40, 0, 0, margin);
        gridLayout.addView(tv, gridParam);
        //------------------------------
        r++;
        int lrow = r + 1;
        r = r + 7;

        if(!H.PAID && mainActivity.adsfreetime<=0) {
            rowSpan = GridLayout.spec(r, 3);
            colspan = GridLayout.spec(0, 2);
            final GridLayout.LayoutParams gridParam2 = new GridLayout.LayoutParams(
                    rowSpan, colspan);
            gridParam2.setGravity(Gravity.CENTER_HORIZONTAL);
            final AdView ad = new AdView(mainActivity);
            ad.setAdSize(AdSize.BANNER);
            ad.setAdUnitId(mainActivity.getunitid(0));
            ad.post(new Runnable() {
                @Override
                public void run() {
                    AdRequest adRequest = new AdRequest.Builder().build();
                    ad.loadAd(adRequest);
                }
            });
            gridLayout.post(new Runnable() {
                @Override
                public void run() {
                    gridLayout.addView(ad, gridParam2);
                }
            });
            r = r + 3;
        }

        final double size = temI.size();
        i = 0;
        for (Item item : temI) {
            if (item.get_amount() != 0) {

                if (item.get_date() == H.RECEIVABLE) {
                    c = 0;
                    rowSpan = GridLayout.spec(r);
                    colspan = GridLayout.spec(c);
                    gridParam = new GridLayout.LayoutParams(
                            rowSpan, colspan);
                    gridParam.setMargins(50, 0, lr, margin);

                    label = new TextView(mainActivity);
                    label.setText(item.get_note());
                    label.setTextColor(Color.rgb(0, 100, 0));

                    label.setTag(item);
                    label.setOnLongClickListener(entrylistner);

                    gridLayout.addView(label, gridParam);
                    //tincome.setText("" + Math.round(100 * payment) / 100.0);
                    tv = new TextView(mainActivity);
                    tv.setTextColor(Color.rgb(0, 100, 0));
                    tv.setText(cs + (Math.round(item.get_amount() * 100)) / 100.0);
                    c = 1;
                    rowSpan = GridLayout.spec(r);
                    colspan = GridLayout.spec(c);
                    gridParam = new GridLayout.LayoutParams(
                            rowSpan, colspan);
                    gridParam.setMargins(lr, 0, 0, margin);
                    gridLayout.addView(tv, gridParam);

                    // for single element, no detailed
                    if (item.get_source() == 1 || item.get_source() == 2 || item.get_source() == 4 || item.get_source() == 8) {
                        r++;
                        continue;
                    }

                    r = radiobtnops(gridLayout, item, rowSpan, colspan, gridParam, label, tv,
                            r, 0, margin, lr, db);

                } else if (item.get_date() == H.PAYABLE) {
                    tem.add(item);
                } else if (item.get_date() == H.NONCOUNTABLE) {
                    noc.add(item);
                }
            }

            final int j = i;
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (pb != null && (j % 2) == 0) {
                        pb.setProgress(25 + (int) ((j / size) * 45));
                        percentage.setText(25 + (int) ((j / size) * 45) + "%");
                    }
                }
            });

            i++;
        }

        r += 4;
        if(!H.PAID && mainActivity.adsfreetime<=0) {
            rowSpan = GridLayout.spec(r, 3);
            colspan = GridLayout.spec(0, 2);
            final GridLayout.LayoutParams gridParam3 = new GridLayout.LayoutParams(
                    rowSpan, colspan);
            //gridParam.setMargins(80, 0, 10, 0);
            gridParam3.setGravity(Gravity.CENTER_HORIZONTAL);
            final AdView ad3 = new AdView(mainActivity);
            ad3.setAdSize(AdSize.BANNER);
            ad3.setAdUnitId(mainActivity.getunitid(3));
            ad3.post(new Runnable() {
                @Override
                public void run() {
                    AdRequest adRequest = new AdRequest.Builder().build();
                    ad3.loadAd(adRequest);
                }
            });
            //final int index=i;
            gridLayout.post(new Runnable() {
                @Override
                public void run() {
                    gridLayout.addView(ad3, gridParam3);
                }
            });
            r = r + 3;
        }


        c = 0;
        rowSpan = GridLayout.spec(r);
        colspan = GridLayout.spec(c);
        gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        gridParam.setMargins(50, 0, lr, margin);
        tv = new TextView(mainActivity);
        tv.setText("********Cost********");
        gridLayout.addView(tv, gridParam);
        //tincome.setText("" + Math.round(100 * payment) / 100.0);

        c = 1;
        rowSpan = GridLayout.spec(r);
        colspan = GridLayout.spec(c);
        gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        tv = new TextView(mainActivity);
        tv.setText("*****");
        gridParam.setMargins(lr + 40, 0, 0, margin);
        gridLayout.addView(tv, gridParam);

        r++;
        //r2 = 0;
        i = 0;
        for (Item item : tem) {
            c = 0;
            rowSpan = GridLayout.spec(r);
            colspan = GridLayout.spec(c);
            gridParam = new GridLayout.LayoutParams(
                    rowSpan, colspan);
            gridParam.setMargins(50, 0, lr, margin);

            label = new TextView(mainActivity);
            label.setText(item.get_note());
            label.setTextColor(Color.RED);

            label.setTag(item);
            label.setOnLongClickListener(entrylistner);

            gridLayout.addView(label, gridParam);
            //tincome.setText("" + Math.round(100 * payment) / 100.0);
            tv = new TextView(mainActivity);
            if (item.get_rcdate() == H.INSURANCE) {
                tv.setText(cs + (Math.round(item.get_amount() * inshare)) / 100.0);
            } else if (item.get_rcdate() == H.GAS) {
                tv.setText(cs + (Math.round(item.get_amount() * gasshare)) / 100.0);
            } else if (item.get_rcdate() == H.DATA) {
                tv.setText(cs + (Math.round(item.get_amount() * datashare)) / 100.0);
            } else {
                tv.setText(cs + (Math.round(item.get_amount() * 100)) / 100.0);
            }
            tv.setTextColor(Color.RED);
            c = 1;
            rowSpan = GridLayout.spec(r);
            colspan = GridLayout.spec(c);
            gridParam = new GridLayout.LayoutParams(
                    rowSpan, colspan);
            gridParam.setMargins(lr, 0, 0, margin);
            gridLayout.addView(tv, gridParam);

            if (item.get_source() == 1 || item.get_source() == 2 || item.get_source() == 4 || item.get_source() == 8) {
                r++;
                continue;
            }

            r = radiobtnops(gridLayout, item, rowSpan, colspan, gridParam, label, tv,
                    r, 0, margin, lr, db);

            final int j = i;
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (pb != null && (j % 2) == 0) {
                        pb.setProgress(70 + (int) ((j / size) * 15));
                        percentage.setText(70 + (int) ((j / size) * 15) + "%");
                    }
                }
            });

            i++;
        }
        r++;


        //-------For next saving---------
        c = 0;
        rowSpan = GridLayout.spec(r);
        colspan = GridLayout.spec(c);
        gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        gridParam.setMargins(50, 0, lr, margin);

        label = new TextView(mainActivity);
        label.setText("Next D.Saving:(Not\nincluded in Cost)");
        label.setTextColor(Color.parseColor("#94024B"));
        gridLayout.addView(label, gridParam);


        tv = new TextView(mainActivity);
        double kilometer = db.getMileageSum(bdate, edate + 24 * 60 * 60 * 1000);
        double toSave = (kilometer) * drate - db.getSum(H.DSAVING);
        tv.setText(cs + Math.round(toSave * 100) / 100.0);
        tv.setTextColor(Color.parseColor("#94024B"));
        c = 1;
        rowSpan = GridLayout.spec(r);
        colspan = GridLayout.spec(c);
        gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        gridParam.setMargins(lr, 0, 0, margin);
        gridLayout.addView(tv, gridParam);

        //------End of next saving------
        r++;
        if (!H.PAID && mainActivity.adsfreetime<=0) {
            rowSpan = GridLayout.spec(r, 3);
            colspan = GridLayout.spec(0, 2);
            final GridLayout.LayoutParams gridParam5 = new GridLayout.LayoutParams(
                    rowSpan, colspan);
            //gridParam.setMargins(80, 0, 10, 0);
            gridParam5.setGravity(Gravity.CENTER_HORIZONTAL);
            final AdView ad5 = new AdView(mainActivity);
            ad5.setAdSize(AdSize.BANNER);
            ad5.setAdUnitId(mainActivity.getunitid(5));
            ad5.post(new Runnable() {
                @Override
                public void run() {
                    AdRequest adRequest = new AdRequest.Builder().build();
                    ad5.loadAd(adRequest);
                }
            });
            //final int index=i;
            gridLayout.post(new Runnable() {
                @Override
                public void run() {
                    gridLayout.addView(ad5, gridParam5);
                }
            });
            r = r + 3;
        }


        c = 0;
        rowSpan = GridLayout.spec(r);
        colspan = GridLayout.spec(c);
        gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        gridParam.setMargins(50, 0, 0, margin);

        i = 0;
        for (Item item : noc) {
            if (item.get_rcdate() == H.DSETTING || item.get_rcdate() == H.SUMMARY) {
                continue;
            }
            c = 0;
            rowSpan = GridLayout.spec(r);
            colspan = GridLayout.spec(c);
            gridParam = new GridLayout.LayoutParams(
                    rowSpan, colspan);
            gridParam.setMargins(10, 0, lr, margin);

            label = new TextView(mainActivity);
            if (item.get_rcdate() == H.MILEAGE) {
                label.setText((unit.equalsIgnoreCase("metric") ? "KMs " : "Miles ") + "Driven:");
            } else {

                if (item.get_rcdate() == H.TRIPDETAIL) {
                    label.setText("[TRIP DURATION]:");
                } else {
                    label.setText(item.get_note());
                }
            }

            label.setTag(item);
            label.setOnLongClickListener(entrylistner);

            gridLayout.addView(label, gridParam);
            //tincome.setText("" + Math.round(100 * payment) / 100.0);
            tv = new TextView(mainActivity);
            if (item.get_rcdate() == H.MILEAGE || item.get_rcdate() == H.DISTANCE) {
                tv.setText((int) (Math.round(item.get_amount())) + (unit.equalsIgnoreCase("metric") ? " KMs " : " Miles "));
            } else if (item.get_rcdate() == H.ONLINEHRS || item.get_rcdate() == H.TRIPHRS || item.get_rcdate() == H.TRIPDETAIL || item.get_rcdate() == H.PREPARETIME || item.get_rcdate() == H.WK_ONLINEHRS) {
                tv.setText((Math.round(item.get_amount() * 100)) / 100.0 + " hrs");
            } else {
                tv.setText(cs + (Math.round(item.get_amount() * 100)) / 100.0);
            }

            if (H.checkslyft(item.get_rcdate())) {
                label.setTextColor(Color.rgb(0, 0, 150));
                tv.setTextColor(Color.rgb(0, 0, 150));
            } else if (item.get_rcdate() == H.TAXS || item.get_rcdate() == H.TOLL) {
                label.setTextColor(Color.rgb(255, 0, 255));
                tv.setTextColor(Color.rgb(255, 0, 255));
                tempp = tempp + (Math.round(item.get_amount() * 100)) / 100.0;
                difn = difn + (Math.round(item.get_amount() * 100)) / 100.0;
            }

            if (item.get_rcdate() == H.UB_PAYOUT) {
                tempp = tempp - (Math.round(item.get_amount() * 100)) / 100.0;
                difn = difn - (Math.round(item.get_amount() * 100)) / 100.0;
                difu += usum;
                difl += lsum;
            }

            c = 1;
            rowSpan = GridLayout.spec(r);
            colspan = GridLayout.spec(c);
            gridParam = new GridLayout.LayoutParams(
                    rowSpan, colspan);
            gridParam.setMargins(lr, 0, 0, margin);
            gridLayout.addView(tv, gridParam);

            if (item.get_source() == 1 || item.get_source() == 2 || item.get_source() == 4 || item.get_source() == 8) {
                r++;
                continue;
            }

            r = radiobtnops(gridLayout, item, rowSpan, colspan, gridParam, label, tv,
                    r, 0, margin, lr, db);

            final int j = i;
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (pb != null && (j % 2) == 0) {
                        pb.setProgress(85 + (int) ((j / size) * 15));
                        percentage.setText(85 + (int) ((j / size) * 15) + "%");
                    }
                }
            });

            i++;
        }


        r = lrow;
        //-------Add difference---------
        //r++;
        c = 0;
        rowSpan = GridLayout.spec(r);
        colspan = GridLayout.spec(c);
        gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        gridParam.setMargins(10, 0, lr, margin);
        tv = new TextView(mainActivity);
        tv.setText("Payout Difference:");
        gridLayout.addView(tv, gridParam);
        //tincome.setText("" + Math.round(100 * payment) / 100.0);

        c = 1;
        rowSpan = GridLayout.spec(r);
        colspan = GridLayout.spec(c);
        gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        tv = new TextView(mainActivity);
        tv.setText("" + H.round(0 - difn));
        if (difn <= 0) {
            tv.setTextColor(Color.rgb(0, 100, 0));
        } else {
            tv.setTextColor(Color.rgb(225, 0, 0));
        }
        gridParam.setMargins(lr, 0, 0, margin);
        gridLayout.addView(tv, gridParam);

        r++;
        c = 0;
        rowSpan = GridLayout.spec(r);
        colspan = GridLayout.spec(c);
        gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        gridParam.setMargins(50, 0, lr, margin);
        tv = new TextView(mainActivity);
        tv.setText("Uber:");
        gridLayout.addView(tv, gridParam);
        //tincome.setText("" + Math.round(100 * payment) / 100.0);

        c = 1;
        rowSpan = GridLayout.spec(r);
        colspan = GridLayout.spec(c);
        gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        tv = new TextView(mainActivity);
        tv.setText("" + H.round(0 - difu));
        if (difu <= 0) {
            tv.setTextColor(Color.rgb(0, 100, 0));
        } else {
            tv.setTextColor(Color.rgb(225, 0, 0));
        }
        gridParam.setMargins(lr + 40, 0, 0, margin);
        gridLayout.addView(tv, gridParam);

        r++;
        c = 0;
        rowSpan = GridLayout.spec(r);
        colspan = GridLayout.spec(c);
        gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        gridParam.setMargins(50, 0, lr, margin);
        tv = new TextView(mainActivity);
        tv.setText("Lyft:");
        gridLayout.addView(tv, gridParam);
        //tincome.setText("" + Math.round(100 * payment) / 100.0);

        c = 1;
        rowSpan = GridLayout.spec(r);
        colspan = GridLayout.spec(c);
        gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        tv = new TextView(mainActivity);
        tv.setText("" + H.round(0 - difl));
        if (difl <= 0) {
            tv.setTextColor(Color.rgb(0, 100, 0));
        } else {
            tv.setTextColor(Color.rgb(225, 0, 0));
        }
        gridParam.setMargins(lr + 40, 0, 0, margin);
        gridLayout.addView(tv, gridParam);

        r += 2;
        c = 0;
        rowSpan = GridLayout.spec(r);
        colspan = GridLayout.spec(c);
        gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        gridParam.setMargins(50, 0, lr, margin);
        tv = new TextView(mainActivity);
        tv.setText("********Income********");
        gridLayout.addView(tv, gridParam);
        //tincome.setText("" + Math.round(100 * payment) / 100.0);

        c = 1;
        rowSpan = GridLayout.spec(r);
        colspan = GridLayout.spec(c);
        gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        tv = new TextView(mainActivity);
        tv.setText("*****");
        gridParam.setMargins(lr + 40, 0, 0, margin);
        gridLayout.addView(tv, gridParam);
        //------------------------------

        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (pb != null) {
                    pb.setProgress(100);
                    percentage.setText("100%");
                }

            }
        });


    }

    private View.OnLongClickListener entrylistner = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            Item item = (Item) view.getTag();
            mainActivity.previousentry = entryid;
            EntryFragment fragment = EntryFragment.newInstance(item.get_rcdate());
            mainActivity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
            return true;
        }
    };


    private void summary_land(final GridLayout gridLayout, final ProgressBar pb, final TextView percentage) {
        DatabaseHandler db = DatabaseHandler.getInstance(mainActivity);
        ArrayList<Item> temI0 = db.getItemsByName(H.SETTINGKEY, H.DESC, -1, 100000);
        ArrayList<Item> temI = new ArrayList<>();

        final int m = temI0.size();
        int jj = 0;
        double usum = 0, lsum = 0;
        for (Item item : temI0) {
            if (item.get_rcdate() == H.MILEAGE) {
                item.set_amount(db.getMileageSum(bdate, edate + 24 * 60 * 60 * 1000));
            } else {
                int entyid = (int) item.get_rcdate();
                item.set_amount(db.getSum(entyid, bdate, edate + 24 * 60 * 60 * 1000));
                if (entyid == H.TAXS || entyid == H.TOLL) {
                    usum += db.getSum(entyid, (short) 0, bdate, edate + 24 * 60 * 60 * 1000);
                    lsum += db.getSum(entyid, (short) 1, bdate, edate + 24 * 60 * 60 * 1000);
                } else if (entyid == H.UB_PAYOUT) {
                    usum -= db.getSum(entyid, (short) 0, bdate, edate + 24 * 60 * 60 * 1000);
                    lsum -= db.getSum(entyid, (short) 1, bdate, edate + 24 * 60 * 60 * 1000);
                }
            }

            final int jjj = jj;
            percentage.post(new Runnable() {
                @Override
                public void run() {
                    pb.setProgress((int) ((jjj / m) * 20.0));
                    percentage.setText((int) ((jjj / m) * 20.0) + "%");
                }
            });
            temI.add(null);
            jj++;

        }

        int tcount = db.getItemCountByName(H.TRIPDETAIL, (short) -1, bdate, (edate + 24 * 60 * 60 * 1000));
        int ucount = db.getItemCountByName(H.TRIPDETAIL, (short) 0, bdate, (edate + 24 * 60 * 60 * 1000));
        int lcount = db.getItemCountByName(H.TRIPDETAIL, (short) 1, bdate, (edate + 24 * 60 * 60 * 1000));


        Collections.copy(temI, temI0);
        Collections.sort(temI);

        int r = 0, c = 0;
        GridLayout.Spec rowSpan = GridLayout.spec(r);
        GridLayout.Spec colspan = GridLayout.spec(c);
        GridLayout.LayoutParams gridParam = null;

        TextView label = null;
        TextView tv = null;

        int margin = 10;// (H.getScreenWidth(mainActivity) / 200);

        ArrayList<Item> tem = new ArrayList<>();
        ArrayList<Item> noc = new ArrayList<>();


        gridLayout.setColumnCount(6);
        gridLayout.setRowCount(7 + temI.size() + 2);
        int i = 0;
        for (r = 0; r < 7; r++) {
            c = 0;
            rowSpan = GridLayout.spec(r);
            colspan = GridLayout.spec(c);
            gridParam = new GridLayout.LayoutParams(
                    rowSpan, colspan);
            gridParam.setMargins(10, 0, 0, margin);

            gridLayout.addView(getLabel(r), gridParam);
            //tincome.setText("" + Math.round(100 * payment) / 100.0);
            tv = getTextView(r);
            c = 1;
            rowSpan = GridLayout.spec(r);
            colspan = GridLayout.spec(c);
            gridParam = new GridLayout.LayoutParams(
                    rowSpan, colspan);
            gridParam.setMargins(margin * 2, 0, 0, margin);
            gridLayout.addView(tv, gridParam);
            final int j = i;
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (pb != null) {
                        pb.setProgress(20 + (int) ((j / 7.0) * 5.0));
                        percentage.setText(20 + (int) ((j / 7.0) * 5.0) + "%");
                    }
                }
            });

            i++;
        }

        //-------Add row count---------
        c = 0;
        rowSpan = GridLayout.spec(r);
        colspan = GridLayout.spec(c);
        gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        gridParam.setMargins(10, 0, 0, margin);
        tv = new TextView(mainActivity);
        tv.setText("Total Uber&Lyft Trip:");
        gridLayout.addView(tv, gridParam);
        //tincome.setText("" + Math.round(100 * payment) / 100.0);

        c = 1;
        rowSpan = GridLayout.spec(r);
        colspan = GridLayout.spec(c);
        gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        tv = new TextView(mainActivity);
        tv.setText("" + tcount);
        gridParam.setMargins(margin * 2, 0, 0, margin);
        gridLayout.addView(tv, gridParam);

        r++;
        c = 0;
        rowSpan = GridLayout.spec(r);
        colspan = GridLayout.spec(c);
        gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        gridParam.setMargins(50, 0, 0, margin);
        tv = new TextView(mainActivity);
        tv.setText("Uber:");
        gridLayout.addView(tv, gridParam);
        //tincome.setText("" + Math.round(100 * payment) / 100.0);

        c = 1;
        rowSpan = GridLayout.spec(r);
        colspan = GridLayout.spec(c);
        gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        tv = new TextView(mainActivity);
        tv.setText("" + ucount);
        gridParam.setMargins(margin * 2 + 40, 0, 0, margin);
        gridLayout.addView(tv, gridParam);

        r++;
        c = 0;
        rowSpan = GridLayout.spec(r);
        colspan = GridLayout.spec(c);
        gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        gridParam.setMargins(50, 0, 0, margin);
        tv = new TextView(mainActivity);
        tv.setText("Lyft:");
        gridLayout.addView(tv, gridParam);
        //tincome.setText("" + Math.round(100 * payment) / 100.0);

        c = 1;
        rowSpan = GridLayout.spec(r);
        colspan = GridLayout.spec(c);
        gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        tv = new TextView(mainActivity);
        tv.setText("" + lcount);
        gridParam.setMargins(margin * 2 + 40, 0, 0, margin);
        gridLayout.addView(tv, gridParam);
        //------------------------------
        r++;
        int lrow = r + 1;
        r = r + 7;


        // For Receivale
        int r2 = 0;
        final double size = temI.size();
        i = 0;
        for (Item item : temI) {
            if (item.get_amount() != 0) {
                if (item.get_date() == H.RECEIVABLE) {
                    c = 2;
                    rowSpan = GridLayout.spec(r2);
                    colspan = GridLayout.spec(c);
                    gridParam = new GridLayout.LayoutParams(
                            rowSpan, colspan);
                    gridParam.setMargins(50, 0, 0, margin);

                    label = new TextView(mainActivity);
                    label.setText(item.get_note());
                    label.setTextColor(Color.rgb(0, 100, 0));
                    label.setTag(item);
                    label.setOnLongClickListener(entrylistner);
                    gridLayout.addView(label, gridParam);
                    //tincome.setText("" + Math.round(100 * payment) / 100.0);
                    tv = new TextView(mainActivity);
                    tv.setTextColor(Color.rgb(0, 100, 0));
                    tv.setText(cs + (Math.round(item.get_amount() * 100)) / 100.0);
                    c = 3;
                    rowSpan = GridLayout.spec(r2);
                    colspan = GridLayout.spec(c);
                    gridParam = new GridLayout.LayoutParams(
                            rowSpan, colspan);
                    gridParam.setMargins(margin * 2, 0, 0, margin);
                    gridLayout.addView(tv, gridParam);

                    // for single element, no detailed
                    if (item.get_source() == 1 || item.get_source() == 2 || item.get_source() == 4 || item.get_source() == 8) {
                        r2++;
                        continue;
                    }

                    r2 = radiobtnops(gridLayout, item, rowSpan, colspan, gridParam, label, tv,
                            r2, 2, margin, margin, db);

                } else if (item.get_date() == H.PAYABLE) {
                    tem.add(item);
                } else if (item.get_date() == H.NONCOUNTABLE) {
                    noc.add(item);
                }
            }

            final int j = i;
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (pb != null && (j % 2) == 0) {
                        pb.setProgress(25 + (int) ((j / size) * 45));
                        percentage.setText(25 + (int) ((j / size) * 45) + "%");
                    }
                }
            });
            i++;
        }


        r2 = 0;
        i = 0;
        for (Item item : tem) {
            c = 4;
            rowSpan = GridLayout.spec(r2);
            colspan = GridLayout.spec(c);
            gridParam = new GridLayout.LayoutParams(
                    rowSpan, colspan);
            gridParam.setMargins(50, 0, 0, margin);

            label = new TextView(mainActivity);
            label.setText(item.get_note());
            label.setTextColor(Color.RED);
            label.setTag(item);
            label.setOnLongClickListener(entrylistner);
            gridLayout.addView(label, gridParam);
            //tincome.setText("" + Math.round(100 * payment) / 100.0);
            tv = new TextView(mainActivity);
            if (item.get_rcdate() == H.INSURANCE) {
                tv.setText(cs + (Math.round(item.get_amount() * inshare)) / 100.0);
            } else if (item.get_rcdate() == H.GAS) {
                tv.setText(cs + (Math.round(item.get_amount() * gasshare)) / 100.0);
            } else if (item.get_rcdate() == H.DATA) {
                tv.setText(cs + (Math.round(item.get_amount() * datashare)) / 100.0);
            } else {
                tv.setText(cs + (Math.round(item.get_amount() * 100)) / 100.0);
            }
            tv.setTextColor(Color.RED);
            c = 5;
            rowSpan = GridLayout.spec(r2);
            colspan = GridLayout.spec(c);
            gridParam = new GridLayout.LayoutParams(
                    rowSpan, colspan);
            gridParam.setMargins(margin * 2, 0, 0, margin);
            gridLayout.addView(tv, gridParam);

            if (item.get_source() == 1 || item.get_source() == 2 || item.get_source() == 4 || item.get_source() == 8) {
                r2++;
                continue;
            }

            r2 = radiobtnops(gridLayout, item, rowSpan, colspan, gridParam, label, tv,
                    r2, 4, margin, margin, db);
            //r2++;
            final int j = i;
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (pb != null && (j % 2) == 0) {
                        pb.setProgress(70 + (int) ((j / size) * 15));
                        percentage.setText(70 + (int) ((j / size) * 15) + "%");
                    }
                }
            });
            i++;
        }
        r2++;


        //-------For next saving---------
        c = 4;
        rowSpan = GridLayout.spec(r2);
        colspan = GridLayout.spec(c);
        gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        gridParam.setMargins(50, 0, 0, margin);

        label = new TextView(mainActivity);
        label.setText("Next D.Saving:(Not\nincluded in Cost)");
        label.setTextColor(Color.parseColor("#94024B"));
        gridLayout.addView(label, gridParam);


        tv = new TextView(mainActivity);
        double kilometer = db.getMileageSum(bdate, edate + 24 * 60 * 60 * 1000);
        double toSave = (kilometer) * drate - db.getSum(H.DSAVING);
        tv.setText(cs + Math.round(toSave * 100) / 100.0);
        tv.setTextColor(Color.parseColor("#94024B"));
        c = 5;
        rowSpan = GridLayout.spec(r2);
        colspan = GridLayout.spec(c);
        gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        gridParam.setMargins(margin * 2, 0, 0, margin);
        gridLayout.addView(tv, gridParam);

        //------End of next saving------

        if (!H.PAID && mainActivity.adsfreetime<=0) {
            rowSpan = GridLayout.spec(r, 3);
            colspan = GridLayout.spec(0, 6);
            final GridLayout.LayoutParams gridParam3 = new GridLayout.LayoutParams(
                    rowSpan, colspan);
            //gridParam.setMargins(80, 0, 10, 0);
            gridParam3.setGravity(Gravity.CENTER_HORIZONTAL);
            final LinearLayout ll = new LinearLayout(mainActivity);
            final AdView ad3 = new AdView(mainActivity);
            ad3.setAdSize(AdSize.BANNER);
            ad3.setAdUnitId(mainActivity.getunitid(3));
            ad3.post(new Runnable() {
                @Override
                public void run() {
                    AdRequest adRequest = new AdRequest.Builder().build();
                    ad3.loadAd(adRequest);
                }
            });


            final AdView ad = new AdView(mainActivity);
            ad.setAdSize(AdSize.BANNER);
            ad.setAdUnitId(mainActivity.getunitid(0));
            ad.post(new Runnable() {
                @Override
                public void run() {
                    AdRequest adRequest = new AdRequest.Builder().build();
                    ad.loadAd(adRequest);
                }
            });
            gridLayout.post(new Runnable() {
                @Override
                public void run() {
                    ll.addView(ad);
                    ll.addView(ad3);
                    gridLayout.addView(ll, gridParam3);
                }
            });
            r = r + 3;
        }

        i = 0;
        for (Item item : noc) {
            if (item.get_rcdate() == H.DSETTING || item.get_rcdate() == H.SUMMARY) {
                continue;
            }
            c = 0;
            rowSpan = GridLayout.spec(r);
            colspan = GridLayout.spec(c);
            gridParam = new GridLayout.LayoutParams(
                    rowSpan, colspan);
            gridParam.setMargins(10, 0, 0, margin);

            label = new TextView(mainActivity);
            if (item.get_rcdate() == H.MILEAGE) {
                label.setText((unit.equalsIgnoreCase("metric") ? "KMs " : "Miles ") + "Driven:");
            } else {
                if (item.get_rcdate() == H.TRIPDETAIL) {
                    label.setText("[TRIP DURATION]:");
                } else {
                    label.setText(item.get_note());
                }
            }
            label.setTag(item);
            label.setOnLongClickListener(entrylistner);

            gridLayout.addView(label, gridParam);
            //tincome.setText("" + Math.round(100 * payment) / 100.0);
            tv = new TextView(mainActivity);
            if (item.get_rcdate() == H.MILEAGE || item.get_rcdate() == H.DISTANCE) {
                tv.setText((int) (Math.round(item.get_amount())) + (unit.equalsIgnoreCase("metric") ? " KMs " : " Miles "));
            } else if (item.get_rcdate() == H.ONLINEHRS || item.get_rcdate() == H.TRIPHRS || item.get_rcdate() == H.TRIPDETAIL || item.get_rcdate() == H.PREPARETIME || item.get_rcdate() == H.WK_ONLINEHRS) {
                tv.setText((Math.round(item.get_amount() * 100)) / 100.0 + " hrs");
            } else {
                tv.setText(cs + (Math.round(item.get_amount() * 100)) / 100.0);
            }

            if (H.checkslyft(item.get_rcdate())) {
                label.setTextColor(Color.rgb(0, 0, 150));
                tv.setTextColor(Color.rgb(0, 0, 150));
            } else if (item.get_rcdate() == H.TAXS || item.get_rcdate() == H.TOLL) {
                label.setTextColor(Color.rgb(255, 0, 255));
                tv.setTextColor(Color.rgb(255, 0, 255));
                tempp = tempp + (Math.round(item.get_amount() * 100)) / 100.0;
                difn = difn + (Math.round(item.get_amount() * 100)) / 100.0;
            }

            if (item.get_rcdate() == H.UB_PAYOUT) {
                tempp = tempp - (Math.round(item.get_amount() * 100)) / 100.0;
                difn = difn - (Math.round(item.get_amount() * 100)) / 100.0;
                difu += usum;
                difl += lsum;
            }

            c = 1;
            rowSpan = GridLayout.spec(r);
            colspan = GridLayout.spec(c);
            gridParam = new GridLayout.LayoutParams(
                    rowSpan, colspan);
            gridParam.setMargins(margin * 2, 0, 0, margin);
            gridLayout.addView(tv, gridParam);

            if (item.get_source() == 1 || item.get_source() == 2 || item.get_source() == 4 || item.get_source() == 8) {
                r++;
                continue;
            }

            r = radiobtnops(gridLayout, item, rowSpan, colspan, gridParam, label, tv,
                    r, 0, margin, margin, db);
            final int j = i;
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (pb != null && (j % 2) == 0) {
                        pb.setProgress(85 + (int) ((j / size) * 15));
                        percentage.setText(85 + (int) ((j / size) * 15) + "%");
                    }
                }
            });
            i++;
        }

        r = lrow;
        //-------Add difference---------
        //r++;
        c = 0;
        rowSpan = GridLayout.spec(r);
        colspan = GridLayout.spec(c);
        gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        gridParam.setMargins(10, 0, margin * 2, margin);
        tv = new TextView(mainActivity);
        tv.setText("Payout Difference:");
        gridLayout.addView(tv, gridParam);
        //tincome.setText("" + Math.round(100 * payment) / 100.0);

        c = 1;
        rowSpan = GridLayout.spec(r);
        colspan = GridLayout.spec(c);
        gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        tv = new TextView(mainActivity);
        tv.setText("" + H.round(0 - difn));
        if (difn <= 0) {
            tv.setTextColor(Color.rgb(0, 100, 0));
        } else {
            tv.setTextColor(Color.rgb(225, 0, 0));
        }
        gridParam.setMargins(margin * 2, 0, 0, margin);
        gridLayout.addView(tv, gridParam);

        r++;
        c = 0;
        rowSpan = GridLayout.spec(r);
        colspan = GridLayout.spec(c);
        gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        gridParam.setMargins(50, 0, margin * 2, margin);
        tv = new TextView(mainActivity);
        tv.setText("Uber:");
        gridLayout.addView(tv, gridParam);
        //tincome.setText("" + Math.round(100 * payment) / 100.0);

        c = 1;
        rowSpan = GridLayout.spec(r);
        colspan = GridLayout.spec(c);
        gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        tv = new TextView(mainActivity);
        tv.setText("" + H.round(0 - difu));
        if (difu <= 0) {
            tv.setTextColor(Color.rgb(0, 100, 0));
        } else {
            tv.setTextColor(Color.rgb(225, 0, 0));
        }
        gridParam.setMargins(margin * 2 + 40, 0, 0, margin);
        gridLayout.addView(tv, gridParam);

        r++;
        c = 0;
        rowSpan = GridLayout.spec(r);
        colspan = GridLayout.spec(c);
        gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        gridParam.setMargins(50, 0, margin * 2, margin);
        tv = new TextView(mainActivity);
        tv.setText("Lyft:");
        gridLayout.addView(tv, gridParam);
        //tincome.setText("" + Math.round(100 * payment) / 100.0);

        c = 1;
        rowSpan = GridLayout.spec(r);
        colspan = GridLayout.spec(c);
        gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        tv = new TextView(mainActivity);
        tv.setText("" + H.round(0 - difl));
        if (difl <= 0) {
            tv.setTextColor(Color.rgb(0, 100, 0));
        } else {
            tv.setTextColor(Color.rgb(225, 0, 0));
        }
        gridParam.setMargins(margin * 2 + 40, 0, 0, margin);
        gridLayout.addView(tv, gridParam);

        r += 2;


        //------------------------------


        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (pb != null) {
                    pb.setProgress(100);
                    percentage.setText("100%");
                }
            }
        });
    }

    private int radiobtnops(GridLayout gridLayout, Item item, GridLayout.Spec rowSpan,
                            GridLayout.Spec colspan, GridLayout.LayoutParams gridParam,
                            TextView label, TextView tv, int r2, int c, int margin, int lr, DatabaseHandler db) {


        String str = Integer.toBinaryString(item.get_source());
        // str must be 4 digits. if less than 4 digit, "0" must be added
        int fl = 4 - str.length();
        for (int i = 0; i < fl; i++) {
            str = "0" + str;
        }
        int cc = c;


        for (int j = 0; j < 4; j++) {
            if (str.charAt(j) == '0') {
                continue;
            }
            double tsum = -1;
            if (item.get_rcdate() == H.MILEAGE) {
                tsum = Math.round(db.getMileageSum((short) j, bdate, edate + 24l * 60l * 60l * 1000l));
            } else if (item.get_rcdate() == H.TRIPHRS || item.get_rcdate() == H.ONLINEHRS || item.get_rcdate() == H.TRIPDETAIL || item.get_rcdate() == H.PREPARETIME) {
                tsum = Math.round(db.getSum((int) item.get_rcdate(), (short) j, bdate, edate + 24l * 60l * 60l * 1000l) * 100) / 100.0;
            } else {
                tsum = (Math.round(db.getSum((int) item.get_rcdate(), (short) j, bdate, edate + 24 * 60 * 60 * 1000) * 100) / 100.0);
                //tv.setText(cs + (Math.round(db.getSum((int) item.get_rcdate(), (short) j, bdate, edate + 24 * 60 * 60 * 1000) * 100) / 100.0));
            }

            if (tsum < 0.00000001) {
                continue;
            }


            gridLayout.setRowCount(gridLayout.getRowCount() + 1);
            r2++;
            cc = c;
            rowSpan = GridLayout.spec(r2);
            colspan = GridLayout.spec(cc);
            gridParam = new GridLayout.LayoutParams(
                    rowSpan, colspan);
            gridParam.setMargins(100, 0, lr, margin);
            //gridParam.set

            label = new TextView(mainActivity);
            label.setText(H.getSourceName((short) j));
            gridLayout.addView(label, gridParam);
            //tincome.setText("" + Math.round(100 * payment) / 100.0);


            tv = new TextView(mainActivity);
            if (item.get_date() == H.PAYABLE) {
                label.setTextColor(Color.rgb(200, 0, 0));
                tv.setTextColor(Color.rgb(200, 0, 0));
            } else if (item.get_date() == H.RECEIVABLE) {
                label.setTextColor(Color.rgb(0, 200, 0));
                tv.setTextColor(Color.rgb(0, 200, 0));
            } else {
                label.setTextColor(Color.BLACK);
                tv.setTextColor(Color.BLACK);
            }
            if (item.get_rcdate() == H.MILEAGE || item.get_rcdate() == H.DISTANCE) {
                tv.setText("    " + (int) (tsum) + " " + (unit.equalsIgnoreCase("metric") ? "KMs " : "Miles "));
            } else if (item.get_rcdate() == H.TRIPHRS || item.get_rcdate() == H.ONLINEHRS || item.get_rcdate() == H.TRIPDETAIL || item.get_rcdate() == H.PREPARETIME || item.get_rcdate() == H.WK_ONLINEHRS) {
                tv.setText("    " + tsum + " hrs");
            } else {
                tv.setText("    " + cs + tsum);
            }
            cc++;
            rowSpan = GridLayout.spec(r2);
            colspan = GridLayout.spec(cc);
            gridParam = new GridLayout.LayoutParams(
                    rowSpan, colspan);
            gridParam.setMargins(lr, 0, 0, margin);
            gridLayout.addView(tv, gridParam);

            if ((item.get_rcdate() == H.DISTANCE || item.get_rcdate() == H.UB_PAYOUT ||
                    item.get_rcdate() == H.TRIPDETAIL || item.get_rcdate() == H.PREPARETIME) && H.getSourceName((short) j).equalsIgnoreCase("lyft")) {
                label.setTextColor(Color.rgb(0, 0, 150));
                tv.setTextColor(Color.rgb(0, 0, 150));
            }
        }

        r2++;

        return r2;
    }

    private TextView getLabel(int r) {

        switch (r) {
            case 0:
                TextView tv = new TextView(mainActivity);
                //(" + (sdf.format(new Date(bdate)) + "-" + (sdf.format(new Date(edate)))) + ")
                tv.setText("Revenue:");
                return tv;
            case 1:
                tv = new TextView(mainActivity);
                tv.setText("Operational Cost:");
                return tv;
            case 2:
                tv = new TextView(mainActivity);
                //tv.setText("Profit(minus(cost+D.Saving)):");
                tv.setText("Profit:");
                return tv;
            case 3:
                tv = new TextView(mainActivity);
                tv.setText("Hourly Profit:");
                return tv;
            case 4:
                tv = new TextView(mainActivity);
                tv.setText("Hourly Trip Profit:");
                return tv;
            case 5:
                tv = new TextView(mainActivity);
                tv.setText("Time Efficency(%):");
                return tv;
            case 6:
                tv = new TextView(mainActivity);
                tv.setText("Profit/per " + (unit.equalsIgnoreCase("metric") ? "km " : "mile "));
                //tv.setTextColor(Color.parseColor("#94024B"));
                return tv;

            case 12:
                if (unit.equalsIgnoreCase("metric")) {
                    tv = new TextView(mainActivity);
                    tv.setText("Total Kilometers:");
                    return tv;
                } else {
                    tv = new TextView(mainActivity);
                    tv.setText("Total Miles:");
                    return tv;
                }
            default:

                tv = new TextView(mainActivity);
                tv.setText(":");
                return tv;

        }


    }

    private TextView getTextView(int r) {


        DatabaseHandler db = DatabaseHandler.getInstance(mainActivity);
        ArrayList<Item> allitems = db.getItemsByName(H.SETTINGKEY, H.DESC, -1, 100000);
        double receivable = 0, receivablen = 0, receivableu = 0, receivablel = 0;
        double payable = 0, payablen = 0, payableu = 0, payablel = 0;
        for (Item item : allitems) {
            if (item.get_date() == H.RECEIVABLE) {
                double lsum = db.getSum((int) item.get_rcdate(), bdate, edate + 24 * 60 * 60 * 1000);
                receivable += lsum;
                if (H.countcheck(item.get_rcdate())) {
                    receivablen += lsum;
                    receivableu += db.getSum((int) item.get_rcdate(), (short) 0, bdate, edate + 24 * 60 * 60 * 1000);

                    if (item.get_rcdate() == H.WK_EARNING) {
                        receivablel += db.getSum((int) item.get_rcdate(), bdate, edate + 24 * 60 * 60 * 1000);
                    } else {
                        receivablel += db.getSum((int) item.get_rcdate(), (short) 1, bdate, edate + 24 * 60 * 60 * 1000);
                    }
                }

            } else if (item.get_date() == H.PAYABLE) {
                double tem = db.getSum((int) item.get_rcdate(), bdate, edate + 24 * 60 * 60 * 1000);
                if (item.get_rcdate() == H.INSURANCE) {
                    tem = tem * inshare / 100;
                } else if (item.get_rcdate() == H.GAS) {
                    tem = tem * gasshare / 100;
                } else if (item.get_rcdate() == H.DATA) {
                    tem = tem * datashare / 100;
                }
                payable += tem;

                if (H.countcheck(item.get_rcdate())) {
                    payablen += tem;
                    payableu += db.getSum((int) item.get_rcdate(), (short) 0, bdate, edate + 24 * 60 * 60 * 1000);
                    payablel += db.getSum((int) item.get_rcdate(), (short) 1, bdate, edate + 24 * 60 * 60 * 1000);

                }

            }

        }

        switch (r) {
            case 0:
                TextView tv = new TextView(mainActivity);
                tv.setText(cs + Math.round(receivable * 100) / 100.0);
                tv.setTextColor(Color.rgb(0, 200, 0));
                return tv;
            case 1:
                tv = new TextView(mainActivity);
                tv.setText(cs + Math.round((payable) * 100) / 100.0);
                tv.setTextColor(Color.rgb(200, 0, 0));

                return tv;
            case 2:
                tv = new TextView(mainActivity);
                tempp = Math.round((receivable - payable) * 100) / 100.0;
                difn = Math.round((receivablen - payablen) * 100) / 100.0;
                difu = Math.round((receivableu - payableu) * 100) / 100.0;
                difl = Math.round((receivablel - payablel) * 100) / 100.0;
                tv.setText(cs + Math.round((receivable - payable) * 100) / 100.0);
                if ((receivable - payable) >= 0) {
                    tv.setTextColor(Color.rgb(0, 200, 0));
                } else {
                    tv.setTextColor(Color.rgb(200, 0, 0));
                }
                return tv;

            case 3:
                tv = new TextView(mainActivity);
                double onlinehrs = db.getSum(H.ONLINEHRS, bdate, edate + 24 * 60 * 60 * 1000) +
                        db.getSum(H.WK_ONLINEHRS, bdate, edate + 24 * 60 * 60 * 1000);
                if (onlinehrs == 0) {
                    tv.setText("NA");
                } else {
                    tv.setText(cs + Math.round((receivable - payable) / onlinehrs * 100) / 100.0);
                }

                if ((receivable - payable) >= 0) {
                    tv.setTextColor(Color.rgb(0, 100, 0));
                } else {
                    tv.setTextColor(Color.rgb(100, 0, 0));
                }

                return tv;
            case 4:
                tv = new TextView(mainActivity);
                double triphrs = db.getSum(H.TRIPHRS, bdate, edate + 24 * 60 * 60 * 1000) +
                        db.getSum(H.TRIPDETAIL, bdate, edate + 24 * 60 * 60 * 1000) +
                        db.getSum(H.PREPARETIME, bdate, edate + 24 * 60 * 60 * 1000);
                if (triphrs == 0) {
                    tv.setText("NA");
                } else {
                    tv.setText(cs + Math.round((receivable - payable) / triphrs * 100) / 100.0);// remove number for formal
                }

                if ((receivable - payable) >= 0) {
                    tv.setTextColor(Color.rgb(0, 100, 0));
                } else {
                    tv.setTextColor(Color.rgb(100, 0, 0));
                }

                return tv;

            case 5:
                tv = new TextView(mainActivity);
                onlinehrs = db.getSum(H.ONLINEHRS, bdate, edate + 24 * 60 * 60 * 1000) +
                        db.getSum(H.WK_ONLINEHRS, bdate, edate + 24 * 60 * 60 * 1000);
                triphrs = db.getSum(H.TRIPHRS, bdate, edate + 24 * 60 * 60 * 1000) +
                        db.getSum(H.TRIPDETAIL, bdate, edate + 24 * 60 * 60 * 1000) +
                        db.getSum(H.PREPARETIME, bdate, edate + 24 * 60 * 60 * 1000);
                if (onlinehrs == 0) {
                    tv.setText("NA");
                } else {
                    tv.setText("" + (Math.round(triphrs / (onlinehrs) * 100) / 1.00));
                }
                return tv;
            case 6:
                double kms = db.getMileageSum(bdate, edate + 24 * 60 * 60 * 1000);
                double tripkms = db.getSum(H.DISTANCE, bdate, edate + 24 * 60 * 60 * 1000);
                tv = new TextView(mainActivity);
                if (kms == 0) {
                    tv.setText("NA");
                } else {
                    tv.setText(cs + H.round((receivable - payable) / kms) +
                            (tripkms == 0 ? "" : "(" + cs + H.round((receivable - payable) / tripkms)) + ")");
                }

                return tv;

        }

        return null;
    }

    private void radiobuttonOP(int entryid) {


        String str = Integer.toBinaryString(DatabaseHandler.getInstance(mainActivity).getSource(entryid));
        // str must be 7 digits. if less than 7 digit, "0" must be added
        int fl = 7 - str.length();
        for (int i = 0; i < fl; i++) {
            str = "0" + str;
        }

        // foruber.setChecked(true);

        // if (true)
        //    return;

        if (str.charAt(0) == '0') {
            foruber.setEnabled(false);
        }

        if (str.charAt(1) == '0') {
            forlyft.setEnabled(false);
        }

        if (str.charAt(2) == '0') {
            forother.setEnabled(false);
        }

        if (str.charAt(3) == '0') {
            overhead.setEnabled(false);
        }

        int a = (str.charAt(5) == '1' ? 2 : 0) + (str.charAt(6) == '1' ? 1 : 0);

        switch (a) {
            case 0:
                foruber.setChecked(true);
                break;
            case 1:
                forlyft.setChecked(true);
                break;
            case 2:
                forother.setChecked(true);
                break;
            case 3:
                overhead.setChecked(true);
                break;
        }
    }

    TextWatcher savebuttonlistener = new TextWatcher() {

        @Override
        public void afterTextChanged(Editable s) {
            if (amount.getText().toString().trim().length() <= 0) {
                save.setEnabled(false);
            } else {
                save.setEnabled(true);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start,
                                      int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start,
                                  int before, int count) {

        }
    };

    TextWatcher textwatcher = new TextWatcher() {

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
            settingsave.setEnabled(true);
        }
    };

    private View.OnClickListener settingSaveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //  if ((dratem.getText().toString().trim().length() == 1 && dratem.getText().toString().startsWith(".")) || insharem.getText().toString().trim().length() == 1 && insharem.getText().toString().startsWith(".")) {
            //       return;
            //   }

            if (datasharem.getText().toString().trim().length() == 0 || dratem.getText().toString().trim().length() == 0 || insharem.getText().toString().trim().length() == 0 || startingm.getText().toString().trim().length() == 0) {
                AlertDialog.Builder alert = new AlertDialog.Builder(mainActivity);
                alert.setMessage("Field can not be empty").setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {

                                dialog.dismiss();

                            }
                        }).show();
                return;
            }


            DatabaseHandler db = DatabaseHandler.getInstance(mainActivity);
            ArrayList<Item> settings = db.getItemsByName(H.SETTINGKEY, H.DESC, -1, 100000);
            for (Item s : settings) {

                switch ((int) s.get_date()) {
                    case 0:
                        s.set_amount(Double.valueOf(startingm.getText().toString()));//gasshare
                        mainActivity.gasshare = s.get_amount();
                        break;
                    case 1:
                        s.set_amount(Double.valueOf(dratem.getText().toString()));//dratshare
                        mainActivity.drate = s.get_amount();
                        break;
                    case 2:
                        s.set_amount(Double.valueOf(insharem.getText().toString()));//inshare
                        mainActivity.inshare = s.get_amount();
                        break;
                    case 3:
                        s.set_note(SP.getString("csymbol", "yyy"));
                        break;
                    case 4:
                        s.set_note(SP.getString("unit", "xxx"));
                        break;
                    case 5:
                        s.set_amount(Double.valueOf(datasharem.getText().toString()));//datahare
                        mainActivity.datashare = s.get_amount();
                        break;
                }
                db.updateItem(s);
            }


            v.setEnabled(false);

            AlertDialog.Builder alert = new AlertDialog.Builder(mainActivity);
            alert.setMessage("New Setting has been saved").setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {

                            dialog.dismiss();

                        }
                    }).show();
            ucode = new Item();
            ucode.set_note(entryid + "SD");
            ucode.set_name(H.USERCODE);
            db.addItem(ucode);

        }
    };

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);

        switch ((int) entryid) {
            case H.ENTRIES:
                menu.removeItem(R.id.action_wv_back);
                menu.removeItem(R.id.action_forward);
                menu.removeItem(R.id.action_about);
                break;
            case H.D_ENTRIES:
                menu.removeItem(R.id.action_gas);
                menu.removeItem(R.id.action_mileage);
                menu.removeItem(R.id.action_records);
                menu.removeItem(R.id.action_datarepair);
                menu.removeItem(R.id.action_export);
                menu.removeItem(R.id.action_import);
                menu.removeItem(R.id.action_add);
                menu.removeItem(R.id.action_setting);
                menu.removeItem(R.id.action_upgrade);
                break;
            case H.SUMMARY:
            case H.DAILYR:
            case H.WEEKR:
                //  case H.DATAE:
                //  case H.ALLR:
                menu.clear();
                inflater.inflate(R.menu.reports, menu);
                menu.removeItem(R.id.action_previous);
                menu.removeItem(R.id.action_next);
                menu.removeItem(R.id.action_toexcel);
                break;
            case H.UBERLOGIN:
                menu.clear();
                inflater.inflate(R.menu.webview, menu);
                menu.removeItem(R.id.entrymatch);
                menu.removeItem(R.id.lfytyears);
                menu.removeItem(R.id.action_wv_back);
                menu.removeItem(R.id.lfytweeks);
                this.menu = menu;
                break;
            case H.DSETTING:
                menu.clear();
                inflater.inflate(R.menu.webview, menu);
                menu.removeItem(R.id.uberupdate);
                menu.removeItem(R.id.ubertripupdate);
                menu.removeItem(R.id.lfytyears);
                menu.removeItem(R.id.action_wv_back);
                menu.removeItem(R.id.lfytweeks);
                menu.removeItem(R.id.action_forward);
               // menu.removeItem(R.id.action_home);
                menu.removeItem(R.id.action_reflesh);
                this.menu = menu;
                break;
            case H.LYFTLOGIN:
                menu.clear();
                inflater.inflate(R.menu.webview, menu);
                this.menu = menu;
                menu.removeItem(R.id.entrymatch);
                menu.removeItem(R.id.uberupdate);
                menu.removeItem(R.id.action_forward);
                menu.removeItem(R.id.uberupdate);
                break;
            case H.TRIPDETAIL:
                menu.clear();
                inflater.inflate(R.menu.tripdetail, menu);
                this.menu = menu;
                menunext = menu.findItem(R.id.action_next);
                menuprevious = menu.findItem(R.id.action_previous);
                itemcolor(menuprevious, mainActivity.getResources().getDrawable(R.drawable.ic_previous_black_24dp), Color.GRAY);
                //MenuItem sp=menu.findItem(R.id.uberupdate);
               // sp.setEnabled(false);
               // sp=menu.findItem(R.id.ubertripupdate);
               // sp.setEnabled(false);
              //  sp=menu.findItem(R.id.lfytweeks);
               // sp.setEnabled(false);
               // sp=menu.findItem(R.id.lfytyears);
               // sp.setEnabled(false);

                break;
            case H.ENTRYMATCH:
                menu.clear();
                inflater.inflate(R.menu.webview, menu);
                menu.removeItem(R.id.uberupdate);
                menu.removeItem(R.id.ubertripupdate);
                menu.removeItem(R.id.lfytyears);
                menu.removeItem(R.id.action_wv_back);
                menu.removeItem(R.id.lfytweeks);
                menu.removeItem(R.id.action_forward);
                // menu.removeItem(R.id.action_home);
                menu.removeItem(R.id.action_reflesh);
                this.menu = menu;
                break;
            case H.REWARDV:
                //  case H.DATAE:
                //  case H.ALLR:
                menu.clear();
                inflater.inflate(R.menu.reports, menu);
                menu.removeItem(R.id.action_previous);
                menu.removeItem(R.id.action_next);
                menu.removeItem(R.id.action_toexcel);
                menu.removeItem(R.id.action_daily);
                menu.removeItem(R.id.action_weekly);
                menu.removeItem(R.id.action_monthly);
                menu.removeItem(R.id.action_period);
                break;
            default:
                menu.clear();
                inflater.inflate(R.menu.reports, menu);
                menu.removeItem(R.id.action_daily);
                menu.removeItem(R.id.action_weekly);
                menu.removeItem(R.id.action_monthly);
                menu.removeItem(R.id.action_period);
                menunext = menu.findItem(R.id.action_next);
                menuprevious = menu.findItem(R.id.action_previous);
                if(entryid==H.DATAE){
                    menu.removeItem(R.id.action_toexcel);
                }
                itemcolor(menuprevious, mainActivity.getResources().getDrawable(R.drawable.ic_previous_black_24dp), Color.GRAY);
        }

    }

    private void tableOP(final long entryid) {

        orderOP(0, entryid);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_home:
                //this.fabmsg
                mainActivity.onBackPressed();
                System.out.println("========Pressedd=======");
                return true;
            case R.id.action_next:
                mainActivity.page++;
                if (orderid == -1) {
                    if (entryid == H.DATAE) {
                        orderOP(2, orderentryid);
                    } else if (entryid == H.ALLR) {
                        orderOP(0, orderentryid);
                    } else {
                        tableOP(entryid);
                    }

                } else {

                    orderOP(orderid, orderentryid);

                }
                return true;
            case R.id.action_previous:

                mainActivity.page--;
                if (mainActivity.page < 0) {
                    mainActivity.page = 0;
                }
                if (orderid == -1) {
                    if (entryid == H.DATAE) {
                        orderOP(2, orderentryid);
                    } else if (entryid == H.ALLR) {
                        orderOP(0, orderentryid);
                    } else {
                        tableOP(entryid);
                    }
                } else {
                    orderOP(orderid, orderentryid);
                }

                //orderentryid=entryid?
                return true;
            case R.id.action_daily:

                EntryFragment
                        fragment = EntryFragment.newInstance(H.DAILYR);
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment).addToBackStack(null)
                        .commit();

                return true;

            case R.id.action_weekly:

                fragment = EntryFragment.newInstance(H.WEEKR);
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment).addToBackStack(null)
                        .commit();

                return true;

            case R.id.action_toexcel:

                 if(gridLayout!=null){
                    int c=gridLayout.getRowCount();
                    String ss="";

                    for(int i=0;i<c;i++){
                        View amount=H.getValue(gridLayout,i,1);
                        View date=H.getValue(gridLayout,i,2);
                        View company=H.getValue(gridLayout,i,3);
                        View note=H.getValue(gridLayout,i,4);
                        if(entryid==H.MILEAGE){
                            View range=H.getValue(gridLayout,i,5);
                            if(amount !=null && amount instanceof TextView) {
                                String dst=((TextView)date).getText().toString();
                                if(i==0){
                                    int n=dst.indexOf("(");
                                    dst=dst.substring(0,n);
                                }
                                ss+=((TextView)amount).getText() + "," + dst + "," + ((TextView)company).getText() + "," + ((TextView)note).getText()+","+((TextView)range).getText()+"\n";
                            }
                        }else if(entryid==H.ALLR){
                            View range=H.getValue(gridLayout,i,0);
                            View c5=H.getValue(gridLayout,i,5);
                            if(amount !=null && amount instanceof TextView) {
                                String dst=((TextView)amount).getText().toString();
                                if(i==0){
                                    int n=dst.indexOf("(");
                                    dst=dst.substring(0,n);
                                }
                                ss+=((TextView)range).getText()+","+dst + "," + ((TextView)date).getText() + "," + ((TextView)company).getText() + "," + ((TextView)note).getText()+","+((TextView)c5).getText()+"\n";
                            }
                        }else{
                            if(amount !=null && amount instanceof TextView) {
                                ss+=((TextView)amount).getText() + "," + ((TextView)date).getText() + "," + ((TextView)company).getText() + "," + ((TextView)note).getText()+"\n";
                            }
                        }


                     }


                     mainActivity.startexport(334,ss);

                     ucode = new Item();
                     ucode.set_note("EXCEL");
                     ucode.set_name(H.USERCODE);
                     DatabaseHandler.getInstance(mainActivity).addItem(ucode);
                     //System.out.println(ss);
                 }


                return true;

            case R.id.action_monthly:

                monthlyReport();

                return true;
            case R.id.action_period:
                changeperiod(null);
                return true;

            case R.id.action_wv_back:

                /*/ not working
                if (true) return true;


                if (webView.canGoBack()) {
                    webView.canGoBack();
                    MenuItem bk = menu.findItem(R.id.action_forward);
                    if (!bk.isEnabled()) {
                        bk.setEnabled(true);
                        itemcolor(bk, getResources().getDrawable(R.drawable.ic_arrow_forward_black_24dp), Color.WHITE);

                    }
                } else {
                    item.setEnabled(false);
                    itemcolor(item, getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp), Color.GRAY);
                }
                //*/
                return true;

            case R.id.action_forward:
                JSONLOCK = true;
                wv.loadUrl("https://partners.uber.com/p3/money/statements/view/current");
                return true;

            case R.id.action_reflesh:

                webView.reload();

                return true;
            case R.id.entrymatch:

                fragment = EntryFragment.newInstance(H.ENTRYMATCH);
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment).addToBackStack(null)
                        .commit();

                return true;

            case R.id.uberupdate:

                if (!H.PAID) {
                    final int rm = SP.getInt("tripbonus",0)+H.TRIPLIMIT - DatabaseHandler.getInstance(mainActivity).getItemCountByName("" + H.TRIPDETAIL);
                    if (rm <= 0) {
                        H.pupmsg(mainActivity, "You have reached " + (SP.getInt("tripbonus",0)+H.TRIPLIMIT) + " trips limit for free edition.");
                        return true;
                    }
                }


                ArrayList<Item> viewids = DatabaseHandler.getInstance(mainActivity).getViewItems("" + H.VIEWID, -1, 100000);
                LayoutInflater factory = LayoutInflater.from(mainActivity);
                final View rview = factory.inflate(R.layout.uberupdate, null);
                progressbar = rview.findViewById(R.id.viewidprogressbar);
                Button btn = (Button) rview.findViewById(R.id.retry);
                percentage = (TextView) rview.findViewById(R.id.percentage);
                auto = (Button) rview.findViewById(R.id.autodownload);
                auto.setVisibility(View.VISIBLE);
                btn.setText("Uber Trip Basics Download");
                JSONLOCK = true;
                viewidbtn = btn;
                viewidbtn.setEnabled(false);
                final RadioGroup rg = rview.findViewById(R.id.entrynames);
                crg = rg;


                RadioButton rb;

                rb = new RadioButton(mainActivity);
                rb.setText("Current Period");
                Item it1 = new Item();
                it1.set_note("current");
                rb.setTag(it1);
                rg.addView(rb);
                for (Item it : viewids) {
                    rb = new RadioButton(mainActivity);
                    rb.setText(it.get_xxx());
                    rb.setTag(it);
                    if (it.get_source() == 1) {
                        rb.setEnabled(false);
                        rb.setTextColor(Color.rgb(255, 50, 200));
                    } else {
                        rb.setTextColor(Color.rgb(200, 0, 0));
                    }
                    rg.addView(rb);
                }
                rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        RadioButton rb = radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
                        currentrb = rb;
                        if (rb != null) {
                            Item it = (Item) rb.getTag();
                            wv.loadUrl("https://partners.uber.com/p3/money/statements/view/" + it.get_note());
                            JSONLOCK = true;
                            viewidbtn.setEnabled(false);
                            viewidbtn.setText("Start Downloading");
                        }
                    }
                });
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RadioButton rb = rg.findViewById(rg.getCheckedRadioButtonId());

                        if (rb != null) {
                            Item it = (Item) rb.getTag();
                            JSONLOCK = true;
                            wv.loadUrl("https://partners.uber.com/p3/money/statements/view/" + it.get_note());
                        } else {
                            H.pupmsg(mainActivity, "Problem?");
                        }
                    }
                });
                auto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //rechecking=false;
                        if (!H.PAID) {
                            final int rm = SP.getInt("tripbonus",0)+H.TRIPLIMIT - DatabaseHandler.getInstance(mainActivity).getItemCountByName("" + H.TRIPDETAIL);
                            if (rm <= 0) {
                                H.pupmsg(mainActivity, "You have reached " + (SP.getInt("tripbonus",0)+H.TRIPLIMIT) + " trips limit for free edition.");
                                return;
                            }
                        }

                        crg.getChildAt(0).setEnabled(false);
                        tripcount = 0;
                        tripcounter = 0;
                        added = 0;
                        updated = 0;
                        missed = 0;

                        if (!autodownload) {
                            int n = 0;
                            RadioButton rb = (RadioButton) crg.getChildAt(n);
                            while (rb != null && !rb.isEnabled()) {
                                n++;
                                rb = (RadioButton) crg.getChildAt(n);
                            }

                            if (rb != null) {
                                autodownload = true;
                                rb.performClick();
                                if (rechecking) {
                                    auto.setText("Stop Rechecking");
                                } else {
                                    auto.setText("Stop Auto Download");
                                }
                            }
                        } else {
                            autodownload = false;
                            if (rechecking) {
                                auto.setText("Restart Rechecking");
                            } else {
                                auto.setText("Restart Auto Download");
                            }
                        }

                    }
                });

                auto.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {


                        for (int i = 0; i < crg.getChildCount(); i++) {
                            if (!crg.getChildAt(i).isEnabled()) {
                                crg.getChildAt(i).setEnabled(true);
                            }
                        }
                        // crg.clearCheck();
                        if (rechecking) {
                            rechecking = false;
                            auto.setText("Start Auto Update");
                        } else {
                            rechecking = true;
                            auto.setText("Rechecking");
                        }

                        return true;
                    }
                });
                final AlertDialog.Builder alert = new AlertDialog.Builder(mainActivity);
                alert.setView(rview).setPositiveButton("CLOSE",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                dialog.dismiss();
                            }
                        });

                AlertDialog alertDialog = alert.create();
                alertDialog.show();
                alertDialog.getWindow().setLayout(H.getScreenWidth(mainActivity), H.getScreenHeight(mainActivity));


                return true;

            case R.id.ubertripupdate:

                //----------------------------
                factory = LayoutInflater.from(mainActivity);
                final View tview = factory.inflate(R.layout.uberupdate, null);
                progressbar = tview.findViewById(R.id.circleprogressbar);
                btn = (Button) tview.findViewById(R.id.retry);
                autou = tview.findViewById(R.id.autodownload);
                autou.setVisibility(View.VISIBLE);

                btn.setText("Update Trip Details");
                viewidbtn = btn;
                viewidbtn.setEnabled(false);
                final RadioGroup trg = tview.findViewById(R.id.entrynames);

                trg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        RadioButton rb = radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
                        currentrb = rb;
                        if (rb != null) {
                            Item it = (Item) rb.getTag();
                            String[] strs = it.get_xxx().split(H.SEPERATOR);
                            final String tripid = strs[0];

                            if (it.get_source() == 0) {
                                mainActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        wv.loadUrl("https://partners.uber.com/p3/payments/trips/" + tripid);
                                    }
                                });

                            } else if (it.get_source() == 1) {

                                mainActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        wv.loadUrl("https://www.lyft.com/api/route_detail_earnings/route/" + tripid);
                                    }
                                });

                            }
                            // xxwebView.loadUrl("https://partners.uber.com/p3/money/statements/view/" + it.get_note());
                            mainActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    if (progressbar != null && !progressbar.isShown()) {
                                        progressbar.setVisibility(View.VISIBLE);
                                    }
                                    viewidbtn.setEnabled(false);
                                    viewidbtn.setText("Start Updating");
                                }
                            });


                        } else {
                            H.pupmsg(mainActivity, "Problem i=" + i);
                        }
                    }
                });
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RadioButton rb = trg.findViewById(trg.getCheckedRadioButtonId());
                        if (rb != null) {
                            Item it = (Item) rb.getTag();
                            //H.pupmsg(getActivity(), "" + it.get_note());
                            JSONLOCK = true;
                            if (progressbar != null && !progressbar.isShown()) {
                                progressbar.setVisibility(View.VISIBLE);
                            }
                            String[] strs = it.get_xxx().split(H.SEPERATOR);
                            String tripid = strs[0];
                            if (it.get_source() == 0) {
                                wv.loadUrl("https://partners.uber.com/p3/payments/trips/" + tripid);
                            } else if (it.get_source() == 1) {
                                wv.loadUrl("https://www.lyft.com/api/route_detail_earnings/route/" + tripid);
                            }

                        } else {
                            H.pupmsg(mainActivity, "Problem?");
                        }
                    }
                });

                autou.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!autodownload) {
                            int n = 0;
                            RadioButton rb = (RadioButton) crg.getChildAt(n);
                            while (rb != null && !rb.isEnabled()) {
                                n++;
                                rb = (RadioButton) crg.getChildAt(n);
                            }

                            if (rb != null) {
                                autodownload = true;
                                rb.performClick();
                                autou.setText("Stop Auto Update");
                            }


                        } else {
                            autodownload = false;

                            if (progressbar != null) {
                                mainActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (progressbar != null && progressbar.isShown()) {
                                            progressbar.setVisibility(View.INVISIBLE);
                                        }
                                        autou.setText("Restart Auto Update");
                                    }
                                });
                            }
                        }

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                RadioButton local = currentrb;
                                RadioButton checkedbtn = null;
                                while (autodownload) {
                                    try {
                                        Thread.sleep(6 * 1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    checkedbtn = crg.findViewById(crg.getCheckedRadioButtonId());
                                    if (checkedbtn == null || !(checkedbtn.getText().toString().equalsIgnoreCase(currentrb.getText().toString()))) {
                                        autodownload = false;
                                        autou.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                autou.setText(tripcp + " Completed.Something Wrong!\nRestart Auto Update");
                                                autou.performClick();
                                            }
                                        }, 10000);

                                    }

                                    if (local.getText().toString().trim().equalsIgnoreCase(currentrb.getText().toString().trim())) {
                                        int i = crg.indexOfChild(currentrb);
                                        i++;
                                        local = (RadioButton) crg.getChildAt(i);
                                        while (local != null && !local.isEnabled()) {
                                            i++;
                                            local = (RadioButton) crg.getChildAt(i);
                                        }

                                        if (local == null) {
                                            autodownload = false;
                                            mainActivity.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if (autou != null) {
                                                        autou.setText("Completed");
                                                        autou.setEnabled(false);
                                                    }
                                                    if (viewidbtn != null) {
                                                        viewidbtn.setText("Completed");
                                                    }

                                                    if (progressbar != null) {
                                                        progressbar.setVisibility(View.GONE);
                                                    }

                                                }
                                            });

                                            break;
                                        } else {
                                            currentrb = local;
                                            final Item it = (Item) local.getTag();
                                            String[] strs = it.get_xxx().split(H.SEPERATOR);
                                            final String tripid = strs[0];

                                            mainActivity.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    crg.check(currentrb.getId());
                                                    if (it.get_source() == 0) {
                                                        wv.loadUrl("https://partners.uber.com/p3/payments/trips/" + tripid);
                                                    } else if (it.get_source() == 1) {
                                                        wv.loadUrl("https://www.lyft.com/api/route_detail_earnings/route/" + tripid);
                                                    }
                                                }
                                            });

                                        }
                                    } else {

                                        local = currentrb;
                                    }
                                }
                            }
                        }).start();

                    }
                });


                LayoutInflater flator = LayoutInflater.from(mainActivity);
                final View barview = flator.inflate(R.layout.progress, null);
                final ProgressBar pb = (ProgressBar) barview.findViewById(R.id.pb);
                final TextView percentage = (TextView) barview.findViewById(R.id.percentage);
                final AlertDialog.Builder alert2 = new AlertDialog.Builder(mainActivity);
                alert2.setView(barview);
                final AlertDialog alertDialog2 = alert2.create();

                AsyncTask task = new AsyncTask<Void, Integer, Void>() {

                    @Override
                    protected void onPreExecute() {
                        alertDialog2.show();
                        //return null;
                    }

                    @Override
                    protected Void doInBackground(Void... arg0) {
                        ArrayList<Item> trips = DatabaseHandler.getInstance(mainActivity).getItemsByName("" + H.TRIPDETAIL, H.DESC, -1, 100000);
                        tript = trips.size();
                        tripcp = 0;
                        RadioButton rb = null;
                        SimpleDateFormat sdt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                        int t = trips.size();
                        for (int i = 0; i < t; i++) {
                            Item it = trips.get(i);
                            rb = new RadioButton(mainActivity);
                            rb.setText(sdt.format(it.get_date()) + "-" + (it.get_source() == 0 ? "Uber" : (it.get_source() == 1 ? "Lyft" : "Unknown")));
                            rb.setTag(it);
                            if (it.get_rcdate() == 1) {
                                tripcp++;
                            } else {
                                rb.setTextColor(Color.rgb(255, 50, 200));
                                trg.addView(rb);
                            }
                            publishProgress((int) ((i * 100.0) / (t * 1.0)));
                        }
                        crg = trg;


                        return null;
                    }

                    @Override
                    protected void onProgressUpdate(Integer... values) {
                        pb.setProgress(values[0]);
                        percentage.setText(values[0] + "%");
                        if (values[0] >= 98) {
                            alertDialog2.dismiss();
                        }
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        autou.setText("Start Auto Update\n(" + (tript - tripcp) + "/" + tript + " Remaining)");
                        final AlertDialog.Builder alertt = new AlertDialog.Builder(mainActivity);
                        alertt.setView(tview).setPositiveButton("CLOSE",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int whichButton) {
                                        dialog.dismiss();
                                        if (progressbar != null) {
                                            progressbar = null;
                                        }
                                    }
                                });

                        AlertDialog alertDialogt = alertt.create();
                        alertDialogt.show();
                        alertDialogt.getWindow().setLayout(H.getScreenWidth(mainActivity), H.getScreenHeight(mainActivity));

                    }

                };
                task.execute((Void[]) null);

                return true;

            case R.id.lfytyears:

                if (!H.PAID) {
                    final int rm = SP.getInt("tripbonus",0)+H.TRIPLIMIT - DatabaseHandler.getInstance(mainActivity).getItemCountByName("" + H.TRIPDETAIL);
                    if (rm <= 0) {
                        H.pupmsg(mainActivity, "You have reached " + (SP.getInt("tripbonus",0)+H.TRIPLIMIT) + " trips limit for free edition.");
                        return true;
                    }
                }

                factory = LayoutInflater.from(mainActivity);
                final View nview = factory.inflate(R.layout.uberupdate, null);
                progressbar = nview.findViewById(R.id.circleprogressbar);
                btn = (Button) nview.findViewById(R.id.retry);
                btn.setText("Lyft Trip Basics Download by Year\n(For Reference)");
                viewidbtn = btn;
                viewidbtn.setEnabled(false);
                viewidbtn.setId(R.id.lfytyears);
                viewidbtn.setTextColor(Color.BLACK);
                final RadioGroup years = nview.findViewById(R.id.entrynames);
                lyftrg = years;

                fillyears(years);

                years.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        RadioButton rb = radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
                        String[] value = (String[]) rb.getTag();
                        if (progressbar != null && !progressbar.isShown()) {
                            progressbar.setVisibility(View.VISIBLE);
                        }
                        viewidbtn.setTextColor(Color.BLACK);

                        if (value[0].equalsIgnoreCase("YEARS")) {
                            lyftyear = rb.getText().toString();
                            wv.loadUrl("https://www.lyft.com/api/driver_yearly_details/" + rb.getText());
                        } else if (value[0].equalsIgnoreCase("YEARSS")) {
                            wv.loadUrl("https://www.lyft.com/api/driver_yearly_details/" + value[1]);
                        } else if (value[0].equalsIgnoreCase("HOME")) {
                            years.removeAllViews();
                            fillyears(years);
                            viewidbtn.setText("Lyft Trip Basics Download by Year\n(For Reference)");
                            if (progressbar != null && progressbar.isShown()) {
                                progressbar.setVisibility(View.INVISIBLE);
                            }
                        } else if (value[0].equalsIgnoreCase("WEEKS")) {
                            lyftweek = value[1];
                            wv.loadUrl("https://www.lyft.com/api/driver_routes/" + value[1]);
                        } else if (value[0].equalsIgnoreCase("TRIP")) {
                            viewidbtn.setText("Start Updating");
                            wv.loadUrl("https://www.lyft.com/api/route_detail_earnings/route/" + value[1]);
                            currentrb = rb;
                        }
                    }
                });

                final AlertDialog.Builder alertn = new AlertDialog.Builder(mainActivity);
                alertn.setView(nview).setPositiveButton("CLOSE",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                dialog.dismiss();
                                // webView.loadDataWithBaseURL("", "<html><body>Completed</body></html>", "text/html", "UTF-8", "");

                            }
                        });

                alertDialogn = alertn.create();
                alertDialogn.show();
                alertDialogn.getWindow().setLayout(H.getScreenWidth(mainActivity), H.getScreenHeight(mainActivity));


                return true;

            case R.id.lfytweeks:
                if (!H.PAID) {
                    final int rm = SP.getInt("tripbonus",0)+H.TRIPLIMIT - DatabaseHandler.getInstance(mainActivity).getItemCountByName("" + H.TRIPDETAIL);
                    if (rm <= 0) {
                        H.pupmsg(mainActivity, "You have reached " + (SP.getInt("tripbonus",0)+H.TRIPLIMIT) + " trips limit for free edition.");
                        return true;
                    }
                }

                factory = LayoutInflater.from(mainActivity);
                final View wview = factory.inflate(R.layout.uberupdate, null);
                progressbar = wview.findViewById(R.id.viewidprogressbar);
                btn = (Button) wview.findViewById(R.id.retry);
                btn.setEnabled(true);
                btn.setText("Lyft Weekly Statements Update by Year\n(For Calculation)");
                viewidbtn = btn;
                //viewidbtn.setEnabled(false);
                viewidbtn.setId(R.id.lfytweeks);
                viewidbtn.setTextColor(Color.BLACK);
                final RadioGroup weeks = wview.findViewById(R.id.entrynames);
                lyftrg = weeks;
                // progressbar.setVisibility(View.VISIBLE);

                fillyears(weeks);
                weeks.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        RadioButton rb = radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
                        String[] value = (String[]) rb.getTag();
                        //progressbar.setVisibility(View.VISIBLE);

                        viewidbtn.setTextColor(Color.BLACK);
                        if (value[0].equalsIgnoreCase("YEARS")) {
                            lyftyear = rb.getText().toString();
                            wv.loadUrl("https://www.lyft.com/api/driver_yearly_details/" + rb.getText());
                        } else if (value[0].equalsIgnoreCase("YEARSS")) {
                            wv.loadUrl("https://www.lyft.com/api/driver_yearly_details/" + value[1]);
                        } else if (value[0].equalsIgnoreCase("HOME")) {
                            weeks.removeAllViews();
                            fillyears(weeks);
                            viewidbtn.setText("Lyft Weekly Statements Update by Year\n(For Calculation))");
                            if (progressbar != null && progressbar.isShown()) {
                                progressbar.setVisibility(View.INVISIBLE);
                            }
                        } else if (value[0].equalsIgnoreCase("WEEKS")) {
                            lyftweek = value[1];
                            wv.loadUrl("https://www.lyft.com/api/driver_week_earnings/" + value[1]);
                            currentrb = rb;
                        }
                    }
                });

                btn.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        for (int i = 0; i < lyftrg.getChildCount(); i++) {
                            if (!lyftrg.getChildAt(i).isEnabled()) {
                                lyftrg.getChildAt(i).setEnabled(true);
                                ((RadioButton) lyftrg.getChildAt(i)).setTextColor(Color.rgb(255, 50, 200));
                            }
                        }
                        return true;
                    }
                });
                final AlertDialog.Builder alertw = new AlertDialog.Builder(mainActivity);
                alertw.setView(wview).setPositiveButton("CLOSE",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                dialog.dismiss();
                                // webView.loadDataWithBaseURL("", "<html><body>Completed</body></html>", "text/html", "UTF-8", "");

                            }
                        });

                alertDialogn = alertw.create();
                alertDialogn.show();
                alertDialogn.getWindow().setLayout(H.getScreenWidth(mainActivity), H.getScreenHeight(mainActivity));


                return true;


            default:
                return super.onOptionsItemSelected(item);

        }


    }

    private void fillyears(RadioGroup years) {
        Calendar calendar = Calendar.getInstance();
        int thisyear = calendar.get(Calendar.YEAR);
        for (int y = 2015; y <= thisyear; y++) {
            RadioButton rb = new RadioButton(mainActivity);
            rb.setText("" + y);
            String[] para = {"YEARS"};
            rb.setTag(para);

            years.addView(rb);
        }

    }

    private void itemcolor(MenuItem mt, Drawable drawable, int color) {
        drawable.mutate().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        mt.setIcon(drawable);
    }

    private void dailyReport(GridLayout gridLayout, final ProgressBar pb, final TextView percentage) {


        int row = (int) ((edate - bdate) / 1000l / 60l / 60l / 24l) + 3;
        gridLayout.setMinimumWidth((int) (width * 0.9));

        ArrayList<Item> items0 = DatabaseHandler.getInstance(mainActivity).getSettingKeys();
        ArrayList<Item> items = new ArrayList<>();
        ArrayList<Item> itemst = new ArrayList<>();
        for (int i = 0; i < items0.size(); i++) {
            if (!H.dtcheck(items0.get(i).get_rcdate())) {
                itemst.add(items0.get(i));
                items.add(null);
            }
        }


        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (pb != null) {
                    pb.setProgress(2);
                    percentage.setText("2%");
                }
            }
        });
        Collections.copy(items, itemst);
        Collections.sort(items);


        int coln = items.size() + 1;


        gridLayout.setColumnCount(coln + 1);
        gridLayout.setRowCount(row + 5);
        // gridLayout.setMinimumHeight(((ItemDetailActivity)getActivity()).getScreenHeight());
        // gridLayout.setMinimumWidth(width);

        int margin = 20;

        int r = 0, c = 0;
        Date rowdate = null;
        GridLayout.Spec rowSpan = GridLayout.spec(r);
        GridLayout.Spec colspan = GridLayout.spec(c);
        GridLayout.LayoutParams gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        TextView label = null;
        long predate = bdate;


//************************Header******************************


        c = 0;
        rowSpan = GridLayout.spec(r);
        colspan = GridLayout.spec(c);
        gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        gridParam.setMargins(0, 0, margin, 0);
        rowdate = new Date(predate);
        label = new TextView(mainActivity);
        label.setText("Date");
        gridParam.setGravity(Gravity.CENTER_HORIZONTAL);
        gridLayout.addView(label, gridParam);//for date

        c = 1;
        rowSpan = GridLayout.spec(r);
        colspan = GridLayout.spec(c);
        gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        gridParam.setMargins(margin, 0, margin, 0);
        label = new TextView(mainActivity);
        label.setText("Revenue");
        gridLayout.addView(label, gridParam);//revenue

        DatabaseHandler db = DatabaseHandler.getInstance(mainActivity);

        for (Item item : items) {

            if (item.get_rcdate() == H.DSETTING || item.get_rcdate() == H.SUMMARY) {
                continue;
            }

            c++;

            rowSpan = GridLayout.spec(r);
            colspan = GridLayout.spec(c);
            gridParam = new GridLayout.LayoutParams(
                    rowSpan, colspan);
            gridParam.setMargins(margin, 0, margin, 0);
            label = new TextView(mainActivity);
            if (item.get_rcdate() == H.MILEAGE) {
                label.setText((unit.equalsIgnoreCase("metric") ? "KMs " : "Miles ") + "Driven");
            } else {
                label.setText("" + item.get_note());
            }

            gridLayout.addView(label, gridParam);

        }

        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (pb != null) {
                    pb.setProgress(4);
                    percentage.setText("4%");
                }
            }
        });
//*********************************** Content*************************


        double re = 0, tem = 0, subtotal = 0;
        int colorcode = 0;
        // int premileage = (int) db.getMax(H.MILEAGE, predate - 24 * 60 * 60 * 1000 * 10, predate);
        int i = 0;
        final double size0 = row;
        for (r = 1; r < row; r++) {
            c = 0;//
            rowSpan = GridLayout.spec(r);
            colspan = GridLayout.spec(c);
            gridParam = new GridLayout.LayoutParams(
                    rowSpan, colspan);
            gridParam.setMargins(margin, 0, margin, 0);
            rowdate = new Date(predate);
            label = new TextView(mainActivity);
            label.setText(sdf.format(rowdate));
            //label.setBackgroundColor(getColor(colorcode));
            label.setTextColor(getColor(colorcode));
            gridLayout.addView(label, gridParam);
            c++;

            for (Item item : items) { // add all revenue together
                if (item.get_rcdate() == H.DSETTING || item.get_rcdate() == H.SUMMARY) {
                    continue;
                }
                c++;
                rowSpan = GridLayout.spec(r);
                colspan = GridLayout.spec(c);
                gridParam = new GridLayout.LayoutParams(
                        rowSpan, colspan);
                gridParam.setMargins(margin, 0, margin, 0);
                label = new TextView(mainActivity);
                // label.setBackgroundColor(getColor(colorcode));
                label.setTextColor(getColor(colorcode));

                if (item.get_date() == H.RECEIVABLE || item.get_date() == H.PAYABLE) {
                    label.setText(cs + H.round(tem = db.getSum((int) item.get_rcdate(), predate, predate + 24 * 60 * 60 * 1000)));

                } else {
                    if (item.get_rcdate() == H.MILEAGE) {
                        tem = db.getMileageSum(predate, predate + 24 * 60 * 60 * 1000);
                        label.setText("" + H.round(tem));

                    } else {
                        label.setText("" + H.round(tem = db.getSum((int) item.get_rcdate(), predate, predate + 24 * 60 * 60 * 1000)));

                    }
                }

                if (item.get_date() == H.RECEIVABLE) {
                    re += tem;
                }
                gridLayout.addView(label, gridParam);

            }


            c = 1;
            rowSpan = GridLayout.spec(r);
            colspan = GridLayout.spec(c);
            gridParam = new GridLayout.LayoutParams(
                    rowSpan, colspan);
            gridParam.setMargins(margin, 0, margin, 0);
            rowdate = new Date(predate);
            label = new TextView(mainActivity);
            label.setTextColor(getColor(colorcode));
            label.setText(cs + H.round(re));
            //label.setMinimumWidth();
            gridLayout.addView(label, gridParam);
            colorcode++;
            if (colorcode == 3) {
                colorcode = 0;
            }
            subtotal += re;
            re = 0;

            predate += 24 * 60 * 60 * 1000;


            final int j = i;
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (pb != null && (j % 5) == 0) {
                        pb.setProgress(4 + (int) ((j / size0) * 86));
                        percentage.setText(4 + (int) ((j / size0) * 86) + "%");
                    }
                }
            });
            i++;

        }

        // --------------------------Total line--------
        r++;
        r++;
        c = 0;
        rowSpan = GridLayout.spec(r);
        colspan = GridLayout.spec(c);
        gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        gridParam.setMargins(0, 0, margin, 0);
        rowdate = new Date(predate);
        label = new TextView(mainActivity);
        label.setText("Subtotal");
        gridParam.setGravity(Gravity.CENTER_HORIZONTAL);
        gridLayout.addView(label, gridParam);//for date

        c = 1;
        rowSpan = GridLayout.spec(r);
        colspan = GridLayout.spec(c);
        gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        gridParam.setMargins(margin, 0, margin, 0);
        label = new TextView(mainActivity);
        label.setText("" + H.round(subtotal));
        gridLayout.addView(label, gridParam);//revenue


        //premileage = (int) db.getMin(H.MILEAGE, bdate, predate - 24 * 60 * 60 * 1000 * 10);
        i = 0;
        final double size = items.size();
        for (Item item : items) {

            if (item.get_rcdate() == H.DSETTING || item.get_rcdate() == H.SUMMARY) {
                continue;
            }

            c++;

            rowSpan = GridLayout.spec(r);
            colspan = GridLayout.spec(c);
            gridParam = new GridLayout.LayoutParams(
                    rowSpan, colspan);
            gridParam.setMargins(margin, 0, margin, 0);
            label = new TextView(mainActivity);

            if (item.get_date() == H.RECEIVABLE || item.get_date() == H.PAYABLE) {
                label.setText(cs + H.round(tem = db.getSum((int) item.get_rcdate(), bdate, predate + 24 * 60 * 60 * 1000)));

            } else {
                if (item.get_rcdate() == H.MILEAGE) {

                    tem = db.getMileageSum(bdate, predate + 24 * 60 * 60 * 1000);
                    label.setText("" + H.round(tem));

                } else {
                    label.setText("" + H.round(tem = db.getSum((int) item.get_rcdate(), bdate, predate + 24 * 60 * 60 * 1000)));
                }

            }

            gridLayout.addView(label, gridParam);


            final int j = i;
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (pb != null && (j % 5) == 0) {
                        pb.setProgress(90 + (int) ((j / size) * 8));
                        percentage.setText(90 + (int) ((j / size) * 8) + "%");
                    }
                }
            });
            i++;

        }


        //************************foot  Header******************************

        r++;
        c = 0;
        rowSpan = GridLayout.spec(r);
        colspan = GridLayout.spec(c);
        gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        gridParam.setMargins(0, 0, margin, 0);
        rowdate = new Date(predate);
        label = new TextView(mainActivity);
        label.setText("Date");
        gridParam.setGravity(Gravity.CENTER_HORIZONTAL);
        gridLayout.addView(label, gridParam);//for date

        c = 1;
        rowSpan = GridLayout.spec(r);
        colspan = GridLayout.spec(c);
        gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        gridParam.setMargins(margin, 0, margin, 0);
        label = new TextView(mainActivity);
        label.setText("Revenue");
        gridLayout.addView(label, gridParam);//revenue

        i = 0;
        for (Item item : items) {

            if (item.get_rcdate() == H.DSETTING || item.get_rcdate() == H.SUMMARY) {
                continue;
            }

            c++;

            rowSpan = GridLayout.spec(r);
            colspan = GridLayout.spec(c);
            gridParam = new GridLayout.LayoutParams(
                    rowSpan, colspan);
            gridParam.setMargins(margin, 0, margin, 0);
            label = new TextView(mainActivity);
            if (item.get_rcdate() == H.MILEAGE) {
                label.setText((unit.equalsIgnoreCase("metric") ? "KMs " : "Miles ") + "Driven");
            } else {
                label.setText("" + item.get_note());
            }

            gridLayout.addView(label, gridParam);
            final int j = i;
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (pb != null && (j % 5) == 0) {
                        pb.setProgress(98 + (int) ((j / size) * 2));
                        percentage.setText(98 + (int) ((j / size) * 2) + "%");
                    }
                }
            });

            i++;

        }

        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (pb != null) {
                    pb.setProgress(100);
                    percentage.setText("100%");
                }
            }
        });


    }

    private int getColor(int code) {
        switch (code) {
            case 0:
                return Color.parseColor("#4d4dff");//blue

            case 1:
                return Color.parseColor("#347235");//green

            case 2:
                return Color.parseColor("#990012");//pink
        }

        return Color.parseColor("#FFFFFF");
    }

    private void weeklyReport(GridLayout gridLayout, final ProgressBar pb, final TextView percentage) {

        ArrayList<Item> items0 = DatabaseHandler.getInstance(mainActivity).getSettingKeys();
        ArrayList<Item> items = new ArrayList<>();
        for (int i = 0; i < items0.size(); i++) {
            items.add(null);
        }
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (pb != null) {
                    pb.setProgress(2);
                    percentage.setText(2 + "%");
                }
            }
        });

        int row = (int) ((edate - bdate) / 1000 / 60 / 60 / 24 / 7) + 2, coln = items.size() + 1;

        Collections.copy(items, items0);
        Collections.sort(items);

        if (row < 1) {
            return;
        }
        gridLayout.setColumnCount(coln + 2);
        gridLayout.setRowCount(row + 6);

        int margin = 20;

        int r = 0, c = 0;
        Date rowdate = null;
        GridLayout.Spec rowSpan = GridLayout.spec(r);
        GridLayout.Spec colspan = GridLayout.spec(c);
        GridLayout.LayoutParams gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        TextView label = null;
        long predate = bdate;
//************************Header******************************
        c = 0;
        rowSpan = GridLayout.spec(r);
        colspan = GridLayout.spec(c);
        gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        gridParam.setMargins(0, 0, margin, 0);
        gridParam.setGravity(Gravity.CENTER_HORIZONTAL);

        label = new TextView(mainActivity);
        label.setText("Week");
        gridLayout.addView(label, gridParam);//for date

        c = 1;
        rowSpan = GridLayout.spec(r);
        colspan = GridLayout.spec(c);
        gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        gridParam.setMargins(margin, 0, margin, 0);

        label = new TextView(mainActivity);
        label.setText("Revenue");
        gridLayout.addView(label, gridParam);//revenue


        c = 2;
        rowSpan = GridLayout.spec(r);
        colspan = GridLayout.spec(c);
        gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        gridParam.setMargins(margin, 0, margin, 0);

        label = new TextView(mainActivity);
        label.setText("Cost");
        gridLayout.addView(label, gridParam);//cost

        DatabaseHandler db = DatabaseHandler.getInstance(mainActivity);

        for (Item item : items) {

            if (item.get_rcdate() == H.DSETTING || item.get_rcdate() == H.SUMMARY) {
                continue;
            }

            c++;

            rowSpan = GridLayout.spec(r);
            colspan = GridLayout.spec(c);
            gridParam = new GridLayout.LayoutParams(
                    rowSpan, colspan);
            gridParam.setMargins(margin, 0, margin, 0);
            rowdate = new Date(predate);
            label = new TextView(mainActivity);
            if (item.get_rcdate() == H.MILEAGE) {
                label.setText((unit.equalsIgnoreCase("metric") ? "KMs " : "Miles ") + "Driven");
            } else {
                label.setText("" + item.get_note());
            }
            gridLayout.addView(label, gridParam);

        }


        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (pb != null) {
                    pb.setProgress(4);
                    percentage.setText(4 + "%");
                }
            }
        });

//*********************************** Content*************************


        double re = 0, tem = 0, cost = 0, tre = 0, tcost = 0;
        int colorcode = 0;
        //int premileage = (int) db.getMax(H.MILEAGE, predate - 24 * 60 * 60 * 1000 * 30, predate);
        int i = 0;
        final double size0 = row;
        for (r = 1; r < row; r++) {
            c = 0;
            rowSpan = GridLayout.spec(r);
            colspan = GridLayout.spec(c);
            gridParam = new GridLayout.LayoutParams(
                    rowSpan, colspan);
            gridParam.setMargins(0, 0, margin, 0);
            rowdate = new Date(predate);
            long newdate = predate + 24 * 60 * 60 * 1000 * 7;
            Date rowdate2 = new Date(predate + 24 * 60 * 60 * 1000 * 6);
            if ((predate + 24 * 60 * 60 * 1000 * 7) > (edate + 24 * 60 * 60 * 1000)) {
                rowdate2 = new Date(edate);
                newdate = edate + 24 * 60 * 60 * 1000;
            }
            label = new TextView(mainActivity);
            label.setText(sdf.format(rowdate) + "-" + sdf.format(rowdate2));
            //label.setBackgroundColor(getColor(colorcode));
            label.setTextColor(getColor(colorcode));
            gridLayout.addView(label, gridParam);
            c++;
            c++;

            for (Item item : items) {
                if (item.get_rcdate() == H.DSETTING || item.get_rcdate() == H.SUMMARY) {
                    continue;
                }
                c++;
                rowSpan = GridLayout.spec(r);
                colspan = GridLayout.spec(c);
                gridParam = new GridLayout.LayoutParams(
                        rowSpan, colspan);
                gridParam.setMargins(margin, 0, margin, 0);
                label = new TextView(mainActivity);
                label.setTextColor(getColor(colorcode));

                if (item.get_date() == H.RECEIVABLE || item.get_date() == H.PAYABLE) {
                    tem = db.getSum((int) item.get_rcdate(), predate, newdate);
                    if (item.get_date() == H.RECEIVABLE) {
                        re += tem;
                    } else {
                        if (item.get_rcdate() == H.INSURANCE) {
                            tem = tem * inshare / 100.0;
                        } else if (item.get_rcdate() == H.GAS) {
                            tem = tem * gasshare / 100.0;
                        } else if (item.get_rcdate() == H.DATA) {
                            tem = tem * datashare / 100.0;
                        }
                        cost += tem;
                    }
                } else {

                    if (item.get_rcdate() == H.MILEAGE) {
                        tem = db.getMileageSum(predate, newdate);
                        //label.setText("" + H.round(tem));


                    } else {
                        tem = db.getSum((int) item.get_rcdate(), predate, newdate);

                    }

                }

                if (item.get_date() == H.RECEIVABLE || item.get_date() == H.PAYABLE) {
                    label.setText(cs + H.round(tem));
                } else {
                    label.setText("" + H.round(tem));
                }

                gridLayout.addView(label, gridParam);

            }


            c = 1;
            rowSpan = GridLayout.spec(r);
            colspan = GridLayout.spec(c);
            gridParam = new GridLayout.LayoutParams(
                    rowSpan, colspan);
            gridParam.setMargins(margin, 0, margin, 0);
            rowdate = new Date(predate);
            label = new TextView(mainActivity);
            label.setTextColor(getColor(colorcode));
            label.setText(cs + H.round(re));
            //label.setMinimumWidth();
            gridLayout.addView(label, gridParam);

            c = 2;
            rowSpan = GridLayout.spec(r);
            colspan = GridLayout.spec(c);
            gridParam = new GridLayout.LayoutParams(
                    rowSpan, colspan);
            gridParam.setMargins(margin, 0, margin, 0);
            rowdate = new Date(predate);
            label = new TextView(mainActivity);
            label.setTextColor(getColor(colorcode));
            label.setText(cs + H.round(cost));


            //label.setMinimumWidth();
            gridLayout.addView(label, gridParam);


            colorcode++;
            if (colorcode == 3) {
                colorcode = 0;
            }
            tre += re;
            re = 0;
            tcost += cost;
            cost = 0;

            predate += 24 * 60 * 60 * 1000 * 7;

            final int j = i;
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (pb != null && (j % 2) == 0) {
                        pb.setProgress(4 + (int) ((j / size0) * 86));
                        percentage.setText(4 + (int) ((j / size0) * 86) + "%");
                    }
                }
            });

            i++;
        }

        //--------sub total-----

        // --------------------------Total line--------
        r++;

        c = 0;
        rowSpan = GridLayout.spec(r);
        colspan = GridLayout.spec(c);
        gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        gridParam.setMargins(0, 0, margin, 0);
        rowdate = new Date(predate);
        label = new TextView(mainActivity);
        label.setText("Subtotal");
        gridParam.setGravity(Gravity.CENTER_HORIZONTAL);
        gridLayout.addView(label, gridParam);//for date

        c = 1;
        rowSpan = GridLayout.spec(r);
        colspan = GridLayout.spec(c);
        gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        gridParam.setMargins(margin, 0, margin, 0);
        label = new TextView(mainActivity);
        label.setText("" + H.round(tre));
        gridLayout.addView(label, gridParam);//revenue

        c = 2;
        rowSpan = GridLayout.spec(r);
        colspan = GridLayout.spec(c);
        gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        gridParam.setMargins(margin, 0, margin, 0);
        label = new TextView(mainActivity);
        label.setText("" + H.round(tcost));
        gridLayout.addView(label, gridParam);//cost


        //premileage = (int) db.getMin(H.MILEAGE, bdate, predate - 24 * 60 * 60 * 1000 * 10);
        i = 0;
        final double size = items.size();
        for (Item item : items) {

            if (item.get_rcdate() == H.DSETTING || item.get_rcdate() == H.SUMMARY) {
                continue;
            }

            c++;

            rowSpan = GridLayout.spec(r);
            colspan = GridLayout.spec(c);
            gridParam = new GridLayout.LayoutParams(
                    rowSpan, colspan);
            gridParam.setMargins(margin, 0, margin, 0);
            label = new TextView(mainActivity);

            if (item.get_date() == H.RECEIVABLE || item.get_date() == H.PAYABLE) {
                tem = db.getSum((int) item.get_rcdate(), bdate, edate + 24 * 60 * 60 * 1000);
                if (item.get_rcdate() == H.INSURANCE) {
                    tem = tem * inshare / 100.0;
                } else if (item.get_rcdate() == H.GAS) {
                    tem = tem * gasshare / 100.0;
                } else if (item.get_rcdate() == H.DATA) {
                    tem = tem * datashare / 100.0;
                }

            } else {
                if (item.get_rcdate() == H.MILEAGE) {

                    tem = db.getMileageSum(bdate, edate + 24 * 60 * 60 * 1000);


                } else {
                    tem = db.getSum((int) item.get_rcdate(), bdate, edate + 24 * 60 * 60 * 1000);


                }

            }
            if (item.get_date() == H.RECEIVABLE || item.get_date() == H.PAYABLE) {
                label.setText(cs + H.round(tem));
            } else {
                label.setText("" + H.round(tem));
            }
            gridLayout.addView(label, gridParam);


            final int j = i;
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (pb != null && (j % 2) == 0) {
                        pb.setProgress(90 + (int) ((j / size) * 6));
                        percentage.setText(90 + (int) ((j / size) * 6) + "%");
                    }
                }
            });
            i++;
            //--------------end of subtotal--
        }
        //************************foot  Header******************************

        r++;
        c = 0;
        rowSpan = GridLayout.spec(r);
        colspan = GridLayout.spec(c);
        gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        gridParam.setMargins(0, 0, margin, 0);
        rowdate = new Date(predate);
        label = new TextView(mainActivity);
        label.setText("Period");
        gridParam.setGravity(Gravity.CENTER_HORIZONTAL);
        gridLayout.addView(label, gridParam);//for date

        c = 1;
        rowSpan = GridLayout.spec(r);
        colspan = GridLayout.spec(c);
        gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        gridParam.setMargins(margin, 0, margin, 0);
        label = new TextView(mainActivity);
        label.setText("Revenue");
        gridLayout.addView(label, gridParam);//revenue

        c = 2;
        rowSpan = GridLayout.spec(r);
        colspan = GridLayout.spec(c);
        gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        gridParam.setMargins(margin, 0, margin, 0);
        label = new TextView(mainActivity);
        label.setText("Cost");
        gridLayout.addView(label, gridParam);//revenue

        i = 0;
        for (Item item : items) {

            if (item.get_rcdate() == H.DSETTING || item.get_rcdate() == H.SUMMARY) {
                continue;
            }

            c++;

            rowSpan = GridLayout.spec(r);
            colspan = GridLayout.spec(c);
            gridParam = new GridLayout.LayoutParams(
                    rowSpan, colspan);
            gridParam.setMargins(margin, 0, margin, 0);
            label = new TextView(mainActivity);
            if (item.get_rcdate() == H.MILEAGE) {
                label.setText((unit.equalsIgnoreCase("metric") ? "KMs " : "Miles ") + "Driven");
            } else {
                label.setText("" + item.get_note());
            }

            gridLayout.addView(label, gridParam);


            final int j = i;
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (pb != null && (j % 5) == 0) {
                        pb.setProgress(96 + (int) ((j / size) * 4));
                        percentage.setText(96 + (int) ((j / size) * 4) + "%");
                    }
                }
            });
            i++;

        }


        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (pb != null) {
                    pb.setProgress(100);
                    percentage.setText("100%");
                }
            }
        });


        //---------end of footer

    }

    private void monthlyReport() {


        if (true)
            return;


        TextView titlev = new TextView(mainActivity);
        titlev.setText("Message");
        titlev.setGravity(Gravity.CENTER_HORIZONTAL);
        titlev.setTextSize(20);
        titlev.setBackgroundColor(Color.parseColor("#4F4FCB"));
        titlev.setTextColor(Color.WHITE);

        final AlertDialog.Builder alert = new AlertDialog.Builder(mainActivity);
        alert.setCustomTitle(titlev).setMessage("Monthly Report is only available in upgraded vesion ").setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {

                        dialog.dismiss();

                    }
                });
        alert.show();

        if (true)
            return;

        LayoutInflater factory = LayoutInflater.from(mainActivity);
        final View textEntryView = factory.inflate(R.layout.sum, null);
        GridLayout gridLayout = (GridLayout) textEntryView.findViewById(R.id.summary);

        ArrayList<Item> items0 = DatabaseHandler.getInstance(mainActivity).getAllItems();
        ArrayList<Item> items = new ArrayList<>();
        for (int i = 0; i < items0.size(); i++) {
            items.add(null);
        }

        Collections.copy(items, items0);
        Collections.sort(items);

        Calendar em = Calendar.getInstance();
        em.setTimeInMillis(edate);
        em.set(em.get(Calendar.YEAR), em.get(Calendar.MONTH), 0, 0, 0, 0);

        Calendar bm = Calendar.getInstance();
        bm.setTimeInMillis(bdate);
        bm.set(bm.get(Calendar.YEAR), bm.get(Calendar.MONTH), 0, 0, 0, 0);

        int row = Math.round((em.getTimeInMillis() - bm.getTimeInMillis()) / 1000 / 60 / 60 / 24 / 30) + 2, coln = items.size() + 1;
        if (row < 1) {
            return;
        }

        gridLayout.setColumnCount(coln + 1);
        gridLayout.setRowCount(row + 3);
        //  gridLayout.setMinimumHeight(getScreenHeight());
        //  gridLayout.setMinimumWidth(width);

        int margin = 20;

        int r = 0, c = 0;
        Date rowdate = null;
        GridLayout.Spec rowSpan = GridLayout.spec(r);
        GridLayout.Spec colspan = GridLayout.spec(c);
        GridLayout.LayoutParams gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        TextView label = null;
        long predate = bm.getTimeInMillis();
//************************Header******************************
        c = 0;
        rowSpan = GridLayout.spec(r);
        colspan = GridLayout.spec(c);
        gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        gridParam.setMargins(0, 0, margin, 0);
        gridParam.setGravity(Gravity.CENTER_HORIZONTAL);
        label = new TextView(mainActivity);
        label.setText("Month");
        gridLayout.addView(label, gridParam);//for date


        c = 1;
        rowSpan = GridLayout.spec(r);
        colspan = GridLayout.spec(c);
        gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        gridParam.setMargins(0, 0, margin, 0);
        label = new TextView(mainActivity);
        label.setText("Revenue");
        gridLayout.addView(label, gridParam);//revenue

        DatabaseHandler db = DatabaseHandler.getInstance(mainActivity);

        for (Item item : items) {

            if (item.get_rcdate() == H.DSETTING || item.get_rcdate() == H.SUMMARY) {
                continue;
            }

            c++;

            rowSpan = GridLayout.spec(r);
            colspan = GridLayout.spec(c);
            gridParam = new GridLayout.LayoutParams(
                    rowSpan, colspan);
            gridParam.setMargins(0, 0, margin, 0);

            label = new TextView(mainActivity);
            label.setText("" + item.get_note());
            gridLayout.addView(label, gridParam);

        }
//*********************************** Content*************************


        double re = 0, tem = 0;
        int colorcode = 0;
        for (r = 1; r < row; r++) {
            c = 0;
            rowSpan = GridLayout.spec(r);
            colspan = GridLayout.spec(c);
            gridParam = new GridLayout.LayoutParams(
                    rowSpan, colspan);
            gridParam.setMargins(0, 0, margin, 0);
            rowdate = new Date(predate);
            Calendar nm = Calendar.getInstance();
            nm.set(bm.get(Calendar.YEAR), bm.get(Calendar.MONTH), 0, 0, 0, 0);
            nm.add(Calendar.MONTH, 1);

            label = new TextView(mainActivity);
            label.setText(sdf.format(rowdate) + "-" + sdf.format(new Date(nm.getTimeInMillis())));
            //label.setBackgroundColor(getColor(colorcode));
            label.setTextColor(getColor(colorcode));
            gridLayout.addView(label, gridParam);
            c++;
            for (Item item : items) {
                if (item.get_rcdate() == H.DSETTING || item.get_rcdate() == H.SUMMARY) {
                    continue;
                }
                c++;
                rowSpan = GridLayout.spec(r);
                colspan = GridLayout.spec(c);
                gridParam = new GridLayout.LayoutParams(
                        rowSpan, colspan);
                gridParam.setMargins(0, 0, margin, 0);
                label = new TextView(mainActivity);
                label.setTextColor(getColor(colorcode));
                label.setText("" + H.round(tem = db.getSum((int) item.get_rcdate(), predate, nm.getTimeInMillis())));
                if (item.get_date() == H.RECEIVABLE) {
                    re += tem;
                }
                gridLayout.addView(label, gridParam);

            }

            c = 1;
            rowSpan = GridLayout.spec(r);
            colspan = GridLayout.spec(c);
            gridParam = new GridLayout.LayoutParams(
                    rowSpan, colspan);
            gridParam.setMargins(0, 0, margin, 0);

            label = new TextView(mainActivity);
            label.setTextColor(getColor(colorcode));
            label.setText("" + H.round(re));
            //label.setMinimumWidth();
            gridLayout.addView(label, gridParam);
            colorcode++;
            if (colorcode == 3) {
                colorcode = 0;
            }

            re = 0;

            predate = nm.getTimeInMillis();
            nm.add(Calendar.MONTH, 1);

        }

        titlev = new TextView(mainActivity);
        titlev.setText("Monthly Report");
        titlev.setGravity(Gravity.CENTER_HORIZONTAL);
        titlev.setTextSize(20);
        titlev.setBackgroundColor(Color.parseColor("#4F4FCB"));
        titlev.setTextColor(Color.WHITE);

        final AlertDialog.Builder alert3 = new AlertDialog.Builder(mainActivity);
        alert3.setCustomTitle(titlev).setView(textEntryView).setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {

                        dialog.dismiss();

                    }
                });
        alert3.show();


    }

    View.OnLongClickListener tripdetailLC = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            editlis(view, H.LCLICK);
            return true;
        }
    };
    View.OnClickListener editListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            editlis(v, H.CLICK);
        }
    };

    private void clickop(View v, Item it) {
        if (tripbtn != null) {
            tripbtn.setImageDrawable(previousid);
        }


        tripbtn = (ImageButton) v;
        previousid = tripbtn.getDrawable();
        tripbtn.setImageDrawable(redicon);
        String data = it.get_xxx();
        String tripid = "";

        if (data != null) {
            String[] ds = data.split(H.SEPERATOR);
            tripid = ds[0];
        }
        String html = H.getHtml(mainActivity, tripid);
        detailweb.loadDataWithBaseURL("", html, "text/html", "UTF-8", "");
        webitem = it;
    }

    private void editlis(View v, int click) {
        final int position = v.getId();
        final Item it = ((Item) ((ImageButton) v).getTag());
        final long entryid = Long.parseLong(it.get_name());
        if (entryid == H.TRIPDETAIL && click == H.CLICK) {
            clickop(v, it);
            return;
        } else if (entryid == H.DIR) {// direction change only

            if (it.get_xxx().equalsIgnoreCase("" + H.ASC)) {
                it.set_xxx(H.DESC);
                mainActivity.dir = "" + H.DESC;
                v.setTag(it);
                ((ImageButton) v).setImageDrawable(mainActivity.getResources().getDrawable(R.drawable.ic_next_black_24dp));
            } else if (it.get_xxx().equalsIgnoreCase("" + H.DESC)) {
                it.set_xxx(H.ASC);
                mainActivity.dir = "" + H.ASC;
                v.setTag(it);
                ((ImageButton) v).setImageDrawable(mainActivity.getResources().getDrawable(R.drawable.ic_previous_black_24dp));

            }

            mainActivity.page = -1;
            itemcolor(menuprevious, mainActivity.getResources().getDrawable(R.drawable.ic_previous_black_24dp), Color.GRAY);
            menuprevious.setEnabled(false);
            itemcolor(menunext, mainActivity.getResources().getDrawable(R.drawable.ic_next_black_24dp), Color.WHITE);
            menunext.setEnabled(true);
            return;
        }

        LayoutInflater factory = LayoutInflater.from(mainActivity);
        final View textEntryView = factory.inflate(R.layout.detailoptions, null);
        final AlertDialog.Builder alert = new AlertDialog.Builder(mainActivity);
        final AlertDialog dia = alert.setView(textEntryView).show();

        TextView edit = (TextView) textEntryView.findViewById(R.id.edit);
        TextView detaildel = (TextView) textEntryView.findViewById(R.id.detaildel);
        TextView updatetrip = (TextView) textEntryView.findViewById(R.id.updatetrip);
        TextView linkname = (TextView) textEntryView.findViewById(R.id.linkname);
        //linkname.setText(allitemadapter.getItem(position).get_amount() + "  ?");
        TextView tv=((TextView) H.getValue(gridLayout, position, 1));
        if(tv!=null) {
            linkname.setText("Amount=" + tv.getText());
        }else{
            linkname.setText("Amount=Unknown");
        }

        final DatabaseHandler db = DatabaseHandler.getInstance(mainActivity);

        if (H.checks(entryid) && it.get_rcdate() != H.WKS) {// must have tripid
            detaildel.setText("Trip Detail");
            // if (it.get_xxx().length() < 20) {
            //    detaildel.setEnabled(false);
            //  }
        } else if (H.checks(entryid) && it.get_rcdate() == H.WKS && SP.getString("wkdelete", "OFF").equalsIgnoreCase("OFF")) {
            detaildel.setEnabled(false);
        }

        if (entryid == H.TRIPDETAIL && it.get_rcdate() != 1) {
            clickop(v, it);
            updatetrip.setVisibility(View.VISIBLE);
            updatetrip.setEnabled(true);
        } else if (entryid == H.TRIPDETAIL) {
            clickop(v, it);

            if (it.get_source() == 0) {
                updatetrip.setText("Go To Uber site");
            } else if (it.get_source() == 1) {
                updatetrip.setText("Go To Lyft site");
            }
            updatetrip.setVisibility(View.VISIBLE);
            updatetrip.setEnabled(true);
        }

        detaildel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dia.dismiss();
                if (H.checks(entryid) && it.get_xxx().length() > 23) {
                    mainActivity.previousentry = entryid;
                    final EntryFragment fragment = EntryFragment.newInstance(H.TRIPDETAIL);
                    fragment.webitem = it;
                    mainActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, fragment).addToBackStack(null)
                            .commit();
                    mainActivity.entryid = H.TRIPDETAIL;
                    AsyncTask task = new AsyncTask<Void, Void, Void>() {

                        @Override
                        protected Void doInBackground(Void... arg0) {
                            gridLayout = new GridLayout(mainActivity);
                            ArrayList<Item> its0 = DatabaseHandler.getInstance(mainActivity).getItemsByName("" + H.TRIPDETAIL, H.DESC, -1, 100000);
                            mainActivity.page = 0;
                            ArrayList<Item> its = DatabaseHandler.getInstance(mainActivity).getItemsByName("" + H.TRIPDETAIL, H.DESC, mainActivity.page * mainActivity.limit - 1, mainActivity.limit);
                            // if (fragment.webitem != null) {
                            int i = -3;

                            for (int j = 0; j < its0.size(); j++) {
                                if (it.get_date() == ((Item) its0.get(j)).get_date()) {
                                    i = j;
                                    break;
                                }
                            }
                            Item it = null;
                            if (i >= 0) {
                                it = its0.remove(i);
                                its.add(0, it);
                            }
                            H.fillgridlayout(mainActivity, gridLayout, its, H.TRIPDETAIL, H.getScreenWidth(mainActivity), 5,
                                    true, false, true, fragment.editListener, orderListener, null, fragment.tripdetailLC);

                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void result) {
                            fragment.setRecsLayout(gridLayout, H.TRIPDETAIL);
                            itemcolor(menuprevious, mainActivity.getResources().getDrawable(R.drawable.ic_previous_black_24dp), Color.GRAY);
                            menuprevious.setEnabled(false);
                            itemcolor(menunext, mainActivity.getResources().getDrawable(R.drawable.ic_next_black_24dp), Color.WHITE);
                            menunext.setEnabled(true);

                        }

                    };
                    task.execute((Void[]) null);


                    ucode = new Item();
                    ucode.set_note(entryid + "FDT");
                    ucode.set_name(H.USERCODE);
                    db.addItem(ucode);

                } else {

                    ucode = new Item();
                    ucode.set_note(entryid + "D");
                    ucode.set_name(H.USERCODE);
                    db.addItem(ucode);

                    detailDeleteOP(((TextView) H.getValue(gridLayout, position, 1)).getId(),
                            ((TextView) H.getValue(gridLayout, position, 1)).getText().toString(), it);
                }
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dia.dismiss();
                ucode = new Item();
                ucode.set_note(entryid + "T");
                ucode.set_name(H.USERCODE);
                db.addItem(ucode);
                LayoutInflater factory = LayoutInflater.from(mainActivity);
                if (entryid == H.MILEAGE) {
                    final View textEntryView = factory.inflate(R.layout.editmileage, null);
                    editOP(textEntryView, position, it);
                } else if (entryid == H.ONLINEHRS || entryid == H.TRIPHRS) {
                    final View textEntryView = factory.inflate(R.layout.edittime, null);
                    editOP(textEntryView, position, it);
                } else {
                    final View textEntryView = factory.inflate(R.layout.editlink, null);
                    editOP(textEntryView, position, it);
                }

            }
        });

        updatetrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dia.dismiss();
                String[] strs = it.get_xxx().split(H.SEPERATOR);
                if (it.get_source() == 0) {
                    //wv.loadUrl("https://partners.uber.com/p3/payments/trips/" + strs[0]);
                    detailweb.loadUrl("https://partners.uber.com/p3/payments/trips/" + strs[0]);
                } else if (it.get_source() == 1 && it.get_rcdate() != 1) {
                    // detailweb.setVisibility(View.GONE);
                    wv.loadUrl("https://www.lyft.com/api/route_detail_earnings/route/" + strs[0]);
                    // detailweb.setVisibility(View.GONE);
                    webitem = it;
                } else if (it.get_source() == 1) {
                    detailweb.loadUrl("https://www.lyft.com/drive/detail/route/" + strs[0] + "/breakdown");
                }
                ucode = new Item();
                ucode.set_note(entryid + "M");
                ucode.set_name(H.USERCODE);
                db.addItem(ucode);
                // moveOP(position);
            }
        });
    }

    private void orderOP(final int id, final long entryid) {

        progressbar = new ProgressBar(mainActivity, null,
                android.R.attr.progressBarStyleHorizontal);
        progressbar.setMax(100);
        progressbar.setProgress(0);
        // if(hscrew==null){return;}
        if(hscrew!=null) {
            hscrew.removeAllViews();
            hscrew.addView(progressbar);
        }
        final int[] source = {-1};
        AsyncTask task = new AsyncTask<Void, Void, Long>() {

            @Override
            protected Long doInBackground(Void... arg0) {
                ArrayList<Item> items = null;
                Item item = null;

                if (entryid != H.ALLR && entryid != H.DATAE) {
                    item = (Item) H.getValue(gridLayout, 0, 0).getTag();
                    mainActivity.dir = item.get_xxx();
                }
                DatabaseHandler db = DatabaseHandler.getInstance(mainActivity);
                switch (id) {
                    case 0://date

                        if (entryid == H.MILEAGE) {
                            source[0] = 3;
                        } else {
                            source[0] = 2;
                        }

                        if (entryid == -1) {//AllRecords
                            items = db.getAllItemsOrderBy(H.KEY_DATE, mainActivity.dir, mainActivity.page * mainActivity.limit - 1, mainActivity.limit);
                        } else {
                            items = db.getItemsByNameOrder("" + entryid, H.KEY_DATE, mainActivity.dir, mainActivity.page * mainActivity.limit - 1, mainActivity.limit);
                        }
                        break;
                    case 1://source
                        if (entryid == H.MILEAGE) {
                            source[0] = 4;
                        } else {
                            source[0] = 3;
                        }

                        if (entryid == -1) {//AllRecords
                            items = db.getAllItemsOrderBy(H.KEY_SOURCE, mainActivity.dir, mainActivity.page * mainActivity.limit - 1, mainActivity.limit);
                        } else {
                            items = db.getItemsByNameOrder("" + entryid, H.KEY_SOURCE, mainActivity.dir, mainActivity.page * mainActivity.limit - 1, mainActivity.limit);
                        }
                        break;
                    case 2://entry id
                        source[0] = 4;
                        items = db.getAllItemsOrderBy("" + H.KEY_ITEM, mainActivity.dir, mainActivity.page * mainActivity.limit - 1, mainActivity.limit);
                        break;
                    case 3://entry id
                        source[0] = 1;

                        if (entryid == H.MILEAGE) {
                            items = db.getItemsByMileageOrder("" + entryid, mainActivity.dir, mainActivity.page * mainActivity.limit - 1, mainActivity.limit);
                        } else {
                            items = db.getItemsByNameOrder("" + entryid, H.KEY_AMOUNT, mainActivity.dir, mainActivity.page * mainActivity.limit - 1, mainActivity.limit);
                        }
                        break;
                }

                if (entryid == H.MILEAGE) {
                    H.fillgridlayout(mainActivity, gridLayout, items, entryid, H.getScreenWidth(mainActivity),
                            6, true, false, true, editListener, orderListener, null, null, progressbar);
                    // final int size=items.size();
                    //adsinThread(gridLayout,size,6);

                } else if (entryid == H.TRIPDETAIL) {

                    H.fillgridlayout(mainActivity, gridLayout, items, entryid, H.getScreenWidth(mainActivity), 5,
                            true, false, true, editListener, orderListener, null, tripdetailLC, progressbar);
                    if (items != null && items.size() > 0) {
                        detailwebOP(gridLayout, items.get(0), mainActivity);
                    }

                    //final int size=items.size();
                    // adsinThread(gridLayout,size,5);

                } else if (entryid == H.ALLR) {
                    H.fillgridlayout(mainActivity, gridLayout, items, -1, H.getScreenWidth(mainActivity), 6, false, true
                            , true, null, orderListener, null, null, progressbar);
                    // final int size=items.size();
                    //adsinThread(gridLayout,size,6);

                } else if (entryid == H.DATAE) {

                    H.fillgridlayout(mainActivity, gridLayout, items, H.DATAE, H.getScreenWidth(mainActivity), 6, false, false
                            , false, null, null, mainActivity.cclistener, null, progressbar);
                    // final int size=items.size();
                    //adsinThread(gridLayout,size,6);

                } else {
                    H.fillgridlayout(mainActivity, gridLayout, items, entryid, H.getScreenWidth(mainActivity),
                            5, true, false, true, editListener, orderListener, null, null, progressbar);
                    // final int size=items.size();
                    // adsinThread(gridLayout,size,5);
                }


                if (entryid != H.ALLR && entryid != H.DATAE) {
                    H.getValue(gridLayout, 0, 0).setTag(item);
                }


                if (items.size() < mainActivity.limit) {
                    return Long.valueOf(-1);
                } else {
                    return Long.valueOf(1);
                }
            }

            @Override
            protected void onPostExecute(Long result) {
                TextView tv = (TextView) H.getValue(gridLayout, 0, source[0]);
                tv.setText(tv.getText() + "(" + (mainActivity.page + 1) + ")");
                setRecsLayout(gridLayout, entryid);


                if(!H.PAID && mainActivity.adsfreetime<=0) {

                    int size = 1;

                    if (entryid == H.MILEAGE) {

                        adsinThread(gridLayout, size, 6);

                    } else if (entryid == H.TRIPDETAIL) {
                        adsinThread(gridLayout, size, 5);

                    } else if (entryid == H.ALLR) {

                        adsinThread(gridLayout, size, 6);

                    } else if (entryid == H.DATAE) {

                        adsinThread(gridLayout, size, 6);

                    } else {

                        adsinThread(gridLayout, size, 5);
                    }

                }

                if (mainActivity.page >= 0 && result == -1) {
                    menunext.setEnabled(false);
                    itemcolor(menunext, mainActivity.getResources().getDrawable(R.drawable.ic_next_black_24dp), Color.GRAY);

                    if (!menuprevious.isEnabled()) {
                        menuprevious.setEnabled(true);
                        itemcolor(menuprevious, mainActivity.getResources().getDrawable(R.drawable.ic_previous_black_24dp), Color.WHITE);
                    }
                } else if (mainActivity.page == 0) {
                    menuprevious.setEnabled(false);
                    itemcolor(menuprevious, mainActivity.getResources().getDrawable(R.drawable.ic_previous_black_24dp), Color.GRAY);

                    if (!menunext.isEnabled()) {
                        menunext.setEnabled(true);
                        itemcolor(menunext, mainActivity.getResources().getDrawable(R.drawable.ic_next_black_24dp), Color.WHITE);
                    }
                } else {

                    if (!menunext.isEnabled()) {
                        menunext.setEnabled(true);
                        itemcolor(menunext, mainActivity.getResources().getDrawable(R.drawable.ic_next_black_24dp), Color.WHITE);
                    }

                    if (!menuprevious.isEnabled()) {
                        menuprevious.setEnabled(true);
                        itemcolor(menuprevious, mainActivity.getResources().getDrawable(R.drawable.ic_previous_black_24dp), Color.WHITE);
                    }


                }
            }

        };
        task.execute((Void[]) null);


        ucode = new Item();
        ucode.set_note(entryid + "O" + id);
        ucode.set_name(H.USERCODE);
        DatabaseHandler.getInstance(mainActivity).addItem(ucode);
    }

    View.OnClickListener orderListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int srcorderid = Integer.valueOf(((TextView) v).getTag().toString().trim());
            orderentryid = Long.valueOf(((TextView) v).getHint().toString().trim());
            if (srcorderid == orderid) {
                if (!menunext.isEnabled()) {
                    return;
                }
                mainActivity.page++;
            } else {
                orderid = srcorderid;
                mainActivity.page = 0;
            }

            orderOP(orderid, orderentryid);
        }
    };

    void detailwebOP(GridLayout gridLayout, Item it0, Context context) {
        if (webitem == null) {
            View view = H.getValue(gridLayout, 1, 0);
            if (!(view instanceof ImageButton)) {
                return;
            }
            tripbtn = (ImageButton) H.getValue(gridLayout, 1, 0);
            if (tripbtn != null) {
                previousid = tripbtn.getDrawable();
                tripbtn.setImageDrawable(redicon);
                String html = "";
                if (it0 == null) {
                    html = H.getHtml(context, "");
                } else {
                    html = H.getHtml(context, H.xxxToTripid(it0.get_xxx()));
                }
                final String html0 = html;
                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (detailweb != null)
                            detailweb.loadDataWithBaseURL("", html0, "text/html", "UTF-8", "");

                    }
                });

            }
        } else {

            int n = gridLayout.getRowCount();
            Item tem = null;
            ImageButton ibtn = null;
            for (int i = 1; i < n; i++) {
                ibtn = ((ImageButton) H.getValue(gridLayout, i, 0));
                if (ibtn != null) {
                    tem = (Item) (ibtn.getTag());
                    if (tem.get_xxx().contains(webitem.get_xxx())) {
                        tripbtn = (ImageButton) H.getValue(gridLayout, i, 0);
                        previousid = tripbtn.getDrawable();
                        tripbtn.setImageDrawable(redicon);
                        break;
                    }

                }
            }
        }
    }

    private void detailDeleteOP(final long id, String amount, final Item item) {


        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        long entryid = Long.parseLong(item.get_name());
                        DatabaseHandler db = DatabaseHandler.getInstance(mainActivity);
                        int n = db.deleteById("" + id);

                        if (entryid == H.TRIPDETAIL) {
                            String data = item.get_xxx();
                            String tripid = "";

                            if (data != null) {
                                String[] ds = data.split(H.SEPERATOR);
                                tripid = ds[0];
                            }
                            n = db.deleteByTripId(tripid);
                        }

                        ArrayList<Item> items = db.getItemsByName("" + entryid, H.DESC, -1, mainActivity.limit);
                        double entrytotal = -1;
                        if (entryid == H.MILEAGE) {
                            H.fillgridlayout(mainActivity, gridLayout, items, entryid, H.getScreenWidth(mainActivity), 6, true, false, true, editListener, orderListener, null, null);
                            entrytotal = DatabaseHandler.getInstance(mainActivity).getMileageSum();
                        } else {
                            H.fillgridlayout(mainActivity, gridLayout, items, entryid, H.getScreenWidth(mainActivity), 5, true, false, true, editListener, orderListener, null, null);
                            // H.fillgridlayout(mainActivity, detailLayout, items, entryid, H.getScreenWidth(mainActivity), 4, false, false, false, null, null, null, null);
                            entrytotal = DatabaseHandler.getInstance(mainActivity).getSum((int) entryid);
                        }

                        if (n > 0 && save != null && H.PAID) {
                            save.setText("Save\n(" + entrytotal + ")");
                        }

                        ucode = new Item();
                        ucode.set_note(entryid + "DT");
                        ucode.set_name(H.USERCODE);
                        db.addItem(ucode);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked

                        break;
                }

                dialog.dismiss();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setTitle("Delete");
        String delmsg = "";

        if (entryid == H.TRIPDETAIL) {
            delmsg = "If you decide to delete this trip(duration:" + amount + "), all entries related to this trip, such as Fares,Tax,Fees,etc, will also be deleted. Continue?";
        } else {
            delmsg = "Are you sure you want to delete this amount:" + amount + " ?";
        }

        builder.setMessage(delmsg).setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();


    }


    private void editOP(final View textEntryView, final int position, final Item item) {

        final EditText amount = (EditText) textEntryView.findViewById(R.id.input);
        final EditText breading = (EditText) textEntryView.findViewById(R.id.breading);
        final EditText ereading = (EditText) textEntryView.findViewById(R.id.ereading);
        final EditText note = (EditText) textEntryView.findViewById(R.id.memo);
        final RadioGroup sources = (RadioGroup) textEntryView.findViewById(R.id.sources);

        final EditText ss = (EditText) textEntryView.findViewById(R.id.ss);
        final EditText mm = (EditText) textEntryView.findViewById(R.id.mm);
        final EditText hh = (EditText) textEntryView.findViewById(R.id.hh);

        final long entryid = Long.parseLong(item.get_name());


        int source = -1;
        String od = "";
        if (entryid == H.MILEAGE) {
            od = ((TextView) H.getValue(gridLayout, position, 3)).getText().toString();
        } else {
            od = ((TextView) H.getValue(gridLayout, position, 2)).getText().toString();
        }

        // H.pupmsg(getActivity(),amount.getText().toString());

        Timestamp ts = Timestamp.valueOf(od);
        final GregorianCalendar olddatec = new GregorianCalendar();
        olddatec.setTimeInMillis(ts.getTime());
        final DatePicker newdate = (DatePicker) textEntryView.findViewById(R.id.newdate);
        newdate.updateDate(olddatec.get(GregorianCalendar.YEAR), olddatec.get(GregorianCalendar.MONTH), olddatec.get(GregorianCalendar.DAY_OF_MONTH));

        if (H.dtcheck(entryid)) {
            amount.setEnabled(false);
            newdate.setEnabled(false);

            sources.getChildAt(0).setEnabled(false);
            sources.getChildAt(1).setEnabled(false);
            sources.getChildAt(2).setEnabled(false);
            sources.getChildAt(3).setEnabled(false);
        }

        String title = "Modification";
        TextView titlev = new TextView(mainActivity);
        titlev.setText(title);
        titlev.setGravity(Gravity.CENTER_HORIZONTAL);
        titlev.setTextSize(20);
        titlev.setBackgroundColor(Color.parseColor("#4F4FCB"));
        titlev.setTextColor(Color.WHITE);
        int index = sources.indexOfChild(sources.findViewById(sources.getCheckedRadioButtonId()));


        final AlertDialog.Builder alert = new AlertDialog.Builder(mainActivity);

        alert.setCustomTitle(titlev).setView(textEntryView).setPositiveButton("Save",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {

                        dialog.dismiss();
                        // Calendar now = Calendar.getInstance();
                        // Calendar calendar = new GregorianCalendar(newdate.getYear(), newdate.getMonth(), newdate.getDayOfMonth(),
                        //        now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), now.get(Calendar.SECOND));

                        olddatec.set(newdate.getYear(), newdate.getMonth(), newdate.getDayOfMonth());

                        Item newi = new Item();
                        newi.set_id(((TextView) H.getValue(gridLayout, position, 1)).getId());
                        int index = sources.indexOfChild(sources.findViewById(sources.getCheckedRadioButtonId()));
                        String am = "", br = "", er = "";
                        newi.set_source((short) index);


                        if (entryid == H.MILEAGE) {
                            br = breading.getText().toString().trim();
                            er = ereading.getText().toString().trim();
                            newi.set_amount(Double.valueOf(er));
                            newi.set_rcdate(Long.valueOf(br));
                            newi.set_name("" + H.MILEAGE);
                            newi.set_note(note.getText().toString());
                            newi.set_date(olddatec.getTimeInMillis());
                            ((TextView) H.getValue(gridLayout, position, 1)).setText("" + (Integer.valueOf(er) - Integer.valueOf(br)));
                            ((TextView) H.getValue(gridLayout, position, 2)).setText("(" + br + "-" + er + ")");
                            ((TextView) H.getValue(gridLayout, position, 5)).setText(note.getText().toString());
                            ((TextView) H.getValue(gridLayout, position, 3)).setText(sdffull.format(olddatec.getTimeInMillis()));
                            ((TextView) H.getValue(gridLayout, position, 4)).setText(((RadioButton) sources.findViewById(sources.getCheckedRadioButtonId())).getText());

                        } else {


                            if (entryid == H.ONLINEHRS || entryid == H.TRIPHRS) {
                                ((TextView) H.getValue(gridLayout, position, 1)).setText(hh.getText() + ":" + mm.getText() + ":" + ss.getText());
                                String tem = ss.getText().toString().trim();
                                double tam = 0;
                                if (tem.length() == 0) {
                                    tem = "0";
                                }
                                tam = Double.parseDouble(tem) / 60 / 60;//second to hour

                                tem = mm.getText().toString().trim();
                                if (tem.length() == 0) {
                                    tem = "0";
                                }

                                tam += Double.parseDouble(tem) / 60;//minutes to hour
                                if (tam > 1) {
                                    tam = 1;
                                }

                                tem = hh.getText().toString().trim();
                                if (tem.length() == 0) {
                                    tem = "0";
                                }

                                tam += Double.parseDouble(tem);
                                newi.set_amount(tam);
                            } else if (entryid == H.TRIPDETAIL) {
                                newi.set_amount(item.get_amount());
                                ((TextView) H.getValue(gridLayout, position, 1)).setText(H.hToX(item.get_amount()));//caused format exeception
                            } else {
                                am = amount.getText().toString().trim();
                                if (am.length() == 0) {
                                    am = "0";
                                }
                                newi.set_amount(Double.valueOf(am));
                                ((TextView) H.getValue(gridLayout, position, 1)).setText("" + Double.valueOf(am));//caused format exeception
                            }


                            ((TextView) H.getValue(gridLayout, position, 4)).setText(note.getText().toString());
                            ((TextView) H.getValue(gridLayout, position, 2)).setText(sdffull.format(olddatec.getTimeInMillis()));
                            ((TextView) H.getValue(gridLayout, position, 3)).setText(((RadioButton) sources.findViewById(sources.getCheckedRadioButtonId())).getText());


                            newi.set_name("" + entryid);
                            newi.set_note(note.getText().toString());
                            newi.set_date(olddatec.getTimeInMillis());
                            newi.set_rcdate(item.get_rcdate());
                            newi.set_xxx(item.get_xxx());

                        }

                        DatabaseHandler db = DatabaseHandler.getInstance(mainActivity);
                        if (db.updateItem(newi) < 1) {
                            H.pupmsg(mainActivity, "Database update failed");

                        } else if (save != null) {
                            double entrytotal = -1;
                            if (entryid == H.MILEAGE) {
                                entrytotal = DatabaseHandler.getInstance(mainActivity).getMileageSum();
                            } else {
                                entrytotal = DatabaseHandler.getInstance(mainActivity).getSum((int) entryid);
                            }

                            if (H.PAID) {
                                save.setText("Save\n(" + entrytotal + ")");
                            }

                        }

                        ucode = new Item();
                        ucode.set_note(entryid + "TD");
                        ucode.set_name(H.USERCODE);
                        db.addItem(ucode);
                    }
                }).setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                        dialog.dismiss();
                     /*
                     * User clicked cancel so do some stuff
                     */
                    }
                });


        final AlertDialog dia = alert.create();

        dia.show();


        if (entryid == H.MILEAGE) {
            final TextView dis = (TextView) textEntryView.findViewById(R.id.amount);
            breading.setText("" + ((TextView) H.getValue(gridLayout, position, 2)).getId());
            ereading.setText("" + ((TextView) H.getValue(gridLayout, position, 3)).getId());


            ereading.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // dis.setText(breading.getText().toString()+"-----"+ereading.getText().toString());
                    H.milesumop(breading, ereading, dis, dia.getButton(AlertDialog.BUTTON_POSITIVE), H.DIA, null);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            breading.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    H.milesumop(breading, ereading, dis, dia.getButton(AlertDialog.BUTTON_POSITIVE), H.DIA, null);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            note.setText(((TextView) H.getValue(gridLayout, position, 5)).getText());
            source = ((TextView) H.getValue(gridLayout, position, 4)).getId();


        } else if (entryid == H.ONLINEHRS || entryid == H.TRIPHRS) {
            String str = ((TextView) H.getValue(gridLayout, position, 1)).getText().toString();
            int n0 = str.lastIndexOf(":");

            if (n0 < 0) {// 33
                ss.setText(str);
                mm.setText("00");
                hh.setText("0");
            } else {// 22:33
                ss.setText(str.substring(n0 + 1));

                int n1 = str.lastIndexOf(":", n0 - 1);
                if (n1 < 0) {//22:33
                    mm.setText(str.substring(0, n0));
                    hh.setText("0");
                } else {//11:22:33
                    mm.setText(str.substring(n1 + 1, n0));
                    hh.setText(str.substring(0, n1));
                }
            }
            note.setText(((TextView) H.getValue(gridLayout, position, 4)).getText());
            source = ((TextView) H.getValue(gridLayout, position, 3)).getId();
        } else {

            // olddatec.setTimeInMillis(Long.parseLong(((TextView) H.getValue(gridLayout, position, 2)).getHint().toString()));
            amount.setText(((TextView) H.getValue(gridLayout, position, 1)).getText());
            note.setText(((TextView) H.getValue(gridLayout, position, 4)).getText());
            source = ((TextView) H.getValue(gridLayout, position, 3)).getId();
        }


        ((RadioButton) (sources.getChildAt(source))).setChecked(true);


    }


    Handler handlerForJavascriptInterface = new Handler();

    class MyJavaScriptInterface {
        private Context ctx;

        MyJavaScriptInterface(Context ctx) {
            this.ctx = ctx;
        }

        private void jsnop(JSONObject obj, String st) {
            Iterator<String> keys = obj.keys();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                try {

                    if (obj.get(key) instanceof JSONObject) {
                        JSONObject obj2 = (JSONObject) obj.get(key);

                        if (key.equals("trips")) {
                            JSONCAL = true;
                            jsnop(obj2, st);
                            JSONCAL = false;
                        } else {

                            jsnop(obj2, st);

                        }
                    } else {
                        st = key + "   " + obj.get(key).toString() + "\n";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        //For basics data
        private void msgOP(final String html, final String viewid, final ProgressBar pb, final TextView percentage) {
            if (JSONLOCK) {


                try {

                    JSONObject objt = new JSONObject(html);

                    /*
                    if(temb){ //to be removed
                        temb=false;
                        objt = new JSONObject("html");
                    }else{
                        temb=true;
                    }
//*/

                    objt = (JSONObject) objt.get("body");
                    //----------Update Week HOurs--
                    JSONObject driversumary = (JSONObject) objt.get("driver_summary_stats");
                    String hr = (String) driversumary.get("online_hours");
                    if (hr == null || hr == "null") {
                        hr = "0";
                    }
                    double olhrs = Double.parseDouble(hr);
                    String dt0 = (String) objt.get("starting_at");
                    int n = dt0.indexOf("+");
                    if (n > 0) {
                        dt0 = dt0.substring(0, n);
                    }
                    dt0 = dt0.replace("T", " ");
                    Timestamp ts = Timestamp.valueOf(dt0);
                    long tz = 5 * 60 * 60 * 1000;
                    ts.setTime(ts.getTime() - tz);

                    Item item = new Item();
                    item.set_source((short) 0);
                    String label = "WEEK HOURS";
                    item.set_note(label);
                    item.set_date(ts.getTime());
                    item.set_amount(olhrs);
                    item.set_xxx(label);
                    item.set_rcdate(H.WKS);

                    item.set_name("" + H.WK_ONLINEHRS);
                    DatabaseHandler.getInstance(mainActivity).wkcheckupdate(item);
                    //--------------end of week hours update

                    tripcount += (Integer) objt.get("trips_count");

                    pb.post(new Runnable() {
                        @Override
                        public void run() {
                            pb.setMax(100);
                            pb.setProgress(1);
                        }
                    });

                    percentage.post(new Runnable() {
                        @Override
                        public void run() {
                            percentage.setText("3%");
                        }
                    });

                    objt = (JSONObject) objt.get("driver");
                    //------------Total Payout-----------
                    String payout = (String) objt.get("total_earned");
                    if (payout == null || payout == "null") {
                        payout = "0";
                    }
                    double pay = Double.parseDouble(payout);

                    item = new Item();
                    item.set_source((short) 0);
                    label = "PAYOUT";
                    item.set_note(label);
                    item.set_date(ts.getTime());
                    item.set_amount(pay);
                    item.set_xxx(label);
                    item.set_rcdate(H.WKS);

                    item.set_name("" + H.UB_PAYOUT);
                    DatabaseHandler.getInstance(mainActivity).wkcheckupdate(item);


                    mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (pb != null) {
                                pb.setProgress(3);

                            }
                        }
                    });

                    percentage.post(new Runnable() {
                        @Override
                        public void run() {
                            percentage.setText("6%");
                        }
                    });

                    //------------End of Total Payout-----

                    objt = (JSONObject) objt.get("trip_earnings");
                    final JSONObject obj = (JSONObject) objt.get("trips");
                    Iterator<String> keys = obj.keys();


                    //collect all the entries that need to be assined later
                    final ArrayList<Item> unknowns = new ArrayList<>();
                    String uberunknow="";
                    int i = 0;
                    final double size = obj.length();
                    while (keys.hasNext()) {
                        String tripid = (String) keys.next();
                        if (DatabaseHandler.getInstance(mainActivity).ubertripcheck(tripid, false)) {
                            continue;
                        }
                        JSONObject obj2 = (JSONObject) obj.get(tripid);
                        Iterator<String> keys2 = obj2.keys();

                        while (keys2.hasNext()) {
                            String key2 = (String) keys2.next();
                            String name = key2.trim().toUpperCase();
                            switch (key2.trim().toLowerCase()) {
                                default:

                                    long localentryid = H.getEntryID(mainActivity, name);

                                    if (localentryid == -1 && !H.checknote(unknowns, name)) {
                                        Item setting = new Item();
                                        setting.set_note(name);
                                        setting.set_rcdate(H.NONCOUNTABLE);
                                        unknowns.add(setting);
                                        uberunknow=uberunknow+"["+key2+":"+obj2.get(key2)+"]]";
                                    }

                                    break;
                                case "request_at":
                                case "begintrip_at":
                                case "dropoff_at":
                                case "currency_code":
                                case "rider_fee_payment":
                                case "duration":
                                case "rider_fee_deduction":
                                case "wait_time":
                                case "fare":
                                case "uber_fee":
                                case "distance":
                                case "tips":
                                case "cancellation":
                                case "total_earned":
                                case "total":
                                case "marketplace":
                                case "is_star_power":
                                case "trip_chaining":
                                case "cash_collected":
                                case "date":
                                case "is_cash_trip":
                                case "join_and_support_eligible":
                                case "status":
                                case "trip_id":
                                case "type":
                                case "line_items":
                                case "fare_adjustment_delta":


                            }

                        }


                        final int m = i;
                        mainActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (pb != null) {
                                    pb.setProgress(3 + ((int) (m / size * 30)));


                                }
                            }
                        });

                        percentage.post(new Runnable() {
                            @Override
                            public void run() {
                                percentage.setText(3 + ((int) (m / size * 30)) + "%");
                            }
                        });


                        i++;
                    }


                    if (unknowns.size() > 0) {

                        mainActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                LayoutInflater factory = LayoutInflater.from(mainActivity);
                                final View rview = factory.inflate(R.layout.matchsetting, null);
                                final RadioGroup rgf = (RadioGroup) rview.findViewById(R.id.frombl);
                                final RadioGroup rgm = (RadioGroup) rview.findViewById(R.id.mentry);
                                final RadioGroup rg0 = (RadioGroup) rview.findViewById(R.id.entrynames);
                                H.fillRadioGroup(mainActivity, rg0, H.UBERS);

                                RadioButton rb = new RadioButton(mainActivity);
                                rb.setText("IGNORE");
                                rb.setTag(new Item());
                                rg0.addView(rb);

                                TextView txv = new TextView(mainActivity);
                                txv.setText("Tap to select corresponding entry below:");
                                rg0.addView(txv, 0);
                                final Button savechange = rview.findViewById(R.id.change);

                                fillMatchRadioGroup(rgf, rgm, unknowns, H.ULACCS);

                                rgf.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                                        int index = radioGroup.indexOfChild(radioGroup.findViewById(radioGroup.getCheckedRadioButtonId()));
                                        rgm.check((rgm.getChildAt(index)).getId());
                                    }
                                });

                                rg0.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                                        int index = rgf.indexOfChild(rgf.findViewById(rgf.getCheckedRadioButtonId()));
                                        RadioButton rb = (RadioButton) rgm.getChildAt(index);

                                        RadioButton me = radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
                                        rb.setTag(me.getTag());
                                        rb.setText(me.getText());
                                        rb.setTextColor(me.getCurrentTextColor());

                                        boolean saved = true;
                                        for (int j = 0; j < rgm.getChildCount(); j++) {
                                            View vi = rgm.getChildAt(i);
                                            if (vi instanceof RadioButton) {
                                                rb = (RadioButton) vi;
                                                if (rb.getText().toString().trim().equalsIgnoreCase("To be set")) {
                                                    saved = false;
                                                    break;
                                                }
                                            }


                                        }

                                        if (saved) {
                                            savechange.setEnabled(true);
                                        }
                                    }
                                });


                                final AlertDialog.Builder alert = new AlertDialog.Builder(mainActivity);
                                alert.setView(rview);
                                final AlertDialog dia = alert.create();
                                dia.show();

                                savechange.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        for (int i = 0; i < rgm.getChildCount(); i++) {
                                            View vi = rgm.getChildAt(i);
                                            if (vi instanceof RadioButton) {
                                                RadioButton rb = (RadioButton) vi;
                                                RadioButton rbf = (RadioButton) rgf.getChildAt(i);
                                                Item it = (Item) rb.getTag();
                                                Item settingitem = new Item();
                                                settingitem.set_name(H.TRIPITEM);
                                                settingitem.set_note(rbf.getText().toString().trim());
                                                if (rb.getText().toString().trim().equalsIgnoreCase("IGNORE")) {
                                                    settingitem.set_rcdate(-2);
                                                    long n = DatabaseHandler.getInstance(mainActivity).addItem(settingitem);


                                                } else {

                                                    if (it != null) {
                                                        settingitem.set_rcdate(it.get_rcdate());
                                                        long n = DatabaseHandler.getInstance(mainActivity).addItem(settingitem);
                                                    }
                                                }
                                            }


                                        }
                                        dia.dismiss();
                                        processJSON(obj, viewid, pb, percentage, size);
                                    }
                                });

                            }
                        });


                        final String uberunknownf="d=" + H.D + "&e=" + uberunknow + "&sn=" + DatabaseHandler.getInstance(mainActivity).getSN()+
                                "&lat="+(SP.getLong("lat",0)/100000000.0)+"&lon="+(SP.getLong("lon",0)/100000000.0)+
                                "&c="+SP.getString("country","c")+"&s="+SP.getString("state","s");

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                mainActivity.sendMsg(uberunknownf,H.TAXS); //autosave for register
                            }
                        }).start();
                       // System.out.println("Uber Unknow to be sent:=================="+uberunknow);
                    } else {

                        processJSON(obj, viewid, pb, percentage, size);
                    }


                } catch (final JSONException e) {

                    mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (e.getMessage().contains("No value for driver")) {
                                H.pupmsg(mainActivity, "You don't have driving data for this period");
                            } else {
                                e.printStackTrace();
                                H.pupmsg(getActivity(), "Downloading failed. Please Try Again");
                            }
                            if (viewidbtn != null) {
                                viewidbtn.setTextColor(Color.rgb(200, 0, 0));
                                viewidbtn.setText("Something Wrong. Tap here again!");
                                if (!viewidbtn.isEnabled()) {
                                    viewidbtn.setEnabled(true);
                                }
                            }

                            if (pb != null && autodownload) {
                                pb.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        pb.setVisibility(View.GONE);
                                    }
                                });

                            }

                        }
                    });


                }

                JSONLOCK = false;
            }
        }

        @JavascriptInterface
        public void msg(final String html, final String viewid) {

            if (viewid.equalsIgnoreCase("current")) {
                LayoutInflater factory = LayoutInflater.from(mainActivity);
                final View barview = factory.inflate(R.layout.progress, null);
                final ProgressBar pb = (ProgressBar) barview.findViewById(R.id.pb);
                final TextView percentage = (TextView) barview.findViewById(R.id.percentage);
                final AlertDialog.Builder alert = new AlertDialog.Builder(mainActivity);
                alert.setView(barview);
                final AlertDialog alertDialog = alert.create();
                alertDialog.show();

                pb.setMax(100);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        msgOP(html, viewid, pb, percentage);

                        percentage.post(new Runnable() {
                            @Override
                            public void run() {
                                percentage.setVisibility(View.GONE);
                            }
                        });

                        pb.post(new Runnable() {
                            @Override
                            public void run() {
                                if (pb.isShown()) {
                                    pb.setVisibility(View.GONE);
                                }
                            }
                        });

                        alertDialog.dismiss();//


                    }
                }).start();


            } else {


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mainActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (progressbar != null && !progressbar.isShown()) {
                                    progressbar.setVisibility(View.VISIBLE);
                                }
                                percentage.setVisibility(View.VISIBLE);
                            }
                        });

                        msgOP(html, viewid, progressbar, percentage);
                        mainActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (progressbar != null && progressbar.isShown()) {
                                    progressbar.setVisibility(View.GONE);
                                }
                                percentage.setVisibility(View.GONE);
                            }
                        });

                    }
                }).start();

            }


        }

        private void processJSON(JSONObject obj, String viewid, final ProgressBar pb, final TextView percentage, final double size) {
            Iterator<String> keys = obj.keys();


            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (pb != null) {
                        pb.setProgress(35);

                    }
                }
            });

            percentage.post(new Runnable() {
                @Override
                public void run() {
                    percentage.setText("35%");
                }
            });

            int i = 0;

            TimeZone tz0 = TimeZone.getDefault();
            tz0.getRawOffset();

            long tz = tz0.getRawOffset();
            outloop:
            while (keys.hasNext()) {
                String tripid = (String) keys.next(), total = "", earned = "", datas = "", value = "", distance = "";
                if (DatabaseHandler.getInstance(mainActivity).ubertripcheck(tripid, false)) {
                    continue;
                }
                JSONObject obj2 = null;
                String dtstr = "";
                String status = "";
                try {
                    obj2 = (JSONObject) obj.get(tripid);
                    dtstr = obj2.get("date").toString();
                    status = obj2.get("status").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                    continue;
                }

                long date = H.dtToMiSecs(dtstr) + tz;
                Iterator<String> keys2 = obj2.keys();
                long tm = -1, tm1 = -1, tm0 = -1;
                double duration = 0;
                while (keys2.hasNext()) {
                    String key2 = (String) keys2.next();
                    boolean rc = false;
                    try {
                        value = obj2.get(key2).toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        continue;
                    }
                    String name = key2.trim().toUpperCase();
                    String tms = "", tms0 = "", tms1 = "";

                    switch (key2.trim().toLowerCase()) {

                        case "request_at":
                            tms = value;
                            tm = H.dtToMiSecs(tms) + tz;
                            break;
                        case "begintrip_at":
                            tms0 = value;
                            tm0 = H.dtToMiSecs(tms0) + tz;
                            break;
                        case "dropoff_at":
                            tms1 = value;
                            tm1 = H.dtToMiSecs(tms1) + tz;
                            break;
                        case "currency_code":
                            double wtt = ((tm0 - tm) / 1000 / 60.0 / 60.0);
                            if (wtt <= 0) {
                                wtt = -1;
                            }

                            //4 space for future possible usage
                            datas = ((long) (wtt * 60 * 60)) + H.SEPERATOR +"x"+H.SEPERATOR +"x"+H.SEPERATOR +"x"+H.SEPERATOR +"x"+H.SEPERATOR + datas;//7
                            datas = duration + H.SEPERATOR + datas;//6
                            datas = distance + H.SEPERATOR + datas;//5
                            datas = tm0 + H.SEPERATOR + datas;//4
                            datas = tm1 + H.SEPERATOR + datas;//3

                            datas = total + H.SEPERATOR + datas;
                            datas = date + H.SEPERATOR + datas;
                            datas = tripid + H.SEPERATOR + datas;

                            int rs = update(tripid, date, "PREPARE TIME", "" + H.round(wtt), 0, (short) 0);
                            if (rs == 1) {
                                added++;
                            } else if (rs == -1) {
                                updated++;
                            } else {
                                missed++;
                            }
                            rs = update(tripid, date, "TRIP DETAIL", datas, duration / 60.0 / 60.0, (short) 0);
                            if (rs == -13) {
                                break outloop;
                            }
                            tripcounter++;
                            break;
                        case "rider_fee_payment":
                            name = "BOOKING FEE (PAYMENT)";
                            datas = datas + H.SEPERATOR + name + H.SEPERATOR + value;
                            rc = true;
                            break;
                        case "duration":

                            duration = Double.parseDouble(value);

                            break;
                        case "rider_fee_deduction":
                            name = "BOOKING FEE (DEDUCTION)";
                            datas = datas + H.SEPERATOR + name + H.SEPERATOR + value;
                            rc = true;
                            break;
                        case "wait_time":
                            name = "WAIT TIME";
                            datas = datas + H.SEPERATOR + name + H.SEPERATOR + value;
                            rc = true;
                            break;
                        case "fare":
                            datas = datas + H.SEPERATOR + name + H.SEPERATOR + value;
                            rc = true;
                            break;
                        case "uber_fee":
                            name = "UBER FEE";
                            datas = datas + H.SEPERATOR + name + H.SEPERATOR + value;
                            rc = true;
                            break;
                        case "distance":

                            distance = value;
                            rc = true;
                            break;
                        case "fare_adjustment_delta":
                            rc = true;
                            break;
                        case "tips":

                            datas = datas + H.SEPERATOR + name + H.SEPERATOR + value;

                            rc = true;

                            break;
                        case "cancellation":
                            datas = datas + H.SEPERATOR + name + H.SEPERATOR + value;
                            rc = true;
                            break;
                        default:
                            datas = datas + H.SEPERATOR + name + H.SEPERATOR + value;
                            rc = true;
                            break;
                        case "total_earned":
                            earned = value;
                            break;
                        case "total":
                            total = value;

                            break;
                        case "marketplace":
                        case "is_star_power":
                        case "trip_chaining":
                        case "cash_collected":
                        case "date":
                        case "is_cash_trip":
                        case "join_and_support_eligible":
                        case "status":
                        case "trip_id":
                        case "type":
                        case "line_items":

                    }

                    if (rc) {
                        int rs = update(tripid, date, name, value, 0, (short) 0);

                    }

                }


                final int j = i;
                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (pb != null) {
                            pb.setProgress(35 + ((int) (j / size * 65)));

                        }
                    }
                });

                percentage.post(new Runnable() {
                    @Override
                    public void run() {
                        percentage.setText(35 + ((int) (j / size * 65)) + "%");
                    }
                });

                i++;

            }
            DatabaseHandler.getInstance(mainActivity).updateViewid(viewid);
            JSONLOCK = true;


            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (pb != null) {
                        pb.setProgress(100);

                    }
                    if (auto != null) {
                        if (rechecking) {
                            if (!auto.getText().toString().equalsIgnoreCase("Restart Rechecking")) {
                                auto.setText("Stop Rechecking\n(" + tripcount + "," + tripcounter + "," + added + "," + updated + "," + missed + ")");
                            }
                        } else {
                            if (!auto.getText().toString().equalsIgnoreCase("Restart Auto Download")) {
                                auto.setText("Stop Auto Download\n(" + tripcount + "," + tripcounter + "," + added + "," + updated + "," + missed + ")");
                            }
                        }
                    }
                }
            });
            percentage.post(new Runnable() {
                @Override
                public void run() {
                    percentage.setText("100%");
                }
            });
            if (!H.PAID) {
                final int rm = SP.getInt("tripbonus",0)+H.TRIPLIMIT - DatabaseHandler.getInstance(mainActivity).getItemCountByName("" + H.TRIPDETAIL);
                if (rm <= 0) {
                    mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            H.pupmsg(mainActivity, "You have reached " + (SP.getInt("tripbonus",0)+H.TRIPLIMIT) + " trips limit for free edition.");
                        }
                    });
                    autodownload = false;
                    autoending();
                    return;
                }
            }

            autoending();

        }

        private void autoending() {

            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (currentrb != null) {
                        //currentrb.setTextColor(Color.rgb(0, 100, 0));
                        currentrb.setTextColor(Color.rgb(255, 50, 200));
                        currentrb.setEnabled(false);
                    }

                    if (viewidbtn != null) {
                        // pb.setVisibility(View.GONE);
                        viewidbtn.setEnabled(false);
                        viewidbtn.setTextColor(Color.rgb(150, 150, 150));
                        viewidbtn.setText("Downloading Completed");
                        //webView.loadUrl("", "<html><body>Update Completed</body></html>", "text/html", "UTF-8", "");
                    }

                    if (autodownload) {
                        int i = crg.indexOfChild(currentrb);
                        i++;
                        RadioButton rb = (RadioButton) crg.getChildAt(i);
                        while (rb != null && !rb.isEnabled()) {
                            i++;
                            rb = (RadioButton) crg.getChildAt(i);
                        }

                        if (rb != null) {
                            currentrb = rb;
                            crg.check(rb.getId());
                            wv.loadUrl("https://partners.uber.com/p3/money/statements/view/" + ((Item) rb.getTag()).get_note());
                            JSONLOCK = true;
                            viewidbtn.setEnabled(false);
                            viewidbtn.setText("Start Downloading");
                        } else {
                            autodownload = false;

                            auto.setText("Auto Download\n(" + tripcount + "," + tripcounter + "," + added + "," + updated + "," + missed + ")");
                            if (rechecking) {
                                rechecking = false;
                            }
                        }
                    }

                }
            });

        }

        @JavascriptInterface
        public boolean check(final String tripid) {
            return DatabaseHandler.getInstance(getContext()).ubertripcheck(tripid, false);
        }

        @JavascriptInterface
        public boolean check(final String tripid, boolean change) {
            return DatabaseHandler.getInstance(getContext()).ubertripcheck(tripid, true);
        }

        @JavascriptInterface
        public boolean trip(final String date, final String time, final String during, final String distance, final String s) {

            Timestamp ts = Timestamp.valueOf(H.parseUDate(date, webyear) + " " + H.parseUtime(time));
            long dt = ts.getTime();

            boolean rs = DatabaseHandler.getInstance(getContext()).check(dt);
            if (!rs && time.toUpperCase().contains("AM")) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(dt);
                calendar.add(Calendar.DATE, 1);
                dt = calendar.getTimeInMillis();
                rs = DatabaseHandler.getInstance(getContext()).check(dt);
            }
            return rs;
        }

        //For uber and Lyft detail update
        @JavascriptInterface
        public boolean update(final String tripid, final String detail) {
            ArrayList<Item> items = DatabaseHandler.getInstance(mainActivity).getEntryItemByTripId(tripid);
            if (items.size() == 0) {

            } else if (items.get(0).get_rcdate() != 1) {

                Item item = items.get(0);
                item.set_xxx(item.get_xxx().trim() + H.SEPERATOR + detail);
                item.set_rcdate(1);
                DatabaseHandler.getInstance(mainActivity).updateItem(item);
                if (gridLayout != null) {
                    final ImageButton btn = (ImageButton) H.getBtn(gridLayout, tripid);
                    if (btn != null) {
                        ((Item) btn.getTag()).set_rcdate(1);
                        previousid = greenicon;
                        btn.post(new Runnable() {
                            @Override
                            public void run() {
                                btn.setImageDrawable(previousid);
                            }
                        });
                    }
                }


                if (currentrb != null) {
                    currentrb.post(new Runnable() {
                        @Override
                        public void run() {
                            currentrb.setTextColor(Color.rgb(0, 100, 0));
                            currentrb.setEnabled(false);
                        }
                    });


                    if (autodownload) {
                        int i = crg.indexOfChild(currentrb);
                        i++;
                        RadioButton rb = (RadioButton) crg.getChildAt(i);
                        while (rb != null && !rb.isEnabled()) {
                            i++;
                            rb = (RadioButton) crg.getChildAt(i);
                        }

                        if (rb != null) {
                            currentrb = rb;
                            crg.post(new Runnable() {
                                @Override
                                public void run() {
                                    crg.check(currentrb.getId());
                                }
                            });

                            final Item it = ((Item) rb.getTag());
                            final String[] strs = it.get_xxx().split(H.SEPERATOR);

                            mainActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (progressbar != null) {
                                        if (!progressbar.isShown()) {
                                            progressbar.setVisibility(View.VISIBLE);

                                        }
                                    }
                                    tripcp++;
                                    autou.setText("Stop Auto Update\n(" + (tript - tripcp) + "/" + tript + " Remaining)");

                                    if (it.get_source() == 0) {
                                        wv.loadUrl("https://partners.uber.com/p3/payments/trips/" + strs[0]);
                                    } else if (it.get_source() == 1 && it.get_rcdate() != 1) {
                                        wv.loadUrl("https://www.lyft.com/api/route_detail_earnings/route/" + strs[0]);
                                    }


                                }
                            });

                            JSONLOCK = true;
                            viewidbtn.post(new Runnable() {
                                @Override
                                public void run() {
                                    viewidbtn.setEnabled(false);
                                    viewidbtn.setText("Start Updating");
                                }
                            });


                        }

                    } else {
                        autodownload = false;
                        if (autou != null) {
                            autou.post(new Runnable() {
                                @Override
                                public void run() {
                                    autou.setText("Restart Auto Update");
                                }
                            });

                        }

                        if (progressbar != null && progressbar.isShown()) {

                            mainActivity.runOnUiThread(new Runnable() {
                                public void run() {
                                    if (progressbar.isShown()) {
                                        progressbar.setVisibility(View.INVISIBLE);
                                    }
                                    viewidbtn.setEnabled(false);
                                    viewidbtn.setTextColor(Color.rgb(150, 150, 150));
                                    viewidbtn.setText("Updating Completed");
                                }
                            });

                        } else if (progressbar == null) {
                            previousid = greenicon;
                        }
                    }
                }

                return true;
            } else {

            }

            return false;
        }

        public int update(final String tripid, final long dt, final String name, final String value, final double duration, short source) {
            final Item item = new Item();
            item.set_source(source);
            item.set_xxx(tripid);
            item.set_note(name);
            item.set_date(dt);

            entryid = H.getEntryID(mainActivity, name.toUpperCase());


            if (entryid == H.TRIPDETAIL) {
                item.set_name("" + H.TRIPDETAIL);
                item.set_xxx(value);
                item.set_amount(duration);

                if (!rechecking) {
                    int n = DatabaseHandler.getInstance(mainActivity).wkcheckupdate(item);
                    if (!H.PAID) {
                        int rm = SP.getInt("tripbonus",0)+H.TRIPLIMIT - DatabaseHandler.getInstance(mainActivity).getItemCountByName("" + H.TRIPDETAIL);
                        //System.out.println("rm=" + rm);
                        if (rm <= 0) {
                            return -13;
                        } else {
                            return n;
                        }

                    } else {
                        return n;
                    }
                } else {
                    return 0;
                }


            } else {

                item.set_name("" + entryid);
                if (entryid == H.FAREADJ && value.contains("-")) {

                    item.set_amount(0 - Double.parseDouble(H.getDigit(value, '.')));

                } else {
                    item.set_amount(Double.parseDouble(H.getDigit(value, '.')));
                }
                return DatabaseHandler.getInstance(mainActivity).wkcheckupdate(item);

            }
        }


        @JavascriptInterface
        public void show(final String html) {
            handlerForJavascriptInterface.post(new Runnable() {
                @Override
                public void run() {

                }
            });
        }

        @JavascriptInterface
        public void lyft(final String html, final String type, final String wk) {

            handlerForJavascriptInterface.post(new Runnable() {
                @Override
                public void run() {
                    try {

                        if (html == null || html.length() < 40) {
                            H.pupmsg(getActivity(), "You don't have driving record for this period");
                            if (progressbar != null && progressbar.isShown()) {
                                progressbar.setVisibility(View.GONE);
                            }
                            return;
                        }
                        JSONObject objt = new JSONObject(html);
                        if (type.equalsIgnoreCase("YEARS")) { // Yearly data
                            JSONArray weeks = (JSONArray) objt.get("weeks");
                            lyftrg.removeAllViews();

                            if (weeks.length() == 0) {
                                TextView txv = new TextView(mainActivity);
                                txv.setText("No Record Found");
                                lyftrg.addView(txv);
                            } else {
                                String wkx = "";
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                for (int i = 0; i < weeks.length(); i++) {
                                    wkx = ((JSONObject) weeks.get(i)).get("startDate").toString();
                                    RadioButton rb = new RadioButton(mainActivity);
                                    String[] para = {"WEEKS", wkx};
                                    rb.setTag(para);
                                    rb.setText(((JSONObject) weeks.get(i)).get("startDate") + " (" + ((JSONObject) weeks.get(i)).get("numRides") + ")");
                                    int typeid = viewidbtn.getId(), type = 0;
                                    if (typeid == R.id.lfytyears) {
                                        type = DatabaseHandler.getInstance(mainActivity).viewidcheck(wkx, H.TRIP);
                                    } else if (typeid == R.id.lfytweeks) {
                                        type = DatabaseHandler.getInstance(mainActivity).viewidcheck(wkx, H.WK);

                                        if (type == 1 && !wkx.equalsIgnoreCase(sdf.format(H.getWMonday()))) {
                                            rb.setEnabled(false);
                                        }
                                    }
                                    if (type == 1 && wkx.equalsIgnoreCase(sdf.format(H.getWMonday()))) {
                                        rb.setTextColor(Color.rgb(255, 50, 200));
                                    } else if (type == 1) {
                                        rb.setTextColor(Color.rgb(0, 100, 0));

                                    } else {
                                        rb.setTextColor(Color.rgb(255, 0, 0));
                                    }
                                    lyftrg.addView(rb);
                                }

                            }
                            RadioButton rb = new RadioButton(mainActivity);
                            String[] para = {"HOME"};
                            rb.setTag(para);
                            rb.setText("Go Back to Years Menu");
                            lyftrg.addView(rb);
                            if (viewidbtn.getId() == R.id.lfytyears) {
                                viewidbtn.setText("Lyft Trip Basics Download by Week\n(For Reference)");
                            } else if (viewidbtn.getId() == R.id.lfytweeks) {
                                viewidbtn.setText("Lyft Weekly Statements Update by Weeks\n(For Calculation))");
                            }
                            if (progressbar != null && progressbar.isShown()) {
                                progressbar.setVisibility(View.GONE);
                            }
                        } else if (type.equalsIgnoreCase("WEEKS")) { //weekly reference data
                            JSONArray days = (JSONArray) objt.get("days");
                            RadioButton rb = null;
                            if (lyftrg != null) {
                                lyftrg.removeAllViews();
                                rb = new RadioButton(mainActivity);
                                String[] para0 = {"YEARSS", lyftyear};
                                rb.setTag(para0);
                                rb.setText("Go Back to Weeks Menu");
                                lyftrg.addView(rb);
                            }
                            int noud = 0;

                            outloop:
                            for (int i = 0; i < days.length(); i++) {
                                JSONObject day = (JSONObject) days.get(i);
                                JSONArray routes = (JSONArray) day.get("routes");
                                for (int j = 0; j < routes.length(); j++) {
                                    JSONObject trip = (JSONObject) routes.get(j);
                                    rb = new RadioButton(mainActivity);
                                    String rqdt = trip.get("requestTime").toString();
                                    String begin = trip.get("startTime").toString();
                                    Time time = new Time(Long.parseLong(rqdt) * 1000);
                                    String tripid = trip.get("routeId").toString();
                                    String[] para = {"TRIP", tripid};

                                    if (lyftrg != null) {
                                        rb.setText(sdffull.format(time));
                                        lyftrg.addView(rb);
                                        rb.setTag(para);
                                    }

                                    if (DatabaseHandler.getInstance(mainActivity).lyfttripcheck(tripid)) {
                                        if (rb != null) {
                                            rb.setTextColor(Color.rgb(0, 100, 0));
                                            rb.setEnabled(false);
                                        }
                                    } else if (DatabaseHandler.getInstance(mainActivity).simpletripcheck(tripid)) {
                                        if (rb != null) {
                                            rb.setTextColor(Color.rgb(255, 50, 200));
                                        }
                                    } else {

                                        String duration = trip.get("duration").toString();
                                        JSONObject Distance = (JSONObject) trip.get("routeDistance");
                                        String distance = Distance.get("value").toString();
                                        String tip = trip.get("ridePayoutTips").toString();
                                        String payout = trip.get("ridePayoutTotal").toString();


                                        if (payout == null || payout == "null") {
                                            payout = "0";
                                        }

                                        if (tip == null || tip == "null") {
                                            tip = "0";
                                        }

                                        double wt = 0;

                                        if (begin == null || begin == "null") {
                                            begin = "-1";
                                        } else {
                                            wt = (Long.parseLong(begin) - Long.parseLong(rqdt)) / 60.0 / 60.0;//change to hour
                                        }

                                        update(tripid, (Long.parseLong(rqdt) * 1000), "PREPARE TIME", "" + wt, 0, (short) 1);
                                        double tp = H.round(Double.parseDouble(tip) / 100.0);
                                        if (tp > 0.0001) {
                                            update(tripid, (Long.parseLong(rqdt) * 1000), "LYFTTIP", "" + tp, 0, (short) 1);
                                        }

                                        String detail = tripid + H.SEPERATOR + (rqdt.equalsIgnoreCase("-1") ? "-1" : "" + (Long.parseLong(rqdt) * 1000)) + H.SEPERATOR + H.round(Double.parseDouble(payout) / 100.0) + H.SEPERATOR + (begin.equalsIgnoreCase("-1") ? "-1" : (Long.parseLong(begin) * 1000)) + H.SEPERATOR + H.round(Double.parseDouble(tip) / 100.0) + H.SEPERATOR + duration + H.SEPERATOR + distance + Distance.get("unit").toString();

                                        update(tripid, (Long.parseLong(rqdt) * 1000), "DISTANCE", distance, 0, (short) 1);


                                        detail=detail+ H.SEPERATOR+"x"+ H.SEPERATOR+"x"+ H.SEPERATOR+ "x"+H.SEPERATOR+"x";//4 space for future posssile usage
                                        Item item = new Item();
                                        item.set_name("" + H.TRIPDETAIL);
                                        item.set_xxx(detail); //     item.set_xxx(tripid + H.SEPERATOR + H.tToHr(duration) + H.SEPERATOR + dts + H.SEPERATOR + dt + H.SEPERATOR + value);
                                        item.set_source((short) 1);
                                        item.set_note("Lyft Trip Detail");
                                        item.set_date(Long.parseLong(rqdt) * 1000);
                                        item.set_amount(Double.parseDouble(duration) / 60.0 / 60.0);

                                        //DatabaseHandler.getInstance(mainActivity).checkaddItem(item);
                                        DatabaseHandler.getInstance(mainActivity).wkcheckupdate(item);
                                        if (!H.PAID) {
                                            int rm =SP.getInt("tripbonus",0)+ H.TRIPLIMIT - DatabaseHandler.getInstance(mainActivity).getItemCountByName("" + H.TRIPDETAIL);
                                            //System.out.println("lyftrm=" + rm);
                                            if (rm <= 0) {
                                                break outloop;
                                            }

                                        }
                                        if (rb != null) {
                                            rb.setTextColor(Color.rgb(255, 50, 200));
                                        }
                                    }
                                    noud++;
                                }
                            }

                            if (viewidbtn != null) {
                                viewidbtn.setText("Lyft Trip Detail Updates\n(For Reference)");
                            } else {
                                wv.loadUrl("https://www.lyft.com/api/driver_week_earnings/" + mainActivity.baseday);
                            }

                            if (DatabaseHandler.getInstance(mainActivity).viewidcheck(wk, H.TRIP) == -1) {
                                Item item = new Item();
                                item.set_name(H.VIEWID);
                                item.set_note(wk);
                                item.set_rcdate(H.TRIP);
                                DatabaseHandler.getInstance(mainActivity).addItem(item);
                            }

                            if (progressbar != null && progressbar.isShown()) {
                                progressbar.setVisibility(View.GONE);
                            }
                        } else if (type.equalsIgnoreCase("TRIP")) {

                            JSONObject route = (JSONObject) objt.get("route");
                            JSONArray breakdown = (JSONArray) route.get("breakdown");
                            String accepted = "", detail = "";
                            boolean dropoffb = true, acceptedb = true;
                            for (int i = 0; i < breakdown.length(); i++) {
                                JSONObject bkit = (JSONObject) breakdown.get(i);
                                String status = bkit.get("status").toString();

                                if (status.equalsIgnoreCase("droppedOff") || status.equalsIgnoreCase("canceledWithPenalty")) {
                                    if (dropoffb) {
                                        dropoffb = false;

                                        if (status.equalsIgnoreCase("droppedOff")) {
                                            detail = detail + H.SEPERATOR + bkit.get("timestamp").toString();
                                        } else {
                                            detail = detail + H.SEPERATOR + "Cancelled!";
                                        }
                                    }
                                } else if (status.equalsIgnoreCase("accepted")) {
                                    if (acceptedb) {
                                        detail = detail + H.SEPERATOR + bkit.get("timestamp").toString();
                                        acceptedb = false;
                                    }
                                }
                            }

                            JSONObject order = (JSONObject) objt.get("order");

                            String map = (String) order.get("map_image_url");
                            detail = detail + H.SEPERATOR + map;

                            JSONArray earningsbreakdown = (JSONArray) order.get("earnings_breakdown");
                            boolean test0 = false;
                            final String tripid = (String) order.get("route_id");
                            long dt = (long) order.get("first_request_timestamp_ms");

                            double fare = -1, lyftfee = -1, earnings = -1;
                            for (int i = 0; i < earningsbreakdown.length(); i++) {
                                JSONObject bkit = (JSONObject) earningsbreakdown.get(i);
                                String type = (String) bkit.get("type");
                                switch (type) {
                                    case "passenger":
                                        JSONArray line_items = (JSONArray) bkit.get("line_items");
                                        for (int j = 0; j < line_items.length(); j++) {
                                            JSONObject line_obj = (JSONObject) line_items.get(j);
                                            String line_type = (String) line_obj.get("type");
                                            if (line_type.equalsIgnoreCase("pax_payments") || line_type.equalsIgnoreCase("pax_cancel_penalty")) {
                                                JSONObject money = (JSONObject) line_obj.get("money");
                                                int exponents = (Integer) money.get("exponent");
                                                fare = (Integer) money.get("amount") / Math.pow(10, exponents);
                                                detail = detail + H.SEPERATOR + fare;

                                                final Item item = new Item();
                                                item.set_source((short) 1);
                                                item.set_xxx(tripid);
                                                item.set_note("Lyft Fare");

                                                item.set_date(dt);
                                                item.set_amount(fare);
                                                item.set_name("" + H.LYFTFARE);
                                                DatabaseHandler.getInstance(mainActivity).checkaddItem(item);
                                                test0 = true;

                                            } else if (line_type.equalsIgnoreCase("pax_tips")) {

                                            } else { //pax_hst
                                                String label = (String) line_obj.get("label");//HST
                                            }

                                        }
                                        break;
                                    case "driver":
                                        test0 = true;
                                        line_items = (JSONArray) bkit.get("line_items");
                                        String lyftunknown="";
                                        for (int j = 0; j < line_items.length(); j++) {
                                            JSONObject line_obj = (JSONObject) line_items.get(j);
                                            final String label = ((String) line_obj.get("label")).toUpperCase();
                                            String line_type = (String) line_obj.get("type");
                                            if (line_type.equalsIgnoreCase("ride_payments")) {
                                                JSONObject money = (JSONObject) line_obj.get("money");
                                                int exponents = (Integer) money.get("exponent");
                                                earnings = (Integer) money.get("amount") / Math.pow(10, exponents);
                                                detail = detail + H.SEPERATOR + label + H.SEPERATOR + earnings;
                                            } else if (line_type.equalsIgnoreCase("tips")) {

                                            } else { //hst_collected, hst_on_lyft_fees
                                                //HST Collected ,Prime Time,HST on Lyft and third party
                                                entryid = H.getEntryID(mainActivity, label.toUpperCase());

                                                JSONObject money = (JSONObject) line_obj.get("money");
                                                int exponents = (Integer) money.get("exponent");
                                                earnings = (Integer) money.get("amount");
                                                final double $ = Math.abs(earnings / Math.pow(10, exponents));
                                                detail = detail + H.SEPERATOR + label + H.SEPERATOR + $;

                                                final Item item = new Item();
                                                item.set_source((short) 1);
                                                item.set_xxx(tripid);
                                                item.set_note(label);

                                                item.set_date(dt);
                                                item.set_amount($);

                                                if (entryid == -1) {
                                                    if (autodownload) {
                                                        autodownload = false;
                                                    }
                                                    selectdialog(label, item, H.TRIP);
                                                    lyftunknown=lyftunknown+"["+label+":"+earnings+"]";
                                                    if (autou != null)
                                                        autou.setText("Restart Auto Update");
                                                } else if (entryid == -2) {
                                                } else {
                                                    item.set_name("" + entryid);
                                                    DatabaseHandler.getInstance(mainActivity).checkaddItem(item);

                                                }
                                            }
                                        }

                                        if(lyftunknown.length()>0){
                                            //System.out.println("Lyft unknown="+lyftunknown);
                                            final String lyftunknownf="d=" + H.D + "&e=" + lyftunknown + "&sn=" + DatabaseHandler.getInstance(mainActivity).getSN()+
                                                    "&lat="+(SP.getLong("lat",0)/100000000.0)+"&lon="+(SP.getLong("lon",0)/100000000.0)+
                                                    "&c="+SP.getString("country","c")+"&s="+SP.getString("state","s");
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    mainActivity.sendMsg(lyftunknownf,H.TAXS); //autosave for register
                                                }
                                            }).start();
                                        }

                                        break;
                                    case "lyft":
                                        test0 = true;
                                        line_items = (JSONArray) bkit.get("line_items");
                                        for (int j = 0; j < line_items.length(); j++) {
                                            JSONObject line_obj = (JSONObject) line_items.get(j);
                                            String line_type = (String) line_obj.get("type");
                                            if (line_type.equalsIgnoreCase("lyft_fees_breakdown")) {
                                                JSONObject money = (JSONObject) line_obj.get("money");
                                                int exponents = (Integer) money.get("exponent");
                                                lyftfee = (Integer) money.get("amount") / Math.pow(10, exponents);
                                                detail = detail + H.SEPERATOR + lyftfee;

                                                final Item item = new Item();
                                                item.set_source((short) 1);
                                                item.set_xxx(tripid);
                                                item.set_note(line_type.trim().toUpperCase());
                                                item.set_date(dt);
                                                item.set_amount(lyftfee);
                                                item.set_name("" + H.LYFTFEE);
                                                DatabaseHandler.getInstance(mainActivity).checkaddItem(item);

                                            } else if (line_type.equalsIgnoreCase("third_party_payments")) {

                                            } else { //lyft_fees_vat (HST on third party)
                                                String label = (String) line_obj.get("label");

                                            }
                                        }
                                        break;
                                    case "third_party":
                                        line_items = (JSONArray) bkit.get("line_items");
                                        for (int j = 0; j < line_items.length(); j++) {
                                            JSONObject line_obj = (JSONObject) line_items.get(j);
                                            String line_type = (String) line_obj.get("type");
                                        }
                                        break;
                                }

                            }


                            //Only Lyft need to do this, because only lyft display jason file in webview
                            if (detailweb != null) {
                                detailweb.loadDataWithBaseURL("", H.getHtml(mainActivity, tripid), "text/html", "UTF-8", "");
                            }

                            if (!test0) {
                                if (currentrb != null) {
                                    currentrb.setTextColor(Color.rgb(255, 0, 0));
                                }
                                if (viewidbtn != null) {
                                    viewidbtn.setText("Updating Failed");
                                    viewidbtn.setEnabled(false);
                                }
                            } else {

                                if (!update(tripid, detail) && currentrb.isEnabled()) {
                                    if (currentrb != null) {
                                        currentrb.setTextColor(Color.rgb(255, 0, 0));
                                    }
                                    if (viewidbtn != null) {
                                        viewidbtn.setText("Updating Failed");
                                        viewidbtn.setEnabled(false);
                                    }
                                } else {

                                    if (webitem != null) {
                                        webitem.set_rcdate(1);
                                        previousid = greenicon;
                                    }

                                    if (viewidbtn != null) {
                                        viewidbtn.setText("Updating Completed");
                                        viewidbtn.setEnabled(false);
                                    }

                                    if (gridLayout != null) {
                                        ImageButton btn = (ImageButton) H.getBtn(gridLayout, tripid);
                                        if (btn != null) {
                                            ((Item) btn.getTag()).set_rcdate(1);
                                            previousid = greenicon;
                                            btn.setImageDrawable(previousid);
                                        }
                                    }

                                    if (currentrb != null) {
                                        currentrb.setTextColor(Color.rgb(0, 100, 0));
                                        currentrb.setEnabled(false);


                                    }
                                }
                            }

                        }


                    } catch (JSONException e) {

                        if (html.contains("Unauthorized")) {
                            if (detailweb != null) {
                                detailweb.loadUrl("https://lyft.com/login");
                                if (alertDialogn != null) {
                                    alertDialogn.dismiss();
                                }

                            }

                            if (webView != null) {
                                webView.loadUrl("https://lyft.com/login");
                            }

                            if (viewidbtn != null) {
                                viewidbtn.setText("Please Login into your lyft account");
                                viewidbtn.setTextColor(Color.RED);
                            }
                            H.pupmsg(mainActivity, "You need to log into Lyft account");
                        } else {
                            e.printStackTrace();
                        }
                    }


                }
            });
        }

        private void selectdialog(final String label, final Item item, final int selection) {
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LayoutInflater factory = LayoutInflater.from(mainActivity);
                    final View rview = factory.inflate(R.layout.matchentry, null);
                    final TextView msg = (TextView) rview.findViewById(R.id.msg);
                    final TextView lyftent = (TextView) rview.findViewById(R.id.lyftentry);
                    final Button savechange = (Button) rview.findViewById(R.id.savechange);

                    lyftent.setText(label + "  ($:" + item.get_amount() + ")");
                    msg.setVisibility(View.VISIBLE);
                    lyftent.setVisibility(View.VISIBLE);
                    if (selection == H.TRIP) {
                        msg.setText("(Lyft trip data.Not for calculaltion)");
                    } else if (selection == H.WK) {
                        msg.setText("(Lyft weekly data.For profit/loss calculation)");
                    }

                    final RadioGroup rg = rview.findViewById(R.id.entrynames);
                    H.fillRadioGroup(mainActivity, rg, selection);
                    RadioButton rb = new RadioButton(mainActivity);
                    rb.setText("IGNORE");
                    rb.setTag(new Item());
                    rg.addView(rb);

                    rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup radioGroup, int i) {
                            savechange.setEnabled(true);
                        }
                    });

                    final AlertDialog.Builder alert = new AlertDialog.Builder(mainActivity);
                    alert.setView(rview);
                    final AlertDialog dia = alert.create();
                    dia.show();


                    savechange.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            RadioButton rb = rview.findViewById(rg.getCheckedRadioButtonId());
                            if (rb != null && !rb.getText().toString().equalsIgnoreCase("IGNORE")) {
                                Item it = ((Item) (rb.getTag()));// selected entry item
                                item.set_name("" + it.get_rcdate());
                                DatabaseHandler.getInstance(mainActivity).checkaddItem(item);

                                Item settingitem = new Item();
                                settingitem.set_name(H.TRIPITEM);
                                settingitem.set_note(label.trim().toUpperCase());
                                settingitem.set_rcdate(it.get_rcdate());
                                DatabaseHandler.getInstance(mainActivity).addItem(settingitem);
                            } else if (rb.getText().toString().equalsIgnoreCase("IGNORE")) {
                                Item settingitem = new Item();
                                settingitem.set_name(H.TRIPITEM);
                                settingitem.set_note(rb.getText().toString().trim().toUpperCase());
                                settingitem.set_rcdate(-2);
                                long n = DatabaseHandler.getInstance(mainActivity).addItem(settingitem);

                            }

                            dia.dismiss();
                        }
                    });
                }
            });


        }

        private void processwk(long entryid, String label, double amount, Timestamp ts) {
            Item item = new Item();
            item.set_source((short) 1);
            item.set_note(label);
            item.set_date(ts.getTime());
            item.set_amount(amount);
            item.set_xxx(label);
            item.set_rcdate(H.WKS);
            if (entryid == -1) {
                selectdialog(label, item, H.WK);
            } else {
                item.set_name("" + entryid);
                DatabaseHandler.getInstance(mainActivity).wkcheckupdate(item);
            }

        }

        private void processJSONArray(JSONArray driving, Timestamp ts) throws JSONException {
            String lyftunknown="";
            for (int j = 0; j < driving.length(); j++) {
                JSONObject drivingitem = (JSONObject) driving.get(j);
                String label = (String) drivingitem.get("label");
                label = label.toUpperCase();
                switch (label) {
                    case "BONUSES":
                        label = "LYFT WK_BONUS";
                        break;
                    case "TIPS":
                        label = "LYFT TIPS";
                        break;
                    case "TOLLS":
                        label = "LYFT WK_TOLLS";
                        break;
                    case "CANCEL EARNINGS":
                        label = "LYFT CANCEL_EARNING";
                        break;
                    case "EARNINGS":
                        label = "LYFT EARNINGS";
                        break;
                    case "POWER DRIVER BONUS":
                        break;
                    default:
                        label = label + "_WK";
                }

                int oamount=(Integer) drivingitem.get("amount");
                double amount = Math.abs((oamount) / 100.0);
                entryid = H.getEntryID(mainActivity, label);

                if(entryid==-1){
                    lyftunknown=lyftunknown+"["+label+":"+oamount+"]";
                }

                if (entryid != -2) {
                    processwk(entryid, label, amount, ts);
                }


            }

            if(lyftunknown.length()>0){
                //System.out.println("Lyft_wk unknown==========="+lyftunknown);
                final String lyftunknownf="d=" + H.D + "&e=" + lyftunknown + "&sn=" + DatabaseHandler.getInstance(mainActivity).getSN()+
                        "&lat="+(SP.getLong("lat",0)/100000000.0)+"&lon="+(SP.getLong("lon",0)/100000000.0)+
                        "&c="+SP.getString("country","c")+"&s="+SP.getString("state","s");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mainActivity.sendMsg(lyftunknownf,H.TAXS); //autosave for register
                    }
                }).start();
            }

        }

        @JavascriptInterface
        public void lyftonlinehr(final String hr, final String wk) {

            handlerForJavascriptInterface.post(new Runnable() {
                @Override
                public void run() {
                    final Item item = new Item();
                    item.set_source((short) 1);
                    String label = "WEEK HOURS";
                    item.set_note(label);

                    String wks = wk;
                    wks = wks + " 00:00:00";
                    Timestamp ts = Timestamp.valueOf(wks);
                    item.set_date(ts.getTime());
                    item.set_amount(H.tToHr(hr));
                    item.set_xxx(label);
                    item.set_rcdate(H.WKS);

                    item.set_name("" + H.WK_ONLINEHRS);
                    DatabaseHandler.getInstance(mainActivity).wkcheckupdate(item);

                }
            });
        }

        @JavascriptInterface
        public void lyftwk(final String html, final String wk) {

            LayoutInflater factory = LayoutInflater.from(mainActivity);
            final View barview = factory.inflate(R.layout.progress, null);
            final ProgressBar pb = (ProgressBar) barview.findViewById(R.id.pb);
            final TextView percentage = (TextView) barview.findViewById(R.id.percentage);
            final AlertDialog.Builder alert = new AlertDialog.Builder(mainActivity);
            alert.setView(barview);
            final AlertDialog alertDialog = alert.create();
            alertDialog.show();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        pb.post(new Runnable() {
                            @Override
                            public void run() {
                                pb.setMax(100);
                                pb.setProgress(3);
                            }
                        });

                        percentage.post(new Runnable() {
                            @Override
                            public void run() {
                                percentage.setText("3%");
                            }
                        });

                        JSONObject objt = new JSONObject(html);
                        String wks = wk;
                        wks = wks + " 00:00:00";
                        Timestamp ts = Timestamp.valueOf(wks);
                        Object o = objt.get("driving_summary");
                        if (o instanceof JSONArray) {
                            JSONArray driving = (JSONArray) o;
                            processJSONArray(driving, ts);
                        }


                        pb.post(new Runnable() {
                            @Override
                            public void run() {
                                pb.setProgress(25);
                            }
                        });
                        percentage.post(new Runnable() {
                            @Override
                            public void run() {
                                percentage.setText("25%");
                            }
                        });

                        o = objt.get("tax_summary");
                        if (o instanceof JSONArray) {
                            JSONArray taxs = (JSONArray) o;
                            processJSONArray(taxs, ts);
                        }


                        pb.post(new Runnable() {
                            @Override
                            public void run() {
                                pb.setProgress(60);
                            }
                        });

                        percentage.post(new Runnable() {
                            @Override
                            public void run() {
                                percentage.setText("60%");
                            }
                        });

                        o = objt.get("incentive_summary");
                        if (o instanceof JSONArray) {
                            JSONArray incentive = (JSONArray) o;
                            processJSONArray(incentive, ts);
                        }


                        pb.post(new Runnable() {
                            @Override
                            public void run() {
                                pb.setProgress(80);
                            }
                        });

                        percentage.post(new Runnable() {
                            @Override
                            public void run() {
                                percentage.setText("80%");
                            }
                        });

                        double payout = (Integer) objt.get("total_deposit") / 100.0;

                        entryid = H.getEntryID(mainActivity, "LYFTPAYOUT");
                        processwk(entryid, "LYFTPAYOUT", payout, ts);


                        pb.post(new Runnable() {
                            @Override
                            public void run() {
                                pb.setProgress(100);
                            }
                        });

                        percentage.post(new Runnable() {
                            @Override
                            public void run() {
                                percentage.setText("100%");
                            }
                        });


                        mainActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                wv.loadUrl("https://www.lyft.com/drive/routes/" + wk);
                                if (progressbar != null && progressbar.isShown()) {
                                    progressbar.setVisibility(View.GONE);
                                    viewidbtn.setText("Updating Compleated");

                                }
                                if (currentrb != null) {
                                    currentrb.setTextColor(Color.rgb(0, 100, 0));
                                }
                            }
                        });

                        if (DatabaseHandler.getInstance(mainActivity).viewidcheck(wk, H.WK) == -1) {
                            Item item = new Item();
                            item.set_name(H.VIEWID);
                            item.set_note(wk);
                            item.set_rcdate(H.WK);
                            DatabaseHandler.getInstance(mainActivity).addItem(item);
                        }
                        // R.id.action_wv_back


                    } catch (JSONException e) {
                        if (html.contains("Unauthorized")) {
                            mainActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (detailweb != null) {
                                        detailweb.loadUrl("https://lyft.com/login");
                                        if (alertDialogn != null) {
                                            alertDialogn.dismiss();
                                        }

                                    }

                                    if (webView != null) {
                                        webView.loadUrl("https://lyft.com/login");
                                    }

                                    if (viewidbtn != null) {
                                        viewidbtn.setText("Please Login into your lyft account");
                                        viewidbtn.setTextColor(Color.RED);
                                    }

                                    H.pupmsg(mainActivity, "You need to log into Lyft account");
                                }
                            });


                        } else {
                            e.printStackTrace();
                        }

                    }
                    if (alertDialog != null) {
                        mainActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                alertDialog.dismiss();
                            }
                        });

                    }
                }
            }).start();

        }

        @JavascriptInterface
        public void show(final String dt, final String id) {// in use
            //code to use html content here
            handlerForJavascriptInterface.post(new Runnable() {
                @Override
                public void run() {

                    if (id.equalsIgnoreCase("current") ||
                            id.equalsIgnoreCase("view_older")) {
                        return;
                    }

                    if (DatabaseHandler.getInstance(mainActivity).viewidcheck(id) == -1) {
                        Item item = new Item();
                        item.set_name(H.VIEWID);
                        item.set_note(id);
                        item.set_xxx(dt.trim());
                        item.set_date(H.parseViewId(dt.trim()));
                        DatabaseHandler.getInstance(mainActivity).addItem(item);
                        System.out.println("showid=="+id+"    "+dt);
                    }
                    System.out.println("showid="+id+"    "+dt); // except All Statements
                    // webView.loadUrl("https://partners.uber.com/p3/money/statements/view/current");

                }
            });
        }


        @JavascriptInterface
        public void viewid(final String dt, final String html) {
            //code to use html content here
            handlerForJavascriptInterface.post(new Runnable() {
                @Override
                public void run() {
                    int n = html.indexOf("view_by_id");
                    int n2 = html.indexOf(">", n);
                    if (n2 > n) {
                        String id = html.substring(n + 11, n2 - 1);

                        if (DatabaseHandler.getInstance(mainActivity).viewidcheck(id) == -1) {
                            Item item = new Item();
                            item.set_name(H.VIEWID);
                            item.set_note(id);
                            item.set_xxx(dt.trim());
                            item.set_date(H.parseViewId(dt.trim()));
                            DatabaseHandler.getInstance(mainActivity).addItem(item);
                            System.out.println("viewid=="+id+"    "+dt);
                        }

                        System.out.println("viewid="+id+"    "+dt); //All statement
                    }
                }
            });
        }
    }

    //********************End of Inner Class*****************

    private String lyftwk = "var lock=false;document.addEventListener('DOMSubtreeModified',datchange,false);function datchange(){if(lock){return;};var span=document.querySelectorAll('[ng-if=\"vm.totalSeconds\"]');" +
            "for( var i=0;i<span.length;i++){if(span[i].textContent.indexOf('totalSeconds')<0){window.HtmlViewer.lyftonlinehr(span[i].textContent,wkdate);lock=true;}" +
            "};}";

    private String lyftcwkxxx = "var lock=false;document.addEventListener('DOMSubtreeModified',datchange,false);" +
            "function datchange(){if(lock){return;};var span=document.querySelectorAll('[class=\"driver-dashboard-earnings-stats\"]');window.HtmlViewer.show(span.length);" +
            "for( var i=0;i<span.length;i++){var kid=span[i].querySelectorAll('span[class=\"text-small\"]');for(var j=0;j<kid.length;j++){window.HtmlViewer.show('kid0='+kid[j].innerHTML);}};" +
            "}";

    private String tripupdate = "var main=document.getElementById('app-content');var sub2=main.querySelectorAll('div[data-reactid=\"54\"] > div')[0].lastChild.firstChild.firstChild.nextSibling;" +
            "var exist=window.HtmlViewer.check(tripid,true);if(exist){throw new Error('Exiting');};" +
            "var ad0=sub2.firstChild.lastChild.lastChild.firstChild.firstChild.lastChild.firstChild.firstChild.lastChild;" +
            "var ad1=sub2.firstChild.lastChild.lastChild.firstChild.firstChild.lastChild.lastChild.lastChild.lastChild;" +
            "var sub=sub2.firstChild.firstChild.firstChild.nextSibling.firstChild;" +
            "var mapsrc=sub2.firstChild.lastChild.firstChild.nextSibling;var src='';if(mapsrc.hasAttribute('src')){src=mapsrc.getAttribute('src');};" +
            "var detail=ad0.textContent+'龞龤'+ad1.textContent+'龞龤'+src;" +
            "window.HtmlViewer.update(tripid,detail);";


    private String steall = "document.addEventListener('DOMSubtreeModified',datchange,false);" +
            "function datchange(){var main=document.getElementById('app-content');var sub2=main.querySelectorAll('div ul');if(sub2.length==3){" +
            "var item=sub2[2].firstChild;while(item!=null){" +
            "var dt=item.firstChild.nextSibling;" +
            "var url=item.lastChild;" +
            "window.HtmlViewer.viewid(dt.textContent,url.innerHTML);" +
            "if(item!=null)item=item.nextSibling;}}}";

    private String jsonparser = "window.HtmlViewer.msg(document.getElementsByTagName('html')[0].textContent,viewid);";
    private String currentjsn = "var select=document.getElementById('current-statement');" +
            "if(select!=null){for( var i=0;i<select.length;i++){window.HtmlViewer.show(select.options [i] .text,select.options [i] .value);};window.HtmlViewer.nextselect();};";

    private String wkstecoding = "var main=document.getElementById('controller-base');var flag1=-1,flag2=2,flag3=3;var gc=0;var datcolor=0;" +
            "var sub=main.querySelectorAll('div[class=\"statements-view\"]')[0];sub=sub.querySelectorAll('div table')[0];if(sub!=null){sub=sub.nextSibling;}else{sub=null;};" +
            "function datchange(e){if(flag1==1){sub=main.querySelectorAll('div[class=\"statements-view\"]')[0];sub=sub.querySelectorAll('div table')[0];if(sub!=null){sub=sub.nextSibling;}else{sub=null;};" +
            "processdays(sub,1);}}" +


            "function processdays(xxx,type){" +
            "if(xxx==null || xxx.childElementCount==0){return;};var days=xxx.firstChild;var i=0;while(days!=null){" +
            "var tem=days.querySelectorAll('ul')[0];if(tem.childElementCount==0){break;};item=tem.firstChild;" +
            "var dat=item.firstChild.firstChild;item=item.nextSibling.nextSibling;" +
            "if(type==0){if(i==0){flag2=dat.textContent;};}else if(type=1){" +
            "if(i==0){var tem=dat.textContent;if(tem==flag2){flag1=1;return;}else{flag2=tem;flag1=-1;};}};" +
            "dat.style.color='red';gc=0;datcolor=0;" +
            "while(item!=null){processtrip(dat,item);if(item!=null){item=item.nextSibling;}};" +
            "i++;if(days!=null){days=days.nextSibling;}" +
            "if(days==null){flag1=1;}; if(datcolor==0){dat.style.color='red';}else if(datcolor==1){dat.style.color='purple';}else{dat.style.color='green';};" +
            "};}" + // end of day function

            "function processDomdaysxxxxxx(xxx){" +
            "var days=xxx.firstChild;var i=0;while(days!=null){" +
            "var item=days.querySelectorAll('ul')[0].firstChild;" +
            "var dat=item.firstChild.firstChild;if(item!=null){item=item.nextSibling;};if(item!=null){item=item.nextSibling;};" +
            "if(i==0){var tem=dat.textContent;if(tem==flag2){flag1=1;return;}else{flag2=tem;flag1=-1;};}" +
            "dat.style.color='red';gc=0;datcolor=0;" +
            "while(item!=null){processtrip(dat,item);if(item!=null){item=item.nextSibling;}};" +
            "i++;if(days!=null){days=days.nextSibling;}" +
            "if(days==null){flag1=1;}; if(datcolor==0){dat.style.color='red';}else if(datcolor==1){dat.style.color='purple';}else{dat.style.color='green';};" +
            "};}" + // end of day function


            "function processtrip(dat,item){" +
            "var tm=item.firstChild.firstChild.firstChild.firstChild.firstChild;if(tm==null){return;};" +
            "var du=tm.nextSibling.nextSibling;" +
            "var dis=du.nextSibling;" +
            "var s=item.firstChild.firstChild.nextSibling.lastChild;" +
            "flag3=s.textContent;" +
            "var result=window.HtmlViewer.trip(dat.textContent,tm.textContent,du.textContent,dis.textContent,s.textContent);" +
            "if(result){datcolor=1;gc++;tm.style.color='green';s.style.color='green';if(gc==(item.parentNode.childElementCount-2)){datcolor=2;};}else{if(" +
            "datcolor==0){datcolor=0;}else{datcolor=1;};s.style.color='red';};" +
            "}" +//end of trip function

            // current statement
            "if(sub!=null){processdays(sub,0);}else{window.HtmlViewer.show('sub=nullllllll');};" +
            "document.addEventListener('DOMSubtreeModified',datchange,false);";
}
