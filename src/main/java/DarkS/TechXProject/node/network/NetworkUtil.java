package DarkS.TechXProject.node.network;

import DarkS.TechXProject.api.network.INetworkContainer;
import DarkS.TechXProject.api.network.INetworkElement;
import DarkS.TechXProject.api.network.INetworkRelay;
import DarkS.TechXProject.api.network.NodeNetwork;
import DarkS.TechXProject.node.network.controller.TileNetworkController;
import DarkS.TechXProject.util.ChatUtil;
import DarkS.TechXProject.util.Logger;
import DarkS.TechXProject.util.Util;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NetworkUtil
{
	public static NodeNetwork readNetwork(World world, List<BlockPos> positions, INetworkContainer container)
	{
		if (container == null) container = new TileNetworkController();

		NodeNetwork result = new NodeNetwork(container);

		for (BlockPos pos : positions)
			result.addToNetwork((INetworkElement) world.getTileEntity(pos));

		return result;
	}

	public static void writeNetworkNBT(NBTTagCompound tag, NodeNetwork network)
	{
		List<BlockPos> positions = network.getElements().stream().map(element -> element.getTile().getPos()).collect(Collectors.toList());

		NBTTagList tagList = new NBTTagList();

		for (BlockPos pos : positions)
		{
			NBTTagCompound posTag = new NBTTagCompound();

			posTag.setInteger("xPos", pos.getX());
			posTag.setInteger("yPos", pos.getY());
			posTag.setInteger("zPos", pos.getZ());

			tagList.appendTag(posTag);
		}

		tag.setTag("elementPos", tagList);
	}

	public static List<BlockPos> readNetworkNBT(NBTTagCompound tag)
	{
		List<BlockPos> positions = new ArrayList<>();

		NBTTagList tagList = tag.getTagList("elementPos", 10);

		for (int i = 0; i < tagList.tagCount(); i++)
		{
			NBTTagCompound posTag = tagList.getCompoundTagAt(i);

			positions.add(new BlockPos(posTag.getInteger("xPos"), posTag.getInteger("yPos"), posTag.getInteger("zPos")));
		}

		return positions;
	}

	public static void link(Object from, Object to)
	{
		if (from == null || to == null)
		{
			ChatUtil.sendNoSpam(Util.player(), ChatFormatting.RED + "Connection Failed!");

			return;
		}

		if (from instanceof INetworkElement && to instanceof INetworkRelay)
		{
			link((INetworkElement) from, (INetworkRelay) to);
		} else if (from instanceof INetworkRelay && to instanceof INetworkElement)
		{
			link((INetworkRelay) from, (INetworkElement) to);
		} else ChatUtil.sendNoSpam(Util.player(), ChatFormatting.RED + "Connection Failed!");
	}

	private static void link(INetworkElement from, INetworkRelay to)
	{
		from.setNetwork(((INetworkElement) to).getNetwork());

		to.addElement(from);

		from.setNetwork(((INetworkElement) to).getNetwork());
	}

	private static void link(INetworkRelay from, INetworkElement to)
	{
		to.setNetwork(((INetworkElement) from).getNetwork());

		from.addElement(to);

		to.setNetwork(((INetworkElement) from).getNetwork());
	}
}
