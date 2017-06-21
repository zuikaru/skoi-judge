package com.talesdev.judge.score;

/**
 * Verdict
 * @author MoKunz
 */
public interface Verdict {
    /**
     * Is accepted?
     * @return true if accepted, otherwise false
     */
    boolean accepted();

    /**
     * Reason for given verdict
     * @return reason
     */
    Reason reason();

    /**
     * Return verdict as code
     * @return verdict code
     */
    String code();

    /**
     * Time used (in ms)
     * @return time
     */
    int time();

    /**
     * Memory used (in kB)
     * @return memory usage
     */
    int memory();
}
