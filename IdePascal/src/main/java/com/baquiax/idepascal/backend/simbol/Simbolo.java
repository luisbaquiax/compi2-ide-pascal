package com.baquiax.idepascal.backend.simbol;

public class Simbolo {
    private String id;
    private Tipo tipo; //tipo de dato
    private String tipoPersonalizado;
    private Object value;
    private boolean mutable;

    private String ambito;

    private Categoria categoria;

    @Override
    public String toString() {
        return "Simbolo{" +
                "id='" + id + '\'' +
                ", tipo=" + tipo +
                ", tipoPersonalizado='" + tipoPersonalizado + '\'' +
                ", value=" + value +
                ", mutable=" + mutable +
                ", ambito='" + ambito + '\'' +
                ", categoria=" + categoria +
                '}';
    }

    public Simbolo(String id, Tipo tipo, Object value, boolean mutable) {
        this.id = id;
        this.tipo = tipo;
        this.value = value;
        this.mutable = mutable;
    }

    public Simbolo(String id, String tipoPersonalizado, Object value, boolean mutable){
        this.id = id;
        this.tipoPersonalizado  = tipoPersonalizado;
        this.value = value;
    }

    public String getAmbito() {
        return ambito;
    }

    public void setAmbito(String ambito) {
        this.ambito = ambito;
    }

    public String getTipoPersonalizado() {
        return tipoPersonalizado;
    }

    public void setTipoPersonalizado(String tipoPersonalizado) {
        this.tipoPersonalizado = tipoPersonalizado;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isMutable() {
        return mutable;
    }

    public void setMutable(boolean mutable) {
        this.mutable = mutable;
    }
}
