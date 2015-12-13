package com.danilafe.cellsim;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class CellularAutoSim extends ApplicationAdapter {
	
	ShapeRenderer shapeRenderer;
	boolean running;
	boolean mode;
	Color cellColor;
	Color cellColorInactive;
	Color emptyColor;
	boolean[][] world;
	float deltaT;
	CellBehaviorInterface cellBehavior;
	
	final int cellWidth = 10;
	final float secDelay = .25F;
	
	@Override
	public void create () {
		shapeRenderer = new ShapeRenderer();
		cellColor = new Color(hslToRgb((float) Math.random(), .35F, .85F));
		emptyColor = new Color(.8F, .8F, .8f, 1);
		cellColorInactive = new Color(.6F, .6F, .6F, 1);
		world = new boolean[640 / cellWidth][480 / cellWidth];
		deltaT = 0;
		mode = true;
		CellBehaviorInterface inequality = (x, y) -> x > y;
		CellBehaviorInterface conwayLife = (x, y) -> {
			int neighbors = numberNeighbors(x, y);
			if(world[x][y]){
				if(neighbors < 2) return false;
				if(neighbors > 3) return false;
				return true;
			} else {
				if(neighbors == 3) return true;
				return false;
			}
		};
		CellBehaviorInterface wallGen = (x, y) -> {
			if(x == 0 || y == 0 || x == world.length - 1 || y == world[0].length - 1) return true;
			int neighbors = numberNeighbors(x, y);
			if(world[x][y]) {
				if(neighbors > 2) return true;
				return false;
			} else {
				if(neighbors > 5) return true;
				return false;
			}
		};
		cellBehavior = wallGen;
	}
	
	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		deltaT += Gdx.graphics.getDeltaTime();
		while(deltaT > secDelay) {
			deltaT -= secDelay;
			if(running) updateBoard();
		}
		
		if(Gdx.input.isTouched()){
			if(Gdx.input.getX() >= 0 && Gdx.input.getX() <= 640 && Gdx.input.getY() >= 0 && Gdx.input.getY() <= 480){
				int x = Gdx.input.getX() / cellWidth;
				int y = (480 - Gdx.input.getY()) / cellWidth;
				world[x][y] = mode;
			}
		}
		if(Gdx.input.isKeyJustPressed(Keys.Z)) fillRandomly();
		if(Gdx.input.isKeyJustPressed(Keys.E)) {
			running = !running;
			if(running) cellColor = new Color(hslToRgb((float) Math.random(), .25F, .85F));
		}
		if(Gdx.input.isKeyJustPressed(Keys.T)) mode = !mode;
		if(Gdx.input.isKeyJustPressed(Keys.R)) 
			for (int i = 0; i < world.length; i ++)
				for (int j = 0; j < world[i].length; j ++)
					world[i][j] = false;
		if(Gdx.input.isKeyJustPressed(Keys.I)) 
			for (int i = 0; i < world.length; i ++)
				for (int j = 0; j < world[i].length; j ++)
					world[i][j] = !world[i][j];
		if(Gdx.input.isKeyJustPressed(Keys.F)) 
			for (int i = 0; i < world.length; i ++)
				for (int j = 0; j < world[i].length; j ++)
					world[i][j] = true;
		
		
		
		shapeRenderer.setAutoShapeType(true);
		shapeRenderer.begin();
		shapeRenderer.set(ShapeType.Filled);
		for(int i = 0; i < world.length; i ++){
			for(int j = 0; j < world[i].length; j++){
				if(world[i][j]) shapeRenderer.setColor((running) ? cellColor : cellColorInactive);
				else shapeRenderer.setColor(emptyColor);
				shapeRenderer.circle(i * cellWidth + cellWidth/2, j * cellWidth + cellWidth/2, cellWidth / 2 * .8F);
			}
		}
		shapeRenderer.end();
	}
	
	void updateBoard(){
		boolean[][] tempArray = new boolean[world.length][world[0].length];
		for(int i = 0; i < world.length; i ++){
			for(int j = 0; j < world[i].length; j ++){
				tempArray[i][j] = cellBehavior.isAlive(i, j);
			}
		}
		for(int i = 0; i < world.length; i ++){
			for(int j = 0; j < world[i].length; j ++){
				world[i][j] = tempArray[i][j];
			}
		}
		
	}
	
	void fillRandomly(){
		for(int i = 0; i < world.length; i ++){
			for(int j = 0; j < world[i].length; j ++){
				world[i][j] = Math.random() < .51D;
			}
		}
	}
	
	int numberNeighbors(int x, int y){
		int neighborCount = 0;
		for(int i = x - 1; i <= x + 1; i ++){
			if(i < 0 || i >= world.length) continue;
			for(int j = y - 1; j <= y + 1; j ++){
				if(j < 0 || j > world[i].length - 1) continue;
				neighborCount += (world[i][j] && !(i == x && j == y)) ? 1 : 0;
			}
		}
		return neighborCount;
	}
	
	int hslToRgb(float h, float s, float l){
		int resultColor = java.awt.Color.getHSBColor(h, s, l).getRGB();
		int alpha = (resultColor >> 24) & 0xFF;
		int red = (resultColor >> 16) & 0xFF;
		int green = (resultColor >> 8) & 0xFF;
		int blue = (resultColor) & 0xFF;
		return (red << 24) | (green << 16) | (blue << 8) | alpha;
	}
	
	interface CellBehaviorInterface {
		public boolean isAlive(int x, int y);
	}
	
}
