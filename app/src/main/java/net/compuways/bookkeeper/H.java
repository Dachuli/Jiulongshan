package net.compuways.bookkeeper;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Created by zule on 1/29/17.
 * usage code: entryid for every entry
 * all records=-1
 */

public class H {

    public static final String D = "p";// d=d developmentt, d=p, produttion
    public static final int LIMIT = 300 * 1000,//time limit for no internet idle
            MAIN = 1,
            ADSLOCK=15*60*1000,//15 minutes
            DIA = 2,
            DAYCHK =0,//1000*60*60*24,
            ADSRESET=0,//1000*60*60*24,
            TRIPLIMIT=150;
    public static long FAILEDTIME = 0, LIFE = 0;
    public static int MSGLEN = 150, Repeat=10;
    public static boolean PAID = false,adsfreerun=true;;
    public static final String SKU = "ulbk200",SEPERATOR="龞龤";
    public static final String KEY_DATE = "_date";
    public static final String KEY_RCDATE = "_rcdate";//record creationg date
    public static final String KEY_ITEM = "_name";
    public static final String KEY_AMOUNT = "_amount";
    public static final String KEY_NOTE = "_note";
    public static final String KEY_SOURCE = "_source",DESC="DESC",ASC="ASC";
    public static final String SETTINGKEY = "-3", USERCODE = "-4", SN = "-5",TRIPITEM="-100",VIEWID="-101";//(-100 t0 -200 for System usage)
    public static final String DRIVERSETTINGS = "DRIVERSETTINGS";
    public static final String STATISTICS = "STATISTICS";
    public static final String ALLRECORDS = "ALLRECORDS";
    public static final int SUMMARY = 1, DSETTING = 2,UBERPAY = 3,TIP = 4,GAS = 5, DATA = 6, INSURANCE = 7,MEAL = 8,MAINTENANCE = 9, DSAVING = 10,EXPENSES = 11, MILEAGE = 12, ONLINEHRS = 13, TRIPHRS = 14,
             OTHERINCOME = 15, LYFTPAY = 16, TAX = 17, MILEPORT = 1, MILELAND = 2, ENTRIES = -3, DATAE = -2, ALLR = -1,WEEKR=-5,DAILYR=-6,
            UBERLOGIN = -10, LYFTLOGIN = -11,ENTRYMATCH=-12, REWARDV=-14,D_ENTRIES = -15,
            TRIPDETAIL=30000,FARE = 30001,  FEES = 30002, CANCELLATION = 30003, UBKP = 30004, UBKD = 30005,PREPARETIME = 30006,
            TAXS=30007,TIPS=30008,TOLL=30009,OCOST=30010,OINCOME=30011,DISTANCE=30012,WTINCOME=30013,FAREADJ=30014,BONUS=30015,ONEUTRAL=30016,PBONUS=30017,
            WK_BONUS=30022,WK_EARNING=30023,WK_PD_BONUS=30024,UB_PAYOUT=30025,WK_ONLINEHRS=30026,
            TRIPTOLL=30028,LYFTFARE=30029,LYFTFEE=30030,TRIPTIP=30031,TRIPTAX=30032,TRIPCANCEL=30033,TRIPCOST=30034,TRIPINCOME=30035,SURGE=30036,TRIPOTHERS=30037,UBOOST=30038;
    public static final int CLICK = 1,DIR=-13, LCLICK = 0, AUTOSAVE = 100, MANUALSAVE = 101, RECEIVABLE = 1001, PAYABLE = 1002, NONCOUNTABLE = 1000,ALL=0,ULACC=1,ULACCS=2;

    public static final short UBER = 0, LYFT = 1, OTHER = 2, OVERHEAD = 3,TRIP=10,WK=11,WKS=-93,ALLS=12,UBERS=13;

    public static double round(double am) {

        return Math.round(am * 100.0) / 100.0;
    }

    public static final String version = "3.3.1";

    public static void sendMsg(String msg) {

    }

    public static String getEntryNameByID(Context context, int entryid) {
        DatabaseHandler db = DatabaseHandler.getInstance(context);
        return db.getEntryNameByID(entryid);
    }

