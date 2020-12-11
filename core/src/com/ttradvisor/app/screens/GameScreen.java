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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.utils.ArraySelection;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ttradvisor.app.TTRAdvisorApp;
import com.ttradvisor.app.classes.Action;
import com.ttradvisor.app.classes.Board;
import com.ttradvisor.app.classes.Board.Route;
import com.ttradvisor.app.classes.CityLocations;
import com.ttradvisor.app.classes.CityLocations.CityLocation;
import com.ttradvisor.app.classes.Colors;
import com.ttradvisor.app.classes.DestinationAction;
import com.ttradvisor.app.classes.DestinationTicket;
import com.ttradvisor.app.classes.DestinationTicketList;
import com.ttradvisor.app.classes.GameState;
import com.ttradvisor.app.classes.HistoryController;
import com.ttradvisor.app.classes.InputTurnController;
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

	private float mapWidth;
	private float mapHeight;

	private boolean mapTappingDisabled = true;
	private CityLocations cityLocs;

	private static final String DEFAULT_CITY_LABEL = "No city selected.";
	private static final String DEFAULT_ROUTE_LABEL = "No route selected.";

	private String selectedCity = DEFAULT_CITY_LABEL;
	private String selectedRoute = DEFAULT_ROUTE_LABEL;
	private Label demoSelectedCity;
	private Label demoSelectedRoute;

	private TextButton destButton;
	private TextButton TCButton;
	// private Label trainCardHand;

	private TextButton claimRouteButton;
	private Label claimRouteTooltip;
	private TextButton cancelClaimRoute;

	private TextButton recommendationsButton;

	private TextButton handDisplayButton;
	private Table handDisplay;
	private Label[] handDisplayLabels;

	private Table organizer;
	private ScrollPane handDisplayPane;
	private List<DestinationTicket> handDisplayTicketList;

	// for multiple selection
	private Array<DestinationTicket> prevSelection;

	private TextButton prevTurn;
	private TextButton nextTurn;
	private Label turnNumber;

	private TextButton quit;

//	private Label errorMessage;
	private TextButton errorMessage;

	private TextureAtlas textureAtlas;
	private Sprite trainImage;
	private RouteLocations routeLocations;

	private ImageTextButton itbBlack;
	private ImageTextButton itbBlue;
	private ImageTextButton itbGreen;
	private ImageTextButton itbRed;
	private ImageTextButton itbYellow;
	private ImageTextButton itbCurrent;


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

//		errorMessage = new Label("", TTRAdvisorApp.skin);
//		errorMessage.setColor(Color.RED);

		errorMessage = new TextButton("", TTRAdvisorApp.skin, "error");
		errorMessage.setVisible(false);

		setupRecommendations();

		setupDisplayElements();

		setupCardInputHandling();

		setupMapInputHandling();

		setupTurnView();

		setupClaimRouteButton();

		setupHandDisplayButton();

