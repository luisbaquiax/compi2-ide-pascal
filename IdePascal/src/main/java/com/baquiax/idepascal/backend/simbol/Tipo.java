package com.baquiax.idepascal.backend.simbol;

public class Tipo {
    private DataType dataType;//tipo de dato entero, real, ... ID

    private DataType typeBase;//la base de tipo de dato
    private String id;//nombre del tipo de dato
    private int dimension;
    private int indiceMinimo;
    private int indiceMaximo;
    private String ambito;

    @Override
    public String toString() {
        return "Tipo{" +
                "dataType=" + dataType +
                ", typeBase=" + typeBase +
                ", id='" + id + '\'' +
                ", dimension=" + dimension +
                ", indiceMinimo=" + indiceMinimo +
                ", indiceMaximo=" + indiceMaximo +
                ", ambito='" + ambito + '\'' +
                '}';
    }

    public Tipo() {
    }

    public Tipo(DataType dataType) {
        this.dataType = dataType;
        this.typeBase = dataType;
        switch (dataType) {
            case ENTERO, REAL, BOOLEAN, CARACTER, CADENA -> {
                this.id = dataType.nombre;
            }
        }
    }

    public Tipo(String id, DataType dataType) {
        this.id = id;
        this.dataType = dataType;
    }

    /**
     * @param id
     * @param dataType
     * @param indiceMinimo
     * @param indiceMaximo
     */
    public Tipo(String id, DataType dataType, int indiceMinimo, int indiceMaximo) {
        this.dataType = dataType;
        this.id = id;
        this.indiceMinimo = indiceMinimo;
        this.indiceMaximo = indiceMaximo;
    }

    /**
     * @param id
     * @param dataType
     * @param typeBase
     * @param indiceMinimo
     * @param indiceMaximo
     */
    public Tipo(String id, DataType dataType, DataType typeBase, int indiceMinimo, int indiceMaximo) {
        this.id = id;
        this.dataType = dataType;
        this.typeBase = typeBase;
        this.indiceMinimo = indiceMinimo;
        this.indiceMaximo = indiceMaximo;
    }

    public DataType getTypeBase() {
        return typeBase;
    }

    public void setTypeBase(DataType typeBase) {
        this.typeBase = typeBase;
    }

    public int getDimension() {
        return dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    public int getIndiceMinimo() {
        return indiceMinimo;
    }

    public void setIndiceMinimo(int indiceMinimo) {
        this.indiceMinimo = indiceMinimo;
    }

    public int getIndiceMaximo() {
        return indiceMaximo;
    }

    public void setIndiceMaximo(int indiceMaximo) {
        this.indiceMaximo = indiceMaximo;
    }

    public String getAmbito() {
        return ambito;
    }

    public void setAmbito(String ambito) {
        this.ambito = ambito;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }
}
