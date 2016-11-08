package me.Kyle;

import java.io.IOException;

public class WordPercentages extends FileAccessor{
	
	double[] wordPercentages;
	double total_words = 0.0;
	
	public WordPercentages(String f) throws IOException
	{
		super(f);
		wordPercentages = new double[16];
	}

	public double getAvgWordLength() 
	{
		double sum = 0.0;
		double count = 0.0;
		for(int i=0; i < wordPercentages.length; i++)
		{
			sum+=(i*wordPercentages[i]);
			count+= wordPercentages[i];
		}
		return (sum/count);
	}


	@Override
	public void processLine(String line) 
	{
		String[] array = line.split("[,.;:?!() ]");
		for(String s : array)
		{
			if(s.length() > 0)
			{
				if(s.length() >= wordPercentages.length)
					wordPercentages[15]++;
				else
					wordPercentages[s.length()]++;
				total_words++;
			}
		}
	}

	public double[] getWordPercentages()
	{
		double[] temp = new double[wordPercentages.length];
		for(int i = 1; i < wordPercentages.length; i++)
		{
			temp[i] = ((wordPercentages[i]/total_words)*100);
		}
		return temp;
	}
	
}
