/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpisensor;

public class RPiTempPressSensor {

    public static void main(String[] args) {

        System.out.println("Starting Pi4J Temperature Pressure Sensor Example");

        try {
            BMP085Device dev = new BMP085Device(false);
            System.out.println("got device!");
            double data[];
            // keep program running until user aborts       
            for (;;) {
                data = dev.getReading();
                System.out.println("temperature=" + data[1] + " pressure=" + (data[0] * 0.01) + " hpa");
                Thread.sleep(700);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
