package jiraiyah.librarian.items;

import jiraiyah.librarian.Librarian;
import jiraiyah.librarian.references.Names;
import jiraiyah.librarian.references.Reference;
import jiraiyah.librarian.references.VillagerProfessions;
import jiraiyah.librarian.utilities.NBTUtils;
import net.minecraft.block.BlockFence;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.*;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"WeakerAccess", "RedundantArrayCreation", "unchecked"})
public class LivingCage extends Item
{
    //public static final String NBT_ANIMAL = "Animal";
    //public static final String NBT_ANIMAL_DISPLAY = "Animal_Metadata";
    //public static final String NBT_CAGE_PREVENT = "[CagePrevent]";
    public static final List<ItemStack> genericNiceRecipeItemList = new ArrayList();
    public static final List<ItemStack> genericEvilRecipeItemList = new ArrayList();
    //public static final String NBT_ANIMAL_ALREADYPICKEDUP = "CursedLassoPickedUp";
    //public static final String NBT_ANIMAL_NOPLACE = "No_Place";
    //public static final String NBT_ANIMAL_NOPLACE_OLD = "NoPlace";

    public LivingCage()
    {
        setUnlocalizedName(Reference.MOD_ID.toLowerCase() + "." + Names.LIVING_CAGE_NAME);
        setRegistryName(Names.LIVING_CAGE_NAME);
        setCreativeTab(Librarian.CREATIVE_TAB);
        setHasSubtypes(true);
        setMaxStackSize(1);
        MinecraftForge.EVENT_BUS.register(this);
        genericNiceRecipeItemList.add(new ItemStack(this, 1, 0));
        genericEvilRecipeItemList.add(new ItemStack(this, 1, 1));
        addPropertyOverride(new ResourceLocation("livingcage", "full"), (stack, worldIn, entityIn) -> hasAnimal(stack) ? 1 : 0);
    }

    public static ItemStack newCraftingVillagerStack(boolean needsContract, VillagerProfessions profession)
    {
        ItemStack itemStack = newCraftingStack(EntityVillager.class);
        NBTTagCompound tags = NBTUtils.getOrInitTagCompound(itemStack.getTagCompound(), "Animal");

        if (needsContract)
        {
            NBTUtils.getOrInitTagCompound(tags, "ForgeData").setBoolean("Contracted", true);
        }

        if (profession != null)
        {
            tags.setInteger("Profession", profession.profession);
            tags.setInteger("Career", profession.career);
        }

        return itemStack;
    }

    public static ItemStack newCraftingStack(Class entity)
    {
        int meta = IMob.class.isAssignableFrom(entity) ? 1 : 0;
        ItemStack itemStack = new ItemStack(new LivingCage(), 1, meta);//com.rwtema.extrautils2.backend.entries.XU2Entries.goldenLasso.newStack(1, meta);
        NBTTagCompound animalTag = NBTUtils.getOrInitTagCompound(NBTUtils.getOrInitTagCompound(itemStack), "Animal");
        animalTag.setString("id", (String) EntityList.classToStringMapping.get(entity));
        setNoPlace(itemStack);
        return itemStack;
    }

    public static void setNoPlace(ItemStack cursedLasso)
    {
        cursedLasso.getTagCompound().setBoolean("No_Place", true);
    }

    /*@SideOnly(Side.CLIENT)
    public boolean renderAsTool()
    {
        return true;
    }*/

    public int getMaxMetadata()
    {
        return 2;
    }

    @SideOnly(Side.CLIENT)
    public void registerTextures()
    {
        genericNiceRecipeItemList.clear();
        genericEvilRecipeItemList.clear();
        genericNiceRecipeItemList.add(new ItemStack(this, 1, 0));
        genericEvilRecipeItemList.add(new ItemStack(this, 1, 1));
        for (Class<? extends Entity> aClass : EntityList.classToStringMapping.keySet()) {
            if (EntityLiving.class.isAssignableFrom(aClass)) {
                if (IMob.class.isAssignableFrom(aClass))
                {
                    ItemStack itemStack = newCraftingStack(aClass);
                    genericEvilRecipeItemList.add(itemStack);
                }
                else if ((EntitySlime.class.isAssignableFrom(aClass)) || (EntityCreature.class.isAssignableFrom(aClass)) || (EntityAmbientCreature.class.isAssignableFrom(aClass)) || (EntityWaterMob.class.isAssignableFrom(aClass)))
                {
                    ItemStack itemStack = newCraftingStack(aClass);
                    genericNiceRecipeItemList.add(itemStack);
                }
            }
        }
        //Textures.register(new String[] { "dark_lasso", "golden_lasso", "lasso_internal_1", "lasso_internal_2" });
    }

    public boolean hasContainerItem(ItemStack stack)
    {
        return hasAnimal(stack);
    }

