package com.mycompany.exemplocompiladortiny;

import com.mycompany.tiny.compiler.*;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class ExemploCompiladorTiny {

    public static void main(String[] args) throws IOException {
        Path arquivo = Path.of(args.length > 0 ? args[0] : "programa.tny");
        String entrada = Files.readString(arquivo, StandardCharsets.UTF_8);

        System.out.println("Arquivo analisado: " + arquivo);

        SimpleCharStream stream = new SimpleCharStream(new StringReader(entrada));
        TinyTokenManager lexer = new TinyTokenManager(stream);
        Token t;

        try {
            System.out.println("\n--- Resultado da Análise Léxica ---");
            do {
                t = lexer.getNextToken();
                if (t.kind != TinyConstants.EOF) {
                    System.out.println("Lido: '" + t.image + "' -> Reconhecido como: " + TinyConstants.tokenImage[t.kind]);
                }
            } while (t.kind != TinyConstants.EOF);

            System.out.println("\nExpressão Aceita! (Nenhum erro léxico)");

        } catch(TokenMgrError erro) {
            System.out.println("\nExpressão Não aceita!: " + erro.getMessage());
            return;
        }

        try {
            System.out.println("\n--- Resultado da Análise Sintática ---");
            Tiny parser = new Tiny(new StringReader(entrada));
            parser.Programa();
            System.out.println("Programa Aceito! (Nenhum erro sintático)");

        } catch(ParseException | TokenMgrError erro) {
            System.out.println("Programa Não aceito!: " + erro.getMessage());
        }
    }
}
