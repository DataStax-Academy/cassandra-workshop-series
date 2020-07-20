package com.datastax.demo.test;

/*
@RunWith(JUnitPlatform.class)
@ExtendWith(SpringExtension.class)
@TestPropertySource(locations="/config-test.properties")
public class Driver4ConnectivityTest {

    @Test
    @DisplayName("Read default application.conf file")
    public void testConnectDse() {
        //CqlSession.builder().withConfigLoader(configLoader)
        //DriverExecutionProfile defaultProfile = config.getDefaultProfile();
        //DriverExecutionProfile olapProfile = config.getProfile("olap");

        try (CqlSession session = CqlSession.builder().build()) {

          DriverConfig config = session.getContext().config();
          DriverExecutionProfile defaultProfile = config.getDefaultProfile();
          System.out.println(defaultProfile.getStringList(DefaultDriverOption.CONTACT_POINTS));

          ResultSet rs = session.execute("select release_version from system.local");
          Row row = rs.one();
          System.out.println(row.getString("release_version"));

          //DriverExecutionProfile olapProfile = config.getProfile("olap");
          //System.out.println(olapProfile.getString(DefaultDriverOption.REQUEST_TIMEOUT));
        }

    }

    @Test
    public void testAsyncDse() {
        CompletionStage<CqlSession> sessionStage = CqlSession.builder().buildAsync();
        sessionStage.thenAccept(session -> System.out.println(Thread.currentThread().getName()));
        CompletionStage<AsyncResultSet> resultStage =
                sessionStage.executeAsync("SELECT release_version FROM system.local");
        resultStage.thenAccept(resultSet -> System.out.println(Thread.currentThread().getName()));
    }

    import com.datastax.driver.core.Cluster;
    import com.datastax.driver.core.ResultSet;
    import com.datastax.driver.core.ResultSetFuture;
    import com.datastax.driver.core.Row;
    import com.datastax.driver.core.Session;
    import com.google.common.util.concurrent.FutureCallback;
    import com.google.common.util.concurrent.Futures;
    import com.google.common.util.concurrent.ListenableFuture;
    import java.util.UUID;
    import reactor.core.publisher.Flux;
    import reactor.core.publisher.FluxSink;
    import reactor.core.publisher.FluxSink.OverflowStrategy;

    public class FluxExamples {

      public static void main(String[] args) {
        try (Cluster cluster = Cluster.builder().addContactPoint("127.0.1.1").build();
             Session session = cluster.connect()
        ) {
          session.execute(
              "CREATE KEYSPACE IF NOT EXISTS test WITH replication = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 }");
          session.execute("CREATE TABLE IF NOT EXISTS test.users (id uuid PRIMARY KEY, name text)");
          session.execute("TRUNCATE test.users");
          for (int i = 0; i < 10; i++) {
            session.execute("INSERT INTO test.users (id, name) VALUES (?, ?)",
                UUID.randomUUID(),
                "user" + i);
          }
          synchronousExample(session);
          asynchronousExample(session);
        }
      }

      private static void synchronousExample(Session session) {
        System.out.println("------------ Synchronous Example -----------------");
        ResultSet rs = session.execute("SELECT * FROM test.users");
        Long count = Flux.fromIterable(rs)
            .map(User::new)
            .doOnNext(System.out::println)
            .count().block();
        System.out.printf("Found %d users total%n", count);
        System.out.println();
      }

      private static void asynchronousExample(Session session) {
        System.out.println("------------ Asynchronous Example -----------------");
        Long count = Flux.<Row>create(sink -> {
          ResultSetFuture future = session.executeAsync("SELECT * FROM test.users");
          consumeAndFetchNext(sink, future);
        }, OverflowStrategy.BUFFER) // ATTENTION can OOM if subscriber not fast enough
            .map(User::new)
            .doOnNext(System.out::println)
            .count().block();
        System.out.printf("Found %d users total%n", count);
        System.out.println();
      }

      private static void consumeAndFetchNext(FluxSink<Row> sink, ListenableFuture<ResultSet> future) {

        Futures.addCallback(future, new FutureCallback<ResultSet>() {

          @Override
          public void onSuccess(ResultSet rs) {
            // How far we can go without triggering the blocking fetch:
            int remainingInPage = rs.getAvailableWithoutFetching();
            for (int i = 0; i < remainingInPage; i++) {
              Row row = rs.one();
              sink.next(row);
            }
            boolean wasLastPage = rs.getExecutionInfo().getPagingState() == null;
            if (wasLastPage) {
              sink.complete();
            } else {
              ListenableFuture<ResultSet> future = rs.fetchMoreResults();
              consumeAndFetchNext(sink, future);
            }
          }

          @Override
          public void onFailure(Throwable t) {
            sink.error(t);
          }
        });
      }


      public static class User {

        private final UUID id;
        private final String name;

        public User(Row row) {
          id = row.getUUID("id");
          name = row.getString("name");
        }

        UUID getId() {
          return id;
        }

        String getName() {
          return name;
        }

        @Override
        public String toString() {
          return "User{" +
              "id=" + id +
              ", name='" + name + '\'' +
              '}';
        }
      }
    }

}*/
