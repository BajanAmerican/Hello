package me.Kyle;

import java.util.Random;

public class Opoly {
	
	private int[] board;
	private Random r;
	
	private int rounds;
	private int points;
	private int spin_counter;

	public Opoly(int boardSize, int seed) 
	{
		board = new int[boardSize];
		board[0] = 1;
		r = new Random(seed);
		rounds = 0;
		points = 100;
		spin_counter = 0;
	}
	
	public Opoly(int boardSize)
	{
		board = new int[boardSize];
		board[0] = 1;
		r = new Random();
		rounds = 0;
		points = 100;
		spin_counter = 0;
	}

	public void playGame() 
	{
		while(!isGameOver())
		{
			drawBoard();
			spinAndMove();
		}
		displayReport();
	}
	
	public void drawBoard()
	{
		char[] display = new char[board.length];
		for(int i=0;i<board.length;i++)
		{
			if(board[i] == 0)
				display[i] = '*';
			else
				display[i] = 'o';
		}
		for(char c : display)
			System.out.print(c);
		System.out.print(" " + points + "\n");
	}
	
	public void spinAndMove()
	{
		move(spin());
	}
	
	public int spin()
	{
		spin_counter++;
		return (r.nextInt(5)+1);
	}
	
	public void move(int rand)
	{
		boolean went = false;
		rounds++;
		for(int i=0;i<board.length;i++)
		{
			if((board[i] == 1)&&((i+rand)>(board.length-1)))
			{
				board[i] = 0;
				board[((i+rand)%(board.length))] = 1;
				points+=100;
				check_modifiers();
				went = true;
			}
		}
		if(!went)
		{
			for(int i = 0; i < board.length; i++)
			{
				if(board[i] == 1)
				{
					board[i+rand] = 1;
					board[i] = 0;
					check_modifiers();
					break;
				}
			}
		}
	}
	
	public void check_modifiers()
	{
		if((spin_counter % 10 == 0) %% (spin_counter > 0))
			points-=50;
		for(int i = 0; i < board.length; i++)
		{
			if((board[i] == 1) && (i == (board.length-1)))
			{
				board[(board.length-1)] = 0;
				board[(board.length-4)] = 1;
			}
			if((board[i] == 1) && (i % 7 == 0))
				points=(points*2);
		}
	}
	
	public boolean isGameOver()
	{
		return (points >= 1000);
	}
	
	public void displayReport()
	{
		drawBoard();
		System.out.println("game over");
		System.out.println("rounds of play: " + rounds);
		System.out.println("final reward: " + points);
	}

}
