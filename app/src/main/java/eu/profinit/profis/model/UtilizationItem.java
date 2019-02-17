package eu.profinit.profis.model;

import java.util.Date;

public class UtilizationItem {

    private Date date;
    private String contract;
    private int hours;
    private String note;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "UtilizationItem{" +
                "date=" + date +
                ", contract='" + contract + '\'' +
                ", hours=" + hours +
                ", note='" + note + '\'' +
                '}';
    }


}
