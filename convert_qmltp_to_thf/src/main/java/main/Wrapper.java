package main;

import exceptions.ParseException;
import fofParser.QmfAstGen;
import org.antlr.v4.runtime.ANTLRInputStream;
import parser.ParseContext;
import util.tree.Node;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;


public class Wrapper {

    private static final Logger log = Logger.getLogger( "default" );

    public static void convertQmfTraverseDirectories(Path inPath, String oPath, boolean dotin, boolean dotout, String dotBin ){
        log.info("traversing directories.");
        if (Files.isDirectory(inPath)){
            log.info("Input is a directory " + inPath.toString());
            log.info("Creating subdirectories.");
            try(Stream<Path> paths = Files.walk(inPath)){

                // create subdirectories
                paths.filter(Files::isDirectory).forEach(d -> {
                    Path newDir = Paths.get(
                            oPath,
                            d.toAbsolutePath().toString().replace(inPath.toAbsolutePath().getParent().toString(), ""));
                    try {
                        Files.createDirectories(newDir);
                        log.info("Created directory " + newDir.toString());
                    } catch (IOException e) {
                        log.warning("Could not create directory " + newDir.toString() + " ::: " + e.getMessage());
                    }
                });

                // embed problems
                try(Stream<Path> pathsNew = Files.walk(inPath)){
                    log.info("Converting problems.");
                    pathsNew.filter(Files::isRegularFile).forEach(f->{
                        String subdir = f.toString().substring(inPath.getParent().toString().length());
                        Path outPath = Paths.get(oPath,subdir);
                        Path inDot = Paths.get(outPath.toString()+".in.dot");
                        Path outDot = Paths.get(outPath.toString()+".out.dot");
                        if (!dotin) inDot = null;
                        if (!dotout) outDot = null;
                        try {
                            boolean success = convertQmfToThf(f,outPath,inDot,outDot,dotBin );
                            if (!success){
                                log.warning("Parse error in problem " + f.toString());
                            }
                        } catch (ParseException e) {
                            log.warning("ParseException: Could not convert " + f.toString() + " ::: " + e.toString() + " ::: " + e.getMessage());
                            //e.printStackTrace();
                            //System.exit(1);
                        } catch (IOException e) {
                            log.warning("Could not convert " + f.toString() + " ::: " + e.toString() + " ::: " + e.getMessage());
                        }
                    });
                    System.exit(0);
                } catch (IOException e){
                    log.severe("Could not traverse directory " + inPath.toString() + " ::: " + e.getMessage());
                    log.severe("Exit.");
                    System.exit(1);
                }
            } catch (IOException e){
                log.severe("Could not traverse directory " + inPath.toString() + " ::: " + e.getMessage());
                log.severe("Exit.");
                System.exit(1);
            }

        }
    }

    public static boolean convertQmfToThf(Path inPath, Path oPath, Path dotin, Path dotout, String dotBin) throws IOException, ParseException {
        log.info("Processing " + inPath);
        if (!Files.isRegularFile(inPath)){
            log.info("Not a regular file:" + inPath.toString());
            return false;
        }

        String problem = null;
        try {
            problem = new String(Files.readAllBytes(inPath));
        } catch (IOException e) {
            throw new IOException("Could not read file " + inPath + " ::: " + e.getMessage());
        }

        ANTLRInputStream inputStream = new ANTLRInputStream(problem);
        parser.ParseContext parseContext = QmfAstGen.parse( inputStream,"tPTP_file",inPath.toString());
        Node root = parseContext.getRoot();

        // create input dot
        /*
        if (dotin != null){
            String dotInContent = root.toDot();
            Files.write(dotin, dotInContent.getBytes());
            if (dotBin != null){
                String cmd = dotBin + " -Tps " + dotin + " -o " + dotin + ".ps";
                Runtime.getRuntime().exec(cmd);
            }
        }*/

        // check for parse error
        if ( parseContext.hasParseError()) return false;

        // convert
        Converter c = new Converter(root,inPath.toString());
        ConvertContext context = c.convert();
        //System.out.println(context.getNewProblem());

        // create output dot
        /*
        if (dotout != null){
            String dotOutContent = context.converted.toDot();
            Files.write(dotout, dotOutContent.getBytes());
            if (dotBin != null){
                String cmd = dotBin + " -Tps " + dotout + " -o " + dotout + ".ps";
                Runtime.getRuntime().exec(cmd);
            }
        }*/


        // output
        String newProblem = context.getNewProblem();
        //System.out.println(newProblem);
        Files.write(oPath,newProblem.getBytes());

        return true;
    }
}