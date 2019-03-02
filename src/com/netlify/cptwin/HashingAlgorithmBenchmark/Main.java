package com.netlify.cptwin.HashingAlgorithmBenchmark;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static long MAXIMUM_DELAY = 2000;
    public static final int NUMBER_OF_TESTS = 10;
    public static long LOW_SECURITY_MS = 100;
    public static long HIGH_SECURITY_MS = 1000;

    public static long modernWorkFactorMultiplier = 1;

    public static ArrayList<Result> BCryptResultList;
    public static ArrayList<Result> SCryptResultList;
    public static ArrayList<Result> PBKDF2ResultList;
    public static ArrayList<Result> Argon2ResultList;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter the approximate age of your CPU in YYYY-MM-DD format: ");
        //2015-03-31 This is approximately the date the i5-5300U was launched
        Period diff = Period.between(LocalDate.parse(sc.nextLine()).withDayOfMonth(1), LocalDateTime.now().withDayOfMonth(1).toLocalDate());
        modernWorkFactorMultiplier = diff.toTotalMonths() / 18;
        System.out.println("Modern Work Factor Multiplier: " + modernWorkFactorMultiplier);
        BCryptResultList = new ArrayList<>();
        SCryptResultList = new ArrayList<>();
        PBKDF2ResultList = new ArrayList<>();
        Argon2ResultList = new ArrayList<>();

        adjustTimeFactors();

        warmUpBcrypt();
        runBcryptBenchmark();
        warmUpSCrypt();
        runSCryptBenchmark();
        warmUpPBKDF2();
        runPBKDF2Benchmark();
        warmUpArgon2();
        runArgon2Benchmark();
        outputResults();
    }

    public static void adjustTimeFactors() {
        LOW_SECURITY_MS = LOW_SECURITY_MS * modernWorkFactorMultiplier;
        HIGH_SECURITY_MS = HIGH_SECURITY_MS * modernWorkFactorMultiplier;
        MAXIMUM_DELAY = MAXIMUM_DELAY * modernWorkFactorMultiplier;
    }

    public static void outputResults() {
        Result bcryptLowSecurityResult = null;
        Result bcryptHighSecurityResult = null;
        for(Result result : BCryptResultList) {
            if(bcryptLowSecurityResult == null) {
                bcryptLowSecurityResult = result;
            } else if((result.timeInMilliseconds <= LOW_SECURITY_MS) && (result.timeInMilliseconds > bcryptLowSecurityResult.timeInMilliseconds)) {
                bcryptLowSecurityResult = result;
            }

            if(bcryptHighSecurityResult == null) {
                bcryptHighSecurityResult = result;
            } else if((result.timeInMilliseconds <= HIGH_SECURITY_MS) && (result.timeInMilliseconds > bcryptHighSecurityResult.timeInMilliseconds)) {
                bcryptHighSecurityResult = result;
            }
        }

        Result scryptLowSecurityResult = null;
        Result scryptHighSecurityResult = null;
        for(Result result : SCryptResultList) {
            if(scryptLowSecurityResult == null) {
                scryptLowSecurityResult = result;
            } else if((result.timeInMilliseconds <= LOW_SECURITY_MS) && (result.timeInMilliseconds > scryptLowSecurityResult.timeInMilliseconds)) {
                scryptLowSecurityResult = result;
            }

            if(scryptHighSecurityResult == null) {
                scryptHighSecurityResult = result;
            } else if((result.timeInMilliseconds <= HIGH_SECURITY_MS) && (result.timeInMilliseconds > scryptHighSecurityResult.timeInMilliseconds)) {
                scryptHighSecurityResult = result;
            }
        }

        Result pbkdf2LowSecurityResult = null;
        Result pbkdf2HighSecurityResult = null;
        for(Result result : PBKDF2ResultList) {
            if(pbkdf2LowSecurityResult == null) {
                pbkdf2LowSecurityResult = result;
            } else if((result.timeInMilliseconds <= LOW_SECURITY_MS) && (result.timeInMilliseconds > pbkdf2LowSecurityResult.timeInMilliseconds)) {
                pbkdf2LowSecurityResult = result;
            }

            if(pbkdf2HighSecurityResult == null) {
                pbkdf2HighSecurityResult = result;
            } else if((result.timeInMilliseconds <= HIGH_SECURITY_MS) && (result.timeInMilliseconds > pbkdf2HighSecurityResult.timeInMilliseconds)) {
                pbkdf2HighSecurityResult = result;
            }
        }

        Result argon2LowSecurityResult = null;
        Result argon2HighSecurityResult = null;
        for(Result result : Argon2ResultList) {
            if(argon2LowSecurityResult == null) {
                argon2LowSecurityResult = result;
            } else if((result.timeInMilliseconds <= LOW_SECURITY_MS) && (result.timeInMilliseconds > argon2LowSecurityResult.timeInMilliseconds)) {
                argon2LowSecurityResult = result;
            }

            if(argon2HighSecurityResult == null) {
                argon2HighSecurityResult = result;
            } else if((result.timeInMilliseconds <= HIGH_SECURITY_MS) && (result.timeInMilliseconds > argon2HighSecurityResult.timeInMilliseconds)) {
                argon2HighSecurityResult = result;
            }
        }

        System.out.println("+++++++++++++++++++++++++++++++++++");
        System.out.println("Results for " + LocalDateTime.now());
        System.out.println("+++++++++++++++++++++++++++++++++++");

        System.out.println("###################################");
        System.out.println("##           BCRYPT             ###");
        System.out.println("# Low Security - Work Factor " + bcryptLowSecurityResult.workFactor);
        System.out.println("# High Security - Work Factor " + bcryptHighSecurityResult.workFactor);
        System.out.println("# High Security Against Next-Gen  #");
        System.out.println("# Work Factor - " + BCryptResultList.get(BCryptResultList.size() - 1).workFactor);
        System.out.println("###################################");

        System.out.println("###################################");
        System.out.println("##           SCRYPT             ###");
        System.out.println("# Low Security - Work Factor " + scryptLowSecurityResult.workFactor);
        System.out.println("# High Security - Work Factor " + scryptHighSecurityResult.workFactor);
        System.out.println("# High Security Against Next-Gen  #");
        System.out.println("# Work Factor - " + SCryptResultList.get(SCryptResultList.size() - 1).workFactor);
        System.out.println("###################################");

        System.out.println("###################################");
        System.out.println("##           PBKDF2             ###");
        System.out.println("# Low Security - Work Factor " + pbkdf2LowSecurityResult.workFactor);
        System.out.println("# High Security - Work Factor " + pbkdf2HighSecurityResult.workFactor);
        System.out.println("# High Security Against Next-Gen  #");
        System.out.println("# Work Factor - " + PBKDF2ResultList.get(PBKDF2ResultList.size() - 1).workFactor);
        System.out.println("###################################");

        System.out.println("###################################");
        System.out.println("##           Argon2             ###");
        System.out.println("# Low Security - Work Factor " + argon2LowSecurityResult.workFactor);
        System.out.println("# High Security - Work Factor " + argon2HighSecurityResult.workFactor);
        System.out.println("# High Security Against Next-Gen  #");
        System.out.println("# Work Factor - " + Argon2ResultList.get(Argon2ResultList.size() - 1).workFactor);
        System.out.println("###################################");

        System.out.println("++++++++++++++CSV Results++++++++++");
        System.out.println("algorithm,workfactor,timeinmilliseconds");
        for(Result result : BCryptResultList) {
            System.out.println(result.hashingAlgorithm.name()+","+result.workFactor+","+result.timeInMilliseconds);
        }
        for(Result result : SCryptResultList) {
            System.out.println(result.hashingAlgorithm.name()+","+result.workFactor+","+result.timeInMilliseconds);
        }
        for(Result result : PBKDF2ResultList) {
            System.out.println(result.hashingAlgorithm.name()+","+result.workFactor+","+result.timeInMilliseconds);
        }
        for(Result result : Argon2ResultList) {
            System.out.println(result.hashingAlgorithm.name()+","+result.workFactor+","+result.timeInMilliseconds);
        }
    }

    public static void warmUpArgon2() {
        System.out.println("Warming up Argon2...");
        for(int i = 4; i <= 20; i++) {
            Argon2.generatePasswordHash("password",i);
        }
        System.out.println("Finished Warming up Argon2!");
    }

    public static void warmUpPBKDF2() {
        System.out.println("Warming up PBKDF2...");
        for(int i = 1; i <= 10; i++) {
            PBKDF2.generatePasswordHash("password", 10000*i);
        }
        System.out.println("Finished Warming up PBKDF2!");
    }

    public static void warmUpSCrypt() {
        System.out.println("Warming up SCrypt...");
        //N = General work factor, iteration count.
        //r = blocksize in use for underlying hash; fine-tunes the relative memory-cost.
        //p = parallelization factor; fine-tunes the relative cpu-cost.
        for(int i = 1; i <= 10; i++) {
            com.lambdaworks.crypto.SCryptUtil.scrypt("password", 16384, 8, 1);
        }
        System.out.println("Finished Warming Up SCrypt!");
    }

    public static void warmUpBcrypt() {
        System.out.println("Warming up BCrypt...");
        for(int i = 4; i <= 16; i++) {
            BCrypt.hashpw("password", BCrypt.gensalt(i));
        }
        System.out.println("Finished Warming Up BCrypt!");
    }

    public static void runArgon2Benchmark() {
        System.out.println("Running Argon2 Benchmark...");
        int i = 100;
        while(true) {
            //System.out.println("Argon2 Work Factor " + i);
            long totalTime = 0;
            long endTime = 0;
            long duration = 0;
            long startTime = System.nanoTime();
            for(int x = 0; x < NUMBER_OF_TESTS; x++) {
                startTime = System.nanoTime();
                Argon2.generatePasswordHash("password",i);
                endTime = System.nanoTime();
                duration = (endTime - startTime)/1000000;
                totalTime += duration;
                //System.out.println("Time taken " + duration + "ms");
            }
            long averageTimeInMs = totalTime/NUMBER_OF_TESTS;
            //System.out.println("AVG Time taken " + averageTimeInMs + "ms");
            //System.out.println("Total Time taken " + totalTime + "ms");
            Argon2ResultList.add(new Result(HashingAlgorithms.ARGON2, i, averageTimeInMs));
            i*=2;
            if((totalTime/NUMBER_OF_TESTS) > MAXIMUM_DELAY) {
                break;
            }
        }
    }

    public static void runPBKDF2Benchmark() {
        System.out.println("Running PBKDF2 Benchmark...");
        int i = 10000;
        while(true) {
            //System.out.println("PBKDF2 Work Factor " + i);
            long totalTime = 0;
            long endTime = 0;
            long duration = 0;
            long startTime = System.nanoTime();
            for(int x = 0; x < NUMBER_OF_TESTS; x++) {
                startTime = System.nanoTime();
                PBKDF2.generatePasswordHash("password", i);
                endTime = System.nanoTime();
                duration = (endTime - startTime)/1000000;
                totalTime += duration;
                //System.out.println("Time taken " + duration + "ms");
            }
            long averageTimeInMs = totalTime/NUMBER_OF_TESTS;
            //System.out.println("AVG Time taken " + averageTimeInMs + "ms");
            //System.out.println("Total Time taken " + totalTime + "ms");
            PBKDF2ResultList.add(new Result(HashingAlgorithms.PBKDF2, i, averageTimeInMs));
            i*=2;
            if((totalTime/NUMBER_OF_TESTS) > MAXIMUM_DELAY) {
                break;
            }
        }
    }

    public static void runSCryptBenchmark() {
        System.out.println("Running SCrypt Benchmark...");
        int i = 1024;
        while(true) {
            //System.out.println("SCrypt Work Factor " + i);
            long totalTime = 0;
            long endTime = 0;
            long duration = 0;
            long startTime = System.nanoTime();
            for(int x = 0; x < NUMBER_OF_TESTS; x++) {
                startTime = System.nanoTime();
                com.lambdaworks.crypto.SCryptUtil.scrypt("password", i, 8, 1);
                endTime = System.nanoTime();
                duration = (endTime - startTime)/1000000;
                totalTime += duration;
                //System.out.println("Time taken " + duration + "ms");
            }
            long averageTimeInMs = totalTime/NUMBER_OF_TESTS;
            //System.out.println("AVG Time taken " + averageTimeInMs + "ms");
            //System.out.println("Total Time taken " + totalTime + "ms");
            SCryptResultList.add(new Result(HashingAlgorithms.SCRYPT, i, averageTimeInMs));
            i*=2;
            if((totalTime/NUMBER_OF_TESTS) > MAXIMUM_DELAY) {
                break;
            }
        }
    }

    public static void runBcryptBenchmark() {
        System.out.println("Running BCrypt Benchmark...");
        int i = 4;
        while(true) {
            //System.out.println("BCrypt Work Factor " + i);
            long totalTime = 0;
            long endTime = 0;
            long duration = 0;
            long startTime = System.nanoTime();
            for(int x = 0; x < NUMBER_OF_TESTS; x++) {
                startTime = System.nanoTime();
                BCrypt.hashpw("password", BCrypt.gensalt(i));
                endTime = System.nanoTime();
                duration = (endTime - startTime)/1000000;
                totalTime += duration;
                //System.out.println("Time taken " + duration + "ms");
            }
            long averageTimeInMs = totalTime/NUMBER_OF_TESTS;
            /*System.out.println("AVG Time taken " + averageTimeInMs + "ms");
            System.out.println("Total Time taken " + totalTime + "ms");*/
            BCryptResultList.add(new Result(HashingAlgorithms.BCRYPT, i, averageTimeInMs));
            i++;
            if((totalTime/NUMBER_OF_TESTS) > MAXIMUM_DELAY) {
                break;
            }
        }
    }
}
