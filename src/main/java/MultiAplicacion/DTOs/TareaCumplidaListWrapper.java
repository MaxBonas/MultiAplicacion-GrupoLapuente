package MultiAplicacion.DTOs;

import MultiAplicacion.entities.TareaCumplida;

import java.util.List;

public class TareaCumplidaListWrapper {
    private List<TareaCumplida> tareasCumplidas;

    public List<TareaCumplida> getTareasCumplidas() {
        return tareasCumplidas;
    }

    public void setTareasCumplidas(List<TareaCumplida> tareasCumplidas) {
        this.tareasCumplidas = tareasCumplidas;
    }
}
