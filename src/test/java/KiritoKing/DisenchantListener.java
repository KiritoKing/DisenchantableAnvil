package KiritoKing;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class DisenchantListener implements Listener {

    @EventHandler
    public void onPrepareAnvil(PrepareAnvilEvent e) {
        AnvilInventory anvilInv = e.getInventory();
        if(anvilInv == null) return;
        if(anvilInv.getType() != InventoryType.ANVIL) return;
        if(anvilInv.getItem(0) == null || anvilInv.getItem(1) == null) return;
        displayEnchantInfo(anvilInv);

        ItemStack origin = anvilInv.getItem(0);
        int enchantNum = origin.getEnchantments().size();
        if(enchantNum > 0 && anvilInv.getItem(1).getType() == Material.BOOK) {
            //TODO: 根据附魔等级与属性调整祛魔花费
            anvilInv.setRepairCost(5); // 该数字无法在
            displayEnchantInfo(anvilInv);
            e.setResult(ItemFactory.buildEnchantedBook(anvilInv.getItem(0)));
        }
    }

    @EventHandler
    public void onClickInventory(InventoryClickEvent e) {
        Inventory inv = e.getClickedInventory();
        if(inv == null) return;
        if(inv.getType() != InventoryType.ANVIL) return;
        if(inv.getItem(2) == null) return; // 当没有出现结果时不响应
        if(e.getSlot() == 2 && inv.getItem(2).getType() == Material.ENCHANTED_BOOK) {
            e.getWhoClicked().setItemOnCursor(inv.getItem(2));
            inv.setItem(2, new ItemStack(Material.AIR)); // 转移物品到玩家指针

            ItemStack books =  inv.getItem(1);
            // 控制书减少
            if(books.getAmount() > 1) {
                books.setAmount(books.getAmount()-1);
                inv.setItem(1, books);
            }
            else
                inv.setItem(1, new ItemStack(Material.AIR));

            // TODO: 通过配置文件控制原物品是否消失
            inv.setItem(0, new ItemStack(Material.AIR));
            e.setCancelled(true);
        }

    }

    private void displayEnchantInfo(AnvilInventory inv) {
        int cost = inv.getRepairCost();
        int maxCost = inv.getMaximumRepairCost();
        String name = inv.getRenameText();
        AnvilDisenchant.logger.info("cost="+cost);
        AnvilDisenchant.logger.info("maxcost="+maxCost);
        AnvilDisenchant.logger.info("name="+name);

    }

}
