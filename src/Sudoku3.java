import java.util.ArrayList;
import java.util.Arrays;

public class Sudoku3 {

    public final Integer[][] fields;
    private Integer[][] solution;
    private final ArrayList<Integer> basicValues = new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));

    /*
     0|1|2
     3|4|5
     6|7|8
    */
    private final Integer[][] squares = new Integer[][]{
            {0, 0, 0, 1, 1, 1, 2, 2, 2},
            {0, 0, 0, 1, 1, 1, 2, 2, 2},
            {0, 0, 0, 1, 1, 1, 2, 2, 2},
            {3, 3, 3, 4, 4, 4, 5, 5, 5},
            {3, 3, 3, 4, 4, 4, 5, 5, 5},
            {3, 3, 3, 4, 4, 4, 5, 5, 5},
            {6, 6, 6, 7, 7, 7, 8, 8, 8},
            {6, 6, 6, 7, 7, 7, 8, 8, 8},
            {6, 6, 6, 7, 7, 7, 8, 8, 8}
    };

    private final Integer[] squareLeftHighCornerRow = new Integer[]{
            0, 0, 0, 3, 3, 3, 6, 6, 6
    };

    private final Integer[] squareLeftHighCornerColumn = new Integer[]{
            0, 3, 6, 0, 3, 6, 0, 3, 6
    };

    Sudoku3 (Integer[][]fields){
        this.fields = fields;
        this.solution = fields;
    }

    Integer[][] getSolution(){


        return null;
    }

    ArrayList<Integer> getPossibleValuesForRow(Integer rowNumber){

        //Analyse which values are already taken
        Integer[] takenValues = new Integer[]{
                solution[rowNumber][0],
                solution[rowNumber][1],
                solution[rowNumber][2],
                solution[rowNumber][3],
                solution[rowNumber][4],
                solution[rowNumber][5],
                solution[rowNumber][6],
                solution[rowNumber][7],
                solution[rowNumber][8]};

        //Check what values are still possible
        ArrayList<Integer> possibleValuesRow = basicValues;
        possibleValuesRow.removeAll(Arrays.asList(takenValues));

        return possibleValuesRow;
    }

    ArrayList<Integer> getPossibleValuesForColumn(Integer columnNumber){

        //Analyse which values are already taken
        Integer[] takenValues = new Integer[]{
                solution[0][columnNumber],
                solution[1][columnNumber],
                solution[2][columnNumber],
                solution[3][columnNumber],
                solution[4][columnNumber],
                solution[5][columnNumber],
                solution[6][columnNumber],
                solution[7][columnNumber],
                solution[8][columnNumber]};

        //Check what values are still possible
        ArrayList<Integer> possibleValuesColumn = basicValues;
        possibleValuesColumn.removeAll(Arrays.asList(takenValues));

        return possibleValuesColumn;
    }

    ArrayList<Integer> getPossibleValuesForSquare(Integer row, Integer column){

        //Get the left high corner of square
        Integer squareNumber = squares[row][column];
        Integer squareCornerRow = squareLeftHighCornerRow[squareNumber];
        Integer squareCornerColumn = squareLeftHighCornerColumn[squareNumber];

        ArrayList<Integer> possibleValues = basicValues;

        for(int rowCounter = 0; rowCounter < 3; rowCounter++){

            for(int columnCounter = 0; columnCounter < 3; columnCounter++){

                possibleValues.remove((Integer)solution[rowCounter][columnCounter]);
            }
        }

        return possibleValues;
    }

    ArrayList<Integer> getPossibleValuesForField(Integer row, Integer column){

        ArrayList<Integer> possibleValues = basicValues;
        possibleValues.retainAll(getPossibleValuesForRow(row));
        possibleValues.retainAll(getPossibleValuesForColumn(column));
        possibleValues.retainAll(getPossibleValuesForSquare(row, column));

        return possibleValues;
    }

    //During solving of sudoku it can occur that further permutations
    // were based on false value taken before
    private void returnToField(int row, int column){

        for(int rowSolution = row; rowSolution < 9; rowSolution++){

            for(int columnSolution = column; columnSolution < 9; columnSolution++){

                solution[rowSolution][columnSolution] = fields[rowSolution][columnSolution];

            }

        }

    }

    //Method returns all possible rows given input fields
    /*
    private ArrayList<Integer[]> permute(ArrayList<Integer> row){

        ArrayList<Integer[]> possibleValues = ;
        ArrayList<Integer[]> permutations;





        return null;
    }
*/
}
