package com.ubirayev;

import java.awt.event.WindowEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import static java.lang.Math.*;
import static java.lang.Math.sqrt;

public class App {

  private static JFrame frame = new JFrame();

  private final Integer rDefault = 6570;
  private final Integer RDefault = 42164;
  private final Integer raDefault = 50000;
  private final Integer i1Default = 2;
  private final Integer i2Default = 2;
  private final Integer I = 51;
  private final Double I_IN_RADIAN = Math.toRadians(I);

  private JPanel mainPanel;
  private JTextField textFieldr;
  private JTextField textFieldR;
  private JButton computeButton;
  private JTextField textFieldra;
  private JTextField textFieldi1;
  private JTextField textFieldi2;

  private App() {
    textFieldr.setText(String.valueOf(rDefault));
    textFieldR.setText(String.valueOf(RDefault));
    textFieldra.setText(String.valueOf(raDefault));
    textFieldi1.setText(String.valueOf(i1Default));
    textFieldi2.setText(String.valueOf(i2Default));

    computeButton.addActionListener(e -> {
      try {
        compute();
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    });
  }

  public static void main(String[] args) {
    frame.setContentPane(new App().mainPanel);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);
  }

  private void compute() throws IOException {
    double k = 398603;
    double r = Double.parseDouble(textFieldr.getText());
    double R = Double.parseDouble(textFieldR.getText());
    double ra = Double.parseDouble(textFieldra.getText());

    computeTwoImpulses(k, r, R);
    computeThreeImpulses(k, r, R, ra);
  }

  private void computeTwoImpulses(double k, double r, double R) throws IOException {
    BufferedWriter deltaVFile = new BufferedWriter(new FileWriter("TwoImpulses.xls"));
    deltaVFile.write("\t ΔV1 \t ΔV2 \t ΔV \n");

    twoImpulsesFirst(k, r, R, deltaVFile);
    twoImpulsesSecond(k, r, R, deltaVFile);
    twoImpulsesThird(k, r, R, deltaVFile);

    double time = PI * pow(((R + r) / 2), 3.0 / 2.0) / sqrt(k);
    deltaVFile.write("Time:" + "\t" + time);
    deltaVFile.close();
  }

  private void computeThreeImpulses(double k, double r, double R, double ra) throws IOException {
    BufferedWriter deltaVFile = new BufferedWriter(new FileWriter("ThreeImpulses.xls"));
    deltaVFile.write("\t ΔV1 \t ΔV2 \t ΔV3 \t ΔV \n");

    threeImpulsesFirst(k, r, R, ra, deltaVFile);
    threeImpulsesSecond(k, r, R, ra, deltaVFile);

    double time = PI * pow(((ra + r) / 2), 3.0 / 2.0) / sqrt(k) +
        PI * pow(((ra + R) / 2), 3.0 / 2.0) / sqrt(k);
    deltaVFile.write("Time:" + "\t" + time);
    deltaVFile.close();
  }

  private void twoImpulsesFirst(double k, double r, double R,
      BufferedWriter deltaVFile) throws IOException {
    double deltaV1 = sqrt(k / r) * (sqrt(2.0 * R / (R + r)) - 1.0);
    double deltaV2 = sqrt(k / R + 2.0 * k * r / R / (R + r) - 2.0 * sqrt(k / R) * sqrt(2.0 * k * r / R / (R + r)) * cos(I_IN_RADIAN));
    double deltaVsum = deltaV1 + deltaV2;
    deltaVFile.write("First:" + deltaV1 + "\t" + deltaV2 + "\t" + deltaVsum + "\n");
  }

  private void twoImpulsesSecond(double k, double r, double R, BufferedWriter deltaVFile) throws IOException {
    double deltaV1 = sqrt(k / r + 2 * k * R / r / (R + r) - 2 * sqrt(k / r) * sqrt(2 * k * R / r / (R + r)) * cos(I_IN_RADIAN));
    double deltaV2 = sqrt(k / R) - sqrt(2.0 * k * r / R / (R + r));
    double deltaVsum = deltaV1 + deltaV2;
    deltaVFile.write("Second:" + deltaV1 + "\t" + deltaV2 + "\t" + deltaVsum + "\n");
  }

  private void twoImpulsesThird(double k, double r, double R, BufferedWriter deltaVFile) throws IOException {
    final double I1 = toRadians(2.8071);
      double deltaV1 = sqrt(
          k / r + 2 * k * R / (r * (R + r)) - 2 * sqrt(k / r) * sqrt(2 * k * R / (r * (R + r)))
              * cos(I1));
      double deltaV2 = sqrt(
          k / R + 2 * k * r / (R * (R + r)) - 2 * sqrt(k / R) * sqrt(2 * k * r / (R * (R + r)))
              * cos(I_IN_RADIAN - I1));
      double deltaVsum = deltaV1 + deltaV2;
      deltaVFile.write("Third:" + "\t" + deltaV1 + "\t" + deltaV2 + "\t" + deltaVsum + "\n");
  }

  private void threeImpulsesFirst(double k, double r, double R, double ra,
      BufferedWriter deltaVFile) throws IOException {
    double deltaV1 = sqrt(k / r) * (sqrt(2.0 * ra / (ra + r)) - 1.0);
    double Va1 = sqrt(k / ra) * sqrt((2.0 * r) / (ra + r));
    double Va2 = sqrt(k / ra) * sqrt((2.0 * R) / (ra + R));
    double deltaV2 = sqrt(pow(Va1, 2) + pow(Va2, 2) - 2 * Va1 * Va2 * cos(I_IN_RADIAN));
    double deltaV3 = sqrt(k / R) * (sqrt((2.0 * ra) / (ra + R)) - 1.0);
    double deltaVsum = deltaV1 + deltaV2 + deltaV3;
    deltaVFile.write(
        "First:" + "\t" + deltaV1 + "\t" + deltaV2 + "\t" + deltaV3 + "\t" + deltaVsum + "\n");
  }

  private void threeImpulsesSecond(double k, double r, double R, double ra,
      BufferedWriter deltaVFile) throws IOException {
      double radian1 = Math.toRadians(Double.parseDouble(textFieldi1.getText()));
      double radian2 = Math.toRadians(Double.parseDouble(textFieldi2.getText()));

      double Vn = sqrt(k / r);
      double Vp1 = sqrt(k / r) * sqrt((2.0 * ra) / (ra + r));
      double deltaV1 = sqrt(pow(Vn, 2) + pow(Vp1, 2) - 2 * Vn * Vp1 * cos(radian1));

      double Va1 = sqrt(k / ra) * sqrt((2.0 * r) / (ra + r));
      double Va2 = sqrt(k / ra) * sqrt((2.0 * R) / (ra + R));
      double deltaV2 = sqrt(pow(Va1, 2) + pow(Va2, 2) - 2 * Va1 * Va2 * cos(radian2));

      double Vk = sqrt(k / R);
      double Vp2 = sqrt(k / R) * (sqrt((2 * ra) / (ra + R)) - 1.0);
      double deltaV3 = sqrt(
          pow(Vk, 2) + pow(Vp2, 2) - 2 * Vk * Vp2 * cos(I_IN_RADIAN - radian1 - radian2));
      double deltaVsum = deltaV1 + deltaV2 + deltaV3;
      deltaVFile
          .write("Second:" + "\t" + deltaV1 + "\t" + deltaV2 + "\t" + deltaV3 + "\t" + deltaVsum + "\n");
  }
}
