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
    private Set<String> dictionary = new HashSet<String>();

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
                    dictionary.add(line);
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
}
