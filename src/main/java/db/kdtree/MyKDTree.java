package db.kdtree;

import db.Entry;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class MyKDTree {
    private Node root;

    /**
     * Adds entry to tree. If this.root == null, new node is set as root, otherwise this.addRecursive is called.
     * @param data data to add.
     */
    public void add(Entry data) {
        Node node = new Node(data);

        if (this.root == null) {
            this.root = node;
        } else {
            this.addRecursive(this.root, node, BigInteger.valueOf(0));
        }
    }

    /**
     * A recursive method to add nodes to the tree.
     * @param currentRoot root of current subtree.
     * @param node node to be added.
     * @param currentDepth current depth.
     * @see <a href="https://en.wikipedia.org/wiki/K-d_tree#Operations_on_k-d_trees"> wikipedia </a>
     * @return node if currentRoot == null, currentRoot otherwise.
     */
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

    /**
     * Finds all nodes whose name equals name.
     * @param name name to find.
     * @return a list containing all entries whose name equals name.
     */
    public List<Entry> findByName(String name) {
        List<Entry> ans = new ArrayList<>();

        Entry target = new Entry(0, name, 0); // a dummy entry to pass to recursive searcher

        this.findRecursive(this.root, target, BigInteger.valueOf(0), ans, Axis.NAME);

        return ans;
    }

    /**
     * Finds all nodes whose account equals account.
     * @param account account to find.
     * @return a list containing all entries whose account equals account.
     */
    public List<Entry> findByAccount(long account) {
        List<Entry> ans = new ArrayList<>();

        Entry target = new Entry(account, "", 0); // a dummy entry to pass to recursive searcher

        this.findRecursive(this.root, target, BigInteger.valueOf(0), ans, Axis.ACCOUNT);

        return ans;
    }

    /**
     * Finds all nodes whose value equals value. Uses EPS constant as precision.
     * @param value value to find.
     * @return a list containing all entries whose value equals value.
     */
    public List<Entry> findByValue(double value) {
        List<Entry> ans = new ArrayList<>();

        Entry target = new Entry(0, "", value); // a dummy entry to pass to recursive searcher

        this.findRecursive(this.root, target, BigInteger.valueOf(0), ans, Axis.VALUE);

        return ans;
    }

    /**
     * A recursive helper method to find entries.
     * @param currentRoot current root.
     * @param target entry to find.
     * @param currentDepth current depths.
     * @param ans list of found entries.
     * @param searchAxis search axis.
     */
    private void findRecursive(Node currentRoot, Entry target, BigInteger currentDepth, List<Entry> ans, Axis searchAxis) {
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
                this.findRecursive(currentRoot.getLeft(), target, currentDepth.add(BigInteger.valueOf(1)), ans, searchAxis);
            } else if (isNodeEqual) {
                ans.add(currentRoot.getData());
            }

            this.findRecursive(currentRoot.getRight(), target, currentDepth.add(BigInteger.valueOf(1)), ans, searchAxis);
        } else {
            if (isNodeEqual) {
                ans.add(currentRoot.getData());
            }

            this.findRecursive(currentRoot.getLeft(), target, currentDepth.add(BigInteger.valueOf(1)), ans, searchAxis);
            this.findRecursive(currentRoot.getRight(), target, currentDepth.add(BigInteger.valueOf(1)), ans, searchAxis);
        }
    }

    /**
     * Deleted an entry from the tree.
     * @param target entry to delete.
     * @throws IllegalArgumentException if the tree does not contain such entry.
     */
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

    /**
     * Returns a list containing all entries in current subtree. Used in delete() method.
     * @param currentRoot root of current subtree.
     * @param entries list of entries.
     */
    private void constructSubtreeList(Node currentRoot, List<Entry> entries) {
        if (currentRoot == null) {
            return;
        }

        entries.add(currentRoot.getData());

        constructSubtreeList(currentRoot.getRight(), entries);
        constructSubtreeList(currentRoot.getLeft(), entries);
    }

    /**
     * Changes an entry by calling delete() and add() methods.
     * @param oldEntry entry to change.
     * @param newEntry new value of the entry.
     * @throws IllegalArgumentException if oldEntry is not present in the tree.
     */
    public void change(Entry oldEntry, Entry newEntry) throws IllegalArgumentException {
        this.delete(oldEntry);
        this.add(newEntry);
    }

    /**
     * Generates a graphviz log of the tree.
     * @param filename file to write to.
     * @see <a href="https://graphviz.org/doc/info/colors.html">graphviz documentation</a>
     * @see <a href="https://dreampuf.github.io/GraphvizOnline"> graphviz parser </a>
     * @throws IOException when writing fails.
     */
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
