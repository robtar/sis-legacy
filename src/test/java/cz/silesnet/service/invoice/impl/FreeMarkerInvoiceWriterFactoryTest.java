package cz.silesnet.service.invoice.impl;

import cz.silesnet.model.Bill;
import cz.silesnet.service.invoice.InvoiceFormat;
import cz.silesnet.service.invoice.InvoiceWriter;
import org.testng.annotations.Test;
import org.unitils.UnitilsTestNG;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBean;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

/**
 * User: der3k
 * Date: 21.10.2010
 * Time: 19:50:50
 */
@SpringApplicationContext({"context/sis-template.xml"})
@Test(groups = "integration")
public class FreeMarkerInvoiceWriterFactoryTest extends UnitilsTestNG {

  private static final String INVOICE_NUMBER = "Invoice number: 12345";
  private static final String INVOICE = "Invoice number: " + INVOICE_NUMBER;

  @SpringBean("invoiceWriterFactory")
  private FreeMarkerInvoiceWriterFactory factory;


  @Test
  public void testInstanceOf() throws Exception {
    Bill bill = mock(Bill.class);
    when(bill.getNumber()).thenReturn(INVOICE_NUMBER);
    InvoiceWriter writer = factory.instanceOf(bill, InvoiceFormat.LINK);
    StringWriter invoiceWriter = new StringWriter();
    writer.write(new PrintWriter(invoiceWriter));
    String invoice = invoiceWriter.toString();
    assertThat(invoice, is(INVOICE));
  }
}
