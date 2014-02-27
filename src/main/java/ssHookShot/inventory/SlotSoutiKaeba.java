package ssHookShot.inventory;

import ssHookShot.HookShot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotSoutiKaeba extends Slot
{
	public SlotSoutiKaeba(IInventory par2IInventory, int par3, int par4, int par5)
	{
		super(par2IInventory, par3, par4, par5);
	}
	public boolean isItemValid(ItemStack par1ItemStack)
	{
		return par1ItemStack == null ? false : par1ItemStack.getItem() == HookShot.instance.替え刃;
	}
	
	public int getSlotStackLimit()
	{
		return 1;
	}
}