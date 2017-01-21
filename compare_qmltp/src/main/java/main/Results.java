package main;

import main.Comparators.CombinedComparator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Results {
    public List<Test> tests;
    public Results(){
        this.tests = new ArrayList<>();
    }
    public void addTest(Test t){
        this.tests.add(t);
    }

    // results
    // concerning qmltp and status
    /*
    public List<Problem> confirmedProblems; // confirm result of qmltp without disagreement of the atps
    public List<Problem> unconfirmedProblems; // qmltp yields a result but atps do not or atps disagree
    public List<Problem> newProblemResultTotal; // qmltp yields unsolved but atps have a result and they agree
    public List<Problem> newProblemResultTHM; // qmltp yields unsolved but atps have a result and they agree
    public List<Problem> newProblemResultCSA; // qmltp yields unsolved but atps have a result and they agree
    */
    public List<Problem> disagreementQmltpAtp; // if qmltp and atp results are different and all atp yield the same result
    public List<Problem> disagreementQmltpMleancop; // if qmltp and atp results are different and all atp yield the same result
    public List<Problem> totalProblems; // all problems
    public List<Problem> unique_THM;

    // atp specific
    public List<Problem> errorAtp; // at least one atp system has an error status
    public List<Problem> disagreementAtpAtp; // if at least two atp systems have different results
    public List<Problem> disagreementAtpMleancop; // if at least two atp systems have different results
    public List<Problem> atpCsaWhenMleancopCsaVaryCumul; // mleancop and atps agree all on CSA in varyCumul domains
    public List<Problem> mleancopCsaVaryCumul; // mleancop has CSA in vary/cumul domains
    public List<Problem> atpCsaWhenMleancopThmVaryCumul; // mleancop has CSA in vary/cumul domains but Embedding has THM

    public HashMap<String,List<Problem>> satallax; // STATUS -> List of Problems
    public HashMap<String,List<Problem>> leo;
    public HashMap<String,List<Problem>> nitpick;
    public HashMap<String,List<Problem>> mleancop;

    // table
    public List<String> table;

    public void evaluate(String outputPath){

        /*
        confirmedProblems = new ArrayList<>(); // y
        unconfirmedProblems = new ArrayList<>(); // y
        newProblemResultTotal = new ArrayList<>(); // y
        newProblemResultTHM = new ArrayList<>(); // y
        newProblemResultCSA = new ArrayList<>(); // y
        newProblems = new ArrayList<>();
        newProblemsSolved = new ArrayList<>(); // n
        */
        disagreementQmltpAtp = new ArrayList<>(); // y
        disagreementQmltpMleancop = new ArrayList<>();
        totalProblems = new ArrayList<>();


        unique_THM = new ArrayList<>();

        errorAtp = new ArrayList<>();
        disagreementAtpAtp = new ArrayList<>(); // y + n
        disagreementAtpMleancop = new ArrayList<>();
        atpCsaWhenMleancopCsaVaryCumul = new ArrayList<>();
        mleancopCsaVaryCumul = new ArrayList<>();
        atpCsaWhenMleancopThmVaryCumul = new ArrayList<>();
        satallax = new HashMap<>();
        satallax.put("ERR",new ArrayList<>());
        satallax.put("UNK",new ArrayList<>());
        satallax.put("THM",new ArrayList<>());
        satallax.put("CSA",new ArrayList<>());
        satallax.put("CSA_verified",new ArrayList<>());
        //satallax.put("CSA_unverified",new ArrayList<>());
        satallax.put("THM_unique",new ArrayList<>());
        leo = new HashMap<>();
        leo.put("ERR",new ArrayList<>());
        leo.put("UNK",new ArrayList<>());
        leo.put("THM",new ArrayList<>());
        leo.put("CSA",new ArrayList<>());
        leo.put("CSA_verified",new ArrayList<>());
        //leo.put("CSA_unverified",new ArrayList<>());
        leo.put("THM_unique",new ArrayList<>());
        nitpick = new HashMap<>();
        nitpick.put("ERR",new ArrayList<>());
        nitpick.put("UNK",new ArrayList<>());
        nitpick.put("THM",new ArrayList<>());
        nitpick.put("CSA",new ArrayList<>());
        nitpick.put("CSA_verified",new ArrayList<>());
        //nitpick.put("CSA_unverified",new ArrayList<>());
        nitpick.put("THM_unique",new ArrayList<>());
        mleancop = new HashMap<>();
        mleancop.put("ERR",new ArrayList<>());
        mleancop.put("UNK",new ArrayList<>());
        mleancop.put("THM",new ArrayList<>());
        mleancop.put("CSA",new ArrayList<>());

        table = new ArrayList<>();
        table.add("Semantics satSUM satTHM satCSA satCSA* leoSUM leoTHM leoCSA leoCSA* nitSUM nitTHM nitCSA nitCSA* U mleanSUM mleanTHM mleanCSA");

        for (Test test : this.tests){
            System.out.println("Evaluate: " + test.test_name + " " + test.system + " " + test.domains);

            // ============================================================
            // Maps for filling up Table entries. Reset after every Test
            HashMap<String,List<Problem>> csatallax = new HashMap<>();
            csatallax.put("ERR",new ArrayList<>());
            csatallax.put("UNK",new ArrayList<>());
            csatallax.put("THM",new ArrayList<>());
            csatallax.put("CSA",new ArrayList<>());
            csatallax.put("CSA_verified",new ArrayList<>());
            //csatallax.put("CSA_unverified",new ArrayList<>());
            csatallax.put("THM_unique",new ArrayList<>());
            HashMap<String,List<Problem>> cleo = new HashMap<>();
            cleo.put("ERR",new ArrayList<>());
            cleo.put("UNK",new ArrayList<>());
            cleo.put("THM",new ArrayList<>());
            cleo.put("CSA",new ArrayList<>());
            cleo.put("CSA_verified",new ArrayList<>());
            //cleo.put("CSA_unverified",new ArrayList<>());
            cleo.put("THM_unique",new ArrayList<>());
            HashMap<String,List<Problem>> cnitpick = new HashMap<>();
            cnitpick.put("ERR",new ArrayList<>());
            cnitpick.put("UNK",new ArrayList<>());
            cnitpick.put("THM",new ArrayList<>());
            cnitpick.put("CSA",new ArrayList<>());
            cnitpick.put("CSA_verified",new ArrayList<>());
            //cnitpick.put("CSA_unverified",new ArrayList<>());
            HashMap<String,List<Problem>> cmleancop = new HashMap<>();
            cmleancop.put("ERR",new ArrayList<>());
            cmleancop.put("UNK",new ArrayList<>());
            cmleancop.put("THM",new ArrayList<>());
            cmleancop.put("CSA",new ArrayList<>());
            List<Problem> cunique_THM = new ArrayList<>();

            for (Problem problem : test.getProblems()){
                //System.out.println(problem.toString());

                // put all problems int a list
                totalProblems.add(problem);

                // ===========================================================
                // save result for every atp in a list for all tests together
                // and for each test individually (prefix c)
                List<Problem> l = satallax.get(problem.status_satallax);
                l.add(problem);
                l = csatallax.get(problem.status_satallax);
                l.add(problem);
                l = leo.get(problem.status_leo);
                l.add(problem);
                l = cleo.get(problem.status_leo);
                l.add(problem);
                l = nitpick.get(problem.status_nitpick);
                l.add(problem);
                l = cnitpick.get(problem.status_nitpick);
                l.add(problem);
                if (problem.status_mleancop != null) {
                    // save to mleancop map
                    l = mleancop.get(problem.status_mleancop);
                    l.add(problem);
                    l = cmleancop.get(problem.status_mleancop);
                    l.add(problem);

                    // verified embedding CSA
                    if (
                            !hasError(problem) &&
                            atpsAgree(problem) &&
                            problem.status_satallax.equals("CSA") &&
                            problem.status_mleancop.equals("CSA")
                            )
                    {
                        l = satallax.get("CSA_verified");
                        l.add(problem);
                        l = csatallax.get("CSA_verified");
                        l.add(problem);
                    }
                    if (
                            !hasError(problem) &&
                            atpsAgree(problem) &&
                            problem.status_leo.equals("CSA") &&
                            problem.status_mleancop.equals("CSA")
                            )
                    {
                        l = leo.get("CSA_verified");
                        l.add(problem);
                        l = cleo.get("CSA_verified");
                        l.add(problem);
                    }
                    if (
                            !hasError(problem) &&
                            atpsAgree(problem) &&
                            problem.status_nitpick.equals("CSA") &&
                            problem.status_mleancop.equals("CSA")
                            )
                    {
                        l = nitpick.get("CSA_verified");
                        l.add(problem);
                        l = cnitpick.get("CSA_verified");
                        l.add(problem);
                    }

                    // 1.0 THM
                    if (
                            !hasError(problem) &&
                            atpsAgree(problem) &&
                            problem.status_satallax.equals("THM") &&
                            problem.status_mleancop.equals("UNK") // excludes contradictions with mleancop, maybe here should be !equals("THM")
                            )
                    {
                        l = satallax.get("THM_unique");
                        l.add(problem);
                        l = csatallax.get("THM_unique");
                        l.add(problem);
                        unique_THM.add(problem);
                        cunique_THM.add(problem);
                    }
                    if (
                            !hasError(problem) &&
                            atpsAgree(problem) &&
                            problem.status_leo.equals("THM") &&
                            problem.status_mleancop.equals("UNK") // excludes contradictions with mleancop, maybe here should be !equals("THM")
                            )
                    {
                        l = leo.get("THM_unique");
                        l.add(problem);
                        l = cleo.get("THM_unique");
                        l.add(problem);
                        unique_THM.add(problem);
                        cunique_THM.add(problem);
                    }
                }

                // compare qmltp status with atp status: atp status is not qmltp status
                //
                // qmltp entry exists &&
                // qmltp has a solution = 'not unsolved'
                // atps have no error
                // atps agree on solution
                // atps have a solution = solution is not UNK
                // atps solution is NOT the same as qmltp solution
                if (
                        problem.status_qmltp != null &&
                        !problem.status_qmltp.equals("UNK") &&
                        !hasError(problem) &&
                        atpsAgree(problem) &&
                        !getAgreedStatus(problem).equals("UNK") &&
                        !getAgreedStatus(problem).equals(problem.status_qmltp)
                        ){
                    disagreementQmltpAtp.add(problem);
                }

                // compare qmltp status with mleancop status: mleancop status is not qmltp status
                //
                // qmltp entry exists
                // qmltp has a solution
                // mleancop entry exists
                // mleancop has a solution = solution is not UNK
                // mleancop solution is NOT the same as qmltp solution
                if (
                        problem.status_qmltp != null &&
                        !problem.status_qmltp.equals("UNK") &&
                        problem.status_mleancop != null &&
                        !problem.status_mleancop.equals("UNK") &&
                        !problem.status_mleancop.equals(problem.status_qmltp)
                        ){
                    disagreementQmltpMleancop.add(problem);
                }

                // compare mleancop status with atp status: mleancop status is not agreed atp status
                // compare qmltp status with mleancop status: mleancop status is not qmltp status
                //
                // mleancop entry exists
                // mleancop has a solution = solution is not UNK
                // atps have no error
                // atps agree on solution
                // atps have a solution = solution is not UNK
                // atps solution is NOT the same as mleancop solution
                if (
                        problem.status_mleancop != null &&
                        !problem.status_mleancop.equals("UNK") &&
                        !hasError(problem) &&
                        atpsAgree(problem) &&
                        !getAgreedStatus(problem).equals("UNK") &&
                        !getAgreedStatus(problem).equals(problem.status_mleancop)
                        ){
                    disagreementAtpMleancop.add(problem);
                }

                // atps agree on CSA and Mleancop has CSA in varying/cumulative semantics
                if (
                        problem.status_mleancop != null &&
                        problem.status_mleancop.equals("CSA") &&
                        ( problem.domains.equals("varying") || problem.domains.equals("cumulative") ) &&
                        !hasError(problem) &&
                        atpsAgree(problem) &&
                        getAgreedStatus(problem).equals("CSA")
                        ){
                    atpCsaWhenMleancopCsaVaryCumul.add(problem);
                }

                // atps agree on CSA and Mleancop has THM in varying/cumulative semantics
                if (
                        problem.status_mleancop != null &&
                        problem.status_mleancop.equals("THM") &&
                        ( problem.domains.equals("varying") || problem.domains.equals("cumulative") ) &&
                        !hasError(problem) &&
                        atpsAgree(problem) &&
                        getAgreedStatus(problem).equals("CSA")
                        ){
                    atpCsaWhenMleancopThmVaryCumul.add(problem);
                }

                // Mleancop has CSA on vary/cumul domain semantics
                if (
                        problem.status_mleancop != null &&
                        problem.status_mleancop.equals("CSA") &&
                        ( problem.domains.equals("varying") || problem.domains.equals("cumulative") )
                        ){
                    mleancopCsaVaryCumul.add(problem);
                }

                // atps disagree or have error
                if (
                        !atpsAgree(problem)
                        ){
                    disagreementAtpAtp.add(problem);
                }


            }

            // create and add table entry for this test. columns are as follows
            //
            // Semantics
            // satSUM satTHM satCSA satCSA*
            // leoSUM leoTHM leoCSA leoCSA*
            // nitSUM nitTHM nitCSA nitCSA*
            // U
            // mleanSUM mleanTHM mleanCSA

            String delimiter = " & ";
            StringBuilder entry = new StringBuilder();
            // Semantics
            entry.append(test.test_name);
            entry.append(delimiter);
            // satSUM satTHM satCSA satCSA*
            entry.append(csatallax.get("THM").size() + csatallax.get("CSA_verified").size());
            entry.append(delimiter);
            entry.append(csatallax.get("THM").size());
            entry.append(delimiter);
            entry.append(csatallax.get("CSA_verified").size());
            entry.append(delimiter);
            entry.append(csatallax.get("CSA").size() - csatallax.get("CSA_verified").size());
            entry.append(delimiter);
            // leoSUM leoTHM leoCSA leoCSA*
            entry.append(cleo.get("THM").size() + cleo.get("CSA").size());
            entry.append(delimiter);
            entry.append(cleo.get("THM").size());
            entry.append(delimiter);
            entry.append(cleo.get("CSA_verified").size());
            entry.append(delimiter);
            entry.append(cleo.get("CSA").size() - cleo.get("CSA_verified").size());
            entry.append(delimiter);
            // nitSUM nitTHM nitCSA nitCSA*
            entry.append(cnitpick.get("THM").size() + cnitpick.get("CSA").size());
            entry.append(delimiter);
            entry.append(cnitpick.get("THM").size());
            entry.append(delimiter);
            entry.append(cnitpick.get("CSA_verified").size());
            entry.append(delimiter);
            entry.append(cnitpick.get("CSA").size() - cnitpick.get("CSA_verified").size());
            entry.append(delimiter);
            // U
            entry.append(cunique_THM.size());
            entry.append(delimiter);
            // mleanSUM mleanTHM mleanCSA
            entry.append(cmleancop.get("THM").size() + cmleancop.get("CSA").size());
            entry.append(delimiter);
            entry.append(cmleancop.get("THM").size());
            entry.append(delimiter);
            entry.append(cmleancop.get("CSA").size());
            entry.append(delimiter);
            table.add(entry.toString());
        }

        System.out.println("");
        System.out.println("==============================================");
        System.out.println("Results:");
        this.outputToStdout();
        this.outputToFiles(outputPath);
        System.out.println("FINISH");
    }

    private void outputToStdout(){
        System.out.println("totalProblems:                     " + totalProblems.size());
        /*
        System.out.println("ConfirmedProblems:       " + confirmedProblems.size());
        System.out.println("unconfirmedProblems:     " + unconfirmedProblems.size());
        System.out.println("newProblemResultTotal:     " + newProblemResultTotal.size());
        System.out.println("newProblemResultTHM:     " + newProblemResultTHM.size());
        System.out.println("newProblemResultCSA:     " + newProblemResultCSA.size());
        */
        System.out.println("disagreementQmltpAtp:              " + disagreementQmltpAtp.size());
        System.out.println("disagreementQmltpMleancop:         " + disagreementQmltpMleancop.size());
        /*
        System.out.println("newProblems:             " + newProblems.size());
        System.out.println("newProblemsSolved:       " + newProblemsSolved.size());
        System.out.println("totallyUnsolvedProblems: " + totallyUnsolvedProblems.size());
        */
        System.out.println("disagreementAtpAtp:                " + disagreementAtpAtp.size());
        System.out.println("disagreementAtpMleancop:           " + disagreementAtpMleancop.size());
        System.out.println("mleancopCsaVaryCumul:              " + mleancopCsaVaryCumul.size());
        System.out.println("atpCsaWhenMleancopCsaVaryCumul:    " + atpCsaWhenMleancopCsaVaryCumul.size());
        System.out.println("atpCsaWhenMleancopThmVaryCumul:    " + atpCsaWhenMleancopThmVaryCumul.size());
        System.out.println();
        System.out.println("ERR satallax               " + satallax.get("ERR").size());
        System.out.println("UNK satallax               " + satallax.get("UNK").size());
        System.out.println("THM satallax               " + satallax.get("THM").size());
        System.out.println("CSA satallax               " + satallax.get("CSA").size());
        System.out.println("CSA_verified satallax      " + satallax.get("CSA_verified").size());
        System.out.println("THM_unique satallax        " + satallax.get("THM_unique").size());
        System.out.println("ERR leo                    " + leo.get("ERR").size());
        System.out.println("UNK leo                    " + leo.get("UNK").size());
        System.out.println("THM leo                    " + leo.get("THM").size());
        System.out.println("CSA leo                    " + leo.get("CSA").size());
        System.out.println("CSA_verified leo           " + leo.get("CSA_verified").size());
        System.out.println("THM_unique leo             " + leo.get("THM_unique").size());
        System.out.println("ERR nitpick                " + nitpick.get("ERR").size());
        System.out.println("UNK nitpick                " + nitpick.get("UNK").size());
        System.out.println("THM nitpick                " + nitpick.get("THM").size());
        System.out.println("CSA nitpick                " + nitpick.get("CSA").size());
        System.out.println("CSA_verified nitpick       " + nitpick.get("CSA_verified").size());
        System.out.println("THM_unique nitpick         " + nitpick.get("THM_unique").size());
        System.out.println("ERR mleancop               " + mleancop.get("ERR").size());
        System.out.println("UNK mleancop               " + mleancop.get("UNK").size());
        System.out.println("THM mleancop               " + mleancop.get("THM").size());
        System.out.println("CSA mleancop               " + mleancop.get("CSA").size());
        System.out.println();
        System.out.println("THM unique sum             " + unique_THM.size());
        System.out.println();
        table.stream()
                .sorted((e1,e2)->new CombinedComparator().compare(e1,e2))
                .forEach(System.out::println);
    }

    private String statusMleanCopNoNull(Problem p){
        if (p == null) return "NUL";
        return p.status_mleancop;
    }

    private String statusQmltpNoNull(Problem p){
        if (p == null) return "NUL";
        return p.status_qmltp;
    }

    private String combineProblem(Problem p){
          return p.system + "," + p.domains + "," + p.constants + "," + p.consequence + "," + p.name + ","
                + statusQmltpNoNull(p) + "," + getAgreedStatus(p) + "," + statusMleanCopNoNull(p);
    }
    private void outputToFiles(String outputPath){
        System.out.println(Paths.get(outputPath.toString(),"totalProblems"));
        try {
            Files.write(Paths.get(outputPath.toString(),"totalProblems"),this.totalProblems.stream()
                    .map(p->combineProblem(p))
                    .collect(Collectors.joining("\n")).getBytes());
        } catch (IOException e) {
            System.err.println("Could not write totalProblems file");
            e.printStackTrace();
        }
        /*
        try {
            Files.write(Paths.get(outputPath.toString(),"confirmedProblems"),this.confirmedProblems.stream()
                    .map(p->p.system + "," + p.domains + "," + p.constants + "," + p.consequence + "," + p.name)
                    .collect(Collectors.joining("\n")).getBytes());
        } catch (IOException e) {
            System.err.println("Could not write confirmedProblems file");
            e.printStackTrace();
        }
        try {
            Files.write(Paths.get(outputPath.toString(),"unconfirmedProblems"),this.unconfirmedProblems.stream()
                    .map(p->p.system + "," + p.domains + "," + p.constants + "," + p.consequence + "," + p.name)
                    .collect(Collectors.joining("\n")).getBytes());
        } catch (IOException e) {
            System.err.println("Could not write unconfirmedProblems file");
            e.printStackTrace();
        }
        try {
            Files.write(Paths.get(outputPath.toString(),"newProblemResultTotal"),this.newProblemResultTotal.stream()
                    .map(p->p.system + "," + p.domains + "," + p.constants + "," + p.consequence + "," + p.name)
                    .collect(Collectors.joining("\n")).getBytes());
        } catch (IOException e) {
            System.err.println("Could not write newProblemResultTotal file");
            e.printStackTrace();
        }
        try {
            Files.write(Paths.get(outputPath.toString(),"newProblemResultTHM"),this.newProblemResultTHM.stream()
                    .map(p->p.system + "," + p.domains + "," + p.constants + "," + p.consequence + "," + p.name)
                    .collect(Collectors.joining("\n")).getBytes());
        } catch (IOException e) {
            System.err.println("Could not write newProblemResultTHM file");
            e.printStackTrace();
        }
        try {
            Files.write(Paths.get(outputPath.toString(),"newProblemResultCSA"),this.newProblemResultCSA.stream()
                    .map(p->p.system + "," + p.domains + "," + p.constants + "," + p.consequence + "," + p.name)
                    .collect(Collectors.joining("\n")).getBytes());
        } catch (IOException e) {
            System.err.println("Could not write newProblemResultCSA file");
            e.printStackTrace();
        }
        */
        try {
            Files.write(Paths.get(outputPath.toString(),"disagreementQmltpAtp"),this.disagreementQmltpAtp.stream()
                    .map(p->combineProblem(p))
                    .collect(Collectors.joining("\n")).getBytes());
        } catch (IOException e) {
            System.err.println("Could not write disagreementQmltpAtp file");
            e.printStackTrace();
        }
        try {
            Files.write(Paths.get(outputPath.toString(),"disagreementQmltpMleancop"),this.disagreementQmltpMleancop.stream()
                    .map(p->combineProblem(p))
                    .collect(Collectors.joining("\n")).getBytes());
        } catch (IOException e) {
            System.err.println("Could not write disagreementQmltpMleancop file");
            e.printStackTrace();
        }
        /*
        try {
            Files.write(Paths.get(outputPath.toString(),"newProblems"),this.newProblems.stream()
                    .map(p->p.system + "," + p.domains + "," + p.constants + "," + p.consequence + "," + p.name)
                    .collect(Collectors.joining("\n")).getBytes());
        } catch (IOException e) {
            System.err.println("Could not write newProblems file");
            e.printStackTrace();
        }
        try {
            Files.write(Paths.get(outputPath.toString(),"newProblemsSolved"),this.newProblemsSolved.stream()
                    .map(p->p.system + "," + p.domains + "," + p.constants + "," + p.consequence + "," + p.name)
                    .collect(Collectors.joining("\n")).getBytes());
        } catch (IOException e) {
            System.err.println("Could not write newProblemsSolved file");
            e.printStackTrace();
        }
        try {
            Files.write(Paths.get(outputPath.toString(),"totallyUnsolvedProblems"),this.totallyUnsolvedProblems.stream()
                    .map(p->p.system + "," + p.domains + "," + p.constants + "," + p.consequence + "," + p.name)
                    .collect(Collectors.joining("\n")).getBytes());
        } catch (IOException e) {
            System.err.println("Could not write totallyUnsolvedProblems file");
            e.printStackTrace();
        }
        */

        try {
            Files.write(Paths.get(outputPath.toString(),"disagreementAtpAtp"),this.disagreementAtpAtp.stream()
                    .map(p->combineProblem(p))
                    .collect(Collectors.joining("\n")).getBytes());
        } catch (IOException e) {
            System.err.println("Could not write disagreementAtpAtp file");
            e.printStackTrace();
        }

        try {
            Files.write(Paths.get(outputPath.toString(),"disagreementAtpMleancop"),this.disagreementAtpMleancop.stream()
                    .map(p->combineProblem(p))
                    .collect(Collectors.joining("\n")).getBytes());
        } catch (IOException e) {
            System.err.println("Could not write disagreementAtpMleancop file");
            e.printStackTrace();
        }
        try {
            Files.write(Paths.get(outputPath.toString(),"mleancopCsaVaryCumul"),this.mleancopCsaVaryCumul.stream()
                    .map(p->combineProblem(p))
                    .collect(Collectors.joining("\n")).getBytes());
        } catch (IOException e) {
            System.err.println("Could not write mleancopCsaVaryCumul file");
            e.printStackTrace();
        }
        try {
            Files.write(Paths.get(outputPath.toString(),"atpCsaWhenMleancopCsaVaryCumul"),this.atpCsaWhenMleancopCsaVaryCumul.stream()
                    .map(p->combineProblem(p))
                    .collect(Collectors.joining("\n")).getBytes());
        } catch (IOException e) {
            System.err.println("Could not write atpCsaWhenMleancopCsaVaryCumul file");
            e.printStackTrace();
        }
        try {
            Files.write(Paths.get(outputPath.toString(),"atpCsaWhenMleancopThmVaryCumul"),this.atpCsaWhenMleancopThmVaryCumul.stream()
                    .map(p->combineProblem(p))
                    .collect(Collectors.joining("\n")).getBytes());
        } catch (IOException e) {
            System.err.println("Could not write atpCsaWhenMleancopThmVaryCumul file");
            e.printStackTrace();
        }
        for (String status : satallax.keySet()){
            String filename = "satallax_" + status;
            try {
                Files.write(Paths.get(outputPath.toString(), filename ),this.satallax.get(status).stream()
                        .map(p->combineProblem(p))
                        .collect(Collectors.joining("\n")).getBytes());
            } catch (IOException e) {
                System.err.println("Could not write " + filename + " file");
                e.printStackTrace();
            }
        }
        for (String status : leo.keySet()){
            String filename = "leo_" + status;
            try {
                Files.write(Paths.get(outputPath.toString(), filename ),this.leo.get(status).stream()
                        .map(p->combineProblem(p))
                        .collect(Collectors.joining("\n")).getBytes());
            } catch (IOException e) {
                System.err.println("Could not write " + filename + " file");
                e.printStackTrace();
            }
        }
        for (String status : nitpick.keySet()){
            String filename = "nitpick_" + status;
            try {
                Files.write(Paths.get(outputPath.toString(), filename ),this.nitpick.get(status).stream()
                        .map(p->combineProblem(p))
                        .collect(Collectors.joining("\n")).getBytes());
            } catch (IOException e) {
                System.err.println("Could not write " + filename + " file");
                e.printStackTrace();
            }
        }
        for (String status : mleancop.keySet()){
            String filename = "mleancop" + status;
            try {
                Files.write(Paths.get(outputPath.toString(), filename ),this.mleancop.get(status).stream()
                        .map(p->combineProblem(p))
                        .collect(Collectors.joining("\n")).getBytes());
            } catch (IOException e) {
                System.err.println("Could not write " + filename + " file");
                e.printStackTrace();
            }
        }
    }

    private boolean hasError(Problem p){
        return p.status_satallax.equals("ERR") || p.status_leo.equals("ERR") || p.status_nitpick.equals("ERR");
    }

    private boolean atpsAgree(Problem p){
        // three agree
        if (p.status_satallax.equals(p.status_leo) && p.status_leo.equals(p.status_nitpick)) return true;
        // one UNK, two agree
        if (p.status_satallax.equals("UNK") && p.status_leo.equals(p.status_nitpick)) return true;
        if (p.status_leo.equals("UNK") && p.status_satallax.equals(p.status_nitpick)) return true;
        if (p.status_nitpick.equals("UNK") && p.status_satallax.equals(p.status_leo)) return true;
        // two UNK (one not UNK)
        if (p.status_satallax.equals("UNK") && p.status_leo.equals("UNK")) return true;
        if (p.status_satallax.equals("UNK") && p.status_nitpick.equals("UNK")) return true;
        if (p.status_nitpick.equals("UNK") && p.status_leo.equals("UNK")) return true;
        // all other cases disagreement
        return false;
    }

    private String getAgreedStatus(Problem p){
        if (p.status_satallax.equals(p.status_leo) && p.status_leo.equals(p.status_nitpick)) return p.status_satallax;
        // one UNK, two agree
        if (p.status_satallax.equals("UNK") && p.status_leo.equals(p.status_nitpick)) return p.status_leo;
        if (p.status_leo.equals("UNK") && p.status_satallax.equals(p.status_nitpick)) return p.status_satallax;
        if (p.status_nitpick.equals("UNK") && p.status_satallax.equals(p.status_leo)) return p.status_satallax;
        // two UNK (one not UNK)
        if (p.status_satallax.equals("UNK") && p.status_leo.equals("UNK")) return p.status_nitpick;
        if (p.status_satallax.equals("UNK") && p.status_nitpick.equals("UNK")) return p.status_leo;
        if (p.status_nitpick.equals("UNK") && p.status_leo.equals("UNK")) return p.status_satallax;
        // disagreement
        return null;
    }

}
