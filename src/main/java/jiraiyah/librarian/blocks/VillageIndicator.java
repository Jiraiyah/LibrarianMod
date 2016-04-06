package jiraiyah.librarian.blocks;

import jiraiyah.librarian.Librarian;
import jiraiyah.librarian.references.Names;
import jiraiyah.librarian.references.Reference;
import jiraiyah.librarian.tileEntities.VillageIndicatorTile;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;

import static net.minecraft.util.BlockRenderLayer.CUTOUT;

public class VillageIndicator extends Block
{
    public VillageIndicator()
    {
        this(Material.wood);
        setUnlocalizedName(Reference.MOD_ID.toLowerCase() + "." + Names.VILLAGE_INDICATOR_NAME);
        setRegistryName(Names.VILLAGE_INDICATOR_NAME);
        setHardness(5);
        setResistance(10);
        blockSoundType = SoundType.WOOD;
        setCreativeTab(Librarian.CREATIVE_TAB);
    }

    public VillageIndicator(Material materialIn)
    {
        super(materialIn);
    }

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

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new VillageIndicatorTile();
    }
}
