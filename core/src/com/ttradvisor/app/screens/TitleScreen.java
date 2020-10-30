package com.ttradvisor.app.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
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
import com.ttradvisor.app.classes.Player;

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
    	batch = new SpriteBatch();
		img = new Texture("ttr.jpeg");
		
        mainApp = main;
        stage = new Stage(new ScreenViewport());

        // Title Screen label
        Label title = new Label("Ticket to Ride Advisor", TTRAdvisorApp.skin,"big-black");
        title.setAlignment(Align.center);
        title.setY(Gdx.graphics.getHeight() - title.getHeight());
        title.setWidth(Gdx.graphics.getWidth());
        stage.addActor(title);
        
        // Box to select number of players (2-5)
        final SelectBox<Integer> numPlayers = new SelectBox<Integer>(TTRAdvisorApp.skin);
        numPlayers.setWidth(Gdx.graphics.getWidth() / 4);
        numPlayers.setHeight(Gdx.graphics.getHeight()/16);
        numPlayers.setPosition(Gdx.graphics.getWidth()/2- numPlayers.getWidth()*7/5,
				Gdx.graphics.getHeight() - numPlayers.getHeight()*4);
        numPlayers.setItems(2,3,4,5);
		
		// Label for the above selection box
		Label numTitle = new Label("# of Players", TTRAdvisorApp.skin,"black");
        numTitle.setPosition(numPlayers.getX(),	numPlayers.getY() + numPlayers.getHeight()*3/2);
        numTitle.setWidth(Gdx.graphics.getWidth()/4);
        numTitle.setHeight(title.getHeight()/2);
        numTitle.setFontScale(5/2);
        stage.addActor(numTitle);
        
        // Label for selection of colors
        Label colorSel = new Label("Input turn order:", TTRAdvisorApp.skin,"black");
        colorSel.setPosition(numPlayers.getX() , numPlayers.getY() - numPlayers.getHeight());
        colorSel.setWidth(Gdx.graphics.getWidth()/4);
        colorSel.setHeight(numTitle.getHeight());
        colorSel.setFontScale(5/2);
        stage.addActor(colorSel);
        
        // Label for user color selection
        Label userSel = new Label("Select your color:", TTRAdvisorApp.skin,"black");
        userSel.setPosition(Gdx.graphics.getWidth()/2 , numTitle.getY());
        userSel.setWidth(Gdx.graphics.getWidth()/4);
        userSel.setHeight(numTitle.getHeight());
        userSel.setFontScale(5/2);
        stage.addActor(userSel);
        
        // user color selection
        final SelectBox<String> userCol = new SelectBox<String>(TTRAdvisorApp.skin);
        userCol.setWidth(Gdx.graphics.getWidth() / 4);
        userCol.setHeight(Gdx.graphics.getHeight()/16);
        userCol.setPosition(userSel.getX(), userSel.getY() - userCol.getHeight()*3/2);
        userCol.setItems("Black", "Blue", "Green", "Red", "Yellow");
        stage.addActor(userCol);
        
        // turn order 1 label
        Label first = new Label("1st:", TTRAdvisorApp.skin);
        first.setPosition(Gdx.graphics.getWidth()/2- numPlayers.getWidth()*9/5, colorSel.getY() - numPlayers.getHeight()*3/2);
        first.setWidth(Gdx.graphics.getWidth()/4);
        first.setHeight(numTitle.getHeight());
        first.setFontScale(5/2);
        stage.addActor(first);
        
        // turn order 1 color selection
        final SelectBox<String> order1 = new SelectBox<String>(TTRAdvisorApp.skin);
        order1.setWidth(Gdx.graphics.getWidth() / 4);
        order1.setHeight(Gdx.graphics.getHeight()/16);
        order1.setPosition(colorSel.getX(), first.getY());
        order1.setItems("Black", "Blue", "Green", "Red", "Yellow");
        stage.addActor(order1);
        
        // turn order 2 label
        Label second = new Label("2nd:", TTRAdvisorApp.skin);
        second.setPosition(first.getX(), first.getY() - numPlayers.getHeight()*3/2);
        second.setWidth(first.getWidth());
        second.setHeight(numTitle.getHeight());
        second.setFontScale(5/2);
        stage.addActor(second);

        // turn order 2 color selection
        final SelectBox<String> order2 = new SelectBox<String>(TTRAdvisorApp.skin);
        order2.setWidth(order1.getWidth());
        order2.setHeight(order1.getHeight());
        order2.setPosition(order1.getX(), second.getY());
        order2.setItems("Black", "Blue", "Green", "Red", "Yellow");
        stage.addActor(order2);
        
        // turn order 3 label
        final Label third = new Label("3rd:", TTRAdvisorApp.skin);
        third.setPosition(first.getX(), second.getY() - numPlayers.getHeight()*3/2);
        third.setWidth(first.getWidth());
        third.setHeight(numTitle.getHeight());
        third.setFontScale(5/2);
        stage.addActor(third);
        third.setVisible(false);

        // turn order 3 color selection
        final SelectBox<String> order3 = new SelectBox<String>(TTRAdvisorApp.skin);
        order3.setWidth(order1.getWidth());
        order3.setHeight(order1.getHeight());
        order3.setPosition(order1.getX(), third.getY());
        order3.setItems("Black", "Blue", "Green", "Red", "Yellow");
        stage.addActor(order3);
        order3.setVisible(false);

        // turn order 4 label
        final Label fourth = new Label("4th:", TTRAdvisorApp.skin);
        fourth.setPosition(first.getX(), third.getY() - numPlayers.getHeight()*3/2);
        fourth.setWidth(first.getWidth());
        fourth.setHeight(numTitle.getHeight());
        fourth.setFontScale(5/2);
        stage.addActor(fourth);
        fourth.setVisible(false);
        
        // turn order 4 color selection
        final SelectBox<String> order4 = new SelectBox<String>(TTRAdvisorApp.skin);
        order4.setWidth(order1.getWidth());
        order4.setHeight(order1.getHeight());
        order4.setPosition(order1.getX(), fourth.getY());
        order4.setItems("Black", "Blue", "Green", "Red", "Yellow");
        stage.addActor(order4);
        order4.setVisible(false);
        
        // turn order 5 label
        final Label fifth = new Label("5th:", TTRAdvisorApp.skin);
        fifth.setPosition(first.getX(), fourth.getY() - numPlayers.getHeight()*3/2);
        fifth.setWidth(first.getWidth());
        fifth.setHeight(numTitle.getHeight());
        fifth.setFontScale(5/2);
        stage.addActor(fifth);
        fifth.setVisible(false);

        // turn order 5 color selection
        final SelectBox<String> order5 = new SelectBox<String>(TTRAdvisorApp.skin);
        order5.setWidth(order1.getWidth());
        order5.setHeight(order1.getHeight());
        order5.setPosition(order1.getX(), fifth.getY());
        order5.setItems("Black", "Blue", "Green", "Red", "Yellow");
        stage.addActor(order5);
        order5.setVisible(false);
        
        numPlayers.addListener(new ChangeListener(){
            @Override
            public void changed (ChangeEvent event, Actor actor){
            	switch(numPlayers.getSelected().intValue()) {
            	case(2):
            		third.setVisible(false);
            		order3.setVisible(false);
            		fourth.setVisible(false);
            		order4.setVisible(false);
            		fifth.setVisible(false);
            		order5.setVisible(false);
            		break;
            	case(3):
            		third.setVisible(true);
            		order3.setVisible(true);
            		fourth.setVisible(false);
            		order4.setVisible(false);
            		fifth.setVisible(false);
            		order5.setVisible(false);
            		break;
            	case(4):
            		third.setVisible(true);
            		order3.setVisible(true);
            		fourth.setVisible(true);
            		order4.setVisible(true);
            		fifth.setVisible(false);
            		order5.setVisible(false);
            		break;
            	case(5):
            		third.setVisible(true);
            		order3.setVisible(true);
            		fourth.setVisible(true);
            		order4.setVisible(true);
            		fifth.setVisible(true);
            		order5.setVisible(true);
            		break;
            	}
            }
        });
		stage.addActor(numPlayers);

        // Button to access GameScreen
        TextButton playButton = new TextButton("Play!", TTRAdvisorApp.skin, "small");
        playButton.setWidth(Gdx.graphics.getWidth()/4);
        playButton.setHeight(playButton.getHeight()*2);
        playButton.setPosition(Gdx.graphics.getWidth()-playButton.getWidth(),playButton.getHeight()/8);
        playButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            	ArrayList<String> tOrder = new ArrayList<String>();
            	ArrayList<Colors.player> order = new ArrayList<Colors.player>();
            	switch(numPlayers.getSelected()) {
            	case(2):
            		tOrder.add(order1.getSelected());
            		tOrder.add(order2.getSelected());
            	;
            	case(3):
            		tOrder.add(order1.getSelected());
            		tOrder.add(order2.getSelected());
            		tOrder.add(order3.getSelected());
            	;
            	case(4):
            		tOrder.add(order1.getSelected());
            		tOrder.add(order2.getSelected());
            		tOrder.add(order3.getSelected());
            		tOrder.add(order4.getSelected());
            	;
            	case(5):
            		tOrder.add(order1.getSelected());
            		tOrder.add(order2.getSelected());
            		tOrder.add(order3.getSelected());
            		tOrder.add(order4.getSelected());
            		tOrder.add(order5.getSelected());
            	;
            	}
            	for(int i = 0; i < tOrder.size(); i++) {
            		for(Colors.player col : Colors.player.values()) {
            			if(col.toString().toLowerCase().equals(tOrder.get(i))) 
            					order.add(col);	
            			}
            		}
            	mainApp.turnOrder = order;
            	mainApp.numPlayers = numPlayers.getSelected();
            	
            	// MOCKUP
            	
            	// TODO set up gameState here before triggering the initial turn & going to GameScreen
            	// Instead of BLACK they should all get the right colors
            	
            	for (int i=0; i<numPlayers.getSelectedIndex() + 2; i++) {
            		mainApp.gameState.getPlayers().add(new Player(Colors.player.BLACK));
            	}
            	
            	mainApp.turnInput.startInitialTurn(); // set up controller for start
            	mainApp.setScreen(new GameScreen(mainApp));
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(playButton);
        
        // Button to access TutorialScreen
        TextButton tutorialButton = new TextButton("Tutorial", TTRAdvisorApp.skin, "small");
        tutorialButton.setWidth(Gdx.graphics.getWidth()/4);
        tutorialButton.setHeight(playButton.getHeight());
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
