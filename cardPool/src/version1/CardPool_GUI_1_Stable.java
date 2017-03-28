package version1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
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
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import com.jgoodies.forms.factories.DefaultComponentFactory;

import org.apache.commons.lang3.text.WordUtils;

public class CardPool_GUI_1_Stable {

	private JFrame DraftInit;
	private static JFrame singleCardView = null;
	private JFrame cardView;
	private JFrame banList;
	private int rerollsSoFar = 0;
	private int pickNumber = 1;
	private int playerDrafting = 0;
	private static int dynamicInt = -1;
	private static Card dynamicCard = new Card("init");
	private ArrayList<Card> drafted = new ArrayList<Card>();
	private ArrayList<Card> threeChoiceTemp = new ArrayList<Card>();
	private static JList<Card> dynamicList = new JList<Card>();
	private static DefaultListModel<Card> dynamicModel1 = new DefaultListModel<Card>();
	private static DefaultListModel<Card> dynamicModel2 = new DefaultListModel<Card>();

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
		DraftInit.setBounds(600, 600, 275, 200);
		DraftInit.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		DraftInit.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		DraftInit.setTitle("Draft Options");
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		DraftInit.setLocation(dim.width/2-DraftInit.getSize().width/2, dim.height/2-DraftInit.getSize().height/2);
		
	
		

		// Draft Start window components
		JLabel lblOfPlayers = DefaultComponentFactory.getInstance().createLabel("Players"); 
		JLabel lblOfCards = DefaultComponentFactory.getInstance().createLabel("Cards to Draft");
		JLabel lblOfRerolls = DefaultComponentFactory.getInstance().createLabel("Rerolls"); 
		JLabel lblOfPool = new JLabel("Pool Fill");	
		JLabel lblCardCount = DefaultComponentFactory.getInstance().createTitle("Cards Available: " + totalCards); 
		JLabel lblUniqueCards = new JLabel("Unique Cards: " + totalUnique); 
		JSpinner numberOfPlayers = new JSpinner(); numberOfPlayers.setModel(new SpinnerNumberModel(1, 1, 10, 1));	
		JSpinner cardsToDraft = new JSpinner(); cardsToDraft.setModel(new SpinnerNumberModel(60, 10, 200, 5)); 
		JSpinner rerolls = new JSpinner(); rerolls.setModel(new SpinnerNumberModel(1, 0, 10, 1)); 
		JComboBox<String> fillStyle = new JComboBox<String>(); 
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
		JMenuItem AncientGear = new JMenuItem("Ancient Gear");
		JMenuItem Archfiend = new JMenuItem("Archfiend");
		JMenuItem Crashbug = new JMenuItem("Crashbug");
		JMenuItem Destiny = new JMenuItem("Destiny Hero");
		JMenuItem Elemental = new JMenuItem("Elemental Hero");
		JMenuItem Exodia = new JMenuItem("Exodia");
		JMenuItem Fissure = new JMenuItem("Fissure");
		JMenuItem Flip = new JMenuItem("Flip");
		JMenuItem Gishki = new JMenuItem("Gishki");
		JMenuItem God = new JMenuItem("God");
		JMenuItem Harpies = new JMenuItem("Harpies");
		JMenuItem Hazy = new JMenuItem("Hazy");
		JMenuItem LV = new JMenuItem("LV");
		JMenuItem Lightsworn = new JMenuItem("Lightsworn");
		JMenuItem Magnet = new JMenuItem("Magnet Warriors");
		JMenuItem Monarchs = new JMenuItem("Monarchs");
		JMenuItem Naturia = new JMenuItem("Naturia");
		JMenuItem Ojama = new JMenuItem("Ojama");
		JMenuItem SuperHeavy = new JMenuItem("Superheavy Samurai");
		JMenuItem Creator = new JMenuItem("The Creator");
		JMenuItem Toon = new JMenuItem("Toon");
		JMenuItem TrapHole = new JMenuItem("Trap Hole");
		JMenuItem Volcanic = new JMenuItem("Volcanic");
		JMenuItem Fusion = new JMenuItem("Fusion");
		JMenuItem Ritual = new JMenuItem("Ritual");
		JMenuItem Draw = new JMenuItem("Card Draw");
		JMenuItem Discard = new JMenuItem("Discard");
		JMenuItem EasySummon = new JMenuItem("Easy Summon");
		JMenuItem Limited = new JMenuItem("Limited");
		JMenuItem SemiLimited = new JMenuItem("Semi-Limited");
		JMenuItem LowAtk = new JMenuItem("Attack < 1500");
		JMenuItem HighAtk = new JMenuItem("Attack > 2600");
		JMenuItem LowLvl = new JMenuItem("Level < 4");
		JMenuItem HighLvl = new JMenuItem("Level > 7");
		JMenu mnBanAttribute = new JMenu("Ban: Attributes");
		JMenuItem Dark = new JMenuItem("Dark");
		JMenuItem Earth = new JMenuItem("Earth");
		JMenuItem Fire = new JMenuItem("Fire");
		JMenuItem Light = new JMenuItem("Light");
		JMenuItem Water = new JMenuItem("Water");
		JMenuItem Wind = new JMenuItem("Wind");
		JMenu mnBanType = new JMenu("Ban: Types");
		JMenuItem Aqua = new JMenuItem("Aqua");
		JMenuItem Beast = new JMenuItem("Beast");
		JMenuItem BeastWarrior = new JMenuItem("Beast-Warrior");
		JMenuItem Dinosaur = new JMenuItem("Dinosaur");
		JMenuItem Divine = new JMenuItem("Divine-Beast");
		JMenuItem Dragon = new JMenuItem("Dragon");
		JMenuItem Fairy = new JMenuItem("Fairy");
		JMenuItem Fiend = new JMenuItem("Fiend");
		JMenuItem Fish = new JMenuItem("Fish");
		JMenuItem Insect = new JMenuItem("Insect");
		JMenuItem Machine = new JMenuItem("Machine");
		JMenuItem Plant = new JMenuItem("Plant");
		JMenuItem Psychic = new JMenuItem("Psychic");
		JMenuItem Pyro = new JMenuItem("Pyro");
		JMenuItem Reptile = new JMenuItem("Reptile");
		JMenuItem Rock = new JMenuItem("Rock");
		JMenuItem SeaSerpent = new JMenuItem("Sea Serpent");
		JMenuItem Spellcaster = new JMenuItem("Spellcaster");
		JMenuItem Thunder = new JMenuItem("Thunder");
		JMenuItem Warrior = new JMenuItem("Warrior");
		JMenuItem WingedBeast = new JMenuItem("Winged Beast");
		JMenuItem Wyrm = new JMenuItem("Wyrm");
		JMenuItem Zombie = new JMenuItem("Zombie");
		JMenu mnBanCard = new JMenu("Ban: Card Type");
		JMenuItem Spell = new JMenuItem("Spells");
		JMenuItem Trap = new JMenuItem("Traps");
		JMenuItem ContinSpell = new JMenuItem("Continuous Spells");
		JMenuItem ContinTrap = new JMenuItem("Continuous Traps");
		JMenuItem Contin = new JMenuItem("Continuous");
		JMenuItem Field = new JMenuItem("Field");
		JMenuItem Quickplay = new JMenuItem("Quickplay");
		JMenuItem Equip = new JMenuItem("Equip");
		JMenuItem Counter = new JMenuItem("Counter");
		JMenu mnBanScore = new JMenu("Ban: Score");
		JMenuItem lowScore = new JMenuItem("Tier Score < 30");
		JMenuItem medScore = new JMenuItem("30 < Tier Score < 60");
		JMenuItem highScore = new JMenuItem("TierScore > 60");
		JMenuItem veryHighScore = new JMenuItem("TierScore > 75");
		JMenuItem OP = new JMenuItem("TierScore > 85");

		DraftInit.getContentPane().add(lblOfPlayers); 
		DraftInit.getContentPane().add(numberOfPlayers);
		DraftInit.getContentPane().add(lblOfCards); 
		DraftInit.getContentPane().add(cardsToDraft);
		DraftInit.getContentPane().add(lblOfRerolls); 
		DraftInit.getContentPane().add(rerolls);
		DraftInit.getContentPane().add(lblOfPool);
		DraftInit.getContentPane().add(fillStyle); 
			fillStyle.setModel(new DefaultComboBoxModel(new String[] {"Random", "Equal (Rarity)", "Equal (Score)", "Harsh"})); 
			fillStyle.setEditable(false);
		DraftInit.getContentPane().add(draftStart); 
		DraftInit.getContentPane().add(lblCardCount);
			lblCardCount.setHorizontalAlignment(SwingConstants.CENTER);
		DraftInit.getContentPane().add(lblUniqueCards);
		DraftInit.setJMenuBar(menuBar);
		menuBar.add(mnViewDatabase); menuBar.add(mnBan);
		viewDatabaseListenerInit(mnViewDatabase, mntmViewAllCards, mntmNewMenuItem, allCards, mntmUltraRares, mntmSuperRares, mntmRares, mntmCommon);
		mnBan.add(blacklist); mnBan.add(mnBanAll);
		mnBan.add(mnBanGrouping); mnBan.add(mnBanAttribute);
		mnBan.add(mnBanType); mnBan.add(mnBanCard); mnBan.add(mnBanScore);

		mnBanAll.add(ultimateBanner); mnBanAll.add(ultraBanner); 
		mnBanAll.add(superBanner); mnBanAll.add(rareBanner);
		mnBanAll.add(commonBanner); mnBan.add(reset);

		mnBanGrouping.add(AncientGear); mnBanGrouping.add(Archfiend);
		mnBanGrouping.add(Crashbug); mnBanGrouping.add(Destiny);
		mnBanGrouping.add(Elemental); mnBanGrouping.add(Exodia);
		mnBanGrouping.add(Fissure); mnBanGrouping.add(Flip);
		mnBanGrouping.add(Gishki); mnBanGrouping.add(God);
		mnBanGrouping.add(Harpies); mnBanGrouping.add(Hazy);
		mnBanGrouping.add(LV); mnBanGrouping.add(Lightsworn);
		mnBanGrouping.add(Magnet); mnBanGrouping.add(Monarchs); 
		mnBanGrouping.add(Naturia); mnBanGrouping.add(Ojama);
		mnBanGrouping.add(SuperHeavy); mnBanGrouping.add(Creator);
		mnBanGrouping.add(Toon); mnBanGrouping.add(TrapHole);
		mnBanGrouping.add(Volcanic); mnBanGrouping.add(Fusion);
		mnBanGrouping.add(Ritual); mnBanGrouping.add(Draw); 
		mnBanGrouping.add(Discard); mnBanGrouping.add(EasySummon);
		mnBanGrouping.add(Limited); mnBanGrouping.add(SemiLimited);
		mnBanGrouping.add(LowAtk); mnBanGrouping.add(HighAtk);
		mnBanGrouping.add(LowLvl); mnBanGrouping.add(HighLvl);

		mnBanAttribute.add(Dark); mnBanAttribute.add(Earth);
		mnBanAttribute.add(Fire); mnBanAttribute.add(Light);
		mnBanAttribute.add(Water); mnBanAttribute.add(Wind);

		mnBanType.add(Aqua); mnBanType.add(Beast); mnBanType.add(BeastWarrior);
		mnBanType.add(Dinosaur); mnBanType.add(Divine); mnBanType.add(Dragon);
		mnBanType.add(Fairy); mnBanType.add(Fiend); mnBanType.add(Fish);
		mnBanType.add(Insect); mnBanType.add(Machine); mnBanType.add(Plant);
		mnBanType.add(Psychic); mnBanType.add(Pyro); mnBanType.add(Reptile);
		mnBanType.add(Rock); mnBanType.add(SeaSerpent); mnBanType.add(Spellcaster);
		mnBanType.add(Thunder); mnBanType.add(Warrior); mnBanType.add(WingedBeast);
		mnBanType.add(Wyrm); mnBanType.add(Zombie);

		mnBanCard.add(Spell); mnBanCard.add(Trap); mnBanCard.add(ContinSpell);
		mnBanCard.add(ContinTrap); mnBanCard.add(Contin); mnBanCard.add(Field);
		mnBanCard.add(Quickplay); mnBanCard.add(Equip); mnBanCard.add(Counter); 

		mnBanScore.add(lowScore); mnBanScore.add(medScore); mnBanScore.add(highScore);
		mnBanScore.add(veryHighScore); mnBanScore.add(OP);

		// Grey out buttons that don't work yet
		//fillStyle.setEnabled(false);

		// Grey out ban buttons that currently ban 0 cards
		Divine.setEnabled(false); Psychic.setEnabled(false);
		Wyrm.setEnabled(false); Creator.setEnabled(false);
		Gishki.setEnabled(false); Hazy.setEnabled(false);
		Volcanic.setEnabled(false); Ojama.setEnabled(false);
		Magnet.setEnabled(false); God.setEnabled(false);
		AncientGear.setEnabled(false);

		blackListenerInit(blacklist, allCards, blackListed, banned, dumbThing, banModel, lblCardCount, lblUniqueCards, DraftInit, mntmUltraRares, mntmSuperRares, mntmRares,
				mntmCommon, ultimateBanner, ultraBanner, superBanner, rareBanner, commonBanner, mntmNewMenuItem, Monarchs, Fissure, TrapHole, Exodia, Water, Naturia,
				Toon, Draw, Ritual, Fusion, LowAtk, Limited, SemiLimited, HighAtk, LowLvl, HighLvl, reset, backupAllCards, allCardsNopeDupe, Light, Dark, Wind, Fire, Earth,
				AncientGear, Archfiend, Crashbug, Destiny, Elemental, Flip, Gishki, God, Harpies, Hazy, LV, Lightsworn, Magnet, Ojama, SuperHeavy, Creator, Volcanic,
				Discard, EasySummon, Aqua, Beast, BeastWarrior, Dinosaur, Divine, Dragon, Fairy, Fiend, Fish, Insect, Machine, Plant, Psychic, Pyro, Reptile, Rock, SeaSerpent, 
				Spellcaster, Thunder, Warrior, WingedBeast, Wyrm, Zombie, Spell, Trap, Contin, ContinSpell, ContinTrap, Field, Quickplay, Equip, Counter, lowScore, medScore,
				highScore, veryHighScore, OP);

		// End primary window components


		// Init main draft window
		draftStart.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				DraftInit.setTitle("Draft");
				DraftInit.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
				// Final Draft Variable Init
				int playerCountLocal = (Integer) numberOfPlayers.getValue();
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

				DraftInit.getContentPane().setLayout(new GridBagLayout());
				GridBagConstraints c = new GridBagConstraints();
				c.fill = GridBagConstraints.HORIZONTAL;




				JButton viewDeck = new JButton("View Deck");
				JButton reroll = new JButton("Reroll"); if (rerollsLocal == 0) { reroll.setEnabled(false); }

				// Change pool fill based on selection made in primary window
				String fillStyleString = fillStyle.getSelectedItem().toString();
				if (fillStyleString == "Random") { poolDeckInit(draftPools, draftDecks, playerCountLocal); fillAllPools(allCards, draftPools, playerCountLocal);	}
				else if (fillStyleString == "Equal (Rarity)") { poolDeckInit(draftPools, draftDecks, playerCountLocal); fillAllPoolsEqual(allCards, draftPools, playerCountLocal);	}
				else if (fillStyleString == "Equal (Score)") { poolDeckInit(draftPools, draftDecks, playerCountLocal); fillAllPoolsScore(allCards, draftPools, playerCountLocal);	}
				else if (fillStyleString == "Harsh") { poolDeckInit(draftPools, draftDecks, playerCountLocal); fillAllPoolsHarsh(allCards, draftPools, playerCountLocal);	}
				else { poolDeckInit(draftPools, draftDecks, playerCountLocal); fillAllPools(allCards, draftPools, playerCountLocal);	}

				int defPoolSize = draftPools.get(playerDrafting).size();

				// Fills up an array used to display the correct database of cards during the draft (banned cards removed)
				ArrayList<Card> draftAllCards = new ArrayList<Card>();
				for (int d = 0; d < draftPools.size(); d++) { for (int e = 0; e < draftPools.get(d).size(); e++) { draftAllCards.add(draftPools.get(d).get(e)); } }
				cardCounter(draftAllCards);
				draftAllCards.sort(draftAllCards.get(0));

				// Sets up the database view menu again after rechecking the database contents
				//viewDatabaseListenerInit2(mnViewDatabase, mntmViewAllCards, mntmNewMenuItem, draftAllCards, mntmUltraRares, mntmSuperRares, mntmRares, mntmCommon, urItem, ulrItem, srItem, rItem, cItem);
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
				JLabel poolCardCount = new JLabel("Pool: " + draftPools.get(playerDrafting).size() + "/" + defPoolSize);
				JLabel playerDraftLbl = new JLabel("Player " + (playerDrafting + 1));
				JLabel pick1Rarity = new JLabel(threeChoiceTemp.get(0).getRarity()); textColor(threeChoiceTemp.get(0), pick1Rarity);
				JLabel pick2Rarity = new JLabel(threeChoiceTemp.get(1).getRarity()); textColor(threeChoiceTemp.get(1), pick2Rarity);
				JLabel pick3Rarity = new JLabel(threeChoiceTemp.get(2).getRarity()); textColor(threeChoiceTemp.get(2), pick3Rarity);

				// Adding everything to the draft window
				c.insets = new Insets(0,10,10,10); 
				c.weightx = 0.5;  c.gridx = 1; c.gridy = 0; playerDraftLbl.setHorizontalAlignment(SwingConstants.CENTER); DraftInit.getContentPane().add(playerDraftLbl, c);
				c.gridx = 0; c.gridy = 1; DraftInit.getContentPane().add(cardPick1, c); 
				c.gridx = 1; c.gridy = 1; DraftInit.getContentPane().add(cardPick2, c); 
				c.gridx = 2; c.gridy = 1; DraftInit.getContentPane().add(cardPick3, c);
				c.gridx = 0; c.gridy = 3; DraftInit.getContentPane().add(viewDeck, c);
				c.gridx = 1; c.gridy = 3; pickCounter.setHorizontalAlignment(SwingConstants.CENTER); DraftInit.getContentPane().add(pickCounter, c);
				c.gridx = 2; c.gridy = 3; DraftInit.getContentPane().add(reroll, c);
				c.gridx = 0; c.gridy = 2; pick1Rarity.setHorizontalAlignment(SwingConstants.CENTER); DraftInit.getContentPane().add(pick1Rarity, c);
				c.gridx = 1; c.gridy = 2; pick2Rarity.setHorizontalAlignment(SwingConstants.CENTER); DraftInit.getContentPane().add(pick2Rarity, c); 
				c.gridx = 2; c.gridy = 2; pick3Rarity.setHorizontalAlignment(SwingConstants.CENTER); DraftInit.getContentPane().add(pick3Rarity, c);
				c.gridx = 1; c.gridy = 4; poolCardCount.setHorizontalAlignment(SwingConstants.CENTER); DraftInit.getContentPane().add(poolCardCount, c);



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

							pick1Rarity.setText(threeChoiceTemp.get(0).getRarity());
							pick2Rarity.setText(threeChoiceTemp.get(1).getRarity());
							pick3Rarity.setText(threeChoiceTemp.get(2).getRarity());
							textColor(threeChoiceTemp.get(0), pick1Rarity);
							textColor(threeChoiceTemp.get(1), pick2Rarity);
							textColor(threeChoiceTemp.get(2), pick3Rarity);

							pickNumber++;
							pickCounter.setText("Pick " + pickNumber + "/" + cardsToDraftLocal);

							poolCardCount.setText("Pool: " + draftPools.get(playerDrafting).size() + "/" + defPoolSize);

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
								playerDraftLbl.setText("Player " + (playerDrafting + 1));
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
									poolCardCount.setText("Pool: " + draftPools.get(playerDrafting).size() + "/" + defPoolSize);

