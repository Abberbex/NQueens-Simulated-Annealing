/**
 * NQueens problem solving with Simulated Annealing 
 * @author Brendan Dickerson
 * AI 2014, Weiss
 */

import java.util.Random;

public class NQueens{
	static int size = 8;
	// state
	
	int[] state = new int[size];  // rows 0 to size - 1
	Random rand = new Random();
	float temp = 4.0f;
	float it = 0;

	public NQueens(int s) {
		size = s;
		// initialize board
		for (int i=0; i<size; i++) {
			state[i] = rand.nextInt(size);  // position of Q in row i
		}
	}

	public void printBoard() {
		for (int row=0; row<size; row++) {
			for (int col=0; col<size; col++) {
				if (state[row] == col) {
					System.out.print(" X ");
				} else {
					System.out.print(" 0 ");
				}
			}
			System.out.println("");
		}

	}

	/**
       Compute the number of pairs that don't conflict
	 */
	public int fitness() {
		int safe = 0;
		int count = 0;
		for (int i=0; i<size; i++) {
			if(count==1)
			{
				safe++;
			}
			count=0;
			for (int j=0; j<size; j++) {
				if (state[i] == state[j]) {
					count++;
				}
			}
		}
		if(count==1)
		{
			safe++;
		}
		return safe;
	}

	/**
       Each queen is specified by its row, e.g. queen 0 is in row 0
       The method countDiagonal returns a count of the number of pairs of queens
       along a diagonal that attack each other.
	 */
	public int countDiagonal() {  //could not figure out how to calculate diagonal attacks
		int count = 0;
		int deltaRow;
		int deltaCol;
		for (int i=0; i<size; i++) {
			for (int j=0+1; j<size; j++) {
				deltaCol = Math.abs(state[i]-state[j]);
				deltaRow = Math.abs(i-(j));
				if(deltaCol==deltaRow) {
					count++;
				}
			}
		}
		return count-(size-1);
	}



	/**
       Each queen is specified by its row, e.g. queen 0 is in row 0
       The method countCol(i) returns a count of the number of queens
       in the same column as the queen in row i.
       This is just the condition that state[j] == state[i]
	 */
	public int countCol() {
		int count = 0;
		for (int i=0; i<size; i++) {
			for (int j=i+1; j<size; j++) {
				if (state[i] == state[j]) {
					count++;
				}
			}
		}
		return count;
	}


	/**
       Compute the number of pairs of queens that are attacking each other
	 */
	public int cost() {
		// fill in

		return countCol()+countDiagonal();
	}
	
	public double prob(float delta, float temp){
		return 1/(1+(Math.exp(delta/temp))); //calculate probability of accepting new state
	}
    
	public void temperatureCalc() {
		temp=(float) (temp*Math.pow(0.95, it)); //exponentially decreasing
	}
	/**
       Neighborhood of a state is all states that differ in exactly one row
       for local search.
	 */
	public void simAnnealing() {
		while (cost()!=0) {
			
			int init = cost(); //initial cost
			int q = rand.nextInt(size); //random queen selected
			int p1 = state[q]; //initial position of queen
			state[q] = rand.nextInt(size); //set queen to new position
			if (cost() >= init) { 
				if ((Math.floor(prob((cost() - init), temp))*1000) < rand.nextInt(1000)) { //use probability to accept new state
					state[q] = p1; //do not take move if prob is not great enough
				}
			}
			it++; // iteration counter
			temperatureCalc(); //calculate new temp
		}
		printBoard(); //display result
	}



	public static void main(String[] args) {
		NQueens board = new NQueens(size);
		board.printBoard();
		
		System.out.println("number of pairs of attacking Queens via cols: " + board.countCol());
		System.out.println("number of pairs of attacking Queens via diag: " + board.countDiagonal());
		System.out.println("Safe queens "+ board.fitness());
		board.simAnnealing();
		System.out.println("number of pairs of attacking Queens via cols: " + board.countCol());
		System.out.println("number of pairs of attacking Queens via diag: " + board.countDiagonal());
		System.out.println("Iterations: "+board.it+"\n Temperature: "+board.temp);

	}
}
