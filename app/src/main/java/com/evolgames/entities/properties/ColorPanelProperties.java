package com.evolgames.entities.properties;

import java.util.ArrayList;
import java.util.List;

public class ColorPanelProperties extends Properties {
    List<SquareProperties> squarePropertiesList;
    public ColorPanelProperties(){
        this.squarePropertiesList = new ArrayList<>();
    }

    @Override
    public Properties copy() {
        return null;
    }

    public List<SquareProperties> getSquarePropertiesList() {
        return squarePropertiesList;
    }
}
