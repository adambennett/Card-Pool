package versionP;

import java.util.ArrayList;

public class CardNode {

    private String name;
    private String attribute;
    private String type;
    private String cardType;
    private String crossLimit;
    private String cardText;

    private Integer atk;
    private Integer def;
    private Integer lvl;
    private Integer quantity;
    private Integer poolLimt;
    private Integer deckLimit;
    private Integer tierScore;

    private Boolean isMonster;
    private Boolean isContinuous;
    private Boolean isQuickplay;
    private Boolean isCounter;
    private Boolean isField;
    private Boolean isEquip;
    private Boolean isRitual;
    private Boolean isNormal;

    private CardRarity rarity;
    private ArrayList<String> synergies;

    public CardNode(String name, String attribute, String type, String cardType, String crossLimit, String cardText, Integer atk, Integer def, Integer lvl, Integer quantity, Integer poolLimt, Integer deckLimit, Integer tierScore, Boolean isMonster, Boolean isContinuous, Boolean isQuickplay, Boolean isCounter, Boolean isField, Boolean isEquip, Boolean isRitual, Boolean isNormal, CardRarity rarity, ArrayList<String> synergies) {
        this.name = name;
        this.attribute = attribute;
        this.type = type;
        this.cardType = cardType;
        this.crossLimit = crossLimit;
        this.cardText = cardText;
        this.atk = atk;
        this.def = def;
        this.lvl = lvl;
        this.quantity = quantity;
        this.poolLimt = poolLimt;
        this.deckLimit = deckLimit;
        this.tierScore = tierScore;
        this.isMonster = isMonster;
        this.isContinuous = isContinuous;
        this.isQuickplay = isQuickplay;
        this.isCounter = isCounter;
        this.isField = isField;
        this.isEquip = isEquip;
        this.isRitual = isRitual;
        this.isNormal = isNormal;
        this.rarity = rarity;
        this.synergies = synergies;
    }

    public static String getRarityAsString(CardRarity rarity) {
        switch (rarity) {
            case COMMON:
                return "Common";
            case UNCOMMON:
                return "Uncommon";
            case RARE:
                return "Rare";
            case SUPER_RARE:
                return "Super Rare";
            case ULTRA_RARE:
                return "Ultra Rare";
            default:
                return "whoops";
        }
    }

    public enum CardRarity {
        COMMON,
        UNCOMMON,
        RARE,
        SUPER_RARE,
        ULTRA_RARE
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCrossLimit() {
        return crossLimit;
    }

    public void setCrossLimit(String crossLimit) {
        this.crossLimit = crossLimit;
    }

    public String getCardText() {
        return cardText;
    }

    public void setCardText(String cardText) {
        this.cardText = cardText;
    }

    public Integer getAtk() {
        return atk;
    }

    public void setAtk(Integer atk) {
        this.atk = atk;
    }

    public Integer getDef() {
        return def;
    }

    public void setDef(Integer def) {
        this.def = def;
    }

    public Integer getLvl() {
        return lvl;
    }

    public void setLvl(Integer lvl) {
        this.lvl = lvl;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getPoolLimt() {
        return poolLimt;
    }

    public void setPoolLimt(Integer poolLimt) {
        this.poolLimt = poolLimt;
    }

    public Integer getDeckLimit() {
        return deckLimit;
    }

    public void setDeckLimit(Integer deckLimit) {
        this.deckLimit = deckLimit;
    }

    public Integer getTierScore() {
        return tierScore;
    }

    public void setTierScore(Integer tierScore) {
        this.tierScore = tierScore;
    }

    public Boolean getMonster() {
        return isMonster;
    }

    public void setMonster(Boolean monster) {
        isMonster = monster;
    }

    public Boolean getContinuous() {
        return isContinuous;
    }

    public void setContinuous(Boolean continuous) {
        isContinuous = continuous;
    }

    public Boolean getQuickplay() {
        return isQuickplay;
    }

    public void setQuickplay(Boolean quickplay) {
        isQuickplay = quickplay;
    }

    public Boolean getCounter() {
        return isCounter;
    }

    public void setCounter(Boolean counter) {
        isCounter = counter;
    }

    public Boolean getField() {
        return isField;
    }

    public void setField(Boolean field) {
        isField = field;
    }

    public Boolean getEquip() {
        return isEquip;
    }

    public void setEquip(Boolean equip) {
        isEquip = equip;
    }

    public Boolean getRitual() {
        return isRitual;
    }

    public void setRitual(Boolean ritual) {
        isRitual = ritual;
    }

    public Boolean getNormal() {
        return isNormal;
    }

    public void setNormal(Boolean normal) {
        isNormal = normal;
    }

    public CardRarity getRarity() {
        return rarity;
    }

    public void setRarity(CardRarity rarity) {
        this.rarity = rarity;
    }

    public ArrayList<String> getSynergies() {
        return synergies;
    }

    public void setSynergies(ArrayList<String> synergies) {
        this.synergies = synergies;
    }
}