//		setupClaimedRouteTextures();

		Image map = new Image(new Texture("high_res_map.png"));

		// filenames for images

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
		Label l1 = new Label("First Recommendation", TTRAdvisorApp.skin, "handDisplaySmall");
		Label l2 = new Label("Second Recommendation", TTRAdvisorApp.skin, "handDisplaySmall");
		Label l3 = new Label("Third Recommendation", TTRAdvisorApp.skin, "handDisplaySmall");
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

	private void setupDisplayElements() {
		// MOCKUP to show state internals
		// Delete this after
//		demonstration = new List<Colors.player>(TTRAdvisorApp.skin);
//		Array<Colors.player> demoColors = new Array<Colors.player>();
//		for (Player p : mainApp.gameState.getPlayers()) {
//			demoColors.add(p.getColor());
//		}
//		demonstration.setItems(demoColors);
//		demonstration.setSize(200, 100);
//		demonstration.setPosition(50, 500);
//		guiStage.addActor(demonstration);

//		demoCurrPlayer = new Label("Current player: " + mainApp.gameState.currentPlayer.getColor(), mainApp.skin);
//		demoCurrPlayer.setColor(0, 0, 0, 1);
//		demoCurrPlayer.setSize(100, 30);
//		demoCurrPlayer.setPosition(50, 400);
//		guiStage.addActor(demoCurrPlayer);

		// moved demo city and demo route to claim route setup

//		Skin skin = new Skin(Gdx.files.internal("untitled folder/untitled.json"));
//		Skin skinStatic = mainApp.skin;
		itbBlack = new ImageTextButton("", TTRAdvisorApp.skin, "playerBlack");
		itbBlue = new ImageTextButton("", TTRAdvisorApp.skin, "playerBlue");
		itbGreen = new ImageTextButton("", TTRAdvisorApp.skin, "playerGreen");
		itbRed = new ImageTextButton("", TTRAdvisorApp.skin, "playerRed");
		itbYellow = new ImageTextButton("", TTRAdvisorApp.skin, "playerYellow");
		itbCurrent = new ImageTextButton("", TTRAdvisorApp.skin, "playerCurrent");

		itbCurrent.setSize(60, 60);

		for (Player p : mainApp.gameState.getPlayers()) {
			switch (p.getColor()) {
			case BLACK:
				itbBlack.setText(Integer.toString(p.getScore()));
				itbBlack.setPosition(Align.left, Gdx.graphics.getHeight() * 4 / 5
						- mainApp.gameState.getPlayers().indexOf(p) * itbCurrent.getHeight());
				itbBlack.setSize(50, 50);
				if (mainApp.gameState.currentPlayer.equals(p)) {
					itbCurrent.setPosition(itbBlack.getX() - (itbCurrent.getWidth() - itbBlack.getWidth()) / 2,
							itbBlack.getY() - ((itbCurrent.getHeight() - itbBlack.getHeight()) / 2));
					guiStage.addActor(itbCurrent);
				}
				guiStage.addActor(itbBlack);
				break;
			case BLUE:
				itbBlue.setText(Integer.toString(p.getScore()));
				itbBlue.setPosition(Align.left, Gdx.graphics.getHeight() * 4 / 5
						- mainApp.gameState.getPlayers().indexOf(p) * itbCurrent.getHeight());
				itbBlue.setSize(50, 50);
//				itbBlue.setBackground(itbBackground.getBackground());
				if (mainApp.gameState.currentPlayer.equals(p)) {
					itbCurrent.setPosition(itbBlue.getX() - ((itbCurrent.getWidth() - itbBlue.getWidth()) / 2),
							itbBlue.getY() - ((itbCurrent.getHeight() - itbBlue.getHeight()) / 2));
					guiStage.addActor(itbCurrent);

				}
				guiStage.addActor(itbBlue);
				break;
			case GREEN:
				itbGreen.setText(Integer.toString(p.getScore()));
				itbGreen.setPosition(Align.left, Gdx.graphics.getHeight() * 4 / 5
						- mainApp.gameState.getPlayers().indexOf(p) * itbCurrent.getHeight());
				itbGreen.setSize(50, 50);
				if (mainApp.gameState.currentPlayer.equals(p)) {
					itbCurrent.setPosition(itbGreen.getX() - ((itbCurrent.getWidth() - itbGreen.getWidth()) / 2),
							itbGreen.getY() - ((itbCurrent.getHeight() - itbGreen.getHeight()) / 2));
					guiStage.addActor(itbCurrent);

				}
				guiStage.addActor(itbGreen);
				break;
			case NONE:
				Gdx.app.error("GameScreen", "Player of color \"NONE\" exists in game state.");
//				button4.setPosition(mainApp.gameState.getPlayers().indexOf(p)*100+50, 500);
//				tempPix.setColor(1, 1, 1, 1);
				// non-fatal error
				break;
			case RED:
				itbRed.setText(Integer.toString(p.getScore()));
				itbRed.setPosition(Align.left, Gdx.graphics.getHeight() * 4 / 5
						- mainApp.gameState.getPlayers().indexOf(p) * itbCurrent.getHeight());
				itbRed.setSize(50, 50);
//				button4.setSi
				if (mainApp.gameState.currentPlayer.equals(p)) {
					itbCurrent.setPosition(itbRed.getX() - ((itbCurrent.getWidth() - itbRed.getWidth()) / 2),
							itbRed.getY() - ((itbCurrent.getHeight() - itbRed.getHeight()) / 2));
					guiStage.addActor(itbCurrent);

				}
				guiStage.addActor(itbRed);
				break;
			case YELLOW:
				itbYellow.setText(Integer.toString(p.getScore()));
				itbYellow.setPosition(Align.left, Gdx.graphics.getHeight() * 4 / 5
						- mainApp.gameState.getPlayers().indexOf(p) * itbCurrent.getHeight());
				itbYellow.setSize(50, 50);
				if (mainApp.gameState.currentPlayer.equals(p)) {
					itbCurrent.setPosition(itbYellow.getX() - ((itbCurrent.getWidth() - itbYellow.getWidth()) / 2),
							itbYellow.getY() - ((itbCurrent.getHeight() - itbYellow.getHeight()) / 2));
					guiStage.addActor(itbCurrent);
				}
				guiStage.addActor(itbYellow);
				break;
			default:
				Gdx.app.error("GameScreen", "Player of invalid color exists in game state.");
//				tempPix.setColor(1, 1, 1, 1);
				// non-fatal error
				break;
			}
		}
	}

	private void playerIconUpdate(Player currentPlayerInHistory) {
		Player curPLayerIcon = currentPlayerInHistory;
		;
		System.out.println("curr player color" + curPLayerIcon.getColor());
		for (Player p : mainApp.gameState.getPlayers()) {
			switch (p.getColor()) {
			case BLACK:
				itbBlack.setText(Integer.toString(p.getScore()));
				itbBlack.toFront();
				if (curPLayerIcon.equals(p)) {
					itbCurrent.setPosition(itbBlack.getX() - (itbCurrent.getWidth() - itbBlack.getWidth()) / 2,
							itbBlack.getY() - ((itbCurrent.getHeight() - itbBlack.getHeight()) / 2));
					guiStage.addActor(itbCurrent);
				}
				guiStage.addActor(itbBlack);
				break;
			case BLUE:
				itbBlue.setText(Integer.toString(p.getScore()));
				itbBlue.toFront();
//				itbBlue.setBackground(itbBackground.getBackground());
				if (curPLayerIcon.equals(p)) {
					itbCurrent.setPosition(itbBlue.getX() - ((itbCurrent.getWidth() - itbBlue.getWidth()) / 2),
							itbBlue.getY() - ((itbCurrent.getHeight() - itbBlue.getHeight()) / 2));
					guiStage.addActor(itbCurrent);

				}
				guiStage.addActor(itbBlue);
				break;
			case GREEN:
				itbGreen.setText(Integer.toString(p.getScore()));
				itbGreen.toFront();
				if (curPLayerIcon.equals(p)) {
					itbCurrent.setPosition(itbGreen.getX() - ((itbCurrent.getWidth() - itbGreen.getWidth()) / 2),
							itbGreen.getY() - ((itbCurrent.getHeight() - itbGreen.getHeight()) / 2));
					guiStage.addActor(itbCurrent);

				}
				guiStage.addActor(itbGreen);
				break;
			case NONE:
				Gdx.app.error("GameScreen", "Player of color \"NONE\" exists in game state.");
//				button4.setPosition(mainApp.gameState.getPlayers().indexOf(p)*100+50, 500);
//				tempPix.setColor(1, 1, 1, 1);
				// non-fatal error
				break;
			case RED:
				itbRed.setText(Integer.toString(p.getScore()));
				itbRed.toFront();
//				button4.setSi
				if (curPLayerIcon.equals(p)) {
					itbCurrent.setPosition(itbRed.getX() - ((itbCurrent.getWidth() - itbRed.getWidth()) / 2),
							itbRed.getY() - ((itbCurrent.getHeight() - itbRed.getHeight()) / 2));
					guiStage.addActor(itbCurrent);

				}
				guiStage.addActor(itbRed);
				break;
			case YELLOW:
				itbYellow.setText(Integer.toString(p.getScore()));
				itbYellow.toFront();
				if (curPLayerIcon.equals(p)) {
					itbCurrent.setPosition(itbYellow.getX() - ((itbCurrent.getWidth() - itbYellow.getWidth()) / 2),
							itbYellow.getY() - ((itbCurrent.getHeight() - itbYellow.getHeight()) / 2));
					guiStage.addActor(itbCurrent);
				}
				guiStage.addActor(itbYellow);
				break;
			default:
				Gdx.app.error("GameScreen", "Player of invalid color exists in game state.");
//				tempPix.setColor(1, 1, 1, 1);
				// non-fatal error
				break;
			}
		}

	}

	private void setupCardInputHandling() {
		// Button to draw Destination tickets
		destButton = new TextButton("Draw Destination \n Ticket", TTRAdvisorApp.skin, "small");
		TCButton = new TextButton("Draw Train \n Card", TTRAdvisorApp.skin, "small");
//		trainCardHand = new Label(mainApp.gameState.currentPlayer.getTCS().toString(), TTRAdvisorApp.skin);
//		trainCardHand.setWidth(Gdx.graphics.getWidth() / 5);
//		trainCardHand.setPosition(trainCardHand.getWidth(), trainCardHand.getHeight() / 8);
		// Button to draw Destination tickets
		destButton.setWidth(Gdx.graphics.getWidth() / 5);
		destButton.setPosition(Gdx.graphics.getWidth() - destButton.getWidth(), destButton.getHeight() / 8);
		destButton.addListener(new InputListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				helperDisableUIForActionInput();
				// trainCardHand.setVisible(false);
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
					
					prevSelection = null;
					// listPane.
					
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

						multipleTickets = destTickets.getSelection().items().orderedItems();
						while (multipleTickets.notEmpty()) {
							System.out.println(multipleTickets.peek().toString());
							drawnTickets.add(multipleTickets.pop());
						}

						boolean isInitial = mainApp.turnInput.isInitialTurn();

						if (mainApp.hist.getTurnIndex() == mainApp.gameState.getCurrentTurnCounter() - 1) {
							if (mainApp.turnInput.makeCorrection(
									new DestinationAction(mainApp.gameState.currentPlayer, drawnTickets))) {
								if (mainApp.hist.getTurnIndex() == 0) {
									isInitial = true;
								}
								advanceTurn(isInitial,
										new DestinationAction(mainApp.gameState.currentPlayer, drawnTickets));
							} else {
								TextButton errorMessageTemp = new TextButton(mainApp.gameState.getError(),
										TTRAdvisorApp.skin, "error");
								errorMessage.setText(mainApp.gameState.getError());
								errorMessage.setWidth(errorMessageTemp.getWidth());
								errorMessage.setPosition(Gdx.graphics.getWidth() / 2 - errorMessage.getWidth() / 2,
										Gdx.graphics.getHeight() / 8);
								errorMessage.setVisible(true);
								if (errorMessage.getText().length() != 0) {
									guiStage.addActor(errorMessage);
								} else {
									errorMessage.setVisible(false);
								}
							}
						}

						else if (mainApp.turnInput
								.takeAction(new DestinationAction(mainApp.gameState.currentPlayer, drawnTickets))) {
							advanceTurn(isInitial,
									new DestinationAction(mainApp.gameState.currentPlayer, drawnTickets));
						} else {
							TextButton errorMessageTemp = new TextButton(mainApp.gameState.getError(),
									TTRAdvisorApp.skin, "error");
							errorMessage.setText(mainApp.gameState.getError());
							errorMessage.setWidth(errorMessageTemp.getWidth());
							errorMessage.setPosition(Gdx.graphics.getWidth() / 2 - errorMessage.getWidth() / 2,
									Gdx.graphics.getHeight() / 8);
							errorMessage.setVisible(true);
							if (errorMessage.getText().length() != 0) {
								guiStage.addActor(errorMessage);
							} else {
								errorMessage.setVisible(false);
							}
						}

						guiStage.setScrollFocus(null);

						helperReenableUIForActionInput();
						// trainCardHand.setVisible(true);
						listPane.setVisible(false);
						table.setVisible(false);
					}

					public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
						return true;
					}
				});

				// Button to input non-user DestinationAction
				TextButton nonUser = new TextButton("Non-User", TTRAdvisorApp.skin, "small");
				nonUser.addListener(new InputListener() {
					public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
						// Adds sample DTs since user can't see what they draw
						drawnTickets.add(new DestinationTicket("sample", "sample", 1));
						drawnTickets.add(new DestinationTicket("sample", "sample", 1));

						boolean isInitial = mainApp.turnInput.isInitialTurn();

						if (mainApp.hist.getTurnIndex() == mainApp.gameState.getCurrentTurnCounter() - 1) {
							if (mainApp.turnInput.makeCorrection(
									new DestinationAction(mainApp.gameState.currentPlayer, drawnTickets))) {
								if (mainApp.hist.getTurnIndex() == 0) {
									isInitial = true;
								}
								advanceTurn(isInitial,
										new DestinationAction(mainApp.gameState.currentPlayer, drawnTickets));
							} else {
								TextButton errorMessageTemp = new TextButton(mainApp.gameState.getError(),
										TTRAdvisorApp.skin, "error");
								errorMessage.setText(mainApp.gameState.getError());
								errorMessage.setWidth(errorMessageTemp.getWidth());
								errorMessage.setPosition(Gdx.graphics.getWidth() / 2 - errorMessage.getWidth() / 2,
										Gdx.graphics.getHeight() / 8);
								errorMessage.setVisible(true);
								if (errorMessage.getText().length() != 0) {
									guiStage.addActor(errorMessage);
								} else {
									errorMessage.setVisible(false);
								}
							}
						}

						else if (mainApp.turnInput
								.takeAction(new DestinationAction(mainApp.gameState.currentPlayer, drawnTickets))) {
							advanceTurn(isInitial,
									new DestinationAction(mainApp.gameState.currentPlayer, drawnTickets));
						} else {
							TextButton errorMessageTemp = new TextButton(mainApp.gameState.getError(),
									TTRAdvisorApp.skin, "error");
							errorMessage.setText(mainApp.gameState.getError());
							errorMessage.setWidth(errorMessageTemp.getWidth());
							errorMessage.setPosition(Gdx.graphics.getWidth() / 2 - errorMessage.getWidth() / 2,
									Gdx.graphics.getHeight() / 8);
							errorMessage.setVisible(true);
							if (errorMessage.getText().length() != 0) {
								guiStage.addActor(errorMessage);
							} else {
								errorMessage.setVisible(false);
							}
						}

						guiStage.setScrollFocus(null);

						helperReenableUIForActionInput();
						// trainCardHand.setVisible(true);
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

						helperReenableUIForActionInput();
						// trainCardHand.setVisible(true);
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
				// If it is not the user's turn, skip DTAction button appears in table
				if (!mainApp.gameState.currentPlayer.equals(mainApp.gameState.getUserPlayer())) {
					table.add(nonUser);
					table.row();
				}
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

				helperDisableUIForActionInput();
				// trainCardHand.setVisible(false);

				final ArrayList<TrainCard> drawnCards = new ArrayList<>();
				final Label drawnCardList = new Label(drawnCards.toString(), TTRAdvisorApp.skin);
				drawnCardList.setPosition(Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 2 + 20);

				final ImageTextButton redTrain = new ImageTextButton("", TTRAdvisorApp.skin, "tcRed");
				final ImageTextButton orangeTrain = new ImageTextButton("", TTRAdvisorApp.skin, "tcOrange");
				final ImageTextButton yellowTrain = new ImageTextButton("", TTRAdvisorApp.skin, "tcYellow");
				final ImageTextButton greenTrain = new ImageTextButton("", TTRAdvisorApp.skin, "tcGreen");
				final ImageTextButton blueTrain = new ImageTextButton("", TTRAdvisorApp.skin, "tcBlue");
				final ImageTextButton pinkTrain = new ImageTextButton("", TTRAdvisorApp.skin, "tcPink");
				final ImageTextButton blackTrain = new ImageTextButton("", TTRAdvisorApp.skin, "tcBlack");
				final ImageTextButton whiteTrain = new ImageTextButton("", TTRAdvisorApp.skin, "tcWhite");
				final ImageTextButton wildTrain = new ImageTextButton("", TTRAdvisorApp.skin, "tcAny");

				redTrain.addListener(new InputListener() {
					public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

						drawnCards.add(new TrainCard(Colors.route.RED));
						drawnCardList.setText(drawnCards.toString());
						int drawCount = 0;

						for (TrainCard tc : drawnCards) {
							if (tc.getColor().equals(Colors.route.RED)) {
								drawCount++;
							}
						}
						redTrain.setText(Integer.toString(drawCount));

						if (mainApp.turnInput.isInitialTurn()) {
							if (drawnCards.size() >= 4) {
								redTrain.setTouchable(Touchable.disabled);
								redTrain.setDisabled(true);
								orangeTrain.setTouchable(Touchable.disabled);
								orangeTrain.setDisabled(true);
								yellowTrain.setTouchable(Touchable.disabled);
								yellowTrain.setDisabled(true);
								greenTrain.setTouchable(Touchable.disabled);
								greenTrain.setDisabled(true);
								blueTrain.setTouchable(Touchable.disabled);
								blueTrain.setDisabled(true);
								pinkTrain.setTouchable(Touchable.disabled);
								pinkTrain.setDisabled(true);
								blackTrain.setTouchable(Touchable.disabled);
								blackTrain.setDisabled(true);
								whiteTrain.setTouchable(Touchable.disabled);
								whiteTrain.setDisabled(true);
								wildTrain.setTouchable(Touchable.disabled);
								wildTrain.setDisabled(true);

							}
						} else {
							if (drawnCards.size() >= 2) {
								redTrain.setTouchable(Touchable.disabled);
								redTrain.setDisabled(true);
								orangeTrain.setTouchable(Touchable.disabled);
								orangeTrain.setDisabled(true);
								yellowTrain.setTouchable(Touchable.disabled);
								yellowTrain.setDisabled(true);
								greenTrain.setTouchable(Touchable.disabled);
								greenTrain.setDisabled(true);
								blueTrain.setTouchable(Touchable.disabled);
								blueTrain.setDisabled(true);
								pinkTrain.setTouchable(Touchable.disabled);
								pinkTrain.setDisabled(true);
								blackTrain.setTouchable(Touchable.disabled);
								blackTrain.setDisabled(true);
								whiteTrain.setTouchable(Touchable.disabled);
								whiteTrain.setDisabled(true);
								wildTrain.setTouchable(Touchable.disabled);
								wildTrain.setDisabled(true);
							}
						}

					}

					public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
						return true;
					}
				});

				orangeTrain.addListener(new InputListener() {
					public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
						drawnCards.add(new TrainCard(Colors.route.ORANGE));
						drawnCardList.setText(drawnCards.toString());

						int drawCount = 0;
						for (TrainCard tc : drawnCards) {
							if (tc.getColor().equals(Colors.route.ORANGE)) {
								drawCount++;
							}
						}
						orangeTrain.setText(Integer.toString(drawCount));

						if (mainApp.turnInput.isInitialTurn()) {
							if (drawnCards.size() >= 4) {
								redTrain.setTouchable(Touchable.disabled);
								redTrain.setDisabled(true);
								orangeTrain.setTouchable(Touchable.disabled);
								orangeTrain.setDisabled(true);
								yellowTrain.setTouchable(Touchable.disabled);
								yellowTrain.setDisabled(true);
								greenTrain.setTouchable(Touchable.disabled);
								greenTrain.setDisabled(true);
								blueTrain.setTouchable(Touchable.disabled);
								blueTrain.setDisabled(true);
								pinkTrain.setTouchable(Touchable.disabled);
								pinkTrain.setDisabled(true);
								blackTrain.setTouchable(Touchable.disabled);
								blackTrain.setDisabled(true);
								whiteTrain.setTouchable(Touchable.disabled);
								whiteTrain.setDisabled(true);
								wildTrain.setTouchable(Touchable.disabled);
								wildTrain.setDisabled(true);

							}
						} else {
							if (drawnCards.size() >= 2) {
								redTrain.setTouchable(Touchable.disabled);
								redTrain.setDisabled(true);
								orangeTrain.setTouchable(Touchable.disabled);
								orangeTrain.setDisabled(true);
								yellowTrain.setTouchable(Touchable.disabled);
								yellowTrain.setDisabled(true);
								greenTrain.setTouchable(Touchable.disabled);
								greenTrain.setDisabled(true);
								blueTrain.setTouchable(Touchable.disabled);
								blueTrain.setDisabled(true);
								pinkTrain.setTouchable(Touchable.disabled);
								pinkTrain.setDisabled(true);
								blackTrain.setTouchable(Touchable.disabled);
								blackTrain.setDisabled(true);
								whiteTrain.setTouchable(Touchable.disabled);
								whiteTrain.setDisabled(true);
								wildTrain.setTouchable(Touchable.disabled);
								wildTrain.setDisabled(true);
							}
						}
					}

					public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
						return true;
					}
				});

				yellowTrain.addListener(new InputListener() {
					public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
						drawnCards.add(new TrainCard(Colors.route.YELLOW));
						drawnCardList.setText(drawnCards.toString());

						int drawCount = 0;
						for (TrainCard tc : drawnCards) {
							if (tc.getColor().equals(Colors.route.YELLOW)) {
								drawCount++;
							}
						}
						yellowTrain.setText(Integer.toString(drawCount));

						if (mainApp.turnInput.isInitialTurn()) {
							if (drawnCards.size() >= 4) {
								redTrain.setTouchable(Touchable.disabled);
								redTrain.setDisabled(true);
								orangeTrain.setTouchable(Touchable.disabled);
								orangeTrain.setDisabled(true);
								yellowTrain.setTouchable(Touchable.disabled);
								yellowTrain.setDisabled(true);
								greenTrain.setTouchable(Touchable.disabled);
								greenTrain.setDisabled(true);
								blueTrain.setTouchable(Touchable.disabled);
								blueTrain.setDisabled(true);
								pinkTrain.setTouchable(Touchable.disabled);
								pinkTrain.setDisabled(true);
								blackTrain.setTouchable(Touchable.disabled);
								blackTrain.setDisabled(true);
								whiteTrain.setTouchable(Touchable.disabled);
								whiteTrain.setDisabled(true);
								wildTrain.setTouchable(Touchable.disabled);
								wildTrain.setDisabled(true);

							}
						} else {
							if (drawnCards.size() >= 2) {
								redTrain.setTouchable(Touchable.disabled);
								redTrain.setDisabled(true);
								orangeTrain.setTouchable(Touchable.disabled);
								orangeTrain.setDisabled(true);
								yellowTrain.setTouchable(Touchable.disabled);
								yellowTrain.setDisabled(true);
								greenTrain.setTouchable(Touchable.disabled);
								greenTrain.setDisabled(true);
								blueTrain.setTouchable(Touchable.disabled);
								blueTrain.setDisabled(true);
								pinkTrain.setTouchable(Touchable.disabled);
								pinkTrain.setDisabled(true);
								blackTrain.setTouchable(Touchable.disabled);
								blackTrain.setDisabled(true);
								whiteTrain.setTouchable(Touchable.disabled);
								whiteTrain.setDisabled(true);
								wildTrain.setTouchable(Touchable.disabled);
								wildTrain.setDisabled(true);
							}
						}
					}

					public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
						return true;
					}
				});

				greenTrain.addListener(new InputListener() {
					public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
						drawnCards.add(new TrainCard(Colors.route.GREEN));
						drawnCardList.setText(drawnCards.toString());

						int drawCount = 0;
						for (TrainCard tc : drawnCards) {
							if (tc.getColor().equals(Colors.route.GREEN)) {
								drawCount++;
							}
						}
						greenTrain.setText(Integer.toString(drawCount));

						if (mainApp.turnInput.isInitialTurn()) {
							if (drawnCards.size() >= 4) {
								redTrain.setTouchable(Touchable.disabled);
								redTrain.setDisabled(true);
								orangeTrain.setTouchable(Touchable.disabled);
								orangeTrain.setDisabled(true);
								yellowTrain.setTouchable(Touchable.disabled);
								yellowTrain.setDisabled(true);
								greenTrain.setTouchable(Touchable.disabled);
								greenTrain.setDisabled(true);
								blueTrain.setTouchable(Touchable.disabled);
								blueTrain.setDisabled(true);
								pinkTrain.setTouchable(Touchable.disabled);
								pinkTrain.setDisabled(true);
								blackTrain.setTouchable(Touchable.disabled);
								blackTrain.setDisabled(true);
								whiteTrain.setTouchable(Touchable.disabled);
								whiteTrain.setDisabled(true);
								wildTrain.setTouchable(Touchable.disabled);
								wildTrain.setDisabled(true);

							}
						} else {
							if (drawnCards.size() >= 2) {
								redTrain.setTouchable(Touchable.disabled);
								redTrain.setDisabled(true);
								orangeTrain.setTouchable(Touchable.disabled);
								orangeTrain.setDisabled(true);
								yellowTrain.setTouchable(Touchable.disabled);
								yellowTrain.setDisabled(true);
								greenTrain.setTouchable(Touchable.disabled);
								greenTrain.setDisabled(true);
								blueTrain.setTouchable(Touchable.disabled);
								blueTrain.setDisabled(true);
								pinkTrain.setTouchable(Touchable.disabled);
								pinkTrain.setDisabled(true);
								blackTrain.setTouchable(Touchable.disabled);
								blackTrain.setDisabled(true);
								whiteTrain.setTouchable(Touchable.disabled);
								whiteTrain.setDisabled(true);
								wildTrain.setTouchable(Touchable.disabled);
								wildTrain.setDisabled(true);
							}
						}
					}

					public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
						return true;
					}
				});

				blueTrain.addListener(new InputListener() {
					public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
						drawnCards.add(new TrainCard(Colors.route.BLUE));
						drawnCardList.setText(drawnCards.toString());

						int drawCount = 0;
						for (TrainCard tc : drawnCards) {
							if (tc.getColor().equals(Colors.route.BLUE)) {
								drawCount++;
							}
						}
						blueTrain.setText(Integer.toString(drawCount));

						if (mainApp.turnInput.isInitialTurn()) {
							if (drawnCards.size() >= 4) {
								redTrain.setTouchable(Touchable.disabled);
								redTrain.setDisabled(true);
								orangeTrain.setTouchable(Touchable.disabled);
								orangeTrain.setDisabled(true);
								yellowTrain.setTouchable(Touchable.disabled);
								yellowTrain.setDisabled(true);
								greenTrain.setTouchable(Touchable.disabled);
								greenTrain.setDisabled(true);
								blueTrain.setTouchable(Touchable.disabled);
								blueTrain.setDisabled(true);
								pinkTrain.setTouchable(Touchable.disabled);
								pinkTrain.setDisabled(true);
								blackTrain.setTouchable(Touchable.disabled);
								blackTrain.setDisabled(true);
								whiteTrain.setTouchable(Touchable.disabled);
								whiteTrain.setDisabled(true);
								wildTrain.setTouchable(Touchable.disabled);
								wildTrain.setDisabled(true);

							}
						} else {
							if (drawnCards.size() >= 2) {
								redTrain.setTouchable(Touchable.disabled);
								redTrain.setDisabled(true);
								orangeTrain.setTouchable(Touchable.disabled);
								orangeTrain.setDisabled(true);
								yellowTrain.setTouchable(Touchable.disabled);
								yellowTrain.setDisabled(true);
								greenTrain.setTouchable(Touchable.disabled);
								greenTrain.setDisabled(true);
								blueTrain.setTouchable(Touchable.disabled);
								blueTrain.setDisabled(true);
								pinkTrain.setTouchable(Touchable.disabled);
								pinkTrain.setDisabled(true);
								blackTrain.setTouchable(Touchable.disabled);
								blackTrain.setDisabled(true);
								whiteTrain.setTouchable(Touchable.disabled);
								whiteTrain.setDisabled(true);
								wildTrain.setTouchable(Touchable.disabled);
								wildTrain.setDisabled(true);
							}
						}
					}

					public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
						return true;
					}
				});

				pinkTrain.addListener(new InputListener() {
					public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
						drawnCards.add(new TrainCard(Colors.route.PINK));
						drawnCardList.setText(drawnCards.toString());

						int drawCount = 0;
						for (TrainCard tc : drawnCards) {
							if (tc.getColor().equals(Colors.route.PINK)) {
								drawCount++;
							}
						}
						pinkTrain.setText(Integer.toString(drawCount));

						if (mainApp.turnInput.isInitialTurn()) {
							if (drawnCards.size() >= 4) {
								redTrain.setTouchable(Touchable.disabled);
								redTrain.setDisabled(true);
								orangeTrain.setTouchable(Touchable.disabled);
								orangeTrain.setDisabled(true);
								yellowTrain.setTouchable(Touchable.disabled);
								yellowTrain.setDisabled(true);
								greenTrain.setTouchable(Touchable.disabled);
								greenTrain.setDisabled(true);
								blueTrain.setTouchable(Touchable.disabled);
								blueTrain.setDisabled(true);
								pinkTrain.setTouchable(Touchable.disabled);
								pinkTrain.setDisabled(true);
								blackTrain.setTouchable(Touchable.disabled);
								blackTrain.setDisabled(true);
								whiteTrain.setTouchable(Touchable.disabled);
								whiteTrain.setDisabled(true);
								wildTrain.setTouchable(Touchable.disabled);
								wildTrain.setDisabled(true);

							}
						} else {
							if (drawnCards.size() >= 2) {
								redTrain.setTouchable(Touchable.disabled);
								redTrain.setDisabled(true);
								orangeTrain.setTouchable(Touchable.disabled);
								orangeTrain.setDisabled(true);
								yellowTrain.setTouchable(Touchable.disabled);
								yellowTrain.setDisabled(true);
								greenTrain.setTouchable(Touchable.disabled);
								greenTrain.setDisabled(true);
								blueTrain.setTouchable(Touchable.disabled);
								blueTrain.setDisabled(true);
								pinkTrain.setTouchable(Touchable.disabled);
								pinkTrain.setDisabled(true);
								blackTrain.setTouchable(Touchable.disabled);
								blackTrain.setDisabled(true);
								whiteTrain.setTouchable(Touchable.disabled);
								whiteTrain.setDisabled(true);
								wildTrain.setTouchable(Touchable.disabled);
								wildTrain.setDisabled(true);
							}
						}
					}

					public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
						return true;
					}
				});

				blackTrain.addListener(new InputListener() {
					public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
						drawnCards.add(new TrainCard(Colors.route.BLACK));
						drawnCardList.setText(drawnCards.toString());

						int drawCount = 0;
						for (TrainCard tc : drawnCards) {
							if (tc.getColor().equals(Colors.route.BLACK)) {
								drawCount++;
							}
						}
						blackTrain.setText(Integer.toString(drawCount));

						if (mainApp.turnInput.isInitialTurn()) {
							if (drawnCards.size() >= 4) {
								redTrain.setTouchable(Touchable.disabled);
								redTrain.setDisabled(true);
								orangeTrain.setTouchable(Touchable.disabled);
								orangeTrain.setDisabled(true);
								yellowTrain.setTouchable(Touchable.disabled);
								yellowTrain.setDisabled(true);
								greenTrain.setTouchable(Touchable.disabled);
								greenTrain.setDisabled(true);
								blueTrain.setTouchable(Touchable.disabled);
								blueTrain.setDisabled(true);
								pinkTrain.setTouchable(Touchable.disabled);
								pinkTrain.setDisabled(true);
								blackTrain.setTouchable(Touchable.disabled);
								blackTrain.setDisabled(true);
								whiteTrain.setTouchable(Touchable.disabled);
								whiteTrain.setDisabled(true);
								wildTrain.setTouchable(Touchable.disabled);
								wildTrain.setDisabled(true);

							}
						} else {
							if (drawnCards.size() >= 2) {
								redTrain.setTouchable(Touchable.disabled);
								redTrain.setDisabled(true);
								orangeTrain.setTouchable(Touchable.disabled);
								orangeTrain.setDisabled(true);
								yellowTrain.setTouchable(Touchable.disabled);
								yellowTrain.setDisabled(true);
								greenTrain.setTouchable(Touchable.disabled);
								greenTrain.setDisabled(true);
								blueTrain.setTouchable(Touchable.disabled);
								blueTrain.setDisabled(true);
								pinkTrain.setTouchable(Touchable.disabled);
								pinkTrain.setDisabled(true);
								blackTrain.setTouchable(Touchable.disabled);
								blackTrain.setDisabled(true);
								whiteTrain.setTouchable(Touchable.disabled);
								whiteTrain.setDisabled(true);
								wildTrain.setTouchable(Touchable.disabled);
								wildTrain.setDisabled(true);
							}
						}
					}

					public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
						return true;
					}
				});

				whiteTrain.addListener(new InputListener() {
					public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
						drawnCards.add(new TrainCard(Colors.route.WHITE));
						drawnCardList.setText(drawnCards.toString());

						int drawCount = 0;
						for (TrainCard tc : drawnCards) {
							if (tc.getColor().equals(Colors.route.WHITE)) {
								drawCount++;
							}
						}
						whiteTrain.setText(Integer.toString(drawCount));

						if (mainApp.turnInput.isInitialTurn()) {
							if (drawnCards.size() >= 4) {
								redTrain.setTouchable(Touchable.disabled);
								redTrain.setDisabled(true);
								orangeTrain.setTouchable(Touchable.disabled);
								orangeTrain.setDisabled(true);
								yellowTrain.setTouchable(Touchable.disabled);
								yellowTrain.setDisabled(true);
								greenTrain.setTouchable(Touchable.disabled);
								greenTrain.setDisabled(true);
								blueTrain.setTouchable(Touchable.disabled);
								blueTrain.setDisabled(true);
								pinkTrain.setTouchable(Touchable.disabled);
								pinkTrain.setDisabled(true);
								blackTrain.setTouchable(Touchable.disabled);
								blackTrain.setDisabled(true);
								whiteTrain.setTouchable(Touchable.disabled);
								whiteTrain.setDisabled(true);
								wildTrain.setTouchable(Touchable.disabled);
								wildTrain.setDisabled(true);

							}
						} else {
							if (drawnCards.size() >= 2) {
								redTrain.setTouchable(Touchable.disabled);
								redTrain.setDisabled(true);
								orangeTrain.setTouchable(Touchable.disabled);
								orangeTrain.setDisabled(true);
								yellowTrain.setTouchable(Touchable.disabled);
								yellowTrain.setDisabled(true);
								greenTrain.setTouchable(Touchable.disabled);
								greenTrain.setDisabled(true);
								blueTrain.setTouchable(Touchable.disabled);
								blueTrain.setDisabled(true);
								pinkTrain.setTouchable(Touchable.disabled);
								pinkTrain.setDisabled(true);
								blackTrain.setTouchable(Touchable.disabled);
								blackTrain.setDisabled(true);
								whiteTrain.setTouchable(Touchable.disabled);
								whiteTrain.setDisabled(true);
								wildTrain.setTouchable(Touchable.disabled);
								wildTrain.setDisabled(true);
							}
						}
					}

					public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
						return true;
					}
				});

				wildTrain.addListener(new InputListener() {
					public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
						drawnCards.add(new TrainCard(Colors.route.ANY));
						drawnCardList.setText(drawnCards.toString());

						int drawCount = 0;
						for (TrainCard tc : drawnCards) {
							if (tc.getColor().equals(Colors.route.ANY)) {
								drawCount++;
							}
						}
						wildTrain.setText(Integer.toString(drawCount));

						if (mainApp.turnInput.isInitialTurn()) {
							if (drawnCards.size() >= 4) {
								redTrain.setTouchable(Touchable.disabled);
								redTrain.setDisabled(true);
								orangeTrain.setTouchable(Touchable.disabled);
								orangeTrain.setDisabled(true);
								yellowTrain.setTouchable(Touchable.disabled);
								yellowTrain.setDisabled(true);
								greenTrain.setTouchable(Touchable.disabled);
								greenTrain.setDisabled(true);
								blueTrain.setTouchable(Touchable.disabled);
								blueTrain.setDisabled(true);
								pinkTrain.setTouchable(Touchable.disabled);
								pinkTrain.setDisabled(true);
								blackTrain.setTouchable(Touchable.disabled);
								blackTrain.setDisabled(true);
								whiteTrain.setTouchable(Touchable.disabled);
								whiteTrain.setDisabled(true);
								wildTrain.setTouchable(Touchable.disabled);
								wildTrain.setDisabled(true);

							}
						} else {
							if (drawnCards.size() >= 2) {
								redTrain.setTouchable(Touchable.disabled);
								redTrain.setDisabled(true);
								orangeTrain.setTouchable(Touchable.disabled);
								orangeTrain.setDisabled(true);
								yellowTrain.setTouchable(Touchable.disabled);
								yellowTrain.setDisabled(true);
								greenTrain.setTouchable(Touchable.disabled);
								greenTrain.setDisabled(true);
								blueTrain.setTouchable(Touchable.disabled);
								blueTrain.setDisabled(true);
								pinkTrain.setTouchable(Touchable.disabled);
								pinkTrain.setDisabled(true);
								blackTrain.setTouchable(Touchable.disabled);
								blackTrain.setDisabled(true);
								whiteTrain.setTouchable(Touchable.disabled);
								whiteTrain.setDisabled(true);
								wildTrain.setTouchable(Touchable.disabled);
								wildTrain.setDisabled(true);
							}
						}
					}

					public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
						return true;
					}
				});
				TextButton done = new TextButton("Done", TTRAdvisorApp.skin, "small");
				done.addListener(new InputListener() {
					public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

						boolean isInitial = mainApp.turnInput.isInitialTurn();

						if (mainApp.hist.getTurnIndex() == mainApp.gameState.getCurrentTurnCounter() - 1) {
							if (mainApp.turnInput
									.makeCorrection(new TrainCardAction(mainApp.gameState.currentPlayer, drawnCards))) {
								if (mainApp.hist.getTurnIndex() == 0) {
									isInitial = true;
								}
								advanceTurn(isInitial,
										new TrainCardAction(mainApp.gameState.currentPlayer, drawnCards));
							} else {
								Label errorMessageTemp = new Label(mainApp.gameState.getError(), TTRAdvisorApp.skin);
								errorMessage.setText(mainApp.gameState.getError());
								errorMessage.setWidth(errorMessageTemp.getWidth());
								errorMessage.setPosition(Gdx.graphics.getWidth() / 2 - errorMessage.getWidth() / 2,
										Gdx.graphics.getHeight() / 8);
								errorMessage.setVisible(true);
							}
						}

						else if (mainApp.turnInput
								.takeAction(new TrainCardAction(mainApp.gameState.currentPlayer, drawnCards))) {
							advanceTurn(isInitial, new TrainCardAction(mainApp.gameState.currentPlayer, drawnCards));
						} else {
							TextButton errorMessageTemp = new TextButton(mainApp.gameState.getError(),
									TTRAdvisorApp.skin, "error");
							errorMessage.setText(mainApp.gameState.getError());
							errorMessage.setWidth(errorMessageTemp.getWidth());
							errorMessage.setPosition(Gdx.graphics.getWidth() / 2 - errorMessage.getWidth() / 2,
									Gdx.graphics.getHeight() / 8);
							errorMessage.setVisible(true);
							if (errorMessage.getText().length() != 0) {
								guiStage.addActor(errorMessage);
							} else {
								errorMessage.setVisible(false);
							}
						}

						// trainCardHand.setText(mainApp.gameState.currentPlayer.getTCS().toString());
						helperReenableUIForActionInput();
						// trainCardHand.setVisible(true);
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

							int drawCount = 0;
							int index = 0;
							if (drawnCards.size() - 1 >= 0) {
								index = drawnCards.size() - 1;
								System.out.println("index" + index);
								switch (drawnCards.get(index).getColor()) {
								case ANY:
									for (TrainCard tc : drawnCards) {
										if (tc.getColor().equals(Colors.route.ANY)) {
											drawCount++;
										}
									}
									drawCount--;
									if (drawCount == 0) {
										wildTrain.setText("");
									} else {
										wildTrain.setText(Integer.toString(drawCount));
									}
									break;
								case BLACK:
									for (TrainCard tc : drawnCards) {
										if (tc.getColor().equals(Colors.route.BLACK)) {
											drawCount++;
										}
									}
									drawCount--;
									if (drawCount == 0) {
										blackTrain.setText("");
									} else {
										blackTrain.setText(Integer.toString(drawCount));
									}

									break;
								case BLUE:
									for (TrainCard tc : drawnCards) {
										if (tc.getColor().equals(Colors.route.BLUE)) {
											drawCount++;
										}
									}
									drawCount--;
									if (drawCount == 0) {
										blueTrain.setText("");
									} else {
										blueTrain.setText(Integer.toString(drawCount));
									}

									break;
								case GREEN:
									for (TrainCard tc : drawnCards) {
										if (tc.getColor().equals(Colors.route.GREEN)) {
											drawCount++;
										}
									}
									drawCount--;
									if (drawCount == 0) {
										greenTrain.setText("");
									} else {
										greenTrain.setText(Integer.toString(drawCount));
									}

									break;
								case ORANGE:
									for (TrainCard tc : drawnCards) {
										if (tc.getColor().equals(Colors.route.ORANGE)) {
											drawCount++;
										}
									}
									drawCount--;
									if (drawCount == 0) {
										orangeTrain.setText("");
									} else {
										orangeTrain.setText(Integer.toString(drawCount));
									}

									break;
								case PINK:
									for (TrainCard tc : drawnCards) {
										if (tc.getColor().equals(Colors.route.PINK)) {
											drawCount++;
										}
									}
									drawCount--;
									if (drawCount == 0) {
										pinkTrain.setText("");
									} else {
										pinkTrain.setText(Integer.toString(drawCount));
									}

									break;
								case RED:
									for (TrainCard tc : drawnCards) {
										if (tc.getColor().equals(Colors.route.RED)) {
											drawCount++;
										}
									}
									drawCount--;
									if (drawCount == 0) {
										redTrain.setText("");
									} else {
										redTrain.setText(Integer.toString(drawCount));
									}
									break;
								case WHITE:
									for (TrainCard tc : drawnCards) {
										if (tc.getColor().equals(Colors.route.WHITE)) {
											drawCount++;
										}
									}
									drawCount--;
									if (drawCount == 0) {
										whiteTrain.setText("");
									} else {
										whiteTrain.setText(Integer.toString(drawCount));
									}

									break;
								case YELLOW:
									for (TrainCard tc : drawnCards) {
										if (tc.getColor().equals(Colors.route.YELLOW)) {
											drawCount++;
										}
									}
									drawCount--;
									if (drawCount == 0) {
										yellowTrain.setText("");
									} else {
										yellowTrain.setText(Integer.toString(drawCount));
									}

									break;
								default:
									break;
								}

							}
							drawnCards.remove(drawnCards.size() - 1);
							drawnCardList.setText(drawnCards.toString());

							if (mainApp.turnInput.isInitialTurn()) {
								if (drawnCards.size() < 4) {
									redTrain.setTouchable(Touchable.enabled);
									redTrain.setDisabled(false);
									orangeTrain.setTouchable(Touchable.enabled);
									orangeTrain.setDisabled(false);
									yellowTrain.setTouchable(Touchable.enabled);
									yellowTrain.setDisabled(false);
									greenTrain.setTouchable(Touchable.enabled);
									greenTrain.setDisabled(false);
									blueTrain.setTouchable(Touchable.enabled);
									blueTrain.setDisabled(false);
									pinkTrain.setTouchable(Touchable.enabled);
									pinkTrain.setDisabled(false);
									blackTrain.setTouchable(Touchable.enabled);
									blackTrain.setDisabled(false);
									whiteTrain.setTouchable(Touchable.enabled);
									whiteTrain.setDisabled(false);
									wildTrain.setTouchable(Touchable.enabled);
									wildTrain.setDisabled(false);

								}
							} else {
								if (drawnCards.size() < 2) {
									redTrain.setTouchable(Touchable.enabled);
									redTrain.setDisabled(false);
									orangeTrain.setTouchable(Touchable.enabled);
									orangeTrain.setDisabled(false);
									yellowTrain.setTouchable(Touchable.enabled);
									yellowTrain.setDisabled(false);
									greenTrain.setTouchable(Touchable.enabled);
									greenTrain.setDisabled(false);
									blueTrain.setTouchable(Touchable.enabled);
									blueTrain.setDisabled(false);
									pinkTrain.setTouchable(Touchable.enabled);
									pinkTrain.setDisabled(false);
									blackTrain.setTouchable(Touchable.enabled);
									blackTrain.setDisabled(false);
									whiteTrain.setTouchable(Touchable.enabled);
									whiteTrain.setDisabled(false);
									wildTrain.setTouchable(Touchable.enabled);
									wildTrain.setDisabled(false);
								}
							}

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
						helperReenableUIForActionInput();
						// trainCardHand.setVisible(true);
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
				drawnCardList.setPosition(Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 2 + 20);
				guiStage.addActor(table);
				guiStage.addActor(drawnCardList);
			}

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
		});

		guiStage.addActor(TCButton);
		// guiStage.addActor(trainCardHand);
		if (errorMessage.getText().length() != 0) {
			guiStage.addActor(errorMessage);
		}
	}

	private void setupClaimRouteButton() {
		claimRouteButton = new TextButton("Claim A Route", TTRAdvisorApp.skin, "small");
		claimRouteButton.setPosition(prevTurn.getX() + prevTurn.getWidth()*2, Gdx.graphics.getHeight() - claimRouteButton.getHeight());
		
		claimRouteTooltip = new Label("Click on two cities to select a route between them!", TTRAdvisorApp.skin, "handDisplaySmall");
		claimRouteTooltip.setPosition(prevTurn.getX() + prevTurn.getWidth()*2, Gdx.graphics.getHeight() - claimRouteButton.getHeight() - claimRouteTooltip.getHeight() - 10);
		claimRouteTooltip.setVisible(false);
		
		demoSelectedCity = new Label(DEFAULT_CITY_LABEL, TTRAdvisorApp.skin, "handDisplaySmall");
		demoSelectedCity.setPosition(claimRouteTooltip.getX(), Gdx.graphics.getHeight() - claimRouteButton.getHeight() - claimRouteTooltip.getHeight() - demoSelectedCity.getHeight() - 10);
		demoSelectedCity.setVisible(false);
		guiStage.addActor(demoSelectedCity);

		demoSelectedRoute = new Label(DEFAULT_ROUTE_LABEL, TTRAdvisorApp.skin, "handDisplaySmall");
		demoSelectedRoute.setPosition(claimRouteTooltip.getX(), Gdx.graphics.getHeight() - claimRouteButton.getHeight() - claimRouteTooltip.getHeight() - demoSelectedCity.getHeight() - demoSelectedRoute.getHeight() - 10);
		demoSelectedRoute.setVisible(false);
		guiStage.addActor(demoSelectedRoute);


		claimRouteButton.addListener(new InputListener() {

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				demoSelectedCity.setVisible(true);
				demoSelectedRoute.setVisible(true);
				mapTappingDisabled = false;
				claimRouteTooltip.setVisible(true);
				helperDisableUIForActionInput();

				cancelClaimRoute = new TextButton("Cancel", TTRAdvisorApp.skin, "small");
				cancelClaimRoute.setPosition(prevTurn.getX() + prevTurn.getWidth() * 2,
						Gdx.graphics.getHeight() - cancelClaimRoute.getHeight());

				cancelClaimRoute.addListener(new InputListener() {
					public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
						mapTappingDisabled = true;
						claimRouteTooltip.setVisible(false);
						cancelClaimRoute.setVisible(false);
						helperReenableUIForActionInput();
					}

					public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
						return true;
					}
				});

				guiStage.addActor(cancelClaimRoute);
			}

			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
		});
		guiStage.addActor(claimRouteTooltip);
		guiStage.addActor(claimRouteButton);
	}

	private void setupHandDisplayButton() {
		
		int flag;
		
		organizer = new Table(TTRAdvisorApp.skin);
		
		handDisplayTicketList = new List<DestinationTicket>(TTRAdvisorApp.skin);
		handDisplayTicketList.setAlignment(Align.left);

		handDisplay = new Table(TTRAdvisorApp.skin);
		handDisplayLabels = new Label[Colors.route.values().length];
		for (int i = 0; i < handDisplayLabels.length; i++) {
			handDisplayLabels[i] = new Label("0", TTRAdvisorApp.skin, "handDisplaySmall");
		}
		refreshHandDisplay();

		String[] hardcodedStuff = { "tcAny", "tcBlack", "tcBlue", "tcGreen", "tcOrange", "tcPink", "tcRed", "tcWhite",
				"tcYellow" };

		for (int i = 0; i < Colors.route.values().length; i++) {
			handDisplay.add(handDisplayLabels[i]).padRight(15f).height(35f);
			ImageTextButton train = new ImageTextButton("", TTRAdvisorApp.skin, hardcodedStuff[i]);
			// different sizes on different resolutions
			// reminder - 200 by 125 is the card graphics aspect ratio
			handDisplay.add(train).padTop(3f).width(Gdx.graphics.getWidth() * 0.07f).height(Gdx.graphics.getWidth() * 0.07f * (125f/200f));
			handDisplay.row();
		}
		handDisplay.right();
		
		organizer.add(handDisplayTicketList).padTop(50f).padRight(15f).top(); organizer.add(handDisplay).padTop(50f);
		organizer.right();
		organizer.setFillParent(true);
		organizer.setVisible(false);
		
//		handDisplayPane = new ScrollPane(handDisplayTicketList, TTRAdvisorApp.skin);
//		handDisplayPane.setX(Gdx.graphics.getWidth() * 2 / 5);
//		handDisplayPane.setY(Gdx.graphics.getHeight() * 1 / 100);
//		handDisplayPane.setHeight(Gdx.graphics.getHeight() * 1 / 2);
//		handDisplayPane.setWidth(Gdx.graphics.getWidth() * 1 / 3);
//
//		guiStage.addActor(handDisplayPane);

		handDisplayButton = new TextButton("Show My Hand", TTRAdvisorApp.skin, "small");
		handDisplayButton.setPosition(Gdx.graphics.getWidth() - handDisplayButton.getWidth(),
				Gdx.graphics.getHeight() - nextTurn.getHeight() - 5 - handDisplayButton.getHeight());
		handDisplayButton.addListener(new InputListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				if (organizer.isVisible()) {
					organizer.setVisible(false);
					handDisplayButton.setText("Show My Hand");
				} else {
					refreshHandDisplay();
					organizer.setVisible(true);
					handDisplayButton.setText("Hide My Hand");
				}
			}

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

		});

		guiStage.addActor(handDisplayButton);
		guiStage.addActor(organizer);

	}

	/**
	 * Helper - refresh the counts of each card in the displayed hand.
	 */
	private void refreshHandDisplay() {
		Player user = mainApp.gameState.getUserPlayer();
		for (int i = 0; i < Colors.route.values().length; i++) {
			handDisplayLabels[i].setText("" + user.getNumberOfColor(Colors.route.values()[i]));
		}
		
		ArrayList<DestinationTicket> userTickets = mainApp.gameState.getUserPlayer().getDTS();
		
		// simple array to give to ScrollPane
		DestinationTicket[] tempArray = new DestinationTicket[userTickets.size()];

		for (int i = 0; i < userTickets.size(); i++) {
			tempArray[i] = userTickets.get(i);
		}

		handDisplayTicketList.setItems(tempArray);
		handDisplayTicketList.invalidate();
		
		if (tempArray.length == 0) {
			handDisplayTicketList.setVisible(false);
		} else {
			handDisplayTicketList.setVisible(true);
		}
	}

	private void setupTurnView() {
		prevTurn = new TextButton("View \n Previous Turn", TTRAdvisorApp.skin, "small");
		prevTurn.setPosition(0, Gdx.graphics.getHeight() - prevTurn.getHeight());
		nextTurn = new TextButton("View \n Next Turn", TTRAdvisorApp.skin, "small");
		nextTurn.setPosition(Gdx.graphics.getWidth() - nextTurn.getWidth(),
				Gdx.graphics.getHeight() - nextTurn.getHeight());
		quit = new TextButton("Quit Game", TTRAdvisorApp.skin, "small");

		quit.setPosition(nextTurn.getX() - nextTurn.getWidth() * 2, Gdx.graphics.getHeight() - quit.getHeight());
		turnNumber = new Label(Integer.toString(mainApp.hist.getTurnIndex()), TTRAdvisorApp.skin, "big-black");
		turnNumber.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() - turnNumber.getHeight());
		prevTurn.addListener(new InputListener() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				if (mainApp.hist.previousTurn()) {
					mainApp.gameState.currentPlayer = mainApp.hist.getGameState().getPlayers()
							.get(mainApp.hist.getTurnIndexView());
					mainApp.gameState.setBoard(mainApp.hist.getGameState().getBoard());
					// trainCardHand.setText(mainApp.hist.getGameState().getPlayers().get(mainApp.hist.getTurnIndexView())
					// .getTCS().toString());
					turnNumber.setText(Integer.toString(mainApp.hist.getTurnIndex()));
//					demoCurrPlayer.setText("Current player: " + mainApp.hist.getGameState().getPlayers().get(mainApp.hist.getTurnIndexView()).getColor());
					if (mainApp.hist.getTurnIndex() < mainApp.gameState.getCurrentTurnCounter() - 1) {
						helperDisableUIForHistoryLook();
					}
					calcAllScore();
					playerIconUpdate(mainApp.gameState.currentPlayer);
					Player user = mainApp.hist.getGameState().getUserPlayer();
					for (int i = 0; i < Colors.route.values().length; i++) {
						handDisplayLabels[i].setText("" + user.getNumberOfColor(Colors.route.values()[i]));
					}
				}
			}

			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
		});
		nextTurn.addListener(new InputListener() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				if (mainApp.hist.nextTurn()) {
					mainApp.gameState.currentPlayer = mainApp.hist.getGameState().getPlayers()
							.get(mainApp.hist.getTurnIndexView());
					mainApp.gameState.setBoard(mainApp.hist.getGameState().getBoard());
					// trainCardHand.setText(mainApp.hist.getGameState().getPlayers().get(mainApp.hist.getTurnIndexView())
					// .getTCS().toString());
					turnNumber.setText(Integer.toString(mainApp.hist.getTurnIndex()));
//					demoCurrPlayer.setText("Current player: " + mainApp.hist.getGameState().getPlayers().get(mainApp.hist.getTurnIndexView()).getColor());
					calcAllScore();
					playerIconUpdate(mainApp.gameState.currentPlayer);
					Player user = mainApp.hist.getGameState().getUserPlayer();
					for (int i = 0; i < Colors.route.values().length; i++) {
						handDisplayLabels[i].setText("" + user.getNumberOfColor(Colors.route.values()[i]));
					}
					if (mainApp.hist.getTurnIndex() >= mainApp.gameState.getCurrentTurnCounter() - 1) {
						helperReenableUIForHistoryLook();
					}
				}
			}

			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
		});
		quit.addListener(new InputListener() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

				helperDisableUIForActionInput();
				// trainCardHand.setVisible(false);
				recommendationsButton.setVisible(false);

				final TextButton confirmYes = new TextButton("Yes, quit", TTRAdvisorApp.skin, "small");
				final TextButton confirmNo = new TextButton("No, return to game", TTRAdvisorApp.skin, "small");

				confirmYes.setPosition(Gdx.graphics.getWidth() / 2 - confirmYes.getWidth(),
						Gdx.graphics.getHeight() / 2);
				confirmNo.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);

				confirmYes.addListener(new InputListener() {
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
				confirmNo.addListener(new InputListener() {
					public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
						helperReenableUIForActionInput();
						// trainCardHand.setVisible(true);
						confirmYes.setVisible(false);
						confirmNo.setVisible(false);
						recommendationsButton.setVisible(true);
					}

					public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
						return true;
					}
				});

				guiStage.addActor(confirmYes);
				guiStage.addActor(confirmNo);

