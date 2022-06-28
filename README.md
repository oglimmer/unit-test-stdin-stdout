# Unit testing a stdin / stdout based program

The class FakeApp implements a CLI program using stdin and stdout for all it's interaction with the user.

This is unit-tested via the code:

```java
@Test
public void lineByLineTests() {

    TestCase testCase = TestCase.build()
            .input("add-user oli glimmer").expect("user added")
            .input("add-user heiner test").expect("user added")
            .input("list-users").expect("user:", "1,oli,glimmer", "2,heiner,test")
            .input("del-user 1").expect("user deleted")
            .input("list-users").expect("user:", "2,heiner,test")
            .input("quit")
            .setup();

    FakeApp.main(null);

    testCase.completeTesting();
}
```
(as seen in class UnitTest).

If you think this isn't unit testing and more component or even end-to-end testing - fair enough.
