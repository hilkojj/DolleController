package com.dollecontroller;

import com.badlogic.gdx.graphics.Color;
import com.fazecast.jSerialComm.SerialPort;

import java.util.Scanner;

public class InputProcessor implements Runnable {

	public enum Status {

		SEARCHING("Zoeken naar controller...", new Color(1, .2f, .2f, 1)),
		CONNECTING("Verbinden...", new Color(.2f, 10, .5f, 1)),
		CONNECTED("Dolle controlller pro edition 2019+ succesfol aangesloten!", new Color(.3f, .4f, 1, 1)),
		FAILED("Kan poort nit openen!", new Color(1, .6f, .2f, 1));

		public String description;
		public Color color;

		Status(String description, Color color) {
			this.description = description;
			this.color = color;
		}

	}

	public Status status = Status.SEARCHING;

	private SerialPort port;
	private Scanner scanner;

	InputProcessor() {
		new Thread(this).start();
	}

	private void findPort() {

		port = null;

		while (port == null) {

			SerialPort ports[] = SerialPort.getCommPorts();

			if (ports.length > 0 && ports[0].openPort()) {

				status = Status.CONNECTING;
				port = ports[0];
				port.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
				scanner = new Scanner(port.getInputStream());
				status = Status.CONNECTED;
				return;

			}

			status = ports.length == 0 ? Status.SEARCHING : Status.FAILED;

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void run() {

		while (DolleApp.running) {

			if (port == null || !port.isOpen())
				findPort();

			if (scanner.hasNextLine())
				processLine(scanner.nextLine());

			for (Actuator actuator : DolleApp.actuators)
				actuator.update();

		}

	}

	private void processLine(String line) {
		if (line.startsWith("I.")) {

			Input i = findInput(line);
			if (i == null) {
				System.err.println("Unrecognized input: " + line);
				return;
			}

			try {
				i.value = line.split("=")[1];
			} catch (Exception ignored) {
			}

		}
	}

	private Input findInput(String line) {

		for (Input i : Input.values())
			if (line.contains(i.name()))
				return i;

		return null;
	}

}
