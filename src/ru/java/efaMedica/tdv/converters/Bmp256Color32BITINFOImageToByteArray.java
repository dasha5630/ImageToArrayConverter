package ru.java.efaMedica.tdv.converters;

import java.io.*;

/**
 * {@code Bmp256Color32BITINFOImageToByteArray} convert .bmp format Image with 256 colors and 32-bit BITMAPINFO to byte array.
 * Byte array write in .txt file in format:
 * first line: number of elements in array, width of image(x coordinate), height of image(y coordinate)
 * others lines: values of pixels color
 */

public class Bmp256Color32BITINFOImageToByteArray {
    private static StringBuilder sb = new StringBuilder();
    private static StringBuilder sbforWidth = new StringBuilder();
    private static StringBuilder sbforHeight = new StringBuilder();
    private static StringBuilder sbforStartPixelСoordinate = new StringBuilder();
    private static Integer count = 0;
    private static Integer countPixels = 0;
    private static byte []data = {0};
    private static Long width = null;
    private static Long height = null;
    private static Long startPixelСoordinate =15L;

    public static void main(String[] args) {
        sb.append("                        \n"); //add for format output txt

        //read input file
        try(FileInputStream fin=new FileInputStream("C:/Users/tdv/IdeaProjects/ImageToArrayConverter/96-965159_green-check-green-check-symbol-green-check-mark.bmp"))
        {
            System.out.printf("File size: %d bytes \n", fin.available());
            Integer i = -1;
            while((i = fin.read())!=-1){
                    //write in buffer(StringBulder) 4byte image width
                    if(count >= 18 && count <= 21) { //32-bit BITMAPINFO format - 4 byte for width
                        sbforWidth.insert(0, String.format("%02X", i));
                        if(count == 21){
                            width = Long.parseLong(sbforWidth.toString(), 16);
                            sb.insert(1, width.toString() + ", ", 0, width.toString().length());
                        }
                    }

                    //write in buffer(StringBulder) 4byte image height
                    if(count >= 22 && count <= 25) { //32-bit BITMAPINFO format - 4 byte for height
                        sbforHeight.insert(0, String.format("%02X", i));
                        if(count == 25){
                            height = Long.parseLong(sbforHeight.toString(), 16);
                            sb.insert(width.toString().length() + 2,
                                    height.toString() + ", ",
                                    0,
                                    height.toString().length());

                        }
                    }
                    //write in buffer(StringBulder) adress when pixels values start
                    if(count >= 10 && count <= 13) {
                        sbforStartPixelСoordinate.insert(0, String.format("%02X", i));
                        if(count == 13){
                            startPixelСoordinate = Long.parseLong(sbforStartPixelСoordinate.toString(), 16);
                           }
                    }

                    //write in buffer(StringBulder) pixels values
                    if(count.longValue() >= startPixelСoordinate){
                        sb.append(String.format("0x%02X", i) + ", ");
                        countPixels++;
                        if (count % 16 == 0 && count != 0) {
                            sb.append("\n");

                        }
                    }
                    count++;
                }
            //write in buffer(StringBulder) number of pixels values
            sb.insert(0, countPixels.toString() + ", ", 0, countPixels.toString().length());
        }

        catch(IOException ex){
            System.out.println(ex.getMessage());
        }

        //write in file
        try(FileOutputStream fos=new FileOutputStream("C:/Users/tdv/IdeaProjects/ImageToArrayConverter/96-965159_green-check-green-check-symbol-green-check-mark.txt"))
        {
            byte[] buffer = sb.toString().getBytes();
            fos.write(buffer, 0, buffer.length);
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }
}



