package jiraiyah.librarian.items;

import jiraiyah.librarian.Librarian;
import jiraiyah.librarian.references.Names;
import jiraiyah.librarian.references.Reference;
import jiraiyah.librarian.utilities.Lang;
import jiraiyah.librarian.utilities.MCTimer;
import jiraiyah.librarian.utilities.NBTUtils;
import net.minecraft.block.BlockFence;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.*;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.List;

import static jiraiyah.librarian.references.Names.NBT.*;

@SuppressWarnings({"WeakerAccess", "RedundantArrayCreation", "unchecked"})
public class LivingCage extends Item
{
    public static final List<ItemStack> genericNiceRecipeItemList = new ArrayList();
    public static final List<ItemStack> genericEvilRecipeItemList = new ArrayList();

    public LivingCage()
    {
        setUnlocalizedName(Reference.MOD_ID.toLowerCase() + "." + Names.LIVING_CAGE_NAME);
        setRegistryName(Names.LIVING_CAGE_NAME);
        setHasSubtypes(true);
        setMaxStackSize(1);
        MinecraftForge.EVENT_BUS.register(this);
        genericNiceRecipeItemList.add(new ItemStack(this, 1, 0));
        genericEvilRecipeItemList.add(new ItemStack(this, 1, 1));
        addPropertyOverride(new ResourceLocation("livingcage", "full"), (stack, worldIn, entityIn) -> hasEntity(stack) ? 1 : 0);
        setCreativeTab(Librarian.CREATIVE_TAB);
    }

    public boolean hasContainerItem(ItemStack stack)
    {
        return hasEntity(stack);
    }

    public ItemStack getContainerItem(ItemStack stack)
    {
        ItemStack copy = stack.copy();
        NBTTagCompound tagCompound = copy.getTagCompound();
        tagCompound.removeTag(THE_ENTITY_TAG);
        tagCompound.removeTag("display");
        return copy;
    }

    public boolean hasEntity(ItemStack itemStack)
    {
        NBTTagCompound tagCompound = itemStack.getTagCompound();
        return hasEntity(tagCompound);
    }

    private boolean hasEntity(NBTTagCompound tagCompound)
    {
        return (tagCompound != null) && (tagCompound.hasKey(THE_ENTITY_TAG, 10));
    }

    public boolean addTargetToLasso(ItemStack stack, EntityLivingBase target, EntityPlayer playerIn)
    {
        if ((target instanceof IMerchant)) {
            target.getDisplayName();
        }
        float health = target.getHealth();
        float maxHealth = target.getMaxHealth();

        NBTTagCompound entityTags = new NBTTagCompound();
        entityTags.setBoolean(IMPOSSIBLE_TAG, false);
        if (!target.writeToNBTOptional(entityTags)) {
            return false;
        }
        if ((!entityTags.hasKey(IMPOSSIBLE_TAG)) || (entityTags.getBoolean(IMPOSSIBLE_TAG))) {
            return false;
        }
        entityTags.removeTag(IMPOSSIBLE_TAG);

        String name = "";
        if (target.hasCustomName()) {
            name = target.getCustomNameTag();
        }
        target.setDead();

        NBTTagCompound nbt = NBTUtils.getOrInitTagCompound(stack);
        nbt.setBoolean(HAS_ENTITY_TAG, false);
        nbt.setTag(THE_ENTITY_TAG, entityTags);
        NBTTagCompound display = NBTUtils.getOrInitTagCompound(nbt, ENTITY_META_TAG);
        display.setFloat(HEALTH_TAG, health);
        display.setFloat(MAX_HEALTH_TAG, maxHealth);
        if (!name.equals("")) {
            stack.setStackDisplayName(name);
        }
        if (playerIn.capabilities.isCreativeMode)
        {
            playerIn.inventory.addItemStackToInventory(stack);
        }
        return true;
    }