    public static boolean isEmailValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        if (email == null) {
            return false;
        }else {

            int n=email.lastIndexOf(".");
            n=email.length()-n;

            if(n==3){
                return false;
            }else {

                return pat.matcher(email).matches();
            }
        }
    }
    public static String getSourceName(short code) {
        switch (code) {

            case 1:
                return "Lyft";
            case 2:
                return "Other";
            case 3:
                return "Overhead";
            case 0:
                return "Uber";
        }
        return "x";
    }

    public static void pupmsg(Context context, String msg) {
        if(context==null){
            return;
        }
        new AlertDialog.Builder(context).setMessage(msg).setPositiveButton("Close",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {

                        dialog.dismiss();

                    }
                }).show();
    }

    public static void remind(Context context) {
            //System.out.println(" Remind /  /out");
        long lapse = (System.currentTimeMillis() - H.FAILEDTIME);

       // System.out.println(" Remind /  / lasp="+lapse+"     "+H.LIMIT);
        if ((lapse) > H.LIMIT && lapse < (System.currentTimeMillis() - 60 * 1000)) {
            H.pupmsg(context, "Sorry! Free app should run ads. Turn on internet connection to avoid this message. Thanks!");
           // System.out.println(" Remind inside");
        }
    }

    public static void fillgridlayout(final Context context, GridLayout gridLayout, ArrayList<Item> items,
                                      long entryid, int width,
                                      int column,
                                      boolean editable,
                                      boolean allrecords,
                                      boolean orderable,
                                      View.OnClickListener clicklistener,
                                      View.OnClickListener orderlistener,
                                      View.OnClickListener cclistener,
                                      View.OnLongClickListener longClick
    ) {
        fillgridlayout(context,gridLayout,items,entryid,width,column,editable,allrecords,orderable,clicklistener,orderlistener,cclistener,longClick,null);

    }

    public static void fillgridlayout(final Context context, final GridLayout gridLayout, ArrayList<Item> items,
                                      long entryid, int width,
                                      int column,
                                      boolean editable,
                                      boolean allrecords,
                                      boolean orderable,
                                      View.OnClickListener clicklistener,
                                      View.OnClickListener orderlistener,
                                      View.OnClickListener cclistener,
                                      View.OnLongClickListener longClick,
                                      final ProgressBar pb
    ) {
        /*
        amount column store item id
        date column hint store date long value
        date column Id store ending reading  amount
        period column Id store begining reading rcdate

        */

        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);


        gridLayout.removeAllViews();
        int rows = (int)(items.size()*(1+4.0/10.0)+5+ 2);
        gridLayout.setMinimumWidth((int) (width * 0.9));
        gridLayout.setColumnCount(column);
        gridLayout.setRowCount(rows);
        int margin = 20;

        int r = 0;
        GridLayout.Spec rowSpan = GridLayout.spec(r);
        GridLayout.Spec colspan = GridLayout.spec(0);

        GridLayout.LayoutParams gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        TextView text = new TextView(context);
        RadioGroup rg = null;
        RadioButton rb = null;
        final SimpleDateFormat sdt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        //----------header-----------------
        int m = 0;
        int e = 0;
        int d = 0;
        if (editable) {
            // This is an empty element to make the number first row elements equal to the rest of rows,
            // otherwise the getValue(r,c) value would be different
            rowSpan = GridLayout.spec(r);
            colspan = GridLayout.spec(0);
            gridParam = new GridLayout.LayoutParams(
                    rowSpan, colspan);
            gridParam.setMargins(0, 0, margin, 10);
            gridParam.setGravity(Gravity.CENTER_HORIZONTAL);
            ImageButton btn = new ImageButton(context);
            Item item=new Item();
            if(((MainActivity)context).dir.equalsIgnoreCase(H.DESC)) {
                btn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_next_black_24dp));
                item.set_xxx(H.DESC);
            }else{
                btn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_previous_black_24dp));
                item.set_xxx(H.ASC);
            }
            btn.setOnClickListener(clicklistener);

            item.set_name(""+H.DIR);

            btn.setTag(item);
            gridLayout.addView(btn, gridParam);


            e = 1;
        }

        rowSpan = GridLayout.spec(r);
        colspan = GridLayout.spec(0 + e);
        gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        gridParam.setMargins(0, 0, margin, 10);
        gridParam.setGravity(Gravity.LEFT);
        text = new TextView(context);
        switch ((int) entryid) {
            case H.WK_ONLINEHRS:
                text.setText("Weekly Hrs");
                break;
            case H.ONLINEHRS:
            case H.TRIPHRS:
            case H.TRIPDETAIL:
            case H.PREPARETIME:
                text.setText("Duration");
                break;
            case H.MILEAGE:
            case H.DISTANCE:
                if (context instanceof MainActivity) {
                    text.setText((((MainActivity) (context)).unit).equalsIgnoreCase("metric") ? "Kms " : "Miles ");
                } else {
                    text.setText("Amount");
                }

                break;
            case H.DATAE:
            case H.ALLR:
                text.setText("Amount");
                break;
            default:

                if (DatabaseHandler.getInstance(context).getEntryTypeByID(entryid) == H.PAYABLE ||
                        DatabaseHandler.getInstance(context).getEntryTypeByID(entryid) == H.RECEIVABLE ||
                        entryid==H.TRIPTIP ||entryid==H.UB_PAYOUT ||
                        entryid==H.TRIPTAX ||entryid==H.TRIPTOLL ||entryid==H.TRIPINCOME ||
                        entryid==H.TAXS ||entryid==H.TOLL ||entryid==H.TRIPTOLL ||entryid==H.TRIPINCOME ||
                        entryid==H.TRIPCOST ||entryid==H.TRIPCANCEL ||
                        entryid==H.LYFTFEE ||entryid==H.LYFTFARE) {
                    String $=((MainActivity) (context)).cs;
                    text.setText($+$+$);
                } else {
                    text.setText("Amount");
                }


        }

        if (entryid != H.DATAE && entryid != H.ALLR) {
            text.setTag("3");
            text.setHint("" + entryid);
            text.setOnClickListener(orderlistener);
            text.setTextColor(Color.rgb(0, 100, 0));
        }
        gridLayout.addView(text, gridParam);//amount


        if (entryid == H.MILEAGE || allrecords || entryid == -2) {
            rowSpan = GridLayout.spec(r);
            colspan = GridLayout.spec(1 + e);
            gridParam = new GridLayout.LayoutParams(
                    rowSpan, colspan);
            gridParam.setMargins(0, 0, margin, 10);
            gridParam.setGravity(Gravity.CENTER_HORIZONTAL);
            text = new TextView(context);
            text.setText("Range(Start,End)");
            gridLayout.addView(text, gridParam);//
            m = 1;
        }


        rowSpan = GridLayout.spec(r);
        colspan = GridLayout.spec(1 + m + e);
        gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        gridParam.setMargins(0, 0, margin, 10);
        gridParam.setGravity(Gravity.CENTER_HORIZONTAL);
        text = new TextView(context);
        text.setText("Date Time");
        if (editable || entryid == -1) {
            // text.setId(0);
            text.setTag("0");
            text.setHint("" + entryid);
            text.setOnClickListener(orderlistener);
            text.setTextColor(Color.rgb(0, 100, 0));
        }
        gridLayout.addView(text, gridParam);// date


        rowSpan = GridLayout.spec(r);
        colspan = GridLayout.spec(2 + m + e);
        gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        gridParam.setMargins(0, 0, margin, 10);
        gridParam.setGravity(Gravity.CENTER_HORIZONTAL);

        text = new TextView(context);
        text.setText("Company");
        if (editable || entryid == -1) {
            //  text.setId(1);
            text.setTag("1");
            text.setHint("" + entryid);
            text.setOnClickListener(orderlistener);
            text.setTextColor(Color.rgb(0, 100, 0));

        }
        gridLayout.addView(text, gridParam);//


        if (entryid == -1 || entryid == -2) {
            rowSpan = GridLayout.spec(r);
            colspan = GridLayout.spec(3 + m + e);
            gridParam = new GridLayout.LayoutParams(
                    rowSpan, colspan);
            gridParam.setMargins(0, 0, margin, 10);
            gridParam.setGravity(Gravity.LEFT);
            text = new TextView(context);
            text.setText("Entry Name");

            // text.setId(2);
            text.setTag("2");
            text.setHint("" + entryid);
            text.setOnClickListener(orderlistener);

            if (entryid == -1) {
                text.setTextColor(Color.rgb(0, 100, 0));
            }

            gridLayout.addView(text, gridParam);
            d = 1;

        }


        rowSpan = GridLayout.spec(r);
        colspan = GridLayout.spec(3 + m + e + d);
        gridParam = new GridLayout.LayoutParams(
                rowSpan, colspan);
        gridParam.setMargins(0, 0, margin, 10);
        gridParam.setGravity(Gravity.CENTER_HORIZONTAL);
        text = new TextView(context);
        text.setText("Memo");
        gridLayout.addView(text, gridParam);// source

        //-----------end of header----------

        int topm=0-Integer.parseInt(SP.getString("entryadjust","0"));
        EditText texte = null;
        r = 1;
        final double size=items.size();
      //  final Handler handler = new Handler();
        for (int i = 0; i < items.size(); i++) {
            //*/
            if(r>rows-1) {
                rows+=3;
               // System.out.println("r=========*********************============="+r+"  ,,  "+rows);
                gridLayout.setRowCount(rows);
            }

            //*/
            m = 0;
            e = 0;
            d = 0;
            final Item item = items.get(i);
            //   final EditText texta = new EditText(context);

            if (editable) {
                rowSpan = GridLayout.spec(r);
                colspan = GridLayout.spec(0);
                gridParam = new GridLayout.LayoutParams(
                        rowSpan, colspan);
                gridParam.setMargins(0, topm, margin, 0);
                ImageButton btn = new ImageButton(context);

                btn.setId(r);

                if(item.get_name().equalsIgnoreCase(""+H.TRIPDETAIL)){
                    btn.setOnLongClickListener(longClick);

                    if(item.get_rcdate()==1){
                        btn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_check_box_green_24dp));
                    }else{
                        btn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_check_box_black_24dp));
                    }
                }else{
                    btn.setImageDrawable(((MainActivity)context).editdrawable);
                }


                btn.setOnClickListener(clicklistener);
                btn.setTag(item);
                gridLayout.addView(btn, gridParam);// button
                e = 1;
            }

