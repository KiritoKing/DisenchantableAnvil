package io.KiritoKing;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class DisenchantListener implements Listener {
  @EventHandler
  public void onPrepareAnvil(PrepareAnvilEvent e) {
    // Called when an item is put in a slot for repair by an anvil.
    boolean isDebug = AnvilDisenchant.Debug;

    AnvilInventory anvilInv = e.getInventory();

    // Exceptions
    if (anvilInv == null) return;
    if (anvilInv.getType() != InventoryType.ANVIL) return;

    // 判断操作物品
    ItemStack rawItem = anvilInv.getItem(0);
    ItemStack books = anvilInv.getItem(1);
    if (rawItem == null || books == null) return;
    if (rawItem.getType() == Material.ENCHANTED_BOOK) return; // 源物品是附魔书时不生效

    if (isDebug) {
      AnvilDisenchant.Logger.info("Raw Item Type: " + rawItem.getType().name());
    }

    int enchantNum = rawItem.getEnchantments().size();
    if (enchantNum == 0 || books.getType() != Material.BOOK) return;

    int levels = AnvilDisenchant.Exp * (AnvilDisenchant.DisenchantAtOnce ? enchantNum : 1);
    // 显示祛魔花费
    for (var viewer : e.getViewers()) {
      // getViewers viewer.sendMessage(String.format("此次对%s的祛魔需要花费%d级经验",
      // anvilInv.getItem(0).getType().name().replace("_", " "), levels));
      Bukkit.getScheduler()
          .runTask(
              AnvilDisenchant.Instance,
              () -> {
                anvilInv.setRepairCost(levels);
                var player = Bukkit.getPlayer(viewer.getName());
                player.updateInventory();
              });
    }

    if (isDebug) displayEnchantInfo(anvilInv); // 在控制台中打印附魔信息

    e.setResult(
        ItemFactory.buildEnchantedBook(anvilInv.getItem(0), AnvilDisenchant.DisenchantAtOnce));
  }

  @EventHandler
  public void onClickInventory(InventoryClickEvent e) {
    boolean isDebug = AnvilDisenchant.Debug;

    // Exceptions
    Inventory inv = e.getClickedInventory();
    if (inv == null) return;
    if (inv.getType() != InventoryType.ANVIL) return;
    if (inv.getItem(0) == null) return;
    if (inv.getItem(0).getType() == Material.ENCHANTED_BOOK) return; // 当源物品是附魔书时不响应
    if (inv.getItem(2) == null) return; // 当没有出现结果时不响应

    if (e.getSlot() == 2
        && inv.getItem(1).getType() == Material.BOOK
        && inv.getItem(2).getType() == Material.ENCHANTED_BOOK) {
      Map<Enchantment, Integer> enchantments = inv.getItem(0).getEnchantments();
      int enchantNum = enchantments.size();
      int levels = AnvilDisenchant.Exp * (AnvilDisenchant.DisenchantAtOnce ? enchantNum : 1);
      HumanEntity human = e.getWhoClicked();
      Player player = Bukkit.getPlayer(human.getName());

      if (levels != 0) {
        if (player.getLevel() < levels) {
          player.sendMessage(String.format("你的经验不足以祛魔，需要%d级", levels));
          return;
        }

        if (player == null) return;
        player.setLevel(player.getLevel() - levels);
        player.sendMessage(String.format("祛魔成功，消耗%d级", levels));
      }

      // 转移物品到玩家指针
      player.setItemOnCursor(inv.getItem(2));
      inv.setItem(2, new ItemStack(Material.AIR));
      ItemStack books = inv.getItem(1);
      // 控制书减少
      if (books.getAmount() > 1) {
        books.setAmount(books.getAmount() - 1);
        inv.setItem(1, books);
      } else inv.setItem(1, new ItemStack(Material.AIR));

      if (AnvilDisenchant.DestroyRaw) {
        if (isDebug) AnvilDisenchant.Logger.info("删除源物品");
        inv.setItem(0, new ItemStack(Material.AIR));
      } else {
        if (AnvilDisenchant.DisenchantAtOnce) {
          if (isDebug) AnvilDisenchant.Logger.info("删除源物品全部附魔");

          inv.setItem(0, new ItemStack(inv.getItem(0).getType()));
        } else {
          if (isDebug) AnvilDisenchant.Logger.info("删除源物品首个附魔");
          var raw = inv.getItem(0);

          var newItem = new ItemStack(inv.getItem(0).getType());
          var meta = raw.getItemMeta().clone();
          var firstEnchant = meta.getEnchants().keySet().iterator().next();
          meta.removeEnchant(firstEnchant);
          newItem.setItemMeta(meta);

          inv.setItem(0, newItem);
        }
      }

      e.setCancelled(true);
    }
  }

  private void displayEnchantInfo(AnvilInventory inv) {
    int cost = inv.getRepairCost();
    int maxCost = inv.getMaximumRepairCost();
    String name = inv.getItem(0).getType().name().replace("_", " ");
    AnvilDisenchant.Logger.info("cost=" + cost);
    AnvilDisenchant.Logger.info("name=" + name);
  }
}
