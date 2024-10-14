package com.fun;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.io.IOException;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws IOException {
        // Main ME consumers.
        ReplicationConsumer replicationConsumer = new ReplicationConsumer("localhost", 9000);
        JournalConsumer journalConsumer = new JournalConsumer("journal.txt");

        // Main ME disruptor.
        Disruptor<Order> meDisruptor = new Disruptor<>(Order::new, 1024,
            Executors.defaultThreadFactory(), ProducerType.SINGLE, new BlockingWaitStrategy());

        // Executions disruptor.
        Disruptor<ExecutionEvent> executionDisruptor = new Disruptor<>(ExecutionEvent::new, 1024,
            Executors.defaultThreadFactory(), ProducerType.SINGLE,  new BlockingWaitStrategy());

        // Maine ME disruptor pipeline.
        BusinessLogicConsumer businessLogicConsumer = new BusinessLogicConsumer(executionDisruptor.getRingBuffer());
        meDisruptor.handleEventsWith(replicationConsumer, journalConsumer).then(businessLogicConsumer);
        meDisruptor.start();

        // Second stage consumers.
        SendExecutionsConsumer sendExecutionsConsumer = new SendExecutionsConsumer();

        // Executions disruptor pipeline.
        executionDisruptor.handleEventsWith(sendExecutionsConsumer);
        executionDisruptor.start();

        // Trading bot.
        TradingBot tradingBot = new TradingBot(meDisruptor.getRingBuffer());
        Thread tradingBotThread = new Thread(tradingBot);
        tradingBotThread.start();
    }
}