import net.akoot.plugins.ultravanilla.util.StringUtil;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class StringTests {

    @Test
    public void testArgs() {
        String[] expected = {
                "hello", "george w. heinback", "my", "name's", "james \"the king\" howard", "aka", "your \"momma\""
        };
        String input = "hello 'george w. heinback' my name's 'james \"the king\" howard' aka \"your \\\"momma\\\"\"";
        assertArrayEquals(expected, StringUtil.toArgs(input));
    }
}
