package org.fun;

import com.lmax.disruptor.EventHandler;
import org.agrona.MutableDirectBuffer;
import org.agrona.concurrent.UnsafeBuffer;

public class ReplicationConsumer implements EventHandler<Order> {
    private final com.fun.OrderEncoder orderEncoder = new com.fun.OrderEncoder();

    public ReplicationConsumer(String host, int port) {
        // Initialize any network related stuff.
    }

    @Override
    public void onEvent(Order order, long sequence, boolean endOfBatch) throws Exception {
        System.out.println("ReplicationConsumer: order=" + order);

        byte[] buffer = new byte[1024];
        MutableDirectBuffer directBuffer = new UnsafeBuffer(buffer);

        // Of course this needs message header, template id etc.
        orderEncoder.wrap(directBuffer, 0)
                .side(order.side == Order.Side.BUY ? 0 : 1)
                .type(order.type == Order.Type.LIMIT ? 0 : 1)
                .price(order.price)
                .quantity(order.quantity);

        // Blast to the network! This is just a basic demonstration.
        // Ideally via some reliable UDP because this needs to be fast...
    }
}
