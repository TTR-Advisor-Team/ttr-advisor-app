package com.ttradvisor.app.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.utils.ArraySelection;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ttradvisor.app.TTRAdvisorApp;
import com.ttradvisor.app.classes.Colors;
import com.ttradvisor.app.classes.DestinationAction;
import com.ttradvisor.app.classes.DestinationTicket;
import com.ttradvisor.app.classes.DestinationTicketList;
import com.ttradvisor.app.classes.Player;
import com.ttradvisor.app.classes.TrainCard;
import com.ttradvisor.app.classes.TrainCardAction;

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
	private DestinationTicketList dtList;
	private ScrollPane listPane;
	private List<DestinationTicket> destTickets;
	private DestinationTicket[] ticketArray;
	private ArraySelection<DestinationTicket> ticketSelection;
	//private Array<DestinationTicket> multipleTickets;

	private List<Colors.player> demonstration; // TODO delete this after demo
	private Label demoCurrPlayer;

	private float mapWidth;
	private float mapHeight;

	public GameScreen(TTRAdvisorApp main) {
		mainApp = main;
		
		//get the initialized DestinationTicketList from GameState
		//need to handle if dtlist is somehow null
		dtList = main.gameState.getDtList();
		destTickets = new List<DestinationTicket>(TTRAdvisorApp.skin);
		//simple array of the DestinationTickets to pass into the Scrollpane
		ticketArray = new DestinationTicket[dtList.getList().size()];
		
		
		for (int i=0; i<dtList.getList().size(); i++) {
			ticketArray[i] = dtList.getTicket(i);
		}
		//Set an equivalent List<DestinationTicket> for the scrollpane
		destTickets.setItems(ticketArray);
		destTickets.setAlignment(Align.left);
		
		// Needed to edit destTicket's selection style in order to select multiple tickets
		ticketSelection = destTickets.getSelection();
		ticketSelection.setMultiple(true);
		destTickets.setSelection(ticketSelection);
		

		guiStage = new Stage(new ScreenViewport());
		mapStage = new Stage(new ScreenViewport());
		inputMult = new InputMultiplexer(guiStage, mapStage);
		
		// MOCKUP to show state internals
		// Delete this after
		demonstration = new List<Colors.player>(TTRAdvisorApp.skin);
		Array<Colors.player> demoColors = new Array<Colors.player>();
		for (Player p : mainApp.gameState.getPlayers()) {
			demoColors.add(p.getColor());
		}
		demonstration.setItems(demoColors);
		demonstration.setSize(200, 100);
		demonstration.setPosition(50, 500);
		guiStage.addActor(demonstration);
		
		demoCurrPlayer = new Label("", mainApp.skin);
		demoCurrPlayer.setColor(0, 0, 0, 1);
		demoCurrPlayer.setSize(100, 30);
		demoCurrPlayer.setPosition(50, 400);
		guiStage.addActor(demoCurrPlayer);
		
		// Button to draw Destination tickets
		final TextButton destButton = new TextButton("Draw Destination \n Ticket", TTRAdvisorApp.skin, "small");
		final TextButton TCButton = new TextButton("Draw Train \n Card", TTRAdvisorApp.skin, "small");
		// Button to draw Destination tickets
		destButton.setWidth(Gdx.graphics.getWidth() / 5);
		destButton.setPosition(Gdx.graphics.getWidth() - destButton.getWidth(), destButton.getHeight() / 8);
		// Jake: Still need to add way to choose DTs within this screen
		destButton.addListener(new InputListener(){
		@Override
		public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
		    destButton.setVisible(false);
		    TCButton.setVisible(false);
		    final Table table = new Table();
		    final ArrayList<DestinationTicket> drawnTickets = new ArrayList<>();
		    // Initialize ScrollPane that holds the list of DTs
		    listPane = new ScrollPane(destTickets, TTRAdvisorApp.skin);
            listPane.setX(Gdx.graphics.getWidth() * 3/5);
            listPane.setY(Gdx.graphics.getHeight()* 1/100);
            listPane.setHeight(Gdx.graphics.getHeight() * 1/2);
            listPane.setWidth(Gdx.graphics.getWidth()*1/3);
            guiStage.addActor(listPane);
            guiStage.setScrollFocus(listPane);
		    
            //Button to confirm DestinationAction with selected tickets
            TextButton done = new TextButton("Done", TTRAdvisorApp.skin, "small");
		    done.addListener(new InputListener() {
		    	public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
		    		//Should check if initial turn, when 2-3 tickets allowed (edge cases)
		    		drawnTickets.add(destTickets.getSelected());		
		        	
		        	mainApp.turnInput.takeAction(new DestinationAction(mainApp.gameState.currentPlayer, drawnTickets));
		        	
		        	guiStage.setScrollFocus(null);
		        	
		    		destButton.setVisible(true);
		    		TCButton.setVisible(true);
		    		listPane.setVisible(false);
				    table.setVisible(false);  	
		    	}
		    	public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
		    		return true;
		    	}
		    });
		    
		    //Button to return to the GameScreen without inputting the action
		    TextButton back = new TextButton("Back", TTRAdvisorApp.skin, "small");
        	back.addListener(new InputListener() {
        		public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
        			drawnTickets.removeAll(drawnTickets);
        			
		        	guiStage.setScrollFocus(null);
        			
                	destButton.setVisible(true);
                	TCButton.setVisible(true);
                	listPane.setVisible(false);
                	table.setVisible(false);
        		}
        		public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                    return true;
                }
        	});
        	//Populate the table with back and done buttons
        	table.bottom();
        	table.add(back);
        	table.row();
        	table.add(done);
        	table.setFillParent(true);
        	
        	guiStage.addActor(table);
		 }
		 @Override
		 public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
		        return true;
		 }
		 });
		guiStage.addActor(destButton);
		
		
		TCButton.setHeight(destButton.getHeight());
		TCButton.setPosition(0, TCButton.getHeight() / 8);
        TCButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            	final Table table = new Table();
            	destButton.setVisible(false);
            	TCButton.setVisible(false);
            	final ArrayList<TrainCard> drawnCards = new ArrayList<>();
            	TextButton redTrain = new TextButton("Red", TTRAdvisorApp.skin, "small");
            	redTrain.addListener(new InputListener() {
            		public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            			drawnCards.add(new TrainCard(Colors.route.RED));
            		}
            		public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
            	});
            	TextButton orangeTrain = new TextButton("Orange", TTRAdvisorApp.skin, "small");
            	orangeTrain.addListener(new InputListener() {
            		public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            			drawnCards.add(new TrainCard(Colors.route.ORANGE));
            		}
            		public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
            	});
            	TextButton yellowTrain = new TextButton("Yellow", TTRAdvisorApp.skin, "small");
            	yellowTrain.addListener(new InputListener() {
            		public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            			drawnCards.add(new TrainCard(Colors.route.YELLOW));
            		}
            		public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
            	});
            	TextButton greenTrain = new TextButton("Green", TTRAdvisorApp.skin, "small");
            	greenTrain.addListener(new InputListener() {
            		public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            			drawnCards.add(new TrainCard(Colors.route.GREEN));
            		}
            		public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
            	});
            	TextButton blueTrain = new TextButton("Blue", TTRAdvisorApp.skin, "small");
            	blueTrain.addListener(new InputListener() {
            		public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            			drawnCards.add(new TrainCard(Colors.route.BLUE));
            		}
            		public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
            	});
            	TextButton pinkTrain = new TextButton("Pink", TTRAdvisorApp.skin, "small");
            	pinkTrain.addListener(new InputListener() {
            		public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            			drawnCards.add(new TrainCard(Colors.route.PINK));
            		}
            		public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
            	});
            	TextButton blackTrain = new TextButton("Black", TTRAdvisorApp.skin, "small");
            	blackTrain.addListener(new InputListener() {
            		public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            			drawnCards.add(new TrainCard(Colors.route.BLACK));
            		}
            		public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
            	});
            	TextButton whiteTrain = new TextButton("White", TTRAdvisorApp.skin, "small");
            	whiteTrain.addListener(new InputListener() {
            		public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            			drawnCards.add(new TrainCard(Colors.route.WHITE));
            		}
            		public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
            	});
            	TextButton wildTrain = new TextButton("Wild", TTRAdvisorApp.skin, "small");
            	wildTrain.addListener(new InputListener() {
            		public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            			drawnCards.add(new TrainCard(Colors.route.ANY));
            		}
            		public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
            	});
            	TextButton done = new TextButton("Done", TTRAdvisorApp.skin, "small");
            	done.addListener(new InputListener() {
            		public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            			
            			// MOCKUP
            			// TODO this should work once the other bits are in place
            			mainApp.turnInput.takeAction(new TrainCardAction(mainApp.gameState.currentPlayer, drawnCards));
            			
            			
            			
            			destButton.setVisible(true);
                    	TCButton.setVisible(true);
                    	table.setVisible(false);
            		}
            		public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
            	});
            	TextButton undo = new TextButton("Undo", TTRAdvisorApp.skin, "small");
            	whiteTrain.addListener(new InputListener() {
            		public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            			drawnCards.remove(drawnCards.size()-1);
            		}
            		public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
            	});
            	TextButton back = new TextButton("Back", TTRAdvisorApp.skin, "small");
            	back.addListener(new InputListener() {
            		public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            			drawnCards.removeAll(drawnCards);
                    	destButton.setVisible(true);
                    	TCButton.setVisible(true);
                    	table.setVisible(false);
            		}
            		public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
            	});
            	TextButton invis = new TextButton("", TTRAdvisorApp.skin, "small");
            	invis.setVisible(false);
            	table.bottom();
            	table.add(invis);
            	table.add(redTrain);
            	table.add(orangeTrain);
            	table.add(yellowTrain);
            	table.row();
            	table.add(invis);
            	table.add(greenTrain);
            	table.add(blueTrain);
            	table.add(pinkTrain);
            	table.add(back);
            	table.row();
            	table.add(undo);
            	table.add(blackTrain);
            	table.add(whiteTrain);
            	table.add(wildTrain);
            	table.add(done);
                table.setFillParent(true);
                