//				mainApp.gameState = new GameState(null, new ArrayList<Player>(), new Board("cities.txt"),
//						new DestinationTicketList("destinations.txt"), new ArrayList<Turn>());
//				mainApp.turnInput = new InputTurnController(mainApp.gameState);
//				mainApp.hist = new HistoryController(mainApp.gameState);
//				mainApp.setScreen(new TitleScreen(mainApp));

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

				if (mapTappingDisabled) {
					// stop processing the tap if it is not allowed right now
					// Gdx.app.log("City Event", "Tapped on background.");
					// selectedCity = DEFAULT_CITY_LABEL;
					// selectedRoute = DEFAULT_ROUTE_LABEL;
					super.tap(event, x, y, count, button);
					return;
				}

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
								mapTappingDisabled = true;
								claimRouteTooltip.setVisible(false);
								helperReenableUIForActionInput();
								cancelClaimRoute.setVisible(false);
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

		mapTappingDisabled = true;
		claimRouteTooltip.setVisible(false);
		cancelClaimRoute.setVisible(false);

		if (routeOptions.get(0).getColor() == routeOptions.get(1).getColor()) {
			// identical colors, ignore
			setupHelperChooseCards(routeOptions.get(0));
			return;
		}

		final Table table = new Table();
		helperDisableUIForActionInput();
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
				selectedCity = DEFAULT_CITY_LABEL;
				selectedRoute = DEFAULT_ROUTE_LABEL;
				mapTappingDisabled = true;
				claimRouteTooltip.setVisible(false);
				cancelClaimRoute.setVisible(false);
				helperReenableUIForActionInput();
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

		mapTappingDisabled = true;
		claimRouteTooltip.setVisible(false);
		cancelClaimRoute.setVisible(false);

		if (mainApp.gameState.currentPlayer.getColor() != mainApp.userColor) {

			RouteAction routeAction = new RouteAction(mainApp.gameState.currentPlayer, new ArrayList<TrainCard>(),
					route);

			boolean isInitial = mainApp.turnInput.isInitialTurn();

			if (mainApp.hist.getTurnIndex() == mainApp.gameState.getCurrentTurnCounter() - 1) {
				if (mainApp.turnInput.makeCorrection(routeAction)) {
					if (mainApp.hist.getTurnIndex() == 0) {
						isInitial = true;
					}
					advanceTurn(isInitial, routeAction);
				} else {
					TextButton errorMessageTemp = new TextButton(mainApp.gameState.getError(), TTRAdvisorApp.skin,
							"error");
					errorMessage.setText(mainApp.gameState.getError());
					errorMessage.setWidth(errorMessageTemp.getWidth());
					errorMessage.setPosition(Gdx.graphics.getWidth() / 2 - errorMessage.getWidth() / 2,
							Gdx.graphics.getHeight() / 8);
					errorMessage.setVisible(true);
					if (errorMessage.getText().length() != 0) {
						guiStage.addActor(errorMessage);
					} else {
						errorMessage.setVisible(false);
					}
				}
			} else if (mainApp.turnInput.takeAction(routeAction)) {
				advanceTurn(isInitial, routeAction);
			} else {
				TextButton errorMessageTemp = new TextButton(mainApp.gameState.getError(), TTRAdvisorApp.skin, "error");
				errorMessage.setText(mainApp.gameState.getError());
				errorMessage.setWidth(errorMessageTemp.getWidth());
				errorMessage.setPosition(Gdx.graphics.getWidth() / 2 - errorMessage.getWidth() / 2,
						Gdx.graphics.getHeight() / 8);
				errorMessage.setVisible(true);
				if (errorMessage.getText().length() != 0) {
					guiStage.addActor(errorMessage);
				} else {
					errorMessage.setVisible(false);
				}
			}

			selectedCity = DEFAULT_CITY_LABEL;
			selectedRoute = DEFAULT_ROUTE_LABEL;

			// trainCardHand.setText(mainApp.gameState.currentPlayer.getTCS().toString());
			helperReenableUIForActionInput();
			// trainCardHand.setVisible(true);
			return;
		}

		final Table table = new Table();
		helperDisableUIForActionInput();
		// trainCardHand.setVisible(false);
		final ArrayList<TrainCard> drawnCards = new ArrayList<>();
		final Label drawnCardList = new Label(drawnCards.toString(), TTRAdvisorApp.skin);
		drawnCardList.setWidth((Gdx.graphics.getWidth() / 5) * 2);
		drawnCardList.setPosition(Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 2 + 20);

		int anyCount = 0;
		int blackCount = 0;
		int blueCount = 0;
		int greenCount = 0;
		int orangeCount = 0;
		int pinkCount = 0;
		int redCount = 0;
		int whiteCount = 0;
		int yellowCount = 0;

		int userIndex = mainApp.gameState.getPlayers().indexOf(mainApp.gameState.getUserPlayer());

		if (mainApp.hist.getTurnIndex() >= 1) {
			for (TrainCard tc : mainApp.gameState.getTurns().get(mainApp.hist.getTurnIndex() - 1).getPlayerSnapshots()
					.get(userIndex).getTCS()) {
				switch (tc.getColor()) {
				case ANY:
					anyCount++;
					break;
				case BLACK:
					blackCount++;
					break;
				case BLUE:
					blueCount++;
					break;
				case GREEN:
					greenCount++;
					break;
				case ORANGE:
					orangeCount++;
					break;
				case PINK:
					pinkCount++;
					break;
				case RED:
					redCount++;
					break;
				case WHITE:
					whiteCount++;
					break;
				case YELLOW:
					yellowCount++;
					break;
				default:
					break;
				}
			}
		}
		final int anyCountF = anyCount;
		final int blackCountF = blackCount;
		final int blueCountF = blueCount;
		final int greenCountF = greenCount;
		final int orangeCountF = orangeCount;
		final int pinkCountF = pinkCount;
		final int redCountF = redCount;
		final int whiteCountF = whiteCount;
		final int yellowCountF = yellowCount;

		final ImageTextButton redTrain = new ImageTextButton("", TTRAdvisorApp.skin, "tcRed");
		if (redCountF == 0) {
			redTrain.setText("");
			redTrain.setTouchable(Touchable.disabled);
			redTrain.setDisabled(true);
		} else {
			redTrain.setText(Integer.toString(redCount));
		}
		redTrain.addListener(new InputListener() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				drawnCards.add(new TrainCard(Colors.route.RED));
				drawnCardList.setText(drawnCards.toString());
				int handCount = 0;
				for (TrainCard tc : drawnCards) {
					if (tc.getColor().equals(Colors.route.RED)) {
						handCount++;
					}
				}
				handCount = redCountF - handCount;
				if (handCount <= 0) {
					redTrain.setText("");
					redTrain.setTouchable(Touchable.disabled);
					redTrain.setDisabled(true);
				} else {
					redTrain.setText(Integer.toString(handCount));
				}
			}

			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
		});
