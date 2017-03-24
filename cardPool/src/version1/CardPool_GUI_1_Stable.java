package version1;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;


import com.jgoodies.forms.factories.DefaultComponentFactory;

public class CardPool_GUI_1_Stable {

	private JFrame DraftInit;
	private JFrame singleCardView;
	private JFrame cardView;
	private JFrame banList;
	private int rerollsSoFar = 0;
	private int pickNumber = 1;
	private int playerDrafting = 0;
	private ArrayList<Card> drafted = new ArrayList<Card>();
	private ArrayList<Card> threeChoiceTemp = new ArrayList<Card>();
	
	// GUI Init Stuff
	public static void main(String[] args) 
	{
		// Start GUI Stuff
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CardPool_GUI_1_Stable window = new CardPool_GUI_1_Stable();
					window.DraftInit.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public CardPool_GUI_1_Stable() {
		initialize();
	}
	// End GUI Init Stuff
	
	
	
	// MAIN
	@SuppressWarnings("unchecked")
	private void initialize() 
	{


		// Variable Initialization
		ArrayList<Card> allCards = new ArrayList<Card>();
		ArrayList<String> dumbThing = new ArrayList<String>();
		ArrayList<ArrayList<Card>> draftDecks = new ArrayList<ArrayList<Card>>();
		ArrayList<ArrayList<Card>> draftPools = new ArrayList<ArrayList<Card>>();
		String[] input = null;
		String line = null;	String name = null;	String attribute = null;	String type = null;
		String cardType = null;	String text = null;	String crosslimit = null;	String rarity = null;
		String synergies = null;
		int atk = 0;	int def = 0;	int lvl = 0;	int tierScore = 0;	
		int limit = 0;	int quantity = 0;	int noOfCards = 1; 
		boolean monster = false;	boolean contin = false;	boolean quickplay = false;	boolean counter = false;
		boolean field = false;	boolean equip = false;	boolean ritual = false;	boolean normal = false;
		// END Variable Init

		// Database Setup
		readDatabase(noOfCards, line, input, name, attribute, type, cardType, atk, def, tierScore, lvl, quantity, limit, crosslimit, rarity, text, synergies, monster, contin, quickplay, counter, field, equip, ritual, normal, allCards);		
		ArrayList<Card> backupAllCards = copyPool(allCards);
		ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
		ArrayList<Card> blackListed = new ArrayList<Card>();
		SortingListModel<Card> banModel = new SortingListModel<Card>();
		JList<Card> banned = new JList<Card>(banModel);
		int totalUnique = allCardsNopeDupe.size();
		int totalCards = cardCount(allCards);
		// Two copies of database - allCards which we edit and backupAllCards which we do not edit, third array is the database with 1 copy of each card
		// totalCards initialized to the size of the database after read, totalUnique has the size equal to number of unique cards in the database
		// banModel is the model that will contain all cards that are banned at any point, banned is the actual list of cards inside the model


		// GUI Setup
		DraftInit = new JFrame();	
		DraftInit.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
		DraftInit.setBounds(100, 100, 189, 250);
		DraftInit.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		DraftInit.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		// Draft Start window components
		JLabel lblOfPlayers = DefaultComponentFactory.getInstance().createLabel("Players");
		JLabel lblOfCards = DefaultComponentFactory.getInstance().createLabel("Cards to Draft");
		JLabel lblOfRerolls = DefaultComponentFactory.getInstance().createLabel("Rerolls");
		JLabel lblCardCount = DefaultComponentFactory.getInstance().createTitle("Card Count");
		JLabel lblUniqueCards = new JLabel("Unique Cards: " + totalUnique);
		JSpinner spinner = new JSpinner(); spinner.setModel(new SpinnerNumberModel(1, 1, 10, 1));
		JSpinner cardsToDraft = new JSpinner(); cardsToDraft.setModel(new SpinnerNumberModel(60, 10, 200, 5));
		JSpinner rerolls = new JSpinner(); rerolls.setModel(new SpinnerNumberModel(1, 0, 10, 1));
		JComboBox fillStyle = new JComboBox();
		JButton draftStart = new JButton("Start Draft");
		JMenuBar menuBar = new JMenuBar();
			JMenu mnViewDatabase = new JMenu("Database");
				JMenuItem mntmViewAllCards = new JMenuItem("View all cards");
				JMenuItem mntmNewMenuItem = new JMenuItem("Ultimate Rare");
				JMenuItem mntmUltraRares = new JMenuItem("Ultra Rare");
				JMenuItem mntmSuperRares = new JMenuItem("Super Rare");
				JMenuItem mntmRares = new JMenuItem("Rare");
				JMenuItem mntmCommon = new JMenuItem("Common");
				JMenuItem cardViewer = new JMenuItem("Card Viewer");
			JMenu mnBan = new JMenu("Ban Menu");
				JMenuItem blacklist = new JMenuItem("Ban Cards");
				JMenuItem reset = new JMenuItem("Reset Pool");
				JMenu mnBanAll = new JMenu("Ban: Rarities");
					JMenuItem ultimateBanner = new JMenuItem("Ultimate Rare");
					JMenuItem ultraBanner = new JMenuItem("Ultra Rare");
					JMenuItem superBanner = new JMenuItem("Super Rare");
					JMenuItem rareBanner = new JMenuItem("Rare");
					JMenuItem commonBanner = new JMenuItem("Common");	
				JMenu mnBanGrouping = new JMenu("Ban: Grouping");
					JMenuItem Monarchs = new JMenuItem("Monarchs");
					JMenuItem Fissure = new JMenuItem("Fissure");
					JMenuItem TrapHole = new JMenuItem("Trap Hole");
					JMenuItem Exodia = new JMenuItem("Exodia");
					JMenuItem Naturia = new JMenuItem("Naturia");
					JMenuItem Water = new JMenuItem("Water");
					JMenuItem Toon = new JMenuItem("Toon");
					JMenuItem Ritual = new JMenuItem("Ritual");
					JMenuItem Fusion = new JMenuItem("Fusion");
					JMenuItem Draw = new JMenuItem("Card Draw");
					JMenuItem Limited = new JMenuItem("Limited Cards");
					JMenuItem LowAtk = new JMenuItem("Attack < 1500");
					JMenuItem HighAtk = new JMenuItem("Attack > 2600");
					JMenuItem LowLvl = new JMenuItem("Level < 4");
					JMenuItem HighLvl = new JMenuItem("Level > 7");
			
		DraftInit.getContentPane().add(lblOfPlayers); DraftInit.getContentPane().add(spinner);
		DraftInit.getContentPane().add(lblOfCards); DraftInit.getContentPane().add(cardsToDraft);
		DraftInit.getContentPane().add(lblOfRerolls); DraftInit.getContentPane().add(rerolls);
		DraftInit.getContentPane().add(fillStyle);
			fillStyle.setModel(new DefaultComboBoxModel(new String[] {"Random", "Equal"}));
			fillStyle.setEditable(false);
			fillStyle.setToolTipText("Pool Fill");
		DraftInit.getContentPane().add(draftStart);
			lblCardCount.setHorizontalAlignment(SwingConstants.CENTER);
			lblCardCount.setText("Cards Available: " + totalCards);
		DraftInit.getContentPane().add(lblCardCount);
		DraftInit.getContentPane().add(lblUniqueCards);
		DraftInit.setJMenuBar(menuBar);
			menuBar.add(mnViewDatabase); menuBar.add(mnBan);
			viewDatabaseListenerInit(mnViewDatabase, mntmViewAllCards, mntmNewMenuItem, allCards, mntmUltraRares, mntmSuperRares, mntmRares, mntmCommon, cardViewer);
			mnBan.add(blacklist); mnBan.add(mnBanAll);
			mnBan.add(mnBanGrouping); mnBanGrouping.add(Monarchs);
			mnBanGrouping.add(Fissure); mnBanGrouping.add(TrapHole);
			mnBanGrouping.add(Exodia); mnBanGrouping.add(Naturia);
			mnBanGrouping.add(Water); mnBanGrouping.add(Toon);
			mnBanGrouping.add(Ritual); mnBanGrouping.add(Fusion);
			mnBanGrouping.add(Draw); mnBanGrouping.add(Limited);
			mnBanGrouping.add(LowAtk); mnBanGrouping.add(HighAtk);
			mnBanGrouping.add(LowLvl); mnBanGrouping.add(HighLvl);
			mnBanAll.add(ultimateBanner); mnBanAll.add(ultraBanner); 
			mnBanAll.add(superBanner); mnBanAll.add(rareBanner);
			mnBanAll.add(commonBanner); mnBan.add(reset);
			blackListenerInit(blacklist, allCards, blackListed, banned, dumbThing, banModel, lblCardCount, lblUniqueCards, DraftInit, mntmUltraRares, mntmSuperRares,
					mntmRares, mntmCommon, ultimateBanner, ultraBanner, superBanner, rareBanner, commonBanner, mntmNewMenuItem, Monarchs, Fissure, TrapHole, Exodia,
					Water, Naturia, Toon, Draw, Ritual, Fusion, LowAtk, Limited, HighAtk, LowLvl, HighLvl, reset, HighLvl, backupAllCards, allCardsNopeDupe);
		
		// End primary window components
			
		
		// Init main draft window
		draftStart.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				DraftInit.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
				// Final Draft Variable Init
				int playerCountLocal = (Integer) spinner.getValue();
				int cardsToDraftLocal = (Integer) cardsToDraft.getValue();
				int rerollsLocal = (Integer) rerolls.getValue();
				boolean urItem = mntmNewMenuItem.isEnabled();
				boolean ulrItem = mntmUltraRares.isEnabled();
				boolean srItem = mntmSuperRares.isEnabled();
				boolean rItem = mntmRares.isEnabled();
				boolean cItem = mntmCommon.isEnabled();
				menuBar.remove(mnBan);
				mnViewDatabase.removeAll();
				DraftInit.getContentPane().removeAll();	
				JButton viewDeck = new JButton("View Deck");
				JButton reroll = new JButton("Reroll"); if (rerollsLocal == 0) { reroll.setEnabled(false); }
				
				// Change pool fill based on selection made in primary window
				String fillStyleString = fillStyle.getSelectedItem().toString();
				if (fillStyleString == "Random") { poolDeckInit(draftPools, draftDecks, playerCountLocal); fillAllPools(allCards, draftPools, playerCountLocal);	}
				else if (fillStyleString == "Equal") { poolDeckInit(draftPools, draftDecks, playerCountLocal); fillAllPools(allCards, draftPools, playerCountLocal);	}
				else { poolDeckInit(draftPools, draftDecks, playerCountLocal); fillAllPools(allCards, draftPools, playerCountLocal);	}
				
				//draftDecks.clear();
				
			
				// Fills up an array used to display the correct database of cards during the draft (banned cards removed)
				ArrayList<Card> draftAllCards = new ArrayList<Card>();
				for (int d = 0; d < draftPools.size(); d++) { for (int c = 0; c < draftPools.get(d).size(); c++) { draftAllCards.add(draftPools.get(d).get(c)); } }
				cardCounter(draftAllCards);
				draftAllCards.sort(draftAllCards.get(0));
				
				// Sets up the database view menu again after rechecking the database contents
				viewDatabaseListenerReinit(mnViewDatabase, draftAllCards, urItem, ulrItem, srItem, rItem, cItem);
				
				// Setup the arrays used for the draft - drafted holds the current players drafted cards, threeChoiceTemp holds the three picks at any given pick
				drafted.clear();
				removeLimitedCardsVoid(draftPools.get(playerDrafting), drafted);
				threeChoiceTemp = picks(draftPools.get(playerDrafting), drafted, 3);
				
				// Setup the card image buttons - looks to fill images with the Small.png suffix first, standardized images for this part of the draft ideally 310x450
				JButton cardPick1 = new JButton(); ImageIcon coh = null;
				JButton cardPick2 = new JButton(); ImageIcon bewd = null;
				JButton cardPick3 = new JButton(); ImageIcon exodia = null;
				if (isImage("src/images/" + threeChoiceTemp.get(0).getName() + "Small.png")) { coh = new ImageIcon("src/images/" + threeChoiceTemp.get(0).getName() + "Small.png");}
				else { coh = new ImageIcon("src/images/" + threeChoiceTemp.get(0).getName() + ".png"); } cardPick1.setIcon(coh);
				if (isImage("src/images/" + threeChoiceTemp.get(1).getName() + "Small.png")) { bewd = new ImageIcon("src/images/" + threeChoiceTemp.get(1).getName() + "Small.png");}
				else { bewd = new ImageIcon("src/images/" + threeChoiceTemp.get(1).getName() + ".png"); } cardPick2.setIcon(bewd);
				if (isImage("src/images/" + threeChoiceTemp.get(2).getName() + "Small.png")) { exodia = new ImageIcon("src/images/" + threeChoiceTemp.get(2).getName() + "Small.png");}
				else { exodia = new ImageIcon("src/images/" + threeChoiceTemp.get(2).getName() + ".png"); } cardPick3.setIcon(exodia);
				
				// Setup the labels on the draft window
				JLabel pickCounter = new JLabel("Pick " + pickNumber + "/" + cardsToDraftLocal);
				JLabel poolCardCount = new JLabel("Cards in Pool: " + draftPools.get(playerDrafting).size());
				JLabel playerDraftLbl = new JLabel("Player: " + (playerDrafting + 1) + " drafting");
				JLabel pick1Rarity = new JLabel(threeChoiceTemp.get(0).getRarity());
				JLabel pick2Rarity = new JLabel(threeChoiceTemp.get(1).getRarity());
				JLabel pick3Rarity = new JLabel(threeChoiceTemp.get(2).getRarity());
				
				// Adding everything to the draft window
				DraftInit.getContentPane().add(cardPick1); DraftInit.getContentPane().add(cardPick2); DraftInit.getContentPane().add(cardPick3);
				DraftInit.getContentPane().add(viewDeck); DraftInit.getContentPane().add(reroll); 
				DraftInit.getContentPane().add(pickCounter); DraftInit.getContentPane().add(poolCardCount);
				DraftInit.getContentPane().add(playerDraftLbl);
				
				// Button Listeners for the draft window - cardPick listeners handle most of the draft logic	
				cardPick1.addActionListener(new ActionListener() 
				{
					public void actionPerformed(ActionEvent arg0) 
					{
						if (pickNumber <= cardsToDraftLocal)
						{
							drafted.add(threeChoiceTemp.get(0));
							draftPools.get(playerDrafting).remove(threeChoiceTemp.get(0));
							draftPools.get(playerDrafting).remove(threeChoiceTemp.get(1));
							draftPools.get(playerDrafting).remove(threeChoiceTemp.get(2));
							drafted.get(drafted.size() - 1).setQuantity(1);		
							cardCounter(drafted); drafted.sort(drafted.get(0));
							picksVoid(draftPools.get(playerDrafting), drafted, 3, threeChoiceTemp);
								
							ImageIcon coh2 = null;
							if (isImage("src/images/" + threeChoiceTemp.get(0).getName() + "Small.png")) { coh2 = new ImageIcon("src/images/" + threeChoiceTemp.get(0).getName() + "Small.png");}
							else { coh2 = new ImageIcon("src/images/" + threeChoiceTemp.get(0).getName() + ".png"); }
							cardPick1.setIcon(coh2);
							
							ImageIcon bewd2 = null;
							if (isImage("src/images/" + threeChoiceTemp.get(1).getName() + "Small.png")) { bewd2 = new ImageIcon("src/images/" + threeChoiceTemp.get(1).getName() + "Small.png");}
							else { bewd2 = new ImageIcon("src/images/" + threeChoiceTemp.get(1).getName() + ".png"); }
							cardPick2.setIcon(bewd2);
							
							ImageIcon exodia2 = null;
							if (isImage("src/images/" + threeChoiceTemp.get(2).getName() + "Small.png")) { exodia2 = new ImageIcon("src/images/" + threeChoiceTemp.get(2).getName() + "Small.png");}
							else { exodia2 = new ImageIcon("src/images/" + threeChoiceTemp.get(2).getName() + ".png"); }
							cardPick3.setIcon(exodia2);			
							
							pickNumber++;
							pickCounter.setText("Pick " + pickNumber + "/" + cardsToDraftLocal);
							
							poolCardCount.setText("Cards in Pool: " + draftPools.get(playerDrafting).size());
							
							if (pickNumber > cardsToDraftLocal)
							{
								draftDecks.get(playerDrafting).clear();
								draftDecks.get(playerDrafting).addAll(drafted);
								cardCounter(draftDecks.get(playerDrafting));
								draftDecks.get(playerDrafting).sort(drafted.get(0));
							}
							
							
							if (pickNumber > cardsToDraftLocal)
							{
								
								playerDrafting++;
								playerDraftLbl.setText("Player: " + (playerDrafting + 1) + " drafting");
								if (playerDrafting < playerCountLocal)
								{
									pickNumber = 1;
									rerollsSoFar = 0;
									reroll.setEnabled(true);
									drafted.clear();
									removeLimitedCardsVoid(draftPools.get(playerDrafting), drafted);
									threeChoiceTemp = picks(draftPools.get(playerDrafting), drafted, 3);
									ImageIcon coh3 = null;
									if (isImage("src/images/" + threeChoiceTemp.get(0).getName() + "Small.png")) { coh3 = new ImageIcon("src/images/" + threeChoiceTemp.get(0).getName() + "Small.png");}
									else { coh3 = new ImageIcon("src/images/" + threeChoiceTemp.get(0).getName() + ".png"); }
									cardPick1.setIcon(coh2);
									
									ImageIcon bewd3 = null;
									if (isImage("src/images/" + threeChoiceTemp.get(1).getName() + "Small.png")) { bewd3 = new ImageIcon("src/images/" + threeChoiceTemp.get(1).getName() + "Small.png");}
									else { bewd3 = new ImageIcon("src/images/" + threeChoiceTemp.get(1).getName() + ".png"); }
									cardPick2.setIcon(bewd2);
									
									ImageIcon exodia3 = null;
									if (isImage("src/images/" + threeChoiceTemp.get(2).getName() + "Small.png")) { exodia3 = new ImageIcon("src/images/" + threeChoiceTemp.get(2).getName() + "Small.png");}
									else { exodia3 = new ImageIcon("src/images/" + threeChoiceTemp.get(2).getName() + ".png"); }
									cardPick3.setIcon(exodia2);			
									pickCounter.setText("Pick " + pickNumber + "/" + cardsToDraftLocal);
									poolCardCount.setText("Cards in Pool: " + draftPools.get(playerDrafting).size());
								}
								else
								{
									JFrame finalFrame = new JFrame();
									finalFrame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
									finalFrame.setBounds(100, 100, 496, 443);
									finalFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
									finalFrame.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
									
									JButton viewDeckOf = new JButton("View Deck");
									JComboBox playerDeck = new JComboBox();
									finalFrame.getContentPane().add(playerDeck);
									switch (playerCountLocal)
									{
										case 1:
											playerDeck.setModel(new DefaultComboBoxModel(new String[] {"Player 1"}));
											break;
										case 2:
											playerDeck.setModel(new DefaultComboBoxModel(new String[] {"Player 1", "Player 2"}));
											break;
										case 3:
											playerDeck.setModel(new DefaultComboBoxModel(new String[] {"Player 1", "Player 2", "Player 3"}));
											break;
										case 4:
											playerDeck.setModel(new DefaultComboBoxModel(new String[] {"Player 1", "Player 2", "Player 3", "Player 4"}));
											break;
										case 5:
											playerDeck.setModel(new DefaultComboBoxModel(new String[] {"Player 1", "Player 2", "Player 3", "Player 4", "Player 5"}));
											break;
										case 6:
											playerDeck.setModel(new DefaultComboBoxModel(new String[] {"Player 1", "Player 2", "Player 3", "Player 4", "Player 5", "Player 6"}));
											break;
										case 7:
											playerDeck.setModel(new DefaultComboBoxModel(new String[] {"Player 1", "Player 2", "Player 3", "Player 4", "Player 5", "Player 6", "Player 7"}));
											break;
										case 8:
											playerDeck.setModel(new DefaultComboBoxModel(new String[] {"Player 1", "Player 2", "Player 3", "Player 4", "Player 5", "Player 6", "Player 7", "Player 8"}));
											break;
										case 9:
											playerDeck.setModel(new DefaultComboBoxModel(new String[] {"Player 1", "Player 2", "Player 3", "Player 4", "Player 5", "Player 6", "Player 7", "Player 8", "Player 9"}));
											break;
										case 10:
											playerDeck.setModel(new DefaultComboBoxModel(new String[] {"Player 1", "Player 2", "Player 3", "Player 4", "Player 5", "Player 6", "Player 7", "Player 8", "Player 9", "Player 10"}));
											break;
										default: 
											playerDeck.setModel(new DefaultComboBoxModel(new String[] {"Player 1", "Player 2", "Player 3", "Player 4", "Player 5", "Player 6", "Player 7", "Player 8", "Player 9", "Player 10"}));
											break;
									}
									
									viewDeckOf.addActionListener(new ActionListener() 
									{
										public void actionPerformed(ActionEvent arg0) 
										{
											JFrame cardViewLocal = new JFrame();	
											cardViewLocal.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
											cardViewLocal.setBounds(100, 100, 496, 443);
											cardViewLocal.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
											ArrayList<Card >allCardsNoDupes = listMaker(draftDecks.get(playerDeck.getSelectedIndex()));
											allCardsNoDupes.sort(allCardsNoDupes.get(0));
											JList list = new JList(allCardsNoDupes.toArray());
											KeyListener keyListener = new KeyListener()
											{
												public void keyPressed(KeyEvent e)
												{
													if (e.getKeyCode() == KeyEvent.VK_ENTER)
													{
														Card selectedCard = (Card) list.getSelectedValue();
														JFrame singlecardViewLocal = new JFrame();
														JPanel panel = new JPanel();
														ImageLabel label = new ImageLabel(new ImageIcon("src/images/" + selectedCard.getName() + ".png"));
														label.setLocation(29, 37);
														panel.add(label);
														singlecardViewLocal.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
														singlecardViewLocal.setBounds(100, 100, 496, 443);
														singlecardViewLocal.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
														singlecardViewLocal.getContentPane().add(panel);
														singlecardViewLocal.pack();
														singlecardViewLocal.setVisible(true);
													}
												}

												@Override
												public void keyReleased(KeyEvent arg0) {


												}

												@Override
												public void keyTyped(KeyEvent arg0) {


												}
											};
											list.addKeyListener(keyListener);
											MouseListener mouseListener = new MouseAdapter() 
											{
												public void mouseClicked(MouseEvent e) 
												{
													if (e.getClickCount() == 2) 
													{    		
														Card selectedCard = (Card) list.getSelectedValue();
														JFrame singlecardViewLocal = new JFrame();
														JPanel panel = new JPanel();
														ImageLabel label = new ImageLabel(new ImageIcon("src/images/" + selectedCard.getName() + ".png"));
														label.setLocation(29, 37);
														panel.add(label);
														singlecardViewLocal.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
														singlecardViewLocal.setBounds(100, 100, 496, 443);
														singlecardViewLocal.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
														singlecardViewLocal.getContentPane().add(panel);
														singlecardViewLocal.pack();
														singlecardViewLocal.setVisible(true);
													}
												}
											};
											list.addMouseListener(mouseListener);
											JScrollPane scrollPane = new JScrollPane();
											scrollPane.setViewportView(list);
											cardViewLocal.getContentPane().add(scrollPane);
											cardViewLocal.pack();
											cardViewLocal.setVisible(true);
										}
										
									});
									
									
									
									playerDeck.setEditable(false);
									finalFrame.getContentPane().add(viewDeckOf);
									
									finalFrame.pack();
									finalFrame.setVisible(true);
									DraftInit.setVisible(false);
									
								}
							}
							
							DraftInit.revalidate(); DraftInit.repaint();
						}
						
					}
				});
				cardPick2.addActionListener(new ActionListener() 
				{
					public void actionPerformed(ActionEvent arg0) 
					{
						if (pickNumber <= cardsToDraftLocal)
						{
							drafted.add(threeChoiceTemp.get(1));
							draftPools.get(playerDrafting).remove(threeChoiceTemp.get(0));
							draftPools.get(playerDrafting).remove(threeChoiceTemp.get(1));
							draftPools.get(playerDrafting).remove(threeChoiceTemp.get(2));
							drafted.get(drafted.size() - 1).setQuantity(1);		
							cardCounter(drafted); drafted.sort(drafted.get(0));
							picksVoid(draftPools.get(playerDrafting), drafted, 3, threeChoiceTemp);
								
							ImageIcon coh2 = null;
							if (isImage("src/images/" + threeChoiceTemp.get(0).getName() + "Small.png")) { coh2 = new ImageIcon("src/images/" + threeChoiceTemp.get(0).getName() + "Small.png");}
							else { coh2 = new ImageIcon("src/images/" + threeChoiceTemp.get(0).getName() + ".png"); }
							cardPick1.setIcon(coh2);
							
							ImageIcon bewd2 = null;
							if (isImage("src/images/" + threeChoiceTemp.get(1).getName() + "Small.png")) { bewd2 = new ImageIcon("src/images/" + threeChoiceTemp.get(1).getName() + "Small.png");}
							else { bewd2 = new ImageIcon("src/images/" + threeChoiceTemp.get(1).getName() + ".png"); }
							cardPick2.setIcon(bewd2);
							
							ImageIcon exodia2 = null;
							if (isImage("src/images/" + threeChoiceTemp.get(2).getName() + "Small.png")) { exodia2 = new ImageIcon("src/images/" + threeChoiceTemp.get(2).getName() + "Small.png");}
							else { exodia2 = new ImageIcon("src/images/" + threeChoiceTemp.get(2).getName() + ".png"); }
							cardPick3.setIcon(exodia2);			
							
							pickNumber++;
							pickCounter.setText("Pick " + pickNumber + "/" + cardsToDraftLocal);
							
							poolCardCount.setText("Cards in Pool: " + draftPools.get(playerDrafting).size());
							
							if (pickNumber > cardsToDraftLocal)
							{
								draftDecks.get(playerDrafting).clear();
								draftDecks.get(playerDrafting).addAll(drafted);
								cardCounter(draftDecks.get(playerDrafting));
								draftDecks.get(playerDrafting).sort(drafted.get(0));
							}
							
							if (pickNumber > cardsToDraftLocal)
							{
								playerDrafting++;
								playerDraftLbl.setText("Player: " + (playerDrafting + 1) + " drafting");
								if (playerDrafting < playerCountLocal)
								{
									pickNumber = 1;
									rerollsSoFar = 0;
									reroll.setEnabled(true);
									drafted.clear();
									removeLimitedCardsVoid(draftPools.get(playerDrafting), drafted);
									threeChoiceTemp = picks(draftPools.get(playerDrafting), drafted, 3);
									ImageIcon coh3 = null;
									if (isImage("src/images/" + threeChoiceTemp.get(0).getName() + "Small.png")) { coh3 = new ImageIcon("src/images/" + threeChoiceTemp.get(0).getName() + "Small.png");}
									else { coh3 = new ImageIcon("src/images/" + threeChoiceTemp.get(0).getName() + ".png"); }
									cardPick1.setIcon(coh2);
									
									ImageIcon bewd3 = null;
									if (isImage("src/images/" + threeChoiceTemp.get(1).getName() + "Small.png")) { bewd3 = new ImageIcon("src/images/" + threeChoiceTemp.get(1).getName() + "Small.png");}
									else { bewd3 = new ImageIcon("src/images/" + threeChoiceTemp.get(1).getName() + ".png"); }
									cardPick2.setIcon(bewd2);
									
									ImageIcon exodia3 = null;
									if (isImage("src/images/" + threeChoiceTemp.get(2).getName() + "Small.png")) { exodia3 = new ImageIcon("src/images/" + threeChoiceTemp.get(2).getName() + "Small.png");}
									else { exodia3 = new ImageIcon("src/images/" + threeChoiceTemp.get(2).getName() + ".png"); }
									cardPick3.setIcon(exodia2);			
									pickCounter.setText("Pick " + pickNumber + "/" + cardsToDraftLocal);
									poolCardCount.setText("Cards in Pool: " + draftPools.get(playerDrafting).size());
								}
								else
								{
									JFrame finalFrame = new JFrame();
									finalFrame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
									finalFrame.setBounds(100, 100, 496, 443);
									finalFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
									finalFrame.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
									
									JButton viewDeckOf = new JButton("View Deck");
									JComboBox playerDeck = new JComboBox();
									finalFrame.getContentPane().add(playerDeck);
									switch (playerCountLocal)
									{
										case 1:
											playerDeck.setModel(new DefaultComboBoxModel(new String[] {"Player 1"}));
											break;
										case 2:
											playerDeck.setModel(new DefaultComboBoxModel(new String[] {"Player 1", "Player 2"}));
											break;
										case 3:
											playerDeck.setModel(new DefaultComboBoxModel(new String[] {"Player 1", "Player 2", "Player 3"}));
											break;
										case 4:
											playerDeck.setModel(new DefaultComboBoxModel(new String[] {"Player 1", "Player 2", "Player 3", "Player 4"}));
											break;
										case 5:
											playerDeck.setModel(new DefaultComboBoxModel(new String[] {"Player 1", "Player 2", "Player 3", "Player 4", "Player 5"}));
											break;
										case 6:
											playerDeck.setModel(new DefaultComboBoxModel(new String[] {"Player 1", "Player 2", "Player 3", "Player 4", "Player 5", "Player 6"}));
											break;
										case 7:
											playerDeck.setModel(new DefaultComboBoxModel(new String[] {"Player 1", "Player 2", "Player 3", "Player 4", "Player 5", "Player 6", "Player 7"}));
											break;
										case 8:
											playerDeck.setModel(new DefaultComboBoxModel(new String[] {"Player 1", "Player 2", "Player 3", "Player 4", "Player 5", "Player 6", "Player 7", "Player 8"}));
											break;
										case 9:
											playerDeck.setModel(new DefaultComboBoxModel(new String[] {"Player 1", "Player 2", "Player 3", "Player 4", "Player 5", "Player 6", "Player 7", "Player 8", "Player 9"}));
											break;
										case 10:
											playerDeck.setModel(new DefaultComboBoxModel(new String[] {"Player 1", "Player 2", "Player 3", "Player 4", "Player 5", "Player 6", "Player 7", "Player 8", "Player 9", "Player 10"}));
											break;
										default: 
											playerDeck.setModel(new DefaultComboBoxModel(new String[] {"Player 1", "Player 2", "Player 3", "Player 4", "Player 5", "Player 6", "Player 7", "Player 8", "Player 9", "Player 10"}));
											break;
									}
									
									viewDeckOf.addActionListener(new ActionListener() 
									{
										public void actionPerformed(ActionEvent arg0) 
										{
											JFrame cardViewLocal = new JFrame();	
											cardViewLocal.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
											cardViewLocal.setBounds(100, 100, 496, 443);
											cardViewLocal.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
											ArrayList<Card >allCardsNoDupes = listMaker(draftDecks.get(playerDeck.getSelectedIndex()));
											JList list = new JList(allCardsNoDupes.toArray());
											KeyListener keyListener = new KeyListener()
											{
												public void keyPressed(KeyEvent e)
												{
													if (e.getKeyCode() == KeyEvent.VK_ENTER)
													{
														Card selectedCard = (Card) list.getSelectedValue();
														JFrame singlecardViewLocal = new JFrame();
														JPanel panel = new JPanel();
														ImageLabel label = new ImageLabel(new ImageIcon("src/images/" + selectedCard.getName() + ".png"));
														label.setLocation(29, 37);
														panel.add(label);
														singlecardViewLocal.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
														singlecardViewLocal.setBounds(100, 100, 496, 443);
														singlecardViewLocal.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
														singlecardViewLocal.getContentPane().add(panel);
														singlecardViewLocal.pack();
														singlecardViewLocal.setVisible(true);
													}
												}

												@Override
												public void keyReleased(KeyEvent arg0) {


												}

												@Override
												public void keyTyped(KeyEvent arg0) {


												}
											};
											list.addKeyListener(keyListener);
											MouseListener mouseListener = new MouseAdapter() 
											{
												public void mouseClicked(MouseEvent e) 
												{
													if (e.getClickCount() == 2) 
													{    		
														Card selectedCard = (Card) list.getSelectedValue();
														//JFrame singlecardViewLocal;
														JFrame singlecardViewLocal = new JFrame();
														JPanel panel = new JPanel();
														ImageLabel label = new ImageLabel(new ImageIcon("src/images/" + selectedCard.getName() + ".png"));
														label.setLocation(29, 37);
														panel.add(label);
														singlecardViewLocal.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
														singlecardViewLocal.setBounds(100, 100, 496, 443);
														singlecardViewLocal.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
														singlecardViewLocal.getContentPane().add(panel);
														singlecardViewLocal.pack();
														singlecardViewLocal.setVisible(true);
													}
												}
											};
											list.addMouseListener(mouseListener);
											JScrollPane scrollPane = new JScrollPane();
											scrollPane.setViewportView(list);
											cardViewLocal.getContentPane().add(scrollPane);
											cardViewLocal.pack();
											cardViewLocal.setVisible(true);
										}
										
									});
									
									
									
									playerDeck.setEditable(false);
									finalFrame.getContentPane().add(viewDeckOf);
									
									finalFrame.pack();
									finalFrame.setVisible(true);
									DraftInit.setVisible(false);
									
								}
							}
							
							DraftInit.revalidate(); DraftInit.repaint();
						}
					}
				});
				cardPick3.addActionListener(new ActionListener() 
				{
					public void actionPerformed(ActionEvent arg0) 
					{
						if (pickNumber <= cardsToDraftLocal)
						{
							drafted.add(threeChoiceTemp.get(2));
							draftPools.get(playerDrafting).remove(threeChoiceTemp.get(0));
							draftPools.get(playerDrafting).remove(threeChoiceTemp.get(1));
							draftPools.get(playerDrafting).remove(threeChoiceTemp.get(2));
							drafted.get(drafted.size() - 1).setQuantity(1);		
							cardCounter(drafted); drafted.sort(drafted.get(0));
							picksVoid(draftPools.get(playerDrafting), drafted, 3, threeChoiceTemp);
								
							ImageIcon coh2 = null;
							if (isImage("src/images/" + threeChoiceTemp.get(0).getName() + "Small.png")) { coh2 = new ImageIcon("src/images/" + threeChoiceTemp.get(0).getName() + "Small.png");}
							else { coh2 = new ImageIcon("src/images/" + threeChoiceTemp.get(0).getName() + ".png"); }
							cardPick1.setIcon(coh2);
							
							ImageIcon bewd2 = null;
							if (isImage("src/images/" + threeChoiceTemp.get(1).getName() + "Small.png")) { bewd2 = new ImageIcon("src/images/" + threeChoiceTemp.get(1).getName() + "Small.png");}
							else { bewd2 = new ImageIcon("src/images/" + threeChoiceTemp.get(1).getName() + ".png"); }
							cardPick2.setIcon(bewd2);
							
							ImageIcon exodia2 = null;
							if (isImage("src/images/" + threeChoiceTemp.get(2).getName() + "Small.png")) { exodia2 = new ImageIcon("src/images/" + threeChoiceTemp.get(2).getName() + "Small.png");}
							else { exodia2 = new ImageIcon("src/images/" + threeChoiceTemp.get(2).getName() + ".png"); }
							cardPick3.setIcon(exodia2);			
							
							pickNumber++;
							pickCounter.setText("Pick " + pickNumber + "/" + cardsToDraftLocal);
							
							poolCardCount.setText("Cards in Pool: " + draftPools.get(playerDrafting).size());
							
							if (pickNumber > cardsToDraftLocal)
							{
								draftDecks.get(playerDrafting).clear();
								draftDecks.get(playerDrafting).addAll(drafted);
								cardCounter(draftDecks.get(playerDrafting));
								draftDecks.get(playerDrafting).sort(drafted.get(0));
							}
							
							if (pickNumber > cardsToDraftLocal)
							{
								playerDrafting++;
								playerDraftLbl.setText("Player: " + (playerDrafting + 1) + " drafting");
								if (playerDrafting < playerCountLocal)
								{
									pickNumber = 1;
									rerollsSoFar = 0;
									reroll.setEnabled(true);
									drafted.clear();
									removeLimitedCardsVoid(draftPools.get(playerDrafting), drafted);
									threeChoiceTemp = picks(draftPools.get(playerDrafting), drafted, 3);
									ImageIcon coh3 = null;
									if (isImage("src/images/" + threeChoiceTemp.get(0).getName() + "Small.png")) { coh3 = new ImageIcon("src/images/" + threeChoiceTemp.get(0).getName() + "Small.png");}
									else { coh3 = new ImageIcon("src/images/" + threeChoiceTemp.get(0).getName() + ".png"); }
									cardPick1.setIcon(coh2);
									
									ImageIcon bewd3 = null;
									if (isImage("src/images/" + threeChoiceTemp.get(1).getName() + "Small.png")) { bewd3 = new ImageIcon("src/images/" + threeChoiceTemp.get(1).getName() + "Small.png");}
									else { bewd3 = new ImageIcon("src/images/" + threeChoiceTemp.get(1).getName() + ".png"); }
									cardPick2.setIcon(bewd2);
									
									ImageIcon exodia3 = null;
									if (isImage("src/images/" + threeChoiceTemp.get(2).getName() + "Small.png")) { exodia3 = new ImageIcon("src/images/" + threeChoiceTemp.get(2).getName() + "Small.png");}
									else { exodia3 = new ImageIcon("src/images/" + threeChoiceTemp.get(2).getName() + ".png"); }
									cardPick3.setIcon(exodia2);			
									pickCounter.setText("Pick " + pickNumber + "/" + cardsToDraftLocal);
									poolCardCount.setText("Cards in Pool: " + draftPools.get(playerDrafting).size());
								}
								else
								{
									JFrame finalFrame = new JFrame();
									finalFrame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
									finalFrame.setBounds(100, 100, 496, 443);
									finalFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
									finalFrame.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
									
									JButton viewDeckOf = new JButton("View Deck");
									JComboBox playerDeck = new JComboBox();
									finalFrame.getContentPane().add(playerDeck);
									switch (playerCountLocal)
									{
										case 1:
											playerDeck.setModel(new DefaultComboBoxModel(new String[] {"Player 1"}));
											break;
										case 2:
											playerDeck.setModel(new DefaultComboBoxModel(new String[] {"Player 1", "Player 2"}));
											break;
										case 3:
											playerDeck.setModel(new DefaultComboBoxModel(new String[] {"Player 1", "Player 2", "Player 3"}));
											break;
										case 4:
											playerDeck.setModel(new DefaultComboBoxModel(new String[] {"Player 1", "Player 2", "Player 3", "Player 4"}));
											break;
										case 5:
											playerDeck.setModel(new DefaultComboBoxModel(new String[] {"Player 1", "Player 2", "Player 3", "Player 4", "Player 5"}));
											break;
										case 6:
											playerDeck.setModel(new DefaultComboBoxModel(new String[] {"Player 1", "Player 2", "Player 3", "Player 4", "Player 5", "Player 6"}));
											break;
										case 7:
											playerDeck.setModel(new DefaultComboBoxModel(new String[] {"Player 1", "Player 2", "Player 3", "Player 4", "Player 5", "Player 6", "Player 7"}));
											break;
										case 8:
											playerDeck.setModel(new DefaultComboBoxModel(new String[] {"Player 1", "Player 2", "Player 3", "Player 4", "Player 5", "Player 6", "Player 7", "Player 8"}));
											break;
										case 9:
											playerDeck.setModel(new DefaultComboBoxModel(new String[] {"Player 1", "Player 2", "Player 3", "Player 4", "Player 5", "Player 6", "Player 7", "Player 8", "Player 9"}));
											break;
										case 10:
											playerDeck.setModel(new DefaultComboBoxModel(new String[] {"Player 1", "Player 2", "Player 3", "Player 4", "Player 5", "Player 6", "Player 7", "Player 8", "Player 9", "Player 10"}));
											break;
										default: 
											playerDeck.setModel(new DefaultComboBoxModel(new String[] {"Player 1", "Player 2", "Player 3", "Player 4", "Player 5", "Player 6", "Player 7", "Player 8", "Player 9", "Player 10"}));
											break;
									}
									
									viewDeckOf.addActionListener(new ActionListener() 
									{
										public void actionPerformed(ActionEvent arg0) 
										{
											JFrame cardViewLocal = new JFrame();	
											cardViewLocal.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
											cardViewLocal.setBounds(100, 100, 496, 443);
											cardViewLocal.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
											ArrayList<Card >allCardsNoDupes = listMaker(draftDecks.get(playerDeck.getSelectedIndex()));
											JList list = new JList(allCardsNoDupes.toArray());
											KeyListener keyListener = new KeyListener()
											{
												public void keyPressed(KeyEvent e)
												{
													if (e.getKeyCode() == KeyEvent.VK_ENTER)
													{
														Card selectedCard = (Card) list.getSelectedValue();
														JFrame singlecardViewLocal = new JFrame();
														JPanel panel = new JPanel();
														ImageLabel label = new ImageLabel(new ImageIcon("src/images/" + selectedCard.getName() + ".png"));
														label.setLocation(29, 37);
														panel.add(label);
														singlecardViewLocal.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
														singlecardViewLocal.setBounds(100, 100, 496, 443);
														singlecardViewLocal.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
														singlecardViewLocal.getContentPane().add(panel);
														singlecardViewLocal.pack();
														singlecardViewLocal.setVisible(true);
													}
												}

												@Override
												public void keyReleased(KeyEvent arg0) {


												}

												@Override
												public void keyTyped(KeyEvent arg0) {


												}
											};
											list.addKeyListener(keyListener);
											MouseListener mouseListener = new MouseAdapter() 
											{
												public void mouseClicked(MouseEvent e) 
												{
													if (e.getClickCount() == 2) 
													{    		
														Card selectedCard = (Card) list.getSelectedValue();
														//JFrame singlecardViewLocal;
														JFrame singlecardViewLocal = new JFrame();
														JPanel panel = new JPanel();
														ImageLabel label = new ImageLabel(new ImageIcon("src/images/" + selectedCard.getName() + ".png"));
														label.setLocation(29, 37);
														panel.add(label);
														singlecardViewLocal.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
														singlecardViewLocal.setBounds(100, 100, 496, 443);
														singlecardViewLocal.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
														singlecardViewLocal.getContentPane().add(panel);
														singlecardViewLocal.pack();
														singlecardViewLocal.setVisible(true);
													}
												}
											};
											list.addMouseListener(mouseListener);
											JScrollPane scrollPane = new JScrollPane();
											scrollPane.setViewportView(list);
											cardViewLocal.getContentPane().add(scrollPane);
											cardViewLocal.pack();
											cardViewLocal.setVisible(true);
										}
										
									});
									
									
									
									playerDeck.setEditable(false);
									finalFrame.getContentPane().add(viewDeckOf);
									
									finalFrame.pack();
									finalFrame.setVisible(true);
									DraftInit.setVisible(false);
									
								}
							}
							
							DraftInit.revalidate(); DraftInit.repaint();
						}
					}
				});	
				reroll.addActionListener(new ActionListener() 
				{
					public void actionPerformed(ActionEvent arg0) 
					{
						if (canReroll(rerollsSoFar, rerollsLocal))
						{
							draftPools.remove(threeChoiceTemp.get(0));
							draftPools.remove(threeChoiceTemp.get(1));
							draftPools.remove(threeChoiceTemp.get(2));
							picksVoid(draftPools.get(playerDrafting), drafted, 3, threeChoiceTemp);
							
							ImageIcon coh2 = null;
							if (isImage("src/images/" + threeChoiceTemp.get(0).getName() + "Small.png")) { coh2 = new ImageIcon("src/images/" + threeChoiceTemp.get(0).getName() + "Small.png");}
							else { coh2 = new ImageIcon("src/images/" + threeChoiceTemp.get(0).getName() + ".png"); }
							cardPick1.setIcon(coh2);
							
							ImageIcon bewd2 = null;
							if (isImage("src/images/" + threeChoiceTemp.get(1).getName() + "Small.png")) { bewd2 = new ImageIcon("src/images/" + threeChoiceTemp.get(1).getName() + "Small.png");}
							else { bewd2 = new ImageIcon("src/images/" + threeChoiceTemp.get(1).getName() + ".png"); }
							cardPick2.setIcon(bewd2);
							
							ImageIcon exodia2 = null;
							if (isImage("src/images/" + threeChoiceTemp.get(2).getName() + "Small.png")) { exodia2 = new ImageIcon("src/images/" + threeChoiceTemp.get(2).getName() + "Small.png");}
							else { exodia2 = new ImageIcon("src/images/" + threeChoiceTemp.get(2).getName() + ".png"); }
							cardPick3.setIcon(exodia2);			
							rerollsSoFar++;
							
							if (canReroll(rerollsSoFar, rerollsLocal) == false) { reroll.setEnabled(false); }
							
							poolCardCount.setText("Cards in Pool: " + draftPools.get(playerDrafting).size());
							
							DraftInit.revalidate(); DraftInit.repaint();
						}
						else
						{
							reroll.setEnabled(false);
						}
					}
				});
				viewDeck.addActionListener(new ActionListener() 
				{
					public void actionPerformed(ActionEvent arg0) 
					{
						JFrame cardViewLocal = new JFrame();	
						cardViewLocal.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
						cardViewLocal.setBounds(100, 100, 496, 443);
						cardViewLocal.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
						ArrayList<Card >allCardsNoDupes = listMaker(drafted);
						JList list = new JList(allCardsNoDupes.toArray());
						KeyListener keyListener = new KeyListener()
						{
							public void keyPressed(KeyEvent e)
							{
								if (e.getKeyCode() == KeyEvent.VK_ENTER)
								{
									Card selectedCard = (Card) list.getSelectedValue();
									JFrame singlecardViewLocal = new JFrame();
									JPanel panel = new JPanel();
									ImageLabel label = new ImageLabel(new ImageIcon("src/images/" + selectedCard.getName() + ".png"));
									label.setLocation(29, 37);
									panel.add(label);
									singlecardViewLocal.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
									singlecardViewLocal.setBounds(100, 100, 496, 443);
									singlecardViewLocal.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
									singlecardViewLocal.getContentPane().add(panel);
									singlecardViewLocal.pack();
									singlecardViewLocal.setVisible(true);
								}
							}

							@Override
							public void keyReleased(KeyEvent arg0) {


							}

							@Override
							public void keyTyped(KeyEvent arg0) {


							}
						};
						list.addKeyListener(keyListener);
						MouseListener mouseListener = new MouseAdapter() 
						{
							public void mouseClicked(MouseEvent e) 
							{
								if (e.getClickCount() == 2) 
								{    		
									Card selectedCard = (Card) list.getSelectedValue();
									//JFrame singlecardViewLocal;
									JFrame singlecardViewLocal = new JFrame();
									JPanel panel = new JPanel();
									ImageLabel label = new ImageLabel(new ImageIcon("src/images/" + selectedCard.getName() + ".png"));
									label.setLocation(29, 37);
									panel.add(label);
									singlecardViewLocal.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
									singlecardViewLocal.setBounds(100, 100, 496, 443);
									singlecardViewLocal.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
									singlecardViewLocal.getContentPane().add(panel);
									singlecardViewLocal.pack();
									singlecardViewLocal.setVisible(true);
								}
							}
						};
						list.addMouseListener(mouseListener);
						JScrollPane scrollPane = new JScrollPane();
						scrollPane.setViewportView(list);
						cardViewLocal.getContentPane().add(scrollPane);
						cardViewLocal.pack();
						cardViewLocal.setVisible(true);
					}
				});

				DraftInit.pack();
				DraftInit.revalidate();
				DraftInit.repaint();
			}
		});
		
		

	}
	// END MAIN
	
	
	// METHODS
	

	// Feed yugiohdatabase.txt database into the program
	// Second method does some updates to the text of cards missing text
	static void readDatabase(int noOfCards, String line, String[] input, String name, String attribute, String type, String cardType, 
			int atk, int def, int tierScore, int lvl, int quantity, int limit, String crosslimit, String rarity, String text, String synergies, boolean monster,
			boolean contin, boolean quickplay, boolean counter, boolean field, boolean equip, boolean ritual, boolean normal, ArrayList<Card> allCards)
	{
		ArrayList<Card> updates = readOtherDatabase();

		try // Try to open the database
		{	
			// Opens database .txt file
			FileInputStream database = new FileInputStream("yugiohDatabase.txt");
			BufferedReader databaseStream = new BufferedReader(new InputStreamReader(database));

			// Loops through the text file and pulls the data for each card into a card object
			// Then stores each card object into allCards[]
			for (int i = 0; i < noOfCards; i++)
			{
				while (databaseStream.ready()) 
				{
					line = databaseStream.readLine();	
					input = line.split("~");

					name = input[0];	attribute = input[1];	type = input[2];	cardType = input[7];	
					atk = Integer.parseInt(input[3]);	def = Integer.parseInt(input[4]); tierScore = Integer.parseInt(input[9]);
					lvl = Integer.parseInt(input[5]);	quantity = Integer.parseInt(input[6]);
					limit = Integer.parseInt(input[15]);	crosslimit = input[16];
					rarity = input[19];	text = input[20];	synergies = input[21];
					if (input[8] == "true") { monster = true; }	else { monster = false; }
					if (input[10] == "true") { contin = true; }	else { contin = false; }
					if (input[11] == "true") { quickplay = true; }	else { quickplay = false; }
					if (input[12] == "true") { counter = true; }	else { counter = false; }
					if (input[13] == "true") { field = true; }	else { field = false; }
					if (input[14] == "true") { equip = true; }	else { equip = false; }
					if (input[17] == "true") { ritual = true; }	else { ritual = false; }
					if (input[18] == "true") { normal = true; }	else { normal = false; }

					Card newCard = new Card(name, attribute, type, cardType, text, crosslimit, rarity, synergies, atk, def, lvl, tierScore, limit, quantity, monster, contin, quickplay, counter, field, equip, ritual, normal);

					for (int p = 0; p < newCard.getQuantity(); p++)
					{
						Card newCard2 = new Card(name, attribute, type, cardType, text, crosslimit, rarity, synergies, atk, def, lvl, tierScore, limit, 1, monster, contin, quickplay, counter, field, equip, ritual, normal);
						allCards.add(newCard2); 
					}
					noOfCards = allCards.size(); if (noOfCards == 1) { i--; }

				}

				for (Card card : updates)
				{
					for (int p = 0; p < allCards.size(); p++)
					{
						boolean check = card.getName().equals(allCards.get(p).getName());
						boolean check2 = allCards.get(p).getText().equals("(Text)");
						if (check && check2)
						{
							allCards.get(p).setText((card.getText()));
						}
					}
				}
			}
			database.close();
		} catch (IOException e) { System.out.println("No file found! Nothing happened."); } // END Try block
	}
	static ArrayList<Card> readOtherDatabase()
	{
		FileInputStream database;
		ArrayList<Card> cardsNeedText = new ArrayList<Card>();
		try 
		{ 
			database = new FileInputStream("yugiohDatabase4.txt");
			BufferedReader databaseStream = new BufferedReader(new InputStreamReader(database));
			String line = null;
			String[] input = null;
			String name = null;
			String text = null;


			while (databaseStream.ready())
			{
				line = databaseStream.readLine();	
				input = line.split("~");
				name = input[1]; text = input[3];
				Card newCard = new Card(name, text);
				cardsNeedText.add(newCard);
			}

			database.close();
		} catch (IOException e) { System.out.println("No file found! Nothing happened.");  }

		return cardsNeedText;

	}

	// Returns how many cards exist in the pool
	static int cardCount(ArrayList<Card> pool)
	{
		int count = 0;
		for (int i = 0; i < pool.size(); i++)
		{
			count += pool.get(i).getQuantity();
		}

		return count;
	}

	// List copy 
	static ArrayList<Card> copyPool(ArrayList<Card> pool)
	{
		ArrayList<Card> temp = new ArrayList<>();
		for (Card card : pool)
		{
			Card tempCard = new Card(card);
			temp.add(tempCard);
		}
		return temp;
	}

	// List copy void return
	static void copyPoolVoid(ArrayList<Card> copyFrom, ArrayList<Card> copyOver)
	{
		copyOver.clear();
		for (Card card : copyFrom)
		{
			Card tempCard = new Card(card);
			copyOver.add(tempCard);
		}
	}

	// After you have drafted all your cards runs through and totals up how many of each card for nice output
	static void cardCounter(ArrayList<Card> drafted)
	{
		ArrayList<Card> temp = new ArrayList<Card>();
		ArrayList<Card> temp2 = new ArrayList<Card>();
		ArrayList<Card> temp3 = new ArrayList<Card>();
		Card tempCard = new Card();
		temp = copyPool(drafted);
		temp2 = copyPool(drafted);

		for (int i = 0; i < temp.size(); i++)
		{
			int counter = 0;
			for (int k = 0; k < temp2.size(); k++)
			{
				if (temp.get(i).getName().equals(temp2.get(k).getName()))
				{
					counter += temp2.get(k).getQuantity();
					temp2.remove(k);
					temp.get(i).setQuantity(counter);
					k = 0;
				}

			}
			tempCard = new Card(temp.get(i));
			tempCard.setQuantity(counter);
			temp3.add(tempCard);
		}


		copyPoolVoid(temp3, drafted);
	}

	// Returns how many of the given card exist within the given pool
	static int howManyCards(ArrayList<Card> pool, Card card)
	{
		int cardCount = 0;
		for (Card check : pool)
		{
			if (check.getName().equals(card.getName()))
			{
				cardCount++;
			}
		}

		return cardCount;
	}

	// These listMaker methods create lists to be used with some of the view menus
	// They return arrays with one of each copy of the cards from the given pool
	// listMaker does it for every card in the pool, ultimate only for ultimate rares and rarity for the your choice of rarity
	static ArrayList<Card> listMaker(ArrayList<Card> pool)
	{
		ArrayList<Card> temp = new ArrayList<Card>();
		boolean checker = true;
		for (int k = 0; k < pool.size(); k++)
		{
			checker = true;
			if (temp.size() == 0) { Card tempCard2 = new Card(pool.get(k)); temp.add(tempCard2); }
			else
			{
				for (int i = 0; i < temp.size(); i++)
				{
					if (pool.get(k).getName().equals(temp.get(i).getName()))
					{
						checker = false; i = temp.size();
					}
				}

				if (checker) 
				{ 
					int howMany = howManyCards(pool, pool.get(k));
					Card newCard = new Card(pool.get(k), howMany); 
					temp.add(newCard); 
				}
			}
		}

		return temp;
	}
	static ArrayList<Card> listMakerUltimate(ArrayList<Card> pool)
	{
		ArrayList<Card> temp = new ArrayList<Card>();
		boolean checker = true;
		for (int k = 0; k < pool.size(); k++)
		{
			checker = true;
			if (temp.size() == 0) 
			{ 
				Card dumbo = new Card("dumbo");
				temp.add(dumbo);
			}
			else
			{
				for (int i = 0; i < temp.size(); i++)
				{
					if (pool.get(k).getName().equals(temp.get(i).getName()))
					{
						checker = false; i = temp.size();
					}
				}

				if (checker) 
				{
					int howMany = howManyCards(pool, pool.get(k));				
					Card newCard = new Card(pool.get(k), howMany); 
					if (newCard.getRarity().equals("Ultimate Rare"))
					{
						temp.add(newCard); 
					}
				}
			}
		}


		temp.remove(0);
		return temp;
	}
	static ArrayList<Card> listMakerRarity(ArrayList<Card> pool, String rarity)
	{
		ArrayList<Card> temp = new ArrayList<Card>();
		boolean checker = true;
		for (int k = 0; k < pool.size(); k++)
		{
			checker = true;
			if (temp.size() == 0) 
			{ 
				Card dumbo = new Card("dumbo");
				temp.add(dumbo);
			}
			else
			{
				for (int i = 0; i < temp.size(); i++)
				{
					if (pool.get(k).getName().equals(temp.get(i).getName()))
					{
						checker = false; i = temp.size();
					}
				}

				if (checker) 
				{
					int howMany = howManyCards(pool, pool.get(k));
					Card newCard = new Card(pool.get(k), howMany); 
					if (newCard.getRarity().equals(rarity))
					{
						temp.add(newCard); 
					}
				}
			}
		}


		temp.remove(0);
		return temp;
	}

	// Prompts the user for number of players and then sets up the decks and pools necessary to allow that number of players to draft decks
	// Used with 'Random' pool fill option
	static int poolDeckInit(ArrayList<ArrayList<Card>> draftPools, ArrayList<ArrayList<Card>> draftDecks, int playerCount)
	{
		for (int i = 0; i < playerCount; i++) 
		{ 
			ArrayList<Card> deck = new ArrayList<Card>();	
			draftPools.add(deck); 
			draftDecks.add(deck); 
		}
		return playerCount;
	}
	static int fillAllPools(ArrayList<Card> allCards, ArrayList<ArrayList<Card>> decks, int noOfPlayers)
	{
		ArrayList<Card> sizeCheck = copyPool(allCards);
		for (int i = 0, j = 0; i < sizeCheck.size(); i++)
		{
			decks.get(j%noOfPlayers).add(randomCardRemove(allCards));
			decks.get(j%noOfPlayers).get(decks.get(j%noOfPlayers).size() - 1).setQuantity(1);
			j++;
			if ((j + 1) > noOfPlayers) { j = 0; }
		}

		int smallestDeck = decks.get((decks.size() - 1)).size();

		for (ArrayList<Card> deck : decks)
		{
			while (deck.size() > smallestDeck)
			{
				randomCardRemove(deck);
			}
		}

		return smallestDeck;

	}

	// Pulls a random card from a given pool and removes one from that pool (equal weight to all cards)
	static Card randomCardRemove(ArrayList<Card> allCards)
	{
		Random seed = new Random();
		int seedValue = seed.nextInt(allCards.size());
		Card selected = allCards.get(seedValue);
		int cardIndex = allCards.indexOf(selected);
		allCards.get(cardIndex).setQuantity(allCards.get(cardIndex).getQuantity() - 1);
		if (allCards.get(cardIndex).getQuantity() <= 0) { allCards.remove(cardIndex); }
		return selected;
	}

	// Used to remove cards from your pool once you have drafted a number of copies equal to the limit
	static ArrayList<Card> removeLimitedCards(ArrayList<Card> draftPool, ArrayList<Card> drafted)
	{
		ArrayList<Card> tempList = copyPool(draftPool);
		ArrayList<Card> tempList2 = new ArrayList<Card>();
		ArrayList<Card> tempList3 = copyPool(drafted);
		for (Card card : tempList3)
		{
			if (card.getQuantity() >= card.getLimit())
			{
				Card temp = new Card(card.getName());
				for (Card remove : tempList)
				{
					if (temp.getName().equals(remove.getName()))
					{
						tempList2.add(remove);
					}
				}
			}
		}

		tempList.removeAll(tempList2);
		return tempList;
	}

	static void removeLimitedCardsVoid(ArrayList<Card> draftPool, ArrayList<Card> drafted)
	{
		ArrayList<Card> tempList = copyPool(draftPool);
		ArrayList<Card> tempList2 = new ArrayList<Card>();
		ArrayList<Card> tempList3 = copyPool(drafted);
		for (Card card : tempList3)
		{
			if (card.getQuantity() >= card.getLimit())
			{
				Card temp = new Card(card.getName());
				for (Card remove : tempList)
				{
					if (temp.getName().equals(remove.getName()))
					{
						tempList2.add(remove);
						//System.out.println("Removed " + temp.getName() + " from your pool.");
					}
				}
			}
		}

		draftPool.removeAll(tempList2);
	}

	// Generates a list of 'random' picks from draftPool
	// Makes sure those cards exist in the pool, are not duplicates of each other and picking any of them should not break deck limits
	static ArrayList<Card> picks(ArrayList<Card> draftPool, ArrayList<Card> deck, int choices)
	{
		ArrayList<Card> picks = new ArrayList<Card>();
		Card temp = new Card();
		boolean rolling = true;
		String common = "Common";
		String rare = "Rare";
		String superR = "Super Rare";
		String ultra = "Ultra Rare";
		String ultimate = "Ultimate Rare";
		String roll = "";
		String seedValue = "";

		for (int i = 0; i < choices; i++)
		{
			rolling = true;
			while (rolling)
			{
				int seed = oneKDie();
				if ((seed < 690) && (howManyRarity(draftPool, common) > 0)) { rolling = rarityRoll(draftPool, common, deck, temp, roll, seedValue, picks, rolling, seed); roll = common; } 							// 69% chance at common				
				else if ((seed >= 690 && seed < 770) && (howManyRarity(draftPool, rare) > 0)) { rolling = rarityRoll(draftPool, rare, deck, temp, roll, seedValue, picks, rolling, seed); roll = rare; }			// 8% chance at rare
				else if ((seed >= 850 && seed < 890) && (howManyRarity(draftPool, superR) > 0)) { rolling = rarityRoll(draftPool, superR, deck, temp, roll, seedValue, picks, rolling, seed); roll = superR; }		// 4% chance at super rare
				else if ((seed >= 890 && seed < 910) && (howManyRarity(draftPool, ultra) > 0)) { rolling = rarityRoll(draftPool, ultra, deck, temp, roll, seedValue, picks, rolling, seed); roll = ultra; }		// 2% chance at ultra rare
				else if ((seed >= 990 && seed <= 1000) && (howManyRarity(draftPool, ultimate) > 0)) { rolling = rarityRoll(draftPool, ultimate, deck, temp, roll, seedValue, picks, rolling, seed); roll = ultimate; }	// 1% chance at ultimate rare
				// Backup
				else { rarityRollBackup(draftPool, common, deck, temp, roll, seedValue, picks, rolling, seed); roll = "Purely Random"; }
			}
		}
		return picks;
	}

	// Generates a list of 'random' picks from draftPool
	// Makes sure those cards exist in the pool, are not duplicates of each other and picking any of them should not break deck limits
	static void picksVoid(ArrayList<Card> draftPool, ArrayList<Card> deck, int choices, ArrayList<Card> threePicks)
		{
			ArrayList<Card> picks = new ArrayList<Card>();
			Card temp = new Card();
			boolean rolling = true;
			String common = "Common";
			String rare = "Rare";
			String superR = "Super Rare";
			String ultra = "Ultra Rare";
			String ultimate = "Ultimate Rare";
			String roll = "";
			String seedValue = "";

			for (int i = 0; i < choices; i++)
			{
				rolling = true;
				while (rolling)
				{
					int seed = oneKDie();
					if ((seed < 690) && (howManyRarity(draftPool, common) > 0)) { rolling = rarityRoll(draftPool, common, deck, temp, roll, seedValue, picks, rolling, seed); roll = common; } 							// 69% chance at common				
					else if ((seed >= 690 && seed < 770) && (howManyRarity(draftPool, rare) > 0)) { rolling = rarityRoll(draftPool, rare, deck, temp, roll, seedValue, picks, rolling, seed); roll = rare; }			// 8% chance at rare
					else if ((seed >= 850 && seed < 890) && (howManyRarity(draftPool, superR) > 0)) { rolling = rarityRoll(draftPool, superR, deck, temp, roll, seedValue, picks, rolling, seed); roll = superR; }		// 4% chance at super rare
					else if ((seed >= 890 && seed < 910) && (howManyRarity(draftPool, ultra) > 0)) { rolling = rarityRoll(draftPool, ultra, deck, temp, roll, seedValue, picks, rolling, seed); roll = ultra; }		// 2% chance at ultra rare
					else if ((seed >= 990 && seed <= 1000) && (howManyRarity(draftPool, ultimate) > 0)) { rolling = rarityRoll(draftPool, ultimate, deck, temp, roll, seedValue, picks, rolling, seed); roll = ultimate; }	// 1% chance at ultimate rare
					// Backup
					else { rarityRollBackup(draftPool, common, deck, temp, roll, seedValue, picks, rolling, seed); roll = "Purely Random"; }
				}
			}
			copyPoolVoid(picks, threePicks);
		}
	

	// Returns a number between 1-1000
	static int oneKDie()
	{
		Random seed = new Random();
		int seedValue = seed.nextInt(1000);
		return seedValue;
	}

	// Returns the number of cards of the given rarity that exist within the given pool
	static int howManyRarity(ArrayList<Card> pool, String rarity)
	{
		int counter = 0;
		for (Card card : pool)
		{
			if (card.getRarity().equals(rarity))
			{
				counter++;
			}
		}

		return counter;
	}

	// Rolls a random card from the draft pool with the passed rarity
	// Auto checks for deck limits and compares against other picks
	static boolean rarityRoll(ArrayList<Card> draftPool, String rarity, ArrayList<Card> deck, Card temp, String roll, String seedValue, ArrayList<Card> picks, boolean rolling, int seed)
	{
		boolean checker = rolling;
		if (isThereRandomRarity(draftPool, rarity))
		{
			temp = randomCardRarity(draftPool, rarity);
			if (limiter(deck, temp)) 
			{
				if (noDupes(picks, temp))
				{
					roll = rarity; seedValue = String.valueOf(seed); picks.add(temp); draftPool.remove(temp);
					checker = false;
				}
			}
		}

		return checker;
	}

	static boolean rarityRollBackup(ArrayList<Card> draftPool, String rarity, ArrayList<Card> deck, Card temp, String roll, String seedValue, ArrayList<Card> picks, boolean rolling, int seed)
	{
		boolean checker = rolling;
		if (isThereRandomRarity(draftPool, rarity))
		{
			temp = randomCard(draftPool);
			if (limiter(deck, temp)) 
			{
				if (noDupes(picks, temp))
				{
					roll = rarity; seedValue = String.valueOf(seed); picks.add(temp); checker = false;
				}
			}
		}

		return checker;
	}

	// Checks every card in the passed pool to see if any match the passed rarity
	// If there are no cards at all that match returns false, otherwise return true
	static boolean isThereRandomRarity(ArrayList<Card> pool, String rarity)
	{
		boolean checker = false;
		for (Card card : pool)
		{
			if (card.getRarity().equals(rarity)) { checker = true; }
		}
		return checker;
	}

	// Pulls a random card with the given rarity from the given pool
	static Card randomCardRarity(ArrayList<Card> pool, String rarity)
	{
		boolean looking = true;
		Card randomCard = new Card();
		while (looking)
		{
			randomCard = randomCard(pool);
			if (randomCard.getRarity().equals(rarity)) { looking = false; }
		}

		return randomCard;
	}

	// Pulls a random card from the given pool (equal weight to all cards)
	static Card randomCard(ArrayList<Card> allCards)
	{
		Random seed = new Random();
		int seedValue = seed.nextInt(allCards.size());
		Card selected = allCards.get(seedValue);
		return selected;
	}

	// Runs through your pool and checks to see that no deck limits are being broken
	// Returns true if the card could be added to your pool
	static boolean limiter(ArrayList<Card> drafted, Card card)
	{
		boolean checker = true;

		for (int i = 0; i < drafted.size(); i++)
		{
			if (card.getName().equals(drafted.get(i).getName()))
			{
				if (drafted.get(i).getQuantity() >= drafted.get(i).getLimit())
				//if (drafted.get(i).getQuantity() >= 3 || drafted.get(i).getQuantity() >= drafted.get(i).getLimit())
				{
					checker = false;
				}
			}
		}

		return checker;
	}
	
	// Checks to make sure all the choices presented are unique from one another
	static boolean noDupes(ArrayList<Card> picks, Card card)
	{
		boolean checker = true;
		if (picks.isEmpty() == false)
		{
			for (Card temp : picks)
			{
				if (card.getName().equals(temp.getName())) { checker = false; break; }
				else { checker = true; }
			}
		}

		return checker;
	}

	// Sets up all the ban>grouping listeners within the ban menu
	@SuppressWarnings("rawtypes")
	static void banGroupListenerInit(ArrayList<Card> allCards, JFrame DraftInit, SortingListModel banModel, JLabel lblCardCount, JLabel lblUniqueCards, 
			JMenuItem Monarchs, JMenuItem Fissure, JMenuItem TrapHole, JMenuItem Exodia, JMenuItem Water, JMenuItem Naturia, JMenuItem Toon, JMenuItem Draw,
			JMenuItem Ritual, JMenuItem Fusion, JMenuItem LowAtk, JMenuItem Limited, JMenuItem SemiLimited, JMenuItem HighAtk, JMenuItem LowLvl, JMenuItem HighLvl)
	{
		Monarchs.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getCrosslimit().equals("monarch")) 
					{				
						for (int bm = 0; bm < banModel.getSize(); bm++)
						{
							if (allCards.get(a).getName().equals(banModel.getCardAt(bm).getName())) 
							{
								checker = true; 
							}
						}

						if (checker == false) 
						{ 
							int howMany = howManyCards(allCards, allCards.get(a));
							Card temp = new Card(allCards.get(a), howMany); 
							banModel.addElement(temp); 
						}
						allCards.remove(a);
						a = 0;
						checker = false;
					}
				}

				Monarchs.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		Fissure.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getCrosslimit().equals("fissure")) 
					{
						for (int bm = 0; bm < banModel.getSize(); bm++)
						{
							if (allCards.get(a).getName().equals(banModel.getCardAt(bm).getName())) 
							{
								checker = true; 
							}
						}

						if (checker == false) 
						{ 
							int howMany = howManyCards(allCards, allCards.get(a));
							Card temp = new Card(allCards.get(a), howMany); 
							banModel.addElement(temp); 
						}
						allCards.remove(a);
						a = 0;
						checker = false;
					}
				}

				Fissure.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		TrapHole.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getCrosslimit().equals("trap hole")) 
					{				
						for (int bm = 0; bm < banModel.getSize(); bm++)
						{
							if (allCards.get(a).getName().equals(banModel.getCardAt(bm).getName())) 
							{
								checker = true; 
							}
						}

						if (checker == false) 
						{ 
							int howMany = howManyCards(allCards, allCards.get(a));
							Card temp = new Card(allCards.get(a), howMany); 
							banModel.addElement(temp); 
						}
						allCards.remove(a);
						a = 0;
						checker = false;
					}
				}

				TrapHole.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		Exodia.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getSynergies().contains("exodia")) 
					{				
						for (int bm = 0; bm < banModel.getSize(); bm++)
						{
							if (allCards.get(a).getName().equals(banModel.getCardAt(bm).getName())) 
							{
								checker = true; 
							}
						}

						if (checker == false) 
						{ 
							int howMany = howManyCards(allCards, allCards.get(a));
							Card temp = new Card(allCards.get(a), howMany); 
							banModel.addElement(temp); 
						}
						allCards.remove(a);
						a = 0;
						checker = false;
					}
				}

				Exodia.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		Naturia.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getSynergies().contains("naturia")) 
					{				
						for (int bm = 0; bm < banModel.getSize(); bm++)
						{
							if (allCards.get(a).getName().equals(banModel.getCardAt(bm).getName())) 
							{
								checker = true; 
							}
						}

						if (checker == false) 
						{ 
							int howMany = howManyCards(allCards, allCards.get(a));
							Card temp = new Card(allCards.get(a), howMany); 
							banModel.addElement(temp); 
						}
						allCards.remove(a);
						a = 0;
						checker = false;
					}
				}

				Naturia.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		Toon.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getSynergies().contains("toon")) 
					{				
						for (int bm = 0; bm < banModel.getSize(); bm++)
						{
							if (allCards.get(a).getName().equals(banModel.getCardAt(bm).getName())) 
							{
								checker = true; 
							}
						}

						if (checker == false) 
						{ 
							int howMany = howManyCards(allCards, allCards.get(a));
							Card temp = new Card(allCards.get(a), howMany); 
							banModel.addElement(temp); 
						}
						allCards.remove(a);
						a = 0;
						checker = false;
					}
				}

				Toon.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		Draw.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getSynergies().contains("draw")) 
					{				
						for (int bm = 0; bm < banModel.getSize(); bm++)
						{
							if (allCards.get(a).getName().equals(banModel.getCardAt(bm).getName())) 
							{
								checker = true; 
							}
						}

						if (checker == false) 
						{ 
							int howMany = howManyCards(allCards, allCards.get(a));
							Card temp = new Card(allCards.get(a), howMany); 
							banModel.addElement(temp); 
						}
						allCards.remove(a);
						a = 0;
						checker = false;
					}
				}

				Draw.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		Ritual.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getSynergies().contains("ritual")) 
					{				
						for (int bm = 0; bm < banModel.getSize(); bm++)
						{
							if (allCards.get(a).getName().equals(banModel.getCardAt(bm).getName())) 
							{
								checker = true; 
							}
						}

						if (checker == false) 
						{ 
							int howMany = howManyCards(allCards, allCards.get(a));
							Card temp = new Card(allCards.get(a), howMany); 
							banModel.addElement(temp); 
						}
						allCards.remove(a);
						a = 0;
						checker = false;
					}
				}

				Ritual.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		Fusion.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getSynergies().contains("fusion")) 
					{				
						for (int bm = 0; bm < banModel.getSize(); bm++)
						{
							if (allCards.get(a).getName().equals(banModel.getCardAt(bm).getName())) 
							{
								checker = true; 
							}
						}

						if (checker == false) 
						{ 
							int howMany = howManyCards(allCards, allCards.get(a));
							Card temp = new Card(allCards.get(a), howMany); 
							banModel.addElement(temp); 
						}
						allCards.remove(a);
						a = 0;
						checker = false;
					}
				}

				Fusion.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		Limited.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getLimit() == 1) 
					{				
						for (int bm = 0; bm < banModel.getSize(); bm++)
						{
							if (allCards.get(a).getName().equals(banModel.getCardAt(bm).getName())) 
							{
								checker = true; 
							}
						}

						if (checker == false) 
						{ 
							int howMany = howManyCards(allCards, allCards.get(a));
							Card temp = new Card(allCards.get(a), howMany); 
							banModel.addElement(temp); 
						}
						allCards.remove(a);
						a = 0;
						checker = false;
					}
				}

				Limited.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		SemiLimited.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getLimit() == 2) 
					{				
						for (int bm = 0; bm < banModel.getSize(); bm++)
						{
							if (allCards.get(a).getName().equals(banModel.getCardAt(bm).getName())) 
							{
								checker = true; 
							}
						}

						if (checker == false) 
						{ 
							int howMany = howManyCards(allCards, allCards.get(a));
							Card temp = new Card(allCards.get(a), howMany); 
							banModel.addElement(temp); 
						}
						allCards.remove(a);
						a = 0;
						checker = false;
					}
				}

				SemiLimited.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		Water.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getAttribute().equals("Water")) 
					{				
						for (int bm = 0; bm < banModel.getSize(); bm++)
						{
							if (allCards.get(a).getName().equals(banModel.getCardAt(bm).getName())) 
							{
								checker = true; 
							}
						}

						if (checker == false) 
						{ 
							int howMany = howManyCards(allCards, allCards.get(a));
							Card temp = new Card(allCards.get(a), howMany); 
							banModel.addElement(temp); 
						}
						allCards.remove(a);
						a = 0;
						checker = false;
					}
				}

				Water.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		LowAtk.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if ((allCards.get(a).getAtk() < 1500) && (allCards.get(a).getAtk() != 0)) 
					{				
						for (int bm = 0; bm < banModel.getSize(); bm++)
						{
							if (allCards.get(a).getName().equals(banModel.getCardAt(bm).getName())) 
							{
								checker = true; 
							}
						}

						if (checker == false) 
						{ 
							int howMany = howManyCards(allCards, allCards.get(a));
							Card temp = new Card(allCards.get(a), howMany); 
							banModel.addElement(temp); 
						}
						allCards.remove(a);
						a = 0;
						checker = false;
					}
				}

				LowAtk.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				DraftInit.revalidate(); DraftInit.repaint(); 
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
			}
		});

		HighAtk.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if ((allCards.get(a).getAtk() > 2600) && (allCards.get(a).getAtk() != 0))  
					{				
						for (int bm = 0; bm < banModel.getSize(); bm++)
						{
							if (allCards.get(a).getName().equals(banModel.getCardAt(bm).getName())) 
							{
								checker = true; 
							}
						}

						if (checker == false) 
						{ 
							int howMany = howManyCards(allCards, allCards.get(a));
							Card temp = new Card(allCards.get(a), howMany); 
							banModel.addElement(temp); 
						}
						allCards.remove(a);
						a = 0;
						checker = false;
					}
				}

				HighAtk.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		LowLvl.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if ((allCards.get(a).getLvl() < 3) && (allCards.get(a).getLvl() != 0))  
					{				
						for (int bm = 0; bm < banModel.getSize(); bm++)
						{
							if (allCards.get(a).getName().equals(banModel.getCardAt(bm).getName())) 
							{
								checker = true; 
							}
						}

						if (checker == false) 
						{ 
							int howMany = howManyCards(allCards, allCards.get(a));
							Card temp = new Card(allCards.get(a), howMany); 
							banModel.addElement(temp); 
						}
						allCards.remove(a);
						a = 0;
						checker = false;
					}
				}

				LowLvl.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		HighLvl.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if ((allCards.get(a).getLvl() > 7) && (allCards.get(a).getLvl() != 0))
					{				
						for (int bm = 0; bm < banModel.getSize(); bm++)
						{
							if (allCards.get(a).getName().equals(banModel.getCardAt(bm).getName())) 
							{
								checker = true; 
							}
						}

						if (checker == false) 
						{ 
							int howMany = howManyCards(allCards, allCards.get(a));
							Card temp = new Card(allCards.get(a), howMany); 
							banModel.addElement(temp); 
						}
						allCards.remove(a);
						a = 0;
						checker = false;
					}
				}

				HighLvl.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});
	}

	// Setup the view database listeners the first time when the program opens
	public static void viewDatabaseListenerInit(JMenu mnViewDatabase, JMenuItem mntmViewAllCards, JMenuItem mntmNewMenuItem, ArrayList<Card> allCards, 
			JMenuItem mntmUltraRares, JMenuItem mntmSuperRares, JMenuItem mntmRares, JMenuItem mntmCommon, JMenuItem cardViewer)
	{
		mntmViewAllCards.addActionListener(new ActionListener() 
		{
			@SuppressWarnings("rawtypes")
			public void actionPerformed(ActionEvent arg0) 
			{

				JFrame cardView = new JFrame();	
				cardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
				cardView.setBounds(100, 100, 496, 443);
				cardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				ArrayList<Card >allCardsNoDupes = listMaker(allCards);
				JList list = new JList(allCardsNoDupes.toArray());
				KeyListener keyListener = new KeyListener()
				{
					public void keyPressed(KeyEvent e)
					{
						if (e.getKeyCode() == KeyEvent.VK_ENTER)
						{
							Card selectedCard = (Card) list.getSelectedValue();
							JFrame singleCardView = new JFrame();
							JPanel panel = new JPanel();
							ImageLabel label = new ImageLabel(new ImageIcon("src/images/" + selectedCard.getName() + ".png"));
							label.setLocation(29, 37);
							panel.add(label);
							singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
							singleCardView.setBounds(100, 100, 496, 443);
							singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
							singleCardView.getContentPane().add(panel);
							singleCardView.pack();
							singleCardView.setVisible(true);
						}
					}

					@Override
					public void keyReleased(KeyEvent arg0) {


					}

					@Override
					public void keyTyped(KeyEvent arg0) {


					}
				};
				list.addKeyListener(keyListener);
				MouseListener mouseListener = new MouseAdapter() 
				{
					public void mouseClicked(MouseEvent e) 
					{
						if (e.getClickCount() == 2) 
						{    		
							Card selectedCard = (Card) list.getSelectedValue();
							//JFrame singleCardView;
							JFrame singleCardView = new JFrame();
							JPanel panel = new JPanel();
							ImageLabel label = new ImageLabel(new ImageIcon("src/images/" + selectedCard.getName() + ".png"));
							label.setLocation(29, 37);
							panel.add(label);
							singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
							singleCardView.setBounds(100, 100, 496, 443);
							singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
							singleCardView.getContentPane().add(panel);
							singleCardView.pack();
							singleCardView.setVisible(true);
						}
					}
				};
				list.addMouseListener(mouseListener);
				JScrollPane scrollPane = new JScrollPane();
				scrollPane.setViewportView(list);
				cardView.getContentPane().add(scrollPane);
				cardView.pack();
				cardView.setVisible(true);
			}
		});
		mnViewDatabase.add(mntmViewAllCards);


		mntmNewMenuItem.addActionListener(new ActionListener() 
		{
			@SuppressWarnings("rawtypes")
			public void actionPerformed(ActionEvent arg0) 
			{
				//JFrame cardView;
				JFrame cardView = new JFrame();	
				cardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
				cardView.setBounds(100, 100, 496, 443);
				cardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				ArrayList<Card >allCardsNoDupes = listMakerRarity(allCards, "Ultimate Rare");
				JList list = new JList(allCardsNoDupes.toArray());
				KeyListener keyListener = new KeyListener()
				{
					public void keyPressed(KeyEvent e)
					{
						if (e.getKeyCode() == KeyEvent.VK_ENTER)
						{
							Card selectedCard = (Card) list.getSelectedValue();
							//JFrame singleCardView;
							JFrame singleCardView = new JFrame();
							JPanel panel = new JPanel();
							ImageLabel label = new ImageLabel(new ImageIcon("src/images/" + selectedCard.getName() + ".png"));
							label.setLocation(29, 37);
							panel.add(label);
							singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
							singleCardView.setBounds(100, 100, 496, 443);
							singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
							singleCardView.getContentPane().add(panel);
							singleCardView.pack();
							singleCardView.setVisible(true);
						}
					}

					@Override
					public void keyReleased(KeyEvent arg0) {


					}

					@Override
					public void keyTyped(KeyEvent arg0) {


					}
				};
				list.addKeyListener(keyListener);
				MouseListener mouseListener = new MouseAdapter() 
				{
					public void mouseClicked(MouseEvent e) 
					{
						if (e.getClickCount() == 2) 
						{    		
							Card selectedCard = (Card) list.getSelectedValue();
							//JFrame singleCardView;
							JFrame singleCardView = new JFrame();
							JPanel panel = new JPanel();
							ImageLabel label = new ImageLabel(new ImageIcon("src/images/" + selectedCard.getName() + ".png"));
							label.setLocation(29, 37);
							panel.add(label);
							singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
							singleCardView.setBounds(100, 100, 496, 443);
							singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
							singleCardView.getContentPane().add(panel);
							singleCardView.pack();
							singleCardView.setVisible(true);
						}
					}
				};
				list.addMouseListener(mouseListener);
				JScrollPane scrollPane = new JScrollPane();
				scrollPane.setViewportView(list);
				cardView.getContentPane().add(scrollPane);
				cardView.pack();
				cardView.setVisible(true);
			}
		});
		mnViewDatabase.add(mntmNewMenuItem);




		mntmUltraRares.addActionListener(new ActionListener() 
		{
			@SuppressWarnings("rawtypes")
			public void actionPerformed(ActionEvent arg0) 
			{
				//JFrame cardView;
				JFrame cardView = new JFrame();	
				cardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
				cardView.setBounds(100, 100, 496, 443);
				cardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				ArrayList<Card >allCardsNoDupes = listMakerRarity(allCards, "Ultra Rare");
				JList list = new JList(allCardsNoDupes.toArray());
				KeyListener keyListener = new KeyListener()
				{
					public void keyPressed(KeyEvent e)
					{
						if (e.getKeyCode() == KeyEvent.VK_ENTER)
						{
							Card selectedCard = (Card) list.getSelectedValue();
							//JFrame singleCardView;
							JFrame singleCardView = new JFrame();
							JPanel panel = new JPanel();
							ImageLabel label = new ImageLabel(new ImageIcon("src/images/" + selectedCard.getName() + ".png"));
							label.setLocation(29, 37);
							panel.add(label);
							singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
							singleCardView.setBounds(100, 100, 496, 443);
							singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
							singleCardView.getContentPane().add(panel);
							singleCardView.pack();
							singleCardView.setVisible(true);
						}
					}

					@Override
					public void keyReleased(KeyEvent arg0) {


					}

					@Override
					public void keyTyped(KeyEvent arg0) {


					}
				};
				list.addKeyListener(keyListener);
				MouseListener mouseListener = new MouseAdapter() 
				{
					public void mouseClicked(MouseEvent e) 
					{
						if (e.getClickCount() == 2) 
						{    		
							Card selectedCard = (Card) list.getSelectedValue();
							//JFrame singleCardView;
							JFrame singleCardView = new JFrame();
							JPanel panel = new JPanel();
							ImageLabel label = new ImageLabel(new ImageIcon("src/images/" + selectedCard.getName() + ".png"));
							label.setLocation(29, 37);
							panel.add(label);
							singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
							singleCardView.setBounds(100, 100, 496, 443);
							singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
							singleCardView.getContentPane().add(panel);
							singleCardView.pack();
							singleCardView.setVisible(true);
						}
					}
				};
				list.addMouseListener(mouseListener);
				JScrollPane scrollPane = new JScrollPane();
				scrollPane.setViewportView(list);
				cardView.getContentPane().add(scrollPane);
				cardView.pack();
				cardView.setVisible(true);
			}
		});
		mnViewDatabase.add(mntmUltraRares);


		mntmSuperRares.addActionListener(new ActionListener() 
		{
			@SuppressWarnings("rawtypes")
			public void actionPerformed(ActionEvent arg0) 
			{
				//	JFrame cardView;
				JFrame cardView = new JFrame();	
				cardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
				cardView.setBounds(100, 100, 496, 443);
				cardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				ArrayList<Card >allCardsNoDupes = listMakerRarity(allCards, "Super Rare");
				JList list = new JList(allCardsNoDupes.toArray());
				KeyListener keyListener = new KeyListener()
				{
					public void keyPressed(KeyEvent e)
					{
						if (e.getKeyCode() == KeyEvent.VK_ENTER)
						{
							Card selectedCard = (Card) list.getSelectedValue();
							//JFrame singleCardView;
							JFrame singleCardView = new JFrame();
							JPanel panel = new JPanel();
							ImageLabel label = new ImageLabel(new ImageIcon("src/images/" + selectedCard.getName() + ".png"));
							label.setLocation(29, 37);
							panel.add(label);
							singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
							singleCardView.setBounds(100, 100, 496, 443);
							singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
							singleCardView.getContentPane().add(panel);
							singleCardView.pack();
							singleCardView.setVisible(true);
						}
					}

					@Override
					public void keyReleased(KeyEvent arg0) {


					}

					@Override
					public void keyTyped(KeyEvent arg0) {


					}
				};
				list.addKeyListener(keyListener);
				MouseListener mouseListener = new MouseAdapter() 
				{
					public void mouseClicked(MouseEvent e) 
					{
						if (e.getClickCount() == 2) 
						{    		
							Card selectedCard = (Card) list.getSelectedValue();
							//JFrame singleCardView;
							JFrame singleCardView = new JFrame();
							JPanel panel = new JPanel();
							ImageLabel label = new ImageLabel(new ImageIcon("src/images/" + selectedCard.getName() + ".png"));
							label.setLocation(29, 37);
							panel.add(label);
							singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
							singleCardView.setBounds(100, 100, 496, 443);
							singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
							singleCardView.getContentPane().add(panel);
							singleCardView.pack();
							singleCardView.setVisible(true);
						}
					}
				};
				list.addMouseListener(mouseListener);
				JScrollPane scrollPane = new JScrollPane();
				scrollPane.setViewportView(list);
				cardView.getContentPane().add(scrollPane);
				cardView.pack();
				cardView.setVisible(true);				
			}
		});
		mnViewDatabase.add(mntmSuperRares);



		mntmRares.addActionListener(new ActionListener() 
		{
			@SuppressWarnings("rawtypes")
			public void actionPerformed(ActionEvent arg0) 
			{
				//JFrame cardView;
				JFrame cardView = new JFrame();	
				cardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
				cardView.setBounds(100, 100, 496, 443);
				cardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				ArrayList<Card >allCardsNoDupes = listMakerRarity(allCards, "Rare");
				JList list = new JList(allCardsNoDupes.toArray());
				KeyListener keyListener = new KeyListener()
				{
					public void keyPressed(KeyEvent e)
					{
						if (e.getKeyCode() == KeyEvent.VK_ENTER)
						{
							Card selectedCard = (Card) list.getSelectedValue();
							//JFrame singleCardView;
							JFrame singleCardView = new JFrame();
							JPanel panel = new JPanel();
							ImageLabel label = new ImageLabel(new ImageIcon("src/images/" + selectedCard.getName() + ".png"));
							label.setLocation(29, 37);
							panel.add(label);
							singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
							singleCardView.setBounds(100, 100, 496, 443);
							singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
							singleCardView.getContentPane().add(panel);
							singleCardView.pack();
							singleCardView.setVisible(true);
						}
					}

					@Override
					public void keyReleased(KeyEvent arg0) {


					}

					@Override
					public void keyTyped(KeyEvent arg0) {


					}
				};
				list.addKeyListener(keyListener);
				MouseListener mouseListener = new MouseAdapter() 
				{
					public void mouseClicked(MouseEvent e) 
					{
						if (e.getClickCount() == 2) 
						{    		
							Card selectedCard = (Card) list.getSelectedValue();
							//JFrame singleCardView;
							JFrame singleCardView = new JFrame();
							JPanel panel = new JPanel();
							ImageLabel label = new ImageLabel(new ImageIcon("src/images/" + selectedCard.getName() + ".png"));
							label.setLocation(29, 37);
							panel.add(label);
							singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
							singleCardView.setBounds(100, 100, 496, 443);
							singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
							singleCardView.getContentPane().add(panel);
							singleCardView.pack();
							singleCardView.setVisible(true);
						}
					}
				};
				list.addMouseListener(mouseListener);
				JScrollPane scrollPane = new JScrollPane();
				scrollPane.setViewportView(list);
				cardView.getContentPane().add(scrollPane);
				cardView.pack();
				cardView.setVisible(true);
			}
		});
		mnViewDatabase.add(mntmRares);


		mntmCommon.addActionListener(new ActionListener() 
		{
			@SuppressWarnings("rawtypes")
			public void actionPerformed(ActionEvent arg0) 
			{
				//JFrame cardView;
				JFrame cardView = new JFrame();	
				cardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
				cardView.setBounds(100, 100, 496, 443);
				cardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				ArrayList<Card >allCardsNoDupes = listMakerRarity(allCards, "Common");
				JList list = new JList(allCardsNoDupes.toArray());
				KeyListener keyListener = new KeyListener()
				{
					public void keyPressed(KeyEvent e)
					{
						if (e.getKeyCode() == KeyEvent.VK_ENTER)
						{
							Card selectedCard = (Card) list.getSelectedValue();
							//JFrame singleCardView;
							JFrame singleCardView = new JFrame();
							JPanel panel = new JPanel();
							ImageLabel label = new ImageLabel(new ImageIcon("src/images/" + selectedCard.getName() + ".png"));
							label.setLocation(29, 37);
							panel.add(label);
							singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
							singleCardView.setBounds(100, 100, 496, 443);
							singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
							singleCardView.getContentPane().add(panel);
							singleCardView.pack();
							singleCardView.setVisible(true);
						}
					}

					@Override
					public void keyReleased(KeyEvent arg0) {


					}

					@Override
					public void keyTyped(KeyEvent arg0) {


					}
				};
				list.addKeyListener(keyListener);
				MouseListener mouseListener = new MouseAdapter() 
				{
					public void mouseClicked(MouseEvent e) 
					{
						if (e.getClickCount() == 2) 
						{    		
							Card selectedCard = (Card) list.getSelectedValue();

							JFrame singleCardView = new JFrame();
							JPanel panel = new JPanel();
							ImageLabel label = new ImageLabel(new ImageIcon("src/images/" + selectedCard.getName() + ".png"));
							label.setLocation(29, 37);
							panel.add(label);
							singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
							singleCardView.setBounds(100, 100, 496, 443);
							singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
							singleCardView.getContentPane().add(panel);
							singleCardView.pack();
							singleCardView.setVisible(true);
						}
					}
				};
				list.addMouseListener(mouseListener);
				JScrollPane scrollPane = new JScrollPane();
				scrollPane.setViewportView(list);
				cardView.getContentPane().add(scrollPane);
				cardView.pack();
				cardView.setVisible(true);
			}
		});
		mnViewDatabase.add(mntmCommon);


		cardViewer.addActionListener(new ActionListener() 
		{
			@SuppressWarnings("rawtypes")
			public void actionPerformed(ActionEvent arg0) 
			{
				//JFrame cardView;
				JFrame cardView = new JFrame();	
				cardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
				cardView.setBounds(100, 100, 496, 443);
				cardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				ArrayList<Card >allCardsNoDupes = listMakerRarity(allCards, "Common");
				JList list = new JList(allCardsNoDupes.toArray());
				Card selectedCard = allCardsNoDupes.get(0);
				JFrame singleCardView = new JFrame();
				JPanel panel = new JPanel();
				JButton last = new JButton("Previous");
				JButton next = new JButton("Next");

				last.addActionListener(new ActionListener() 
				{
					public void actionPerformed(ActionEvent arg0) 
					{

					}

				});

				next.addActionListener(new ActionListener() 
				{
					public void actionPerformed(ActionEvent arg0) 
					{

					}

				});

				ImageLabel label = new ImageLabel(new ImageIcon("src/images/" + selectedCard.getName() + ".png"));
				label.setLocation(29, 37);
				panel.add(last);
				panel.add(label);
				panel.add(next);
				last.setEnabled(false);
				singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
				singleCardView.setBounds(100, 100, 496, 443);
				singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				singleCardView.getContentPane().add(panel);
				singleCardView.pack();
				singleCardView.setVisible(true);
			}
		});
		mnViewDatabase.add(cardViewer);
	}

	// Setup the view database listeners the second time once the draft starts
	public static void viewDatabaseListenerReinit(JMenu mnViewDatabase, ArrayList<Card> draftAllCards, boolean urItem, boolean ulrItem, boolean srItem, boolean rItem, boolean cItem)
	{
		// Database Viewer
		JMenuItem mntmViewAllCards = new JMenuItem("View all cards");
		mntmViewAllCards.addActionListener(new ActionListener() 
		{
			@SuppressWarnings("rawtypes")
			public void actionPerformed(ActionEvent arg0) 
			{

				JFrame cardView = new JFrame();	
				cardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
				cardView.setBounds(100, 100, 496, 443);
				cardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				ArrayList<Card >allCardsNoDupes = listMaker(draftAllCards);
				JList list = new JList(allCardsNoDupes.toArray());
				KeyListener keyListener = new KeyListener()
				{
					public void keyPressed(KeyEvent e)
					{
						if (e.getKeyCode() == KeyEvent.VK_ENTER)
						{
							Card selectedCard = (Card) list.getSelectedValue();
							JFrame singleCardView = new JFrame();
							JPanel panel = new JPanel();
							ImageLabel label = new ImageLabel(new ImageIcon("src/images/" + selectedCard.getName() + ".png"));
							label.setLocation(29, 37);
							panel.add(label);
							singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
							singleCardView.setBounds(100, 100, 496, 443);
							singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
							singleCardView.getContentPane().add(panel);
							singleCardView.pack();
							singleCardView.setVisible(true);
						}
					}

					@Override
					public void keyReleased(KeyEvent arg0) {


					}

					@Override
					public void keyTyped(KeyEvent arg0) {


					}
				};
				list.addKeyListener(keyListener);
				MouseListener mouseListener = new MouseAdapter() 
				{
					public void mouseClicked(MouseEvent e) 
					{
						if (e.getClickCount() == 2) 
						{    		
							Card selectedCard = (Card) list.getSelectedValue();
							JFrame singleCardView = new JFrame();
							JPanel panel = new JPanel();
							ImageLabel label = new ImageLabel(new ImageIcon("src/images/" + selectedCard.getName() + ".png"));
							label.setLocation(29, 37);
							panel.add(label);
							singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
							singleCardView.setBounds(100, 100, 496, 443);
							singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
							singleCardView.getContentPane().add(panel);
							singleCardView.pack();
							singleCardView.setVisible(true);
						}
					}
				};
				list.addMouseListener(mouseListener);
				JScrollPane scrollPane = new JScrollPane();
				scrollPane.setViewportView(list);
				cardView.getContentPane().add(scrollPane);
				cardView.pack();
				cardView.setVisible(true);
			}
		});
		mnViewDatabase.add(mntmViewAllCards);

		JMenuItem mntmNewMenuItem = new JMenuItem("Ultimate Rare");
		mntmNewMenuItem.addActionListener(new ActionListener() 
		{
			@SuppressWarnings("rawtypes")
			public void actionPerformed(ActionEvent arg0) 
			{
				//JFrame cardView;
				JFrame cardView = new JFrame();	
				cardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
				cardView.setBounds(100, 100, 496, 443);
				cardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				ArrayList<Card >allCardsNoDupes = listMakerRarity(draftAllCards, "Ultimate Rare");
				JList list = new JList(allCardsNoDupes.toArray());
				KeyListener keyListener = new KeyListener()
				{
					public void keyPressed(KeyEvent e)
					{
						if (e.getKeyCode() == KeyEvent.VK_ENTER)
						{
							Card selectedCard = (Card) list.getSelectedValue();
							//JFrame singleCardView;
							JFrame singleCardView = new JFrame();
							JPanel panel = new JPanel();
							ImageLabel label = new ImageLabel(new ImageIcon("src/images/" + selectedCard.getName() + ".png"));
							label.setLocation(29, 37);
							panel.add(label);
							singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
							singleCardView.setBounds(100, 100, 496, 443);
							singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
							singleCardView.getContentPane().add(panel);
							singleCardView.pack();
							singleCardView.setVisible(true);
						}
					}

					@Override
					public void keyReleased(KeyEvent arg0) {


					}

					@Override
					public void keyTyped(KeyEvent arg0) {


					}
				};
				list.addKeyListener(keyListener);
				MouseListener mouseListener = new MouseAdapter() 
				{
					public void mouseClicked(MouseEvent e) 
					{
						if (e.getClickCount() == 2) 
						{    		
							Card selectedCard = (Card) list.getSelectedValue();
							JFrame singleCardView = new JFrame();
							JPanel panel = new JPanel();
							ImageLabel label = new ImageLabel(new ImageIcon("src/images/" + selectedCard.getName() + ".png"));
							label.setLocation(29, 37);
							panel.add(label);
							singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
							singleCardView.setBounds(100, 100, 496, 443);
							singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
							singleCardView.getContentPane().add(panel);
							singleCardView.pack();
							singleCardView.setVisible(true);
						}
					}
				};
				list.addMouseListener(mouseListener);
				JScrollPane scrollPane = new JScrollPane();
				scrollPane.setViewportView(list);
				cardView.getContentPane().add(scrollPane);
				cardView.pack();
				cardView.setVisible(true);
			}
		});
		mnViewDatabase.add(mntmNewMenuItem);
		mntmNewMenuItem.setEnabled(urItem);



		JMenuItem mntmUltraRares = new JMenuItem("Ultra Rare");
		mntmUltraRares.addActionListener(new ActionListener() 
		{
			@SuppressWarnings("rawtypes")
			public void actionPerformed(ActionEvent arg0) 
			{
				JFrame cardView = new JFrame();	
				cardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
				cardView.setBounds(100, 100, 496, 443);
				cardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				ArrayList<Card >allCardsNoDupes = listMakerRarity(draftAllCards, "Ultra Rare");
				JList list = new JList(allCardsNoDupes.toArray());
				KeyListener keyListener = new KeyListener()
				{
					public void keyPressed(KeyEvent e)
					{
						if (e.getKeyCode() == KeyEvent.VK_ENTER)
						{
							Card selectedCard = (Card) list.getSelectedValue();
							JFrame singleCardView = new JFrame();
							JPanel panel = new JPanel();
							ImageLabel label = new ImageLabel(new ImageIcon("src/images/" + selectedCard.getName() + ".png"));
							label.setLocation(29, 37);
							panel.add(label);
							singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
							singleCardView.setBounds(100, 100, 496, 443);
							singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
							singleCardView.getContentPane().add(panel);
							singleCardView.pack();
							singleCardView.setVisible(true);
						}
					}

					@Override
					public void keyReleased(KeyEvent arg0) {


					}

					@Override
					public void keyTyped(KeyEvent arg0) {


					}
				};
				list.addKeyListener(keyListener);
				MouseListener mouseListener = new MouseAdapter() 
				{
					public void mouseClicked(MouseEvent e) 
					{
						if (e.getClickCount() == 2) 
						{    		
							Card selectedCard = (Card) list.getSelectedValue();
							JFrame singleCardView = new JFrame();
							JPanel panel = new JPanel();
							ImageLabel label = new ImageLabel(new ImageIcon("src/images/" + selectedCard.getName() + ".png"));
							label.setLocation(29, 37);
							panel.add(label);
							singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
							singleCardView.setBounds(100, 100, 496, 443);
							singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
							singleCardView.getContentPane().add(panel);
							singleCardView.pack();
							singleCardView.setVisible(true);
						}
					}
				};
				list.addMouseListener(mouseListener);
				JScrollPane scrollPane = new JScrollPane();
				scrollPane.setViewportView(list);
				cardView.getContentPane().add(scrollPane);
				cardView.pack();
				cardView.setVisible(true);
			}
		});
		mnViewDatabase.add(mntmUltraRares);
		mntmUltraRares.setEnabled(ulrItem);

		JMenuItem mntmSuperRares = new JMenuItem("Super Rare");
		mntmSuperRares.addActionListener(new ActionListener() 
		{
			@SuppressWarnings("rawtypes")
			public void actionPerformed(ActionEvent arg0) 
			{
				JFrame cardView = new JFrame();	
				cardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
				cardView.setBounds(100, 100, 496, 443);
				cardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				ArrayList<Card >allCardsNoDupes = listMakerRarity(draftAllCards, "Super Rare");
				JList list = new JList(allCardsNoDupes.toArray());
				KeyListener keyListener = new KeyListener()
				{
					public void keyPressed(KeyEvent e)
					{
						if (e.getKeyCode() == KeyEvent.VK_ENTER)
						{
							Card selectedCard = (Card) list.getSelectedValue();
							JFrame singleCardView = new JFrame();
							JPanel panel = new JPanel();
							ImageLabel label = new ImageLabel(new ImageIcon("src/images/" + selectedCard.getName() + ".png"));
							label.setLocation(29, 37);
							panel.add(label);
							singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
							singleCardView.setBounds(100, 100, 496, 443);
							singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
							singleCardView.getContentPane().add(panel);
							singleCardView.pack();
							singleCardView.setVisible(true);
						}
					}

					@Override
					public void keyReleased(KeyEvent arg0) {


					}

					@Override
					public void keyTyped(KeyEvent arg0) {


					}
				};
				list.addKeyListener(keyListener);
				MouseListener mouseListener = new MouseAdapter() 
				{
					public void mouseClicked(MouseEvent e) 
					{
						if (e.getClickCount() == 2) 
						{    		
							Card selectedCard = (Card) list.getSelectedValue();
							JFrame singleCardView = new JFrame();
							JPanel panel = new JPanel();
							ImageLabel label = new ImageLabel(new ImageIcon("src/images/" + selectedCard.getName() + ".png"));
							label.setLocation(29, 37);
							panel.add(label);
							singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
							singleCardView.setBounds(100, 100, 496, 443);
							singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
							singleCardView.getContentPane().add(panel);
							singleCardView.pack();
							singleCardView.setVisible(true);
						}
					}
				};
				list.addMouseListener(mouseListener);
				JScrollPane scrollPane = new JScrollPane();
				scrollPane.setViewportView(list);
				cardView.getContentPane().add(scrollPane);
				cardView.pack();
				cardView.setVisible(true);				
			}
		});
		mnViewDatabase.add(mntmSuperRares);
		mntmSuperRares.setEnabled(srItem);

		JMenuItem mntmRares = new JMenuItem("Rare");
		mntmRares.addActionListener(new ActionListener() 
		{
			@SuppressWarnings("rawtypes")
			public void actionPerformed(ActionEvent arg0) 
			{
				JFrame cardView = new JFrame();	
				cardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
				cardView.setBounds(100, 100, 496, 443);
				cardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				ArrayList<Card >allCardsNoDupes = listMakerRarity(draftAllCards, "Rare");
				JList list = new JList(allCardsNoDupes.toArray());
				KeyListener keyListener = new KeyListener()
				{
					public void keyPressed(KeyEvent e)
					{
						if (e.getKeyCode() == KeyEvent.VK_ENTER)
						{
							Card selectedCard = (Card) list.getSelectedValue();
							JFrame singleCardView = new JFrame();
							JPanel panel = new JPanel();
							ImageLabel label = new ImageLabel(new ImageIcon("src/images/" + selectedCard.getName() + ".png"));
							label.setLocation(29, 37);
							panel.add(label);
							singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
							singleCardView.setBounds(100, 100, 496, 443);
							singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
							singleCardView.getContentPane().add(panel);
							singleCardView.pack();
							singleCardView.setVisible(true);
						}
					}

					@Override
					public void keyReleased(KeyEvent arg0) {


					}

					@Override
					public void keyTyped(KeyEvent arg0) {


					}
				};
				list.addKeyListener(keyListener);
				MouseListener mouseListener = new MouseAdapter() 
				{
					public void mouseClicked(MouseEvent e) 
					{
						if (e.getClickCount() == 2) 
						{    		
							Card selectedCard = (Card) list.getSelectedValue();
							JFrame singleCardView = new JFrame();
							JPanel panel = new JPanel();
							ImageLabel label = new ImageLabel(new ImageIcon("src/images/" + selectedCard.getName() + ".png"));
							label.setLocation(29, 37);
							panel.add(label);
							singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
							singleCardView.setBounds(100, 100, 496, 443);
							singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
							singleCardView.getContentPane().add(panel);
							singleCardView.pack();
							singleCardView.setVisible(true);
						}
					}
				};
				list.addMouseListener(mouseListener);
				JScrollPane scrollPane = new JScrollPane();
				scrollPane.setViewportView(list);
				cardView.getContentPane().add(scrollPane);
				cardView.pack();
				cardView.setVisible(true);
			}
		});
		mnViewDatabase.add(mntmRares);
		mntmRares.setEnabled(rItem);

		JMenuItem mntmCommon = new JMenuItem("Common");
		mntmCommon.addActionListener(new ActionListener() 
		{
			@SuppressWarnings("rawtypes")
			public void actionPerformed(ActionEvent arg0) 
			{
				JFrame cardView = new JFrame();	
				cardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
				cardView.setBounds(100, 100, 496, 443);
				cardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				ArrayList<Card >allCardsNoDupes = listMakerRarity(draftAllCards, "Common");
				JList list = new JList(allCardsNoDupes.toArray());
				KeyListener keyListener = new KeyListener()
				{
					public void keyPressed(KeyEvent e)
					{
						if (e.getKeyCode() == KeyEvent.VK_ENTER)
						{
							Card selectedCard = (Card) list.getSelectedValue();
							JFrame singleCardView = new JFrame();
							JPanel panel = new JPanel();
							ImageLabel label = new ImageLabel(new ImageIcon("src/images/" + selectedCard.getName() + ".png"));
							label.setLocation(29, 37);
							panel.add(label);
							singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
							singleCardView.setBounds(100, 100, 496, 443);
							singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
							singleCardView.getContentPane().add(panel);
							singleCardView.pack();
							singleCardView.setVisible(true);
						}
					}

					@Override
					public void keyReleased(KeyEvent arg0) {


					}

					@Override
					public void keyTyped(KeyEvent arg0) {


					}
				};
				list.addKeyListener(keyListener);
				MouseListener mouseListener = new MouseAdapter() 
				{
					public void mouseClicked(MouseEvent e) 
					{
						if (e.getClickCount() == 2) 
						{    		
							Card selectedCard = (Card) list.getSelectedValue();

							JFrame singleCardView = new JFrame();
							JPanel panel = new JPanel();
							ImageLabel label = new ImageLabel(new ImageIcon("src/images/" + selectedCard.getName() + ".png"));
							label.setLocation(29, 37);
							panel.add(label);
							singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
							singleCardView.setBounds(100, 100, 496, 443);
							singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
							singleCardView.getContentPane().add(panel);
							singleCardView.pack();
							singleCardView.setVisible(true);
						}
					}
				};
				list.addMouseListener(mouseListener);
				JScrollPane scrollPane = new JScrollPane();
				scrollPane.setViewportView(list);
				cardView.getContentPane().add(scrollPane);
				cardView.pack();
				cardView.setVisible(true);
			}
		});
		mnViewDatabase.add(mntmCommon);
		mntmCommon.setEnabled(cItem);

		JMenuItem cardViewer = new JMenuItem("Card Viewer");
		cardViewer.addActionListener(new ActionListener() 
		{
			@SuppressWarnings("rawtypes")
			public void actionPerformed(ActionEvent arg0) 
			{
				JFrame cardView = new JFrame();	
				cardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
				cardView.setBounds(100, 100, 496, 443);
				cardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				ArrayList<Card >allCardsNoDupes = listMakerRarity(draftAllCards, "Common");
				JList list = new JList(allCardsNoDupes.toArray());
				Card selectedCard = allCardsNoDupes.get(0);
				JFrame singleCardView = new JFrame();
				JPanel panel = new JPanel();
				JButton last = new JButton("Previous");
				JButton next = new JButton("Next");

				last.addActionListener(new ActionListener() 
				{
					public void actionPerformed(ActionEvent arg0) 
					{

					}

				});

				next.addActionListener(new ActionListener() 
				{
					public void actionPerformed(ActionEvent arg0) 
					{

					}

				});

				ImageLabel label = new ImageLabel(new ImageIcon("src/images/" + selectedCard.getName() + ".png"));
				label.setLocation(29, 37);
				panel.add(last);
				panel.add(label);
				panel.add(next);
				last.setEnabled(false);
				singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
				singleCardView.setBounds(100, 100, 496, 443);
				singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				singleCardView.getContentPane().add(panel);
				singleCardView.pack();
				singleCardView.setVisible(true);
			}
		});
		mnViewDatabase.add(cardViewer);
		// End View Database 
	}

	// Sets up all the ban menu listeners
	@SuppressWarnings("rawtypes")
	public static void blackListenerInit(JMenuItem blacklist, ArrayList<Card> allCards, ArrayList<Card> blackListed, 
			JList<Card> banned, ArrayList<String> dumbThing, SortingListModel banModel, JLabel lblCardCount, JLabel lblUniqueCards, JFrame DraftInit,
			JMenuItem mntmUltraRares, JMenuItem mntmSuperRares, JMenuItem mntmRares, JMenuItem mntmCommon, JMenuItem ultimateBanner, JMenuItem ultraBanner,
			JMenuItem superBanner, JMenuItem rareBanner, JMenuItem commonBanner, JMenuItem mntmNewMenuItem, JMenuItem Monarchs, JMenuItem Fissure, JMenuItem TrapHole, 
			JMenuItem Exodia, JMenuItem Water, JMenuItem Naturia, JMenuItem Toon, JMenuItem Draw, JMenuItem Ritual, JMenuItem Fusion, JMenuItem LowAtk, JMenuItem Limited,
			JMenuItem SemiLimited, JMenuItem HighAtk, JMenuItem LowLvl, JMenuItem HighLvl, JMenuItem reset, ArrayList<Card> backupAllCards, ArrayList<Card> allCardsNopeDupe)
	{
		
		KeyListener keyListener2 = new KeyListener()
		{
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					if (banned.isSelectionEmpty() == false)
					{
						Card selectedCard = (Card) banned.getSelectedValue();
						JFrame singleCardView = new JFrame();
						JPanel panel = new JPanel();
						ImageLabel label = new ImageLabel(new ImageIcon("src/images/" + selectedCard.getName() + ".png"));
						label.setLocation(29, 37);
						panel.add(label);
						singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
						singleCardView.setBounds(100, 100, 496, 443);
						singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
						singleCardView.getContentPane().add(panel);
						singleCardView.pack();
						singleCardView.setVisible(true);
					}
					else {}
				}
				else {}
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub

			}
		};
		banned.addKeyListener(keyListener2);
		
		
		blacklist.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				
				banned.addKeyListener(keyListener2);
				ArrayList<Card >allCardsNoDupes = listMaker(allCards);
				DefaultListModel<Card> model = new DefaultListModel<Card>();
				for(Card s:allCardsNoDupes) { model.addElement(s); }
				JList<Card> list = new JList<Card>(model);
				Insets EMPTY_INSETS = new Insets(0, 0, 0, 0);
				JFrame banList = new JFrame();
				banList.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
				banList.setBounds(100, 100, 496, 443);
				banList.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				banList.getContentPane().setLayout(new GridBagLayout());
				JLabel sourceLabel = new JLabel("Allowed Cards");
				banList.getContentPane().add(sourceLabel, new GridBagConstraints(0, 0, 1, 1, 0, 0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE,
						EMPTY_INSETS, 0, 0));
				banList.getContentPane().add(new JScrollPane(list), new GridBagConstraints(0, 1, 1, 5, .5,
						1, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						EMPTY_INSETS, 0, 0));
				JLabel destLabel = new JLabel("Banned Cards");
				banList.getContentPane().add(destLabel, new GridBagConstraints(2, 0, 1, 1, 0, 0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE,
						EMPTY_INSETS, 0, 0));
				banList.getContentPane().add(new JScrollPane(banned), new GridBagConstraints(2, 1, 1, 5, .5,
						1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						EMPTY_INSETS, 0, 0));

				KeyListener keyListener = new KeyListener()
				{
					public void keyPressed(KeyEvent e)
					{
						if (e.getKeyCode() == KeyEvent.VK_ENTER)
						{
							if (list.isSelectionEmpty() == false)
							{
								Card selectedCard = (Card) list.getSelectedValue();
								JFrame singleCardView = new JFrame();
								JPanel panel = new JPanel();
								ImageLabel label = new ImageLabel(new ImageIcon("src/images/" + selectedCard.getName() + ".png"));
								label.setLocation(29, 37);
								panel.add(label);
								singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
								singleCardView.setBounds(100, 100, 496, 443);
								singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
								singleCardView.getContentPane().add(panel);
								singleCardView.pack();
								singleCardView.setVisible(true);
							}
						}
					}

					@Override
					public void keyReleased(KeyEvent arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void keyTyped(KeyEvent arg0) {
						// TODO Auto-generated method stub

					}
				};
				list.addKeyListener(keyListener);
				
				



				if (blackListed.size() == 0)
				{

					JButton confirm = new JButton("Confirm");
					banList.getContentPane().add(confirm, new GridBagConstraints(1, 2, 1, 2, 0, .25,
							GridBagConstraints.CENTER, GridBagConstraints.NONE,
							EMPTY_INSETS, 0, 0));

					MouseListener allowMouseListener = new MouseAdapter() 
					{
						public void mouseClicked(MouseEvent e) 
						{
							if (dumbThing.size() == 0)
							{
								if (e.getClickCount() == 2) 
								{    		
									Card selectedCard = (Card) list.getSelectedValue();
									blackListed.add(selectedCard);
									model.removeElement(selectedCard);
									banModel.addElement(selectedCard);
									list.revalidate(); list.repaint();
									banList.revalidate(); banList.repaint();

								}
							}
						}
					};
					list.addMouseListener(allowMouseListener);

					confirm.addActionListener(new ActionListener() 
					{
						public void actionPerformed(ActionEvent arg0) 
						{
							// Removes all the cards from the current blacklist from the pool
							for (int b = 0; b < blackListed.size(); b++)
							{
								for (int a = 0; a < allCards.size(); a++)
								{
									if (blackListed.get(b).getName().equals(allCards.get(a).getName()))
									{
										allCards.remove(a);
										a = 0;
									}
								}
							}

							dumbThing.add("What a waste of memory");

							// Refresh the total card count and the draft start window (eventually also should refresh draft window)
							int totalCards3 = cardCount(allCards);
							lblCardCount.setText("Cards Available: " + totalCards3);
							ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
							int totalUnique = allCardsNopeDupe.size();
							lblUniqueCards.setText("Unique Cards: " + totalUnique);
							banned.removeKeyListener(keyListener2);
							DraftInit.revalidate(); DraftInit.repaint(); 
							banList.removeMouseListener(allowMouseListener);
							banList.revalidate(); list.revalidate();
							banList.setVisible(false);
						}
					});
				}
				banList.setVisible(true);
				int totalCards4 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards4);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint();
			}
		});


		commonBanner.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getRarity().equals("Common")) 
					{
						for (int bm = 0; bm < banModel.getSize(); bm++)
						{
							if (allCards.get(a).getName().equals(banModel.getCardAt(bm).getName())) 
							{
								checker = true; 
							}
						}

						if (checker == false) 
						{ 
							int howMany = howManyCards(allCards, allCards.get(a));
							Card temp = new Card(allCards.get(a), howMany); 
							banModel.addElement(temp); 
						}
						allCards.remove(a);
						a = 0;
						checker = false;
					}
				}

				mntmCommon.setEnabled(false);
				commonBanner.setEnabled(false);
				banned.removeKeyListener(keyListener2);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		rareBanner.addActionListener(new ActionListener() 
		{
			boolean checker = false;
			public void actionPerformed(ActionEvent arg0) 
			{
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getRarity().equals("Rare")) 
					{
						for (int bm = 0; bm < banModel.getSize(); bm++)
						{
							if (allCards.get(a).getName().equals(banModel.getCardAt(bm).getName())) 
							{
								checker = true; 
							}
						}

						if (checker == false) 
						{ 
							int howMany = howManyCards(allCards, allCards.get(a));
							Card temp = new Card(allCards.get(a), howMany); 
							banModel.addElement(temp); 
						}
						allCards.remove(a);
						a = 0;
						checker = false;
					}
				}

				mntmRares.setEnabled(false);
				rareBanner.setEnabled(false);
				banned.removeKeyListener(keyListener2);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		superBanner.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getRarity().equals("Super Rare")) 
					{
						for (int bm = 0; bm < banModel.getSize(); bm++)
						{
							if (allCards.get(a).getName().equals(banModel.getCardAt(bm).getName())) 
							{
								checker = true; 
							}
						}

						if (checker == false) 
						{ 
							int howMany = howManyCards(allCards, allCards.get(a));
							Card temp = new Card(allCards.get(a), howMany); 
							banModel.addElement(temp); 
						}
						allCards.remove(a);
						a = 0;
						checker = false;
					}
				}

				mntmSuperRares.setEnabled(false);
				superBanner.setEnabled(false);
				banned.removeKeyListener(keyListener2);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		ultraBanner.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getRarity().equals("Ultra Rare")) 
					{
						for (int bm = 0; bm < banModel.getSize(); bm++)
						{
							if (allCards.get(a).getName().equals(banModel.getCardAt(bm).getName())) 
							{
								checker = true; 
							}
						}

						if (checker == false) 
						{ 
							int howMany = howManyCards(allCards, allCards.get(a));
							Card temp = new Card(allCards.get(a), howMany); 
							banModel.addElement(temp); 
						}
						allCards.remove(a);
						a = 0;
						checker = false;
					}
				}

				mntmUltraRares.setEnabled(false);
				ultraBanner.setEnabled(false);
				banned.removeKeyListener(keyListener2);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		ultimateBanner.addActionListener(new ActionListener() 
		{

			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getRarity().equals("Ultimate Rare")) 
					{
						for (int bm = 0; bm < banModel.getSize(); bm++)
						{
							if (allCards.get(a).getName().equals(banModel.getCardAt(bm).getName())) 
							{
								checker = true; 
							}
						}

						if (checker == false) 
						{ 
							int howMany = howManyCards(allCards, allCards.get(a));
							Card temp = new Card(allCards.get(a), howMany); 
							banModel.addElement(temp); 
						}
						allCards.remove(a);
						a = 0;
						checker = false;
					}
				}

				mntmNewMenuItem.setEnabled(false);
				ultimateBanner.setEnabled(false);
				banned.removeKeyListener(keyListener2);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});





		// Reset Listener
		reset.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				allCards.clear(); dumbThing.clear(); blackListed.clear(); banModel.clear();
				copyPoolVoid(backupAllCards, allCards); mntmCommon.setEnabled(true);
				mntmRares.setEnabled(true);	mntmSuperRares.setEnabled(true);
				mntmUltraRares.setEnabled(true); mntmNewMenuItem.setEnabled(true);
				ultimateBanner.setEnabled(true); ultraBanner.setEnabled(true);
				superBanner.setEnabled(true); rareBanner.setEnabled(true);
				commonBanner.setEnabled(true); Monarchs.setEnabled(true); 
				Fissure.setEnabled(true); TrapHole.setEnabled(true);
				Exodia.setEnabled(true); Naturia.setEnabled(true);
				Water.setEnabled(true);	Toon.setEnabled(true);
				Draw.setEnabled(true); Limited.setEnabled(true);
				LowAtk.setEnabled(true); HighAtk.setEnabled(true);
				LowLvl.setEnabled(true); HighLvl.setEnabled(true);
				Ritual.setEnabled(true); Fusion.setEnabled(true);
				int totalCards2 = cardCount(allCards); lblCardCount.setText("Cards Available: " + totalCards2);
				int totalUnique = allCardsNopeDupe.size(); lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint();
			}
		});
		// end reset listener

		banGroupListenerInit(allCards, DraftInit, banModel, lblCardCount, lblUniqueCards, Monarchs, Fissure, TrapHole,  Exodia,  Water,  
				Naturia,  Toon,  Draw, Ritual,  Fusion,  LowAtk,  Limited, SemiLimited,  HighAtk,  LowLvl,  HighLvl);	
	}
	
	// Returns true if an image exists at the given path
	public static boolean isImage(String image_path)
	{
		Image image = new ImageIcon(image_path).getImage();
		if(image.getWidth(null) == -1)
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	// Logic for handling rerolls
	public boolean canReroll(int rerolls, int rerollsAllowed)
	{
		boolean checker = true;
		if (rerolls >= rerollsAllowed) { checker = false; }
		return checker;
	}
	public void  incrRerolls(int rerollsAllowed)
	{
		rerollsAllowed++;
	}

}
// END CLASS CardPool_GUI_1_Stable

// Implementing ImageLabel so we can display pictures on our frames
@SuppressWarnings("serial")
class ImageLabel extends JLabel { public ImageLabel(String img) { this(new ImageIcon(img)); }
public ImageLabel(ImageIcon icon) { setIcon(icon); setIconTextGap(0); setBorder(null); setText(null); setSize(icon.getImage().getWidth(null), icon.getImage().getHeight(null)); } }
