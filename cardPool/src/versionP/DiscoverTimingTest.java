package versionP;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class DiscoverTimingTest {

	public static void main(String[] args) 
	{
		LinkedBlockingQueue<Card> pool = new LinkedBlockingQueue<Card>();
		LinkedBlockingQueue<LinkedBlockingQueue<Card>> decks = new LinkedBlockingQueue<LinkedBlockingQueue<Card>>();
		LinkedBlockingQueue<Card> temp1 = new LinkedBlockingQueue<Card>();
		LinkedBlockingQueue<Card> temp2 = new LinkedBlockingQueue<Card>();
		decks.add(temp1); decks.add(temp2);
		Iterator<LinkedBlockingQueue<Card>> i = decks.iterator();
		long startTime = System.nanoTime();
		String directory = "C:\\Users\\Adam\\Documents\\YGO Draft Decks\\Fri Jun 23 [5654341]\\Discovery";
		File dir = new File(directory);
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
			for (File child : directoryListing) 
			{
				if (child.getName().equals("Pool.txt"))
				{
					Split s = new Split(child);
					int chunk = s.getStart() / 8;
					try { s.processAll(8, chunk, pool, 1); } catch (Exception e) { e.printStackTrace(); }
				}

				else if (child.getName().equals("Deck 1.txt"))
				{
					Split s = new Split(child);
					int chunk = s.getStart() / 8;
					try { s.processAll(8, chunk, i.next(), 0); } catch (Exception e) { e.printStackTrace(); }
				}

				else if (child.getName().equals("Deck 2.txt"))
				{
					Split s = new Split(child);
					int chunk = s.getStart() / 8;
					try { s.processAll(8, chunk, i.next(), 0); } catch (Exception e) { e.printStackTrace(); }
				}
			}	
		}
	
		long endTime = System.nanoTime();
		long durationNS = (endTime - startTime);
		long durationMS = durationNS / 1000000;
		Iterator<LinkedBlockingQueue<Card>> i2 = decks.iterator();
		//System.out.println("Read Speed: " + durationNS + " nano seconds");
		System.out.println("Read Speed: " + durationMS + " milli seconds");
		System.out.println("Pool Size: " + pool.size());
		System.out.println("Deck 1 Size: " + i2.next().size());
		System.out.println("Deck 2 Size: " + i2.next().size());
		
		/*ArrayList<Card> poolArray = new ArrayList<Card>();
		poolArray.addAll(pool);
		for (Card card : poolArray)
		{
			System.out.println(card.getName() + " x" + card.getQuantity());
		}
*/
	}

}