//		ImageButton orangeTrain = new ImageButton(trainCardImages[Colors.route.ORANGE.ordinal()].getDrawable());
		final ImageTextButton orangeTrain = new ImageTextButton("", TTRAdvisorApp.skin, "tcOrange");
		if (orangeCountF == 0) {
			orangeTrain.setText("");
			orangeTrain.setTouchable(Touchable.disabled);
			orangeTrain.setDisabled(true);
		} else {
			orangeTrain.setText(Integer.toString(orangeCount));
		}
		orangeTrain.addListener(new InputListener() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				drawnCards.add(new TrainCard(Colors.route.ORANGE));
				drawnCardList.setText(drawnCards.toString());
				int handCount = 0;
				for (TrainCard tc : drawnCards) {
					if (tc.getColor().equals(Colors.route.ORANGE)) {
						handCount++;
					}
				}
				handCount = orangeCountF - handCount;
				if (handCount <= 0) {
					orangeTrain.setText("");
					orangeTrain.setTouchable(Touchable.disabled);
					orangeTrain.setDisabled(true);
				} else {
					orangeTrain.setText(Integer.toString(handCount));
				}
			}

			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
		});
//		ImageButton yellowTrain = new ImageButton(trainCardImages[Colors.route.YELLOW.ordinal()].getDrawable());
		final ImageTextButton yellowTrain = new ImageTextButton("", TTRAdvisorApp.skin, "tcYellow");
		if (yellowCountF == 0) {
			yellowTrain.setText("");
			yellowTrain.setTouchable(Touchable.disabled);
			yellowTrain.setDisabled(true);
		} else {
			yellowTrain.setText(Integer.toString(yellowCount));
		}
		yellowTrain.addListener(new InputListener() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				drawnCards.add(new TrainCard(Colors.route.YELLOW));
				drawnCardList.setText(drawnCards.toString());
				int handCount = 0;
				for (TrainCard tc : drawnCards) {
					if (tc.getColor().equals(Colors.route.YELLOW)) {
						handCount++;
					}
				}
				handCount = yellowCountF - handCount;
				if (handCount <= 0) {
					yellowTrain.setText("");
					yellowTrain.setTouchable(Touchable.disabled);
					yellowTrain.setDisabled(true);
				} else {
					yellowTrain.setText(Integer.toString(handCount));
				}
			}

			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
		});
