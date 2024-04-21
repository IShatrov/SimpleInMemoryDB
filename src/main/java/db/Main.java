package db;

import db.kdtree.MyKDTree;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        MyKDTree tree = new MyKDTree();

        tree.add(new Entry(500, "vladimir", 123.456));
        tree.add(new Entry(700, "boris", 1.34));
        tree.add(new Entry(300, "boris", 31.45));
        tree.add(new Entry(400, "alexey", 1.34));
        tree.add(new Entry(123, "x", 1.34));
        tree.add(new Entry(501, "anastasia", -8));
        tree.add(new Entry(502, "denis", 10));
        tree.add(new Entry(350, "andrey", 0));
        tree.add(new Entry(351, "anya", 100));
        tree.add(new Entry(400, "aaaaaa", 0.3));
        tree.add(new Entry(349, "vladimir", 0.3));
        tree.add(new Entry(501, "znxs,x", 0.3));

        tree.delete(new Entry(123, "x", 1.34));
        tree.change(new Entry(500, "vladimir", 123.456), new Entry(500, "vladimir", 300));

        List<Entry> foundEntries;

        foundEntries = tree.findByName("vladimir");
        System.out.println(Arrays.toString(foundEntries.toArray()));

        foundEntries = tree.findByValue(0.3);
        System.out.println(Arrays.toString(foundEntries.toArray()));

        foundEntries = tree.findByAccount(501);
        System.out.println(Arrays.toString(foundEntries.toArray()));

        try {
            tree.graphvizLog("graphviz.txt");
        } catch (Exception ex) {
            System.out.println("error");
        }
    }
}