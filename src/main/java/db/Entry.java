package db;

public class Entry {
    private long account;
    private String name;
    private double value;

    public Entry(long account, String name, double value) {
        this.account = account;
        this.name = name;
        this.value = value;
    }

    public void setAccount(long account) {
        this.account = account;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
