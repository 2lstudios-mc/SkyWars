package dev._2lstudios.skywars.chest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import dev._2lstudios.skywars.SkyWars;
import dev._2lstudios.skywars.game.GameItem;
import dev._2lstudios.skywars.utils.BukkitUtil;

public class ChestManager {
  private final Map<ChestType, ItemStack> openItems = new EnumMap<>(ChestType.class);
  
  private int basicChanceIndex = 0;
  
  private Collection<GameItem> basicGameItems = new HashSet<>();
  
  private int normalChanceIndex = 0;
  
  private Collection<GameItem> normalGameItems = new HashSet<>();
  
  private int insaneChanceIndex = 0;
  
  private Collection<GameItem> insaneGameItems = new HashSet<>();
  
  public ChestManager() {
    reloadItems();
  }
  
  private void reloadItems() {
    ItemStack basicOpenItem = BukkitUtil.createItem(Material.CHEST, ChatColor.GREEN + "Basico");
    ItemStack normalOpenItem = BukkitUtil.createItem(Material.CHEST, ChatColor.YELLOW + "Normal");
    ItemStack insaneOpenItem = BukkitUtil.createItem(Material.CHEST, ChatColor.RED + "Insano");
    int chanceIndex = 0;
    Collection<GameItem> gameItems = new HashSet<>();
    this.openItems.put(ChestType.BASIC, basicOpenItem);
    this.openItems.put(ChestType.NORMAL, normalOpenItem);
    this.openItems.put(ChestType.INSANE, insaneOpenItem);
    for (ChestType chestType : ChestType.values()) {
      for (String itemDataString : SkyWars.getConfigurationUtil()
        .getConfiguration("%datafolder%/" + chestType.toString().toLowerCase() + ".yml")
        .getStringList("items")) {
        String[] itemDataArray = itemDataString.split(", ");
        int chance = 0;
        Material material = Material.STONE;
        int data = 0;
        int amount = 1;
        List<Enchantment> enchantments = new ArrayList<>();
        List<Integer> levels = new ArrayList<>();
        ItemStack itemStack = new ItemStack(material, amount);
        int index;
        for (index = 0; index < itemDataArray.length; index++) {
          if (index == 0) {
            chance = Integer.parseInt(itemDataArray[index]);
          } else if (index == 1) {
            if (itemDataArray[index].contains(":")) {
              String[] itemMaterialArray = itemDataArray[index].split(":");
              material = Material.getMaterial(itemMaterialArray[0].toUpperCase());
              data = Integer.parseInt(itemMaterialArray[1]);
            } else {
              material = Material.getMaterial(itemDataArray[index].toUpperCase());
              if (material == null)
                SkyWars.getInstance().getLogger()
                  .info(itemDataArray[index] + " from " + chestType + ".yml cannot be loaded!"); 
            } 
          } else if (index == 2) {
            amount = Integer.parseInt(itemDataArray[index]);
          } else if (itemDataArray[index].contains(":")) {
            String[] itemEnchantmentArray = itemDataArray[index].split(":");
            enchantments.add(Enchantment.getByName(itemEnchantmentArray[0].toUpperCase()));
            levels.add(Integer.valueOf(itemEnchantmentArray[1]));
          } 
        } 
        if (material != null)
          itemStack.setType(material); 
        itemStack.setAmount(amount);
        itemStack.setDurability((short)data);
        for (index = 0; index < enchantments.size(); index++) {
          if (enchantments.get(index) != null && levels.get(index) != null)
            itemStack.addEnchantment(enchantments.get(index), levels.get(index));
        } 
        gameItems.add(new GameItem(chanceIndex, chanceIndex + chance, itemStack));
        chanceIndex += chance;
      }
      switch (chestType) {
        case BASIC:
          this.basicChanceIndex = chanceIndex;
          this.basicGameItems = new HashSet<>(gameItems);
          break;
        case NORMAL:
          this.normalChanceIndex = chanceIndex;
          this.normalGameItems = new HashSet<>(gameItems);
          break;
        case INSANE:
          this.insaneChanceIndex = chanceIndex;
          this.insaneGameItems = new HashSet<>(gameItems);
          break;
      }
      chanceIndex = 0;
      gameItems.clear();
    } 
  }
  
  public int getNormalChanceIndex() {
    return this.normalChanceIndex;
  }
  
  public Collection<GameItem> getNormalGameItems() {
    return this.normalGameItems;
  }
  
  public int getBasicChanceIndex() {
    return this.basicChanceIndex;
  }
  
  public Collection<GameItem> getBasicGameItems() {
    return this.basicGameItems;
  }
  
  public int getInsaneChanceIndex() {
    return this.insaneChanceIndex;
  }
  
  public Collection<GameItem> getInsaneGameItems() {
    return this.insaneGameItems;
  }
  
  public ItemStack getOpenItem(ChestType chestType) {
    return this.openItems.getOrDefault(chestType, null);
  }
}
