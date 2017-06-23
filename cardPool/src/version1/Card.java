package version1;

import java.util.Comparator;

public class Card implements Comparator<Card> 
{
	private String name;	private String attribute;	private String type;	private String cardType;
	private String text;	private String crosslimit;	private String rarity;	private String synergies;
	
	private int atk;	private int def;	private int lvl;	private int tierScore;
	private int limit;	private int quantity;	private boolean monster;	private boolean contin;
	private boolean quickplay;	private boolean counter;	private boolean field;	private boolean equip;
	private boolean ritual;	private boolean normal;
	
	public Card()	{}
	
	public Card(String name, String attribute, String type, String cardType,
			String text, String crosslimit, String rarity, String synergies,
			int atk, int def, int lvl, int tierScore, int limit, int quantity,
			boolean monster, boolean contin, boolean quickplay,
			boolean counter, boolean field, boolean equip, boolean ritual,
			boolean normal) 
	{
		super();
		this.name = name;	this.attribute = attribute;	this.type = type;	this.cardType = cardType;
		this.text = text;	this.crosslimit = crosslimit;	this.rarity = rarity;	this.synergies = synergies;
		this.atk = atk;	this.def = def;	this.lvl = lvl;	this.tierScore = tierScore;	this.limit = limit;
		this.quantity = quantity;	this.monster = monster;	this.contin = contin;	this.quickplay = quickplay;
		this.counter = counter;	this.field = field;	this.equip = equip;	this.ritual = ritual;	this.normal = normal;
	}
	
	
	public Card(Card card) 
	{
		this.name = card.getName();
		this.attribute = card.getAttribute();
		this.type = card.getType();
		this.cardType = card.getCardType();
		this.text = card.getText();
		this.crosslimit = card.getCrosslimit();
		this.rarity = card.getRarity();
		this.synergies = card.getSynergies();
		this.atk = card.getAtk();
		this.def = card.getDef();
		this.lvl = card.getLvl();
		this.tierScore = card.getTierScore();
		this.limit = card.getLimit();
		this.quantity = card.getQuantity();
		this.monster = card.isMonster();
		this.contin = card.isContin();
		this.quickplay = card.isQuickplay();
		this.counter = card.isCounter();
		this.field = card.isField();
		this.ritual = card.isRitual();
		this.normal = card.isNormal();
	}
	
	public Card(Card card, int quantity) 
	{
		this.name = card.getName();
		this.attribute = card.getAttribute();
		this.type = card.getType();
		this.cardType = card.getCardType();
		this.text = card.getText();
		this.crosslimit = card.getCrosslimit();
		this.rarity = card.getRarity();
		this.synergies = card.getSynergies();
		this.atk = card.getAtk();
		this.def = card.getDef();
		this.lvl = card.getLvl();
		this.tierScore = card.getTierScore();
		this.limit = card.getLimit();
		this.quantity = quantity;
		this.monster = card.isMonster();
		this.contin = card.isContin();
		this.quickplay = card.isQuickplay();
		this.counter = card.isCounter();
		this.field = card.isField();
		this.ritual = card.isRitual();
		this.normal = card.isNormal();
	}
	
	
	public Card(Card card, String text) 
	{
		this.name = card.getName();
		this.attribute = card.getAttribute();
		this.type = card.getType();
		this.cardType = card.getCardType();
		this.text = text;
		this.crosslimit = card.getCrosslimit();
		this.rarity = card.getRarity();
		this.synergies = card.getSynergies();
		this.atk = card.getAtk();
		this.def = card.getDef();
		this.lvl = card.getLvl();
		this.tierScore = card.getTierScore();
		this.limit = card.getLimit();
		this.quantity = card.getQuantity();
		this.monster = card.isMonster();
		this.contin = card.isContin();
		this.quickplay = card.isQuickplay();
		this.counter = card.isCounter();
		this.field = card.isField();
		this.ritual = card.isRitual();
		this.normal = card.isNormal();
	}
	
	
	public Card(String name)
	{
		this.name = name;
	}
	
	public Card(String name, String text)
	{
		this.name = name;
		this.text = text;
	}
	
	public Card(String name, int tierScore)
	{
		this.name = name;
		this.tierScore = tierScore;
	}
	
	public Card(String name, int tierScore, int quantity, String rarity)
	{
		this.name = name;
		this.tierScore = tierScore;
		this.quantity = quantity;
		this.rarity = rarity;
	}
	
	public void decrement(int no)
	{
		this.quantity = this.quantity - no;
	}
	
	public void decrement()
	{
		this.quantity--;
	}

	// Getters and Setters //
	public String getName() { return name;	}
	public void setName(String name) { this.name = name; }
	public String getAttribute() { return attribute; }
	public void setAttribute(String attribute) { this.attribute = attribute; }
	public String getType() { return type; }
	public void setType(String type) { this.type = type; }
	public String getCardType() { return cardType; }
	public void setCardType(String cardType) { this.cardType = cardType; }
	public String getText() { return text; }
	public void setText(String text) { this.text = text; }
	public String getCrosslimit() { return crosslimit; }
	public void setCrosslimit(String crosslimit) { this.crosslimit = crosslimit; }
	public String getRarity() { return rarity; }
	public void setRarity(String rarity) { this.rarity = rarity; }
	public String getSynergies() { return synergies; }
	public void setSynergies(String synergies) { this.synergies = synergies; }
	public int getAtk() { return atk; }
	public void setAtk(int atk) { this.atk = atk; }
	public int getDef() { return def; }
	public void setDef(int def) { this.def = def; }
	public int getLvl() { return lvl; }
	public void setLvl(int lvl) { this.lvl = lvl; }
	public int getTierScore() { return tierScore; }
	public void setTierScore(int tierScore) { this.tierScore = tierScore; }
	public int getLimit() { return limit; }
	public void setLimit(int limit) { this.limit = limit; }
	public int getQuantity() { return quantity; }
	public void setQuantity(int quantity) { this.quantity = quantity; }
	public boolean isMonster() { return monster; }
	public void setMonster(boolean monster) { this.monster = monster; }
	public boolean isContin() { return contin; }
	public void setContin(boolean contin) { this.contin = contin; }
	public boolean isQuickplay() { return quickplay; }
	public void setQuickplay(boolean quickplay) { this.quickplay = quickplay; }
	public boolean isCounter() { return counter; }
	public void setCounter(boolean counter) { this.counter = counter; }
	public boolean isField() { return field; }
	public void setField(boolean field) { this.field = field; }
	public boolean isEquip() { return equip; }
	public void setEquip(boolean equip) { this.equip = equip; }
	public boolean isRitual() { return ritual; }
	public void setRitual(boolean ritual) { this.ritual = ritual; }
	public boolean isNormal() { return normal; }
	public void setNormal(boolean normal) { this.normal = normal; }

	@Override
	public int compare(Card card1, Card card2) 
	{
		return card1.getName().compareTo(card2.getName());
	}

	
	@Override
	public String toString() {
		return this.name + "  x" + this.quantity;
	}
}