//              table.setDebug(true); // turn on all debug lines (table, cell, and widget)
                guiStage.addActor(table);
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
		
		guiStage.addActor(TCButton);

		setupMapInputHandling();

		Image map = new Image(new Texture("low_res_board.jpg"));

		// notes: doing this is risky (I think) because texture coordinates != world
		// coordinates
		// but I guess it's fine as long as we never scale the textures independently of
		// the world
		// (so that 1px in texture is always == 1px in world)
		mapWidth = map.getWidth();
		mapHeight = map.getHeight();

		mapStage.addActor(map);
		camera = (OrthographicCamera) mapStage.getViewport().getCamera();
		camera.setToOrtho(false);
		camera.zoom = 1;

		camera.position.set(0, 0, camera.position.z);
		clampCamera();

	}

	/**
	 * Handlers for mapStage ActorGestureListener used here for sake of identifying
	 * zooms/pans on android
	 */
	private void setupMapInputHandling() {

		mapStage.addListener(new InputListener() {

			@Override
			public boolean scrolled(InputEvent event, float x, float y, int amount) {
				camera.zoom += (float) amount / 10f;
				clampCamera();
				// Gdx.app.log("Camera", "Zoomed to: " + camera.zoom);
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

				if (panOriginScreen == null) {
					panOriginScreen = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
					panOriginCamera = new Vector3(camera.position);
				}
				panCurrentScreen = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);

				trueScreenDelta = new Vector3((panOriginScreen.x - panCurrentScreen.x) * camera.zoom,
						(panOriginScreen.y - panCurrentScreen.y) * camera.zoom, 0);

				camera.position.set(panOriginCamera.x + trueScreenDelta.x, panOriginCamera.y - trueScreenDelta.y,
						camera.position.z);
				// Gdx.app.log("Camera", "Translated to coords: " + camera.position.x + ", " + camera.position.y);
				super.pan(event, x, y, deltaX, deltaY);
			}

			@Override
			public void panStop(InputEvent event, float x, float y, int pointer, int button) {
				clampCamera();
				panOriginScreen = null;
				super.panStop(event, x, y, pointer, button);
			}

			@Override
			public void zoom(InputEvent event, float initialDistance, float distance) {
				float ratio = (initialDistance / distance) * camera.zoom;
				camera.zoom = ratio;
				clampCamera();
				// Gdx.app.log("Camera", "Zoomed to: " + camera.zoom);
				super.zoom(event, initialDistance, distance);
			}
			
		});

	}

	private void clampCamera() {

		camera.zoom = MathUtils.clamp(camera.zoom, Math.min(mapHeight/camera.viewportHeight, mapWidth/camera.viewportWidth) * 0.2f, Math.min(mapHeight/camera.viewportHeight, mapWidth/camera.viewportWidth));

		float effectiveViewportWidth = camera.viewportWidth * camera.zoom;
		float effectiveViewportHeight = camera.viewportHeight * camera.zoom;

		camera.position.x = MathUtils.clamp(camera.position.x, effectiveViewportWidth / 2f,
				mapWidth - effectiveViewportWidth / 2f);
		camera.position.y = MathUtils.clamp(camera.position.y, effectiveViewportHeight / 2f,
				mapHeight - effectiveViewportHeight / 2f);

		// Gdx.app.log("Camera", "Clamped to coords: " + camera.position.x + ", " + camera.position.y);
	}

	@Override
	public void show() {
		Gdx.app.log("MainScreen", "show");
		Gdx.input.setInputProcessor(inputMult);

	}

	@Override
	public void render(float delta) {
		demoCurrPlayer.setText("Current player: " + mainApp.gameState.currentPlayer.getColor());
		Gdx.gl.glClearColor(.58f, .71f, .78f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();

		mapStage.act();
		guiStage.act();
		mapStage.draw();
		guiStage.draw();
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
