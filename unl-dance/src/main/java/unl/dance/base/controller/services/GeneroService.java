package unl.dance.base.controller.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;

import jakarta.validation.constraints.NotEmpty;
import unl.dance.base.controller.dao.dao_models.DaoGenero;
import unl.dance.base.models.Genero;



@BrowserCallable
@Transactional(propagation = Propagation.REQUIRES_NEW)
@AnonymousAllowed

public class GeneroService {
    private DaoGenero da;
    public GeneroService() {
        da = new DaoGenero();
    }

    public void createGenero(@NotEmpty String nombre) throws Exception{
        da.getObj().setNombre(nombre);
        if(!da.save())
            throw new  Exception("No se pudo guardar los datos de genero");
    }

    public void updateGenero(@NotEmpty Integer id, @NotEmpty String nombre) throws Exception{
        da.setObj(da.listAll().get(id));
        da.getObj().setNombre(nombre);
        if(!da.update(id))
            throw new  Exception("No se pudo modificar los datos de artista");
    }

    public List<Genero> list(Pageable pageable) {        
        return Arrays.asList(da.listAll().toArray());
    }
    public List<Genero> listAll() {  
       // System.out.println("**********Entro aqui");  
        //System.out.println("lengthy "+Arrays.asList(da.listAll().toArray()).size());    
        return (List<Genero>)Arrays.asList(da.listAll().toArray());
    }

    
    
}
