package cz.silesnet.event.consumer;

import cz.silesnet.event.EventConsumer;
import cz.silesnet.event.Event;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * User: der3k
 * Date: 5.2.12
 * Time: 18:59
 */
public class ServiceEventConsumer implements EventConsumer {

    private final Log log = LogFactory.getLog(getClass());

    public void consume(final Event event) {
        log.info("consumed " + event.toString());
    }
}
