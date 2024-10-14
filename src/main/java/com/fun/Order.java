package com.fun;

public class Order implements Cloneable {
    public enum Side { BUY, SELL }
    public enum Type { LIMIT, MARKET }

    public Side side = Side.BUY;
    public Type type = Type.MARKET;
    public long price = -1;
    public long quantity = -1;

    public String toString() {
        return side + "," + type + "," + price + "," + quantity;
    }

    @Override
    public Order clone() throws CloneNotSupportedException {
        return (Order) super.clone();
    }
}
