package pj.jordanlin.imageeditor;

import java.io.File;
import java.io.PrintWriter;
import java.lang.Math;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class ImageEditor
{

    public static void main(String [] args)
    {
        try
        {
            //read the file
            FileReader reader = new FileReader((args[0]));
            Scanner in = new Scanner((reader));
            in.useDelimiter("((#[^\\n]*\\n)|(\\s+))+");
            in.next();
            int width = in.nextInt();
            int height = in.nextInt();
            in.nextInt();
            Pixel[][] image = new Pixel[height][width];
            for(int i = 0; i< height ; i++)
            {
                for (int j=0 ; j < width; j++)
                {
                    image[i][j] = new Pixel(in.nextInt(),in.nextInt(),in.nextInt());
                }
            }


            //process the file
            Pixel[][] newImage = new Pixel[height][width];
            String CommandLine = args[2];
            if(CommandLine.equals("invert"))
            {
                for(int i = 0; i< height ; i++)
                {
                    for (int j=0 ; j < width; j++)
                    {
                        int r = 255 - image[i][j].getR();
                        int g = 255 - image[i][j].getG();
                        int b = 255 - image[i][j].getB();
                        newImage[i][j] = new Pixel(r,g,b);
                    }
                }
            }
            else if (CommandLine.equals("grayscale"))
            {
                for(int i = 0; i< height ; i++)
                {
                    for (int j=0 ; j < width; j++)
                    {
                        int g = image[i][j].getAverage();
                        newImage[i][j] = new Pixel(g,g,g);
                    }
                }
            }
            else if(CommandLine.equals("emboss"))
            {
               for(int i = 0; i< height; i++)
               {
                   for(int j = 0; j< width; j++)
                   {
                       int nextI = i -1;
                       int nextJ = j -1;
                       int v = 128;
                       int maxDifference ;

                       if(nextI < 0 || nextJ < 0)
                       {
                           newImage[i][j] = new Pixel(v,v,v);
                       }
                       else
                       {
                           int redDiff = image[i][j].getR() - image[nextI][nextJ].getR();
                           int greenDiff = image[i][j].getG() - image[nextI][nextJ].getG();
                           int blueDiff = image[i][j].getB() - image[nextI][nextJ].getB();
                           maxDifference = Math.max(Math.abs(blueDiff),Math.max(Math.abs(redDiff),Math.abs(greenDiff)));

                           if(Math.abs(redDiff) == maxDifference)
                           {
                               v += redDiff;
                           }
                           else if (Math.abs(greenDiff) == maxDifference)
                           {
                               v += greenDiff;
                           }
                           else
                           {
                               v+= blueDiff;
                           }
                           if(v < 0)
                               v = 0;
                           else if(v > 255)
                               v = 255;
                           newImage[i][j] = new Pixel(v,v,v);
                       }
                   }
               }
            }
            else if(CommandLine.equals("motionblur"))
            {
                int blurLength = Integer.parseInt(args[3]);
                for(int i = 0; i< height; i++)
                {
                    int n = blurLength;
                    for (int j = 0; j < width; j++)
                    {

                        if(j+blurLength> width)
                        {
                            n = width - j;
                        }
                        int redSum =0 , greenSum =0 , blueSum = 0;
                        for (int k = 0; k < n; k++)
                        {
                           redSum += image[i][j+k].getR();
                           greenSum += image[i][j+k].getG();
                           blueSum += image[i][j+k].getB();
                        }
                        if(n > 0)
                        {
                            newImage[i][j] = new Pixel(redSum/n,greenSum/n,blueSum/n);
                        }
                        else
                        {
                            newImage[i][j] = new Pixel(image[i][j].getR(),image[i][j].getG(),image[i][j].getB());
                        }
                    }
                }
            }
            else
            {
                System.out.println("Error");
            }

            //print the file
            PrintWriter pw = new PrintWriter(new File(args[1]));
            pw.println("P3");
            pw.println(width);
            pw.println(height);
            pw.println("255");
            for(int i = 0; i< height; i++) {
                for (int j = 0; j < width; j++) {
                    pw.println(newImage[i][j].getR());
                    pw.println(newImage[i][j].getG());
                    pw.println(newImage[i][j].getB());
                }
            }
            pw.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }
}
