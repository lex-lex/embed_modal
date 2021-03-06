package util.external_software_wrappers;

import org.zeroturnaround.process.JavaProcess;
import org.zeroturnaround.process.Processes;
import util.ProcessKiller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class MLeanCopWrapper {

    private static final Logger log = Logger.getLogger( "default" );
    public static String mleancop_binary = "runmleancop";

    public String stdout = "";
    public String stderr = "";
    public String status = "";
    public boolean timeout = false;
    public double duration = -1;

    public void call(Path filename,long timeout,TimeUnit unit, String axiom, String domains) {
        this.stdout = "";
        this.stderr = "";
        this.status = "";
        this.timeout = false;
        this.duration = timeout;

        List<String> params = java.util.Arrays.asList("/bin/bash", mleancop_binary, axiom, domains, filename.toString(), String.valueOf(timeout));
        Process proc = null;
        try {
            ProcessBuilder mleancop = new ProcessBuilder(params);
            Instant start = Instant.now();
            proc = mleancop.start();
            System.out.println("::: mleancop started");
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
            if (!proc.waitFor(timeout+11, unit)) {
                System.out.println("::: waited but timeout");
                ProcessKiller.killAllOlderThan((int)timeout+10,"mleancop");
                log.fine(filename.toString() + " : Proof Timeout");
                this.timeout = true;
            }else{
                System.out.println("::: waited no timeout");
                Instant end = Instant.now();
                Duration delta = Duration.between(start,end);
                this.duration =  (double) delta.getSeconds() + ( (double) delta.getNano() ) / 1000000000.0;
            }
            if (!this.timeout) {
                String s = null;
                System.out.println("::: now reading stdout");
                while ((s = stdInput.readLine()) != null) {
                    System.out.println("::: reading one line from stdout");
                    stdout += s;
                }
                System.out.println("::: finished reading from stdout. now reading stderr");
                while ((s = stdError.readLine()) != null) {
                    System.out.println("::: reading one line from stderr");
                    stderr += s;
                }
                System.out.println("::: finished reading from stderr");
            }
        } catch (IOException e) {
            if (this.stderr == null) this.stderr = e.getMessage();
            if (this.stdout == null) this.stdout = e.getMessage();
            System.out.println("::: IOException");
            System.out.println(e.toString());
        } catch (InterruptedException e) {
            System.out.println("::: Interrupted Exception");
            System.err.println(filename.toString() + " : Interrupted Exception.");
            if (this.stderr == null) this.stderr = e.getMessage();
            if (this.stdout == null) this.stdout = e.getMessage();
            System.out.println(e.toString());
            this.timeout = true;
        }finally {
            this.status = extractSZSStatus(this.stdout);
            //System.out.println(this.status);
        }
        System.out.println("::: Mleancop call finished");

            /*
            if (!proc.waitFor(timeout, unit)) {
                log.fine(filename.toString() + " : Proof Timeout");
                this.timeout = true;
                ProcessKiller.destroyProc(proc, 1500L);
            }else{
                Instant end = Instant.now();
                Duration delta = Duration.between(start,end);
                this.duration =  (double) delta.getSeconds() + ( (double) delta.getNano() ) / 1000000000.0;
                }
            String s = null;
            while ((s = stdInput.readLine()) != null) {
                stdout += s;
            }
            while ((s = stdError.readLine()) != null) {
                stderr += s;
            }
            JavaProcess process = Processes.newJavaProcess(proc);
            if (process.isAlive()) ProcessKiller.destroyProc(proc, 1500L);
        } catch (IOException e) {
            if (this.stderr == null) this.stderr = e.getMessage();
            if (this.stdout == null) this.stdout = e.getMessage();
            if (proc != null) ProcessKiller.destroyProc(proc, 1500L);
        } catch (InterruptedException e) {
            log.fine(filename.toString() + " : Interrupted Exception.");
            if (this.stderr == null) this.stderr = e.getMessage();
            if (this.stdout == null) this.stdout = e.getMessage();
            this.timeout = true;
            if (proc != null) ProcessKiller.destroyProc(proc, 1500L);
        }finally {
            this.status = extractSZSStatus(this.stdout);
            //System.out.println(this.status);
            if (proc != null) ProcessKiller.destroyProc(proc, 1500L);
        }*/
    }

    private String extractSZSStatus(String consoleOutput){
        int szs_start = consoleOutput.indexOf("SZS status ");
        if (szs_start == -1) return "NOSTATUS";
        szs_start += "SZS status ".length();
        int szs_end = consoleOutput.indexOf(" ",szs_start);
        if (szs_end == -1) szs_end = consoleOutput.length();
        String status = consoleOutput.substring(szs_start,szs_end);
        return status;
    }

    public String getSZSStatus(){
        return this.status;
    }

    public boolean isTheorem(){
        return this.status.contains("Theorem");
    }

    public boolean isCounterSatisfiable(){
        return this.status.contains("CounterSatisfiable");
    }

    public boolean hasParserError(){
        return this.stdout.contains("Parse problem");
    }

    public boolean hasError(){
        return this.stdout.contains("Error");
    }

    public String getAbbrevStatus(){
        if (this.isTheorem()) return "THM";
        if (this.isCounterSatisfiable()) return "CSA";
        if (this.hasError()) return "ERR";
        return "UNK";
    }

}
