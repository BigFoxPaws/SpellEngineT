package net.combatspells.utils;

import net.combatspells.api.spell.Spell;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Tameable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

import java.util.List;

public class TargetHelper {
    public enum Relation {
        FRIENDLY, NEUTRAL, HOSTILE
    }

    public static Relation getRelation(LivingEntity caster, Entity target) {
        var casterTeam = caster.getScoreboardTeam();
        var targetTeam = target.getScoreboardTeam();
        if (target instanceof Tameable tameable) {
            var owner = tameable.getOwner();
            if (owner != null) {
                return getRelation(caster, owner);
            }
        }
        if (casterTeam == null && targetTeam == null) {
            if (caster instanceof PlayerEntity casterPlayer) {
                if (target instanceof PlayerEntity targetEntity) {
                    return Relation.FRIENDLY;
                }
            }
            return Relation.NEUTRAL;
        }
        return caster.isTeammate(target) ? Relation.FRIENDLY : Relation.HOSTILE;
    }

    public static boolean actionAllowed(boolean beneficial, Relation relation) {
        switch (relation) {
            case FRIENDLY -> {
                return beneficial;
            }
            case NEUTRAL, HOSTILE -> {
                return !beneficial;
            }
        }
        assert true;
        return true;
    }

    public static Entity targetFromRaycast(Entity caster, float range) {
        Vec3d start = caster.getEyePos();
        Vec3d look = caster.getRotationVec(1.0F)
                .normalize()
                .multiply(range);
        Vec3d end = start.add(look);
        Box searchAABB = caster.getBoundingBox().expand(range, range, range);
        var hitResult = ProjectileUtil.raycast(caster, start, end, searchAABB, (target) -> {
            return !target.isSpectator() && target.canHit();
        }, range*range); // `range*range` is provided for squared distance comparison
        if (hitResult != null) {
            return hitResult.getEntity();
        }
        return null;
    }

    public static List<Entity> targetsFromArea(Entity caster, float range, Spell.Release.Target.Area area) {
        var horizontal = range * area.horizontal_range_multiplier;
        var vertical = range * area.vertical_range_multiplier;
        var box = caster.getBoundingBox().expand(
                horizontal,
                vertical,
                horizontal);
        var squaredDistance = range * range;
        var raycastStart = caster.getEyePos();
        var entities = caster.world.getOtherEntities(caster, box, (target) -> {
            return !target.isSpectator() && target.canHit()
                    && target.squaredDistanceTo(caster) <= squaredDistance
                    && raycastObstacleFree(raycastStart, target.getPos().add(0, target.getHeight() / 2F, 0));
        });
        return entities;
    }

    private static boolean raycastObstacleFree(Vec3d start, Vec3d end) {
        var client = MinecraftClient.getInstance();
        var world = client.world;
        var hit = client.world.raycast(new RaycastContext(start, end, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, client.player));
        return hit.getType() != HitResult.Type.BLOCK;
    }
}