//		ImageButton greenTrain = new ImageButton(trainCardImages[Colors.route.GREEN.ordinal()].getDrawable());
		final ImageTextButton greenTrain = new ImageTextButton("", TTRAdvisorApp.skin, "tcGreen");
		if (greenCountF == 0) {
			greenTrain.setText("");
			greenTrain.setTouchable(Touchable.disabled);
			greenTrain.setDisabled(true);
		} else {
			greenTrain.setText(Integer.toString(greenCount));
		}
		greenTrain.addListener(new InputListener() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				drawnCards.add(new TrainCard(Colors.route.GREEN));
				drawnCardList.setText(drawnCards.toString());
				int handCount = 0;
				for (TrainCard tc : drawnCards) {
					if (tc.getColor().equals(Colors.route.GREEN)) {
						handCount++;
					}
				}
				handCount = greenCountF - handCount;
				if (handCount <= 0) {
					greenTrain.setText("");
					greenTrain.setTouchable(Touchable.disabled);
					greenTrain.setDisabled(true);
				} else {
					greenTrain.setText(Integer.toString(handCount));
				}
			}

			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
		});
//		ImageButton blueTrain = new ImageButton(trainCardImages[Colors.route.BLUE.ordinal()].getDrawable());
		final ImageTextButton blueTrain = new ImageTextButton("", TTRAdvisorApp.skin, "tcBlue");
		if (blueCountF == 0) {
			blueTrain.setText("");
			blueTrain.setTouchable(Touchable.disabled);
			blueTrain.setDisabled(true);
		} else {
			blueTrain.setText(Integer.toString(blueCount));
		}
		blueTrain.addListener(new InputListener() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				drawnCards.add(new TrainCard(Colors.route.BLUE));
				drawnCardList.setText(drawnCards.toString());
				int handCount = 0;
				for (TrainCard tc : drawnCards) {
					if (tc.getColor().equals(Colors.route.BLUE)) {
						handCount++;
					}
				}
				handCount = blueCountF - handCount;
				if (handCount <= 0) {
					blueTrain.setText("");
					blueTrain.setTouchable(Touchable.disabled);
					blueTrain.setDisabled(true);
				} else {
					blueTrain.setText(Integer.toString(handCount));
				}
			}

			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
		});
