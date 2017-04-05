package version1;

import java.awt.Color;
import java.awt.Component;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.BorderFactory;
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
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import com.jgoodies.forms.factories.DefaultComponentFactory;

import org.apache.commons.lang3.text.WordUtils;

public class CardPool_GUI_1_Stable {

	private JFrame DraftInit;
	private static JFrame singleCardView = null;
	private int rerollsSoFar = 0;
	private int pickNumber = 1;
	private int playerDrafting = 0;
	private static int avgScore = 0;
	private static Card dynamicCard = new Card("init");
	private ArrayList<Card> drafted = new ArrayList<Card>();
	private ArrayList<Card> threeChoiceTemp = new ArrayList<Card>();
	private static JList<Card> dynamicList = new JList<Card>();
	private static JList<ListEntry> dynamicList2 = new JList<ListEntry>();
	private static DefaultListModel<Card> dynamicModel1 = new DefaultListModel<Card>();

	// GUI Init Stuff
	public static void main(String[] args) 
	{
		// Start GUI Stuff
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CardPool_GUI_1_Stable window = new CardPool_GUI_1_Stable();
					window.DraftInit.setVisible(true);
					
					if (window.DraftInit.isVisible() == false) { System.exit(0); }
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
		DraftInit.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		mnViewDatabase.setToolTipText("View cards that exist within the overall draft pool");
		JMenuItem mntmViewAllCards = new JMenuItem("View all cards");
		JMenuItem mntmNewMenuItem = new JMenuItem("Ultimate Rare");
		JMenuItem mntmUltraRares = new JMenuItem("Ultra Rare");
		JMenuItem mntmSuperRares = new JMenuItem("Super Rare");
		JMenuItem mntmRares = new JMenuItem("Rare");
		JMenuItem mntmCommon = new JMenuItem("Common");
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
		JMenuItem Aroma = new JMenuItem("Aroma");
		JMenuItem Crashbug = new JMenuItem("Crashbug");
		JMenuItem Destiny = new JMenuItem("Destiny Hero");
		JMenuItem Elemental = new JMenuItem("Elemental Hero");
		JMenuItem Exodia = new JMenuItem("Exodia");
		JMenuItem Fissure = new JMenuItem("Fissure");
		JMenuItem Flip = new JMenuItem("Flip");
		JMenuItem Galaxy = new JMenuItem("Galaxy");
		JMenuItem Gishki = new JMenuItem("Gishki");
		JMenuItem God = new JMenuItem("God");
		JMenuItem Harpies = new JMenuItem("Harpies");
		JMenuItem Hazy = new JMenuItem("Hazy");
		JMenuItem HeroicChallenger = new JMenuItem("Heroic Challenger");
		JMenuItem LV = new JMenuItem("LV");
		JMenuItem Lightsworn = new JMenuItem("Lightsworn");
		JMenuItem Magnet = new JMenuItem("Magnet Warriors");
		JMenuItem Monarchs = new JMenuItem("Monarchs");
		JMenuItem Naturia = new JMenuItem("Naturia");
		JMenuItem Nekroz = new JMenuItem("Nekroz");
		JMenuItem Ojama = new JMenuItem("Ojama");
		JMenuItem Predaplant = new JMenuItem("Predaplant");
		JMenuItem SuperHeavy = new JMenuItem("Superheavy Samurai");
		JMenuItem Creator = new JMenuItem("The Creator");
		JMenuItem Toon = new JMenuItem("Toon");
		JMenuItem TrapHole = new JMenuItem("Trap Hole");
		JMenuItem Vampire = new JMenuItem("Vampire");
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
		//mnBanGrouping.add(Aroma);
		mnBanGrouping.add(Crashbug); mnBanGrouping.add(Destiny);
		mnBanGrouping.add(Elemental); mnBanGrouping.add(Exodia);
		mnBanGrouping.add(Fissure); mnBanGrouping.add(Flip);
		//mnBanGrouping.add(Galaxy);
		mnBanGrouping.add(Gishki); mnBanGrouping.add(God);
		mnBanGrouping.add(Harpies); 
		//mnBanGrouping.add(Hazy);
		//mnBanGrouping.add(HeroicChallenger);
		mnBanGrouping.add(LV); mnBanGrouping.add(Lightsworn);
		mnBanGrouping.add(Magnet); mnBanGrouping.add(Monarchs); 
		mnBanGrouping.add(Naturia);
		//mnBanGrouping.add(Nekroz);
		mnBanGrouping.add(Ojama); mnBanGrouping.add(Predaplant);
		mnBanGrouping.add(SuperHeavy); 
		//mnBanGrouping.add(Creator);
		mnBanGrouping.add(Toon); 
		mnBanGrouping.add(TrapHole);
		//mnBanGrouping.add(Volcanic); 
		//mnBanGrouping.add(Vampire);
		mnBanGrouping.add(Fusion);
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

		// Grey out ban buttons that currently ban 0 cards
		Psychic.setEnabled(false);
		

		blackListenerInit(blacklist, allCards, blackListed, banned, dumbThing, banModel, lblCardCount, lblUniqueCards, DraftInit, mntmUltraRares, mntmSuperRares, mntmRares,
				mntmCommon, ultimateBanner, ultraBanner, superBanner, rareBanner, commonBanner, mntmNewMenuItem, Monarchs, Fissure, TrapHole, Exodia, Water, Naturia, Nekroz,
				Toon, Draw, Ritual, Fusion, LowAtk, Limited, SemiLimited, HighAtk, LowLvl, HighLvl, reset, backupAllCards, allCardsNopeDupe, Light, Dark, Wind, Fire, Earth,
				AncientGear, Archfiend, Aroma, Crashbug, Destiny, Elemental, Flip, Galaxy, Gishki, God, Harpies, Hazy, HeroicChallenger, LV, Lightsworn, Magnet, Ojama, Predaplant, SuperHeavy, Creator, Vampire, Volcanic,
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
				JButton reroll = new JButton("Reroll " + "(" + rerollsLocal + ")"); if (rerollsLocal == 0) { reroll.setEnabled(false); }
				reroll.setToolTipText("<html>Discard these three cards from your pool.<br>(This does not count as a pick.)");
				viewDeck.setToolTipText("<html>View your current deck.");

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
				
				cardPick1.setBorder(BorderFactory.createEmptyBorder()); cardPick1.setContentAreaFilled(false); cardPick1.setMargin(new Insets(0, 0, 0, 0));
				cardPick2.setBorder(BorderFactory.createEmptyBorder()); cardPick2.setContentAreaFilled(false); cardPick2.setMargin(new Insets(0, 0, 0, 0));
				cardPick3.setBorder(BorderFactory.createEmptyBorder()); cardPick3.setContentAreaFilled(false); cardPick3.setMargin(new Insets(0, 0, 0, 0));
				//reroll.setBorder(BorderFactory.createEmptyBorder()); reroll.setContentAreaFilled(false); reroll.setMargin(new Insets(0, 0, 0, 0));
				//viewDeck.setBorder(BorderFactory.createEmptyBorder()); viewDeck.setContentAreaFilled(false); viewDeck.setMargin(new Insets(0, 0, 0, 0));
				
				
				
				// Setup the labels on the draft window
				JLabel pickCounter = new JLabel("Pick " + pickNumber + "/" + cardsToDraftLocal);
				JLabel poolCardCount = new JLabel("Pool: " + draftPools.get(playerDrafting).size() + "/" + defPoolSize);
				//JProgressBar poolSize = new JProgressBar(0, defPoolSize);
				JLabel playerDraftLbl = new JLabel("Player " + (playerDrafting + 1));
				JLabel pick1Rarity = new JLabel(threeChoiceTemp.get(0).getRarity()); textColor(threeChoiceTemp.get(0), pick1Rarity);
				JLabel pick2Rarity = new JLabel(threeChoiceTemp.get(1).getRarity()); textColor(threeChoiceTemp.get(1), pick2Rarity);
				JLabel pick3Rarity = new JLabel(threeChoiceTemp.get(2).getRarity()); textColor(threeChoiceTemp.get(2), pick3Rarity);
				JLabel pick1Score = new JLabel(String.valueOf(threeChoiceTemp.get(0).getTierScore())); 
				JLabel pick2Score = new JLabel(String.valueOf(threeChoiceTemp.get(1).getTierScore())); 
				JLabel pick3Score = new JLabel(String.valueOf(threeChoiceTemp.get(2).getTierScore())); 
				JProgressBar avgDeckScore = new JProgressBar(0, 100);
				avgDeckScore.setToolTipText("Average Card Score");
				avgDeckScore.setStringPainted(true);
				avgDeckScore.setString(String.valueOf(avgScore));
				/*poolSize.setToolTipText("Pool Size");
				poolSize.setStringPainted(true);
				poolSize.setString(String.valueOf(draftPools.get(playerDrafting).size()));*/

				// Adding everything to the draft window
				c.insets = new Insets(0,10,10,10); 
				c.weightx = 0.5;  c.gridx = 1; c.gridy = 0; playerDraftLbl.setHorizontalAlignment(SwingConstants.CENTER); DraftInit.getContentPane().add(playerDraftLbl, c);
				c.insets = new Insets(30,75,10,75); 
				c.gridx = 0; c.gridy = 1; DraftInit.getContentPane().add(cardPick1, c); 
				c.gridx = 1; c.gridy = 1; DraftInit.getContentPane().add(cardPick2, c); 
				c.gridx = 2; c.gridy = 1; DraftInit.getContentPane().add(cardPick3, c);
				c.insets = new Insets(0,75,10,75); 
				c.gridx = 0; c.gridy = 4; DraftInit.getContentPane().add(viewDeck, c);
				c.gridx = 1; c.gridy = 4; pickCounter.setHorizontalAlignment(SwingConstants.CENTER); DraftInit.getContentPane().add(pickCounter, c);
				c.gridx = 2; c.gridy = 4; DraftInit.getContentPane().add(reroll, c);
				c.insets = new Insets(0,10,10,10); 
				c.gridx = 0; c.gridy = 2; pick1Rarity.setHorizontalAlignment(SwingConstants.CENTER); DraftInit.getContentPane().add(pick1Rarity, c);
				c.gridx = 1; c.gridy = 2; pick2Rarity.setHorizontalAlignment(SwingConstants.CENTER); DraftInit.getContentPane().add(pick2Rarity, c); 
				c.gridx = 2; c.gridy = 2; pick3Rarity.setHorizontalAlignment(SwingConstants.CENTER); DraftInit.getContentPane().add(pick3Rarity, c);
				c.gridx = 0; c.gridy = 3; pick1Score.setHorizontalAlignment(SwingConstants.CENTER); DraftInit.getContentPane().add(pick1Score, c);
				c.gridx = 1; c.gridy = 3; pick2Score.setHorizontalAlignment(SwingConstants.CENTER); DraftInit.getContentPane().add(pick2Score, c);
				c.gridx = 2; c.gridy = 3; pick3Score.setHorizontalAlignment(SwingConstants.CENTER); DraftInit.getContentPane().add(pick3Score, c);
				c.gridx = 1; c.gridy = 5; poolCardCount.setHorizontalAlignment(SwingConstants.CENTER); DraftInit.getContentPane().add(poolCardCount, c);
				//c.gridx = 1; c.gridy = 5; poolCardCount.setHorizontalAlignment(SwingConstants.CENTER); DraftInit.getContentPane().add(poolSize, c);
				c.insets = new Insets(0,500,10,500); 
				c.gridx = 0; c.gridy = 6; c.gridwidth = 3; DraftInit.getContentPane().add(avgDeckScore, c);



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
							
							pick1Score.setText(String.valueOf(threeChoiceTemp.get(0).getTierScore()));
							pick2Score.setText(String.valueOf(threeChoiceTemp.get(1).getTierScore()));
							pick3Score.setText(String.valueOf(threeChoiceTemp.get(2).getTierScore()));
							
							avgScore = avgScoreCards(drafted, (pickNumber));
							avgDeckScore.setString(String.valueOf(avgScore));
							avgDeckScore.setValue(avgScore);

							pickNumber++;
							pickCounter.setText("Pick " + pickNumber + "/" + cardsToDraftLocal);

							poolCardCount.setText("Pool: " + draftPools.get(playerDrafting).size() + "/" + defPoolSize);

							if (pickNumber > cardsToDraftLocal)
							{
								ArrayList<Card> temp = new ArrayList<Card>();
								copyPoolVoid(drafted, temp);
								draftDecks.get(playerDrafting).clear();
								draftDecks.get(playerDrafting).addAll(drafted);
								cardCounter(draftDecks.get(playerDrafting));
								draftDecks.get(playerDrafting).sort(drafted.get(0));
								if (playerDrafting + 1 < playerCountLocal)
								{
									JFrame tempConfirmBox = new JFrame();
									Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
									tempConfirmBox.setLocation(dim.width/2-tempConfirmBox.getSize().width/2, dim.height/2-tempConfirmBox.getSize().height/2);
									tempConfirmBox.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
									tempConfirmBox.setBounds(100, 100, 496, 443);
									tempConfirmBox.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
									tempConfirmBox.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
									JTextArea sendTo = new JTextArea(1, 15);
									JButton send = new JButton("Send");
									sendTo.setToolTipText("Send to this address");
									SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); Date date = new Date();
									send.addActionListener(new ActionListener()
									{
										@Override
										public void actionPerformed(ActionEvent e) 
										{
											// EMAIL
											String to = sendTo.getText(); 
											String from = "cardpoolsender@gmail.com"; 
											String host = "localhost";//or IP address  

											//Get the session object  
											Properties properties = System.getProperties();  
											properties.put("mail.smtp.starttls.enable", "true"); 
											properties.put("mail.smtp.host", "smtp.gmail.com");
											properties.put("mail.smtp.user", from); // User name
											properties.put("mail.smtp.password", "plaintxtpassword"); // password
											properties.put("mail.smtp.port", "587");
											properties.put("mail.smtp.auth", "true");
											//Session session = Session.getDefaultInstance(properties);  
											Session session = Session.getDefaultInstance(properties, 
													new javax.mail.Authenticator(){
												protected PasswordAuthentication getPasswordAuthentication() {
													return new PasswordAuthentication(
															from, "plaintxtpassword");// Specify the Username and the PassWord
												}
											});

											//compose the message  
											try{  
												MimeMessage message = new MimeMessage(session);  
												message.setFrom(new InternetAddress(from));  
												message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));  
												message.setSubject("YGO Deck " + date);  
												message.setText(mailMessage(temp));  

												// Send message  
												Transport.send(message);  
												System.out.println("Message sent to " + to);  

											}catch (MessagingException mex) {mex.printStackTrace();}  

										}// END EMAIL
									});
								  
									JButton confirm = new JButton("Next Player");
									confirm.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent arg0) { tempConfirmBox.setVisible(false); DraftInit.setVisible(true); } });
									tempConfirmBox.add(sendTo);
									tempConfirmBox.add(send);
									tempConfirmBox.add(confirm);
									tempConfirmBox.setResizable(false);
									tempConfirmBox.pack();
									tempConfirmBox.setVisible(true);
									DraftInit.setVisible(false);
								}
								
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
									
									pick1Score.setText(String.valueOf(threeChoiceTemp.get(0).getTierScore()));
									pick2Score.setText(String.valueOf(threeChoiceTemp.get(1).getTierScore()));
									pick3Score.setText(String.valueOf(threeChoiceTemp.get(2).getTierScore()));
									
									avgScore = avgScoreCards(drafted, (pickNumber));
									avgDeckScore.setString(String.valueOf(avgScore));
									avgDeckScore.setValue(avgScore);
									
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
									JTextArea sendTo = new JTextArea(1, 15);
									JButton send = new JButton("Send");
									sendTo.setToolTipText("Send to this address");
									SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); Date date = new Date();
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

											stats.setEditable(false);
											cardViewLocal.getContentPane().add(stats);
											cardViewLocal.pack();
											cardViewLocal.setVisible(true);

										}
									});

									send.addActionListener(new ActionListener()
									{

										@Override
										public void actionPerformed(ActionEvent e) 
										{
											sendTo.setEditable(false);
											// EMAIL
											String to = sendTo.getText(); 
											String from = "cardpoolsender@gmail.com"; 
											String host = "localhost";//or IP address  

											//Get the session object  
											Properties properties = System.getProperties();  
											properties.put("mail.smtp.starttls.enable", "true"); 
											properties.put("mail.smtp.host", "smtp.gmail.com");
											properties.put("mail.smtp.user", from); // User name
											properties.put("mail.smtp.password", "plaintxtpassword"); // password
											properties.put("mail.smtp.port", "587");
											properties.put("mail.smtp.auth", "true");
											//Session session = Session.getDefaultInstance(properties);  
											Session session = Session.getDefaultInstance(properties, 
													new javax.mail.Authenticator(){
												protected PasswordAuthentication getPasswordAuthentication() {
													return new PasswordAuthentication(
															from, "plaintxtpassword");// Specify the Username and the PassWord
												}
											});

											//compose the message  
											try{  
												MimeMessage message = new MimeMessage(session);  
												message.setFrom(new InternetAddress(from));  
												message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));  
												message.setSubject("YGO Deck " + date);  
												message.setText(mailMessage(draftDecks.get(playerDeck.getSelectedIndex())));  

												// Send message  
												Transport.send(message);  
												System.out.println("Message sent to " + to);  

											}catch (MessagingException mex) {mex.printStackTrace();}  

										}// END EMAIL
									});
								
									
									
									playerDeck.setEditable(false);
									finalFrame.getContentPane().add(viewDeckOf);
									finalFrame.getContentPane().add(viewStats);
									finalFrame.getContentPane().add(send);
									finalFrame.getContentPane().add(sendTo);
									finalFrame.setTitle("Draft Complete");
									finalFrame.setResizable(false);

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
							
							pick1Score.setText(String.valueOf(threeChoiceTemp.get(0).getTierScore()));
							pick2Score.setText(String.valueOf(threeChoiceTemp.get(1).getTierScore()));
							pick3Score.setText(String.valueOf(threeChoiceTemp.get(2).getTierScore()));
							
							avgScore = avgScoreCards(drafted, (pickNumber));
							avgDeckScore.setString(String.valueOf(avgScore));
							avgDeckScore.setValue(avgScore);
							

							pickNumber++;
							pickCounter.setText("Pick " + pickNumber + "/" + cardsToDraftLocal);

							poolCardCount.setText("Pool: " + draftPools.get(playerDrafting).size() + "/" + defPoolSize);

							if (pickNumber > cardsToDraftLocal)
							{
								ArrayList<Card> temp = new ArrayList<Card>();
								copyPoolVoid(drafted, temp);
								draftDecks.get(playerDrafting).clear();
								draftDecks.get(playerDrafting).addAll(drafted);
								cardCounter(draftDecks.get(playerDrafting));
								draftDecks.get(playerDrafting).sort(drafted.get(0));
								if (playerDrafting + 1 < playerCountLocal)
								{
									JFrame tempConfirmBox = new JFrame();
									
									Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
									tempConfirmBox.setLocation(dim.width/2-tempConfirmBox.getSize().width/2, dim.height/2-tempConfirmBox.getSize().height/2);
									tempConfirmBox.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
									tempConfirmBox.setBounds(100, 100, 496, 443);
									tempConfirmBox.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
									tempConfirmBox.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
									JTextArea sendTo = new JTextArea(1, 15);
									JButton send = new JButton("Send");
									sendTo.setToolTipText("Send to this address");
									SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); Date date = new Date();
									send.addActionListener(new ActionListener()
									{
										@Override
										public void actionPerformed(ActionEvent e) 
										{
											// EMAIL
											String to = sendTo.getText(); 
											String from = "cardpoolsender@gmail.com"; 
											String host = "localhost";//or IP address  

											//Get the session object  
											Properties properties = System.getProperties();  
											properties.put("mail.smtp.starttls.enable", "true"); 
											properties.put("mail.smtp.host", "smtp.gmail.com");
											properties.put("mail.smtp.user", from); // User name
											properties.put("mail.smtp.password", "plaintxtpassword"); // password
											properties.put("mail.smtp.port", "587");
											properties.put("mail.smtp.auth", "true");
											//Session session = Session.getDefaultInstance(properties);  
											Session session = Session.getDefaultInstance(properties, 
													new javax.mail.Authenticator(){
												protected PasswordAuthentication getPasswordAuthentication() {
													return new PasswordAuthentication(
															from, "plaintxtpassword");// Specify the Username and the PassWord
												}
											});

											//compose the message  
											try{  
												MimeMessage message = new MimeMessage(session);  
												message.setFrom(new InternetAddress(from));  
												message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));  
												message.setSubject("YGO Deck " + date);  
												message.setText(mailMessage(temp));  

												// Send message  
												Transport.send(message);  
												System.out.println("Message sent to " + to);  

											}catch (MessagingException mex) {mex.printStackTrace();}  

										}// END EMAIL
									});
								  
									JButton confirm = new JButton("Next Player");
									confirm.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent arg0) { tempConfirmBox.setVisible(false); DraftInit.setVisible(true); } });
									tempConfirmBox.add(sendTo);
									tempConfirmBox.add(send);
									tempConfirmBox.add(confirm);
									tempConfirmBox.setResizable(false);
									tempConfirmBox.pack();
									tempConfirmBox.setVisible(true);
									DraftInit.setVisible(false);
									
								}
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
									
									pick1Score.setText(String.valueOf(threeChoiceTemp.get(0).getTierScore()));
									pick2Score.setText(String.valueOf(threeChoiceTemp.get(1).getTierScore()));
									pick3Score.setText(String.valueOf(threeChoiceTemp.get(2).getTierScore()));
									
									avgScore = avgScoreCards(drafted, (pickNumber));
									avgDeckScore.setString(String.valueOf(avgScore));
									avgDeckScore.setValue(avgScore);
									
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
									JTextArea sendTo = new JTextArea(1, 15);
									JButton send = new JButton("Send");
									sendTo.setToolTipText("Send to this address");
									SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); Date date = new Date();
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

											stats.setEditable(false);
											cardViewLocal.getContentPane().add(stats);
											cardViewLocal.pack();
											cardViewLocal.setVisible(true);

										}
									});

									send.addActionListener(new ActionListener()
									{

										@Override
										public void actionPerformed(ActionEvent e) 
										{
											sendTo.setEditable(false);
											// EMAIL
											String to = sendTo.getText(); 
											String from = "cardpoolsender@gmail.com"; 
											String host = "localhost";//or IP address  

											//Get the session object  
											Properties properties = System.getProperties();  
											properties.put("mail.smtp.starttls.enable", "true"); 
											properties.put("mail.smtp.host", "smtp.gmail.com");
											properties.put("mail.smtp.user", from); // User name
											properties.put("mail.smtp.password", "plaintxtpassword"); // password
											properties.put("mail.smtp.port", "587");
											properties.put("mail.smtp.auth", "true");
											//Session session = Session.getDefaultInstance(properties);  
											Session session = Session.getDefaultInstance(properties, 
													new javax.mail.Authenticator(){
												protected PasswordAuthentication getPasswordAuthentication() {
													return new PasswordAuthentication(
															from, "plaintxtpassword");// Specify the Username and the PassWord
												}
											});

											//compose the message  
											try{  
												MimeMessage message = new MimeMessage(session);  
												message.setFrom(new InternetAddress(from));  
												message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));  
												message.setSubject("YGO Deck " + date);  
												message.setText(mailMessage(draftDecks.get(playerDeck.getSelectedIndex())));  

												// Send message  
												Transport.send(message);  
												System.out.println("Message sent to " + to);  

											}catch (MessagingException mex) {mex.printStackTrace();}  

										}// END EMAIL
									});
								
									
									
									playerDeck.setEditable(false);
									finalFrame.getContentPane().add(viewDeckOf);
									finalFrame.getContentPane().add(viewStats);
									finalFrame.getContentPane().add(send);
									finalFrame.getContentPane().add(sendTo);
									finalFrame.setTitle("Draft Complete");
									finalFrame.setResizable(false);

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
							
							pick1Score.setText(String.valueOf(threeChoiceTemp.get(0).getTierScore()));
							pick2Score.setText(String.valueOf(threeChoiceTemp.get(1).getTierScore()));
							pick3Score.setText(String.valueOf(threeChoiceTemp.get(2).getTierScore()));
							
							avgScore = avgScoreCards(drafted, (pickNumber));
							avgDeckScore.setString(String.valueOf(avgScore));
							avgDeckScore.setValue(avgScore);
							

							pickNumber++;
							pickCounter.setText("Pick " + pickNumber + "/" + cardsToDraftLocal);

							poolCardCount.setText("Pool: " + draftPools.get(playerDrafting).size() + "/" + defPoolSize);

							if (pickNumber > cardsToDraftLocal)
							{
								ArrayList<Card> temp = new ArrayList<Card>();
								copyPoolVoid(drafted, temp);
								draftDecks.get(playerDrafting).clear();
								draftDecks.get(playerDrafting).addAll(drafted);
								cardCounter(draftDecks.get(playerDrafting));
								draftDecks.get(playerDrafting).sort(drafted.get(0));
								if (playerDrafting + 1 < playerCountLocal)
								{
									JFrame tempConfirmBox = new JFrame();
									
									Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
									tempConfirmBox.setLocation(dim.width/2-tempConfirmBox.getSize().width/2, dim.height/2-tempConfirmBox.getSize().height/2);
									tempConfirmBox.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
									tempConfirmBox.setBounds(100, 100, 496, 443);
									tempConfirmBox.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
									tempConfirmBox.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
									JTextArea sendTo = new JTextArea(1, 15);
									JButton send = new JButton("Send");
									sendTo.setToolTipText("Send to this address");
									SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); Date date = new Date();
									send.addActionListener(new ActionListener()
									{
										@Override
										public void actionPerformed(ActionEvent e) 
										{
											// EMAIL
											String to = sendTo.getText(); 
											String from = "cardpoolsender@gmail.com"; 
											String host = "localhost";//or IP address  

											//Get the session object  
											Properties properties = System.getProperties();  
											properties.put("mail.smtp.starttls.enable", "true"); 
											properties.put("mail.smtp.host", "smtp.gmail.com");
											properties.put("mail.smtp.user", from); // User name
											properties.put("mail.smtp.password", "plaintxtpassword"); // password
											properties.put("mail.smtp.port", "587");
											properties.put("mail.smtp.auth", "true");
											//Session session = Session.getDefaultInstance(properties);  
											Session session = Session.getDefaultInstance(properties, 
													new javax.mail.Authenticator(){
												protected PasswordAuthentication getPasswordAuthentication() {
													return new PasswordAuthentication(
															from, "plaintxtpassword");// Specify the Username and the PassWord
												}
											});

											//compose the message  
											try{  
												MimeMessage message = new MimeMessage(session);  
												message.setFrom(new InternetAddress(from));  
												message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));  
												message.setSubject("YGO Deck " + date);  
												message.setText(mailMessage(temp));  

												// Send message  
												Transport.send(message);  
												System.out.println("Message sent to " + to);  

											}catch (MessagingException mex) {mex.printStackTrace();}  

										}// END EMAIL
									});
								  
									JButton confirm = new JButton("Next Player");
									confirm.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent arg0) { tempConfirmBox.setVisible(false); DraftInit.setVisible(true); } });
									tempConfirmBox.add(sendTo);
									tempConfirmBox.add(send);
									tempConfirmBox.add(confirm);
									tempConfirmBox.setResizable(false);
									tempConfirmBox.pack();
									tempConfirmBox.setVisible(true);
									DraftInit.setVisible(false);
									
								}
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
									
									pick1Score.setText(String.valueOf(threeChoiceTemp.get(0).getTierScore()));
									pick2Score.setText(String.valueOf(threeChoiceTemp.get(1).getTierScore()));
									pick3Score.setText(String.valueOf(threeChoiceTemp.get(2).getTierScore()));
									
									avgScore = avgScoreCards(drafted, (pickNumber));
									avgDeckScore.setString(String.valueOf(avgScore));
									avgDeckScore.setValue(avgScore);
									
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
									JTextArea sendTo = new JTextArea(1, 15);
									JButton send = new JButton("Send");
									sendTo.setToolTipText("Send to this address");
									SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); Date date = new Date();
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

											stats.setEditable(false);
											cardViewLocal.getContentPane().add(stats);
											cardViewLocal.pack();
											cardViewLocal.setVisible(true);

										}
									});

									send.addActionListener(new ActionListener()
									{

										@Override
										public void actionPerformed(ActionEvent e) 
										{
											sendTo.setEditable(false);
											// EMAIL
											String to = sendTo.getText(); 
											String from = "cardpoolsender@gmail.com"; 
											String host = "localhost";//or IP address  

											//Get the session object  
											Properties properties = System.getProperties();  
											properties.put("mail.smtp.starttls.enable", "true"); 
											properties.put("mail.smtp.host", "smtp.gmail.com");
											properties.put("mail.smtp.user", from); // User name
											properties.put("mail.smtp.password", "plaintxtpassword"); // password
											properties.put("mail.smtp.port", "587");
											properties.put("mail.smtp.auth", "true");
											//Session session = Session.getDefaultInstance(properties);  
											Session session = Session.getDefaultInstance(properties, 
													new javax.mail.Authenticator(){
												protected PasswordAuthentication getPasswordAuthentication() {
													return new PasswordAuthentication(
															from, "plaintxtpassword");// Specify the Username and the PassWord
												}
											});

											//compose the message  
											try{  
												MimeMessage message = new MimeMessage(session);  
												message.setFrom(new InternetAddress(from));  
												message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));  
												message.setSubject("YGO Deck " + date);  
												message.setText(mailMessage(draftDecks.get(playerDeck.getSelectedIndex())));  

												// Send message  
												Transport.send(message);  
												System.out.println("Message sent to " + to);  

											}catch (MessagingException mex) {mex.printStackTrace();}  

										}// END EMAIL
									});
								
									
									
									playerDeck.setEditable(false);
									finalFrame.getContentPane().add(viewDeckOf);
									finalFrame.getContentPane().add(viewStats);
									finalFrame.getContentPane().add(send);
									finalFrame.getContentPane().add(sendTo);
									finalFrame.setTitle("Draft Complete");
									finalFrame.setResizable(false);

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
							
							pick1Score.setText(String.valueOf(threeChoiceTemp.get(0).getTierScore()));
							pick2Score.setText(String.valueOf(threeChoiceTemp.get(1).getTierScore()));
							pick3Score.setText(String.valueOf(threeChoiceTemp.get(2).getTierScore()));
							
							avgScore = avgScoreCards(drafted, (pickNumber));
							avgDeckScore.setString(String.valueOf(avgScore));
							avgDeckScore.setValue(avgScore);
							

							rerollsSoFar++;
							if (canReroll(rerollsSoFar, rerollsLocal) == false) { reroll.setEnabled(false); }
							reroll.setText("Reroll " + "(" + (rerollsLocal - rerollsSoFar) + ")");

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
						cardViewLocal.setResizable(false);
						cardViewLocal.setAlwaysOnTop(true);
						DefaultListModel viewDeckModel = new DefaultListModel();
						if (drafted.size() > 0) { listEntryMaker(drafted, viewDeckModel); }
						dynamicList2 = new JList(viewDeckModel);
						dynamicList2.setCellRenderer(new ListEntryCellRenderer());
						
						// Uhhhh yeah this should be a switch
						ArrayList<Card> AquaCards = new ArrayList<Card>();
						for (Card card : drafted) { if (card.getType().equals("Aqua")) { AquaCards.add(card); } }
						
						ArrayList<Card> BeastCards = new ArrayList<Card>();
						for (Card card : drafted) { if (card.getType().equals("Beast")) { BeastCards.add(card); } }
						
						ArrayList<Card> BeastWarriorCards = new ArrayList<Card>();
						for (Card card : drafted) { if (card.getType().equals("Beast-Warrior")) { BeastWarriorCards.add(card); } }
						
						ArrayList<Card> DinosaurCards = new ArrayList<Card>();
						for (Card card : drafted) { if (card.getType().equals("Dinosaur")) { DinosaurCards.add(card); } }
						
						ArrayList<Card> DivineCards = new ArrayList<Card>();
						for (Card card : drafted) { if (card.getType().equals("Divine-Beast")) { DivineCards.add(card); } }
						
						ArrayList<Card> DragonCards = new ArrayList<Card>();
						for (Card card : drafted) { if (card.getType().equals("Dragon")) { DragonCards.add(card); } }
						
						ArrayList<Card> FairyCards = new ArrayList<Card>();
						for (Card card : drafted) { if (card.getType().equals("Fairy")) { FairyCards.add(card); } }
						
						ArrayList<Card> FiendCards = new ArrayList<Card>();
						for (Card card : drafted) { if (card.getType().equals("Fiend")) { FiendCards.add(card); } }
						
						ArrayList<Card> FishCards = new ArrayList<Card>();
						for (Card card : drafted) { if (card.getType().equals("Fish")) { FishCards.add(card); } }
						
						ArrayList<Card> InsectCards = new ArrayList<Card>();
						for (Card card : drafted) { if (card.getType().equals("Insect")) { InsectCards.add(card); } }
						
						ArrayList<Card> MachineCards = new ArrayList<Card>();
						for (Card card : drafted) { if (card.getType().equals("Machine")) { MachineCards.add(card); } }
					
						ArrayList<Card> PlantCards = new ArrayList<Card>();
						for (Card card : drafted) { if (card.getType().equals("Plant")) { PlantCards.add(card); } }
						
						ArrayList<Card> PsychicCards = new ArrayList<Card>();
						for (Card card : drafted) { if (card.getType().equals("Psychic")) { PsychicCards.add(card); } }
						
						ArrayList<Card> PyroCards = new ArrayList<Card>();
						for (Card card : drafted) { if (card.getType().equals("Pyro")) { PyroCards.add(card); } }
						
						ArrayList<Card> ReptileCards = new ArrayList<Card>();
						for (Card card : drafted) { if (card.getType().equals("Reptile")) { ReptileCards.add(card); } }
						
						ArrayList<Card> RockCards = new ArrayList<Card>();
						for (Card card : drafted) { if (card.getType().equals("Rock")) { RockCards.add(card); } }
						
						ArrayList<Card> SeaSerpentCards = new ArrayList<Card>();
						for (Card card : drafted) { if (card.getType().equals("Sea Serpent")) { SeaSerpentCards.add(card); } }
						
						ArrayList<Card> SpellcasterCards = new ArrayList<Card>();
						for (Card card : drafted) { if (card.getType().equals("Spellcaster")) { SpellcasterCards.add(card); } }
						
						ArrayList<Card> ThunderCards = new ArrayList<Card>();
						for (Card card : drafted) { if (card.getType().equals("Thunder")) { ThunderCards.add(card); } }
						
						ArrayList<Card> WarriorCards = new ArrayList<Card>();
						for (Card card : drafted) { if (card.getType().equals("Warrior")) { WarriorCards.add(card); } }
						
						ArrayList<Card> ZombieCards = new ArrayList<Card>();
						for (Card card : drafted) { if (card.getType().equals("Zombie")) { ZombieCards.add(card); } }
						
						ArrayList<Card> WingedBeastCards = new ArrayList<Card>();
						for (Card card : drafted) { if (card.getType().equals("Winged Beast")) { WingedBeastCards.add(card); } }
						
						ArrayList<Card> DarkCards = new ArrayList<Card>();
						for (Card card : drafted) { if (card.getAttribute().equals("Dark")) { DarkCards.add(card); } }
						
						ArrayList<Card> FireCards = new ArrayList<Card>();
						for (Card card : drafted) { if (card.getAttribute().equals("Fire")) { FireCards.add(card); } }
						
						ArrayList<Card> EarthCards = new ArrayList<Card>();
						for (Card card : drafted) { if (card.getAttribute().equals("Earth")) { EarthCards.add(card); } }

						ArrayList<Card> LightCards = new ArrayList<Card>();
						for (Card card : drafted) { if (card.getAttribute().equals("Light")) { LightCards.add(card); } }
						
						ArrayList<Card> WaterCards = new ArrayList<Card>();
						for (Card card : drafted) { if (card.getAttribute().equals("Water")) { WaterCards.add(card); } }
						
						ArrayList<Card> WindCards = new ArrayList<Card>();
						for (Card card : drafted) { if (card.getAttribute().equals("Wind")) { WindCards.add(card); } }
						
						ArrayList<Card> SpellCards = new ArrayList<Card>();
						for (Card card : drafted) { if (card.getCardType().equals("Spell")) { SpellCards.add(card); } }
						
						ArrayList<Card> TrapCards = new ArrayList<Card>();
						for (Card card : drafted) { if (card.getCardType().equals("Trap")) { TrapCards.add(card); } }
						
						ArrayList<Card> lowLvlCards = new ArrayList<Card>();
						for (Card card : drafted) { if (card.getLvl() < 5 && card.getLvl() != 0) { lowLvlCards.add(card); } }
						
						ArrayList<Card> highLvlCards = new ArrayList<Card>();
						for (Card card : drafted) { if (card.getLvl() > 6) { highLvlCards.add(card); } }
						
						ArrayList<Card> medLvlCards = new ArrayList<Card>();
						for (Card card : drafted) { if (card.getLvl() > 4 && card.getLvl() < 7) { medLvlCards.add(card); } }
						
						
						
						int aqua = typeCount(drafted, "Aqua"); 
						int beast = typeCount(drafted, "Beast");
						int beastWarrior = typeCount(drafted, "Beast-Warrior"); 
						int dinosaur = typeCount(drafted, "Dinosaur");
						int divine = typeCount(drafted, "Divine-Beast"); 
						int dragon = typeCount(drafted, "Dragon");
						int fairy = typeCount(drafted, "Fairy"); 
						int fiend = typeCount(drafted, "Fiend");
						int fish = typeCount(drafted, "Fish");
						int insect = typeCount(drafted, "Insect");
						int machine = typeCount(drafted, "Machine"); 
						int plant = typeCount(drafted, "Plant");
						int psychic = typeCount(drafted, "Psychic"); 
						int pyro = typeCount(drafted, "Pyro");
						int reptile = typeCount(drafted, "Reptile"); 
						int rock = typeCount(drafted, "Rock");
						int seaSerpent = typeCount(drafted, "Sea Serpent"); 
						int thunder = typeCount(drafted, "Thunder");
						int warrior = typeCount(drafted, "Warrior"); 
						int zombie = typeCount(drafted, "Zombie"); 
						int wingedBeast = typeCount(drafted, "Winged Beast");
						int dark = attributeCount(drafted, "Dark"); 
						int earth = attributeCount(drafted, "Earth");
						int fire = attributeCount(drafted, "Fire");
						int light = attributeCount(drafted, "Light");
						int water = attributeCount(drafted, "Water");
						int wind = attributeCount(drafted, "Wind");
						int spells = spellCount(drafted);
						int traps = trapCount(drafted); 
						int spellcaster = typeCount(drafted, "Spellcaster");
						int lowLvlCount = lvlLessCount(drafted, 5); 
						int highLvlCount = lvlMoreCount(drafted, 6);
						int medLvlCount = lvlBetweenCount(drafted, 4, 7);
						int totalScore = scoreCards(drafted);
						int avgScore = 0;
						if ((pickNumber - 1) == 0) {}
						else { avgScore = avgScoreCards(drafted, (pickNumber - 1)); }
						
						JButton spellBtn = new JButton(); spellBtn.setBorder(BorderFactory.createEmptyBorder()); spellBtn.setContentAreaFilled(false);
						JButton trapBtn = new JButton(); trapBtn.setBorder(BorderFactory.createEmptyBorder()); trapBtn.setContentAreaFilled(false);
						JButton aquaBtn = new JButton(); aquaBtn.setBorder(BorderFactory.createEmptyBorder()); aquaBtn.setContentAreaFilled(false);
						JButton beastBtn = new JButton(); beastBtn.setBorder(BorderFactory.createEmptyBorder()); beastBtn.setContentAreaFilled(false);
						JButton beastWarriorBtn = new JButton(); beastWarriorBtn.setBorder(BorderFactory.createEmptyBorder()); beastWarriorBtn.setContentAreaFilled(false);
						JButton dinosaurBtn = new JButton(); dinosaurBtn.setBorder(BorderFactory.createEmptyBorder()); dinosaurBtn.setContentAreaFilled(false);
						JButton divineBtn = new JButton(); divineBtn.setBorder(BorderFactory.createEmptyBorder()); divineBtn.setContentAreaFilled(false);
						JButton dragonBtn = new JButton(); dragonBtn.setBorder(BorderFactory.createEmptyBorder()); dragonBtn.setContentAreaFilled(false);
						JButton fairyBtn = new JButton(); fairyBtn.setBorder(BorderFactory.createEmptyBorder()); fairyBtn.setContentAreaFilled(false);
						JButton fiendBtn = new JButton(); fiendBtn.setBorder(BorderFactory.createEmptyBorder()); fiendBtn.setContentAreaFilled(false);
						JButton fishBtn = new JButton(); fishBtn.setBorder(BorderFactory.createEmptyBorder()); fishBtn.setContentAreaFilled(false);
						JButton insectBtn = new JButton(); insectBtn.setBorder(BorderFactory.createEmptyBorder()); insectBtn.setContentAreaFilled(false);
						JButton machineBtn = new JButton(); machineBtn.setBorder(BorderFactory.createEmptyBorder()); machineBtn.setContentAreaFilled(false);
						JButton plantBtn = new JButton(); plantBtn.setBorder(BorderFactory.createEmptyBorder()); plantBtn.setContentAreaFilled(false);
						JButton psychicBtn = new JButton(); psychicBtn.setBorder(BorderFactory.createEmptyBorder()); psychicBtn.setContentAreaFilled(false);
						JButton pyroBtn = new JButton(); pyroBtn.setBorder(BorderFactory.createEmptyBorder()); pyroBtn.setContentAreaFilled(false);
						JButton reptileBtn = new JButton(); reptileBtn.setBorder(BorderFactory.createEmptyBorder()); reptileBtn.setContentAreaFilled(false);
						JButton rockBtn = new JButton(); rockBtn.setBorder(BorderFactory.createEmptyBorder()); rockBtn.setContentAreaFilled(false);
						JButton seaSerpentBtn = new JButton(); seaSerpentBtn.setBorder(BorderFactory.createEmptyBorder()); seaSerpentBtn.setContentAreaFilled(false);
						JButton spellcasterBtn = new JButton(); spellcasterBtn.setBorder(BorderFactory.createEmptyBorder()); spellcasterBtn.setContentAreaFilled(false);
						JButton thunderBtn = new JButton(); thunderBtn.setBorder(BorderFactory.createEmptyBorder()); thunderBtn.setContentAreaFilled(false);
						JButton warriorBtn = new JButton(); warriorBtn.setBorder(BorderFactory.createEmptyBorder()); warriorBtn.setContentAreaFilled(false);
						JButton wingedBeastBtn = new JButton(); wingedBeastBtn.setBorder(BorderFactory.createEmptyBorder()); wingedBeastBtn.setContentAreaFilled(false);
						JButton zombieBtn = new JButton(); zombieBtn.setBorder(BorderFactory.createEmptyBorder()); zombieBtn.setContentAreaFilled(false);
						JButton darkBtn = new JButton(); darkBtn.setBorder(BorderFactory.createEmptyBorder()); darkBtn.setContentAreaFilled(false);
						JButton fireBtn = new JButton(); fireBtn.setBorder(BorderFactory.createEmptyBorder()); fireBtn.setContentAreaFilled(false);
						JButton earthBtn = new JButton(); earthBtn.setBorder(BorderFactory.createEmptyBorder()); earthBtn.setContentAreaFilled(false);
						JButton lightBtn = new JButton(); lightBtn.setBorder(BorderFactory.createEmptyBorder()); lightBtn.setContentAreaFilled(false);
						JButton waterBtn = new JButton(); waterBtn.setBorder(BorderFactory.createEmptyBorder()); waterBtn.setContentAreaFilled(false);
						JButton windBtn = new JButton(); windBtn.setBorder(BorderFactory.createEmptyBorder()); windBtn.setContentAreaFilled(false);
						JButton monsterBtn = new JButton(); monsterBtn.setBorder(BorderFactory.createEmptyBorder()); monsterBtn.setContentAreaFilled(false);
						JButton oneSacBtn = new JButton(); oneSacBtn.setBorder(BorderFactory.createEmptyBorder()); oneSacBtn.setContentAreaFilled(false);
						JButton twoSacBtn = new JButton(); twoSacBtn.setBorder(BorderFactory.createEmptyBorder()); twoSacBtn.setContentAreaFilled(false);
						JButton fullListBtn = new JButton(); fullListBtn.setBorder(BorderFactory.createEmptyBorder()); fullListBtn.setContentAreaFilled(false);
						
						ImageIcon aquaI = new ImageIcon("src/images/Type - Aqua.png"); aquaBtn.setToolTipText("Aqua"); aquaBtn.setIcon(aquaI);
						ImageIcon beastI = new ImageIcon("src/images/Type - Beast.png"); beastBtn.setToolTipText("Beast"); beastBtn.setIcon(beastI);
						ImageIcon beastWarriorI = new ImageIcon("src/images/Type - Beast-Warrior.png"); beastWarriorBtn.setToolTipText("Beast-Warrior"); beastWarriorBtn.setIcon(beastWarriorI);
						ImageIcon dinosaurI = new ImageIcon("src/images/Type - Dinosaur.png"); dinosaurBtn.setToolTipText("Dinosaur"); dinosaurBtn.setIcon(dinosaurI);
						ImageIcon divineI = new ImageIcon("src/images/Type - Divine-Beast.png"); divineBtn.setToolTipText("Divine"); divineBtn.setIcon(divineI);
						ImageIcon dragonI = new ImageIcon("src/images/Type - Dragon.png"); dragonBtn.setToolTipText("Dragon"); dragonBtn.setIcon(dragonI);
						ImageIcon fairyI = new ImageIcon("src/images/Type - Fairy.png"); fairyBtn.setToolTipText("Fairy"); fairyBtn.setIcon(fairyI);
						ImageIcon fiendI = new ImageIcon("src/images/Type - Fiend.png"); fiendBtn.setToolTipText("Fiend"); fiendBtn.setIcon(fiendI);
						ImageIcon fishI = new ImageIcon("src/images/Type - Fish.png"); fishBtn.setToolTipText("Fish"); fishBtn.setIcon(fishI);
						ImageIcon insectI = new ImageIcon("src/images/Type - Insect.png"); insectBtn.setToolTipText("Insect"); insectBtn.setIcon(insectI);
						ImageIcon machineI = new ImageIcon("src/images/Type - Machine.png"); machineBtn.setToolTipText("Machine"); machineBtn.setIcon(machineI);
						ImageIcon plantI = new ImageIcon("src/images/Type - Plant.png"); plantBtn.setToolTipText("Plant"); plantBtn.setIcon(plantI);
						ImageIcon psychicI = new ImageIcon("src/images/Type - Psychic.png"); psychicBtn.setToolTipText("Psychic"); psychicBtn.setIcon(psychicI);
						ImageIcon pyroI = new ImageIcon("src/images/Type - pyro.png"); pyroBtn.setToolTipText("Pyro"); pyroBtn.setIcon(pyroI);
						ImageIcon reptileI = new ImageIcon("src/images/Type - reptile.png"); reptileBtn.setToolTipText("Reptile"); reptileBtn.setIcon(reptileI);
						ImageIcon rockI = new ImageIcon("src/images/Type - rock.png"); rockBtn.setToolTipText("Rock"); rockBtn.setIcon(rockI);
						ImageIcon seaSerpentI = new ImageIcon("src/images/Type - Sea Serpent.png"); seaSerpentBtn.setToolTipText("Sea Serpent"); seaSerpentBtn.setIcon(seaSerpentI);
						ImageIcon spellcasterI = new ImageIcon("src/images/Type - Spellcaster.png"); spellcasterBtn.setToolTipText("Spellcaster"); spellcasterBtn.setIcon(spellcasterI);
						ImageIcon thunderI = new ImageIcon("src/images/Type - Thunder.png"); thunderBtn.setToolTipText("Thunder"); thunderBtn.setIcon(thunderI);
						ImageIcon warriorI = new ImageIcon("src/images/Type - Warrior.png"); warriorBtn.setToolTipText("Warrior"); warriorBtn.setIcon(warriorI);
						ImageIcon wingedBeastI = new ImageIcon("src/images/Type - Winged Beast.png"); wingedBeastBtn.setToolTipText("Winged Beast"); wingedBeastBtn.setIcon(wingedBeastI);
						ImageIcon zombieI = new ImageIcon("src/images/Type - Zombie.png"); zombieBtn.setToolTipText("Zombie"); zombieBtn.setIcon(zombieI);
						ImageIcon darkI = new ImageIcon("src/images/Attribute - Dark.png"); darkBtn.setToolTipText("Dark"); darkBtn.setIcon(darkI);
						ImageIcon fireI = new ImageIcon("src/images/Attribute - Fire.png"); fireBtn.setToolTipText("Fire"); fireBtn.setIcon(fireI);
						ImageIcon earthI = new ImageIcon("src/images/Attribute - Earth.png"); earthBtn.setToolTipText("Earth"); earthBtn.setIcon(earthI);
						ImageIcon lightI = new ImageIcon("src/images/Attribute - Light.png"); lightBtn.setToolTipText("Light"); lightBtn.setIcon(lightI);
						ImageIcon waterI = new ImageIcon("src/images/Attribute - Water.png"); waterBtn.setToolTipText("water"); waterBtn.setIcon(waterI);
						ImageIcon windI = new ImageIcon("src/images/Attribute - Wind.png"); windBtn.setToolTipText("Wind"); windBtn.setIcon(windI);
						ImageIcon monsterI = new ImageIcon("src/images/Level Under 4 Icons.png"); monsterBtn.setToolTipText("No Tribute"); monsterBtn.setIcon(monsterI);
						ImageIcon oneSacI = new ImageIcon("src/images/Level 56 Icons.png"); oneSacBtn.setToolTipText("One Tribute"); oneSacBtn.setIcon(oneSacI);
						ImageIcon twoSacI = new ImageIcon("src/images/Level 7 Icons.png"); twoSacBtn.setToolTipText("Two Tribute"); twoSacBtn.setIcon(twoSacI);
						ImageIcon spellI = new ImageIcon("src/images/Attribute - Spell.png"); spellBtn.setToolTipText("Spell"); spellBtn.setIcon(spellI);
						ImageIcon trapI = new ImageIcon("src/images/Attribute - Trap.png"); trapBtn.setToolTipText("Trap"); trapBtn.setIcon(trapI);
						ImageLabel score1I = new ImageLabel(new ImageIcon("src/images/Total Deck Score.png")); score1I.setToolTipText("Total Deck Score");
						ImageLabel score2I = new ImageLabel(new ImageIcon("src/images/Total Deck Score.png")); score2I.setToolTipText("Average Card Score");
						ImageIcon refreshI = new ImageIcon("src/images/View - Reset.png"); fullListBtn.setToolTipText("Full Deck"); fullListBtn.setIcon(refreshI);
						
						
						JPanel panel = new JPanel();
						panel.setLayout(new GridBagLayout());
						GridBagConstraints c = new GridBagConstraints();
						c.fill = GridBagConstraints.BOTH;
						c.weightx = 1.0;
						
						JLabel typeLbl = new JLabel("Types");
						JLabel attributeLbl = new JLabel("Attributes");
					
						JProgressBar cardProgress = new JProgressBar(0, (cardsToDraftLocal - 1));
						cardProgress.setValue(pickNumber - 1);
						cardProgress.setStringPainted(true);
						cardProgress.setString((pickNumber - 1) + "/" + cardsToDraftLocal);
						
						// Attributes
						JProgressBar darkProgress = new JProgressBar(0, (cardsToDraftLocal -1)); darkProgress.setValue(dark); darkProgress.setStringPainted(true);
						darkProgress.setString(String.valueOf(dark));
						toolTipCards(DarkCards, darkProgress);
						JProgressBar earthProgress = new JProgressBar(0, (cardsToDraftLocal -1)); earthProgress.setValue(earth); earthProgress.setStringPainted(true);
						earthProgress.setString(String.valueOf(earth));
						toolTipCards(EarthCards, earthProgress);
						JProgressBar fireProgress = new JProgressBar(0, (cardsToDraftLocal -1)); fireProgress.setValue(fire); fireProgress.setStringPainted(true);
						fireProgress.setString(String.valueOf(fire));
						toolTipCards(FireCards, fireProgress);
						JProgressBar lightProgress = new JProgressBar(0, (cardsToDraftLocal -1)); lightProgress.setValue(light); lightProgress.setStringPainted(true);
						lightProgress.setString(String.valueOf(light));
						toolTipCards(LightCards, lightProgress);
						JProgressBar waterProgress = new JProgressBar(0, (cardsToDraftLocal -1)); waterProgress.setValue(water); waterProgress.setStringPainted(true);
						waterProgress.setString(String.valueOf(water));
						toolTipCards(WaterCards, waterProgress);
						JProgressBar windProgress = new JProgressBar(0, (cardsToDraftLocal -1)); windProgress.setValue(wind); windProgress.setStringPainted(true);
						windProgress.setString(String.valueOf(wind));
						toolTipCards(WindCards, windProgress);
						JProgressBar monsterProgress = new JProgressBar(0, (cardsToDraftLocal -1)); monsterProgress.setValue(lowLvlCount); monsterProgress.setStringPainted(true);
						monsterProgress.setString(String.valueOf(lowLvlCount));
						toolTipCards(lowLvlCards, monsterProgress);
						JProgressBar spellProgress = new JProgressBar(0, (cardsToDraftLocal -1)); spellProgress.setValue(wind); spellProgress.setStringPainted(true);
						spellProgress.setString(String.valueOf(spells));
						toolTipCards(SpellCards, spellProgress);
						JProgressBar trapProgress = new JProgressBar(0, (cardsToDraftLocal -1)); trapProgress.setValue(wind); trapProgress.setStringPainted(true);
						trapProgress.setString(String.valueOf(traps));
						toolTipCards(TrapCards, trapProgress);
						
						// Types
						JProgressBar aquaProgress = new JProgressBar(0, (cardsToDraftLocal - 1)); aquaProgress.setValue(aqua); aquaProgress.setStringPainted(true);
						aquaProgress.setString(String.valueOf(aqua)); toolTipCards(AquaCards, aquaProgress);
						JProgressBar beastProgress = new JProgressBar(0, (cardsToDraftLocal - 1)); beastProgress.setValue(beast); beastProgress.setStringPainted(true);
						beastProgress.setString(String.valueOf(beast)); toolTipCards(BeastCards, beastProgress);
						JProgressBar beastWarriorProgress = new JProgressBar(0, (cardsToDraftLocal - 1)); beastWarriorProgress.setValue(beastWarrior); beastWarriorProgress.setStringPainted(true);
						beastWarriorProgress.setString(String.valueOf(beastWarrior)); 
						toolTipCards(BeastWarriorCards, beastWarriorProgress);
						JProgressBar dinosaurProgress = new JProgressBar(0, (cardsToDraftLocal - 1)); dinosaurProgress.setValue(dinosaur); dinosaurProgress.setStringPainted(true);
						dinosaurProgress.setString(String.valueOf(dinosaur));
						toolTipCards(DinosaurCards, dinosaurProgress);
						JProgressBar divineProgress = new JProgressBar(0, (cardsToDraftLocal - 1)); divineProgress.setValue(divine); divineProgress.setStringPainted(true);
						divineProgress.setString(String.valueOf(divine));
						toolTipCards(DivineCards, divineProgress);
						JProgressBar dragonProgress = new JProgressBar(0, (cardsToDraftLocal - 1)); dragonProgress.setValue(dragon); dragonProgress.setStringPainted(true);
						dragonProgress.setString(String.valueOf(dragon));
						toolTipCards(DragonCards, dragonProgress);
						JProgressBar fairyProgress = new JProgressBar(0, (cardsToDraftLocal - 1)); fairyProgress.setValue(fairy); fairyProgress.setStringPainted(true);
						fairyProgress.setString(String.valueOf(fairy));
						toolTipCards(FairyCards, fairyProgress);
						JProgressBar fiendProgress = new JProgressBar(0, (cardsToDraftLocal - 1)); fiendProgress.setValue(fiend); fiendProgress.setStringPainted(true);
						fiendProgress.setString(String.valueOf(fiend));
						toolTipCards(FiendCards, fiendProgress);
						JProgressBar fishProgress = new JProgressBar(0, (cardsToDraftLocal - 1)); fishProgress.setValue(fish); fishProgress.setStringPainted(true);
						fishProgress.setString(String.valueOf(fish));
						toolTipCards(FishCards, fishProgress);
						JProgressBar insectProgress = new JProgressBar(0, (cardsToDraftLocal - 1)); insectProgress.setValue(insect); insectProgress.setStringPainted(true);
						insectProgress.setString(String.valueOf(insect));
						toolTipCards(InsectCards, insectProgress);
						JProgressBar machineProgress = new JProgressBar(0, (cardsToDraftLocal - 1)); machineProgress.setValue(machine); machineProgress.setStringPainted(true);
						machineProgress.setString(String.valueOf(machine));
						toolTipCards(MachineCards, machineProgress);
						JProgressBar plantProgress = new JProgressBar(0, (cardsToDraftLocal - 1)); plantProgress.setValue(plant); plantProgress.setStringPainted(true);
						plantProgress.setString(String.valueOf(plant));
						toolTipCards(PlantCards, plantProgress);
						JProgressBar psychicProgress = new JProgressBar(0, (cardsToDraftLocal - 1)); psychicProgress.setValue(psychic); psychicProgress.setStringPainted(true);
						psychicProgress.setString(String.valueOf(psychic));
						toolTipCards(PsychicCards, psychicProgress);
						JProgressBar pyroProgress = new JProgressBar(0, (cardsToDraftLocal - 1)); pyroProgress.setValue(pyro); pyroProgress.setStringPainted(true);
						pyroProgress.setString(String.valueOf(pyro));
						toolTipCards(PyroCards, pyroProgress);
						JProgressBar reptileProgress = new JProgressBar(0, (cardsToDraftLocal - 1)); reptileProgress.setValue(reptile); reptileProgress.setStringPainted(true);
						reptileProgress.setString(String.valueOf(reptile));
						toolTipCards(ReptileCards, reptileProgress);
						JProgressBar rockProgress = new JProgressBar(0, (cardsToDraftLocal - 1)); rockProgress.setValue(rock); rockProgress.setStringPainted(true);
						rockProgress.setString(String.valueOf(rock));
						toolTipCards(RockCards, rockProgress);
						JProgressBar seaSerpentProgress = new JProgressBar(0, (cardsToDraftLocal - 1)); seaSerpentProgress.setValue(seaSerpent); seaSerpentProgress.setStringPainted(true);
						seaSerpentProgress.setString(String.valueOf(seaSerpent));
						toolTipCards(SeaSerpentCards, seaSerpentProgress);
						JProgressBar spellcasterProgress = new JProgressBar(0, (cardsToDraftLocal - 1)); spellcasterProgress.setValue(spellcaster); spellcasterProgress.setStringPainted(true);
						spellcasterProgress.setString(String.valueOf(spellcaster));
						toolTipCards(SpellcasterCards, spellcasterProgress);
						JProgressBar thunderProgress = new JProgressBar(0, (cardsToDraftLocal - 1)); thunderProgress.setValue(fiend); thunderProgress.setStringPainted(true);
						thunderProgress.setString(String.valueOf(thunder));
						toolTipCards(ThunderCards, thunderProgress);
						JProgressBar warriorProgress = new JProgressBar(0, (cardsToDraftLocal - 1)); warriorProgress.setValue(warrior); warriorProgress.setStringPainted(true);
						warriorProgress.setString(String.valueOf(warrior));
						toolTipCards(WarriorCards, warriorProgress);
						JProgressBar wingedBeastProgress = new JProgressBar(0, (cardsToDraftLocal - 1)); wingedBeastProgress.setValue(wingedBeast); wingedBeastProgress.setStringPainted(true);
						wingedBeastProgress.setString(String.valueOf(wingedBeast));
						toolTipCards(WingedBeastCards, wingedBeastProgress);
						JProgressBar zombieProgress = new JProgressBar(0, (cardsToDraftLocal - 1)); zombieProgress.setValue(zombie); zombieProgress.setStringPainted(true);
						zombieProgress.setString(String.valueOf(zombie));
						toolTipCards(ZombieCards, zombieProgress);
						
						// Other
						/*JProgressBar lowLvlProgress = new JProgressBar(0, (cardsToDraftLocal - 1)); lowLvlProgress.setValue(lowLvlCount); lowLvlProgress.setStringPainted(true);
						lowLvlProgress.setString(String.valueOf(lowLvlCount));
						toolTipCards(lowLvlCards, lowLvlProgress);*/
						JProgressBar medLvlProgress = new JProgressBar(0, (cardsToDraftLocal - 1)); medLvlProgress.setValue(medLvlCount); medLvlProgress.setStringPainted(true);
						medLvlProgress.setString(String.valueOf(medLvlCount));
						toolTipCards(medLvlCards, medLvlProgress);
						JProgressBar highLvlProgress = new JProgressBar(0, (cardsToDraftLocal - 1)); highLvlProgress.setValue(highLvlCount); highLvlProgress.setStringPainted(true);
						highLvlProgress.setString(String.valueOf(highLvlCount));
						toolTipCards(highLvlCards, highLvlProgress);
						JProgressBar totalScoreProgress = new JProgressBar(0, (cardsToDraftLocal * 100)); totalScoreProgress.setValue(totalScore); totalScoreProgress.setStringPainted(true);
						totalScoreProgress.setString(String.valueOf(totalScore));
						JProgressBar avgScoreProgress = new JProgressBar(0, 100); avgScoreProgress.setValue(avgScore); avgScoreProgress.setStringPainted(true);
						avgScoreProgress.setString(String.valueOf(avgScore));
						
						
						
						
						KeyListener keyListener = new KeyListener()
						{
							public void keyPressed(KeyEvent e)
							{
								if (e.getKeyCode() == KeyEvent.VK_ENTER)
								{
									dynamicCard = dynamicList2.getSelectedValue().getCard();									
									singleCardView = new JFrame();
									singleCardView.setTitle(dynamicCard.getName());
									singleCardView.setAlwaysOnTop(true);
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
												fullView.setAlwaysOnTop(true);
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
											textFrame.setAlwaysOnTop(true);
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
						//list.addKeyListener(keyListener);
						dynamicList2.addKeyListener(keyListener);
						MouseListener mouseListener = new MouseAdapter() 
						{
							public void mouseClicked(MouseEvent e) 
							{
								if (e.getClickCount() == 2) 
								{    		
									dynamicCard = dynamicList2.getSelectedValue().getCard();
									singleCardView = new JFrame();		
									singleCardView.setTitle(dynamicCard.getName());
									singleCardView.setAlwaysOnTop(true);
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
												fullView.setAlwaysOnTop(true);
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
											textFrame.setAlwaysOnTop(true);
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
						dynamicList2.addMouseListener(mouseListener);
						JScrollPane scrollPane = new JScrollPane();
						scrollPane.setViewportView(dynamicList2);
						int defaultHeight = scrollPane.getHeight();
						int defaultWidth = scrollPane.getWidth();
						Dimension spDim = new Dimension(defaultWidth, defaultHeight);
						scrollPane.setMinimumSize(spDim);
						scrollPane.setMaximumSize(spDim);
						
						c.gridx = 0; c.gridy = 0; c.gridheight = 600; c.gridwidth = 1; c.fill = GridBagConstraints.BOTH; panel.add(scrollPane, c);
						
						c.insets = new Insets(5, 5, 5, 5); c.gridheight = 1;
						c.gridx = 2; c.gridy = 1; panel.add(typeLbl, c);
						c.gridx = 4; c.gridy = 1; c.gridwidth = 1; panel.add(attributeLbl, c);
						c.gridx = 2; c.gridy = 2; c.gridwidth = 1; panel.add(darkProgress, c);
						c.gridx = 1; c.gridy = 2; panel.add(darkBtn, c);
						c.gridx = 2; c.gridy = 3; panel.add(earthProgress, c);
						c.gridx = 1; c.gridy = 3; panel.add(earthBtn, c);
						c.gridx = 2; c.gridy = 4; panel.add(fireProgress, c);
						c.gridx = 1; c.gridy = 4; panel.add(fireBtn, c);
						c.gridx = 2; c.gridy = 5; panel.add(lightProgress, c);
						c.gridx = 1; c.gridy = 5; panel.add(lightBtn, c);
						c.gridx = 2; c.gridy = 6; panel.add(waterProgress, c);
						c.gridx = 1; c.gridy = 6; panel.add(waterBtn, c);
						c.gridx = 2; c.gridy = 7; panel.add(windProgress, c);
						c.gridx = 1; c.gridy = 7; panel.add(windBtn, c);
						c.gridx = 2; c.gridy = 8; panel.add(monsterProgress, c);
						c.gridx = 1; c.gridy = 8; panel.add(monsterBtn, c);
						c.gridx = 2; c.gridy = 9; panel.add(medLvlProgress, c);
						c.gridx = 1; c.gridy = 9; panel.add(oneSacBtn, c);
						c.gridx = 2; c.gridy = 10; panel.add(highLvlProgress, c);
						c.gridx = 1; c.gridy = 10; panel.add(twoSacBtn, c);
						c.gridx = 2; c.gridy = 11; panel.add(spellProgress, c);
						c.gridx = 1; c.gridy = 11; panel.add(spellBtn, c);
						c.gridx = 2; c.gridy = 12; panel.add(trapProgress, c);
						c.gridx = 1; c.gridy = 12; panel.add(trapBtn, c);
						c.gridx = 4; c.gridy = 2; panel.add(aquaProgress, c);
						c.gridx = 3; c.gridy = 2; panel.add(aquaBtn, c);
						c.gridx = 4; c.gridy = 3; panel.add(beastProgress, c);
						c.gridx = 3; c.gridy = 3; panel.add(beastBtn, c);
						c.gridx = 4; c.gridy = 4; panel.add(beastWarriorProgress, c);
						c.gridx = 3; c.gridy = 4; panel.add(beastWarriorBtn, c);
						c.gridx = 4; c.gridy = 5; panel.add(dinosaurProgress, c);
						c.gridx = 3; c.gridy = 5; panel.add(dinosaurBtn, c);
						c.gridx = 4; c.gridy = 6; panel.add(divineProgress, c);
						c.gridx = 3; c.gridy = 6; panel.add(divineBtn, c);
						c.gridx = 4; c.gridy = 7; panel.add(dragonProgress, c);
						c.gridx = 3; c.gridy = 7; panel.add(dragonBtn, c);
						c.gridx = 4; c.gridy = 8; panel.add(fairyProgress, c);
						c.gridx = 3; c.gridy = 8; panel.add(fairyBtn, c);
						c.gridx = 4; c.gridy = 9; panel.add(fiendProgress, c);
						c.gridx = 3; c.gridy = 9; panel.add(fiendBtn, c);
						c.gridx = 4; c.gridy = 10; panel.add(fishProgress, c);
						c.gridx = 3; c.gridy = 10; panel.add(fishBtn, c);
						c.gridx = 4; c.gridy = 11; panel.add(insectProgress, c);
						c.gridx = 3; c.gridy = 11; panel.add(insectBtn, c);
						c.gridx = 4; c.gridy = 12; panel.add(machineProgress, c);
						c.gridx = 3; c.gridy = 12; panel.add(machineBtn, c);
						c.gridx = 6; c.gridy = 2; panel.add(plantProgress, c);
						c.gridx = 5; c.gridy = 2; panel.add(plantBtn, c);
						c.gridx = 6; c.gridy = 3; panel.add(psychicProgress, c);
						c.gridx = 5; c.gridy = 3; panel.add(psychicBtn, c);
						c.gridx = 6; c.gridy = 4; panel.add(pyroProgress, c);
						c.gridx = 5; c.gridy = 4; panel.add(pyroBtn, c);
						c.gridx = 6; c.gridy = 5; panel.add(reptileProgress, c);
						c.gridx = 5; c.gridy = 5; panel.add(reptileBtn, c);
						c.gridx = 6; c.gridy = 6; panel.add(rockProgress, c);
						c.gridx = 5; c.gridy = 6; panel.add(rockBtn, c);
						c.gridx = 6; c.gridy = 7; panel.add(seaSerpentProgress, c);
						c.gridx = 5; c.gridy = 7; panel.add(seaSerpentBtn, c);
						c.gridx = 6; c.gridy = 8; panel.add(spellcasterProgress, c);
						c.gridx = 5; c.gridy = 8; panel.add(spellcasterBtn, c);
						c.gridx = 6; c.gridy = 9; panel.add(thunderProgress, c);
						c.gridx = 5; c.gridy = 9; panel.add(thunderBtn, c);
						c.gridx = 6; c.gridy = 10; panel.add(warriorProgress, c);
						c.gridx = 5; c.gridy = 10; panel.add(warriorBtn, c);
						c.gridx = 6; c.gridy = 11; panel.add(wingedBeastProgress, c);
						c.gridx = 5; c.gridy = 11; panel.add(wingedBeastBtn, c);
						c.gridx = 6; c.gridy = 12; panel.add(zombieProgress, c);
						c.gridx = 5; c.gridy = 12; panel.add(zombieBtn, c);
						c.gridx = 2; c.gridy = 13; c.gridwidth = 5; panel.add(totalScoreProgress, c);
						c.gridx = 1; c.gridy = 13; c.gridwidth = 1; panel.add(score1I, c);
						c.gridx = 2; c.gridy = 14; c.gridwidth = 5; panel.add(avgScoreProgress, c);
						c.gridx = 1; c.gridy = 14; c.gridwidth = 1; panel.add(score2I, c);
						c.insets = new Insets(0, 5, 10, 5);
						c.gridx = 0; c.gridy = 601; panel.add(cardProgress, c);
						c.gridx = 1; c.gridy = 601; panel.add(fullListBtn, c);
						cardViewLocal.getContentPane().add(panel);
						setupDeckListeners(cardViewLocal, scrollPane, fullListBtn, drafted, aquaBtn, AquaCards, beastBtn, BeastCards,
								BeastWarriorCards, beastWarriorBtn, DinosaurCards, dinosaurBtn, DivineCards, divineBtn, DragonCards, dragonBtn,
								FairyCards, fairyBtn, FiendCards, fiendBtn, FishCards, fishBtn, InsectCards, insectBtn, MachineCards, machineBtn,
								PlantCards, plantBtn, PsychicCards, psychicBtn, PyroCards, pyroBtn, ReptileCards, reptileBtn, RockCards, rockBtn,
								SeaSerpentCards, seaSerpentBtn, SpellcasterCards, spellcasterBtn, ThunderCards, thunderBtn, WarriorCards, warriorBtn,
								WingedBeastCards, wingedBeastBtn, ZombieCards, zombieBtn, DarkCards, darkBtn, FireCards, fireBtn, EarthCards, earthBtn,
								LightCards, lightBtn, WaterCards, waterBtn, WindCards, windBtn, lowLvlCards, monsterBtn, medLvlCards, oneSacBtn, highLvlCards,
								twoSacBtn, SpellCards, spellBtn, TrapCards, trapBtn, spDim);
						
						
		
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
					
					// This line is for debugging when I fuck something up on the database input side
					//System.out.println(input[0] + " -- " + input[21]);

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
	
	static void listEntryMaker(ArrayList<Card> pool, DefaultListModel model)
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
		
		temp.sort(temp.get(0));
		cardCounter(temp);
		
		for (Card card : temp) 
		{
			 model.addElement(new ListEntry(card.getName(), new ImageIcon("src/images/Quantity - " + card.getQuantity() + ".png"), card));
		}
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
		
		/*
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
		*/
	
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
				if ((seed < 726) && (howManyRarity(draftPool, common) > 0)) { rolling = rarityRoll(draftPool, common, deck, temp, roll, seedValue, picks, rolling, seed); roll = common; } 								// 72.6% chance at common				
				else if ((seed >= 726 && seed < 910) && (howManyRarity(draftPool, rare) > 0)) { rolling = rarityRoll(draftPool, rare, deck, temp, roll, seedValue, picks, rolling, seed); roll = rare; }				// 18.4% chance at rare
				else if ((seed >= 910 && seed < 972) && (howManyRarity(draftPool, superR) > 0)) { rolling = rarityRoll(draftPool, superR, deck, temp, roll, seedValue, picks, rolling, seed); roll = superR; }			// 6.2% chance at super rare
				else if ((seed >= 972 && seed < 996) && (howManyRarity(draftPool, ultra) > 0)) { rolling = rarityRoll(draftPool, ultra, deck, temp, roll, seedValue, picks, rolling, seed); roll = ultra; }				// 2.4% chance at ultra rare
				else if ((seed >= 996 && seed <= 1000) && (howManyRarity(draftPool, ultimate) > 0)) { rolling = rarityRoll(draftPool, ultimate, deck, temp, roll, seedValue, picks, rolling, seed); roll = ultimate; }	// 0.4% chance at ultimate rare
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
				if ((seed < 726) && (howManyRarity(draftPool, common) > 0)) { rolling = rarityRoll(draftPool, common, deck, temp, roll, seedValue, picks, rolling, seed); roll = common; } 							// 69% chance at common				
				else if ((seed >= 726 && seed < 910) && (howManyRarity(draftPool, rare) > 0)) { rolling = rarityRoll(draftPool, rare, deck, temp, roll, seedValue, picks, rolling, seed); roll = rare; }			// 8% chance at rare
				else if ((seed >= 910 && seed < 972) && (howManyRarity(draftPool, superR) > 0)) { rolling = rarityRoll(draftPool, superR, deck, temp, roll, seedValue, picks, rolling, seed); roll = superR; }		// 4% chance at super rare
				else if ((seed >= 972 && seed < 996) && (howManyRarity(draftPool, ultra) > 0)) { rolling = rarityRoll(draftPool, ultra, deck, temp, roll, seedValue, picks, rolling, seed); roll = ultra; }		// 2% chance at ultra rare
				else if ((seed >= 996 && seed <= 1000) && (howManyRarity(draftPool, ultimate) > 0)) { rolling = rarityRoll(draftPool, ultimate, deck, temp, roll, seedValue, picks, rolling, seed); roll = ultimate; }	// 1% chance at ultimate rare
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
			JMenuItem Lightsworn, JMenuItem Magnet, JMenuItem Ojama, JMenuItem Predaplant, JMenuItem SuperHeavy, JMenuItem Creator, JMenuItem Volcanic,
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
		
		Predaplant.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean checker = false;
				for (int a = 0; a < allCards.size(); a++)
				{
					if (allCards.get(a).getSynergies().contains("predaplant")) 
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

				Predaplant.setEnabled(false);
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
			JMenuItem Exodia, JMenuItem Water, JMenuItem Naturia, JMenuItem Nekroz, JMenuItem Toon, JMenuItem Draw, JMenuItem Ritual, JMenuItem Fusion, JMenuItem LowAtk, JMenuItem Limited,
			JMenuItem SemiLimited, JMenuItem HighAtk, JMenuItem LowLvl, JMenuItem HighLvl, JMenuItem reset, ArrayList<Card> backupAllCards, ArrayList<Card> allCardsNopeDupe,
			JMenuItem Light, JMenuItem Dark, JMenuItem Wind, JMenuItem Fire, JMenuItem Earth, JMenuItem AncientGear, JMenuItem Archfiend, JMenuItem Aroma, JMenuItem Crashbug, JMenuItem Destiny,
			JMenuItem Elemental, JMenuItem Flip, JMenuItem Galaxy, JMenuItem Gishki, JMenuItem God, JMenuItem Harpies, JMenuItem Hazy, JMenuItem HeroicChallenger, JMenuItem LV, JMenuItem Lightsworn, JMenuItem Magnet,
			JMenuItem Ojama, JMenuItem Predaplant, JMenuItem SuperHeavy, JMenuItem Creator, JMenuItem Vampire, JMenuItem Volcanic, JMenuItem Discard, JMenuItem EasySummon, JMenuItem Aqua,
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
				AncientGear.setEnabled(true); Archfiend.setEnabled(true); Aroma.setEnabled(true);
				Crashbug.setEnabled(true); Destiny.setEnabled(true); Elemental.setEnabled(true);
				Flip.setEnabled(true); Galaxy.setEnabled(true); Gishki.setEnabled(true); God.setEnabled(true);
				Harpies.setEnabled(true); Hazy.setEnabled(true); HeroicChallenger.setEnabled(true);
				LV.setEnabled(true);
				Lightsworn.setEnabled(true); Magnet.setEnabled(true); Ojama.setEnabled(true);
				Predaplant.setEnabled(true); Nekroz.setEnabled(true);
				SuperHeavy.setEnabled(true); Creator.setEnabled(true); Vampire.setEnabled(true);
				Volcanic.setEnabled(true);
				Discard.setEnabled(true); EasySummon.setEnabled(true); Aqua.setEnabled(true);
				Beast.setEnabled(true); BeastWarrior.setEnabled(true); Dinosaur.setEnabled(true);
				Divine.setEnabled(true); Dragon.setEnabled(true); Fairy.setEnabled(true);
				Fiend.setEnabled(true); Fish.setEnabled(true); Insect.setEnabled(true);
				Machine.setEnabled(true); Plant.setEnabled(true); 
				//Psychic.setEnabled(true);
				Pyro.setEnabled(true); Reptile.setEnabled(true); Rock.setEnabled(true);
				SeaSerpent.setEnabled(true); Spellcaster.setEnabled(true); Thunder.setEnabled(true);
				Warrior.setEnabled(true); WingedBeast.setEnabled(true); Wyrm.setEnabled(true);
				Zombie.setEnabled(true);  Spell.setEnabled(true); Trap.setEnabled(true);
				ContinSpell.setEnabled(true); ContinTrap.setEnabled(true); Contin.setEnabled(true);
				Quickplay.setEnabled(true); Field.setEnabled(true); Equip.setEnabled(true);
				Counter.setEnabled(true); lowScore.setEnabled(true); medScore.setEnabled(true);
				highScore.setEnabled(true); veryHighScore.setEnabled(true); OP.setEnabled(true);
				int totalCards2 = cardCount(allCards); lblCardCount.setText("Cards Available: " + totalCards2);
				int totalUnique = allCardsNopeDupe.size(); lblUniqueCards.setText("Unique Cards: " + totalUnique);
				DraftInit.revalidate(); DraftInit.repaint();
			}
		});
		// end reset listener

		banGroupListenerInit(allCards, DraftInit, banModel, lblCardCount, lblUniqueCards, Monarchs, Fissure, TrapHole,  Exodia,  
				Naturia, Toon,  Draw, Ritual,  Fusion,  LowAtk,  Limited, SemiLimited,  HighAtk,  LowLvl,  HighLvl, AncientGear, 
				Archfiend, Crashbug, Destiny, Elemental, Flip, Gishki, God, Harpies, Hazy, LV, Lightsworn, Magnet, Ojama, Predaplant, SuperHeavy,
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
	

	

	
	// Type counter
	public static int typeCount(ArrayList<Card> pool, String type)
	{
		int count = 0;
		for (Card card : pool)
		{
			if (card.getType().equals(type))
			{
				count++;
			}
		}
		
		return count;
	}
	
	// Attribute counter
	public static int attributeCount(ArrayList<Card> pool, String attribute)
	{
		int count = 0;
		for (Card card : pool)
		{
			if (card.getAttribute().equals(attribute))
			{
				count++;
			}
		}
		
		return count;
	}
	
	
	// Spell counter
	public static int spellCount(ArrayList<Card> pool)
	{
		int count = 0;
		for (Card card : pool)
		{
			if (card.getCardType().equals("Spell"))
			{
				count++;
			}
		}
		
		return count;
	}
	
	// Trap counter
	public static int trapCount(ArrayList<Card> pool)
	{
		int count = 0;
		for (Card card : pool)
		{
			if (card.getCardType().equals("Trap"))
			{
				count++;
			}
		}
		
		return count;
	}
	
	// Level < counter
	public static int lvlLessCount(ArrayList<Card> pool, int lvl)
	{
		int count = 0;
		for (Card card : pool)
		{
			if (card.getLvl() < lvl && card.getLvl() != 0)
			{
				count++;
			}
		}
		
		return count;
	}
	
	// Level > counter
	public static int lvlMoreCount(ArrayList<Card> pool, int lvl)
	{
		int count = 0;
		for (Card card : pool)
		{
			if (card.getLvl() > lvl)
			{
				count++;
			}
		}
		
		return count;
	}
	

	// Level >< counter
	public static int lvlBetweenCount(ArrayList<Card> pool, int lowLvl, int highLvl)
	{
		int count = 0;
		for (Card card : pool)
		{
			if (card.getLvl() > lowLvl && card.getLvl() < highLvl)
			{
				count++;
			}
		}
		
		return count;
	}
	
	// Calculates total score for the cards in the given pool
	public static int scoreCards(ArrayList<Card> pool)
	{
		int score = 0;
		for (Card card : pool)
		{
			score += card.getTierScore();
		}
		return score;
	}
	
	// Calculates avg score per card in the given pool
	public static int avgScoreCards(ArrayList<Card> pool, int poolSize)
	{
		int score = 0;
		for (Card card : pool)
		{
			score += card.getTierScore();
		}
		
		score = score / poolSize;
		return score;
	}
	
	public static void setupDeckListeners(JFrame localFrame, JScrollPane scrollPane, JButton fullListBtn, ArrayList<Card> fullDeck, JButton aqua, ArrayList<Card> AquaCards, JButton beast, ArrayList<Card> BeastCards,
			ArrayList<Card> BeastWarriorCards, JButton BeastWarriorBtn, ArrayList<Card> DinosaurCards, JButton DinosaurBtn, ArrayList<Card> DivineCards, JButton DivineBtn, ArrayList<Card> DragonCards, JButton DragonBtn,
			ArrayList<Card> FairyCards, JButton FairyBtn, ArrayList<Card> FiendCards, JButton FiendBtn, ArrayList<Card> FishCards, JButton FishBtn, ArrayList<Card> InsectCards, JButton InsectBtn, ArrayList<Card> MachineCards, JButton MachineBtn,
			ArrayList<Card> PlantCards, JButton PlantBtn, ArrayList<Card> PsychicCards, JButton PsychicBtn, ArrayList<Card> PyroCards, JButton PyroBtn, ArrayList<Card> ReptileCards, JButton ReptileBtn, ArrayList<Card> RockCards, JButton RockBtn,
			ArrayList<Card> SeaSerpentCards, JButton SeaSerpentBtn, ArrayList<Card> SpellcasterCards, JButton SpellcasterBtn, ArrayList<Card> ThunderCards, JButton ThunderBtn, ArrayList<Card> WarriorCards, JButton WarriorBtn,
			ArrayList<Card> WingedBeastCards, JButton WingedBeastBtn, ArrayList<Card> ZombieCards, JButton ZombieBtn, ArrayList<Card> DarkCards, JButton DarkBtn, ArrayList<Card> FireCards, JButton FireBtn, ArrayList<Card> EarthCards,JButton  EarthBtn,
			ArrayList<Card> LightCards, JButton LightBtn, ArrayList<Card> WaterCards, JButton WaterBtn, ArrayList<Card> WindCards, JButton WindBtn, ArrayList<Card> MonsterCards, JButton MonsterBtn, ArrayList<Card> OneSacCards, JButton OneSacBtn, ArrayList<Card> TwoSacCards,
			JButton TwoSacBtn, ArrayList<Card> SpellCards, JButton SpellBtn, ArrayList<Card> TrapCards, JButton TrapBtn, Dimension spDim)
	{
		
		
		
		fullListBtn.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				refreshList(fullDeck);
				scrollPane.setMinimumSize(spDim);
				scrollPane.setMaximumSize(spDim);
				scrollPane.setViewportView(dynamicList2);
				localFrame.revalidate(); localFrame.repaint();
			}
			
		});
		
		aqua.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				if (AquaCards.isEmpty() == false)
				{
					refreshList(AquaCards);
					scrollPane.setMinimumSize(spDim);
					scrollPane.setMaximumSize(spDim);
					scrollPane.setViewportView(dynamicList2);
					localFrame.revalidate(); localFrame.repaint();
				}
				
			}
			
		});
		
		beast.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				if (BeastCards.isEmpty() == false)
				{
					refreshList(BeastCards);
					scrollPane.setMinimumSize(spDim);
					scrollPane.setMaximumSize(spDim);
					scrollPane.setViewportView(dynamicList2);
					localFrame.revalidate(); localFrame.repaint();
			
				}
			}
		});
		
		BeastWarriorBtn.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				if (BeastWarriorCards.isEmpty() == false)
				{
					refreshList(BeastWarriorCards);
					scrollPane.setMinimumSize(spDim);
					scrollPane.setMaximumSize(spDim);
					scrollPane.setViewportView(dynamicList2);
					localFrame.revalidate(); localFrame.repaint();
			
				}
			}
		});
		
		DinosaurBtn.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				if (DinosaurCards.isEmpty() == false)
				{
					refreshList(DinosaurCards);
					scrollPane.setMinimumSize(spDim);
					scrollPane.setMaximumSize(spDim);
					scrollPane.setViewportView(dynamicList2);
					localFrame.revalidate(); localFrame.repaint();
			
				}
			}
		});
		
		DivineBtn.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				if (DivineCards.isEmpty() == false)
				{
					refreshList(DivineCards);
					scrollPane.setMinimumSize(spDim);
					scrollPane.setMaximumSize(spDim);
					scrollPane.setViewportView(dynamicList2);
					localFrame.revalidate(); localFrame.repaint();
			
				}
			}
		});
		
		DragonBtn.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				if (DragonCards.isEmpty() == false)
				{
					refreshList(DragonCards);
					scrollPane.setMinimumSize(spDim);
					scrollPane.setMaximumSize(spDim);
					scrollPane.setViewportView(dynamicList2);
					localFrame.revalidate(); localFrame.repaint();
			
				}
			}
		});
		
		FairyBtn.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				if (FairyCards.isEmpty() == false)
				{
					refreshList(FairyCards);
					scrollPane.setMinimumSize(spDim);
					scrollPane.setMaximumSize(spDim);
					scrollPane.setViewportView(dynamicList2);
					localFrame.revalidate(); localFrame.repaint();
			
				}
			}
		});
		
		FiendBtn.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				if (FiendCards.isEmpty() == false)
				{
					refreshList(FiendCards);
					scrollPane.setMinimumSize(spDim);
					scrollPane.setMaximumSize(spDim);
					scrollPane.setViewportView(dynamicList2);
					localFrame.revalidate(); localFrame.repaint();
			
				}
			}
		});
		
		FishBtn.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				if (FishCards.isEmpty() == false)
				{
					refreshList(FishCards);
					scrollPane.setMinimumSize(spDim);
					scrollPane.setMaximumSize(spDim);
					scrollPane.setViewportView(dynamicList2);
					localFrame.revalidate(); localFrame.repaint();
			
				}
			}
		});
		
		InsectBtn.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				if (InsectCards.isEmpty() == false)
				{
					refreshList(InsectCards);
					scrollPane.setMinimumSize(spDim);
					scrollPane.setMaximumSize(spDim);
					scrollPane.setViewportView(dynamicList2);
					localFrame.revalidate(); localFrame.repaint();
			
				}
			}
		});
		
		MachineBtn.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				if (MachineCards.isEmpty() == false)
				{
					refreshList(MachineCards);
					scrollPane.setMinimumSize(spDim);
					scrollPane.setMaximumSize(spDim);
					scrollPane.setViewportView(dynamicList2);
					localFrame.revalidate(); localFrame.repaint();
			
				}
			}
		});
		
		PlantBtn.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				if (PlantCards.isEmpty() == false)
				{
					refreshList(PlantCards);
					scrollPane.setMinimumSize(spDim);
					scrollPane.setMaximumSize(spDim);
					scrollPane.setViewportView(dynamicList2);
					localFrame.revalidate(); localFrame.repaint();
			
				}
			}
		});
		
		PsychicBtn.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				if (PsychicCards.isEmpty() == false)
				{
					refreshList(PsychicCards);
					scrollPane.setMinimumSize(spDim);
					scrollPane.setMaximumSize(spDim);
					scrollPane.setViewportView(dynamicList2);
					localFrame.revalidate(); localFrame.repaint();
			
				}
			}
		});
		
		PyroBtn.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				if (PyroCards.isEmpty() == false)
				{
					refreshList(PyroCards);
					scrollPane.setMinimumSize(spDim);
					scrollPane.setMaximumSize(spDim);
					scrollPane.setViewportView(dynamicList2);
					localFrame.revalidate(); localFrame.repaint();
			
				}
			}
		});
		
		ReptileBtn.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				if (ReptileCards.isEmpty() == false)
				{
					refreshList(ReptileCards);
					scrollPane.setMinimumSize(spDim);
					scrollPane.setMaximumSize(spDim);
					scrollPane.setViewportView(dynamicList2);
					localFrame.revalidate(); localFrame.repaint();
			
				}
			}
		});
		
		RockBtn.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				if (RockCards.isEmpty() == false)
				{
					refreshList(RockCards);
					scrollPane.setMinimumSize(spDim);
					scrollPane.setMaximumSize(spDim);
					scrollPane.setViewportView(dynamicList2);
					localFrame.revalidate(); localFrame.repaint();
			
				}
			}
		});
		
		SeaSerpentBtn.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				if (SeaSerpentCards.isEmpty() == false)
				{
					refreshList(SeaSerpentCards);
					scrollPane.setMinimumSize(spDim);
					scrollPane.setMaximumSize(spDim);
					scrollPane.setViewportView(dynamicList2);
					localFrame.revalidate(); localFrame.repaint();
			
				}
			}
		});
		
		SpellcasterBtn.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				if (SpellcasterCards.isEmpty() == false)
				{
					refreshList(SpellcasterCards);
					scrollPane.setMinimumSize(spDim);
					scrollPane.setMaximumSize(spDim);
					scrollPane.setViewportView(dynamicList2);
					localFrame.revalidate(); localFrame.repaint();
			
				}
			}
		});
		
		ThunderBtn.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				if (ThunderCards.isEmpty() == false)
				{
					refreshList(ThunderCards);
					scrollPane.setMinimumSize(spDim);
					scrollPane.setMaximumSize(spDim);
					scrollPane.setViewportView(dynamicList2);
					localFrame.revalidate(); localFrame.repaint();
			
				}
			}
		});
		
		WarriorBtn.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				if (WarriorCards.isEmpty() == false)
				{
					refreshList(WarriorCards);
					scrollPane.setMinimumSize(spDim);
					scrollPane.setMaximumSize(spDim);
					scrollPane.setViewportView(dynamicList2);
					localFrame.revalidate(); localFrame.repaint();
			
				}
			}
		});
		
		WingedBeastBtn.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				if (WingedBeastCards.isEmpty() == false)
				{
					refreshList(WingedBeastCards);
					scrollPane.setMinimumSize(spDim);
					scrollPane.setMaximumSize(spDim);
					scrollPane.setViewportView(dynamicList2);
					localFrame.revalidate(); localFrame.repaint();
			
				}
			}
		});
		
		ZombieBtn.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				if (ZombieCards.isEmpty() == false)
				{
					refreshList(ZombieCards);
					scrollPane.setMinimumSize(spDim);
					scrollPane.setMaximumSize(spDim);
					scrollPane.setViewportView(dynamicList2);
					localFrame.revalidate(); localFrame.repaint();
			
				}
			}
		});
		
		DarkBtn.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				if (DarkCards.isEmpty() == false)
				{
					refreshList(DarkCards);
					scrollPane.setMinimumSize(spDim);
					scrollPane.setMaximumSize(spDim);
					scrollPane.setViewportView(dynamicList2);
					localFrame.revalidate(); localFrame.repaint();
			
				}
			}
		});
		
		FireBtn.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				if (FireCards.isEmpty() == false)
				{
					refreshList(FireCards);
					scrollPane.setMinimumSize(spDim);
					scrollPane.setMaximumSize(spDim);
					scrollPane.setViewportView(dynamicList2);
					localFrame.revalidate(); localFrame.repaint();
			
				}
			}
		});
		
		EarthBtn.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				if (EarthCards.isEmpty() == false)
				{
					refreshList(EarthCards);
					scrollPane.setMinimumSize(spDim);
					scrollPane.setMaximumSize(spDim);
					scrollPane.setViewportView(dynamicList2);
					localFrame.revalidate(); localFrame.repaint();
			
				}
			}
		});
		
		LightBtn.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				if (LightCards.isEmpty() == false)
				{
					refreshList(LightCards);
					scrollPane.setMinimumSize(spDim);
					scrollPane.setMaximumSize(spDim);
					scrollPane.setViewportView(dynamicList2);
					localFrame.revalidate(); localFrame.repaint();
			
				}
			}
		});
		
		WaterBtn.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				if (WaterCards.isEmpty() == false)
				{
					refreshList(WaterCards);
					scrollPane.setMinimumSize(spDim);
					scrollPane.setMaximumSize(spDim);
					scrollPane.setViewportView(dynamicList2);
					localFrame.revalidate(); localFrame.repaint();
			
				}
			}
		});
		
		WindBtn.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				if (WindCards.isEmpty() == false)
				{
					refreshList(WindCards);
					scrollPane.setMinimumSize(spDim);
					scrollPane.setMaximumSize(spDim);
					scrollPane.setViewportView(dynamicList2);
					localFrame.revalidate(); localFrame.repaint();
			
				}
			}
		});
		
		MonsterBtn.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				if (MonsterCards.isEmpty() == false)
				{
					refreshList(MonsterCards);
					scrollPane.setMinimumSize(spDim);
					scrollPane.setMaximumSize(spDim);
					scrollPane.setViewportView(dynamicList2);
					localFrame.revalidate(); localFrame.repaint();
			
				}
			}
		});
		
		OneSacBtn.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				if (OneSacCards.isEmpty() == false)
				{
					refreshList(OneSacCards);
					scrollPane.setMinimumSize(spDim);
					scrollPane.setMaximumSize(spDim);
					scrollPane.setViewportView(dynamicList2);
					localFrame.revalidate(); localFrame.repaint();
			
				}
			}
		});
		
		TwoSacBtn.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				if (TwoSacCards.isEmpty() == false)
				{
					refreshList(TwoSacCards);
					scrollPane.setMinimumSize(spDim);
					scrollPane.setMaximumSize(spDim);
					scrollPane.setViewportView(dynamicList2);
					localFrame.revalidate(); localFrame.repaint();
			
				}
			}
		});
		
		SpellBtn.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				if (SpellCards.isEmpty() == false)
				{
					refreshList(SpellCards);
					scrollPane.setMinimumSize(spDim);
					scrollPane.setMaximumSize(spDim);
					scrollPane.setViewportView(dynamicList2);
					localFrame.revalidate(); localFrame.repaint();
			
				}
			}
		});
		
		TrapBtn.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				if (TrapCards.isEmpty() == false)
				{
					refreshList(TrapCards);
					scrollPane.setMinimumSize(spDim);
					scrollPane.setMaximumSize(spDim);
					scrollPane.setViewportView(dynamicList2);
					localFrame.revalidate(); localFrame.repaint();
			
				}
			}
		});
	}
	
	public static void refreshList(ArrayList<Card> cards)
	{
		ArrayList<Card> tempDeck = listMaker(cards);
		DefaultListModel localModel = new DefaultListModel();
		for (Card card : tempDeck) { localModel.addElement(new ListEntry(card.getName(), new ImageIcon("src/images/Quantity - " + card.getQuantity() + ".png"), card)); }
		dynamicList2.setModel(localModel);
	}
	
	public static void toolTipCards(ArrayList<Card> cards, JProgressBar bar)
	{
		String names = "<html>";
		for (Card card : cards) 
		{
			if (names.equals("<html>")) { names = names + card.getName() + "<br>"; }
			else { names = names + card.getName() + "<br>"; }
		}
		
		if (names.equals("<html>")) { names = "Empty"; }
		bar.setToolTipText(names);
		
	}
	
	public static String mailMessage(ArrayList<Card> deck)
	{
		String cards = "";
		deck.sort(deck.get(0));
		cardCounter(deck);
		for (Card card : deck)
		{
			if (card.getQuantity() == 0) {}
			else
			{
				cards = cards + card.getName() + "  x" + card.getQuantity() + "\n";
			}
		}
		
		return cards;
	}
		

}
// END CLASS CardPool_GUI_1_Stable

