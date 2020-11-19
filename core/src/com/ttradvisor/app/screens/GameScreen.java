package com.ttradvisor.app.screens;

import java.util.ArrayList;
import java.util.LinkedList;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.utils.ArraySelection;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ttradvisor.app.TTRAdvisorApp;
import com.ttradvisor.app.classes.Action;
import com.ttradvisor.app.classes.Board.Route;
import com.ttradvisor.app.classes.CityLocations;
import com.ttradvisor.app.classes.CityLocations.CityLocation;
import com.ttradvisor.app.classes.Colors;
import com.ttradvisor.app.classes.DestinationAction;
import com.ttradvisor.app.classes.DestinationTicket;
import com.ttradvisor.app.classes.DestinationTicketList;
import com.ttradvisor.app.classes.Player;
import com.ttradvisor.app.classes.Recommender;
import com.ttradvisor.app.classes.RouteAction;
import com.ttradvisor.app.classes.RouteLocations;
import com.ttradvisor.app.classes.RouteLocations.RouteLocation;
import com.ttradvisor.app.classes.RouteLocations.TrainLocation;
import com.ttradvisor.app.classes.TrainCard;
import com.ttradvisor.app.classes.TrainCardAction;
import com.ttradvisor.app.classes.Turn;

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

	private boolean showHide;
	private Label rec1;
	private Label rec2;
	private Label rec3;
	private DestinationTicketList dtList;

	private ScrollPane listPane;
	private List<DestinationTicket> destTickets;
	private DestinationTicket[] ticketArray;
	private ArraySelection<DestinationTicket> ticketSelection;
	private Array<DestinationTicket> multipleTickets;

	private List<Colors.player> demonstration; // TODO delete this after demo
	private Label demoCurrPlayer;

	private float mapWidth;
	private float mapHeight;

	private CityLocations cityLocs;

	private static final String DEFAULT_CITY_LABEL = "No city selected.";
	private static final String DEFAULT_ROUTE_LABEL = "No route selected.";

	private String selectedCity = DEFAULT_CITY_LABEL;
	private String selectedRoute = DEFAULT_ROUTE_LABEL;
	private Label demoSelectedCity;
	private Label demoSelectedRoute;

	private TextButton destButton;
	private TextButton TCButton;
	private Label trainCardHand;

	// for multiple selection
	private Array<DestinationTicket> prevSelection;

	private TextButton prevTurn;
	private TextButton nextTurn;
	private Label turnNumber;
	
	private Label errorMessage;

	private ArrayList<TextureRegion> playerColors;
	
	private TextureAtlas textureAtlas;
    private Sprite trainImage;
	private RouteLocations routeLocations;

	public GameScreen(TTRAdvisorApp main) {
		mainApp = main;

		// get the initialized DestinationTicketList from GameState
		// need to handle if dtlist is somehow null
		dtList = main.gameState.getDtList();
		destTickets = new List<DestinationTicket>(TTRAdvisorApp.skin);
		// simple array of the DestinationTickets to pass into the Scrollpane
		ticketArray = new DestinationTicket[dtList.getList().size()];

		for (int i = 0; i < dtList.getList().size(); i++) {
			ticketArray[i] = dtList.getTicket(i);
		}
		// Set an equivalent List<DestinationTicket> for the scrollpane
		destTickets.setItems(ticketArray);
		destTickets.setAlignment(Align.left);

		// Needed to edit destTicket's selection style in order to select multiple
		// tickets
		ticketSelection = destTickets.getSelection();
		ticketSelection.setMultiple(true);
		destTickets.setSelection(ticketSelection);

		guiStage = new Stage(new ScreenViewport());
		mapStage = new Stage(new ScreenViewport());
		inputMult = new InputMultiplexer(guiStage, mapStage);

		setupRecommendations();

		setupDisplayElements();

		setupCardInputHandling();

		setupMapInputHandling();

		setupTurnView();

//		setupClaimedRouteTextures();

		Image map = new Image(new Texture("high_res_map.png"));

		// Important note: The assumption here is that we NEVER
		// scale texture coordinates independently of the world coordinates
		// So, 1px in texture is always == 1px in world
		mapWidth = map.getWidth();
		mapHeight = map.getHeight();

		// load city locations; these are relative to the high_res_map.png image above
		cityLocs = new CityLocations("city_locations.json");

		mapStage.addActor(map);
		camera = (OrthographicCamera) mapStage.getViewport().getCamera();
		camera.setToOrtho(false);
		camera.zoom = 1;

		camera.position.set(0, 0, camera.position.z);
		clampCamera();

		textureAtlas = new TextureAtlas("trains.txt");
		routeLocations = new RouteLocations("routes.json");
	
	}

	private void setupRecommendations() {
		Label l1 = new Label("First Recommendation", TTRAdvisorApp.skin);
		Label l2 = new Label("Second Recommendation", TTRAdvisorApp.skin);
		Label l3 = new Label("Third Recommendation", TTRAdvisorApp.skin);
		rec1 = l1;
		rec2 = l2;
		rec3 = l3;
		showHide = true;

		final TextButton t = new TextButton("ShowRecommendations", TTRAdvisorApp.skin, "small");
		t.setHeight(t.getHeight() / 2);
		t.setPosition(Gdx.graphics.getWidth() / 5, Gdx.graphics.getHeight() * .15f);
		t.addListener(new InputListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

				if (showHide) {
					rec1.setVisible(true);
					rec2.setVisible(true);
					rec3.setVisible(true);
					t.setText("HideRecommendations");
					showHide = false;
					return;
				}
				if (!showHide) {
					rec1.setVisible(false);
					rec2.setVisible(false);
					rec3.setVisible(false);
					t.setText("ShowRecommendations");
					showHide = true;
					return;
				}

			}

			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
		});

		l1.setPosition(Gdx.graphics.getWidth() / 5, Gdx.graphics.getHeight() / 8);
		l2.setPosition(Gdx.graphics.getWidth() / 5, Gdx.graphics.getHeight() / 8 - l1.getHeight());
		l3.setPosition(Gdx.graphics.getWidth() / 5, Gdx.graphics.getHeight() / 8 - l1.getHeight() * 2);

		l1.setName("rec1");
		l2.setName("rec2");
		l3.setName("rec3");

		l1.setVisible(false);
		l2.setVisible(false);
		l3.setVisible(false);
		guiStage.addActor(t);
		guiStage.addActor(l1);
		guiStage.addActor(l2);
		guiStage.addActor(l3);

	}

	private void setupDisplayElements() {
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

		demoCurrPlayer = new Label("Current player: " + mainApp.gameState.currentPlayer.getColor(), mainApp.skin);
		demoCurrPlayer.setColor(0, 0, 0, 1);
		demoCurrPlayer.setSize(100, 30);
		demoCurrPlayer.setPosition(50, 400);
		guiStage.addActor(demoCurrPlayer);

		demoSelectedCity = new Label("", mainApp.skin);
		demoSelectedCity.setColor(0, 0, 0, 1);
		demoSelectedCity.setSize(100, 30);
		demoSelectedCity.setPosition(50, 360);
		guiStage.addActor(demoSelectedCity);

		demoSelectedRoute = new Label("", mainApp.skin);
		demoSelectedRoute.setColor(0, 0, 0, 1);
		demoSelectedRoute.setSize(100, 30);
		demoSelectedRoute.setPosition(50, 320);
		guiStage.addActor(demoSelectedRoute);
	}

	private void setupCardInputHandling() {
		errorMessage = new Label("", TTRAdvisorApp.skin);
		errorMessage.setWidth(Gdx.graphics.getWidth()/2);
		errorMessage.setPosition(Gdx.graphics.getWidth()/2 - errorMessage.getWidth()/2, Gdx.graphics.getHeight()/8);
		errorMessage.setColor(Color.RED);
        
		// Button to draw Destination tickets
		destButton = new TextButton("Draw Destination \n Ticket", TTRAdvisorApp.skin, "small");
		TCButton = new TextButton("Draw Train \n Card", TTRAdvisorApp.skin, "small");
		trainCardHand = new Label(mainApp.gameState.currentPlayer.getTCS().toString(), TTRAdvisorApp.skin);
		trainCardHand.setWidth(Gdx.graphics.getWidth() / 5);
		trainCardHand.setPosition(trainCardHand.getWidth(), trainCardHand.getHeight() / 8);
		// Button to draw Destination tickets
		destButton.setWidth(Gdx.graphics.getWidth() / 5);
		destButton.setPosition(Gdx.graphics.getWidth() - destButton.getWidth(), destButton.getHeight() / 8);
		// Jake: Still need to add way to choose DTs within this screen
		destButton.addListener(new InputListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				destButton.setVisible(false);
				TCButton.setVisible(false);
				trainCardHand.setVisible(false);
				final Table table = new Table();
				final ArrayList<DestinationTicket> drawnTickets = new ArrayList<>();
				// Initialize ScrollPane that holds the list of DTs
				listPane = new ScrollPane(destTickets, TTRAdvisorApp.skin);
				listPane.setX(Gdx.graphics.getWidth() * 3 / 5);
				listPane.setY(Gdx.graphics.getHeight() * 1 / 100);
				listPane.setHeight(Gdx.graphics.getHeight() * 1 / 2);
				listPane.setWidth(Gdx.graphics.getWidth() * 1 / 3);

				// multiple selection for android only
				if (Gdx.app.getType() == ApplicationType.Android) {
					destTickets.addListener(new ClickListener() {

						@Override
						public void clicked(InputEvent event, float x, float y) {
							DestinationTicket clicked = destTickets.getItemAt(y);

							if (prevSelection == null) {
								prevSelection = new Array<DestinationTicket>();
							}

							if (prevSelection.contains(clicked, true)) {
								// ticket was already selected, remove it
								destTickets.getSelection().addAll(prevSelection);
								destTickets.getSelection().remove(clicked);
							} else {
								// new ticket selection; make sure the existing selection is preserved
								destTickets.getSelection().addAll(prevSelection);
							}
							prevSelection = destTickets.getSelection().toArray();

							super.clicked(event, x, y);
						}
					});
				}

				guiStage.addActor(listPane);
				guiStage.setScrollFocus(listPane);

				// Button to confirm DestinationAction with selected tickets
				TextButton done = new TextButton("Done", TTRAdvisorApp.skin, "small");
				done.addListener(new InputListener() {
					public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
						// reset the multiple selection tracking
						prevSelection = null;

//		    		for (DestinationTicket ticket : destTickets.getSelection().toArray()) {
//		    			drawnTickets.add(ticket);
//		    		}

						multipleTickets = destTickets.getSelection().items().orderedItems();
						while (multipleTickets.notEmpty()) {
							System.out.println(multipleTickets.peek().toString());
							drawnTickets.add(multipleTickets.pop());
						}

						boolean isInitial = mainApp.turnInput.isInitialTurn();

						if (mainApp.turnInput.takeAction(new DestinationAction(mainApp.gameState.currentPlayer, drawnTickets))) {
							
							advanceTurn(isInitial,new DestinationAction(mainApp.gameState.currentPlayer, drawnTickets));
						}
						else {	
							errorMessage.setText(mainApp.gameState.getError());
							errorMessage.setVisible(true);
						}

						guiStage.setScrollFocus(null);

						destButton.setVisible(true);
						TCButton.setVisible(true);
						trainCardHand.setVisible(true);
						listPane.setVisible(false);
						table.setVisible(false);
					}

					public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
						return true;
					}
				});

				// Button to return to the GameScreen without inputting the action
				TextButton back = new TextButton("Back", TTRAdvisorApp.skin, "small");
				back.addListener(new InputListener() {
					public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
						// clear for multiple selection
						prevSelection = null;
						drawnTickets.removeAll(drawnTickets);

						guiStage.setScrollFocus(null);

						destButton.setVisible(true);
						TCButton.setVisible(true);
						trainCardHand.setVisible(true);
						listPane.setVisible(false);
						table.setVisible(false);
					}

					public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
						return true;
					}
				});
				// Populate the table with back and done buttons
				table.bottom();
				table.add(back);
				table.row();
				table.add(done);
				table.setFillParent(true);

				guiStage.addActor(table);
			}

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
		});
		guiStage.addActor(destButton);

		TCButton.setHeight(destButton.getHeight());
		TCButton.setPosition(0, TCButton.getHeight() / 8);
		TCButton.addListener(new InputListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				final Table table = new Table();
				destButton.setVisible(false);
				TCButton.setVisible(false);
				trainCardHand.setVisible(false);
				final ArrayList<TrainCard> drawnCards = new ArrayList<>();
				final Label drawnCardList = new Label(drawnCards.toString(), TTRAdvisorApp.skin);
				drawnCardList.setWidth((Gdx.graphics.getWidth() / 5) * 2);
				drawnCardList.setPosition(drawnCardList.getWidth(), drawnCardList.getHeight() * 9);
				TextButton redTrain = new TextButton("Red", TTRAdvisorApp.skin, "small");
				redTrain.addListener(new InputListener() {
					public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
						drawnCards.add(new TrainCard(Colors.route.RED));
						drawnCardList.setText(drawnCards.toString());
					}

					public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
						return true;
					}
				});
				TextButton orangeTrain = new TextButton("Orange", TTRAdvisorApp.skin, "small");
				orangeTrain.addListener(new InputListener() {
					public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
						drawnCards.add(new TrainCard(Colors.route.ORANGE));
						drawnCardList.setText(drawnCards.toString());
					}

					public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
						return true;
					}
				});
				TextButton yellowTrain = new TextButton("Yellow", TTRAdvisorApp.skin, "small");
				yellowTrain.addListener(new InputListener() {
					public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
						drawnCards.add(new TrainCard(Colors.route.YELLOW));
						drawnCardList.setText(drawnCards.toString());
					}

					public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
						return true;
					}
				});
				TextButton greenTrain = new TextButton("Green", TTRAdvisorApp.skin, "small");
				greenTrain.addListener(new InputListener() {
					public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
						drawnCards.add(new TrainCard(Colors.route.GREEN));
						drawnCardList.setText(drawnCards.toString());
					}

					public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
						return true;
					}
				});
				TextButton blueTrain = new TextButton("Blue", TTRAdvisorApp.skin, "small");
				blueTrain.addListener(new InputListener() {
					public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
						drawnCards.add(new TrainCard(Colors.route.BLUE));
						drawnCardList.setText(drawnCards.toString());
					}

					public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
						return true;
					}
				});
				TextButton pinkTrain = new TextButton("Pink", TTRAdvisorApp.skin, "small");
				pinkTrain.addListener(new InputListener() {
					public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
						drawnCards.add(new TrainCard(Colors.route.PINK));
						drawnCardList.setText(drawnCards.toString());
					}

					public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
						return true;
					}
				});
				TextButton blackTrain = new TextButton("Black", TTRAdvisorApp.skin, "small");
				blackTrain.addListener(new InputListener() {
					public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
						drawnCards.add(new TrainCard(Colors.route.BLACK));
						drawnCardList.setText(drawnCards.toString());
					}

					public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
						return true;
					}
				});
				TextButton whiteTrain = new TextButton("White", TTRAdvisorApp.skin, "small");
				whiteTrain.addListener(new InputListener() {
					public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
						drawnCards.add(new TrainCard(Colors.route.WHITE));
						drawnCardList.setText(drawnCards.toString());
					}

					public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
						return true;
					}
				});
				TextButton wildTrain = new TextButton("Wild", TTRAdvisorApp.skin, "small");
				wildTrain.addListener(new InputListener() {
					public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
						drawnCards.add(new TrainCard(Colors.route.ANY));
						drawnCardList.setText(drawnCards.toString());
					}

					public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
						return true;
					}
				});
				TextButton done = new TextButton("Done", TTRAdvisorApp.skin, "small");
				done.addListener(new InputListener() {
					public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

						boolean isInitial = mainApp.turnInput.isInitialTurn();

						if (mainApp.turnInput.takeAction(new TrainCardAction(mainApp.gameState.currentPlayer, drawnCards))) {
							advanceTurn(isInitial, new TrainCardAction(mainApp.gameState.currentPlayer, drawnCards));
						} else {	
							errorMessage.setText(mainApp.gameState.getError());
							errorMessage.setVisible(true);
						}

						trainCardHand.setText(mainApp.gameState.currentPlayer.getTCS().toString());
						destButton.setVisible(true);
						TCButton.setVisible(true);
						trainCardHand.setVisible(true);
						table.setVisible(false);
						drawnCardList.setVisible(false);
					}

					public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
						return true;
					}
				});
				TextButton undo = new TextButton("Undo", TTRAdvisorApp.skin, "small");
				undo.addListener(new InputListener() {
					public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
						if (!drawnCards.isEmpty()) {
							drawnCards.remove(drawnCards.size() - 1);
							drawnCardList.setText(drawnCards.toString());
						}
					}

					public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
						return true;
					}
				});
				TextButton back = new TextButton("Back", TTRAdvisorApp.skin, "small");
				back.addListener(new InputListener() {
					public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
						drawnCards.removeAll(drawnCards);
						destButton.setVisible(true);
						TCButton.setVisible(true);
						trainCardHand.setVisible(true);
						table.setVisible(false);
						drawnCardList.setVisible(false);
					}

					public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
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
				guiStage.addActor(drawnCardList);
			}

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
		});

		guiStage.addActor(TCButton);
		guiStage.addActor(trainCardHand);
		guiStage.addActor(errorMessage);
	}

	private void setupTurnView() {
		prevTurn = new TextButton("View \n Previous Turn", TTRAdvisorApp.skin, "small");
		prevTurn.setPosition(0, Gdx.graphics.getHeight() - prevTurn.getHeight());
		nextTurn = new TextButton("View \n Next Turn", TTRAdvisorApp.skin, "small");
		nextTurn.setPosition(Gdx.graphics.getWidth() - nextTurn.getWidth(),
				Gdx.graphics.getHeight() - nextTurn.getHeight());
		turnNumber = new Label(Integer.toString(mainApp.hist.getTurnIndex()), TTRAdvisorApp.skin);
		turnNumber.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() - turnNumber.getHeight());
		prevTurn.addListener(new InputListener() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				if (mainApp.hist.previousTurn()) {
					mainApp.gameState.setBoard(mainApp.hist.getGameState().getBoard());
					trainCardHand.setText(mainApp.hist.getGameState().getPlayers().get(mainApp.hist.getTurnIndexView())
							.getTCS().toString());
					turnNumber.setText(Integer.toString(mainApp.hist.getTurnIndex()));
					demoCurrPlayer.setText("Current player: " + mainApp.hist.getGameState().getPlayers().get(mainApp.hist.getTurnIndexView()).getColor());
				}
			}

			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
		});
		nextTurn.addListener(new InputListener() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				if (mainApp.hist.nextTurn()) {
					mainApp.gameState.setBoard(mainApp.hist.getGameState().getBoard());
					trainCardHand.setText(mainApp.hist.getGameState().getPlayers().get(mainApp.hist.getTurnIndexView())
							.getTCS().toString());
					turnNumber.setText(Integer.toString(mainApp.hist.getTurnIndex()));
					demoCurrPlayer.setText("Current player: " + mainApp.hist.getGameState().getPlayers().get(mainApp.hist.getTurnIndexView()).getColor());
				}
			}

			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
		});
		guiStage.addActor(turnNumber);
		guiStage.addActor(prevTurn);
		guiStage.addActor(nextTurn);
	}

	/**
	 * Handlers for mapStage ActorGestureListener used here for sake of identifying
	 * zooms/pans on android
	 */
	private void setupMapInputHandling() {

		// enable scrolling with scroll wheel for desktop
		mapStage.addListener(new InputListener() {

			@Override
			public boolean scrolled(InputEvent event, float x, float y, int amount) {
				camera.zoom += (float) amount / 10f;
				clampCamera();
				// Gdx.app.log("Camera", "Zoomed to: " + camera.zoom);
				return super.scrolled(event, x, y, amount);
			}

		});

		// android gestures
		mapStage.addListener(new ActorGestureListener() {

			// screen (not world) coordinates of the start of the pan and current pan
			private Vector3 panOriginScreen;
			private Vector3 panCurrentScreen;
			private Vector3 trueScreenDelta;
			// world coordinates of camera when starting the pan
			private Vector3 panOriginCamera;

			@Override
			public void tap(InputEvent event, float x, float y, int count, int button) {
				// unproject the world coordinates of tap
				Vector3 tapPos = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
				for (CityLocation loc : cityLocs.getCityLocations()) {

//					Gdx.app.log("City Event", "Distance to " + loc.name + " is " + tapPos.dst(loc.x, loc.y, 0) + " px.");

					// cities have a radius of 16 pixels, roughly
					if (tapPos.dst(loc.x, loc.y, 0) < 16) {
						if (selectedCity == DEFAULT_CITY_LABEL) {
							// select first city
							selectedCity = loc.name;
						} else {
							// second city selected
							LinkedList<Route> routeOptions = mainApp.gameState.getBoard().getAllRoutes(selectedCity,
									loc.name);

							// just deselect if no routes between them
							if (routeOptions.isEmpty()) {
								selectedCity = DEFAULT_CITY_LABEL;
								selectedRoute = DEFAULT_ROUTE_LABEL;
								super.tap(event, x, y, count, button);
								return;
							} else if (routeOptions.size() == 1) {
								// exactly one route exists
								setupHelperChooseCards(routeOptions.get(0));
							} else {
								// 2+ routes exist
								setupHelperChooseRoute(routeOptions);
							}

						}
						super.tap(event, x, y, count, button);
						return;
					}
				}
				// Gdx.app.log("City Event", "Tapped on background.");
				selectedCity = DEFAULT_CITY_LABEL;
				selectedRoute = DEFAULT_ROUTE_LABEL;
				super.tap(event, x, y, count, button);
				return;
			}

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
				// Gdx.app.log("Camera", "Translated to coords: " + camera.position.x + ", " +
				// camera.position.y);
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

	/**
	 * @param routeOptions the options we've narrowed down to
	 */
	private void setupHelperChooseRoute(final LinkedList<Route> routeOptions) {

		if (routeOptions.get(0).getColor() == routeOptions.get(1).getColor()) {
			// identical colors, ignore
			setupHelperChooseCards(routeOptions.get(0));
			return;
		}

		final Table table = new Table();
		destButton.setVisible(false);
		TCButton.setVisible(false);
		TextButton routeChoice0 = new TextButton("Color: " + routeOptions.get(0).getColor(), TTRAdvisorApp.skin,
				"small");
		routeChoice0.addListener(new InputListener() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				setupHelperChooseCards(routeOptions.get(0));
				table.setVisible(false);
			}

			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
		});
		TextButton routeChoice1 = new TextButton("Color: " + routeOptions.get(1).getColor(), TTRAdvisorApp.skin,
				"small");
		routeChoice1.addListener(new InputListener() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				setupHelperChooseCards(routeOptions.get(1));
				table.setVisible(false);
			}

			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
		});
		TextButton back = new TextButton("Cancel", TTRAdvisorApp.skin, "small");
		back.addListener(new InputListener() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				destButton.setVisible(true);
				TCButton.setVisible(true);
				table.setVisible(false);
			}

			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
		});
		table.center();
		table.add(routeChoice0);
		table.add(routeChoice1);
		table.row();
		table.add(back);
		table.setFillParent(true);
		guiStage.addActor(table);
	}

	/**
	 * @param route the one route we've narrowed down to
	 */
	private void setupHelperChooseCards(final Route route) {

		selectedRoute = "Selected: " + route;
		
		if (mainApp.gameState.currentPlayer.getColor() != mainApp.userColor) {
			
			boolean isInitial = mainApp.turnInput.isInitialTurn();

			RouteAction routeAction = new RouteAction(mainApp.gameState.currentPlayer, new ArrayList<TrainCard>(), route);
	
			if (mainApp.turnInput.takeAction(routeAction)) {
				advanceTurn(isInitial, routeAction);
			}
	
			selectedCity = DEFAULT_CITY_LABEL;
	
			trainCardHand.setText(mainApp.gameState.currentPlayer.getTCS().toString());
			destButton.setVisible(true);
			TCButton.setVisible(true);
			trainCardHand.setVisible(true);
			return;
		}

		final Table table = new Table();
		destButton.setVisible(false);
		TCButton.setVisible(false);
		trainCardHand.setVisible(false);
		final ArrayList<TrainCard> drawnCards = new ArrayList<>();
		final Label drawnCardList = new Label(drawnCards.toString(), TTRAdvisorApp.skin);
		drawnCardList.setWidth((Gdx.graphics.getWidth() / 5) * 2);
		drawnCardList.setPosition(drawnCardList.getWidth(), drawnCardList.getHeight() * 9);
		TextButton redTrain = new TextButton("Red", TTRAdvisorApp.skin, "small");
		redTrain.addListener(new InputListener() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				drawnCards.add(new TrainCard(Colors.route.RED));
				drawnCardList.setText(drawnCards.toString());
			}

			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
		});
		TextButton orangeTrain = new TextButton("Orange", TTRAdvisorApp.skin, "small");
		orangeTrain.addListener(new InputListener() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				drawnCards.add(new TrainCard(Colors.route.ORANGE));
				drawnCardList.setText(drawnCards.toString());
			}

			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
		});
		TextButton yellowTrain = new TextButton("Yellow", TTRAdvisorApp.skin, "small");
		yellowTrain.addListener(new InputListener() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				drawnCards.add(new TrainCard(Colors.route.YELLOW));
				drawnCardList.setText(drawnCards.toString());
			}

			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
		});
		TextButton greenTrain = new TextButton("Green", TTRAdvisorApp.skin, "small");
		greenTrain.addListener(new InputListener() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				drawnCards.add(new TrainCard(Colors.route.GREEN));
				drawnCardList.setText(drawnCards.toString());
			}

			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
		});
		TextButton blueTrain = new TextButton("Blue", TTRAdvisorApp.skin, "small");
		blueTrain.addListener(new InputListener() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				drawnCards.add(new TrainCard(Colors.route.BLUE));
				drawnCardList.setText(drawnCards.toString());
			}

			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
		});
		TextButton pinkTrain = new TextButton("Pink", TTRAdvisorApp.skin, "small");
		pinkTrain.addListener(new InputListener() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				drawnCards.add(new TrainCard(Colors.route.PINK));
				drawnCardList.setText(drawnCards.toString());
			}

			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
		});
		TextButton blackTrain = new TextButton("Black", TTRAdvisorApp.skin, "small");
		blackTrain.addListener(new InputListener() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				drawnCards.add(new TrainCard(Colors.route.BLACK));
				drawnCardList.setText(drawnCards.toString());
			}

			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
		});
		TextButton whiteTrain = new TextButton("White", TTRAdvisorApp.skin, "small");
		whiteTrain.addListener(new InputListener() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				drawnCards.add(new TrainCard(Colors.route.WHITE));
				drawnCardList.setText(drawnCards.toString());
			}

			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
		});
		TextButton wildTrain = new TextButton("Wild", TTRAdvisorApp.skin, "small");
		wildTrain.addListener(new InputListener() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				drawnCards.add(new TrainCard(Colors.route.ANY));
				drawnCardList.setText(drawnCards.toString());
			}

			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
		});
		TextButton done = new TextButton("Done", TTRAdvisorApp.skin, "small");
		done.addListener(new InputListener() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

				boolean isInitial = mainApp.turnInput.isInitialTurn();

				// send the selected cards
				RouteAction routeAction = new RouteAction(mainApp.gameState.currentPlayer, drawnCards, route);

				if (mainApp.turnInput.takeAction(routeAction)) {
					advanceTurn(isInitial, routeAction);
				} else {	
					errorMessage.setText(mainApp.gameState.getError());
					errorMessage.setVisible(true);
				}

				selectedCity = DEFAULT_CITY_LABEL;

				trainCardHand.setText(mainApp.gameState.currentPlayer.getTCS().toString());
				destButton.setVisible(true);
				TCButton.setVisible(true);
				trainCardHand.setVisible(true);
				table.setVisible(false);
				drawnCardList.setVisible(false);
			}

			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
		});
		TextButton undo = new TextButton("Undo", TTRAdvisorApp.skin, "small");
		undo.addListener(new InputListener() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				if (!drawnCards.isEmpty()) {
					drawnCards.remove(drawnCards.size() - 1);
					drawnCardList.setText(drawnCards.toString());
				}
			}

			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
		});
		TextButton back = new TextButton("Back", TTRAdvisorApp.skin, "small");
		back.addListener(new InputListener() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				drawnCards.removeAll(drawnCards);
				destButton.setVisible(true);
				TCButton.setVisible(true);
				trainCardHand.setVisible(true);
				table.setVisible(false);
				drawnCardList.setVisible(false);
			}

			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
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

