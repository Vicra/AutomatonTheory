package AutomatonsTheory.AutomatonExtensions.RegularExpressionParserDirectory;

import AutomatonsTheory.AutomatonExtensions.RegularExpressionParserDirectory.tree.Node;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public  class RegularExpressionParser {


    public Node Parse(String regexp) throws Exception {
        // convert String into InputStream
        InputStream is = new ByteArrayInputStream(regexp.getBytes());

        // read it with BufferedReader
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        parser p = new parser(new Lexer(br));
        return (Node)p.parse().value;
    }
}
