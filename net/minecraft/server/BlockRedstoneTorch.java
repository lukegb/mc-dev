package net.minecraft.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BlockRedstoneTorch extends BlockTorch {

    private boolean isOn;
    private static Map b = new HashMap();

    private boolean a(World world, int i, int j, int k, boolean flag) {
        if (!b.containsKey(world)) {
            b.put(world, new ArrayList());
        }

        List list = (List) b.get(world);

        if (flag) {
            list.add(new RedstoneUpdateInfo(i, j, k, world.getTime()));
        }

        int l = 0;

        for (int i1 = 0; i1 < list.size(); ++i1) {
            RedstoneUpdateInfo redstoneupdateinfo = (RedstoneUpdateInfo) list.get(i1);

            if (redstoneupdateinfo.a == i && redstoneupdateinfo.b == j && redstoneupdateinfo.c == k) {
                ++l;
                if (l >= 8) {
                    return true;
                }
            }
        }

        return false;
    }

    protected BlockRedstoneTorch(int i, boolean flag) {
        super(i);
        this.isOn = flag;
        this.b(true);
        this.a((CreativeModeTab) null);
    }

    public int a(World world) {
        return 2;
    }

    public void onPlace(World world, int i, int j, int k) {
        if (world.getData(i, j, k) == 0) {
            super.onPlace(world, i, j, k);
        }

        if (this.isOn) {
            world.applyPhysics(i, j - 1, k, this.id);
            world.applyPhysics(i, j + 1, k, this.id);
            world.applyPhysics(i - 1, j, k, this.id);
            world.applyPhysics(i + 1, j, k, this.id);
            world.applyPhysics(i, j, k - 1, this.id);
            world.applyPhysics(i, j, k + 1, this.id);
        }
    }

    public void remove(World world, int i, int j, int k, int l, int i1) {
        if (this.isOn) {
            world.applyPhysics(i, j - 1, k, this.id);
            world.applyPhysics(i, j + 1, k, this.id);
            world.applyPhysics(i - 1, j, k, this.id);
            world.applyPhysics(i + 1, j, k, this.id);
            world.applyPhysics(i, j, k - 1, this.id);
            world.applyPhysics(i, j, k + 1, this.id);
        }
    }

    public int b(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        if (!this.isOn) {
            return 0;
        } else {
            int i1 = iblockaccess.getData(i, j, k);

            return i1 == 5 && l == 1 ? 0 : (i1 == 3 && l == 3 ? 0 : (i1 == 4 && l == 2 ? 0 : (i1 == 1 && l == 5 ? 0 : (i1 == 2 && l == 4 ? 0 : 15))));
        }
    }

    private boolean m(World world, int i, int j, int k) {
        int l = world.getData(i, j, k);

        return l == 5 && world.isBlockFacePowered(i, j - 1, k, 0) ? true : (l == 3 && world.isBlockFacePowered(i, j, k - 1, 2) ? true : (l == 4 && world.isBlockFacePowered(i, j, k + 1, 3) ? true : (l == 1 && world.isBlockFacePowered(i - 1, j, k, 4) ? true : l == 2 && world.isBlockFacePowered(i + 1, j, k, 5))));
    }

    public void a(World world, int i, int j, int k, Random random) {
        boolean flag = this.m(world, i, j, k);
        List list = (List) b.get(world);

        while (list != null && !list.isEmpty() && world.getTime() - ((RedstoneUpdateInfo) list.get(0)).d > 60L) {
            list.remove(0);
        }

        if (this.isOn) {
            if (flag) {
                world.setTypeIdAndData(i, j, k, Block.REDSTONE_TORCH_OFF.id, world.getData(i, j, k), 3);
                if (this.a(world, i, j, k, true)) {
                    world.makeSound((double) ((float) i + 0.5F), (double) ((float) j + 0.5F), (double) ((float) k + 0.5F), "random.fizz", 0.5F, 2.6F + (world.random.nextFloat() - world.random.nextFloat()) * 0.8F);

                    for (int l = 0; l < 5; ++l) {
                        double d0 = (double) i + random.nextDouble() * 0.6D + 0.2D;
                        double d1 = (double) j + random.nextDouble() * 0.6D + 0.2D;
                        double d2 = (double) k + random.nextDouble() * 0.6D + 0.2D;

                        world.addParticle("smoke", d0, d1, d2, 0.0D, 0.0D, 0.0D);
                    }
                }
            }
        } else if (!flag && !this.a(world, i, j, k, false)) {
            world.setTypeIdAndData(i, j, k, Block.REDSTONE_TORCH_ON.id, world.getData(i, j, k), 3);
        }
    }

    public void doPhysics(World world, int i, int j, int k, int l) {
        if (!this.d(world, i, j, k, l)) {
            boolean flag = this.m(world, i, j, k);

            if (this.isOn && flag || !this.isOn && !flag) {
                world.a(i, j, k, this.id, this.a(world));
            }
        }
    }

    public int c(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        return l == 0 ? this.b(iblockaccess, i, j, k, l) : 0;
    }

    public int getDropType(int i, Random random, int j) {
        return Block.REDSTONE_TORCH_ON.id;
    }

    public boolean isPowerSource() {
        return true;
    }

    public boolean i(int i) {
        return i == Block.REDSTONE_TORCH_OFF.id || i == Block.REDSTONE_TORCH_ON.id;
    }
}
