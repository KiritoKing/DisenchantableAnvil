package KiritoKing;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

public class ItemFactory {
    static public ItemStack buildEnchantedBook(ItemStack origin) {
        ItemStack eBook = new ItemStack(Material.ENCHANTED_BOOK, 1);
        EnchantmentStorageMeta bookMeta = (EnchantmentStorageMeta) eBook.getItemMeta();

        // TODO: 根据配置文件控制附魔结果：数量和等级
        if(origin.getEnchantments().size() > 0) {
            for(Enchantment ench: origin.getEnchantments().keySet()) { // 获取Map[ench, level]中的key
                bookMeta.addStoredEnchant(ench, origin.getEnchantments().get(ench), true);
            }
            eBook.setItemMeta(bookMeta);
        }

        return eBook;
    }
}
