
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class SpriteSlicer 
{
    public static void main(String args[])
    {
        /*Image board = null;
        try
        {
            board = new Image(new FileInputStream("board.png"));
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
            System.exit(-1);
        }
        
        PixelReader pixelReader = board.getPixelReader();
        
        int width = (int) board.getWidth();
        int height = (int) board.getHeight();
        double increment = board.getWidth()/8;
        */
        
        Image square = getFrameSquare(10, 251, 251, Color.BLUE);
        BufferedImage bImage = SwingFXUtils.fromFXImage(square, null);
        try 
        {
            ImageIO.write(bImage, "png", new File("D:\\assets\\Chess\\selection.png"));
        }
        catch (IOException e) 
        {
            e.printStackTrace();
            System.exit(-1);
        }
        
        square = getFrameSquare(10, 251, 251, Color.GREEN);
        bImage = SwingFXUtils.fromFXImage(square, null);
        try 
        {
            ImageIO.write(bImage, "png", new File("D:\\assets\\Chess\\promotion.png"));
        }
        catch (IOException e) 
        {
            e.printStackTrace();
            System.exit(-1);
        }
        
        square = getFrameSquare(10, 251, 251, Color.PURPLE);
        bImage = SwingFXUtils.fromFXImage(square, null);
        try 
        {
            ImageIO.write(bImage, "png", new File("D:\\assets\\Chess\\castle.png"));
        }
        catch (IOException e) 
        {
            e.printStackTrace();
            System.exit(-1);
        }
    }
    
    public static Image getFrameSquare(int thickness, int width, int height, Color color)
    {
        WritableImage square = new WritableImage(width, height);
        PixelWriter pixelWriter = square.getPixelWriter();
        
        for(int k = 0; k < thickness; k++)
        {
            for(int i = k; i < width - k; i++)
            {
                pixelWriter.setColor(i, k, color);
                pixelWriter.setColor(i, height - k - 1, color);
            }
            
            for(int i = k + 1; i < height - k - 1; i++)
            {
                pixelWriter.setColor(k, i, color);
                pixelWriter.setColor(width - k - 1, i, color);
            }
        }
        
        return square;
    }
}
