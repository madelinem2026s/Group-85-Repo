
package robotassembly;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

public class RobotAssemblyMainTest
{

    @Test
    public void testMainHandlesEndOfInput()
    {
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;

        try
        {
            System.setIn(new ByteArrayInputStream(new byte[0]));

            ByteArrayOutputStream output = new ByteArrayOutputStream();

            System.setOut(new PrintStream(output));

            assertDoesNotThrow(() -> RobotAssemblyMain.main(new String[0]));

            String text =
                new String(output.toByteArray(), StandardCharsets.UTF_8);

            assertFalse(text.isEmpty());

        }
        finally
        {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
    }


    @Test
    public void testMainHandlesInvalidDifficultyThenEndOfInput()
    {
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;

        try
        {
            System.setIn(
                new ByteArrayInputStream(
                    "invalid\n".getBytes(StandardCharsets.UTF_8)));

            ByteArrayOutputStream output = new ByteArrayOutputStream();

            System.setOut(new PrintStream(output));

            assertDoesNotThrow(() -> RobotAssemblyMain.main(new String[0]));

            String text =
                new String(output.toByteArray(), StandardCharsets.UTF_8);

            assertFalse(text.isEmpty());

        }
        finally
        {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
    }
}
