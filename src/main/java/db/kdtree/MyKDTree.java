package db.kdtree;

import db.Entry;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class MyKDTree {
    private Node root;

    public void add(Entry data) {
        Node node = new Node(data);

        if (this.root == null) {
            this.root = node;
        } else {
            this.addRecursive(this.root, node, BigInteger.valueOf(0));
        }
    }

    private Node addRecursive(Node currentRoot, Node node, BigInteger currentDepth) {
        if (currentRoot == null) {
            return node;
        }

        int axisIndex = currentDepth.mod(BigInteger.valueOf(Axis.values().length)).intValue();
        Axis axis = Axis.values()[axisIndex];

        boolean isNodeLower = node.isLower(currentRoot, axis);

        if (isNodeLower) {
            currentRoot.setLeft(this.addRecursive(currentRoot.getLeft(), node, currentDepth.add(BigInteger.valueOf(1))));
        } else {
            currentRoot.setRight(this.addRecursive(currentRoot.getRight(), node, currentDepth.add(BigInteger.valueOf(1))));
        }

        return currentRoot;
    }

    public List<Entry> findByName(String name) {
        List<Entry> ans = new ArrayList<>();

        Entry target = new Entry(0, name, 0); // a dummy entry to pass to recursive searcher

        this.findByNameRecursive(this.root, target, BigInteger.valueOf(0), ans, Axis.NAME);

        return ans;
    }
    public List<Entry> findByAccount(long account) {
        List<Entry> ans = new ArrayList<>();

        Entry target = new Entry(account, "", 0); // a dummy entry to pass to recursive searcher

        this.findByNameRecursive(this.root, target, BigInteger.valueOf(0), ans, Axis.ACCOUNT);

        return ans;
    }

    public List<Entry> findByValue(double value) {
        List<Entry> ans = new ArrayList<>();

        Entry target = new Entry(0, "", value); // a dummy entry to pass to recursive searcher

        this.findByNameRecursive(this.root, target, BigInteger.valueOf(0), ans, Axis.VALUE);

        return ans;
    }

    private void findByNameRecursive(Node currentRoot, Entry target, BigInteger currentDepth, List<Entry> ans, Axis searchAxis) {
        if (currentRoot == null) {
            return;
        }

        int axisIndex = currentDepth.mod(BigInteger.valueOf(Axis.values().length)).intValue();
        Axis axis = Axis.values()[axisIndex];

        boolean isNodeLower = !currentRoot.isLower(target, axis);

        boolean isNodeEqual = currentRoot.equals(target, searchAxis);

        if (isNodeEqual) {
            isNodeLower = false;
        }

        if (axis == searchAxis) {
            if (isNodeLower) {
                this.findByNameRecursive(currentRoot.getLeft(), target, currentDepth.add(BigInteger.valueOf(1)), ans, searchAxis);
            } else if (isNodeEqual) {
                ans.add(currentRoot.getData());
            }

            this.findByNameRecursive(currentRoot.getRight(), target, currentDepth.add(BigInteger.valueOf(1)), ans, searchAxis);
        } else {
            if (isNodeEqual) {
                ans.add(currentRoot.getData());
            }

            this.findByNameRecursive(currentRoot.getLeft(), target, currentDepth.add(BigInteger.valueOf(1)), ans, searchAxis);
            this.findByNameRecursive(currentRoot.getRight(), target, currentDepth.add(BigInteger.valueOf(1)), ans, searchAxis);
        }
    }

    public void delete(Entry target) throws IllegalArgumentException {
        List<Entry> candidates = this.findByAccount(target.getAccount());

        boolean containsTarget = false;
        for (Entry entry : candidates) {
            if (entry.equals(target)) {
                containsTarget = true;
            }
        }

        if (!containsTarget) {
            throw new IllegalArgumentException("Tree does not contain " + target);
        }

        List<Entry> entries = new ArrayList<>();

        constructSubtreeList(this.root, entries);

        this.root = null;

        entries.forEach(entry -> {
            if (!entry.equals(target)) {
                this.add(entry);
            }
        });
    }

    private void constructSubtreeList(Node currentRoot, List<Entry> entries) {
        if (currentRoot == null) {
            return;
        }

        entries.add(currentRoot.getData());

        constructSubtreeList(currentRoot.getRight(), entries);
        constructSubtreeList(currentRoot.getLeft(), entries);
    }

    public void change(Entry oldEntry, Entry newEntry) throws IllegalArgumentException {
        this.delete(oldEntry);
        this.add(newEntry);
    }

    public void graphvizLog(String filename) throws IOException {
        FileWriter writer = new FileWriter(filename);

        writer.write("""
                digraph dump
                {
                \tnode[shape = "record", style = "rounded, filled"];
                """);

        this.root.graphvizWriteChildren(writer);

        writer.write("}");

        writer.close();
    }
}
