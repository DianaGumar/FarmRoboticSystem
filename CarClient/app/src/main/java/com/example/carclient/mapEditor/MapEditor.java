package com.example.carclient.mapEditor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class MapEditor {

    public static String[] TranslateArrayToPath(double[][] map){

        if(isClearFrame(map)){
            return MoveTranslator(map);
        }

        return null;
    }

    public static String[] TranslateImageToPath(byte[] bb, int h, int w){

        double[][] map = TranslateBMPToArray(bb, h,w);

        for (double[] doubles : map) {
            System.out.println(Arrays.toString(doubles));
        }

        if(isClearFrame(map)){
            return MoveTranslator(map);
        }

        return null;

    }

    private static double[][] TranslateBMPToArray(byte[] imageBytes, int h, int w){

        //сокращаем RGBA до одного числа
        double[] imageBytes2 = new double[imageBytes.length/4];
        int j = 0;

        for(int z = 0; z < imageBytes.length; z+=4){
            if(imageBytes[z] == -1 && imageBytes[z+1] == -1 && imageBytes[z+2] == -1 && imageBytes[z+3] == -1){
                imageBytes2[j] = 0.0;
            }
            else if(imageBytes[z] == -1 && imageBytes[z+1] == 0 && imageBytes[z+2] == 0 && imageBytes[z+3] == 0){
                imageBytes2[j] = 1.0;
            }
            else if(imageBytes[z] == -1 && imageBytes[z+1] == 26 && imageBytes[z+2] == -111 && imageBytes[z+3] == 69){
                imageBytes2[j] = 0.1;
            }
            else if(imageBytes[z] == -1 && imageBytes[z+1] == 53 && imageBytes[z+2] == 37 && imageBytes[z+3] == -72){
                imageBytes2[j] = 0.5;
            }

            j++;
        }

        double[][] imageBytes3 = new double[h][w];

        int z=0;
        for (int i = 0; i < h; i++){
            for (j = 0; j < w; j++){
                imageBytes3[i][j] = imageBytes2[z];
                z++;
            }
        }

        return imageBytes3;
    }

    //перенести в другой класс
    private static byte[] readMap(String path) {

//        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);

        return stream.toByteArray();
    }

    private static boolean isClearFrame(double[][] map){

        for(int i = 0; i < map.length; i++){
            if(     map[i][0] > 0 &&
                    map[map.length - 1][i] > 0 &&
                    map[0][i] > 0 &&
                    map[i][map[0].length - 1] > 0){

                return false;
            }
//            map[i][0] = 0;
//            map[map.length - 1][i] = 0;
//            map[0][i] = 0;
//            map[i][map[0].length - 1] = 0;
        }

        return true;
    }

    //составляет одномерный массив из двумерной пронумерованной карты
    private static String[] MoveTranslator(double[][] map){

        int start = 1;
        int size = 0;
        int ii = 0;
        int jj = 0;

        //history
        int i_h = 0;
        int j_h = 0;

        //подсчитываем размер будущего массива
        for(int i =0; i < map.length; i++){
            for(int j = 0; j< map[i].length; j++){
                if(map[i][j] > 0) {
                    size++;
                    //проверяем на стартовую точку
                    if(map[i][j] == 0.1) {

                        i_h = i;
                        j_h = j;

                        if(map[i][j+1] > 0){
                            size++;
                            start = 2;
                            ii = i;
                            jj = j+1;

                        }else if(map[i-1][j] > 0){
                            start = 1;
                            ii = i-1;
                            jj = j;
                        }
                    }
                    //проверяем на наличие датчика
                    else if(map[i][j] == 0.5){
                        size++;
                    }

                    //проверка на углы
                    if(map[i][j+1] > 0 &&  map[i][j-1] > 0 || map[i+1][j] > 0 &&  map[i-1][j] > 0) {
                        size++;
                    }

                }
            }
        }

        String[] str =  new String[size];
        System.out.println(size);

        if(start == 1){
            str[0] = "f";
        }else {
            str[0] = "rr";
            str[1] = "f";
        }

        //вытягиваем путь из карты и записываем значения в строку
        for (int z = start; z < size-1; z++){
            map[ii][jj] = z+1;

            //проверка на прямолинейность
            if(map[ii][jj+1] > 0 &&  map[ii][jj-1] > 0 ) {
                str[z] = "f";

                if(j_h == jj - 1){
                    i_h = ii;
                    j_h = jj;
                    jj++;
                }else {
                    i_h = ii;
                    j_h = jj;
                    jj--;
                }

            }
            else if(map[ii+1][jj] > 0 &&  map[ii-1][jj] > 0){
                str[z] = "f";

                if(i_h == ii + 1){
                    i_h = ii;
                    j_h = jj;
                    ii--;
                }else {
                    i_h = ii;
                    j_h = jj;
                    ii++;
                }
            }
            else if(i_h == ii+1 && j_h == jj){
                if(map[ii][jj+1] > 0) {
                    str[z]= "rr";
                    i_h = ii;
                    j_h = jj;
                    jj++;
                }
                else{
                    str[z]= "rl";
                    i_h = ii;
                    j_h = jj;
                    jj--;
                }

                str[z+1]= "f";
                z++;

            }
            else if(i_h == ii-1 && j_h == jj){
                if(map[ii][jj-1] > 0) {
                    str[z]= "rr";
                    i_h = ii;
                    j_h = jj;
                    jj--;
                }
                else{
                    str[z]= "rl";
                    i_h = ii;
                    j_h = jj;
                    jj++;
                }

                str[z+1]= "f";
                z++;
            }
            else if(i_h == ii && j_h == jj-1){
                if(map[ii+1][jj] > 0) {
                    str[z]= "rr";
                    i_h = ii;
                    j_h = jj;
                    ii++;
                }
                else{
                    str[z]= "rl";
                    i_h = ii;
                    j_h = jj;
                    ii--;
                }

                str[z+1]= "f";
                z++;
            }
            else if(i_h == ii && j_h == jj+1){
                if(map[ii-1][jj] > 0) {
                    str[z]= "rr";
                    i_h = ii;
                    j_h = jj;
                    ii--;
                }
                else{
                    str[z]= "rl";
                    i_h = ii;
                    j_h = jj;
                    ii++;
                }

                str[z+1]= "f";
                z++;
            }

            //если есть датчик= добавляем остановку
            if(map[ii][jj] == 0.5){
                str[z+1] = "s";
                z++;
            }


        }


        for (double[] doubles : map) {
            System.out.println(Arrays.toString(doubles));
        }


        str[size-1]="s";
        return str;
    }

}
