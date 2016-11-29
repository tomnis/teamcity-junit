# teamcity-junit
We have a framework that performs monitoring of some dynamically generated JUnit tests. One component of this is a custom JUnit runner
that notifies JUnit about test status. Certain interleavings of test notification events, such as the following, are incorrectly
interpreted by TeamCity:

- begin test A
- begin test B
- end test A
- begin test C
- end test B
- end test C (C threw an exception)

Included here is a small example that simulates this interleaving. See `Runner.scala` and `ExampleTest.scala`.

When run from IntelliJ, this is correctly interpreted as 3 distinct tests with 1 failure:
![intellij](screenshots/intellij.png)

Gradle also correctly interprets this as 3 distinct tests with 1 failure:
![gradle](screenshots/gradle.png)

However, TeamCity interprets this as 2 successful executions of A, and 1 successful execution of B:
![teamcity](screenshots/teamcity.png)
