package me.Scrabble;

public class MaxScrabbleScore extends TextFileAccessor {

	String longestLine;
	int maxScore;
	int[] point_storage;
	
	public MaxScrabbleScore()
	{
		longestLine = "";
		maxScore = 0;
		point_storage = new int[]{1,3,3,2,1,4,2,4,1,8,5,1,3,1,1,3,10,1,1,1,1,4,4,8,4,10};
	}
	
	@Override
	protected void processLine(String line) 
	{
		int tempScore = 0;
		for(int i = 0; i < line.length(); i++)
		{
			if(Character.isLetter(line.charAt(i)))
			{
				int char_score = 0;
				
				if(line.charAt(i) == 'a' || line.charAt(i) == 'A')
					char_score+=point_storage[0];
				if(line.charAt(i) == 'b' || line.charAt(i) == 'B')
					char_score+=point_storage[1];
				if(line.charAt(i) == 'c' || line.charAt(i) == 'C')
					char_score+=point_storage[2];
				if(line.charAt(i) == 'd' || line.charAt(i) == 'D')
					char_score+=point_storage[3];
				if(line.charAt(i) == 'e' || line.charAt(i) == 'E')
					char_score+=point_storage[4];
				if(line.charAt(i) == 'f' || line.charAt(i) == 'F')
					char_score+=point_storage[5];
				if(line.charAt(i) == 'g' || line.charAt(i) == 'G')
					char_score+=point_storage[6];
				if(line.charAt(i) == 'h' || line.charAt(i) == 'H')
					char_score+=point_storage[7];
				if(line.charAt(i) == 'i' || line.charAt(i) == 'I')
					char_score+=point_storage[8];
				if(line.charAt(i) == 'j' || line.charAt(i) == 'J')
					char_score+=point_storage[9];
				if(line.charAt(i) == 'k' || line.charAt(i) == 'K')
					char_score+=point_storage[10];
				if(line.charAt(i) == 'l' || line.charAt(i) == 'L')
					char_score+=point_storage[11];
				if(line.charAt(i) == 'm' || line.charAt(i) == 'M')
					char_score+=point_storage[12];
				if(line.charAt(i) == 'n' || line.charAt(i) == 'N')
					char_score+=point_storage[13];
				if(line.charAt(i) == 'o' || line.charAt(i) == 'O')
					char_score+=point_storage[14];
				if(line.charAt(i) == 'p' || line.charAt(i) == 'P')
					char_score+=point_storage[15];
				if(line.charAt(i) == 'q' || line.charAt(i) == 'Q')
					char_score+=point_storage[16];
				if(line.charAt(i) == 'r' || line.charAt(i) == 'R')
					char_score+=point_storage[17];
				if(line.charAt(i) == 's' || line.charAt(i) == 'S')
					char_score+=point_storage[18];
				if(line.charAt(i) == 't' || line.charAt(i) == 'T')
					char_score+=point_storage[19];
				if(line.charAt(i) == 'u' || line.charAt(i) == 'U')
					char_score+=point_storage[20];
				if(line.charAt(i) == 'v' || line.charAt(i) == 'V')
					char_score+=point_storage[21];
				if(line.charAt(i) == 'w' || line.charAt(i) == 'W')
					char_score+=point_storage[22];
				if(line.charAt(i) == 'x' || line.charAt(i) == 'X')
					char_score+=point_storage[23];
				if(line.charAt(i) == 'y' || line.charAt(i) == 'Y')
					char_score+=point_storage[24];
				if(line.charAt(i) == 'z' || line.charAt(i) == 'Z')
					char_score+=point_storage[25];
				
				boolean div = false;
				if(i % 9 == 0)
				{
					if(i % 4 == 0)
					{
						char_score = (char_score*2);
						div=true;
					}
					else
						char_score = (char_score*3);
				}
				if(!div)
				{
					if(i % 4 == 0)
						char_score = (char_score*2);
				}
				tempScore+=char_score;
			}
		}
		if(tempScore > maxScore)
		{
			maxScore = tempScore;
			longestLine = line;
		}
	}

	@Override
	public String getReportStr() 
	{	
		return "Max scrabble score: " + maxScore + " for this line: " + longestLine;
	}

}
