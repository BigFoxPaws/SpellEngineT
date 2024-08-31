package net.spell_engine.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.spell_engine.client.util.Color;
import net.spell_power.api.SpellSchool;
import net.spell_power.api.SpellSchools;

@Environment(value= EnvType.CLIENT)
public class SpellHitParticle extends SpriteBillboardParticle {

    public SpellHitParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
        super(clientWorld, d, e, f, 0.0, 0.0, 0.0);
        this.velocityMultiplier = 0.7f;
        this.gravityStrength = 0.5f;
        this.velocityX *= (double)0.1f;
        this.velocityY *= (double)0.1f;
        this.velocityZ *= (double)0.1f;
        this.velocityX += g * 0.4;
        this.velocityY += h * 0.4;
        this.velocityZ += i * 0.4;
        this.scale *= 0.75f;
        this.maxAge = Math.max((int)(6.0 / (Math.random() * 0.8 + 0.6)), 1);
        this.collidesWithWorld = false;
        this.tick();
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public int getBrightness(float tint) {
        return 255;
    }

    public static class GenericFactory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;
        public final Color color;

        public GenericFactory(SpriteProvider spriteProvider, Color color) {
            this.spriteProvider = spriteProvider;
            this.color = color;
        }

        public Particle createParticle(SimpleParticleType SimpleParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            var particle = new SpellHitParticle(clientWorld, d, e, f, g, h, i);
            particle.setSprite(this.spriteProvider);
            float j = clientWorld.random.nextFloat() * 0.5F + 0.35F;
            particle.setColor(color.red() * j, color.green() * j, color.blue() * j);
            particle.velocityY += clientWorld.random.nextFloat() * 0.5F;
            particle.alpha = 0.75F;
            particle.maxAge *= 2;
            return particle;
        }
    }

    @Environment(EnvType.CLIENT)
    public static class FrostFactory extends GenericFactory {
        public FrostFactory(SpriteProvider spriteProvider) {
            super(spriteProvider, Color.from(0x66ccff));
        }
    }

    @Environment(EnvType.CLIENT)
    public static class ArcaneFactory extends GenericFactory {
        public ArcaneFactory(SpriteProvider spriteProvider) {
            super(spriteProvider, Color.from(SpellSchools.ARCANE.color));
        }
    }

    @Environment(EnvType.CLIENT)
    public static class HolyFactory extends GenericFactory {
        public HolyFactory(SpriteProvider spriteProvider) {
            super(spriteProvider, Color.from(0xffff99));
        }
    }
}
