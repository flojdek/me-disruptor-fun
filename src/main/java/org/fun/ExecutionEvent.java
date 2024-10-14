package org.fun;

public class ExecutionEvent {
    public Order matchedOrder;
    public Order incomingOrder;

    public String toString() {
        return "incomingOrder=" + incomingOrder + ",matchedOrder=" + matchedOrder;
    }
}
