package ssHookShot.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class InventorySouti implements IInventory
{
	private ItemStack[] stackInput = new ItemStack[6];
	
	// IInventoryを実装しただけ
	public int getSizeInventory()
	{
		return stackInput.length;
	}
	
	public ItemStack getStackInSlot(int i)
	{
		return this.stackInput[i];
	}
	
	public ItemStack decrStackSize(int par1, int par2)
	{
		if (this.stackInput[par1] != null)
		{
			ItemStack var3 = this.stackInput[par1];
			this.stackInput[par1] = null;
			return var3;
		}
		else
		{
			return null;
		}
	}
	
	public ItemStack getStackInSlotOnClosing(int par1)
	{
		if (this.stackInput[par1] != null)
		{
			ItemStack var2 = this.stackInput[par1];
			this.stackInput[par1] = null;
			return var2;
		}
		else
		{
			return null;
		}
	}
	
	public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
	{
		this.stackInput[par1] = par2ItemStack;
	}

    @Override
    public String getInventoryName() {
        return "Inventory";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    public int getInventoryStackLimit()
	{
		return 64;
	}

    @Override
    public void markDirty() {}

    public void onInventoryChanged(){}
	
	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
	{
		return true;
	}

    @Override
    public void openInventory() {}

    @Override
    public void closeInventory() {}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return false;
	}
}