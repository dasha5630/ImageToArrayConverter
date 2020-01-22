package ru.java.efaMedica.tdv.converters;

import java.io.*;

public class Bmp24BitImageToByteArray {
    private static StringBuilder sb = new StringBuilder();
    private static StringBuilder sbforWidth = new StringBuilder();
    private static StringBuilder sbforHeight = new StringBuilder();
    private static StringBuilder sbforStartPixelСoordinate = new StringBuilder();
    private static StringBuilder sbRGB = new StringBuilder();
    private static Long rgbPixelValue = 0L;
    private static Integer red = 0;
    private static Integer green = 0;
    private static Integer blue = 0;
    private static Integer count = 0;
    private static Integer countPixels = 0;
    private static byte []data = {0};
    private static Long width = null;
    private static Long height = null;
    private static Long startPixelСoordinate =15L;
    private static int countTwo = 0;

    public static void main(String[] args) {
        sb.append("                        \n"); //add for format output txt

        //read input file
        try(FileInputStream fin=new FileInputStream("C:/Users/tdv/IdeaProjects/ImageToArrayConverter/ne_ok.bmp"))
        {
            System.out.printf("File size: %d bytes \n", fin.available());
            Integer i = -1;
            int state = 0;
            while((i = fin.read())!=-1){
                //write in buffer(StringBulder) 4byte image width
                if(count >= 18 && count <= 21) { //32-bit BITMAPINFO format - 4 byte for width
                    sbforWidth.insert(0, String.format("%02X", i));
                    if(count == 21){
                        width = Long.parseLong(sbforWidth.toString(), 16);
                        sb.append(width.toString() + ", ");
                    }
                }

                //write in buffer(StringBulder) 4byte image height
                if(count >= 22 && count <= 25) { //32-bit BITMAPINFO format - 4 byte for height
                    sbforHeight.insert(0, String.format("%02X", i));
                    if(count == 25){
                        height = Long.parseLong(sbforHeight.toString(), 16);
                        sb.append(height.toString() + ", \n");

                    }
                }
                //write in buffer(StringBulder) adress when pixels values start
                if(count >= 10 && count <= 13) {
                    sbforStartPixelСoordinate.insert(0, String.format("%02X", i));
                    if(count == 13){
                        startPixelСoordinate = Long.parseLong(sbforStartPixelСoordinate.toString(), 16);
                        System.out.println(startPixelСoordinate.toString());
                    }
                }

                //write in buffer(StringBulder) pixels values
                if(count.longValue() >= startPixelСoordinate){
                    switch (state){
                        case 0:
                            red = i;
                            if(red < 128) red = 0;
                            else red = 1;
                            state = 1;
                            break;

                        case 1:
                            green = i;
                            if(green < 128) green = 0;
                            else green = 1;
                            state = 2;
                            break;

                        case 2:
                            blue = i;
                            if(blue < 128) blue = 0;
                            else blue = 1;
                            countTwo++;

                            if(countTwo == 1) sb.append("0x");
                            if(red == 0 && green == 0 && blue == 0) sb.append(String.format("%01X", 0));
                            else if(red == 0 && green == 0 && blue != 0) sb.append(String.format("%01X", 12));
                            else if(red == 0 && green != 0 && blue == 0) sb.append(String.format("%01X", 10));
                            else if(red == 0 && green != 0 && blue != 0) sb.append(String.format("%01X", 14));
                            else if(red != 0 && green == 0 && blue == 0) sb.append(String.format("%01X", 9));
                            else if(red != 0 && green == 0 && blue != 0) sb.append(String.format("%01X", 13));
                            else if(red != 0 && green != 0 && blue == 0) sb.append(String.format("%01X", 11));
                            else if(red != 0 && green != 0 && blue != 0) sb.append(String.format("%01X", 15));
                            else sb.append(String.format("%01X", 15));
                            if(countTwo == 2) {
                                sb.append(", ");
                                if (countPixels % 16 == 0 && countPixels != 0) {
                                    sb.append("\n");
                                }
                                countPixels++;
                                countTwo = 0;

                            }

                            state = 0;
                            break;
                    }

/*                    sbRGB.insert(0, String.format("%02X", i));
                    if(count%3 == 0 && count !=0){
                        rgbPixelValue = Long.parseLong(sbRGB.toString(), 16);
                        sbRGB.setLength(0);
                        System.out.print(rgbPixelValue.toString());
                        sb.append(String.format("0x%02X", i) + ", ");
                        if (count % 16 == 0 && count != 0) {
                            sb.append("\n");
                        }
                        countPixels++;
                    }*/

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
        try(FileOutputStream fos = new FileOutputStream("C:/Users/tdv/IdeaProjects/ImageToArrayConverter/ne_ok.txt"))
        {
            byte[] buffer = sb.toString().getBytes();
            fos.write(buffer, 0, buffer.length);
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }
}


/*                //write in buffer(StringBulder) pixels values
                if(count.longValue() >= startPixelСoordinate){
                    if(countPixels%4 == 0 && countPixels !=0){
                        rgbPixelValue = Long.parseLong(sbRGB.toString(), 16);
                        System.out.print(rgbPixelValue.toString());
                        sb.append(String.format("0x%02X", rgbPixelValue.toString()) + ", ");
                        if (count % 16 == 0 && count != 0) {
                            sb.append("\n");
                        }
                        countPixels++;
                    }
                    sbRGB.insert(0, String.format("%02X", i));
                }*/
