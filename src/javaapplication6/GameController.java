package javaapplication6;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import processing.core.PApplet;

public class GameController {
    private final TileMap map;
    private final Player player;
    private final List<NPC> npcs;
    private final LightSource[] lights;
    private final Random rng = new Random();
    private boolean night = false;
    private int dayTimer = 0;
    private int purifiedCount = 0;
    private boolean win = false;

    public GameController(TileMap map, Player player, List<NPC> npcs, LightSource[] lights) {
        this.map = map;
        this.player = player;
        this.npcs = npcs;
        this.lights = lights;
    }

    public void update() {
        dayTimer++;
        if (dayTimer >= 1200) {
            dayTimer = 0;
            night = !night;
            if (night) {
                pickDisguised();
            }
        }

        if (win) {
            return;
        }

        player.update(map);
        for (NPC npc : npcs) {
            npc.update(map, player);
            npc.maybeExpose(player, player.isShadowVision());
            npc.applyLightDamage(night, lights);
            if (npc.isPurified() && !npc.isCounted()) {
                purifiedCount++;
                npc.setCounted(true);
            }
        }

        if (purifiedCount >= 2) {
            win = true;
        }
    }

    private void pickDisguised() {
        List<NPC> alive = new ArrayList<>();
        for (NPC npc : npcs) {
            if (!npc.isPurified()) {
                alive.add(npc);
            }
        }
        Collections.shuffle(alive, rng);
        for (int i = 0; i < alive.size(); i++) {
            NPC npc = alive.get(i);
            if (i < 2) {
                npc.setState(NPC.STATE_DISGUISED);
            } else if (npc.getState() != NPC.STATE_PURIFIED) {
                npc.setState(NPC.STATE_NORMAL);
            }
        }
    }

    public void drawWorld(PApplet app) {
        map.draw(app);
        for (LightSource light : lights) {
            light.draw(app);
        }
        for (NPC npc : npcs) {
            npc.draw(app, player.isShadowVision());
        }
        player.draw(app);
    }

    public boolean isNight() {
        return night;
    }

    public int getPurifiedCount() {
        return purifiedCount;
    }

    public boolean isWin() {
        return win;
    }

    public Player getPlayer() {
        return player;
    }

    public LightSource[] getLights() {
        return lights;
    }

    public TileMap getMap() {
        return map;
    }

    public int getDayTimerSeconds() {
        return (1200 - dayTimer) / 60;
    }
}
