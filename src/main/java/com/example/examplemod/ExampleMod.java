package com.example.examplemod;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.init.Blocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

@Mod(modid = ExampleMod.MODID, version = ExampleMod.VERSION)
public class ExampleMod {
    public static final String MODID = "examplemod";
    public static final String VERSION = "1.0";

    @Mod.Instance(MODID)
    public static ExampleMod instance;

    private boolean renderCustomText = false;
    private String customText = "";

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getWorld().getBlockState(event.getPos()).getBlock();

            if (block == Blocks.CHEST) {
                TileEntityChest chest = (TileEntityChest) event.getWorld().getTileEntity(event.getPos());
                if (chest != null) {
                    displayChestContents(chest);
                }
            }
        }
    }

    private void displayChestContents(TileEntityChest chest) {
        IInventory chestInventory = chest;

        StringBuilder textBuilder = new StringBuilder();
        for (int slot = 0; slot < chestInventory.getSizeInventory(); slot++) {
            if (chestInventory.getStackInSlot(slot) != null) {
                String displayName = chestInventory.getStackInSlot(slot).getDisplayName();
                int stackSize = chestInventory.getStackInSlot(slot).stackSize;
                textBuilder.append(displayName).append(" x").append(stackSize).append("\n");
            }
        }

        customText = textBuilder.toString();
        renderCustomText = true;
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {
        if (renderCustomText) {
            if (event.getType() == ElementType.ALL) {
                renderCustomText(event);
            }
        }
    }

    private void renderCustomText(RenderGameOverlayEvent.Text event) {
        // You can customize the position, color, and style of the text here.
        int x = 10;
        int y = 10;
        int color = 0xFFFFFF; // White color
        event.getLeft().add(new TextComponentString(customText).setStyle(event.getMatrixStack(), x, y, color));
        renderCustomText = false; // Reset the flag after rendering the text.
    }
}
