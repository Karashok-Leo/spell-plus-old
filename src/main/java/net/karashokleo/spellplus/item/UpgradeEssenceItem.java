package net.karashokleo.spellplus.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import net.spell_power.api.MagicSchool;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class UpgradeEssenceItem extends SpellEssenceItem
{
    private final Item originItem;
    private final Item upgradeItem;
    private final String tagNeeded;

    public UpgradeEssenceItem(Settings settings, int tier, MagicSchool school, Item originItem, Item upgradeItem, String tagNeeded)
    {
        super(settings, tier, school);
        this.originItem = originItem;
        this.upgradeItem = upgradeItem;
        this.tagNeeded = tagNeeded;
    }

    @Override
    protected boolean applyEffect(PlayerEntity player)
    {
        ItemStack stack = player.getOffHandStack();
        if (player.getCommandTags().contains(tagNeeded) && stack.isOf(originItem))
        {
            stack.decrement(1);
            player.giveItemStack(upgradeItem.getDefaultStack());
            return true;
        }
        return false;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context)
    {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(Text.translatable("text.spell-plus.upgrade", originItem.getName(), upgradeItem.getName()));
    }
}
