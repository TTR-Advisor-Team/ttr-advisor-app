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
        numPlayers.setPosition(Gdx.graphics.getWidth()/2- numPlayers.getWidth()*9/5,
				Gdx.graphics.getHeight() - numPlayers.getHeight()*4);
        numPlayers.setItems(2,3,4,5);
		
		// Label for the above selection box
		Label numTitle = new Label("# of Players", TTRAdvisorApp.skin,"black");
        numTitle.setPosition(numPlayers.getX(),	numPlayers.getY() + numTitle.getHeight()*2);
        numTitle.setWidth(Gdx.graphics.getWidth()/4);
        numTitle.setHeight(title.getHeight()/2);
        stage.addActor(numTitle);
        
        // Label for selection of colors
        Label colorSel = new Label("Player Colors:", TTRAdvisorApp.skin,"black");
        colorSel.setPosition(numPlayers.getX() , numPlayers.getY() - numPlayers.getHeight());
        colorSel.setWidth(Gdx.graphics.getWidth()/4);
        colorSel.setHeight(numTitle.getHeight());
        stage.addActor(colorSel);
        
        // Black color select
        Label black = new Label(Colors.player.BLACK.toString(), TTRAdvisorApp.skin,"black");
        black.setPosition(numPlayers.getX(), colorSel.getY() - colorSel.getHeight()*5/3);
        black.setWidth(Gdx.graphics.getWidth()/4);
        black.setHeight(Gdx.graphics.getHeight()/40);
        black.setFontScale(2);
        
        // Checkbox for black color is being used
        final CheckBox bkUsedBox = new CheckBox("Yes", TTRAdvisorApp.skin);
        // used for X of checked box
        float  corX = black.getWidth();
        // used for Y of checked box
        float corY =  bkUsedBox.getHeight()/4;
        bkUsedBox.setScale(8);
        bkUsedBox.setPosition(black.getX()  + corX, black.getY() - corY);
        // Checkbox for black color not being used
        final CheckBox bkNotUsedBox = new CheckBox("No", TTRAdvisorApp.skin);
        bkNotUsedBox.setPosition(bkUsedBox.getX() + corX/2, black.getY() - corY);
        // Check whether tmax players is selected
        if(numPlayers.getSelected().equals(5)){
        	bkUsedBox.setChecked(true);
        }
        else {
        	bkNotUsedBox.setChecked(true);
        }
        // Listeners for check boxes
        bkUsedBox.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button)
            {
            	if (bkUsedBox.isChecked()) {
            		bkNotUsedBox.setChecked(false);
            	}
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        bkNotUsedBox.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button)
            {
            	if (bkNotUsedBox.isChecked()) {
            		bkUsedBox.setChecked(false);
            	}
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(black);
        stage.addActor(bkUsedBox);
        stage.addActor(bkNotUsedBox);
        
        //Blue color select
        Label blue = new Label(Colors.player.BLUE.toString(), TTRAdvisorApp.skin,"black");
        blue.setPosition(numPlayers.getX(), black.getY() - black.getHeight()*5/2);
        blue.setWidth(black.getWidth());
        blue.setHeight(black.getHeight());
        blue.setFontScale(black.getFontScaleX(), black.getFontScaleY());

        // Checkbox for blue color is being used
        final CheckBox blUsedBox = new CheckBox("Yes", TTRAdvisorApp.skin);
        blUsedBox.setPosition(blue.getX() + corX, blue.getY() - corY);
        // Checkbox for blue color not being used
        final CheckBox blNotUsedBox = new CheckBox("No", TTRAdvisorApp.skin);
        blNotUsedBox.setPosition(blUsedBox.getX()  + corX/2, blue.getY()- corY);
        // Check whether max players is selected
        if(numPlayers.getSelected() == 5){
        	blUsedBox.setChecked(true);
        }
        else {
        	blNotUsedBox.setChecked(true);
        }
        // Listeners for check boxes
        blUsedBox.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button)
            {
            	if (blUsedBox.isChecked()) {
            		blNotUsedBox.setChecked(false);
            	}
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        blNotUsedBox.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button)
            {
            	if (blNotUsedBox.isChecked()) {
            		blUsedBox.setChecked(false);
            	}
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(blue);
        stage.addActor(blUsedBox);
        stage.addActor(blNotUsedBox);
        
        // Green color select
        Label green = new Label(Colors.player.GREEN.toString(), TTRAdvisorApp.skin,"black");
        green.setPosition(numPlayers.getX(), blue.getY() - blue.getHeight()*5/2);
        green.setWidth(black.getWidth());
        green.setHeight(black.getHeight());
        green.setFontScale(black.getFontScaleX(), black.getFontScaleY());
        
        // Checkbox for green color is being used
        final CheckBox grUsedBox = new CheckBox("Yes", TTRAdvisorApp.skin);
        grUsedBox.setPosition(green.getX() + corX, green.getY() - corY);
        // Checkbox for green color not being used
        final CheckBox grNotUsedBox = new CheckBox("No", TTRAdvisorApp.skin);
        grNotUsedBox.setPosition(grUsedBox.getX() + corX/2, green.getY() - corY);
        // Check whether max players is selected
        if(numPlayers.getSelected() == 5){
        	grUsedBox.setChecked(true);
        }
        else {
        	grNotUsedBox.setChecked(true);
        }
        // Listeners for check boxes
        grUsedBox.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button)
            {
            	if (grUsedBox.isChecked()) {
            		grNotUsedBox.setChecked(false);
            	}
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        grNotUsedBox.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button)
            {
            	if (grNotUsedBox.isChecked()) {
            		grUsedBox.setChecked(false);
            	}
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(green);
        stage.addActor(grUsedBox);
        stage.addActor(grNotUsedBox);
        
        // Red color select
        Label red = new Label(Colors.player.RED.toString(), TTRAdvisorApp.skin,"black");
        red.setPosition(numPlayers.getX(), green.getY() - green.getHeight()*5/2);
        red.setWidth(black.getWidth());
        red.setHeight(black.getHeight());
        red.setFontScale(black.getFontScaleX(), black.getFontScaleY());
        
        // Checkbox for red color is being used
        final CheckBox rdUsedBox = new CheckBox("Yes", TTRAdvisorApp.skin);
        rdUsedBox.setPosition(red.getX() + corX, red.getY() - corY);
        // Checkbox for red color not being used
        final CheckBox rdNotUsedBox = new CheckBox("No", TTRAdvisorApp.skin);
        rdNotUsedBox.setPosition(rdUsedBox.getX() + corX/2, red.getY() - corY);
        // Check whether max players is selected
        if(numPlayers.getSelected() == 5){
        	rdUsedBox.setChecked(true);
        }
        else {
        	rdNotUsedBox.setChecked(true);
        }
        // Listeners for check boxes
        rdUsedBox.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button)
            {
            	if (rdUsedBox.isChecked()) {
            		rdNotUsedBox.setChecked(false);
            	}
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        rdNotUsedBox.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button)
            {
            	if (rdNotUsedBox.isChecked()) {
            		rdUsedBox.setChecked(false);
            	}
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(red);
        stage.addActor(rdUsedBox);
        stage.addActor(rdNotUsedBox);
        
        // Yellow color select
        Label yellow = new Label(Colors.player.YELLOW.toString(), TTRAdvisorApp.skin,"black");
        yellow.setPosition(numPlayers.getX(), red.getY() - red.getHeight()*5/2);
        yellow.setWidth(black.getWidth());
        yellow.setHeight(black.getHeight());
        yellow.setFontScale(black.getFontScaleX(), black.getFontScaleY());

        // Checkbox for yellow color is being used
        final CheckBox ywUsedBox = new CheckBox("Yes", TTRAdvisorApp.skin);
        ywUsedBox.setPosition(yellow.getX() + corX, yellow.getY() - corY);
        // Checkbox for yellow color not being used
        final CheckBox ywNotUsedBox = new CheckBox("No", TTRAdvisorApp.skin);
        ywNotUsedBox.setPosition(ywUsedBox.getX() + corX/2, yellow.getY() - corY);
        // Check whether max players is selected
        if(numPlayers.getSelected() == 5){
        	ywUsedBox.setChecked(true);
        }
        else {
        	ywNotUsedBox.setChecked(true);
        }
        // Listeners for check boxes
        ywUsedBox.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button)
            {
            	if (ywUsedBox.isChecked()) {
            		ywNotUsedBox.setChecked(false);
            	}
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        ywNotUsedBox.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button)
            {
            	if (ywNotUsedBox.isChecked()) {
            		ywUsedBox.setChecked(false);
            	}
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(yellow);
        stage.addActor(ywUsedBox);
        stage.addActor(ywNotUsedBox);
        
        numPlayers.addListener(new ChangeListener(){
            @Override
            public void changed (ChangeEvent event, Actor actor){
            	// if 5 players is selected then all the colors must be used
            	if(numPlayers.getSelected() == 5) {
            		bkUsedBox.setChecked(true);
            		blUsedBox.setChecked(true);
            		grUsedBox.setChecked(true);
            		rdUsedBox.setChecked(true);
            		ywUsedBox.setChecked(true);
            		
            		bkNotUsedBox.setChecked(false);
            		blNotUsedBox.setChecked(false);
            		grNotUsedBox.setChecked(false);
            		rdNotUsedBox.setChecked(false);
            		ywNotUsedBox.setChecked(false);
            	}
            	else {
            		bkNotUsedBox.setChecked(true);
            		blNotUsedBox.setChecked(true);
            		grNotUsedBox.setChecked(true);
            		rdNotUsedBox.setChecked(true);
            		ywNotUsedBox.setChecked(true);
            		
            		bkUsedBox.setChecked(false);
            		blUsedBox.setChecked(false);
            		grUsedBox.setChecked(false);
            		rdUsedBox.setChecked(false);
            		ywUsedBox.setChecked(false);
            	}
            }
        });
		stage.addActor(numPlayers);
        
        Label used = new Label("Used:", TTRAdvisorApp.skin,"black");
        used.setWidth(colorSel.getWidth());
        used.setPosition(bkUsedBox.getX(), colorSel.getY());
        used.setHeight(colorSel.getHeight());
        stage.addActor(used);
        
        final TextField turnOrder = new TextField("", TTRAdvisorApp.skin);
        turnOrder.setWidth(numPlayers.getWidth());
        turnOrder.setHeight(numPlayers.getHeight());
        turnOrder.setPosition(Gdx.graphics.getWidth()/2, numPlayers.getY());
        turnOrder.setMessageText("Turn Order: (Ex: black,blue,green");
        stage.addActor(turnOrder);

        // Button to access GameScreen
        TextButton playButton = new TextButton("Play!", TTRAdvisorApp.skin, "small");
        playButton.setWidth(Gdx.graphics.getWidth()/4);
        playButton.setHeight(playButton.getHeight()*2);
        playButton.setPosition(Gdx.graphics.getWidth()-playButton.getWidth(),playButton.getHeight()/8);
        playButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            	String turns = turnOrder.getText();
            	String[] tOrder = turns.split(",");
            	ArrayList<Colors.player> order = new ArrayList<Colors.player>();
            	for(int i = 0; i < tOrder.length; i++) {
            		for(Colors.player col : Colors.player.values()) {
            			if(col.toString().toLowerCase().equals(tOrder[i])) {
            				order.add(col);
            			}
            		}
            	}
            	mainApp.turnOrder = order;
            	mainApp.numPlayers = numPlayers.getSelected();
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
