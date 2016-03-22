package DrShadow.TechXProject.conduit.item;

import DrShadow.TechXProject.conduit.item.filter.IItemStackFilter;
import DrShadow.TechXProject.items.inventory.ItemInventory;
import DrShadow.TechXProject.util.GhostItemHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerConduitBase extends Container
{
	private IInventory inventory;
	private ItemInventory itemInventory;
	private ItemStack filter;

	public ContainerConduitBase(InventoryPlayer inventoryPlayer, IInventory inventory)
	{
		this.inventory = inventory;

		filter = inventory.getStackInSlot(0);
		itemInventory = new ItemInventory(filter, 8, "");

		itemInventory.initializeInventory(filter);

		addSlotToContainer(new SlotItemFilter(this, inventory, 0, 80, 35));

		if (inventory.getStackInSlot(0) != null)
		{
			addSlotToContainer(new SlotGhostItem(itemInventory, 0, 57, 12));
			addSlotToContainer(new SlotGhostItem(itemInventory, 1, 80, 11));
			addSlotToContainer(new SlotGhostItem(itemInventory, 2, 103, 12));
			addSlotToContainer(new SlotGhostItem(itemInventory, 3, 104, 35));
			addSlotToContainer(new SlotGhostItem(itemInventory, 4, 103, 58));
			addSlotToContainer(new SlotGhostItem(itemInventory, 5, 80, 59));
			addSlotToContainer(new SlotGhostItem(itemInventory, 6, 57, 58));
			addSlotToContainer(new SlotGhostItem(itemInventory, 7, 56, 35));
		}

		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 9; j++)
			{
				addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (int i = 0; i < 9; i++)
		{
			addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
		}
	}

	@Override
	public ItemStack slotClick(int slotId, int clickedButton, int mode, EntityPlayer player)
	{
		InventoryPlayer inventoryPlayer = player.inventory;
		{
			if (slotId >= 0)
			{
				Slot slot = this.inventorySlots.get(slotId);

				if (slot instanceof SlotGhostItem)
				{
					if ((mode == 0 || mode == 1) && (clickedButton == 0 || clickedButton == 1))
					{
						ItemStack slotStack = slot.getStack();
						ItemStack heldStack = inventoryPlayer.getItemStack();

						if (mode == 0)
						{
							if (clickedButton == 0)
							{
								if (heldStack != null)
								{
									if (!((SlotGhostItem) slot).canBeAccessed())
									{
										return super.slotClick(slotId, clickedButton, mode, player);
									} else
									{
										ItemStack copyStack = heldStack.copy();
										GhostItemHelper.setItemGhostAmount(copyStack, 0);
										copyStack.stackSize = 1;
										slot.putStack(copyStack);
									}
								}
							} else
							{
								if (slotStack != null) slot.putStack(null);
							}
						}
					}
				}
			}
		}
		return super.slotClick(slotId, clickedButton, mode, player);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		ItemStack itemstack = null;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index == 0)
			{
				if (!this.mergeItemStack(itemstack1, 9, 1 + 36, true))
				{
					return null;
				}

				slot.onSlotChange(itemstack1, itemstack);
			} else if (index > 0)
			{
				if (itemstack1.getItem() instanceof IItemStackFilter)
				{
					if (!this.mergeItemStack(itemstack1, 0, 1, false))
					{
						return null;
					}
				}
			} else if (!this.mergeItemStack(itemstack1, 9, 1 + 36, false))
			{
				return null;
			}

			if (itemstack1.stackSize == 0)
			{
				slot.putStack(null);
			} else
			{
				slot.onSlotChanged();
			}

			if (itemstack1.stackSize == itemstack.stackSize)
			{
				return null;
			}

			slot.onPickupFromSlot(playerIn, itemstack1);
		}

		return itemstack;
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return true;
	}

	private class SlotItemFilter extends Slot
	{
		public ContainerConduitBase container;

		public SlotItemFilter(ContainerConduitBase container, IInventory inventory, int slotIndex, int x, int y)
		{
			super(inventory, slotIndex, x, y);
			this.container = container;
		}

		@Override
		public boolean isItemValid(ItemStack itemStack)
		{
			return itemStack.getItem() instanceof IItemStackFilter;
		}

		@Override
		public void onSlotChanged()
		{
			super.onSlotChanged();
			for (int i = 1; i <= 8; i++)
			{
				itemInventory.initializeInventory(getStack());

				Slot slot = container.getSlot(i);
				slot.onSlotChanged();
			}
		}

		@Override
		public int getSlotStackLimit()
		{
			return 1;
		}
	}

	public class SlotGhostItem extends Slot
	{
		private ItemInventory itemInv;

		public SlotGhostItem(ItemInventory inventory, int slotIndex, int x, int y)
		{
			super(inventory, slotIndex, x, y);
			itemInv = inventory;
		}

		@Override
		public boolean isItemValid(ItemStack stack)
		{
			return false;
		}

		@Override
		public boolean canTakeStack(EntityPlayer playerIn)
		{
			return false;
		}

		public boolean canBeAccessed()
		{
			return itemInv.canInventoryBeManipulated();
		}

		@Override
		public void onSlotChanged()
		{
			super.onSlotChanged();
		}
	}
}