// Implementing ImageLabel so we can display pictures on our frames
@SuppressWarnings("serial")
class ImageLabel extends JLabel { public ImageLabel(String img) { this(new ImageIcon(img)); }
public ImageLabel(ImageIcon icon) { setIcon(icon); setIconTextGap(0); setBorder(null); setText(null); setSize(icon.getImage().getWidth(null), icon.getImage().getHeight(null)); } }

// Implementing some stuff for displaying lists better in the future, only works with view deck during the draft currently
class ListEntry {
   private String value;
   private ImageIcon icon;
   private Card card;
   public ListEntry(String value, ImageIcon icon) {
      this.value = value;
      this.icon = icon;
   }
   public ListEntry(String value, ImageIcon icon, Card card) {
	      this.value = value;
	      this.icon = icon;
	      this.card = card;
	   }
   public String getValue() {
      return value;
   }
   public ImageIcon getIcon() {
      return icon;
   }
   public Card getCard() { return card; }
   public String toString() {
      return value;
   }}

class ListEntryCellRenderer extends JLabel implements ListCellRenderer 
{ 
	private JLabel label; 
	
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) 
	{
      ListEntry entry = (ListEntry) value;
  
      setText(value.toString());
      setIcon(entry.getIcon());
      
   
      if (isSelected) {
         setBackground(list.getSelectionBackground());
         setForeground(list.getSelectionForeground());
      }
      else {
         setBackground(list.getBackground());
         setForeground(list.getForeground());
      }
  
      setEnabled(list.isEnabled());
      setFont(list.getFont());
      setOpaque(true);
  
      return this;
   }}


