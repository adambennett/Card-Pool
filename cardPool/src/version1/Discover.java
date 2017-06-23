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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.ToolTipManager;
import javax.swing.border.BevelBorder;

import org.apache.commons.lang3.text.WordUtils;

public class Discover 
{

	private static int avgScore = 0;
	private static int avgScoreUn = 0;
	private JFrame discoverInit = new JFrame();
	private JFrame discoverFrame = new JFrame();
	private JFrame threeCards = new JFrame();
	private static JFrame singleCardView = null;
	private static JList<ListEntry> dynamicList2 = new JList<ListEntry>();
	private static Card dynamicCard = new Card("init");
	private String directory = "";
	private String selectedType = "Field";
	private String selectedDest = "Hand";
	private String selectedCost = "Life Points x500";
	private String selectedEffect = "No Battle Phases";
	private String discoverEffect = "Random";
	private String discoverLocation = "Pool";
	private String discoverPlayer = "Player 1";
	private String selectedRadio = "Deck";
	@SuppressWarnings("unused")
	private String customCard = "";
	private JButton card1 = new JButton();
	private JButton card2 = new JButton();
	private JButton card3 = new JButton();
	private JButton cOneText = new JButton("Text");
	private JButton cTwoText = new JButton("Text");
	private JButton cThreeText = new JButton("Text");
	private JLabel cOneScore = new JLabel();
	private JLabel cTwoScore = new JLabel();
	private JLabel cThreeScore = new JLabel();
	private JLabel cOneName = new JLabel();
	private JLabel cTwoName = new JLabel();
	private JLabel cThreeName = new JLabel();
	private JLabel cOneRarity = new JLabel();
	private JLabel cTwoRarity = new JLabel();
	private JLabel cThreeRarity = new JLabel();
	private ImageIcon cardIcon1 = null;
	private ImageIcon cardIcon2 = null;
	private ImageIcon cardIcon3 = null;
	private Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	private ArrayList<Card> player1Discovers = new ArrayList<Card>();
	private ArrayList<Card> player2Discovers = new ArrayList<Card>();
	private static ArrayList<Card> possibleOutcomes = new ArrayList<Card>();
	private ArrayList<Card> curatedSet = new ArrayList<Card>();
	private ArrayList<Card> threeChoiceTemp = new ArrayList<Card>();
	private ImageIcon aquaI = new ImageIcon("src/images/Type - Aqua.png"); 
	private ImageIcon beastI = new ImageIcon("src/images/Type - Beast.png"); 
	private ImageIcon beastWarriorI = new ImageIcon("src/images/Type - Beast-Warrior.png"); 
	private ImageIcon dinosaurI = new ImageIcon("src/images/Type - Dinosaur.png"); 
	private ImageIcon divineI = new ImageIcon("src/images/Type - Divine-Beast.png"); 
	private ImageIcon dragonI = new ImageIcon("src/images/Type - Dragon.png");
	private ImageIcon fairyI = new ImageIcon("src/images/Type - Fairy.png"); 
	private ImageIcon fiendI = new ImageIcon("src/images/Type - Fiend.png");
	private ImageIcon fishI = new ImageIcon("src/images/Type - Fish.png"); 
	private ImageIcon insectI = new ImageIcon("src/images/Type - Insect.png"); 
	private ImageIcon machineI = new ImageIcon("src/images/Type - Machine.png"); 
	private ImageIcon plantI = new ImageIcon("src/images/Type - Plant.png"); 
	private ImageIcon psychicI = new ImageIcon("src/images/Type - Psychic.png"); 
	private ImageIcon pyroI = new ImageIcon("src/images/Type - pyro.png"); 
	private ImageIcon reptileI = new ImageIcon("src/images/Type - reptile.png");
	private ImageIcon rockI = new ImageIcon("src/images/Type - rock.png");
	private ImageIcon seaSerpentI = new ImageIcon("src/images/Type - Sea Serpent.png"); 
	private ImageIcon spellcasterI = new ImageIcon("src/images/Type - Spellcaster.png"); 
	private ImageIcon thunderI = new ImageIcon("src/images/Type - Thunder.png");
	private ImageIcon warriorI = new ImageIcon("src/images/Type - Warrior.png"); 
	private ImageIcon wingedBeastI = new ImageIcon("src/images/Type - Winged Beast.png"); 
	private ImageIcon zombieI = new ImageIcon("src/images/Type - Zombie.png"); 
	private ImageIcon darkI = new ImageIcon("src/images/Attribute - Dark.png"); 
	private ImageIcon fireI = new ImageIcon("src/images/Attribute - Fire.png"); 
	private ImageIcon earthI = new ImageIcon("src/images/Attribute - Earth.png");
	private ImageIcon lightI = new ImageIcon("src/images/Attribute - Light.png"); 
	private ImageIcon waterI = new ImageIcon("src/images/Attribute - Water.png");
	private ImageIcon windI = new ImageIcon("src/images/Attribute - Wind.png"); 
	private ImageIcon monsterI = new ImageIcon("src/images/Level Under 4 Icons.png");
	private ImageIcon oneSacI = new ImageIcon("src/images/Level 56 Icons.png"); 
	private ImageIcon twoSacI = new ImageIcon("src/images/Level 7 Icons.png"); 
	private ImageIcon spellI = new ImageIcon("src/images/Attribute - Spell.png");
	private ImageIcon trapI = new ImageIcon("src/images/Attribute - Trap.png"); 
	private ImageLabel score1I = new ImageLabel(new ImageIcon("src/images/Total Deck Score.png")); 
	private ImageLabel score2I = new ImageLabel(new ImageIcon("src/images/Total Deck Score.png")); 
	private ImageIcon refreshI = new ImageIcon("src/images/View - Reset.png"); 
	private static JLabel poolSize = new JLabel("Pool Size: ");
	private static JLabel deck1Size = new JLabel("Deck 1: ");
	private static JLabel deck2Size = new JLabel("Deck 2: ");
	private static JLabel average = new JLabel("Avg. Score: ");
	private static JLabel poolSizeNum = new JLabel("0000");
	private static JLabel d1SizeNum = new JLabel("00");
	private static JLabel d2SizeNum = new JLabel("00");
	private static JLabel avgNum = new JLabel("00");
	

