#Searching and Downloading Google Images/Image Links

#Import Libraries

import time       #Importing the time library to check the time of code execution
import sys    #Importing the System Library

import urllib2



########### Edit From Here ###########

#This list is used to search keywords. You can edit this list to search for google images of your choice. You can simply add and remove elements of the list.
search_keywords = [ 'Aquaactress Arowana', 'Aquaactress Guppy', 'Aquaactress Tetra', 'Aquarium Lighting', 'Aquarium Set', 'Aquarium Stage', 'Archfiend Palabyrinth', 'Archfiend Soldier', 'Arisen Gaia the Fierce Knight', 'Autonomous Action Unit', 'Axe of Despair', 'BOXer', 'Bad Luck Blast', 'Bad Reaction to Simochi', 'Barkions Bark', 'Barrel Behind the Door', 'Barrel Dragon', 'Battle Footballer', 'Battle-Scarred', 'Beast of Talwar', 'Begone, Knave!', 'Bickuribox', 'Big Koala', 'Birdface', 'Black Illusion Ritual', 'Black Luster Ritual', 'Black Luster Soldier', 'Black Pendant', 'Blast Held By a Tribute', 'Blazing Mirror Force', 'Blizzard Princess', 'Block Attack', 'Blowback Dragon', 'Blue-Eyes Toon Dragon', 'Book of Moon', 'Book of Secret Arts', 'Bottomless Shifting Sand', 'Brain Control', 'Butterfly Dagger - Elma', 'Caius the Shadow Monarch', 'Card Destruction', 'Card Shuffle', 'Card Trader', 'Card of Sacrifice', 'Castle Walls', 'Cestus of Dagla', 'Chicken Game', 'Chorus of Sanctuary', 'Chthonian Blast', 'Clock Tower Prison', 'Closed Forest', 'Coach Goblin', 'Comic Hand', 'Commencement Dance', 'Contract With Exodia', 'Contract With the Abyss', 'Cost Down', 'Crab Turtle', 'Crashbug X', 'Crashbug Y', 'Crashbug Z', 'Cup of Ace', 'Curse of Dragonfire', 'Cursed Seal of the Forbidden Spell', 'Cyber Shield', 'Cyber-Tech Alligator', 'DNA Transplant', 'Dark Energy', 'Dark Factory of Mass Production', 'Dark Gray', 'Dark Hole', 'Dark Horizon', 'Dark Magician', 'Dark Mirror Force', 'Dark Snake Syndrome','De-spell', 'Destiny Draw', 'Destiny Hero - Dasher', 'Destiny Hero - Defender', 'Destiny Hero - Dogma', 'Destiny Hero - Dreadmaster', 'Destiny Hero - Fear Monger', 'Destiny Hero - Plasma', 'Dian Keto the Cure Master', 'Divine Wind of Mist Valley', 'Dragon Capture Jar', 'Dragon Treasure', 'Dragonic Attack', 'Drop Off', 'Eisbahn', 'Ekibyo Drakmord', 'Elemental Hero Bladedge', 'Exodia Necross', 'Exxod, Master of the Guard', 'Fairy Box', 'Falling Down', 'Fiend Skull Dragon', 'Fiendish Chain', 'Fissure', 'Forced Requisition', 'Forest', 'Freed the Brave Warrior', 'Freed the Matchless General', 'Fulfillment of the Contract', 'Full House', 'Fushioh Richie', 'Fusion Gate', 'Gagagigo', 'Gear Golem the Moving Fortress', 'Gemini Elf', 'Gene-Warped Warwolf', 'Genex Ally Crusher', 'Germ Infection', 'Giant Trunade', 'Gilford the Lightning', 'Girochin Kuwagata', 'Goblin of Greed', 'Goldd, Wu-Lord of Dark World', 'Good Goblin Housekeeping', 'Graceful Charity', 'Granmarg the Rock Monarch', 'Gravi-Crush Dragon', 'Gravity Axe - Grarl', 'Gravity Bind', 'Great Dezard', 'Guardian Elma', 'Guardian Kayest', 'Guardian Sphinx', 'Hamburger Recipe', 'Hammer Shot', 'Hane-Hane', 'Harpie Lady Phoenix Formation', 'Harpie Lady', 'Harpies Brother', 'Harpies Feather Duster', 'Harpies Hunting Ground', 'Heart of the Clear Water', 'Heart of the Underdog', 'Heavy Storm', 'High Tide Gyojin', 'Horn of Heaven', 'Horn of the Unicorn', 'Hungry Burger', 'Hyper-Ancient Shark Megalodon', 'Hysteric Party', 'Ice Queen', 'Icy Crevasse', 'Imperial Order', 'Incandescent Ordeal', 'Infected Mail', 'Injection Fairy Lily', 'Insect Barrier', 'Invigoration', 'Jain, Lightsworn Paladin', 'Jar of Greed', 'Jellyfish', 'Jinzo', 'Jirai Gumo', 'Kabazauls', 'Kaiser Colosseum', 'Kaiser Sea Horse', 'Killer Needle', 'Kuriboh', 'La Jinn the Mystical Genie of the Lamp', 'Labyrinth Wall', 'Landoises Luminous Mass', 'Lava Golem', 'Left Arm of the Forbidden One', 'Left Leg of the Forbidden One', 'Legendary Flame Lord', 'Legendary Sword', 'Leodrakes Mane', 'Level Up!', 'Levia-Dragon - Daedalus', 'Light and Darkness Dragon', 'Light of Intervention', 'Lightning Blade', 'Lightning Vortex', 'Lionhearted Locomotive', 'Lord of D.', 'Luster Dragon', 'Machine Conversion Factory', 'Magic Cylinder', 'Magic Drain', 'Magic Jammer', 'Magical Contract Door', 'Magical Mallet', 'Magical Marionette', 'Magical Stone Excavation', 'Malevolent Nuzzler', 'Man-Eater Bug', 'Man-Eating Treasure Chest', 'Manga Ryu-Ran', 'March of the Monarchs', 'Mask of Brutality', 'Mask of Darkness', 'Mask of Dispel', 'Mask of Restrict', 'Mausoleum of the Emperor', 'Megamorph', 'Meklord Emperor Wisel', 'Metalmorph', 'Metalsilver Armor', 'Mirror Force', 'Mist Valley Executor', 'Mist Valley Falcon', 'Mobius the Frost Monarch', 'Mobius the Mega Monarch', 'Molten Destruction', 'Monster Reborn', 'Moray of Greed', 'Mountain', 'Ms. Judge', 'Mystic Lamp', 'Mystic Plasma Zone', 'Mystic Tomato', 'Mystical Moon', 'Mythical Beast Cerberus', 'Naturia Dragonfly', 'Naturia Guardian', 'Naturia Horneedle', 'Naturia Hydrangea', 'Naturia Mantis', 'Naturia Mosquito', 'Naturia Pineapple', 'Naturia Pumpkin', 'Naturia Rosewhip', 'Naturia Sacred Tree', 'Naturia Strawberry', 'Neo Bug', 'Neo the Magic Swordsman', 'Nightmare Wheel', 'Niwatori', 'Numinous Healer', 'Obliterate!!!', 'Ocean Dragon Lord - Neo Daedalus', 'Oh F!sh!', 'Ominous Fortunetelling', 'Opticlops', 'Pandemonium', 'Panther Warrior', 'Paralyzing Potion', 'Parasite Paracide', 'Penguin Soldier', 'Performance of Sword', 'Piercing Moray', 'Pitch-Black Power Stone', 'Polymerization', 'Poseidon Wave', 'Pot of Dichotomy', 'Pot of Duality', 'Pot of Greed', 'Pot of the Forbidden', 'Power Frame', 'Power Pickaxe', 'Power of Kaishin', 'Precious Cards from Beyond', 'Premature Burial', 'Prevent Rat', 'Proto-Cyber Dragon', 'Pyramid of Light', 'Quantum Cat', 'Radiant Mirror Force', 'Raigeki Bottle', 'Raigeki', 'Rainbow Dark Dragon', 'Raise Body Heat', 'Raiza the Storm Monarch', 'Raviel, Lord of Phantasms', 'Red Eyes Toon Dragon', 'Red-Eyes B. Dragon', 'Red-Eyes Black Metal Dragon', 'Red-Eyes Spirit', 'Red-Eyes Zombie Dragon', 'Relinquished', 'Remove Brainwashing', 'Remove Trap', 'Reverse trap', 'Right Arm of the Forbidden One', 'Right Leg of the Forbidden One', 'Ring of Magnetism', 'Riryoku Field', 'Rising Air Current', 'Robbin Goblin', 'Rocket Pilder', 'Rod of Silence - Kayest', 'Rogue Doll', 'Rope of Life', 'Rush Recklessly', 'Ryko, Lightsworn Hunter', 'Ryu-Kishin Powered', 'Ryu-Ran', 'Sakuretsu Armor', 'Salvage', 'Sanga of the Thunder', 'Sauropod Brachion', 'Scapegoat', 'Science Soldier', 'Scroll of Bewitchment', 'Sea Serpent Warrior of Darkness', 'Secret Sect Druid Wid', 'Senju of the Thousand Hands', 'Seven Tools of the Bandit', 'Shadow Spell', 'Shard of Greed', 'Shiens Spy', 'Shining Abyss', 'Shooting Star Bow - Ceal', 'Shrine of Mist Valley', 'Skull Invitation', 'Skull Lair', 'Skull Red Bird', 'Smashing Ground', 'Smoke Grenade of the Thief', 'Snatch Steal', 'Snowdust Dragon', 'Sogen', 'Solemn Judgment', 'Sorcerous Spell Wall', 'Soul Exchange', 'Soul Tiger', 'Spacegate', 'Spatial Collapse', 'Spell Economics', 'Spell Vanishing', 'Spellbinding Circle', 'Sphere Kuriboh', 'Spirits Invitation', 'Stim-Pack', 'Storming Mirror Force', 'Storm', 'Stray Lambs', 'Strike Ninja', 'Summon Breaker', 'Summoned Skull', 'Super Crashbug', 'Super Soldier Shield', 'Superancient Deepsea King Coelacanth', 'Superheavy Samurai Flutist', 'Superheavy Samurai Soulbeads', 'Superheavy Samurai Soulpiercer', 'Supply', 'Sword of Deep-Seated', 'Swords of Burning Light', 'Swords of Concealing Light', 'Swords of Revealing Light', 'Terraforming', 'Terrorking Salmon', 'That Six', 'The Dark - Hex Sealed Fusion', 'The Dragon Dwelling in the Cave', 'The Dragon Dwelling in the Deep', 'The Emperors Holiday', 'The End of Anubis', 'The Fiend Megacyber', 'The Flute of Summoning Dragon', 'The Gates of Dark World', 'The Golden Apples', 'The Legendary Exodia Incarnate', 'The Legendary Fisherman', 'The Rock Spirit', 'The Seal of Orichalcos', 'The White Stone of Legend', 'Thestalos the Firestorm Monarch', 'Thousand Dragon', 'Time Wizard', 'Toon Barrel Dragon', 'Toon Dark Magician Girl', 'Toon Dark Magician', 'Toon Gemini Elf', 'Toon Mermaid', 'Toon Summoned Skull', 'Toon World', 'Tornado Wall', 'Torrential Tribute', 'Tower of Babel', 'Trap Hole', 'Trap Jammer', 'Trap Stun', 'Trap of Board Eraser', 'Treacherous Trap Hole', 'Triple Star Trion', 'Turtle Oath', 'Tutan Mask', 'Twin Swords of Flashing Light - Tryce', 'Twin-Barrel Dragon', 'Twin-Headed Thunder Dragon', 'Type Zero Magic Crusher', 'Tyrant Dragon', 'Ultimate Insect LV7', 'Ultimate Offering', 'Umiiruka', 'Umi', 'United Front', 'Unstable Evolution', 'Vanitys Call', 'Vile Germs', 'Violet Crystal', 'Vorse Raider', 'Waboku', 'Wall of Illusion', 'Wall of Revealing Light', 'Wasteland', 'Wattaildragon', 'Wattcube', 'Wicked-Breaking Flamberge - Baou', 'Wingweaver', 'Wiretap', 'Witch of the Black Forest', 'Worm Apocalypse', 'Yami', 'Zaborg the Thunder Monarch', 'Zombyra the Dark', 'Zure, Knight of Dark World', ]
#c = sys.argv[1];
search_keyword = [ search_keywords[1] ]
#This list is used to further add suffix to your search term. Each element of the list will help you download 100 images. First element is blank which denotes that no suffix is added to the search keyword of the above list. You can edit the list by adding/deleting elements from it.So if the first element of the search_keyword is 'Australia' and the second element of keywords is 'high resolution', then it will search for 'Australia High Resolution'
keywords = ['yugioh']

