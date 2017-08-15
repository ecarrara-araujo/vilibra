package br.eng.ecarrara.vilibra.model;

import java.util.ArrayList;
import java.util.List;

public class BookVolumeCollection {

    private List<BookVolume> items = new ArrayList<BookVolume>();

    public BookVolumeCollection() {
        this.items = new ArrayList<>();
    }

    public List<BookVolume> getItems() {
        return items;
    }

    public void setItems(List<BookVolume> items) {
        this.items = items;
    }
}
