package com.netlify.cptwin.HashingAlgorithmBenchmark;

public class Result {

    public HashingAlgorithms hashingAlgorithm;
    public int workFactor;
    public long timeInMilliseconds;

    public Result(HashingAlgorithms hashingAlgorithm, int workFactor, long timeInMilliseconds)
    {
        super();
        this.hashingAlgorithm = hashingAlgorithm;
        this.workFactor = workFactor;
        this.timeInMilliseconds = timeInMilliseconds;
    }

}
