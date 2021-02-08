package plusm.vilonix.gapi.utils.core;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

import java.util.EnumMap;

public class FaceUtil {
    public static final BlockFace[] AXIS = new BlockFace[4];
    public static final BlockFace[] RADIAL = new BlockFace[]{BlockFace.WEST, BlockFace.NORTH_WEST, BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.EAST, BlockFace.SOUTH_EAST, BlockFace.SOUTH, BlockFace.SOUTH_WEST};
    public static final BlockFace[] CARDINAL = new BlockFace[]{BlockFace.WEST, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH};
    public static final BlockFace[] BLOCK_SIDES = new BlockFace[]{BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.UP, BlockFace.DOWN};
    public static final BlockFace[] ATTACHEDFACES = new BlockFace[]{BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.UP};
    public static final BlockFace[] ATTACHEDFACESDOWN = FaceUtil.BLOCK_SIDES;
    private static final EnumMap<BlockFace, Integer> notches = new EnumMap<>(BlockFace.class);
    private static final EnumMap<BlockFace, Integer> faces = new EnumMap<>(BlockFace.class);

    static {
        for (int i = 0; i < FaceUtil.RADIAL.length; ++i) {
            FaceUtil.notches.put(FaceUtil.RADIAL[i], i);
        }
        for (int i = 0; i < FaceUtil.CARDINAL.length; ++i) {
            FaceUtil.faces.put(FaceUtil.CARDINAL[i], i);
        }
        for (int i = 0; i < FaceUtil.AXIS.length; ++i) {
            FaceUtil.AXIS[i] = FaceUtil.RADIAL[i << 1];
        }
    }

    public static boolean isAlongX(final BlockFace face) {
        return face.getModX() != 0 && face.getModZ() == 0;
    }

    public static boolean isAlongY(final BlockFace face) {
        return isVertical(face);
    }

    public static boolean isAlongZ(final BlockFace face) {
        return face.getModZ() != 0 && face.getModX() == 0;
    }

    public static BlockFace notchToFace(final int notch) {
        return FaceUtil.RADIAL[notch & 0x7];
    }

    public static BlockFace rotate(final BlockFace from, final int notchCount) {
        return notchToFace(faceToNotch(from) + notchCount);
    }

    public static BlockFace combine(final BlockFace from, final BlockFace to) {
        if (from == BlockFace.NORTH) {
            if (to == BlockFace.WEST) {
                return BlockFace.NORTH_WEST;
            }
            if (to == BlockFace.EAST) {
                return BlockFace.NORTH_EAST;
            }
        } else if (from == BlockFace.EAST) {
            if (to == BlockFace.NORTH) {
                return BlockFace.NORTH_EAST;
            }
            if (to == BlockFace.SOUTH) {
                return BlockFace.SOUTH_EAST;
            }
        } else if (from == BlockFace.SOUTH) {
            if (to == BlockFace.WEST) {
                return BlockFace.SOUTH_WEST;
            }
            if (to == BlockFace.EAST) {
                return BlockFace.SOUTH_EAST;
            }
        } else if (from == BlockFace.WEST) {
            if (to == BlockFace.NORTH) {
                return BlockFace.NORTH_WEST;
            }
            if (to == BlockFace.SOUTH) {
                return BlockFace.SOUTH_WEST;
            }
        }
        return from;
    }

    public static BlockFace subtract(final BlockFace face1, final BlockFace face2) {
        return notchToFace(faceToNotch(face1) - faceToNotch(face2));
    }

    public static BlockFace add(final BlockFace face1, final BlockFace face2) {
        return notchToFace(faceToNotch(face1) + faceToNotch(face2));
    }

    public static BlockFace[] getFaces(final BlockFace main) {
        switch (main) {
            case SOUTH_EAST: {
                return new BlockFace[]{BlockFace.SOUTH, BlockFace.EAST};
            }
            case SOUTH_WEST: {
                return new BlockFace[]{BlockFace.SOUTH, BlockFace.WEST};
            }
            case NORTH_EAST: {
                return new BlockFace[]{BlockFace.NORTH, BlockFace.EAST};
            }
            case NORTH_WEST: {
                return new BlockFace[]{BlockFace.NORTH, BlockFace.WEST};
            }
            default: {
                return new BlockFace[]{main, main.getOppositeFace()};
            }
        }
    }

