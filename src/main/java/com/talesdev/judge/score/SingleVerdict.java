package com.talesdev.judge.score;

/**
 * Single verdict
 *
 * @author MoKunz
 */
public final class SingleVerdict implements Verdict {
    private final Reason reason;
    private final int time;
    private final int memory;

    public SingleVerdict(Reason reason, int time, int memory) {
        this.reason = reason;
        this.time = time;
        this.memory = memory;
    }

    public SingleVerdict(Reason reason) {
        this(reason,0,0);
    }

    @Override
    public boolean accepted() {
        return reason == Reason.ACCEPTED;
    }

    @Override
    public Reason reason() {
        return reason;
    }

    @Override
    public String code() {
        return String.valueOf(reason.code());
    }

    @Override
    public int time() {
        return time;
    }

    @Override
    public int memory() {
        return memory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SingleVerdict that = (SingleVerdict) o;

        return reason == that.reason;

    }

    @Override
    public int hashCode() {
        return reason != null ? reason.hashCode() : 0;
    }
}
