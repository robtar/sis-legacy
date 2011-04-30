package cz.silesnet.service;

import cz.silesnet.model.Bill;
import cz.silesnet.model.Customer;
import cz.silesnet.model.Invoicing;
import cz.silesnet.model.enums.Country;
import org.apache.commons.lang.mutable.MutableInt;
import org.springframework.mail.MailException;

import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

/**
 * Interface defining operations needed for automatic billing.
 *
 * @author Richard Sikora
 */
public interface BillingManager {

  // ~ Methods
  // ----------------------------------------------------------------

  public Bill get(Long billId);

  public List<Bill> getByStatus(Invoicing invoicing, Boolean isConfirmed,
                                Boolean isSent, Boolean isDelivered, Boolean isArchived);

  public List<Bill> getByCustomer(Customer c);

  public List<Bill> getBySentMail(Invoicing invoicing);

  public List<Bill> getByNumber(String billNumber);

  public List<Bill> getByExample(Bill bill);

  public Customer fetchCustomer(Bill bill);

  public void insert(Bill bill);

  public void insertAll(List<Bill> bills);

  public void delete(Bill bill);

  public void update(Bill bill);

  /**
   * Generate bills that are to be issued due to given date. Bills are not
   * persisted, they need to be confirmed first.
   *
   * @param due       due date.
   * @param numbering
   * @return list of bills that are applicable due given date. Bill includes
   *         periodical and one-time services as billFor items.
   */
  public List<Bill> generateAll(Invoicing invoicing,
                                List<Customer> customers, Date due, String numbering);


  public Bill generate(Customer customer, Date due, String number);

  /**
   * Persist, send and export given bills.
   */
  public Bill send(Bill bill, MutableInt emailedCounter);

  public void reSendAll(Invoicing invoicing, List<Bill> bills);

  public void email(Bill bill) throws MailException;

  public void emailAll(List<Bill> bills);

  /**
   * Confirms delivery of billFor acording to unique billFor hashcode. Hashcode is
   * part of link sent by email to customer.
   *
   * @param uuid unique hashcode of given billFor.
   * @return confirmed Bill instance, null otherwise.
   */
  public Bill confirmDelivery(String uuid);

  public void sendNextInvoice();

  public boolean getSendingEnabled(Country country);

  public void setSendingEnabled(boolean status, Country country);

  public int getCountByStatus(Invoicing invoicing, Boolean isConfirmed,
                              Boolean isSent, Boolean isDelivered, Boolean isArchived,
                              Boolean isSnail);

  public void invoice(Invoicing invoicing);

  public List<Bill> getInvoices(Invoicing invoicing);

  public List<Bill> getUndeliveredInvoices(Invoicing invoicing);

  public List<Bill> getDeliveredInvoices(Invoicing invoicing);

  public List<Bill> getForPrintingInvoices(Invoicing invoicing);

  public int getUndeliveredInvoicesCount(Invoicing invoicing);

  public int getDeliveredInvoicesCount(Invoicing invoicing);

  public int getForPrintingInvoicesCount(Invoicing invoicing);

  public List<Invoicing> getInvoicings(Country country);

  public Invoicing getInvoicing(Long id);

  public void insertInvoicing(Invoicing invoicing);

  public void updateInvoicing(Invoicing invoicing);

  public void deleteInvoicing(Invoicing invoicing);

  public int getInvoicingSum(Invoicing invoicing);

  public void exportAllToInsert(Invoicing invoicing, List<Bill> bills,
                                PrintWriter writer);

  public void billCustomersIn(Invoicing invoicing);
}