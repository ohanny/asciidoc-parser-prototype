package fr.icodem.asciidoc.parser;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class HandlerNotificationsEmitter {
    // threading purpose
    private BlockingQueue<ActionRequest> tasks = new ArrayBlockingQueue<>(1024);
    private Semaphore semaphore = new Semaphore(0);
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private AtomicBoolean parsingComplete = new AtomicBoolean();
    private CyclicBarrier barrier = new CyclicBarrier(2);

    public HandlerNotificationsEmitter() {
        Runnable actionTask = () -> {
            try {
                // process ready tasks
                while (!parsingComplete.get()) {
                    semaphore.acquire();
                    processReadyRequests();
                }

                // parsing is finished, do a last check
                processReadyRequests();

                // notify arrival to barrier
                barrier.await();
            } catch (BrokenBarrierException | InterruptedException e) {
                e.printStackTrace();
            }

        };
        executorService.execute(actionTask);

    }

    private void processReadyRequests() throws InterruptedException {
        ActionRequest ar = tasks.peek();
        while (ar != null && ar.isReady()) {
            tasks.take().getAction().run();
            ar = tasks.peek();
        }
    }

    public void addActionRequest(ActionRequest.ActionRequestType type,
                                  Runnable runnable) {
        addActionRequest(type, runnable, true);
    }

    public void addActionRequest(ActionRequest.ActionRequestType type,
                                  Runnable runnable, boolean ready) {
        try {
            ActionRequest req = new ActionRequest(type, runnable, ready);
            tasks.put(req);
            // unlock outputter thread if locked
            if (semaphore.getQueueLength() == 1) {
                ActionRequest ar = tasks.peek();
                if (ar != null && ar.isReady()) {
                    semaphore.release();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void parsingComplete() {
        // mark end of parsing
        parsingComplete.set(true);

        // end of parsing, mark every action ready
        tasks.stream().filter(ar -> !ar.isReady()).forEach(ar -> ar.ready());

        // unlock outputter thread if locked
        if (semaphore.getQueueLength() == 1) {
            semaphore.release();
        }

        // notify arrival to barrier (wait for writer thread to finish)
        try {
            barrier.await();
        } catch (BrokenBarrierException  | InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();

    }

    public void markFirstReady(ActionRequest.ActionRequestType type, Runnable runnable) {
        tasks.stream()
                .filter(ar -> ar.getType().equals(type))
                .findFirst()
                .ifPresent(ar -> ar.ready(runnable));
    }

    public void releaseListener() {
        // unlock outputter thread if locked
        if (semaphore.getQueueLength() == 1) {
            ActionRequest ar = tasks.peek();
            if (ar != null && ar.isReady()) {
                semaphore.release();
            }
        }
    }

}
