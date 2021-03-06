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

    private Integer rowBeginn = 0;

    private Integer columnBeginn = 0;

    private Integer solveCounter = 0;

    Sudoku3 (Integer[][]fields){
        this.fields = fields;

        this.solution = new Integer[fields.length][];
        for(int row = 0; row < fields.length; row++){
            this.solution[row] = fields[row].clone();
        }
    }

    Integer[][] getSolution(){

        System.out.println("isSudokuSolved(): " + isSudokuSolved());

        while(isSudokuSolved() == false){
            stepSolve(rowBeginn, columnBeginn);
        }

        return solution;
    }

    private void stepSolve(Integer row, Integer column){

        solveCounter++;
        System.out.println("stepSolve row: " + row + ", column: " + column);
        System.out.println(Arrays.deepToString(solution));
        System.out.println("solveCounter: " + solveCounter);

        if(solveCounter < 1000 && row < 9) {

            if (fields[row][column] != 0) {
                stepSolve(getNextStepRow(row, column), getNextStepColumn(column));
            } else if (solution[row][column] == 0) {

                ArrayList<Integer> possibleValues = getPossibleValuesForField(row, column);
                switch (possibleValues.size()) {
                    case 0:

                        System.out.println("Out of ammo!");
                        System.out.println("Getting back to row: " + getLastModifiedFieldRow(row, column) +
                                " column: " + getLastModifiedFieldColumn(row, column));

                        stepSolve(getLastModifiedFieldRow(row, column), getLastModifiedFieldColumn(row, column));
                        return;
                    default:
                        System.out.println("Helol!");
                        solution[row][column] = possibleValues.get(0);
                        stepSolve(getNextStepRow(row, column), getNextStepColumn(column));
                }

            } else if (solution[row][column] != 0) {

                Integer tempValue = solution[row][column];
                solution[row][column] = 0;

                ArrayList<Integer> filteredPossibleValues = getFilteredPossibleValuesForField(row, column, tempValue);
                System.out.println("filteredPossibleValuesSize: " + filteredPossibleValues.size());
                System.out.println("tempValue: " + tempValue);

                switch (filteredPossibleValues.size()) {
                    case 0:

                        System.out.println("case 0");
                        stepSolve(getLastModifiedFieldRow(row, column), getLastModifiedFieldColumn(row, column));
                        return;
                    default:

                        System.out.println("default > 0");
                        solution[row][column] = filteredPossibleValues.get(0);
                        stepSolve(getNextStepRow(row, column), getNextStepColumn(column));
                }
            }
        } else {
            rowBeginn = row;
            columnBeginn = column;
            solveCounter = 0;
            return;
        }
    }

    private boolean isSudokuSolved(){
        for(int row = 0; row < 9; row++){

            int sum = 0;
            for(int column = 0; column < 9; column++){
                sum = sum + solution[row][column];
            }
            if(sum != 45){return false;}
        }

        for(int column = 0; column < 9; column++){

            int sum = 0;
            for(int row = 0; row < 9; row++){
                sum = sum + solution[row][column];
            }
            if(sum != 45){return false;}
        }

        return true;
    }

    private Integer getNextStepRow(Integer row, Integer column){
        return column != 8 ? row : ++row;
    }

    private Integer getNextStepColumn(Integer column){
        return column != 8 ? ++column : 0;
    }

    private Integer getPreviousStepRow(Integer row, Integer column){
        return column != 0 ? row : --row;
    }

    private Integer getPreviousStepColumn(Integer column){
        return column != 0 ? --column : 8;
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

        return possibleValues;
    }

    private ArrayList<Integer> getPossibleValuesForField(Integer row, Integer column){

        ArrayList<Integer> possibleValues = new ArrayList<>(Arrays.asList(basicValues));
        possibleValues.retainAll(getPossibleValuesForRow(row));
        possibleValues.retainAll(getPossibleValuesForColumn(column));
        possibleValues.retainAll(getPossibleValuesForSquare(row, column));
        System.out.println("PossibleValues: " + possibleValues.size());
        return possibleValues;
    }

    private Integer getLastModifiedFieldRow(Integer row, Integer column){

        Integer previousRow = getPreviousStepRow(row, column);
        Integer previousColumn = getPreviousStepColumn(column);
        if(row == 1 && column == 1){
            System.out.println(Arrays.deepToString(fields));
        }
        for(int rowCounter = previousRow; rowCounter > -1; rowCounter--){

            for(int columnCounter = previousColumn; columnCounter > -1; columnCounter--){
                if(fields[rowCounter][columnCounter] == 0){return rowCounter;}
            }
            previousColumn = 8; //Start next loop from the end of row
        }

        System.out.println("I fucked up looking for row!");
        return 0;
    }

    private Integer getLastModifiedFieldColumn(Integer row, Integer column){

        Integer previousRow = getPreviousStepRow(row, column);
        Integer previousColumn = getPreviousStepColumn(column);

        if(row == 4 && column == 1){
            System.out.println("previousRow: " + previousRow);
            System.out.println("previousColumn: " + previousColumn);
        }
        for(int rowCounter = previousRow; rowCounter > -1; rowCounter--){

            for(int columnCounter = previousColumn; columnCounter > -1; columnCounter--){
                if(fields[rowCounter][columnCounter] == 0){return columnCounter;}
            }
            previousColumn = 8;  //Start next loop from the end of row
        }

        System.out.println("I fucked up looking for column!");
        return 0;
    }

    private ArrayList<Integer> getFilteredPossibleValuesForField(Integer row, Integer column, Integer tempValue){

        ArrayList<Integer> possibleValues = getPossibleValuesForField(row, column);
        Integer indexCurrentValue = possibleValues.indexOf((Integer)tempValue);

        System.out.println("Metoda getFilteredPossibleValuesForField");
        System.out.println("indexCurrentValue " + indexCurrentValue);
        System.out.println("possibleValuesSize " + possibleValues.size());
        System.out.println("PossibleValues: " + possibleValues.toString());

        return indexCurrentValue == possibleValues.size() - 1 ?
                new ArrayList<Integer>() :
                new ArrayList<Integer>(possibleValues.subList(indexCurrentValue + 1, possibleValues.size()));
    }

    //During solving of sudoku it can occur that further permutations
    // were based on false value taken before
    private void returnToField(Integer row, Integer column){

        for(int rowSolution = row; rowSolution < 9; rowSolution++){

            for(int columnSolution = column + 1; columnSolution < 9; columnSolution++){

                solution[rowSolution][columnSolution] = fields[rowSolution][columnSolution];
            }
        }

        System.out.println("Solution array after method returnToField(row: " + row + " column: " + column + ")");
        System.out.println(Arrays.deepToString(solution));

        stepSolve(row, column);
    }

    private Integer getNextPossibleValue(int row, int column){

        ArrayList<Integer> possibleValues = getPossibleValuesForField(row, column);
        Integer currentValue = solution[row][column];
        Integer indexCurrentValue = possibleValues.indexOf((Integer)currentValue);
        return indexCurrentValue != possibleValues.size() - 1 ? possibleValues.get(indexCurrentValue) : 0;
    }


}
