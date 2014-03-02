package ssHookShot.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import ssHookShot.HookShot;

public class ContainerMoveLeg extends Container{

	private IInventory leftInv = new InventorySouti();
	private IInventory rightInv = new InventorySouti();
	private IInventory leftGus = new InventoryGus();
	private IInventory rightGus = new InventoryGus();

	private final int invSlotSize = leftInv.getSizeInventory() + rightInv.getSizeInventory() + leftGus.getSizeInventory() + rightGus.getSizeInventory();

	private EntityPlayer player;
	private IInventory playerInventory;

	public ContainerMoveLeg(EntityPlayer player)
	{
		this.player          = player;
		this.playerInventory = player.inventory;

		NBTTagList nbttaglist = player.getCurrentArmor(1).getTagCompound().getTagList("lgbitems", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < 1; ++i)
		{
			if(nbttaglist.tagCount()>0){
				NBTTagCompound nbt = nbttaglist.getCompoundTagAt(i);
				int j = nbt.getByte("Slot") & 255;

				if (j >= 0 && j < leftGus.getSizeInventory())
				{
					leftGus.setInventorySlotContents(i,ItemStack.loadItemStackFromNBT(nbt));
				}
			}
		}

		addSlotToContainer(new SlotSoutiGus(leftGus,0, 8, 10));

		nbttaglist = player.getCurrentArmor(1).getTagCompound().getTagList("rgbitems", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < 1; ++i)
		{
			if(nbttaglist.tagCount()>0){
				NBTTagCompound nbt = nbttaglist.getCompoundTagAt(i);
				int j = nbt.getByte("Slot") & 255;

				if (j >= 0 && j < rightGus.getSizeInventory())
				{
					rightGus.setInventorySlotContents(i,ItemStack.loadItemStackFromNBT(nbt));
				}
			}
		}

		addSlotToContainer(new SlotSoutiGus(rightGus, 0, 8, 44));

		nbttaglist = player.getCurrentArmor(1).getTagCompound().getTagList("litems", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < nbttaglist.tagCount(); ++i)
		{
			NBTTagCompound nbt = nbttaglist.getCompoundTagAt(i);
			int j = nbt.getByte("Slot") & 255;

			if (j >= 0 && j < leftInv.getSizeInventory())
			{
				leftInv.setInventorySlotContents(i,ItemStack.loadItemStackFromNBT(nbt));
			}
		}

		for (int slotIndex = 0; slotIndex < 6; ++slotIndex)
		{
			addSlotToContainer(new SlotMoveLegBlade(leftInv, slotIndex, 44 + slotIndex * 18, 10));
		}

		nbttaglist = player.getCurrentArmor(1).getTagCompound().getTagList("ritems", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < nbttaglist.tagCount(); ++i)
		{
			NBTTagCompound nbt = nbttaglist.getCompoundTagAt(i);
			int j = nbt.getByte("Slot") & 255;

			if (j >= 0 && j < rightInv.getSizeInventory())
			{
				rightInv.setInventorySlotContents(i,ItemStack.loadItemStackFromNBT(nbt));
			}
		}

		for (int slotIndex = 0; slotIndex < 6; ++slotIndex)
		{
			addSlotToContainer(new SlotMoveLegBlade(rightInv, slotIndex, 44 + slotIndex * 18, 44));
		}

		for (int rows = 0; rows < 3; ++rows)
		{
			for (int slotIndex = 0; slotIndex < 9; ++slotIndex)
			{
				addSlotToContainer(new Slot(playerInventory, slotIndex + rows * 9 + 9, 8 + slotIndex * 18, 84 + rows * 18));
			}
		}

		for (int slotIndex = 0; slotIndex < 9; ++slotIndex)
		{
			addSlotToContainer(new Slot(playerInventory, slotIndex, 8 + slotIndex * 18, 142));
		}
	}

