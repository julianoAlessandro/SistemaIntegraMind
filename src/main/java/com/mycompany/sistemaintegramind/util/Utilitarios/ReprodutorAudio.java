
package com.mycompany.sistemaintegramind.util.Utilitarios;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class ReprodutorAudio {

    private SourceDataLine linha;
    private Thread threadReproducao;

    private volatile boolean reproduzindo;
    private volatile boolean pausado;
    private volatile boolean pararSolicitado;

    private volatile IOException erroReproducao;

    // Inicia a reprodução do áudio
    public synchronized void reproduzir(File arquivo) throws Exception {

        if (arquivo == null || !arquivo.isFile()) {
            throw new IOException("Arquivo de áudio não encontrado.");
        }

        if (threadReproducao != null && threadReproducao.isAlive()) {
            throw new IllegalStateException(
                    "Já existe uma reprodução em andamento."
            );
        }

        AudioInputStream audio = AudioSystem.getAudioInputStream(arquivo);

        try {

            javax.sound.sampled.AudioFormat formato = audio.getFormat();

            DataLine.Info informacao = new DataLine.Info(
                    SourceDataLine.class,
                    formato
            );

            if (!AudioSystem.isLineSupported(informacao)) {
                throw new IOException(
                        "O dispositivo de saída não suporta o formato deste áudio."
                );
            }

            SourceDataLine novaLinha =
                    (SourceDataLine) AudioSystem.getLine(informacao);

            novaLinha.open(formato);
            novaLinha.start();

            linha = novaLinha;
            pararSolicitado = false;
            pausado = false;
            reproduzindo = true;
            erroReproducao = null;

            threadReproducao = new Thread(() -> {

                executarReproducao(audio, novaLinha);

            }, "ReprodutorAudioIntegraMIND");

            threadReproducao.start();

        } catch (Exception e) {

            try {
                audio.close();
            } catch (IOException ex) {
                e.addSuppressed(ex);
            }

            throw e;
        }
    }

    // Executa a reprodução em segundo plano
    private void executarReproducao(
            AudioInputStream audio,
            SourceDataLine linhaAtual) {

        byte[] buffer = new byte[8192];

        try (AudioInputStream stream = audio) {

            int bytesLidos;

            while (!pararSolicitado
                    && (bytesLidos = stream.read(buffer)) != -1) {

                int deslocamento = 0;

                while (deslocamento < bytesLidos && !pararSolicitado) {

                    // Aguarda enquanto o áudio estiver pausado
                    while (pausado && !pararSolicitado) {
                        Thread.sleep(50);
                    }

                    if (pararSolicitado) {
                        break;
                    }

                    deslocamento += linhaAtual.write(
                            buffer,
                            deslocamento,
                            bytesLidos - deslocamento
                    );
                }
            }

            // Aguarda a saída do áudio terminar normalmente
            if (!pararSolicitado) {
                linhaAtual.drain();
            }

        } catch (Exception e) {

            if (!pararSolicitado) {

                erroReproducao = new IOException(
                        "Erro durante a reprodução do áudio.",
                        e
                );
            }

        } finally {

            linhaAtual.stop();
            linhaAtual.close();

            synchronized (this) {

                if (linha == linhaAtual) {
                    linha = null;
                }

                if (threadReproducao == Thread.currentThread()) {
                    threadReproducao = null;
                }

                reproduzindo = false;
                pausado = false;
            }
        }
    }

    // Pausa a reprodução
    public synchronized void pausar() {

        if (reproduzindo && !pausado && linha != null) {

            pausado = true;
            linha.stop();
        }
    }

    // Continua a reprodução
    public synchronized void continuar() {

        if (reproduzindo && pausado && linha != null) {

            pausado = false;
            linha.start();
        }
    }

    // Interrompe completamente a reprodução
    public void parar() {

        Thread threadAtual;

        synchronized (this) {

            if (threadReproducao == null) {
                return;
            }

            pararSolicitado = true;
            pausado = false;

            threadAtual = threadReproducao;

            if (linha != null) {

                linha.stop();
                linha.flush();
                linha.close();
            }
        }

        if (threadAtual != null) {

            threadAtual.interrupt();

            try {

                threadAtual.join(2000);

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();
            }
        }
    }

    // Verifica se está reproduzindo
    public boolean isReproduzindo() {
        return reproduzindo;
    }

    // Verifica se está pausado
    public boolean isPausado() {
        return pausado;
    }

    // Retorna eventual erro de reprodução
    public IOException getErroReproducao() {
        return erroReproducao;
    }
}

