package jiraiyah.librarian.blocks;

import jiraiyah.librarian.Librarian;
import jiraiyah.librarian.renderers.Reference;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class ChunkLoader extends BlockContainer
{

    public ChunkLoader()
    {
        this(Material.rock);
        setUnlocalizedName(Reference.MOD_ID.toLowerCase() + "." + "chunkloader");
        setRegistryName("chunkloader");
        setHardness(20);
        setResistance(200);
        setStepSound(SoundType.STONE);
        setCreativeTab(Librarian.CREATIVE_TAB);
    }

    protected ChunkLoader(Material materialIn)
    {
        super(materialIn);
    }

    @Override
    public boolean isNormalCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isFullyOpaque(IBlockState state)
    {
        return false;
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return super.damageDropped(state);
    }

    @Override
    public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side)
    {
        return side == EnumFacing.DOWN;
    }

    @Override
    public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
    {
        return true;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if(playerIn.isSneaking())
            return false;
        if (!worldIn.isRemote)
        {
            // TODO : add code
        }
        return true;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        if (worldIn.isRemote)
            return;
        // TODO : add code
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return null;
    }
}
