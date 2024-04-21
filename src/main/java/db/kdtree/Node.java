package db.kdtree;

import db.Entry;

import java.io.FileWriter;
import java.io.IOException;

class Node {
    private static final double EPS = 1e-6;
    private static final String color = "cornsilk2";

    private final Entry data;
    private Node left, right;

    Node(Entry data) {
        this.data = data;
    }

    void setLeft(Node left) {
        this.left = left;
    }

    void setRight(Node right) {
        this.right = right;
    }

    Entry getData() {
        return data;
    }

    Node getLeft() {
        return left;
    }

    Node getRight() {
        return right;
    }

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

    private void graphvizLog(FileWriter writer) throws IOException {
        writer.write("\t\"" + this + "\"[label = \"{" + this.getData().toString() + "}\", fillcolor = " + color + "];\n\n");
    }

    boolean isLower(Entry other, Axis axis) {
        return switch (axis) {
            case NAME -> this.getData().getName().compareTo(other.getName()) < 0;
            case VALUE -> this.getData().getValue() < other.getValue();
            case ACCOUNT -> this.getData().getAccount() < other.getAccount();
        };
    }

    boolean isLower(Node other, Axis axis) {
        return this.isLower(other.getData(), axis);
    }

    boolean equals(Entry other, Axis axis) {
        return switch (axis) {
            case NAME -> this.getData().getName().equals(other.getName());
            case VALUE -> Math.abs(this.getData().getValue() - other.getValue()) < EPS;
            case ACCOUNT -> this.getData().getAccount() == other.getAccount();
        };
    }
}
