package com.ttradvisor.app.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ttradvisor.app.TTRAdvisorApp;
import com.ttradvisor.app.classes.Colors;

/**
 * Created by julienvillegas on 17/01/2017.
 * 
 * Copied in as example code to modify
 */
public class TitleScreen implements Screen {

    private Stage stage;
    private TTRAdvisorApp mainApp;
    
    private SpriteBatch batch;
	private Texture img;

    public TitleScreen(TTRAdvisorApp main) {
    	final int players = 2;
    	batch = new SpriteBatch();
		img = new Texture("ttr.jpeg");
		
        mainApp = main;
        stage = new Stage(new ScreenViewport());

        Label title = new Label("Ticket to Ride Advisor", TTRAdvisorApp.skin,"big-black");
        title.setAlignment(Align.center);
        title.setY(Gdx.graphics.getHeight() - title.getHeight());
        title.setWidth(Gdx.graphics.getWidth());
        stage.addActor(title);
        
        SelectBox<Integer> numPlayers = new SelectBox<Integer>(TTRAdvisorApp.skin);
        numPlayers.setWidth(Gdx.graphics.getWidth() / 4);
        numPlayers.setPosition(Gdx.graphics.getWidth()/2 - numPlayers.getWidth()*5/4,
				Gdx.graphics.getHeight() / 2 - numPlayers.getHeight() / 2);
        numPlayers.setItems(2,3,4,5);
        numPlayers.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				
			}
        });
        numPlayers.setSelected(2);
		stage.addActor(numPlayers);
		
		Label numTitle = new Label("# of Players", TTRAdvisorApp.skin,"black");
        numTitle.setPosition(Gdx.graphics.getWidth()/2 - numPlayers.getWidth()*5/4,
				Gdx.graphics.getHeight() / 2 + numPlayers.getHeight()*3/4);
        numTitle.setWidth(Gdx.graphics.getWidth()/4);
        numTitle.setHeight(title.getHeight()/6);
        stage.addActor(numTitle);
        
        Label black = new Label(Colors.player.BLACK.toString(), TTRAdvisorApp.skin,"black");
        black.setPosition(Gdx.graphics.getWidth()/2 , numPlayers.getY() + numPlayers.getHeight()/2);
        black.setWidth(Gdx.graphics.getWidth()/4);
        black.setHeight(title.getHeight()/8);
        stage.addActor(black);
        
        Label blue = new Label(Colors.player.BLUE.toString(), TTRAdvisorApp.skin,"black");
        blue.setPosition(Gdx.graphics.getWidth()/2, black.getY() - black.getHeight()*2);
        blue.setWidth(Gdx.graphics.getWidth()/4);
        blue.setHeight(title.getHeight()/8);
        stage.addActor(blue);
        
        Label green = new Label(Colors.player.GREEN.toString(), TTRAdvisorApp.skin,"black");
        green.setPosition(Gdx.graphics.getWidth()/2, blue.getY() - blue.getHeight()*2);
        green.setWidth(Gdx.graphics.getWidth()/4);
        green.setHeight(title.getHeight()/8);
        stage.addActor(green);
        
        Label red = new Label(Colors.player.RED.toString(), TTRAdvisorApp.skin,"black");
        red.setPosition(Gdx.graphics.getWidth()/2, green.getY() - green.getHeight()*2);
        red.setWidth(Gdx.graphics.getWidth()/4);
        red.setHeight(title.getHeight()/8);
        stage.addActor(red);
        
        Label yellow = new Label(Colors.player.YELLOW.toString(), TTRAdvisorApp.skin,"black");
        yellow.setPosition(Gdx.graphics.getWidth()/2, red.getY() - red.getHeight()*2);
        yellow.setWidth(Gdx.graphics.getWidth()/4);
        yellow.setHeight(title.getHeight()/8);
        stage.addActor(yellow);
        
        Label colorSel = new Label("Player Colors", TTRAdvisorApp.skin,"black");
        colorSel.setPosition(Gdx.graphics.getWidth()/2, black.getY() + black.getHeight()*5/2);
        colorSel.setWidth(Gdx.graphics.getWidth()/4);
        colorSel.setHeight(title.getHeight()/6);
        stage.addActor(colorSel);

        TextButton playButton = new TextButton("Play!", TTRAdvisorApp.skin, "small");
        playButton.setWidth(Gdx.graphics.getWidth()/4);
        playButton.setPosition(Gdx.graphics.getWidth()-playButton.getWidth(),playButton.getHeight()/8);
        playButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                mainApp.setScreen(new GameScreen(mainApp));
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(playButton);
        
        TextButton tutorialButton = new TextButton("Tutorial", TTRAdvisorApp.skin, "small");
        tutorialButton.setWidth(Gdx.graphics.getWidth()/4);
        tutorialButton.setPosition(0,tutorialButton.getHeight()/8);
        tutorialButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(tutorialButton);

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
		batch.draw(img, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.end();
        stage.act();
        stage.draw();
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
        stage.dispose();
    }
}