    public ItemStack getContainerItem(ItemStack stack)
    {
        ItemStack copy = stack.copy();
        NBTTagCompound tagCompound = copy.getTagCompound();
        tagCompound.removeTag("Animal");
        tagCompound.removeTag("display");
        return copy;
    }

    /*public int getRenderLayers(@Nullable ItemStack itemStack)
    {
        return hasAnimal(itemStack) ? 1 : 0;
    }*/

    public boolean hasEffect(ItemStack stack)
    {
        return false;
    }

    public boolean hasAnimal(ItemStack itemStack)
    {
        NBTTagCompound tagCompound = itemStack.getTagCompound();
        return hasAnimal(tagCompound);
    }

    private boolean hasAnimal(NBTTagCompound tagCompound)
    {
        return (tagCompound != null) && (tagCompound.hasKey("Animal", 10));
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand)
    {
        if (hasAnimal(stack)) {
            return false;
        }
        if (target.worldObj.isRemote) {
            return true;
        }
        if (stack.getItemDamage() == 1)
        {
            /*if (!(target instanceof IMob))
            {
                //playerIn.addChatComponentMessage(Lang.chat("%s is not a hostile mob.", new Object[] { target.getDisplayName() }));
                return false;
            }*/
            if (!playerIn.capabilities.isCreativeMode)
            {
                if ((target instanceof EntityDragon || target instanceof EntityWither))
                {
                    //playerIn.addChatComponentMessage(Lang.chat("%s is too powerful.", new Object[] { target.getDisplayName() }));
                    return false;
                }
                NBTTagCompound data = target.getEntityData();
                /*if (data.getBoolean("CursedLassoPickedUp")) {
                    return true;
                }*/
                /*float health = target.getHealth();
                float maxHealth = target.getMaxHealth();*/

                /*float threshold = MathHelper.clamp_float(maxHealth / 4.0F, 4.0F, 10.0F);
                if (health > threshold)
                {
                    //playerIn.addChatComponentMessage(Lang.chat("%s has too much health (%s hearts). Reduce to %s hearts.", new Object[] { target.getDisplayName(), Integer.valueOf((int)Math.floor(health / 2.0F)), Integer.valueOf((int)Math.floor(threshold / 2.0F)) }));
                    return false;
                }*/
            }
        }
        else
        {
            /*if ((target instanceof IMob))
            {
                //playerIn.addChatComponentMessage(Lang.chat("%s is a hostile mob.", new Object[] { target.getDisplayName() }));
                return false;
            }*/
            if ((!(target instanceof EntitySlime)) && (!(target instanceof EntityCreature)) && (!(target instanceof EntityAmbientCreature)) && (!(target instanceof EntityWaterMob))) {
                return false;
            }
            /*if (((EntityLiving)target).getAttackTarget() != null)
            {
                //playerIn.addChatComponentMessage(Lang.chat("%s is too busy attacking someone.", new Object[] { target.getDisplayName() }));
                return false;
            }*/
        }
        return addTargetToLasso(stack, target);
    }