//		ImageButton pinkTrain = new ImageButton(trainCardImages[Colors.route.PINK.ordinal()].getDrawable());
		final ImageTextButton pinkTrain = new ImageTextButton("", TTRAdvisorApp.skin, "tcPink");
		if (pinkCountF == 0) {
			pinkTrain.setText("");
			pinkTrain.setTouchable(Touchable.disabled);
			pinkTrain.setDisabled(true);
		} else {
			pinkTrain.setText(Integer.toString(pinkCount));
		}
		pinkTrain.addListener(new InputListener() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				drawnCards.add(new TrainCard(Colors.route.PINK));
				drawnCardList.setText(drawnCards.toString());
				int handCount = 0;
				for (TrainCard tc : drawnCards) {
					if (tc.getColor().equals(Colors.route.PINK)) {
						handCount++;
					}
				}
				handCount = pinkCountF - handCount;
				if (handCount <= 0) {
					pinkTrain.setText("");
					pinkTrain.setTouchable(Touchable.disabled);
					pinkTrain.setDisabled(true);
				} else {
					pinkTrain.setText(Integer.toString(handCount));
				}
			}

			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
		});
//		ImageButton blackTrain = new ImageButton(trainCardImages[Colors.route.BLACK.ordinal()].getDrawable());
		final ImageTextButton blackTrain = new ImageTextButton("", TTRAdvisorApp.skin, "tcBlack");
		if (blackCountF == 0) {
			blackTrain.setText("");
			blackTrain.setTouchable(Touchable.disabled);
			blackTrain.setDisabled(true);
		} else {
			blackTrain.setText(Integer.toString(blackCount));
		}
		blackTrain.addListener(new InputListener() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				drawnCards.add(new TrainCard(Colors.route.BLACK));
				drawnCardList.setText(drawnCards.toString());
				int handCount = 0;
				for (TrainCard tc : drawnCards) {
					if (tc.getColor().equals(Colors.route.BLACK)) {
						handCount++;
					}
				}
				handCount = blackCountF - handCount;
				if (handCount <= 0) {
					blackTrain.setText("");
					blackTrain.setTouchable(Touchable.disabled);
					blackTrain.setDisabled(true);
				} else {
					blackTrain.setText(Integer.toString(handCount));
				}
			}

			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
		});
