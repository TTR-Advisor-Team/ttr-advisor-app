package com.ttradvisor.app.screens;

import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.utils.ArraySelection;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ttradvisor.app.TTRAdvisorApp;
import com.ttradvisor.app.classes.Board.Route;
import com.ttradvisor.app.classes.Board;
import com.ttradvisor.app.classes.Colors;
import com.ttradvisor.app.classes.DestinationTicket;
import com.ttradvisor.app.classes.DestinationTicketList;
import com.ttradvisor.app.classes.GameState;
import com.ttradvisor.app.classes.HistoryController;
import com.ttradvisor.app.classes.InputTurnController;
import com.ttradvisor.app.classes.Player;
import com.ttradvisor.app.classes.RouteLocations;
import com.ttradvisor.app.classes.Turn;
import com.ttradvisor.app.classes.RouteLocations.RouteLocation;
import com.ttradvisor.app.classes.RouteLocations.TrainLocation;

/**
 * Created by julienvillegas on 17/01/2017.
 * 
 * Copied in as example code to modify
 */
public class TutorialScreen implements Screen {

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

	private List<DestinationTicket> destTickets;
	private DestinationTicket[] ticketArray;
	private ArraySelection<DestinationTicket> ticketSelection;

	private float mapWidth;
	private float mapHeight;

	private static final String DEFAULT_CITY_LABEL = "No city selected.";
	private static final String DEFAULT_ROUTE_LABEL = "No route selected.";

	private String selectedCity = DEFAULT_CITY_LABEL;
	private String selectedRoute = DEFAULT_ROUTE_LABEL;
	private Label demoSelectedCity;
	private Label demoSelectedRoute;

	private TextButton destButton;
	private TextButton TCButton;
	private Label trainCardHand;
	
	private TextButton claimRouteButton;
	private Label claimRouteTooltip;

	private TextButton recommendationsButton;

	private TextButton prevTurn;
	private TextButton nextTurn;
	private Label turnNumber;
	
	private TextButton quit;
	
	private Label errorMessage;

	private ArrayList<TextureRegion> playerColors;
	
	private TextureAtlas textureAtlas;
    private Sprite trainImage;
	private RouteLocations routeLocations;
	
	private Dialog tutorialStart;
	private Dialog tutorialDTC;
	private Dialog tutorialRec;
	private Dialog tutorialDDT;
	private Dialog tutorialPT;
	private Dialog tutorialCR;
	private Label welcome;
	private Label dtc;
	private Label showRec;
	private Label ddt;
	private Label pt;
	private Label cr;

