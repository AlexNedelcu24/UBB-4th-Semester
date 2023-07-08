package persistence;


public interface ICrudRepository<ID, E >{

    /*Competitor*/
    E save(E entity);

    E delete(E entity);

    /**/
    Iterable<E> findAll();

    /*challenge*/
    E findOne(ID id);
}
