package org.fun;

import com.lmax.disruptor.RingBuffer;

public class TradingBot implements Runnable {
    private final RingBuffer<Order> ringBuffer;

    public TradingBot(RingBuffer<Order> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void produceMaker1() {
        long sequence = ringBuffer.next();  // Grab the next sequence
        try {
            Order orderEvent = ringBuffer.get(sequence); // Get the entry in the Disruptor
            orderEvent.side = Order.Side.BUY;   // Set order details
            orderEvent.type = Order.Type.LIMIT;
            orderEvent.price = 60000;
            orderEvent.quantity = 2;
            System.out.println(orderEvent);
        } finally {
            ringBuffer.publish(sequence); // Publish the event
        }
    }

    public void produceTaker2() {
        long sequence = ringBuffer.next();  // Grab the next sequence
        try {
            Order orderEvent = ringBuffer.get(sequence); // Get the entry in the Disruptor
            orderEvent.side = Order.Side.BUY;   // Set order details
            orderEvent.type = Order.Type.LIMIT;
            orderEvent.price = 61000;
            orderEvent.quantity = 2;
            System.out.println(orderEvent);
        } finally {
            ringBuffer.publish(sequence); // Publish the event
        }
    }

    public void produceMaker3() {
        long sequence = ringBuffer.next();  // Grab the next sequence
        try {
            Order orderEvent = ringBuffer.get(sequence); // Get the entry in the Disruptor
            orderEvent.side = Order.Side.SELL;   // Set order details
            orderEvent.type = Order.Type.LIMIT;
            orderEvent.price = 63000;
            orderEvent.quantity = 2;
            System.out.println(orderEvent);
        } finally {
            ringBuffer.publish(sequence); // Publish the event
        }
    }

    public void produceMaker4() {
        long sequence = ringBuffer.next();  // Grab the next sequence
        try {
            Order orderEvent = ringBuffer.get(sequence); // Get the entry in the Disruptor
            orderEvent.side = Order.Side.SELL;   // Set order details
            orderEvent.type = Order.Type.LIMIT;
            orderEvent.price = 65000;
            orderEvent.quantity = 2;
            System.out.println(orderEvent);
        } finally {
            ringBuffer.publish(sequence); // Publish the event
        }
    }

    public void produceMaker5() {
        long sequence = ringBuffer.next();  // Grab the next sequence
        try {
            Order orderEvent = ringBuffer.get(sequence); // Get the entry in the Disruptor
            orderEvent.side = Order.Side.BUY;   // Set order details
            orderEvent.type = Order.Type.LIMIT;
            orderEvent.price = 66000;
            orderEvent.quantity = 3;
            System.out.println(orderEvent);
        } finally {
            ringBuffer.publish(sequence); // Publish the event
        }
    }

    @Override
    public void run() {
        produceMaker1();
        produceTaker2();
        produceMaker3();
        produceMaker4();
        produceMaker5();
    }
}