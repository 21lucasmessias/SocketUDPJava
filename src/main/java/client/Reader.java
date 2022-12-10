package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Reader implements Runnable {

    private final BufferedReader reader;

    public Reader(Socket socket) throws IOException {
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (reader.ready()) {
                    final String message = reader.readLine();
                    System.out.println(message);

                    if (message.equals("end")) {
                        reader.close();
                        break;
                    }
                }

                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
