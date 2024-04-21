package db;

public class Entry {
    private static final double EPS = 1e-6;

    private final long account;
    private final String name;
    private final double value;

    /**
     * Constructs an entry.
     * @param account account
     * @param name name
     * @param value value
     */
    public Entry(long account, String name, double value) {
        this.account = account;
        this.name = name;
        this.value = value;
    }

    /**
     * Account getter method.
     * @return this.account.
     */
    public long getAccount() {
        return account;
    }

    /**
     * Name getter method.
     * @return this.name.
     */
    public String getName() {
        return name;
    }

    /**
     * Value getter method.
     * @return this.value.
     */
    public double getValue() {
        return value;
    }

    /**
     * Converts this entry to string.
     * @return a string representation of the entry.
     */
    public String toString() {
        return "account: " + account + " ; name: " + name + " ; value: " + value;
    }

    /**
     * Checks entry fields for equality. For doubles, uses EPS constant as precision.
     * @param another entry to compare with.
     * @return true if all fields are equals, false otherwise.
     */
    public boolean equals(Entry another) {
        return this.name.equals(another.name) &&
                this.account == another.account &&
                Math.abs(this.value - another.value) < EPS;
    }
}
