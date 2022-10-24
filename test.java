//package com.java2novice.ds.queue;
import java.util.*;

public class test {


    public static String addBinary(String a, String b) {
        int sum = 0;
        for (int i = 0, j = a.length(); i < a.length() && j > 0; i++, j--) {
            sum += Integer.parseInt(a.substring(j - 1, j)) * Math.pow(2, i);
        }
        for (int i = 0, j = b.length(); i < b.length() && j > 0; i++, j--) {
            sum += Integer.parseInt(b.substring(j - 1, j)) * Math.pow(2, i);
        }

        String rv = "";
        for (int i = sum; i > 0; i = i / 2) {
            if (i % 2 == 1) rv = "1" + rv;
            else rv = "0" + rv;
        }

        return rv;

    }

    public static void main(String[] args){
        System.out.println(addBinary("1010", "1011"));

    }
}
