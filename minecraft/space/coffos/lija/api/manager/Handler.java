package space.coffos.lija.api.manager;

import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Handler<T> {

    private CopyOnWriteArrayList<T> contents = new CopyOnWriteArrayList<>();

    public CopyOnWriteArrayList<T> getContents() {
        return contents;
    }

    public void setContents(CopyOnWriteArrayList<T> contents) {
        this.contents = contents;
    }
}