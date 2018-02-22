import java.util.ArrayList;
import java.util.Arrays;

public class Sudoku3 {

    public final Integer[][] fields;
    private Integer[][] solution;

    //private final ArrayList<Integer> basicValues = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
    private final Integer[] basicValues = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9};

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

        stepSolve(0, 0);

        return solution;
    }

    private void stepSolve(Integer row, Integer column){

        System.out.println("stepSolve row: " + row + ", column: " + column);
        //System.out.println(Arrays.deepToString(solution));
        if(fields[row][column] != 0){
             stepSolve(getNextStepRow(row, column), getNextStepColumn(column));
        } else if(solution[row][column] == 0){

            ArrayList<Integer> possibleValues = getPossibleValuesForField(row, column);
            switch(possibleValues.size()){
                case 0:
                    System.out.println("Out of ammo!");
                    System.out.println("Getting back to row: " + row + " column: " + column);
                    returnToField(getLastModifiedFieldRow(row, column), getLastModifiedFieldColumn(row, column));
                default:
                    System.out.println("Helol!");
                    solution[row][column] = possibleValues.get(0);
                    stepSolve(getNextStepRow(row, column), getNextStepColumn(column));
            }

        } else {

            ArrayList<Integer> filteredPossibleValues = getFilteredPossibleValuesForField(row, column);
            switch(filteredPossibleValues.size()){
                case 0:
                    returnToField(getLastModifiedFieldRow(row, column), getLastModifiedFieldColumn(row, column));
                default:
                    solution[row][column] = filteredPossibleValues.get(0);
                    stepSolve(getNextStepRow(row, column), getNextStepColumn(column));
            }
        }

    }

    private Integer getNextStepRow(Integer row, Integer column){
        return column != 8 ? row : ++row;
    }

    private Integer getNextStepColumn(Integer column){
        return column != 8 ? ++column : 0;
    }

    private Integer getPreviousStepRow(Integer row, Integer column){
        return column != 1 ? row : --row;
    }

    private Integer getPreviousStepColumn(Integer column){
        return column != 1 ? --column : 8;
    }

    private ArrayList<Integer> getPossibleValuesForRow(Integer rowNumber){

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
        ArrayList<Integer> possibleValuesRow = new ArrayList<>(Arrays.asList(basicValues));;
        possibleValuesRow.removeAll(Arrays.asList(takenValues));
        System.out.println("possibleValuesRow: " + possibleValuesRow.size());
        return possibleValuesRow;
    }

    private ArrayList<Integer> getPossibleValuesForColumn(Integer columnNumber){

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
        ArrayList<Integer> possibleValuesColumn = new ArrayList<>(Arrays.asList(basicValues));
        possibleValuesColumn.removeAll(Arrays.asList(takenValues));
        System.out.println("possibleValuesColumnSize: " + possibleValuesColumn.size());

        return possibleValuesColumn;
    }

    private ArrayList<Integer> getPossibleValuesForSquare(Integer row, Integer column){

        //Get the left high corner of square
        Integer squareNumber = squares[row][column];
        Integer squareCornerRow = squareLeftHighCornerRow[squareNumber];
        Integer squareCornerColumn = squareLeftHighCornerColumn[squareNumber];

        ArrayList<Integer> possibleValues = new ArrayList<>(Arrays.asList(basicValues));
        for(int rowCounter = 0; rowCounter < 3; rowCounter++){

            for(int columnCounter = 0; columnCounter < 3; columnCounter++){

                possibleValues.remove((Integer)solution[squareCornerRow + rowCounter][squareCornerColumn + columnCounter]);
            }
        }
       System.out.println("possibleValuesSquare: " + possibleValues.size());
        return possibleValues;
    }

    private ArrayList<Integer> getPossibleValuesForField(Integer row, Integer column){

        ArrayList<Integer> possibleValues = new ArrayList<>(Arrays.asList(basicValues));
        possibleValues.retainAll(getPossibleValuesForRow(row));
        possibleValues.retainAll(getPossibleValuesForColumn(column));
        possibleValues.retainAll(getPossibleValuesForSquare(row, column));

        return possibleValues;
    }

    private Integer getLastModifiedFieldRow(Integer row, Integer column){

        Integer previousRow = getPreviousStepRow(row, column);
        Integer previousColumn = getPreviousStepColumn(column);

        for(int columnCounter = previousColumn; columnCounter > -1; columnCounter--){

            for(int rowCounter = previousRow; rowCounter > -1; rowCounter--){
                if(fields[rowCounter][columnCounter] == 0){return rowCounter;}
            }
        }
        System.out.println("I fucked up!");
        return 0;
    }

    private Integer getLastModifiedFieldColumn(Integer row, Integer column){

        Integer previousRow = getPreviousStepRow(row, column);
        Integer previousColumn = getPreviousStepColumn(column);

        for(int columnCounter = previousColumn; columnCounter > -1; columnCounter--){

            for(int rowCounter = previousRow; rowCounter > -1; rowCounter--){
                if(fields[rowCounter][columnCounter] == 0){return columnCounter;}
            }
        }
        return 0;
    }

    private ArrayList<Integer> getFilteredPossibleValuesForField(Integer row, Integer column){

        ArrayList<Integer> possibleValues = getPossibleValuesForField(row, column);
        Integer indexCurrentValue = possibleValues.indexOf((Integer)solution[row][column]);

        return indexCurrentValue == possibleValues.size() - 1 ?
                null :
                new ArrayList<Integer>(possibleValues.subList(indexCurrentValue, possibleValues.size() - 1));
    }

    //During solving of sudoku it can occur that further permutations
    // were based on false value taken before
    private void returnToField(Integer row, Integer column){

        for(int rowSolution = row; rowSolution < 9; rowSolution++){

            for(int columnSolution = column + 1; columnSolution < 9; columnSolution++){

                solution[rowSolution][columnSolution] = fields[rowSolution][columnSolution];
            }
        }

        stepSolve(row, column);
    }

    private Integer getNextPossibleValue(int row, int column){

        ArrayList<Integer> possibleValues = getPossibleValuesForField(row, column);
        Integer currentValue = solution[row][column];
        Integer indexCurrentValue = possibleValues.indexOf((Integer)currentValue);
        return indexCurrentValue != possibleValues.size() - 1 ? possibleValues.get(indexCurrentValue) : 0;
    }


}
