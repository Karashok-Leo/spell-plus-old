package net.karashokleo.spellplus.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import net.spell_engine.particle.Particles;
import net.spell_power.api.MagicSchool;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public abstract class SpellEssenceItem extends Item
{
    private static final Map<MagicSchool, ParticleEffect> map = Map.of(
            MagicSchool.ARCANE, Particles.arcane_spell.particleType,
            MagicSchool.FIRE, ParticleTypes.FLAME,
            MagicSchool.FROST, Particles.snowflake.particleType,
            MagicSchool.HEALING, Particles.healing_ascend.particleType,
            MagicSchool.LIGHTNING, ParticleTypes.SMOKE,
            MagicSchool.SOUL, ParticleTypes.SOUL_FIRE_FLAME);
    protected int tier;
    protected MagicSchool school;

    public SpellEssenceItem(Settings settings, int tier, MagicSchool school)
    {
        super(settings);
        this.tier = tier;
        this.school = school;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand)
    {
        ItemStack stack = user.getStackInHand(hand);
        if (world.isClient() || hand == Hand.OFF_HAND || user.getOffHandStack().isEmpty())
            return TypedActionResult.fail(stack);
        user.setCurrentHand(hand);
        return TypedActionResult.consume(stack);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks)
    {
        if (!world.isClient()) return;
        if (remainingUseTicks % 8 != 0) return;
        for (int i = 0; i < remainingUseTicks; i++)
        {
            double radian = Math.toRadians(i * 360.0 / remainingUseTicks);
            double x = user.getX() + Math.cos(radian) * remainingUseTicks / 16;
            double z = user.getZ() + Math.sin(radian) * remainingUseTicks / 16;
            world.addParticle(ParticleTypes.END_ROD, x, user.getY() + 1, z, 0.0, 0.0, 0.0);
        }
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user)
    {
        if (user instanceof PlayerEntity player)
            if (applyEffect(player))
            {
                for (int i = 0; i < (tier + 1) * 30; i++)
                {
                    double radian = Math.toRadians(i * 360.0 / (tier + 1) / 30);
                    double x = Math.cos(radian) / 5;
                    double z = Math.sin(radian) / 5;
                    world.addParticle(map.get(school), player.getX(), player.getY() + 1, player.getZ(), x, 0.05, z);
                }
                player.sendMessage(Text.translatable("text.spell-plus.success"), true);
                if (!player.isCreative())
                    stack.decrement(1);
            } else player.sendMessage(Text.translatable("text.spell-plus.failure"), true);
        return stack;
    }

    protected abstract boolean applyEffect(PlayerEntity player);

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context)
    {
        tooltip.add(Text.translatable("text.spell-plus.use"));
        tooltip.add(Text.translatable("text.spell-plus.effect"));
    }

    @Override
    public int getMaxUseTime(ItemStack stack)
    {
        return 32;
    }

    @Override
    public UseAction getUseAction(ItemStack stack)
    {
        return UseAction.BOW;
    }
}
