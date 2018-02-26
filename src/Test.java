import java.util.Arrays;

public class Test {

    public static void main(String[] args){

        Integer[][] fields = new Integer[][]{
                {5, 3, 0, 0, 7, 0, 0, 0, 0},
                {6, 0, 0, 1, 9, 5, 0, 0, 0},
                {0, 9, 8, 0, 0, 0, 0, 6, 0},
                {8, 0, 0, 0, 6, 0, 0, 0, 3},
                {4, 0, 0, 8, 0, 3, 0, 0, 1},
                {7, 0, 0, 0, 2, 0, 0, 0, 6},
                {0, 6, 0, 0, 0, 0, 2, 8, 0},
                {0, 0, 0, 4, 1, 9, 0, 0, 5},
                {0, 0, 0, 0, 8, 0, 0, 7, 9}
                };

        Integer[][] fields2 = new Integer[][]{
                {0, 0, 0, 0, 0, 7, 5, 0, 0},
                {7, 0, 0, 1, 0, 0, 0, 4, 0},
                {5, 0, 0, 0, 0, 0, 2, 0, 0},
                {0, 0, 1, 3, 9, 0, 0, 0, 8},
                {3, 0, 0, 7, 8, 6, 0, 0, 4},
                {8, 0, 0, 0, 4, 1, 7, 0, 0},
                {0, 0, 8, 0, 0, 0, 0, 0, 9},
                {0, 5, 0, 0, 0, 3, 0, 0, 1},
                {0, 0, 4, 6, 0, 0, 0, 0, 0}
        };


        Sudoku3 sudoku = new Sudoku3(fields2);
        prettyPrint(sudoku.getSolution());
        //System.out.println(Arrays.deepToString(sudoku.getSolution()));
    }

    public static void prettyPrint(Integer[][] sudoku){
        for(int row = 0; row < 9; row++){
            for(int column = 0; column < 9; column++){
                System.out.print(sudoku[row][column] + " ");
            }
            System.out.println();
        }
    }

}
