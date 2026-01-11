package javaapplication6;

import processing.core.PApplet;

public class TileMap {
    public static final int TILE = 32;
    private final int[][] tiles;
    private final int cols;
    private final int rows;

    public TileMap(int[][] tiles) {
        this.tiles = tiles;
        this.rows = tiles.length;
        this.cols = tiles[0].length;
    }

    public int getWidth() {
        return cols * TILE;
    }

    public int getHeight() {
        return rows * TILE;
    }

    public void draw(PApplet app) {
        app.pushStyle();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int t = tiles[r][c];
                if (t == 1) {
                    app.fill(70, 90, 110);
                } else {
                    app.fill(140, 160, 170);
                }
                app.noStroke();
                app.rect(c * TILE, r * TILE, TILE, TILE);
            }
        }
        app.popStyle();
    }

    public boolean isSolidAt(float px, float py) {
        int c = (int) (px / TILE);
        int r = (int) (py / TILE);
        if (r < 0 || r >= rows || c < 0 || c >= cols) {
            return true;
        }
        return tiles[r][c] == 1;
    }

    public boolean collides(float x, float y, float w, float h) {
        return isSolidAt(x, y)
            || isSolidAt(x + w - 1, y)
            || isSolidAt(x, y + h - 1)
            || isSolidAt(x + w - 1, y + h - 1);
    }
}
