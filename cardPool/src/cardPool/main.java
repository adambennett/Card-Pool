package cardPool;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

import javax.swing.ImageIcon;

public class main 
{

	@SuppressWarnings({ "null", "resource", "unused" })
	public static void main(String[] args) 
	{
		// Variable Initialization
		ArrayList<Card> allCards = new ArrayList<Card>();
		ArrayList<Card> drafted = new ArrayList<Card>();
		ArrayList<Card> draftPool = new ArrayList<Card>();
		ArrayList<Card> threeChoiceTemp = new ArrayList<Card>();
		ArrayList<ArrayList<Card>> draftDecks = new ArrayList<ArrayList<Card>>();
		ArrayList<ArrayList<Card>> draftPools = new ArrayList<ArrayList<Card>>();
		ArrayList<String> types = new ArrayList<String>();
		
		String[] input = null;
		String[] synergyList = null;
		
		int[] input2 = null;
		
		Scanner scan = new Scanner(System.in);
		
		String line = null;	String name = null;	String attribute = null;	String type = null;
		String cardType = null;	String text = null;	String crosslimit = null;	String rarity = null;
		String synergies = null;

		int atk = 0;	int def = 0;	int lvl = 0;	int tierScore = 0;	
		int limit = 0;	int quantity = 0;	int noOfCards = 1; int playerDrafting = 0;

		boolean monster = false;	boolean contin = false;	boolean quickplay = false;	boolean counter = false;
		boolean field = false;	boolean equip = false;	boolean ritual = false;	boolean normal = false; boolean scanCheck = true;
		// END Variable Init
		

		readDatabase(noOfCards, line, input, name, attribute, type, cardType, atk, def, tierScore, lvl, quantity, limit, crosslimit, rarity, text, synergies, monster, contin, quickplay, counter, field, equip, ritual, normal, allCards);		
	
		//printRarities(allCards);
		ArrayList<Card> allCardsNoDupes = listMaker(allCards);
		//printRarities(allCardsNoDupes);
		//imageCheck(allCardsNoDupes);
		//monsterPrintAttribute(allCardsNoDupes, "Water");
		//monsterPrintType(allCardsNoDupes, "Dinosaur");
		printDiscoverEffectAttribute(allCardsNoDupes, "Water");
		//cardDistrib(allCardsNoDupes);
		//cardDistrib(allCards);
		//crossLimitPrint(allCardsNoDupes);
		//textCheck(allCardsNoDupes);
		//parseHubFormat(allCardsNoDupes);
		//printDiscoverSpell(allCardsNoDupes);
		
		
		// CONSOLE DRAFT START
		/*
		int totalCards = cardCount(allCards);	
		int playerCount = poolDeckInit(draftPools, draftDecks, scan, scanCheck);
		int poolSize = fillAllPools(allCards, draftPools, playerCount);
		int cardsToDraft = howManyToDraft(scanCheck, scan, totalCards, poolSize);
		//int cardsToDraft = draftPools.get(0).size();
		int noOfPlayers = playerCount;
		draftDecks.clear();
				
		// While there are still players that need to draft, keep drafting
		while (noOfPlayers > 0)
		{
			// This fills up a pool of cards equal to the size of the pool every other player has drafted from
			draftPool = draftPools.get(playerDrafting);
			drafted = new ArrayList<Card>();
			boolean rerolled = false;
			String favoredType = "";
			String pickedSynergy = "";
			int pickNumber = 1;
			types = typeGen(draftPool);
			favoredType = randomType(types);
			pickedSynergy = synGen(draftPool);
			//System.out.println("Starting favored type: " + favoredType);
			//System.out.println("Starting synergy: " + pickedSynergy);
			for (int i = 0; i < cardsToDraft;)
			{
				if (draftPool.size() > 3)
				{
					if (drafted.size() > 0)
					{ 
						favoredType = typeCounter(drafted); 
						pickedSynergy = synGen(drafted);
					}
					//threeChoiceTemp = threeChoices(draftPool, drafted, favoredType, pickedSynergy);
					draftPool = removeLimitedCards(draftPool, drafted);
					threeChoiceTemp = picks(draftPool, drafted, favoredType, pickedSynergy, 3);
					System.out.println("\nPick #" + pickNumber + " -- " + draftPool.size() + "/" + poolSize + " cards");
					System.out.println("-------------------------");
					System.out.println("A -- " + threeChoiceTemp.get(0).getName() + " -- " + threeChoiceTemp.get(0).getRarity());
					System.out.println("B -- " + threeChoiceTemp.get(1).getName() + " -- " + threeChoiceTemp.get(1).getRarity());
					System.out.println("C -- " + threeChoiceTemp.get(2).getName() + " -- " + threeChoiceTemp.get(2).getRarity());
					System.out.println("Enter the letter of your choice: ");
					String chosenCard = scan.next();
					//String chosenCard = "A";
					boolean keepSwitchin = true;
		
					while (keepSwitchin)
					{
						switch (chosenCard) 
						{
						case "A":
							drafted.add(threeChoiceTemp.get(0));
							drafted.get(drafted.size() - 1).setQuantity(1);
							drafted = cardCounter(drafted);
							draftPool.remove(threeChoiceTemp.get(0));
							draftPool.remove(threeChoiceTemp.get(1));
							draftPool.remove(threeChoiceTemp.get(2));
							keepSwitchin = false;
							i++; pickNumber++;
							break;
						case "A/text":
							System.out.println("\n"+ threeChoiceTemp.get(0).getText());
							System.out.println("\nPick #" + pickNumber);
							System.out.println("-------------------------");
							System.out.println("A -- " + threeChoiceTemp.get(0).getName() + " -- " + threeChoiceTemp.get(0).getRarity());
							System.out.println("B -- " + threeChoiceTemp.get(1).getName() + " -- " + threeChoiceTemp.get(1).getRarity());
							System.out.println("C -- " + threeChoiceTemp.get(2).getName() + " -- " + threeChoiceTemp.get(2).getRarity());
							System.out.println("Enter the letter of your choice: ");
							chosenCard = scan.next();
							break;
						case "A/stats":
							System.out.println("\nATK: "+ threeChoiceTemp.get(0).getAtk() + "  DEF: " + threeChoiceTemp.get(0).getDef()  + "  Level:" + threeChoiceTemp.get(0).getLvl() + "\nType: " + threeChoiceTemp.get(0).getType() + "  Attribute: " + threeChoiceTemp.get(0).getAttribute() + "\nType: " + threeChoiceTemp.get(0).getCardType());
							System.out.println("\nPick #" + pickNumber);
							System.out.println("-------------------------");
							System.out.println("A -- " + threeChoiceTemp.get(0).getName() + " -- " + threeChoiceTemp.get(0).getRarity());
							System.out.println("B -- " + threeChoiceTemp.get(1).getName() + " -- " + threeChoiceTemp.get(1).getRarity());
							System.out.println("C -- " + threeChoiceTemp.get(2).getName() + " -- " + threeChoiceTemp.get(2).getRarity());
							System.out.println("Enter the letter of your choice: ");
							chosenCard = scan.next();
							break;
						case "A/type":
							System.out.println("\n"+ threeChoiceTemp.get(0).getCardType());
							System.out.println("\nPick #" + pickNumber);
							System.out.println("-------------------------");
							System.out.println("A -- " + threeChoiceTemp.get(0).getName() + " -- " + threeChoiceTemp.get(0).getRarity());
							System.out.println("B -- " + threeChoiceTemp.get(1).getName() + " -- " + threeChoiceTemp.get(1).getRarity());
							System.out.println("C -- " + threeChoiceTemp.get(2).getName() + " -- " + threeChoiceTemp.get(2).getRarity());
							System.out.println("Enter the letter of your choice: ");
							chosenCard = scan.next();
							break;
						case "a":
							drafted.add(threeChoiceTemp.get(0));
							drafted.get(drafted.size() - 1).setQuantity(1);
							drafted = cardCounter(drafted);
							draftPool.remove(threeChoiceTemp.get(0));
							draftPool.remove(threeChoiceTemp.get(1));
							draftPool.remove(threeChoiceTemp.get(2));
							keepSwitchin = false;
							i++; pickNumber++;
							break;
						case "a/text":
							System.out.println("\n"+ threeChoiceTemp.get(0).getText());
							System.out.println("\nPick #" + pickNumber);
							System.out.println("-------------------------");
							System.out.println("A -- " + threeChoiceTemp.get(0).getName() + " -- " + threeChoiceTemp.get(0).getRarity());
							System.out.println("B -- " + threeChoiceTemp.get(1).getName() + " -- " + threeChoiceTemp.get(1).getRarity());
							System.out.println("C -- " + threeChoiceTemp.get(2).getName() + " -- " + threeChoiceTemp.get(2).getRarity());
							System.out.println("Enter the letter of your choice: ");
							chosenCard = scan.next();
							break;
						case "a/stats":
							System.out.println("\nATK: "+ threeChoiceTemp.get(0).getAtk() + "  DEF: " + threeChoiceTemp.get(0).getDef()  + "  Level:" + threeChoiceTemp.get(0).getLvl() + "\nType: " + threeChoiceTemp.get(0).getType() + "  Attribute: " + threeChoiceTemp.get(0).getAttribute() + "\nType: " + threeChoiceTemp.get(0).getCardType());
							System.out.println("\nPick #" + pickNumber);
							System.out.println("-------------------------");
							System.out.println("A -- " + threeChoiceTemp.get(0).getName() + " -- " + threeChoiceTemp.get(0).getRarity());
							System.out.println("B -- " + threeChoiceTemp.get(1).getName() + " -- " + threeChoiceTemp.get(1).getRarity());
							System.out.println("C -- " + threeChoiceTemp.get(2).getName() + " -- " + threeChoiceTemp.get(2).getRarity());
							System.out.println("Enter the letter of your choice: ");
							chosenCard = scan.next();
							break;
						case "a/type":
							System.out.println("\n"+ threeChoiceTemp.get(0).getCardType());
							System.out.println("Pick #" + pickNumber);
							System.out.println("-------------------------");
							System.out.println("A -- " + threeChoiceTemp.get(0).getName() + " -- " + threeChoiceTemp.get(0).getRarity());
							System.out.println("B -- " + threeChoiceTemp.get(1).getName() + " -- " + threeChoiceTemp.get(1).getRarity());
							System.out.println("C -- " + threeChoiceTemp.get(2).getName() + " -- " + threeChoiceTemp.get(2).getRarity());
							System.out.println("Enter the letter of your choice: ");
							chosenCard = scan.next();
							break;
						case "B":
							drafted.add(threeChoiceTemp.get(1));
							drafted.get(drafted.size() - 1).setQuantity(1);
							drafted = cardCounter(drafted);
							draftPool.remove(threeChoiceTemp.get(0));
							draftPool.remove(threeChoiceTemp.get(1));
							draftPool.remove(threeChoiceTemp.get(2));
							keepSwitchin = false;
							i++; pickNumber++;
							break;
						case "B/text":
							System.out.println("\n"+ threeChoiceTemp.get(1).getText());
							System.out.println("\nPick #" + pickNumber);
							System.out.println("-------------------------");
							System.out.println("A -- " + threeChoiceTemp.get(0).getName() + " -- " + threeChoiceTemp.get(0).getRarity());
							System.out.println("B -- " + threeChoiceTemp.get(1).getName() + " -- " + threeChoiceTemp.get(1).getRarity());
							System.out.println("C -- " + threeChoiceTemp.get(2).getName() + " -- " + threeChoiceTemp.get(2).getRarity());
							System.out.println("Enter the letter of your choice: ");
							chosenCard = scan.next();
							break;
						case "B/stats":
							System.out.println("\nATK: "+ threeChoiceTemp.get(1).getAtk() + "  DEF: " + threeChoiceTemp.get(1).getDef()  + "  Level:" + threeChoiceTemp.get(1).getLvl() + "\nType: " + threeChoiceTemp.get(1).getType() + "  Attribute: " + threeChoiceTemp.get(1).getAttribute() + "\nType: " + threeChoiceTemp.get(1).getCardType());
							System.out.println("\nPick #" + pickNumber);
							System.out.println("-------------------------");
							System.out.println("A -- " + threeChoiceTemp.get(0).getName() + " -- " + threeChoiceTemp.get(0).getRarity());
							System.out.println("B -- " + threeChoiceTemp.get(1).getName() + " -- " + threeChoiceTemp.get(1).getRarity());
							System.out.println("C -- " + threeChoiceTemp.get(2).getName() + " -- " + threeChoiceTemp.get(2).getRarity());
							System.out.println("Enter the letter of your choice: ");
							chosenCard = scan.next();
							break;
						case "B/type":
							System.out.println("\n"+ threeChoiceTemp.get(1).getCardType());
							System.out.println("\nPick #" + pickNumber);
							System.out.println("-------------------------");
							System.out.println("A -- " + threeChoiceTemp.get(0).getName() + " -- " + threeChoiceTemp.get(0).getRarity());
							System.out.println("B -- " + threeChoiceTemp.get(1).getName() + " -- " + threeChoiceTemp.get(1).getRarity());
							System.out.println("C -- " + threeChoiceTemp.get(2).getName() + " -- " + threeChoiceTemp.get(2).getRarity());
							System.out.println("Enter the letter of your choice: ");
							chosenCard = scan.next();
							break;
						case "b":
							drafted.add(threeChoiceTemp.get(1));
							drafted.get(drafted.size() - 1).setQuantity(1);
							drafted = cardCounter(drafted);
							draftPool.remove(threeChoiceTemp.get(0));
							draftPool.remove(threeChoiceTemp.get(1));
							draftPool.remove(threeChoiceTemp.get(2));
							keepSwitchin = false;
							i++; pickNumber++;
							break;
						case "b/text":
							System.out.println("\n"+ threeChoiceTemp.get(1).getText());
							System.out.println("\nPick #" + pickNumber);
							System.out.println("-------------------------");
							System.out.println("A -- " + threeChoiceTemp.get(0).getName() + " -- " + threeChoiceTemp.get(0).getRarity());
							System.out.println("B -- " + threeChoiceTemp.get(1).getName() + " -- " + threeChoiceTemp.get(1).getRarity());
							System.out.println("C -- " + threeChoiceTemp.get(2).getName() + " -- " + threeChoiceTemp.get(2).getRarity());
							System.out.println("Enter the letter of your choice: ");
							chosenCard = scan.next();
							break;
						case "b/stats":
							System.out.println("\nATK: "+ threeChoiceTemp.get(1).getAtk() + "  DEF: " + threeChoiceTemp.get(1).getDef() + "\nType: " + threeChoiceTemp.get(1).getType() + "  Attribute: " + threeChoiceTemp.get(1).getAttribute() + "\nType: " + threeChoiceTemp.get(1).getCardType());
							System.out.println("\nPick #" + pickNumber);
							System.out.println("-------------------------");
							System.out.println("A -- " + threeChoiceTemp.get(0).getName() + " -- " + threeChoiceTemp.get(0).getRarity());
							System.out.println("B -- " + threeChoiceTemp.get(1).getName() + " -- " + threeChoiceTemp.get(1).getRarity());
							System.out.println("C -- " + threeChoiceTemp.get(2).getName() + " -- " + threeChoiceTemp.get(2).getRarity());
							System.out.println("Enter the letter of your choice: ");
							chosenCard = scan.next();
							break;
						case "b/type":
							System.out.println("\n"+ threeChoiceTemp.get(1).getCardType());
							System.out.println("\nPick #" + pickNumber);
							System.out.println("-------------------------");
							System.out.println("A -- " + threeChoiceTemp.get(0).getName() + " -- " + threeChoiceTemp.get(0).getRarity());
							System.out.println("B -- " + threeChoiceTemp.get(1).getName() + " -- " + threeChoiceTemp.get(1).getRarity());
							System.out.println("C -- " + threeChoiceTemp.get(2).getName() + " -- " + threeChoiceTemp.get(2).getRarity());
							System.out.println("Enter the letter of your choice: ");
							chosenCard = scan.next();
							break;
						case "C":
							drafted.add(threeChoiceTemp.get(2));
							drafted.get(drafted.size() - 1).setQuantity(1);
							drafted = cardCounter(drafted);
							draftPool.remove(threeChoiceTemp.get(0));
							draftPool.remove(threeChoiceTemp.get(1));
							draftPool.remove(threeChoiceTemp.get(2));
							keepSwitchin = false;
							i++; pickNumber++;
							break;
						case "C/text":
							System.out.println("\n"+ threeChoiceTemp.get(2).getText());
							System.out.println("\nPick #" + pickNumber);
							System.out.println("-------------------------");
							System.out.println("A -- " + threeChoiceTemp.get(0).getName() + " -- " + threeChoiceTemp.get(0).getRarity());
							System.out.println("B -- " + threeChoiceTemp.get(1).getName() + " -- " + threeChoiceTemp.get(1).getRarity());
							System.out.println("C -- " + threeChoiceTemp.get(2).getName() + " -- " + threeChoiceTemp.get(2).getRarity());
							System.out.println("Enter the letter of your choice: ");
							chosenCard = scan.next();
							break;
						case "C/stats":
							System.out.println("\nATK: "+ threeChoiceTemp.get(2).getAtk() + "  DEF: " + threeChoiceTemp.get(2).getDef() + "\nType: " + threeChoiceTemp.get(2).getType() + "  Attribute: " + threeChoiceTemp.get(2).getAttribute() + "\nType: " + threeChoiceTemp.get(2).getCardType());
							System.out.println("\nPick #" + pickNumber);
							System.out.println("-------------------------");
							System.out.println("A -- " + threeChoiceTemp.get(0).getName() + " -- " + threeChoiceTemp.get(0).getRarity());
							System.out.println("B -- " + threeChoiceTemp.get(1).getName() + " -- " + threeChoiceTemp.get(1).getRarity());
							System.out.println("C -- " + threeChoiceTemp.get(2).getName() + " -- " + threeChoiceTemp.get(2).getRarity());
							System.out.println("Enter the letter of your choice: ");
							chosenCard = scan.next();
							break;
						case "C/type":
							System.out.println("\n"+ threeChoiceTemp.get(2).getCardType());
							System.out.println("\nPick #" + pickNumber);
							System.out.println("-------------------------");
							System.out.println("A -- " + threeChoiceTemp.get(0).getName() + " -- " + threeChoiceTemp.get(0).getRarity());
							System.out.println("B -- " + threeChoiceTemp.get(1).getName() + " -- " + threeChoiceTemp.get(1).getRarity());
							System.out.println("C -- " + threeChoiceTemp.get(2).getName() + " -- " + threeChoiceTemp.get(2).getRarity());
							System.out.println("Enter the letter of your choice: ");
							chosenCard = scan.next();
							break;
						case "c":
							drafted.add(threeChoiceTemp.get(2));
							drafted.get(drafted.size() - 1).setQuantity(1);
							drafted = cardCounter(drafted);
							draftPool.remove(threeChoiceTemp.get(0));
							draftPool.remove(threeChoiceTemp.get(1));
							draftPool.remove(threeChoiceTemp.get(2));
							keepSwitchin = false;
							i++; pickNumber++;
							break;
						case "c/text":
							System.out.println("\n"+ threeChoiceTemp.get(2).getText());
							System.out.println("\nPick #" + pickNumber);
							System.out.println("-------------------------");
							System.out.println("A -- " + threeChoiceTemp.get(0).getName() + " -- " + threeChoiceTemp.get(0).getRarity());
							System.out.println("B -- " + threeChoiceTemp.get(1).getName() + " -- " + threeChoiceTemp.get(1).getRarity());
							System.out.println("C -- " + threeChoiceTemp.get(2).getName() + " -- " + threeChoiceTemp.get(2).getRarity());
							System.out.println("Enter the letter of your choice: ");
							chosenCard = scan.next();
							break;
						case "c/stats":
							System.out.println("\nATK: "+ threeChoiceTemp.get(2).getAtk() + "  DEF: " + threeChoiceTemp.get(2).getDef() + "\nType: " + threeChoiceTemp.get(2).getType() + "  Attribute: " + threeChoiceTemp.get(2).getAttribute() + "\nType: " + threeChoiceTemp.get(2).getCardType());
							System.out.println("\nPick #" + pickNumber);
							System.out.println("-------------------------");
							System.out.println("A -- " + threeChoiceTemp.get(0).getName() + " -- " + threeChoiceTemp.get(0).getRarity());
							System.out.println("B -- " + threeChoiceTemp.get(1).getName() + " -- " + threeChoiceTemp.get(1).getRarity());
							System.out.println("C -- " + threeChoiceTemp.get(2).getName() + " -- " + threeChoiceTemp.get(2).getRarity());
							System.out.println("Enter the letter of your choice: ");
							chosenCard = scan.next();
							break;
						case "c/type":
							System.out.println("\n"+ threeChoiceTemp.get(2).getCardType());
							System.out.println("\nPick #" + pickNumber);
							System.out.println("-------------------------");
							System.out.println("A -- " + threeChoiceTemp.get(0).getName() + " -- " + threeChoiceTemp.get(0).getRarity());
							System.out.println("B -- " + threeChoiceTemp.get(1).getName() + " -- " + threeChoiceTemp.get(1).getRarity());
							System.out.println("C -- " + threeChoiceTemp.get(2).getName() + " -- " + threeChoiceTemp.get(2).getRarity());
							System.out.println("Enter the letter of your choice: ");
							chosenCard = scan.next();
							break;
						case "reroll":
							if (rerolled)
							{ 
								System.out.println("You can only reroll 1 time");
								System.out.println("\nPick #" + pickNumber);
								System.out.println("-------------------------");
								System.out.println("A -- " + threeChoiceTemp.get(0).getName() + " -- " + threeChoiceTemp.get(0).getRarity());
								System.out.println("B -- " + threeChoiceTemp.get(1).getName() + " -- " + threeChoiceTemp.get(1).getRarity());
								System.out.println("C -- " + threeChoiceTemp.get(2).getName() + " -- " + threeChoiceTemp.get(2).getRarity());
								System.out.println("\n" + draftPool.size() + " cards left in your pool.");
								System.out.println("Enter the letter of your choice: ");
								chosenCard = scan.next();
								break; 
							}
							else
							{
								System.out.println("\nAll 3 cards have been discarded and you have rerolled 3 new cards");
								draftPool.remove(threeChoiceTemp.get(0));
								draftPool.remove(threeChoiceTemp.get(1));
								draftPool.remove(threeChoiceTemp.get(2));
								keepSwitchin = false;
								rerolled = true;
								pickNumber++;
								break;
							}
						case "deck":
							int cardNumber = 1;
							Card comparator3 = new Card();
							Collections.sort(drafted, comparator3);
							drafted = cardCounter(drafted);
							System.out.println("\n-------------------------");
							for (int j = 0; j < drafted.size(); j++)
							{
								if (drafted.get(j).getQuantity() > 0)
								{
									System.out.println("#" + cardNumber + " - " + drafted.get(j).getName() + " - x" + drafted.get(j).getQuantity() + " - " + drafted.get(j).getType());
									cardNumber++;
								}
							}
							System.out.println("\nFavored type: " + favoredType);
							System.out.println("Picked Synergy: " + pickedSynergy);
							System.out.println("-------------------------");
							System.out.println("\nPick #" + pickNumber);
							System.out.println("-------------------------");
							System.out.println("A -- " + threeChoiceTemp.get(0).getName() + " -- " + threeChoiceTemp.get(0).getRarity());
							System.out.println("B -- " + threeChoiceTemp.get(1).getName() + " -- " + threeChoiceTemp.get(1).getRarity());
							System.out.println("C -- " + threeChoiceTemp.get(2).getName() + " -- " + threeChoiceTemp.get(2).getRarity());
							System.out.println("Enter the letter of your choice: ");
							chosenCard = scan.next();
							break;
						case "pool":
							int cardNumber2 = 1;
							ArrayList<Card> tempList = copyPool(draftPool);
							tempList = cardCounter(tempList);
							Card comparator = new Card();
							Collections.sort(tempList, comparator);
							System.out.println("\n-------------------------");
							for (int t = 0; t < tempList.size(); t++) 
							{ 
								if (tempList.get(t).getQuantity() > 0)
								{
									System.out.println("#" + cardNumber2 + " - " + tempList.get(t).getName() + " - x" + tempList.get(t).getQuantity()); cardNumber2++; 
								}
							}
							System.out.println("-------------------------");
							System.out.println("\nPick #" + pickNumber);
							System.out.println("-------------------------");
							System.out.println("A -- " + threeChoiceTemp.get(0).getName() + " -- " + threeChoiceTemp.get(0).getRarity());
							System.out.println("B -- " + threeChoiceTemp.get(1).getName() + " -- " + threeChoiceTemp.get(1).getRarity());
							System.out.println("C -- " + threeChoiceTemp.get(2).getName() + " -- " + threeChoiceTemp.get(2).getRarity());
							System.out.println("Enter the letter of your choice: ");
							chosenCard = scan.next();
							break;
						case "pool/ultimate":
							cardNumber2 = 1;
							int quantityOf = 0;
							tempList = copyPool(draftPool);
							tempList = cardCounter(tempList);
							comparator = new Card();
							Collections.sort(tempList, comparator);
							System.out.println("\n-------------------------");
							for (int t = 0; t < tempList.size(); t++) 
							{ 
								if ((tempList.get(t).getQuantity() > 0) && (tempList.get(t).getRarity().equals("Ultimate Rare")))
								{
									System.out.println("#" + cardNumber2 + " - " + tempList.get(t).getName() + " - x" + tempList.get(t).getQuantity()); cardNumber2++; 
									quantityOf += tempList.get(t).getQuantity();
								}
							}
							System.out.println("# of ultimate rares: " + quantityOf);
							System.out.println("-------------------------");
							System.out.println("\nPick #" + pickNumber);
							System.out.println("-------------------------");
							System.out.println("A -- " + threeChoiceTemp.get(0).getName() + " -- " + threeChoiceTemp.get(0).getRarity());
							System.out.println("B -- " + threeChoiceTemp.get(1).getName() + " -- " + threeChoiceTemp.get(1).getRarity());
							System.out.println("C -- " + threeChoiceTemp.get(2).getName() + " -- " + threeChoiceTemp.get(2).getRarity());
							System.out.println("Enter the letter of your choice: ");
							chosenCard = scan.next();
							break;
						case "pool/ultra":
							cardNumber2 = 1;
							quantityOf = 0;
							tempList = copyPool(draftPool);
							tempList = cardCounter(tempList);
							comparator = new Card();
							Collections.sort(tempList, comparator);
							System.out.println("\n-------------------------");
							for (int t = 0; t < tempList.size(); t++) 
							{ 
								if ((tempList.get(t).getQuantity() > 0) && (tempList.get(t).getRarity().equals("Ultra Rare")))
								{
									System.out.println("#" + cardNumber2 + " - " + tempList.get(t).getName() + " - x" + tempList.get(t).getQuantity()); cardNumber2++; 
									quantityOf += tempList.get(t).getQuantity();
								}
							}
							System.out.println("# of ultra rares: " + quantityOf);
							System.out.println("-------------------------");
							System.out.println("\nPick #" + pickNumber);
							System.out.println("-------------------------");
							System.out.println("A -- " + threeChoiceTemp.get(0).getName() + " -- " + threeChoiceTemp.get(0).getRarity());
							System.out.println("B -- " + threeChoiceTemp.get(1).getName() + " -- " + threeChoiceTemp.get(1).getRarity());
							System.out.println("C -- " + threeChoiceTemp.get(2).getName() + " -- " + threeChoiceTemp.get(2).getRarity());
							System.out.println("Enter the letter of your choice: ");
							chosenCard = scan.next();
							break;
						case "pool/super":
							cardNumber2 = 1;
							quantityOf = 0;
							tempList = copyPool(draftPool);
							tempList = cardCounter(tempList);
							comparator = new Card();
							Collections.sort(tempList, comparator);
							System.out.println("\n-------------------------");
							for (int t = 0; t < tempList.size(); t++) 
							{ 
								if ((tempList.get(t).getQuantity() > 0) && (tempList.get(t).getRarity().equals("Super Rare")))
								{
									System.out.println("#" + cardNumber2 + " - " + tempList.get(t).getName() + " - x" + tempList.get(t).getQuantity()); cardNumber2++; 
									quantityOf += tempList.get(t).getQuantity();
								}
							}
							System.out.println("# of super rares: " + quantityOf);
							System.out.println("-------------------------");
							System.out.println("\nPick #" + pickNumber);
							System.out.println("-------------------------");
							System.out.println("A -- " + threeChoiceTemp.get(0).getName() + " -- " + threeChoiceTemp.get(0).getRarity());
							System.out.println("B -- " + threeChoiceTemp.get(1).getName() + " -- " + threeChoiceTemp.get(1).getRarity());
							System.out.println("C -- " + threeChoiceTemp.get(2).getName() + " -- " + threeChoiceTemp.get(2).getRarity());
							System.out.println("Enter the letter of your choice: ");
							chosenCard = scan.next();
							break;
						case "pool/rare":
							cardNumber2 = 1;
							quantityOf = 0;
							tempList = copyPool(draftPool);
							tempList = cardCounter(tempList);
							comparator = new Card();
							Collections.sort(tempList, comparator);
							System.out.println("\n-------------------------");
							for (int t = 0; t < tempList.size(); t++) 
							{ 
								if ((tempList.get(t).getQuantity() > 0) && (tempList.get(t).getRarity().equals("Rare")))
								{
									System.out.println("#" + cardNumber2 + " - " + tempList.get(t).getName() + " - x" + tempList.get(t).getQuantity()); cardNumber2++; 
									quantityOf += tempList.get(t).getQuantity();
								}
							}
							System.out.println("# of rares: " + quantityOf);
							System.out.println("-------------------------");
							System.out.println("\nPick #" + pickNumber);
							System.out.println("-------------------------");
							System.out.println("A -- " + threeChoiceTemp.get(0).getName() + " -- " + threeChoiceTemp.get(0).getRarity());
							System.out.println("B -- " + threeChoiceTemp.get(1).getName() + " -- " + threeChoiceTemp.get(1).getRarity());
							System.out.println("C -- " + threeChoiceTemp.get(2).getName() + " -- " + threeChoiceTemp.get(2).getRarity());
							System.out.println("Enter the letter of your choice: ");
							chosenCard = scan.next();
							break;
						case "pool/common":
							cardNumber2 = 1;
							quantityOf = 0;
							tempList = copyPool(draftPool);
							tempList = cardCounter(tempList);
							comparator = new Card();
							Collections.sort(tempList, comparator);
							System.out.println("\n-------------------------");
							for (int t = 0; t < tempList.size(); t++) 
							{ 
								if ((tempList.get(t).getQuantity() > 0) && (tempList.get(t).getRarity().equals("Common")))
								{
									System.out.println("#" + cardNumber2 + " - " + tempList.get(t).getName() + " - x" + tempList.get(t).getQuantity()); cardNumber2++; 
									quantityOf += tempList.get(t).getQuantity();
								}
							}
							System.out.println("# of commons: " + quantityOf);
							System.out.println("-------------------------");
							System.out.println("\nPick #" + pickNumber);
							System.out.println("-------------------------");
							System.out.println("A -- " + threeChoiceTemp.get(0).getName() + " -- " + threeChoiceTemp.get(0).getRarity());
							System.out.println("B -- " + threeChoiceTemp.get(1).getName() + " -- " + threeChoiceTemp.get(1).getRarity());
							System.out.println("C -- " + threeChoiceTemp.get(2).getName() + " -- " + threeChoiceTemp.get(2).getRarity());
							System.out.println("Enter the letter of your choice: ");
							chosenCard = scan.next();
							break;
						case "pool/synergy":
							cardNumber2 = 1;
							quantityOf = 0;
							tempList = copyPool(draftPool);
							tempList = cardCounter(tempList);
							comparator = new Card();
							Collections.sort(tempList, comparator);
							System.out.println("\n-------------------------");
							for (int t = 0; t < tempList.size(); t++) 
							{ 
								if ((tempList.get(t).getQuantity() > 0) && (tempList.get(t).getSynergies().contains(pickedSynergy)))
								{
									System.out.println("#" + cardNumber2 + " - " + tempList.get(t).getName() + " - x" + tempList.get(t).getQuantity() + " - " + tempList.get(t).getSynergies()); cardNumber2++; 
									quantityOf += tempList.get(t).getQuantity();
								}
							}
							System.out.println("# of cards with the picked synergy: " + quantityOf);
							System.out.println("-------------------------");
							System.out.println("\nPick #" + pickNumber);
							System.out.println("-------------------------");
							System.out.println("A -- " + threeChoiceTemp.get(0).getName() + " -- " + threeChoiceTemp.get(0).getRarity());
							System.out.println("B -- " + threeChoiceTemp.get(1).getName() + " -- " + threeChoiceTemp.get(1).getRarity());
							System.out.println("C -- " + threeChoiceTemp.get(2).getName() + " -- " + threeChoiceTemp.get(2).getRarity());
							System.out.println("Enter the letter of your choice: ");
							chosenCard = scan.next();
							break;
						case "pool/type":
							cardNumber2 = 1;
							quantityOf = 0;
							tempList = copyPool(draftPool);
							tempList = cardCounter(tempList);
							comparator = new Card();
							Collections.sort(tempList, comparator);
							System.out.println("\n-------------------------");
							for (int t = 0; t < tempList.size(); t++) 
							{ 
								if ((tempList.get(t).getQuantity() > 0) && (tempList.get(t).getType().equals(favoredType)))
								{
									System.out.println("#" + cardNumber2 + " - " + tempList.get(t).getName() + " - x" + tempList.get(t).getQuantity() + " - " + tempList.get(t).getSynergies()); cardNumber2++; 
									quantityOf += tempList.get(t).getQuantity();
								}
							}
							System.out.println("# of cards with your favored type: " + quantityOf);
							System.out.println("-------------------------");
							System.out.println("\nPick #" + pickNumber);
							System.out.println("-------------------------");
							System.out.println("A -- " + threeChoiceTemp.get(0).getName() + " -- " + threeChoiceTemp.get(0).getRarity());
							System.out.println("B -- " + threeChoiceTemp.get(1).getName() + " -- " + threeChoiceTemp.get(1).getRarity());
							System.out.println("C -- " + threeChoiceTemp.get(2).getName() + " -- " + threeChoiceTemp.get(2).getRarity());
							System.out.println("Enter the letter of your choice: ");
							chosenCard = scan.next();
							break;
						case "exit":
							System.exit(0);
						case "quit":
							System.exit(0);
						case "terminate":
							System.exit(0);
						case "Obliterate!":
							System.out.println("holy fuck the whole systems gone haywire");
							System.out.println("fuckin' exodia always fuckin' everything up");
							System.exit(0);
						case "obliterate!":
							System.out.println("holy fuck the whole systems gone haywire");
							System.out.println("fuckin' exodia always fuckin' everything up");
							System.exit(0);
						default:
							System.out.println("Bad input!\n");
							System.out.println("\nPick #" + pickNumber);
							System.out.println("-------------------------");
							System.out.println("A -- " + threeChoiceTemp.get(0).getName() + " -- " + threeChoiceTemp.get(0).getRarity());
							System.out.println("B -- " + threeChoiceTemp.get(1).getName() + " -- " + threeChoiceTemp.get(1).getRarity());
							System.out.println("C -- " + threeChoiceTemp.get(2).getName() + " -- " + threeChoiceTemp.get(2).getRarity());
							System.out.println("Enter the letter of your choice: ");
							chosenCard = scan.next();
							break;
						}
					}
				}
				else { i = cardsToDraft; }
			}
			drafted = cardCounter(drafted);
			draftDecks.add(drafted);
			noOfPlayers--; playerDrafting++;
			if (noOfPlayers > 0) { System.out.println("\nOK! You are done drafting. Next player."); }
		} // END Draft While()
		

		System.out.println("\nDraft complete! " + playerCount + " players each drafted a pool of " + draftDecks.get(0).size() + " cards." );
		for (int i = 0; i < playerCount; i++)
		{
			System.out.println("\nPlayer " + (i+1) + "'s Pool" + " -- " + draftDecks.get(i).size() + " cards");
			System.out.println("---------------\n");
			int cardNumber = 1;
			Card compare = new Card();
			Collections.sort(draftDecks.get(i), compare);
			for (int j = 0; j < draftDecks.get(i).size(); j++)
			{
				if (draftDecks.get(i).get(j).getQuantity() > 0)
				{
					System.out.println("#" + cardNumber + " - " + draftDecks.get(i).get(j).getName() + " - x" + draftDecks.get(i).get(j).getQuantity());
					cardNumber++;
				}
			}

		}
		*/
		
		allCards.clear(); drafted.clear(); draftPool.clear();


	} // END Main
	
	
	
