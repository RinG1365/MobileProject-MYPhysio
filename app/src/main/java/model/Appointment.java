package model;

public class Appointment {
    String bid;
    String bdate;
    String btime;
    String btype;
    String bcat;
    String bprice;
    String bstatus;
    String bstaff;

    public Appointment(){}

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getBdate() {
        return bdate;
    }

    public void setBdate(String bdate) {
        this.bdate = bdate;
    }

    public String getBtime() {
        return btime;
    }

    public void setBtime(String btime) {
        this.btime = btime;
    }

    public String getBtype() {
        return btype;
    }

    public void setBtype(String btype) {
        this.btype = btype;
    }

    public String getBcat() {
        return bcat;
    }

    public void setBcat(String bcat) {
        this.bcat = bcat;
    }

    public String getBprice() {
        return bprice;
    }

    public void setBprice(String bprice) {
        this.bprice = bprice;
    }

    public String getBstatus() {
        return bstatus;
    }

    public void setBstatus(String bstatus) {
        this.bstatus = bstatus;
    }

    public String getBstaff() {
        return bstaff;
    }

    public void setBstaff(String bstaff) {
        this.bstaff = bstaff;
    }
}