    public boolean addTargetToLasso(ItemStack stack, EntityLivingBase target)
    {
        if ((target instanceof IMerchant)) {
            target.getDisplayName();
        }
        float health = target.getHealth();
        float maxHealth = target.getMaxHealth();

        NBTTagCompound entityTags = new NBTTagCompound();
        entityTags.setBoolean("[CagePrevent]", false);
        if (!target.writeToNBTOptional(entityTags)) { //.func_98035_c(entityTags)
            return false;
        }
        if ((!entityTags.hasKey("[CagePrevent]")) || (entityTags.getBoolean("[CagePrevent]"))) {
            return false;
        }
        entityTags.removeTag("[CagePrevent]");

        String name = "";
        if (target.hasCustomName()) {
            name = target.getCustomNameTag();
        }
        target.setDead();

        NBTTagCompound nbt = NBTUtils.getOrInitTagCompound(stack);
        nbt.setBoolean("No_Place", false);
        nbt.setTag("Animal", entityTags);
        NBTTagCompound display = NBTUtils.getOrInitTagCompound(nbt, "Animal_Metadata");
        display.setFloat("Health", health);
        display.setFloat("MaxHealth", maxHealth);
        if (!name.equals("")) {
            stack.setStackDisplayName(name);
        }
        return true;
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (!hasAnimal(stack))
        {
            return EnumActionResult.FAIL;
        }
        NBTTagCompound stackTags = stack.getTagCompound();
        NBTTagCompound entityTags = stackTags.getCompoundTag("Animal");
        if ((entityTags.hasNoTags()) || (!entityTags.hasKey("id", 8)))
        {
            stackTags.removeTag("Animal");
            return EnumActionResult.FAIL;
        }
        if (worldIn.isRemote) {
            return EnumActionResult.SUCCESS;
        }
        if (!playerIn.canPlayerEdit(pos.offset(side), side, stack)) {
            return EnumActionResult.FAIL;
        }
        /*if ((stack.getItemDamage() == 1) && (worldIn.getDifficulty() == EnumDifficulty.PEACEFUL))
        {
            //playerIn.addChatComponentMessage(Lang.chat("Difficulty set to peaceful.", new Object[0]));
            return EnumActionResult.FAIL;
        }*/
        if (stackTags.getBoolean("No_Place"))
        {
            if (playerIn.isSneaking())
            {
                stackTags.removeTag("Animal");
                stackTags.removeTag("Animal_Metadata");
                stackTags.removeTag("No_Place");
                //playerIn.addChatComponentMessage(Lang.chat("Mob soul released.", new Object[0]));
                stack.clearCustomName();
                return EnumActionResult.SUCCESS;
            }
            //float health = stackTags.getCompoundTag("Animal_Metadata").getFloat("Health");
            //playerIn.addChatComponentMessage(Lang.chat("Unable to place mob.", new Object[0]));
            //if (health <= 1.0E-10D) {
                //playerIn.addChatComponentMessage(Lang.chat("Mob's body is dead.", new Object[0]));
            //}
            //playerIn.addChatComponentMessage(Lang.chat("Sneak-right-click to release soul.", new Object[0]));
            return EnumActionResult.SUCCESS;
        }
        IBlockState iblockstate = worldIn.getBlockState(pos);

        pos = pos.offset(side);
        double d0 = 0.0D;
        if ((side == EnumFacing.UP) && ((iblockstate.getBlock() instanceof BlockFence))) {
            d0 = 0.5D;
        }
        entityTags.setTag("Pos", newDoubleNBTList(new double[] { pos.getX() + 0.5D, pos.getY() + d0, pos.getZ() + 0.5D }));
        entityTags.setTag("Motion", newDoubleNBTList(new double[] { 0.0D, 0.0D, 0.0D }));
        entityTags.setFloat("FallDistance", 0.0F);
        entityTags.setInteger("Dimension", worldIn.provider.getDimension());

        Entity entity = EntityList.createEntityFromNBT(entityTags, worldIn);
        if (entity != null)
        {
            if (((entity instanceof EntityLiving)) && (stack.hasDisplayName())) {
                entity.setCustomNameTag(stack.getDisplayName());
            }
            /*if ((entity instanceof IMob)) {
                entity.getEntityData().setBoolean("CursedLassoPickedUp", true);
            }*/
            worldIn.spawnEntityInWorld(entity);
        }
        stackTags.removeTag("Animal");
        stackTags.removeTag("Animal_Metadata");
        stackTags.removeTag("No_Place");
        stack.clearCustomName();
        if (playerIn.capabilities.isCreativeMode) {
            playerIn.replaceItemInInventory(0, stack.copy());
        }
        playerIn.inventory.markDirty();

        return EnumActionResult.SUCCESS;
    }

    private NBTTagList newDoubleNBTList(double... numbers)
    {
        NBTTagList nbttaglist = new NBTTagList();
        for (double d0 : numbers) {
            nbttaglist.appendTag(new NBTTagDouble(d0));
        }
        return nbttaglist;
    }
//https://github.com/maruohon/enderutilities/blob/master/src/main/java/fi/dy/masa/enderutilities/item/ItemLivingManipulator.java#L89
    /*@SideOnly(Side.CLIENT)
    public EntityList.EntityEggInfo getEgg(NBTTagCompound entityTag)
    {
        String id = entityTag.getString("id");
        Map<String, EntityList.EntityEggInfo> eggs = EntityList.entityEggs;
        if (eggs.containsKey(id)) {
            return (EntityList.EntityEggInfo)eggs.get(id);
        }
        int i = EntityList.getIDFromString(id);
        if (EntityList.entityEggs.containsKey(Integer.valueOf(i))) {
            return (EntityList.EntityEggInfo)EntityList.entityEggs.get(Integer.valueOf(i));
        }
        return null;
    }*/

    /*public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
        NBTTagCompound itemTags = stack.getTagCompound();
        if (hasAnimal(itemTags))
        {
            NBTTagCompound entityTags = itemTags.getCompoundTag("Animal");
            if (entityTags.hasKey("id"))
            {
                if (!tooltip.isEmpty()) {
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
                float health = itemTags.getCompoundTag("Animal_Metadata").getFloat("Health");
                float maxHealth = itemTags.getCompoundTag("Animal_Metadata").getFloat("MaxHealth");
                tooltip.add(Lang.translateArgs("Health: %s/%s", new Object[] { Float.valueOf(health), Float.valueOf(maxHealth) }));
                if (stack.hasDisplayName()) {
                    tooltip.add(stack.getDisplayName());
                }
            }
        }
    }*/

    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
    {
        subItems.add(new ItemStack(itemIn, 1, 0));
        subItems.add(new ItemStack(itemIn, 1, 1));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
    {
        return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
    }
}
