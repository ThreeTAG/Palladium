package net.threetag.palladium.addonpack.version;

import java.util.Collection;
import java.util.List;

public interface VersionInterval {
    
    VersionInterval INFINITE = new VersionIntervalImpl(null, false, null, false);

    /**
     * Get whether the interval uses {@link SemanticVersion} compatible bounds.
     *
     * @return True if both bounds are open (null), {@link SemanticVersion} instances or a combination of both, false otherwise.
     */
    boolean isSemantic();

    /**
     * Get the lower limit of the version interval.
     *
     * @return Version's lower limit or null if none, inclusive depending on {@link #isMinInclusive()}
     */
    Version getMin();

    /**
     * Get whether the lower limit of the version interval is inclusive.
     *
     * @return True if inclusive, false otherwise
     */
    boolean isMinInclusive();

    /**
     * Get the upper limit of the version interval.
     *
     * @return Version's upper limit or null if none, inclusive depending on {@link #isMaxInclusive()}
     */
    Version getMax();

    /**
     * Get whether the upper limit of the version interval is inclusive.
     *
     * @return True if inclusive, false otherwise
     */
    boolean isMaxInclusive();

    default VersionInterval and(VersionInterval o) {
        return and(this, o);
    }

    default List<VersionInterval> or(Collection<VersionInterval> o) {
        return or(o, this);
    }

    default List<VersionInterval> not() {
        return not(this);
    }

    /**
     * Compute the intersection between two version intervals.
     */
    static VersionInterval and(VersionInterval a, VersionInterval b) {
        return VersionIntervalImpl.and(a, b);
    }

    /**
     * Compute the intersection between two potentially disjoint of version intervals.
     */
    static List<VersionInterval> and(Collection<VersionInterval> a, Collection<VersionInterval> b) {
        return VersionIntervalImpl.and(a, b);
    }

    /**
     * Compute the union between multiple version intervals.
     */
    static List<VersionInterval> or(Collection<VersionInterval> a, VersionInterval b) {
        return VersionIntervalImpl.or(a, b);
    }

    static List<VersionInterval> not(VersionInterval interval) {
        return VersionIntervalImpl.not(interval);
    }

    static List<VersionInterval> not(Collection<VersionInterval> intervals) {
        return VersionIntervalImpl.not(intervals);
    }
}
