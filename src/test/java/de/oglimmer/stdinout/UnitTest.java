package de.oglimmer.stdinout;

import org.junit.jupiter.api.Test;

public class UnitTest {

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
}
