package io.KiritoKing;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.Map;

public class ItemFactory {
    static public ItemStack buildEnchantedBook(ItemStack origin, boolean disenchantAtOnce) {
        ItemStack eBook = new ItemStack(Material.ENCHANTED_BOOK, 1);
        EnchantmentStorageMeta bookMeta = (EnchantmentStorageMeta) eBook.getItemMeta();

        if(origin.getEnchantments().size() > 0) {
            if (disenchantAtOnce) {
                for(Enchantment ench: origin.getEnchantments().keySet()) { // 获取Map[ench, level]中的key
                    bookMeta.addStoredEnchant(ench, origin.getEnchantments().get(ench), true);
                }
            }
            else {
                var enchants = origin.getEnchantments().keySet();
                var firstEnch = enchants.iterator().next();
                bookMeta.addStoredEnchant(firstEnch, origin.getEnchantments().get(firstEnch), true);
            }
            eBook.setItemMeta(bookMeta);
        }
        return eBook;
    }
}
