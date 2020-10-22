package com.ttradvisor.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ttradvisor.app.TTRAdvisorApp;

import java.time.Instant;

/**
 * Created by julienvillegas on 17/01/2017.
 * 
 * Copied in as example code to modify
 */
public class GameScreen implements Screen {

    private TTRAdvisorApp mainApp;
    private InputMultiplexer inputMult;
    private Stage mapStage;
    private OrthographicCamera camera;
    private Stage guiStage;
    
    private float mapWidth;
    private float mapHeight;

//
//    private float minAltitude = 0.5f;
//    private float maxAltitude = 2.5f;
//
//    private float percent;
//    private long startTime;
//    private final float animation_duration = 15000;

    public GameScreen(TTRAdvisorApp main) {
        mainApp = main;
        guiStage = new Stage(new ScreenViewport());
        mapStage = new Stage(new ScreenViewport());
        inputMult = new InputMultiplexer(guiStage, mapStage);
        
        setupMapInputHandling();

        Image map = new Image(new Texture("low_res_board.jpg"));
        
        // notes: doing this is risky (I think) because texture coordinates != world coordinates
        // but I guess it's fine as long as we never scale the textures independently of the world
        // (so that 1px in texture is always == 1px in world)
        mapWidth = map.getWidth();
        mapHeight = map.getHeight();
        
        mapStage.addActor(map);
        camera = (OrthographicCamera) mapStage.getViewport().getCamera();
        camera.setToOrtho(false);
        camera.zoom = 1;
        
        camera.position.set(0, 0, camera.position.z);
        // startTime = Instant.now().toEpochMilli();

    }
    
    /**
     * Handlers for mapStage
     * ActorGestureListener used here for sake of identifying zooms/pans on android
     */
    private void setupMapInputHandling() {
    	
    	mapStage.addListener(new InputListener() {
    		
    		@Override
    		public boolean scrolled(InputEvent event, float x, float y, int amount) {
    			camera.zoom += (float) amount / 10f;
    		    Gdx.app.log("Camera", "Zoomed to: " + camera.zoom);
    			return super.scrolled(event, x, y, amount);
    		}
    		
    	});

    	mapStage.addListener(new ActorGestureListener() {
    		
    		// screen (not world) coordinates of the start of the pan and current pan
    		private Vector3 panOriginScreen;
    		private Vector3 panCurrentScreen;
    		private Vector3 trueScreenDelta;
    		// world coordinates of camera when starting the pan
    		private Vector3 panOriginCamera;
    		
    		@Override
    		public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
    			
    			// WORK IN PROGRESS (works but maybe not for all situations)
    			
    			if (panOriginScreen == null) {
    				panOriginScreen = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
    				panOriginCamera = new Vector3(camera.position);
    			}
    			panCurrentScreen = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
    			
    			trueScreenDelta = new Vector3(panOriginScreen.x - panCurrentScreen.x, panOriginScreen.y - panCurrentScreen.y, 0);
    		
    			camera.position.set(panOriginCamera.x + trueScreenDelta.x, panOriginCamera.y - trueScreenDelta.y, camera.position.z);
    		    Gdx.app.log("Camera", "Translated to coords: " + camera.position.x + ", " + camera.position.y);
    			super.pan(event, x, y, deltaX, deltaY);
    		}
    		
    		@Override
    		public void panStop(InputEvent event, float x, float y, int pointer, int button) {
    			if (camera.position.x < 0) {
    				camera.position.x = 0;
    			}
    			else if (camera.position.x > mapWidth) {
    				camera.position.x = mapWidth;
    			}
    			if (camera.position.y < 0) {
    				camera.position.y = 0;
    			}
    			else if (camera.position.y > mapHeight) {
    				camera.position.y = mapHeight;
    			}
    			camera.position.set(camera.position.x, camera.position.y, camera.position.z);
    		    Gdx.app.log("Camera", "Snapped to coords: " + camera.position.x + ", " + camera.position.y);
    		    panOriginScreen = null;
    			super.panStop(event, x, y, pointer, button);
    		}
    		
    		@Override
    		public void zoom(InputEvent event, float initialDistance, float distance) {
    		    float ratio = (initialDistance / distance) * camera.zoom;
    		    camera.zoom = ratio;
    		    Gdx.app.log("Camera", "Zoomed to: " + camera.zoom);
    			super.zoom(event, initialDistance, distance);
    		}
    	});

    }

    @Override
    public void show() {
        Gdx.app.log("MainScreen","show");
        Gdx.input.setInputProcessor(inputMult);

    }

    @Override
    public void render(float delta) {
//        //java 8
//        long secondFromStart = Instant.now().toEpochMilli()-startTime;
//        percent = (secondFromStart%animation_duration)/animation_duration;
//        percent = (float)Math.cos(percent*Math.PI*2)/2+0.5f;
//        Gdx.app.log("render","secondFromStart:"+ secondFromStart+", %:"+percent);
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        moveCamera();
        
        mapStage.act();
        guiStage.act();
        mapStage.draw();
        guiStage.draw();
    }

    private void moveCamera(){
//        float currentX = startX + (endX-startX)*percent;
//        float currentY = startY + (endY-startY)*percent;
//        float percentZ = Math.abs(percent - 0.5f)*2;
//        float currentZ = maxAltitude - (maxAltitude-minAltitude)*percentZ  ;
//
//        camera.position.x = currentX;
//        camera.position.y = currentY;
//        camera.zoom = currentZ;
    	
        camera.update();
    }


    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        mapStage.dispose();
        guiStage.dispose();
    }


}
