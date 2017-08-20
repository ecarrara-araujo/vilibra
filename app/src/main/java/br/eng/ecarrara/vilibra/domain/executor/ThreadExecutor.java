package br.eng.ecarrara.vilibra.domain.executor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * {@link Executor} implementation based on a ThreadPoolExecutor.
 *
 * This implementation is based on the reference implementation of Pedro Vicente
 * on https://github.com/pedrovgs/EffectiveAndroidUI/blob/master/app/src/main/java/com/github/pedrovgs/effectiveandroidui/executor/ThreadExecutor.java
 */
public class ThreadExecutor implements Executor {

    private static final int CORE_POOL_SIZE = 3;
    private static final int MAX_POOL_SIZE = 5;
    private static final int KEEP_ALIVE_TIME = 120;
    private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;
    private static final BlockingQueue<Runnable> WORK_QUEUE = new LinkedBlockingQueue<Runnable>();

    private ThreadPoolExecutor threadPoolExecutor;

    @Inject
    public ThreadExecutor() {
        int corePoolSize = CORE_POOL_SIZE;
        int maxPoolSize = MAX_POOL_SIZE;
        int keepAliveTime = KEEP_ALIVE_TIME;
        TimeUnit timeUnit = TIME_UNIT;
        BlockingQueue<Runnable> workQueue = WORK_QUEUE;
        threadPoolExecutor = new ThreadPoolExecutor(
                corePoolSize,
                maxPoolSize,
                keepAliveTime,
                timeUnit,
                workQueue);
    }

    @Override public void execute(final Interactor interactor) {
        if (interactor == null) {
            throw new IllegalArgumentException("Interactor to execute can't be null");
        }
        threadPoolExecutor.submit(new Runnable() {
            @Override public void run() {
                interactor.operation();
            }
        });
    }
}
