package versionP;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class Split 
{
	private File file;
	private long startingSize = 0;

    public Split(File file) {
        this.file = file;
        this.startingSize = file.length();
    }
    
    public Split(InputStream stream)
    {
    	try {
			this.startingSize = stream.available();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public String processPart(long start, long end, LinkedBlockingQueue<Card> pool)
        throws Exception
    {
    	String[] input = null;
		String line = null;	String name = null;	String attribute = null;	String type = null;
		String cardType = null;	String text = null;	String crosslimit = null;	String rarity = null;
		String synergies = null;
		int atk = 0;	int def = 0;	int lvl = 0;	int tierScore = 0;	
		int limit = 0;	int quantity = 0;
		boolean monster = false;	boolean contin = false;	boolean quickplay = false;	boolean counter = false;
		boolean field = false;	boolean equip = false;	boolean ritual = false;	boolean normal = false;
        FileInputStream database = new FileInputStream(file);
		BufferedReader databaseStream = new BufferedReader(new InputStreamReader(database));
		database.skip(start);
        long chunkSize = startingSize / (end - start);
        long bytesRead = 0;
        while (bytesRead < chunkSize)
        {
	        while (databaseStream.ready())
	        {
		        line = databaseStream.readLine();
		        byte[] test = new byte[55];
		        test = line.getBytes();
		        bytesRead += test.length;
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
				adder(pool, newCard);
			}
        }
        Thread.sleep(1000);
        database.close();
        return "dummy";
    }
    
    public String processPartDraft(long start, long end, LinkedBlockingQueue<Card> pool)
            throws Exception
        {
        	String[] input = null;
    		String line = null;	String name = null;	String attribute = null;	String type = null;
    		String cardType = null;	String text = null;	String crosslimit = null;	String rarity = null;
    		String synergies = null;
    		int atk = 0;	int def = 0;	int lvl = 0;	int tierScore = 0;	
    		int limit = 0;	int quantity = 0;
    		boolean monster = false;	boolean contin = false;	boolean quickplay = false;	boolean counter = false;
    		boolean field = false;	boolean equip = false;	boolean ritual = false;	boolean normal = false;
            FileInputStream database = new FileInputStream(file);
    		BufferedReader databaseStream = new BufferedReader(new InputStreamReader(database));
    		database.skip(start);
            long chunkSize = startingSize / (end - start);
            long bytesRead = 0;
            while (bytesRead < chunkSize)
            {
    	        while (databaseStream.ready())
    	        {
    		        line = databaseStream.readLine();
    		        byte[] test = new byte[55];
    		        test = line.getBytes();
    		        bytesRead += test.length;
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
						draftAdder(pool, newCard2);
					}
	
    			}
            }
            Thread.sleep(1000);
            database.close();
            return "dummy";
        }

    
    public Callable<String> processPartTask(final long start, final long end, LinkedBlockingQueue<Card> pool) {
        return new Callable<String>() {
            public String call()
                throws Exception
            {
                return processPart(start, end, pool);
            }
        };
    }
    
    public Callable<String> processPartTaskDraft(final long start, final long end, LinkedBlockingQueue<Card> pool) {
        return new Callable<String>() {
            public String call()
                throws Exception
            {
                return processPartDraft(start, end, pool);
            }
        };
    }

    
    public void processAll(int noOfThreads, int chunkSize, LinkedBlockingQueue<Card> pool, int trip)
        throws Exception
    {
        int count = (int)((startingSize + chunkSize - 1) / chunkSize);
        java.util.List<Callable<String>> tasks = new ArrayList<Callable<String>>(count);
       
        for(int i = 0; i < count; i++)
        { tasks.add(processPartTask(i * chunkSize, Math.min(startingSize, (i+1) * chunkSize), pool)); }
      
        ExecutorService es = Executors.newFixedThreadPool(noOfThreads);
        es.invokeAll(tasks);
        es.shutdown();
        spreader(pool);
       
        if (trip > 0) 
        {
        	for (Card card : pool)
        	{
        		System.out.println(card.getName() + " x" + card.getQuantity());
        	}
        }

    }
    
    public void processAllDraft(int noOfThreads, int chunkSize, LinkedBlockingQueue<Card> pool, int trip)
            throws Exception
        {
    	//System.out.println(chunkSize);
            int count = (int)((startingSize + chunkSize - 1) / chunkSize);
            java.util.List<Callable<String>> tasks = new ArrayList<Callable<String>>(count);
           
            for(int i = 0; i < count; i++)
            { tasks.add(processPartTaskDraft(i * chunkSize, Math.min(startingSize, (i+1) * chunkSize), pool)); }
          
            ExecutorService es = Executors.newFixedThreadPool(noOfThreads);
            es.invokeAll(tasks);
            es.shutdown();
            //spreader(pool);
           
            if (trip > 0) 
            {
            	for (Card card : pool)
            	{
            		System.out.println(card.getName() + " x" + card.getQuantity());
            	}
            }

        }
    
    public void adder(LinkedBlockingQueue<Card> pool, Card card)
    {
    	boolean add = true;
    	for (Card cardLocal : pool)
    	{
    		if (cardLocal.getName().equals(card.getName()))
    		{
    			add = false;
    		}
    	}
    	
    	if (add) { pool.add(card); }
    }
    
    public void draftAdder(LinkedBlockingQueue<Card> pool, Card card)
    {
    	pool.add(card);
    }
    
    public void spreader(LinkedBlockingQueue<Card> pool)
    {
    	ArrayList<Card> poolArray = new ArrayList<Card>();
    	poolArray.addAll(pool);
    	ArrayList<Card> temp = new ArrayList<Card>();
    	for (Card card : poolArray)
    	{
    			Card newCard = new Card(card, 1);
    			if (Character.isUpperCase(newCard.getName().charAt(0))) { temp.add(newCard); }
    			else if (Character.isDigit((newCard.getName().charAt(0)))) { temp.add(newCard); }
    		
    	}
    	
    	voidCopyALtoLBQ(temp, pool);
    	
    }
    
    public void spreader2(LinkedBlockingQueue<Card> pool)
    {
    	ArrayList<Card> poolArray = new ArrayList<Card>();
    	poolArray.addAll(pool);
    	ArrayList<Card> temp = new ArrayList<Card>();
    	for (Card card : poolArray)
    	{
    			Card newCard = new Card(card);
    			if (Character.isUpperCase(newCard.getName().charAt(0))) { temp.add(newCard); }
    			else if (Character.isDigit((newCard.getName().charAt(0)))) { temp.add(newCard); }
    		
    	}
    	
    	voidCopyALtoLBQ(temp, pool);
    	
    }
    
    public void voidCopyLBQtoAL(ArrayList<Card> copyOver, LinkedBlockingQueue<Card> copyFrom)
    {
    	copyOver.clear();
		for (Card card : copyFrom)
		{
			Card tempCard = new Card(card);
			copyOver.add(tempCard);
		}
    }
    
    public void voidCopyALtoLBQ(ArrayList<Card> copyFrom, LinkedBlockingQueue<Card> copyOver)
    {
    	copyOver.clear();
		for (Card card : copyFrom)
		{
			Card tempCard = new Card(card);
			copyOver.add(tempCard);
		}
    }
    
    public static int countLines(String filename) throws IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(filename));
        try {
            byte[] c = new byte[1024];
            int count = 0;
            int readChars = 0;
            boolean empty = true;
            while ((readChars = is.read(c)) != -1) {
                empty = false;
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
            }
            return (count == 0 && !empty) ? 1 : count;
        } finally {
            is.close();
        }
    }

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public long getStartingSize() {
		return startingSize;
	}
	
	public int getStart()
	{
		return (int)startingSize;
	}
}
