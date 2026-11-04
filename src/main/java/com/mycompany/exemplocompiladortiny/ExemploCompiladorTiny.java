package com.mycompany.exemplocompiladortiny;

import com.mycompany.tiny.compiler.*;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ExemploCompiladorTiny {

    public static void main(String[] args) throws IOException {
        List<String> argumentos = new ArrayList<>(List.of(args));
        boolean mostrarTokens = argumentos.remove("--tokens");
        Path arquivo = Path.of(argumentos.isEmpty() ? "programa.tny" : argumentos.get(0));
        String entrada = Files.readString(arquivo, StandardCharsets.UTF_8);

        if (mostrarTokens && !listarTokens(entrada)) {
            return;
        }

        Tiny parser = new Tiny(new StringReader(entrada));
        try {
            parser.Programa();
            System.out.println("Aceito");
            for (String erro : parser.getErros()) {
                System.out.println(erro);
            }
        } catch (ParseException | TokenMgrError erro) {
            System.out.println("Não aceito: " + erro.getMessage());
        }
    }

    /** Lista os tokens e comentários; retorna false se houver erro léxico. */
    private static boolean listarTokens(String entrada) {
        TinyTokenManager lexer = new TinyTokenManager(new SimpleCharStream(new StringReader(entrada)));
        try {
            Token t;
            do {
                t = lexer.getNextToken();
                Token c = t.specialToken;
                while (c != null && c.specialToken != null) {
                    c = c.specialToken;
                }
                for (; c != null; c = c.next) {
                    System.out.println("Comentário: '" + c.image + "' -> Ignorado");
                }
                if (t.kind != TinyConstants.EOF) {
                    System.out.println("Lido: '" + t.image + "' -> Reconhecido como: " + TinyConstants.tokenImage[t.kind]);
                }
            } while (t.kind != TinyConstants.EOF);
            return true;
        } catch (TokenMgrError erro) {
            System.out.println("Não aceito: " + erro.getMessage());
            return false;
        }
    }
}
