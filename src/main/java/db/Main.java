package db;

import db.kdtree.MyKDTree;

public class Main {
    public static void main(String[] args) {
        MyKDTree tree = new MyKDTree();

        tree.add(new Entry(123, "aaa", 1.23));
        tree.add(new Entry(124, "aba", 1.34));
    }
}