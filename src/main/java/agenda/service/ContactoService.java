package agenda.service;

import agenda.entity.Contacto;
import agenda.repository.ContactoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ContactoService {
    @Autowired
    ContactoRepository contactoRepository;

    public List<Contacto> list(){
        return  contactoRepository.findAll();
    }


    public Optional<Contacto> getOne(int id){
        return contactoRepository.findById(id);
    }

    public void save(Contacto contacto){
        contactoRepository.save(contacto);
    }

    public void delete(int id){
        contactoRepository.deleteById(id);
    }

    public boolean existsById(int id){
        return contactoRepository.existsById(id);
    }



}
