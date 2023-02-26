package io.KiritoKing;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class AnvilDisenchant extends JavaPlugin {
    public static Logger Logger;
    public static JavaPlugin Instance;

    public static boolean Debug;
    public static int Exp;
    public static boolean DisenchantAtOnce;
    public static boolean DestroyRaw;

    private void initConfig() {
        // 创建默认配置文件
        var config = getConfig();
        config.addDefault("exp-requirement", 1);
        config.addDefault("disenchant-at-one-time", false);
        config.addDefault("debug", false);
        config.addDefault("destroy-raw",false);
        config.options().copyDefaults(true);
        saveConfig();

    }

    public static void loadConfig(AnvilDisenchant plugin) {
        var config = plugin.getConfig();
        Debug = config.getBoolean("debug");
        DisenchantAtOnce = config.getBoolean("disenchant-at-one-time");
        Exp = config.getInt("exp-requirement");
        DestroyRaw = config.getBoolean("destroy-raw");
    }

    @Override
    public void onEnable() {
        Logger = getLogger();
        Instance = this;

        initConfig();
        loadConfig(this);

        getLogger().info("铁砧祛魔 v2.0 By:KiritoKing 加载完成");

        Bukkit.getPluginManager().registerEvents(new DisenchantListener(), this);
        getLogger().info("事件监听器注册成功");

        getCommand("reload").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (s.equalsIgnoreCase("adreload"))
        {
            reloadConfig(); // 从文件重新加载
            loadConfig(this);
            commandSender.sendMessage("已重载AnvilDisenchanter配置");
            commandSender.sendMessage(String.format("每次祛魔消耗的登记=%d", Exp));
            commandSender.sendMessage("是否一次性全部祛魔="+String.valueOf(DisenchantAtOnce));
            commandSender.sendMessage("是否销毁原物品="+String.valueOf(DestroyRaw));
            return true;
        }
        return false;
    }


    @Override
    public void onDisable() {
        super.onDisable();
    }

}
