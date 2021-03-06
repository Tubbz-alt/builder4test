package uk.co.caeldev.builder4test;

import uk.co.caeldev.builder4test.resolvers.Resolver;
import uk.co.caeldev.builder4test.resolvers.SupplierResolver;
import uk.co.caeldev.builder4test.resolvers.ValueResolver;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class EntityBuilder<K> implements ApplyField<EntityBuilder<K>> {

    private final Function<LookUp, K> creator;
    private final LookUp lookUp;

    private EntityBuilder(Function<LookUp, K> creator) {
        this.creator = creator;
        this.lookUp = new DefaultLookUp();
    }

    private EntityBuilder(Function<LookUp, K> creator, Map<Field, Resolver> fields) {
        this.creator = creator;
        this.lookUp = new DefaultLookUp(fields);
    }

    private EntityBuilder(Function<LookUp, K> creator, LookUp lookUp) {
        this.creator = creator;
        this.lookUp = lookUp;
    }

    protected static <T> EntityBuilder<T> entityBuilder(Function<LookUp, T> creator, Map<Field, Resolver> fields) {
        return new EntityBuilder<>(creator, fields);
    }

    protected static <T> EntityBuilder<T> entityBuilder(Function<LookUp, T> creator) {
        return new EntityBuilder<>(creator);
    }

    protected static <T> EntityBuilder<T> entityBuilder(Function<LookUp, T> creator, LookUp lookUp) {
        return new EntityBuilder<>(creator, lookUp);
    }

    @Override
    public <V> EntityBuilder<K> applySupplier(Field<V> field, Supplier<V> value) {
        lookUp.put(field, new SupplierResolver<>(value));
        return this;
    }

    @Override
    public <U> EntityBuilder<K> applyValue(Field<U> field, U value) {
        lookUp.put(field, new ValueResolver<>(value));
        return this;
    }

    @Override
    public <V> EntityBuilder<K> applyCreator(Field<V> field, Function<LookUp, V> creator) {
        return applySupplier(field, () -> creator.apply(lookUp));
    }

    public <V> EntityBuilder<K> nullify(Field<V> field) {
        lookUp.put(field, new SupplierResolver<>(() -> null));
        return this;
    }

    public K get() {
        return creator.apply(lookUp);
    }
}
