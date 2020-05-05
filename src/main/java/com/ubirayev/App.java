package com.ubirayev;

import java.awt.event.WindowEvent;

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

    BufferedWriter deltaVFile = new BufferedWriter(new FileWriter("ThreeImpulses.xls"));
    deltaVFile.write("\t ΔV1 \t ΔV2 \t ΔV3 \t ΔV \n");

    threeImpulsesFirst(k, r, R, ra, deltaVFile);
    threeImpulsesSecond(k, r, R, ra, deltaVFile);
    deltaVFile.close();
  }

  private void threeImpulsesFirst(double k, double r, double R, double ra,
      BufferedWriter deltaVFile) throws IOException {
    double radian = Math.toRadians(I);
    double deltaV1 = sqrt(k / r) * (sqrt(2.0 * ra / (ra + r)) - 1.0);
    double Va1 = sqrt(k / ra) * sqrt((2.0 * r) / (ra + r));
    double Va2 = sqrt(k / ra) * sqrt((2.0 * R) / (ra + R));
    double deltaV2 = sqrt(pow(Va1, 2) + pow(Va2, 2) - 2 * Va1 * Va2 * cos(radian));
    double deltaV3 = sqrt(k / R) * (sqrt((2.0 * ra) / (ra + R)) - 1.0);
    double deltaVsum = deltaV1 + deltaV2 + deltaV3;
    deltaVFile.write(
        "First:" + "\t" + deltaV1 + "\t" + deltaV2 + "\t" + deltaV3 + "\t" + deltaVsum + "\n");
  }

  private void threeImpulsesSecond(double k, double r, double R, double ra,
      BufferedWriter deltaVFile) throws IOException {
      double radian = Math.toRadians(I);
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
          pow(Vk, 2) + pow(Vp2, 2) - 2 * Vk * Vp2 * cos(radian - radian1 - radian2));
      double deltaVsum = deltaV1 + deltaV2 + deltaV3;
      deltaVFile
          .write("Second:" + "\t" + deltaV1 + "\t" + deltaV2 + "\t" + deltaV3 + "\t" + deltaVsum + "\n");
  }
}