	@Override
	public void onContainerClosed(EntityPlayer p)
	{
		super.onContainerClosed(p);

		if (!this.player.worldObj.isRemote)
		{

			NBTTagList nbttaglist = new NBTTagList();

			for (int i = 0; i < this.leftInv.getSizeInventory(); ++i)
			{
				if (this.leftInv.getStackInSlot(i) != null)
				{
					NBTTagCompound nbt = new NBTTagCompound();
					nbt.setByte("Slot", (byte) i);
					this.leftInv.getStackInSlot(i).writeToNBT(nbt);
					nbttaglist.appendTag(nbt);
				}
			}

			p.getCurrentArmor(1).getTagCompound().setTag("litems", nbttaglist);

			nbttaglist = new NBTTagList();

			for (int i = 0; i < this.rightInv.getSizeInventory(); ++i)
			{
				if (this.rightInv.getStackInSlot(i) != null)
				{
					NBTTagCompound nbttagcompound1 = new NBTTagCompound();
					nbttagcompound1.setByte("Slot", (byte)i);
					this.rightInv.getStackInSlot(i).writeToNBT(nbttagcompound1);
					nbttaglist.appendTag(nbttagcompound1);
				}
			}

			p.getCurrentArmor(1).getTagCompound().setTag("ritems", nbttaglist);

			nbttaglist = new NBTTagList();
			
			for (int i = 0; i < this.leftGus.getSizeInventory(); ++i)
			{
				if (this.leftGus.getStackInSlot(i) != null)
				{
					NBTTagCompound nbttagcompound1 = new NBTTagCompound();
					nbttagcompound1.setByte("Slot", (byte)i);
					this.leftGus.getStackInSlot(i).writeToNBT(nbttagcompound1);
					nbttaglist.appendTag(nbttagcompound1);
				}
			}

			p.getCurrentArmor(1).getTagCompound().setTag("lgbitems", nbttaglist);

			nbttaglist = new NBTTagList();

			for (int i = 0; i < this.rightGus.getSizeInventory(); ++i)
			{
				if (this.rightGus.getStackInSlot(i) != null)
				{
					NBTTagCompound nbttagcompound1 = new NBTTagCompound();
					nbttagcompound1.setByte("Slot", (byte)i);
					this.rightGus.getStackInSlot(i).writeToNBT(nbttagcompound1);
					nbttaglist.appendTag(nbttagcompound1);
				}
			}

			p.getCurrentArmor(1).getTagCompound().setTag("rgbitems", nbttaglist);
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer p, int slotIndex)
	{
		ItemStack itemstack = null;
		Slot slot = (Slot)this.inventorySlots.get(slotIndex);

		if (slot != null && slot.getHasStack())
		{
			ItemStack stack = slot.getStack();
			itemstack = stack.copy();

			if (slotIndex >= invSlotSize && slotIndex < invSlotSize + 36) {
				if (stack.getItem() == HookShot.instance.itemFuel) {
					if (!this.mergeItemStack(stack, 0, 2, false))
					{
						return null;
					}
				}
				else if (stack.getItem() == HookShot.instance.itemBlade) {
					if (!this.mergeItemStack(stack, 2, invSlotSize, false))
					{
						return null;
					}
				}
				else if (slotIndex >= invSlotSize && slotIndex < invSlotSize + 27)
				{
					if (!this.mergeItemStack(stack, invSlotSize + 27, invSlotSize + 36, false))
					{
						return null;
					}
				}
				else if (slotIndex >= invSlotSize + 27 && slotIndex < invSlotSize + 36)
				{
					if (!this.mergeItemStack(stack, invSlotSize, invSlotSize + 27, false))
					{
						return null;
					}
				}
			}
			else if (!this.mergeItemStack(stack, invSlotSize + 27, invSlotSize + 36, false))
			{
				return null;
			}

			if (stack.stackSize == 0)
			{
				slot.putStack(null);
			}
			else
			{
				slot.onSlotChanged();
			}

			if (stack.stackSize == itemstack.stackSize)
			{
				return null;
			}

			slot.onPickupFromSlot(p, stack);
		}

		return itemstack;
	}
}
