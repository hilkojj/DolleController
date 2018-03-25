package dollecontroller;

import com.fazecast.jSerialComm.SerialPort;

import java.util.Scanner;

public class InputProcessor implements Runnable {

	public String statusInfo = "het is vandaag geen kerst hoogstwaarschijnlijk";

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

				statusInfo = "Verbinden...";
				port = ports[0];
				port.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
				scanner = new Scanner(port.getInputStream());
				statusInfo = "Dolle controlller pro edition 2019+ succesfol aangesloten!";
				return;

			}

			statusInfo = ports.length == 0 ?
					"Soeken naar controler..."
					:
					"Kan poort nit openen! " +
					"Heb je dit vaker geopend of is ardino Serial Monitor nog aan?";

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void run() {

		while (Main.running) {

			if (port == null || !port.isOpen())
				findPort();

			if (scanner.hasNextLine())
				processLine(scanner.nextLine());

			for (Actuator actuator : Main.ACTUATORS)
				actuator.update();

		}

	}

	private void processLine(String line) {
		if (line.startsWith("I.")) {

			InputValue i = findInput(line);
			if (i == null) {
				System.err.println("Unrecognized input: " + line);
				return;
			}

			try {
				i.value = Float.parseFloat(line.split("=")[1]);
			} catch (Exception ignored) {}

		}
	}

	private InputValue findInput(String line) {

		for (InputValue i : InputValue.values())
			if (line.contains(i.name()))
				return i;

		return null;
	}

}
