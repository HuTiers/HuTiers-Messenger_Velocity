package hu.jgj52.huTiersMessengerVelocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import org.slf4j.Logger;
import redis.clients.jedis.JedisPooled;

import java.util.Map;

@Plugin(id = "hutiers-messenger_velocity", name = "HuTiers-Messenger Velocity", version = "1.2", authors = {"JGJ52"})
public class HuTiersMessengerVelocity {

    public static JedisPooled jedis;
    private final Logger logger;

    @Inject
    public HuTiersMessengerVelocity(Logger logger) {
        this.logger = logger;
    }


    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        try {
            Config config = new Config("hutiersmessenger");
            config.load();
            Map<String, Object> cfg = config.getConfig();
            String host = cfg.get("host").toString();
            int port = Integer.parseInt(cfg.get("port").toString());
            String user = cfg.get("user").toString();
            String password = cfg.get("password").toString();
            jedis = new JedisPooled(host, port, user, password);
            logger.info("HuTiers Messenger successfully connected!");
        } catch (Exception e) {
            logger.error("HuTiers Messenger failed to connect!");
            e.printStackTrace();
        }
    }
}
