package net.threetag.palladium.addonpack.version;

import java.util.*;

public final class VersionPredicateParser {
    private static final VersionComparisonOperator[] OPERATORS = VersionComparisonOperator.values();

    public static VersionPredicate parse(String predicate) throws VersionParsingException {
        List<VersionPredicateParser.SingleVersionPredicate> predicateList = new ArrayList<>();

        for (String s : predicate.split(" ")) {
            s = s.trim();

            if (s.isEmpty() || s.equals("*")) {
                continue;
            }

            VersionComparisonOperator operator = VersionComparisonOperator.EQUAL;

            for (VersionComparisonOperator op : OPERATORS) {
                if (s.startsWith(op.getSerialized())) {
                    operator = op;
                    s = s.substring(op.getSerialized().length());
                    break;
                }
            }

            Version version = VersionParser.parse(s, true);

            if (version instanceof SemanticVersion) {
                SemanticVersion semVer = (SemanticVersion) version;

                if (semVer.hasWildcard()) { // .x version -> replace with conventional version by replacing the operator
                    if (operator != VersionComparisonOperator.EQUAL) {
                        throw new VersionParsingException("Invalid predicate: " + predicate + ", version ranges with wildcards (.X) require using the equality operator or no operator at all!");
                    }

                    assert !semVer.getPrereleaseKey().isPresent();

                    int compCount = semVer.getVersionComponentCount();
                    assert compCount == 2 || compCount == 3;

                    operator = compCount == 2 ? VersionComparisonOperator.SAME_TO_NEXT_MAJOR : VersionComparisonOperator.SAME_TO_NEXT_MINOR;

                    int[] newComponents = new int[semVer.getVersionComponentCount() - 1];

                    for (int i = 0; i < semVer.getVersionComponentCount() - 1; i++) {
                        newComponents[i] = semVer.getVersionComponent(i);
                    }

                    version = new SemanticVersionImpl(newComponents, "", semVer.getBuildKey().orElse(null));
                }
            } else if (!operator.isMinInclusive() && !operator.isMaxInclusive()) { // non-semver without inclusive bound
                throw new VersionParsingException("Invalid predicate: " + predicate + ", version ranges need to be semantic version compatible to use operators that exclude the bound!");
            } else { // non-semver with inclusive bound
                operator = VersionComparisonOperator.EQUAL;
            }

            predicateList.add(new VersionPredicateParser.SingleVersionPredicate(operator, version));
        }

        if (predicateList.isEmpty()) {
            return VersionPredicateParser.AnyVersionPredicate.INSTANCE;
        } else if (predicateList.size() == 1) {
            return predicateList.get(0);
        } else {
            return new VersionPredicateParser.MultiVersionPredicate(predicateList);
        }
    }

    public static Set<VersionPredicate> parse(Collection<String> predicates) throws VersionParsingException {
        Set<VersionPredicate> ret = new HashSet<>(predicates.size());

        for (String version : predicates) {
            ret.add(parse(version));
        }

        return ret;
    }

    public static VersionPredicate getAny() {
        return VersionPredicateParser.AnyVersionPredicate.INSTANCE;
    }

    static class AnyVersionPredicate implements VersionPredicate {

        static final VersionPredicate INSTANCE = new VersionPredicateParser.AnyVersionPredicate();

        private AnyVersionPredicate() {
        }

        @Override
        public boolean test(Version t) {
            return true;
        }

        @Override
        public List<? extends PredicateTerm> getTerms() {
            return Collections.emptyList();
        }

        @Override
        public VersionInterval getInterval() {
            return VersionIntervalImpl.INFINITE;
        }

        @Override
        public String toString() {
            return "*";
        }
    }

    static class SingleVersionPredicate implements VersionPredicate, VersionPredicate.PredicateTerm {
        private final VersionComparisonOperator operator;
        private final Version refVersion;

        SingleVersionPredicate(VersionComparisonOperator operator, Version refVersion) {
            this.operator = operator;
            this.refVersion = refVersion;
        }

        @Override
        public boolean test(Version version) {
            Objects.requireNonNull(version, "null version");

            return operator.test(version, refVersion);
        }

        @Override
        public List<PredicateTerm> getTerms() {
            return Collections.singletonList(this);
        }

        @Override
        public VersionInterval getInterval() {
            if (refVersion instanceof SemanticVersion) {
                SemanticVersion version = (SemanticVersion) refVersion;

                return new VersionIntervalImpl(operator.minVersion(version), operator.isMinInclusive(),
                        operator.maxVersion(version), operator.isMaxInclusive());
            } else {
                return new VersionIntervalImpl(refVersion, true, refVersion, true);
            }
        }

        @Override
        public VersionComparisonOperator getOperator() {
            return operator;
        }

        @Override
        public Version getReferenceVersion() {
            return refVersion;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof VersionPredicateParser.SingleVersionPredicate) {
                VersionPredicateParser.SingleVersionPredicate o = (VersionPredicateParser.SingleVersionPredicate) obj;

                return operator == o.operator && refVersion.equals(o.refVersion);
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return operator.ordinal() * 31 + refVersion.hashCode();
        }

        @Override
        public String toString() {
            return operator.getSerialized().concat(refVersion.toString());
        }
    }

    static class MultiVersionPredicate implements VersionPredicate {
        private final List<VersionPredicateParser.SingleVersionPredicate> predicates;

        MultiVersionPredicate(List<VersionPredicateParser.SingleVersionPredicate> predicates) {
            this.predicates = predicates;
        }

        @Override
        public boolean test(Version version) {
            Objects.requireNonNull(version, "null version");

            for (VersionPredicateParser.SingleVersionPredicate predicate : predicates) {
                if (!predicate.test(version)) return false;
            }

            return true;
        }

        @Override
        public List<? extends PredicateTerm> getTerms() {
            return predicates;
        }

        @Override
        public VersionInterval getInterval() {
            if (predicates.isEmpty()) return VersionPredicateParser.AnyVersionPredicate.INSTANCE.getInterval();

            VersionInterval ret = predicates.get(0).getInterval();

            for (int i = 1; i < predicates.size(); i++) {
                ret = VersionIntervalImpl.and(ret, predicates.get(i).getInterval());
            }

            return ret;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof VersionPredicateParser.MultiVersionPredicate) {
                VersionPredicateParser.MultiVersionPredicate o = (VersionPredicateParser.MultiVersionPredicate) obj;

                return predicates.equals(o.predicates);
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return predicates.hashCode();
        }

        @Override
        public String toString() {
            StringBuilder ret = new StringBuilder();

            for (VersionPredicateParser.SingleVersionPredicate predicate : predicates) {
                if (ret.length() > 0) ret.append(' ');
                ret.append(predicate.toString());
            }

            return ret.toString();
        }
    }
}
