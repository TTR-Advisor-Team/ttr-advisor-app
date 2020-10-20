package com.ttradvisor.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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

//    private final int startX = 1100;// -Gdx.graphics.getWidth()/2;
//    private final int startY = 1225;
    
    private float cameraX;
    private float cameraY;

//    private final int endX = 2350;// -Gdx.graphics.getWidth()/2;
//    private final int endY = 600;
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

        Image map = new Image(new Texture("map.jpg"));
        
        mapStage.addActor(map);
        camera = (OrthographicCamera) mapStage.getViewport().getCamera();
        camera.setToOrtho(true);
        // startTime = Instant.now().toEpochMilli();

    }
    
    
    // Andrey's thoughts
    // might need to do these events on the map Image instead
    // stage vs. world coordinates (?)
    private void setupMapInputHandling() {
    	mapStage.addListener(new ClickListener() {

    		@Override
    		public void clicked(InputEvent event, float x, float y) {
    			cameraX = x;
    			cameraY = y;
    			camera.position.set(cameraX, cameraY, camera.position.z);
    			super.clicked(event, x, y);
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
