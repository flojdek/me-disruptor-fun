package com.fun;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;

import java.util.*;

public class BusinessLogicConsumer implements EventHandler<Order> {
    private final ArrayList<Order> buyOrders = new ArrayList<>();
    private final ArrayList<Order> sellOrders = new ArrayList<>();
    private final RingBuffer<ExecutionEvent> executionEventRingBuffer;

    public BusinessLogicConsumer(RingBuffer<ExecutionEvent> executionEventRingBuffer) {
        this.executionEventRingBuffer = executionEventRingBuffer;
    }

    @Override
    public void onEvent(Order order, long sequence, boolean endOfBatch) throws Exception {
        System.out.println("BusinessLogicConsumer: order=" + order);
        if (order.side == Order.Side.BUY) {
            matchOrder(order, sellOrders, buyOrders);
        } else {
            matchOrder(order, buyOrders, sellOrders);
        }
        System.out.println("== book ==");
        for (Order o : sellOrders) {
            System.out.println("sell: " + o);
        }
        for (Order o : buyOrders) {
            System.out.println("buy: " + o);
        }
        System.out.println("==========");
    }

    private void addOrder(Order newOrder, ArrayList<Order> orders) {
        // Can be optimised with binary search but a bit more faffing around.
        if (orders.isEmpty()) orders.add(newOrder);
        else {
            int idx = 0;
            while (idx < orders.size() && (newOrder.side == Order.Side.BUY
                    ? orders.get(idx).price > newOrder.price
                    : orders.get(idx).price < newOrder.price)) idx++;
            orders.add(idx, newOrder);
        }
    }

    private void matchOrder(Order incomingOrder, ArrayList<Order> oppositeOrders, ArrayList<Order> sameSideOrders)
        throws CloneNotSupportedException {
        long remainingQuantity = incomingOrder.quantity;

        Iterator<Order> iterator = oppositeOrders.iterator();
        while (iterator.hasNext() && remainingQuantity > 0) {
            Order oppositeOrder = iterator.next();

            if (incomingOrder.side == Order.Side.BUY
                    ? oppositeOrder.price <= incomingOrder.price
                    : oppositeOrder.price >= incomingOrder.price) {
                long fillQuantity = Math.min(remainingQuantity, oppositeOrder.quantity);

                long executionSequence = executionEventRingBuffer.next();
                ExecutionEvent event = executionEventRingBuffer.get(executionSequence);
                event.incomingOrder = incomingOrder.clone();
                event.matchedOrder = oppositeOrder.clone();
                executionEventRingBuffer.publish(executionSequence);

                remainingQuantity -= fillQuantity;
                oppositeOrder.quantity -= fillQuantity;
                if (oppositeOrder.quantity <= 0) {
                    iterator.remove();
                }
            } else {
                break;
            }
        }

        if (remainingQuantity > 0) {
            incomingOrder.quantity = remainingQuantity; // Update incoming order quantity
            addOrder(incomingOrder, sameSideOrders);
        }
    }
}

