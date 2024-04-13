package db.kdtree;

import db.Entry;

import java.math.BigInteger;

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

        boolean isNodeLower = switch (axis) {
            case NAME -> node.getData().getName().compareTo(currentRoot.getData().getName()) < 0;
            case VALUE -> node.getData().getValue() < currentRoot.getData().getValue();
            case ACCOUNT -> node.getData().getAccount() < currentRoot.getData().getAccount();
        };

        if (isNodeLower) {
            currentRoot.setLeft(this.addRecursive(currentRoot.getLeft(), node, currentDepth.add(BigInteger.valueOf(1))));
        } else {
            currentRoot.setRight(this.addRecursive(currentRoot.getRight(), node, currentDepth.add(BigInteger.valueOf(1))));
        }

        return currentRoot;
    }
}
