package hasoffer.adp.api.listener;

import hasoffer.adp.base.utils.TimeUtils;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.concurrent.atomic.AtomicLong;

public class ServerSessionListener implements HttpSessionListener {

    private final static int SESSION_DISPLAY_COUNT = 100;

    private AtomicLong curCount = new AtomicLong(0);

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        httpSessionEvent.getSession().setMaxInactiveInterval(20);
        curCount.addAndGet(1);
        show();
    }

    private void show() {
        long count = curCount.get();
        if (count % SESSION_DISPLAY_COUNT == 0) {
            System.out.println(String.format("[%s] - current session count : %d.", TimeUtils.now(), count));
        }
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        curCount.addAndGet(-1);
        show();
    }
}
