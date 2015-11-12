package com.infinity.data;

import android.util.Log;

import com.infinity.model.MaterialItem;
import com.infinity.model.OutputItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    public static String convertTime(int h, int m) {
        String rs = "";
        if (h != 0) {
            rs = rs + h + " tiếng";
            if (m != 0) {
                rs = rs + " " + m + " phút";
            }
        } else {
            if (m != 0) {
                rs = rs + " " + m + " phút";
            }
        }
        return rs;
    }

    public static OutputItem convertMatListToOutput(ArrayList<MaterialItem> materials) {
        String talk = "";
        String mess = "";
        for (MaterialItem material : materials) {
            String unit = "";
            if (material.getUnit().equals("g")) {
                unit = "gam";
            } else if (material.getUnit().equals("ml")) {
                unit = "mi li lít";
            } else {
                unit = material.getUnit();
            }
            if (!material.getAmount().equals("0")) {
                mess = mess + material.getAmount() + material.getUnit() + " " + material.getName() + ", ";
                talk = talk + material.getAmount() + " " + unit + " " + material.getName() + ", ";
            } else {
                mess = mess + material.getName() + ", ";
                talk = talk + material.getName() + ", ";
            }
        }
        Log.d("TienDH", "Convert - talk: " + talk + " mess: " + mess);
        return new OutputItem(talk, mess);
    }

    public static String formatDate(String date, String initDateFormat, String endDateFormat) throws ParseException {

        Date initDate = new SimpleDateFormat(initDateFormat).parse(date);
        SimpleDateFormat formatter = new SimpleDateFormat(endDateFormat);
        String parsedDate = formatter.format(initDate);
        return parsedDate;
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
