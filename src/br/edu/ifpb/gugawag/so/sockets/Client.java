package br.edu.ifpb.gugawag.so.sockets;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class NfsClient {
    public static void main(String[] args) throws IOException {
        System.out.println("== Cliente NFS ==");

        Socket socket = new Socket("127.0.0.1", 7001);
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Comando (readdir/rename/create/remove): ");
            String command = scanner.nextLine();
            out.writeUTF(command);

            switch (command) {
                case "readdir":
                    String fileList = in.readUTF();
                    System.out.println("Lista de arquivos: " + fileList);
                    break;
                case "rename":
                case "create":
                case "remove":
                    System.out.print("Nome do arquivo: ");
                    String fileName = scanner.nextLine();
                    out.writeUTF(fileName);
                    String response = in.readUTF();
                    System.out.println(response);
                    break;
                default:
                    System.out.println("Comando inválido");
            }
        }
    }
}
