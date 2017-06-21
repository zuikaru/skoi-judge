package com.talesdev.judge.score;

import java.util.List;

/**
 * Score consists of many verdict from each test case
 * @author MoKunz
 */
public interface Score extends Verdict{
    /**
     * All sub-verdict
     * @return list of verdict
     */
    List<Verdict> all();

    /**
     * How many test cases were passed?
     * @return number of test case
     */
    int passed();

    /**
     * How many test cases?
     * @return number of test case
     */
    int total();

    /**
     * Percentage
     * @return percentage
     */
    double percentage();

}
