/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpiarduinoserialjssc;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;

public class RPiArduinoSerialjSSC {

    static SerialPort serialPort = new SerialPort("/dev/ttyACM0");

    public static void main(String[] args) {
        String[] portNames = SerialPortList.getPortNames();
        try {
            for (String portName : portNames) {
                System.out.println("Port:" + portName);
            }
            serialPort.openPort();
            // Wait for the arduino to initialize
            Thread.sleep(3000);
            serialPort.setParams(SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            serialPort.addEventListener(new SerialPortReader(), SerialPort.MASK_RXCHAR);
            System.out.println("\"Hello World!!!\" successfully writen to port: " + serialPort.writeBytes("y".getBytes()));
            Thread.sleep(3000);
            System.out.println("\"Hello World 2!!!\" successfully writen to port: " + serialPort.writeBytes("y".getBytes()));
            //System.out.println("Port closed: " + serialPort.closePort());
        } catch (SerialPortException | InterruptedException ex) {
            System.out.println(ex);
        }
    }

    static class SerialPortReader implements SerialPortEventListener {

        StringBuilder message = new StringBuilder();

        @Override
        public void serialEvent(SerialPortEvent event) {
            if (event.isRXCHAR() && event.getEventValue() > 0) {
                try {
                    byte buffer[] = serialPort.readBytes();
                    for (byte b : buffer) {
                        if ((b == '\r' || b == '\n') && message.length() > 0) {
                            String toProcess = message.toString();
                            System.out.println("Message received" +toProcess);
                            message.setLength(0);
                        } else {
                            message.append((char) b);
                        }
                    }
                } catch (SerialPortException ex) {
                    System.out.println(ex);
                    System.out.println("serialEvent");
                }
            }
        }
    }

}
