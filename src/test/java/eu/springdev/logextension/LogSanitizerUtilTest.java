package eu.springdev.logextension;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class LogSanitizerUtilTest {

	@Test
	public void testClean_returnsParameterForBlankInput() throws Exception {
		assertThat(LogSanitizerUtil.clean(null)).isNull();
		assertThat(LogSanitizerUtil.clean("")).isEqualTo("");
		assertThat(LogSanitizerUtil.clean("  ")).isEqualTo("  ");
	}

	@Test
	public void testClean_escapesWhiteSpace() throws Exception {
		assertThat(LogSanitizerUtil.clean("test\n some text")).isEqualTo("test_ some text");
		assertThat(LogSanitizerUtil.clean("test\r some text")).isEqualTo("test_ some text");
		assertThat(LogSanitizerUtil.clean("test\t some text")).isEqualTo("test_ some text");
		assertThat(LogSanitizerUtil.clean("test\" some text")).isEqualTo("test_ some text");
	}

	@Test
	public void testClean_keepsFormatterParameters() throws Exception {
		assertThat(LogSanitizerUtil.clean("parameter\n of method {} {}")).isEqualTo("parameter_ of method {} {}");
	}
}