	// DRAFT METHODS

	// Creates a pool of cards for the currently drafting player
	// Should enforce some sort of pool limits on cards that have a lot of copies (ex: monster reborn has 21 copies)
	static ArrayList<Card> poolFill(ArrayList<Card> allCards, int poolSize)
	{
		ArrayList<Card> draftPool = new ArrayList<Card>();
		for (int i = 0; i < poolSize; i++)
		{ 
			draftPool.add(randomCardRemove(allCards));
			draftPool.get(draftPool.size() - 1).setQuantity(1);
		}

		return draftPool;
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

	static ArrayList<Card> drafter(ArrayList<ArrayList<Card>> draftPool, ArrayList<String> types, int cardsToDraft, int player)
	{
		// Variable Init
		int pickNumber = 1;
		boolean rerolled = false;
		String favoredType = "";
		String activeSynergy = "";
		ArrayList<Card> drafted = new ArrayList<Card>();
		ArrayList<Card> picks = new ArrayList<Card>();
		// End Variable Init

		// Setup favored type and active synergy
		types = typeGen(draftPool.get(player));
		favoredType = randomType(types);
		activeSynergy = synGen(draftPool.get(player));

		for (int i = 0; i < cardsToDraft;)
		{
			if (drafted.size() > 0)
			{ 
				favoredType = typeCounter(drafted); 
				activeSynergy = synGen(drafted);
			}
			picks = threeChoices(draftPool.get(player), drafted, favoredType, activeSynergy);
			// Look for GUI input as to which card was selected
			// add that card to drafted
			// remove the other two from the pool
			// count cards in drafted
			// sort drafted
		}

		drafted = cardCounter(drafted);	
		return drafted;
	}
	// Generates a list of 'random' picks from draftPool
	// Makes sure those cards exist in the pool, are not duplicates of each other and picking any of them should not break deck limits
	static ArrayList<Card> picks(ArrayList<Card> draftPool, ArrayList<Card> deck, String favoredType, String activeSynergy, int choices)
	{
		ArrayList<Card> picks = new ArrayList<Card>();
		//ArrayList<Card> tempList = new ArrayList<Card>();
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
				/*
				// Favored Type
				else if (seed >= 910 && seed < 990) 
				{
					
					
				}
				*/
				/*
				//Active Synergy
				else if (seed >= 770 && seed < 850) 
				{ 
					boolean rolling2 = true;
					int counter = 0;
					while (counter < 4)
					{
						int seed2 = oneKDie();
						if (seed2 < 800) { counter += synergyRoll(activeSynergy, seed, rolling2, draftPool, common, temp, roll, seedValue, picks, deck); roll = "Syn - Common"; }
						else if (seed2 >= 800 && seed2 < 975) { counter += synergyRoll(activeSynergy, seed, rolling2, draftPool, rare, temp, roll, seedValue, picks, deck);roll = "Syn - Rare"; }
						else if (seed2 >= 995) { counter += synergyRoll(activeSynergy, seed, rolling2, draftPool, superR, temp, roll, seedValue, picks, deck); roll = "Syn - Super Rare"; }
						else { counter += synergyRoll(activeSynergy, seed, rolling2, draftPool, common, temp, roll, seedValue, picks, deck); }
					}
					
				
				}
				*/
				// Backup
				else { rarityRollBackup(draftPool, common, deck, temp, roll, seedValue, picks, rolling, seed); roll = "Purely Random"; }
			}
			//System.out.println("Roll: " + roll);
		}
		
		
		return picks;
	}

	// Generates an list of three 'random' cards from a given pool
	// Makes sure those cards exist in the pool, are not duplicates of each other and have the same rarity
	static ArrayList<Card> threeChoices(ArrayList<Card> draftPool, ArrayList<Card> drafted, String favoredType, String pickedSynergy)
	{
		ArrayList<Card> threeChoices = new ArrayList<Card>();
		ArrayList<Card> tempList = new ArrayList<Card>();
		Card temp = new Card("dummy");
		boolean cardCheck = true;
		boolean cardCheck2 = true;
		String common = "Common";
		String rare = "Rare";
		String superR = "Super Rare";
		String ultra = "Ultra Rare";
		String ultimate = "Ultimate Rare";
		String roll = "";
		String seedValue = "";

		for (int p = 0; p < 3; p++)
		{
			while (cardCheck)
			{
				int seed = oneKDie();
				temp = new Card("dummy");
				// Common - 69% chance
				if (seed < 690) 
				{ 
					temp = randomCardRarity(draftPool, common); 
					roll = common; seedValue = String.valueOf(seed); 
				}									

				// Rare - 8% chance
				else if (seed >= 690 && seed < 770) 
				{ 
					temp = randomCardRarity(draftPool, rare); 
					roll = rare; seedValue = String.valueOf(seed);
				}

				// Super Rare - 4% chance
				else if (seed >= 850 && seed < 890) 
				{ 
					temp = randomCardRarity(draftPool, superR); 
					roll = superR; seedValue = String.valueOf(seed); 
				}	

				// Ultra Rare - 2% chance
				else if (seed >= 890 && seed < 910)
				{ 
					temp = randomCardRarity(draftPool, ultra); 
					roll = ultra; seedValue = String.valueOf(seed); 
				}				

				// Ultimate Rare - 1% chance
				else if (seed >= 990 && seed <= 1000) 
				{ 
					temp = randomCardRarity(draftPool, ultimate); 
					roll = ultimate; seedValue = String.valueOf(seed);
				}			

				// Picked Synergy - 8% chance
				else if (seed >= 770 && seed < 850)																												
				{	
					// If theres some synergy cards available, roll again
					if (someSynergy(draftPool, tempList, pickedSynergy))
					{
						int seed3 = oneKDie();	
						// Roll until you find one of them
						while (temp.getName().equals("dummy"))
						{
							if (seed3 < 800) { temp = randomCardRarity(tempList, common); roll = ("Picked Synergy - Common " + pickedSynergy); }							
							else if (seed3 >= 800 && seed3 < 975) { temp = randomCardRarity(tempList, rare); roll = ("Picked Synergy - Rare " + pickedSynergy); }			
							else if (seed3 >= 975 && seed3 < 995) { temp = randomCardRarity(tempList, superR); roll = ("Picked Synergy - Super Rare " + pickedSynergy); }	
							else if (seed3 >= 995) { temp = randomCardRarity(tempList, ultra); roll = ("Picked Synergy - Ultra Rare " + pickedSynergy); }					
							else { temp = randomCardRarity(tempList, common); roll = ("Picked Synergy - WhoopsieCommon " + pickedSynergy); }
							tempList = new ArrayList<Card>();													
							seedValue = String.valueOf(seed);
						}
					}				
				}	

				// Favored Type - 8% chance
				else if (seed >= 910 && seed < 990) 															
				{ 																								
					if (someType(draftPool, tempList, favoredType))
					{
						int seed2 = oneKDie();	
						// Roll until you find one of them
						while (temp.getName().equals("dummy"))
						{
							seed2 = oneKDie();																		
							if (seed2 < 800) { temp = randomCardRarity(tempList, common); roll = "Favored Type - Common"; }								
							else if (seed2 >= 800 && seed2 < 975) { temp = randomCardRarity(tempList, rare); roll = "Favored Type - Rare"; }			
							else if (seed2 >= 975 && seed2 < 995) { temp = randomCardRarity(tempList, superR); roll = "Favored Type - Super Rare"; }	
							else if (seed2 >= 995) { temp = randomCardRarity(tempList, ultra); roll = "Favored Type - Ultra Rare"; }					
							else { temp = randomCardRarity(tempList, common); roll = "Favored Type - WhoopsieCommon"; }
							tempList = new ArrayList<Card>();													
							seedValue = String.valueOf(seed);
						}
					}
				}			

				else 
				{ 
					temp = randomCardRarity(draftPool, common); 
					roll = "Uh oh. Backup common!"; 
					seedValue = String.valueOf(seed); 
				}

				if (limiter(drafted, temp)) { cardCheck = false; }
				else { cardCheck = true; System.out.println("Limited the addition of: " + temp.getName()); }

				if (threeChoices.isEmpty() == false)
				{
					while (cardCheck2 && (cardCheck == false))
					{
						for (Card card : threeChoices)
						{
							if (temp.getName().equals(card.getName())) { cardCheck2 = true; break; }
							else { cardCheck2 = false; }
						}

						if (cardCheck2 == true) { cardCheck = true; }
					}
				}
			}

			if (temp.getName().equals("dummy")) { p--; roll = "No synergy? " + pickedSynergy; }
			else { threeChoices.add(temp); }
			System.out.println("Roll: " + roll + "- Seed: " + seedValue);
			cardCheck = true; cardCheck2 = true; 

		}
		return threeChoices;
	}
	
	static void ultimatePrinter(ArrayList<Card> draftPool, ArrayList<Card> threeChoiceTemp, int pickNumber)
	{
		int cardNumber2 = 1;
		int quantityOf = 0;
		ArrayList<Card >tempList = copyPool(draftPool);
		tempList = cardCounter(tempList);
		Card comparator = new Card();
		Collections.sort(tempList, comparator);
		System.out.println("\n-------------------------");
		for (int t = 0; t < tempList.size(); t++) 
		{ 
			if ((tempList.get(t).getQuantity() > 0) && (tempList.get(t).getRarity().equals("Ultimate Rare")))
			{
				System.out.println("#" + cardNumber2 + " - " + tempList.get(t).getName() + " - x" + tempList.get(t).getQuantity()); cardNumber2++; 
				quantityOf += tempList.get(t).getQuantity();
			}
		}
		System.out.println("# of ultimate rares: " + quantityOf);
		System.out.println("-------------------------");
		System.out.println("\nPick #" + pickNumber);
		System.out.println("-------------------------");
		System.out.println("A -- " + threeChoiceTemp.get(0).getName() + " -- " + threeChoiceTemp.get(0).getRarity());
		System.out.println("B -- " + threeChoiceTemp.get(1).getName() + " -- " + threeChoiceTemp.get(1).getRarity());
		System.out.println("C -- " + threeChoiceTemp.get(2).getName() + " -- " + threeChoiceTemp.get(2).getRarity());
		System.out.println("Enter the letter of your choice: ");
	}

	// Returns a number between 1-1000
	static int oneKDie()
	{
		Random seed = new Random();
		int seedValue = seed.nextInt(1000);
		return seedValue;
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
				if (drafted.get(i).getQuantity() + 1 > drafted.get(i).getLimit())
				{
					checker = false;
				}
			}
		}

		return checker;
	}

	// Runs through your pool and checks to see that no deck limits are being broken
	// Returns true if the card could be added to your pool
	static boolean limiter2(ArrayList<Card> drafted, Card card)
	{
		boolean checker = true;
		int quantity = 0;
		for (int i = 0; i < drafted.size(); i++)
		{
			if (card.getName().equals(drafted.get(i).getName()))
			{
				quantity += drafted.get(i).getQuantity();
			}

			if (quantity + 1 > drafted.get(i).getLimit())
			{
				checker = false;
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
						//System.out.println("Removed " + temp.getName() + " from your pool.");
					}
				}
			}
		}
		
		tempList.removeAll(tempList2);
		return tempList;
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
					roll = rarity; seedValue = String.valueOf(seed); picks.add(temp); checker = false;
				}
			}
		}
		
		return checker;
	}
	
	static int synergyRoll(String activeSynergy, int seed, boolean rolling, ArrayList<Card> draftPool, String rarity, Card temp, String roll, String seedValue, ArrayList<Card> picks, ArrayList<Card> deck)
	{
		boolean checker = rolling;
		if (isThereRandomSynergy(draftPool, activeSynergy))
		{
			if (isThereRandomRarity(draftPool, rarity))
			{
				temp = randomCardRarity(draftPool, rarity);
				if ((limiter(deck, temp)) && noDupes(picks, temp)) { roll = rarity; seedValue = String.valueOf(seed); picks.add(temp); checker = false; }
				else 
				{
					while (limiter(deck, temp) == false)
					{
						while (noDupes(picks,temp) == false)
						{
							temp = randomCardRarity(draftPool, rarity);
						}
					}
					roll = rarity; seedValue = String.valueOf(seed); picks.add(temp); checker = false;
				}
			}
			else { checker = false; }
		}
		else { checker = false;}
		
		return 1;
	}
	
	// Determines the true size of the pool
	// Multiplies each card by its quantity and sums the total number of cards
	static int cardCount(ArrayList<Card> pool)
	{
		int count = 0;
		for (int i = 0; i < pool.size(); i++)
		{
			count += pool.get(i).getQuantity();
		}

		return count;
	}

	// After you have drafted all your cards runs through and totals up how many of each card for nice output
	static ArrayList<Card> cardCounter(ArrayList<Card> drafted)
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


		return temp3;
	}
	
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
	
	// Checks every card in the passed pool to see if any match the passed type
	// If there are no cards at all that match returns false, otherwise return true
	static boolean isThereRandomType(ArrayList<Card> pool, String type)
	{
		boolean checker = false;
		for (Card card : pool)
		{
			if (card.getType().equals(type)) { checker = true; }
		}
		return checker;
	}

	// Pulls a random card with the given type from the given pool
	static Card randomCardType(ArrayList<Card> pool, String type)
	{
		boolean looking = true;
		Card randomCard = new Card();
		while (looking)
		{
			randomCard = randomCard(pool);
			if (randomCard.getType().equals(type)) { looking = false; }
		}

		return randomCard;
	}
	
	// Checks every card in the passed pool to see if any match the passed type
	// If there are no cards at all that match returns false, otherwise return true
	static boolean isThereRandomAttribute(ArrayList<Card> pool, String attribute)
	{
		boolean checker = false;
		for (Card card : pool)
		{
			if (card.getAttribute().equals(attribute)) { checker = true; }
		}
		return checker;
	}
	
	// Pulls a random card with the given attribute from the given pool
	static Card randomCardAttribute(ArrayList<Card> pool, String attribute)
	{
		boolean looking = true;
		Card randomCard = new Card();
		while (looking)
		{
			randomCard = randomCard(pool);
			if (randomCard.getAttribute().equals(attribute)) { looking = false; }
		}

		return randomCard;
	}
	
	// Checks every card in the passed pool to see if any match the passed synergy
	// If there are no cards at all that match returns false, otherwise return true
	static boolean isThereRandomSynergy(ArrayList<Card> pool, String synergy)
	{
		boolean checker = false;
		for (Card card : pool)
		{
			String[] synergies = synergyFill(card);
			for (String syn : synergies)
			{
				if (syn.equals(synergy)) { checker = true; }
			}
		}
		return checker;
	}

	// Pulls a random card with the given synergy from the given pool 
	static Card randomCardSynergy(ArrayList<Card> pool, String synergy)
	{
		boolean looking = true;
		Card randomCard = new Card();
		while (looking)
		{
			randomCard = randomCard(pool);
			String[] synergies = synergyFill(randomCard);
			for (String syn : synergies)
			{
				if (syn.equals(synergy)) { looking = false; }
			}
		}

		return randomCard;
	}

	// Runs through a cards synergies and returns a string[] containing them
	static String[] synergyFill(Card theCard)
	{
		String[] synergies = theCard.getSynergies().split("&");
		return synergies;
	}

	// Returns a random type from the array of types passed in
	static String randomType(ArrayList<String> types)
	{
		Random iterator = new Random();
		int rand = iterator.nextInt(types.size());
		String type = types.get(rand);
		return type;
	}

	// Runs through your pool of drafted cards and fills up a list with all your synergies
	// Returns a random synergy out of that list
	static String synGen(ArrayList<Card> drafted)
	{
		ArrayList<String> availableSyn = new ArrayList<String>();
		String pickedSynergy = "";

		for (int m = 0; m < drafted.size(); m++)
		{
			String[] cardSyns = synergyFill(drafted.get(m));

			for (String syn : cardSyns)
			{
				availableSyn.add(syn);
			}
		}

		Random seed4 = new Random();
		int seed4Value = seed4.nextInt(availableSyn.size());
		pickedSynergy = availableSyn.get(seed4Value); 
		return pickedSynergy;
	}

	// Runs through your pool of cards and determines your favored type
	static String typeCounter(ArrayList<Card> drafted)
	{
		int typeCount = 0;
		String favoredType = "";

		ArrayList<Card> poolCopy1 = new ArrayList<Card>();
		ArrayList<Card> poolCopy2 = new ArrayList<Card>();
		poolCopy1 = copyPool(drafted);
		poolCopy2 = copyPool(drafted);

		for (int i = 0; i < poolCopy1.size(); i++)
		{
			int counter = 0;
			if (poolCopy1.get(i).getType().equals("none")) { }
			else 
			{
				for (int k = 0; k < poolCopy2.size(); k++)
				{
					boolean save = poolCopy1.get(i).getType().equals(poolCopy2.get(k).getType());
					if (save)
					{
						counter++;
						if (counter > typeCount) { favoredType = poolCopy1.get(i).getType(); typeCount = counter;}
					}
				}
			}
		}
		//System.out.println("New favored type: " + favoredType);
		return favoredType;
	}
	
	// Returns an arraylist of all the types in the passed pool
	static ArrayList<String> typeGen(ArrayList<Card> pool)
	{
		ArrayList<String> types = new ArrayList<String>();
		for (Card card : pool)
		{
			if (types.contains(card.getType())) { }
			else 
			{
				types.add(card.getType());
			}
		}

		return types;
	}

	// Pulls a random card from the given pool (equal weight to all cards)
	static Card randomCard(ArrayList<Card> allCards)
	{
		Random seed = new Random();
		int seedValue = seed.nextInt(allCards.size());
		Card selected = allCards.get(seedValue);
		return selected;
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

	// Fills templist with all the cards from pool that match the passed synergy
	// Returns false if no cards available
	static boolean someSynergy(ArrayList<Card> pool, ArrayList<Card> tempList, String pickedSynergy)
	{
		for (int m = 0; m < pool.size(); m++)
		{							
			if (pool.get(m).getSynergies().equals(pickedSynergy))
			{
				Card newCard = new Card(pool.get(m));											// Copy the cards from the pool that match the rolled synergy
				tempList.add(newCard);																// List of all cards in your pool that match that synergy
			}
		}

		if (tempList.size() > 0) { return true; }
		else { return false; }
	}

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
	
	@SuppressWarnings("resource")
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
	
	// Function that opens the database and reads the cards into allCards
	// allCards will contain copies of each card equal to their quantity in the database
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

	// Prompts the user to find out how many cards they would like to draft and sets up necessary variables
	static int howManyToDraft(boolean scanCheck, Scanner scan, int realTotalCards, int poolSize)
	{
		int cardsToDraft = 1;
		System.out.println("There are " + realTotalCards + " cards available in the database.");
		System.out.println("Each player will draft from a pool of " + poolSize + " cards, randomly chosen from the database.\n");
		while(scanCheck)
		{
			try 
			{
				System.out.println("Enter the number of cards to draft from that pool: ");
				cardsToDraft = scan.nextInt();
				scanCheck = false;
			} 

			catch (InputMismatchException e) 
			{
				System.out.println("Bad input!");
				@SuppressWarnings("unused") String a = scan.next();
			}
		}

		return cardsToDraft;
	}

	// Fills templist with all the cards from pool that match the passed type
	// Returns false if no cards available
	static boolean someType(ArrayList<Card> pool, ArrayList<Card> tempList, String favoredType)
	{
		for (int m = 0; m < pool.size(); m++)
		{							
			if (pool.get(m).getType().equals(favoredType))
			{
				Card newCard = new Card(pool.get(m));											// Copy the cards from the pool that match the rolled synergy
				tempList.add(newCard);																// List of all cards in your pool that match that synergy
			}
		}

		if (tempList.size() > 0) { return true; }
		else { return false; }
	}

	// Prompts the user for number of players and then sets up the decks and pools necessary to allow that number of players to draft decks
	static int poolDeckInit(ArrayList<ArrayList<Card>> draftPools, ArrayList<ArrayList<Card>> draftDecks, Scanner scan, boolean scanCheck)
	{
		int noOfPlayers = 1;
		while(scanCheck)
		{
			try 
			{
				System.out.println("Enter number of players: ");
				noOfPlayers = scan.nextInt();
				scanCheck = false;
			} 

			catch (InputMismatchException e) 
			{
				System.out.println("Bad input!");
				@SuppressWarnings("unused") String a = scan.next();
			}
		}


		for (int i = 0; i < noOfPlayers; i++) { ArrayList<Card> deck = new ArrayList<Card>();	draftPools.add(deck); draftDecks.add(deck); }
		return noOfPlayers;
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
	
	public static ArrayList<Card> discoverSpell(ArrayList<Card> pool)
	{
		int spells = 0;
		boolean checker = false;
		cardCounter(pool);
		pool.sort(pool.get(0));
		ArrayList<Card> spellList = new ArrayList<Card>();
		ArrayList<Card> threeSpells = new ArrayList<Card>();
		for (Card card : pool)
		{
			if (card.getCardType().equals("Spell"))
			{
				spells++;
				spellList.add(card);
			}
		}
		
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
	
	public static ArrayList<Card> discoverEffectAttribute(ArrayList<Card> pool, String attribute)
	{
		int spells = 0;
		boolean checker = false;
		cardCounter(pool);
		pool.sort(pool.get(0));
		ArrayList<Card> spellList = new ArrayList<Card>();
		ArrayList<Card> threeSpells = new ArrayList<Card>();
		for (Card card : pool)
		{
			if (card.getAttribute().equals(attribute) && card.getCardType().equals("Effect Monster"))
			{
				spells++;
				spellList.add(card);
			}
		}
		
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
	
	public static void printRarities(ArrayList<Card> pool)
	{
		System.out.println("\n\nRARITY PRINT\n--------------------");
		//ArrayList<Card> allCardsNoDupes = listMaker(pool);
		int ultimates = 0; int ultras = 0; int supers = 0; int rares = 0; int commons = 0;
		for (Card card : pool)
		{	
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
					System.out.println("Default on rarity check. Card: " + card.getName());	break;
			}
			
		}
		
		System.out.println("Ultimate Rare: " + ultimates);
		System.out.println("Ultra Rare: " + ultras);
		System.out.println("Super Rare: " + supers);
		System.out.println("Rares " + rares);
		System.out.println("Commons: " + commons);
	}
	public static void imageCheck(ArrayList<Card> pool)
	{
		System.out.println("\n\nIMAGE CHECK\n--------------------");
		int unique = 0;
		for (Card card : pool)
		{			
			if (isImage("src/images/" + card.getName() + ".png")) { }
			else { System.out.print(card.getName() + "', '"); unique++; }
		}
		System.out.println("\nCards w/o Image: " + unique);
	}
	public static void crossLimitPrint(ArrayList<Card> pool)
	{
		System.out.println("\n\nCROSS LIMIT PRINT\n--------------------");
		for (Card card : pool)
		{
			if (!card.getCrosslimit().equals("none")) { System.out.println(card.getName() + " - " + card.getCrosslimit());}
		}
	}
	public static void textCheck(ArrayList<Card> pool)
	{
		System.out.println("\n\nTEXT CHECK\n--------------------");
		Card dummy = new Card("dummy");
		int textlessCounter = 0;
		for (Card card : pool)
		{
			if (card.getText().equals("(Text)"))
			{
				if (card.getName().equals(dummy.getName())) {}
				else { textlessCounter++; dummy = card; System.out.println(card.getName()); }
			}
		}
		
		System.out.println("\nCards w/o text: " + textlessCounter);
	}
	
	public static void monsterPrintAttribute(ArrayList<Card> pool, String attribute)
	{
		System.out.println("\n\n" + attribute + " MONSTERS\n--------------------");
		Card dummy = new Card("dummy");
		int textlessCounter = 0;
		for (Card card : pool)
		{
			if (card.getAttribute().equals(attribute))
			{
				if (card.getName().equals(dummy.getName())) {}
				else { textlessCounter++; dummy = card; System.out.println(card.getName()); }
			}
		}
		
		System.out.println("\n" + attribute + " Monsters: " + textlessCounter);
	}
	
	public static void monsterPrintType(ArrayList<Card> pool, String attribute)
	{
		System.out.println("\n\n" + attribute + " MONSTERS\n--------------------");
		Card dummy = new Card("dummy");
		int textlessCounter = 0;
		for (Card card : pool)
		{
			if (card.getType().equals(attribute))
			{
				if (card.getName().equals(dummy.getName())) {}
				else { textlessCounter++; dummy = card; System.out.println(card.getName()); }
			}
		}
		
		System.out.println("\n" + attribute + " Monsters: " + textlessCounter);
	}
	
	public static void cardDistrib(ArrayList<Card> pool)
	{
		System.out.println("\n\nCARD DISTRIBUTION\n--------------------");
		Card dummy = new Card("dummy");
		int normal = 0;
		int effect = 0;
		int fusion = 0;
		int spells = 0;
		int traps = 0;
		int ritual = 0;
		int other = 0;
		for (Card card : pool)
		{
			if (card.getCardType().equals("Effect Monster"))
			{
				effect++;
			}
			
			else if (card.getCardType().equals("Normal Monster"))
			{
				normal++;
			}
			
			else if (card.getCardType().equals("Fusion Monster"))
			{
				fusion++;
			}
			
			else if (card.getCardType().equals("Ritual Monster"))
			{
				ritual++;
			}
			
			else if (card.getCardType().equals("Spell"))
			{
				spells++;
			}
			
			else if (card.getCardType().equals("Trap"))
			{
				traps++;
			}
			
			else
			{
				other++;
			}
		}
		
		System.out.println("Normal: " + normal);
		System.out.println("Effect: " + effect);
		System.out.println("Fusion: " + fusion);
		System.out.println("Ritual: " + ritual);
		System.out.println("Spells: " + spells);
		System.out.println("Traps: " + traps);
		System.out.println("Other: " + other);
	}
	
	public static void parseHubFormat(ArrayList<Card> pool)
	{
		System.out.println("\n\nPARSEHUB FORMAT\n--------------------");
		Card dummy = new Card("dummy");
		int textlessCounter = 0;
		for (Card card : pool)
		{
			if (card.getText().equals("(Text)"))
			{
				if (card.getName().equals(dummy.getName())) {}
				else { textlessCounter++; dummy = card; System.out.print("\"" + card.getName() + "\", "); }
			}
		}
		
		System.out.println("\nCards w/o text: " + textlessCounter);
	}
	
	public static void printDiscoverSpell(ArrayList<Card> pool)
	{
		System.out.println("\n\nDISCOVER RANDOM SPELL\n--------------------");
		ArrayList<Card> discovered = discoverSpell(pool);
		for (int i = 0; i < 3; i++)
		{
			System.out.println("Spell #" + (i + 1) + ": " + discovered.get(i).getName());
		}
	}
	
	public static void printDiscoverEffectAttribute(ArrayList<Card> pool, String attribute)
	{
		System.out.println("\n\nDISCOVER RANDOM " + attribute + " MONSTER\n--------------------");
		ArrayList<Card> discovered = discoverEffectAttribute(pool, attribute);
		for (int i = 0; i < 3; i++)
		{
			System.out.println("Spell #" + (i + 1) + ": " + discovered.get(i).getName());
		}
	}}
 // END Class