    public static BlockFace getRailsCartDirection(final BlockFace raildirection) {
        switch (raildirection) {
            case SOUTH_WEST:
            case NORTH_EAST: {
                return BlockFace.NORTH_WEST;
            }
            case SOUTH_EAST:
            case NORTH_WEST: {
                return BlockFace.SOUTH_WEST;
            }
            default: {
                return raildirection;
            }
        }
    }

    public static BlockFace toRailsDirection(final BlockFace direction) {
        switch (direction) {
            case NORTH: {
                return BlockFace.SOUTH;
            }
            case WEST: {
                return BlockFace.EAST;
            }
            default: {
                return direction;
            }
        }
    }

    public static boolean isSubCardinal(final BlockFace face) {
        switch (face) {
            case SOUTH_EAST:
            case SOUTH_WEST:
            case NORTH_EAST:
            case NORTH_WEST: {
                return true;
            }
            default: {
                return false;
            }
        }
    }

    public static boolean isVertical(final BlockFace face) {
        return face == BlockFace.UP || face == BlockFace.DOWN;
    }

    public static BlockFace getVertical(final boolean up) {
        return up ? BlockFace.UP : BlockFace.DOWN;
    }

    public static BlockFace getVertical(final double dy) {
        return getVertical(dy >= 0.0);
    }

    public static boolean hasSubDifference(final BlockFace face1, final BlockFace face2) {
        return getFaceYawDifference(face1, face2) <= 45;
    }

    public static Vector faceToVector(final BlockFace face, final double length) {
        return faceToVector(face).multiply(length);
    }

    public static Vector faceToVector(final BlockFace face) {
        return new Vector(face.getModX(), face.getModY(), face.getModZ());
    }

    public static BlockFace getDirection(final Location from, final Location to, final boolean useSubCardinalDirections) {
        return getDirection(to.getX() - from.getX(), to.getZ() - from.getZ(), useSubCardinalDirections);
    }

    public static BlockFace getDirection(final Block from, final Block to, final boolean useSubCardinalDirections) {
        return getDirection(to.getX() - from.getX(), to.getZ() - from.getZ(), useSubCardinalDirections);
    }

    public static BlockFace getDirection(final Vector movement) {
        return getDirection(movement, true);
    }

    public static BlockFace getDirection(final Vector movement, final boolean useSubCardinalDirections) {
        return getDirection(movement.getX(), movement.getZ(), useSubCardinalDirections);
    }

    public static BlockFace getDirection(final double dx, final double dz, final boolean useSubCardinalDirections) {
        return yawToFace(MathUtil.getLookAtYaw(dx, dz), useSubCardinalDirections);
    }

    public static int getFaceYawDifference(final BlockFace face1, final BlockFace face2) {
        return MathUtil.getAngleDifference(faceToYaw(face1), faceToYaw(face2));
    }

    public static double cos(final BlockFace face) {
        switch (face) {
            case SOUTH_WEST:
            case NORTH_WEST: {
                return -0.707106781;
            }
            case SOUTH_EAST:
            case NORTH_EAST: {
                return 0.707106781;
            }
            case EAST: {
                return 1.0;
            }
            case WEST: {
                return -1.0;
            }
            default: {
                return 0.0;
            }
        }
    }

    public static double sin(final BlockFace face) {
        switch (face) {
            case NORTH_EAST:
            case NORTH_WEST: {
                return -0.707106781;
            }
            case SOUTH_EAST:
            case SOUTH_WEST: {
                return 0.707106781;
            }
            case NORTH: {
                return -1.0;
            }
            case SOUTH: {
                return 1.0;
            }
            default: {
                return 0.0;
            }
        }
    }

    public static int faceToYaw(final BlockFace face) {
        return wrapAngle(45 * faceToNotch(face));
    }

    public static int wrapAngle(final int angle) {
        int wrappedAngle;
        for (wrappedAngle = angle; wrappedAngle <= -180; wrappedAngle += 360) {
        }
        while (wrappedAngle > 180) {
            wrappedAngle -= 360;
        }
        return wrappedAngle;
    }

    public static int faceToNotch(final BlockFace face) {
        final Integer notch = FaceUtil.faces.get(face);
        return (notch == null) ? 0 : notch;
    }

    public static BlockFace yawToFace(final float yaw) {
        return yawToFace(yaw, true);
    }

    public static BlockFace yawToFace(final float yaw, final boolean useSubCardinalDirections) {
        if (useSubCardinalDirections) {
            return FaceUtil.RADIAL[Math.round(yaw / 45.0f) & 0x7];
        }
        return FaceUtil.AXIS[Math.round(yaw / 90.0f) & 0x3];
    }
}