//--------------amount-----------

            rowSpan = GridLayout.spec(r);
            colspan = GridLayout.spec(e);
            gridParam = new GridLayout.LayoutParams(
                    rowSpan, colspan);
            gridParam.setMargins(0, 40, margin, 0);
            gridParam.setGravity(Gravity.LEFT);
            if (entryid == -2) {
                final EditText texta = new EditText(context);
                if (!H.PAID && !item.get_name().equalsIgnoreCase("" + H.TIP)) {
                    texta.setEnabled(false);
                }
                texta.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                texta.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String str = s.toString();
                        if (str == null || str.length() <= 0) {
                            return;
                        }


                        if (!item.get_name().equalsIgnoreCase("" + H.MILEAGE)) {
                            item.set_amount(Double.parseDouble(str));
                            DatabaseHandler.getInstance(context).updateItem(item);
                        }

                        Item ucode = new Item();
                        ucode.set_note("SS1");
                        ucode.set_name(H.USERCODE);
                        DatabaseHandler.getInstance(context).addItem(ucode);

                    }

                    @Override
                    public void afterTextChanged(Editable s) {


                    }
                });
                if (item.get_name().equalsIgnoreCase("" + H.MILEAGE)) {
                    texta.setText("" + (int) (item.get_amount() - item.get_rcdate()));
                    texta.setEnabled(false);
                } else {
                    texta.setText("" + H.round(item.get_amount()));
                }

                gridLayout.addView(texta, gridParam);//amount

                // range :

                if (item.get_name().equalsIgnoreCase("" + H.MILEAGE)) {
                    rowSpan = GridLayout.spec(r);
                    colspan = GridLayout.spec(1 + e);
                    gridParam = new GridLayout.LayoutParams(
                            rowSpan, colspan);
                    gridParam.setMargins(0, 40, margin, 0);
                    gridParam.setGravity(Gravity.LEFT);
                    LinearLayout llayout = new LinearLayout(context);
                    llayout.setOrientation(LinearLayout.HORIZONTAL);
                    texte = new EditText(context);
                    if (!H.PAID && !item.get_name().equalsIgnoreCase("" + H.TIP)) {
                        texte.setEnabled(false);
                    }
                    texte.setInputType(InputType.TYPE_CLASS_NUMBER);
                    texte.setText(item.get_rcdate() + "");
                    texte.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            String str = s.toString();
                            if (str == null || str.length() <= 0) {
                                return;
                            }
                            item.set_rcdate(Long.parseLong(str));

                            texta.setText("" + (int) (item.get_amount() - item.get_rcdate()));
                            if ((item.get_amount() - item.get_rcdate()) < 0) {
                                texta.setTextColor(Color.RED);
                            } else {
                                texta.setTextColor(Color.BLACK);
                            }
                            DatabaseHandler.getInstance(context).updateItem(item);
                            Item ucode = new Item();
                            ucode.set_note("SS2");
                            ucode.set_name(H.USERCODE);
                            DatabaseHandler.getInstance(context).addItem(ucode);

                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                    llayout.addView(texte);

                    texte = new EditText(context);
                    if (!H.PAID && !item.get_name().equalsIgnoreCase("" + H.TIP)) {
                        texte.setEnabled(false);
                    }
                    texte.setInputType(InputType.TYPE_CLASS_NUMBER);
                    texte.setText(((int) item.get_amount()) + "");
                    texte.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            String str = s.toString();
                            if (str == null || str.length() <= 0) {
                                return;
                            }
                            item.set_amount(Long.parseLong(str));
                            texta.setText("" + (int) (item.get_amount() - item.get_rcdate()));
                            if ((item.get_amount() - item.get_rcdate()) < 0) {
                                texta.setTextColor(Color.RED);
                            } else {
                                texta.setTextColor(Color.BLACK);
                            }
                            DatabaseHandler.getInstance(context).updateItem(item);
                            Item ucode = new Item();
                            ucode.set_note("SS2");
                            ucode.set_name(H.USERCODE);
                            DatabaseHandler.getInstance(context).addItem(ucode);

                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                    llayout.addView(texte);
                    gridLayout.addView(llayout, gridParam);//
                    m = 1;
                }

            } else {
                text = new TextView(context);
                if (items.get(i).get_name().equalsIgnoreCase("" + H.MILEAGE)) {
                    text.setText("" + (int) (items.get(i).get_amount() - items.get(i).get_rcdate()));
                } else if (items.get(i).get_name().equalsIgnoreCase("" + H.ONLINEHRS) ||
                        items.get(i).get_name().equalsIgnoreCase("" + H.TRIPDETAIL) ||
                        items.get(i).get_name().equalsIgnoreCase("" + H.PREPARETIME) ||
                        items.get(i).get_name().equalsIgnoreCase("" + H.WK_ONLINEHRS) ||
                        items.get(i).get_name().equalsIgnoreCase("" + H.TRIPHRS)) {
                    text.setText(H.hToX(items.get(i).get_amount()));
                    text.setTag(items.get(i));
                } else {
                    text.setText("" + H.round(item.get_amount()));
                }
                text.setId((int) item.get_id());
                gridLayout.addView(text, gridParam);//amount
            }


//----------------range--------

            if (item.get_name().equalsIgnoreCase("" + H.MILEAGE) && entryid == -2) {
                ;
            } else if (item.get_name().equalsIgnoreCase("" + H.MILEAGE)) {
                rowSpan = GridLayout.spec(r);
                colspan = GridLayout.spec(1 + e);
                gridParam = new GridLayout.LayoutParams(
                        rowSpan, colspan);
                gridParam.setMargins(0, 40, margin, 0);
                gridParam.setGravity(Gravity.LEFT);
                text = new TextView(context);
                text.setText("(" + items.get(i).get_rcdate() + "-" + (int) items.get(i).get_amount() + ")");
                text.setId((int) items.get(i).get_rcdate());
                gridLayout.addView(text, gridParam);//
                m = 1;
            } else if (allrecords || entryid == -2) {// in all records situaion,non mileage entry must add empty column to make it look nice

                rowSpan = GridLayout.spec(r);
                colspan = GridLayout.spec(1 + e);
                gridParam = new GridLayout.LayoutParams(
                        rowSpan, colspan);
                gridParam.setMargins(0, 40, margin, 0);
                gridParam.setGravity(Gravity.LEFT);
                text = new TextView(context);
                if (entryid == -2 && !H.PAID && !item.get_name().equalsIgnoreCase("" + H.TIP)) {
                    text.setText("Editable in AdsFree");
                } else {
                    text.setText("------");
                }
                gridLayout.addView(text, gridParam);//
                m = 1;

            }
