package net.spell_engine.particle;

import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.spell_engine.SpellEngineMod;

import java.util.List;

public class Particles {
    private static class Helper extends DefaultParticleType {
        protected Helper(boolean alwaysShow) {
            super(alwaysShow);
        }
    }
    private static DefaultParticleType createSimple() {
        return new Helper(false);
    }

    public static class ParticleEntry {
        public final Identifier id;
        public final DefaultParticleType particleType = Particles.createSimple();
        public boolean usesCustomTexture = false;
        public ParticleEntry(String name) {
            this.id =  new Identifier(SpellEngineMod.ID, name);
        }
        public ParticleEntry customTexture() {
            this.usesCustomTexture = true;
            return this;
        }
    }

//    public static final ParticleEntry arcane_spark = new ParticleEntry("arcane_spark");
    public static final ParticleEntry arcane_spell = new ParticleEntry("arcane_spell");
    public static final ParticleEntry flame = new ParticleEntry("flame");
    public static final ParticleEntry frost = new ParticleEntry("frost").customTexture();
    public static final ParticleEntry snowflake = new ParticleEntry("snowflake");

    public static final List<ParticleEntry> all;
    static {
        all = List.of(arcane_spell, flame, frost, snowflake);
    }

    public static void register() {
        for(var entry: all) {
            Registry.register(Registry.PARTICLE_TYPE, entry.id, entry.particleType);
        }
    }
}