package br.edu.ifpb.gugawag.so.sockets;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws IOException {
        System.out.println("== Cliente de Arquivos ==");

        Socket socket = new Socket("127.0.0.1", 7001);
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        DataInputStream dis = new DataInputStream(socket.getInputStream());

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nComandos disponíveis: readdir, rename, create, remove");
            System.out.print("Digite um comando: ");
            String command = scanner.nextLine();

            dos.writeUTF(command);

            if (command.equals("readdir")) {
                String resposta = dis.readUTF();
                System.out.println("Lista de arquivos: " + resposta);
            } else {
                String resposta = dis.readUTF();
                System.out.println("Resposta do servidor: " + resposta);
            }
        }
    }
}
