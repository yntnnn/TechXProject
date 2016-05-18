package DrShadow.TechXProject.conduit.item.gui;

import DrShadow.TechXProject.conduit.item.ContainerConduitBase;
import DrShadow.TechXProject.reference.Reference;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;

@SideOnly(Side.CLIENT)
public class GuiConduitBase extends GuiContainer
{
	private int left, top;

	private IInventory inventory;

	public GuiConduitBase(InventoryPlayer inventoryPlayer, IInventory inventory)
	{
		super(new ContainerConduitBase(inventoryPlayer, inventory));

		this.inventory = inventory;

		this.xSize = 176;
		this.ySize = 166;
	}

	@Override
	public void initGui()
	{
		super.initGui();

		left = (this.width - this.xSize) / 2;
		top = (this.height - this.ySize) / 2;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		if (inventory.getStackInSlot(0) == null)
		{
			ResourceLocation soulForgeGuiTextures = new ResourceLocation(Reference.MOD_ID + ":textures/gui/gui_Conduit.png");
			mc.getTextureManager().bindTexture(soulForgeGuiTextures);

			for (int i = 0; i < 2; i++)
			{
				for (int j = 0; j < 4; j++)
				{
					drawRect(29 + j * 18, 31 + i * 20, 29 + j * 18 + 18, 31 + i * 20 + 18, new Color(1, 1, 1, 0.35f).getRGB());
				}
			}
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		ResourceLocation texture = new ResourceLocation(Reference.MOD_ID + ":textures/gui/gui_Conduit.png");
		mc.getTextureManager().bindTexture(texture);
		drawTexturedModalRect(left, top, 0, 0, this.xSize, this.ySize);
	}
}
