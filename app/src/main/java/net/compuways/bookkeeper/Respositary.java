package net.compuways.bookkeeper;

import android.arch.lifecycle.LiveData;
import android.content.SharedPreferences;

import java.util.List;

/**
 * Created by zuleli on 2018-07-18.
 */

public class Respositary {
    LiveData<List> initlocal(){return null;}
    LiveData<List>  initInternet(){return null;}
    boolean purIdCheck(){return false;}







    String endcode = "颽醢䞔";
    String[] comacode = new String[]{"龘罾", "襬甕", "嵾皦", "蠹䒉", "堊搋", "豟鑴", "嬔篺", "鼖䪀"};
    private SharedPreferences SP;
    String dir = "DESC";
}
