package br.edu.ifpb.gugawag.so.sockets;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class NfsServer {
    private static List<String> files = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        System.out.println("== Servidor NFS ==");

        // Configurar o socket do servidor
        ServerSocket serverSocket = new ServerSocket(7001);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Cliente conectado: " + clientSocket.getInetAddress());

            // Crie uma thread para lidar com cada cliente
            Thread clientHandler = new Thread(new ClientHandler(clientSocket));
            clientHandler.start();
        }
    }

    static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private DataInputStream in;
        private DataOutputStream out;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                in = new DataInputStream(clientSocket.getInputStream());
                out = new DataOutputStream(clientSocket.getOutputStream());

                while (true) {
                    String request = in.readUTF();
                    String[] parts = request.split(" ");

                    String command = parts[0];
                    switch (command) {
                        case "readdir":
                            listFiles();
                            break;
                        case "rename":
                            renameFile(parts[1], parts[2]);
                            break;
                        case "create":
                            createFile(parts[1]);
                            break;
                        case "remove":
                            removeFile(parts[1]);
                            break;
                        default:
                            out.writeUTF("Comando inválido");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void listFiles() throws IOException {
            out.writeUTF(String.join(",", files));
        }

        private void renameFile(String oldName, String newName) throws IOException {
            if (files.contains(oldName)) {
                files.remove(oldName);
                files.add(newName);
                out.writeUTF("Arquivo renomeado com sucesso.");
            } else {
                out.writeUTF("Arquivo não encontrado.");
            }
        }

        private void createFile(String fileName) throws IOException {
            if (!files.contains(fileName)) {
                files.add(fileName);
                out.writeUTF("Arquivo criado com sucesso.");
            } else {
                out.writeUTF("Arquivo já existe.");
            }
        }

        private void removeFile(String fileName) throws IOException {
            if (files.contains(fileName)) {
                files.remove(fileName);
                out.writeUTF("Arquivo removido com sucesso.");
            } else {
                out.writeUTF("Arquivo não encontrado.");
            }
        }
    }
}
