package hu.jgj52.huTiersMessengerVelocity;

import redis.clients.jedis.JedisPubSub;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import static hu.jgj52.huTiersMessengerVelocity.HuTiersMessengerVelocity.jedis;

public class Messenger {
    private static final ExecutorService thread = Executors.newSingleThreadExecutor(r -> new Thread(r, "hutiers-manager"));


    public static void listen(String channel, Consumer<String> consumer) {
        thread.submit(() -> {
            if (jedis == null) {
                try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
                listen(channel, consumer);
                return;
            }

            try {
                jedis.subscribe(new JedisPubSub() {
                    @Override
                    public void onMessage(String ch, String message) {
                        consumer.accept(message);
                    }
                }, channel);
            } catch (Exception e) {
                e.printStackTrace();
                try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
            }
        });
    }

    public static void send(String channel, String message) {
        thread.submit(() -> {
            if (jedis == null) {
                try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
                send(channel, message);
                return;
            }

            try {
                jedis.publish(channel, message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}