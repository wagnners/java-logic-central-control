/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.udesc.simuladormemoriacentrallogica.server;

import br.udesc.simuladormemoriacentrallogica.model.Mensagem;
import br.udesc.simuladormemoriacentrallogica.model.MensagemInvalida;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ricardo Augusto Küstner
 */
public class ServerListener  implements Runnable {

    private Server             server;

    private ObjectOutputStream output;
    private ObjectInputStream  input;


    public ServerListener(Server server) {
        try {
            this.server = server;

            this.output = new ObjectOutputStream(server.getSocket().getOutputStream());
            this.input  = new ObjectInputStream(server.getSocket().getInputStream());
        } catch (IOException ex) {
            try {
                server.getSocket().close();
            } catch (IOException ex1) {
                Logger.getLogger(ServerListener.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }

    }

    @Override
    public void run() {
        Mensagem mensagem = null;
        try {
            System.out.println("Aguardando mensagem!");
            while ((mensagem = new Mensagem((Short[]) input.readObject())) != null) {
                System.out.println("Deu boa pra ler!");
                server.send(mensagem, output);
            }
        } catch (MensagemInvalida ex) {
            System.out.println("Inválido!");
//            mensagem.setSituacao();
            server.send(mensagem, output);
        } catch (SocketException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Deu ruim pra ler!");
            Logger.getLogger(ServerListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
