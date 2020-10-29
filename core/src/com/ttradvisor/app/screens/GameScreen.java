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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ttradvisor.app.TTRAdvisorApp;
import com.ttradvisor.app.classes.TrainCard;

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

	public GameScreen(TTRAdvisorApp main) {
		mainApp = main;
		guiStage = new Stage(new ScreenViewport());
		mapStage = new Stage(new ScreenViewport());
		inputMult = new InputMultiplexer(guiStage, mapStage);

		// Button to draw Destination tickets
		final TextButton destButton = new TextButton("Draw DT", TTRAdvisorApp.skin, "small");
		destButton.setWidth(Gdx.graphics.getWidth() / 5);
		destButton.setPosition(Gdx.graphics.getWidth() - destButton.getWidth(), destButton.getHeight() / 8);
		// Jake: Still need to add way to choose DTs within this screen
//        destButton.addListener(new InputListener(){
//            @Override
//            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
//        	 	  
//            }
//            @Override
//            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
//                return true;
//            }
//        });
		
		guiStage.addActor(destButton);
		
		final TextButton TCButton = new TextButton("Draw Train \n Card", TTRAdvisorApp.skin, "small");
		TCButton.setHeight(destButton.getHeight());
		TCButton.setPosition(0, TCButton.getHeight() / 8);
        TCButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            	destButton.setVisible(false);
            	TCButton.setVisible(false);
            	final ArrayList<TrainCard> drawnCards = new ArrayList<>();
            	TextButton redTrain = new TextButton("Red", TTRAdvisorApp.skin, "small");
            	redTrain.addListener(new InputListener() {
            		public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
//            			drawnCards.add(new TrainCard("red"));
            		}
            		public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
            	});
            	TextButton orangeTrain = new TextButton("Orange", TTRAdvisorApp.skin, "small");
            	orangeTrain.addListener(new InputListener() {
            		public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
//            			drawnCards.add(new TrainCard("orange"));
            		}
            		public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
            	});
            	TextButton yellowTrain = new TextButton("Yellow", TTRAdvisorApp.skin, "small");
            	yellowTrain.addListener(new InputListener() {
            		public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
//            			drawnCards.add(new TrainCard("yellow"));
            		}
            		public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
            	});
            	TextButton greenTrain = new TextButton("Green", TTRAdvisorApp.skin, "small");
            	greenTrain.addListener(new InputListener() {
            		public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
//            			drawnCards.add(new TrainCard("green"));
            		}
            		public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
            	});
            	TextButton blueTrain = new TextButton("Blue", TTRAdvisorApp.skin, "small");
            	blueTrain.addListener(new InputListener() {
            		public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
//            			drawnCards.add(new TrainCard("blue"));
            		}
            		public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
            	});
            	TextButton purpleTrain = new TextButton("Purple", TTRAdvisorApp.skin, "small");
            	purpleTrain.addListener(new InputListener() {
            		public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
//            			drawnCards.add(new TrainCard("purple"));
            		}
            		public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
            	});
            	TextButton blackTrain = new TextButton("Black", TTRAdvisorApp.skin, "small");
            	blackTrain.addListener(new InputListener() {
            		public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
//            			drawnCards.add(new TrainCard("black"));
            		}
            		public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
            	});
            	TextButton whiteTrain = new TextButton("White", TTRAdvisorApp.skin, "small");
            	whiteTrain.addListener(new InputListener() {
            		public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
//            			drawnCards.add(new TrainCard("white"));
            		}
            		public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
            	});
            	TextButton wildTrain = new TextButton("Wild", TTRAdvisorApp.skin, "small");
            	wildTrain.addListener(new InputListener() {
            		public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
//            			drawnCards.add(new TrainCard("wild"));
            		}
            		public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
            	});
            	TextButton done = new TextButton("Done", TTRAdvisorApp.skin, "small");
            	done.addListener(new InputListener() {
            		public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
//            			inputTurnController(TrainCardAction(drawnCards));
            		}
            		public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
            	});
            	TextButton undo = new TextButton("Undo", TTRAdvisorApp.skin, "small");
            	whiteTrain.addListener(new InputListener() {
            		public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            			if(!drawnCards.isEmpty()) {
            				drawnCards.remove(drawnCards.size()-1);
            			}
            		}
            		public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
            	});
            	TextButton back = new TextButton("Back", TTRAdvisorApp.skin, "small");
            	back.addListener(new InputListener() {
            		public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
//            			drawnCards.removeAll(drawnCards);
            		}
            		public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
            	});
            	TextButton invis = new TextButton("", TTRAdvisorApp.skin, "small");
            	invis.setVisible(false);
            	//Red, Orange, Yellow, Green, Blue, Purple, Black & White, wild
            	Table table = new Table();
            	table.bottom();
            	table.add(invis);
            	table.add(redTrain);
            	table.add(orangeTrain);
            	table.add(yellowTrain);
            	table.row();
            	table.add(invis);
            	table.add(greenTrain);
            	table.add(blueTrain);
            	table.add(purpleTrain);
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
