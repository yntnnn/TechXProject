package DrShadow.TechXProject.configuration.box;

import DrShadow.TechXProject.util.Helper;
import DrShadow.TechXProject.util.OverlayHelper;
import net.minecraft.client.gui.FontRenderer;

import java.awt.*;
import java.text.NumberFormat;

public class FloatBox
{
	public int x, y;
	public float defaultValue, minValue, maxValue, currentValue;

	public FloatBox(float defaultValue, float minValue, float maxValue)
	{
		this.defaultValue = defaultValue;
		this.minValue = minValue;
		this.maxValue = maxValue;
	}

	public void onMouseClicked(int mouseX, int mouseY)
	{
		Rectangle left = new Rectangle(x - 10, y, 8, 10);
		Rectangle right = new Rectangle(x + 128, y, 8, 10);

		if (left.contains(mouseX, mouseY))
		{
			if (currentValue > minValue)
			{
				currentValue -= 0.01f;
			}
		}

		if (right.contains(mouseX, mouseY))
		{
			if (currentValue < maxValue)
			{
				currentValue += 0.01f;
			}
		}
	}

	public void render(int x, int y)
	{
		this.x = x;
		this.y = y;

		OverlayHelper overlayHelper = new OverlayHelper();
		FontRenderer fontRenderer = Helper.minecraft().fontRendererObj;

		String current = NumberFormat.getInstance().format(getCurrentValue());

		overlayHelper.drawPlaneWithFullBorder(x, y, 126, 10, Color.BLACK.hashCode(), Color.GRAY.hashCode());

		overlayHelper.drawPlaneWithFullBorder(x - 10, y, 8, 10, Color.RED.hashCode(), new Color(0, 0, 0, 0).hashCode());
		overlayHelper.drawPlaneWithFullBorder(x + 128, y, 8, 10, Color.GREEN.hashCode(), new Color(0, 0, 0, 0).hashCode());

		fontRenderer.drawString(current, x + (128 / 2) - (fontRenderer.getStringWidth(current) / 2), y + 1, Color.WHITE.hashCode(), true);
	}

	public float getCurrentValue()
	{
		return currentValue;
	}

	public void setCurrentValue(float value)
	{
		this.currentValue = Helper.keepInBounds(value, minValue, maxValue);
	}
}