//-----------------Date:-------------

            rowSpan = GridLayout.spec(r);
            colspan = GridLayout.spec(1 + m + e);
            gridParam = new GridLayout.LayoutParams(
                    rowSpan, colspan);
            gridParam.setMargins(0, 40, margin, 0);
            gridParam.setGravity(Gravity.LEFT);

            String mdate = sdt.format(item.get_date());

            if (entryid == -2) {
                final EditText texte2 = new EditText(context);
                texte2.setText(mdate);
                if (!H.PAID && !item.get_name().equalsIgnoreCase("" + H.TIP)) {
                    texte2.setEnabled(false);
                }
                texte2.setInputType(InputType.TYPE_CLASS_DATETIME);
                texte2.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.length() != 19) {
                            return;
                        } else {
                            String str = s.toString();
                            int n0 = str.indexOf("-");
                            int n1 = str.indexOf("-", n0 + 1);
                            int n2 = str.indexOf("-", n1 + 1);
                            int n3 = str.indexOf(":");
                            int n4 = str.indexOf(":", n3 + 1);
                            int n5 = str.indexOf(":", n4 + 1);
                            if (n0 != 4 ||
                                    n1 != 7 ||
                                    n2 >= 0 ||
                                    n3 != 13 ||
                                    n4 != 16 ||
                                    n5 >= 0) {
                                return;
                            }
                        }
                        if (texte2.getCurrentTextColor() == Color.BLUE) {
                            texte2.setTextColor(Color.CYAN);
                        } else {
                            texte2.setTextColor(Color.BLUE);
                        }


                        Timestamp ts = Timestamp.valueOf(s.toString().trim());
                        ts.getTime();
                        item.set_date(ts.getTime());
                        DatabaseHandler.getInstance(context).updateItem(item);

                        Item ucode = new Item();
                        ucode.set_note("SS3");
                        ucode.set_name(H.USERCODE);
                        DatabaseHandler.getInstance(context).addItem(ucode);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                if (item.get_name().equalsIgnoreCase("" + H.MILEAGE)) {
                    texte2.setId((int) item.get_amount());//?
                }
                gridLayout.addView(texte2, gridParam);// date

            } else {
                text = new TextView(context);
                text.setText(mdate);
                if (item.get_name().equalsIgnoreCase("" + H.MILEAGE)) {
                    text.setId((int) item.get_amount());//?
                }
                text.setHint("" + items.get(i).get_date());//used in ItemDeatailActivity
                gridLayout.addView(text, gridParam);// date
            }


//---------radio button----------

            rowSpan = GridLayout.spec(r);
            colspan = GridLayout.spec(2 + m + e);
            gridParam = new GridLayout.LayoutParams(
                    rowSpan, colspan);
            gridParam.setGravity(Gravity.LEFT);

            if (entryid == H.DATAE) {
                gridParam.setMargins(0, 70, margin + 30, 0);
                rg = new RadioGroup(context);
                rg.setOrientation(RadioGroup.HORIZONTAL);


                rb = new RadioButton(context);
                if (!H.PAID && !item.get_name().equalsIgnoreCase("" + H.TIP)) {
                    rb.setEnabled(false);
                }

                rb.setText("Uber");
                rb.setHint("" + (4 * i));
                rb.setOnClickListener(cclistener);
                rg.addView(rb);
                if (items.get(i).get_name().equalsIgnoreCase("" + H.GAS) ||
                        items.get(i).get_name().equalsIgnoreCase("" + H.INSURANCE) ||
                        items.get(i).get_name().equalsIgnoreCase("" + H.MAINTENANCE) ||
                        items.get(i).get_name().equalsIgnoreCase("" + H.DATA) ||
                        items.get(i).get_name().equalsIgnoreCase("" + H.MEAL) ||
                        items.get(i).get_name().equalsIgnoreCase("" + H.LYFTPAY) ||
                        items.get(i).get_name().equalsIgnoreCase("" + H.DSAVING)) {
                    rb.setEnabled(false);
                }

                rb = new RadioButton(context);
                if (!H.PAID && !item.get_name().equalsIgnoreCase("" + H.TIP)) {
                    rb.setEnabled(false);
                }
                rb.setText("Lyft");
                rb.setHint("" + (4 * i + 1));
                rb.setOnClickListener(cclistener);
                rg.addView(rb);
                if (items.get(i).get_name().equalsIgnoreCase("" + H.GAS) ||
                        items.get(i).get_name().equalsIgnoreCase("" + H.INSURANCE) ||
                        items.get(i).get_name().equalsIgnoreCase("" + H.MAINTENANCE) ||
                        items.get(i).get_name().equalsIgnoreCase("" + H.DATA) ||
                        items.get(i).get_name().equalsIgnoreCase("" + H.MEAL) ||
                        items.get(i).get_name().equalsIgnoreCase("" + H.UBERPAY) ||
                        items.get(i).get_name().equalsIgnoreCase("" + H.DSAVING)) {
                    rb.setEnabled(false);
                }


                rb = new RadioButton(context);
                if (!H.PAID && !item.get_name().equalsIgnoreCase("" + H.TIP)) {
                    rb.setEnabled(false);
                }
                rb.setHint("" + (4 * i + 2));
                rb.setText("Other");
                rb.setOnClickListener(cclistener);
                rg.addView(rb);
                if (items.get(i).get_name().equalsIgnoreCase("" + H.GAS) ||
                        items.get(i).get_name().equalsIgnoreCase("" + H.INSURANCE) ||
                        items.get(i).get_name().equalsIgnoreCase("" + H.MAINTENANCE) ||
                        items.get(i).get_name().equalsIgnoreCase("" + H.DATA) ||
                        items.get(i).get_name().equalsIgnoreCase("" + H.MEAL) ||
                        items.get(i).get_name().equalsIgnoreCase("" + H.UBERPAY) ||
                        items.get(i).get_name().equalsIgnoreCase("" + H.LYFTPAY) ||
                        items.get(i).get_name().equalsIgnoreCase("" + H.DSAVING)) {
                    rb.setEnabled(false);
                }

                rb = new RadioButton(context);
                if (!H.PAID && !item.get_name().equalsIgnoreCase("" + H.TIP)) {
                    rb.setEnabled(false);
                }
                rb.setHint("" + (4 * i + 3));
                rb.setText("Overhead");
                rb.setOnClickListener(cclistener);
                if (items.get(i).get_name().equalsIgnoreCase("" + H.TRIPHRS) ||
                        items.get(i).get_name().equalsIgnoreCase("" + H.UBERPAY) ||
                        items.get(i).get_name().equalsIgnoreCase("" + H.LYFTPAY) ||
                        items.get(i).get_name().equalsIgnoreCase("" + H.MILEAGE) ||
                        items.get(i).get_name().equalsIgnoreCase("" + H.OTHERINCOME) ||
                        items.get(i).get_name().equalsIgnoreCase("" + H.TIP) ||
                        items.get(i).get_name().equalsIgnoreCase("" + H.ONLINEHRS)
                        ) {
                    rb.setEnabled(false);
                }
                rg.addView(rb);

                gridLayout.addView(rg, gridParam);// sources

            } else {
                gridParam.setMargins(20, 40, margin, 20);
                text = new TextView(context);
                text.setText(H.getSourceName(items.get(i).get_source()));
                text.setId(items.get(i).get_source());
                gridLayout.addView(text, gridParam);// source
            }

            //------------company--------

            if (entryid == -1 || entryid == -2) {
                rowSpan = GridLayout.spec(r);
                colspan = GridLayout.spec(3 + m + e);
                gridParam = new GridLayout.LayoutParams(
                        rowSpan, colspan);
                gridParam.setMargins(0, 40, margin, 0);
                gridParam.setGravity(Gravity.LEFT);
                text = new TextView(context);
                //
                text.setText(H.getEntryNameByID(context, Integer.parseInt(items.get(i).get_name())));
                gridLayout.addView(text, gridParam);
                d = 1;

            }

