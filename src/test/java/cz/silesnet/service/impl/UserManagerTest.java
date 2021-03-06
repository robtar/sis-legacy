package cz.silesnet.service.impl;

import cz.silesnet.model.User;
import cz.silesnet.service.UserManager;
import org.testng.annotations.Test;
import org.unitils.spring.annotation.SpringBean;

import java.util.List;

/**
 * User Manager tests. Testing Loggin in.
 *
 * @author Richard Sikora
 */
public class UserManagerTest extends BaseServiceTestCase {

  @SpringBean("userManager")
  private UserManager umgr = null;

  @Test
  public void testGetUsers() {
    List<User> users = umgr.getUsers();

    for (User u : users)
      log.debug(u);
  }

}