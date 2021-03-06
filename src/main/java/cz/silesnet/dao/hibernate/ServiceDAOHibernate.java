package cz.silesnet.dao.hibernate;

import cz.silesnet.dao.ServiceDAO;
import cz.silesnet.model.Service;
import cz.silesnet.model.ServiceBlueprint;
import cz.silesnet.model.enums.Country;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.classic.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.*;

/**
 * Concrete implementation of ServiceDAO using Hibernate.
 *
 * @author Richard Sikora
 */
public class ServiceDAOHibernate extends HibernateDaoSupport implements ServiceDAO {

    // ~ Methods
    // ----------------------------------------------------------------

    public List<Service> getAllOrphans() {
        return getHibernateTemplate().find(
                "from cz.silesnet.model.Service as s where not exists (from cz.silesnet.model.Customer as c where c.id = s.customerId)");
    }

    public Service get(Long serviceId) {
        return (Service) getHibernateTemplate().get(Service.class, serviceId);
    }

    public void remove(Service service) {
        getHibernateTemplate().delete(service);
    }

    public void save(Service service) {
        getHibernateTemplate().saveOrUpdate(service);
    }

    public Map<String, Long> calculateSummaryFor(Country c) {
        Map<String, Long> sum = new LinkedHashMap<String, Long>();
        Session session = getSessionFactory().getCurrentSession();
        // count service related statistics
        SQLQuery query = session.createSQLQuery("select sum(case\n" +
                "  when s.bps = 'M' then s.download * 1000\n" +
                "  when s.bps = 'k' then s.download\n" +
                "  else 0\n" +
                "end) as download,\n" +
                "sum(case\n" +
                "  when s.bps = 'M' then s.upload * 1000\n" +
                "  when s.bps = 'k' then s.upload\n" +
                "  else 0\n" +
                "end) as upload,\n" +
                "sum(s.price) as price\n" +
                "from services as s, customers c\n" +
                "where c.id=s.customer_id\n" +
                "and c.country = " + c.getId() + "\n" +
                "and c.is_active")
                .addScalar("download", Hibernate.LONG)
                .addScalar("upload", Hibernate.LONG)
                .addScalar("price", Hibernate.LONG);
        Object[] result = (Object[]) query.uniqueResult();
        sum.put("overviewCustomers.totalDownload", (Long) result[0] / 1000);
        sum.put("overviewCustomers.totalUpload", (Long) result[1] / 1000);
        sum.put("overviewCustomers.totalPrice.CZK", (Long) result[2]);

        // count customers
        query = session.createSQLQuery("select count(id) as customers_count\n" +
                "from customers\n" +
                "where country = " + c.getId() + "\n" +
                "and is_active")
                .addScalar("customers_count", Hibernate.LONG);
        sum.put("overviewCustomers.totalCustomers", (Long) query.uniqueResult());

        return sum;
    }

    public void evict(Service service) {
        getHibernateTemplate().evict(service);
    }

    public ServiceBlueprint findBlueprint(Integer blueprintId) {
        return getHibernateTemplate().get(ServiceBlueprint.class, blueprintId);
    }

    public void saveBlueprint(ServiceBlueprint blueprint) {
        getHibernateTemplate().saveOrUpdate(blueprint);
    }

    public Long findMaxIdInRange(long min, long max) {
        Session session = getSessionFactory().getCurrentSession();
        final SQLQuery query = session.createSQLQuery("select max(id) as max_id" +
                " from services where " + min + " <= id and id <= " + max).addScalar("max_id", Hibernate.LONG);
        return (Long) query.uniqueResult();
    }

    public List<Service> getByExample(Service s) {
        if (s == null)
            return null;
        if (s.getContract() != null) {
            final List<Service> services = getHibernateTemplate().find("from Service as s where s.contract = ?", s.getContract());
            final HashMap<Long, Service> unique = new HashMap<Long, Service>();
            for (Service service : services)
                unique.put(service.getCustomerId(), service);
            final List<Service> result = new ArrayList<Service>();
            result.addAll(unique.values());
            return result;
        }
        if (s.getPeriod() != null && s.getPeriod().getTo() != null) {
            return getHibernateTemplate().find("from Service as s where s.period.to <= ?", s.getPeriod().getTo());
        }
        return new ArrayList<Service>(0);
    }

}