package kz.iitu.diploma.filter;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@SuppressWarnings({"FieldCanBeLocal", "PointlessArithmeticExpression"})
public class IpDDoSChecker {

  private final long CHECK_NANOS_PERIOD = 1000L * 1000000L * 10L;//1 second
  private final long KILL_NANOS = 1000L * 1000000L * 600L;//10 minute
  private final int CHECK_PERIOD_LIMIT = 100;

  private final AtomicLong lastCleanOld = new AtomicLong(System.nanoTime());


  private static class IpRecord {

    public final Object sync = new Object();

    public final AtomicLong lastCheck = new AtomicLong(System.nanoTime());

    public final AtomicLong checkCount = new AtomicLong();

    public IpRecord(String ip) {}

  }

  private final ConcurrentHashMap<String, IpRecord> ipChecking = new ConcurrentHashMap<>();

  public boolean checkFastIp(String ip) {
    IpRecord ipRecord = ipChecking.computeIfAbsent(ip, IpRecord::new);
    long current = System.nanoTime();

    if(ipRecord.checkCount.get()>= CHECK_PERIOD_LIMIT){
      ipRecord.lastCheck.set(current);
      cleanOld();
      return true;
    }

    if (current - ipRecord.lastCheck.get() >= CHECK_NANOS_PERIOD) {
      ipRecord.lastCheck.set(current);
      ipRecord.checkCount.set(1);
      cleanOld();
      return false;
    }

    long checkedCount = ipRecord.checkCount.incrementAndGet();
    cleanOld();
    return checkedCount >= CHECK_PERIOD_LIMIT;
  }


  private void cleanOld() {
    long current = System.nanoTime();
    {
      if (current - lastCleanOld.get() < KILL_NANOS) {
        return;
      }
      lastCleanOld.set(current);
    }
    {
      Set<String> ipToRemove = new HashSet<>();
      for (Entry<String, IpRecord> e : ipChecking.entrySet()) {
        if (e.getValue().lastCheck.get() - current >= KILL_NANOS) {
          ipToRemove.add(e.getKey());
        }
      }
      for (String ip : ipToRemove) {
        ipChecking.remove(ip);
      }
    }
  }


}

