package DrShadow.TechXProject.conduit.network.controller;

import DrShadow.TechXProject.api.network.INetworkContainer;
import DrShadow.TechXProject.api.network.INetworkElement;
import DrShadow.TechXProject.api.network.INetworkRelay;
import DrShadow.TechXProject.conduit.network.ConduitNetwork;
import DrShadow.TechXProject.conduit.network.relay.TileNetworkRelay;
import DrShadow.TechXProject.items.ItemWrench;
import DrShadow.TechXProject.tileEntities.ModTileEntity;
import DrShadow.TechXProject.util.Logger;
import DrShadow.TechXProject.util.Util;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class TileNetworkController extends ModTileEntity implements INetworkContainer
{
	public static final int rad = 8;
	private ConduitNetwork network;
	private int cooldown = 100;
	private int relayCooldown = 100 * 2;

	public TileNetworkController()
	{
		initNetwork();
	}

	@Override
	public void update()
	{
		cooldown -= 1;
		relayCooldown -= 1;

		if (cooldown <= 0)
		{
			cooldown = 100;

			searchNetwork();
		}

		if (relayCooldown <= 0)
		{
			relayCooldown = 100 * 2;

			searchRelays();
		}
	}

	@Override
	public ConduitNetwork getNetwork()
	{
		return network;
	}

	@Override
	public void setNetwork(ConduitNetwork network)
	{
		this.network = network;
	}

	@Override
	public void searchNetwork()
	{
		if (Util.player() != null && Util.player().getHeldItem() != null && Util.player().getHeldItem().getItem() instanceof ItemWrench)
		{
			Util.spawnParticlesOnBorder(pos.getX() - rad, pos.getY() - rad / 2, pos.getZ() - rad, pos.getX() + rad, pos.getY() + rad / 2, pos.getZ() + rad, worldObj, 0.01f, 0, 1);
		}

		network.clear();

		for (int x = pos.getX() - rad; x < pos.getX() + rad; x++)
		{
			for (int y = pos.getY() - rad / 2; y < pos.getY() + rad / 2; y++)
			{
				for (int z = pos.getZ() - rad; z < pos.getZ() + rad; z++)
				{
					TileEntity tile = worldObj.getTileEntity(new BlockPos(x, y, z));

					if (tile != null && tile instanceof INetworkElement)
					{
						INetworkElement element = (INetworkElement) tile;

						addToNetwork(element);

						for (INetworkElement networkElement : network.getElements())
						{
							if (networkElement != null) networkElement.setNetwork(network);
						}

						markDirty();
					}
				}
			}
		}
	}

	@Override
	public void searchRelays()
	{
		int relayRad = rad + TileNetworkRelay.relayRad;

		if (Util.player() != null && Util.player().getHeldItem() != null && Util.player().getHeldItem().getItem() instanceof ItemWrench)
		{
			Util.spawnParticlesOnBorder(pos.getX() - relayRad, pos.getY() - relayRad / 2, pos.getZ() - relayRad, pos.getX() + relayRad, pos.getY() + relayRad / 2, pos.getZ() + relayRad, worldObj, 0.01f, 0.45f, 0.55f);
		}

		for (int x = pos.getX() - relayRad; x < pos.getX() + relayRad; x++)
		{
			for (int y = pos.getY() - relayRad; y < pos.getY() + relayRad; y++)
			{
				for (int z = pos.getZ() - relayRad; z < pos.getZ() + relayRad; z++)
				{
					TileEntity tile = worldObj.getTileEntity(new BlockPos(x, y, z));

					if (tile != null && tile instanceof INetworkRelay)
					{
						INetworkRelay relay = (INetworkRelay) tile;

						for (INetworkElement element : relay.getElements())
						{
							if (element != null && !network.isInNetwork(element)) network.addToNetwork(element);
						}
					}
				}
			}
		}
	}

	@Override
	public ConduitNetwork addToNetwork(INetworkElement toAdd)
	{
		if (!network.isInNetwork(toAdd))
		{
			Logger.info(toAdd);

			network.addToNetwork(toAdd);
		}

		return network;
	}

	@Override
	public ConduitNetwork removeFromNetwork(INetworkElement toRemove)
	{
		network.removeFromNetwork(toRemove);

		return network;
	}

	@Override
	public void updateNetwork()
	{
		network.update();
	}

	@Override
	public TileEntity getController()
	{
		return this;
	}

	@Override
	public void initNetwork()
	{
		if (network == null)
		{
			network = new ConduitNetwork(this);
		}
	}
}