	public TutorialScreen(TTRAdvisorApp main) {
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

		errorMessage = new Label("", TTRAdvisorApp.skin);
		errorMessage.setColor(Color.RED);
		
		welcome = new Label("Hello, this tutorial is designed to help familiarize\n" + 
							"yourself with the apps functions and layout. Once you\n" + 
							"are finished with the tutorial, you can click the quit\n" + 
							"button to return to the main screen. Please click the draw\n" +
							"train card button to continue.", TTRAdvisorApp.skin);
		welcome.setColor(Color.BLACK);
		welcome.setAlignment(Align.topLeft);
		
		dtc = new Label("When it is your turn, you can click the Draw Train Card\n" + 
				"button to input the train cards you got on your turn.\n" + 
				"You'll be able to input four for the first turn and two\n" + 
				"for any turn after that. If it is not your turn, clicking\n" +
				"the button will advance the turn and you do not input the\n" +
				"the cards that other players pick up. Click the Show\n" +
				"Recommendations button to advance the tutorial.", TTRAdvisorApp.skin);
		dtc.setColor(Color.BLACK);
		dtc.setAlignment(Align.topLeft);
		
		showRec = new Label("When it is your turn, you can click the Show\n" + 
				"Recommendations button to see the three best possible\n" + 
				"turns that you could take. Clicking the button again will\n" + 
				"hide the recommendations. Please click the Draw\n" +
				"Destination Ticket button to continue the tutorial.", TTRAdvisorApp.skin);
		showRec.setColor(Color.BLACK);
		showRec.setAlignment(Align.topLeft);
		
		ddt = new Label("When it is your turn, you can click the Draw Destination\n" + 
				"Tickets button to input the (2-3) destination tickets that you\n" + 
				"selected on your turn. If it is not your turn, clicking\n" +
				"the button will advance the turn and you do not input the\n" +
				"the cards that other players pick up. Click the View\n" +
				"Prev Turn button to continue the tutorial.", TTRAdvisorApp.skin);
		ddt.setColor(Color.BLACK);
		ddt.setAlignment(Align.topLeft);
		
		pt = new Label("Clicking the View Prev Turn button will take you to the\n" + 
				"state of game during the previous turn. You are able\n" + 
				"to edit the most recent turn but no turns before that.\n" +
				"Clicking the View Next Turn button will take you back to\n" +
				"the turn you left. The turn counter is located at the top\n" +
				"center of your screen. Please click the Claim Route\n" +
				"button to continue the tutorial.", TTRAdvisorApp.skin);
		pt.setColor(Color.BLACK);
		pt.setAlignment(Align.topLeft);
		
		cr = new Label("When it is your turn, you can click the Claim Route\n" + 
				"button to input the route you claimed on your turn.\n" + 
				"After clicking the button, click on the two adjacent\n" +
				"cities of your route and select the cards you are using\n" +
				"to pay for the route. If it is not your turn, you will\n" +
				"do the same thing but without seleceting cards. Click\n" + 
				"the Quit button to return to the main menu.", TTRAdvisorApp.skin);
		cr.setColor(Color.BLACK);
		cr.setAlignment(Align.topLeft);
		
		
		tutorialStart = new Dialog("Welocome to the Ticket to Ride Advisor Tutorial", TTRAdvisorApp.skin);
		tutorialStart.setWidth(Gdx.graphics.getWidth()/2);
		tutorialStart.setHeight(Gdx.graphics.getHeight()/2);
		tutorialStart.setPosition(Gdx.graphics.getWidth()/2 - tutorialStart.getWidth()/2, 
						Gdx.graphics.getHeight()/2 - tutorialStart.getHeight()/2);
		tutorialStart.add(welcome).pad(0,0,tutorialStart.getHeight()/2,20).row();
		tutorialStart.setMovable(false);
		guiStage.addActor(tutorialStart);
		
		tutorialDTC = new Dialog("Welocome to the Ticket to Ride Advisor Tutorial", TTRAdvisorApp.skin);
		tutorialDTC.setWidth(Gdx.graphics.getWidth()/2);
		tutorialDTC.setHeight(Gdx.graphics.getHeight()/2);
		tutorialDTC.setPosition(Gdx.graphics.getWidth()/2 - tutorialStart.getWidth()/2, 
						Gdx.graphics.getHeight()/2 - tutorialStart.getHeight()/2);
		tutorialDTC.add(dtc).pad(0,0,tutorialStart.getHeight()/2,20).row();
		tutorialDTC.setVisible(false);
		tutorialDTC.setMovable(false);
		guiStage.addActor(tutorialDTC);
		
		tutorialRec = new Dialog("Welocome to the Ticket to Ride Advisor Tutorial", TTRAdvisorApp.skin);
		tutorialRec.setWidth(Gdx.graphics.getWidth()/2);
		tutorialRec.setHeight(Gdx.graphics.getHeight()/2);
		tutorialRec.setPosition(Gdx.graphics.getWidth()/2 - tutorialStart.getWidth()/2, 
						Gdx.graphics.getHeight()/2 - tutorialStart.getHeight()/2);
		tutorialRec.add(showRec).pad(0,0,tutorialStart.getHeight()/2,20).row();
		tutorialRec.setVisible(false);
		tutorialRec.setMovable(false);
		guiStage.addActor(tutorialRec);
		
		tutorialDDT = new Dialog("Welocome to the Ticket to Ride Advisor Tutorial", TTRAdvisorApp.skin);
		tutorialDDT.setWidth(Gdx.graphics.getWidth()/2);
		tutorialDDT.setHeight(Gdx.graphics.getHeight()/2);
		tutorialDDT.setPosition(Gdx.graphics.getWidth()/2 - tutorialStart.getWidth()/2, 
						Gdx.graphics.getHeight()/2 - tutorialStart.getHeight()/2);
		tutorialDDT.add(ddt).pad(0,0,tutorialStart.getHeight()/2,20).row();
		tutorialDDT.setVisible(false);
		tutorialDDT.setMovable(false);
		guiStage.addActor(tutorialDDT);
		
		tutorialPT = new Dialog("Welocome to the Ticket to Ride Advisor Tutorial", TTRAdvisorApp.skin);
		tutorialPT.setWidth(Gdx.graphics.getWidth()/2);
		tutorialPT.setHeight(Gdx.graphics.getHeight()/2);
		tutorialPT.setPosition(Gdx.graphics.getWidth()/2 - tutorialStart.getWidth()/2, 
						Gdx.graphics.getHeight()/2 - tutorialStart.getHeight()/2);
		tutorialPT.add(pt).pad(0,0,tutorialStart.getHeight()/2,20).row();
		tutorialPT.setVisible(false);
		tutorialPT.setMovable(false);
		guiStage.addActor(tutorialPT);
		
		tutorialCR = new Dialog("Welocome to the Ticket to Ride Advisor Tutorial", TTRAdvisorApp.skin);
		tutorialCR.setWidth(Gdx.graphics.getWidth()/2);
		tutorialCR.setHeight(Gdx.graphics.getHeight()/2);
		tutorialCR.setPosition(Gdx.graphics.getWidth()/2 - tutorialStart.getWidth()/2, 
						Gdx.graphics.getHeight()/2 - tutorialStart.getHeight()/2);
		tutorialCR.add(cr).pad(0,0,tutorialStart.getHeight()/2,20).row();
		tutorialCR.setVisible(false);
		tutorialCR.setMovable(false);
		guiStage.addActor(tutorialCR);
		
		setupRecommendations();

		setupCardInputHandling();

		setupMapInputHandling();

		setupTurnView();
		
		setupClaimRouteButton();

		Image map = new Image(new Texture("high_res_map.png"));

		
		// Important note: The assumption here is that we NEVER
		// scale texture coordinates independently of the world coordinates
		// So, 1px in texture is always == 1px in world
		mapWidth = map.getWidth();
		mapHeight = map.getHeight();

		mapStage.addActor(map);
		camera = (OrthographicCamera) mapStage.getViewport().getCamera();
		camera.setToOrtho(false);
		camera.zoom = 2;

		camera.position.set(1, 1, 0);
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

		recommendationsButton = new TextButton("ShowRecommendations", TTRAdvisorApp.skin, "small");
		recommendationsButton.setHeight(recommendationsButton.getHeight() / 2);
		recommendationsButton.setPosition(Gdx.graphics.getWidth() / 5, Gdx.graphics.getHeight() * .15f);
		recommendationsButton.addListener(new InputListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

				tutorialDTC.setVisible(false);
				tutorialRec.setVisible(true);
				if (showHide) {
					rec1.setVisible(true);
					rec2.setVisible(true);
					rec3.setVisible(true);
					recommendationsButton.setText("HideRecommendations");
					showHide = false;
					return;
				}
				if (!showHide) {
					rec1.setVisible(false);
					rec2.setVisible(false);
					rec3.setVisible(false);
					recommendationsButton.setText("ShowRecommendations");
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
		guiStage.addActor(recommendationsButton);
		guiStage.addActor(l1);
		guiStage.addActor(l2);
		guiStage.addActor(l3);

	}

	private void setupCardInputHandling() {
//		errorMessage = new Label("", TTRAdvisorApp.skin);
		// Button to draw Destination tickets
		destButton = new TextButton("Draw Destination \n Ticket", TTRAdvisorApp.skin, "small");
		TCButton = new TextButton("Draw Train \n Card", TTRAdvisorApp.skin, "small");
		trainCardHand = new Label("", TTRAdvisorApp.skin);
		trainCardHand.setWidth(Gdx.graphics.getWidth() / 5);
		trainCardHand.setPosition(trainCardHand.getWidth(), trainCardHand.getHeight() / 8);
		// Button to draw Destination tickets
		destButton.setWidth(Gdx.graphics.getWidth() / 5);
		destButton.setPosition(Gdx.graphics.getWidth() - destButton.getWidth(), destButton.getHeight() / 8);
		// Jake: Still need to add way to choose DTs within this screen
		destButton.addListener(new InputListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				tutorialRec.setVisible(false);
				tutorialDDT.setVisible(true);
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
				tutorialStart.setVisible(false);
				tutorialDTC.setVisible(true);
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

	private void setupClaimRouteButton() {
		demoSelectedCity = new Label("", mainApp.skin);
		demoSelectedCity.setColor(0, 0, 0, 1);
		demoSelectedCity.setSize(100, 30);
		demoSelectedCity.setPosition(50, 300);
		demoSelectedCity.setVisible(false);
		guiStage.addActor(demoSelectedCity);

		demoSelectedRoute = new Label("", mainApp.skin);
		demoSelectedRoute.setColor(0, 0, 0, 1);
		demoSelectedRoute.setSize(100, 30);
		demoSelectedRoute.setPosition(50, 260);
		demoSelectedRoute.setVisible(false);
		guiStage.addActor(demoSelectedRoute);
		
		claimRouteTooltip = new Label("", TTRAdvisorApp.skin);
		claimRouteButton = new TextButton("Claim A Route", TTRAdvisorApp.skin, "small");
		claimRouteTooltip.setColor(Color.BLACK);

		claimRouteButton.setPosition(prevTurn.getX() + prevTurn.getWidth()*2, Gdx.graphics.getHeight() - claimRouteButton.getHeight());
		claimRouteButton.addListener(new InputListener() {
			
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				tutorialPT.setVisible(false);
				tutorialCR.setVisible(true);
			}
			
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
		});
		guiStage.addActor(claimRouteTooltip);
		guiStage.addActor(claimRouteButton);
	}
	
	private void setupTurnView() {
		prevTurn = new TextButton("View \n Previous Turn", TTRAdvisorApp.skin, "small");
		prevTurn.setPosition(0, Gdx.graphics.getHeight() - prevTurn.getHeight());
		nextTurn = new TextButton("View \n Next Turn", TTRAdvisorApp.skin, "small");
		nextTurn.setPosition(Gdx.graphics.getWidth() - nextTurn.getWidth(),
				Gdx.graphics.getHeight() - nextTurn.getHeight());
		quit = new TextButton("Quit Game", TTRAdvisorApp.skin, "small");
		
		quit.setPosition(nextTurn.getX() - nextTurn.getWidth()*2, Gdx.graphics.getHeight() - quit.getHeight());
		turnNumber = new Label(Integer.toString(mainApp.hist.getTurnIndex()), TTRAdvisorApp.skin);
		turnNumber.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() - turnNumber.getHeight());
		prevTurn.addListener(new InputListener() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				tutorialDDT.setVisible(false);
				tutorialPT.setVisible(true);
			}

			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
		});
		nextTurn.addListener(new InputListener() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

			}

			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
		});
		quit.addListener(new InputListener() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				mainApp.gameState = new GameState(null, new ArrayList<Player>(), new Board("cities.txt"),
						new DestinationTicketList("destinations.txt"), new ArrayList<Turn>());
				mainApp.turnInput = new InputTurnController(mainApp.gameState);
				mainApp.hist = new HistoryController(mainApp.gameState);
				mainApp.setScreen(new TitleScreen(mainApp));
			}

			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			
		});
		guiStage.addActor(turnNumber);
		guiStage.addActor(prevTurn);
		guiStage.addActor(nextTurn);
		guiStage.addActor(quit);
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
		
		mapStage.getCamera().update();

		if (!mapStage.getRoot().isVisible())
			return;

		mapStage.getBatch().setProjectionMatrix(camera.combined);

		mapStage.getBatch().begin();

		mapStage.getRoot().draw(mapStage.getBatch(), 1);

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
