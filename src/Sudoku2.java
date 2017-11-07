import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Sudoku2 {

	int [][] Fields;
	
	public static void main(String[] arg){

		int[][] Pola = {
				{0,0,0,0,0,0,8,0,6},
				{0,0,9,0,0,0,0,0,0},
				{0,0,6,0,4,2,0,0,0},
				{0,8,0,1,0,0,0,0,0},
				{0,1,0,0,0,0,0,2,0},
				{0,0,0,0,0,9,0,4,0},
				{0,0,0,8,3,0,1,0,0},
				{0,0,0,0,0,0,9,0,0},
				{2,0,5,0,0,0,0,0,0}
		};
		
		Sudoku2 TestSudoku = new Sudoku2(Pola);
		System.out.println(Arrays.toString(TestSudoku.StartSolving()));
		//SolvedSudoku = TestSudoku.SolveSudoku(CheckedSudoku, RowStart, ColumnStart, PossibleValues)
		//TestSudoku.PermutationFirstRow(TestSudoku.GetFirstRow());
		
		
	}
	
	//Constructor
	public Sudoku2(int[][] Fields){
		this.Fields = Fields;
	}
	
	//Method returns a list of values that can be written into an empty field
	//However there's no absolute certainty that on of those values will be the right input
	ArrayList<Integer> GetPossibleValues(int[][] Sudoku, int Row, int Column){
		
		ArrayList<Integer> ListPossibleValues = new ArrayList<Integer>();
		ArrayList<Integer> ListNotPossibleValues = new ArrayList<Integer>();
		
		//filling list with possible values
		for(int i = 1; i < 10; i++){
			ListPossibleValues.add(i);
		}
		
		//Checking row
		for(int CheckedColumn = 0; CheckedColumn < 9; CheckedColumn++){
			if(Sudoku[Row][CheckedColumn] != 0){ListNotPossibleValues.add(Sudoku[Row][CheckedColumn]);}
		}
		
		//Checking column
		for(int CheckedRow = 0; CheckedRow < 9; CheckedRow++){
			if(Sudoku[CheckedRow][Column] != 0){ListNotPossibleValues.add(Sudoku[CheckedRow][Column]);}
		}
		
		//Checking square
		int LeftUpperCornerRow;
		int LeftUpperCornerColumn;
		
		//Setting left upper row of square
		if(Row < 3){LeftUpperCornerRow = 0;}
		else if(Row < 6){LeftUpperCornerRow = 3;}
		else{LeftUpperCornerRow = 6;}
		
		//Setting left upper column of square
		if(Row < 3){LeftUpperCornerColumn = 0;}
		else if(Row < 6){LeftUpperCornerColumn = 3;}
		else{LeftUpperCornerColumn = 6;}
				
		//Loop through all positions in square
		for(int RowSquare = LeftUpperCornerRow; RowSquare < LeftUpperCornerColumn + 3; RowSquare++){
			
			for(int ColumnSquare = LeftUpperCornerColumn; ColumnSquare < LeftUpperCornerColumn + 3; ColumnSquare++){
				if(Sudoku[RowSquare][ColumnSquare] != 0){ListNotPossibleValues.add(Sudoku[RowSquare][ColumnSquare]);}
			}
		}
		
		//Clear list of possible values
		for(int indexListNotPossibleValues = 0; indexListNotPossibleValues < ListNotPossibleValues.size(); indexListNotPossibleValues++){
			int NotPossibleValue = ListNotPossibleValues.get(indexListNotPossibleValues);
			
			for(int indexListPossibleValues = 0; indexListPossibleValues < ListPossibleValues.size(); indexListPossibleValues++){
				if(ListPossibleValues.get(indexListPossibleValues) == NotPossibleValue){ListPossibleValues.remove(indexListPossibleValues);
					break;
				}
			}
		}
		
		System.out.println("Liczba wykluczonych wartoœci to: " + ListNotPossibleValues.size());
		System.out.println("Dla okienka (" + Row + "," + Column +") potencjalne wartoœci to: " + ListPossibleValues);
		return ListPossibleValues;
	}
	
	//Method gets back to the previous field and fixes the list of possible values that can be input
	//knowing that given argument is most certainly not the one that we are looking for
	ArrayList<Integer> GetModifiedPossibleValues(int[][] Sudoku, int Row, int Column, int PreviousCheckedValue){
		
		ArrayList<Integer> PossibleValues = GetPossibleValues(Sudoku, Row, Column);
		
		//Throw out the wrong number and the previous ones
		for(int PossibleValueLoop = PossibleValues.size() - 1; PossibleValueLoop > -1; PossibleValueLoop--){
			if(PossibleValues.get(PossibleValueLoop) <= PreviousCheckedValue){PossibleValues.remove(PossibleValueLoop);}
		}
		
		return PossibleValues;
		
	}
	
	//Method returns a list containing coordinates of the last modified field
	//First value - row, second value - column
	ArrayList<Integer> GetPreviousModifiedPosition(int RowCurrent, int ColumnCurrent){
		
		int[][] SudokuOriginal = getFields();
		ArrayList<Integer> PreviousModifiedPositions = new ArrayList<Integer>();
		int RowLoop = RowCurrent;
		int ColumnLoop = ColumnCurrent;
		
		//Loop backwards to the last modidified field
		{
			//First element in row(higher than second)
			if(RowLoop > 1 && ColumnLoop == 0){
				RowLoop--;
				ColumnLoop = 8;
			}
			//first element in whole sudoku
			else if(RowLoop == 0 && ColumnLoop == 0){
				return PreviousModifiedPositions;
			}
			else{
				ColumnLoop--;
			}
			
		}while(SudokuOriginal[RowLoop][ColumnLoop] != 0);
		
		PreviousModifiedPositions.add(RowLoop);
		PreviousModifiedPositions.add(ColumnLoop);
		return PreviousModifiedPositions;
		
	}
	
	//Method returns next empty field
	ArrayList<Integer> GetNextEmptyPosition(int RowStart, int ColumnStart){
		
		//Sudoku from which we've started
		int[][] SudokuOriginal = getFields();
		ArrayList<Integer> NextPositions = GetNextPosition(RowStart, ColumnStart);
		ArrayList<Integer> NextEmptyPositions = GetNextPosition(RowStart, ColumnStart);
		
		for(int RowLoop = NextPositions.get(0); RowLoop < 9; RowLoop++){
			
			for(int ColumnLoop = NextPositions.get(1); ColumnLoop < 9; ColumnLoop++){
			
				if(SudokuOriginal[RowLoop][ColumnLoop] == 0){
					NextEmptyPositions.add(RowLoop);
					NextEmptyPositions.add(ColumnLoop);
					return NextEmptyPositions;
				}
			}	
		}
		return NextEmptyPositions;
	}
	
	ArrayList<Integer> GetNextPosition(int RowStart, int ColumnStart){
		
		ArrayList<Integer> PositionNextField = new ArrayList<Integer>();
		
		//Check if the field is last in row
		if(ColumnStart == 8){
			PositionNextField.add(RowStart++);
			PositionNextField.add(0);
		}else{
			PositionNextField.add(RowStart);
			PositionNextField.add(ColumnStart++);		
		}
		
		return PositionNextField;
		
	}
	
	int[][] StartSolving(){
		
		//Find first empty field
		for(int Column = 0; Column < 9; Column++){
			for(int Row = 0; Row < 9; Row++){
				if(Fields[Row][Column] == 0){return SolveSudoku(Fields, Row, Column, GetPossibleValues(Fields, Row, Column));}
			}
		}
		
		return Fields;
	}
	
	int[][] SolveSudoku(int[][] CheckedSudoku, int RowStart, int ColumnStart, ArrayList<Integer> PossibleValues){
		
		ArrayList<Integer> NextEmptyPosition = GetNextEmptyPosition(RowStart, ColumnStart);
		
		//Check if the position of field in the sudoku is not last
		if(RowStart != 8 && ColumnStart != 8){
			
			//Sudoku from which we've started
			int[][] SudokuOriginal = getFields();
			
			//Check if originally there was no number in it
			if(SudokuOriginal[RowStart][ColumnStart] != 0){
				
				//Check if there's any possible values that can be input to the field
				if(PossibleValues.size() > 0){
					
					//Loop through all possible values
					for(int PossibleValueLoop = 0; PossibleValueLoop < PossibleValues.size(); PossibleValueLoop++){
						
						CheckedSudoku[RowStart][ColumnStart] = PossibleValues.get(PossibleValueLoop);
						PrintSudoku(CheckedSudoku);
						return SolveSudoku(CheckedSudoku, NextEmptyPosition.get(0), NextEmptyPosition.get(1),
								GetPossibleValues(CheckedSudoku, NextEmptyPosition.get(0), NextEmptyPosition.get(1)));
					}
				}
				//Misconception - we need to change the value of last modified field
				else{
					
					ArrayList<Integer> PreviousModifiedPosition = GetPreviousModifiedPosition(RowStart, ColumnStart);
					int FalseCheckedValue = CheckedSudoku[PreviousModifiedPosition.get(0)][PreviousModifiedPosition.get(1)];
					CheckedSudoku[PreviousModifiedPosition.get(0)][PreviousModifiedPosition.get(1)] = 0;
					PrintSudoku(CheckedSudoku);
					return SolveSudoku(CheckedSudoku, PreviousModifiedPosition.get(0), PreviousModifiedPosition.get(1), 
							GetModifiedPossibleValues(CheckedSudoku, PreviousModifiedPosition.get(0), PreviousModifiedPosition.get(1),
									FalseCheckedValue));
				}
			}
			else{
				PrintSudoku(CheckedSudoku);
				return SolveSudoku(CheckedSudoku, RowStart, ColumnStart, 
						GetPossibleValues(CheckedSudoku, NextEmptyPosition.get(0), NextEmptyPosition.get(1)));
			}
			
		}
		else{
			CheckedSudoku[RowStart][ColumnStart] = PossibleValues.get(0);
			PrintSudoku(CheckedSudoku);
			if (IsSudokuSolved(CheckedSudoku)){return CheckedSudoku;}
			else{return getFields();}
		}

		return getFields();
	}
	
	static boolean IsSudokuSolved(int[][] CheckedSudoku){
		
		int sum = 0;
		
		for(int RowLoop = 0; RowLoop < 9; RowLoop++){
			
			for(int ColumnLoop = 0; ColumnLoop < 9; ColumnLoop++){
				sum = sum + CheckedSudoku[RowLoop][ColumnLoop];
			}
			
			if(sum != 45){return false;}
			else{sum = 0;}
		}
		
		return true;
	}
	
	void PrintSudoku(int[][] PrintedSudoku){
		
		String PrintRow = "";
		System.out.println("-------------------------");
		for(int Row = 0; Row < 9; Row++){
			
			PrintRow = "";
			for(int Column = 0; Column < 9; Column++){
				PrintRow = PrintRow + PrintedSudoku[Row][Column] + " ";
			}
			
			System.out.println(PrintRow);
		}

		System.out.println("-------------------------");
	}
	
	
	public int[][] getFields() {
		return Fields;
	}
	
}
