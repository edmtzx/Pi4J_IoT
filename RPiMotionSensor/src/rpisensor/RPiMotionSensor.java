/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpisensor;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class RPiMotionSensor {

    public static void main(String[] args) {
        System.out.println("Starting Pi4J Motion Sensor Example");

        // create gpio controller           
        final GpioController gpioSensor = GpioFactory.getInstance();
        final GpioPinDigitalInput sensor = gpioSensor.provisionDigitalInputPin(RaspiPin.GPIO_04, PinPullResistance.PULL_DOWN);

        // create another gpio controller                           
        final GpioController gpioLED = GpioFactory.getInstance();
        final GpioPinDigitalOutput led = gpioLED.provisionDigitalOutputPin(RaspiPin.GPIO_05, "LED", PinState.HIGH);

        led.high();

        // create and register gpio pin listener            
        sensor.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {

                if (event.getState().isHigh()) {
                    System.out.println("Motion Detected!");
                    led.toggle();
                }

                if (event.getState().isLow()) {
                    System.out.println("All is quiet...");
                    led.toggle();
                }
            }
        });

        try {
            // keep program running until user aborts       
            for (;;) {
                Thread.sleep(300);
            }
        } catch (final Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
