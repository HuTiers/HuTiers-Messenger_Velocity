package hu.jgj52.huTiersMessengerVelocity;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import static hu.jgj52.huTiersMessengerVelocity.HuTiersMessengerVelocity.*;

public class Messenger {
    private static final ExecutorService thread = Executors.newSingleThreadExecutor(r -> new Thread(r, "hutiers-messenger"));

    public static void listen(String channel, Consumer<String> consumer) {
        new Thread(() -> {
            try (Jedis sub = new Jedis(host, port)) {
                sub.auth(user, password);
                sub.subscribe(new JedisPubSub() {
                    @Override
                    public void onMessage(String ch, String message) {
                        consumer.accept(message);
                    }
                }, channel);
            }
        }, "redis-listener-" + channel).start();
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