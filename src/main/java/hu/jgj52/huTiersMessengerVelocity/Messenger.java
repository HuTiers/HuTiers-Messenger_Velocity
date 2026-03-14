package hu.jgj52.huTiersMessengerVelocity;

import redis.clients.jedis.JedisPubSub;

import java.util.function.Consumer;

import static hu.jgj52.huTiersMessengerVelocity.HuTiersMessengerVelocity.jedis;

public class Messenger {
    public static void listen(String channel, Consumer<String> consumer) {
        new Thread(() -> jedis.subscribe(new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                consumer.accept(message);
            }
        }, channel)).start();
    }

    public static void send(String channel, String message) {
        new Thread(() -> jedis.publish(channel, message));
    }
}
