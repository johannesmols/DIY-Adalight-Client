package mols.johannes;

import java.util.List;

public class PostRequest {

    private int monitor;
    private List<List<Integer>> top, bottom, left, right;
    private String[] order;
    private boolean[] inverted;

    public PostRequest() {

    }

    public PostRequest(int monitor, List<List<Integer>> top, List<List<Integer>> bottom, List<List<Integer>> left, List<List<Integer>> right, String[] order, boolean[] inverted) {
        this.monitor = monitor;
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
        this.order = order;
        this.inverted = inverted;
    }

    public int getMonitor() {
        return monitor;
    }

    public void setMonitor(int monitor) {
        this.monitor = monitor;
    }

    public List<List<Integer>> getTop() {
        return top;
    }

    public void setTop(List<List<Integer>> top) {
        this.top = top;
    }

    public List<List<Integer>> getBottom() {
        return bottom;
    }

    public void setBottom(List<List<Integer>> bottom) {
        this.bottom = bottom;
    }

    public List<List<Integer>> getLeft() {
        return left;
    }

    public void setLeft(List<List<Integer>> left) {
        this.left = left;
    }

    public List<List<Integer>> getRight() {
        return right;
    }

    public void setRight(List<List<Integer>> right) {
        this.right = right;
    }

    public String[] getOrder() {
        return order;
    }

    public void setOrder(String[] order) {
        this.order = order;
    }

    public boolean[] getInverted() {
        return inverted;
    }

    public void setInverted(boolean[] inverted) {
        this.inverted = inverted;
    }
}