//---------note:--------------
            rowSpan = GridLayout.spec(r);
            colspan = GridLayout.spec(3 + m + e + d);
            gridParam = new GridLayout.LayoutParams(
                    rowSpan, colspan);
            gridParam.setMargins(0, 40, margin, 0);
            gridParam.setGravity(Gravity.LEFT);
            if (entryid == -2) {

                final EditText text0 = new EditText(context);
                if (!H.PAID && !item.get_name().equalsIgnoreCase("" + H.TIP)) {
                    text0.setEnabled(false);
                }
                text0.setText(items.get(i).get_note());
                text0.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        item.set_note(text0.getText().toString());
                        DatabaseHandler.getInstance(context).updateItem(item);
                        Item ucode = new Item();
                        ucode.set_note("SS6");
                        ucode.set_name(H.USERCODE);
                        DatabaseHandler.getInstance(context).addItem(ucode);

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                gridLayout.addView(text0, gridParam);//

            } else {
                text = new TextView(context);
                text.setText(items.get(i).get_note());
                gridLayout.addView(text, gridParam);//
            }


            r++;

            final int j=i;
            int step=50;
            if(entryid==H.DATAE){
                step=5;
            }else if(entryid==H.ALLR){
                step=20;
            }
            if(pb!=null && (i%step)==0){
                ((MainActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pb.setProgress((int)((j/size)*100));
                    }
                });

            }else if(pb!=null && i >size-3){
                ((MainActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pb.setProgress(100);
                    }
                });

            }

            if(!H.PAID && ((MainActivity)context).adsfreetime<=0) {
                if (r % H.Repeat == H.Repeat - 1) {
                    r = r + 3;
                }
            }


        }


    }

    public static int getScreenWidth(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        if(activity!=null) {
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        }
        return displayMetrics.widthPixels;
    }

    public static int getScreenHeight(Activity activity) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public static View getValue(GridLayout gridLayout, int r, int column) {
        int n = gridLayout.getColumnCount();

        // return ""+gridLayout.getChildCount()+" "+gridLayout.getRowCount()+"  "+gridLayout.getColumnCount()+
        //    ((TextView)gridLayout.getChildAt(2)).getText();
        return (View) gridLayout.getChildAt(r * n + column);

    }

    public static View getBtn(GridLayout gridLayout, String tripid) {
        int r = gridLayout.getRowCount();
        int n = gridLayout.getColumnCount();
        for(int i=1;i<r;i++){
            ImageButton btn=(ImageButton) gridLayout.getChildAt(i*n);
            if(btn==null){
                continue;
            }
            Item item=(Item)btn.getTag();
            if(item.get_xxx().contains(tripid)){
                return btn;
            }
        }
        return null;

    }


    public static void updateSPOrder(ArrayList<Integer> order, SharedPreferences SP) {

        String orderstr = "";
        for (int i = 0; i < order.size(); i++) {
            if (i == 0) {
                orderstr = "" + order.get(i);
            } else {
                orderstr += "," + order.get(i);
            }
        }

        SP.edit().putString("order", orderstr).commit();

    }

    public static void updateSPULOrder(ArrayList<Integer> order, SharedPreferences SP) {

        String orderstr = "";
        for (int i = 0; i < order.size(); i++) {
            if (i == 0) {
                orderstr = "" + order.get(i);
            } else {
                orderstr += "," + order.get(i);
            }
        }

        SP.edit().putString("ulorder", orderstr).commit();

    }

    public static AdListener adlistener = new AdListener() {
        @Override
        public void onAdLoaded() {
            // Code to be executed when an ad finishes loading.
            FAILEDTIME = 0;
        }

        @Override
        public void onAdFailedToLoad(int errorCode) {
            // Code to be executed when an ad request fails.
            if (FAILEDTIME == 0) {
                FAILEDTIME = System.currentTimeMillis();
            }
        }

        @Override
        public void onAdOpened() {
            // Code to be executed when an ad opens an overlay that
            // covers the screen.
        }

        @Override
        public void onAdLeftApplication() {
            // Code to be executed when the user has left the app.
        }

        @Override
        public void onAdClosed() {
            // Code to be executed when when the user is about to return
            // to the app after tapping on an ad.
        }
    };

    public static void milesumop(EditText prereading, EditText amount, TextView milesum, Button save, int source, SharedPreferences SP) {
        String pre = prereading.getText().toString().trim();
        String amountf = amount.getText().toString().trim();
        if (pre.length() == 0 || amountf.length() == 0) {
            milesum.setTextColor(Color.RED);
            milesum.setText("Not Valid!");
            // save.setEnabled(false);
            return;
        }

        if (source == H.MAIN) {
            SP.edit().putInt("BREADING", Integer.valueOf(pre)).commit();
            SP.edit().putInt("EREADING", Integer.valueOf(amountf)).commit();
        }

        double dif = Double.parseDouble(amountf) - Double.parseDouble(pre);
        milesum.setText("" + dif);
        if (dif < 0) {
            milesum.setTextColor(Color.RED);
            save.setEnabled(false);
        } else {
            milesum.setTextColor(Color.rgb(0, 150, 0));
            save.setEnabled(true);
        }
    }

    public static int getEntryID(Context context, String name) {
        DatabaseHandler db = DatabaseHandler.getInstance(context);
        return db.nameToEntryID(name);

    }

    public static String getDigit(String str, char include) {
        char[] ch = str.trim().toCharArray();
        String tem = "";
        for (int i = 0; i < ch.length; i++) {
            if (Character.isDigit(ch[i]) || ch[i] == include)
                tem += ch[i];

        }
        return tem;
    }
    public static String getDigit(String str) {

        return getDigit(str,'颽');
    }
    public static String getLetter(String str) {
        char[] ch = str.trim().toCharArray();
        String tem = "";
        for (int i = 0; i < ch.length; i++) {
            if (Character.isLetter(ch[i]))
                tem += ch[i];

        }
        return tem;
    }
    public static String parseUtime(String time) {

        return parseUtime(time,false);
    }

    public static long dtToMiSecs(String dt) {
      //2017-12-12T22:09:05+00:00
        String or=dt;
        if(dt==null || dt.trim().length()==0){
            return -1;
        }
        int n=dt.indexOf("+");
        if(n>0){
            dt=dt.substring(0,n);
        }

        dt=dt.toUpperCase();
        dt=dt.replace("T"," ").trim();

        if(dt.length()==19) {
            Timestamp ts = Timestamp.valueOf(dt);
            return ts.getTime();
        }else{
            return -1;
        }


    }
    public static String parseUtime(String time,boolean special) {
        if (time == null || time.length() < 3) {
            return "00:00:00";
        }
        time=time.toUpperCase();

        String t=getDigit(time,':');
        int n = t.indexOf(":");
        if(n<0){

            if(time.contains("AM")){
                if(t.length()==0) {
                    return "00:00:00";
                }else if(t.length()==1){
                    return "0"+t+":00:00";
                }else if(t.length()==2){
                    return t+":00:00";
                }

            }else if(time.contains("PM")){
                if(t.length()==0){
                    t="0";
                }
                int h=12+Integer.parseInt(t);
                return h+":00:00";

            }else {
                return "00:00:00";
            }

        }

        //


        int h=Integer.parseInt(t.substring(0,n));
        String m=t.substring(n+1);
        n = m.indexOf(":");

        if(n<0){
            m=m+":00";
        }

        if(special){

            n=m.lastIndexOf(":");
            m=m.substring(0,n);
            m=m+":00";
        }

        m=m.trim();
        if(m.length()>5){
            m=m.substring(0,5);
        }

        time=time.toUpperCase();

        if(time.contains("AM")){

            if(h<10){
                return "0"+h+":"+m;
            }else{
                return h+":"+m;
            }

        }else if(time.contains("PM")){
            if(h<12){
              h=h+12;
            }
            return h+":"+m;
        }else {
            return "00:00:00";
        }


    }
    public static long parseViewId(String dt){
        if(dt==null || dt.length()<4){
            return -1;
        }
        Calendar calendar=Calendar.getInstance();
        int cmonth=calendar.get(Calendar.MONTH)+1;
        int cyear=calendar.get(Calendar.YEAR);
        String month,day;
        int year=-1;
        int sign=dt.indexOf("-");
        if(sign>0){
            String cmd=dt.substring(0,sign);
            sign=cmd.indexOf(" ");
            month=cmd.substring(0,sign).trim().toUpperCase();
            day=cmd.substring(sign+1).trim();
            if(day.length()==1){
                day="0"+day;
            }

        }else{
            sign=dt.trim().lastIndexOf(" ");
            String mt=dt.substring(0,sign);
            String y=dt.substring(sign+1).trim();

            sign=mt.trim().indexOf(" ");
            month=mt.substring(0,sign).trim().toUpperCase();
            day=mt.substring(sign+1).trim();
            if(day.length()==1){
                day="0"+day;
            }
            year=Integer.parseInt(y);

        }

        Timestamp ts=Timestamp.valueOf(parseyd(month,year,cmonth,cyear,day)+" 00:00:00");

        return ts.getTime();
    }
    public static String parseUDate(String date,int year){
        if (date == null || date.length() < 3) {
            return "2018-01-01";
        }
        String data0=date;
        String day="";
        int leng=date.length();
        int n = date.indexOf(",");
        date = date.substring(n + 1).trim();

        if(leng>20) {

            n = date.indexOf(",");

            if (n == -1) {
                date = data0;
            } else {
                String time = parseUtime(date.substring(n + 1));
                date = date.substring(0, n);
            }

            day = getDigit(date).trim();
            if (day.length() == 1) {
                day = "0" + day;
            }

        }else{
            n = date.indexOf(" ");
            day=getDigit(date).trim();

            if (day.length() == 1) {
                day = "0" + day;
            }

            date=date.substring(0,n);

        }
        String month=getLetter(date).toUpperCase();
        Calendar calendar=Calendar.getInstance();
        int cmonth=calendar.get(Calendar.MONTH)+1;
        int cyear=calendar.get(Calendar.YEAR);

        return parseyd(month,year,cmonth,cyear,day);

    }

    private static String parseyd(String month,int year,int cmonth,int cyear,String day){
        switch (month){
            case "JAN":
            case "JANUARY":

                if(year!=-1){
                    return year+"-01-" + day;
                }

                if(cmonth<1){
                    return (cyear-1)+"-01-" + day;
                }else {
                    return cyear+"-01-" + day;
                }
            case "FEB":
            case "FEBRUARY":
                if(year!=-1){
                    return year+"-02-" + day;
                }

                if(cmonth<2){
                    return (cyear-1)+"-02-" + day;
                }else {
                    return cyear+"-02-" + day;
                }
            case "MARCH":
            case "MAR":

                if(year!=-1){
                    return year+"-03-" + day;
                }

                if(cmonth<3){
                    return (cyear-1)+"-03-" + day;
                }else {
                    return cyear+"-03-" + day;
                }
            case "APR":
            case "APRIL":
                if(year!=-1){
                    return year+"-04-" + day;
                }
                if(cmonth<4){
                    return (cyear-1)+"-04-" + day;
                }else {
                    return cyear+"-04-" + day;
                }
            case "MAY":

                if(year!=-1){
                    return year+"-05-" + day;
                }
                if(cmonth<5){
                    return (cyear-1)+"-05-" + day;
                }else {
                    return cyear+"-05-" + day;
                }
            case "JUNE":
            case "JUN":

                if(year!=-1){
                    return year+"-06-" + day;
                }
                if(cmonth<6){
                    return (cyear-1)+"-06-" + day;
                }else {
                    return cyear+"-06-" + day;
                }
            case "JULY":
            case "JUL":

                if(year!=-1){
                    return year+"-07-" + day;
                }

                if(cmonth<7){
                    return (cyear-1)+"-07-" + day;
                }else {
                    return cyear+"-07-" + day;
                }
            case "AUGUST":
            case "AUG":
                if(year!=-1){
                    return year+"-08-" + day;
                }
                if(cmonth<8){
                    return (cyear-1)+"-08-" + day;
                }else {
                    return cyear+"-08-" + day;
                }
            case "SEPTEMBER":
            case "SEP":

                if(year!=-1){
                    return year+"-09-" + day;
                }
                if(cmonth<9){
                    return (cyear-1)+"-09-" + day;
                }else {
                    return cyear+"-09-" + day;
                }
            case "OCTOBER":
            case "OCT":

                if(year!=-1){
                    return year+"-10-" + day;
                }
                if(cmonth<10){
                    return (cyear-1)+"-10-" + day;
                }else {
                    return cyear+"-10-" + day;
                }
            case "NOVEMBER":
            case "NOV":

                if(year!=-1){
                    return year+"-11-" + day;
                }
                if(cmonth<11){
                    return (cyear-1)+"-11-" + day;
                }else {
                    return cyear+"-11-" + day;
                }
            case "DEC":
            case "DECEMBER":

                if(year!=-1){
                    return year+"-12-" + day;
                }
                if(cmonth<12){
                    return (cyear-1)+"-12-" + day;
                }else {
                    return cyear+"-12-" + day;
                }
            default:
                return cyear+"-01-"+day;
        }

    }
    public static double tToHr(String duration){

        int nm=duration.indexOf("m");
        int nh=duration.indexOf("h");
        int ns=duration.indexOf("s");
        double sum=0;

        if(nh>0){
            sum=Double.parseDouble(getDigit(duration.substring(0,nh)));
            if(nm>0){
                sum=sum+Double.parseDouble(getDigit(duration.substring(nh+1,nm).trim()))/60.0;
                if(ns>0){
                    sum=sum+Double.parseDouble(getDigit(duration.substring(nm+1,ns).trim()))/60.0/60.0;
                }
            }else{

                if(ns>0){
                    sum=sum+Double.parseDouble(getDigit(duration.substring(nh+1,ns).trim()))/60.0/60.0;
                }
            }
        }else{

            if(nm>0){
                sum=Double.parseDouble(getDigit(duration.substring(0,nm).trim()))/60.0;
                if(ns>0){
                    sum=sum+Double.parseDouble(getDigit(duration.substring(nm+1,ns).trim()))/60.0/60.0;
                }
            }else{
                if(ns>0){
                    sum=Double.parseDouble(getDigit(duration.substring(0,ns).trim()))/60.0/60.0;
                }
            }
        }

        return sum;

    }

    public static String hToX(double hr){
        long diff = (long) (hr * 60 * 60 * 1000);
        long seconds = (diff / 1000) % 60;
        long minutes = (diff / 1000 / 60) % 60;
        long hours = (diff / 1000 / 60 / 60);
        String str=(hours > 0 ? hours + ":" : "") + ((minutes < 10) ? "0" : "") + minutes + ":" + ((seconds < 10) ? "0" : "") + seconds;

        return str;
    }

    public static String sToX(String second){
        long diff = (long) (Double.parseDouble(second));
        long seconds = diff % 60;
        long minutes = diff / 60;
        long hours =0;
        if(minutes>=60){
            hours=minutes/60;
            minutes=minutes % 60;
        }
        String str=(hours > 0 ? hours + "hr" : "") + ((minutes >0) ? minutes+"m" : "") + ((seconds>0) ? seconds+"s" : "");

        return str;
    }


    public static void fillRadioGroup(Context context,RadioGroup rg,int selection){

        ArrayList<Item> entries=new ArrayList<>();
        ArrayList<Item> alls=DatabaseHandler.getInstance(context).getItemsByName(H.SETTINGKEY,H.DESC,0,1000);
        if(selection==H.ALL){
            entries=DatabaseHandler.getInstance(context).getItemsByName(H.SETTINGKEY,H.DESC,0,1000);
        }else if(selection==H.ULACC || selection==H.ULACCS){
            entries=DatabaseHandler.getInstance(context).getBLAccEntries();
        }else if(selection==H.TRIP){
            for(Item it:alls){
                if(it.get_rcdate()==H.TRIPTAX || it.get_rcdate()==H.TRIPOTHERS || it.get_rcdate()==H.TRIPCOST
                        || it.get_rcdate()==H.BONUS ||it.get_rcdate()==H.TRIPINCOME){
                    entries.add(it);
                }
            }
        }else if(selection==H.UBERS){
            for(Item it:alls){
                if(it.get_rcdate()==H.TAXS || it.get_rcdate()==H.OCOST || it.get_rcdate()==H.ONEUTRAL || it.get_rcdate()==H.OINCOME ||it.get_rcdate()==H.TOLL){
                    entries.add(it);
                }
            }
        }else if(selection==H.WK){
            for(Item it:alls){
                if(it.get_rcdate()==H.TAXS || it.get_rcdate()==H.OCOST || it.get_rcdate()==H.ONEUTRAL || it.get_rcdate()==H.OINCOME ||it.get_rcdate()==H.WK_BONUS){
                  entries.add(it);
                }
            }
        }else{
            entries=DatabaseHandler.getInstance(context).getBLAccEntries(selection);
        }

        for(Item it:entries){
            RadioButton rb=new RadioButton(context);
            rb.setText(it.get_note());
            rb.setTag(it);

            if(it.get_rcdate()!=H.SUMMARY && it.get_rcdate()!=H.DSETTING  &&
                    // it.get_rcdate()<30000  &&
                    it.get_rcdate()!=H.TRIPDETAIL  &&
                    !it.get_note().trim().equalsIgnoreCase("") &&
                    it.get_rcdate()!=H.TRIPHRS && it.get_rcdate()!=H.ONLINEHRS && it.get_rcdate()!=H.MILEAGE
                // && it.get_rcdate()!=H.LYFTPAY  && it.get_rcdate()!=H.UBERPAY
                    ) {

                if (it.get_date() == H.RECEIVABLE) {//problem
                    rb.setTextColor(Color.parseColor("#347235"));
                } else if (it.get_date() == H.PAYABLE) {
                    rb.setTextColor(Color.parseColor("#990012"));
                } else if(it.get_rcdate()==H.TAXS ||it.get_rcdate()==H.TOLL){
                    rb.setTextColor(Color.rgb(255,50,200));
                }else{
                    rb.setTextColor(Color.BLACK);
                }
                rg.addView(rb);

            }
        }


    }

    public static String xxxToTripid(String xxx){
        String tripid = "";
        int index = xxx.indexOf(H.SEPERATOR);

        if (index == -1) {
            tripid = xxx.trim();
        } else {
            tripid = xxx.substring(0, index).trim();
        }
        return tripid;
    }

    public static String getHtml(Context context,String tripid){


        String html="<html><body>";
        ArrayList<Item> items=null;
        if(tripid==null || tripid.trim().length()==0){
            items=DatabaseHandler.getInstance(context).getItemsByName(""+H.TRIPDETAIL,H.DESC,0,1000);
        }else{
            items=DatabaseHandler.getInstance(context).getEntryItemByTripId(tripid);
        }

        if(items.size()==0) {
           return html="<html><body>No Record Found</body></html>";
        }


        Item it = items.get(0);
        String[] strs = it.get_xxx().split(H.SEPERATOR);
        String map = "";
        int limit = strs.length;

        if (limit < 4) {  // origanl=8
            html = html + "</body></html>";
            return html;
        }

        Timestamp ts = new Timestamp(Long.valueOf(strs[1]));
        Timestamp ts0 = null, ts1 = null;
        if(it.get_source()==0) {


            int n = it.get_xxx().indexOf("maps.googleapis.com");


            if (n > 0) {
                limit = limit - 3;
                map = "<img src='" + strs[limit + 2] + "' width='350' height='200' >";
            }

            for (int i = 0; i < limit; i++) {
                if (i < 13) {// original 9
                    continue;
                }

                if ((i % 2) == 1) {
                    if(strs[i].equalsIgnoreCase("EARNINGS_BOOST_NON_COMMISSIONABLE")){
                        strs[i]="Boost";
                    }
                    html = html + strs[i] + ":  ";
                } else if ((i % 2) == 0) {
                    //html = html + "  " + (((MainActivity) (context)).cs) + strs[i] + "</br>";
                    html = html + "  "  + (((MainActivity) (context)).cs) + strs[i] + "</br>";
                }
            }



            long t0=Long.valueOf(strs[4]),t1=Long.valueOf(strs[3]);;
            if (!strs[3].equalsIgnoreCase("-1") && !strs[4].equalsIgnoreCase("-1") && t0>1513951634&& t1>1513951634) {
                ts0 = new Timestamp(t0);
                ts1 = new Timestamp(t1);
            }

            html = html + "</br>Total Payout:  " + (((MainActivity) (context)).cs) + strs[2] + "</br></br>Request Time:  " + ts + "</br>Duration:  " + sToX(strs[6]) + "</br>Distance:  " + strs[5] +
                    ((((MainActivity) (context)).unit).equalsIgnoreCase("metric") ? "Kms " : "Miles ") +
                    "</br>Trip Begins:  " + (ts0 == null ? "-" : ts0) + "</br>Trip Ends:  " + (ts1 == null ? "-" : ts1) +
                    "</br>Preparing Time:  " + sToX(strs[7]);
            if (n > 0) {
                html = html + "</br>Pick Up:" + strs[strs.length - 3] + "</br>Drop Off:" + strs[strs.length - 2];
            }


        }else if(it.get_source()==1){
           // 5a3d11935f1cfb0f820ecef4龞龤1513951634000龞龤676龞龤1513951634000龞龤0龞龤645

            long t=Long.valueOf(strs[3]);
            if (!strs[3].equalsIgnoreCase("-1") && t>1513951634) {
                ts0 = new Timestamp(t);
            }


            if(strs.length>=20) {
                html = html + "</br></br>Request Time:  " + ts +
                        "</br>Accepted Time:  " + strs[12]+
                        "</br>Trip Begins:  " + (ts0 == null ? "-" : ts0) +
                        "</br>Drop Off:  " + strs[13]+
                        "</br>Preparing Time:  " + sToX(""+((Long.valueOf(strs[3])-Long.valueOf(strs[1]))/1000))+
                        "</br>Duration:  " + sToX(strs[5]) + "</br>Distance:  " + strs[6] +
                        (strs[4].equalsIgnoreCase("0.0") ? "":"</br>Tip:  " + strs[4]);


                map = "<img src='" + strs[14] + "' width='350' height='200' >";


                html = html + "</br></br>Total Payout:  " + (((MainActivity) (context)).cs) + strs[2];
                        html = html + "</br>Fare:  "+(((MainActivity) (context)).cs) + strs[strs.length-2];
                html = html + "</br>Lyft Fee:  " + (((MainActivity) (context)).cs) +strs[strs.length-1]+ "</br>";

                for (int i = 15; i < strs.length-2; i++) {

                    if ((i % 2) == 1) {
                        html = html + strs[i] + ":  ";
                    } else if ((i % 2) == 0) {
                        html = html + "  " + (((MainActivity) (context)).cs) + strs[i] + "</br>";
                    }
                }

            }else {
                html = html + "</br>Total Payout:  " + (((MainActivity) (context)).cs) + strs[2] + "</br></br>Request Time:  " + ts +

                        "</br>Trip Begins:  " + (ts0 == null ? "-" : ts0) +
                        "</br>Preparing Time:  " + sToX(""+((Long.valueOf(strs[3])-Long.valueOf(strs[1]))/1000))+
                        "</br>Duration:  " + sToX(strs[5]) + "</br>Distance:  " + strs[6] +

                        (strs[4].equalsIgnoreCase("0") ? "":"</br>Tip:  " +(((MainActivity) (context)).cs) + strs[4]);

            }
        }

        html=html+"</br></br>Trip ID:  "+strs[0]+"</br></br>"+map+"</body></html>";

        return html;
    }
    public static boolean checknote(ArrayList<Item> items,String note){

        for(Item it:items){
            if(it.get_note().contains(note)){
                return true;
            }
        }

        return false;
    }

    public static boolean dtcheck(long entryid){

        if(entryid == H.TRIPDETAIL){
            return true;
        }else {
            return checks(entryid);
        }


    }

    public static boolean checks(long entryid){

        if(entryid == H.FARE || entryid == H.PREPARETIME || entryid == H.TOLL || entryid==H.FAREADJ ||entryid==H.WK_ONLINEHRS ||entryid==H.UBOOST||
                entryid == H.CANCELLATION || entryid == H.TAXS || entryid == H.UBKP ||entryid==H.TRIPTOLL ||entryid==H.WK_PD_BONUS||
                entryid == H.UBKD || entryid == H.FEES || entryid == H.TIPS || entryid == H.WTINCOME || entryid==H.UB_PAYOUT||entryid==H.TRIPOTHERS||
                entryid == H.OCOST || entryid == H.OINCOME || entryid == H.DISTANCE || entryid == H.ONEUTRAL ||entryid==H.WK_BONUS||
                entryid ==H.TRIPTIP ||entryid ==H.LYFTFARE ||entryid ==H.BONUS || entryid ==H.TRIPTAX ||entryid ==H.TRIPINCOME ||
                entryid ==H.PBONUS || entryid ==H.TRIPCANCEL || entryid ==H.TRIPCOST|| entryid ==H.LYFTFEE ||entryid==H.WK_EARNING){
            return true;
        }else {
            return false;
        }


    }

    public static boolean checkslyft(long entryid){

        if(entryid ==H.TRIPTIP ||entryid ==H.LYFTFARE ||entryid ==H.BONUS || entryid ==H.TRIPTAX ||entryid ==H.TRIPINCOME ||entryid ==H.TRIPOTHERS ||
                entryid ==H.PBONUS || entryid ==H.TRIPCANCEL || entryid ==H.TRIPCOST|| entryid ==H.LYFTFEE || entryid==H.TRIPTOLL){
            return true;
        }else {
            return false;
        }


    }

    public static boolean countcheck(long entryid){

        if(entryid ==H.FARE ||entryid ==H.FAREADJ ||entryid ==H.FEES || entryid ==H.TIPS ||entryid ==H.CANCELLATION ||entryid ==H.WTINCOME ||
                entryid ==H.OCOST ||entryid ==H.WK_EARNING ||entryid ==H.WK_PD_BONUS ||entryid ==H.WK_BONUS ||
                entryid ==H.WK_BONUS || entryid ==H.WK_EARNING || entryid ==H.OINCOME|| entryid ==H.ONEUTRAL || entryid==H.WK_PD_BONUS){
            return true;
        }else {
            return false;
        }


    }
    public static long getWMonday(){
        Calendar c = Calendar.getInstance(Locale.getDefault());
        // c.set(Calendar.MONTH, 8);
        long now=c.getTimeInMillis();

        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        c.set(Calendar.HOUR_OF_DAY, 4);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        long monday=c.getTimeInMillis();

        if(monday>now){
            monday=monday-7*24*60*60*1000;
        }

        return monday;
    }

}
