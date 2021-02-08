package plusm.vilonix.gapi.utils.core;

import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_12_R1.TrigMath;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@UtilityClass
public class MathUtil {

    public static final float nanoToSec = 1.0E-9f;
    public static final float FLOAT_ROUNDING_ERROR = 1.0E-6f;
    public static final float PI = 3.1415927f;
    public static final float PI2 = 6.2831855f;
    public static final float SQRT_3 = 1.7320508f;
    public static final float E = 2.7182817f;
    public static final float radiansToDegrees = 57.295776f;
    public static final float radDeg = 57.295776f;
    public static final float degreesToRadians = 0.017453292f;
    public static final float degRad = 0.017453292f;
    public static final float DEGTORAD = 0.017453292f;
    public static final float RADTODEG = 57.29578f;
    public static final double HALFROOTOFTWO = 0.707106781;
    static final int ATAN2_DIM = (int) Math.sqrt(16384.0);
    private static final int SIN_BITS = 14;
    private static final int SIN_MASK = 16383;
    private static final int SIN_COUNT = 16384;
    private static final float radFull = 6.2831855f;
    private static final float degFull = 360.0f;
    private static final float radToIndex = 2607.5945f;
    private static final float degToIndex = 45.511112f;
    private static final int ATAN2_BITS = 7;
    private static final int ATAN2_BITS2 = 14;
    private static final int ATAN2_MASK = 16383;
    private static final int ATAN2_COUNT = 16384;
    private static final float INV_ATAN2_DIM_MINUS_1 = 1.0f / (ATAN2_DIM - 1);
    private static final int BIG_ENOUGH_INT = 16384;
    private static final double BIG_ENOUGH_FLOOR = 16384.0;
    private static final double CEIL = 0.9999999;
    private static final double BIG_ENOUGH_CEIL = 16384.999999999996;
    private static final double BIG_ENOUGH_ROUND = 16384.5;
    private static final int CHUNK_BITS = 4;
    private static final int CHUNK_VALUES = 16;
    private static Random random = new Random();

    public static Vector getBackVector(Location loc) {
        float newZ = (float) (loc.getZ() + (1 * Math.sin(Math.toRadians(loc.getYaw() + 90))));
        float newX = (float) (loc.getX() + (1 * Math.cos(Math.toRadians(loc.getYaw() + 90))));
        return new Vector(newX - loc.getX(), 0, newZ - loc.getZ());
    }

