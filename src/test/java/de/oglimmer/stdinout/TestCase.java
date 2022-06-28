package de.oglimmer.stdinout;

import org.junit.jupiter.api.Assertions;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestCase {

    class TestStep {
        List<String> inputs;
        List<String> expectedOutputs;

        TestStep(List<String> inputs) {
            this.inputs = inputs;
        }
    }

    class TestInputStream extends InputStream {

        @Override
        public int read() {
            throw new RuntimeException("not implemented");
        }

        @Override
        public int read(byte b[], int off, int len) {
            if (expectedQueueType != QueueType.INPUT) {
                Assertions.fail(String.format("data was read but write was expected. mainCounter: %d, readSubCounter: %d, writeSubCounter: %d",
                        mainCounter, readSubCounter, writeSubCounter));
            }
            List<String> inputs = testSteps.get(mainCounter).inputs;
            String inputString = inputs.get(readSubCounter) + "\n";
            readSubCounter++;
            // when all input blocks are found, switch to expect blocks
            if (readSubCounter == inputs.size()) {
                expectedQueueType = QueueType.OUTPUT;
            }
            ByteArrayInputStream bais = new ByteArrayInputStream(inputString.getBytes());
            return bais.read(b, off, len);
        }
    }

    class TestOutputStream extends OutputStream {

        private String buffer = "";

        @Override
        public void write(int b) {
            throw new RuntimeException("not implemented");
        }

        @Override
        public void write(byte[] b, int off, int len) {
            if (expectedQueueType != QueueType.OUTPUT) {
                Assertions.fail(String.format("data was written but read was expected. mainCounter: %d, readSubCounter: %d, writeSubCounter: %d",
                        mainCounter, readSubCounter, writeSubCounter));
            }
            buffer += new String(b, 0, len);
            if (buffer.contains("\n")) {
                // remove string to test from buffer (0...\n)
                int posNewline = buffer.indexOf("\n");
                String stringToTest = buffer.substring(0, posNewline);
                if (posNewline < buffer.length() - 1) {
                    buffer = buffer.substring(posNewline + 1);
                } else {
                    buffer = "";
                }
                // check string against expected result
                String expectedOutput = testSteps.get(mainCounter).expectedOutputs.get(writeSubCounter);
                if (!stringToTest.equals(expectedOutput)) {
                    Assertions.fail(String.format("Failed to validate '%s' because '%s' was received. mainCounter: %d, writeSubCounter: %d",
                            expectedOutput, stringToTest, mainCounter, writeSubCounter));
                }
                writeSubCounter++;
                // when all expected blocks are found, switch to input
                if (writeSubCounter == testSteps.get(mainCounter).expectedOutputs.size()) {
                    expectedQueueType = QueueType.INPUT;
                    writeSubCounter = 0;
                    readSubCounter = 0;
                    mainCounter++;
                }
            }
        }
    }

    enum QueueType {
        INPUT, OUTPUT
    }

    private int mainCounter;
    private int writeSubCounter;
    private int readSubCounter;

    private QueueType expectedQueueType = QueueType.INPUT;

    private List<TestStep> testSteps = new ArrayList<>();

    private InputStream inSaved;
    private PrintStream outSaved;


    public static TestCase build() {
        return new TestCase();
    }

    public TestCase input(String... input) {
        testSteps.add(new TestStep(Arrays.asList(input)));
        return this;
    }

    public TestCase expect(String... expectedOutput) {
        testSteps.get(testSteps.size() - 1).expectedOutputs = Arrays.asList(expectedOutput);
        return this;
    }

    public TestCase setup() {
        saveDefaultStdInOut();

        System.setIn(new TestInputStream());
        System.setOut(new PrintStream(new TestOutputStream()));

        return this;
    }


    public void completeTesting() {
        restoreDefaultStdInOut();

        System.out.println("Tests successfully completed.");
    }

    private void saveDefaultStdInOut() {
        inSaved = System.in;
        outSaved = System.out;
    }

    private void restoreDefaultStdInOut() {
        System.setIn(inSaved);
        System.setOut(outSaved);
    }
}


