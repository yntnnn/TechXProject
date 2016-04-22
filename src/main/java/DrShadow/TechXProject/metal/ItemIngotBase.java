package DrShadow.TechXProject.metal;

import DrShadow.TechXProject.blocks.base.IRecipeProvider;
import DrShadow.TechXProject.blocks.base.IRenderer;
import DrShadow.TechXProject.init.InitItems;
import DrShadow.TechXProject.items.ItemBase;
import DrShadow.TechXProject.reference.Reference;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.List;

public class ItemIngotBase extends ItemBase implements IRenderer, IRecipeProvider
{
	public ItemIngotBase()
	{
		setHasSubtypes(true);
	}

	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
	{
		for (EnumMetals metal : EnumMetals.values())
		{
			if (metal.contains(EnumMetalType.INGOT) && !metal.contains(EnumMetalType.VANILLA))
			{
				subItems.add(new ItemStack(this, 1, metal.ordinal()));
			}
		}
	}

	@Override
	public int getMetadata(int damage)
	{
		return damage;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return "item." + Reference.MOD_ID.toLowerCase() + "." + EnumMetals.values()[getMetadata(stack.getItemDamage())].getName() + "Ingot";
	}

	@Override
	public void registerModel()
	{
		for (EnumMetals metal : EnumMetals.values())
		{
			if (metal.contains(EnumMetalType.INGOT) && !metal.contains(EnumMetalType.VANILLA))
			{
				ModelLoader.setCustomModelResourceLocation(this, metal.ordinal(), new ModelResourceLocation(Reference.MOD_ID + ":" + metal.getName().toLowerCase() + "Ingot", "inventory"));
			}
		}
	}

	@Override
	public void registerRecipes()
	{
		for (EnumMetals metal : EnumMetals.values())
		{
			if (metal.contains(EnumMetalType.INGOT) && metal.contains(EnumMetalType.NUGGET) && metal.registerRecipe())
			{
				GameRegistry.addShapelessRecipe(new ItemStack(InitItems.nugget.item, 9, metal.ordinal()), new ItemStack(InitItems.ingot.item, 1, metal.ordinal()));
				GameRegistry.addShapedRecipe(new ItemStack(InitItems.ingot.item, 1, metal.ordinal()), "nnn", "nnn", "nnn", 'n', new ItemStack(InitItems.nugget.item, 1, metal.ordinal()));
			}
		}
	}
}
