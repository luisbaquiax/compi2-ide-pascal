#! /bin/bashecho "STARTING JFLEX COMPILING"
echo "STARTING JFLEX COMPILING"
java -jar "jflex-full-1.9.1.jar" -d "..\..\java\com\baquiax\idepascal\backend" "lexer.jflex"

echo "STARTING CUP COMPILING"
java -jar "java-cup-11.jar" -parser Parser "parser.cup"

move Parser.java "..\..\java\com\baquiax\idepascal\backend\Parser.java"

move sym.java "..\..\java\com\baquiax\idepascal\backend\sym.java"
