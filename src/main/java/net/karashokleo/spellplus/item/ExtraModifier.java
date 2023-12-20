package net.karashokleo.spellplus.item;

import com.google.common.collect.Multimap;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class ExtraModifier
{
    private static final String name = "ExtraModifier";

    public final double threshold;
    public final EquipmentSlot slot;
    public final EntityAttribute attribute;
    public final double value;
    public final EntityAttributeModifier.Operation operation;

    public ExtraModifier(double threshold, EquipmentSlot slot, EntityAttribute attribute)
    {
        this(threshold, slot, attribute, 1.0);
    }

    public ExtraModifier(double threshold, EquipmentSlot slot, EntityAttribute attribute, double value)
    {
        this(threshold, slot, attribute, value, EntityAttributeModifier.Operation.ADDITION);
    }

    public ExtraModifier(double threshold, EquipmentSlot slot, EntityAttribute attribute, double value, EntityAttributeModifier.Operation operation)
    {
        this.threshold = threshold;
        this.slot = slot;
        this.attribute = attribute;
        this.value = value;
        this.operation = operation;
    }

    private String getAttributeId()
    {
        return Registries.ATTRIBUTE.getId(attribute).toString();
    }

    public boolean applyToStack(ItemStack stack)
    {
        if (stack.isEmpty()) return false;
        NbtCompound nbt = stack.getOrCreateNbt();
        if (!nbt.contains("ExtraModifiers"))
            nbt.put("ExtraModifiers", new NbtCompound());
        NbtCompound extraModifiers = nbt.getCompound("ExtraModifiers");
        if (!extraModifiers.contains("Level"))
            extraModifiers.putInt("Level", 0);
        int level = extraModifiers.getInt("Level");
        if (level >= threshold) return false;
        extraModifiers.putInt("Level", level + 1);
        if (!extraModifiers.contains("AttributeModifiers", NbtElement.LIST_TYPE))
            extraModifiers.put("AttributeModifiers", new NbtList());
        NbtList nbtList = extraModifiers.getList("AttributeModifiers", NbtElement.COMPOUND_TYPE);
        EntityAttributeModifier modifier = new EntityAttributeModifier(name, value, operation);
        NbtCompound nbtCompound = modifier.toNbt();
        nbtCompound.putString("AttributeName", getAttributeId());
        if (slot != null)
            nbtCompound.putString("Slot", slot.getName());
        nbtList.add(nbtCompound);
        return true;
    }

    public static void addModifiers(ItemStack stack, EquipmentSlot slot, Multimap<EntityAttribute, EntityAttributeModifier> modifiers)
    {
        NbtCompound nbt = stack.getOrCreateNbt();
        if (!nbt.contains("ExtraModifiers"))
            return;
        NbtCompound extraModifiers = nbt.getCompound("ExtraModifiers");
        if (!extraModifiers.contains("AttributeModifiers", NbtElement.LIST_TYPE))
            return;
        NbtList nbtList = extraModifiers.getList("AttributeModifiers", NbtElement.COMPOUND_TYPE);
        for (int i = 0; i < nbtList.size(); ++i)
        {
            EntityAttributeModifier entityAttributeModifier;
            Optional<EntityAttribute> optional;
            NbtCompound nbtCompound = nbtList.getCompound(i);
            if (
                    nbtCompound.contains("Slot", NbtElement.STRING_TYPE) &&
                            !nbtCompound.getString("Slot").equals(slot.getName()) ||
                            (optional = Registries.ATTRIBUTE.getOrEmpty(Identifier.tryParse(nbtCompound.getString("AttributeName")))).isEmpty() ||
                            (entityAttributeModifier = EntityAttributeModifier.fromNbt(nbtCompound)) == null ||
                            entityAttributeModifier.getId().getLeastSignificantBits() == 0L ||
                            entityAttributeModifier.getId().getMostSignificantBits() == 0L
            ) continue;
            modifiers.put(optional.get(), entityAttributeModifier);
        }
    }
}
