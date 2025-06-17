package unl.dance.base.controller.services;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;

import io.micrometer.common.lang.NonNull;
import jakarta.validation.constraints.NotEmpty;
import unl.dance.base.controller.dao.dao_models.DaoAlbum;
import unl.dance.base.models.Album;



@BrowserCallable
@Transactional(propagation = Propagation.REQUIRES_NEW)
@AnonymousAllowed

public class AlbumService {
    private DaoAlbum da;
    public AlbumService() {
        da = new DaoAlbum();
    }

    public void createAlbum(@NotEmpty String nombre, @NonNull Date fecha) throws Exception{
        da.getObj().setNombre(nombre);
        da.getObj().setFecha(fecha);
        if(!da.save())
            throw new  Exception("No se pudo guardar los datos del Album");
    }

    public void updateAlbum(@NotEmpty Integer id, @NotEmpty String nombre, @NonNull Date fecha) throws Exception{
        da.setObj(da.listAll().get(id));
        da.getObj().setNombre(nombre);
        da.getObj().setFecha(fecha);
        
        if(!da.update(id))
            throw new  Exception("No se pudo modificar los datos del Album");
    }

    public List<Album> list(Pageable pageable) {        
        return Arrays.asList(da.listAll().toArray());
    }
    public List<Album> listAll() {  
       // System.out.println("**********Entro aqui");  
        //System.out.println("lengthy "+Arrays.asList(da.listAll().toArray()).size());    
        return (List<Album>)Arrays.asList(da.listAll().toArray());
    }

    
}