//      table.setDebug(true); // turn on all debug lines (table, cell, and widget)
		guiStage.addActor(table);
		guiStage.addActor(drawnCardList);
	}

	/**
	 * Should only be called when the InputTurnController returns true on an Action
	 * 
	 * @param isInitial - whether we are advancing from the initial turn
	 * @param Action    - the action to record as happening in the turn list
	 */
	private void advanceTurn(boolean isInitial, Action action) {
		if (isInitial) {
			mainApp.gameState.currentPlayer = mainApp.gameState.getPlayers().get(0);
		} else {
			int currPlayerIndex = mainApp.gameState.getPlayers().indexOf(mainApp.gameState.currentPlayer);
			if (currPlayerIndex < mainApp.gameState.getPlayers().size() - 1) {
				mainApp.gameState.currentPlayer = mainApp.gameState.getPlayers().get(currPlayerIndex + 1);
			} else {
				mainApp.gameState.currentPlayer = mainApp.gameState.getPlayers().get(0);
			}
		}

		ArrayList<Player> deepCopyPlayers = new ArrayList<Player>();
		for (Player p : mainApp.gameState.getPlayers()) {
			deepCopyPlayers.add(p.getDeepCopy());
		}

		mainApp.gameState.addTurn(new Turn(mainApp.gameState.getBoard().snapshotBoard(), action, deepCopyPlayers));
		mainApp.hist.setTurnIndex(mainApp.gameState.getCurrentTurnCounter());
		turnNumber.setText(Integer.toString(mainApp.hist.getTurnIndex()));
		if (mainApp.userColor.equals(mainApp.gameState.currentPlayer.getColor())) {
			Recommender rec = new Recommender(mainApp.gameState.getBoard(), mainApp.gameState.currentPlayer,
					mainApp.gameState.getCurrentTurnCounter(), mainApp.gameState.getPlayers().size());
			ArrayList<String> recs = rec.calculate(mainApp.gameState.currentPlayer.getDTS());
			rec1.setText(recs.get(0));
			rec2.setText(recs.get(1));
			rec3.setText(recs.get(2));
		}
		demoCurrPlayer.setText("Current player: " + mainApp.gameState.currentPlayer.getColor());
		errorMessage.setVisible(false);

	}

	
	private void clampCamera() {

		camera.zoom = MathUtils.clamp(camera.zoom,
				Math.min(mapHeight / camera.viewportHeight, mapWidth / camera.viewportWidth) * 0.2f,
				Math.min(mapHeight / camera.viewportHeight, mapWidth / camera.viewportWidth));

		float effectiveViewportWidth = camera.viewportWidth * camera.zoom;
		float effectiveViewportHeight = camera.viewportHeight * camera.zoom;

		camera.position.x = MathUtils.clamp(camera.position.x, effectiveViewportWidth * 0.6f / 2f,
				mapWidth - effectiveViewportWidth * 0.6f / 2f);
		camera.position.y = MathUtils.clamp(camera.position.y, effectiveViewportHeight * 0.6f / 2f,
				mapHeight - effectiveViewportHeight * 0.6f / 2f);

		// Gdx.app.log("Camera", "Clamped to coords: " + camera.position.x + ", " +
		// camera.position.y);
	}