    private NBTTagList newDoubleNBTList(double... numbers)
    {
        NBTTagList nbttaglist = new NBTTagList();
        for (double d0 : numbers) {
            nbttaglist.appendTag(new NBTTagDouble(d0));
        }
        return nbttaglist;
    }

    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
    {
        subItems.add(new ItemStack(itemIn, 1, 0));
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advancedTooltips)
    {
        NBTTagCompound itemTags = stack.getTagCompound();
        if (hasEntity(itemTags))
        {
            NBTTagCompound entityTags = itemTags.getCompoundTag(THE_ENTITY_TAG);
            if (entityTags.hasKey("id"))
            {
                if (!tooltip.isEmpty())
                {
                    tooltip.set(0, (tooltip.get(0)).replaceFirst(TextFormatting.ITALIC + stack.getDisplayName() + TextFormatting.RESET, getItemStackDisplayName(stack)));
                }
                String id = entityTags.getString("id");
                String animal_name = I18n.translateToLocal("entity." + id + ".name");
                tooltip.add(animal_name);
                if ("Villager".equals(id))
                {
                    if (entityTags.hasKey("Profession"))
                    {
                        int career = entityTags.getInteger("Career");
                        int profession = entityTags.getInteger("Profession");

                        int t = MCTimer.clientTimer >> 4;

                        String s1 = null;
                        switch (profession)
                        {
                            case 0:
                                if (career == 0) {
                                    career = t % 4 + 1;
                                }
                                if (career == 1) {
                                    s1 = "farmer";
                                } else if (career == 2) {
                                    s1 = "fisherman";
                                } else if (career == 3) {
                                    s1 = "shepherd";
                                } else if (career == 4) {
                                    s1 = "fletcher";
                                }
                                break;
                            case 1:
                                s1 = "librarian";
                                break;
                            case 2:
                                s1 = "cleric";
                                break;
                            case 3:
                                if (career == 0) {
                                    career = t % 3 + 1;
                                }
                                if (career == 1) {
                                    s1 = "armor";
                                } else if (career == 2) {
                                    s1 = "weapon";
                                } else if (career == 3) {
                                    s1 = "tool";
                                }
                                break;
                            case 4:
                                if (career == 0) {
                                    career = t % 2 + 1;
                                }
                                if (career == 1) {
                                    s1 = "butcher";
                                } else if (career == 2) {
                                    s1 = "leather";
                                }
                                break;
                        }
                        if (s1 != null) {
                            tooltip.add(I18n.translateToLocal("entity.Villager." + s1));
                        }
                    }
                    if (entityTags.getCompoundTag("ForgeData").getBoolean("Contracted")) {
                        tooltip.add(Lang.translate("*Under Contract*"));
                    }
                }
                float health = itemTags.getCompoundTag(ENTITY_META_TAG).getFloat(HEALTH_TAG);
                float maxHealth = itemTags.getCompoundTag(ENTITY_META_TAG).getFloat(MAX_HEALTH_TAG);
                tooltip.add(Lang.translateArgs("Health: %s/%s", new Object[] { Float.valueOf(health), Float.valueOf(maxHealth) }));
                if (stack.hasDisplayName()) {
                    tooltip.add(stack.getDisplayName());
                }
            }
        }

    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        /*

        to get the village !
        worldIn.getVillageCollection().getVillageList(); <--- returns the villages in this world
        then the line bellow is like linq in c#
        List<Person> persons = …
        Stream<Person> personsOver18 = persons.stream().filter(p -> p.getAge() > 18);

        float ps = pos.getX();
        Village thisV;
        Optional villages = worldIn.getVillageCollection().getVillageList().stream().filter(v -> ps < v.getCenter().getX() + v.getVillageRadius()).findFirst();
        if (villages.isPresent())
            thisV = (Village) villages.get();

        and now we can get access to all the info in that village keep in mind we need to check z coord too !
        */

        if (!hasEntity(stack))
        {
            return EnumActionResult.FAIL;
        }
        NBTTagCompound stackTags = stack.getTagCompound();
        NBTTagCompound entityTags = stackTags.getCompoundTag(THE_ENTITY_TAG);
        if ((entityTags.hasNoTags()) || (!entityTags.hasKey("id", 8)))
        {
            stackTags.removeTag(THE_ENTITY_TAG);
            return EnumActionResult.FAIL;
        }
        if (worldIn.isRemote) {
            return EnumActionResult.SUCCESS;
        }
        if (!playerIn.canPlayerEdit(pos.offset(side), side, stack)) {
            return EnumActionResult.FAIL;
        }
        if (stackTags.getBoolean(HAS_ENTITY_TAG))
        {
            if (playerIn.isSneaking())
            {
                stackTags.removeTag(THE_ENTITY_TAG);
                stackTags.removeTag(ENTITY_META_TAG);
                stackTags.removeTag(HAS_ENTITY_TAG);
                stack.clearCustomName();
                return EnumActionResult.SUCCESS;
            }
            return EnumActionResult.SUCCESS;
        }
        IBlockState iblockstate = worldIn.getBlockState(pos);

        pos = pos.offset(side);
        double d0 = 0.0D;
        if ((side == EnumFacing.UP) && ((iblockstate.getBlock() instanceof BlockFence))) {
            d0 = 0.5D;
        }
        entityTags.setTag(POSITION_TAG, newDoubleNBTList(new double[] { pos.getX() + 0.5D, pos.getY() + d0, pos.getZ() + 0.5D }));
        entityTags.setTag(MOTION_TAG, newDoubleNBTList(new double[] { 0.0D, 0.0D, 0.0D }));
        entityTags.setFloat(FALL_DISTANCE_TAG, 0.0F);
        entityTags.setInteger(DIMENTION_TAG, worldIn.provider.getDimension());

        Entity entity = EntityList.createEntityFromNBT(entityTags, worldIn);
        if (entity != null)
        {
            if (((entity instanceof EntityLiving)) && (stack.hasDisplayName())) {
                entity.setCustomNameTag(stack.getDisplayName());
            }
            worldIn.spawnEntityInWorld(entity);
        }
        stackTags.removeTag(THE_ENTITY_TAG);
        stackTags.removeTag(ENTITY_META_TAG);
        stackTags.removeTag(HAS_ENTITY_TAG);
        stack.clearCustomName();
        if (playerIn.capabilities.isCreativeMode)
        {
            playerIn.inventory.removeStackFromSlot(playerIn.inventory.getSlotFor(stack));
        }
        playerIn.inventory.markDirty();

        return EnumActionResult.SUCCESS;
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand)
    {
        if (hasEntity(stack)) {
            return false;
        }
        if (target.worldObj.isRemote) {
            return true;
        }
        if (stack.getItemDamage() == 1)
        {
            if (!playerIn.capabilities.isCreativeMode)
            {
                if ((target instanceof EntityDragon || target instanceof EntityWither))
                {
                    return false;
                }
            }
        }
        else
        {
            if ((!(target instanceof EntitySlime)) &&
                    (!(target instanceof EntityCreature)) &&
                    (!(target instanceof EntityAmbientCreature)) &&
                    (!(target instanceof EntityWaterMob))) {
                return false;
            }
        }
        return addTargetToLasso(stack, target, playerIn);
    }
}
