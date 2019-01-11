package za.co.apricotdb.viewport.align;

public class Bounds {

    private double left = 0;
    private double top = 0;
    private double right = 0;
    private double bottom = 0;

    Bounds(double left, double top, double right, double bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public double getLeft() {
        return left;
    }

    public void setLeft(double left) {
        this.left = left;
    }

    public double getTop() {
        return top;
    }

    public void setTop(double top) {
        this.top = top;
    }

    public double getRight() {
        return right;
    }

    public void setRight(double right) {
        this.right = right;
    }

    public double getBottom() {
        return bottom;
    }

    public void setBottom(double bottom) {
        this.bottom = bottom;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("X0=[").append(left).append("], X1=[").append(right).append("], Y0=[").append(top).append("], Y1=[")
                .append(bottom).append("]");

        return sb.toString();
    }
}
