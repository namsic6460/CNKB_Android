package lkd.namsic.game.base;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class ConcurrentArrayList<T> implements List<T>, Serializable {

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final List<T> list;

    public ConcurrentArrayList() {
        this.list = new ArrayList<>();
    }

    @Override
    public int size() {
        readWriteLock.readLock().lock();

        try {
            return list.size();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public boolean isEmpty() {
        readWriteLock.readLock().lock();

        try {
            return list.isEmpty();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public boolean contains(@Nullable Object o) {
        readWriteLock.readLock().lock();

        try {
            return list.contains(o);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @NonNull
    @Override
    public Iterator<T> iterator() {
        readWriteLock.readLock().lock();

        try {
            return new ArrayList<>(list).iterator();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @NonNull
    @Override
    public Object[] toArray() {
        readWriteLock.readLock().lock();

        try {
            return list.toArray();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @SuppressWarnings("SuspiciousToArrayCall")
    @NonNull
    @Override
    public <T1> T1[] toArray(@NonNull T1[] a) {
        readWriteLock.readLock().lock();

        try {
            return list.toArray(a);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public boolean add(T t) {
        readWriteLock.writeLock().lock();

        try {
            return list.add(t);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public void add(int index, T element) {
        readWriteLock.writeLock().lock();

        try {
            list.add(index, element);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public boolean remove(@Nullable Object o) {
        readWriteLock.writeLock().lock();

        try {
            return list.remove(o);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public T remove(int index) {
        readWriteLock.writeLock().lock();

        try {
            return list.remove(index);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public boolean containsAll(@NonNull Collection<?> c) {
        readWriteLock.readLock().lock();

        try {
            return list.containsAll(c);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public boolean addAll(@NonNull Collection<? extends T> c) {
        readWriteLock.writeLock().lock();

        try {
            return list.addAll(c);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public boolean addAll(int index, @NonNull Collection<? extends T> c) {
        readWriteLock.writeLock().lock();

        try {
            return list.addAll(index, c);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public boolean removeAll(@NonNull Collection<?> c) {
        readWriteLock.writeLock().lock();

        try {
            return list.removeAll(c);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public boolean retainAll(@NonNull Collection<?> c) {
        readWriteLock.readLock().lock();

        try {
            return list.retainAll(c);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public void clear() {
        readWriteLock.writeLock().lock();

        try {
            list.clear();
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public T get(int index) {
        readWriteLock.readLock().lock();

        try {
            return list.get(index);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public T set(int index, T element) {
        readWriteLock.writeLock().lock();

        try {
            return list.set(index, element);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public int indexOf(@Nullable Object o) {
        readWriteLock.readLock().lock();

        try {
            return list.indexOf(o);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public int lastIndexOf(@Nullable Object o) {
        readWriteLock.readLock().lock();

        try {
            return list.lastIndexOf(o);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @NonNull
    @Override
    public ListIterator<T> listIterator() {
        readWriteLock.readLock().lock();

        try {
            return new ArrayList<>(list).listIterator();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @NonNull
    @Override
    public ListIterator<T> listIterator(int index) {
        readWriteLock.readLock().lock();

        try {
            return new ArrayList<>(list).listIterator(index);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @NonNull
    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        readWriteLock.readLock().lock();

        try {
            return new ArrayList<>(list).subList(fromIndex, toIndex);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public void replaceAll(@NonNull UnaryOperator<T> operator) {
        readWriteLock.writeLock().lock();

        try {
            list.replaceAll(operator);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public void sort(@Nullable Comparator<? super T> c) {
        readWriteLock.writeLock().lock();

        try {
            list.sort(c);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @NonNull
    @Override
    public Spliterator<T> spliterator() {
        readWriteLock.readLock().lock();

        try {
            return new ArrayList<>(list).spliterator();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public boolean removeIf(@NonNull Predicate<? super T> filter) {
        readWriteLock.writeLock().lock();

        try {
            return list.removeIf(filter);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @NonNull
    @Override
    public Stream<T> stream() {
        readWriteLock.readLock().lock();

        try {
            return new ArrayList<>(list).stream();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @NonNull
    @Override
    public Stream<T> parallelStream() {
        readWriteLock.readLock().lock();

        try {
            return new ArrayList<>(list).parallelStream();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

}