########### End of Editing ###########




#Downloading entire Web Document (Raw Page Content)
def download_page(url):
    version = (3,0)
    cur_version = sys.version_info
    if cur_version >= version:     #If the Current Version of Python is 3.0 or above
        import urllib.request    #urllib library for Extracting web pages
        try:
            headers = {}
            headers['User-Agent'] = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36"
            req = urllib.request.Request(url, headers = headers)
            resp = urllib.request.urlopen(req)
            respData = str(resp.read())
            return respData
        except Exception as e:
            print(str(e))
    else:                        #If the Current Version of Python is 2.x
        import urllib2
        try:
            headers = {}
            headers['User-Agent'] = "Mozilla/5.0 (X11; Linux i686) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.27 Safari/537.17"
            req = urllib2.Request(url, headers = headers)
            response = urllib2.urlopen(req)
            page = response.read()
            return page
        except:
            return"Page Not found"


#Finding 'Next Image' from the given raw page
def _images_get_next_item(s):
    start_line = s.find('rg_di')
    if start_line == -1:    #If no links are found then give an error!
        end_quote = 0
        link = "no_links"
        return link, end_quote
    else:
        start_line = s.find('"class="rg_meta"')
        start_content = s.find('"ou"',start_line+1)
        end_content = s.find(',"ow"',start_content+1)
        content_raw = str(s[start_content+6:end_content-1])
        return content_raw, end_content


