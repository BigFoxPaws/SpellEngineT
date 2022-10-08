package net.combatspells.config;

import net.combatspells.client.gui.HudElement;
import net.minecraft.util.math.Vec2f;

public class HudConfig {
    public HudElement base;
    public Part target;
    public Part icon;
    public int bar_width;

    public static class Part { public Part() { }
        public boolean visible = true;
        public Vec2f offset = Vec2f.ZERO;
        public Part(boolean visible, Vec2f offset) {
            this.visible = visible;
            this.offset = offset;
        }
    }

    public static HudConfig createDefault() {
        var barWidth = 90;
        var config = new HudConfig();
        config.base = new HudElement(HudElement.Origin.BOTTOM, new Vec2f(0, -66));
        config.target = new Part(true, targetOffsetUp());
        config.icon = new Part(true, iconRight(barWidth));
        config.bar_width = barWidth;
        return config;
    }

    public static HudConfig preset(HudElement.Origin origin) {
        int offsetW = 70;
        int offsetH = 16;
        var barWidth = 90;
        var offset = new Vec2f(0, 0);
        var target = new Part();
        var icon = new Part();
        switch (origin) {
            case TOP -> {
                offset = new Vec2f(0, offsetH);
                target.offset = targetOffsetDown();
                icon.offset = iconRight(barWidth);
            }
            case TOP_LEFT -> {
                offset = new Vec2f(offsetW - 8, offsetH);
                target.offset = targetOffsetDown();
                icon.offset = iconRight(barWidth);
            }
            case TOP_RIGHT -> {
                offset = new Vec2f((-1) * offsetW + 8, offsetH);
                target.offset = targetOffsetDown();
                icon.offset = iconLeft(barWidth);
            }
            case BOTTOM -> {
                offset = new Vec2f(0, (-1) * offsetH);
                target.offset = targetOffsetUp();
                icon.offset = iconRight(barWidth);
            }
            case BOTTOM_LEFT -> {
                offset = new Vec2f(offsetW - 8, (-1) * offsetH);
                target.offset = targetOffsetUp();
                icon.offset = iconRight(barWidth);
            }
            case BOTTOM_RIGHT -> {
                offset = new Vec2f((-1) * offsetW + 8, (-1) * offsetH);
                target.offset = targetOffsetUp();
                icon.offset = iconLeft(barWidth);
            }
        }

        var config = new HudConfig();
        config.base = new HudElement(origin, offset);
        config.target = target;
        config.icon = icon;
        config.bar_width = barWidth;
        return config;
    }

    private static Vec2f targetOffsetUp() {
        return new Vec2f(0, -12);
    }

    private static Vec2f targetOffsetDown() {
        return new Vec2f(0, 12);
    }

    private static Vec2f iconLeft(int barWidth) {
        return new Vec2f(- (barWidth / 2) - 10 - 16, -6);
    }

    private static Vec2f iconRight(int barWidth) {
        return new Vec2f((barWidth / 2) + 10, -6);
    }
}