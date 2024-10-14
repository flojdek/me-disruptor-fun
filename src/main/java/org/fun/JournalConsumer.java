package org.fun;

import com.lmax.disruptor.EventHandler;

import java.io.FileWriter;
import java.io.IOException;

public class JournalConsumer implements EventHandler<Order> {
    private final FileWriter writer;

    public JournalConsumer(String fileName) throws IOException {
        this.writer = new FileWriter(fileName, true);
    }

    @Override
    public void onEvent(Order order, long sequence, boolean endOfBatch) throws IOException {
        System.out.println("JournalConsumer: order=" + order);
        writer.write(order.side + "," + order.type + "," + order.price + "," + order.quantity + "\n");
        if (endOfBatch) writer.flush();
    }
}
