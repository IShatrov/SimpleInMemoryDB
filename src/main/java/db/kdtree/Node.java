package db.kdtree;

import db.Entry;

import java.io.FileWriter;
import java.io.IOException;

class Node {
    private static final double EPS = 1e-6;
    private static final String color = "cornsilk2";

    private final Entry data;
    private Node left, right;

    /**
     * COnstructs node from entry.
     * @param data entry to construct from.
     */
    Node(Entry data) {
        this.data = data;
    }

    /**
     * Setter method for left.
     * @param left node to be set.
     */
    void setLeft(Node left) {
        this.left = left;
    }

    /**
     * Setter method for right.
     * @param right node to be set.
     */
    void setRight(Node right) {
        this.right = right;
    }

    /**
     * Getter method for data.
     * @return this.data.
     */
    Entry getData() {
        return data;
    }

    /**
     * Getter method for left.
     * @return this.left.
     */
    Node getLeft() {
        return left;
    }

    /**
     * Getter method for right.
     * @return this.right.
     */
    Node getRight() {
        return right;
    }

    /**
     * Writes children of this node in graphviz log.
     * @param writer file to write to.
     * @throws IOException when writing fails.
     */
    void graphvizWriteChildren(FileWriter writer) throws IOException {
        if (left != null) {
            writer.write("\t\"" + this + "\"->\"" + left + "\";\n");
            left.graphvizWriteChildren(writer);
        }

        if (right != null) {
            writer.write("\t\"" + this + "\"->\"" + right + "\";\n");
            right.graphvizWriteChildren(writer);
        }

        this.graphvizLog(writer);
    }

    /**
     * Generates a graphviz log for this node.
     * @param writer file to write to
     * @throws IOException when writing fails.
     */
    private void graphvizLog(FileWriter writer) throws IOException {
        writer.write("\t\"" + this + "\"[label = \"{" + this.getData().toString() + "}\", fillcolor = " + color + "];\n\n");
    }

    /**
     * Compares a field of this node with the same field of an entry.
     * @param other entry to compare with.
     * @param axis field to compare.
     * @return true if specified field of this node is lower of the same field of the entry, false otherwise.
     */
    boolean isLower(Entry other, Axis axis) {
        return switch (axis) {
            case NAME -> this.getData().getName().compareTo(other.getName()) < 0;
            case VALUE -> this.getData().getValue() < other.getValue();
            case ACCOUNT -> this.getData().getAccount() < other.getAccount();
        };
    }

    /**
     * Compares a field of this node with the same field of another entry by calling this.isLower(other.getData(), axis).
     * @param other node to compare with.
     * @param axis field to compare.
     * @return true if specified field of this node is lower of the same field of another node, false otherwise.
     */
    boolean isLower(Node other, Axis axis) {
        return this.isLower(other.getData(), axis);
    }


    /**
     * Checks a field of this node with the same field of an entry for equality.
     * @param other entry to compare with.
     * @param axis field to compare.
     * @return true if specified field are equal, false otherwise. For doubles, uses EPS constant as precision.
     */
    boolean equals(Entry other, Axis axis) {
        return switch (axis) {
            case NAME -> this.getData().getName().equals(other.getName());
            case VALUE -> Math.abs(this.getData().getValue() - other.getValue()) < EPS;
            case ACCOUNT -> this.getData().getAccount() == other.getAccount();
        };
    }
}
