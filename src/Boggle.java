import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;

public class Boggle {

    //length of a N*N boggle board
    private int boardLength;
    private char[][] board;
    //dictionary contains all valid words that can be used by player to earn points
    private Trie dictionary = new Trie();
    //validWordsOnBoard contains all valid words that are on boggle board
    private Set<String> validWordsOnBoard = new HashSet<String>();

    public Boggle(int boardLength, final String dictionaryPath, final String boardPath) {

        this.boardLength = boardLength;
        board = new char[boardLength][boardLength];
        readBoard(boardPath, board);
        readDictionary(dictionaryPath);
    }

    private void readDictionary(final String filePath) {

        try{
            InputStream in = Files.newInputStream(FileSystems.getDefault().getPath(filePath));
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line = null;

            while ((line = reader.readLine()) != null) {

                if(line.length() >= 3){
                    dictionary.add(line.toLowerCase());
                }
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private void readBoard(final String boardFile, char board[][]) {

        byte[] bytes;
        String content;

        try{
            bytes = Files.readAllBytes(new File(boardFile).toPath());
            content = new String(bytes, StandardCharsets.UTF_8);
            content = content.toLowerCase();

            int contentStringIndex = 0;

            for (int i = 0; i < boardLength; i++) {
                for (int j = 0; j < boardLength; j++) {
                    board[i][j] = content.charAt(contentStringIndex++);
                }
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public void displayBoard(){

        for (int i = 0; i < boardLength; i++) {
            System.out.print("\n");
            for (int j = 0; j < boardLength; j++) {
                System.out.print(board[i][j] + " ");
            }
        }
    }

    /**find all words starting with each word on board using depth first traversal
    each word found which also appears in dictionary is added to validWordsOnBoard*/
    private void findWordsOnBoard(String word, boolean[][] visited, char[][] board, int i, int j) {

        //mark visited word cell as true so we don't use it again in the formation of a word
        visited[i][j] = true;
        //add the word of cell just visited to the end of the word
        word += board[i][j];

        //if word is valid, add it to validWordsOnBoard, else ignore
        if(word.length() >= 3 && dictionary.search(word))
            validWordsOnBoard.add(word);

        for(int row = i - 1; row <= i + 1 && row < board.length; row++) {

            for(int column = j - 1; column <= j+ 1 && column < board[0].length; column++) {

                if(row >= 0 && column >= 0 && !visited[row][column]){

                    if(dictionary.hasPrefix(word)){
                        findWordsOnBoard(word, visited, board, row, column);
                    }
                }
            }
        }

        //take out the last letter (of just visited cell) from word
        String temp = word.substring(0,word.length()-1);
        word = temp;
        visited[i][j] = false;
    }

    /**find all words on boggle board which are in dictionary*/
    public void findValidWordsOnBoard(){

        //mark each visited cell to ensure it gets used only once while forming a word
        boolean[][] visited = new boolean[boardLength][boardLength];
        String word = "";

        for(int i = 0; i < boardLength; i++) {
            for(int j = 0; j < boardLength; j++)
                findWordsOnBoard(word, visited, board, i, j);
        }
    }

    /**export all valid words found on boggle board to data/validWords.txt */
    public void exportValidWordsToFile(){

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter( new FileWriter( "data/validWords.txt"));
            if(validWordsOnBoard.size() == 0){
                writer.write("No valid words on board found");
            }else{
                for(String validWord : validWordsOnBoard) writer.write(validWord + "\n");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if ( writer != null) writer.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public int getBoardLength() {
        return boardLength;
    }

    public Set<String> getValidWordsOnBoard() {
        Set<String> validWordsOnBoardCopy = new HashSet<String>();
        validWordsOnBoardCopy.addAll(validWordsOnBoard);
        return validWordsOnBoardCopy;
    }
}