#Getting all links with the help of '_images_get_next_image'
def _images_get_all_items(page):
    items = []
    while True:
        item, end_content = _images_get_next_item(page)
        if item == "no_links":
            break
        else:
            items.append(item)      #Append all the links in the list named 'Links'
            time.sleep(0.1)        #Timer could be used to slow down the request for image downloads
            page = page[end_content:]
    return items


############## Main Program ############
t0 = time.time()   #start the timer


#Download Image Links
i= 0
while i<len(search_keyword):
	items = []
	iteration = "Item no.: " + str(i+1) + " -->" + " Item name = " + str(search_keyword[i])
	print (iteration)
	print ("Evaluating...")
	search_keywords = search_keyword[i]
	search = search_keywords.replace(' ','%20')
	j = 0
	while j<len(keywords):
		pure_keyword = keywords[j].replace(' ','%20')
		url = 'https://www.google.com/search?q=' + search + pure_keyword + '&espv=2&biw=1366&bih=667&site=webhp&source=lnms&tbm=isch&sa=X&ei=XosDVaCXD8TasATItgE&ved=0CAcQ_AUoAg'
		raw_html =  (download_page(url))
		time.sleep(0.1)
		items = items + (_images_get_all_items(raw_html))
		j = j + 1
	#print ("Image Links = "+str(items))
	print ("Total Image Links = "+str(len(items)))
	print ("\n")
	i = i+1


	#This allows you to write all the links into a test file. This text file will be created in the same directory as your code. You can comment out the below 3 lines to stop writing the output to the text file.
	info = open('output.txt', 'a')        #Open the text file called database.txt
	info.write(str(i) + ': ' + str(search_keyword[i-1]) + ": " + str(items) + "\n\n\n")         #Write the title of the page
	info.close()                            #Close the file

