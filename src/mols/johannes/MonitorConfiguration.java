package mols.johannes;

public class MonitorConfiguration {

    private int top, bottom, left, right;
    private float offset_top_bottom_pct, offset_left_right_pct;

    public MonitorConfiguration() {

    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getBottom() {
        return bottom;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public float getOffset_top_bottom_pct() {
        return offset_top_bottom_pct;
    }

    public void setOffset_top_bottom_pct(float offset_top_bottom_pct) {
        this.offset_top_bottom_pct = offset_top_bottom_pct;
    }

    public float getOffset_left_right_pct() {
        return offset_left_right_pct;
    }

    public void setOffset_left_right_pct(float offset_left_right_pct) {
        this.offset_left_right_pct = offset_left_right_pct;
    }
}
