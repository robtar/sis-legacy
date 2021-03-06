package cz.silesnet.dao;

/**
 * Created by IntelliJ IDEA.
 * User: sikorric
 * Date: May 17, 2010
 * Time: 2:05:37 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GenericDao<E> {
  E find(long id);

  void store(E entity);

  void remove(E entity);
}
