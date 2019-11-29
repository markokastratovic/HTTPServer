/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marko
 */
public class Server {

    public static void main(String[] args) throws Exception {
        System.out.println("send request through browser using this format");
        System.out.println("http://localhost:8080/?op=+&a=2&b=2?");
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("Waiting...");
        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("Server:Client connected");

            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            byte[] bytes = new byte[1024];
            inputStream.read(bytes);
            System.out.println(new String(bytes));

            Server.respondToRequest(new String(bytes), outputStream);

            inputStream.close();
            socket.close();
        }
    }

    public static String calculate(String operation, String a, String b) {
        double a1 = Double.parseDouble(a);
        double b1 = Double.parseDouble(b);

        if (operation.equals("+")) {
            return String.valueOf(a1 + b1);
        }
        if (operation.equals("-")) {
            return String.valueOf(a1 - b1);
        }
        if (operation.equals("*")) {
            return String.valueOf(a1 * b1);
        }
        if (operation.equals("/")) {
            return String.valueOf(a1 / b1);
        }
        return "DOSLO JE DO GRESKE! PROVERITE UNETE VREDNOSTI";
    }

    public static void respondToRequest(String request, OutputStream os) {

        int i1 = request.indexOf("op=") + 3;
        int i2 = request.indexOf("&");
        System.out.println(i1 + "  " + i2);

        int i3 = request.indexOf("a=") + 2;
        int i4 = request.indexOf("&", i2 + 1);
        System.out.println(i3 + "  " + i4);

        int i5 = request.indexOf("b=") + 2;
        int i6 = request.indexOf("?", i5+1);
        System.out.println(i5 + "  " + i6);

        String op = request.substring(i1, i2);
        String a = request.substring(i3, i4);
        String b = request.substring(i5, i6);

        // String response = "HTTP/1.1 200 OK\nDate: Mon, 27 Jul 2009 12:28:53 GMT\nServer: Apache/2.2.14 (Win32)\nLast-Modified: Wed, 22 Jul 2009 19:15:56 GMT\nContent-Length: 88\nContent-Type: text/html\nConnection: Closed\n\n<!DOCTYPE html> <html> <body> <h1>Hello, World!</h1> </body> </html>";
        String result = a + op + b + "=" + Server.calculate(op, a, b);

        String shortResponse = "HTTP/1.1 200 OK\nDate: Mon, 27 Jul 2009 12:28:53 GMT\nContent-Type: text/html\nConnection: Closed\n\n<!DOCTYPE html> <html> <body> <h1>" + result + "</h1> </body> </html>";
        try {
            System.out.println(shortResponse);
            os.write(shortResponse.getBytes());
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
