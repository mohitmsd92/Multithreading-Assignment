package Multi_Threading;
public class Q5 {


    public static void main(String[] args) throws InterruptedException {
        Q5 reentrantLockExample = new Q5();
        Account account1 = new Account("1001", 10000);
        Account account2 = new Account("2001", 20000);
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                reentrantLockExample.lock(account1, account2);
                reentrantLockExample.transfer(account1, account2, 100);
                reentrantLockExample.unLock(account1, account2);
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 500; i++) {
                reentrantLockExample.lock(account2, account1);
                reentrantLockExample.transfer(account2, account1, 100);
                reentrantLockExample.unLock(account2, account1);
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("Total balance for both account: " + (account1.getAmount() + account2.getAmount()));
    }

    private void transfer(Account fromAccount, Account toAccount, Integer amount) {
        System.out.println("Transferring amount: " + amount + " from account: " + fromAccount.getAccountNumber() + " to account: " + toAccount.getAccountNumber());
        toAccount.setAmount(toAccount.getAmount() - amount);
        fromAccount.setAmount(fromAccount.getAmount() + amount);
    }

    private void lock(Account fromAccount, Account toAccount) {
        while (true) {
            Boolean fromAccountLocked = fromAccount.getLock().tryLock();
            Boolean toAccountLocked = toAccount.getLock().tryLock();
            if (fromAccountLocked && toAccountLocked) {
                return;
            }
            if (fromAccountLocked) {
                fromAccount.getLock().unlock();
            }
            if (toAccountLocked) {
                toAccount.getLock().unlock();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void unLock(Account fromAccount, Account toAccount) {
        fromAccount.getLock().unlock();
        toAccount.getLock().unlock();
    }
}