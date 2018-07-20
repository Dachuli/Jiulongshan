package net.compuways.bookkeeper;

/**
 * Created by zule on 1/23/17.
 */

public class Item implements Comparable<Item> {
    /*
    * When _name=H.SETTINGKEY(-3),_date represent different system setting, 0=starting km, 1= depreciation rate,2=share of insurance
    *                3=currency symble, 4=measurence system. 0-2 stored in amount, 3-4 stored in note.
    *                H.Receivable=1002, H.Payable=1001,H.Noncountable=1000
    *
    *Name reprensent a type of data, settingkey or actual data
    *
    * Name and rcdate combined to decide an entry(item) in the main screen.
    * When name!=Settingkey, it represent the entry(item) in the main screen, store actual data
    *
    *
    * H.Autosave=System.currentmillimes time, H.Manual =user pick up a date
    *
    * In mileage, name represent id of H.Mileage, _date represent the date of data, _rcdata represent the ending reading,
    *           the amount represent the miles driven
    *
    *
    * */

    private long _id;
    private long _date=-1;
    private long _rcdate=-1;//record creationg date, actually it is used for multiple purpose
    private String _name="";
    private double _amount=-1;
    private String _note="";
    private short _source=0;//0=Uber, 1=Lyft,2=other,3=overhead
    private String _xxx="";

    public Item() {

    }

    @Override
    public int compareTo(Item item) {

        if(this._amount>item.get_amount()){
            return -1;
        }else if(this._amount>item.get_amount()){
            return 1;
        }else
            return 0;
    }

    public Item(long id, long date, long rcdate, String name, double amount, String note) {
        this._id=id;
        this._date=date;
        this._rcdate=rcdate;
        this._name=name;
        this._amount=amount;
        this._note=note;
    }


    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public long get_date() {
        return _date;
    }

    public void set_date(long _date) {
        this._date = _date;
    }

    public long get_rcdate() {
        return _rcdate;
    }

    public void set_rcdate(long _rcdate) {
        this._rcdate = _rcdate;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public double get_amount() {
        return _amount;
    }

    public void set_amount(double _amount) {
        this._amount = _amount;
    }

    public String get_note() {
        return _note;
    }

    public void set_note(String _note) {
        this._note = _note;
    }


    public short get_source() {
        return _source;
    }

    public String get_xxx() {
        return _xxx;
    }

    public void set_source(short _source) {
        this._source = _source;
    }

    public void set_xxx(String _xxx) {
        this._xxx = _xxx;
    }

}
