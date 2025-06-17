package unl.dance.base.controller.dao.dao_models;

import unl.dance.base.controller.dao.AdapterDao;
import unl.dance.base.models.Genero;

public class DaoGenero extends AdapterDao<Genero> {
    private Genero obj;

    public DaoGenero() {
        super(Genero.class);
        // TODO Auto-generated constructor stub
    }

    public Genero getObj() {
        if (obj == null)
            this.obj = new Genero();
        return this.obj;
    }

    public void setObj(Genero obj) {
        this.obj = obj;
    }

    public Boolean save() {
        try {
            obj.setId(listAll().getLength()+1);
            this.persist(obj);
            return true;
        } catch (Exception e) {
            //TODO
            return false;
            // TODO: handle exception
        }
    }

    public Boolean update(Integer pos) {
        try {
            this.update(obj, pos);
            return true;
        } catch (Exception e) {
            //TODO
            return false;
            // TODO: handle exception
        }
    }

    public static void main(String[] args) {
        DaoGenero da = new DaoGenero();
        da.getObj().setId(da.listAll().getLength() + 1);
        da.getObj().setNombre("Masculino");
        if (da.save())
            System.out.println("GUARDADO");
        else
            System.out.println("Hubo un error");
        
    }

}