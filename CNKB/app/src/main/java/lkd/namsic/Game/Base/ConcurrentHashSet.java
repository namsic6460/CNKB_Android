package lkd.namsic.Game.Base;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ConcurrentHashSet<T> implements Set<T> {

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Set<T> set;

    public ConcurrentHashSet() {
        this.set = new HashSet<>();
    }

    public ConcurrentHashSet(Set<T> set) {
        this.set = set;
    }

    @Override
    public int size() {
        readWriteLock.readLock().lock();

        try {
            return set.size();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public boolean isEmpty() {
        readWriteLock.readLock().lock();

        try {
            return set.isEmpty();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public boolean contains(@Nullable Object o) {
        readWriteLock.readLock().lock();

        try {
            return set.contains(o);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @NonNull
    @Override
    public Iterator<T> iterator() {
        readWriteLock.readLock().lock();

        try {
            return new HashSet<>(set).iterator();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @NonNull
    @Override
    public Object[] toArray() {
        readWriteLock.readLock().lock();

        try {
            return set.toArray();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @NonNull
    @Override
    public <T1> T1[] toArray(@NonNull T1[] a) {
        readWriteLock.readLock().lock();

        try {
            return set.toArray(a);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public boolean add(T t) {
        readWriteLock.writeLock().lock();

        try {
            return set.add(t);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public boolean remove(@Nullable Object o) {
        readWriteLock.writeLock().lock();

        try {
            return set.remove(o);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public boolean containsAll(@NonNull Collection<?> c) {
        readWriteLock.readLock().lock();

        try {
            return set.containsAll(c);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public boolean addAll(@NonNull Collection<? extends T> c) {
        readWriteLock.writeLock().lock();

        try {
            return set.addAll(c);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public boolean removeAll(@NonNull Collection<?> c) {
        readWriteLock.writeLock().lock();

        try {
            return set.removeAll(c);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public boolean retainAll(@NonNull Collection<?> c) {
        readWriteLock.readLock().lock();

        try {
            return set.retainAll(c);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public void clear() {
        readWriteLock.writeLock().lock();

        try {
            set.clear();
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @NonNull
    @Override
    public Spliterator<T> spliterator() {
        readWriteLock.readLock().lock();

        try {
            return new HashSet<>(set).spliterator();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public boolean removeIf(@NonNull Predicate<? super T> filter) {
        readWriteLock.writeLock().lock();

        try {
            return set.removeIf(filter);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @NonNull
    @Override
    public Stream<T> stream() {
        readWriteLock.readLock().lock();

        try {
            return new HashSet<>(set).stream();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @NonNull
    @Override
    public Stream<T> parallelStream() {
        readWriteLock.readLock().lock();

        try {
            return new HashSet<>(set).parallelStream();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }
    
}