t1 = time.time()    #stop the timer
total_time = t1-t0   #Calculating the total time required to crawl, find and download all the links of 60,000 images
print("Total time taken: "+str(total_time)+" Seconds")
print ("Starting Download...")

## To save imges to the same directory
# IN this saving process we are just skipping the URL if there is any error

k=0
errorCount=0
while(2):
	from urllib2 import Request,urlopen
	from urllib2 import URLError, HTTPError
	
	try:
		req = Request(items[k], headers={"User-Agent": "Mozilla/5.0 (X11; Linux i686) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.27 Safari/537.17"})
		response = urlopen(req)
		output_file = open(str(search_keyword[k])+".png",'wb')
		data = response.read()
		output_file.write(data)
		response.close();

		print("completed ====> "+str(k+1))

		k=k+1;
		

	except IOError:   #If there is any IOError

		errorCount+=1
		print("IOError on image "+str(k+1))
		k=k+1;

	except HTTPError as e:  #If there is any HTTPError

		errorCount+=1
		print("HTTPError"+str(k))
		k=k+1;
	except URLError as e:

		errorCount+=1
		print("URLError "+str(k))
		k=k+1;
print("\n")
print("All are downloaded")
print("\n"+str(errorCount)+" ----> total Errors")


#----End of the main program ----#
