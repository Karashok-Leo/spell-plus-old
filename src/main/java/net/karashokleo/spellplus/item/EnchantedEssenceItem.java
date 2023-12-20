package net.karashokleo.spellplus.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.spell_power.api.MagicSchool;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EnchantedEssenceItem extends SpellEssenceItem
{
    private final ExtraModifier extraModifier;

    public EnchantedEssenceItem(Settings settings, int tier, MagicSchool school, ExtraModifier extraModifier)
    {
        super(settings, tier, school);
        this.extraModifier = extraModifier;
    }

    protected boolean applyEffect(PlayerEntity player)
    {
        return extraModifier.applyToStack(player.getOffHandStack());
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context)
    {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(ScreenTexts.EMPTY);
        tooltip.add(Text.translatable("text.spell-plus." + extraModifier.slot.getName()).formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("text.spell-plus.threshold", extraModifier.threshold).formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("text.spell-plus.modifier", extraModifier.threshold).formatted(Formatting.GRAY));

        double d = extraModifier.value;
        double e = extraModifier.operation == EntityAttributeModifier.Operation.MULTIPLY_BASE || extraModifier.operation == EntityAttributeModifier.Operation.MULTIPLY_TOTAL ? d * 100.0 : (extraModifier.attribute.equals(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE) ? d * 10.0 : d);
        if (d > 0.0)
        {
            tooltip.add(Text.translatable("attribute.modifier.plus." + extraModifier.operation.getId(), ItemStack.MODIFIER_FORMAT.format(e), Text.translatable(extraModifier.attribute.getTranslationKey())).formatted(Formatting.BLUE));
        } else if (d < 0.0)
            tooltip.add(Text.translatable("attribute.modifier.take." + extraModifier.operation.getId(), ItemStack.MODIFIER_FORMAT.format(e *= -1.0), Text.translatable(extraModifier.attribute.getTranslationKey())).formatted(Formatting.RED));
    }
}
