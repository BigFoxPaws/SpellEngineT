package net.spell_engine.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.spell_engine.client.util.Color;
import net.spell_power.api.SpellSchools;
import org.jetbrains.annotations.Nullable;

@Environment(value= EnvType.CLIENT)
public class SpellFlameParticle extends AbstractSlowingParticle {
    private SpriteProvider spriteProviderForAnimation = null;

    public SpellFlameParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
        super(clientWorld, d, e, f, g, h, i);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_LIT;
    }

    @Override
    public void move(double dx, double dy, double dz) {
        this.setBoundingBox(this.getBoundingBox().offset(dx, dy, dz));
        this.repositionFromBoundingBox();
    }

    @Override
    public float getSize(float tickDelta) {
        float f = ((float) this.age + tickDelta) / (float) this.maxAge;
        return this.scale * (1.0f - f * f * 0.5f);
    }

    @Override
    public int getBrightness(float tint) {
        return 255;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.spriteProviderForAnimation != null) {
            this.setSpriteForAge(this.spriteProviderForAnimation);
        }
    }


    @Environment(EnvType.CLIENT)
    public static class FlameFactory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public FlameFactory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            var particle = new SpellFlameParticle(clientWorld, d, e, f, g, h, i);
            particle.setSprite(this.spriteProvider);
            return particle;
        }
    }

    @Environment(EnvType.CLIENT)
    public static class AnimatedFlameFactory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public AnimatedFlameFactory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            var particle = new SpellFlameParticle(clientWorld, d, e, f, g, h, i);
            particle.setSprite(this.spriteProvider);
            particle.spriteProviderForAnimation = this.spriteProvider;
            return particle;
        }
    }

    @Environment(EnvType.CLIENT)
    public static class ColoredAnimatedFactory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;
        private final Color color;
        private final float scale;
        protected float randomColorFloor = 0.5F;
        protected float randomColorRange = 0.35F;

        public ColoredAnimatedFactory(Color color, float scale, SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
            this.color = color;
            this.scale = scale;
        }

        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            var particle = new SpellFlameParticle(clientWorld, d, e, f, g, h, i);
            particle.setSprite(this.spriteProvider);
            particle.spriteProviderForAnimation = this.spriteProvider;

            var red = color.red();
            var green = color.green();
            var blue = color.blue();
            if (randomColorRange > 0) {
                red = (clientWorld.random.nextFloat() * randomColorFloor + randomColorRange) * red;
                green = (clientWorld.random.nextFloat() * randomColorFloor + randomColorRange) * green;
                blue = (clientWorld.random.nextFloat() * randomColorFloor + randomColorRange) * blue;
            }
            float j = clientWorld.random.nextFloat() * 0.5F + 0.35F;
            particle.setColor(red, green, blue);
            particle.scale = this.scale;
            particle.setAlpha(1F);
            return particle;
        }
    }

    @Environment(EnvType.CLIENT)
    public static class ElectricSparkFactory extends ColoredAnimatedFactory {
        public ElectricSparkFactory(SpriteProvider spriteProvider) {
            super(Color.ELECTRIC, 0.75F, spriteProvider);
            randomColorRange = 0F;
        }
    }

    @Environment(EnvType.CLIENT)
    public static class SmokeFactory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public SmokeFactory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            var particle = new SpellFlameParticle(clientWorld, d, e, f, g, h, i);
            particle.setSprite(this.spriteProvider);
            particle.setColor(1F, 1F, 1F);
            particle.spriteProviderForAnimation = this.spriteProvider;
            particle.velocityMultiplier = 0.8F;
            particle.setAlpha(0.9F);
            return particle;
        }
    }

    @Environment(EnvType.CLIENT)
    public static class MediumFlameFactory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public MediumFlameFactory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            var particle = new SpellFlameParticle(clientWorld, d, e, f, g, h, i);
            particle.setSprite(this.spriteProvider);
            particle.spriteProviderForAnimation = this.spriteProvider;
            particle.scale = 0.5F;
            particle.maxAge *= 0.5;
            return particle;
        }
    }

    @Environment(EnvType.CLIENT)
    public static class FrostShard implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public FrostShard(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public static Color color = Color.FROST;

        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            var particle = new SpellFlameParticle(clientWorld, d, e, f, g, h, i);
            particle.setSprite(this.spriteProvider);
            float j = clientWorld.random.nextFloat() * 0.5F + 0.35F;
            particle.setColor(color.red() * j, color.green() * j, color.blue() * j);
            particle.velocityY *= clientWorld.random.nextFloat() * 0.2F + 0.9F;
            particle.maxAge = Math.round(clientWorld.random.nextFloat() * 3) + 5;
            return particle;
        }
    }

    public static class ColorableFactory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;
        public Color color = Color.from(0xffffff);

        public ColorableFactory(SpriteProvider spriteProvider, Color color) {
            this.spriteProvider = spriteProvider;
            this.color = color;
        }

        @Nullable
        @Override
        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            var particle = new SpellFlameParticle(clientWorld, d, e, f, g, h, i);
            particle.setSprite(this.spriteProvider);
            float j = clientWorld.random.nextFloat() * 0.5F + 0.35F;
            particle.setColor(color.red() * j, color.green() * j, color.blue() * j);
            return particle;
        }
    }


    @Environment(EnvType.CLIENT)
    public static class HealingFactory extends ColorableFactory {
        public HealingFactory(SpriteProvider spriteProvider) {
            super(spriteProvider, Color.from(SpellSchools.HEALING.color));
        }
    }

    @Environment(EnvType.CLIENT)
    public static class HolyFactory extends ColorableFactory {
        public HolyFactory(SpriteProvider spriteProvider) {
            super(spriteProvider, Color.HOLY);
        }
    }

    @Environment(EnvType.CLIENT)
    public static class NatureFactory extends ColorableFactory {
        public NatureFactory(SpriteProvider spriteProvider) {
            super(spriteProvider, Color.NATURE);
        }
    }

    @Environment(EnvType.CLIENT)
    public static class NatureSlowingFactory extends NatureFactory {
        public NatureSlowingFactory(SpriteProvider spriteProvider) {
            super(spriteProvider);
        }
        @Override
        public @Nullable Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            var particle = (SpellFlameParticle)super.createParticle(defaultParticleType, clientWorld, d, e, f, g, h, i);
            particle.velocityMultiplier = 0.8F;
            return particle;
        }
    }
}
