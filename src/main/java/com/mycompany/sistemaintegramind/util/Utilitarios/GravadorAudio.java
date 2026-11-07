package com.mycompany.sistemaintegramind.util.Utilitarios;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Line;

public class GravadorAudio {

    private TargetDataLine linha;
    private Thread threadGravacao;
    private File arquivoAudioTemporario;
    private volatile IOException erroGravacao;

    // Inicia a gravação
    public synchronized void iniciarGravacao() throws Exception {

        if (isGravando()) {
            throw new IllegalStateException(
                    "Já existe uma gravação em andamento."
            );
        }

        if (threadGravacao != null) {
            throw new IllegalStateException(
                    "Finalize ou cancele a gravação anterior."
            );
        }

        if (arquivoAudioTemporario != null
                && arquivoAudioTemporario.exists()) {

            throw new IllegalStateException(
                    "Já existe um áudio aguardando salvamento ou cancelamento."
            );
        }

        AudioFormat formatoAudio = new AudioFormat(
                44100.0F,
                16,
                2,
                true,
                false
        );

        DataLine.Info informacao = new DataLine.Info(
                TargetDataLine.class,
                formatoAudio
        );

        TargetDataLine novaLinha = (TargetDataLine) AudioSystem.getLine(informacao);

        novaLinha.open(formatoAudio);

        File novoArquivo = null;

        try {

            // Cria arquivo temporário no Windows
            novoArquivo = File.createTempFile(
                    "integramind_audio_",
                    ".wav"
            );

            novoArquivo.deleteOnExit();

            arquivoAudioTemporario = novoArquivo;
            erroGravacao = null;
            linha = novaLinha;

            // Inicia a captura do microfone
            novaLinha.start();

            TargetDataLine linhaAtual = novaLinha;
            File arquivoAtual = novoArquivo;

            // Thread responsável por gravar o áudio
            threadGravacao = new Thread(() -> {

                try (AudioInputStream audioInputStream
                        = new AudioInputStream(linhaAtual)) {

                    AudioSystem.write(
                            audioInputStream,
                            AudioFileFormat.Type.WAVE,
                            arquivoAtual
                    );

                } catch (IOException e) {
                    erroGravacao = e;
                }

            }, "GravacaoAudioIntegraMIND");

            threadGravacao.start();

        } catch (Exception e) {

            novaLinha.close();
            linha = null;
            threadGravacao = null;
            arquivoAudioTemporario = null;

            if (novoArquivo != null) {
                novoArquivo.delete();
            }

            throw e;
        }
    }

    // Interrompe e finaliza a gravação
    public synchronized File pararGravacao() throws IOException {

        if (!isGravando()) {
            throw new IllegalStateException(
                    "Não existe gravação em andamento."
            );
        }

        TargetDataLine linhaAtual = linha;
        Thread threadAtual = threadGravacao;

        // Interrompe a captura do microfone
        linhaAtual.stop();
        linhaAtual.close();

        try {

            // Aguarda a finalização do arquivo WAV
            if (threadAtual != null) {
                threadAtual.join();
            }

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            throw new IOException(
                    "Erro ao finalizar a gravação.",
                    e
            );
        }

        linha = null;
        threadGravacao = null;

        if (erroGravacao != null) {
            throw erroGravacao;
        }

        if (arquivoAudioTemporario == null
                || !arquivoAudioTemporario.exists()
                || arquivoAudioTemporario.length() <= 44) {

            throw new IOException(
                    "O arquivo de áudio não foi gerado corretamente."
            );
        }

        return arquivoAudioTemporario;
    }

    // Cancela a gravação e exclui o arquivo temporário
    public synchronized void cancelarAudio() throws IOException {

        TargetDataLine linhaAtual = linha;
        Thread threadAtual = threadGravacao;

        // Interrompe o microfone caso ainda esteja gravando
        if (linhaAtual != null && linhaAtual.isOpen()) {

            linhaAtual.stop();
            linhaAtual.close();
        }

        try {

            // Aguarda a finalização da thread
            if (threadAtual != null) {
                threadAtual.join();
            }

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            throw new IOException(
                    "Erro ao cancelar a gravação.",
                    e
            );
        }

        linha = null;
        threadGravacao = null;
        erroGravacao = null;

        // Exclui o arquivo temporário
        if (arquivoAudioTemporario != null
                && arquivoAudioTemporario.exists()) {

            if (!arquivoAudioTemporario.delete()) {

                arquivoAudioTemporario.deleteOnExit();

                throw new IOException(
                        "Não foi possível excluir o áudio temporário."
                );
            }
        }

        arquivoAudioTemporario = null;
    }

    // Verifica se existe uma gravação em andamento
    public synchronized boolean isGravando() {

        return linha != null
                && linha.isOpen()
                && linha.isRunning();
    }

    // Retorna o arquivo temporário
    public synchronized File getArquivoAudioTemporario() {

        return arquivoAudioTemporario;
    }

    // Libera a referência depois que o áudio foi salvo definitivamente
    public synchronized void liberarAudioSalvo() {

        if (isGravando() || threadGravacao != null) {
            throw new IllegalStateException(
                    "Finalize a gravação antes de liberar o áudio."
            );
        }

        arquivoAudioTemporario = null;
        erroGravacao = null;
    }


public static void listarDispositivosAudio() {

    Mixer.Info[] dispositivos = AudioSystem.getMixerInfo();

    System.out.println("\n========== DIAGNÓSTICO DE ÁUDIO ==========");

    System.out.println("Java: " + System.getProperty("java.version"));
    System.out.println("Sistema: " + System.getProperty("os.name"));

    System.out.println("Mixers encontrados: " + dispositivos.length);

    for (Mixer.Info dispositivo : dispositivos) {

        Mixer mixer = AudioSystem.getMixer(dispositivo);

        System.out.println("\nDispositivo: " + dispositivo.getName());
        System.out.println("Descrição: " + dispositivo.getDescription());

        Line.Info[] entradas = mixer.getTargetLineInfo();

        System.out.println("Entradas disponíveis: " + entradas.length);

        for (Line.Info entrada : entradas) {

            System.out.println("Linha: " + entrada);

            if (entrada instanceof DataLine.Info) {

                DataLine.Info informacao = (DataLine.Info) entrada;

                for (AudioFormat formato : informacao.getFormats()) {

                    System.out.println("Formato: " + formato);
                }
            }
        }
    }

    System.out.println("\n========== FIM DO DIAGNÓSTICO ==========\n");
}


}