									pick1Rarity.setText(threeChoiceTemp.get(0).getRarity());
									pick2Rarity.setText(threeChoiceTemp.get(1).getRarity());
									pick3Rarity.setText(threeChoiceTemp.get(2).getRarity());
									textColor(threeChoiceTemp.get(0), pick1Rarity);
									textColor(threeChoiceTemp.get(1), pick2Rarity);
									textColor(threeChoiceTemp.get(2), pick3Rarity);
								}
								else
								{
									JFrame finalFrame = new JFrame();
									finalFrame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
									finalFrame.setBounds(100, 100, 496, 443);
									finalFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
									finalFrame.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

									JButton viewDeckOf = new JButton("View Deck");
									JButton viewStats = new JButton("Deck Stats");
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
											cardViewLocal.setTitle("Player " + (playerDeck.getSelectedIndex() + 1) + "'s Deck");
											cardViewLocal.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
											cardViewLocal.setBounds(100, 100, 496, 443);
											cardViewLocal.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
											ArrayList<Card>allCardsNoDupes = listMaker(draftDecks.get(playerDeck.getSelectedIndex()));
											allCardsNoDupes.sort(allCardsNoDupes.get(0));
											JList list = new JList(allCardsNoDupes.toArray());
											dynamicList = list;
											KeyListener keyListener = new KeyListener()
											{
												public void keyPressed(KeyEvent e)
												{
													if (e.getKeyCode() == KeyEvent.VK_ENTER)
													{
														dynamicCard = (Card) dynamicList.getSelectedValue();
														singleCardView = new JFrame();										
														JPanel panel = new JPanel();
														boolean smallImage = false;
														JButton fullSize = new JButton("Full Resolution");
														panel.setLayout(new GridBagLayout());
														GridBagConstraints c = new GridBagConstraints();
														c.fill = GridBagConstraints.HORIZONTAL;
														ImageLabel label;
														if (isImage("src/images/" + dynamicCard.getName() + "Small.png")) 
														{ 
															label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + "Small.png"));
															smallImage = true;
														}
														else { label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png")); }
														JLabel rarity = new JLabel(dynamicCard.getRarity()); textColor(dynamicCard, rarity);
														JLabel quantity = new JLabel("Quantity: " + dynamicCard.getQuantity());
														Integer tempInt = dynamicCard.getTierScore();
														String scoreString = tempInt.toString();
														JLabel score = new JLabel("Tier Score: " + scoreString);
														JButton showText = new JButton("Card Text");
														c.insets = new Insets(5, 5, 5, 5);
														c.weightx = 0.5; c.gridx = 0; c.gridy = 0; c.gridheight = 3; panel.add(label, c);
														c.gridheight = 1; c.gridx = 0; c.gridy = 6; panel.add(rarity, c);
														rarity.setHorizontalAlignment(SwingConstants.CENTER); 
														c.gridx = 0; c.gridy = 7;  panel.add(quantity, c);
														quantity.setHorizontalAlignment(SwingConstants.CENTER);
														c.gridx = 0; c.gridy = 8;  panel.add(score, c);
														score.setHorizontalAlignment(SwingConstants.CENTER); 
														c.gridx = 0; c.gridy = 4;  panel.add(showText, c);
														showText.setHorizontalAlignment(SwingConstants.CENTER); 
														showText.setMargin( new Insets(0,0,0,0) );if (smallImage)
														{
															c.weightx = 0.5; c.gridx = 0; c.gridy = 5; panel.add(fullSize, c); 
															fullSize.setHorizontalAlignment(SwingConstants.CENTER);
															fullSize.addActionListener(new ActionListener()
															{
																@Override
																public void actionPerformed(ActionEvent e) 
																{
																	JFrame fullView = new JFrame();
																	fullView.setTitle(dynamicList.getSelectedValue().getName() + " - Zoomed");
																	JPanel fullPanel = new JPanel();
																	ImageLabel bigImage = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png"));
																	fullPanel.add(bigImage);
																	fullView.getContentPane().add(fullPanel);
																	fullView.pack();
																	fullView.setResizable(false);
																	fullView.setVisible(true);
																	
																}
																
															});
														}
														singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
														singleCardView.setBounds(100, 100, 496, 443);
														singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
														singleCardView.getContentPane().add(panel);
														singleCardView.pack();						
														singleCardView.setResizable(false);
														singleCardView.setVisible(true);
														
														showText.addActionListener(new ActionListener()
														{
															@Override
															public void actionPerformed(ActionEvent arg0) 
															{
																JFrame textFrame = new JFrame();
																textFrame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
																textFrame.setBounds(100, 100, 496, 443);
																textFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
																JPanel textPanel = new JPanel();
																JTextArea textField = new JTextArea(6, 40);
																textField.setEditable(false);
																String temp = dynamicCard.getText();
																String newTemp = WordUtils.wrap(temp, 75, "\n", false);
																textField.setText(newTemp);
																textField.setWrapStyleWord(true);
																JScrollPane vertScroll = new JScrollPane(textField);
																textPanel.add(vertScroll);
																textFrame.getContentPane().add(textPanel);
																textFrame.pack();
																textFrame.setResizable(false);
																textFrame.setVisible(true);
																
																
															}
														});
													
													
													}
												}

												@Override public void keyReleased(KeyEvent arg0) {} @Override public void keyTyped(KeyEvent arg0) {}
											};
											list.addKeyListener(keyListener);
											MouseListener mouseListener = new MouseAdapter() 
											{
												public void mouseClicked(MouseEvent e) 
												{
													if (e.getClickCount() == 2) 
													{    		
														dynamicCard = (Card) dynamicList.getSelectedValue();
														singleCardView = new JFrame();										
														JPanel panel = new JPanel();
														boolean smallImage = false;
														JButton fullSize = new JButton("Full Resolution");
														panel.setLayout(new GridBagLayout());
														GridBagConstraints c = new GridBagConstraints();
														c.fill = GridBagConstraints.HORIZONTAL;
														ImageLabel label;
														if (isImage("src/images/" + dynamicCard.getName() + "Small.png")) 
														{ 
															label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + "Small.png"));
															smallImage = true;
														}
														else { label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png")); }
														JLabel rarity = new JLabel(dynamicCard.getRarity()); textColor(dynamicCard, rarity);
														JLabel quantity = new JLabel("Quantity: " + dynamicCard.getQuantity());
														Integer tempInt = dynamicCard.getTierScore();
														String scoreString = tempInt.toString();
														JLabel score = new JLabel("Tier Score: " + scoreString);
														JButton showText = new JButton("Card Text");
														c.insets = new Insets(5, 5, 5, 5);
														c.weightx = 0.5; c.gridx = 0; c.gridy = 0; c.gridheight = 3; panel.add(label, c);
														c.gridheight = 1; c.gridx = 0; c.gridy = 6; panel.add(rarity, c);
														rarity.setHorizontalAlignment(SwingConstants.CENTER); 
														c.gridx = 0; c.gridy = 7;  panel.add(quantity, c);
														quantity.setHorizontalAlignment(SwingConstants.CENTER);
														c.gridx = 0; c.gridy = 8;  panel.add(score, c);
														score.setHorizontalAlignment(SwingConstants.CENTER); 
														c.gridx = 0; c.gridy = 4;  panel.add(showText, c);
														showText.setHorizontalAlignment(SwingConstants.CENTER); 
														showText.setMargin( new Insets(0,0,0,0) );if (smallImage)
														{
															c.weightx = 0.5; c.gridx = 0; c.gridy = 5; panel.add(fullSize, c); 
															fullSize.setHorizontalAlignment(SwingConstants.CENTER);
															fullSize.addActionListener(new ActionListener()
															{
																@Override
																public void actionPerformed(ActionEvent e) 
																{
																	JFrame fullView = new JFrame();
																	fullView.setTitle(dynamicList.getSelectedValue().getName() + " - Zoomed");
																	JPanel fullPanel = new JPanel();
																	ImageLabel bigImage = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png"));
																	fullPanel.add(bigImage);
																	fullView.getContentPane().add(fullPanel);
																	fullView.pack();
																	fullView.setResizable(false);
																	fullView.setVisible(true);
																	
																}
																
															});
														}
														singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
														singleCardView.setBounds(100, 100, 496, 443);
														singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
														singleCardView.getContentPane().add(panel);
														singleCardView.pack();						
														singleCardView.setResizable(false);
														singleCardView.setVisible(true);
														
														showText.addActionListener(new ActionListener()
														{
															@Override
															public void actionPerformed(ActionEvent arg0) 
															{
																JFrame textFrame = new JFrame();
																textFrame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
																textFrame.setBounds(100, 100, 496, 443);
																textFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
																JPanel textPanel = new JPanel();
																JTextArea textField = new JTextArea(6, 40);
																textField.setEditable(false);
																String temp = dynamicCard.getText();
																String newTemp = WordUtils.wrap(temp, 75, "\n", false);
																textField.setText(newTemp);
																textField.setWrapStyleWord(true);
																JScrollPane vertScroll = new JScrollPane(textField);
																textPanel.add(vertScroll);
																textFrame.getContentPane().add(textPanel);
																textFrame.pack();
																textFrame.setResizable(false);
																textFrame.setVisible(true);
																
																
															}
														});
													
													
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

									viewStats.addActionListener(new ActionListener() 
									{
										public void actionPerformed(ActionEvent arg0) 
										{
											JFrame cardViewLocal = new JFrame();	
											cardViewLocal.setTitle("Player " + (playerDeck.getSelectedIndex() + 1) + "'s Stats");
											cardViewLocal.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
											cardViewLocal.setBounds(100, 100, 496, 443);
											cardViewLocal.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
											ArrayList<Card >allCardsNoDupes = listMaker(draftDecks.get(playerDeck.getSelectedIndex()));
											Card highCard = new Card("temp", 0);
											Card highCard2 = new Card("temp", 0);
											Card highCard3 = new Card("temp", 0);
											int deckScore = 0;
											int ultimates = 0;
											int ultras = 0;
											int supers = 0;
											int rares = 0;
											int commons = 0;
											for (Card card : allCardsNoDupes) 
											{
												if (card.getTierScore() > highCard.getTierScore()) { highCard = card; }
												deckScore += (card.getTierScore() * card.getQuantity());
												switch (card.getRarity())
												{
												case "Ultimate Rare":
													ultimates++; break;
												case "Ultra Rare":
													ultras++; break;
												case "Super Rare":
													supers++; break;
												case "Rare":
													rares++; break;
												case "Common":
													commons++; break;
												default:
													break;
												}
											}

											for (Card card : allCardsNoDupes)
											{
												if (card.getTierScore() < highCard.getTierScore() && card.getTierScore() > highCard2.getTierScore())
												{
													highCard2 = card;
												}
											}

											for (Card card : allCardsNoDupes)
											{
												if (card.getTierScore() < highCard2.getTierScore() && card.getTierScore() > highCard3.getTierScore())
												{
													highCard3 = card;
												}
											}

											int avgCardScore = deckScore / cardsToDraftLocal;
											JTextArea stats = new JTextArea("Deck Score: " + deckScore + "\nAverage Card Score: " + avgCardScore + "\n\nUltimate Rares: " + ultimates 
													+ "\nUltra Rares: " + ultras + "\nSuper Rares: " + supers + "\nRares: " + rares
													+ "\nCommons: " + commons + "\n\nTop 3 Cards:\n"
													+ highCard.getName() + " (" + highCard.getTierScore() + ")\n"
													+ highCard2.getName() + " (" + highCard2.getTierScore() + ")\n"
													+ highCard3.getName() + " (" + highCard3.getTierScore() + ")");

											cardViewLocal.getContentPane().add(stats);
											cardViewLocal.pack();
											cardViewLocal.setVisible(true);

										}
									});


									playerDeck.setEditable(false);
									finalFrame.getContentPane().add(viewDeckOf);
									finalFrame.getContentPane().add(viewStats);

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

							pick1Rarity.setText(threeChoiceTemp.get(0).getRarity());
							pick2Rarity.setText(threeChoiceTemp.get(1).getRarity());
							pick3Rarity.setText(threeChoiceTemp.get(2).getRarity());
							textColor(threeChoiceTemp.get(0), pick1Rarity);
							textColor(threeChoiceTemp.get(1), pick2Rarity);
							textColor(threeChoiceTemp.get(2), pick3Rarity);

							pickNumber++;
							pickCounter.setText("Pick " + pickNumber + "/" + cardsToDraftLocal);

							poolCardCount.setText("Pool: " + draftPools.get(playerDrafting).size() + "/" + defPoolSize);

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
								playerDraftLbl.setText("Player " + (playerDrafting + 1));
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
									poolCardCount.setText("Pool: " + draftPools.get(playerDrafting).size() + "/" + defPoolSize);

									pick1Rarity.setText(threeChoiceTemp.get(0).getRarity());
									pick2Rarity.setText(threeChoiceTemp.get(1).getRarity());
									pick3Rarity.setText(threeChoiceTemp.get(2).getRarity());
									textColor(threeChoiceTemp.get(0), pick1Rarity);
									textColor(threeChoiceTemp.get(1), pick2Rarity);
									textColor(threeChoiceTemp.get(2), pick3Rarity);
								}
								else
								{
									JFrame finalFrame = new JFrame();
									finalFrame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
									finalFrame.setBounds(100, 100, 496, 443);
									finalFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
									finalFrame.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

									JButton viewDeckOf = new JButton("View Deck");
									JButton viewStats = new JButton("Deck Stats");
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
											cardViewLocal.setTitle("Player " + (playerDeck.getSelectedIndex() + 1) + "'s Deck");
											cardViewLocal.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
											cardViewLocal.setBounds(100, 100, 496, 443);
											cardViewLocal.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
											ArrayList<Card >allCardsNoDupes = listMaker(draftDecks.get(playerDeck.getSelectedIndex()));
											JList list = new JList(allCardsNoDupes.toArray());
											dynamicList = list;
											KeyListener keyListener = new KeyListener()
											{
												public void keyPressed(KeyEvent e)
												{
													if (e.getKeyCode() == KeyEvent.VK_ENTER)
													{
														dynamicCard = (Card) dynamicList.getSelectedValue();
														singleCardView = new JFrame();										
														JPanel panel = new JPanel();
														boolean smallImage = false;
														JButton fullSize = new JButton("Full Resolution");
														panel.setLayout(new GridBagLayout());
														GridBagConstraints c = new GridBagConstraints();
														c.fill = GridBagConstraints.HORIZONTAL;
														ImageLabel label;
														if (isImage("src/images/" + dynamicCard.getName() + "Small.png")) 
														{ 
															label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + "Small.png"));
															smallImage = true;
														}
														else { label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png")); }
														JLabel rarity = new JLabel(dynamicCard.getRarity()); textColor(dynamicCard, rarity);
														JLabel quantity = new JLabel("Quantity: " + dynamicCard.getQuantity());
														Integer tempInt = dynamicCard.getTierScore();
														String scoreString = tempInt.toString();
														JLabel score = new JLabel("Tier Score: " + scoreString);
														JButton showText = new JButton("Card Text");
														c.insets = new Insets(5, 5, 5, 5);
														c.weightx = 0.5; c.gridx = 0; c.gridy = 0; c.gridheight = 3; panel.add(label, c);
														c.gridheight = 1; c.gridx = 0; c.gridy = 6; panel.add(rarity, c);
														rarity.setHorizontalAlignment(SwingConstants.CENTER); 
														c.gridx = 0; c.gridy = 7;  panel.add(quantity, c);
														quantity.setHorizontalAlignment(SwingConstants.CENTER);
														c.gridx = 0; c.gridy = 8;  panel.add(score, c);
														score.setHorizontalAlignment(SwingConstants.CENTER); 
														c.gridx = 0; c.gridy = 4;  panel.add(showText, c);
														showText.setHorizontalAlignment(SwingConstants.CENTER); 
														showText.setMargin( new Insets(0,0,0,0) );if (smallImage)
														{
															c.weightx = 0.5; c.gridx = 0; c.gridy = 5; panel.add(fullSize, c); 
															fullSize.setHorizontalAlignment(SwingConstants.CENTER);
															fullSize.addActionListener(new ActionListener()
															{
																@Override
																public void actionPerformed(ActionEvent e) 
																{
																	JFrame fullView = new JFrame();
																	JPanel fullPanel = new JPanel();
																	ImageLabel bigImage = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png"));
																	fullPanel.add(bigImage);
																	fullView.getContentPane().add(fullPanel);
																	fullView.pack();
																	fullView.setResizable(false);
																	fullView.setVisible(true);
																	
																}
																
															});
														}
														singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
														singleCardView.setBounds(100, 100, 496, 443);
														singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
														singleCardView.getContentPane().add(panel);
														singleCardView.pack();						
														singleCardView.setResizable(false);
														singleCardView.setVisible(true);
														
														showText.addActionListener(new ActionListener()
														{
															@Override
															public void actionPerformed(ActionEvent arg0) 
															{
																JFrame textFrame = new JFrame();
																textFrame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
																textFrame.setBounds(100, 100, 496, 443);
																textFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
																JPanel textPanel = new JPanel();
																JTextArea textField = new JTextArea(6, 40);
																textField.setEditable(false);
																String temp = dynamicCard.getText();
																String newTemp = WordUtils.wrap(temp, 75, "\n", false);
																textField.setText(newTemp);
																textField.setWrapStyleWord(true);
																JScrollPane vertScroll = new JScrollPane(textField);
																textPanel.add(vertScroll);
																textFrame.getContentPane().add(textPanel);
																textFrame.pack();
																textFrame.setResizable(false);
																textFrame.setVisible(true);
																
																
															}
														});
													
													
													}
												}

												@Override public void keyReleased(KeyEvent arg0) {} @Override public void keyTyped(KeyEvent arg0) {}
											};
											list.addKeyListener(keyListener);
											MouseListener mouseListener = new MouseAdapter() 
											{
												public void mouseClicked(MouseEvent e) 
												{
													if (e.getClickCount() == 2) 
													{    		
														dynamicCard = (Card) dynamicList.getSelectedValue();
														singleCardView = new JFrame();										
														JPanel panel = new JPanel();
														boolean smallImage = false;
														JButton fullSize = new JButton("Full Resolution");
														panel.setLayout(new GridBagLayout());
														GridBagConstraints c = new GridBagConstraints();
														c.fill = GridBagConstraints.HORIZONTAL;
														ImageLabel label;
														if (isImage("src/images/" + dynamicCard.getName() + "Small.png")) 
														{ 
															label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + "Small.png"));
															smallImage = true;
														}
														else { label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png")); }
														JLabel rarity = new JLabel(dynamicCard.getRarity()); textColor(dynamicCard, rarity);
														JLabel quantity = new JLabel("Quantity: " + dynamicCard.getQuantity());
														Integer tempInt = dynamicCard.getTierScore();
														String scoreString = tempInt.toString();
														JLabel score = new JLabel("Tier Score: " + scoreString);
														JButton showText = new JButton("Card Text");
														c.insets = new Insets(5, 5, 5, 5);
														c.weightx = 0.5; c.gridx = 0; c.gridy = 0; c.gridheight = 3; panel.add(label, c);
														c.gridheight = 1; c.gridx = 0; c.gridy = 6; panel.add(rarity, c);
														rarity.setHorizontalAlignment(SwingConstants.CENTER); 
														c.gridx = 0; c.gridy = 7;  panel.add(quantity, c);
														quantity.setHorizontalAlignment(SwingConstants.CENTER);
														c.gridx = 0; c.gridy = 8;  panel.add(score, c);
														score.setHorizontalAlignment(SwingConstants.CENTER); 
														c.gridx = 0; c.gridy = 4;  panel.add(showText, c);
														showText.setHorizontalAlignment(SwingConstants.CENTER); 
														showText.setMargin( new Insets(0,0,0,0) );if (smallImage)
														{
															c.weightx = 0.5; c.gridx = 0; c.gridy = 5; panel.add(fullSize, c); 
															fullSize.setHorizontalAlignment(SwingConstants.CENTER);
															fullSize.addActionListener(new ActionListener()
															{
																@Override
																public void actionPerformed(ActionEvent e) 
																{
																	JFrame fullView = new JFrame();
																	JPanel fullPanel = new JPanel();
																	ImageLabel bigImage = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png"));
																	fullPanel.add(bigImage);
																	fullView.getContentPane().add(fullPanel);
																	fullView.pack();
																	fullView.setResizable(false);
																	fullView.setVisible(true);
																	
																}
																
															});
														}
														singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
														singleCardView.setBounds(100, 100, 496, 443);
														singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
														singleCardView.getContentPane().add(panel);
														singleCardView.pack();						
														singleCardView.setResizable(false);
														singleCardView.setVisible(true);
														
														showText.addActionListener(new ActionListener()
														{
															@Override
															public void actionPerformed(ActionEvent arg0) 
															{
																JFrame textFrame = new JFrame();
																textFrame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
																textFrame.setBounds(100, 100, 496, 443);
																textFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
																JPanel textPanel = new JPanel();
																JTextArea textField = new JTextArea(6, 40);
																textField.setEditable(false);
																String temp = dynamicCard.getText();
																String newTemp = WordUtils.wrap(temp, 75, "\n", false);
																textField.setText(newTemp);
																textField.setWrapStyleWord(true);
																JScrollPane vertScroll = new JScrollPane(textField);
																textPanel.add(vertScroll);
																textFrame.getContentPane().add(textPanel);
																textFrame.pack();
																textFrame.setResizable(false);
																textFrame.setVisible(true);
																
																
															}
														});
													
													
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

									viewStats.addActionListener(new ActionListener() 
									{
										public void actionPerformed(ActionEvent arg0) 
										{
											JFrame cardViewLocal = new JFrame();	
											cardViewLocal.setTitle("Player " + (playerDeck.getSelectedIndex() + 1) + "'s Stats");
											cardViewLocal.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
											cardViewLocal.setBounds(100, 100, 496, 443);
											cardViewLocal.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
											ArrayList<Card >allCardsNoDupes = listMaker(draftDecks.get(playerDeck.getSelectedIndex()));
											Card highCard = new Card("temp", 0);
											Card highCard2 = new Card("temp", 0);
											Card highCard3 = new Card("temp", 0);
											int deckScore = 0;
											int ultimates = 0;
											int ultras = 0;
											int supers = 0;
											int rares = 0;
											int commons = 0;
											for (Card card : allCardsNoDupes) 
											{
												if (card.getTierScore() > highCard.getTierScore()) { highCard = card; }
												deckScore += (card.getTierScore() * card.getQuantity());
												switch (card.getRarity())
												{
												case "Ultimate Rare":
													ultimates++; break;
												case "Ultra Rare":
													ultras++; break;
												case "Super Rare":
													supers++; break;
												case "Rare":
													rares++; break;
												case "Common":
													commons++; break;
												default:
													break;
												}
											}

											for (Card card : allCardsNoDupes)
											{
												if (card.getTierScore() < highCard.getTierScore() && card.getTierScore() > highCard2.getTierScore())
												{
													highCard2 = card;
												}
											}

											for (Card card : allCardsNoDupes)
											{
												if (card.getTierScore() < highCard2.getTierScore() && card.getTierScore() > highCard3.getTierScore())
												{
													highCard3 = card;
												}
											}

											int avgCardScore = deckScore / cardsToDraftLocal;
											JTextArea stats = new JTextArea("Deck Score: " + deckScore + "\nAverage Card Score: " + avgCardScore + "\n\nUltimate Rares: " + ultimates 
													+ "\nUltra Rares: " + ultras + "\nSuper Rares: " + supers + "\nRares: " + rares
													+ "\nCommons: " + commons + "\n\nTop 3 Cards:\n"
													+ highCard.getName() + " (" + highCard.getTierScore() + ")\n"
													+ highCard2.getName() + " (" + highCard2.getTierScore() + ")\n"
													+ highCard3.getName() + " (" + highCard3.getTierScore() + ")");

											cardViewLocal.getContentPane().add(stats);
											cardViewLocal.pack();
											cardViewLocal.setVisible(true);

										}
									});



									playerDeck.setEditable(false);
									finalFrame.getContentPane().add(viewDeckOf);
									finalFrame.getContentPane().add(viewStats);

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

							pick1Rarity.setText(threeChoiceTemp.get(0).getRarity());
							pick2Rarity.setText(threeChoiceTemp.get(1).getRarity());
							pick3Rarity.setText(threeChoiceTemp.get(2).getRarity());
							textColor(threeChoiceTemp.get(0), pick1Rarity);
							textColor(threeChoiceTemp.get(1), pick2Rarity);
							textColor(threeChoiceTemp.get(2), pick3Rarity);

							pickNumber++;
							pickCounter.setText("Pick " + pickNumber + "/" + cardsToDraftLocal);

							poolCardCount.setText("Pool: " + draftPools.get(playerDrafting).size() + "/" + defPoolSize);

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
								playerDraftLbl.setText("Player " + (playerDrafting + 1));
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
									poolCardCount.setText("Pool: " + draftPools.get(playerDrafting).size() + "/" + defPoolSize);

									pick1Rarity.setText(threeChoiceTemp.get(0).getRarity());
									pick2Rarity.setText(threeChoiceTemp.get(1).getRarity());
									pick3Rarity.setText(threeChoiceTemp.get(2).getRarity());
									textColor(threeChoiceTemp.get(0), pick1Rarity);
									textColor(threeChoiceTemp.get(1), pick2Rarity);
									textColor(threeChoiceTemp.get(2), pick3Rarity);
								}
								else
								{
									JFrame finalFrame = new JFrame();
									finalFrame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
									finalFrame.setBounds(100, 100, 496, 443);
									finalFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
									finalFrame.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

									JButton viewDeckOf = new JButton("View Deck");
									JButton viewStats = new JButton("Deck Stats");
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
											cardViewLocal.setTitle("Player " + (playerDeck.getSelectedIndex() + 1) + "'s Deck");
											cardViewLocal.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
											cardViewLocal.setBounds(100, 100, 496, 443);
											cardViewLocal.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
											ArrayList<Card >allCardsNoDupes = listMaker(draftDecks.get(playerDeck.getSelectedIndex()));
											JList list = new JList(allCardsNoDupes.toArray());
											dynamicList = list;
											KeyListener keyListener = new KeyListener()
											{
												public void keyPressed(KeyEvent e)
												{
													if (e.getKeyCode() == KeyEvent.VK_ENTER)
													{
														dynamicCard = (Card) dynamicList.getSelectedValue();
														singleCardView = new JFrame();										
														JPanel panel = new JPanel();
														boolean smallImage = false;
														JButton fullSize = new JButton("Full Resolution");
														panel.setLayout(new GridBagLayout());
														GridBagConstraints c = new GridBagConstraints();
														c.fill = GridBagConstraints.HORIZONTAL;
														ImageLabel label;
														if (isImage("src/images/" + dynamicCard.getName() + "Small.png")) 
														{ 
															label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + "Small.png"));
															smallImage = true;
														}
														else { label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png")); }
														JLabel rarity = new JLabel(dynamicCard.getRarity()); textColor(dynamicCard, rarity);
														JLabel quantity = new JLabel("Quantity: " + dynamicCard.getQuantity());
														Integer tempInt = dynamicCard.getTierScore();
														String scoreString = tempInt.toString();
														JLabel score = new JLabel("Tier Score: " + scoreString);
														JButton showText = new JButton("Card Text");
														c.insets = new Insets(5, 5, 5, 5);
														c.weightx = 0.5; c.gridx = 0; c.gridy = 0; c.gridheight = 3; panel.add(label, c);
														c.gridheight = 1; c.gridx = 0; c.gridy = 6; panel.add(rarity, c);
														rarity.setHorizontalAlignment(SwingConstants.CENTER); 
														c.gridx = 0; c.gridy = 7;  panel.add(quantity, c);
														quantity.setHorizontalAlignment(SwingConstants.CENTER);
														c.gridx = 0; c.gridy = 8;  panel.add(score, c);
														score.setHorizontalAlignment(SwingConstants.CENTER); 
														c.gridx = 0; c.gridy = 4;  panel.add(showText, c);
														showText.setHorizontalAlignment(SwingConstants.CENTER); 
														showText.setMargin( new Insets(0,0,0,0) );if (smallImage)
														{
															c.weightx = 0.5; c.gridx = 0; c.gridy = 5; panel.add(fullSize, c); 
															fullSize.setHorizontalAlignment(SwingConstants.CENTER);
															fullSize.addActionListener(new ActionListener()
															{
																@Override
																public void actionPerformed(ActionEvent e) 
																{
																	JFrame fullView = new JFrame();
																	JPanel fullPanel = new JPanel();
																	ImageLabel bigImage = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png"));
																	fullPanel.add(bigImage);
																	fullView.getContentPane().add(fullPanel);
																	fullView.pack();
																	fullView.setResizable(false);
																	fullView.setVisible(true);
																	
																}
																
															});
														}
														singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
														singleCardView.setBounds(100, 100, 496, 443);
														singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
														singleCardView.getContentPane().add(panel);
														singleCardView.pack();						
														singleCardView.setResizable(false);
														singleCardView.setVisible(true);
														
														showText.addActionListener(new ActionListener()
														{
															@Override
															public void actionPerformed(ActionEvent arg0) 
															{
																JFrame textFrame = new JFrame();
																textFrame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
																textFrame.setBounds(100, 100, 496, 443);
																textFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
																JPanel textPanel = new JPanel();
																JTextArea textField = new JTextArea(6, 40);
																textField.setEditable(false);
																String temp = dynamicCard.getText();
																String newTemp = WordUtils.wrap(temp, 75, "\n", false);
																textField.setText(newTemp);
																textField.setWrapStyleWord(true);
																JScrollPane vertScroll = new JScrollPane(textField);
																textPanel.add(vertScroll);
																textFrame.getContentPane().add(textPanel);
																textFrame.pack();
																textFrame.setResizable(false);
																textFrame.setVisible(true);
																
																
															}
														});
													
													
													}
												}

												@Override public void keyReleased(KeyEvent arg0) {} @Override public void keyTyped(KeyEvent arg0) {}
											};
											list.addKeyListener(keyListener);
											MouseListener mouseListener = new MouseAdapter() 
											{
												public void mouseClicked(MouseEvent e) 
												{
													if (e.getClickCount() == 2) 
													{    		
														dynamicCard = (Card) dynamicList.getSelectedValue();
														singleCardView = new JFrame();										
														JPanel panel = new JPanel();
														boolean smallImage = false;
														JButton fullSize = new JButton("Full Resolution");
														panel.setLayout(new GridBagLayout());
														GridBagConstraints c = new GridBagConstraints();
														c.fill = GridBagConstraints.HORIZONTAL;
														ImageLabel label;
														if (isImage("src/images/" + dynamicCard.getName() + "Small.png")) 
														{ 
															label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + "Small.png"));
															smallImage = true;
														}
														else { label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png")); }
														JLabel rarity = new JLabel(dynamicCard.getRarity()); textColor(dynamicCard, rarity);
														JLabel quantity = new JLabel("Quantity: " + dynamicCard.getQuantity());
														Integer tempInt = dynamicCard.getTierScore();
														String scoreString = tempInt.toString();
														JLabel score = new JLabel("Tier Score: " + scoreString);
														JButton showText = new JButton("Card Text");
														c.insets = new Insets(5, 5, 5, 5);
														c.weightx = 0.5; c.gridx = 0; c.gridy = 0; c.gridheight = 3; panel.add(label, c);
														c.gridheight = 1; c.gridx = 0; c.gridy = 6; panel.add(rarity, c);
														rarity.setHorizontalAlignment(SwingConstants.CENTER); 
														c.gridx = 0; c.gridy = 7;  panel.add(quantity, c);
														quantity.setHorizontalAlignment(SwingConstants.CENTER);
														c.gridx = 0; c.gridy = 8;  panel.add(score, c);
														score.setHorizontalAlignment(SwingConstants.CENTER); 
														c.gridx = 0; c.gridy = 4;  panel.add(showText, c);
														showText.setHorizontalAlignment(SwingConstants.CENTER); 
														showText.setMargin( new Insets(0,0,0,0) );if (smallImage)
														{
															c.weightx = 0.5; c.gridx = 0; c.gridy = 5; panel.add(fullSize, c); 
															fullSize.setHorizontalAlignment(SwingConstants.CENTER);
															fullSize.addActionListener(new ActionListener()
															{
																@Override
																public void actionPerformed(ActionEvent e) 
																{
																	JFrame fullView = new JFrame();
																	JPanel fullPanel = new JPanel();
																	ImageLabel bigImage = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png"));
																	fullPanel.add(bigImage);
																	fullView.getContentPane().add(fullPanel);
																	fullView.pack();
																	fullView.setResizable(false);
																	fullView.setVisible(true);
																	
																}
																
															});
														}
														singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
														singleCardView.setBounds(100, 100, 496, 443);
														singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
														singleCardView.getContentPane().add(panel);
														singleCardView.pack();						
														singleCardView.setResizable(false);
														singleCardView.setVisible(true);
														
														showText.addActionListener(new ActionListener()
														{
															@Override
															public void actionPerformed(ActionEvent arg0) 
															{
																JFrame textFrame = new JFrame();
																textFrame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
																textFrame.setBounds(100, 100, 496, 443);
																textFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
																JPanel textPanel = new JPanel();
																JTextArea textField = new JTextArea(6, 40);
																textField.setEditable(false);
																String temp = dynamicCard.getText();
																String newTemp = WordUtils.wrap(temp, 75, "\n", false);
																textField.setText(newTemp);
																textField.setWrapStyleWord(true);
																JScrollPane vertScroll = new JScrollPane(textField);
																textPanel.add(vertScroll);
																textFrame.getContentPane().add(textPanel);
																textFrame.pack();
																textFrame.setResizable(false);
																textFrame.setVisible(true);
																
																
															}
														});
													
													
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

									viewStats.addActionListener(new ActionListener() 
									{
										public void actionPerformed(ActionEvent arg0) 
										{
											JFrame cardViewLocal = new JFrame();
											cardViewLocal.setTitle("Player " + (playerDeck.getSelectedIndex() + 1) + "'s Stats");
											cardViewLocal.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
											cardViewLocal.setBounds(100, 100, 496, 443);
											cardViewLocal.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
											ArrayList<Card >allCardsNoDupes = listMaker(draftDecks.get(playerDeck.getSelectedIndex()));
											Card highCard = new Card("temp", 0);
											Card highCard2 = new Card("temp", 0);
											Card highCard3 = new Card("temp", 0);
											int deckScore = 0;
											int ultimates = 0;
											int ultras = 0;
											int supers = 0;
											int rares = 0;
											int commons = 0;
											for (Card card : allCardsNoDupes) 
											{
												if (card.getTierScore() > highCard.getTierScore()) { highCard = card; }
												deckScore += (card.getTierScore() * card.getQuantity());
												switch (card.getRarity())
												{
												case "Ultimate Rare":
													ultimates++; break;
												case "Ultra Rare":
													ultras++; break;
												case "Super Rare":
													supers++; break;
												case "Rare":
													rares++; break;
												case "Common":
													commons++; break;
												default:
													break;
												}
											}

											for (Card card : allCardsNoDupes)
											{
												if (card.getTierScore() < highCard.getTierScore() && card.getTierScore() > highCard2.getTierScore())
												{
													highCard2 = card;
												}
											}

											for (Card card : allCardsNoDupes)
											{
												if (card.getTierScore() < highCard2.getTierScore() && card.getTierScore() > highCard3.getTierScore())
												{
													highCard3 = card;
												}
											}

											int avgCardScore = deckScore / cardsToDraftLocal;
											JTextArea stats = new JTextArea("Deck Score: " + deckScore + "\nAverage Card Score: " + avgCardScore + "\n\nUltimate Rares: " + ultimates 
													+ "\nUltra Rares: " + ultras + "\nSuper Rares: " + supers + "\nRares: " + rares
													+ "\nCommons: " + commons + "\n\nTop 3 Cards:\n"
													+ highCard.getName() + " (" + highCard.getTierScore() + ")\n"
													+ highCard2.getName() + " (" + highCard2.getTierScore() + ")\n"
													+ highCard3.getName() + " (" + highCard3.getTierScore() + ")");

											cardViewLocal.getContentPane().add(stats);
											cardViewLocal.pack();
											cardViewLocal.setVisible(true);

										}
									});



									playerDeck.setEditable(false);
									finalFrame.getContentPane().add(viewDeckOf);
									finalFrame.getContentPane().add(viewStats);

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

							pick1Rarity.setText(threeChoiceTemp.get(0).getRarity());
							pick2Rarity.setText(threeChoiceTemp.get(1).getRarity());
							pick3Rarity.setText(threeChoiceTemp.get(2).getRarity());
							textColor(threeChoiceTemp.get(0), pick1Rarity);
							textColor(threeChoiceTemp.get(1), pick2Rarity);
							textColor(threeChoiceTemp.get(2), pick3Rarity);

							rerollsSoFar++;
							if (canReroll(rerollsSoFar, rerollsLocal) == false) { reroll.setEnabled(false); }

							poolCardCount.setText("Pool: " + draftPools.get(playerDrafting).size() + "/" + defPoolSize);

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
						cardViewLocal.setTitle("Player " + (playerDrafting + 1) + "'s Deck");
						cardViewLocal.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
						cardViewLocal.setBounds(100, 100, 496, 443);
						cardViewLocal.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
						ArrayList<Card >allCardsNoDupes = listMaker(drafted);
						JList list = new JList(allCardsNoDupes.toArray());
						dynamicList = list;
						KeyListener keyListener = new KeyListener()
						{
							public void keyPressed(KeyEvent e)
							{
								if (e.getKeyCode() == KeyEvent.VK_ENTER)
								{
									dynamicCard = (Card) dynamicList.getSelectedValue();
									singleCardView = new JFrame();										
									JPanel panel = new JPanel();
									boolean smallImage = false;
									JButton fullSize = new JButton("Full Resolution");
									panel.setLayout(new GridBagLayout());
									GridBagConstraints c = new GridBagConstraints();
									c.fill = GridBagConstraints.HORIZONTAL;
									ImageLabel label;
									if (isImage("src/images/" + dynamicCard.getName() + "Small.png")) 
									{ 
										label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + "Small.png"));
										smallImage = true;
									}
									else { label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png")); }
									JLabel rarity = new JLabel(dynamicCard.getRarity()); textColor(dynamicCard, rarity);
									JLabel quantity = new JLabel("Quantity: " + dynamicCard.getQuantity());
									Integer tempInt = dynamicCard.getTierScore();
									String scoreString = tempInt.toString();
									JLabel score = new JLabel("Tier Score: " + scoreString);
									JButton showText = new JButton("Card Text");
									c.insets = new Insets(5, 5, 5, 5);
									c.weightx = 0.5; c.gridx = 0; c.gridy = 0; c.gridheight = 3; panel.add(label, c);
									c.gridheight = 1; c.gridx = 0; c.gridy = 6; panel.add(rarity, c);
									rarity.setHorizontalAlignment(SwingConstants.CENTER); 
									c.gridx = 0; c.gridy = 7;  panel.add(quantity, c);
									quantity.setHorizontalAlignment(SwingConstants.CENTER);
									c.gridx = 0; c.gridy = 8;  panel.add(score, c);
									score.setHorizontalAlignment(SwingConstants.CENTER); 
									c.gridx = 0; c.gridy = 4;  panel.add(showText, c);
									showText.setHorizontalAlignment(SwingConstants.CENTER); 
									showText.setMargin( new Insets(0,0,0,0) );if (smallImage)
									{
										c.weightx = 0.5; c.gridx = 0; c.gridy = 5; panel.add(fullSize, c); 
										fullSize.setHorizontalAlignment(SwingConstants.CENTER);
										fullSize.addActionListener(new ActionListener()
										{
											@Override
											public void actionPerformed(ActionEvent e) 
											{
												JFrame fullView = new JFrame();
												JPanel fullPanel = new JPanel();
												ImageLabel bigImage = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png"));
												fullPanel.add(bigImage);
												fullView.getContentPane().add(fullPanel);
												fullView.pack();
												fullView.setResizable(false);
												fullView.setVisible(true);
												
											}
											
										});
									}
									singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
									singleCardView.setBounds(100, 100, 496, 443);
									singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
									singleCardView.getContentPane().add(panel);
									singleCardView.pack();						
									singleCardView.setResizable(false);
									singleCardView.setVisible(true);
									
									showText.addActionListener(new ActionListener()
									{
										@Override
										public void actionPerformed(ActionEvent arg0) 
										{
											JFrame textFrame = new JFrame();
											textFrame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
											textFrame.setBounds(100, 100, 496, 443);
											textFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
											JPanel textPanel = new JPanel();
											JTextArea textField = new JTextArea(6, 40);
											textField.setEditable(false);
											String temp = dynamicCard.getText();
											String newTemp = WordUtils.wrap(temp, 75, "\n", false);
											textField.setText(newTemp);
											textField.setWrapStyleWord(true);
											JScrollPane vertScroll = new JScrollPane(textField);
											textPanel.add(vertScroll);
											textFrame.getContentPane().add(textPanel);
											textFrame.pack();
											textFrame.setResizable(false);
											textFrame.setVisible(true);
											
											
										}
									});
								
								
								}
							}

							@Override public void keyReleased(KeyEvent arg0) {} @Override public void keyTyped(KeyEvent arg0) {}
						};
						list.addKeyListener(keyListener);
						MouseListener mouseListener = new MouseAdapter() 
						{
							public void mouseClicked(MouseEvent e) 
							{
								if (e.getClickCount() == 2) 
								{    		
									dynamicCard = (Card) dynamicList.getSelectedValue();
									singleCardView = new JFrame();										
									JPanel panel = new JPanel();
									boolean smallImage = false;
									JButton fullSize = new JButton("Full Resolution");
									panel.setLayout(new GridBagLayout());
									GridBagConstraints c = new GridBagConstraints();
									c.fill = GridBagConstraints.HORIZONTAL;
									ImageLabel label;
									if (isImage("src/images/" + dynamicCard.getName() + "Small.png")) 
									{ 
										label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + "Small.png"));
										smallImage = true;
									}
									else { label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png")); }
									JLabel rarity = new JLabel(dynamicCard.getRarity()); textColor(dynamicCard, rarity);
									JLabel quantity = new JLabel("Quantity: " + dynamicCard.getQuantity());
									Integer tempInt = dynamicCard.getTierScore();
									String scoreString = tempInt.toString();
									JLabel score = new JLabel("Tier Score: " + scoreString);
									JButton showText = new JButton("Card Text");
									c.insets = new Insets(5, 5, 5, 5);
									c.weightx = 0.5; c.gridx = 0; c.gridy = 0; c.gridheight = 3; panel.add(label, c);
									c.gridheight = 1; c.gridx = 0; c.gridy = 6; panel.add(rarity, c);
									rarity.setHorizontalAlignment(SwingConstants.CENTER); 
									c.gridx = 0; c.gridy = 7;  panel.add(quantity, c);
									quantity.setHorizontalAlignment(SwingConstants.CENTER);
									c.gridx = 0; c.gridy = 8;  panel.add(score, c);
									score.setHorizontalAlignment(SwingConstants.CENTER); 
									c.gridx = 0; c.gridy = 4;  panel.add(showText, c);
									showText.setHorizontalAlignment(SwingConstants.CENTER); 
									showText.setMargin( new Insets(0,0,0,0) );if (smallImage)
									{
										c.weightx = 0.5; c.gridx = 0; c.gridy = 5; panel.add(fullSize, c); 
										fullSize.setHorizontalAlignment(SwingConstants.CENTER);
										fullSize.addActionListener(new ActionListener()
										{
											@Override
											public void actionPerformed(ActionEvent e) 
											{
												JFrame fullView = new JFrame();
												JPanel fullPanel = new JPanel();
												ImageLabel bigImage = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png"));
												fullPanel.add(bigImage);
												fullView.getContentPane().add(fullPanel);
												fullView.pack();
												fullView.setResizable(false);
												fullView.setVisible(true);
												
											}
											
										});
									}
									singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
									singleCardView.setBounds(100, 100, 496, 443);
									singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
									singleCardView.getContentPane().add(panel);
									singleCardView.pack();						
									singleCardView.setResizable(false);
									singleCardView.setVisible(true);
									
									showText.addActionListener(new ActionListener()
									{
										@Override
										public void actionPerformed(ActionEvent arg0) 
										{
											JFrame textFrame = new JFrame();
											textFrame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
											textFrame.setBounds(100, 100, 496, 443);
											textFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
											JPanel textPanel = new JPanel();
											JTextArea textField = new JTextArea(6, 40);
											textField.setEditable(false);
											String temp = dynamicCard.getText();
											String newTemp = WordUtils.wrap(temp, 75, "\n", false);
											textField.setText(newTemp);
											textField.setWrapStyleWord(true);
											JScrollPane vertScroll = new JScrollPane(textField);
											textPanel.add(vertScroll);
											textFrame.getContentPane().add(textPanel);
											textFrame.pack();
											textFrame.setResizable(false);
											textFrame.setVisible(true);
											
											
										}
									});
								
								
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
				DraftInit.setVisible(false);
				DraftInit.setExtendedState(JFrame.MAXIMIZED_BOTH); 
				DraftInit.setVisible(true);
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
					if (input[8].equals("true")) { monster = true; }	else { monster = false; }
					if (input[10].equals("true")) { contin = true; }	else { contin = false; }
					if (input[11].equals("true")) { quickplay = true; }	else { quickplay = false; }
					if (input[12].equals("true")) { counter = true; }	else { counter = false; }
					if (input[13].equals("true")) { field = true; }	else { field = false; }
					if (input[14].equals("true")) { equip = true; }	else { equip = false; }
					if (input[17].equals("true")) { ritual = true; }	else { ritual = false; }
					if (input[18].equals("true")) { normal = true; }	else { normal = false; }

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
	
	// Card copy void return
	static void copyCardVoid(Card copyFrom, Card copyOver)
	{
		copyOver = new Card(copyFrom);
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
			if (temp.size() == 0) { Card tempCard2 = new Card("temp"); temp.add(tempCard2); }

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

			for (Card card : temp) { if (card.getName().equals("temp")) { temp.remove(card); } }
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
	public static int fillAllPools(ArrayList<Card> allCards, ArrayList<ArrayList<Card>> decks, int noOfPlayers)
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
	
	public static void fillAllPoolsEqual(ArrayList<Card> allCards, ArrayList<ArrayList<Card>> decks, int noOfPlayers)
	{
		int ultimates = 0; int ultras = 0; int supers = 0; int rares = 0; int commons = 0;
		for (Card card : allCards)
		{
			switch (card.getRarity())
			{
				case "Ultimate Rare":
					ultimates++;
					break;
				case "Ultra Rare":
					ultras++;
					break;
				case "Super Rare":
					supers++;
					break;
				case "Rare":
					rares++;
					break;
				case "Common":
					commons++;
					break;
				default: break;
			}
		}
				
		double ultimatesAllowed = Math.floor((ultimates / noOfPlayers) - (noOfPlayers));
		double ultrasAllowed = Math.floor((ultras / noOfPlayers) - (noOfPlayers - 2));
		double supersAllowed = Math.floor((supers / noOfPlayers) - (noOfPlayers - 6));
		double raresAllowed = Math.floor((rares / noOfPlayers) - ((noOfPlayers * 2)));
		double commonsAllowed = Math.floor((commons / noOfPlayers) - (noOfPlayers * 4) + 24);
		
		if (ultimatesAllowed < 0 ) { ultimatesAllowed = 1; }
		if (ultrasAllowed < 0 ) { ultrasAllowed = 2; }
		if (supersAllowed < 0) { supersAllowed = 8; }
		
	
		for (int i = 0, j = 0; i < (ultimatesAllowed * noOfPlayers); i++)
		{
			decks.get(j%noOfPlayers).add(randomCardRemoveRarity(allCards, "Ultimate Rare"));
			decks.get(j%noOfPlayers).get(decks.get(j%noOfPlayers).size() - 1).setQuantity(1);
			j++;
			if ((j + 1) > noOfPlayers) { j = 0; }
		}
		
		for (int i = 0, j = 0; i < (ultrasAllowed * noOfPlayers); i++)
		{
			decks.get(j%noOfPlayers).add(randomCardRemoveRarity(allCards, "Ultra Rare"));
			decks.get(j%noOfPlayers).get(decks.get(j%noOfPlayers).size() - 1).setQuantity(1);
			j++;
			if ((j + 1) > noOfPlayers) { j = 0; }
		}
		
		for (int i = 0, j = 0; i < (supersAllowed * noOfPlayers); i++)
		{
			decks.get(j%noOfPlayers).add(randomCardRemoveRarity(allCards, "Super Rare"));
			decks.get(j%noOfPlayers).get(decks.get(j%noOfPlayers).size() - 1).setQuantity(1);
			j++;
			if ((j + 1) > noOfPlayers) { j = 0; }
		}

		for (int i = 0, j = 0; i < (raresAllowed * noOfPlayers); i++)
		{
			decks.get(j%noOfPlayers).add(randomCardRemoveRarity(allCards, "Rare"));
			decks.get(j%noOfPlayers).get(decks.get(j%noOfPlayers).size() - 1).setQuantity(1);
			j++;
			if ((j + 1) > noOfPlayers) { j = 0; }
		}
		
		for (int i = 0, j = 0; i < (commonsAllowed * noOfPlayers); i++)
		{
			decks.get(j%noOfPlayers).add(randomCardRemoveRarity(allCards, "Common"));
			decks.get(j%noOfPlayers).get(decks.get(j%noOfPlayers).size() - 1).setQuantity(1);
			j++;
			if ((j + 1) > noOfPlayers) { j = 0; }
		}
	
	}
	
	public static void fillAllPoolsHarsh(ArrayList<Card> allCards, ArrayList<ArrayList<Card>> decks, int noOfPlayers)
	{		
		double ultimatesAllowed = 2;
		double ultrasAllowed = 8; if (noOfPlayers > 2) { ultrasAllowed = 4; }
		double supersAllowed = 20; if (noOfPlayers > 2 && noOfPlayers < 8) { supersAllowed = 10; } else if (noOfPlayers > 8) { supersAllowed = 5; }
		double raresAllowed = 60; if (noOfPlayers > 2 && noOfPlayers < 8) { raresAllowed = 35; } else if (noOfPlayers > 8) { raresAllowed = 15; } 
		double commonsAllowed = 400; if (noOfPlayers > 2 && noOfPlayers < 8) { commonsAllowed = 250; } else if (noOfPlayers > 8) { commonsAllowed = 100; }

	
		for (int i = 0, j = 0; i < (ultimatesAllowed * noOfPlayers); i++)
		{
			decks.get(j%noOfPlayers).add(randomCardRemoveRarity(allCards, "Ultimate Rare"));
			decks.get(j%noOfPlayers).get(decks.get(j%noOfPlayers).size() - 1).setQuantity(1);
			j++;
			if ((j + 1) > noOfPlayers) { j = 0; }
		}
		
		for (int i = 0, j = 0; i < (ultrasAllowed * noOfPlayers); i++)
		{
			decks.get(j%noOfPlayers).add(randomCardRemoveRarity(allCards, "Ultra Rare"));
			decks.get(j%noOfPlayers).get(decks.get(j%noOfPlayers).size() - 1).setQuantity(1);
			j++;
			if ((j + 1) > noOfPlayers) { j = 0; }
		}
		
		for (int i = 0, j = 0; i < (supersAllowed * noOfPlayers); i++)
		{
			decks.get(j%noOfPlayers).add(randomCardRemoveRarity(allCards, "Super Rare"));
			decks.get(j%noOfPlayers).get(decks.get(j%noOfPlayers).size() - 1).setQuantity(1);
			j++;
			if ((j + 1) > noOfPlayers) { j = 0; }
		}

		for (int i = 0, j = 0; i < (raresAllowed * noOfPlayers); i++)
		{
			decks.get(j%noOfPlayers).add(randomCardRemoveRarity(allCards, "Rare"));
			decks.get(j%noOfPlayers).get(decks.get(j%noOfPlayers).size() - 1).setQuantity(1);
			j++;
			if ((j + 1) > noOfPlayers) { j = 0; }
		}
		
		for (int i = 0, j = 0; i < (commonsAllowed * noOfPlayers); i++)
		{
			decks.get(j%noOfPlayers).add(randomCardRemoveRarity(allCards, "Common"));
			decks.get(j%noOfPlayers).get(decks.get(j%noOfPlayers).size() - 1).setQuantity(1);
			j++;
			if ((j + 1) > noOfPlayers) { j = 0; }
		}
	
	}
	
	public static void fillAllPoolsScore(ArrayList<Card> allCards, ArrayList<ArrayList<Card>> decks, int noOfPlayers)
	{
		int ultimates = 0; int ultras = 0; int supers = 0; int rares = 0; int commons = 0;
		for (Card card : allCards)
		{
			int score = card.getTierScore();
			if (score < 30) { commons++; }
			else if (score > 30 && score < 50) { rares++; }
			else if (score > 50 && score < 70) { supers++; }
			else if (score > 70 && score < 85) { ultras++; }
			else if (score > 85) { ultimates++; }
			else {}
		}
				
		double ultimatesAllowed = Math.floor((ultimates / noOfPlayers) - (noOfPlayers));
		ultimatesAllowed -= 0;
		double ultrasAllowed = Math.floor((ultras / noOfPlayers) - (noOfPlayers - 2));
		ultrasAllowed -= (110 / noOfPlayers);
		double supersAllowed = Math.floor((supers / noOfPlayers) - (noOfPlayers - 6));
		supersAllowed -= (130 / noOfPlayers);
		double raresAllowed = Math.floor((rares / noOfPlayers) - ((noOfPlayers * 2)));
		raresAllowed += (90 / noOfPlayers);
		double commonsAllowed = Math.floor((commons / noOfPlayers) - (noOfPlayers * 4) + 24);
		commonsAllowed += (120 / noOfPlayers);
		
		if (ultimatesAllowed < 0 ) { ultimatesAllowed = 1; }
		if (ultrasAllowed < 0 ) { ultrasAllowed = 2; }
		if (supersAllowed < 0) { supersAllowed = 8; }
		
	
		for (int i = 0, j = 0; i < (ultimatesAllowed * noOfPlayers); i++)
		{
			decks.get(j%noOfPlayers).add(randomCardRemoveScore(allCards, 85, 100));
			decks.get(j%noOfPlayers).get(decks.get(j%noOfPlayers).size() - 1).setQuantity(1);
			j++;
			if ((j + 1) > noOfPlayers) { j = 0; }
		}
		
		for (int i = 0, j = 0; i < (ultrasAllowed * noOfPlayers); i++)
		{
			decks.get(j%noOfPlayers).add(randomCardRemoveScore(allCards, 70, 85));
			decks.get(j%noOfPlayers).get(decks.get(j%noOfPlayers).size() - 1).setQuantity(1);
			j++;
			if ((j + 1) > noOfPlayers) { j = 0; }
		}
		
		for (int i = 0, j = 0; i < (supersAllowed * noOfPlayers); i++)
		{
			decks.get(j%noOfPlayers).add(randomCardRemoveScore(allCards, 50, 70));
			decks.get(j%noOfPlayers).get(decks.get(j%noOfPlayers).size() - 1).setQuantity(1);
			j++;
			if ((j + 1) > noOfPlayers) { j = 0; }
		}

		for (int i = 0, j = 0; i < (raresAllowed * noOfPlayers); i++)
		{
			decks.get(j%noOfPlayers).add(randomCardRemoveScore(allCards, 30, 50));
			decks.get(j%noOfPlayers).get(decks.get(j%noOfPlayers).size() - 1).setQuantity(1);
			j++;
			if ((j + 1) > noOfPlayers) { j = 0; }
		}
		
		for (int i = 0, j = 0; i < (commonsAllowed * noOfPlayers); i++)
		{
			decks.get(j%noOfPlayers).add(randomCardRemoveScore(allCards, -1, 30));
			decks.get(j%noOfPlayers).get(decks.get(j%noOfPlayers).size() - 1).setQuantity(1);
			j++;
			if ((j + 1) > noOfPlayers) { j = 0; }
		}
		
		int aUltimates = 0; int aUltras = 0; int aSupers = 0; int aRares = 0; int aCommons = 0;
		for (ArrayList<Card> deck : decks)
		{
			for (Card card : deck)
			{
				int score = card.getTierScore();
				if (score < 30) { aCommons++; }
				else if (score > 30 && score < 50) { aRares++; }
				else if (score > 50 && score < 70) { aSupers++; }
				else if (score > 70 && score < 85) { aUltras++; }
				else if (score > 85) { aUltimates++; }
				else {}
			}
		}
		
		System.out.println("Cards over 85 score per player: " + ultimatesAllowed);
		System.out.println("Cards between 70-85 per player: " + ultrasAllowed);
		System.out.println("Cards between 50-70 per player: " + supersAllowed);
		System.out.println("Cards between 30-50 per player: " + raresAllowed);
		System.out.println("Cards under 30 score per player: " + commonsAllowed);
		
		System.out.println("Cards over 85 score in pool: " + aUltimates);
		System.out.println("Cards between 70-85 in pool: " + aUltras);
		System.out.println("Cards between 50-70 in pool: " + aSupers);
		System.out.println("Cards between 30-50 in pool: " + aRares);
		System.out.println("Cards under 35 score in pool: " + aCommons);
	
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
	
	// Pulls a random card from a given pool and removes one from that pool (equal weight to all cards)
	static Card randomCardRemoveRarity(ArrayList<Card> allCards, String rarity)
	{
		ArrayList<Card> allOfRarity = new ArrayList<Card>();
		for (Card card : allCards)
		{
			if (card.getRarity().equals(rarity))
			{
				allOfRarity.add(card);
			}
		}
		Random seed = new Random();
		int seedValue = seed.nextInt(allOfRarity.size());
		Card selected = allOfRarity.get(seedValue);
		int cardIndex = allOfRarity.indexOf(selected);
		allOfRarity.get(cardIndex).setQuantity(allOfRarity.get(cardIndex).getQuantity() - 1);
		if (allOfRarity.get(cardIndex).getQuantity() <= 0) { allOfRarity.remove(cardIndex); }
		return selected;
	}
	
	// Pulls a random card from a given pool and removes one from that pool (equal weight to all cards)
	static Card randomCardRemoveScore(ArrayList<Card> allCards, int lowerBound, int upperBound)
	{
		ArrayList<Card> allOfRarity = new ArrayList<Card>();
		for (Card card : allCards)
		{
			if (card.getTierScore() > lowerBound && card.getTierScore() < upperBound)
			{
				allOfRarity.add(card);
			}
		}
		Random seed = new Random();
		int seedValue = seed.nextInt(allOfRarity.size());
		Card selected = allOfRarity.get(seedValue);
		int cardIndex = allOfRarity.indexOf(selected);
		allOfRarity.get(cardIndex).setQuantity(allOfRarity.get(cardIndex).getQuantity() - 1);
		if (allOfRarity.get(cardIndex).getQuantity() <= 0) { allOfRarity.remove(cardIndex); }
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
				if ((seed < 850) && (howManyRarity(draftPool, common) > 0)) { rolling = rarityRoll(draftPool, common, deck, temp, roll, seedValue, picks, rolling, seed); roll = common; } 								// 85% chance at common				
				else if ((seed >= 850 && seed < 925) && (howManyRarity(draftPool, rare) > 0)) { rolling = rarityRoll(draftPool, rare, deck, temp, roll, seedValue, picks, rolling, seed); roll = rare; }				// 7.5% chance at rare
				else if ((seed >= 925 && seed < 980) && (howManyRarity(draftPool, superR) > 0)) { rolling = rarityRoll(draftPool, superR, deck, temp, roll, seedValue, picks, rolling, seed); roll = superR; }			// 5.5% chance at super rare
				else if ((seed >= 980 && seed < 995) && (howManyRarity(draftPool, ultra) > 0)) { rolling = rarityRoll(draftPool, ultra, deck, temp, roll, seedValue, picks, rolling, seed); roll = ultra; }				// 1.5% chance at ultra rare
				else if ((seed >= 995 && seed <= 1000) && (howManyRarity(draftPool, ultimate) > 0)) { rolling = rarityRoll(draftPool, ultimate, deck, temp, roll, seedValue, picks, rolling, seed); roll = ultimate; }	// 0.5% chance at ultimate rare
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
				if ((seed < 850) && (howManyRarity(draftPool, common) > 0)) { rolling = rarityRoll(draftPool, common, deck, temp, roll, seedValue, picks, rolling, seed); roll = common; } 							// 69% chance at common				
				else if ((seed >= 850 && seed < 925) && (howManyRarity(draftPool, rare) > 0)) { rolling = rarityRoll(draftPool, rare, deck, temp, roll, seedValue, picks, rolling, seed); roll = rare; }			// 8% chance at rare
				else if ((seed >= 925 && seed < 980) && (howManyRarity(draftPool, superR) > 0)) { rolling = rarityRoll(draftPool, superR, deck, temp, roll, seedValue, picks, rolling, seed); roll = superR; }		// 4% chance at super rare
				else if ((seed >= 980 && seed < 995) && (howManyRarity(draftPool, ultra) > 0)) { rolling = rarityRoll(draftPool, ultra, deck, temp, roll, seedValue, picks, rolling, seed); roll = ultra; }		// 2% chance at ultra rare
				else if ((seed >= 995 && seed <= 1000) && (howManyRarity(draftPool, ultimate) > 0)) { rolling = rarityRoll(draftPool, ultimate, deck, temp, roll, seedValue, picks, rolling, seed); roll = ultimate; }	// 1% chance at ultimate rare
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

	// All the listeners for the ban: scores sub menu
	static void banScoreListenerInit(ArrayList<Card> allCards, SortingListModel banModel, JLabel lblCardCount, JLabel lblUniqueCards, JFrame DraftInit, JMenuItem lowScore,
			JMenuItem medScore, JMenuItem highScore, JMenuItem veryHighScore, JMenuItem OP)
		{
			lowScore.addActionListener(new ActionListener() 
			{
				public void actionPerformed(ActionEvent arg0) 
				{
					boolean checker = false;
					for (int a = 0; a < allCards.size(); a++)
					{
						if (allCards.get(a).getTierScore() < 30) 
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

					lowScore.setEnabled(false);
					int totalCards2 = cardCount(allCards);
					lblCardCount.setText("Cards Available: " + totalCards2);
					ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
					int totalUnique = allCardsNopeDupe.size();
					lblUniqueCards.setText("Unique Cards: " + totalUnique);
					DraftInit.revalidate(); DraftInit.repaint(); 
				}
			});
			
			medScore.addActionListener(new ActionListener() 
			{
				public void actionPerformed(ActionEvent arg0) 
				{
					boolean checker = false;
					for (int a = 0; a < allCards.size(); a++)
					{
						if (allCards.get(a).getTierScore() > 30 && allCards.get(a).getTierScore() < 60) 
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

					medScore.setEnabled(false);
					int totalCards2 = cardCount(allCards);
					lblCardCount.setText("Cards Available: " + totalCards2);
					ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
					int totalUnique = allCardsNopeDupe.size();
					lblUniqueCards.setText("Unique Cards: " + totalUnique);
					DraftInit.revalidate(); DraftInit.repaint(); 
				}
			});
			
			highScore.addActionListener(new ActionListener() 
			{
				public void actionPerformed(ActionEvent arg0) 
				{
					boolean checker = false;
					for (int a = 0; a < allCards.size(); a++)
					{
						if (allCards.get(a).getTierScore() > 60) 
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

					highScore.setEnabled(false);
					int totalCards2 = cardCount(allCards);
					lblCardCount.setText("Cards Available: " + totalCards2);
					ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
					int totalUnique = allCardsNopeDupe.size();
					lblUniqueCards.setText("Unique Cards: " + totalUnique);
					DraftInit.revalidate(); DraftInit.repaint(); 
				}
			});
			
			veryHighScore.addActionListener(new ActionListener() 
			{
				public void actionPerformed(ActionEvent arg0) 
				{
					boolean checker = false;
					for (int a = 0; a < allCards.size(); a++)
					{
						if (allCards.get(a).getTierScore() > 75) 
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

					veryHighScore.setEnabled(false);
					int totalCards2 = cardCount(allCards);
					lblCardCount.setText("Cards Available: " + totalCards2);
					ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
					int totalUnique = allCardsNopeDupe.size();
					lblUniqueCards.setText("Unique Cards: " + totalUnique);
					DraftInit.revalidate(); DraftInit.repaint(); 
				}
			});
			
			OP.addActionListener(new ActionListener() 
			{
				public void actionPerformed(ActionEvent arg0) 
				{
					boolean checker = false;
					for (int a = 0; a < allCards.size(); a++)
					{
						if (allCards.get(a).getTierScore() > 85) 
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

					OP.setEnabled(false);
					int totalCards2 = cardCount(allCards);
					lblCardCount.setText("Cards Available: " + totalCards2);
					ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
					int totalUnique = allCardsNopeDupe.size();
					lblUniqueCards.setText("Unique Cards: " + totalUnique);
					DraftInit.revalidate(); DraftInit.repaint(); 
				}
			});

		}
	
	
	// All the listeners for the ban: types sub menu
	static void banTypeListenerInit(ArrayList<Card> allCards, SortingListModel banModel, JLabel lblCardCount, JLabel lblUniqueCards, JFrame DraftInit, JMenuItem Aqua,
			JMenuItem Beast, JMenuItem BeastWarrior, JMenuItem Dinosaur, JMenuItem Divine, JMenuItem Dragon, JMenuItem Fairy, JMenuItem Fiend, JMenuItem Fish, JMenuItem Insect,
			JMenuItem Machine, JMenuItem Plant, JMenuItem Psychic, JMenuItem Pyro, JMenuItem Reptile, JMenuItem Rock, JMenuItem SeaSerpent, JMenuItem Spellcaster,
			JMenuItem Thunder, JMenuItem Warrior, JMenuItem WingedBeast, JMenuItem Wyrm, JMenuItem Zombie)
	{
		Aqua.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getType().equals("Aqua")) 
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

				Aqua.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		Beast.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getType().equals("Beast")) 
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

				Beast.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		BeastWarrior.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getType().equals("Beast-Warrior")) 
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

				BeastWarrior.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		Dinosaur.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getType().equals("Dinosaur")) 
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

				Dinosaur.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		Divine.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getType().equals("Divine-Beast")) 
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

				Divine.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		Dragon.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getType().equals("Dragon")) 
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

				Dragon.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		Fairy.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getType().equals("Fairy")) 
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

				Fairy.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		Fiend.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getType().equals("Fiend")) 
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

				Fiend.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		Fish.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getType().equals("Fish")) 
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

				Fish.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		Insect.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getType().equals("Insect")) 
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

				Insect.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		Machine.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getType().equals("Machine")) 
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

				Machine.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		Plant.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getType().equals("Plant")) 
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

				Plant.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		Psychic.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getType().equals("Psychic")) 
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

				Psychic.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		Pyro.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getType().equals("Pyro")) 
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

				Pyro.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		Reptile.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getType().equals("Reptile")) 
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

				Reptile.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		Rock.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getType().equals("Rock")) 
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

				Rock.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});


		SeaSerpent.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getType().equals("Sea Serpent")) 
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

				SeaSerpent.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		Spellcaster.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getType().equals("Spellcaster")) 
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

				Spellcaster.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		Thunder.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getType().equals("Thunder")) 
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

				Thunder.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		Warrior.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getType().equals("Warrior")) 
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

				Warrior.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		WingedBeast.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getType().equals("Winged Beast")) 
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

				WingedBeast.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		Wyrm.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getType().equals("Wyrm")) 
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

				Wyrm.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		Zombie.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getType().equals("Zombie")) 
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

				Zombie.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

	}

	
	// All the listeners for the ban: card type sub menu
		static void banCardTypeListenerInit(ArrayList<Card> allCards, SortingListModel banModel, JLabel lblCardCount, JLabel lblUniqueCards, JFrame DraftInit, JMenuItem Spell,
				JMenuItem Trap, JMenuItem Contin, JMenuItem ContinSpell, JMenuItem ContinTrap, JMenuItem Field, JMenuItem Quickplay, JMenuItem Equip, JMenuItem Counter)
		{
			Spell.addActionListener(new ActionListener() 
			{
				public void actionPerformed(ActionEvent arg0) 
				{
					boolean checker = false;                                                                                                                                                                                  
					for (int a = 0; a < allCards.size(); a++)
					{
						if (allCards.get(a).getCardType().equals("Spell")) 
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

					Spell.setEnabled(false);
					int totalCards2 = cardCount(allCards);
					lblCardCount.setText("Cards Available: " + totalCards2);
					ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
					int totalUnique = allCardsNopeDupe.size();
					lblUniqueCards.setText("Unique Cards: " + totalUnique);
					DraftInit.revalidate(); DraftInit.repaint(); 
				}
			});
			
			Trap.addActionListener(new ActionListener() 
			{
				public void actionPerformed(ActionEvent arg0) 
				{
					boolean checker = false;                                                                                                                                                                                  
					for (int a = 0; a < allCards.size(); a++)
					{
						if (allCards.get(a).getCardType().equals("Trap")) 
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

					Trap.setEnabled(false);
					int totalCards2 = cardCount(allCards);
					lblCardCount.setText("Cards Available: " + totalCards2);
					ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
					int totalUnique = allCardsNopeDupe.size();
					lblUniqueCards.setText("Unique Cards: " + totalUnique);
					DraftInit.revalidate(); DraftInit.repaint(); 
				}
			});
			
			Contin.addActionListener(new ActionListener() 
			{
				public void actionPerformed(ActionEvent arg0) 
				{
					boolean checker = false;                                                                                                                                                                                  
					for (int a = 0; a < allCards.size(); a++)
					{
						if (allCards.get(a).isContin()) 
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

					Contin.setEnabled(false);
					int totalCards2 = cardCount(allCards);
					lblCardCount.setText("Cards Available: " + totalCards2);
					ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
					int totalUnique = allCardsNopeDupe.size();
					lblUniqueCards.setText("Unique Cards: " + totalUnique);
					DraftInit.revalidate(); DraftInit.repaint(); 
				}
			});
			
			ContinSpell.addActionListener(new ActionListener() 
			{
				public void actionPerformed(ActionEvent arg0) 
				{
					boolean checker = false;                                                                                                                                                                                  
					for (int a = 0; a < allCards.size(); a++)
					{
						if ((allCards.get(a).isContin()) && (allCards.get(a).getCardType().equals("Spell"))) 
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

					ContinSpell.setEnabled(false);
					int totalCards2 = cardCount(allCards);
					lblCardCount.setText("Cards Available: " + totalCards2);
					ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
					int totalUnique = allCardsNopeDupe.size();
					lblUniqueCards.setText("Unique Cards: " + totalUnique);
					DraftInit.revalidate(); DraftInit.repaint(); 
				}
			});
			
			ContinTrap.addActionListener(new ActionListener() 
			{
				public void actionPerformed(ActionEvent arg0) 
				{
					boolean checker = false;                                                                                                                                                                                  
					for (int a = 0; a < allCards.size(); a++)
					{
						if (allCards.get(a).isContin() && allCards.get(a).getCardType().equals("Trap")) 
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

					ContinTrap.setEnabled(false);
					int totalCards2 = cardCount(allCards);
					lblCardCount.setText("Cards Available: " + totalCards2);
					ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
					int totalUnique = allCardsNopeDupe.size();
					lblUniqueCards.setText("Unique Cards: " + totalUnique);
					DraftInit.revalidate(); DraftInit.repaint(); 
				}
			});
			
			Field.addActionListener(new ActionListener() 
			{
				public void actionPerformed(ActionEvent arg0) 
				{
					boolean checker = false;                                                                                                                                                                                  
					for (int a = 0; a < allCards.size(); a++)
					{
						if (allCards.get(a).isField()) 
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

					Field.setEnabled(false);
					int totalCards2 = cardCount(allCards);
					lblCardCount.setText("Cards Available: " + totalCards2);
					ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
					int totalUnique = allCardsNopeDupe.size();
					lblUniqueCards.setText("Unique Cards: " + totalUnique);
					DraftInit.revalidate(); DraftInit.repaint(); 
				}
			});
			
			Quickplay.addActionListener(new ActionListener() 
			{
				public void actionPerformed(ActionEvent arg0) 
				{
					boolean checker = false;                                                                                                                                                                                  
					for (int a = 0; a < allCards.size(); a++)
					{
						if (allCards.get(a).isQuickplay()) 
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

					Quickplay.setEnabled(false);
					int totalCards2 = cardCount(allCards);
					lblCardCount.setText("Cards Available: " + totalCards2);
					ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
					int totalUnique = allCardsNopeDupe.size();
					lblUniqueCards.setText("Unique Cards: " + totalUnique);
					DraftInit.revalidate(); DraftInit.repaint(); 
				}
			});
			
			Equip.addActionListener(new ActionListener() 
			{
				public void actionPerformed(ActionEvent arg0) 
				{
					boolean checker = false;                                                                                                                                                                                  
					for (int a = 0; a < allCards.size(); a++)
					{
						if (allCards.get(a).isEquip()) 
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

					Equip.setEnabled(false);
					int totalCards2 = cardCount(allCards);
					lblCardCount.setText("Cards Available: " + totalCards2);
					ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
					int totalUnique = allCardsNopeDupe.size();
					lblUniqueCards.setText("Unique Cards: " + totalUnique);
					DraftInit.revalidate(); DraftInit.repaint(); 
				}
			});
			
			Counter.addActionListener(new ActionListener() 
			{
				public void actionPerformed(ActionEvent arg0) 
				{
					boolean checker = false;                                                                                                                                                                                  
					for (int a = 0; a < allCards.size(); a++)
					{
						if (allCards.get(a).isCounter()) 
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

					Counter.setEnabled(false);
					int totalCards2 = cardCount(allCards);
					lblCardCount.setText("Cards Available: " + totalCards2);
					ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
					int totalUnique = allCardsNopeDupe.size();
					lblUniqueCards.setText("Unique Cards: " + totalUnique);
					DraftInit.revalidate(); DraftInit.repaint(); 
				}
			});
		}
	
	

	// All the listeners for the ban: attributes sub menu
	static void banAttributeListenerInit(ArrayList<Card> allCards, SortingListModel banModel, JLabel lblCardCount, JLabel lblUniqueCards, JFrame DraftInit, JMenuItem Water,
			JMenuItem Fire, JMenuItem Wind, JMenuItem Light, JMenuItem Earth, JMenuItem Dark)
	{
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

		Wind.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getAttribute().equals("Wind")) 
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

				Wind.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		Light.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getAttribute().equals("Light")) 
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

				Light.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		Dark.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getAttribute().equals("Dark")) 
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

				Dark.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		Fire.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getAttribute().equals("Fire")) 
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

				Fire.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		Earth.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getAttribute().equals("Earth")) 
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

				Earth.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});
	}

	// Sets up all the ban>grouping listeners within the ban menu
	@SuppressWarnings("rawtypes")
	static void banGroupListenerInit(ArrayList<Card> allCards, JFrame DraftInit, SortingListModel banModel, JLabel lblCardCount, JLabel lblUniqueCards, 
			JMenuItem Monarchs, JMenuItem Fissure, JMenuItem TrapHole, JMenuItem Exodia, JMenuItem Naturia, JMenuItem Toon, JMenuItem Draw,
			JMenuItem Ritual, JMenuItem Fusion, JMenuItem LowAtk, JMenuItem Limited, JMenuItem SemiLimited, JMenuItem HighAtk, JMenuItem LowLvl, JMenuItem HighLvl,
			JMenuItem AncientGear, JMenuItem Archfiend, JMenuItem Crashbug, JMenuItem Destiny, JMenuItem Elemental,
			JMenuItem Flip, JMenuItem Gishki, JMenuItem God, JMenuItem Harpies, JMenuItem Hazy, JMenuItem LV,
			JMenuItem Lightsworn, JMenuItem Magnet, JMenuItem Ojama, JMenuItem SuperHeavy, JMenuItem Creator, JMenuItem Volcanic,
			JMenuItem Discard, JMenuItem EasySummon)
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

		AncientGear.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getSynergies().contains("ancient gear")) 
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

				AncientGear.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		Archfiend.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getSynergies().contains("archfiend")) 
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

				Archfiend.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		Crashbug.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getSynergies().contains("crashbug")) 
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

				Crashbug.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		Destiny.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getSynergies().contains("destiny hero")) 
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

				Destiny.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		Elemental.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getSynergies().contains("elemental hero")) 
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

				Elemental.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		Flip.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getSynergies().contains("flip")) 
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

				Flip.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		Gishki.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getSynergies().contains("gishki")) 
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

				Gishki.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		God.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getSynergies().contains("god")) 
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

				God.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		Harpies.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getSynergies().contains("harpie")) 
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

				Harpies.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		Hazy.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getSynergies().contains("hazy")) 
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

				Hazy.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		LV.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getSynergies().contains("LV")) 
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

				LV.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		Lightsworn.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getSynergies().contains("lightsworn")) 
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

				Lightsworn.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		Magnet.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getSynergies().contains("magnet warrior")) 
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

				Magnet.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		Ojama.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getSynergies().contains("ojama")) 
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

				Ojama.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		SuperHeavy.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getSynergies().contains("superheavy samurai")) 
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

				SuperHeavy.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		Creator.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getSynergies().contains("creator")) 
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

				Creator.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		Volcanic.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getSynergies().contains("volcanic")) 
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

				Volcanic.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		Discard.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if ((allCards.get(a).getSynergies().contains("player discard")) || (allCards.get(a).getSynergies().contains("opponent discard"))) 
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

				Discard.setEnabled(false);
				int totalCards2 = cardCount(allCards);
				lblCardCount.setText("Cards Available: " + totalCards2);
				ArrayList<Card> allCardsNopeDupe = listMaker(allCards);
				int totalUnique = allCardsNopeDupe.size();
				lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint(); 
			}
		});

		EasySummon.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getSynergies().contains("easy summon")) 
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

				EasySummon.setEnabled(false);
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
			JMenuItem mntmUltraRares, JMenuItem mntmSuperRares, JMenuItem mntmRares, JMenuItem mntmCommon)
	{
		mntmViewAllCards.addActionListener(new ActionListener() 
		{
			@SuppressWarnings("rawtypes")
			public void actionPerformed(ActionEvent arg0) 
			{

				JFrame cardView = new JFrame();	
				cardView.setTitle("All Cards");
				cardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
				cardView.setBounds(100, 100, 600, 600);
				cardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				
				cardView.getContentPane().setLayout(new GridBagLayout());
				GridBagConstraints c = new GridBagConstraints();
				c.fill = GridBagConstraints.HORIZONTAL;

				ArrayList<Card >allCardsNoDupes = listMaker(allCards);
				dynamicModel1.clear();
				for (Card card : allCardsNoDupes) { dynamicModel1.addElement(card); }
				dynamicList = new JList(dynamicModel1);
				JTextArea searchBar = new JTextArea(1, 15);
				JScrollPane scrollPane = new JScrollPane(dynamicList);
				JScrollPane scrollPane2 = new JScrollPane(searchBar);
				//JButton search = new JButton("Search");
				searchBar.addKeyListener(new KeyListener() 
				{
					@Override
					public void keyPressed(KeyEvent arg0) 
					{
						
						
					}

					@Override
					public void keyReleased(KeyEvent e) 
					{
						for (Card card : allCardsNoDupes)
						{
							//if (card.getName().indexOf(e.toString()) > 0) 
							if (card.getName().toLowerCase().contains(searchBar.getText().toLowerCase()))
							{
								dynamicModel1.addElement(card);
								
							} 
 						}
						
						
						
					}

					@Override
					public void keyTyped(KeyEvent arg0) 
					{
						dynamicModel1.clear();
					}
				});
				
				KeyListener keyListener = new KeyListener()
				{
					public void keyPressed(KeyEvent e)
					{
						if (e.getKeyCode() == KeyEvent.VK_ENTER)
						{
							dynamicCard = (Card) dynamicList.getSelectedValue();
							singleCardView = new JFrame();										
							JPanel panel = new JPanel();
							boolean smallImage = false;
							JButton fullSize = new JButton("Full Resolution");
							panel.setLayout(new GridBagLayout());
							GridBagConstraints c = new GridBagConstraints();
							c.fill = GridBagConstraints.HORIZONTAL;
							ImageLabel label;
							if (isImage("src/images/" + dynamicCard.getName() + "Small.png")) 
							{ 
								label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + "Small.png"));
								smallImage = true;
							}
							else { label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png")); }
							JLabel rarity = new JLabel(dynamicCard.getRarity()); textColor(dynamicCard, rarity);
							JLabel quantity = new JLabel("Quantity: " + dynamicCard.getQuantity());
							Integer tempInt = dynamicCard.getTierScore();
							String scoreString = tempInt.toString();
							JLabel score = new JLabel("Tier Score: " + scoreString);
							JButton showText = new JButton("Card Text");
							c.insets = new Insets(5, 5, 5, 5);
							c.weightx = 0.5; c.gridx = 0; c.gridy = 0; c.gridheight = 3; panel.add(label, c);
							c.gridheight = 1; c.gridx = 0; c.gridy = 6; panel.add(rarity, c);
							rarity.setHorizontalAlignment(SwingConstants.CENTER); 
							c.gridx = 0; c.gridy = 7;  panel.add(quantity, c);
							quantity.setHorizontalAlignment(SwingConstants.CENTER);
							c.gridx = 0; c.gridy = 8;  panel.add(score, c);
							score.setHorizontalAlignment(SwingConstants.CENTER); 
							c.gridx = 0; c.gridy = 4;  panel.add(showText, c);
							showText.setHorizontalAlignment(SwingConstants.CENTER); 
							showText.setMargin( new Insets(0,0,0,0) );if (smallImage)
							{
								c.weightx = 0.5; c.gridx = 0; c.gridy = 5; panel.add(fullSize, c); 
								fullSize.setHorizontalAlignment(SwingConstants.CENTER);
								fullSize.addActionListener(new ActionListener()
								{
									@Override
									public void actionPerformed(ActionEvent e) 
									{
										JFrame fullView = new JFrame();
										JPanel fullPanel = new JPanel();
										ImageLabel bigImage = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png"));
										fullPanel.add(bigImage);
										fullView.getContentPane().add(fullPanel);
										fullView.pack();
										fullView.setResizable(false);
										fullView.setVisible(true);
										
									}
									
								});
							}
							singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
							singleCardView.setBounds(100, 100, 496, 443);
							singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
							singleCardView.getContentPane().add(panel);
							singleCardView.pack();						
							singleCardView.setResizable(false);
							singleCardView.setVisible(true);
							
							showText.addActionListener(new ActionListener()
							{
								@Override
								public void actionPerformed(ActionEvent arg0) 
								{
									JFrame textFrame = new JFrame();
									textFrame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
									textFrame.setBounds(100, 100, 496, 443);
									textFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
									JPanel textPanel = new JPanel();
									JTextArea textField = new JTextArea(6, 40);
									textField.setEditable(false);
									String temp = dynamicCard.getText();
									String newTemp = WordUtils.wrap(temp, 75, "\n", false);
									textField.setText(newTemp);
									textField.setWrapStyleWord(true);
									JScrollPane vertScroll = new JScrollPane(textField);
									textPanel.add(vertScroll);
									textFrame.getContentPane().add(textPanel);
									textFrame.pack();
									textFrame.setResizable(false);
									textFrame.setVisible(true);
									
									
								}
							});
						
						
						}
						
						
					}

					@Override public void keyReleased(KeyEvent arg0) {} @Override public void keyTyped(KeyEvent arg0) {}
				};
				
				MouseListener mouseListener = new MouseAdapter() 
				{
					public void mouseClicked(MouseEvent e) 
					{
						if (e.getClickCount() == 2) 
						{    		
							dynamicCard = (Card) dynamicList.getSelectedValue();
							singleCardView = new JFrame();										
							JPanel panel = new JPanel();
							boolean smallImage = false;
							JButton fullSize = new JButton("Full Resolution");
							panel.setLayout(new GridBagLayout());
							GridBagConstraints c = new GridBagConstraints();
							c.fill = GridBagConstraints.HORIZONTAL;
							ImageLabel label;
							if (isImage("src/images/" + dynamicCard.getName() + "Small.png")) 
							{ 
								label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + "Small.png"));
								smallImage = true;
							}
							else { label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png")); }
							JLabel rarity = new JLabel(dynamicCard.getRarity()); textColor(dynamicCard, rarity);
							textColor(dynamicCard, rarity);
							JLabel quantity = new JLabel("Quantity: " + dynamicCard.getQuantity());
							Integer tempInt = dynamicCard.getTierScore();
							String scoreString = tempInt.toString();
							JLabel score = new JLabel("Tier Score: " + scoreString);
							JButton showText = new JButton("Card Text");
							c.insets = new Insets(5, 5, 5, 5);
							c.weightx = 0.5; c.gridx = 0; c.gridy = 0; c.gridheight = 3; panel.add(label, c);
							c.gridheight = 1; c.gridx = 0; c.gridy = 6; panel.add(rarity, c);
							rarity.setHorizontalAlignment(SwingConstants.CENTER); 
							c.gridx = 0; c.gridy = 7;  panel.add(quantity, c);
							quantity.setHorizontalAlignment(SwingConstants.CENTER);
							c.gridx = 0; c.gridy = 8;  panel.add(score, c);
							score.setHorizontalAlignment(SwingConstants.CENTER); 
							c.gridx = 0; c.gridy = 4;  panel.add(showText, c);
							showText.setHorizontalAlignment(SwingConstants.CENTER); 
							showText.setMargin( new Insets(0,0,0,0) );if (smallImage)
							{
								c.weightx = 0.5; c.gridx = 0; c.gridy = 5; panel.add(fullSize, c); 
								fullSize.setHorizontalAlignment(SwingConstants.CENTER);
								fullSize.addActionListener(new ActionListener()
								{
									@Override
									public void actionPerformed(ActionEvent e) 
									{
										JFrame fullView = new JFrame();
										JPanel fullPanel = new JPanel();
										ImageLabel bigImage = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png"));
										fullPanel.add(bigImage);
										fullView.getContentPane().add(fullPanel);
										fullView.pack();
										fullView.setResizable(false);
										fullView.setVisible(true);
										
									}
									
								});
							}
							singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
							singleCardView.setBounds(100, 100, 496, 443);
							singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
							singleCardView.getContentPane().add(panel);
							singleCardView.pack();						
							singleCardView.setResizable(false);
							singleCardView.setVisible(true);
							
							showText.addActionListener(new ActionListener()
							{
								@Override
								public void actionPerformed(ActionEvent arg0) 
								{
									JFrame textFrame = new JFrame();
									textFrame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
									textFrame.setBounds(100, 100, 496, 443);
									textFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
									JPanel textPanel = new JPanel();
									JTextArea textField = new JTextArea(6, 40);
									textField.setEditable(false);
									String temp = dynamicCard.getText();
									String newTemp = WordUtils.wrap(temp, 75, "\n", false);
									textField.setText(newTemp);
									textField.setWrapStyleWord(true);
									JScrollPane vertScroll = new JScrollPane(textField);
									textPanel.add(vertScroll);
									textFrame.getContentPane().add(textPanel);
									textFrame.pack();
									textFrame.setResizable(false);
									textFrame.setVisible(true);
									
									
								}
							});
						
						
						}
					}
				};
				
				
				dynamicList.addKeyListener(keyListener);
				dynamicList.addMouseListener(mouseListener);
				c.weightx = 1.0; c.gridwidth = 2; c.weighty = 1.0; c.gridx = 0; c.gridy = 0; cardView.getContentPane().add(scrollPane, c);
				c.insets = new Insets(0, 5, 0, 5); c.gridwidth = 1; c.gridheight = 4; c.gridx = 0; c.gridy = 2; cardView.getContentPane().add(scrollPane2, c);
				//c.gridx = 1; c.gridy = 2; cardView.getContentPane().add(search, c);
				cardView.pack();
				cardView.setResizable(false);
				cardView.setVisible(true);
			}
		});
		mnViewDatabase.add(mntmViewAllCards);

		mntmNewMenuItem.addActionListener(new ActionListener() 
		{
			@SuppressWarnings("rawtypes")
			public void actionPerformed(ActionEvent arg0) 
			{

				JFrame cardView = new JFrame();	
				cardView.setTitle("Ultimate Rares");
				cardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
				cardView.setBounds(100, 100, 600, 600);
				cardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				
				cardView.getContentPane().setLayout(new GridBagLayout());
				GridBagConstraints c = new GridBagConstraints();
				c.fill = GridBagConstraints.HORIZONTAL;

				ArrayList<Card >allCardsNoDupes = listMakerRarity(allCards, "Ultimate Rare");
				dynamicModel1.clear();
				for (Card card : allCardsNoDupes) { dynamicModel1.addElement(card); }
				dynamicList = new JList(dynamicModel1);
				JTextArea searchBar = new JTextArea(1, 15);
				JScrollPane scrollPane = new JScrollPane(dynamicList);
				JScrollPane scrollPane2 = new JScrollPane(searchBar);
				//JButton search = new JButton("Search");
				searchBar.addKeyListener(new KeyListener() 
				{
					@Override
					public void keyPressed(KeyEvent arg0) 
					{
						
						
					}

					@Override
					public void keyReleased(KeyEvent e) 
					{
						for (Card card : allCardsNoDupes)
						{
							//if (card.getName().indexOf(e.toString()) > 0) 
							if (card.getName().toLowerCase().contains(searchBar.getText().toLowerCase()))
							{
								dynamicModel1.addElement(card);
							} 
 						}
						
						
						
					}

					@Override
					public void keyTyped(KeyEvent arg0) 
					{
						dynamicModel1.clear();
					}
				});
				
				KeyListener keyListener = new KeyListener()
				{
					public void keyPressed(KeyEvent e)
					{
						if (e.getKeyCode() == KeyEvent.VK_ENTER)
						{
							dynamicCard = (Card) dynamicList.getSelectedValue();
							singleCardView = new JFrame();										
							JPanel panel = new JPanel();
							boolean smallImage = false;
							JButton fullSize = new JButton("Full Resolution");
							panel.setLayout(new GridBagLayout());
							GridBagConstraints c = new GridBagConstraints();
							c.fill = GridBagConstraints.HORIZONTAL;
							ImageLabel label;
							if (isImage("src/images/" + dynamicCard.getName() + "Small.png")) 
							{ 
								label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + "Small.png"));
								smallImage = true;
							}
							else { label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png")); }
							JLabel rarity = new JLabel(dynamicCard.getRarity()); textColor(dynamicCard, rarity);
							textColor(dynamicCard, rarity);
							JLabel quantity = new JLabel("Quantity: " + dynamicCard.getQuantity());
							Integer tempInt = dynamicCard.getTierScore();
							String scoreString = tempInt.toString();
							JLabel score = new JLabel("Tier Score: " + scoreString);
							JButton showText = new JButton("Card Text");
							c.insets = new Insets(5, 5, 5, 5);
							c.weightx = 0.5; c.gridx = 0; c.gridy = 0; c.gridheight = 3; panel.add(label, c);
							c.gridheight = 1; c.gridx = 0; c.gridy = 6; panel.add(rarity, c);
							rarity.setHorizontalAlignment(SwingConstants.CENTER); 
							c.gridx = 0; c.gridy = 7;  panel.add(quantity, c);
							quantity.setHorizontalAlignment(SwingConstants.CENTER);
							c.gridx = 0; c.gridy = 8;  panel.add(score, c);
							score.setHorizontalAlignment(SwingConstants.CENTER); 
							c.gridx = 0; c.gridy = 4;  panel.add(showText, c);
							showText.setHorizontalAlignment(SwingConstants.CENTER); 
							showText.setMargin( new Insets(0,0,0,0) );if (smallImage)
							{
								c.weightx = 0.5; c.gridx = 0; c.gridy = 5; panel.add(fullSize, c); 
								fullSize.setHorizontalAlignment(SwingConstants.CENTER);
								fullSize.addActionListener(new ActionListener()
								{
									@Override
									public void actionPerformed(ActionEvent e) 
									{
										JFrame fullView = new JFrame();
										JPanel fullPanel = new JPanel();
										ImageLabel bigImage = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png"));
										fullPanel.add(bigImage);
										fullView.getContentPane().add(fullPanel);
										fullView.pack();
										fullView.setResizable(false);
										fullView.setVisible(true);
										
									}
									
								});
							}
							singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
							singleCardView.setBounds(100, 100, 496, 443);
							singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
							singleCardView.getContentPane().add(panel);
							singleCardView.pack();						
							singleCardView.setResizable(false);
							singleCardView.setVisible(true);
							
							showText.addActionListener(new ActionListener()
							{
								@Override
								public void actionPerformed(ActionEvent arg0) 
								{
									JFrame textFrame = new JFrame();
									textFrame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
									textFrame.setBounds(100, 100, 496, 443);
									textFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
									JPanel textPanel = new JPanel();
									JTextArea textField = new JTextArea(6, 40);
									textField.setEditable(false);
									String temp = dynamicCard.getText();
									String newTemp = WordUtils.wrap(temp, 75, "\n", false);
									textField.setText(newTemp);
									textField.setWrapStyleWord(true);
									JScrollPane vertScroll = new JScrollPane(textField);
									textPanel.add(vertScroll);
									textFrame.getContentPane().add(textPanel);
									textFrame.pack();
									textFrame.setResizable(false);
									textFrame.setVisible(true);
									
									
								}
							});
						
						
						}
					}

					@Override public void keyReleased(KeyEvent arg0) {} @Override public void keyTyped(KeyEvent arg0) {}
				};
				
				MouseListener mouseListener = new MouseAdapter() 
				{
					public void mouseClicked(MouseEvent e) 
					{
						if (e.getClickCount() == 2) 
						{    		
							dynamicCard = (Card) dynamicList.getSelectedValue();
							singleCardView = new JFrame();										
							JPanel panel = new JPanel();
							boolean smallImage = false;
							JButton fullSize = new JButton("Full Resolution");
							panel.setLayout(new GridBagLayout());
							GridBagConstraints c = new GridBagConstraints();
							c.fill = GridBagConstraints.HORIZONTAL;
							ImageLabel label;
							if (isImage("src/images/" + dynamicCard.getName() + "Small.png")) 
							{ 
								label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + "Small.png"));
								smallImage = true;
							}
							else { label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png")); }
							JLabel rarity = new JLabel(dynamicCard.getRarity()); textColor(dynamicCard, rarity);
							JLabel quantity = new JLabel("Quantity: " + dynamicCard.getQuantity());
							Integer tempInt = dynamicCard.getTierScore();
							String scoreString = tempInt.toString();
							JLabel score = new JLabel("Tier Score: " + scoreString);
							JButton showText = new JButton("Card Text");
							c.insets = new Insets(5, 5, 5, 5);
							c.weightx = 0.5; c.gridx = 0; c.gridy = 0; c.gridheight = 3; panel.add(label, c);
							c.gridheight = 1; c.gridx = 0; c.gridy = 6; panel.add(rarity, c);
							rarity.setHorizontalAlignment(SwingConstants.CENTER); 
							c.gridx = 0; c.gridy = 7;  panel.add(quantity, c);
							quantity.setHorizontalAlignment(SwingConstants.CENTER);
							c.gridx = 0; c.gridy = 8;  panel.add(score, c);
							score.setHorizontalAlignment(SwingConstants.CENTER); 
							c.gridx = 0; c.gridy = 4;  panel.add(showText, c);
							showText.setHorizontalAlignment(SwingConstants.CENTER); 
							showText.setMargin( new Insets(0,0,0,0) );if (smallImage)
							{
								c.weightx = 0.5; c.gridx = 0; c.gridy = 5; panel.add(fullSize, c); 
								fullSize.setHorizontalAlignment(SwingConstants.CENTER);
								fullSize.addActionListener(new ActionListener()
								{
									@Override
									public void actionPerformed(ActionEvent e) 
									{
										JFrame fullView = new JFrame();
										JPanel fullPanel = new JPanel();
										ImageLabel bigImage = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png"));
										fullPanel.add(bigImage);
										fullView.getContentPane().add(fullPanel);
										fullView.pack();
										fullView.setResizable(false);
										fullView.setVisible(true);
										
									}
									
								});
							}
							singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
							singleCardView.setBounds(100, 100, 496, 443);
							singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
							singleCardView.getContentPane().add(panel);
							singleCardView.pack();						
							singleCardView.setResizable(false);
							singleCardView.setVisible(true);
							
							showText.addActionListener(new ActionListener()
							{
								@Override
								public void actionPerformed(ActionEvent arg0) 
								{
									JFrame textFrame = new JFrame();
									textFrame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
									textFrame.setBounds(100, 100, 496, 443);
									textFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
									JPanel textPanel = new JPanel();
									JTextArea textField = new JTextArea(6, 40);
									textField.setEditable(false);
									String temp = dynamicCard.getText();
									String newTemp = WordUtils.wrap(temp, 75, "\n", false);
									textField.setText(newTemp);
									textField.setWrapStyleWord(true);
									JScrollPane vertScroll = new JScrollPane(textField);
									textPanel.add(vertScroll);
									textFrame.getContentPane().add(textPanel);
									textFrame.pack();
									textFrame.setResizable(false);
									textFrame.setVisible(true);
									
									
								}
							});
						
						
						}
					}
				};
				
				
				dynamicList.addKeyListener(keyListener);
				dynamicList.addMouseListener(mouseListener);
				c.weightx = 1.0; c.gridwidth = 2; c.weighty = 1.0; c.gridx = 0; c.gridy = 0; cardView.getContentPane().add(scrollPane, c);
				c.insets = new Insets(0, 5, 0, 5); c.gridwidth = 1; c.gridheight = 4; c.gridx = 0; c.gridy = 2; cardView.getContentPane().add(scrollPane2, c);
				//c.gridx = 1; c.gridy = 2; cardView.getContentPane().add(search, c);
				cardView.pack();
				cardView.setResizable(false);
				cardView.setVisible(true);
			}
		});
		mnViewDatabase.add(mntmNewMenuItem);


		mntmUltraRares.addActionListener(new ActionListener() 
		{
			@SuppressWarnings("rawtypes")
			public void actionPerformed(ActionEvent arg0) 
			{

				JFrame cardView = new JFrame();	
				cardView.setTitle("Ultra Rares");
				cardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
				cardView.setBounds(100, 100, 600, 600);
				cardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				
				cardView.getContentPane().setLayout(new GridBagLayout());
				GridBagConstraints c = new GridBagConstraints();
				c.fill = GridBagConstraints.HORIZONTAL;

				ArrayList<Card >allCardsNoDupes = listMakerRarity(allCards, "Ultra Rare");
				dynamicModel1.clear();
				for (Card card : allCardsNoDupes) { dynamicModel1.addElement(card); }
				dynamicList = new JList(dynamicModel1);
				JTextArea searchBar = new JTextArea(1, 15);
				JScrollPane scrollPane = new JScrollPane(dynamicList);
				JScrollPane scrollPane2 = new JScrollPane(searchBar);
				//JButton search = new JButton("Search");
				searchBar.addKeyListener(new KeyListener() 
				{
					@Override
					public void keyPressed(KeyEvent arg0) 
					{
						
						
					}

					@Override
					public void keyReleased(KeyEvent e) 
					{
						for (Card card : allCardsNoDupes)
						{
							//if (card.getName().indexOf(e.toString()) > 0) 
							if (card.getName().toLowerCase().contains(searchBar.getText().toLowerCase()))
							{
								dynamicModel1.addElement(card);
								
							} 
 						}
						
						
						
					}

					@Override
					public void keyTyped(KeyEvent arg0) 
					{
						dynamicModel1.clear();
					}
				});
				
				KeyListener keyListener = new KeyListener()
				{
					public void keyPressed(KeyEvent e)
					{
						if (e.getKeyCode() == KeyEvent.VK_ENTER)
						{
							dynamicCard = (Card) dynamicList.getSelectedValue();
							singleCardView = new JFrame();										
							JPanel panel = new JPanel();
							boolean smallImage = false;
							JButton fullSize = new JButton("Full Resolution");
							panel.setLayout(new GridBagLayout());
							GridBagConstraints c = new GridBagConstraints();
							c.fill = GridBagConstraints.HORIZONTAL;
							ImageLabel label;
							if (isImage("src/images/" + dynamicCard.getName() + "Small.png")) 
							{ 
								label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + "Small.png"));
								smallImage = true;
							}
							else { label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png")); }
							JLabel rarity = new JLabel(dynamicCard.getRarity()); textColor(dynamicCard, rarity);
							JLabel quantity = new JLabel("Quantity: " + dynamicCard.getQuantity());
							Integer tempInt = dynamicCard.getTierScore();
							String scoreString = tempInt.toString();
							JLabel score = new JLabel("Tier Score: " + scoreString);
							JButton showText = new JButton("Card Text");
							c.insets = new Insets(5, 5, 5, 5);
							c.weightx = 0.5; c.gridx = 0; c.gridy = 0; c.gridheight = 3; panel.add(label, c);
							c.gridheight = 1; c.gridx = 0; c.gridy = 6; panel.add(rarity, c);
							rarity.setHorizontalAlignment(SwingConstants.CENTER); 
							c.gridx = 0; c.gridy = 7;  panel.add(quantity, c);
							quantity.setHorizontalAlignment(SwingConstants.CENTER);
							c.gridx = 0; c.gridy = 8;  panel.add(score, c);
							score.setHorizontalAlignment(SwingConstants.CENTER); 
							c.gridx = 0; c.gridy = 4;  panel.add(showText, c);
							showText.setHorizontalAlignment(SwingConstants.CENTER); 
							showText.setMargin( new Insets(0,0,0,0) );if (smallImage)
							{
								c.weightx = 0.5; c.gridx = 0; c.gridy = 5; panel.add(fullSize, c); 
								fullSize.setHorizontalAlignment(SwingConstants.CENTER);
								fullSize.addActionListener(new ActionListener()
								{
									@Override
									public void actionPerformed(ActionEvent e) 
									{
										JFrame fullView = new JFrame();
										JPanel fullPanel = new JPanel();
										ImageLabel bigImage = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png"));
										fullPanel.add(bigImage);
										fullView.getContentPane().add(fullPanel);
										fullView.pack();
										fullView.setResizable(false);
										fullView.setVisible(true);
										
									}
									
								});
							}
							singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
							singleCardView.setBounds(100, 100, 496, 443);
							singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
							singleCardView.getContentPane().add(panel);
							singleCardView.pack();						
							singleCardView.setResizable(false);
							singleCardView.setVisible(true);
							
							showText.addActionListener(new ActionListener()
							{
								@Override
								public void actionPerformed(ActionEvent arg0) 
								{
									JFrame textFrame = new JFrame();
									textFrame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
									textFrame.setBounds(100, 100, 496, 443);
									textFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
									JPanel textPanel = new JPanel();
									JTextArea textField = new JTextArea(6, 40);
									textField.setEditable(false);
									String temp = dynamicCard.getText();
									String newTemp = WordUtils.wrap(temp, 75, "\n", false);
									textField.setText(newTemp);
									textField.setWrapStyleWord(true);
									JScrollPane vertScroll = new JScrollPane(textField);
									textPanel.add(vertScroll);
									textFrame.getContentPane().add(textPanel);
									textFrame.pack();
									textFrame.setResizable(false);
									textFrame.setVisible(true);
									
									
								}
							});
						
						
						}
					}

					@Override public void keyReleased(KeyEvent arg0) {} @Override public void keyTyped(KeyEvent arg0) {}
				};
				
				MouseListener mouseListener = new MouseAdapter() 
				{
					public void mouseClicked(MouseEvent e) 
					{
						if (e.getClickCount() == 2) 
						{    		
							dynamicCard = (Card) dynamicList.getSelectedValue();
							singleCardView = new JFrame();										
							JPanel panel = new JPanel();
							boolean smallImage = false;
							JButton fullSize = new JButton("Full Resolution");
							panel.setLayout(new GridBagLayout());
							GridBagConstraints c = new GridBagConstraints();
							c.fill = GridBagConstraints.HORIZONTAL;
							ImageLabel label;
							if (isImage("src/images/" + dynamicCard.getName() + "Small.png")) 
							{ 
								label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + "Small.png"));
								smallImage = true;
							}
							else { label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png")); }
							JLabel rarity = new JLabel(dynamicCard.getRarity()); textColor(dynamicCard, rarity);
							JLabel quantity = new JLabel("Quantity: " + dynamicCard.getQuantity());
							Integer tempInt = dynamicCard.getTierScore();
							String scoreString = tempInt.toString();
							JLabel score = new JLabel("Tier Score: " + scoreString);
							JButton showText = new JButton("Card Text");
							c.insets = new Insets(5, 5, 5, 5);
							c.weightx = 0.5; c.gridx = 0; c.gridy = 0; c.gridheight = 3; panel.add(label, c);
							c.gridheight = 1; c.gridx = 0; c.gridy = 6; panel.add(rarity, c);
							rarity.setHorizontalAlignment(SwingConstants.CENTER); 
							c.gridx = 0; c.gridy = 7;  panel.add(quantity, c);
							quantity.setHorizontalAlignment(SwingConstants.CENTER);
							c.gridx = 0; c.gridy = 8;  panel.add(score, c);
							score.setHorizontalAlignment(SwingConstants.CENTER); 
							c.gridx = 0; c.gridy = 4;  panel.add(showText, c);
							showText.setHorizontalAlignment(SwingConstants.CENTER); 
							showText.setMargin( new Insets(0,0,0,0) );if (smallImage)
							{
								c.weightx = 0.5; c.gridx = 0; c.gridy = 5; panel.add(fullSize, c); 
								fullSize.setHorizontalAlignment(SwingConstants.CENTER);
								fullSize.addActionListener(new ActionListener()
								{
									@Override
									public void actionPerformed(ActionEvent e) 
									{
										JFrame fullView = new JFrame();
										JPanel fullPanel = new JPanel();
										ImageLabel bigImage = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png"));
										fullPanel.add(bigImage);
										fullView.getContentPane().add(fullPanel);
										fullView.pack();
										fullView.setResizable(false);
										fullView.setVisible(true);
										
									}
									
								});
							}
							singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
							singleCardView.setBounds(100, 100, 496, 443);
							singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
							singleCardView.getContentPane().add(panel);
							singleCardView.pack();						
							singleCardView.setResizable(false);
							singleCardView.setVisible(true);
							
							showText.addActionListener(new ActionListener()
							{
								@Override
								public void actionPerformed(ActionEvent arg0) 
								{
									JFrame textFrame = new JFrame();
									textFrame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
									textFrame.setBounds(100, 100, 496, 443);
									textFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
									JPanel textPanel = new JPanel();
									JTextArea textField = new JTextArea(6, 40);
									textField.setEditable(false);
									String temp = dynamicCard.getText();
									String newTemp = WordUtils.wrap(temp, 75, "\n", false);
									textField.setText(newTemp);
									textField.setWrapStyleWord(true);
									JScrollPane vertScroll = new JScrollPane(textField);
									textPanel.add(vertScroll);
									textFrame.getContentPane().add(textPanel);
									textFrame.pack();
									textFrame.setResizable(false);
									textFrame.setVisible(true);
									
									
								}
							});
						
						
						}
					}
				};
				
				
				dynamicList.addKeyListener(keyListener);
				dynamicList.addMouseListener(mouseListener);
				c.weightx = 1.0; c.gridwidth = 2; c.weighty = 1.0; c.gridx = 0; c.gridy = 0; cardView.getContentPane().add(scrollPane, c);
				c.insets = new Insets(0, 5, 0, 5); c.gridwidth = 1; c.gridheight = 4; c.gridx = 0; c.gridy = 2; cardView.getContentPane().add(scrollPane2, c);
				//c.gridx = 1; c.gridy = 2; cardView.getContentPane().add(search, c);
				cardView.pack();
				cardView.setResizable(false);
				cardView.setVisible(true);
			}
		});
		mnViewDatabase.add(mntmUltraRares);


		mntmSuperRares.addActionListener(new ActionListener() 
		{
			@SuppressWarnings("rawtypes")
			public void actionPerformed(ActionEvent arg0) 
			{

				JFrame cardView = new JFrame();	
				cardView.setTitle("Super Rares");
				cardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
				cardView.setBounds(100, 100, 600, 600);
				cardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				
				cardView.getContentPane().setLayout(new GridBagLayout());
				GridBagConstraints c = new GridBagConstraints();
				c.fill = GridBagConstraints.HORIZONTAL;

				ArrayList<Card >allCardsNoDupes = listMakerRarity(allCards, "Super Rare");
				dynamicModel1.clear();
				for (Card card : allCardsNoDupes) { dynamicModel1.addElement(card); }
				dynamicList = new JList(dynamicModel1);
				JTextArea searchBar = new JTextArea(1, 15);
				JScrollPane scrollPane = new JScrollPane(dynamicList);
				JScrollPane scrollPane2 = new JScrollPane(searchBar);
				//JButton search = new JButton("Search");
				searchBar.addKeyListener(new KeyListener() 
				{
					@Override
					public void keyPressed(KeyEvent arg0) 
					{
						
						
					}

					@Override
					public void keyReleased(KeyEvent e) 
					{
						for (Card card : allCardsNoDupes)
						{
							//if (card.getName().indexOf(e.toString()) > 0) 
							if (card.getName().toLowerCase().contains(searchBar.getText().toLowerCase()))
							{
								dynamicModel1.addElement(card);
								
							} 
 						}
						
						
						
					}

					@Override
					public void keyTyped(KeyEvent arg0) 
					{
						dynamicModel1.clear();
					}
				});
				
				KeyListener keyListener = new KeyListener()
				{
					public void keyPressed(KeyEvent e)
					{
						if (e.getKeyCode() == KeyEvent.VK_ENTER)
						{
							dynamicCard = (Card) dynamicList.getSelectedValue();
							singleCardView = new JFrame();										
							JPanel panel = new JPanel();
							boolean smallImage = false;
							JButton fullSize = new JButton("Full Resolution");
							panel.setLayout(new GridBagLayout());
							GridBagConstraints c = new GridBagConstraints();
							c.fill = GridBagConstraints.HORIZONTAL;
							ImageLabel label;
							if (isImage("src/images/" + dynamicCard.getName() + "Small.png")) 
							{ 
								label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + "Small.png"));
								smallImage = true;
							}
							else { label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png")); }
							JLabel rarity = new JLabel(dynamicCard.getRarity()); textColor(dynamicCard, rarity);
							JLabel quantity = new JLabel("Quantity: " + dynamicCard.getQuantity());
							Integer tempInt = dynamicCard.getTierScore();
							String scoreString = tempInt.toString();
							JLabel score = new JLabel("Tier Score: " + scoreString);
							JButton showText = new JButton("Card Text");
							c.insets = new Insets(5, 5, 5, 5);
							c.weightx = 0.5; c.gridx = 0; c.gridy = 0; c.gridheight = 3; panel.add(label, c);
							c.gridheight = 1; c.gridx = 0; c.gridy = 6; panel.add(rarity, c);
							rarity.setHorizontalAlignment(SwingConstants.CENTER); 
							c.gridx = 0; c.gridy = 7;  panel.add(quantity, c);
							quantity.setHorizontalAlignment(SwingConstants.CENTER);
							c.gridx = 0; c.gridy = 8;  panel.add(score, c);
							score.setHorizontalAlignment(SwingConstants.CENTER); 
							c.gridx = 0; c.gridy = 4;  panel.add(showText, c);
							showText.setHorizontalAlignment(SwingConstants.CENTER); 
							showText.setMargin( new Insets(0,0,0,0) );if (smallImage)
							{
								c.weightx = 0.5; c.gridx = 0; c.gridy = 5; panel.add(fullSize, c); 
								fullSize.setHorizontalAlignment(SwingConstants.CENTER);
								fullSize.addActionListener(new ActionListener()
								{
									@Override
									public void actionPerformed(ActionEvent e) 
									{
										JFrame fullView = new JFrame();
										JPanel fullPanel = new JPanel();
										ImageLabel bigImage = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png"));
										fullPanel.add(bigImage);
										fullView.getContentPane().add(fullPanel);
										fullView.pack();
										fullView.setResizable(false);
										fullView.setVisible(true);
										
									}
									
								});
							}
							singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
							singleCardView.setBounds(100, 100, 496, 443);
							singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
							singleCardView.getContentPane().add(panel);
							singleCardView.pack();						
							singleCardView.setResizable(false);
							singleCardView.setVisible(true);
							
							showText.addActionListener(new ActionListener()
							{
								@Override
								public void actionPerformed(ActionEvent arg0) 
								{
									JFrame textFrame = new JFrame();
									textFrame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
									textFrame.setBounds(100, 100, 496, 443);
									textFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
									JPanel textPanel = new JPanel();
									JTextArea textField = new JTextArea(6, 40);
									textField.setEditable(false);
									String temp = dynamicCard.getText();
									String newTemp = WordUtils.wrap(temp, 75, "\n", false);
									textField.setText(newTemp);
									textField.setWrapStyleWord(true);
									JScrollPane vertScroll = new JScrollPane(textField);
									textPanel.add(vertScroll);
									textFrame.getContentPane().add(textPanel);
									textFrame.pack();
									textFrame.setResizable(false);
									textFrame.setVisible(true);
									
									
								}
							});
						
						
						}
					}

					@Override public void keyReleased(KeyEvent arg0) {} @Override public void keyTyped(KeyEvent arg0) {}
				};
				
				MouseListener mouseListener = new MouseAdapter() 
				{
					public void mouseClicked(MouseEvent e) 
					{
						if (e.getClickCount() == 2) 
						{    		
							dynamicCard = (Card) dynamicList.getSelectedValue();
							singleCardView = new JFrame();										
							JPanel panel = new JPanel();
							boolean smallImage = false;
							JButton fullSize = new JButton("Full Resolution");
							panel.setLayout(new GridBagLayout());
							GridBagConstraints c = new GridBagConstraints();
							c.fill = GridBagConstraints.HORIZONTAL;
							ImageLabel label;
							if (isImage("src/images/" + dynamicCard.getName() + "Small.png")) 
							{ 
								label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + "Small.png"));
								smallImage = true;
							}
							else { label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png")); }
							JLabel rarity = new JLabel(dynamicCard.getRarity()); textColor(dynamicCard, rarity);
							JLabel quantity = new JLabel("Quantity: " + dynamicCard.getQuantity());
							Integer tempInt = dynamicCard.getTierScore();
							String scoreString = tempInt.toString();
							JLabel score = new JLabel("Tier Score: " + scoreString);
							JButton showText = new JButton("Card Text");
							c.insets = new Insets(5, 5, 5, 5);
							c.weightx = 0.5; c.gridx = 0; c.gridy = 0; c.gridheight = 3; panel.add(label, c);
							c.gridheight = 1; c.gridx = 0; c.gridy = 6; panel.add(rarity, c);
							rarity.setHorizontalAlignment(SwingConstants.CENTER); 
							c.gridx = 0; c.gridy = 7;  panel.add(quantity, c);
							quantity.setHorizontalAlignment(SwingConstants.CENTER);
							c.gridx = 0; c.gridy = 8;  panel.add(score, c);
							score.setHorizontalAlignment(SwingConstants.CENTER); 
							c.gridx = 0; c.gridy = 4;  panel.add(showText, c);
							showText.setHorizontalAlignment(SwingConstants.CENTER); 
							showText.setMargin( new Insets(0,0,0,0) );if (smallImage)
							{
								c.weightx = 0.5; c.gridx = 0; c.gridy = 5; panel.add(fullSize, c); 
								fullSize.setHorizontalAlignment(SwingConstants.CENTER);
								fullSize.addActionListener(new ActionListener()
								{
									@Override
									public void actionPerformed(ActionEvent e) 
									{
										JFrame fullView = new JFrame();
										JPanel fullPanel = new JPanel();
										ImageLabel bigImage = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png"));
										fullPanel.add(bigImage);
										fullView.getContentPane().add(fullPanel);
										fullView.pack();
										fullView.setResizable(false);
										fullView.setVisible(true);
										
									}
									
								});
							}
							singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
							singleCardView.setBounds(100, 100, 496, 443);
							singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
							singleCardView.getContentPane().add(panel);
							singleCardView.pack();						
							singleCardView.setResizable(false);
							singleCardView.setVisible(true);
							
							showText.addActionListener(new ActionListener()
							{
								@Override
								public void actionPerformed(ActionEvent arg0) 
								{
									JFrame textFrame = new JFrame();
									textFrame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
									textFrame.setBounds(100, 100, 496, 443);
									textFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
									JPanel textPanel = new JPanel();
									JTextArea textField = new JTextArea(6, 40);
									textField.setEditable(false);
									String temp = dynamicCard.getText();
									String newTemp = WordUtils.wrap(temp, 75, "\n", false);
									textField.setText(newTemp);
									textField.setWrapStyleWord(true);
									JScrollPane vertScroll = new JScrollPane(textField);
									textPanel.add(vertScroll);
									textFrame.getContentPane().add(textPanel);
									textFrame.pack();
									textFrame.setResizable(false);
									textFrame.setVisible(true);
									
									
								}
							});
						
						
						}
					}
				};
				
				
				dynamicList.addKeyListener(keyListener);
				dynamicList.addMouseListener(mouseListener);
				c.weightx = 1.0; c.gridwidth = 2; c.weighty = 1.0; c.gridx = 0; c.gridy = 0; cardView.getContentPane().add(scrollPane, c);
				c.insets = new Insets(0, 5, 0, 5); c.gridwidth = 1; c.gridheight = 4; c.gridx = 0; c.gridy = 2; cardView.getContentPane().add(scrollPane2, c);
				//c.gridx = 1; c.gridy = 2; cardView.getContentPane().add(search, c);
				cardView.pack();
				cardView.setResizable(false);
				cardView.setVisible(true);
			}
		});
		mnViewDatabase.add(mntmSuperRares);



		mntmRares.addActionListener(new ActionListener() 
		{
			@SuppressWarnings("rawtypes")
			public void actionPerformed(ActionEvent arg0) 
			{

				JFrame cardView = new JFrame();	
				cardView.setTitle("Rares");
				cardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
				cardView.setBounds(100, 100, 600, 600);
				cardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				
				cardView.getContentPane().setLayout(new GridBagLayout());
				GridBagConstraints c = new GridBagConstraints();
				c.fill = GridBagConstraints.HORIZONTAL;

				ArrayList<Card >allCardsNoDupes = listMakerRarity(allCards, "Rare");
				dynamicModel1.clear();
				for (Card card : allCardsNoDupes) { dynamicModel1.addElement(card); }
				dynamicList = new JList(dynamicModel1);
				JTextArea searchBar = new JTextArea(1, 15);
				JScrollPane scrollPane = new JScrollPane(dynamicList);
				JScrollPane scrollPane2 = new JScrollPane(searchBar);
				//JButton search = new JButton("Search");
				searchBar.addKeyListener(new KeyListener() 
				{
					@Override
					public void keyPressed(KeyEvent arg0) 
					{
						
						
					}

					@Override
					public void keyReleased(KeyEvent e) 
					{
						for (Card card : allCardsNoDupes)
						{
							//if (card.getName().indexOf(e.toString()) > 0) 
							if (card.getName().toLowerCase().contains(searchBar.getText().toLowerCase()))
							{
								dynamicModel1.addElement(card);
								
							} 
 						}
						
						
						
					}

					@Override
					public void keyTyped(KeyEvent arg0) 
					{
						dynamicModel1.clear();
					}
				});
				
				KeyListener keyListener = new KeyListener()
				{
					public void keyPressed(KeyEvent e)
					{
						if (e.getKeyCode() == KeyEvent.VK_ENTER)
						{
							dynamicCard = (Card) dynamicList.getSelectedValue();
							singleCardView = new JFrame();										
							JPanel panel = new JPanel();
							boolean smallImage = false;
							JButton fullSize = new JButton("Full Resolution");
							panel.setLayout(new GridBagLayout());
							GridBagConstraints c = new GridBagConstraints();
							c.fill = GridBagConstraints.HORIZONTAL;
							ImageLabel label;
							if (isImage("src/images/" + dynamicCard.getName() + "Small.png")) 
							{ 
								label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + "Small.png"));
								smallImage = true;
							}
							else { label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png")); }
							JLabel rarity = new JLabel(dynamicCard.getRarity()); textColor(dynamicCard, rarity);
							JLabel quantity = new JLabel("Quantity: " + dynamicCard.getQuantity());
							Integer tempInt = dynamicCard.getTierScore();
							String scoreString = tempInt.toString();
							JLabel score = new JLabel("Tier Score: " + scoreString);
							JButton showText = new JButton("Card Text");
							c.insets = new Insets(5, 5, 5, 5);
							c.weightx = 0.5; c.gridx = 0; c.gridy = 0; c.gridheight = 3; panel.add(label, c);
							c.gridheight = 1; c.gridx = 0; c.gridy = 6; panel.add(rarity, c);
							rarity.setHorizontalAlignment(SwingConstants.CENTER); 
							c.gridx = 0; c.gridy = 7;  panel.add(quantity, c);
							quantity.setHorizontalAlignment(SwingConstants.CENTER);
							c.gridx = 0; c.gridy = 8;  panel.add(score, c);
							score.setHorizontalAlignment(SwingConstants.CENTER); 
							c.gridx = 0; c.gridy = 4;  panel.add(showText, c);
							showText.setHorizontalAlignment(SwingConstants.CENTER); 
							showText.setMargin( new Insets(0,0,0,0) );if (smallImage)
							{
								c.weightx = 0.5; c.gridx = 0; c.gridy = 5; panel.add(fullSize, c); 
								fullSize.setHorizontalAlignment(SwingConstants.CENTER);
								fullSize.addActionListener(new ActionListener()
								{
									@Override
									public void actionPerformed(ActionEvent e) 
									{
										JFrame fullView = new JFrame();
										JPanel fullPanel = new JPanel();
										ImageLabel bigImage = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png"));
										fullPanel.add(bigImage);
										fullView.getContentPane().add(fullPanel);
										fullView.pack();
										fullView.setResizable(false);
										fullView.setVisible(true);
										
									}
									
								});
							}
							singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
							singleCardView.setBounds(100, 100, 496, 443);
							singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
							singleCardView.getContentPane().add(panel);
							singleCardView.pack();						
							singleCardView.setResizable(false);
							singleCardView.setVisible(true);
							
							showText.addActionListener(new ActionListener()
							{
								@Override
								public void actionPerformed(ActionEvent arg0) 
								{
									JFrame textFrame = new JFrame();
									textFrame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
									textFrame.setBounds(100, 100, 496, 443);
									textFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
									JPanel textPanel = new JPanel();
									JTextArea textField = new JTextArea(6, 40);
									textField.setEditable(false);
									String temp = dynamicCard.getText();
									String newTemp = WordUtils.wrap(temp, 75, "\n", false);
									textField.setText(newTemp);
									textField.setWrapStyleWord(true);
									JScrollPane vertScroll = new JScrollPane(textField);
									textPanel.add(vertScroll);
									textFrame.getContentPane().add(textPanel);
									textFrame.pack();
									textFrame.setResizable(false);
									textFrame.setVisible(true);
									
									
								}
							});
						
						
						}
					}

					@Override public void keyReleased(KeyEvent arg0) {} @Override public void keyTyped(KeyEvent arg0) {}
				};
				
				MouseListener mouseListener = new MouseAdapter() 
				{
					public void mouseClicked(MouseEvent e) 
					{
						if (e.getClickCount() == 2) 
						{    		
							dynamicCard = (Card) dynamicList.getSelectedValue();
							singleCardView = new JFrame();										
							JPanel panel = new JPanel();
							boolean smallImage = false;
							JButton fullSize = new JButton("Full Resolution");
							panel.setLayout(new GridBagLayout());
							GridBagConstraints c = new GridBagConstraints();
							c.fill = GridBagConstraints.HORIZONTAL;
							ImageLabel label;
							if (isImage("src/images/" + dynamicCard.getName() + "Small.png")) 
							{ 
								label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + "Small.png"));
								smallImage = true;
							}
							else { label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png")); }
							JLabel rarity = new JLabel(dynamicCard.getRarity()); textColor(dynamicCard, rarity);
							JLabel quantity = new JLabel("Quantity: " + dynamicCard.getQuantity());
							Integer tempInt = dynamicCard.getTierScore();
							String scoreString = tempInt.toString();
							JLabel score = new JLabel("Tier Score: " + scoreString);
							JButton showText = new JButton("Card Text");
							c.insets = new Insets(5, 5, 5, 5);
							c.weightx = 0.5; c.gridx = 0; c.gridy = 0; c.gridheight = 3; panel.add(label, c);
							c.gridheight = 1; c.gridx = 0; c.gridy = 6; panel.add(rarity, c);
							rarity.setHorizontalAlignment(SwingConstants.CENTER); 
							c.gridx = 0; c.gridy = 7;  panel.add(quantity, c);
							quantity.setHorizontalAlignment(SwingConstants.CENTER);
							c.gridx = 0; c.gridy = 8;  panel.add(score, c);
							score.setHorizontalAlignment(SwingConstants.CENTER); 
							c.gridx = 0; c.gridy = 4;  panel.add(showText, c);
							showText.setHorizontalAlignment(SwingConstants.CENTER); 
							showText.setMargin( new Insets(0,0,0,0) );if (smallImage)
							{
								c.weightx = 0.5; c.gridx = 0; c.gridy = 5; panel.add(fullSize, c); 
								fullSize.setHorizontalAlignment(SwingConstants.CENTER);
								fullSize.addActionListener(new ActionListener()
								{
									@Override
									public void actionPerformed(ActionEvent e) 
									{
										JFrame fullView = new JFrame();
										JPanel fullPanel = new JPanel();
										ImageLabel bigImage = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png"));
										fullPanel.add(bigImage);
										fullView.getContentPane().add(fullPanel);
										fullView.pack();
										fullView.setResizable(false);
										fullView.setVisible(true);
										
									}
									
								});
							}
							singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
							singleCardView.setBounds(100, 100, 496, 443);
							singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
							singleCardView.getContentPane().add(panel);
							singleCardView.pack();						
							singleCardView.setResizable(false);
							singleCardView.setVisible(true);
							
							showText.addActionListener(new ActionListener()
							{
								@Override
								public void actionPerformed(ActionEvent arg0) 
								{
									JFrame textFrame = new JFrame();
									textFrame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
									textFrame.setBounds(100, 100, 496, 443);
									textFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
									JPanel textPanel = new JPanel();
									JTextArea textField = new JTextArea(6, 40);
									textField.setEditable(false);
									String temp = dynamicCard.getText();
									String newTemp = WordUtils.wrap(temp, 75, "\n", false);
									textField.setText(newTemp);
									textField.setWrapStyleWord(true);
									JScrollPane vertScroll = new JScrollPane(textField);
									textPanel.add(vertScroll);
									textFrame.getContentPane().add(textPanel);
									textFrame.pack();
									textFrame.setResizable(false);
									textFrame.setVisible(true);
									
									
								}
							});
						
						
						}
					}
				};
				
				
				dynamicList.addKeyListener(keyListener);
				dynamicList.addMouseListener(mouseListener);
				c.weightx = 1.0; c.gridwidth = 2; c.weighty = 1.0; c.gridx = 0; c.gridy = 0; cardView.getContentPane().add(scrollPane, c);
				c.insets = new Insets(0, 5, 0, 5); c.gridwidth = 1; c.gridheight = 4; c.gridx = 0; c.gridy = 2; cardView.getContentPane().add(scrollPane2, c);
				//c.gridx = 1; c.gridy = 2; cardView.getContentPane().add(search, c);
				cardView.pack();
				cardView.setResizable(false);
				cardView.setVisible(true);
			}
		});
		mnViewDatabase.add(mntmRares);


		mntmCommon.addActionListener(new ActionListener() 
		{
			@SuppressWarnings("rawtypes")
			public void actionPerformed(ActionEvent arg0) 
			{

				JFrame cardView = new JFrame();	
				cardView.setTitle("Commons");
				cardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
				cardView.setBounds(100, 100, 600, 600);
				cardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				
				cardView.getContentPane().setLayout(new GridBagLayout());
				GridBagConstraints c = new GridBagConstraints();
				c.fill = GridBagConstraints.HORIZONTAL;

				ArrayList<Card >allCardsNoDupes = listMakerRarity(allCards, "Common");
				dynamicModel1.clear();
				for (Card card : allCardsNoDupes) { dynamicModel1.addElement(card); }
				dynamicList = new JList(dynamicModel1);
				JTextArea searchBar = new JTextArea(1, 15);
				JScrollPane scrollPane = new JScrollPane(dynamicList);
				JScrollPane scrollPane2 = new JScrollPane(searchBar);
				//JButton search = new JButton("Search");
				searchBar.addKeyListener(new KeyListener() 
				{
					@Override
					public void keyPressed(KeyEvent arg0) 
					{
						
						
					}

					@Override
					public void keyReleased(KeyEvent e) 
					{
						for (Card card : allCardsNoDupes)
						{
							//if (card.getName().indexOf(e.toString()) > 0) 
							if (card.getName().toLowerCase().contains(searchBar.getText().toLowerCase()))
							{
								dynamicModel1.addElement(card);
								
							} 
 						}
						
						
						
					}

					@Override
					public void keyTyped(KeyEvent arg0) 
					{
						dynamicModel1.clear();
					}
				});
				
				KeyListener keyListener = new KeyListener()
				{
					public void keyPressed(KeyEvent e)
					{
						if (e.getKeyCode() == KeyEvent.VK_ENTER)
						{
							dynamicCard = (Card) dynamicList.getSelectedValue();
							singleCardView = new JFrame();										
							JPanel panel = new JPanel();
							boolean smallImage = false;
							JButton fullSize = new JButton("Full Resolution");
							panel.setLayout(new GridBagLayout());
							GridBagConstraints c = new GridBagConstraints();
							c.fill = GridBagConstraints.HORIZONTAL;
							ImageLabel label;
							if (isImage("src/images/" + dynamicCard.getName() + "Small.png")) 
							{ 
								label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + "Small.png"));
								smallImage = true;
							}
							else { label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png")); }
							JLabel rarity = new JLabel(dynamicCard.getRarity()); textColor(dynamicCard, rarity);
							JLabel quantity = new JLabel("Quantity: " + dynamicCard.getQuantity());
							Integer tempInt = dynamicCard.getTierScore();
							String scoreString = tempInt.toString();
							JLabel score = new JLabel("Tier Score: " + scoreString);
							JButton showText = new JButton("Card Text");
							c.insets = new Insets(5, 5, 5, 5);
							c.weightx = 0.5; c.gridx = 0; c.gridy = 0; c.gridheight = 3; panel.add(label, c);
							c.gridheight = 1; c.gridx = 0; c.gridy = 6; panel.add(rarity, c);
							rarity.setHorizontalAlignment(SwingConstants.CENTER); 
							c.gridx = 0; c.gridy = 7;  panel.add(quantity, c);
							quantity.setHorizontalAlignment(SwingConstants.CENTER);
							c.gridx = 0; c.gridy = 8;  panel.add(score, c);
							score.setHorizontalAlignment(SwingConstants.CENTER); 
							c.gridx = 0; c.gridy = 4;  panel.add(showText, c);
							showText.setHorizontalAlignment(SwingConstants.CENTER); 
							showText.setMargin( new Insets(0,0,0,0) );if (smallImage)
							{
								c.weightx = 0.5; c.gridx = 0; c.gridy = 5; panel.add(fullSize, c); 
								fullSize.setHorizontalAlignment(SwingConstants.CENTER);
								fullSize.addActionListener(new ActionListener()
								{
									@Override
									public void actionPerformed(ActionEvent e) 
									{
										JFrame fullView = new JFrame();
										JPanel fullPanel = new JPanel();
										ImageLabel bigImage = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png"));
										fullPanel.add(bigImage);
										fullView.getContentPane().add(fullPanel);
										fullView.pack();
										fullView.setResizable(false);
										fullView.setVisible(true);
										
									}
									
								});
							}
							singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
							singleCardView.setBounds(100, 100, 496, 443);
							singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
							singleCardView.getContentPane().add(panel);
							singleCardView.pack();						
							singleCardView.setResizable(false);
							singleCardView.setVisible(true);
							
							showText.addActionListener(new ActionListener()
							{
								@Override
								public void actionPerformed(ActionEvent arg0) 
								{
									JFrame textFrame = new JFrame();
									textFrame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
									textFrame.setBounds(100, 100, 496, 443);
									textFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
									JPanel textPanel = new JPanel();
									JTextArea textField = new JTextArea(6, 40);
									textField.setEditable(false);
									String temp = dynamicCard.getText();
									String newTemp = WordUtils.wrap(temp, 75, "\n", false);
									textField.setText(newTemp);
									textField.setWrapStyleWord(true);
									JScrollPane vertScroll = new JScrollPane(textField);
									textPanel.add(vertScroll);
									textFrame.getContentPane().add(textPanel);
									textFrame.pack();
									textFrame.setResizable(false);
									textFrame.setVisible(true);
									
									
								}
							});
						
						
						}
					}

					@Override public void keyReleased(KeyEvent arg0) {} @Override public void keyTyped(KeyEvent arg0) {}
				};
				
				MouseListener mouseListener = new MouseAdapter() 
				{
					public void mouseClicked(MouseEvent e) 
					{
						if (e.getClickCount() == 2) 
						{    		
							dynamicCard = (Card) dynamicList.getSelectedValue();
							singleCardView = new JFrame();										
							JPanel panel = new JPanel();
							boolean smallImage = false;
							JButton fullSize = new JButton("Full Resolution");
							panel.setLayout(new GridBagLayout());
							GridBagConstraints c = new GridBagConstraints();
							c.fill = GridBagConstraints.HORIZONTAL;
							ImageLabel label;
							if (isImage("src/images/" + dynamicCard.getName() + "Small.png")) 
							{ 
								label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + "Small.png"));
								smallImage = true;
							}
							else { label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png")); }
							JLabel rarity = new JLabel(dynamicCard.getRarity()); textColor(dynamicCard, rarity);
							JLabel quantity = new JLabel("Quantity: " + dynamicCard.getQuantity());
							Integer tempInt = dynamicCard.getTierScore();
							String scoreString = tempInt.toString();
							JLabel score = new JLabel("Tier Score: " + scoreString);
							JButton showText = new JButton("Card Text");
							c.insets = new Insets(5, 5, 5, 5);
							c.weightx = 0.5; c.gridx = 0; c.gridy = 0; c.gridheight = 3; panel.add(label, c);
							c.gridheight = 1; c.gridx = 0; c.gridy = 6; panel.add(rarity, c);
							rarity.setHorizontalAlignment(SwingConstants.CENTER); 
							c.gridx = 0; c.gridy = 7;  panel.add(quantity, c);
							quantity.setHorizontalAlignment(SwingConstants.CENTER);
							c.gridx = 0; c.gridy = 8;  panel.add(score, c);
							score.setHorizontalAlignment(SwingConstants.CENTER); 
							c.gridx = 0; c.gridy = 4;  panel.add(showText, c);
							showText.setHorizontalAlignment(SwingConstants.CENTER); 
							showText.setMargin( new Insets(0,0,0,0) );if (smallImage)
							{
								c.weightx = 0.5; c.gridx = 0; c.gridy = 5; panel.add(fullSize, c); 
								fullSize.setHorizontalAlignment(SwingConstants.CENTER);
								fullSize.addActionListener(new ActionListener()
								{
									@Override
									public void actionPerformed(ActionEvent e) 
									{
										JFrame fullView = new JFrame();
										JPanel fullPanel = new JPanel();
										ImageLabel bigImage = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png"));
										fullPanel.add(bigImage);
										fullView.getContentPane().add(fullPanel);
										fullView.pack();
										fullView.setResizable(false);
										fullView.setVisible(true);
										
									}
									
								});
							}
							singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
							singleCardView.setBounds(100, 100, 496, 443);
							singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
							singleCardView.getContentPane().add(panel);
							singleCardView.pack();						
							singleCardView.setResizable(false);
							singleCardView.setVisible(true);
							
							showText.addActionListener(new ActionListener()
							{
								@Override
								public void actionPerformed(ActionEvent arg0) 
								{
									JFrame textFrame = new JFrame();
									textFrame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
									textFrame.setBounds(100, 100, 496, 443);
									textFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
									JPanel textPanel = new JPanel();
									JTextArea textField = new JTextArea(6, 40);
									textField.setEditable(false);
									String temp = dynamicCard.getText();
									String newTemp = WordUtils.wrap(temp, 75, "\n", false);
									textField.setText(newTemp);
									textField.setWrapStyleWord(true);
									JScrollPane vertScroll = new JScrollPane(textField);
									textPanel.add(vertScroll);
									textFrame.getContentPane().add(textPanel);
									textFrame.pack();
									textFrame.setResizable(false);
									textFrame.setVisible(true);
									
									
								}
							});
						
						
						}
					}
				};
				
				
				dynamicList.addKeyListener(keyListener);
				dynamicList.addMouseListener(mouseListener);
				c.weightx = 1.0; c.gridwidth = 2; c.weighty = 1.0; c.gridx = 0; c.gridy = 0; cardView.getContentPane().add(scrollPane, c);
				c.insets = new Insets(0, 5, 0, 5); c.gridwidth = 1; c.gridheight = 4; c.gridx = 0; c.gridy = 2; cardView.getContentPane().add(scrollPane2, c);
				//c.gridx = 1; c.gridy = 2; cardView.getContentPane().add(search, c);
				cardView.pack();
				cardView.setResizable(false);
				cardView.setVisible(true);
			}
		});
		mnViewDatabase.add(mntmCommon);
	}
	
	// Setup the view database listeners the first time when the program opens
		public static void viewDatabaseListenerInit2(JMenu mnViewDatabase, JMenuItem mntmViewAllCards, JMenuItem mntmNewMenuItem, ArrayList<Card> draftAllCards, 
				JMenuItem mntmUltraRares, JMenuItem mntmSuperRares, JMenuItem mntmRares, JMenuItem mntmCommon, boolean urItem, boolean ulrItem, boolean srItem, 
				boolean rItem, boolean cItem)
		{
			
			mnViewDatabase.removeAll();
			
			
			
			mntmViewAllCards.addActionListener(new ActionListener() 
			{
				@SuppressWarnings("rawtypes")
				public void actionPerformed(ActionEvent arg0) 
				{

					JFrame cardView = new JFrame();	
					cardView.setTitle("All Cards");
					cardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
					cardView.setBounds(100, 100, 600, 600);
					cardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
					cardView.getContentPane().setLayout(new GridBagLayout());
					GridBagConstraints c = new GridBagConstraints();
					c.fill = GridBagConstraints.HORIZONTAL;

					ArrayList<Card >allCardsNoDupes = listMaker(draftAllCards);
					dynamicModel1.clear();
					for (Card card : allCardsNoDupes) { dynamicModel1.addElement(card); }
					dynamicList = new JList(dynamicModel1);
					JTextArea searchBar = new JTextArea(1, 15);
					JScrollPane scrollPane = new JScrollPane(dynamicList);
					JScrollPane scrollPane2 = new JScrollPane(searchBar);
					//JButton search = new JButton("Search");
					searchBar.addKeyListener(new KeyListener() 
					{
						@Override
						public void keyPressed(KeyEvent arg0) 
						{
							
							
						}

						@Override
						public void keyReleased(KeyEvent e) 
						{
							for (Card card : allCardsNoDupes)
							{
								//if (card.getName().indexOf(e.toString()) > 0) 
								if (card.getName().toLowerCase().contains(searchBar.getText().toLowerCase()))
								{
									dynamicModel1.addElement(card);
									
								} 
	 						}
							
							
							
						}

						@Override
						public void keyTyped(KeyEvent arg0) 
						{
							dynamicModel1.clear();
						}
					});
					
					KeyListener keyListener = new KeyListener()
					{
						public void keyPressed(KeyEvent e)
						{
							if (e.getKeyCode() == KeyEvent.VK_ENTER)
							{
								dynamicCard = (Card) dynamicList.getSelectedValue();
								singleCardView = new JFrame();		
								JPanel panel = new JPanel();
								boolean smallImage = false;
								JButton fullSize = new JButton("Full Resolution");
								panel.setLayout(new GridBagLayout());
								GridBagConstraints c = new GridBagConstraints();
								c.fill = GridBagConstraints.HORIZONTAL;
								ImageLabel label;
								if (isImage("src/images/" + dynamicCard.getName() + "Small.png")) 
								{ 
									label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + "Small.png"));
									smallImage = true;
								}
								else { label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png")); }
								JLabel rarity = new JLabel(dynamicCard.getRarity()); textColor(dynamicCard, rarity);
								JLabel quantity = new JLabel("Quantity: " + dynamicCard.getQuantity());
								Integer tempInt = dynamicCard.getTierScore();
								String scoreString = tempInt.toString();
								JLabel score = new JLabel("Tier Score: " + scoreString);
								JButton showText = new JButton("Card Text");
								c.insets = new Insets(5, 5, 5, 5);
								c.weightx = 0.5; c.gridx = 0; c.gridy = 0; c.gridheight = 3; panel.add(label, c);
								c.gridheight = 1; c.gridx = 0; c.gridy = 6; panel.add(rarity, c);
								rarity.setHorizontalAlignment(SwingConstants.CENTER); 
								c.gridx = 0; c.gridy = 7;  panel.add(quantity, c);
								quantity.setHorizontalAlignment(SwingConstants.CENTER);
								c.gridx = 0; c.gridy = 8;  panel.add(score, c);
								score.setHorizontalAlignment(SwingConstants.CENTER); 
								c.gridx = 0; c.gridy = 4;  panel.add(showText, c);
								showText.setHorizontalAlignment(SwingConstants.CENTER); 
								showText.setMargin( new Insets(0,0,0,0) );if (smallImage)
								{
									c.weightx = 0.5; c.gridx = 0; c.gridy = 5; panel.add(fullSize, c); 
									fullSize.setHorizontalAlignment(SwingConstants.CENTER);
									fullSize.addActionListener(new ActionListener()
									{
										@Override
										public void actionPerformed(ActionEvent e) 
										{
											JFrame fullView = new JFrame();
											JPanel fullPanel = new JPanel();
											ImageLabel bigImage = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png"));
											fullPanel.add(bigImage);
											fullView.getContentPane().add(fullPanel);
											fullView.pack();
											fullView.setResizable(false);
											fullView.setVisible(true);
											
										}
										
									});
								}
								singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
								singleCardView.setBounds(100, 100, 496, 443);
								singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
								singleCardView.getContentPane().add(panel);
								singleCardView.pack();						
								singleCardView.setResizable(false);
								singleCardView.setVisible(true);
								
								showText.addActionListener(new ActionListener()
								{
									@Override
									public void actionPerformed(ActionEvent arg0) 
									{
										JFrame textFrame = new JFrame();
										textFrame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
										textFrame.setBounds(100, 100, 496, 443);
										textFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
										JPanel textPanel = new JPanel();
										JTextArea textField = new JTextArea(6, 40);
										textField.setEditable(false);
										String temp = dynamicCard.getText();
										String newTemp = WordUtils.wrap(temp, 75, "\n", false);
										textField.setText(newTemp);
										textField.setWrapStyleWord(true);
										JScrollPane vertScroll = new JScrollPane(textField);
										textPanel.add(vertScroll);
										textFrame.getContentPane().add(textPanel);
										textFrame.pack();
										textFrame.setResizable(false);
										textFrame.setVisible(true);
										
										
									}
								});
							
							
							}
							
							
						}

						@Override public void keyReleased(KeyEvent arg0) {} @Override public void keyTyped(KeyEvent arg0) {}
					};
					
					MouseListener mouseListener = new MouseAdapter() 
					{
						public void mouseClicked(MouseEvent e) 
						{
							if (e.getClickCount() == 2) 
							{    		
								dynamicCard = (Card) dynamicList.getSelectedValue();
								singleCardView = new JFrame();										
								JPanel panel = new JPanel();
								boolean smallImage = false;
								JButton fullSize = new JButton("Full Resolution");
								panel.setLayout(new GridBagLayout());
								GridBagConstraints c = new GridBagConstraints();
								c.fill = GridBagConstraints.HORIZONTAL;
								ImageLabel label;
								if (isImage("src/images/" + dynamicCard.getName() + "Small.png")) 
								{ 
									label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + "Small.png"));
									smallImage = true;
								}
								else { label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png")); }
								JLabel rarity = new JLabel(dynamicCard.getRarity()); textColor(dynamicCard, rarity);
								textColor(dynamicCard, rarity);
								JLabel quantity = new JLabel("Quantity: " + dynamicCard.getQuantity());
								Integer tempInt = dynamicCard.getTierScore();
								String scoreString = tempInt.toString();
								JLabel score = new JLabel("Tier Score: " + scoreString);
								JButton showText = new JButton("Card Text");
								c.insets = new Insets(5, 5, 5, 5);
								c.weightx = 0.5; c.gridx = 0; c.gridy = 0; c.gridheight = 3; panel.add(label, c);
								c.gridheight = 1; c.gridx = 0; c.gridy = 6; panel.add(rarity, c);
								rarity.setHorizontalAlignment(SwingConstants.CENTER); 
								c.gridx = 0; c.gridy = 7;  panel.add(quantity, c);
								quantity.setHorizontalAlignment(SwingConstants.CENTER);
								c.gridx = 0; c.gridy = 8;  panel.add(score, c);
								score.setHorizontalAlignment(SwingConstants.CENTER); 
								c.gridx = 0; c.gridy = 4;  panel.add(showText, c);
								showText.setHorizontalAlignment(SwingConstants.CENTER); 
								showText.setMargin( new Insets(0,0,0,0) );if (smallImage)
								{
									c.weightx = 0.5; c.gridx = 0; c.gridy = 5; panel.add(fullSize, c); 
									fullSize.setHorizontalAlignment(SwingConstants.CENTER);
									fullSize.addActionListener(new ActionListener()
									{
										@Override
										public void actionPerformed(ActionEvent e) 
										{
											JFrame fullView = new JFrame();
											JPanel fullPanel = new JPanel();
											ImageLabel bigImage = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png"));
											fullPanel.add(bigImage);
											fullView.getContentPane().add(fullPanel);
											fullView.pack();
											fullView.setResizable(false);
											fullView.setVisible(true);
											
										}
										
									});
								}
								singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
								singleCardView.setBounds(100, 100, 496, 443);
								singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
								singleCardView.getContentPane().add(panel);
								singleCardView.pack();						
								singleCardView.setResizable(false);
								singleCardView.setVisible(true);
								
								showText.addActionListener(new ActionListener()
								{
									@Override
									public void actionPerformed(ActionEvent arg0) 
									{
										JFrame textFrame = new JFrame();
										textFrame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
										textFrame.setBounds(100, 100, 496, 443);
										textFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
										JPanel textPanel = new JPanel();
										JTextArea textField = new JTextArea(6, 40);
										textField.setEditable(false);
										String temp = dynamicCard.getText();
										String newTemp = WordUtils.wrap(temp, 75, "\n", false);
										textField.setText(newTemp);
										textField.setWrapStyleWord(true);
										JScrollPane vertScroll = new JScrollPane(textField);
										textPanel.add(vertScroll);
										textFrame.getContentPane().add(textPanel);
										textFrame.pack();
										textFrame.setResizable(false);
										textFrame.setVisible(true);
										
										
									}
								});
							
							
							}
						}
					};
					
					
					dynamicList.addKeyListener(keyListener);
					dynamicList.addMouseListener(mouseListener);
					c.weightx = 1.0; c.gridwidth = 2; c.weighty = 1.0; c.gridx = 0; c.gridy = 0; cardView.getContentPane().add(scrollPane, c);
					c.insets = new Insets(0, 5, 0, 5); c.gridwidth = 1; c.gridheight = 4; c.gridx = 0; c.gridy = 2; cardView.getContentPane().add(scrollPane2, c);
					//c.gridx = 1; c.gridy = 2; cardView.getContentPane().add(search, c);
					cardView.pack();
					cardView.setResizable(false);
					cardView.setVisible(true);
				}
			});
			mnViewDatabase.add(mntmViewAllCards);

			mntmNewMenuItem.addActionListener(new ActionListener() 
			{
				@SuppressWarnings("rawtypes")
				public void actionPerformed(ActionEvent arg0) 
				{

					JFrame cardView = new JFrame();	
					cardView.setTitle("Ultimate Rares");
					cardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
					cardView.setBounds(100, 100, 600, 600);
					cardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
					
					cardView.getContentPane().setLayout(new GridBagLayout());
					GridBagConstraints c = new GridBagConstraints();
					c.fill = GridBagConstraints.HORIZONTAL;

					ArrayList<Card >allCardsNoDupes = listMakerRarity(draftAllCards, "Ultimate Rare");
					dynamicModel1.clear();
					for (Card card : allCardsNoDupes) { dynamicModel1.addElement(card); }
					dynamicList = new JList(dynamicModel1);
					JTextArea searchBar = new JTextArea(1, 15);
					JScrollPane scrollPane = new JScrollPane(dynamicList);
					JScrollPane scrollPane2 = new JScrollPane(searchBar);
					//JButton search = new JButton("Search");
					searchBar.addKeyListener(new KeyListener() 
					{
						@Override
						public void keyPressed(KeyEvent arg0) 
						{
							
							
						}

						@Override
						public void keyReleased(KeyEvent e) 
						{
							for (Card card : allCardsNoDupes)
							{
								//if (card.getName().indexOf(e.toString()) > 0) 
								if (card.getName().toLowerCase().contains(searchBar.getText().toLowerCase()))
								{
									dynamicModel1.addElement(card);
								} 
	 						}
							
							
							
						}

						@Override
						public void keyTyped(KeyEvent arg0) 
						{
							dynamicModel1.clear();
						}
					});
					
					KeyListener keyListener = new KeyListener()
					{
						public void keyPressed(KeyEvent e)
						{
							if (e.getKeyCode() == KeyEvent.VK_ENTER)
							{
								dynamicCard = (Card) dynamicList.getSelectedValue();
								singleCardView = new JFrame();										
								JPanel panel = new JPanel();
								boolean smallImage = false;
								JButton fullSize = new JButton("Full Resolution");
								panel.setLayout(new GridBagLayout());
								GridBagConstraints c = new GridBagConstraints();
								c.fill = GridBagConstraints.HORIZONTAL;
								ImageLabel label;
								if (isImage("src/images/" + dynamicCard.getName() + "Small.png")) 
								{ 
									label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + "Small.png"));
									smallImage = true;
								}
								else { label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png")); }
								JLabel rarity = new JLabel(dynamicCard.getRarity()); textColor(dynamicCard, rarity);
								textColor(dynamicCard, rarity);
								JLabel quantity = new JLabel("Quantity: " + dynamicCard.getQuantity());
								Integer tempInt = dynamicCard.getTierScore();
								String scoreString = tempInt.toString();
								JLabel score = new JLabel("Tier Score: " + scoreString);
								JButton showText = new JButton("Card Text");
								c.insets = new Insets(5, 5, 5, 5);
								c.weightx = 0.5; c.gridx = 0; c.gridy = 0; c.gridheight = 3; panel.add(label, c);
								c.gridheight = 1; c.gridx = 0; c.gridy = 6; panel.add(rarity, c);
								rarity.setHorizontalAlignment(SwingConstants.CENTER); 
								c.gridx = 0; c.gridy = 7;  panel.add(quantity, c);
								quantity.setHorizontalAlignment(SwingConstants.CENTER);
								c.gridx = 0; c.gridy = 8;  panel.add(score, c);
								score.setHorizontalAlignment(SwingConstants.CENTER); 
								c.gridx = 0; c.gridy = 4;  panel.add(showText, c);
								showText.setHorizontalAlignment(SwingConstants.CENTER); 
								showText.setMargin( new Insets(0,0,0,0) );if (smallImage)
								{
									c.weightx = 0.5; c.gridx = 0; c.gridy = 5; panel.add(fullSize, c); 
									fullSize.setHorizontalAlignment(SwingConstants.CENTER);
									fullSize.addActionListener(new ActionListener()
									{
										@Override
										public void actionPerformed(ActionEvent e) 
										{
											JFrame fullView = new JFrame();
											JPanel fullPanel = new JPanel();
											ImageLabel bigImage = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png"));
											fullPanel.add(bigImage);
											fullView.getContentPane().add(fullPanel);
											fullView.pack();
											fullView.setResizable(false);
											fullView.setVisible(true);
											
										}
										
									});
								}
								singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
								singleCardView.setBounds(100, 100, 496, 443);
								singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
								singleCardView.getContentPane().add(panel);
								singleCardView.pack();						
								singleCardView.setResizable(false);
								singleCardView.setVisible(true);
								
								showText.addActionListener(new ActionListener()
								{
									@Override
									public void actionPerformed(ActionEvent arg0) 
									{
										JFrame textFrame = new JFrame();
										textFrame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
										textFrame.setBounds(100, 100, 496, 443);
										textFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
										JPanel textPanel = new JPanel();
										JTextArea textField = new JTextArea(6, 40);
										textField.setEditable(false);
										String temp = dynamicCard.getText();
										String newTemp = WordUtils.wrap(temp, 75, "\n", false);
										textField.setText(newTemp);
										textField.setWrapStyleWord(true);
										JScrollPane vertScroll = new JScrollPane(textField);
										textPanel.add(vertScroll);
										textFrame.getContentPane().add(textPanel);
										textFrame.pack();
										textFrame.setResizable(false);
										textFrame.setVisible(true);
										
										
									}
								});
							
							
							}
						}

						@Override public void keyReleased(KeyEvent arg0) {} @Override public void keyTyped(KeyEvent arg0) {}
					};
					
					MouseListener mouseListener = new MouseAdapter() 
					{
						public void mouseClicked(MouseEvent e) 
						{
							if (e.getClickCount() == 2) 
							{    		
								dynamicCard = (Card) dynamicList.getSelectedValue();
								singleCardView = new JFrame();										
								JPanel panel = new JPanel();
								boolean smallImage = false;
								JButton fullSize = new JButton("Full Resolution");
								panel.setLayout(new GridBagLayout());
								GridBagConstraints c = new GridBagConstraints();
								c.fill = GridBagConstraints.HORIZONTAL;
								ImageLabel label;
								if (isImage("src/images/" + dynamicCard.getName() + "Small.png")) 
								{ 
									label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + "Small.png"));
									smallImage = true;
								}
								else { label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png")); }
								JLabel rarity = new JLabel(dynamicCard.getRarity()); textColor(dynamicCard, rarity);
								JLabel quantity = new JLabel("Quantity: " + dynamicCard.getQuantity());
								Integer tempInt = dynamicCard.getTierScore();
								String scoreString = tempInt.toString();
								JLabel score = new JLabel("Tier Score: " + scoreString);
								JButton showText = new JButton("Card Text");
								c.insets = new Insets(5, 5, 5, 5);
								c.weightx = 0.5; c.gridx = 0; c.gridy = 0; c.gridheight = 3; panel.add(label, c);
								c.gridheight = 1; c.gridx = 0; c.gridy = 6; panel.add(rarity, c);
								rarity.setHorizontalAlignment(SwingConstants.CENTER); 
								c.gridx = 0; c.gridy = 7;  panel.add(quantity, c);
								quantity.setHorizontalAlignment(SwingConstants.CENTER);
								c.gridx = 0; c.gridy = 8;  panel.add(score, c);
								score.setHorizontalAlignment(SwingConstants.CENTER); 
								c.gridx = 0; c.gridy = 4;  panel.add(showText, c);
								showText.setHorizontalAlignment(SwingConstants.CENTER); 
								showText.setMargin( new Insets(0,0,0,0) );if (smallImage)
								{
									c.weightx = 0.5; c.gridx = 0; c.gridy = 5; panel.add(fullSize, c); 
									fullSize.setHorizontalAlignment(SwingConstants.CENTER);
									fullSize.addActionListener(new ActionListener()
									{
										@Override
										public void actionPerformed(ActionEvent e) 
										{
											JFrame fullView = new JFrame();
											JPanel fullPanel = new JPanel();
											ImageLabel bigImage = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png"));
											fullPanel.add(bigImage);
											fullView.getContentPane().add(fullPanel);
											fullView.pack();
											fullView.setResizable(false);
											fullView.setVisible(true);
											
										}
										
									});
								}
								singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
								singleCardView.setBounds(100, 100, 496, 443);
								singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
								singleCardView.getContentPane().add(panel);
								singleCardView.pack();						
								singleCardView.setResizable(false);
								singleCardView.setVisible(true);
								
								showText.addActionListener(new ActionListener()
								{
									@Override
									public void actionPerformed(ActionEvent arg0) 
									{
										JFrame textFrame = new JFrame();
										textFrame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
										textFrame.setBounds(100, 100, 496, 443);
										textFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
										JPanel textPanel = new JPanel();
										JTextArea textField = new JTextArea(6, 40);
										textField.setEditable(false);
										String temp = dynamicCard.getText();
										String newTemp = WordUtils.wrap(temp, 75, "\n", false);
										textField.setText(newTemp);
										textField.setWrapStyleWord(true);
										JScrollPane vertScroll = new JScrollPane(textField);
										textPanel.add(vertScroll);
										textFrame.getContentPane().add(textPanel);
										textFrame.pack();
										textFrame.setResizable(false);
										textFrame.setVisible(true);
										
										
									}
								});
							
							
							}
						}
					};
					
					
					dynamicList.addKeyListener(keyListener);
					dynamicList.addMouseListener(mouseListener);
					c.weightx = 1.0; c.gridwidth = 2; c.weighty = 1.0; c.gridx = 0; c.gridy = 0; cardView.getContentPane().add(scrollPane, c);
					c.insets = new Insets(0, 5, 0, 5); c.gridwidth = 1; c.gridheight = 4; c.gridx = 0; c.gridy = 2; cardView.getContentPane().add(scrollPane2, c);
					//c.gridx = 1; c.gridy = 2; cardView.getContentPane().add(search, c);
					cardView.pack();
					cardView.setResizable(false);
					cardView.setVisible(true);
				}
			});
			mnViewDatabase.add(mntmNewMenuItem);


			mntmUltraRares.addActionListener(new ActionListener() 
			{
				@SuppressWarnings("rawtypes")
				public void actionPerformed(ActionEvent arg0) 
				{

					JFrame cardView = new JFrame();	
					cardView.setTitle("Ultra Rares");
					cardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
					cardView.setBounds(100, 100, 600, 600);
					cardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
					
					cardView.getContentPane().setLayout(new GridBagLayout());
					GridBagConstraints c = new GridBagConstraints();
					c.fill = GridBagConstraints.HORIZONTAL;

					ArrayList<Card >allCardsNoDupes = listMakerRarity(draftAllCards, "Ultra Rare");
					dynamicModel1.clear();
					for (Card card : allCardsNoDupes) { dynamicModel1.addElement(card); }
					dynamicList = new JList(dynamicModel1);
					JTextArea searchBar = new JTextArea(1, 15);
					JScrollPane scrollPane = new JScrollPane(dynamicList);
					JScrollPane scrollPane2 = new JScrollPane(searchBar);
					//JButton search = new JButton("Search");
					searchBar.addKeyListener(new KeyListener() 
					{
						@Override
						public void keyPressed(KeyEvent arg0) 
						{
							
							
						}

						@Override
						public void keyReleased(KeyEvent e) 
						{
							for (Card card : allCardsNoDupes)
							{
								//if (card.getName().indexOf(e.toString()) > 0) 
								if (card.getName().toLowerCase().contains(searchBar.getText().toLowerCase()))
								{
									dynamicModel1.addElement(card);
									
								} 
	 						}
							
							
							
						}

						@Override
						public void keyTyped(KeyEvent arg0) 
						{
							dynamicModel1.clear();
						}
					});
					
					KeyListener keyListener = new KeyListener()
					{
						public void keyPressed(KeyEvent e)
						{
							if (e.getKeyCode() == KeyEvent.VK_ENTER)
							{
								dynamicCard = (Card) dynamicList.getSelectedValue();
								singleCardView = new JFrame();										
								JPanel panel = new JPanel();
								boolean smallImage = false;
								JButton fullSize = new JButton("Full Resolution");
								panel.setLayout(new GridBagLayout());
								GridBagConstraints c = new GridBagConstraints();
								c.fill = GridBagConstraints.HORIZONTAL;
								ImageLabel label;
								if (isImage("src/images/" + dynamicCard.getName() + "Small.png")) 
								{ 
									label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + "Small.png"));
									smallImage = true;
								}
								else { label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png")); }
								JLabel rarity = new JLabel(dynamicCard.getRarity()); textColor(dynamicCard, rarity);
								JLabel quantity = new JLabel("Quantity: " + dynamicCard.getQuantity());
								Integer tempInt = dynamicCard.getTierScore();
								String scoreString = tempInt.toString();
								JLabel score = new JLabel("Tier Score: " + scoreString);
								JButton showText = new JButton("Card Text");
								c.insets = new Insets(5, 5, 5, 5);
								c.weightx = 0.5; c.gridx = 0; c.gridy = 0; c.gridheight = 3; panel.add(label, c);
								c.gridheight = 1; c.gridx = 0; c.gridy = 6; panel.add(rarity, c);
								rarity.setHorizontalAlignment(SwingConstants.CENTER); 
								c.gridx = 0; c.gridy = 7;  panel.add(quantity, c);
								quantity.setHorizontalAlignment(SwingConstants.CENTER);
								c.gridx = 0; c.gridy = 8;  panel.add(score, c);
								score.setHorizontalAlignment(SwingConstants.CENTER); 
								c.gridx = 0; c.gridy = 4;  panel.add(showText, c);
								showText.setHorizontalAlignment(SwingConstants.CENTER); 
								showText.setMargin( new Insets(0,0,0,0) );if (smallImage)
								{
									c.weightx = 0.5; c.gridx = 0; c.gridy = 5; panel.add(fullSize, c); 
									fullSize.setHorizontalAlignment(SwingConstants.CENTER);
									fullSize.addActionListener(new ActionListener()
									{
										@Override
										public void actionPerformed(ActionEvent e) 
										{
											JFrame fullView = new JFrame();
											JPanel fullPanel = new JPanel();
											ImageLabel bigImage = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png"));
											fullPanel.add(bigImage);
											fullView.getContentPane().add(fullPanel);
											fullView.pack();
											fullView.setResizable(false);
											fullView.setVisible(true);
											
										}
										
									});
								}
								singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
								singleCardView.setBounds(100, 100, 496, 443);
								singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
								singleCardView.getContentPane().add(panel);
								singleCardView.pack();						
								singleCardView.setResizable(false);
								singleCardView.setVisible(true);
								
								showText.addActionListener(new ActionListener()
								{
									@Override
									public void actionPerformed(ActionEvent arg0) 
									{
										JFrame textFrame = new JFrame();
										textFrame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
										textFrame.setBounds(100, 100, 496, 443);
										textFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
										JPanel textPanel = new JPanel();
										JTextArea textField = new JTextArea(6, 40);
										textField.setEditable(false);
										String temp = dynamicCard.getText();
										String newTemp = WordUtils.wrap(temp, 75, "\n", false);
										textField.setText(newTemp);
										textField.setWrapStyleWord(true);
										JScrollPane vertScroll = new JScrollPane(textField);
										textPanel.add(vertScroll);
										textFrame.getContentPane().add(textPanel);
										textFrame.pack();
										textFrame.setResizable(false);
										textFrame.setVisible(true);
										
										
									}
								});
							
							
							}
						}

						@Override public void keyReleased(KeyEvent arg0) {} @Override public void keyTyped(KeyEvent arg0) {}
					};
					
					MouseListener mouseListener = new MouseAdapter() 
					{
						public void mouseClicked(MouseEvent e) 
						{
							if (e.getClickCount() == 2) 
							{    		
								dynamicCard = (Card) dynamicList.getSelectedValue();
								singleCardView = new JFrame();										
								JPanel panel = new JPanel();
								boolean smallImage = false;
								JButton fullSize = new JButton("Full Resolution");
								panel.setLayout(new GridBagLayout());
								GridBagConstraints c = new GridBagConstraints();
								c.fill = GridBagConstraints.HORIZONTAL;
								ImageLabel label;
								if (isImage("src/images/" + dynamicCard.getName() + "Small.png")) 
								{ 
									label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + "Small.png"));
									smallImage = true;
								}
								else { label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png")); }
								JLabel rarity = new JLabel(dynamicCard.getRarity()); textColor(dynamicCard, rarity);
								JLabel quantity = new JLabel("Quantity: " + dynamicCard.getQuantity());
								Integer tempInt = dynamicCard.getTierScore();
								String scoreString = tempInt.toString();
								JLabel score = new JLabel("Tier Score: " + scoreString);
								JButton showText = new JButton("Card Text");
								c.insets = new Insets(5, 5, 5, 5);
								c.weightx = 0.5; c.gridx = 0; c.gridy = 0; c.gridheight = 3; panel.add(label, c);
								c.gridheight = 1; c.gridx = 0; c.gridy = 6; panel.add(rarity, c);
								rarity.setHorizontalAlignment(SwingConstants.CENTER); 
								c.gridx = 0; c.gridy = 7;  panel.add(quantity, c);
								quantity.setHorizontalAlignment(SwingConstants.CENTER);
								c.gridx = 0; c.gridy = 8;  panel.add(score, c);
								score.setHorizontalAlignment(SwingConstants.CENTER); 
								c.gridx = 0; c.gridy = 4;  panel.add(showText, c);
								showText.setHorizontalAlignment(SwingConstants.CENTER); 
								showText.setMargin( new Insets(0,0,0,0) );if (smallImage)
								{
									c.weightx = 0.5; c.gridx = 0; c.gridy = 5; panel.add(fullSize, c); 
									fullSize.setHorizontalAlignment(SwingConstants.CENTER);
									fullSize.addActionListener(new ActionListener()
									{
										@Override
										public void actionPerformed(ActionEvent e) 
										{
											JFrame fullView = new JFrame();
											JPanel fullPanel = new JPanel();
											ImageLabel bigImage = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png"));
											fullPanel.add(bigImage);
											fullView.getContentPane().add(fullPanel);
											fullView.pack();
											fullView.setResizable(false);
											fullView.setVisible(true);
											
										}
										
									});
								}
								singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
								singleCardView.setBounds(100, 100, 496, 443);
								singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
								singleCardView.getContentPane().add(panel);
								singleCardView.pack();						
								singleCardView.setResizable(false);
								singleCardView.setVisible(true);
								
								showText.addActionListener(new ActionListener()
								{
									@Override
									public void actionPerformed(ActionEvent arg0) 
									{
										JFrame textFrame = new JFrame();
										textFrame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
										textFrame.setBounds(100, 100, 496, 443);
										textFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
										JPanel textPanel = new JPanel();
										JTextArea textField = new JTextArea(6, 40);
										textField.setEditable(false);
										String temp = dynamicCard.getText();
										String newTemp = WordUtils.wrap(temp, 75, "\n", false);
										textField.setText(newTemp);
										textField.setWrapStyleWord(true);
										JScrollPane vertScroll = new JScrollPane(textField);
										textPanel.add(vertScroll);
										textFrame.getContentPane().add(textPanel);
										textFrame.pack();
										textFrame.setResizable(false);
										textFrame.setVisible(true);
										
										
									}
								});
							
							
							}
						}
					};
					
					
					dynamicList.addKeyListener(keyListener);
					dynamicList.addMouseListener(mouseListener);
					c.weightx = 1.0; c.gridwidth = 2; c.weighty = 1.0; c.gridx = 0; c.gridy = 0; cardView.getContentPane().add(scrollPane, c);
					c.insets = new Insets(0, 5, 0, 5); c.gridwidth = 1; c.gridheight = 4; c.gridx = 0; c.gridy = 2; cardView.getContentPane().add(scrollPane2, c);
					//c.gridx = 1; c.gridy = 2; cardView.getContentPane().add(search, c);
					cardView.pack();
					cardView.setResizable(false);
					cardView.setVisible(true);
				}
			});
			mnViewDatabase.add(mntmUltraRares);


			mntmSuperRares.addActionListener(new ActionListener() 
			{
				@SuppressWarnings("rawtypes")
				public void actionPerformed(ActionEvent arg0) 
				{

					JFrame cardView = new JFrame();	
					cardView.setTitle("Super Rares");
					cardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
					cardView.setBounds(100, 100, 600, 600);
					cardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
					
					cardView.getContentPane().setLayout(new GridBagLayout());
					GridBagConstraints c = new GridBagConstraints();
					c.fill = GridBagConstraints.HORIZONTAL;

					ArrayList<Card >allCardsNoDupes = listMakerRarity(draftAllCards, "Super Rare");
					dynamicModel1.clear();
					for (Card card : allCardsNoDupes) { dynamicModel1.addElement(card); }
					dynamicList = new JList(dynamicModel1);
					JTextArea searchBar = new JTextArea(1, 15);
					JScrollPane scrollPane = new JScrollPane(dynamicList);
					JScrollPane scrollPane2 = new JScrollPane(searchBar);
					//JButton search = new JButton("Search");
					searchBar.addKeyListener(new KeyListener() 
					{
						@Override
						public void keyPressed(KeyEvent arg0) 
						{
							
							
						}

						@Override
						public void keyReleased(KeyEvent e) 
						{
							for (Card card : allCardsNoDupes)
							{
								//if (card.getName().indexOf(e.toString()) > 0) 
								if (card.getName().toLowerCase().contains(searchBar.getText().toLowerCase()))
								{
									dynamicModel1.addElement(card);
									
								} 
	 						}
							
							
							
						}

						@Override
						public void keyTyped(KeyEvent arg0) 
						{
							dynamicModel1.clear();
						}
					});
					
					KeyListener keyListener = new KeyListener()
					{
						public void keyPressed(KeyEvent e)
						{
							if (e.getKeyCode() == KeyEvent.VK_ENTER)
							{
								dynamicCard = (Card) dynamicList.getSelectedValue();
								singleCardView = new JFrame();										
								JPanel panel = new JPanel();
								boolean smallImage = false;
								JButton fullSize = new JButton("Full Resolution");
								panel.setLayout(new GridBagLayout());
								GridBagConstraints c = new GridBagConstraints();
								c.fill = GridBagConstraints.HORIZONTAL;
								ImageLabel label;
								if (isImage("src/images/" + dynamicCard.getName() + "Small.png")) 
								{ 
									label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + "Small.png"));
									smallImage = true;
								}
								else { label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png")); }
								JLabel rarity = new JLabel(dynamicCard.getRarity()); textColor(dynamicCard, rarity);
								JLabel quantity = new JLabel("Quantity: " + dynamicCard.getQuantity());
								Integer tempInt = dynamicCard.getTierScore();
								String scoreString = tempInt.toString();
								JLabel score = new JLabel("Tier Score: " + scoreString);
								JButton showText = new JButton("Card Text");
								c.insets = new Insets(5, 5, 5, 5);
								c.weightx = 0.5; c.gridx = 0; c.gridy = 0; c.gridheight = 3; panel.add(label, c);
								c.gridheight = 1; c.gridx = 0; c.gridy = 6; panel.add(rarity, c);
								rarity.setHorizontalAlignment(SwingConstants.CENTER); 
								c.gridx = 0; c.gridy = 7;  panel.add(quantity, c);
								quantity.setHorizontalAlignment(SwingConstants.CENTER);
								c.gridx = 0; c.gridy = 8;  panel.add(score, c);
								score.setHorizontalAlignment(SwingConstants.CENTER); 
								c.gridx = 0; c.gridy = 4;  panel.add(showText, c);
								showText.setHorizontalAlignment(SwingConstants.CENTER); 
								showText.setMargin( new Insets(0,0,0,0) );if (smallImage)
								{
									c.weightx = 0.5; c.gridx = 0; c.gridy = 5; panel.add(fullSize, c); 
									fullSize.setHorizontalAlignment(SwingConstants.CENTER);
									fullSize.addActionListener(new ActionListener()
									{
										@Override
										public void actionPerformed(ActionEvent e) 
										{
											JFrame fullView = new JFrame();
											JPanel fullPanel = new JPanel();
											ImageLabel bigImage = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png"));
											fullPanel.add(bigImage);
											fullView.getContentPane().add(fullPanel);
											fullView.pack();
											fullView.setResizable(false);
											fullView.setVisible(true);
											
										}
										
									});
								}
								singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
								singleCardView.setBounds(100, 100, 496, 443);
								singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
								singleCardView.getContentPane().add(panel);
								singleCardView.pack();						
								singleCardView.setResizable(false);
								singleCardView.setVisible(true);
								
								showText.addActionListener(new ActionListener()
								{
									@Override
									public void actionPerformed(ActionEvent arg0) 
									{
										JFrame textFrame = new JFrame();
										textFrame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
										textFrame.setBounds(100, 100, 496, 443);
										textFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
										JPanel textPanel = new JPanel();
										JTextArea textField = new JTextArea(6, 40);
										textField.setEditable(false);
										String temp = dynamicCard.getText();
										String newTemp = WordUtils.wrap(temp, 75, "\n", false);
										textField.setText(newTemp);
										textField.setWrapStyleWord(true);
										JScrollPane vertScroll = new JScrollPane(textField);
										textPanel.add(vertScroll);
										textFrame.getContentPane().add(textPanel);
										textFrame.pack();
										textFrame.setResizable(false);
										textFrame.setVisible(true);
										
										
									}
								});
							
							
							}
						}

						@Override public void keyReleased(KeyEvent arg0) {} @Override public void keyTyped(KeyEvent arg0) {}
					};
					
					MouseListener mouseListener = new MouseAdapter() 
					{
						public void mouseClicked(MouseEvent e) 
						{
							if (e.getClickCount() == 2) 
							{    		
								dynamicCard = (Card) dynamicList.getSelectedValue();
								singleCardView = new JFrame();										
								JPanel panel = new JPanel();
								boolean smallImage = false;
								JButton fullSize = new JButton("Full Resolution");
								panel.setLayout(new GridBagLayout());
								GridBagConstraints c = new GridBagConstraints();
								c.fill = GridBagConstraints.HORIZONTAL;
								ImageLabel label;
								if (isImage("src/images/" + dynamicCard.getName() + "Small.png")) 
								{ 
									label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + "Small.png"));
									smallImage = true;
								}
								else { label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png")); }
								JLabel rarity = new JLabel(dynamicCard.getRarity()); textColor(dynamicCard, rarity);
								JLabel quantity = new JLabel("Quantity: " + dynamicCard.getQuantity());
								Integer tempInt = dynamicCard.getTierScore();
								String scoreString = tempInt.toString();
								JLabel score = new JLabel("Tier Score: " + scoreString);
								JButton showText = new JButton("Card Text");
								c.insets = new Insets(5, 5, 5, 5);
								c.weightx = 0.5; c.gridx = 0; c.gridy = 0; c.gridheight = 3; panel.add(label, c);
								c.gridheight = 1; c.gridx = 0; c.gridy = 6; panel.add(rarity, c);
								rarity.setHorizontalAlignment(SwingConstants.CENTER); 
								c.gridx = 0; c.gridy = 7;  panel.add(quantity, c);
								quantity.setHorizontalAlignment(SwingConstants.CENTER);
								c.gridx = 0; c.gridy = 8;  panel.add(score, c);
								score.setHorizontalAlignment(SwingConstants.CENTER); 
								c.gridx = 0; c.gridy = 4;  panel.add(showText, c);
								showText.setHorizontalAlignment(SwingConstants.CENTER); 
								showText.setMargin( new Insets(0,0,0,0) );if (smallImage)
								{
									c.weightx = 0.5; c.gridx = 0; c.gridy = 5; panel.add(fullSize, c); 
									fullSize.setHorizontalAlignment(SwingConstants.CENTER);
									fullSize.addActionListener(new ActionListener()
									{
										@Override
										public void actionPerformed(ActionEvent e) 
										{
											JFrame fullView = new JFrame();
											JPanel fullPanel = new JPanel();
											ImageLabel bigImage = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png"));
											fullPanel.add(bigImage);
											fullView.getContentPane().add(fullPanel);
											fullView.pack();
											fullView.setResizable(false);
											fullView.setVisible(true);
											
										}
										
									});
								}
								singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
								singleCardView.setBounds(100, 100, 496, 443);
								singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
								singleCardView.getContentPane().add(panel);
								singleCardView.pack();						
								singleCardView.setResizable(false);
								singleCardView.setVisible(true);
								
								showText.addActionListener(new ActionListener()
								{
									@Override
									public void actionPerformed(ActionEvent arg0) 
									{
										JFrame textFrame = new JFrame();
										textFrame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
										textFrame.setBounds(100, 100, 496, 443);
										textFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
										JPanel textPanel = new JPanel();
										JTextArea textField = new JTextArea(6, 40);
										textField.setEditable(false);
										String temp = dynamicCard.getText();
										String newTemp = WordUtils.wrap(temp, 75, "\n", false);
										textField.setText(newTemp);
										textField.setWrapStyleWord(true);
										JScrollPane vertScroll = new JScrollPane(textField);
										textPanel.add(vertScroll);
										textFrame.getContentPane().add(textPanel);
										textFrame.pack();
										textFrame.setResizable(false);
										textFrame.setVisible(true);
										
										
									}
								});
							
							
							}
						}
					};
					
					
					dynamicList.addKeyListener(keyListener);
					dynamicList.addMouseListener(mouseListener);
					c.weightx = 1.0; c.gridwidth = 2; c.weighty = 1.0; c.gridx = 0; c.gridy = 0; cardView.getContentPane().add(scrollPane, c);
					c.insets = new Insets(0, 5, 0, 5); c.gridwidth = 1; c.gridheight = 4; c.gridx = 0; c.gridy = 2; cardView.getContentPane().add(scrollPane2, c);
					//c.gridx = 1; c.gridy = 2; cardView.getContentPane().add(search, c);
					cardView.pack();
					cardView.setResizable(false);
					cardView.setVisible(true);
				}
			});
			mnViewDatabase.add(mntmSuperRares);



			mntmRares.addActionListener(new ActionListener() 
			{
				@SuppressWarnings("rawtypes")
				public void actionPerformed(ActionEvent arg0) 
				{

					JFrame cardView = new JFrame();	
					cardView.setTitle("Rares");
					cardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
					cardView.setBounds(100, 100, 600, 600);
					cardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
					
					cardView.getContentPane().setLayout(new GridBagLayout());
					GridBagConstraints c = new GridBagConstraints();
					c.fill = GridBagConstraints.HORIZONTAL;

					ArrayList<Card >allCardsNoDupes = listMakerRarity(draftAllCards, "Rare");
					dynamicModel1.clear();
					for (Card card : allCardsNoDupes) { dynamicModel1.addElement(card); }
					dynamicList = new JList(dynamicModel1);
					JTextArea searchBar = new JTextArea(1, 15);
					JScrollPane scrollPane = new JScrollPane(dynamicList);
					JScrollPane scrollPane2 = new JScrollPane(searchBar);
					//JButton search = new JButton("Search");
					searchBar.addKeyListener(new KeyListener() 
					{
						@Override
						public void keyPressed(KeyEvent arg0) 
						{
							
							
						}

						@Override
						public void keyReleased(KeyEvent e) 
						{
							for (Card card : allCardsNoDupes)
							{
								//if (card.getName().indexOf(e.toString()) > 0) 
								if (card.getName().toLowerCase().contains(searchBar.getText().toLowerCase()))
								{
									dynamicModel1.addElement(card);
									
								} 
	 						}
							
							
							
						}

						@Override
						public void keyTyped(KeyEvent arg0) 
						{
							dynamicModel1.clear();
						}
					});
					
					KeyListener keyListener = new KeyListener()
					{
						public void keyPressed(KeyEvent e)
						{
							if (e.getKeyCode() == KeyEvent.VK_ENTER)
							{
								dynamicCard = (Card) dynamicList.getSelectedValue();
								singleCardView = new JFrame();										
								JPanel panel = new JPanel();
								boolean smallImage = false;
								JButton fullSize = new JButton("Full Resolution");
								panel.setLayout(new GridBagLayout());
								GridBagConstraints c = new GridBagConstraints();
								c.fill = GridBagConstraints.HORIZONTAL;
								ImageLabel label;
								if (isImage("src/images/" + dynamicCard.getName() + "Small.png")) 
								{ 
									label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + "Small.png"));
									smallImage = true;
								}
								else { label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png")); }
								JLabel rarity = new JLabel(dynamicCard.getRarity()); textColor(dynamicCard, rarity);
								JLabel quantity = new JLabel("Quantity: " + dynamicCard.getQuantity());
								Integer tempInt = dynamicCard.getTierScore();
								String scoreString = tempInt.toString();
								JLabel score = new JLabel("Tier Score: " + scoreString);
								JButton showText = new JButton("Card Text");
								c.insets = new Insets(5, 5, 5, 5);
								c.weightx = 0.5; c.gridx = 0; c.gridy = 0; c.gridheight = 3; panel.add(label, c);
								c.gridheight = 1; c.gridx = 0; c.gridy = 6; panel.add(rarity, c);
								rarity.setHorizontalAlignment(SwingConstants.CENTER); 
								c.gridx = 0; c.gridy = 7;  panel.add(quantity, c);
								quantity.setHorizontalAlignment(SwingConstants.CENTER);
								c.gridx = 0; c.gridy = 8;  panel.add(score, c);
								score.setHorizontalAlignment(SwingConstants.CENTER); 
								c.gridx = 0; c.gridy = 4;  panel.add(showText, c);
								showText.setHorizontalAlignment(SwingConstants.CENTER); 
								showText.setMargin( new Insets(0,0,0,0) );if (smallImage)
								{
									c.weightx = 0.5; c.gridx = 0; c.gridy = 5; panel.add(fullSize, c); 
									fullSize.setHorizontalAlignment(SwingConstants.CENTER);
									fullSize.addActionListener(new ActionListener()
									{
										@Override
										public void actionPerformed(ActionEvent e) 
										{
											JFrame fullView = new JFrame();
											JPanel fullPanel = new JPanel();
											ImageLabel bigImage = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png"));
											fullPanel.add(bigImage);
											fullView.getContentPane().add(fullPanel);
											fullView.pack();
											fullView.setResizable(false);
											fullView.setVisible(true);
											
										}
										
									});
								}
								singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
								singleCardView.setBounds(100, 100, 496, 443);
								singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
								singleCardView.getContentPane().add(panel);
								singleCardView.pack();						
								singleCardView.setResizable(false);
								singleCardView.setVisible(true);
								
								showText.addActionListener(new ActionListener()
								{
									@Override
									public void actionPerformed(ActionEvent arg0) 
									{
										JFrame textFrame = new JFrame();
										textFrame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
										textFrame.setBounds(100, 100, 496, 443);
										textFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
										JPanel textPanel = new JPanel();
										JTextArea textField = new JTextArea(6, 40);
										textField.setEditable(false);
										String temp = dynamicCard.getText();
										String newTemp = WordUtils.wrap(temp, 75, "\n", false);
										textField.setText(newTemp);
										textField.setWrapStyleWord(true);
										JScrollPane vertScroll = new JScrollPane(textField);
										textPanel.add(vertScroll);
										textFrame.getContentPane().add(textPanel);
										textFrame.pack();
										textFrame.setResizable(false);
										textFrame.setVisible(true);
										
										
									}
								});
							
							
							}
						}

						@Override public void keyReleased(KeyEvent arg0) {} @Override public void keyTyped(KeyEvent arg0) {}
					};
					
					MouseListener mouseListener = new MouseAdapter() 
					{
						public void mouseClicked(MouseEvent e) 
						{
							if (e.getClickCount() == 2) 
							{    		
								dynamicCard = (Card) dynamicList.getSelectedValue();
								singleCardView = new JFrame();										
								JPanel panel = new JPanel();
								boolean smallImage = false;
								JButton fullSize = new JButton("Full Resolution");
								panel.setLayout(new GridBagLayout());
								GridBagConstraints c = new GridBagConstraints();
								c.fill = GridBagConstraints.HORIZONTAL;
								ImageLabel label;
								if (isImage("src/images/" + dynamicCard.getName() + "Small.png")) 
								{ 
									label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + "Small.png"));
									smallImage = true;
								}
								else { label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png")); }
								JLabel rarity = new JLabel(dynamicCard.getRarity()); textColor(dynamicCard, rarity);
								JLabel quantity = new JLabel("Quantity: " + dynamicCard.getQuantity());
								Integer tempInt = dynamicCard.getTierScore();
								String scoreString = tempInt.toString();
								JLabel score = new JLabel("Tier Score: " + scoreString);
								JButton showText = new JButton("Card Text");
								c.insets = new Insets(5, 5, 5, 5);
								c.weightx = 0.5; c.gridx = 0; c.gridy = 0; c.gridheight = 3; panel.add(label, c);
								c.gridheight = 1; c.gridx = 0; c.gridy = 6; panel.add(rarity, c);
								rarity.setHorizontalAlignment(SwingConstants.CENTER); 
								c.gridx = 0; c.gridy = 7;  panel.add(quantity, c);
								quantity.setHorizontalAlignment(SwingConstants.CENTER);
								c.gridx = 0; c.gridy = 8;  panel.add(score, c);
								score.setHorizontalAlignment(SwingConstants.CENTER); 
								c.gridx = 0; c.gridy = 4;  panel.add(showText, c);
								showText.setHorizontalAlignment(SwingConstants.CENTER); 
								showText.setMargin( new Insets(0,0,0,0) );if (smallImage)
								{
									c.weightx = 0.5; c.gridx = 0; c.gridy = 5; panel.add(fullSize, c); 
									fullSize.setHorizontalAlignment(SwingConstants.CENTER);
									fullSize.addActionListener(new ActionListener()
									{
										@Override
										public void actionPerformed(ActionEvent e) 
										{
											JFrame fullView = new JFrame();
											JPanel fullPanel = new JPanel();
											ImageLabel bigImage = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png"));
											fullPanel.add(bigImage);
											fullView.getContentPane().add(fullPanel);
											fullView.pack();
											fullView.setResizable(false);
											fullView.setVisible(true);
											
										}
										
									});
								}
								singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
								singleCardView.setBounds(100, 100, 496, 443);
								singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
								singleCardView.getContentPane().add(panel);
								singleCardView.pack();						
								singleCardView.setResizable(false);
								singleCardView.setVisible(true);
								
								showText.addActionListener(new ActionListener()
								{
									@Override
									public void actionPerformed(ActionEvent arg0) 
									{
										JFrame textFrame = new JFrame();
										textFrame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
										textFrame.setBounds(100, 100, 496, 443);
										textFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
										JPanel textPanel = new JPanel();
										JTextArea textField = new JTextArea(6, 40);
										textField.setEditable(false);
										String temp = dynamicCard.getText();
										String newTemp = WordUtils.wrap(temp, 75, "\n", false);
										textField.setText(newTemp);
										textField.setWrapStyleWord(true);
										JScrollPane vertScroll = new JScrollPane(textField);
										textPanel.add(vertScroll);
										textFrame.getContentPane().add(textPanel);
										textFrame.pack();
										textFrame.setResizable(false);
										textFrame.setVisible(true);
										
										
									}
								});
							
							
							}
						}
					};
					
					
					dynamicList.addKeyListener(keyListener);
					dynamicList.addMouseListener(mouseListener);
					c.weightx = 1.0; c.gridwidth = 2; c.weighty = 1.0; c.gridx = 0; c.gridy = 0; cardView.getContentPane().add(scrollPane, c);
					c.insets = new Insets(0, 5, 0, 5); c.gridwidth = 1; c.gridheight = 4; c.gridx = 0; c.gridy = 2; cardView.getContentPane().add(scrollPane2, c);
					//c.gridx = 1; c.gridy = 2; cardView.getContentPane().add(search, c);
					cardView.pack();
					cardView.setResizable(false);
					cardView.setVisible(true);
				}
			});
			mnViewDatabase.add(mntmRares);


			mntmCommon.addActionListener(new ActionListener() 
			{
				@SuppressWarnings("rawtypes")
				public void actionPerformed(ActionEvent arg0) 
				{

					JFrame cardView = new JFrame();	
					cardView.setTitle("Commons");
					cardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
					cardView.setBounds(100, 100, 600, 600);
					cardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
					
					cardView.getContentPane().setLayout(new GridBagLayout());
					GridBagConstraints c = new GridBagConstraints();
					c.fill = GridBagConstraints.HORIZONTAL;

					ArrayList<Card >allCardsNoDupes = listMakerRarity(draftAllCards, "Common");
					dynamicModel1.clear();
					for (Card card : allCardsNoDupes) { dynamicModel1.addElement(card); }
					dynamicList = new JList(dynamicModel1);
					JTextArea searchBar = new JTextArea(1, 15);
					JScrollPane scrollPane = new JScrollPane(dynamicList);
					JScrollPane scrollPane2 = new JScrollPane(searchBar);
					//JButton search = new JButton("Search");
					searchBar.addKeyListener(new KeyListener() 
					{
						@Override
						public void keyPressed(KeyEvent arg0) 
						{
							
							
						}

						@Override
						public void keyReleased(KeyEvent e) 
						{
							for (Card card : allCardsNoDupes)
							{
								//if (card.getName().indexOf(e.toString()) > 0) 
								if (card.getName().toLowerCase().contains(searchBar.getText().toLowerCase()))
								{
									dynamicModel1.addElement(card);
									
								} 
	 						}
							
							
							
						}

						@Override
						public void keyTyped(KeyEvent arg0) 
						{
							dynamicModel1.clear();
						}
					});
					
					KeyListener keyListener = new KeyListener()
					{
						public void keyPressed(KeyEvent e)
						{
							if (e.getKeyCode() == KeyEvent.VK_ENTER)
							{
								dynamicCard = (Card) dynamicList.getSelectedValue();
								singleCardView = new JFrame();										
								JPanel panel = new JPanel();
								boolean smallImage = false;
								JButton fullSize = new JButton("Full Resolution");
								panel.setLayout(new GridBagLayout());
								GridBagConstraints c = new GridBagConstraints();
								c.fill = GridBagConstraints.HORIZONTAL;
								ImageLabel label;
								if (isImage("src/images/" + dynamicCard.getName() + "Small.png")) 
								{ 
									label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + "Small.png"));
									smallImage = true;
								}
								else { label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png")); }
								JLabel rarity = new JLabel(dynamicCard.getRarity()); textColor(dynamicCard, rarity);
								JLabel quantity = new JLabel("Quantity: " + dynamicCard.getQuantity());
								Integer tempInt = dynamicCard.getTierScore();
								String scoreString = tempInt.toString();
								JLabel score = new JLabel("Tier Score: " + scoreString);
								JButton showText = new JButton("Card Text");
								c.insets = new Insets(5, 5, 5, 5);
								c.weightx = 0.5; c.gridx = 0; c.gridy = 0; c.gridheight = 3; panel.add(label, c);
								c.gridheight = 1; c.gridx = 0; c.gridy = 6; panel.add(rarity, c);
								rarity.setHorizontalAlignment(SwingConstants.CENTER); 
								c.gridx = 0; c.gridy = 7;  panel.add(quantity, c);
								quantity.setHorizontalAlignment(SwingConstants.CENTER);
								c.gridx = 0; c.gridy = 8;  panel.add(score, c);
								score.setHorizontalAlignment(SwingConstants.CENTER); 
								c.gridx = 0; c.gridy = 4;  panel.add(showText, c);
								showText.setHorizontalAlignment(SwingConstants.CENTER); 
								showText.setMargin( new Insets(0,0,0,0) );if (smallImage)
								{
									c.weightx = 0.5; c.gridx = 0; c.gridy = 5; panel.add(fullSize, c); 
									fullSize.setHorizontalAlignment(SwingConstants.CENTER);
									fullSize.addActionListener(new ActionListener()
									{
										@Override
										public void actionPerformed(ActionEvent e) 
										{
											JFrame fullView = new JFrame();
											JPanel fullPanel = new JPanel();
											ImageLabel bigImage = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png"));
											fullPanel.add(bigImage);
											fullView.getContentPane().add(fullPanel);
											fullView.pack();
											fullView.setResizable(false);
											fullView.setVisible(true);
											
										}
										
									});
								}
								singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
								singleCardView.setBounds(100, 100, 496, 443);
								singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
								singleCardView.getContentPane().add(panel);
								singleCardView.pack();						
								singleCardView.setResizable(false);
								singleCardView.setVisible(true);
								
								showText.addActionListener(new ActionListener()
								{
									@Override
									public void actionPerformed(ActionEvent arg0) 
									{
										JFrame textFrame = new JFrame();
										textFrame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
										textFrame.setBounds(100, 100, 496, 443);
										textFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
										JPanel textPanel = new JPanel();
										JTextArea textField = new JTextArea(6, 40);
										textField.setEditable(false);
										String temp = dynamicCard.getText();
										String newTemp = WordUtils.wrap(temp, 75, "\n", false);
										textField.setText(newTemp);
										textField.setWrapStyleWord(true);
										JScrollPane vertScroll = new JScrollPane(textField);
										textPanel.add(vertScroll);
										textFrame.getContentPane().add(textPanel);
										textFrame.pack();
										textFrame.setResizable(false);
										textFrame.setVisible(true);
										
										
									}
								});
							
							
							}
						}

						@Override public void keyReleased(KeyEvent arg0) {} @Override public void keyTyped(KeyEvent arg0) {}
					};
					
					MouseListener mouseListener = new MouseAdapter() 
					{
						public void mouseClicked(MouseEvent e) 
						{
							if (e.getClickCount() == 2) 
							{    		
								dynamicCard = (Card) dynamicList.getSelectedValue();
								singleCardView = new JFrame();										
								JPanel panel = new JPanel();
								boolean smallImage = false;
								JButton fullSize = new JButton("Full Resolution");
								panel.setLayout(new GridBagLayout());
								GridBagConstraints c = new GridBagConstraints();
								c.fill = GridBagConstraints.HORIZONTAL;
								ImageLabel label;
								if (isImage("src/images/" + dynamicCard.getName() + "Small.png")) 
								{ 
									label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + "Small.png"));
									smallImage = true;
								}
								else { label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png")); }
								JLabel rarity = new JLabel(dynamicCard.getRarity()); textColor(dynamicCard, rarity);
								JLabel quantity = new JLabel("Quantity: " + dynamicCard.getQuantity());
								Integer tempInt = dynamicCard.getTierScore();
								String scoreString = tempInt.toString();
								JLabel score = new JLabel("Tier Score: " + scoreString);
								JButton showText = new JButton("Card Text");
								c.insets = new Insets(5, 5, 5, 5);
								c.weightx = 0.5; c.gridx = 0; c.gridy = 0; c.gridheight = 3; panel.add(label, c);
								c.gridheight = 1; c.gridx = 0; c.gridy = 6; panel.add(rarity, c);
								rarity.setHorizontalAlignment(SwingConstants.CENTER); 
								c.gridx = 0; c.gridy = 7;  panel.add(quantity, c);
								quantity.setHorizontalAlignment(SwingConstants.CENTER);
								c.gridx = 0; c.gridy = 8;  panel.add(score, c);
								score.setHorizontalAlignment(SwingConstants.CENTER); 
								c.gridx = 0; c.gridy = 4;  panel.add(showText, c);
								showText.setHorizontalAlignment(SwingConstants.CENTER); 
								showText.setMargin( new Insets(0,0,0,0) );if (smallImage)
								{
									c.weightx = 0.5; c.gridx = 0; c.gridy = 5; panel.add(fullSize, c); 
									fullSize.setHorizontalAlignment(SwingConstants.CENTER);
									fullSize.addActionListener(new ActionListener()
									{
										@Override
										public void actionPerformed(ActionEvent e) 
										{
											JFrame fullView = new JFrame();
											JPanel fullPanel = new JPanel();
											ImageLabel bigImage = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png"));
											fullPanel.add(bigImage);
											fullView.getContentPane().add(fullPanel);
											fullView.pack();
											fullView.setResizable(false);
											fullView.setVisible(true);
											
										}
										
									});
								}
								singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
								singleCardView.setBounds(100, 100, 496, 443);
								singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
								singleCardView.getContentPane().add(panel);
								singleCardView.pack();						
								singleCardView.setResizable(false);
								singleCardView.setVisible(true);
								
								showText.addActionListener(new ActionListener()
								{
									@Override
									public void actionPerformed(ActionEvent arg0) 
									{
										JFrame textFrame = new JFrame();
										textFrame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
										textFrame.setBounds(100, 100, 496, 443);
										textFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
										JPanel textPanel = new JPanel();
										JTextArea textField = new JTextArea(6, 40);
										textField.setEditable(false);
										String temp = dynamicCard.getText();
										String newTemp = WordUtils.wrap(temp, 75, "\n", false);
										textField.setText(newTemp);
										textField.setWrapStyleWord(true);
										JScrollPane vertScroll = new JScrollPane(textField);
										textPanel.add(vertScroll);
										textFrame.getContentPane().add(textPanel);
										textFrame.pack();
										textFrame.setResizable(false);
										textFrame.setVisible(true);
										
										
									}
								});
							
							
							}
						}
					};
					
					
					dynamicList.addKeyListener(keyListener);
					dynamicList.addMouseListener(mouseListener);
					c.weightx = 1.0; c.gridwidth = 2; c.weighty = 1.0; c.gridx = 0; c.gridy = 0; cardView.getContentPane().add(scrollPane, c);
					c.insets = new Insets(0, 5, 0, 5); c.gridwidth = 1; c.gridheight = 4; c.gridx = 0; c.gridy = 2; cardView.getContentPane().add(scrollPane2, c);
					//c.gridx = 1; c.gridy = 2; cardView.getContentPane().add(search, c);
					cardView.pack();
					cardView.setResizable(false);
					cardView.setVisible(true);
				}
			});
			mnViewDatabase.add(mntmCommon);
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
				cardView.setTitle("All Cards");
				cardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
				cardView.setBounds(100, 100, 600, 600);
				cardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				
				cardView.getContentPane().setLayout(new GridBagLayout());
				GridBagConstraints c = new GridBagConstraints();
				c.fill = GridBagConstraints.HORIZONTAL;

				ArrayList<Card >allCardsNoDupes = listMaker(draftAllCards);
				dynamicModel1.clear();
				for (Card card : allCardsNoDupes) { dynamicModel1.addElement(card); }
				dynamicList = new JList(dynamicModel1);
				JTextArea searchBar = new JTextArea(1, 15);
				JScrollPane scrollPane = new JScrollPane(dynamicList);
				JScrollPane scrollPane2 = new JScrollPane(searchBar);
				//JButton search = new JButton("Search");
				searchBar.addKeyListener(new KeyListener() 
				{
					@Override
					public void keyPressed(KeyEvent arg0) 
					{
						
						
					}

					@Override
					public void keyReleased(KeyEvent e) 
					{
						for (Card card : allCardsNoDupes)
						{
							//if (card.getName().indexOf(e.toString()) > 0) 
							if (card.getName().toLowerCase().contains(searchBar.getText().toLowerCase()))
							{
								dynamicModel1.addElement(card);
								
							} 
 						}
						
						
						
					}

					@Override
					public void keyTyped(KeyEvent arg0) 
					{
						dynamicModel1.clear();
					}
				});
				
				KeyListener keyListener = new KeyListener()
				{
					public void keyPressed(KeyEvent e)
					{
						if (e.getKeyCode() == KeyEvent.VK_ENTER)
						{
							dynamicCard = (Card) dynamicList.getSelectedValue();
							singleCardView = new JFrame();		
							singleCardView.setTitle(dynamicList.getSelectedValue().getName());
							JPanel panel = new JPanel();
							boolean smallImage = false;
							JButton fullSize = new JButton("Full Resolution");
							panel.setLayout(new GridBagLayout());
							GridBagConstraints c = new GridBagConstraints();
							c.fill = GridBagConstraints.HORIZONTAL;
							ImageLabel label;
							if (isImage("src/images/" + dynamicCard.getName() + "Small.png")) 
							{ 
								label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + "Small.png"));
								smallImage = true;
							}
							else { label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png")); }
							JLabel rarity = new JLabel(dynamicCard.getRarity()); textColor(dynamicCard, rarity);
							JLabel quantity = new JLabel("Quantity: " + dynamicCard.getQuantity());
							Integer tempInt = dynamicCard.getTierScore();
							String scoreString = tempInt.toString();
							JLabel score = new JLabel("Tier Score: " + scoreString);
							JButton showText = new JButton("Card Text");
							c.insets = new Insets(5, 5, 5, 5);
							c.weightx = 0.5; c.gridx = 0; c.gridy = 0; c.gridheight = 3; panel.add(label, c);
							c.gridheight = 1; c.gridx = 0; c.gridy = 6; panel.add(rarity, c);
							rarity.setHorizontalAlignment(SwingConstants.CENTER); 
							c.gridx = 0; c.gridy = 7;  panel.add(quantity, c);
							quantity.setHorizontalAlignment(SwingConstants.CENTER);
							c.gridx = 0; c.gridy = 8;  panel.add(score, c);
							score.setHorizontalAlignment(SwingConstants.CENTER); 
							c.gridx = 0; c.gridy = 4;  panel.add(showText, c);
							showText.setHorizontalAlignment(SwingConstants.CENTER); 
							showText.setMargin( new Insets(0,0,0,0) );if (smallImage)
							{
								c.weightx = 0.5; c.gridx = 0; c.gridy = 5; panel.add(fullSize, c); 
								fullSize.setHorizontalAlignment(SwingConstants.CENTER);
								fullSize.addActionListener(new ActionListener()
								{
									@Override
									public void actionPerformed(ActionEvent e) 
									{
										JFrame fullView = new JFrame();
										JPanel fullPanel = new JPanel();
										ImageLabel bigImage = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png"));
										fullPanel.add(bigImage);
										fullView.getContentPane().add(fullPanel);
										fullView.pack();
										fullView.setResizable(false);
										fullView.setVisible(true);
										
									}
									
								});
							}
							singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
							singleCardView.setBounds(100, 100, 496, 443);
							singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
							singleCardView.getContentPane().add(panel);
							singleCardView.pack();						
							singleCardView.setResizable(false);
							singleCardView.setVisible(true);
							
							showText.addActionListener(new ActionListener()
							{
								@Override
								public void actionPerformed(ActionEvent arg0) 
								{
									JFrame textFrame = new JFrame();
									textFrame.setTitle(dynamicList.getSelectedValue().getName() + " - Text");
									textFrame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
									textFrame.setBounds(100, 100, 496, 443);
									textFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
									JPanel textPanel = new JPanel();
									JTextArea textField = new JTextArea(6, 40);
									textField.setEditable(false);
									String temp = dynamicCard.getText();
									String newTemp = WordUtils.wrap(temp, 75, "\n", false);
									textField.setText(newTemp);
									textField.setWrapStyleWord(true);
									JScrollPane vertScroll = new JScrollPane(textField);
									textPanel.add(vertScroll);
									textFrame.getContentPane().add(textPanel);
									textFrame.pack();
									textFrame.setResizable(false);
									textFrame.setVisible(true);
									
									
								}
							});
						
						
						}
						
						
					}

					@Override public void keyReleased(KeyEvent arg0) {} @Override public void keyTyped(KeyEvent arg0) {}
				};
				
				MouseListener mouseListener = new MouseAdapter() 
				{
					public void mouseClicked(MouseEvent e) 
					{
						if (e.getClickCount() == 2) 
						{    		
							dynamicCard = (Card) dynamicList.getSelectedValue();
							singleCardView = new JFrame();			
							singleCardView.setTitle(dynamicList.getSelectedValue().getName());
							JPanel panel = new JPanel();
							boolean smallImage = false;
							JButton fullSize = new JButton("Full Resolution");
							panel.setLayout(new GridBagLayout());
							GridBagConstraints c = new GridBagConstraints();
							c.fill = GridBagConstraints.HORIZONTAL;
							ImageLabel label;
							if (isImage("src/images/" + dynamicCard.getName() + "Small.png")) 
							{ 
								label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + "Small.png"));
								smallImage = true;
							}
							else { label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png")); }
							JLabel rarity = new JLabel(dynamicCard.getRarity()); textColor(dynamicCard, rarity);
							textColor(dynamicCard, rarity);
							JLabel quantity = new JLabel("Quantity: " + dynamicCard.getQuantity());
							Integer tempInt = dynamicCard.getTierScore();
							String scoreString = tempInt.toString();
							JLabel score = new JLabel("Tier Score: " + scoreString);
							JButton showText = new JButton("Card Text");
							c.insets = new Insets(5, 5, 5, 5);
							c.weightx = 0.5; c.gridx = 0; c.gridy = 0; c.gridheight = 3; panel.add(label, c);
							c.gridheight = 1; c.gridx = 0; c.gridy = 6; panel.add(rarity, c);
							rarity.setHorizontalAlignment(SwingConstants.CENTER); 
							c.gridx = 0; c.gridy = 7;  panel.add(quantity, c);
							quantity.setHorizontalAlignment(SwingConstants.CENTER);
							c.gridx = 0; c.gridy = 8;  panel.add(score, c);
							score.setHorizontalAlignment(SwingConstants.CENTER); 
							c.gridx = 0; c.gridy = 4;  panel.add(showText, c);
							showText.setHorizontalAlignment(SwingConstants.CENTER); 
							showText.setMargin( new Insets(0,0,0,0) );if (smallImage)
							{
								c.weightx = 0.5; c.gridx = 0; c.gridy = 5; panel.add(fullSize, c); 
								fullSize.setHorizontalAlignment(SwingConstants.CENTER);
								fullSize.addActionListener(new ActionListener()
								{
									@Override
									public void actionPerformed(ActionEvent e) 
									{
										JFrame fullView = new JFrame();
										JPanel fullPanel = new JPanel();
										ImageLabel bigImage = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png"));
										fullPanel.add(bigImage);
										fullView.getContentPane().add(fullPanel);
										fullView.pack();
										fullView.setResizable(false);
										fullView.setVisible(true);
										
									}
									
								});
							}
							singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
							singleCardView.setBounds(100, 100, 496, 443);
							singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
							singleCardView.getContentPane().add(panel);
							singleCardView.pack();						
							singleCardView.setResizable(false);
							singleCardView.setVisible(true);
							
							showText.addActionListener(new ActionListener()
							{
								@Override
								public void actionPerformed(ActionEvent arg0) 
								{
									JFrame textFrame = new JFrame();
									textFrame.setTitle(dynamicList.getSelectedValue().getName() + " - Text");
									textFrame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
									textFrame.setBounds(100, 100, 496, 443);
									textFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
									JPanel textPanel = new JPanel();
									JTextArea textField = new JTextArea(6, 40);
									textField.setEditable(false);
									String temp = dynamicCard.getText();
									String newTemp = WordUtils.wrap(temp, 75, "\n", false);
									textField.setText(newTemp);
									textField.setWrapStyleWord(true);
									JScrollPane vertScroll = new JScrollPane(textField);
									textPanel.add(vertScroll);
									textFrame.getContentPane().add(textPanel);
									textFrame.pack();
									textFrame.setResizable(false);
									textFrame.setVisible(true);
									
									
								}
							});
						
						
						}
					}
				};
				
				
				dynamicList.addKeyListener(keyListener);
				dynamicList.addMouseListener(mouseListener);
				c.weightx = 1.0; c.gridwidth = 2; c.weighty = 1.0; c.gridx = 0; c.gridy = 0; cardView.getContentPane().add(scrollPane, c);
				c.insets = new Insets(0, 5, 0, 5); c.gridwidth = 1; c.gridheight = 4; c.gridx = 0; c.gridy = 2; cardView.getContentPane().add(scrollPane2, c);
				//c.gridx = 1; c.gridy = 2; cardView.getContentPane().add(search, c);
				cardView.pack();
				cardView.setResizable(false);
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

				JFrame cardView = new JFrame();	
				cardView.setTitle("Ultimate Rares");
				cardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
				cardView.setBounds(100, 100, 600, 600);
				cardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				
				cardView.getContentPane().setLayout(new GridBagLayout());
				GridBagConstraints c = new GridBagConstraints();
				c.fill = GridBagConstraints.HORIZONTAL;

				ArrayList<Card >allCardsNoDupes = listMakerRarity(draftAllCards, "Ultimate Rare");
				dynamicModel1.clear();
				for (Card card : allCardsNoDupes) { dynamicModel1.addElement(card); }
				dynamicList = new JList(dynamicModel1);
				JTextArea searchBar = new JTextArea(1, 15);
				JScrollPane scrollPane = new JScrollPane(dynamicList);
				JScrollPane scrollPane2 = new JScrollPane(searchBar);
				//JButton search = new JButton("Search");
				searchBar.addKeyListener(new KeyListener() 
				{
					@Override
					public void keyPressed(KeyEvent arg0) 
					{
						
						
					}

					@Override
					public void keyReleased(KeyEvent e) 
					{
						for (Card card : allCardsNoDupes)
						{
							//if (card.getName().indexOf(e.toString()) > 0) 
							if (card.getName().toLowerCase().contains(searchBar.getText().toLowerCase()))
							{
								dynamicModel1.addElement(card);
							} 
 						}
						
						
						
					}

					@Override
					public void keyTyped(KeyEvent arg0) 
					{
						dynamicModel1.clear();
					}
				});
				
				KeyListener keyListener = new KeyListener()
				{
					public void keyPressed(KeyEvent e)
					{
						if (e.getKeyCode() == KeyEvent.VK_ENTER)
						{
							dynamicCard = (Card) dynamicList.getSelectedValue();
							singleCardView = new JFrame();										
							JPanel panel = new JPanel();
							boolean smallImage = false;
							JButton fullSize = new JButton("Full Resolution");
							panel.setLayout(new GridBagLayout());
							GridBagConstraints c = new GridBagConstraints();
							c.fill = GridBagConstraints.HORIZONTAL;
							ImageLabel label;
							if (isImage("src/images/" + dynamicCard.getName() + "Small.png")) 
							{ 
								label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + "Small.png"));
								smallImage = true;
							}
							else { label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png")); }
							JLabel rarity = new JLabel(dynamicCard.getRarity()); textColor(dynamicCard, rarity);
							textColor(dynamicCard, rarity);
							JLabel quantity = new JLabel("Quantity: " + dynamicCard.getQuantity());
							Integer tempInt = dynamicCard.getTierScore();
							String scoreString = tempInt.toString();
							JLabel score = new JLabel("Tier Score: " + scoreString);
							JButton showText = new JButton("Card Text");
							c.insets = new Insets(5, 5, 5, 5);
							c.weightx = 0.5; c.gridx = 0; c.gridy = 0; c.gridheight = 3; panel.add(label, c);
							c.gridheight = 1; c.gridx = 0; c.gridy = 6; panel.add(rarity, c);
							rarity.setHorizontalAlignment(SwingConstants.CENTER); 
							c.gridx = 0; c.gridy = 7;  panel.add(quantity, c);
							quantity.setHorizontalAlignment(SwingConstants.CENTER);
							c.gridx = 0; c.gridy = 8;  panel.add(score, c);
							score.setHorizontalAlignment(SwingConstants.CENTER); 
							c.gridx = 0; c.gridy = 4;  panel.add(showText, c);
							showText.setHorizontalAlignment(SwingConstants.CENTER); 
							showText.setMargin( new Insets(0,0,0,0) );if (smallImage)
							{
								c.weightx = 0.5; c.gridx = 0; c.gridy = 5; panel.add(fullSize, c); 
								fullSize.setHorizontalAlignment(SwingConstants.CENTER);
								fullSize.addActionListener(new ActionListener()
								{
									@Override
									public void actionPerformed(ActionEvent e) 
									{
										JFrame fullView = new JFrame();
										JPanel fullPanel = new JPanel();
										ImageLabel bigImage = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png"));
										fullPanel.add(bigImage);
										fullView.getContentPane().add(fullPanel);
										fullView.pack();
										fullView.setResizable(false);
										fullView.setVisible(true);
										
									}
									
								});
							}
							singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
							singleCardView.setBounds(100, 100, 496, 443);
							singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
							singleCardView.getContentPane().add(panel);
							singleCardView.pack();						
							singleCardView.setResizable(false);
							singleCardView.setVisible(true);
							
							showText.addActionListener(new ActionListener()
							{
								@Override
								public void actionPerformed(ActionEvent arg0) 
								{
									JFrame textFrame = new JFrame();
									textFrame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
									textFrame.setBounds(100, 100, 496, 443);
									textFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
									JPanel textPanel = new JPanel();
									JTextArea textField = new JTextArea(6, 40);
									textField.setEditable(false);
									String temp = dynamicCard.getText();
									String newTemp = WordUtils.wrap(temp, 75, "\n", false);
									textField.setText(newTemp);
									textField.setWrapStyleWord(true);
									JScrollPane vertScroll = new JScrollPane(textField);
									textPanel.add(vertScroll);
									textFrame.getContentPane().add(textPanel);
									textFrame.pack();
									textFrame.setResizable(false);
									textFrame.setVisible(true);
									
									
								}
							});
						
						
						}
					}

					@Override public void keyReleased(KeyEvent arg0) {} @Override public void keyTyped(KeyEvent arg0) {}
				};
				
				MouseListener mouseListener = new MouseAdapter() 
				{
					public void mouseClicked(MouseEvent e) 
					{
						if (e.getClickCount() == 2) 
						{    		
							dynamicCard = (Card) dynamicList.getSelectedValue();
							singleCardView = new JFrame();										
							JPanel panel = new JPanel();
							boolean smallImage = false;
							JButton fullSize = new JButton("Full Resolution");
							panel.setLayout(new GridBagLayout());
							GridBagConstraints c = new GridBagConstraints();
							c.fill = GridBagConstraints.HORIZONTAL;
							ImageLabel label;
							if (isImage("src/images/" + dynamicCard.getName() + "Small.png")) 
							{ 
								label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + "Small.png"));
								smallImage = true;
							}
							else { label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png")); }
							JLabel rarity = new JLabel(dynamicCard.getRarity()); textColor(dynamicCard, rarity);
							JLabel quantity = new JLabel("Quantity: " + dynamicCard.getQuantity());
							Integer tempInt = dynamicCard.getTierScore();
							String scoreString = tempInt.toString();
							JLabel score = new JLabel("Tier Score: " + scoreString);
							JButton showText = new JButton("Card Text");
							c.insets = new Insets(5, 5, 5, 5);
							c.weightx = 0.5; c.gridx = 0; c.gridy = 0; c.gridheight = 3; panel.add(label, c);
							c.gridheight = 1; c.gridx = 0; c.gridy = 6; panel.add(rarity, c);
							rarity.setHorizontalAlignment(SwingConstants.CENTER); 
							c.gridx = 0; c.gridy = 7;  panel.add(quantity, c);
							quantity.setHorizontalAlignment(SwingConstants.CENTER);
							c.gridx = 0; c.gridy = 8;  panel.add(score, c);
							score.setHorizontalAlignment(SwingConstants.CENTER); 
							c.gridx = 0; c.gridy = 4;  panel.add(showText, c);
							showText.setHorizontalAlignment(SwingConstants.CENTER); 
							showText.setMargin( new Insets(0,0,0,0) );if (smallImage)
							{
								c.weightx = 0.5; c.gridx = 0; c.gridy = 5; panel.add(fullSize, c); 
								fullSize.setHorizontalAlignment(SwingConstants.CENTER);
								fullSize.addActionListener(new ActionListener()
								{
									@Override
									public void actionPerformed(ActionEvent e) 
									{
										JFrame fullView = new JFrame();
										JPanel fullPanel = new JPanel();
										ImageLabel bigImage = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png"));
										fullPanel.add(bigImage);
										fullView.getContentPane().add(fullPanel);
										fullView.pack();
										fullView.setResizable(false);
										fullView.setVisible(true);
										
									}
									
								});
							}
							singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
							singleCardView.setBounds(100, 100, 496, 443);
							singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
							singleCardView.getContentPane().add(panel);
							singleCardView.pack();						
							singleCardView.setResizable(false);
							singleCardView.setVisible(true);
							
							showText.addActionListener(new ActionListener()
							{
								@Override
								public void actionPerformed(ActionEvent arg0) 
								{
									JFrame textFrame = new JFrame();
									textFrame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
									textFrame.setBounds(100, 100, 496, 443);
									textFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
									JPanel textPanel = new JPanel();
									JTextArea textField = new JTextArea(6, 40);
									textField.setEditable(false);
									String temp = dynamicCard.getText();
									String newTemp = WordUtils.wrap(temp, 75, "\n", false);
									textField.setText(newTemp);
									textField.setWrapStyleWord(true);
									JScrollPane vertScroll = new JScrollPane(textField);
									textPanel.add(vertScroll);
									textFrame.getContentPane().add(textPanel);
									textFrame.pack();
									textFrame.setResizable(false);
									textFrame.setVisible(true);
									
									
								}
							});
						
						
						}
					}
				};
				
				
				dynamicList.addKeyListener(keyListener);
				dynamicList.addMouseListener(mouseListener);
				c.weightx = 1.0; c.gridwidth = 2; c.weighty = 1.0; c.gridx = 0; c.gridy = 0; cardView.getContentPane().add(scrollPane, c);
				c.insets = new Insets(0, 5, 0, 5); c.gridwidth = 1; c.gridheight = 4; c.gridx = 0; c.gridy = 2; cardView.getContentPane().add(scrollPane2, c);
				//c.gridx = 1; c.gridy = 2; cardView.getContentPane().add(search, c);
				cardView.pack();
				cardView.setResizable(false);
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
				cardView.setTitle("Ultra Rares");
				cardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
				cardView.setBounds(100, 100, 600, 600);
				cardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				
				cardView.getContentPane().setLayout(new GridBagLayout());
				GridBagConstraints c = new GridBagConstraints();
				c.fill = GridBagConstraints.HORIZONTAL;

				ArrayList<Card >allCardsNoDupes = listMakerRarity(draftAllCards, "Ultra Rare");
				dynamicModel1.clear();
				for (Card card : allCardsNoDupes) { dynamicModel1.addElement(card); }
				dynamicList = new JList(dynamicModel1);
				JTextArea searchBar = new JTextArea(1, 15);
				JScrollPane scrollPane = new JScrollPane(dynamicList);
				JScrollPane scrollPane2 = new JScrollPane(searchBar);
				//JButton search = new JButton("Search");
				searchBar.addKeyListener(new KeyListener() 
				{
					@Override
					public void keyPressed(KeyEvent arg0) 
					{
						
						
					}

					@Override
					public void keyReleased(KeyEvent e) 
					{
						for (Card card : allCardsNoDupes)
						{
							//if (card.getName().indexOf(e.toString()) > 0) 
							if (card.getName().toLowerCase().contains(searchBar.getText().toLowerCase()))
							{
								dynamicModel1.addElement(card);
							} 
 						}
						
						
						
					}

					@Override
					public void keyTyped(KeyEvent arg0) 
					{
						dynamicModel1.clear();
					}
				});
				
				KeyListener keyListener = new KeyListener()
				{
					public void keyPressed(KeyEvent e)
					{
						if (e.getKeyCode() == KeyEvent.VK_ENTER)
						{
							dynamicCard = (Card) dynamicList.getSelectedValue();
							singleCardView = new JFrame();										
							JPanel panel = new JPanel();
							boolean smallImage = false;
							JButton fullSize = new JButton("Full Resolution");
							panel.setLayout(new GridBagLayout());
							GridBagConstraints c = new GridBagConstraints();
							c.fill = GridBagConstraints.HORIZONTAL;
							ImageLabel label;
							if (isImage("src/images/" + dynamicCard.getName() + "Small.png")) 
							{ 
								label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + "Small.png"));
								smallImage = true;
							}
							else { label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png")); }
							JLabel rarity = new JLabel(dynamicCard.getRarity()); textColor(dynamicCard, rarity);
							textColor(dynamicCard, rarity);
							JLabel quantity = new JLabel("Quantity: " + dynamicCard.getQuantity());
							Integer tempInt = dynamicCard.getTierScore();
							String scoreString = tempInt.toString();
							JLabel score = new JLabel("Tier Score: " + scoreString);
							JButton showText = new JButton("Card Text");
							c.insets = new Insets(5, 5, 5, 5);
							c.weightx = 0.5; c.gridx = 0; c.gridy = 0; c.gridheight = 3; panel.add(label, c);
							c.gridheight = 1; c.gridx = 0; c.gridy = 6; panel.add(rarity, c);
							rarity.setHorizontalAlignment(SwingConstants.CENTER); 
							c.gridx = 0; c.gridy = 7;  panel.add(quantity, c);
							quantity.setHorizontalAlignment(SwingConstants.CENTER);
							c.gridx = 0; c.gridy = 8;  panel.add(score, c);
							score.setHorizontalAlignment(SwingConstants.CENTER); 
							c.gridx = 0; c.gridy = 4;  panel.add(showText, c);
							showText.setHorizontalAlignment(SwingConstants.CENTER); 
							showText.setMargin( new Insets(0,0,0,0) );if (smallImage)
							{
								c.weightx = 0.5; c.gridx = 0; c.gridy = 5; panel.add(fullSize, c); 
								fullSize.setHorizontalAlignment(SwingConstants.CENTER);
								fullSize.addActionListener(new ActionListener()
								{
									@Override
									public void actionPerformed(ActionEvent e) 
									{
										JFrame fullView = new JFrame();
										JPanel fullPanel = new JPanel();
										ImageLabel bigImage = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png"));
										fullPanel.add(bigImage);
										fullView.getContentPane().add(fullPanel);
										fullView.pack();
										fullView.setResizable(false);
										fullView.setVisible(true);
										
									}
									
								});
							}
							singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
							singleCardView.setBounds(100, 100, 496, 443);
							singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
							singleCardView.getContentPane().add(panel);
							singleCardView.pack();						
							singleCardView.setResizable(false);
							singleCardView.setVisible(true);
							
							showText.addActionListener(new ActionListener()
							{
								@Override
								public void actionPerformed(ActionEvent arg0) 
								{
									JFrame textFrame = new JFrame();
									textFrame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
									textFrame.setBounds(100, 100, 496, 443);
									textFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
									JPanel textPanel = new JPanel();
									JTextArea textField = new JTextArea(6, 40);
									textField.setEditable(false);
									String temp = dynamicCard.getText();
									String newTemp = WordUtils.wrap(temp, 75, "\n", false);
									textField.setText(newTemp);
									textField.setWrapStyleWord(true);
									JScrollPane vertScroll = new JScrollPane(textField);
									textPanel.add(vertScroll);
									textFrame.getContentPane().add(textPanel);
									textFrame.pack();
									textFrame.setResizable(false);
									textFrame.setVisible(true);
									
									
								}
							});
						
						
						}
					}

					@Override public void keyReleased(KeyEvent arg0) {} @Override public void keyTyped(KeyEvent arg0) {}
				};
				
				MouseListener mouseListener = new MouseAdapter() 
				{
					public void mouseClicked(MouseEvent e) 
					{
						if (e.getClickCount() == 2) 
						{    		
							dynamicCard = (Card) dynamicList.getSelectedValue();
							singleCardView = new JFrame();										
							JPanel panel = new JPanel();
							boolean smallImage = false;
							JButton fullSize = new JButton("Full Resolution");
							panel.setLayout(new GridBagLayout());
							GridBagConstraints c = new GridBagConstraints();
							c.fill = GridBagConstraints.HORIZONTAL;
							ImageLabel label;
							if (isImage("src/images/" + dynamicCard.getName() + "Small.png")) 
							{ 
								label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + "Small.png"));
								smallImage = true;
							}
							else { label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png")); }
							JLabel rarity = new JLabel(dynamicCard.getRarity()); textColor(dynamicCard, rarity);
							JLabel quantity = new JLabel("Quantity: " + dynamicCard.getQuantity());
							Integer tempInt = dynamicCard.getTierScore();
							String scoreString = tempInt.toString();
							JLabel score = new JLabel("Tier Score: " + scoreString);
							JButton showText = new JButton("Card Text");
							c.insets = new Insets(5, 5, 5, 5);
							c.weightx = 0.5; c.gridx = 0; c.gridy = 0; c.gridheight = 3; panel.add(label, c);
							c.gridheight = 1; c.gridx = 0; c.gridy = 6; panel.add(rarity, c);
							rarity.setHorizontalAlignment(SwingConstants.CENTER); 
							c.gridx = 0; c.gridy = 7;  panel.add(quantity, c);
							quantity.setHorizontalAlignment(SwingConstants.CENTER);
							c.gridx = 0; c.gridy = 8;  panel.add(score, c);
							score.setHorizontalAlignment(SwingConstants.CENTER); 
							c.gridx = 0; c.gridy = 4;  panel.add(showText, c);
							showText.setHorizontalAlignment(SwingConstants.CENTER); 
							showText.setMargin( new Insets(0,0,0,0) );if (smallImage)
							{
								c.weightx = 0.5; c.gridx = 0; c.gridy = 5; panel.add(fullSize, c); 
								fullSize.setHorizontalAlignment(SwingConstants.CENTER);
								fullSize.addActionListener(new ActionListener()
								{
									@Override
									public void actionPerformed(ActionEvent e) 
									{
										JFrame fullView = new JFrame();
										JPanel fullPanel = new JPanel();
										ImageLabel bigImage = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png"));
										fullPanel.add(bigImage);
										fullView.getContentPane().add(fullPanel);
										fullView.pack();
										fullView.setResizable(false);
										fullView.setVisible(true);
										
									}
									
								});
							}
							singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
							singleCardView.setBounds(100, 100, 496, 443);
							singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
							singleCardView.getContentPane().add(panel);
							singleCardView.pack();						
							singleCardView.setResizable(false);
							singleCardView.setVisible(true);
							
							showText.addActionListener(new ActionListener()
							{
								@Override
								public void actionPerformed(ActionEvent arg0) 
								{
									JFrame textFrame = new JFrame();
									textFrame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
									textFrame.setBounds(100, 100, 496, 443);
									textFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
									JPanel textPanel = new JPanel();
									JTextArea textField = new JTextArea(6, 40);
									textField.setEditable(false);
									String temp = dynamicCard.getText();
									String newTemp = WordUtils.wrap(temp, 75, "\n", false);
									textField.setText(newTemp);
									textField.setWrapStyleWord(true);
									JScrollPane vertScroll = new JScrollPane(textField);
									textPanel.add(vertScroll);
									textFrame.getContentPane().add(textPanel);
									textFrame.pack();
									textFrame.setResizable(false);
									textFrame.setVisible(true);
									
									
								}
							});
						
						
						}
					}
				};
				
				
				dynamicList.addKeyListener(keyListener);
				dynamicList.addMouseListener(mouseListener);
				c.weightx = 1.0; c.gridwidth = 2; c.weighty = 1.0; c.gridx = 0; c.gridy = 0; cardView.getContentPane().add(scrollPane, c);
				c.insets = new Insets(0, 5, 0, 5); c.gridwidth = 1; c.gridheight = 4; c.gridx = 0; c.gridy = 2; cardView.getContentPane().add(scrollPane2, c);
				//c.gridx = 1; c.gridy = 2; cardView.getContentPane().add(search, c);
				cardView.pack();
				cardView.setResizable(false);
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
				cardView.setTitle("Super Rares");
				cardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
				cardView.setBounds(100, 100, 600, 600);
				cardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				
				cardView.getContentPane().setLayout(new GridBagLayout());
				GridBagConstraints c = new GridBagConstraints();
				c.fill = GridBagConstraints.HORIZONTAL;

				ArrayList<Card >allCardsNoDupes = listMakerRarity(draftAllCards, "Super Rare");
				dynamicModel1.clear();
				for (Card card : allCardsNoDupes) { dynamicModel1.addElement(card); }
				dynamicList = new JList(dynamicModel1);
				JTextArea searchBar = new JTextArea(1, 15);
				JScrollPane scrollPane = new JScrollPane(dynamicList);
				JScrollPane scrollPane2 = new JScrollPane(searchBar);
				//JButton search = new JButton("Search");
				searchBar.addKeyListener(new KeyListener() 
				{
					@Override
					public void keyPressed(KeyEvent arg0) 
					{
						
						
					}

					@Override
					public void keyReleased(KeyEvent e) 
					{
						for (Card card : allCardsNoDupes)
						{
							//if (card.getName().indexOf(e.toString()) > 0) 
							if (card.getName().toLowerCase().contains(searchBar.getText().toLowerCase()))
							{
								dynamicModel1.addElement(card);
							} 
 						}
						
						
						
					}

					@Override
					public void keyTyped(KeyEvent arg0) 
					{
						dynamicModel1.clear();
					}
				});
				
				KeyListener keyListener = new KeyListener()
				{
					public void keyPressed(KeyEvent e)
					{
						if (e.getKeyCode() == KeyEvent.VK_ENTER)
						{
							dynamicCard = (Card) dynamicList.getSelectedValue();
							singleCardView = new JFrame();										
							JPanel panel = new JPanel();
							boolean smallImage = false;
							JButton fullSize = new JButton("Full Resolution");
							panel.setLayout(new GridBagLayout());
							GridBagConstraints c = new GridBagConstraints();
							c.fill = GridBagConstraints.HORIZONTAL;
							ImageLabel label;
							if (isImage("src/images/" + dynamicCard.getName() + "Small.png")) 
							{ 
								label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + "Small.png"));
								smallImage = true;
							}
							else { label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png")); }
							JLabel rarity = new JLabel(dynamicCard.getRarity()); textColor(dynamicCard, rarity);
							textColor(dynamicCard, rarity);
							JLabel quantity = new JLabel("Quantity: " + dynamicCard.getQuantity());
							Integer tempInt = dynamicCard.getTierScore();
							String scoreString = tempInt.toString();
							JLabel score = new JLabel("Tier Score: " + scoreString);
							JButton showText = new JButton("Card Text");
							c.insets = new Insets(5, 5, 5, 5);
							c.weightx = 0.5; c.gridx = 0; c.gridy = 0; c.gridheight = 3; panel.add(label, c);
							c.gridheight = 1; c.gridx = 0; c.gridy = 6; panel.add(rarity, c);
							rarity.setHorizontalAlignment(SwingConstants.CENTER); 
							c.gridx = 0; c.gridy = 7;  panel.add(quantity, c);
							quantity.setHorizontalAlignment(SwingConstants.CENTER);
							c.gridx = 0; c.gridy = 8;  panel.add(score, c);
							score.setHorizontalAlignment(SwingConstants.CENTER); 
							c.gridx = 0; c.gridy = 4;  panel.add(showText, c);
							showText.setHorizontalAlignment(SwingConstants.CENTER); 
							showText.setMargin( new Insets(0,0,0,0) );if (smallImage)
							{
								c.weightx = 0.5; c.gridx = 0; c.gridy = 5; panel.add(fullSize, c); 
								fullSize.setHorizontalAlignment(SwingConstants.CENTER);
								fullSize.addActionListener(new ActionListener()
								{
									@Override
									public void actionPerformed(ActionEvent e) 
									{
										JFrame fullView = new JFrame();
										JPanel fullPanel = new JPanel();
										ImageLabel bigImage = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png"));
										fullPanel.add(bigImage);
										fullView.getContentPane().add(fullPanel);
										fullView.pack();
										fullView.setResizable(false);
										fullView.setVisible(true);
										
									}
									
								});
							}
							singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
							singleCardView.setBounds(100, 100, 496, 443);
							singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
							singleCardView.getContentPane().add(panel);
							singleCardView.pack();						
							singleCardView.setResizable(false);
							singleCardView.setVisible(true);
							
							showText.addActionListener(new ActionListener()
							{
								@Override
								public void actionPerformed(ActionEvent arg0) 
								{
									JFrame textFrame = new JFrame();
									textFrame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
									textFrame.setBounds(100, 100, 496, 443);
									textFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
									JPanel textPanel = new JPanel();
									JTextArea textField = new JTextArea(6, 40);
									textField.setEditable(false);
									String temp = dynamicCard.getText();
									String newTemp = WordUtils.wrap(temp, 75, "\n", false);
									textField.setText(newTemp);
									textField.setWrapStyleWord(true);
									JScrollPane vertScroll = new JScrollPane(textField);
									textPanel.add(vertScroll);
									textFrame.getContentPane().add(textPanel);
									textFrame.pack();
									textFrame.setResizable(false);
									textFrame.setVisible(true);
									
									
								}
							});
						
						
						}
					}

					@Override public void keyReleased(KeyEvent arg0) {} @Override public void keyTyped(KeyEvent arg0) {}
				};
				
				MouseListener mouseListener = new MouseAdapter() 
				{
					public void mouseClicked(MouseEvent e) 
					{
						if (e.getClickCount() == 2) 
						{    		
							dynamicCard = (Card) dynamicList.getSelectedValue();
							singleCardView = new JFrame();										
							JPanel panel = new JPanel();
							boolean smallImage = false;
							JButton fullSize = new JButton("Full Resolution");
							panel.setLayout(new GridBagLayout());
							GridBagConstraints c = new GridBagConstraints();
							c.fill = GridBagConstraints.HORIZONTAL;
							ImageLabel label;
							if (isImage("src/images/" + dynamicCard.getName() + "Small.png")) 
							{ 
								label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + "Small.png"));
								smallImage = true;
							}
							else { label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png")); }
							JLabel rarity = new JLabel(dynamicCard.getRarity()); textColor(dynamicCard, rarity);
							JLabel quantity = new JLabel("Quantity: " + dynamicCard.getQuantity());
							Integer tempInt = dynamicCard.getTierScore();
							String scoreString = tempInt.toString();
							JLabel score = new JLabel("Tier Score: " + scoreString);
							JButton showText = new JButton("Card Text");
							c.insets = new Insets(5, 5, 5, 5);
							c.weightx = 0.5; c.gridx = 0; c.gridy = 0; c.gridheight = 3; panel.add(label, c);
							c.gridheight = 1; c.gridx = 0; c.gridy = 6; panel.add(rarity, c);
							rarity.setHorizontalAlignment(SwingConstants.CENTER); 
							c.gridx = 0; c.gridy = 7;  panel.add(quantity, c);
							quantity.setHorizontalAlignment(SwingConstants.CENTER);
							c.gridx = 0; c.gridy = 8;  panel.add(score, c);
							score.setHorizontalAlignment(SwingConstants.CENTER); 
							c.gridx = 0; c.gridy = 4;  panel.add(showText, c);
							showText.setHorizontalAlignment(SwingConstants.CENTER); 
							showText.setMargin( new Insets(0,0,0,0) );if (smallImage)
							{
								c.weightx = 0.5; c.gridx = 0; c.gridy = 5; panel.add(fullSize, c); 
								fullSize.setHorizontalAlignment(SwingConstants.CENTER);
								fullSize.addActionListener(new ActionListener()
								{
									@Override
									public void actionPerformed(ActionEvent e) 
									{
										JFrame fullView = new JFrame();
										JPanel fullPanel = new JPanel();
										ImageLabel bigImage = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png"));
										fullPanel.add(bigImage);
										fullView.getContentPane().add(fullPanel);
										fullView.pack();
										fullView.setResizable(false);
										fullView.setVisible(true);
										
									}
									
								});
							}
							singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
							singleCardView.setBounds(100, 100, 496, 443);
							singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
							singleCardView.getContentPane().add(panel);
							singleCardView.pack();						
							singleCardView.setResizable(false);
							singleCardView.setVisible(true);
							
							showText.addActionListener(new ActionListener()
							{
								@Override
								public void actionPerformed(ActionEvent arg0) 
								{
									JFrame textFrame = new JFrame();
									textFrame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
									textFrame.setBounds(100, 100, 496, 443);
									textFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
									JPanel textPanel = new JPanel();
									JTextArea textField = new JTextArea(6, 40);
									textField.setEditable(false);
									String temp = dynamicCard.getText();
									String newTemp = WordUtils.wrap(temp, 75, "\n", false);
									textField.setText(newTemp);
									textField.setWrapStyleWord(true);
									JScrollPane vertScroll = new JScrollPane(textField);
									textPanel.add(vertScroll);
									textFrame.getContentPane().add(textPanel);
									textFrame.pack();
									textFrame.setResizable(false);
									textFrame.setVisible(true);
									
									
								}
							});
						
						
						}
					}
				};
				
				
				dynamicList.addKeyListener(keyListener);
				dynamicList.addMouseListener(mouseListener);
				c.weightx = 1.0; c.gridwidth = 2; c.weighty = 1.0; c.gridx = 0; c.gridy = 0; cardView.getContentPane().add(scrollPane, c);
				c.insets = new Insets(0, 5, 0, 5); c.gridwidth = 1; c.gridheight = 4; c.gridx = 0; c.gridy = 2; cardView.getContentPane().add(scrollPane2, c);
				//c.gridx = 1; c.gridy = 2; cardView.getContentPane().add(search, c);
				cardView.pack();
				cardView.setResizable(false);
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
				cardView.setTitle("Rares");
				cardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
				cardView.setBounds(100, 100, 600, 600);
				cardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				
				cardView.getContentPane().setLayout(new GridBagLayout());
				GridBagConstraints c = new GridBagConstraints();
				c.fill = GridBagConstraints.HORIZONTAL;

				ArrayList<Card >allCardsNoDupes = listMakerRarity(draftAllCards, "Rare");
				dynamicModel1.clear();
				for (Card card : allCardsNoDupes) { dynamicModel1.addElement(card); }
				dynamicList = new JList(dynamicModel1);
				JTextArea searchBar = new JTextArea(1, 15);
				JScrollPane scrollPane = new JScrollPane(dynamicList);
				JScrollPane scrollPane2 = new JScrollPane(searchBar);
				//JButton search = new JButton("Search");
				searchBar.addKeyListener(new KeyListener() 
				{
					@Override
					public void keyPressed(KeyEvent arg0) 
					{
						
						
					}

					@Override
					public void keyReleased(KeyEvent e) 
					{
						for (Card card : allCardsNoDupes)
						{
							//if (card.getName().indexOf(e.toString()) > 0) 
							if (card.getName().toLowerCase().contains(searchBar.getText().toLowerCase()))
							{
								dynamicModel1.addElement(card);
							} 
 						}
						
						
						
					}

					@Override
					public void keyTyped(KeyEvent arg0) 
					{
						dynamicModel1.clear();
					}
				});
				
				KeyListener keyListener = new KeyListener()
				{
					public void keyPressed(KeyEvent e)
					{
						if (e.getKeyCode() == KeyEvent.VK_ENTER)
						{
							dynamicCard = (Card) dynamicList.getSelectedValue();
							singleCardView = new JFrame();										
							JPanel panel = new JPanel();
							boolean smallImage = false;
							JButton fullSize = new JButton("Full Resolution");
							panel.setLayout(new GridBagLayout());
							GridBagConstraints c = new GridBagConstraints();
							c.fill = GridBagConstraints.HORIZONTAL;
							ImageLabel label;
							if (isImage("src/images/" + dynamicCard.getName() + "Small.png")) 
							{ 
								label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + "Small.png"));
								smallImage = true;
							}
							else { label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png")); }
							JLabel rarity = new JLabel(dynamicCard.getRarity()); textColor(dynamicCard, rarity);
							textColor(dynamicCard, rarity);
							JLabel quantity = new JLabel("Quantity: " + dynamicCard.getQuantity());
							Integer tempInt = dynamicCard.getTierScore();
							String scoreString = tempInt.toString();
							JLabel score = new JLabel("Tier Score: " + scoreString);
							JButton showText = new JButton("Card Text");
							c.insets = new Insets(5, 5, 5, 5);
							c.weightx = 0.5; c.gridx = 0; c.gridy = 0; c.gridheight = 3; panel.add(label, c);
							c.gridheight = 1; c.gridx = 0; c.gridy = 6; panel.add(rarity, c);
							rarity.setHorizontalAlignment(SwingConstants.CENTER); 
							c.gridx = 0; c.gridy = 7;  panel.add(quantity, c);
							quantity.setHorizontalAlignment(SwingConstants.CENTER);
							c.gridx = 0; c.gridy = 8;  panel.add(score, c);
							score.setHorizontalAlignment(SwingConstants.CENTER); 
							c.gridx = 0; c.gridy = 4;  panel.add(showText, c);
							showText.setHorizontalAlignment(SwingConstants.CENTER); 
							showText.setMargin( new Insets(0,0,0,0) );if (smallImage)
							{
								c.weightx = 0.5; c.gridx = 0; c.gridy = 5; panel.add(fullSize, c); 
								fullSize.setHorizontalAlignment(SwingConstants.CENTER);
								fullSize.addActionListener(new ActionListener()
								{
									@Override
									public void actionPerformed(ActionEvent e) 
									{
										JFrame fullView = new JFrame();
										JPanel fullPanel = new JPanel();
										ImageLabel bigImage = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png"));
										fullPanel.add(bigImage);
										fullView.getContentPane().add(fullPanel);
										fullView.pack();
										fullView.setResizable(false);
										fullView.setVisible(true);
										
									}
									
								});
							}
							singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
							singleCardView.setBounds(100, 100, 496, 443);
							singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
							singleCardView.getContentPane().add(panel);
							singleCardView.pack();						
							singleCardView.setResizable(false);
							singleCardView.setVisible(true);
							
							showText.addActionListener(new ActionListener()
							{
								@Override
								public void actionPerformed(ActionEvent arg0) 
								{
									JFrame textFrame = new JFrame();
									textFrame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
									textFrame.setBounds(100, 100, 496, 443);
									textFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
									JPanel textPanel = new JPanel();
									JTextArea textField = new JTextArea(6, 40);
									textField.setEditable(false);
									String temp = dynamicCard.getText();
									String newTemp = WordUtils.wrap(temp, 75, "\n", false);
									textField.setText(newTemp);
									textField.setWrapStyleWord(true);
									JScrollPane vertScroll = new JScrollPane(textField);
									textPanel.add(vertScroll);
									textFrame.getContentPane().add(textPanel);
									textFrame.pack();
									textFrame.setResizable(false);
									textFrame.setVisible(true);
									
									
								}
							});
						
						
						}
					}

					@Override public void keyReleased(KeyEvent arg0) {} @Override public void keyTyped(KeyEvent arg0) {}
				};
				
				MouseListener mouseListener = new MouseAdapter() 
				{
					public void mouseClicked(MouseEvent e) 
					{
						if (e.getClickCount() == 2) 
						{    		
							dynamicCard = (Card) dynamicList.getSelectedValue();
							singleCardView = new JFrame();										
							JPanel panel = new JPanel();
							boolean smallImage = false;
							JButton fullSize = new JButton("Full Resolution");
							panel.setLayout(new GridBagLayout());
							GridBagConstraints c = new GridBagConstraints();
							c.fill = GridBagConstraints.HORIZONTAL;
							ImageLabel label;
							if (isImage("src/images/" + dynamicCard.getName() + "Small.png")) 
							{ 
								label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + "Small.png"));
								smallImage = true;
							}
							else { label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png")); }
							JLabel rarity = new JLabel(dynamicCard.getRarity()); textColor(dynamicCard, rarity);
							JLabel quantity = new JLabel("Quantity: " + dynamicCard.getQuantity());
							Integer tempInt = dynamicCard.getTierScore();
							String scoreString = tempInt.toString();
							JLabel score = new JLabel("Tier Score: " + scoreString);
							JButton showText = new JButton("Card Text");
							c.insets = new Insets(5, 5, 5, 5);
							c.weightx = 0.5; c.gridx = 0; c.gridy = 0; c.gridheight = 3; panel.add(label, c);
							c.gridheight = 1; c.gridx = 0; c.gridy = 6; panel.add(rarity, c);
							rarity.setHorizontalAlignment(SwingConstants.CENTER); 
							c.gridx = 0; c.gridy = 7;  panel.add(quantity, c);
							quantity.setHorizontalAlignment(SwingConstants.CENTER);
							c.gridx = 0; c.gridy = 8;  panel.add(score, c);
							score.setHorizontalAlignment(SwingConstants.CENTER); 
							c.gridx = 0; c.gridy = 4;  panel.add(showText, c);
							showText.setHorizontalAlignment(SwingConstants.CENTER); 
							showText.setMargin( new Insets(0,0,0,0) );if (smallImage)
							{
								c.weightx = 0.5; c.gridx = 0; c.gridy = 5; panel.add(fullSize, c); 
								fullSize.setHorizontalAlignment(SwingConstants.CENTER);
								fullSize.addActionListener(new ActionListener()
								{
									@Override
									public void actionPerformed(ActionEvent e) 
									{
										JFrame fullView = new JFrame();
										JPanel fullPanel = new JPanel();
										ImageLabel bigImage = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png"));
										fullPanel.add(bigImage);
										fullView.getContentPane().add(fullPanel);
										fullView.pack();
										fullView.setResizable(false);
										fullView.setVisible(true);
										
									}
									
								});
							}
							singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
							singleCardView.setBounds(100, 100, 496, 443);
							singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
							singleCardView.getContentPane().add(panel);
							singleCardView.pack();						
							singleCardView.setResizable(false);
							singleCardView.setVisible(true);
							
							showText.addActionListener(new ActionListener()
							{
								@Override
								public void actionPerformed(ActionEvent arg0) 
								{
									JFrame textFrame = new JFrame();
									textFrame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
									textFrame.setBounds(100, 100, 496, 443);
									textFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
									JPanel textPanel = new JPanel();
									JTextArea textField = new JTextArea(6, 40);
									textField.setEditable(false);
									String temp = dynamicCard.getText();
									String newTemp = WordUtils.wrap(temp, 75, "\n", false);
									textField.setText(newTemp);
									textField.setWrapStyleWord(true);
									JScrollPane vertScroll = new JScrollPane(textField);
									textPanel.add(vertScroll);
									textFrame.getContentPane().add(textPanel);
									textFrame.pack();
									textFrame.setResizable(false);
									textFrame.setVisible(true);
									
									
								}
							});
						
						
						}
					}
				};
				
				
				dynamicList.addKeyListener(keyListener);
				dynamicList.addMouseListener(mouseListener);
				c.weightx = 1.0; c.gridwidth = 2; c.weighty = 1.0; c.gridx = 0; c.gridy = 0; cardView.getContentPane().add(scrollPane, c);
				c.insets = new Insets(0, 5, 0, 5); c.gridwidth = 1; c.gridheight = 4; c.gridx = 0; c.gridy = 2; cardView.getContentPane().add(scrollPane2, c);
				//c.gridx = 1; c.gridy = 2; cardView.getContentPane().add(search, c);
				cardView.pack();
				cardView.setResizable(false);
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
				cardView.setTitle("Commons");
				cardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
				cardView.setBounds(100, 100, 600, 600);
				cardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				
				cardView.getContentPane().setLayout(new GridBagLayout());
				GridBagConstraints c = new GridBagConstraints();
				c.fill = GridBagConstraints.HORIZONTAL;

				ArrayList<Card >allCardsNoDupes = listMakerRarity(draftAllCards, "Common");
				dynamicModel1.clear();
				for (Card card : allCardsNoDupes) { dynamicModel1.addElement(card); }
				dynamicList = new JList(dynamicModel1);
				JTextArea searchBar = new JTextArea(1, 15);
				JScrollPane scrollPane = new JScrollPane(dynamicList);
				JScrollPane scrollPane2 = new JScrollPane(searchBar);
				//JButton search = new JButton("Search");
				searchBar.addKeyListener(new KeyListener() 
				{
					@Override
					public void keyPressed(KeyEvent arg0) 
					{
						
						
					}

					@Override
					public void keyReleased(KeyEvent e) 
					{
						for (Card card : allCardsNoDupes)
						{
							//if (card.getName().indexOf(e.toString()) > 0) 
							if (card.getName().toLowerCase().contains(searchBar.getText().toLowerCase()))
							{
								dynamicModel1.addElement(card);
							} 
 						}
						
						
						
					}

					@Override
					public void keyTyped(KeyEvent arg0) 
					{
						dynamicModel1.clear();
					}
				});
				
				KeyListener keyListener = new KeyListener()
				{
					public void keyPressed(KeyEvent e)
					{
						if (e.getKeyCode() == KeyEvent.VK_ENTER)
						{
							dynamicCard = (Card) dynamicList.getSelectedValue();
							singleCardView = new JFrame();										
							JPanel panel = new JPanel();
							boolean smallImage = false;
							JButton fullSize = new JButton("Full Resolution");
							panel.setLayout(new GridBagLayout());
							GridBagConstraints c = new GridBagConstraints();
							c.fill = GridBagConstraints.HORIZONTAL;
							ImageLabel label;
							if (isImage("src/images/" + dynamicCard.getName() + "Small.png")) 
							{ 
								label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + "Small.png"));
								smallImage = true;
							}
							else { label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png")); }
							JLabel rarity = new JLabel(dynamicCard.getRarity()); textColor(dynamicCard, rarity);
							textColor(dynamicCard, rarity);
							JLabel quantity = new JLabel("Quantity: " + dynamicCard.getQuantity());
							Integer tempInt = dynamicCard.getTierScore();
							String scoreString = tempInt.toString();
							JLabel score = new JLabel("Tier Score: " + scoreString);
							JButton showText = new JButton("Card Text");
							c.insets = new Insets(5, 5, 5, 5);
							c.weightx = 0.5; c.gridx = 0; c.gridy = 0; c.gridheight = 3; panel.add(label, c);
							c.gridheight = 1; c.gridx = 0; c.gridy = 6; panel.add(rarity, c);
							rarity.setHorizontalAlignment(SwingConstants.CENTER); 
							c.gridx = 0; c.gridy = 7;  panel.add(quantity, c);
							quantity.setHorizontalAlignment(SwingConstants.CENTER);
							c.gridx = 0; c.gridy = 8;  panel.add(score, c);
							score.setHorizontalAlignment(SwingConstants.CENTER); 
							c.gridx = 0; c.gridy = 4;  panel.add(showText, c);
							showText.setHorizontalAlignment(SwingConstants.CENTER); 
							showText.setMargin( new Insets(0,0,0,0) );if (smallImage)
							{
								c.weightx = 0.5; c.gridx = 0; c.gridy = 5; panel.add(fullSize, c); 
								fullSize.setHorizontalAlignment(SwingConstants.CENTER);
								fullSize.addActionListener(new ActionListener()
								{
									@Override
									public void actionPerformed(ActionEvent e) 
									{
										JFrame fullView = new JFrame();
										JPanel fullPanel = new JPanel();
										ImageLabel bigImage = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png"));
										fullPanel.add(bigImage);
										fullView.getContentPane().add(fullPanel);
										fullView.pack();
										fullView.setResizable(false);
										fullView.setVisible(true);
										
									}
									
								});
							}
							singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
							singleCardView.setBounds(100, 100, 496, 443);
							singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
							singleCardView.getContentPane().add(panel);
							singleCardView.pack();						
							singleCardView.setResizable(false);
							singleCardView.setVisible(true);
							
							showText.addActionListener(new ActionListener()
							{
								@Override
								public void actionPerformed(ActionEvent arg0) 
								{
									JFrame textFrame = new JFrame();
									textFrame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
									textFrame.setBounds(100, 100, 496, 443);
									textFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
									JPanel textPanel = new JPanel();
									JTextArea textField = new JTextArea(6, 40);
									textField.setEditable(false);
									String temp = dynamicCard.getText();
									String newTemp = WordUtils.wrap(temp, 75, "\n", false);
									textField.setText(newTemp);
									textField.setWrapStyleWord(true);
									JScrollPane vertScroll = new JScrollPane(textField);
									textPanel.add(vertScroll);
									textFrame.getContentPane().add(textPanel);
									textFrame.pack();
									textFrame.setResizable(false);
									textFrame.setVisible(true);
									
									
								}
							});
						
						
						}
					}

					@Override public void keyReleased(KeyEvent arg0) {} @Override public void keyTyped(KeyEvent arg0) {}
				};
				
				MouseListener mouseListener = new MouseAdapter() 
				{
					public void mouseClicked(MouseEvent e) 
					{
						if (e.getClickCount() == 2) 
						{    		
							dynamicCard = (Card) dynamicList.getSelectedValue();
							singleCardView = new JFrame();										
							JPanel panel = new JPanel();
							boolean smallImage = false;
							JButton fullSize = new JButton("Full Resolution");
							panel.setLayout(new GridBagLayout());
							GridBagConstraints c = new GridBagConstraints();
							c.fill = GridBagConstraints.HORIZONTAL;
							ImageLabel label;
							if (isImage("src/images/" + dynamicCard.getName() + "Small.png")) 
							{ 
								label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + "Small.png"));
								smallImage = true;
							}
							else { label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png")); }
							JLabel rarity = new JLabel(dynamicCard.getRarity()); textColor(dynamicCard, rarity);
							JLabel quantity = new JLabel("Quantity: " + dynamicCard.getQuantity());
							Integer tempInt = dynamicCard.getTierScore();
							String scoreString = tempInt.toString();
							JLabel score = new JLabel("Tier Score: " + scoreString);
							JButton showText = new JButton("Card Text");
							c.insets = new Insets(5, 5, 5, 5);
							c.weightx = 0.5; c.gridx = 0; c.gridy = 0; c.gridheight = 3; panel.add(label, c);
							c.gridheight = 1; c.gridx = 0; c.gridy = 6; panel.add(rarity, c);
							rarity.setHorizontalAlignment(SwingConstants.CENTER); 
							c.gridx = 0; c.gridy = 7;  panel.add(quantity, c);
							quantity.setHorizontalAlignment(SwingConstants.CENTER);
							c.gridx = 0; c.gridy = 8;  panel.add(score, c);
							score.setHorizontalAlignment(SwingConstants.CENTER); 
							c.gridx = 0; c.gridy = 4;  panel.add(showText, c);
							showText.setHorizontalAlignment(SwingConstants.CENTER); 
							showText.setMargin( new Insets(0,0,0,0) );if (smallImage)
							{
								c.weightx = 0.5; c.gridx = 0; c.gridy = 5; panel.add(fullSize, c); 
								fullSize.setHorizontalAlignment(SwingConstants.CENTER);
								fullSize.addActionListener(new ActionListener()
								{
									@Override
									public void actionPerformed(ActionEvent e) 
									{
										JFrame fullView = new JFrame();
										JPanel fullPanel = new JPanel();
										ImageLabel bigImage = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png"));
										fullPanel.add(bigImage);
										fullView.getContentPane().add(fullPanel);
										fullView.pack();
										fullView.setResizable(false);
										fullView.setVisible(true);
										
									}
									
								});
							}
							singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
							singleCardView.setBounds(100, 100, 496, 443);
							singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
							singleCardView.getContentPane().add(panel);
							singleCardView.pack();						
							singleCardView.setResizable(false);
							singleCardView.setVisible(true);
							
							showText.addActionListener(new ActionListener()
							{
								@Override
								public void actionPerformed(ActionEvent arg0) 
								{
									JFrame textFrame = new JFrame();
									textFrame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
									textFrame.setBounds(100, 100, 496, 443);
									textFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
									JPanel textPanel = new JPanel();
									JTextArea textField = new JTextArea(6, 40);
									textField.setEditable(false);
									String temp = dynamicCard.getText();
									String newTemp = WordUtils.wrap(temp, 75, "\n", false);
									textField.setText(newTemp);
									textField.setWrapStyleWord(true);
									JScrollPane vertScroll = new JScrollPane(textField);
									textPanel.add(vertScroll);
									textFrame.getContentPane().add(textPanel);
									textFrame.pack();
									textFrame.setResizable(false);
									textFrame.setVisible(true);
									
									
								}
							});
						
						
						}
					}
				};
				
				
				dynamicList.addKeyListener(keyListener);
				dynamicList.addMouseListener(mouseListener);
				c.weightx = 1.0; c.gridwidth = 2; c.weighty = 1.0; c.gridx = 0; c.gridy = 0; cardView.getContentPane().add(scrollPane, c);
				c.insets = new Insets(0, 5, 0, 5); c.gridwidth = 1; c.gridheight = 4; c.gridx = 0; c.gridy = 2; cardView.getContentPane().add(scrollPane2, c);
				//c.gridx = 1; c.gridy = 2; cardView.getContentPane().add(search, c);
				cardView.pack();
				cardView.setResizable(false);
				cardView.setVisible(true);
			}
		});
		mnViewDatabase.add(mntmCommon);
		mntmCommon.setEnabled(cItem);
		// End View Database 
	}

	// Sets up all the ban menu listeners
	@SuppressWarnings("rawtypes")
	public static void blackListenerInit(JMenuItem blacklist, ArrayList<Card> allCards, ArrayList<Card> blackListed, 
			JList<Card> banned, ArrayList<String> dumbThing, SortingListModel banModel, JLabel lblCardCount, JLabel lblUniqueCards, JFrame DraftInit,
			JMenuItem mntmUltraRares, JMenuItem mntmSuperRares, JMenuItem mntmRares, JMenuItem mntmCommon, JMenuItem ultimateBanner, JMenuItem ultraBanner,
			JMenuItem superBanner, JMenuItem rareBanner, JMenuItem commonBanner, JMenuItem mntmNewMenuItem, JMenuItem Monarchs, JMenuItem Fissure, JMenuItem TrapHole, 
			JMenuItem Exodia, JMenuItem Water, JMenuItem Naturia, JMenuItem Toon, JMenuItem Draw, JMenuItem Ritual, JMenuItem Fusion, JMenuItem LowAtk, JMenuItem Limited,
			JMenuItem SemiLimited, JMenuItem HighAtk, JMenuItem LowLvl, JMenuItem HighLvl, JMenuItem reset, ArrayList<Card> backupAllCards, ArrayList<Card> allCardsNopeDupe,
			JMenuItem Light, JMenuItem Dark, JMenuItem Wind, JMenuItem Fire, JMenuItem Earth, JMenuItem AncientGear, JMenuItem Archfiend, JMenuItem Crashbug, JMenuItem Destiny,
			JMenuItem Elemental, JMenuItem Flip, JMenuItem Gishki, JMenuItem God, JMenuItem Harpies, JMenuItem Hazy, JMenuItem LV, JMenuItem Lightsworn, JMenuItem Magnet,
			JMenuItem Ojama, JMenuItem SuperHeavy, JMenuItem Creator, JMenuItem Volcanic, JMenuItem Discard, JMenuItem EasySummon, JMenuItem Aqua,
			JMenuItem Beast, JMenuItem BeastWarrior, JMenuItem Dinosaur, JMenuItem Divine, JMenuItem Dragon, JMenuItem Fairy, JMenuItem Fiend, JMenuItem Fish, JMenuItem Insect,
			JMenuItem Machine, JMenuItem Plant, JMenuItem Psychic, JMenuItem Pyro, JMenuItem Reptile, JMenuItem Rock, JMenuItem SeaSerpent, JMenuItem Spellcaster,
			JMenuItem Thunder, JMenuItem Warrior, JMenuItem WingedBeast, JMenuItem Wyrm, JMenuItem Zombie, JMenuItem Spell, JMenuItem Trap, JMenuItem Contin, JMenuItem ContinSpell,
			JMenuItem ContinTrap, JMenuItem Field, JMenuItem Quickplay, JMenuItem Equip, JMenuItem Counter, JMenuItem lowScore, JMenuItem medScore, JMenuItem highScore,
			JMenuItem veryHighScore, JMenuItem OP)
	{
		KeyListener keyListener2 = new KeyListener()
		{
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					if (banned.isSelectionEmpty() == false)
					{
						dynamicList = banned;
						dynamicCard = (Card) dynamicList.getSelectedValue();
						singleCardView = new JFrame();		
						singleCardView.setTitle(dynamicCard.getName());
						JPanel panel = new JPanel();
						boolean smallImage = false;
						JButton fullSize = new JButton("Full Resolution");
						panel.setLayout(new GridBagLayout());
						GridBagConstraints c = new GridBagConstraints();
						c.fill = GridBagConstraints.HORIZONTAL;
						ImageLabel label;
						if (isImage("src/images/" + dynamicCard.getName() + "Small.png")) 
						{ 
							label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + "Small.png"));
							smallImage = true;
						}
						else { label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png")); }
						JLabel rarity = new JLabel(dynamicCard.getRarity()); textColor(dynamicCard, rarity);
						JLabel quantity = new JLabel("Quantity: " + dynamicCard.getQuantity());
						Integer tempInt = dynamicCard.getTierScore();
						String scoreString = tempInt.toString();
						JLabel score = new JLabel("Tier Score: " + scoreString);
						JButton showText = new JButton("Card Text");
						c.insets = new Insets(5, 5, 5, 5);
						c.weightx = 0.5; c.gridx = 0; c.gridy = 0; c.gridheight = 3; panel.add(label, c);
						c.gridheight = 1; c.gridx = 0; c.gridy = 6; panel.add(rarity, c);
						rarity.setHorizontalAlignment(SwingConstants.CENTER); 
						c.gridx = 0; c.gridy = 7;  panel.add(quantity, c);
						quantity.setHorizontalAlignment(SwingConstants.CENTER);
						c.gridx = 0; c.gridy = 8;  panel.add(score, c);
						score.setHorizontalAlignment(SwingConstants.CENTER); 
						c.gridx = 0; c.gridy = 4;  panel.add(showText, c);
						showText.setHorizontalAlignment(SwingConstants.CENTER); 
						showText.setMargin( new Insets(0,0,0,0) );if (smallImage)
						{
							c.weightx = 0.5; c.gridx = 0; c.gridy = 5; panel.add(fullSize, c); 
							fullSize.setHorizontalAlignment(SwingConstants.CENTER);
							fullSize.addActionListener(new ActionListener()
							{
								@Override
								public void actionPerformed(ActionEvent e) 
								{
									JFrame fullView = new JFrame();
									fullView.setTitle(dynamicCard.getName() + " - Zoomed");
									JPanel fullPanel = new JPanel();
									ImageLabel bigImage = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png"));
									fullPanel.add(bigImage);
									fullView.getContentPane().add(fullPanel);
									fullView.pack();
									fullView.setResizable(false);
									fullView.setVisible(true);
									
								}
								
							});
						}
						singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
						singleCardView.setBounds(100, 100, 496, 443);
						singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
						singleCardView.getContentPane().add(panel);
						singleCardView.pack();						
						singleCardView.setResizable(false);
						singleCardView.setVisible(true);
						
						showText.addActionListener(new ActionListener()
						{
							@Override
							public void actionPerformed(ActionEvent arg0) 
							{
								JFrame textFrame = new JFrame();
								textFrame.setTitle(dynamicCard.getName() + " - Text");
								textFrame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
								textFrame.setBounds(100, 100, 496, 443);
								textFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
								JPanel textPanel = new JPanel();
								JTextArea textField = new JTextArea(6, 40);
								textField.setEditable(false);
								String temp = dynamicCard.getText();
								String newTemp = WordUtils.wrap(temp, 75, "\n", false);
								textField.setText(newTemp);
								textField.setWrapStyleWord(true);
								JScrollPane vertScroll = new JScrollPane(textField);
								textPanel.add(vertScroll);
								textFrame.getContentPane().add(textPanel);
								textFrame.pack();
								textFrame.setResizable(false);
								textFrame.setVisible(true);
								
								
							}
						});
					
					
					}
					else {}
				}
				
			}

			@Override public void keyReleased(KeyEvent arg0) {} @Override public void keyTyped(KeyEvent arg0) {}
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
				dynamicList = list;
				Insets EMPTY_INSETS = new Insets(0, 0, 0, 0);
				JFrame banList = new JFrame();
				banList.setTitle("Ban List");
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
								dynamicCard = (Card) dynamicList.getSelectedValue();
								singleCardView = new JFrame();		
								singleCardView.setTitle(dynamicCard.getName());
								JPanel panel = new JPanel();
								boolean smallImage = false;
								JButton fullSize = new JButton("Full Resolution");
								panel.setLayout(new GridBagLayout());
								GridBagConstraints c = new GridBagConstraints();
								c.fill = GridBagConstraints.HORIZONTAL;
								ImageLabel label;
								if (isImage("src/images/" + dynamicCard.getName() + "Small.png")) 
								{ 
									label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + "Small.png"));
									smallImage = true;
								}
								else { label = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png")); }
								JLabel rarity = new JLabel(dynamicCard.getRarity()); textColor(dynamicCard, rarity);
								JLabel quantity = new JLabel("Quantity: " + dynamicCard.getQuantity());
								Integer tempInt = dynamicCard.getTierScore();
								String scoreString = tempInt.toString();
								JLabel score = new JLabel("Tier Score: " + scoreString);
								JButton showText = new JButton("Card Text");
								c.insets = new Insets(5, 5, 5, 5);
								c.weightx = 0.5; c.gridx = 0; c.gridy = 0; c.gridheight = 3; panel.add(label, c);
								c.gridheight = 1; c.gridx = 0; c.gridy = 6; panel.add(rarity, c);
								rarity.setHorizontalAlignment(SwingConstants.CENTER); 
								c.gridx = 0; c.gridy = 7;  panel.add(quantity, c);
								quantity.setHorizontalAlignment(SwingConstants.CENTER);
								c.gridx = 0; c.gridy = 8;  panel.add(score, c);
								score.setHorizontalAlignment(SwingConstants.CENTER); 
								c.gridx = 0; c.gridy = 4;  panel.add(showText, c);
								showText.setHorizontalAlignment(SwingConstants.CENTER); 
								showText.setMargin( new Insets(0,0,0,0) );if (smallImage)
								{
									c.weightx = 0.5; c.gridx = 0; c.gridy = 5; panel.add(fullSize, c); 
									fullSize.setHorizontalAlignment(SwingConstants.CENTER);
									fullSize.addActionListener(new ActionListener()
									{
										@Override
										public void actionPerformed(ActionEvent e) 
										{
											JFrame fullView = new JFrame();
											fullView.setTitle(dynamicCard.getName() + " - Zoomed");
											JPanel fullPanel = new JPanel();
											ImageLabel bigImage = new ImageLabel(new ImageIcon("src/images/" + dynamicCard.getName() + ".png"));
											fullPanel.add(bigImage);
											fullView.getContentPane().add(fullPanel);
											fullView.pack();
											fullView.setResizable(false);
											fullView.setVisible(true);
											
										}
										
									});
								}
								singleCardView.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
								singleCardView.setBounds(100, 100, 496, 443);
								singleCardView.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
								singleCardView.getContentPane().add(panel);
								singleCardView.pack();						
								singleCardView.setResizable(false);
								singleCardView.setVisible(true);
								
								showText.addActionListener(new ActionListener()
								{
									@Override
									public void actionPerformed(ActionEvent arg0) 
									{
										JFrame textFrame = new JFrame();
										textFrame.setTitle(dynamicCard.getName() + " - Text");
										textFrame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
										textFrame.setBounds(100, 100, 496, 443);
										textFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
										JPanel textPanel = new JPanel();
										JTextArea textField = new JTextArea(6, 40);
										textField.setEditable(false);
										String temp = dynamicCard.getText();
										String newTemp = WordUtils.wrap(temp, 75, "\n", false);
										textField.setText(newTemp);
										textField.setWrapStyleWord(true);
										JScrollPane vertScroll = new JScrollPane(textField);
										textPanel.add(vertScroll);
										textFrame.getContentPane().add(textPanel);
										textFrame.pack();
										textFrame.setResizable(false);
										textFrame.setVisible(true);
										
										
									}
								});
							
							
							}
						}
					}

					@Override public void keyReleased(KeyEvent arg0) {} @Override public void keyTyped(KeyEvent arg0) {}
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

				Card tempLady = new Card("7 Colored Fish");
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getName().equals(tempLady.getName())) { allCards.remove(allCards.get(a)); }
				}


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
				Card tempLady = new Card("4-Starred Ladybug of Doom");
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getName().equals(tempLady.getName())) { allCards.remove(allCards.get(a)); }
				}


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
				Draw.setEnabled(true); Limited.setEnabled(true); SemiLimited.setEnabled(true);
				LowAtk.setEnabled(true); HighAtk.setEnabled(true);
				LowLvl.setEnabled(true); HighLvl.setEnabled(true);
				Ritual.setEnabled(true); Fusion.setEnabled(true);
				Wind.setEnabled(true); Dark.setEnabled(true);
				Light.setEnabled(true); Fire.setEnabled(true); Earth.setEnabled(true);
				AncientGear.setEnabled(true); Archfiend.setEnabled(true);
				Crashbug.setEnabled(true); Destiny.setEnabled(true); Elemental.setEnabled(true);
				Flip.setEnabled(true); Gishki.setEnabled(true); God.setEnabled(true);
				Harpies.setEnabled(true); Hazy.setEnabled(true); LV.setEnabled(true);
				Lightsworn.setEnabled(true); Magnet.setEnabled(true); Ojama.setEnabled(true);
				SuperHeavy.setEnabled(true); Creator.setEnabled(true); Volcanic.setEnabled(true);
				Discard.setEnabled(true); EasySummon.setEnabled(true); Aqua.setEnabled(true);
				Beast.setEnabled(true); BeastWarrior.setEnabled(true); Dinosaur.setEnabled(true);
				Divine.setEnabled(true); Dragon.setEnabled(true); Fairy.setEnabled(true);
				Fiend.setEnabled(true); Fish.setEnabled(true); Insect.setEnabled(true);
				Machine.setEnabled(true); Plant.setEnabled(true); Psychic.setEnabled(true);
				Pyro.setEnabled(true); Reptile.setEnabled(true); Rock.setEnabled(true);
				SeaSerpent.setEnabled(true); Spellcaster.setEnabled(true); Thunder.setEnabled(true);
				Warrior.setEnabled(true); WingedBeast.setEnabled(true); Wyrm.setEnabled(true);
				Zombie.setEnabled(true);  Spell.setEnabled(true); Trap.setEnabled(true);
				ContinSpell.setEnabled(true); ContinTrap.setEnabled(true); Contin.setEnabled(true);
				Quickplay.setEnabled(true); Field.setEnabled(true); Equip.setEnabled(true);
				Counter.setEnabled(true); /*lowScore.setEnabled(true); medScore.setEnabled(true);
				highScore.setEnabled(true); veryHighScore.setEnabled(true); OP.setEnabled(true);
				 */
				int totalCards2 = cardCount(allCards); lblCardCount.setText("Cards Available: " + totalCards2);
				int totalUnique = allCardsNopeDupe.size(); lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint();
			}
		});
		// end reset listener

		banGroupListenerInit(allCards, DraftInit, banModel, lblCardCount, lblUniqueCards, Monarchs, Fissure, TrapHole,  Exodia,  
				Naturia,  Toon,  Draw, Ritual,  Fusion,  LowAtk,  Limited, SemiLimited,  HighAtk,  LowLvl,  HighLvl, AncientGear, 
				Archfiend, Crashbug, Destiny, Elemental, Flip, Gishki, God, Harpies, Hazy, LV, Lightsworn, Magnet, Ojama, SuperHeavy,
				Creator, Volcanic, Discard, EasySummon);	

		banAttributeListenerInit(allCards, banModel, lblCardCount, lblUniqueCards, DraftInit, Water, Fire, Wind, Light, Earth, Dark);

		banTypeListenerInit(allCards, banModel, lblCardCount, lblUniqueCards, DraftInit, Aqua, Beast, BeastWarrior, Dinosaur, Divine,
				Dragon, Fairy, Fiend, Fish, Insect, Machine, Plant, Psychic, Pyro, Reptile, Rock, SeaSerpent, Spellcaster, Thunder, Warrior,
				WingedBeast, Wyrm, Zombie);
		
		banCardTypeListenerInit(allCards, banModel, lblCardCount, lblUniqueCards, DraftInit, Spell, Trap, Contin, ContinSpell, ContinTrap, Field,
				Quickplay, Equip, Counter);
		
		banScoreListenerInit(allCards, banModel, lblCardCount, lblUniqueCards, DraftInit, lowScore, medScore, highScore, veryHighScore, OP);


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
	
	public static ArrayList<Card> fakeQuickInit()
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
				
				return allCards;
	}

	// Font color logic
	public static void textColor(Card card, JLabel tag)
	{
		switch (card.getRarity())
		{
		case "Ultimate Rare":
			tag.setForeground(Color.ORANGE); break;

		case "Ultra Rare":
			tag.setForeground(Color.GREEN); break;

		case "Super Rare":
			tag.setForeground(Color.RED); break;

		case "Rare":
			tag.setForeground(Color.BLUE); break;

		case "Common":
			tag.setForeground(Color.GRAY); break;

		default:
			tag.setForeground(Color.BLACK); break;
		}
	}

}
// END CLASS CardPool_GUI_1_Stable

// Implementing ImageLabel so we can display pictures on our frames
@SuppressWarnings("serial")
class ImageLabel extends JLabel { public ImageLabel(String img) { this(new ImageIcon(img)); }
public ImageLabel(ImageIcon icon) { setIcon(icon); setIconTextGap(0); setBorder(null); setText(null); setSize(icon.getImage().getWidth(null), icon.getImage().getHeight(null)); } }
