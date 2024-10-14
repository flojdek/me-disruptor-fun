package org.fun;

import com.lmax.disruptor.EventHandler;

public class SendExecutionsConsumer implements EventHandler<ExecutionEvent> {

    @Override
    public void onEvent(ExecutionEvent event, long sequence, boolean endOfBatch) {
        handleEvent(event);
    }

    private void handleEvent(ExecutionEvent event) {
        System.out.println("SendExecutionsConsumer: event: " + event);
    }
}
