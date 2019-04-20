import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class ChessGUI extends Application
{
    public static void main(String args[])
    {
        launch();
    }
    
    public void start(Stage stage)
    {
        BorderPane mainPane = new BorderPane();
        
        Text currentPlayer = new Text("It is the turn of the white player!");
        currentPlayer.setTextAlignment(TextAlignment.JUSTIFY);
        
        Button quitBtn = new Button("Quit");
        Button helpBtn = new Button("Help");
        HBox buttonMenu = new HBox();
        //buttonMenu.setSpacing(25);
        buttonMenu.getChildren().addAll(helpBtn, quitBtn);
        buttonMenu.setAlignment(Pos.CENTER);
        buttonMenu.setPadding(new Insets(10, 20, 20, 20));
        
        GridPane chessBoardPane = getChessBoardPane(mainPane);
        mainPane.setCenter(chessBoardPane);
        mainPane.setTop(currentPlayer);       
        mainPane.setBottom(buttonMenu);
        
        BorderPane.setAlignment(currentPlayer, Pos.CENTER);
        BorderPane.setAlignment(chessBoardPane, Pos.CENTER);
        
        Scene scene = new Scene(new Group(mainPane), 1300, 900);
        stage.setScene(scene);
        stage.show();
        
        mainPane.prefWidthProperty().bind(scene.widthProperty());
        mainPane.prefHeightProperty().bind(scene.heightProperty());
    }
    
    private GridPane getChessBoardPane(BorderPane pane)
    {
        GridPane chessBoardPane = new GridPane();
        chessBoardPane.setAlignment(Pos.CENTER);
        chessBoardPane.setVgap(0);
        chessBoardPane.setHgap(0);
        chessBoardPane.setPadding(new Insets(25, 25, 25, 25));
        
        Image blackSquare = null;
        Image whiteSquare = null;
        try 
        {
            blackSquare = new Image(new FileInputStream("graphics//brown square.png"));
            whiteSquare = new Image(new FileInputStream("graphics//white square.png"));
        }
        catch (FileNotFoundException e) 
        {
            e.printStackTrace();
            System.exit(-1);
        }
        
        for (int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                StackPane squarePane = new StackPane();
                
                if(i % 2 == 0)
                {
                    if(j % 2 == 0)
                    {
                        squarePane.getChildren().add(getSquareImage(blackSquare));
                    }
                    else
                    {
                        squarePane.getChildren().add(getSquareImage(whiteSquare));
                    }
                }
                else
                {
                    if(j % 2 == 0)
                    {
                        squarePane.getChildren().add(getSquareImage(whiteSquare));
                    }
                    else
                    {
                        squarePane.getChildren().add(getSquareImage(blackSquare));
                    }
                }
               
                chessBoardPane.add(squarePane, i, j);
            }
        }
        
        return chessBoardPane;
    }
    
    private ImageView getSquareImage(Image image)
    {
        ImageView newSquare = new ImageView(image);
        newSquare.setFitHeight(100);
        newSquare.setFitWidth(100);
        
        return newSquare;
    }
}
