package io.flowing.retail.vgr.persistence;

import io.flowing.retail.vgr.domain.Vgr;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;


@Component
public interface VgrRepository extends CrudRepository<Vgr, String> {

}