//	/**
//	 * Initialize textures for all players' colors Call in constructor only
//	 */
//	private void setupClaimedRouteTextures() {
//		playerColors = new ArrayList<TextureRegion>();
//
//		Pixmap tempPix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
//
//		// for each player: generate a 1x1 texture (using pixmap) of their color
//		for (Player p : mainApp.gameState.getPlayers()) {
//			switch (p.getColor()) {
//			case BLACK:
//				tempPix.setColor(0, 0, 0, 1);
//				break;
//			case BLUE:
//				tempPix.setColor(0, 0, 1, 1);
//				break;
//			case GREEN:
//				tempPix.setColor(0, 1, 0, 1);
//				break;
//			case NONE:
//				Gdx.app.error("GameScreen", "Player of color \"NONE\" exists in game state.");
//				tempPix.setColor(1, 1, 1, 1);
//				// non-fatal error
//				break;
//			case RED:
//				tempPix.setColor(1, 0, 0, 1);
//				break;
//			case YELLOW:
//				tempPix.setColor(1, 1, 0, 1);
//				break;
//			default:
//				Gdx.app.error("GameScreen", "Player of invalid color exists in game state.");
//				tempPix.setColor(1, 1, 1, 1);
//				// non-fatal error
//				break;
//			}
//			tempPix.fill();
//			playerColors.add(new TextureRegion(new Texture(tempPix)));
//		}
//		tempPix.dispose();
//	}

	/**
	 * Call every render cycle instead of mapStage.draw()
	 */
	private void drawMapStageManually() {

//		mapStage.getCamera().update();
//
//		if (!mapStage.getRoot().isVisible())
//			return;
//
//		Batch batch = mapStage.getBatch();
//		batch.setProjectionMatrix(camera.combined);
//
//		mapStage.getBatch().begin();
//
//		mapStage.getRoot().draw(mapStage.getBatch(), 1);
//
//		for (Player p : mainApp.gameState.getPlayers()) {
//			for (Route r : mainApp.gameState.getBoard().getAllRoutesOfPlayer(p.getColor())) {
//
//				Vector2 beginLoc = cityLocs.getCityLocation(r.getBegin());
//				Vector2 endLoc = cityLocs.getCityLocation(r.getEnd());
//
//				float dist = beginLoc.dst(endLoc);
//
//				double angle = Math.atan2(endLoc.y - beginLoc.y, endLoc.x - beginLoc.x) * 180 / Math.PI;
//
//				TextureRegion tex = playerColors.get(mainApp.gameState.getPlayers().indexOf(p));
//
//				mapStage.getBatch().draw(tex, beginLoc.x, beginLoc.y, 0, 0, dist, 10, 1, 1, (float) angle);
//
//			}
//		}
//
//		// and do the default behavior too
//		mapStage.getBatch().end();
		
		mapStage.getCamera().update();

		if (!mapStage.getRoot().isVisible())
			return;

		mapStage.getBatch().setProjectionMatrix(camera.combined);

		mapStage.getBatch().begin();

		mapStage.getRoot().draw(mapStage.getBatch(), 1);
		
		
//		for (int i = 0;i < mainApp.gameState.getBoard().getAllRoutes("Atlanta", "Raleigh").size();i++) {
//			System.out.println( mainApp.gameState.getBoard().getAllRoutes("Atlanta", "Raleigh").get(i).getOwner());
//		}
		
		
		for (Player p : mainApp.gameState.getPlayers()) {
			if(p.getColor().equals(Colors.player.BLACK)) {
				trainImage = textureAtlas.createSprite("BlackTrain");
			}
			if (p.getColor().equals(Colors.player.BLUE)) {
				trainImage = textureAtlas.createSprite("BlueTrain");
			}
			if (p.getColor().equals(Colors.player.GREEN)) {
				trainImage = textureAtlas.createSprite("GreenTrain");
			}
			if (p.getColor().equals(Colors.player.RED)) {
				trainImage = textureAtlas.createSprite("RedTrain");
			}
			if (p.getColor().equals(Colors.player.YELLOW)) {
				trainImage = textureAtlas.createSprite("YellowTrain");
			}
			for (Route r : mainApp.gameState.getBoard().getAllRoutesOfPlayer(p.getColor())) {
				if (r.getColor().equals(Colors.route.ANY) && mainApp.gameState.getBoard().getAllRoutes(r.getBegin(), r.getEnd()).size() == 2) {
					if (mainApp.gameState.getBoard().getAllRoutes(r.getBegin(), r.getEnd()).get(0).getOwner().equals(p.getColor())) {
						for (TrainLocation t: routeLocations.getRouteLocation(r.getBegin(), r.getEnd(), r.getColor().toString()).trains){
							mapStage.getBatch().draw(trainImage, t.x - 33, t.y - 15, 33, 15, 66, 30, 1, 1, t.r);
						}
					}else {
						int secondANYIndex = 1 + routeLocations.getList().indexOf(routeLocations.getRouteLocation(r.getBegin(), r.getEnd(), r.getColor().toString()));
						RouteLocation rL = routeLocations.getList().get(secondANYIndex);
						for (TrainLocation t: rL.trains){
							mapStage.getBatch().draw(trainImage, t.x - 33, t.y - 15, 33, 15, 66, 30, 1, 1, t.r);
						}
					}
					
				}
				else {
					for (TrainLocation t: routeLocations.getRouteLocation(r.getBegin(), r.getEnd(), r.getColor().toString()).trains){
						mapStage.getBatch().draw(trainImage, t.x - 33, t.y - 15, 33, 15, 66, 30, 1, 1, t.r);
					}
				}

			}
		}
		
		mapStage.getBatch().end();
	

	}

	@Override
	public void show() {
		Gdx.app.log("MainScreen", "show");
		Gdx.input.setInputProcessor(inputMult);

	}

	@Override
	public void render(float delta) {
		demoSelectedCity.setText(selectedCity);
		demoSelectedRoute.setText(selectedRoute);
		Gdx.gl.glClearColor(.58f, .71f, .78f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();

		mapStage.act();
		guiStage.act();
		drawMapStageManually();
		// mapStage.draw();
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
		for (TextureRegion tr : playerColors) {
			tr.getTexture().dispose();
		}
		mapStage.dispose();
		guiStage.dispose();
	}

}