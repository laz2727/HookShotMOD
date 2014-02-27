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

public class ContainerSouti extends Container{

	private IInventory 左 = new InventorySouti();
	private IInventory 右 = new InventorySouti();
	private IInventory 左ボンベ = new InventoryGus();
	private IInventory 右ボンベ = new InventoryGus();

	private final int 燃料と替え刃SIZE = 左.getSizeInventory() + 右.getSizeInventory() + 左ボンベ.getSizeInventory() + 右ボンベ.getSizeInventory();

	private EntityPlayer player;                            // プレイヤー(今回はほとんど使わないが, チャット欄にメッセージを出したいときなどに使う
	private IInventory playerInventory;

	public ContainerSouti(EntityPlayer player)
	{
		this.player          = player;
		this.playerInventory = player.inventory;

		NBTTagList nbttaglist = player.getCurrentArmor(1).getTagCompound().getTagList("lgbitems", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < 1; ++i)
		{
			if(nbttaglist.tagCount()>0){
				NBTTagCompound nbt = (NBTTagCompound)nbttaglist.getCompoundTagAt(i);
				int j = nbt.getByte("Slot") & 255;

				if (j >= 0 && j < 左ボンベ.getSizeInventory())
				{
					左ボンベ.setInventorySlotContents(i,ItemStack.loadItemStackFromNBT(nbt));
				}
			}
		}

		addSlotToContainer(new SlotSoutiGus(左ボンベ,0, 8, 10));

		nbttaglist = player.getCurrentArmor(1).getTagCompound().getTagList("rgbitems", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < 1; ++i)
		{
			if(nbttaglist.tagCount()>0){
				NBTTagCompound nbt = (NBTTagCompound)nbttaglist.getCompoundTagAt(i);
				int j = nbt.getByte("Slot") & 255;

				if (j >= 0 && j < 右ボンベ.getSizeInventory())
				{
					右ボンベ.setInventorySlotContents(i,ItemStack.loadItemStackFromNBT(nbt));
				}
			}
		}

		addSlotToContainer(new SlotSoutiGus(右ボンベ, 0, 8, 44));

		nbttaglist = player.getCurrentArmor(1).getTagCompound().getTagList("litems", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < nbttaglist.tagCount(); ++i)
		{
			NBTTagCompound nbt = (NBTTagCompound)nbttaglist.getCompoundTagAt(i);
			int j = nbt.getByte("Slot") & 255;

			if (j >= 0 && j < 左.getSizeInventory())
			{
				左.setInventorySlotContents(i,ItemStack.loadItemStackFromNBT(nbt));
			}
		}

		for (int slotIndex = 0; slotIndex < 6; ++slotIndex)
		{
			addSlotToContainer(new SlotSoutiKaeba(左, slotIndex, 44 + slotIndex * 18, 10));
		}

		nbttaglist = player.getCurrentArmor(1).getTagCompound().getTagList("ritems", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < nbttaglist.tagCount(); ++i)
		{
			NBTTagCompound nbt = (NBTTagCompound)nbttaglist.getCompoundTagAt(i);
			int j = nbt.getByte("Slot") & 255;

			if (j >= 0 && j < 右.getSizeInventory())
			{
				右.setInventorySlotContents(i,ItemStack.loadItemStackFromNBT(nbt));
			}
		}

		for (int slotIndex = 0; slotIndex < 6; ++slotIndex)
		{
			addSlotToContainer(new SlotSoutiKaeba(右, slotIndex, 44 + slotIndex * 18, 44));
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

			for (int i = 0; i < this.左.getSizeInventory(); ++i)
			{
				if (this.左.getStackInSlot(i) != null)
				{
					NBTTagCompound nbt = new NBTTagCompound();
					nbt.setByte("Slot", (byte) i);
					this.左.getStackInSlot(i).writeToNBT(nbt);
					nbttaglist.appendTag(nbt);
				}
			}

			p.getCurrentArmor(1).getTagCompound().setTag("litems", nbttaglist);

			nbttaglist = new NBTTagList();

			for (int i = 0; i < this.右.getSizeInventory(); ++i)
			{
				if (this.右.getStackInSlot(i) != null)
				{
					NBTTagCompound nbttagcompound1 = new NBTTagCompound();
					nbttagcompound1.setByte("Slot", (byte)i);
					this.右.getStackInSlot(i).writeToNBT(nbttagcompound1);
					nbttaglist.appendTag(nbttagcompound1);
				}
			}

			p.getCurrentArmor(1).getTagCompound().setTag("ritems", nbttaglist);

			nbttaglist = new NBTTagList();
			
			for (int i = 0; i < this.左ボンベ.getSizeInventory(); ++i)
			{
				if (this.左ボンベ.getStackInSlot(i) != null)
				{
					NBTTagCompound nbttagcompound1 = new NBTTagCompound();
					nbttagcompound1.setByte("Slot", (byte)i);
					this.左ボンベ.getStackInSlot(i).writeToNBT(nbttagcompound1);
					nbttaglist.appendTag(nbttagcompound1);
				}
			}

			p.getCurrentArmor(1).getTagCompound().setTag("lgbitems", nbttaglist);

			nbttaglist = new NBTTagList();

			for (int i = 0; i < this.右ボンベ.getSizeInventory(); ++i)
			{
				if (this.右ボンベ.getStackInSlot(i) != null)
				{
					NBTTagCompound nbttagcompound1 = new NBTTagCompound();
					nbttagcompound1.setByte("Slot", (byte)i);
					this.右ボンベ.getStackInSlot(i).writeToNBT(nbttagcompound1);
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

			if (slotIndex >= 燃料と替え刃SIZE && slotIndex < 燃料と替え刃SIZE + 36) {
				if (stack.getItem() == HookShot.instance.燃料) {
					if (!this.mergeItemStack(stack, 0, 2, false))
					{
						return null;
					}
				}
				else if (stack.getItem() == HookShot.instance.替え刃) {
					if (!this.mergeItemStack(stack, 2, 燃料と替え刃SIZE, false))
					{
						return null;
					}
				}
				else if (slotIndex >= 燃料と替え刃SIZE && slotIndex < 燃料と替え刃SIZE + 27)
				{
					if (!this.mergeItemStack(stack, 燃料と替え刃SIZE + 27, 燃料と替え刃SIZE + 36, false))
					{
						return null;
					}
				}
				else if (slotIndex >= 燃料と替え刃SIZE + 27 && slotIndex < 燃料と替え刃SIZE + 36)
				{
					if (!this.mergeItemStack(stack, 燃料と替え刃SIZE, 燃料と替え刃SIZE + 27, false))
					{
						return null;
					}
				}
			}
			else if (!this.mergeItemStack(stack, 燃料と替え刃SIZE + 27, 燃料と替え刃SIZE + 36, false))
			{
				return null;
			}

			if (stack.stackSize == 0)
			{
				slot.putStack((ItemStack)null);
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
