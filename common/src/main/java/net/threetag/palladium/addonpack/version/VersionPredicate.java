package net.threetag.palladium.addonpack.version;

import java.util.Collection;
import java.util.function.Predicate;

public interface VersionPredicate extends Predicate<Version> {

    /**
     * Get all terms that have to be satisfied for this predicate to match.
     *
     * @return Required predicate terms, empty if anything matches
     */
    Collection<? extends VersionPredicate.PredicateTerm> getTerms();

    /**
     * Get the version interval representing the matched versions.
     *
     * @return Covered version interval or null if nothing
     */
    VersionInterval getInterval();

    interface PredicateTerm {
        VersionComparisonOperator getOperator();
        Version getReferenceVersion();
    }

    static VersionPredicate parse(String predicate) throws VersionParsingException {
        return VersionPredicateParser.parse(predicate);
    }

    static Collection<VersionPredicate> parse(Collection<String> predicates) throws VersionParsingException {
        return VersionPredicateParser.parse(predicates);
    }
}