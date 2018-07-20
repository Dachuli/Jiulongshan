package net.compuways.bookkeeper;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;

/**
 * Created by zuleli on 2018-07-15.
 */

public class BKViewModel extends ViewModel {
    static boolean PAID=false;
    static int limit=50;
    private static BKViewModel instance;

    ArrayList<Item> datarepareitems;
    long entryid = -918;
    ArrayList<Item> names = new ArrayList<>(),ulnames = new ArrayList<>();
    Item ucode;


    private LiveData mainInitLiveData,mainInternetLiveDate,fraLiveData;

    private Respositary respositary=null;

    private BKViewModel (Respositary respositary){
        this.respositary=respositary;
    }

    public static BKViewModel getInstance(Respositary respositary){
        if(instance==null){
            instance=new BKViewModel(respositary);
        }
        return instance;
    }

    void initlocal(){
        respositary.initlocal();
        //mainInitLiveData.setValue();????????????????/
    }
    void initInternet(){
        respositary.initInternet();
    }
    void purIdCheck(){

    }

    public LiveData getFraLiveData(){

        return fraLiveData;
    }
    public LiveData getMainInitLiveData(){

        return mainInitLiveData;
    }
    public LiveData getMainInternetLiveData(){

        return mainInternetLiveDate;
    }

}
