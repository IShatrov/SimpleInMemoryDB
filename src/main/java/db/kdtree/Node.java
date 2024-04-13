package db.kdtree;

import db.Entry;

class Node {
    private Entry data;
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
}
