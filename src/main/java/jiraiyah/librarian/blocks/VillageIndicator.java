package jiraiyah.librarian.blocks;

import jiraiyah.librarian.Librarian;
import jiraiyah.librarian.references.Names;
import jiraiyah.librarian.references.Reference;
import jiraiyah.librarian.tileEntities.VillageIndicatorTile;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class VillageIndicator extends Block implements ITileEntityProvider
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
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new VillageIndicatorTile();
    }
}
