package KiritoKing;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class AnvilDisenchant extends JavaPlugin {
    public static Logger logger;
    public void AnvilDisenchant() {
        logger = this.getLogger();
    }

    @Override
    public void onEnable() {
        // 初始化注册事件监听器
        logger.info("Disenchantable-Anvil plugin initialized!");
        Bukkit.getPluginManager().registerEvents(new DisenchantListener(), this);
        logger.info("Listener registered!");
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
