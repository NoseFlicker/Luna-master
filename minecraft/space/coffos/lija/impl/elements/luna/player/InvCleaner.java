package space.coffos.lija.impl.elements.luna.player;

import com.google.common.collect.Multimap;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.*;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import space.coffos.lija.api.element.Category;
import space.coffos.lija.api.element.Element;
import space.coffos.lija.api.element.ElementStructure;
import space.coffos.lija.api.event.EventRegister;
import space.coffos.lija.api.setting.ModeHandler;
import space.coffos.lija.api.setting.Setting;
import space.coffos.lija.impl.events.EventUpdate;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Zhn17
 * <-> 2018-04-15 <-> 20:36
 **/
@ElementStructure(name = "InvCleaner", category = Category.PLAYER, clientType = "Luna")
public class InvCleaner extends Element {

    @ModeHandler(name = "Mode", currentOption = "Normal", options = {"Normal", "New"}, locked = false)
    public static Setting mode;

    @EventRegister
    public void onUpdate(EventUpdate eventUpdate) {
        if (!getMode().contains(mode.getValString())) {
            toggle();
            toggle();
        }
        if (mode.getValString().equalsIgnoreCase("Normal")) {
            setMode(mode.getValString());

            CopyOnWriteArrayList<Integer> uselessItems = new CopyOnWriteArrayList<>();

            for (int o = 0; o < 45; ++o)
                if (mc.thePlayer.inventoryContainer.getSlot(o).getHasStack()) {
                    ItemStack item = mc.thePlayer.inventoryContainer.getSlot(o).getStack();
                    if (mc.thePlayer.inventory.armorItemInSlot(0) == item | mc.thePlayer.inventory.armorItemInSlot(1) == item | mc.thePlayer.inventory.armorItemInSlot(2) == item | mc.thePlayer.inventory.armorItemInSlot(3) == item)
                        continue;
                    assert item != null;
                    if (item.getItem() != null & Item.getIdFromItem(item.getItem()) != 0 & !stackIsUseful(o))
                        uselessItems.add(o);
                }

            if (!uselessItems.isEmpty() && mc.thePlayer.ticksExisted % 3 == 0) {
                mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.OPEN_INVENTORY));
                mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, uselessItems.get(0), 1, 4, mc.thePlayer);
                uselessItems.remove(0);
            }

        } else if (mode.getValString().equalsIgnoreCase("New")) {
            setMode(mode.getValString());

            if (mc.currentScreen instanceof GuiInventory | mc.currentScreen instanceof GuiContainer) {
                CopyOnWriteArrayList<Integer> uselessItems = new CopyOnWriteArrayList<>();
                for (int o = 0; o < 45; ++o)
                    if (mc.thePlayer.inventoryContainer.getSlot(o).getHasStack()) {
                        ItemStack item = mc.thePlayer.inventoryContainer.getSlot(o).getStack();
                        if (mc.thePlayer.inventory.armorItemInSlot(0) == item | mc.thePlayer.inventory.armorItemInSlot(1) == item | mc.thePlayer.inventory.armorItemInSlot(2) == item | mc.thePlayer.inventory.armorItemInSlot(3) == item)
                            continue;
                        assert item != null;
                        if (item.getItem() != null & Item.getIdFromItem(item.getItem()) != 0 & !stackIsUseful(o))
                            uselessItems.add(o);
                    }

                if (!uselessItems.isEmpty()) {
                    if (mc.thePlayer.ticksExisted % 3 == 0) {
                        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, uselessItems.get(0), 1, 4, mc.thePlayer);
                        uselessItems.remove(0);
                    }
                }
            }
        }
    }

    private boolean stackIsUseful(int i) {
        ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

        boolean hasAlreadyOrBetter = false;

        if (itemStack.getItem() instanceof ItemSword | itemStack.getItem() instanceof ItemPickaxe
                | itemStack.getItem() instanceof ItemAxe) {
            int o = 0;
            while (o < 45) {
                if (o == i) {
                    ++o;
                    continue;
                }
                if (mc.thePlayer.inventoryContainer.getSlot(o).getHasStack()) {
                    ItemStack item = mc.thePlayer.inventoryContainer.getSlot(o).getStack();
                    assert item != null;
                    if (item.getItem() instanceof ItemSword | Objects.requireNonNull(item).getItem() instanceof ItemAxe | item.getItem() instanceof ItemPickaxe) {
                        float damageFound = getItemDamage(itemStack);
                        damageFound += EnchantmentHelper.func_152377_a(itemStack, EnumCreatureAttribute.UNDEFINED);

                        float damageCurrent = getItemDamage(item);
                        damageCurrent += EnchantmentHelper.func_152377_a(item, EnumCreatureAttribute.UNDEFINED);

                        if (damageCurrent > damageFound) {
                            hasAlreadyOrBetter = true;
                            break;
                        }
                    }
                }
                ++o;
            }
        } else if (itemStack.getItem() instanceof ItemArmor) {
            for (int o = 0; o < 45; ++o) {
                if (i == o)
                    continue;
                if (mc.thePlayer.inventoryContainer.getSlot(o).getHasStack()) {
                    ItemStack item = mc.thePlayer.inventoryContainer.getSlot(o).getStack();
                    assert item != null;
                    if (item.getItem() instanceof ItemArmor) {
                        List<Integer> helmet = Arrays.asList(298, 314, 302, 306, 310);
                        List<Integer> chestPlate = Arrays.asList(299, 315, 303, 307, 311);
                        List<Integer> leggings = Arrays.asList(300, 316, 304, 308, 312);
                        List<Integer> boots = Arrays.asList(301, 317, 305, 309, 313);

                        if (helmet.contains(Item.getIdFromItem(item.getItem())) & helmet.contains(Item.getIdFromItem(itemStack.getItem())) | helmet.indexOf(Item.getIdFromItem(itemStack.getItem())) < helmet.indexOf(Item.getIdFromItem(item.getItem()))) {
                            hasAlreadyOrBetter = true;
                            break;
                        } else if (chestPlate.contains(Item.getIdFromItem(item.getItem())) & chestPlate.contains(Item.getIdFromItem(itemStack.getItem())) | chestPlate.indexOf(Item.getIdFromItem(itemStack.getItem())) < chestPlate.indexOf(Item.getIdFromItem(item.getItem()))) {
                            hasAlreadyOrBetter = true;
                            break;
                        } else if (leggings.contains(Item.getIdFromItem(item.getItem())) & leggings.contains(Item.getIdFromItem(itemStack.getItem())) | leggings.indexOf(Item.getIdFromItem(itemStack.getItem())) < leggings.indexOf(Item.getIdFromItem(item.getItem()))) {
                            hasAlreadyOrBetter = true;
                            break;
                        } else if (boots.contains(Item.getIdFromItem(item.getItem())) & boots.contains(Item.getIdFromItem(itemStack.getItem())) | boots.indexOf(Item.getIdFromItem(itemStack.getItem())) < boots.indexOf(Item.getIdFromItem(item.getItem()))) {
                            hasAlreadyOrBetter = true;
                            break;
                        }
                    }
                }
            }
        }

        for (int o = 0; o < 45; ++o) {
            if (i == o)
                continue;
            if (mc.thePlayer.inventoryContainer.getSlot(o).getHasStack()) {
                ItemStack item = mc.thePlayer.inventoryContainer.getSlot(o).getStack();
                assert item != null;
                if (item.getItem() instanceof ItemSword | item.getItem() instanceof ItemAxe | item.getItem() instanceof ItemBow | item.getItem() instanceof ItemFishingRod | item.getItem() instanceof ItemArmor | item.getItem() instanceof ItemPickaxe | Item.getIdFromItem(item.getItem()) == 346 && Item.getIdFromItem(itemStack.getItem()) == Item.getIdFromItem(item.getItem())) {
                    hasAlreadyOrBetter = true;
                    break;
                }
            }
        }

        if (Item.getIdFromItem(itemStack.getItem()) == 367 || Item.getIdFromItem(itemStack.getItem()) == 259)
            return false; // rotten flesh
        // flint & steel
        if (Item.getIdFromItem(itemStack.getItem()) == 262 || Item.getIdFromItem(itemStack.getItem()) == 264 || Item.getIdFromItem(itemStack.getItem()) == 265 || Item.getIdFromItem(itemStack.getItem()) == 336 || Item.getIdFromItem(itemStack.getItem()) == 266 || itemStack.hasDisplayName())
            return true;
        if (Item.getIdFromItem(itemStack.getItem()) == 345 || Item.getIdFromItem(itemStack.getItem()) == 46 || Item.getIdFromItem(itemStack.getItem()) == 261 || Item.getIdFromItem(itemStack.getItem()) == 262 || Item.getIdFromItem(itemStack.getItem()) == 116 || Item.getIdFromItem(itemStack.getItem()) == 54 | Item.getIdFromItem(itemStack.getItem()) == 54)
            return false;

        if (hasAlreadyOrBetter) return false;

        if (itemStack.getItem() instanceof ItemArmor || itemStack.getItem() instanceof ItemAxe || itemStack.getItem() instanceof ItemSword || itemStack.getItem() instanceof ItemPotion || itemStack.getItem() instanceof ItemBlock || itemStack.getItem() instanceof ItemFood || itemStack.getItem() instanceof ItemEnderPearl)
            return true;
        if (itemStack.getItem() instanceof ItemBow || Item.getIdFromItem(itemStack.getItem()) == 345 || itemStack.getItem() instanceof ItemFlintAndSteel)
            return false;
        return itemStack.getItem() instanceof ItemPickaxe;
    }

    private float getItemDamage(ItemStack itemStack) {
        Multimap multimap = itemStack.getAttributeModifiers();
        if (!multimap.isEmpty()) {
            Iterator iterator = multimap.entries().iterator();
            if (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                AttributeModifier attributeModifier = (AttributeModifier) entry.getValue();
                double damage;
                if (attributeModifier.getOperation() != 1 & attributeModifier.getOperation() != 2)
                    damage = attributeModifier.getAmount();
                else damage = attributeModifier.getAmount() * 100.0;
                if (attributeModifier.getAmount() > 1.0) return 1.0f + (float) damage;
                return 1.0f;
            }
        }
        return 1.0f;
    }
}