package br.edu.ifpb.gugawag.so.sockets;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.net.*;
import java.util.*;

import java.io.*;
import java.net.*;
import java.util.*;

import java.io.*;
import java.net.*;
import java.util.*;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private static List<String> arquivos = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        System.out.println("== Servidor de Arquivos ==");

        ServerSocket serverSocket = new ServerSocket(7001);
        Socket socket = serverSocket.accept();

        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        DataInputStream dis = new DataInputStream(socket.getInputStream());

        while (true) {
            String comando = dis.readUTF();
            String[] partes = comando.split(" ");

            String resposta = "";

            if (partes.length > 0) {
                String operacao = partes[0];

                switch (operacao) {
                    case "readdir":
                        resposta = readdir();
                        break;
                    case "rename":
                        if (partes.length == 3) {
                            String nomeAntigo = partes[1];
                            String nomeNovo = partes[2];
                            resposta = rename(nomeAntigo, nomeNovo);
                        } else {
                            resposta = "Comando 'rename' deve ter 2 argumentos: nome_antigo nome_novo";
                        }
                        break;
                    case "create":
                        if (partes.length == 2) {
                            String nomeArquivo = partes[1];
                            resposta = create(nomeArquivo);
                        } else {
                            resposta = "Comando 'create' deve ter 1 argumento: nome_arquivo";
                        }
                        break;
                    case "remove":
                        if (partes.length == 2) {
                            String nomeArquivo = partes[1];
                            resposta = remove(nomeArquivo);
                        } else {
                            resposta = "Comando 'remove' deve ter 1 argumento: nome_arquivo";
                        }
                        break;
                    default:
                        resposta = "Comando desconhecido: " + operacao;
                        break;
                }
            }

            dos.writeUTF(resposta);
        }
    }

    private static String readdir() {
        return String.join(", ", arquivos);
    }

    private static String rename(String nomeAntigo, String nomeNovo) {
        if (arquivos.contains(nomeAntigo)) {
            arquivos.remove(nomeAntigo);
            arquivos.add(nomeNovo);
            return "Arquivo renomeado com sucesso.";
        } else {
            return "Arquivo não encontrado.";
        }
    }

    private static String create(String nomeArquivo) {
        if (!arquivos.contains(nomeArquivo)) {
            arquivos.add(nomeArquivo);
            return "Arquivo criado com sucesso.";
        } else {
            return "Arquivo já existe.";
        }
    }

    private static String remove(String nomeArquivo) {
        if (arquivos.contains(nomeArquivo)) {
            arquivos.remove(nomeArquivo);
            return "Arquivo removido com sucesso.";
        } else {
            return "Arquivo não encontrado.";
        }
    }
}
