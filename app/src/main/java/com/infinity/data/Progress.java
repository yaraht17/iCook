package com.infinity.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Progress {
    public static boolean CheckList(String s,String[] input){

        ArrayList<String> arinput = new ArrayList<>();
        for(int i=0;i<input.length;i++) arinput.add(input[i]);
        List<String> list = arinput;
        Set<String> set = new HashSet<String>(list);
        return set.contains(s);

    }
    public static ArrayList<String> tokenizer(ArrayList<String> arinput) {
        ArrayList<String> aroutput = new ArrayList<>();
        String soche = "";
        String thuchien = "";
        String cacdung = "";
        String machnho = "";
        List<String> list = arinput;
        Set<String> set = new HashSet<String>(list);
        boolean checkSoche = set.contains("SƠ CHẾ");
        boolean checkThuchien = set.contains("THỰC HIỆN");
        boolean checkCachdung = set.contains("CÁCH DÙNG");
        boolean checkMachnho = set.contains("MÁCH NHỎ");
        int k = -1;
        if (checkSoche) {
            soche += "SƠ CHẾ . ";
            if (checkThuchien)
                for (int i = 1; i < arinput.size(); i++) {
                    if (arinput.get(i).equals("THỰC HIỆN")) {
                        k = i;
                        break;
                    } else
                        soche += arinput.get(i) + " ";
                }
        }

        if (checkThuchien) {
            thuchien += "THỰC HIỆN . ";
            if (checkCachdung) {
                for (int i = k + 1; i < arinput.size(); i++) {
                    if (arinput.get(i).equals("CÁCH DÙNG")) {
                        k = i;
                        break;
                    } else {
                        thuchien += arinput.get(i) + " ";
                    }

                }
            } else {
                if (checkMachnho) {
                    for (int i = k + 1; i < arinput.size(); i++)
                        if (arinput.get(i).equals("MÁCH NHỎ")) {
                            k = i;
                            break;
                        } else {
                            thuchien += arinput.get(i) + " ";
                        }
                } else {
                    for (int i = k + 1; i < arinput.size(); i++) thuchien += arinput.get(i) + " ";
                }
            }
        }

        if (checkCachdung) {
            cacdung += "CÁCH DÙNG . ";
            if (checkMachnho) {
                machnho += "MÁCH NHỎ . ";
                for (int i = k + 1; i < arinput.size(); i++) {
                    if (arinput.get(i).equals("MÁCH NHỎ")) {
                        k = i;
                        break;
                    } else
                        cacdung += arinput.get(i) + " ";
                }
                for (int i = k + 1; i < arinput.size(); i++) {

                    machnho += arinput.get(i) + " ";

                }
            } else {
                for (int i = k + 1; i < arinput.size(); i++)
                    cacdung += arinput.get(i) + " ";
            }
        } else {
            if (!checkCachdung && checkMachnho) {
                machnho += "MÁCH NHỎ . ";
                for (int i = k + 1; i < arinput.size(); i++) {

                    machnho += arinput.get(i) + " ";

                }
            }
        }
        if (soche.length() > 0)
            aroutput.add(soche);
        if (thuchien.length() > 0)
            aroutput.add(thuchien);
        if (cacdung.length() > 0)
            aroutput.add(cacdung);
        if (machnho.length() > 0)
            aroutput.add(machnho);
        return aroutput;
    }
}