    public static List<Location> getCircleSide(Location center, double radius, int amount) {
        World world = center.getWorld();
        double increment = (2 * Math.PI) / amount;
        ArrayList<Location> locations = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            double angle = i * increment;
            double x = center.getX() + (radius * Math.cos(angle));
            double z = center.getZ() + (radius * Math.sin(angle));
            locations.add(new Location(world, x, center.getY(), z));
        }
        return locations;
    }

    public static List<Location> getCircleUp(Location center, double radius, int amount) {
        World world = center.getWorld();
        double increment = (2 * Math.PI) / amount;
        ArrayList<Location> locations = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            double angle = i * increment;
            double y = center.getY() + (radius * Math.cos(angle));
            double z = center.getZ() + (radius * Math.sin(angle));
            locations.add(new Location(world, center.getX(), y, z));
        }
        return locations;
    }

    public static List<Double> getCircleYaw(int amount) {
        double increment = (2 * Math.PI) / amount;
        ArrayList<Double> values = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            values.add(i * increment);
        }
        return values;
    }

    public static Vector getVector(Location fromLoc, Location toLoc) {
        Vector from = new Vector(fromLoc.getX(), fromLoc.getY(), fromLoc.getZ());
        Vector to = new Vector(toLoc.getX(), toLoc.getY(), toLoc.getZ());
        return to.subtract(from);
    }

    public static long pairInt(int x, int y) {
        return (long) x << 32 | (long) y & 4294967295L;
    }

    public static final Vector rotateVector(final Vector v, final float yawDegrees, final float pitchDegrees) {
        final double yaw = Math.toRadians(-1.0f * (yawDegrees + 90.0f));
        final double pitch = Math.toRadians(-pitchDegrees);
        final double cosYaw = Math.cos(yaw);
        final double cosPitch = Math.cos(pitch);
        final double sinYaw = Math.sin(yaw);
        final double sinPitch = Math.sin(pitch);
        double initialX = v.getX();
        final double initialY = v.getY();
        double x = initialX * cosPitch - initialY * sinPitch;
        final double y = initialX * sinPitch + initialY * cosPitch;
        final double initialZ = v.getZ();
        initialX = x;
        final double z = initialZ * cosYaw - initialX * sinYaw;
        x = initialZ * sinYaw + initialX * cosYaw;
        return new Vector(x, y, z);
    }

    public static final Vector rotateVector(final Vector v, final Location location) {
        return rotateVector(v, location.getYaw(), location.getPitch());
    }

    public static double trim(final int degree, final double d) {
        final StringBuilder format = new StringBuilder("#.#");
        for (int twoDForm = 1; twoDForm < degree; ++twoDForm) {
            format.append("#");
        }
        final DecimalFormat var5 = new DecimalFormat(format.toString());
        return Double.valueOf(var5.format(d));
    }

    public static int getAngleDifference(final int angle1, final int angle2) {
        return Math.abs(wrapAngle(angle1 - angle2));
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

    public static float wrapAngle(final float angle) {
        float wrappedAngle;
        for (wrappedAngle = angle; wrappedAngle <= -180.0f; wrappedAngle += 360.0f) {
        }
        while (wrappedAngle > 180.0f) {
            wrappedAngle -= 360.0f;
        }
        return wrappedAngle;
    }

    public static double normalize(final double x, final double z, final double reqx, final double reqz) {
        return Math.sqrt(lengthSquared(reqx, reqz) / lengthSquared(x, z));
    }

    public static float getLookAtYaw(final Entity loc, final Entity lookat) {
        return getLookAtYaw(loc.getLocation(), lookat.getLocation());
    }

    public static float getLookAtYaw(final Block loc, final Block lookat) {
        return getLookAtYaw(loc.getLocation(), lookat.getLocation());
    }

    public static float getLookAtYaw(final Location loc, final Location lookat) {
        return getLookAtYaw(lookat.getX() - loc.getX(), lookat.getZ() - loc.getZ());
    }

    public static float getLookAtYaw(final Vector motion) {
        return getLookAtYaw(motion.getX(), motion.getZ());
    }

    public static float getLookAtYaw(final double dx, final double dz) {
        return atan2(dz, dx) - 180.0f;
    }

    public static double lengthSquared(final double... values) {
        double rval = 0.0;
        for (final double value : values) {
            rval += value * value;
        }
        return rval;
    }

    public static double length(final double... values) {
        return Math.sqrt(lengthSquared(values));
    }

    public static float getLookAtPitch(final double dX, final double dY, final double dZ) {
        return getLookAtPitch(dY, length(dX, dZ));
    }

    public static float getLookAtPitch(final double dY, final double dXZ) {
        return -atan(dY / dXZ);
    }

    public static float atan(final double value) {
        return 57.29578f * (float) TrigMath.atan(value);
    }

    public static float atan2(final double y, final double x) {
        return 57.29578f * (float) TrigMath.atan2(y, x);
    }

    public static int floor(final double value) {
        final int i = (int) value;
        return (value < i) ? (i - 1) : i;
    }

    public static int ceil(final double value) {
        return -floor(-value);
    }

    public static Location move(final Location loc, final Vector offset) {
        return move(loc, offset.getX(), offset.getY(), offset.getZ());
    }

    public static Location move(final Location loc, final double dx, final double dy, final double dz) {
        final Vector off = rotate(loc.getYaw(), loc.getPitch(), dx, dy, dz);
        final double x = loc.getX() + off.getX();
        final double y = loc.getY() + off.getY();
        final double z = loc.getZ() + off.getZ();
        return new Location(loc.getWorld(), x, y, z, loc.getYaw(), loc.getPitch());
    }

    public static Vector rotate(final float yaw, final float pitch, final Vector vector) {
        return rotate(yaw, pitch, vector.getX(), vector.getY(), vector.getZ());
    }

    public static Vector rotate(final float yaw, final float pitch, final double x, final double y, final double z) {
        float angle = yaw * 0.017453292f;
        final double sinyaw = Math.sin(angle);
        final double cosyaw = Math.cos(angle);
        angle = pitch * 0.017453292f;
        final double sinpitch = Math.sin(angle);
        final double cospitch = Math.cos(angle);
        final Vector vector = new Vector();
        vector.setX(x * sinyaw - y * cosyaw * sinpitch - z * cosyaw * cospitch);
        vector.setY(y * cospitch - z * sinpitch);
        vector.setZ(-(x * cosyaw) - y * sinyaw * sinpitch - z * sinyaw * cospitch);
        return vector;
    }

    public static double round(final double value, final int decimals) {
        final double p = Math.pow(10.0, decimals);
        return Math.round(value * p) / p;
    }

    public static double fixNaN(final double value) {
        return fixNaN(value, 0.0);
    }

    public static double fixNaN(final double value, final double def) {
        return Double.isNaN(value) ? def : value;
    }

    public static int toChunk(final double loc) {
        return floor(loc / 16.0);
    }

    public static int toChunk(final int loc) {
        return loc >> 4;
    }

    public static double useOld(final double oldvalue, final double newvalue, final double peruseold) {
        return oldvalue + peruseold * (newvalue - oldvalue);
    }

    public static double lerp(final double d1, final double d2, final double stage) {
        if (Double.isNaN(stage) || stage > 1.0) {
            return d2;
        }
        if (stage < 0.0) {
            return d1;
        }
        return d1 * (1.0 - stage) + d2 * stage;
    }

    public static Vector lerp(final Vector vec1, final Vector vec2, final double stage) {
        final Vector newvec = new Vector();
        newvec.setX(lerp(vec1.getX(), vec2.getX(), stage));
        newvec.setY(lerp(vec1.getY(), vec2.getY(), stage));
        newvec.setZ(lerp(vec1.getZ(), vec2.getZ(), stage));
        return newvec;
    }

    public static Location lerp(final Location loc1, final Location loc2, final double stage) {
        final Location newloc = new Location(loc1.getWorld(), 0.0, 0.0, 0.0);
        newloc.setX(lerp(loc1.getX(), loc2.getX(), stage));
        newloc.setY(lerp(loc1.getY(), loc2.getY(), stage));
        newloc.setZ(lerp(loc1.getZ(), loc2.getZ(), stage));
        newloc.setYaw((float) lerp(loc1.getYaw(), loc2.getYaw(), stage));
        newloc.setPitch((float) lerp(loc1.getPitch(), loc2.getPitch(), stage));
        return newloc;
    }

    public static boolean isInverted(final double value1, final double value2) {
        return (value1 > 0.0 && value2 < 0.0) || (value1 < 0.0 && value2 > 0.0);
    }

    public static Vector getDirection(final float yaw, final float pitch) {
        final Vector vector = new Vector();
        final double rotX = 0.017453292f * yaw;
        final double rotY = 0.017453292f * pitch;
        vector.setY(-Math.sin(rotY));
        final double h = Math.cos(rotY);
        vector.setX(-h * Math.sin(rotX));
        vector.setZ(h * Math.cos(rotX));
        return vector;
    }

    public static double clamp(final double value, final double limit) {
        return clamp(value, -limit, limit);
    }

    public static double clamp(final double value, final double min, final double max) {
        return (value < min) ? min : ((value > max) ? max : value);
    }

    public static float clamp(final float value, final float limit) {
        return clamp(value, -limit, limit);
    }

    public static int clamp(final int value, final int limit) {
        return clamp(value, -limit, limit);
    }

    public static int invert(final int value, final boolean negative) {
        return negative ? (-value) : value;
    }

    public static float invert(final float value, final boolean negative) {
        return negative ? (-value) : value;
    }

    public static double invert(final double value, final boolean negative) {
        return negative ? (-value) : value;
    }

    public static void setVectorLength(final Vector vector, final double length) {
        setVectorLengthSquared(vector, Math.signum(length) * length * length);
    }

    public static void setVectorLengthSquared(final Vector vector, final double lengthsquared) {
        final double vlength = vector.lengthSquared();
        if (Math.abs(vlength) > 1.0E-4) {
            if (lengthsquared < 0.0) {
                vector.multiply(-Math.sqrt(-lengthsquared / vlength));
            } else {
                vector.multiply(Math.sqrt(lengthsquared / vlength));
            }
        }
    }

    public static boolean isHeadingTo(final BlockFace direction, final Vector velocity) {
        return isHeadingTo(FaceUtil.faceToVector(direction), velocity);
    }

    public static boolean isHeadingTo(final Location from, final Location to, final Vector velocity) {
        return isHeadingTo(new Vector(to.getX() - from.getX(), to.getY() - from.getY(), to.getZ() - from.getZ()), velocity);
    }

    public static boolean isHeadingTo(final Vector offset, final Vector velocity) {
        final double dbefore = offset.lengthSquared();
        if (dbefore < 1.0E-4) {
            return true;
        }
        final Vector clonedVelocity = velocity.clone();
        setVectorLengthSquared(clonedVelocity, dbefore);
        return dbefore > clonedVelocity.subtract(offset).lengthSquared();
    }

    public static float getAngleDifference(final float angle1, final float angle2) {
        return Math.abs(wrapAngle(angle1 - angle2));
    }

    public static int r(final int i) {
        return random.nextInt(i);
    }

    public static double offset2d(final Entity a, final Entity b) {
        return offset2d(a.getLocation().toVector(), b.getLocation().toVector());
    }

    public static double offset2d(final Location a, final Location b) {
        return offset2d(a.toVector(), b.toVector());
    }

    public static double offset2d(final Vector a, final Vector b) {
        a.setY(0);
        b.setY(0);
        return a.subtract(b).length();
    }

    public static float sin(final float radians) {
        return Sin.table[(int) (radians * 2607.5945f) & 0x3FFF];
    }

    public static float cos(final float radians) {
        return Sin.table[(int) ((radians + 1.5707964f) * 2607.5945f) & 0x3FFF];
    }

    public static float sinDeg(final float degrees) {
        return Sin.table[(int) (degrees * 45.511112f) & 0x3FFF];
    }

    public static float cosDeg(final float degrees) {
        return Sin.table[(int) ((degrees + 90.0f) * 45.511112f) & 0x3FFF];
    }

    public static boolean isInteger(final Object object) {
        try {
            Integer.parseInt(object.toString());
            return true;
        } catch (Exception var2) {
            return false;
        }
    }

    public static boolean isDouble(final Object object) {
        try {
            Double.parseDouble(object.toString());
            return true;
        } catch (Exception var2) {
            return false;
        }
    }

    public static float atan2(float y, float x) {
        float mul;
        float add;
        if (x < 0.0f) {
            if (y < 0.0f) {
                y = -y;
                mul = 1.0f;
            } else {
                mul = -1.0f;
            }
            x = -x;
            add = -3.1415927f;
        } else {
            if (y < 0.0f) {
                y = -y;
                mul = -1.0f;
            } else {
                mul = 1.0f;
            }
            add = 0.0f;
        }
        final float invDiv = 1.0f / (((x < y) ? y : x) * INV_ATAN2_DIM_MINUS_1);
        if (invDiv == Double.POSITIVE_INFINITY) {
            return ((float) Math.atan2(y, x) + add) * mul;
        }
        final int xi = (int) (x * invDiv);
        final int yi = (int) (y * invDiv);
        return (Atan2.table[yi * ATAN2_DIM + xi] + add) * mul;
    }

    public static int random(final int range) {
        return random.nextInt(range + 1);
    }

    public static int random(final int start, final int end) {
        return start + random.nextInt(end - start + 1);
    }

    public static boolean randomBoolean() {
        return random.nextBoolean();
    }

    public static boolean randomBoolean(final float chance) {
        return random() < chance;
    }

    public static float random() {
        return random.nextFloat();
    }

    public static float random(final float range) {
        return random.nextFloat() * range;
    }

    public static float random(final float start, final float end) {
        return start + random.nextFloat() * (end - start);
    }

    public static int nextPowerOfTwo(int value) {
        if (value == 0) {
            return 1;
        }
        value = (--value | value >> 1);
        value |= value >> 2;
        value |= value >> 4;
        value |= value >> 8;
        value |= value >> 16;
        return value + 1;
    }

    public static boolean isPowerOfTwo(final int value) {
        return value != 0 && (value & value - 1) == 0x0;
    }

    public static int clamp(final int value, final int min, final int max) {
        return (value < min) ? min : ((value > max) ? max : value);
    }

    public static short clamp(final short value, final short min, final short max) {
        return (value < min) ? min : ((value > max) ? max : value);
    }

    public static float clamp(final float value, final float min, final float max) {
        return (value < min) ? min : ((value > max) ? max : value);
    }

    public static int floor(final float x) {
        return (int) (x + 16384.0) - 16384;
    }

    public static int floorPositive(final float x) {
        return (int) x;
    }

    public static int ceil(final float x) {
        return (int) (x + 16384.999999999996) - 16384;
    }

    public static int ceilPositive(final float x) {
        return (int) (x + 0.9999999);
    }

    public static int round(final float x) {
        return (int) (x + 16384.5) - 16384;
    }

    public static int roundPositive(final float x) {
        return (int) (x + 0.5f);
    }

    public static boolean isZero(final float value) {
        return Math.abs(value) <= 1.0E-6f;
    }

    public static boolean isZero(final float value, final float tolerance) {
        return Math.abs(value) <= tolerance;
    }

    public static boolean isEqual(final float a, final float b) {
        return Math.abs(a - b) <= 1.0E-6f;
    }

    public static boolean isEqual(final float a, final float b, final float tolerance) {
        return Math.abs(a - b) <= tolerance;
    }

    public static Vector rotateAroundAxisX(final Vector v, final double angle) {
        final double cos = Math.cos(angle);
        final double sin = Math.sin(angle);
        final double y = v.getY() * cos - v.getZ() * sin;
        final double z = v.getY() * sin + v.getZ() * cos;
        return v.setY(y).setZ(z);
    }

    public static Vector rotateAroundAxisY(final Vector v, final double angle) {
        final double cos = Math.cos(angle);
        final double sin = Math.sin(angle);
        final double x = v.getX() * cos + v.getZ() * sin;
        final double z = v.getX() * -sin + v.getZ() * cos;
        return v.setX(x).setZ(z);
    }

    public static Vector rotateAroundAxisZ(final Vector v, final double angle) {
        final double cos = Math.cos(angle);
        final double sin = Math.sin(angle);
        final double x = v.getX() * cos - v.getY() * sin;
        final double y = v.getX() * sin + v.getY() * cos;
        return v.setX(x).setY(y);
    }

    public static Vector rotateVector(final Vector v, final double angleX, final double angleY, final double angleZ) {
        rotateAroundAxisX(v, angleX);
        rotateAroundAxisY(v, angleY);
        rotateAroundAxisZ(v, angleZ);
        return v;
    }

    public static double angleToXAxis(final Vector vector) {
        return Math.atan2(vector.getX(), vector.getY());
    }

    public static Vector getRandomVector() {
        final double x = random.nextDouble() * 2.0 - 1.0;
        final double y = random.nextDouble() * 2.0 - 1.0;
        final double z = random.nextDouble() * 2.0 - 1.0;
        return new Vector(x, y, z).normalize();
    }

    public static void applyVelocity(final Entity ent, final Vector v) {
        if (!ent.hasMetadata("NPC")) {
            ent.setVelocity(v);
        }
    }

    public static Vector getRandomCircleVector() {
        final double rnd = random.nextDouble() * 2.0 * 3.141592653589793;
        final double x = Math.cos(rnd);
        final double z = Math.sin(rnd);
        return new Vector(x, 0.0, z);
    }

    public static Material getRandomMaterial(final Material[] materials) {
        return materials[random.nextInt(materials.length)];
    }

    public static double getRandomAngle() {
        return random.nextDouble() * 2.0 * 3.141592653589793;
    }

    public static double randomDouble(final double min, final double max) {
        return (Math.random() < 0.5) ? ((1.0 - Math.random()) * (max - min) + min) : (Math.random() * (max - min) + min);
    }

    public static float randomRangeFloat(final float min, final float max) {
        return (float) ((Math.random() < 0.5) ? ((1.0 - Math.random()) * (max - min) + min) : (Math.random() * (max - min) + min));
    }

    public static byte randomByte(final int max) {
        return (byte) random.nextInt(max + 1);
    }

    public static int randomRangeInt(final int min, final int max) {
        return (int) ((Math.random() < 0.5) ? ((1.0 - Math.random()) * (max - min) + min) : (Math.random() * (max - min) + min));
    }

    public static double offset(final Entity a, final Entity b) {
        return offset(a.getLocation().toVector(), b.getLocation().toVector());
    }

    public static double offset(final Location a, final Location b) {
        return offset(a.toVector(), b.toVector());
    }

    public static double offset(final Vector a, final Vector b) {
        return a.subtract(b).length();
    }

    private static class Atan2 {
        static final float[] table;

        static {
            table = new float[16384];
            for (int i = 0; i < ATAN2_DIM; ++i) {
                for (int j = 0; j < ATAN2_DIM; ++j) {
                    final float x0 = i / (float) ATAN2_DIM;
                    final float y0 = j / (float) ATAN2_DIM;
                    Atan2.table[j * ATAN2_DIM + i] = (float) Math.atan2(y0, x0);
                }
            }
        }
    }

    private static class Sin {
        static final float[] table;

        static {
            table = new float[16384];
            for (int i = 0; i < 16384; ++i) {
                Sin.table[i] = (float) Math.sin((i + 0.5f) / 16384.0f * 6.2831855f);
            }
            for (int i = 0; i < 360; i += 90) {
                Sin.table[(int) (i * 45.511112f) & 0x3FFF] = (float) Math.sin(i * 0.017453292f);
            }
        }
    }
}
