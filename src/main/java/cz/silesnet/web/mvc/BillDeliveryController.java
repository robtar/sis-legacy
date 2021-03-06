package cz.silesnet.web.mvc;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controller handling customer billFor delivery confirmations. Only wraps
 * BillingControler and delegates to its confirmDelivery().
 *
 * @author Richard Sikora
 */
public class BillDeliveryController extends AbstractController {
  private BillingController bCtrl;

  public void setBillingController(BillingController bCtrl) {
    this.bCtrl = bCtrl;
  }

  protected ModelAndView handleRequestInternal(HttpServletRequest request,
                                               HttpServletResponse response) throws Exception {
    return bCtrl.confirmDelivery(request, response);
  }

}
