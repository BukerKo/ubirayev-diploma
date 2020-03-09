package com.ubirayev;

import org.jfree.data.xy.XYDataItem;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.*;
import static java.lang.Math.sqrt;

public class App {
    
    private final Integer rDefault = 6570;
    private final Integer RDefault = 42164;
    
    private JPanel mainPanel;
    private JTextField textFieldr;
    private JTextField textFieldR;
    private JButton computeButton;
    
    private App() {
        textFieldr.setText(String.valueOf(rDefault));
        textFieldR.setText(String.valueOf(RDefault));
        
        computeButton.addActionListener(e -> {
            try {
                compute();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }
    
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setContentPane(new App().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    
    private void compute() throws IOException {
        double k = 398603;
        double r = Double.parseDouble(textFieldr.getText());
        double R = Double.parseDouble(textFieldR.getText());
    
        first(k, r, R);
        second(k, r, R);
    }
    
    private static void first(double k, double r, double R) throws IOException {
        BufferedWriter deltaViFile = new BufferedWriter(new FileWriter("FirstDeltaV(i).xls"));
        BufferedWriter deltaVrFile = new BufferedWriter(new FileWriter("FirstDeltaV(r).xls"));
        deltaViFile.write("i \t deltaV1 \t deltaV2 \t deltaVsum\n");
        deltaVrFile.write("r \t deltaV1 \t deltaV2 \t deltaVsum\n");
        
        Map<String, List<XYDataItem>> series = new HashMap<>();
        List<XYDataItem> deltaV1List = new ArrayList<>();
        List<XYDataItem> deltaV2List = new ArrayList<>();
        List<XYDataItem> deltaVsumList = new ArrayList<>();
        for (double i = 1.0; i <= 51.0; i++)
        {
            double radian = Math.toRadians(i);
            double deltaV1 = sqrt(k / r) * (sqrt(2.0 * R / (R + r)) - 1.0);
            double deltaV2 = sqrt(k / R + 2.0 * k * r / R / (R + r) - 2.0 * sqrt(k / R) * sqrt(2.0 * k * r / R / (R + r)) * cos(radian));
            double deltaVsum = deltaV1 + deltaV2;
            deltaV1List.add(new XYDataItem(radian, deltaV1));
            deltaV2List.add(new XYDataItem(radian, deltaV2));
            deltaVsumList.add(new XYDataItem(radian, deltaVsum));
            deltaViFile.write(i + "\t" + deltaV1 + "\t" + deltaV2 + "\t" + deltaVsum + "\n");
        }
        series.put("deltaV1", deltaV1List);
        series.put("deltaV2", deltaV2List);
        series.put("deltaVsum", deltaVsumList);
        new PlotFrame(series, "First", "i", "V(i)");
        
        
        series = new HashMap<>();
        deltaV1List = new ArrayList<>();
        deltaV2List = new ArrayList<>();
        deltaVsumList = new ArrayList<>();
        double radian = Math.toRadians(51.0);
        for (double rwave = 1.0; rwave <= 26; rwave++)
        {
            R = r * rwave;
            double deltaV1 = sqrt(k / r) * (sqrt(2.0 * R / (R + r)) - 1.0);
            double deltaV2 = sqrt(k / R + 2.0 * k * r / R / (R + r) - 2.0 * sqrt(k / R) * sqrt(2.0 * k * r / R / (R + r)) * cos(radian));
            double deltaVsum = deltaV1 + deltaV2;
            deltaV1List.add(new XYDataItem(rwave, deltaV1));
            deltaV2List.add(new XYDataItem(rwave, deltaV2));
            deltaVsumList.add(new XYDataItem(rwave, deltaVsum));
            deltaVrFile.write(rwave + "\t" + deltaV1 + "\t" + deltaV2 + "\t" + deltaVsum + "\n");
        }
    
        series.put("deltaV1", deltaV1List);
        series.put("deltaV2", deltaV2List);
        series.put("deltaVsum", deltaVsumList);
        new PlotFrame(series, "First", "r", "V(r)");
        
        deltaViFile.close();
        deltaVrFile.close();
    }
    
    static void second(double k, double r, double R) throws IOException {
        BufferedWriter deltaViFile = new BufferedWriter(new FileWriter("SecondDeltaV(i).xls"));
        BufferedWriter deltaVrFile = new BufferedWriter(new FileWriter("SecondDeltaV(r).xls"));
        deltaViFile.write("i \t deltaV1 \t deltaV2 \t deltaVsum\n");
        deltaVrFile.write("r \t deltaV1 \t deltaV2 \t deltaVsum\n");
    
        Map<String, List<XYDataItem>> series = new HashMap<>();
        List<XYDataItem> deltaV1List = new ArrayList<>();
        List<XYDataItem> deltaV2List = new ArrayList<>();
        List<XYDataItem> deltaVsumList = new ArrayList<>();
        for (double i = 1.0; i <= 51.0; i++)
        {
            double radian = Math.toRadians(i);
            double deltaV1 = sqrt(k / r + 2 * k * R / r / (R + r) - 2 * sqrt(k / r) * sqrt(2 * k * R / r / (R + r)) * cos(radian));
            double deltaV2 = sqrt(k / R) - sqrt(2.0 * k * r / R / (R + r));
            double deltaVsum = deltaV1 + deltaV2;
            deltaV1List.add(new XYDataItem(radian, deltaV1));
            deltaV2List.add(new XYDataItem(radian, deltaV2));
            deltaVsumList.add(new XYDataItem(radian, deltaVsum));
            deltaViFile.write(i + "\t" + deltaV1 + "\t" + deltaV2 + "\t" + deltaVsum + "\n");
        }
        series.put("deltaV1", deltaV1List);
        series.put("deltaV2", deltaV2List);
        series.put("deltaVsum", deltaVsumList);
        new PlotFrame(series, "Second", "i", "V(i)");
    
    
        series = new HashMap<>();
        deltaV1List = new ArrayList<>();
        deltaV2List = new ArrayList<>();
        deltaVsumList = new ArrayList<>();
        double radian = Math.toRadians(51.0);
        for (double rwave = 1.0; rwave <= 26; rwave++)
        {
            R = r * rwave;
            double deltaV1 = sqrt(k / r + 2 * k * R / r / (R + r) - 2 * sqrt(k / r) * sqrt(2 * k * R / r / (R + r)) * cos(radian));
            double deltaV2 = sqrt(k / R) - sqrt(2.0 * k * r / R / (R + r));
            double deltaVsum = deltaV1 + deltaV2;
            deltaV1List.add(new XYDataItem(rwave, deltaV1));
            deltaV2List.add(new XYDataItem(rwave, deltaV2));
            deltaVsumList.add(new XYDataItem(rwave, deltaVsum));
            deltaVrFile.write(rwave + "\t" + deltaV1 + "\t" + deltaV2 + "\t" + deltaVsum + "\n");
        }
    
        series.put("deltaV1", deltaV1List);
        series.put("deltaV2", deltaV2List);
        series.put("deltaVsum", deltaVsumList);
        new PlotFrame(series, "Second", "r", "V(r)");
        
        deltaViFile.close();
        deltaVrFile.close();
    }
}
