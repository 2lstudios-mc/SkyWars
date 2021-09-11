package dev._2lstudios.skywars.menus;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import dev._2lstudios.inventoryapi.InventoryAPI;
import dev._2lstudios.inventoryapi.events.InventoryAPIClickEvent;
import dev._2lstudios.inventoryapi.inventory.InventoryUtil;
import dev._2lstudios.skywars.SkyWars;
import dev._2lstudios.skywars.SkyWarsManager;
import dev._2lstudios.skywars.game.GameMenu;
import dev._2lstudios.skywars.game.player.GamePlayer;
import dev._2lstudios.skywars.game.player.GamePlayerManager;

public class ShopMenu implements GameMenu, Listener {
  private static final String ID = "sw_shopmenu";
  private static final String TITLE = "SkyWars - Tienda";

  private final MenuManager menuManager;
  private final GamePlayerManager playerManager;
  private final InventoryUtil inventoryUtil;
  private final ItemStack openItem = new ItemStack(Material.CHEST, 1);

  public ShopMenu(final SkyWarsManager skyWarsManager) {
    this.menuManager = skyWarsManager.getMenuManager();
    this.playerManager = skyWarsManager.getPlayerManager();
    this.inventoryUtil = InventoryAPI.getInstance().getInventoryUtil();
    final ItemMeta openItemMeta = this.openItem.getItemMeta();
    openItemMeta.setDisplayName(ChatColor.YELLOW + "Menu de Tienda");
    openItem.setItemMeta(openItemMeta);

    final Plugin plugin = SkyWars.getInstance();
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
  }

  public ItemStack getOpenItem() {
    return this.openItem;
  }

  public Inventory getInventory(final GamePlayer gamePlayer, final int page) {
    final Inventory inventory = inventoryUtil.createInventory(TITLE, gamePlayer.getPlayer(), page, ID).getInventory();
    final ItemStack particlesItem = new ItemStack(Material.BLAZE_POWDER, 1);
    final ItemMeta particlesItemMeta = particlesItem.getItemMeta();
    particlesItemMeta.setDisplayName(ChatColor.YELLOW + "Particulas");
    particlesItem.setItemMeta(particlesItemMeta);
    inventory.setItem(10, menuManager.getMenu(MenuType.KIT).getOpenItem());
    inventory.setItem(13, menuManager.getMenu(MenuType.CAGE).getOpenItem());
    inventory.setItem(16, particlesItem);
    inventory.setItem(49, inventoryUtil.getCloseItem());

    return null;
  }

  @Override
  public Inventory getInventory(GamePlayer gamePlayer) {
    getInventory(gamePlayer, 1);

    return null;
  }

  @EventHandler(ignoreCancelled = true)
  public void onInventoryAPIClick(final InventoryAPIClickEvent event) {
    if (!event.getInventoryWrapper().getId().equals(ID)) {
      return;
    }
    
    final Player player = event.getPlayer();
    final GamePlayer gamePlayer = playerManager.getPlayer(player);
    final ItemStack item = event.getEvent().getCurrentItem();

    if (item != null)
      if (item.isSimilar(inventoryUtil.getCloseItem())) {
        gamePlayer.getPlayer().closeInventory();
      } else {
        final GameMenu kitMenu = this.menuManager.getMenu(MenuType.KIT);
        final GameMenu cageMenu = this.menuManager.getMenu(MenuType.CAGE);
        if (item.isSimilar(kitMenu.getOpenItem())) {
          kitMenu.getInventory(gamePlayer);
        } else if (item.isSimilar(cageMenu.getOpenItem())) {
          cageMenu.getInventory(gamePlayer);
        }
      }
  }

  public String getTitle() {
    return TITLE;
  }

  public MenuType getType() {
    return MenuType.SHOP;
  }
}
