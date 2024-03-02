package com.evolgames.entities.properties;

import java.util.ArrayList;
import java.util.List;

public class ColorPanelProperties extends Properties {
    List<SquareProperties> squarePropertiesList;
    public ColorPanelProperties(){
        this.squarePropertiesList = new ArrayList<>();
    }

    public List<SquareProperties> getSquarePropertiesList() {
        return squarePropertiesList;
    }
}
