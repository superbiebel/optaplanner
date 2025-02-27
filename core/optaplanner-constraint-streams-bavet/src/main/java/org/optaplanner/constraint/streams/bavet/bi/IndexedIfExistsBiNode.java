package org.optaplanner.constraint.streams.bavet.bi;

import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.optaplanner.constraint.streams.bavet.common.AbstractIndexedIfExistsNode;
import org.optaplanner.constraint.streams.bavet.common.ExistsCounter;
import org.optaplanner.constraint.streams.bavet.common.TupleLifecycle;
import org.optaplanner.constraint.streams.bavet.common.index.IndexProperties;
import org.optaplanner.constraint.streams.bavet.common.index.Indexer;
import org.optaplanner.constraint.streams.bavet.uni.UniTuple;
import org.optaplanner.core.api.function.TriPredicate;

final class IndexedIfExistsBiNode<A, B, C> extends AbstractIndexedIfExistsNode<BiTuple<A, B>, C> {

    private final BiFunction<A, B, IndexProperties> mappingAB;
    private final TriPredicate<A, B, C> filtering;

    public IndexedIfExistsBiNode(boolean shouldExist,
            BiFunction<A, B, IndexProperties> mappingAB, Function<C, IndexProperties> mappingC,
            int inputStoreIndexAB, int inputStoreIndexC,
            TupleLifecycle<BiTuple<A, B>> tupleLifecycle,
            Indexer<BiTuple<A, B>, ExistsCounter<BiTuple<A, B>>> indexerAB,
            Indexer<UniTuple<C>, Set<ExistsCounter<BiTuple<A, B>>>> indexerC,
            TriPredicate<A, B, C> filtering) {
        super(shouldExist, mappingC, inputStoreIndexAB, inputStoreIndexC, tupleLifecycle, indexerAB, indexerC,
                filtering != null);
        this.mappingAB = mappingAB;
        this.filtering = filtering;
    }

    @Override
    protected IndexProperties createIndexProperties(BiTuple<A, B> leftTuple) {
        return mappingAB.apply(leftTuple.getFactA(), leftTuple.getFactB());
    }

    @Override
    protected boolean testFiltering(BiTuple<A, B> leftTuple, UniTuple<C> rightTuple) {
        return filtering.test(leftTuple.getFactA(), leftTuple.getFactB(), rightTuple.getFactA());
    }

    @Override
    public String toString() {
        return "IfExistsBiWithUniNode";
    }

}
