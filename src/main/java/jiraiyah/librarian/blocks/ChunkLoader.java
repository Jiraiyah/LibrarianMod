package jiraiyah.librarian.blocks;

import jiraiyah.librarian.Librarian;
import jiraiyah.librarian.events.ChunkLoaderManager;
import jiraiyah.librarian.infrastructure.PacketCustom;
import jiraiyah.librarian.network.ChunkLoaderSPH;
import jiraiyah.librarian.references.Names;
import jiraiyah.librarian.references.Reference;
import jiraiyah.librarian.tileEntities.ChunkLoaderBaseTile;
import jiraiyah.librarian.tileEntities.ChunkLoaderTile;
import jiraiyah.librarian.utilities.ServerUtils;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import static net.minecraft.util.BlockRenderLayer.CUTOUT;

public class ChunkLoader extends Block
{

    //region cTor
    public ChunkLoader()
    {
        this(Material.ROCK);
        setUnlocalizedName(Reference.MOD_ID.toLowerCase() + "." + Names.CHUNK_LOADER_NAME);
        setRegistryName(Names.CHUNK_LOADER_NAME);
        setHardness(20);
        setResistance(100);
        blockSoundType = SoundType.STONE;
        setCreativeTab(Librarian.CREATIVE_TAB);
    }

    protected ChunkLoader(Material materialIn)
    {
        super(materialIn);
    }
    //endregion

    //region Block Rendering
    @Override
    public boolean isNormalCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public BlockRenderLayer getBlockLayer()
    {
        return CUTOUT;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    //endregion

    @Override
    public int damageDropped(IBlockState state)
    {
        return super.damageDropped(state);
    }

    @Override
    public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side)
    {
        return true;
        //return side == EnumFacing.DOWN;
    }

    @Override
    public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
    {
        return true;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote && playerIn.isSneaking())
        {
            return false;
        }
        ChunkLoaderTile tile = (ChunkLoaderTile) worldIn.getTileEntity(pos);
        if (tile.owner == null || tile.owner.equals(playerIn.getName()) ||
                ChunkLoaderManager.opInteract() && ServerUtils.isPlayerOP(playerIn.getName()))
        {
            PacketCustom packet = new PacketCustom(ChunkLoaderSPH.channel, 12);
            packet.writeCoord(pos);
            packet.sendToPlayer(playerIn);
        }
        else
        {
            playerIn.addChatMessage(new TextComponentTranslation("librarian.accessdenied"));
        }
        return true;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        if (worldIn.isRemote)
            return;
        ChunkLoaderBaseTile ctile = (ChunkLoaderBaseTile) worldIn.getTileEntity(pos);
        ctile.onBlockPlacedBy(placer);
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new ChunkLoaderTile();
    }
}
