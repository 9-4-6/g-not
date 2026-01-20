package com.example.gnote.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Lock;

public class CustomLock implements Lock {
    // 自定义AQS同步器（独占模式）
    private final Sync sync = new Sync();

    // 内部类：重写AQS的tryAcquire和tryRelease
    private static class Sync extends AbstractQueuedSynchronizer {
        // 尝试获取独占锁（state=0表示未锁定，1表示锁定）
        @Override
        protected boolean tryAcquire(int arg) {
            if (compareAndSetState(0, 1)) {
                // 设置当前线程为独占线程
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        // 尝试释放独占锁
        @Override
        protected boolean tryRelease(int arg) {
            if (getState() == 0) {
                throw new IllegalMonitorStateException();
            }
            // 清空独占线程
            setExclusiveOwnerThread(null);
            // 重置state为0
            setState(0);
            return true;
        }

        // 判断是否持有锁
        protected boolean isLocked() {
            return getState() != 0;
        }
    }

    // 实现Lock接口的方法
    @Override
    public void lock() {
        sync.acquire(1); // AQS的acquire方法（会调用tryAcquire）
    }

    @Override
    public void unlock() {
        sync.release(1); // AQS的release方法（会调用tryRelease）
    }

    // 其他Lock方法（简化，仅实现核心）
    @Override public void lockInterruptibly() throws InterruptedException { sync.acquireInterruptibly(1); }
    @Override public boolean tryLock() { return sync.tryAcquire(1); }
    @Override public boolean tryLock(long time, TimeUnit unit) throws InterruptedException { return sync.tryAcquireNanos(1, unit.toNanos(time)); }
    @Override public java.util.concurrent.locks.Condition newCondition() { return sync.new ConditionObject(); }
}