//		ImageButton whiteTrain = new ImageButton(trainCardImages[Colors.route.WHITE.ordinal()].getDrawable());
		final ImageTextButton whiteTrain = new ImageTextButton("", TTRAdvisorApp.skin, "tcWhite");
		if (whiteCountF == 0) {
			whiteTrain.setText("");
			whiteTrain.setTouchable(Touchable.disabled);
			whiteTrain.setDisabled(true);
		} else {
			whiteTrain.setText(Integer.toString(whiteCount));
		}
		whiteTrain.addListener(new InputListener() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				drawnCards.add(new TrainCard(Colors.route.WHITE));
				drawnCardList.setText(drawnCards.toString());
				int handCount = 0;
				for (TrainCard tc : drawnCards) {
					if (tc.getColor().equals(Colors.route.WHITE)) {
						handCount++;
					}
				}
				handCount = whiteCountF - handCount;
				if (handCount <= 0) {
					whiteTrain.setText("");
					whiteTrain.setTouchable(Touchable.disabled);
					whiteTrain.setDisabled(true);
				} else {
					whiteTrain.setText(Integer.toString(handCount));
				}
			}

			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
		});
//		ImageButton wildTrain = new ImageButton(trainCardImages[Colors.route.ANY.ordinal()].getDrawable());
		final ImageTextButton wildTrain = new ImageTextButton("", TTRAdvisorApp.skin, "tcAny");
		if (anyCountF == 0) {
			wildTrain.setText("");
			wildTrain.setTouchable(Touchable.disabled);
			wildTrain.setDisabled(true);
		} else {
			wildTrain.setText(Integer.toString(anyCount));
		}
		wildTrain.addListener(new InputListener() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				drawnCards.add(new TrainCard(Colors.route.ANY));
				drawnCardList.setText(drawnCards.toString());
				int handCount = 0;
				for (TrainCard tc : drawnCards) {
					if (tc.getColor().equals(Colors.route.ANY)) {
						handCount++;
					}
				}
				handCount = anyCountF - handCount;
				if (handCount <= 0) {
					wildTrain.setText("");
					wildTrain.setTouchable(Touchable.disabled);
					wildTrain.setDisabled(true);
				} else {
					wildTrain.setText(Integer.toString(handCount));
				}
			}

			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
		});
		TextButton done = new TextButton("Done", TTRAdvisorApp.skin, "small");
		done.addListener(new InputListener() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

				// send the selected cards
				RouteAction routeAction = new RouteAction(mainApp.gameState.currentPlayer, drawnCards, route);

				boolean isInitial = mainApp.turnInput.isInitialTurn();

				if (mainApp.hist.getTurnIndex() == mainApp.gameState.getCurrentTurnCounter() - 1) {
					if (mainApp.turnInput.makeCorrection(routeAction)) {
						if (mainApp.hist.getTurnIndex() == 0) {
							isInitial = true;
						}
						advanceTurn(isInitial, routeAction);
					} else {
						TextButton errorMessageTemp = new TextButton(mainApp.gameState.getError(), TTRAdvisorApp.skin,
								"error");
						errorMessage.setText(mainApp.gameState.getError());
						errorMessage.setWidth(errorMessageTemp.getWidth());
						errorMessage.setPosition(Gdx.graphics.getWidth() / 2 - errorMessage.getWidth() / 2,
								Gdx.graphics.getHeight() / 8);
						errorMessage.setVisible(true);
						if (errorMessage.getText().length() != 0) {
							guiStage.addActor(errorMessage);
						} else {
							errorMessage.setVisible(false);
						}
					}
				} else if (mainApp.turnInput.takeAction(routeAction)) {
					advanceTurn(isInitial, routeAction);
				} else {
					TextButton errorMessageTemp = new TextButton(mainApp.gameState.getError(), TTRAdvisorApp.skin,
							"error");
					errorMessage.setText(mainApp.gameState.getError());
					errorMessage.setWidth(errorMessageTemp.getWidth());
					errorMessage.setPosition(Gdx.graphics.getWidth() / 2 - errorMessage.getWidth() / 2,
							Gdx.graphics.getHeight() / 8);
					errorMessage.setVisible(true);
					if (errorMessage.getText().length() != 0) {
						guiStage.addActor(errorMessage);
					} else {
						errorMessage.setVisible(false);
					}
				}

				selectedCity = DEFAULT_CITY_LABEL;
				selectedRoute = DEFAULT_ROUTE_LABEL;

				// trainCardHand.setText(mainApp.gameState.currentPlayer.getTCS().toString());
				helperReenableUIForActionInput();
				// trainCardHand.setVisible(true);
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

					int drawCount = 0;
					int index = 0;
					if (drawnCards.size() - 1 >= 0) {
						index = drawnCards.size() - 1;
						switch (drawnCards.get(index).getColor()) {
						case ANY:
							for (TrainCard tc : drawnCards) {
								if (tc.getColor().equals(Colors.route.ANY)) {
									drawCount++;
								}
							}
							drawCount--;
							drawCount = anyCountF - drawCount;
							if (drawCount <= 0) {
								wildTrain.setText("");
								wildTrain.setTouchable(Touchable.disabled);
							} else {
								wildTrain.setText(Integer.toString(drawCount));
								wildTrain.setTouchable(Touchable.enabled);
								wildTrain.setDisabled(false);
							}
							break;
						case BLACK:
							for (TrainCard tc : drawnCards) {
								if (tc.getColor().equals(Colors.route.BLACK)) {
									drawCount++;
								}
							}
							drawCount--;
							drawCount = blackCountF - drawCount;
							if (drawCount <= 0) {
								blackTrain.setText("");
								blackTrain.setTouchable(Touchable.disabled);
							} else {
								blackTrain.setText(Integer.toString(drawCount));
								blackTrain.setTouchable(Touchable.enabled);
								blackTrain.setDisabled(false);
							}
							break;
						case BLUE:
							for (TrainCard tc : drawnCards) {
								if (tc.getColor().equals(Colors.route.BLUE)) {
									drawCount++;
								}
							}
							drawCount--;
							drawCount = blueCountF - drawCount;
							if (drawCount <= 0) {
								blueTrain.setText("");
								blueTrain.setTouchable(Touchable.disabled);
							} else {
								blueTrain.setText(Integer.toString(drawCount));
								blueTrain.setTouchable(Touchable.enabled);
								blueTrain.setDisabled(false);
							}
							break;
						case GREEN:
							for (TrainCard tc : drawnCards) {
								if (tc.getColor().equals(Colors.route.GREEN)) {
									drawCount++;
								}
							}
							drawCount--;
							drawCount = greenCountF - drawCount;
							if (drawCount <= 0) {
								greenTrain.setText("");
								greenTrain.setTouchable(Touchable.disabled);
							} else {
								greenTrain.setText(Integer.toString(drawCount));
								greenTrain.setTouchable(Touchable.enabled);
								greenTrain.setDisabled(false);
							}
							break;
						case ORANGE:
							for (TrainCard tc : drawnCards) {
								if (tc.getColor().equals(Colors.route.ORANGE)) {
									drawCount++;
								}
							}
							drawCount--;
							drawCount = orangeCountF - drawCount;
							if (drawCount <= 0) {
								orangeTrain.setText("");
								orangeTrain.setTouchable(Touchable.disabled);
							} else {
								orangeTrain.setText(Integer.toString(drawCount));
								orangeTrain.setTouchable(Touchable.enabled);
								orangeTrain.setDisabled(false);
							}
							break;
						case PINK:
							for (TrainCard tc : drawnCards) {
								if (tc.getColor().equals(Colors.route.PINK)) {
									drawCount++;
								}
							}
							drawCount--;
							drawCount = pinkCountF - drawCount;
							if (drawCount <= 0) {
								pinkTrain.setText("");
								pinkTrain.setTouchable(Touchable.disabled);
							} else {
								pinkTrain.setText(Integer.toString(drawCount));
								pinkTrain.setTouchable(Touchable.enabled);
								pinkTrain.setDisabled(false);
							}
							break;
						case RED:
							for (TrainCard tc : drawnCards) {
								if (tc.getColor().equals(Colors.route.RED)) {
									drawCount++;
								}
							}
							drawCount--;
							drawCount = redCountF - drawCount;
							if (drawCount <= 0) {
								redTrain.setText("");
								redTrain.setTouchable(Touchable.disabled);
							} else {
								redTrain.setText(Integer.toString(drawCount));
								redTrain.setTouchable(Touchable.enabled);
								redTrain.setDisabled(false);
							}
							break;
						case WHITE:
							for (TrainCard tc : drawnCards) {
								if (tc.getColor().equals(Colors.route.WHITE)) {
									drawCount++;
								}
							}
							drawCount--;
							drawCount = whiteCountF - drawCount;
							if (drawCount <= 0) {
								whiteTrain.setText("");
								whiteTrain.setTouchable(Touchable.disabled);
							} else {
								whiteTrain.setText(Integer.toString(drawCount));
								whiteTrain.setTouchable(Touchable.enabled);
								whiteTrain.setDisabled(false);
							}
							break;
						case YELLOW:
							for (TrainCard tc : drawnCards) {
								if (tc.getColor().equals(Colors.route.YELLOW)) {
									drawCount++;
								}
							}
							drawCount--;
							drawCount = yellowCountF - drawCount;
							if (drawCount <= 0) {
								yellowTrain.setText("");
								yellowTrain.setTouchable(Touchable.disabled);
							} else {
								yellowTrain.setText(Integer.toString(drawCount));
								yellowTrain.setTouchable(Touchable.enabled);
								yellowTrain.setDisabled(false);
							}
							break;
						default:
							break;
						}

					}
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

				selectedCity = DEFAULT_CITY_LABEL;
				selectedRoute = DEFAULT_ROUTE_LABEL;

				drawnCards.removeAll(drawnCards);
				helperReenableUIForActionInput();
				// trainCardHand.setVisible(true);
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
		if (errorMessage.getText().length() != 0) {
			guiStage.addActor(errorMessage);
		}
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

		destTickets = new List<DestinationTicket>(TTRAdvisorApp.skin);
		ticketArray = new DestinationTicket[dtList.getList().size()];
		for (int i = 0; i < dtList.getList().size(); i++) {
			ticketArray[i] = dtList.getTicket(i);
		}
		destTickets.setItems(ticketArray);
		ticketSelection = destTickets.getSelection();
		ticketSelection.setMultiple(true);
		destTickets.setSelection(ticketSelection);
//		demoCurrPlayer.setText("Current player: " + mainApp.gameState.currentPlayer.getColor());

		calcAllScore();
		playerIconUpdate(mainApp.gameState.currentPlayer);
		refreshHandDisplay();
		errorMessage.setVisible(false);
	}

	/**
	 * Disable just the Destination Ticket, Train Card, and Route selection features
	 * (while in past turns)
	 */
	private void helperDisableUIForHistoryLook() {
		destButton.setVisible(false);
		TCButton.setVisible(false);
		claimRouteButton.setVisible(false);
		errorMessage.setVisible(false);
	}

	/**
	 * Disable all the Action input features AND the history buttons (while
	 * inputting an action using either the train card menu or DT menu)
	 */
	private void helperDisableUIForActionInput() {
		destButton.setVisible(false);
		TCButton.setVisible(false);
		claimRouteButton.setVisible(false);
		prevTurn.setVisible(false);
		nextTurn.setVisible(false);
		quit.setVisible(false);
		errorMessage.setVisible(false);
	}

	private void helperReenableUIForHistoryLook() {
		destButton.setVisible(true);
		TCButton.setVisible(true);
		claimRouteButton.setVisible(true);
		errorMessage.setVisible(false);
	}

	private void helperReenableUIForActionInput() {
		demoSelectedCity.setVisible(false);
		demoSelectedRoute.setVisible(false);
		destButton.setVisible(true);
		TCButton.setVisible(true);
		claimRouteButton.setVisible(true);
		prevTurn.setVisible(true);
		nextTurn.setVisible(true);
		quit.setVisible(true);
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

	private void calcAllScore() {
		int longestRoute = 0;
		int longestBounus = 10;
		Recommender rec = new Recommender(mainApp.gameState.getBoard(), mainApp.gameState.currentPlayer,
				mainApp.gameState.getCurrentTurnCounter(), mainApp.gameState.getPlayers().size());

		if (mainApp.gameState.getLastTurn()) {
			// find longest route
			for (Player p : mainApp.gameState.getPlayers()) {
				if (longestRoute < rec.longestRoute(p.getColor())) {
					longestRoute = rec.longestRoute(p.getColor());
				}
			}
		}

		// add score
		for (Player p : mainApp.gameState.getPlayers()) {
			int score = 0;

			// claimed routes
			for (Route r : mainApp.gameState.getBoard().getAllRoutesOfPlayer(p.getColor())) {
				switch (r.getCost()) {
				case 1:
					score = score + 1;
					break;
				case 2:
					score = score + 2;
					break;
				case 3:
					score = score + 4;
					break;
				case 4:
					score = score + 7;
					break;
				case 5:
					score = score + 10;
					;
					break;
				case 6:
					score = score + 15;
					break;
				default:
					Gdx.app.error("GameScreen", "Invalid Route Length");
					break;
				}
			}
			score = score / 2;

			if (mainApp.gameState.getLastTurn()) {
				// completed destination cards
				for (DestinationTicket ticket : p.getDTS()) {
					if (ticket.getCompleted()) {
						score = score + ticket.getValue();
					}
				}

				// longest route bonus
				if (longestRoute == rec.longestRoute(p.getColor())) {
					score = score + longestBounus;
				}
			}

			p.setScore(score);
		}

	}

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
			if (p.getColor().equals(Colors.player.BLACK)) {
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
				if (r.getColor().equals(Colors.route.ANY)
						&& mainApp.gameState.getBoard().getAllRoutes(r.getBegin(), r.getEnd()).size() == 2) {
					if (mainApp.gameState.getBoard().getAllRoutes(r.getBegin(), r.getEnd()).get(0).getOwner()
							.equals(p.getColor())) {
						if (null != routeLocations.getRouteLocation(r.getBegin(), r.getEnd(),
								r.getColor().toString())) {
							for (TrainLocation t : routeLocations.getRouteLocation(r.getBegin(), r.getEnd(),
									r.getColor().toString()).trains) {
								mapStage.getBatch().draw(trainImage, t.x - 33, t.y - 15, 33, 15, 66, 30, 1, 1, t.r);
							}
						}
					} else {
						if (-1 != routeLocations.getList().indexOf(
								routeLocations.getRouteLocation(r.getBegin(), r.getEnd(), r.getColor().toString()))) {
							int secondANYIndex = 1 + routeLocations.getList().indexOf(
									routeLocations.getRouteLocation(r.getBegin(), r.getEnd(), r.getColor().toString()));
							RouteLocation rL = routeLocations.getList().get(secondANYIndex);
							if (null != rL) {
								for (TrainLocation t : rL.trains) {
									mapStage.getBatch().draw(trainImage, t.x - 33, t.y - 15, 33, 15, 66, 30, 1, 1, t.r);
								}
							}
						}
					}
				} else {
					if (null != routeLocations.getRouteLocation(r.getBegin(), r.getEnd(), r.getColor().toString())) {
						for (TrainLocation t : routeLocations.getRouteLocation(r.getBegin(), r.getEnd(),
								r.getColor().toString()).trains) {
							mapStage.getBatch().draw(trainImage, t.x - 33, t.y - 15, 33, 15, 66, 30, 1, 1, t.r);
						}
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
		demoSelectedCity.setWidth(demoSelectedCity.getPrefWidth());
		demoSelectedRoute.setText(selectedRoute);
		demoSelectedRoute.setWidth(demoSelectedRoute.getPrefWidth());
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
		mapStage.dispose();
		guiStage.dispose();
	}

}