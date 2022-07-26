package agenda.controller;


import agenda.dto.ContactoDto;
import agenda.dto.Mensaje;
import agenda.entity.Contacto;
import agenda.security.entity.Usuario;
import agenda.security.jwt.JwtProvider;
import agenda.security.service.UsuarioService;
import agenda.service.ContactoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/contactos")
public class ContactoController {
    @Autowired
    ContactoService contactoService;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    JwtProvider jwtProvider;

    @GetMapping("/listar")
    public ResponseEntity<List<Contacto>> list(@RequestHeader(value="x-token") String token){
        String idUsuario = jwtProvider.getIdUsuarioFromToken(token);
        List<Contacto> list = contactoService.list();
        List<Contacto> contactosUsuario = new ArrayList<>();

        for(Contacto contacto : list){
            if (contacto.getUsuario().getId() ==Integer.valueOf(idUsuario)){
                contactosUsuario.add(contacto);
            }
        }

        return new ResponseEntity(contactosUsuario, HttpStatus.OK);
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity <Contacto> buscar(@PathVariable("id")int id,@RequestHeader(value="x-token") String token){
        String idUsuario = jwtProvider.getIdUsuarioFromToken(token);
        List<Contacto> list = contactoService.list();
        List<Contacto> contactosUsuario = new ArrayList<>();
        Contacto contactoBuscado;
        for(Contacto contacto : list){
            if (contacto.getUsuario().getId() ==Integer.valueOf(idUsuario)){
                contactosUsuario.add(contacto);
            }
        }
        for(Contacto contacto : contactosUsuario){
            if(contacto.getId() == id){
                contactoBuscado = contacto;
                return new ResponseEntity(contactoBuscado, HttpStatus.OK);
            }
        }

        return new ResponseEntity(new Mensaje("El contacto buscado no existe"), HttpStatus.OK);

    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/crear")
    public ResponseEntity<?> crear(@RequestHeader(value="x-token") String token, @RequestBody ContactoDto contactoDto ){
        if(StringUtils.isBlank(contactoDto.getNombre())) {
            return new ResponseEntity(new Mensaje("el nombre es obligatorio"), HttpStatus.BAD_REQUEST);
        }
        if(StringUtils.isBlank(contactoDto.getApellido())) {
            return new ResponseEntity(new Mensaje("el apellido es obligatorio"), HttpStatus.BAD_REQUEST);
        }
        if(StringUtils.isBlank(contactoDto.getEmail())) {
            return new ResponseEntity(new Mensaje("el email es obligatorio"), HttpStatus.BAD_REQUEST);
        }
        if(StringUtils.isBlank(contactoDto.getTelefono())) {
            return new ResponseEntity(new Mensaje("el telefono es obligatorio"), HttpStatus.BAD_REQUEST);
        }

        //Obtenemos el id del usuario que realiza la peticion del token
        String id = jwtProvider.getIdUsuarioFromToken(token);

        //Buscamos los datos del usuario y lo creamos para pasarlo al Contacto
        Usuario usuario = usuarioService.getByIdUSuario(Integer.valueOf(id)).get();


        //Guardamos el contacto en la base de datos
        Contacto contacto = new Contacto(contactoDto.getNombre(), contactoDto.getApellido(),contactoDto.getEmail(), contactoDto.getTelefono(), usuario);
        contactoService.save(contacto);
        return new ResponseEntity(new Mensaje("contacto creado"), HttpStatus.OK);


    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizar(@PathVariable("id")int id, @RequestBody ContactoDto contactoDto){
        if(!contactoService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        if(StringUtils.isBlank(contactoDto.getNombre())) {
            return new ResponseEntity(new Mensaje("el nombre es obligatorio"), HttpStatus.BAD_REQUEST);
        }
        if(StringUtils.isBlank(contactoDto.getApellido())) {
            return new ResponseEntity(new Mensaje("el apellido es obligatorio"), HttpStatus.BAD_REQUEST);
        }
        if(StringUtils.isBlank(contactoDto.getEmail())) {
            return new ResponseEntity(new Mensaje("el email es obligatorio"), HttpStatus.BAD_REQUEST);
        }
        if(StringUtils.isBlank(contactoDto.getTelefono())) {
            return new ResponseEntity(new Mensaje("el telefono es obligatorio"), HttpStatus.BAD_REQUEST);
        }


        Contacto contacto = contactoService.getOne(id).get();
        contacto.setNombre(contactoDto.getNombre());
        contacto.setApellido(contactoDto.getApellido());
        contacto.setEmail(contactoDto.getEmail());
        contacto.setTelefono(contactoDto.getTelefono());
        contactoService.save(contacto);
        return new ResponseEntity(new Mensaje("contacto actualizado"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/borrar/{id}")
    public ResponseEntity<?> delete(@PathVariable("id")int id){
        if(!contactoService.existsById(id)) {
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);

        }
        contactoService.delete(id);
        return new ResponseEntity(new Mensaje("contacto eliminado"), HttpStatus.OK);
    }

}
