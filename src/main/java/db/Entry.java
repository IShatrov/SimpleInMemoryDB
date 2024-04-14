package db;

public class Entry {
    private final long account;
    private final String name;
    private final double value;

    public Entry(long account, String name, double value) {
        this.account = account;
        this.name = name;
        this.value = value;
    }

    public long getAccount() {
        return account;
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }

    public String toString() {
        return "account: " + account + " ; name: " + name + " ; value: " + value;
    }
}
