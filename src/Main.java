public class Main {

    private final static String dictionaryPath = "data/dictionary.txt";
    private final static String boardPath = "data/board.txt";
    private final static int boardLength = 4;

    public static void main(String[] args) {

        Boggle board = new Boggle(boardLength, dictionaryPath, boardPath);
        board.displayBoard();
        board.findValidWordsOnBoard();
        board.exportValidWordsToFile();
    }
}