	// GUI Init Stuff
	public static void main(String[] args) 
	{
		// Start GUI Stuff
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Discover window = new Discover();
					window.discoverInit.setVisible(true);

					if (window.discoverInit.isVisible() == false) { System.exit(0); }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});



	}
	public Discover() {
		initialize();

	}
	// End GUI Init Stuff



	// MAIN
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initialize() 
	{
		int noDismissDelay = Integer.MAX_VALUE;
		ToolTipManager.sharedInstance().setDismissDelay(noDismissDelay);

		// Variable Initialization
		ArrayList<Card> pool = new ArrayList<Card>();
		ArrayList<ArrayList<Card>> decks = new ArrayList<ArrayList<Card>>();
		ArrayList<Card> temp = new ArrayList<Card>();
		ArrayList<Card> temp2 = new ArrayList<Card>();
		decks.add(temp); decks.add(temp2);
		ArrayList<String> spellTypes = new ArrayList<String>(Arrays.asList("Standard", "Quickplay", "Continuous", "Field", "Equip"));
		ArrayList<String> spellTypesDynamic = new ArrayList<String>(Arrays.asList("Standard", "Quickplay", "Continuous", "Field", "Equip"));
		ArrayList<String> spellDest = new ArrayList<String>(Arrays.asList("Deck", "Activate", "Graveyard", "Banished", "Opponent's Deck", "Opponent's Hand", "Set"));
		ArrayList<String> spellDestDynamic = new ArrayList<String>(Arrays.asList("Deck", "Activate", "Graveyard", "Banished", "Opponent's Deck", "Opponent's Hand", "Set"));
		ArrayList<String> spellCost = new ArrayList<String>(Arrays.asList("None", "Life Points x1000", "Life Points x500", "Life Points x2000", "Discard 1 Card",
				"Discard 2 Cards", "Tribute 1 Monster", "Tribute 1 5*+ Monster", "Send Top 2 Cards to Graveyard", "Shuffle Deck"));
		ArrayList<String> spellCostDynamic = new ArrayList<String>(Arrays.asList("None", "Life Points x1000", "Life Points x500", "Life Points x2000", "Discard 1 Card",
				"Discard 2 Cards", "Tribute 1 Monster", "Tribute 1 5*+ Monster", "Send Top 2 Cards to Graveyard", "Shuffle Deck"));
		@SuppressWarnings("unused")
		ArrayList<String> standardSpellEffect = new ArrayList<String>(Arrays.asList("Opponent discards 2 cards", "Opponent discards 1 card", "Opponent tributes 1 monster", "Destroy all monsters", "Banish all monsters",
				"Discover a card", "Add any card from your deck to your hand", "Special Summon a monster from your Graveyard", "Special Summon a monster from any Graveyard",
				"Your opponent skips their next battle phase", "Your opponent skips their next draw phase", "Discover 2 cards", "Draw 1/Discard 2 (Opponent)", "Draw 1",
				"Draw 2", "Draw 3/Discard 1", "Draw 2/Discard 1", "Draw 1/Discard 1", "Draw 3", "Draw 1/Discard 2", "Draw 2/Discard 2", "Draw 3/Discard 2", "Draw 3/Discard 4", "Draw 2/Discard 3", "Draw 2/Discard 2 (Opponent)"));
		ArrayList<String> standardSpellEffectDynamic = new ArrayList<String>(Arrays.asList("Opponent discards 2 cards", "Opponent discards 1 card", "Opponent tributes 1 monster", "Destroy all monsters", "Banish all monsters",
				"Discover a card", "Add any card from your deck to your hand", "Special Summon a monster from your Graveyard", "Special Summon a monster from any Graveyard",
				"Your opponent skips their next battle phase", "Your opponent skips their next draw phase", "Discover 2 cards", "Draw 1/Discard 2 (Opponent)", "Draw 1",
				"Draw 2", "Draw 3/Discard 1", "Draw 2/Discard 1", "Draw 1/Discard 1", "Draw 3", "Draw 1/Discard 2", "Draw 2/Discard 2", "Draw 3/Discard 2", "Draw 3/Discard 4", "Draw 2/Discard 3", "Draw 2/Discard 2 (Opponent)"));
		@SuppressWarnings("unused")
		ArrayList<String> quickplaySpellEffect = new ArrayList<String>(Arrays.asList("Destroy 1 face-up monster", "Destroy 1 face-up spell", "Destroy 1 spell or trap", "Inflict 500 Direct Damage",
				"Inflict 1000 Direct Damage", "Inflict 1500 Direct Damage", "Inflict 2000 Direct Damage", "Inflict 600 Direct Damage", "Inflict 700 Direct Damage", "Inflict 200 Direct Damage",
				"Inflict 4000 Direct Damage", "Monster +750 ATK", "Monster +500 ATK", "Monster +200 ATK", "Monster +500 DEF", "Monster +2000 DEF", "Summon 2 0/0 Tokens", "Summon a 0/0 Token",
				"Summon 4 0/0 Tokens", "Summon 5 0/0 Tokens", "Life Points +2000", "Life Points +1000", "Life Points +8000", "Life Points +250", "Destroy 2 face-up monsters", "Destroy 1 monster", "Destroy 2 face-up spells",
				"Destroy 1 face-up trap", "Special Summon a 2* monster"));
		ArrayList<String> quickplaySpellEffectDynamic = new ArrayList<String>(Arrays.asList("Destroy 1 face-up monster", "Destroy 1 face-up spell", "Destroy 1 spell or trap", "Inflict 500 Direct Damage",
				"Inflict 1000 Direct Damage", "Inflict 1500 Direct Damage", "Inflict 2000 Direct Damage", "Inflict 600 Direct Damage", "Inflict 700 Direct Damage", "Inflict 200 Direct Damage",
				"Inflict 4000 Direct Damage", "Monster +750 ATK", "Monster +500 ATK", "Monster +200 ATK", "Monster +500 DEF", "Monster +2000 DEF", "Summon 2 0/0 Tokens", "Summon a 0/0 Token",
				"Summon 4 0/0 Tokens", "Summon 5 0/0 Tokens", "Life Points +2000", "Life Points +1000", "Life Points +8000", "Life Points +250", "Destroy 2 face-up monsters", "Destroy 1 monster", "Destroy 2 face-up spells",
				"Destroy 1 face-up trap", "Special Summon a 2* monster"));
		@SuppressWarnings("unused")
		ArrayList<String> continSpellEffect = new ArrayList<String>(Arrays.asList("Monsters +500 ATK", "Monsters +600 ATK", "Monsters +700 ATK", "Monsters +200 ATK", "Monsters +300 ATK",
				"Monsters +500 DEF", "Monsters +1000 DEF", "Monsters +600 DEF", "Monsters Level +2", "Monsters Level +3", "Monsters Level +4", "Monsters Level -1", "Monsters Level -2",
				"Your monsters cannot be targeted", "Monster Effects Negated", "Traps Negated", "Spells Negated", "No Special Summons", "No Tribute Summons", "No Card Draw",
				"Your monsters can attack directly", "Pay 500 LP to attack", "Pay 500 LP to Summon", "Pay 500 LP to Special Summon", "Pay 1000 LP to attack", "Pay 2000 LP to attack", "No Battle Phases",
				"No Standby Phases"));
		ArrayList<String> continSpellEffectDynamic = new ArrayList<String>(Arrays.asList("Monsters +500 ATK", "Monsters +600 ATK", "Monsters +700 ATK", "Monsters +200 ATK", "Monsters +300 ATK",
				"Monsters +500 DEF", "Monsters +1000 DEF", "Monsters +600 DEF", "Monsters Level +2", "Monsters Level +3", "Monsters Level +4", "Monsters Level -1", "Monsters Level -2",
				"Your monsters cannot be targeted", "Monster Effects Negated", "Traps Negated", "Spells Negated", "No Special Summons", "No Tribute Summons", "No Card Draw",
				"Your monsters can attack directly", "Pay 500 LP to attack", "Pay 500 LP to Summon", "Pay 500 LP to Special Summon", "Pay 1000 LP to attack", "Pay 2000 LP to attack", "No Battle Phases",
				"No Standby Phases"));
		@SuppressWarnings("unused")
		ArrayList<String> fieldSpellEffect = new ArrayList<String>(Arrays.asList("Monsters +500 ATK", "Monsters +600 ATK", "Monsters +700 ATK", "Monsters +200 ATK", "Monsters +300 ATK",
				"Monsters +500 DEF", "Monsters +1000 DEF", "Monsters +600 DEF", "Monsters Level +2", "Monsters Level +3", "Monsters Level +4", "Monsters Level -1", "Monsters Level -2",
				"Your monsters cannot be targeted", "Monster Effects Negated", "Traps Negated", "Spells Negated", "No Special Summons", "No Tribute Summons", "No Card Draw",
				"Your monsters can attack directly", "Pay 500 LP to attack", "Pay 500 LP to Summon", "Pay 500 LP to Special Summon", "Pay 1000 LP to attack", "Pay 2000 LP to attack", "No Battle Phases",
				"No Standby Phases"));
		ArrayList<String> fieldSpellEffectDynamic = new ArrayList<String>(Arrays.asList("Monsters +500 ATK", "Monsters +600 ATK", "Monsters +700 ATK", "Monsters +200 ATK", "Monsters +300 ATK",
				"Monsters +500 DEF", "Monsters +1000 DEF", "Monsters +600 DEF", "Monsters Level +2", "Monsters Level +3", "Monsters Level +4", "Monsters Level -1", "Monsters Level -2",
				"Your monsters cannot be targeted", "Monster Effects Negated", "Traps Negated", "Spells Negated", "No Special Summons", "No Tribute Summons", "No Card Draw",
				"Your monsters can attack directly", "Pay 500 LP to attack", "Pay 500 LP to Summon", "Pay 500 LP to Special Summon", "Pay 1000 LP to attack", "Pay 2000 LP to attack", "No Battle Phases",
				"No Standby Phases"));
		@SuppressWarnings("unused")
		ArrayList<String> equipSpellEffect = new ArrayList<String>(Arrays.asList("Gain +200 ATK", "Gain +300 ATK", "Gain +400 ATK", "Gain +500 ATK", "Gain +500 DEF", "Gain +600 DEF", "Gain +700 DEF",
				"Gain +1200 DEF", "Gain +2300 DEF", "Gain +3000 DEF", "Can attack directly", "Gain 500 LP each attack", "Draw a card each attack", "Level -2", "Level -3", "Level +5", "+500/+1500", "+1000/+500", 
				"+750/+250", "+200/+2000", "+200/+1000", "+300/+300", "+450/+500", "Destroy any monster that attack this", "Destroy any monster this attacks"));
		ArrayList<String> equipSpellEffectDynamic = new ArrayList<String>(Arrays.asList("Gain +200 ATK", "Gain +300 ATK", "Gain +400 ATK", "Gain +500 ATK", "Gain +500 DEF", "Gain +600 DEF", "Gain +700 DEF",
				"Gain +1200 DEF", "Gain +2300 DEF", "Gain +3000 DEF", "Can attack directly", "Gain 500 LP each attack", "Draw a card each attack", "Level -2", "Level -3", "Level +5", "+500/+1500", "+1000/+500", 
				"+750/+250", "+200/+2000", "+200/+1000", "+300/+300", "+450/+500", "Destroy any monster that attack this", "Destroy any monster this attacks"));
		String[] input = null;
		String line = null;	String name = null;	String attribute = null;	String type = null;
		String cardType = null;	String text = null;	String crosslimit = null;	String rarity = null;
		String synergies = null;
		int atk = 0;	int def = 0;	int lvl = 0;	int tierScore = 0;	
		int limit = 0;	int quantity = 0;	int noOfCards = 1; 
		boolean monster = false;	boolean contin = false;	boolean quickplay = false;	boolean counter = false;
		boolean field = false;	boolean equip = false;	boolean ritual = false;	boolean normal = false;
		// END Variable Init

		//GUI Element Init
		JLabel playerLbl = new JLabel("Player");
		JLabel cardLbl = new JLabel("Card");
		JLabel discoverLbl = new JLabel("Discover Effect");
		JLabel setLbl = new JLabel("Card Set");
		JLabel csTitle = new JLabel("Custom Spell Options");
		JLabel csType = new JLabel("Type");
		JLabel csDest = new JLabel("Destination");
		JLabel csCost = new JLabel("Cost");
		JLabel csEffect = new JLabel("Effect");

		/*JComboBox cardOptions = new JComboBox(); cardOptions.setModel(new DefaultComboBoxModel(new String[] {"Animal Companion", "Anti-Trap Sludge", "Battle Miner", "Call the Curators", "Card Generator", "Dragon Summoning Ritual", "Dragonfire Statue", "Drakonid Operative", "Excavator", "Firelands Portal", "Greedpot Avatar", "High Tide",
				"Infinity Dragon", "Ivory Knight", "Mad Scientist", "Mega-Excavator", "Missingno", "Monster Box", "Mulch", "Museum Curator", "Nexus-Champion Saraad", "Party at Kaiba's", "Pierre the Gambler", "Piloted Shredder", "Piloted Sky Golem", "Pool Party", "Sacrificial Pact", "Set the Stage", "Sneed's Old Shredder", "Spell Roulette",
				"Spellslinger", "The Curator", "The Legend of Exodia", "Unstable Portal", "Wrath of the Old Gods", "Yogg-Saron, Hope's End"}));
		 */
		JComboBox cardOptions = new JComboBox(); cardOptions.setModel(new DefaultComboBoxModel(new String[] {"Card Generator", "High Tide", "Missingno", "Spell Roulette"}));
		/*JComboBox discoverEffects = new JComboBox(); discoverEffects.setModel(new DefaultComboBoxModel(new String[] {"Beast", "Card Draw", "Common", "Common+", "Counter Trap", "Divine-Beast", "Dragon", "Dragon 5+", "Effect Monster", "Exodia", "Fire", "Level 1", "Level 1+", "Level 10+", "Level 10-", "Level 11+", "Level 12", "Level 2+", "Level 2-", "Level 3+", "Level 3-", "Level 4+", "Level 4-", "Level 5+", "Level 5-", "Level 6+", "Level 7-", "Level 8+", "Level 8-", "Level 9+", "Level 9-", "Monster", "Random", "Spell", "Super Rare", "Super Rare+", "Trap", "Ultimate Rare", "Ultra Rare", "Ultra Rare+", "Ultra Rare+ Monster", "Water Effect", "Water 6-", "Zombie" }));*/
		JComboBox discoverEffects = new JComboBox(); discoverEffects.setModel(new DefaultComboBoxModel(new String[] {"Effect Monster", "Random", "Spell", "Water Effect"}));
		discoverEffects.setSelectedIndex(1);
		JComboBox cardSet = new JComboBox(); cardSet.setModel(new DefaultComboBoxModel(new String[] {"Pool", "Curated Set", "Player 1 Deck", "Player 2 Deck"}));
		JComboBox player = new JComboBox(); player.setModel(new DefaultComboBoxModel(new String[] {"Player 1", "Player 2"}));
		JButton list = new JButton("List");
		JButton discover = new JButton("Discover");
		JButton gen1 = new JButton("Generate");
		JButton gen2 = new JButton("Generate"); gen2.setEnabled(false);
		JButton gen3 = new JButton("Generate"); gen3.setEnabled(false);
		JButton gen4 = new JButton("Generate"); gen4.setEnabled(false);
		JButton csType1 = new JButton("??????????"); csType1.setEnabled(false);
		JButton csType2 = new JButton("??????????"); csType2.setEnabled(false);
		JButton csType3 = new JButton("??????????"); csType3.setEnabled(false);
		JButton csDest1 = new JButton("Hand"); csDest1.setEnabled(false);
		JButton csDest2 = new JButton("??????????"); csDest2.setEnabled(false);
		JButton csDest3 = new JButton("??????????"); csDest3.setEnabled(false);
		JButton csCost1 = new JButton("??????????"); csCost1.setEnabled(false);
		JButton csCost2 = new JButton("??????????"); csCost2.setEnabled(false);
		JButton csCost3 = new JButton("??????????"); csCost3.setEnabled(false);
		JButton csEffect1 = new JButton("??????????"); csEffect1.setEnabled(false);
		JButton csEffect2 = new JButton("??????????"); csEffect2.setEnabled(false);
		JButton csEffect3 = new JButton("??????????"); csEffect3.setEnabled(false);
		JButton reset = new JButton("Reset"); reset.setEnabled(false);
		JRadioButton poolRadio = new JRadioButton("Pool");
		JRadioButton curatedRadio = new JRadioButton("Curated Set");
		JRadioButton outcomeRadio = new JRadioButton("Possibilities");
		JRadioButton deckRadio = new JRadioButton("Deck"); deckRadio.setSelected(true);
		JRadioButton discoverRadio = new JRadioButton("Discovered");
		// END GUI Element Init

		// Database Setup
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File("C:\\Users\\Adam\\Documents\\YGO Draft Decks"));
		chooser.setDialogTitle("select folder");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);

		chooser.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				directory = chooser.getSelectedFile().toString();
				File dir = new File(directory);
				File[] directoryListing = dir.listFiles();
				if (directoryListing != null) {
					for (File child : directoryListing) 
					{
						if (child.getName().equals("Pool.txt"))
						{
							readDatabase(noOfCards, line, input, name, attribute, type, cardType, atk, def, tierScore, lvl, quantity, limit, crosslimit, rarity, text, synergies, monster, contin, quickplay, counter, field, equip, ritual, normal, pool, child.getAbsolutePath());		
						}

						else if (child.getName().equals("Deck 1.txt"))
						{
							readDatabase(noOfCards, line, input, name, attribute, type, cardType, atk, def, tierScore, lvl, quantity, limit, crosslimit, rarity, text, synergies, monster, contin, quickplay, counter, field, equip, ritual, normal, decks.get(0), child.getAbsolutePath());	
						}

						else if (child.getName().equals("Deck 2.txt"))
						{
							readDatabase(noOfCards, line, input, name, attribute, type, cardType, atk, def, tierScore, lvl, quantity, limit, crosslimit, rarity, text, synergies, monster, contin, quickplay, counter, field, equip, ritual, normal, decks.get(1), child.getAbsolutePath());	
						}
					}	
				}
				
				Integer psN = pool.size();
				String psnString = psN.toString();
				poolSizeNum.setText(psnString);
				
				
				for (Card card : pool)
				{
					if (card.getSynergies().contains("curated"))
					{
						if (curatedSet.size() == 0) { curatedSet.add(card); }
						for (Card card2 : curatedSet)
						{
							if (card.getName().equals(card2.getName()))
							{
								
							}
							else { curatedSet.add(card); }
						}
						
					}
				}
				
				discover.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e) 
					{
						if (discoverEffect.equals("Random"))
						{
							threeChoiceTemp = discoverCard(pool);
						}

						else if (discoverEffect.equals("Monster"))
						{
							threeChoiceTemp = discoverMonster(pool);
						}

						else if (discoverEffect.equals("Spell") || discoverEffect.equals("Trap"))
						{
							threeChoiceTemp = discoverCardType(pool, discoverEffect);
						}

						else if (discoverEffect.equals("Water Effect"))
						{
							threeChoiceTemp = discoverAttribType(pool, "Effect Monster", "Water");
						}

						else if (discoverEffect.equals("Effect Monster"))
						{
							threeChoiceTemp = discoverCardType(pool, "Effect Monster");
						}
						
						else if (discoverEffect.equals("Beast"))
						{
							threeChoiceTemp = discoverMonsterType(pool, "Beast");
						}
						
						else if (discoverEffect.equals("Level 11-"))
						{
							threeChoiceTemp = discoverLevelLower(pool, 12);
						}
						
						else if (discoverEffect.equals("Level 10-"))
						{
							threeChoiceTemp = discoverLevelLower(pool, 11);
						}
						
						else if (discoverEffect.equals("Level 9-"))
						{
							threeChoiceTemp = discoverLevelLower(pool, 10);
						}
						
						else if (discoverEffect.equals("Level 8-"))
						{
							threeChoiceTemp = discoverLevelLower(pool, 9);
						}
						
						else if (discoverEffect.equals("Level 7-"))
						{
							threeChoiceTemp = discoverLevelLower(pool, 8);
						}
						
						else if (discoverEffect.equals("Level 6-"))
						{
							threeChoiceTemp = discoverLevelLower(pool, 7);
						}
						
						else if (discoverEffect.equals("Level 5-"))
						{
							threeChoiceTemp = discoverLevelLower(pool, 6);
						}
						
						else if (discoverEffect.equals("Level 4-"))
						{
							threeChoiceTemp = discoverLevelLower(pool, 5);
						}
						
						else if (discoverEffect.equals("Level 3-"))
						{
							threeChoiceTemp = discoverLevelLower(pool, 4);
						}
						
						else if (discoverEffect.equals("Level 2-"))
						{
							threeChoiceTemp = discoverLevelLower(pool, 3);
						}
						
						else if (discoverEffect.equals("Level 1"))
						{
							threeChoiceTemp = discoverLevelLower(pool, 2);
						}
						
						else if (discoverEffect.equals("Level 1+"))
						{
							threeChoiceTemp = discoverLevelHigher(pool, 0);
						}
						
						else if (discoverEffect.equals("Level 2+"))
						{
							threeChoiceTemp = discoverLevelHigher(pool, 1);
						}
						
						else if (discoverEffect.equals("Level 3+"))
						{
							threeChoiceTemp = discoverLevelHigher(pool, 2);
						}
						
						else if (discoverEffect.equals("Level 4+"))
						{
							threeChoiceTemp = discoverLevelHigher(pool, 3);
						}
						
						else if (discoverEffect.equals("Level 5+"))
						{
							threeChoiceTemp = discoverLevelHigher(pool, 4);
						}
						
						else if (discoverEffect.equals("Level 6+"))
						{
							threeChoiceTemp = discoverLevelHigher(pool, 5);
						}
						
						else if (discoverEffect.equals("Level 7+"))
						{
							threeChoiceTemp = discoverLevelHigher(pool, 6);
						}
						
						else if (discoverEffect.equals("Level 8+"))
						{
							threeChoiceTemp = discoverLevelHigher(pool, 7);
						}
						
						else if (discoverEffect.equals("Level 9+"))
						{
							threeChoiceTemp = discoverLevelHigher(pool, 8);
						}
						
						else if (discoverEffect.equals("Level 10+"))
						{
							threeChoiceTemp = discoverLevelHigher(pool, 9);
						}
						
						else if (discoverEffect.equals("Level 11+"))
						{
							threeChoiceTemp = discoverLevelHigher(pool, 10);
						}
						
						else if (discoverEffect.equals("Level 12"))
						{
							threeChoiceTemp = discoverLevelHigher(pool, 11);
						}



						if (isImage("src/images/" + threeChoiceTemp.get(0).getName() + "Small.png")) { cardIcon1 = new ImageIcon("src/images/" + threeChoiceTemp.get(0).getName() + "Small.png");}
						else { cardIcon1 = new ImageIcon("src/images/" + threeChoiceTemp.get(0).getName() + ".png"); }
						card1.setIcon(cardIcon1);


						if (isImage("src/images/" + threeChoiceTemp.get(1).getName() + "Small.png")) { cardIcon2 = new ImageIcon("src/images/" + threeChoiceTemp.get(1).getName() + "Small.png");}
						else { cardIcon2 = new ImageIcon("src/images/" + threeChoiceTemp.get(1).getName() + ".png"); }
						card2.setIcon(cardIcon2);


						if (isImage("src/images/" + threeChoiceTemp.get(2).getName() + "Small.png")) { cardIcon3 = new ImageIcon("src/images/" + threeChoiceTemp.get(2).getName() + "Small.png");}
						else { cardIcon3 = new ImageIcon("src/images/" + threeChoiceTemp.get(2).getName() + ".png"); }
						card3.setIcon(cardIcon3);		

						card1.setBorder(BorderFactory.createEmptyBorder()); card1.setContentAreaFilled(false); card1.setMargin(new Insets(0, 0, 0, 0));
						card2.setBorder(BorderFactory.createEmptyBorder()); card2.setContentAreaFilled(false); card2.setMargin(new Insets(0, 0, 0, 0));
						card3.setBorder(BorderFactory.createEmptyBorder()); card3.setContentAreaFilled(false); card3.setMargin(new Insets(0, 0, 0, 0));

						cOneRarity.setText(threeChoiceTemp.get(0).getRarity());
						cTwoRarity.setText(threeChoiceTemp.get(1).getRarity());
						cThreeRarity.setText(threeChoiceTemp.get(2).getRarity());
						textColor(threeChoiceTemp.get(0), cOneRarity);
						textColor(threeChoiceTemp.get(1), cTwoRarity);
						textColor(threeChoiceTemp.get(2), cThreeRarity);

						cOneScore.setText(String.valueOf(threeChoiceTemp.get(0).getTierScore()));
						cTwoScore.setText(String.valueOf(threeChoiceTemp.get(1).getTierScore()));
						cThreeScore.setText(String.valueOf(threeChoiceTemp.get(2).getTierScore()));

						cOneName.setText(threeChoiceTemp.get(0).getName());
						cTwoName.setText(threeChoiceTemp.get(1).getName());
						cThreeName.setText(threeChoiceTemp.get(2).getName());

						threeCards.pack();
						threeCards.setLocation(dim.width/2-threeCards.getSize().width/2, dim.height/2-threeCards.getSize().height/2);
						threeCards.setVisible(true);
					}

				});

				list.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent arg0) 
					{
						JFrame cardViewLocal = new JFrame();	
						cardViewLocal.setTitle(discoverPlayer + "'s Deck");
						cardViewLocal.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
						cardViewLocal.setBounds(100, 100, 496, 443);
						cardViewLocal.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
						cardViewLocal.setResizable(false);
						cardViewLocal.setAlwaysOnTop(true);
						DefaultListModel viewDeckModel = new DefaultListModel();
						ArrayList<Card> drafted = new ArrayList<Card>();
						int cardsToDraftLocal = 100;
						if (discoverPlayer.equals("Player 1")) 
						{ 
							if (selectedRadio.equals("Discovered")) { copyPoolVoid(player1Discovers, drafted); }
							else if (selectedRadio.equals("Deck")) { copyPoolVoid(decks.get(0), drafted); }
							else if (selectedRadio.equals("Curated Set")) { copyPoolVoid(curatedSet, drafted); }
							else if (selectedRadio.equals("Pool")) { copyPoolVoid(pool, drafted); cardsToDraftLocal = 2500; }
							else if (selectedRadio.equals("Possibilities")) { copyPoolVoid(possibleOutcomes, drafted); cardsToDraftLocal = 2500; }
							if (drafted.size() > 0) { listEntryMaker(drafted, viewDeckModel); }
						}
						
						else if (discoverPlayer.equals("Player 2")) 
						{ 
							if (selectedRadio.equals("Discovered")) { copyPoolVoid(player2Discovers, drafted); cardsToDraftLocal = 100; }
							else if (selectedRadio.equals("Deck")) { copyPoolVoid(decks.get(1), drafted); cardsToDraftLocal = 100; }
							else if (selectedRadio.equals("Curated Set")) { copyPoolVoid(curatedSet, drafted); cardsToDraftLocal = 100; }
							else if (selectedRadio.equals("Pool")) { copyPoolVoid(pool, drafted);  cardsToDraftLocal = 2500;}
							else if (selectedRadio.equals("Possibilities")) { copyPoolVoid(possibleOutcomes, drafted); cardsToDraftLocal = 2500; }
							if (drafted.size() > 0) { listEntryMaker(drafted, viewDeckModel); }
						}
						
						dynamicList2 = new JList(viewDeckModel);
						dynamicList2.setCellRenderer(new ListEntryCellRenderer());

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


						aquaBtn.setToolTipText("Aqua"); aquaBtn.setIcon(aquaI);
						beastBtn.setToolTipText("Beast"); beastBtn.setIcon(beastI);
						beastWarriorBtn.setToolTipText("Beast-Warrior"); beastWarriorBtn.setIcon(beastWarriorI);
						dinosaurBtn.setToolTipText("Dinosaur"); dinosaurBtn.setIcon(dinosaurI);
						divineBtn.setToolTipText("Divine"); divineBtn.setIcon(divineI);
						dragonBtn.setToolTipText("Dragon"); dragonBtn.setIcon(dragonI);
						fairyBtn.setToolTipText("Fairy"); fairyBtn.setIcon(fairyI);
						fiendBtn.setToolTipText("Fiend"); fiendBtn.setIcon(fiendI);
						fishBtn.setToolTipText("Fish"); fishBtn.setIcon(fishI);
						insectBtn.setToolTipText("Insect"); insectBtn.setIcon(insectI);
						machineBtn.setToolTipText("Machine"); machineBtn.setIcon(machineI);
						plantBtn.setToolTipText("Plant"); plantBtn.setIcon(plantI);
						psychicBtn.setToolTipText("Psychic"); psychicBtn.setIcon(psychicI);
						pyroBtn.setToolTipText("Pyro"); pyroBtn.setIcon(pyroI);
						reptileBtn.setToolTipText("Reptile"); reptileBtn.setIcon(reptileI);
						rockBtn.setToolTipText("Rock"); rockBtn.setIcon(rockI);
						seaSerpentBtn.setToolTipText("Sea Serpent"); seaSerpentBtn.setIcon(seaSerpentI);
						spellcasterBtn.setToolTipText("Spellcaster"); spellcasterBtn.setIcon(spellcasterI);
						thunderBtn.setToolTipText("Thunder"); thunderBtn.setIcon(thunderI);
						warriorBtn.setToolTipText("Warrior"); warriorBtn.setIcon(warriorI);
						wingedBeastBtn.setToolTipText("Winged Beast"); wingedBeastBtn.setIcon(wingedBeastI);
						zombieBtn.setToolTipText("Zombie"); zombieBtn.setIcon(zombieI);
						darkBtn.setToolTipText("Dark"); darkBtn.setIcon(darkI);
						fireBtn.setToolTipText("Fire"); fireBtn.setIcon(fireI);
						earthBtn.setToolTipText("Earth"); earthBtn.setIcon(earthI);
						lightBtn.setToolTipText("Light"); lightBtn.setIcon(lightI);
						waterBtn.setToolTipText("water"); waterBtn.setIcon(waterI);
						windBtn.setToolTipText("Wind"); windBtn.setIcon(windI);
						monsterBtn.setToolTipText("No Tribute"); monsterBtn.setIcon(monsterI);
						oneSacBtn.setToolTipText("One Tribute"); oneSacBtn.setIcon(oneSacI);
						twoSacBtn.setToolTipText("Two Tribute"); twoSacBtn.setIcon(twoSacI);
						spellBtn.setToolTipText("Spell"); spellBtn.setIcon(spellI);
						trapBtn.setToolTipText("Trap"); trapBtn.setIcon(trapI);
						score1I.setToolTipText("Total Deck Score");
						score2I.setToolTipText("Average Card Score");
						fullListBtn.setToolTipText("Full Deck"); fullListBtn.setIcon(refreshI);


						JPanel panel = new JPanel();
						panel.setLayout(new GridBagLayout());
						GridBagConstraints c = new GridBagConstraints();
						c.fill = GridBagConstraints.BOTH;
						c.weightx = 1.0;

						JLabel typeLbl = new JLabel("Types");
						JLabel attributeLbl = new JLabel("Attributes");

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
						JProgressBar medLvlProgress = new JProgressBar(0, (cardsToDraftLocal - 1)); medLvlProgress.setValue(medLvlCount); medLvlProgress.setStringPainted(true);
						medLvlProgress.setString(String.valueOf(medLvlCount));
						toolTipCards(medLvlCards, medLvlProgress);
						JProgressBar highLvlProgress = new JProgressBar(0, (cardsToDraftLocal - 1)); highLvlProgress.setValue(highLvlCount); highLvlProgress.setStringPainted(true);
						highLvlProgress.setString(String.valueOf(highLvlCount));
						toolTipCards(highLvlCards, highLvlProgress);




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
						c.insets = new Insets(0, 5, 10, 5);
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

				card1.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						if (discoverPlayer.equals("Player 1"))
						{
							player1Discovers.add(threeChoiceTemp.get(0));
							threeCards.setVisible(false);
						}
						
						else 
						{
							player2Discovers.add(threeChoiceTemp.get(0));
							threeCards.setVisible(false);
						}
					}						
				});
				
				card2.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						if (discoverPlayer.equals("Player 1"))
						{
							player1Discovers.add(threeChoiceTemp.get(1));
							threeCards.setVisible(false);
						}
						
						else 
						{
							player2Discovers.add(threeChoiceTemp.get(1));
							threeCards.setVisible(false);
						}
					}						
				});
				
				card3.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						if (discoverPlayer.equals("Player 1"))
						{
							player1Discovers.add(threeChoiceTemp.get(2));
							threeCards.setVisible(false);
						}
						
						else 
						{
							player2Discovers.add(threeChoiceTemp.get(2));
							threeCards.setVisible(false);
						}
					}						
				});
				
				
				discoverInit.setVisible(false);
				discoverFrame.pack();
				discoverFrame.setLocation(dim.width/2-discoverFrame.getSize().width/2, dim.height/2-discoverFrame.getSize().height/2);
				discoverFrame.setVisible(true);

			}
		});

		// GUI Setup
		discoverInit = new JFrame();	
		discoverInit.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
		discoverInit.setBounds(600, 600, 275, 200);
		discoverInit.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		discoverInit.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		discoverInit.setTitle("Draft Directory Selection");
		discoverInit.getContentPane().add(chooser);
		discoverInit.setResizable(false);
		discoverInit.pack();
		discoverInit.setLocation(dim.width/2-discoverInit.getSize().width/2, dim.height/2-discoverInit.getSize().height/2);

		discoverFrame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
		discoverFrame.setBounds(600, 600, 275, 200);
		discoverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		discoverFrame.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		discoverFrame.setTitle("Yu-Gi-Oh! Discover Menu");
		discoverFrame.setVisible(false);

		threeCards.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
		threeCards.setBounds(600, 600, 275, 200);
		threeCards.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		threeCards.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		threeCards.setTitle("Choose one");
		threeCards.setResizable(false);
		threeCards.setVisible(false);

		JPanel left = new JPanel();
		JPanel right = new JPanel();
		JPanel discoverPanel = new JPanel();
		
		right.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED)); 
		left.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED)); 

		discoverFrame.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.insets = new Insets(5,5,5,5); 

		c.gridx = 0; c.gridy = 0; discoverFrame.add(left, c);
		c.gridx = 1; c.gridy = 0; discoverFrame.add(right, c);
		c.gridx = 0; c.gridy = 0; threeCards.add(discoverPanel, c);

		left.setLayout(new GridBagLayout());
		GridBagConstraints c2 = new GridBagConstraints();
		c2.fill = GridBagConstraints.HORIZONTAL;
		c2.insets = new Insets(5,5,5,5); 
		c2.weightx = 0.5;
		c2.gridx = 0; c2.gridy = 0; left.add(playerLbl, c2);
		c2.gridx = 1; c2.gridy = 0; left.add(player, c2);
		c2.gridx = 0; c2.gridy = 1; left.add(cardLbl, c2);
		c2.gridx = 0; c2.gridy = 2; left.add(discoverLbl, c2);
		c2.gridx = 0; c2.gridy = 3; left.add(setLbl, c2);
		c2.gridx = 1; c2.gridy = 1; left.add(cardOptions, c2);
		c2.gridx = 1; c2.gridy = 2; left.add(discoverEffects, c2);
		c2.gridx = 1; c2.gridy = 3; left.add(cardSet, c2);
		c2.gridx = 0; c2.gridy = 4; left.add(list, c2);
		c2.gridx = 1; c2.gridy = 4; c2.gridheight = 2; left.add(discover, c2);
		c2.gridheight = 1; c2.gridx = 1; c2.gridy = 6; left.add(poolSize, c2);
		c2.gridx = 1; c2.gridy = 7; left.add(deck1Size, c2);
		c2.gridx = 1; c2.gridy = 8; left.add(deck2Size, c2);
		c2.gridx = 1; c2.gridy = 9; left.add(average, c2);
		c2.gridx = 2; c2.gridy = 6; left.add(poolSizeNum, c2);
		c2.gridx = 2; c2.gridy = 7; left.add(d1SizeNum, c2);
		c2.gridx = 2; c2.gridy = 8; left.add(d2SizeNum, c2);
		c2.gridx = 2; c2.gridy = 9; left.add(avgNum, c2);
		c2.insets = new Insets(5, 30, 5, 40);
		c2.gridx = 0; c2.gridy = 6; left.add(poolRadio, c2);
		c2.gridx = 0; c2.gridy = 7; left.add(curatedRadio, c2);
		c2.gridx = 0; c2.gridy = 8; left.add(outcomeRadio, c2);
		c2.gridx = 0; c2.gridy = 9; left.add(deckRadio, c2);
		c2.gridx = 0; c2.gridy = 10; left.add(discoverRadio, c2);

		right.setLayout(new GridBagLayout());
		GridBagConstraints c3 = new GridBagConstraints();
		c3.fill = GridBagConstraints.HORIZONTAL;
		c3.insets = new Insets(5,10,5,10); 
		c3.weightx = 1.0;
		c.gridx = 0; c.gridy = 0; right.add(csTitle, c3);
		c3.weightx = 0.5;
		c3.gridx = 0; c3.gridy = 1; right.add(csType, c3);
		c3.gridx = 0; c3.gridy = 2; right.add(csDest, c3);
		c3.gridx = 0; c3.gridy = 3; right.add(csCost, c3);
		c3.gridx = 0; c3.gridy = 4; right.add(csEffect, c3);
		c3.gridx = 1; c3.gridy = 1; right.add(gen1, c3);
		c3.gridx = 1; c3.gridy = 2; right.add(gen2, c3);
		c3.gridx = 1; c3.gridy = 3; right.add(gen3, c3);
		c3.gridx = 1; c3.gridy = 4; right.add(gen4, c3);
		c3.gridx = 2; c3.gridy = 1; right.add(csType1, c3);
		c3.gridx = 3; c3.gridy = 1; right.add(csType2, c3);
		c3.gridx = 4; c3.gridy = 1; right.add(csType3, c3);
		c3.gridx = 2; c3.gridy = 2; right.add(csDest1, c3);
		c3.gridx = 3; c3.gridy = 2; right.add(csDest2, c3);
		c3.gridx = 4; c3.gridy = 2; right.add(csDest3, c3);
		c3.gridx = 2; c3.gridy = 3; right.add(csCost1, c3);
		c3.gridx = 3; c3.gridy = 3; right.add(csCost2, c3);
		c3.gridx = 4; c3.gridy = 3; right.add(csCost3, c3);
		c3.gridx = 2; c3.gridy = 4; right.add(csEffect1, c3);
		c3.gridx = 3; c3.gridy = 4; right.add(csEffect2, c3);
		c3.gridx = 4; c3.gridy = 4; right.add(csEffect3, c3);
		c3.gridx = 3; c3.gridy = 5; right.add(reset, c3);

		discoverPanel.setLayout(new GridBagLayout());
		GridBagConstraints c4 = new GridBagConstraints();
		c4.fill = GridBagConstraints.HORIZONTAL;
		c4.insets = new Insets(5,10,5,10); 
		c4.weightx = 0.5;
		c4.gridx = 0; c4.gridy = 0; discoverPanel.add(cOneName, c4); cOneName.setHorizontalAlignment(SwingConstants.CENTER);
		c4.gridx = 1; c4.gridy = 0; discoverPanel.add(cTwoName, c4);  cTwoName.setHorizontalAlignment(SwingConstants.CENTER);
		c4.gridx = 2; c4.gridy = 0; discoverPanel.add(cThreeName, c4);  cThreeName.setHorizontalAlignment(SwingConstants.CENTER);
		c4.gridx = 0; c4.gridy = 2; discoverPanel.add(card1, c4);
		c4.gridx = 1; c4.gridy = 2; discoverPanel.add(card2, c4);
		c4.gridx = 2; c4.gridy = 2; discoverPanel.add(card3, c4);
		c4.gridx = 0; c4.gridy = 3; discoverPanel.add(cOneScore, c4);	 cOneScore.setHorizontalAlignment(SwingConstants.CENTER);
		c4.gridx = 1; c4.gridy = 3; discoverPanel.add(cTwoScore, c4);	 cTwoScore.setHorizontalAlignment(SwingConstants.CENTER);
		c4.gridx = 2; c4.gridy = 3; discoverPanel.add(cThreeScore, c4);	 cThreeScore.setHorizontalAlignment(SwingConstants.CENTER);
		c4.gridx = 0; c4.gridy = 4; discoverPanel.add(cOneRarity, c4);	 cOneRarity.setHorizontalAlignment(SwingConstants.CENTER);
		c4.gridx = 1; c4.gridy = 4; discoverPanel.add(cTwoRarity, c4);	 cTwoRarity.setHorizontalAlignment(SwingConstants.CENTER);
		c4.gridx = 2; c4.gridy = 4; discoverPanel.add(cThreeRarity, c4);	 cThreeRarity.setHorizontalAlignment(SwingConstants.CENTER);
		c4.insets = new Insets(5,180,0,180);
		c4.gridx = 0; c4.gridy = 1; discoverPanel.add(cOneText, c4);	 cOneText.setHorizontalAlignment(SwingConstants.CENTER);
		c4.gridx = 1; c4.gridy = 1; discoverPanel.add(cTwoText, c4);	cTwoText.setHorizontalAlignment(SwingConstants.CENTER);
		c4.gridx = 2; c4.gridy = 1; discoverPanel.add(cThreeText, c4);	cThreeText.setHorizontalAlignment(SwingConstants.CENTER);


		gen1.addActionListener(new ActionListener() 
		{

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				csType1.setBorderPainted(true);
				csType2.setBorderPainted(true);
				csType3.setBorderPainted(true);
				Random seed = new Random();
				int value = seed.nextInt(spellTypesDynamic.size());
				String type1 = spellTypesDynamic.get(value);
				spellTypesDynamic.remove(value);
				value = seed.nextInt(spellTypesDynamic.size());
				String type2 = spellTypesDynamic.get(value);
				spellTypesDynamic.remove(value);
				value = seed.nextInt(spellTypesDynamic.size());
				String type3 = spellTypesDynamic.get(value);

				csType1.setText(type1);
				csType2.setText(type2);
				csType3.setText(type3);
				csType1.setEnabled(true);
				csType2.setEnabled(true);
				csType3.setEnabled(true);
				gen1.setEnabled(false);
				copyPoolVoidString(spellTypes, spellTypesDynamic);
			}

		});

		gen2.addActionListener(new ActionListener() 
		{

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				csDest1.setBorderPainted(true);
				csDest2.setBorderPainted(true);
				csDest3.setBorderPainted(true);
				Random seed = new Random();
				int value = seed.nextInt(spellDestDynamic.size());
				String type2 = spellDestDynamic.get(value);
				spellDestDynamic.remove(value);
				value = seed.nextInt(spellDestDynamic.size());
				String type3 = spellDestDynamic.get(value);

				csDest2.setText(type2);
				csDest3.setText(type3);
				csDest1.setEnabled(true);
				csDest2.setEnabled(true);
				csDest3.setEnabled(true);
				gen2.setEnabled(false);
				copyPoolVoidString(spellDest, spellDestDynamic);
			}

		});

		gen3.addActionListener(new ActionListener() 
		{

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				csCost1.setBorderPainted(true);
				csCost2.setBorderPainted(true);
				csCost3.setBorderPainted(true);
				Random seed = new Random();
				int value = seed.nextInt(spellCostDynamic.size());
				String type1 = spellCostDynamic.get(value);
				spellCostDynamic.remove(value);
				value = seed.nextInt(spellCostDynamic.size());
				String type2 = spellCostDynamic.get(value);
				spellCostDynamic.remove(value);
				value = seed.nextInt(spellCostDynamic.size());
				String type3 = spellCostDynamic.get(value);

				csCost1.setText(type1);
				csCost2.setText(type2);
				csCost3.setText(type3);
				csCost1.setEnabled(true);
				csCost2.setEnabled(true);
				csCost3.setEnabled(true);
				gen3.setEnabled(false);
				copyPoolVoidString(spellCost, spellCostDynamic);
			}

		});

		gen4.addActionListener(new ActionListener() 
		{

			@Override
			public void actionPerformed(ActionEvent e) 
			{

				if (selectedType.equals("Field"))
				{
					csEffect1.setBorderPainted(true);
					csEffect2.setBorderPainted(true);
					csEffect3.setBorderPainted(true);
					Random seed = new Random();
					int value = seed.nextInt(fieldSpellEffectDynamic.size());
					String type1 = fieldSpellEffectDynamic.get(value);
					fieldSpellEffectDynamic.remove(value);
					value = seed.nextInt(fieldSpellEffectDynamic.size());
					String type2 = fieldSpellEffectDynamic.get(value);
					fieldSpellEffectDynamic.remove(value);
					value = seed.nextInt(fieldSpellEffectDynamic.size());
					String type3 = fieldSpellEffectDynamic.get(value);

					csEffect1.setText(type1);
					csEffect2.setText(type2);
					csEffect3.setText(type3);
					csEffect1.setEnabled(true);
					csEffect2.setEnabled(true);
					csEffect3.setEnabled(true);
					gen4.setEnabled(false);
					copyPoolVoidString(spellCost, spellCostDynamic);
				}

				else if (selectedType.equals("Standard"))
				{
					Random seed = new Random();
					int value = seed.nextInt(standardSpellEffectDynamic.size());
					String type1 = standardSpellEffectDynamic.get(value);
					standardSpellEffectDynamic.remove(value);
					value = seed.nextInt(standardSpellEffectDynamic.size());
					String type2 = standardSpellEffectDynamic.get(value);
					standardSpellEffectDynamic.remove(value);
					value = seed.nextInt(standardSpellEffectDynamic.size());
					String type3 = standardSpellEffectDynamic.get(value);

					csEffect1.setText(type1);
					csEffect2.setText(type2);
					csEffect3.setText(type3);
					csEffect1.setEnabled(true);
					csEffect2.setEnabled(true);
					csEffect3.setEnabled(true);
					gen4.setEnabled(false);
					copyPoolVoidString(spellCost, spellCostDynamic);
				}

				else if (selectedType.equals("Continuous"))
				{
					Random seed = new Random();
					int value = seed.nextInt(continSpellEffectDynamic.size());
					String type1 = continSpellEffectDynamic.get(value);
					continSpellEffectDynamic.remove(value);
					value = seed.nextInt(continSpellEffectDynamic.size());
					String type2 = continSpellEffectDynamic.get(value);
					continSpellEffectDynamic.remove(value);
					value = seed.nextInt(continSpellEffectDynamic.size());
					String type3 = continSpellEffectDynamic.get(value);

					csEffect1.setText(type1);
					csEffect2.setText(type2);
					csEffect3.setText(type3);
					csEffect1.setEnabled(true);
					csEffect2.setEnabled(true);
					csEffect3.setEnabled(true);
					gen4.setEnabled(false);
					copyPoolVoidString(spellCost, spellCostDynamic);
				}

				else if (selectedType.equals("Equip"))
				{
					Random seed = new Random();
					int value = seed.nextInt(equipSpellEffectDynamic.size());
					String type1 = equipSpellEffectDynamic.get(value);
					equipSpellEffectDynamic.remove(value);
					value = seed.nextInt(equipSpellEffectDynamic.size());
					String type2 = equipSpellEffectDynamic.get(value);
					equipSpellEffectDynamic.remove(value);
					value = seed.nextInt(equipSpellEffectDynamic.size());
					String type3 = equipSpellEffectDynamic.get(value);

					csEffect1.setText(type1);
					csEffect2.setText(type2);
					csEffect3.setText(type3);
					csEffect1.setEnabled(true);
					csEffect2.setEnabled(true);
					csEffect3.setEnabled(true);
					gen4.setEnabled(false);
					copyPoolVoidString(spellCost, spellCostDynamic);
				}

				else if (selectedType.equals("Quickplay"))
				{
					Random seed = new Random();
					int value = seed.nextInt(quickplaySpellEffectDynamic.size());
					String type1 = quickplaySpellEffectDynamic.get(value);
					quickplaySpellEffectDynamic.remove(value);
					value = seed.nextInt(quickplaySpellEffectDynamic.size());
					String type2 = quickplaySpellEffectDynamic.get(value);
					quickplaySpellEffectDynamic.remove(value);
					value = seed.nextInt(quickplaySpellEffectDynamic.size());
					String type3 = quickplaySpellEffectDynamic.get(value);

					csEffect1.setText(type1);
					csEffect2.setText(type2);
					csEffect3.setText(type3);
					csEffect1.setEnabled(true);
					csEffect2.setEnabled(true);
					csEffect3.setEnabled(true);
					gen4.setEnabled(false);
					copyPoolVoidString(spellCost, spellCostDynamic);
				}




			}

		});

		csType1.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				csType1.setEnabled(false);
				csType2.setBorderPainted(false);
				csType3.setBorderPainted(false);
				csType2.setEnabled(false);
				csType3.setEnabled(false);
				selectedType = csType1.getText();
				gen2.setEnabled(true);
			}

		});

		csType2.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				csType1.setEnabled(false);
				csType1.setBorderPainted(false);
				csType3.setBorderPainted(false);
				csType2.setEnabled(false);
				csType3.setEnabled(false);
				selectedType = csType2.getText();
				gen2.setEnabled(true);
			}

		});

		csType3.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				csType1.setEnabled(false);
				csType2.setBorderPainted(false);
				csType1.setBorderPainted(false);
				csType2.setEnabled(false);
				csType3.setEnabled(false);
				selectedType = csType3.getText();
				gen2.setEnabled(true);
			}

		});

		csDest1.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				csDest2.setBorderPainted(false);
				csDest3.setBorderPainted(false);
				csDest1.setEnabled(false);
				csDest2.setEnabled(false);
				csDest3.setEnabled(false);
				gen3.setEnabled(true);
				selectedDest = csDest1.getText();
			}

		});

		csDest2.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				csDest1.setBorderPainted(false);
				csDest3.setBorderPainted(false);
				csDest1.setEnabled(false);
				csDest2.setEnabled(false);
				csDest3.setEnabled(false);
				gen3.setEnabled(true);
				selectedDest = csDest2.getText();
			}

		});

		csDest3.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				csDest2.setBorderPainted(false);
				csDest1.setBorderPainted(false);
				csDest1.setEnabled(false);
				csDest2.setEnabled(false);
				csDest3.setEnabled(false);
				gen3.setEnabled(true);
				selectedDest = csDest3.getText();
			}

		});

		csCost1.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				csCost2.setBorderPainted(false);
				csCost3.setBorderPainted(false);
				csCost1.setEnabled(false);
				csCost2.setEnabled(false);
				csCost3.setEnabled(false);
				gen4.setEnabled(true);
				selectedCost = csCost1.getText();
			}

		});

		csCost2.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				csCost1.setBorderPainted(false);
				csCost3.setBorderPainted(false);
				csCost1.setEnabled(false);
				csCost2.setEnabled(false);
				csCost3.setEnabled(false);
				gen4.setEnabled(true);
				selectedCost = csCost2.getText();
			}

		});

		csCost3.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				csCost2.setBorderPainted(false);
				csCost1.setBorderPainted(false);
				csCost1.setEnabled(false);
				csCost2.setEnabled(false);
				csCost3.setEnabled(false);
				gen4.setEnabled(true);
				selectedCost = csCost3.getText();
			}

		});

		csEffect1.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				csEffect2.setBorderPainted(false);
				csEffect3.setBorderPainted(false);
				csEffect1.setEnabled(false);
				csEffect2.setEnabled(false);
				csEffect3.setEnabled(false);
				reset.setEnabled(true);
				selectedEffect = csEffect1.getText();

				customCard = selectedType + " Spell that goes to your " + selectedDest + " with the effect: " + selectedEffect + ". To activate this spell you must " + selectedCost;

			}

		});

		csEffect2.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				csEffect1.setBorderPainted(false);
				csEffect3.setBorderPainted(false);
				csEffect1.setEnabled(false);
				csEffect2.setEnabled(false);
				csEffect3.setEnabled(false);
				reset.setEnabled(true);
				selectedEffect = csEffect2.getText();
				customCard = selectedType + " Spell that goes to your " + selectedDest + " with the effect: " + selectedEffect + ". To activate this spell you must " + selectedCost;

			}

		});

		csEffect3.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				csEffect2.setBorderPainted(false);
				csEffect1.setBorderPainted(false);
				csEffect1.setEnabled(false);
				csEffect2.setEnabled(false);
				csEffect3.setEnabled(false);
				reset.setEnabled(true);
				selectedEffect = csEffect3.getText();
				customCard = selectedType + " Spell that goes to your " + selectedDest + " with the effect: " + selectedEffect + ". To activate this spell you must " + selectedCost;
			}

		});

		reset.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				gen1.setEnabled(true);		
				csType1.setBorderPainted(true);
				csType2.setBorderPainted(true);
				csType3.setBorderPainted(true);
				csCost1.setBorderPainted(true);
				csCost2.setBorderPainted(true);
				csCost3.setBorderPainted(true);
				csDest1.setBorderPainted(true);
				csDest2.setBorderPainted(true);
				csDest3.setBorderPainted(true);
				csEffect1.setBorderPainted(true);
				csEffect2.setBorderPainted(true);
				csEffect3.setBorderPainted(true);
			}
		});

		cardOptions.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				cardSet.setSelectedIndex(0);

				if (cardOptions.getSelectedItem().toString().equals("Animal Companion"))
				{
					discoverEffects.setSelectedIndex(0);
				}

				else if (cardOptions.getSelectedItem().toString().equals("Anti-Trap Sludge"))
				{
					discoverEffects.setSelectedIndex(4);
				}

				else if (cardOptions.getSelectedItem().toString().equals("Battle Miner"))
				{
					discoverEffects.setSelectedIndex(31);
				}

				else if (cardOptions.getSelectedItem().toString().equals("Call the Curators"))
				{
					discoverEffects.setSelectedIndex(32);
					cardSet.setSelectedIndex(1);
				}

				else if (cardOptions.getSelectedItem().toString().equals("Card Generator"))
				{
					//discoverEffects.setSelectedIndex(31);
					discoverEffects.setSelectedIndex(1);
				}

				else if (cardOptions.getSelectedItem().toString().equals("Dragon Summoning Ritual"))
				{
					discoverEffects.setSelectedIndex(7);
				}

				else if (cardOptions.getSelectedItem().toString().equals("Dragonfire Statue"))
				{
					discoverEffects.setSelectedIndex(10);
				}

				else if (cardOptions.getSelectedItem().toString().equals("Drakonid Operative"))
				{
					discoverEffects.setSelectedIndex(32);
					if (player.getSelectedItem().toString().equals("Player 1"))
					{
						cardSet.setSelectedIndex(3);
					}
					else
					{
						cardSet.setSelectedIndex(2);
					}

				}

				else if (cardOptions.getSelectedItem().toString().equals("Excavator"))
				{
					discoverEffects.setSelectedIndex(32);
				}

				else if (cardOptions.getSelectedItem().toString().equals("Firelands Portal"))
				{
					discoverEffects.setSelectedIndex(23);
				}

				else if (cardOptions.getSelectedItem().toString().equals("Greedpot Avatar"))
				{
					discoverEffects.setSelectedIndex(1);
				}

				else if (cardOptions.getSelectedItem().toString().equals("High Tide"))
				{
					//discoverEffects.setSelectedIndex(41);
					discoverEffects.setSelectedIndex(3);
				}

				else if (cardOptions.getSelectedItem().toString().equals("Infinity Dragon"))
				{
					discoverEffects.setSelectedIndex(37);
				}

				else if (cardOptions.getSelectedItem().toString().equals("Ivory Knight"))
				{
					discoverEffects.setSelectedIndex(31);
				}

				else if (cardOptions.getSelectedItem().toString().equals("Mad Scientist"))
				{
					discoverEffects.setSelectedIndex(36);
				}

				else if (cardOptions.getSelectedItem().toString().equals("Mega-Excavator"))
				{
					discoverEffects.setSelectedIndex(35);
				}

				else if (cardOptions.getSelectedItem().toString().equals("Missingno"))
				{
					//discoverEffects.setSelectedIndex(31);
					discoverEffects.setSelectedIndex(0);
				}

				else if (cardOptions.getSelectedItem().toString().equals("Monster Box"))
				{
					discoverEffects.setSelectedIndex(31);
				}

				else if (cardOptions.getSelectedItem().toString().equals("Mulch"))
				{
					discoverEffects.setSelectedIndex(31);
				}

				else if (cardOptions.getSelectedItem().toString().equals("Museum Curator"))
				{
					discoverEffects.setSelectedIndex(31);
					cardSet.setSelectedIndex(1);
				}

				else if (cardOptions.getSelectedItem().toString().equals("Nexus-Champion Saraad"))
				{
					discoverEffects.setSelectedIndex(33);
				}

				else if (cardOptions.getSelectedItem().toString().equals("Party at Kaiba's"))
				{
					discoverEffects.setSelectedIndex(31);
				}

				else if (cardOptions.getSelectedItem().toString().equals("Pierre the Gambler"))
				{
					discoverEffects.setSelectedIndex(34);
				}

				else if (cardOptions.getSelectedItem().toString().equals("Piloted Shredder"))
				{
					discoverEffects.setSelectedIndex(20);
				}

				else if (cardOptions.getSelectedItem().toString().equals("Piloted Sky Golem"))
				{
					discoverEffects.setSelectedIndex(24);
				}

				else if (cardOptions.getSelectedItem().toString().equals("Pool Party"))
				{
					discoverEffects.setSelectedIndex(42);
				}

				else if (cardOptions.getSelectedItem().toString().equals("Sacrificial Pact"))
				{
					discoverEffects.setSelectedIndex(43);
				}

				else if (cardOptions.getSelectedItem().toString().equals("Set the Stage"))
				{
					discoverEffects.setSelectedIndex(31);
				}

				else if (cardOptions.getSelectedItem().toString().equals("Sneed's Old Shredder"))
				{
					discoverEffects.setSelectedIndex(40);
				}

				else if (cardOptions.getSelectedItem().toString().equals("Spell Roulette"))
				{
					//discoverEffects.setSelectedIndex(33);
					discoverEffects.setSelectedIndex(2);
				}

				else if (cardOptions.getSelectedItem().toString().equals("Spellslinger"))
				{
					discoverEffects.setSelectedIndex(33);
				}

				else if (cardOptions.getSelectedItem().toString().equals("The Curator"))
				{
					discoverEffects.setSelectedIndex(32);
					cardSet.setSelectedIndex(1);
				}

				else if (cardOptions.getSelectedItem().toString().equals("The Legend of Exodia"))
				{
					discoverEffects.setSelectedIndex(9);
				}

				else if (cardOptions.getSelectedItem().toString().equals("Unstable Portal"))
				{
					discoverEffects.setSelectedIndex(31);
				}

				else if (cardOptions.getSelectedItem().toString().equals("Wrath of the Old Gods"))
				{
					discoverEffects.setSelectedIndex(5);
				}

				else if (cardOptions.getSelectedItem().toString().equals("Yogg-Saron, Hope's End"))
				{
					discoverEffects.setSelectedIndex(33);
				}
			}

		});

		discoverEffects.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				avgScore = 0;
				avgScoreUn = 0;
				discoverEffect = discoverEffects.getSelectedItem().toString();
				
				if (discoverEffect.equals("Effect Monster"))
				{
					if (discoverLocation.equals("Pool"))
					{
						discoverCardType(pool, "Effect Monster"); 
						avgScoreCalc(possibleOutcomes); 
						avgScoreCalcUnique(possibleOutcomes); 
					}
					
					else if (discoverLocation.equals("Curated Set"))
					{
						discoverCardType(curatedSet, "Effect Monster"); 
						avgScoreCalc(possibleOutcomes); 
						avgScoreCalcUnique(possibleOutcomes); 
					}
					
					else if (discoverLocation.equals("Player 1 Deck"))
					{
						discoverCardType(decks.get(0), "Effect Monster"); 
						avgScoreCalc(possibleOutcomes); 
						avgScoreCalcUnique(possibleOutcomes); 
					}
					
					else if (discoverLocation.equals("Player 2 Deck"))
					{
						discoverCardType(decks.get(1), "Effect Monster"); 
						avgScoreCalc(possibleOutcomes); 
						avgScoreCalcUnique(possibleOutcomes); 
					}
				}
				
				else if (discoverEffect.equals("Random"))
				{
					possibleOutcomes.clear();
					copyPoolVoid(pool, possibleOutcomes);
					avgScoreCalc(possibleOutcomes); 
					avgScoreCalcUnique(possibleOutcomes); 
				}
				
				else if (discoverEffect.equals("Water Effect"))
				{
					if (discoverLocation.equals("Pool"))
					{
						discoverAttribType(pool, "Effect Monster", "Water");
						avgScoreCalc(possibleOutcomes); 
						avgScoreCalcUnique(possibleOutcomes);
					}
					
					else if (discoverLocation.equals("Curated Set"))
					{
						discoverAttribType(curatedSet, "Effect Monster", "Water");
						avgScoreCalc(possibleOutcomes); 
						avgScoreCalcUnique(possibleOutcomes);
					}
					
					else if (discoverLocation.equals("Player 1 Deck"))
					{
						discoverAttribType(decks.get(0), "Effect Monster", "Water");
						avgScoreCalc(possibleOutcomes); 
						avgScoreCalcUnique(possibleOutcomes);
					}
					
					else if (discoverLocation.equals("Player 2 Deck"))
					{
						discoverAttribType(decks.get(1), "Effect Monster", "Water");
						avgScoreCalc(possibleOutcomes); 
						avgScoreCalcUnique(possibleOutcomes);
					}
				}
				
				else if (discoverEffect.equals("Spell"))
				{
					
					if (discoverLocation.equals("Pool")) 
					{ 
						discoverCardType(pool, "Spell"); 
						avgScoreCalc(possibleOutcomes); 
						avgScoreCalcUnique(possibleOutcomes); 
					}
					else if (discoverLocation.equals("Curated Set")) 
					{
						discoverCardType(curatedSet, "Spell"); 
						avgScoreCalc(possibleOutcomes); 
						avgScoreCalcUnique(possibleOutcomes); 
					}
					
					
					else if (discoverLocation.equals("Player 1 Deck"))
					{
						discoverCardType(decks.get(0), "Spell"); 
						avgScoreCalc(possibleOutcomes); 
						avgScoreCalcUnique(possibleOutcomes); 
					}
					
					else if (discoverLocation.equals("Player 2 Deck"))
					{
						discoverCardType(decks.get(1), "Spell"); 
						avgScoreCalc(possibleOutcomes); 
						avgScoreCalcUnique(possibleOutcomes); 
					}
				}
				Integer avgScoreLocal = avgScore;
				String asString = avgScoreLocal.toString();
				avgNum.setText(asString);
			}

		});

		cardSet.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				discoverLocation = cardSet.getSelectedItem().toString();
				discoverEffects.setSelectedIndex(discoverEffects.getSelectedIndex());
			}

		});

		player.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				discoverPlayer = player.getSelectedItem().toString();
			}

		});
		
		curatedRadio.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				poolRadio.setSelected(false);
				outcomeRadio.setSelected(false);
				deckRadio.setSelected(false);
				discoverRadio.setSelected(false);
				selectedRadio = "Curated Set";
			}
		});
		
		poolRadio.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				curatedRadio.setSelected(false);
				outcomeRadio.setSelected(false);
				deckRadio.setSelected(false);
				discoverRadio.setSelected(false);
				selectedRadio = "Pool";
			}
		});
		
		outcomeRadio.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				poolRadio.setSelected(false);
				curatedRadio.setSelected(false);
				deckRadio.setSelected(false);
				discoverRadio.setSelected(false);
				selectedRadio = "Possibilities";
			}
		});
		
		deckRadio.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				poolRadio.setSelected(false);
				outcomeRadio.setSelected(false);
				curatedRadio.setSelected(false);
				discoverRadio.setSelected(false);
				selectedRadio = "Deck";
			}
		});
		
		discoverRadio.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				poolRadio.setSelected(false);
				outcomeRadio.setSelected(false);
				deckRadio.setSelected(false);
				curatedRadio.setSelected(false);
				selectedRadio = "Discovered";
			}
		});



	}

	/**
	 * Feed card database text file into the program
	 * 
	 * @param noOfCards
	 * @return void
	 */
	static void readDatabase(int noOfCards, String line, String[] input, String name, String attribute, String type, String cardType, 
			int atk, int def, int tierScore, int lvl, int quantity, int limit, String crosslimit, String rarity, String text, String synergies, boolean monster,
			boolean contin, boolean quickplay, boolean counter, boolean field, boolean equip, boolean ritual, boolean normal, ArrayList<Card> allCards, String databaseName)
	{
		ArrayList<Card> updates = readOtherDatabase();

		try // Try to open the database
		{	
			// Opens database .txt file
			FileInputStream database = new FileInputStream(databaseName);
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

	/**
	 * Does some updates to the text of cards missing text
	 * 
	 * 
	 * @return void
	 */
	public static ArrayList<Card> readOtherDatabase()
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

	/**
	 * List copy
	 * 
	 * @param pool
	 * @return duplicated pool
	 */
	public static ArrayList<Card> copyPool(ArrayList<Card> pool)
	{
		ArrayList<Card> temp = new ArrayList<>();
		for (Card card : pool)
		{
			Card tempCard = new Card(card);
			temp.add(tempCard);
		}
		return temp;
	}

	/**
	 * Creates a list to be used with some of the view menus.
	 * Returned array contains one of each copy of the cards from the given pool.
	 * 
	 * 
	 * @param pool
	 * @return the correctly formatted list
	 */
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

	/**
	 * Counts up how many of the given card exist within the given pool
	 * 
	 * @param pool
	 * @param card
	 * @return number of the given card that is in the pool
	 */ 
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

	/**
	 * Counts how many cards exist in the pool
	 * 
	 * @param pool
	 * @return number of cards in the pool
	 */
	// 
	public static int cardCount(ArrayList<Card> pool)
	{
		int count = 0;
		for (int i = 0; i < pool.size(); i++)
		{
			count += pool.get(i).getQuantity();
		}

		return count;
	}

	/**
	 * List copy with void return. Pass in the list to copy from and then the list to copy into.
	 * 
	 * @param copyFrom
	 * @param copyOver
	 * @return void
	 */
	public static void copyPoolVoid(ArrayList<Card> copyFrom, ArrayList<Card> copyOver)
	{
		copyOver.clear();
		for (Card card : copyFrom)
		{
			Card tempCard = new Card(card);
			copyOver.add(tempCard);
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

	/**
	 * List copy with void return. Pass in the list to copy from and then the list to copy into.
	 * 
	 * @param copyFrom
	 * @param copyOver
	 * @return void
	 */
	public static void copyPoolVoidString(ArrayList<String> copyFrom, ArrayList<String> copyOver)
	{
		copyOver.clear();
		for (String card : copyFrom)
		{
			String tempCard = card;
			copyOver.add(tempCard);
		}
	}

	public static ArrayList<Card> discoverCardType(ArrayList<Card> pool, String cardType)
	{
		int spells = 0;
		boolean checker = false;
		cardCounter(pool);
		pool.sort(pool.get(0));
		ArrayList<Card> spellList = new ArrayList<Card>();
		ArrayList<Card> threeSpells = new ArrayList<Card>();
		for (Card card : pool)
		{
			if (card.getCardType().equals(cardType))
			{
				spells++;
				spellList.add(card);
			}
		}
		
		copyPoolVoid(spellList, possibleOutcomes);

		while (threeSpells.size() < 3)
		{
			Random rand = new Random();
			int seed = rand.nextInt(spells);
			Card discovered = new Card(spellList.get(seed));
			for (Card card : threeSpells)
			{
				if (discovered.getName().equals(card.getName()))
				{
					checker = true;
				}
			}

			if (checker == false) { threeSpells.add(discovered); }
		}

		return threeSpells;

	}
	
	public static ArrayList<Card> discoverMonsterType(ArrayList<Card> pool, String type)
	{
		int spells = 0;
		boolean checker = false;
		cardCounter(pool);
		pool.sort(pool.get(0));
		ArrayList<Card> spellList = new ArrayList<Card>();
		ArrayList<Card> threeSpells = new ArrayList<Card>();
		for (Card card : pool)
		{
			if (card.getType().equals(type))
			{
				spells++;
				spellList.add(card);
			}
		}
		
		copyPoolVoid(spellList, possibleOutcomes);

		while (threeSpells.size() < 3)
		{
			Random rand = new Random();
			int seed = rand.nextInt(spells);
			Card discovered = new Card(spellList.get(seed));
			for (Card card : threeSpells)
			{
				if (discovered.getName().equals(card.getName()))
				{
					checker = true;
				}
			}

			if (checker == false) { threeSpells.add(discovered); }
		}

		return threeSpells;

	}

	public static ArrayList<Card> discoverCard(ArrayList<Card> pool)
	{
		boolean checker = false;
		cardCounter(pool);
		pool.sort(pool.get(0));
		ArrayList<Card> threeSpells = new ArrayList<Card>();
		
		copyPoolVoid(pool, possibleOutcomes);

		while (threeSpells.size() < 3)
		{
			Random rand = new Random();
			int seed = rand.nextInt(pool.size());
			Card discovered = new Card(pool.get(seed));
			for (Card card : threeSpells)
			{
				if (discovered.getName().equals(card.getName()))
				{
					checker = true;
				}
			}

			if (checker == false) { threeSpells.add(discovered); }
		}

		return threeSpells;

	}

	public static ArrayList<Card> discoverCardType2(ArrayList<Card> pool, String cardType, String cardType2)
	{
		int spells = 0;
		boolean checker = false;
		cardCounter(pool);
		pool.sort(pool.get(0));
		ArrayList<Card> spellList = new ArrayList<Card>();
		ArrayList<Card> threeSpells = new ArrayList<Card>();
		for (Card card : pool)
		{
			if (card.getCardType().equals(cardType))
			{
				spells++;
				spellList.add(card);
			}

			else if (card.getCardType().equals(cardType2))
			{
				spells++;
				spellList.add(card);
			}
		}
		
		copyPoolVoid(spellList, possibleOutcomes);

		while (threeSpells.size() < 3)
		{
			Random rand = new Random();
			int seed = rand.nextInt(spells);
			Card discovered = new Card(spellList.get(seed));
			for (Card card : threeSpells)
			{
				if (discovered.getName().equals(card.getName()))
				{
					checker = true;
				}
			}

			if (checker == false) { threeSpells.add(discovered); }
		}

		return threeSpells;

	}

	public static ArrayList<Card> discoverAttribType(ArrayList<Card> pool, String cardType, String attrib)
	{
		int spells = 0;
		boolean checker = false;
		cardCounter(pool);
		pool.sort(pool.get(0));
		ArrayList<Card> spellList = new ArrayList<Card>();
		ArrayList<Card> threeSpells = new ArrayList<Card>();
		for (Card card : pool)
		{
			if (card.getCardType().equals(cardType) && card.getAttribute().equals(attrib))
			{
				spells++;
				spellList.add(card);
			}
		}
		
		copyPoolVoid(spellList, possibleOutcomes);

		while (threeSpells.size() < 3)
		{
			Random rand = new Random();
			int seed = rand.nextInt(spells);
			Card discovered = new Card(spellList.get(seed));
			for (Card card : threeSpells)
			{
				if (discovered.getName().equals(card.getName()))
				{
					checker = true;
				}
			}

			if (checker == false) { threeSpells.add(discovered); }
		}

		return threeSpells;

	}

	public static ArrayList<Card> discoverMonster(ArrayList<Card> pool)
	{
		int spells = 0;
		boolean checker = false;
		cardCounter(pool);
		pool.sort(pool.get(0));
		ArrayList<Card> spellList = new ArrayList<Card>();
		ArrayList<Card> threeSpells = new ArrayList<Card>();
		for (Card card : pool)
		{
			if (card.getCardType().equals("Normal Monster"))
			{
				spells++;
				spellList.add(card);
			}

			else if (card.getCardType().equals("Effect Monster"))
			{
				spells++;
				spellList.add(card);
			}

			else if (card.getCardType().equals("Ritual Monster"))
			{
				spells++;
				spellList.add(card);
			}

			else if (card.getCardType().equals("Fusion Monster"))
			{
				spells++;
				spellList.add(card);
			}
		}
		
		copyPoolVoid(spellList, possibleOutcomes);

		while (threeSpells.size() < 3)
		{
			Random rand = new Random();
			int seed = rand.nextInt(spells);
			Card discovered = new Card(spellList.get(seed));
			for (Card card : threeSpells)
			{
				if (discovered.getName().equals(card.getName()))
				{
					checker = true;
				}
			}

			if (checker == false) { threeSpells.add(discovered); }
		}

		return threeSpells;

	}

	public static ArrayList<Card> discoverLevel(ArrayList<Card> pool, int level)
	{
		int spells = 0;
		boolean checker = false;
		cardCounter(pool);
		pool.sort(pool.get(0));
		ArrayList<Card> spellList = new ArrayList<Card>();
		ArrayList<Card> threeSpells = new ArrayList<Card>();
		for (Card card : pool)
		{
			if (card.getLvl() == level)
			{
				spells++;
				spellList.add(card);
			}
		}
		
		copyPoolVoid(spellList, possibleOutcomes);

		while (threeSpells.size() < 3)
		{
			Random rand = new Random();
			int seed = rand.nextInt(spells);
			Card discovered = new Card(spellList.get(seed));
			for (Card card : threeSpells)
			{
				if (discovered.getName().equals(card.getName()))
				{
					checker = true;
				}
			}

			if (checker == false) { threeSpells.add(discovered); }
		}

		return threeSpells;

	}
	
	public static ArrayList<Card> discoverLevelLower(ArrayList<Card> pool, int level)
	{
		int spells = 0;
		boolean checker = false;
		cardCounter(pool);
		pool.sort(pool.get(0));
		ArrayList<Card> spellList = new ArrayList<Card>();
		ArrayList<Card> threeSpells = new ArrayList<Card>();
		for (Card card : pool)
		{
			if (card.getLvl() < level && card.getLvl() > 0)
			{
				spells++;
				spellList.add(card);
			}
		}
		
		copyPoolVoid(spellList, possibleOutcomes);

		while (threeSpells.size() < 3)
		{
			Random rand = new Random();
			int seed = rand.nextInt(spells);
			Card discovered = new Card(spellList.get(seed));
			for (Card card : threeSpells)
			{
				if (discovered.getName().equals(card.getName()))
				{
					checker = true;
				}
			}

			if (checker == false) { threeSpells.add(discovered); }
		}

		return threeSpells;

	}
	
	public static ArrayList<Card> discoverLevelHigher(ArrayList<Card> pool, int level)
	{
		int spells = 0;
		boolean checker = false;
		cardCounter(pool);
		pool.sort(pool.get(0));
		ArrayList<Card> spellList = new ArrayList<Card>();
		ArrayList<Card> threeSpells = new ArrayList<Card>();
		for (Card card : pool)
		{
			if (card.getLvl() > level)
			{
				spells++;
				spellList.add(card);
			}
		}
		
		copyPoolVoid(spellList, possibleOutcomes);

		while (threeSpells.size() < 3)
		{
			Random rand = new Random();
			int seed = rand.nextInt(spells);
			Card discovered = new Card(spellList.get(seed));
			for (Card card : threeSpells)
			{
				if (discovered.getName().equals(card.getName()))
				{
					checker = true;
				}
			}

			if (checker == false) { threeSpells.add(discovered); }
		}

		return threeSpells;

	}

	public static ArrayList<Card> discoverPoolParty(ArrayList<Card> pool, int level, String type)
	{
		int spells = 0;
		boolean checker = false;
		cardCounter(pool);
		pool.sort(pool.get(0));
		ArrayList<Card> spellList = new ArrayList<Card>();
		ArrayList<Card> threeSpells = new ArrayList<Card>();
		for (Card card : pool)
		{
			if (card.getLvl() <= level)
			{
				if (card.getAttribute().equals(type))
				{
					spells++;
					spellList.add(card);
				}
			}
		}

		copyPoolVoid(spellList, possibleOutcomes);

		while (threeSpells.size() < 3)
		{
			Random rand = new Random();
			int seed = rand.nextInt(spells);
			Card discovered = new Card(spellList.get(seed));
			for (Card card : threeSpells)
			{
				if (discovered.getName().equals(card.getName()))
				{
					checker = true;
				}
			}

			if (checker == false) { threeSpells.add(discovered); }
		}

		return threeSpells;

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

	/**
	 * Creates a list to be used with the view deck menu seen while choosing cards.
	 * Returned array contains one of each copy of the cards from the given pool.
	 * The list entries are formatted to contain images and text for nice looking quantity icons.
	 * 
	 * 
	 * @param pool
	 * @param model
	 * @return the correctly formatted list
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
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
			if (card.getQuantity() == 3 && card.getLimit() == 3) { model.addElement(new ListEntry(card.getName(), new ImageIcon("src/images/Quantity - Lim3.png"), card)); }
			else if (card.getQuantity() == 2 && card.getLimit() == 2) { model.addElement(new ListEntry(card.getName(), new ImageIcon("src/images/Quantity - Lim2.png"), card));}
			else if (card.getQuantity() == 1 && card.getLimit() == 1) { model.addElement(new ListEntry(card.getName(), new ImageIcon("src/images/Quantity - Lim1.png"), card));}
			else if (card.getQuantity() == 2 && card.getLimit() != 2) { model.addElement(new ListEntry(card.getName(), new ImageIcon("src/images/Quantity - 2Alt.png"), card)); }
			else if (card.getQuantity() > 3) { model.addElement(new ListEntry(card.getName(), new ImageIcon("src/images/Quantity - 3+.png"), card)); } 
			else { model.addElement(new ListEntry(card.getName(), new ImageIcon("src/images/Quantity - " + card.getQuantity() + ".png"), card)); }
		}
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

	/**
	 * After you have drafted all your cards runs through and totals up how many of each card for nice output
	 * 
	 * @param drafted
	 * @return void
	 */
	public static void cardCounter(ArrayList<Card> drafted)
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
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void refreshList(ArrayList<Card> cards)
	{
		ArrayList<Card> tempDeck = listMaker(cards);
		DefaultListModel localModel = new DefaultListModel();
		for (Card card : tempDeck) 
		{ 
			//localModel.addElement(new ListEntry(card.getName(), new ImageIcon("src/images/Quantity - " + card.getQuantity() + ".png"), card)); 
			if (card.getQuantity() == 3 && card.getLimit() == 3) { localModel.addElement(new ListEntry(card.getName(), new ImageIcon("src/images/Quantity - Lim3.png"), card)); }
			else if (card.getQuantity() == 2 && card.getLimit() == 2) { localModel.addElement(new ListEntry(card.getName(), new ImageIcon("src/images/Quantity - Lim2.png"), card));}
			else if (card.getQuantity() == 1 && card.getLimit() == 1) { localModel.addElement(new ListEntry(card.getName(), new ImageIcon("src/images/Quantity - Lim1.png"), card));}
			else if (card.getQuantity() == 2 && card.getLimit() != 2) { localModel.addElement(new ListEntry(card.getName(), new ImageIcon("src/images/Quantity - 2Alt.png"), card)); }
			else if (card.getQuantity() > 3) { localModel.addElement(new ListEntry(card.getName(), new ImageIcon("src/images/Quantity - 3+.png"), card)); } 
			else { localModel.addElement(new ListEntry(card.getName(), new ImageIcon("src/images/Quantity - " + card.getQuantity() + ".png"), card)); }
		}
		dynamicList2.setModel(localModel);
	}
	
	public static void avgScoreCalc(ArrayList<Card> pool)
	{
		avgScore = 0;
		for (Card card : pool)
		{
			avgScore += card.getTierScore();
		}
		
		if (pool.size() > 0)
		{
			avgScore = avgScore / pool.size();
		}
		
	}
	
	public static void avgScoreCalcUnique(ArrayList<Card> pool)
	{
		avgScoreUn = 0;
		ArrayList<Card> noDupes = listMaker(pool);
		for (Card card : noDupes)
		{
			avgScoreUn += card.getTierScore();
		}
		
		if (noDupes.size() > 0)
		{
			avgScoreUn = avgScoreUn / noDupes.size();
		}
		
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
}
